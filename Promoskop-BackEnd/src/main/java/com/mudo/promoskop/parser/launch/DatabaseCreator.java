package com.mudo.promoskop.parser.launch;

import java.util.ArrayList;
import java.util.List;

import com.mudo.promoskop.parser.service.HibernateService;
import com.mudo.promoskop.parser.util.ExcelParser;
import com.mudo.promoskop.web.model.Product;

public class DatabaseCreator {

	public static void main(String[] args) {
		try {
			List<Product> productList = new ArrayList<Product>();
			productList.addAll(parseExcelFiles(1));
			productList.addAll(parseExcelFiles(2));
			productList.addAll(parseExcelFiles(3));
			productList.addAll(parseExcelFiles(4));

			HibernateService.saveProducts(productList);
			HibernateService.session.getTransaction().commit();
			HibernateService.session.close();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<Product> parseExcelFiles(int index) {
		ExcelParser parser = new ExcelParser();
		parser.readDatasFromExcelFile("LIST_" + index + ".xlsx");

		return parser.getProductList();
	}
}
