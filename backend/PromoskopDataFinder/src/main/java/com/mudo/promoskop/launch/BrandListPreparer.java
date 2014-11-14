package com.mudo.promoskop.launch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.Charsets;

public class BrandListPreparer {

	private static final String JSON_FILE_NAME = "migros_all.json";

	private static final String OUT_FILE_NAME = "marka-listesi.csv";

	private static final String BRAND_TAG = "\"nm\": ";

	private static List<String> allProductLines = new ArrayList<String>();

	private static Map<String, String> brandProductMap = new TreeMap<String, String>();

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		brandProductMap = getAllBrandsFromJson();
		writeAllBrandListToFile(brandProductMap);
	}

	private static void writeAllBrandListToFile(Map<String, String> brandProductMap) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(OUT_FILE_NAME, Charsets.UTF_8.name());
		for (Entry<String, String> brandProduct : brandProductMap.entrySet()) {
			String brand = brandProduct.getKey();
			String product = brandProduct.getValue();
			Integer repeatCount = getBrandRepeatCount(brand);
			writer.println(brand.concat(";").concat(product).concat(";").concat(repeatCount.toString()).concat(";"));
		}
		writer.close();
	}

	private static Map<String, String> getAllBrandsFromJson() {
		allProductLines = getAllProductLines();
		for (String product : allProductLines) {
			String brand = getBrand(product);
			brandProductMap.put(brand, product);
		}
		return brandProductMap;
	}

	private static String getBrand(String product) {
		return product.split(" ", 2)[0];
	}

	private static Integer getBrandRepeatCount(String brand) {
		int repeatCount = 0;
		for (String line : allProductLines)
			if (line.startsWith(brand))
				repeatCount++;
		return repeatCount;
	}

	private static List<String> getAllProductLines() {
		List<String> allJsonLines = getAllFileLines();
		for (String line : allJsonLines)
			if (isBrandLine(line))
				allProductLines.add(formatProductLine(line));
		return allProductLines;
	}
	
	private static String formatProductLine(String line) {
		return line.trim().replace("\"nm\": \"", "").replace("\",", "");
	}

	private static List<String> getAllFileLines() {
		try {
			return Files.readAllLines(new File(JSON_FILE_NAME).toPath(), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private static boolean isBrandLine(String line) {
		return line.contains(BRAND_TAG);
	}
}
