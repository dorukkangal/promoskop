package com.mudo.promoskop.service;

import com.mudo.promoskop.util.JsonFilter;

public interface JsonService {

	public String generateJsonForProducts(JsonFilter filter, String containText) throws Exception;

	public String generateJsonForProduct(JsonFilter filter, int id) throws Exception;

	public String generateJsonForBasket(JsonFilter filter, double currentLatitude, double currentLongitude, double maxDistance, int[] barcodeIds) throws Exception;

	public String generateJsonForPopularProducts(JsonFilter filter, int count) throws Exception;
}
