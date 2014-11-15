package com.mudo.promoskop.launch.barcode;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.Charsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MigrosBarcodeQuerier {
	private static final String WEB_SERVICE = "https://www.sanalmarket.com.tr/kweb/getProductDeviceService.do";
	private static final String WEB_SERVICE_PARAM = "<customer><districtId>34000022020</districtId><barcode>barcode_num</barcode><udid>mgr_3D1A8926-2432-41F7-ABB2-55818AB1E8CD</udid><email></email><hashCode></hashCode><device>iPhone</device></customer>";

	private static final String IN_FILE_NAME = "barcode/netbilgi.csv";
	private static final String OUT_FILE_NAME = "barcode/migros.csv";

	private static PrintWriter writer;

	private static Set<String> allBarcodeNumbers = new HashSet<String>();

	private static int queryCount = 0;

	public static void main(String[] args) {

		try {
			openOutFile();
			writeHeader();
			allBarcodeNumbers = readAllBarcodeNumbers();
			for (String barcodeNumber : allBarcodeNumbers) {
				Document doc = queryBarcodeFromWebservice(barcodeNumber);
				if (isResultSuccess(doc))
					writeResponseToFile(getResponse(doc));
				else
					System.out.println(barcodeNumber + " numarasi ile urun bulunamadi.");
				delay(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeOutFile();
		}
	}

	private static Response getResponse(Document doc) {
		Response response = new Response();
		response.barcode = doc.getElementsByTag("barcode").text();
		response.productid = doc.getElementsByTag("productid").text();
		response.name = doc.getElementsByTag("correctedname").text();
		response.storeId = doc.getElementsByTag("storeid").text();
		response.mccprice = doc.getElementsByTag("mccprice").text();
		response.mccpricesymbol = doc.getElementsByTag("mccpricesymbol").text();
		response.pricewithextravalue = doc.getElementsByTag("pricewithextravalue").text();
		response.pricewithextravaluesymbol = doc.getElementsByTag("pricewithextravaluesymbol").text();
		response.onsale = doc.getElementsByTag("onsale").text();
		response.onstock = doc.getElementsByTag("onstock").text();
		response.stockamount = doc.getElementsByTag("stockamount").text();
		response.mediumimageurl = doc.getElementsByTag("mediumimageurl").text();
		response.unitformat = doc.getElementsByTag("unitformat").text();
		response.hasmigroskop = doc.getElementsByTag("hasmigroskop").text();
		response.hasdiscount = doc.getElementsByTag("hasdiscount").text();
		response.company = doc.getElementsByTag("company").text();
		return response;
	}

	private static boolean isResultSuccess(Document doc) {
		if (doc == null)
			return false;
		return doc.getElementsByTag("errorcode").text().equals("1");
	}

	private static Document queryBarcodeFromWebservice(String barcodeNumber) {
		try {
			queryCount++;
			System.out.println(queryCount + ") " + barcodeNumber + " numarasi sorgulaniyor...");
			return Jsoup.connect(WEB_SERVICE).header("Content-Type", "application/x-www-form-urlencoded")
					.data("xmlString", WEB_SERVICE_PARAM.replace("barcode_num", barcodeNumber)).userAgent("Mozilla/5.0").timeout(0).post();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Set<String> readAllBarcodeNumbers() {
		List<String> allLines = readAllFileLines();
		for (String line : allLines)
			allBarcodeNumbers.add(getBarcodeNumber(line));
		return allBarcodeNumbers;
	}

	private static String getBarcodeNumber(String line) {
		return line.trim().split(";")[0];
	}

	private static List<String> readAllFileLines() {
		try {
			return Files.readAllLines(new File(IN_FILE_NAME).toPath(), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private static void writeResponseToFile(Response response) {
		writer.println(response.toString());
		writer.flush();
	}

	private static void writeHeader() {
		writer.println("barcode;productid;name;storeId;mccprice;mccpricesymbol;pricewithextravalue;pricewithextravaluesymbol;onsale;onstock;stockamount;mediumimageurl;unitformat;hasmigroskop;hasdiscount;company;");
		writer.flush();
	}

	private static PrintWriter openOutFile() throws IOException {
		return writer = new PrintWriter(OUT_FILE_NAME, "UTF-8");
	}

	private static void closeOutFile() {
		writer.close();
	}

	private static void delay(int time) {
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static class Response {
		public String barcode;
		public String productid;
		public String name;
		public String storeId;
		public String mccprice;
		public String mccpricesymbol;
		public String pricewithextravalue;
		public String pricewithextravaluesymbol;
		public String onsale;
		public String onstock;
		public String stockamount;
		public String mediumimageurl;
		public String unitformat;
		public String hasmigroskop;
		public String hasdiscount;
		public String company;

		@Override
		public String toString() {
			return barcode.concat(";").concat(productid).concat(";").concat(name).concat(";").concat(storeId).concat(";").concat(mccprice).concat(";")
					.concat(mccpricesymbol).concat(";").concat(pricewithextravalue).concat(";").concat(pricewithextravaluesymbol).concat(";").concat(onsale).concat(";")
					.concat(onstock).concat(";").concat(stockamount).concat(";").concat(mediumimageurl).concat(";").concat(unitformat).concat(";").concat(hasmigroskop)
					.concat(";").concat(hasdiscount).concat(";").concat(company).concat(";");
		}
	}
}
