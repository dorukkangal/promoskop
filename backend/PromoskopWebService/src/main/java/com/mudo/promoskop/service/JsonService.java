package com.mudo.promoskop.service;

import org.springframework.http.ResponseEntity;

public interface JsonService {

	public final static String[] PRODUCT_BY_ID_FILTER = {};

	public final static String[] PRODUCT_BY_NAME_FILTER = { "branches" };

	public final static String[] EXCEPTION_FILTER = { "detail_message", "cause", "stack_trace", "suppressed_exceptions", "localized_message", "suppressed" };

	public ResponseEntity<String> generateJsonForProducts(String[] ignorableFieldNames, String containText);

	public ResponseEntity<String> generateJsonForProduct(String[] ignorableFieldNames, int id);

	public String generateJsonForException(Exception ex);
}
