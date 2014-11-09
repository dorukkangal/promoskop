package com.mudo.promoskop.service;

import org.springframework.http.ResponseEntity;

import com.mudo.promoskop.util.JsonFilter;

public interface JsonService {

	public ResponseEntity<String> generateJsonForProducts(JsonFilter filter, String containText);

	public ResponseEntity<String> generateJsonForProduct(JsonFilter filter, int id);

	public String generateJsonForException(Exception ex);

	public ResponseEntity<String> generateJsonForBasket(JsonFilter filter, double currentLatitude, double currentLongitude, double maxDistance, int[] barcodeIds);
}
