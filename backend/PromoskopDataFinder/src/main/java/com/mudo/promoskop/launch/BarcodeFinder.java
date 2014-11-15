package com.mudo.promoskop.launch;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BarcodeFinder {

	private static final String BARCODE_SITE = "http://shop.netbilgi.com.tr/?p=productsList&iPage=";
	private static final int START_PAGE_INDEX = 401;
	private static final int STOP_PAGE_INDEX = 401;

	private static final String OUT_FILE_NAME = "barkode/netbilgi.csv";

	private static PrintWriter writer;

	public static void main(String[] args) {

		try {
			openOutFile();
			for (int i = START_PAGE_INDEX; i <= STOP_PAGE_INDEX; i++) {
				writeProductsToFile(getProductsFromHtmlPage(i));
				delay();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeOutFile();
		}
	}

	private static List<Product> getProductsFromHtmlPage(int pageIndex) {
		List<Product> pageProducts = new ArrayList<Product>();
		try {
			Document doc = getHtmlPageContent(pageIndex);
			Elements productWrappers = getProductEntries(doc, pageIndex);
			for (int elementIndex = 0; elementIndex < productWrappers.size(); elementIndex++)
				pageProducts.add(getProduct(productWrappers.get(elementIndex), pageIndex, elementIndex));
		} catch (IOException e) {
			System.out.println(pageIndex);
			e.printStackTrace();
		}
		return pageProducts;
	}

	private static Product getProduct(Element productWrapper, int pageIndex, int elementIndexPerPage) {
		String barcode = getBarcodeFromTag(productWrapper);
		String productName = getProductName(productWrapper);
		if (barcode.length() <= 0 || productName.length() <= 0)
			System.out.println(pageIndex + ". sayfa urun bilgileri hatali olabilir: " + elementIndexPerPage);
		return new Product(barcode, productName);
	}

	private static String getBarcodeFromTag(Element productWrapper) {
		return productWrapper.getElementsByTag("h2").select("a").text();
	}

	private static String getProductName(Element productWrapper) {
		return productWrapper.getElementsByTag("h4").text();
	}

	private static Elements getProductEntries(Document doc, int pageIndex) throws IOException {
		Elements productWrappers = doc.getElementsByClass("entry");
		if (productWrappers.size() != 100)
			System.out.println(pageIndex + ". sayfada 100'den farkli urun bulundu. urun sayisi: " + productWrappers.size());
		return productWrappers;
	}

	private static Document getHtmlPageContent(int pageIndex) throws IOException {
		System.out.println(pageIndex + ". sayfaya baglaniliyor...");
		return Jsoup.connect(BARCODE_SITE + pageIndex).timeout(0).get();
	}

	private static void writeProductsToFile(List<Product> products) {
		for (Product temp : products)
			writer.println(temp.toString());
		writer.flush();
	}

	private static PrintWriter openOutFile() throws IOException {
		return writer = new PrintWriter(OUT_FILE_NAME, "UTF-8");
	}

	private static void closeOutFile() {
		writer.close();
	}

	private static void delay() {
		try {
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static class Product {
		String barcode;
		String productName;

		public Product(String barcode, String productName) {
			this.barcode = barcode;
			this.productName = productName;
		}

		@Override
		public String toString() {
			return barcode.concat(";").concat(productName).concat(";");
		}
	}
}
