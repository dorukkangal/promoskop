package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.response.ProductResponse;

public interface ProductResponseService {

	public ProductResponse findByBarcode(String barcode);

	public List<ProductResponse> findBySubString(String containText);

	public List<ProductResponse> findMaxGapped(int count);
}
