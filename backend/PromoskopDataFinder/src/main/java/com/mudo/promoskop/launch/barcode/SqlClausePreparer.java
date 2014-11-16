package com.mudo.promoskop.launch.barcode;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.Charsets;

public class SqlClausePreparer {

	private static final String IN_FILE_NAME = "barcode/migros.csv";
	private static final String OUT_FILE_NAME = "sql/promoskop-data.sql";

	private static List<String> clausesForOnStock = new ArrayList<String>();
	private static List<String> clausesForOffStock = new ArrayList<String>();

	private static PrintWriter writer;

	public static void main(String[] args) throws IOException {
		openOutFile();
		for (String line : readAllFileLines())
			prepareSqlClause(line);

		writeToOutFile("-- On Stock--------------------------------------------------------------\n");
		for (String clause : clausesForOnStock)
			writeToOutFile(clause);

		writeToOutFile("\n\n-- Off Stock--------------------------------------------------------------");
		for (String clause : clausesForOffStock)
			writeToOutFile(clause);
		closeOutFile();
	}

	private static void prepareSqlClause(String line) {
		String[] splits = line.split(";");
		String barcode = splits[0];
		String productName = splits[2].replaceAll("'", "''");
		String price = splits[6];
		String url = splits[11];

		String clause1 = "INSERT INTO product (barcode, name, url) VALUES('" + barcode + "', '" + productName + "', '" + url + "');";
		if (splits[9].equals("true")) {
			String clause2 = "\nINSERT INTO product_branch(product_id,branch_id,price) VALUES ((SELECT id FROM product WHERE barcode='" + barcode + "'), 1, " + price + ");";
			clausesForOnStock.add(clause1.concat(clause2));
		} else if (splits[9].equals("false")) {
			clausesForOffStock.add(clause1);
		}
	}

	private static List<String> readAllFileLines() {
		try {
			return Files.readAllLines(new File(IN_FILE_NAME).toPath(), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private static void writeToOutFile(String sqlClause) {
		writer.println(sqlClause);
		writer.flush();
	}

	private static PrintWriter openOutFile() throws IOException {
		return writer = new PrintWriter(OUT_FILE_NAME, Charsets.UTF_8.name());
	}

	private static void closeOutFile() {
		writer.close();
	}
}
