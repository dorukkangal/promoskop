package com.mudo.promoskop.parser.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mudo.promoskop.parser.model.Product;

public class ExcelParser {

	private static XSSFWorkbook workbook = null;

	private List<Product> productList = new ArrayList<Product>();

	public void readDatasFromExcelFile(String excelFile) {
		if (loadExcel(excelFile) > 0) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			for (Iterator<Row> rowIterator = sheet.iterator(); rowIterator.hasNext();) {
				Row row = rowIterator.next();
				try {
					Product product = new Product();
					int productId = getProductId(row);
					product.setId(productId);

					String productName = getProductName(row);
					product.setName(productName);
					productList.add(product);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Integer getProductId(Row row) {
		try {
			Cell cell = row.getCell(0);
			return getNumericCellValue(cell);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getProductName(Row row) {
		try {
			Cell cell = row.getCell(1);
			return getStringCellValue(cell);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Integer getNumericCellValue(Cell cell) {
		try {
			if (isFilledCell(cell))
				return (int) cell.getNumericCellValue();
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getStringCellValue(Cell cell) {
		if (isFilledCell(cell)) {
			String cellValue = cell.getStringCellValue();
			return cellValue.replace("\n", "").replace("  ", " ").trim();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private String getCellValueOnRow(Row row, int cellIndex) {
		Cell cell = getCellOnRow(row, cellIndex);
		if (cell != null)
			return cell.toString();
		return null;
	}

	private Cell getCellOnRow(Row row, int cellIndex) {
		int i = 0;
		for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext(); ++i)
			if (i == cellIndex)
				return cellIterator.next();
		return null;
	}

	private int getCellCountOnRow(Row row) {
		int count = 0;
		for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext();) {
			Cell cell = cellIterator.next();
			if (cell.getStringCellValue() == null || cell.getStringCellValue().equals(""))
				return count;
			count++;
		}
		return count;
	}

	@SuppressWarnings("unused")
	private Object getObject(Set<?> set, String value) {
		for (Object entity : set)
			if (value.trim().equals(entity.toString().trim()))
				return entity;
		return null;
	}

	private boolean isFilledCell(Cell cell) {
		return cell != null && cell.toString() != null && !cell.toString().equals("");
	}

	@SuppressWarnings("unused")
	private boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*[.,]?\\d*");
	}

	@SuppressWarnings("unused")
	private boolean isCellCountBiggerThan(Row row, int n) {
		return n > getCellCountOnRow(row);
	}

	@SuppressWarnings("unused")
	private boolean isContains(List<?> list, String value) {
		for (Object entity : list)
			if (value.equals(entity.toString()))
				return true;
		return false;
	}

	public int loadExcel(String excelFile) {
		try {
			workbook = new XSSFWorkbook(new FileInputStream(getExcelFile(excelFile)));
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private File getExcelFile(String excelFile) {
		return new File("excel/" + excelFile);
	}

	public List<Product> getProductList() {
		return productList;
	}
}
