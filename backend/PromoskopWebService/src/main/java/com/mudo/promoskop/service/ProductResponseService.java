package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.response.ProductResponse;

public interface ProductResponseService {

	public ProductResponse findById(int id);

	public List<ProductResponse> findBySubString(String containText);
}
