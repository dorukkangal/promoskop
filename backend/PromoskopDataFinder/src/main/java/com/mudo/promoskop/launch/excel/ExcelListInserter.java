package com.mudo.promoskop.launch.excel;

import java.util.ArrayList;
import java.util.List;

import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.service.HibernateService;
import com.mudo.promoskop.util.ExcelParser;

public class ExcelListInserter {

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
