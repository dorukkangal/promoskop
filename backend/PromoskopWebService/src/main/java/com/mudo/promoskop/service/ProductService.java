package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.model.Product;

public interface ProductService {

	public Product findByBarcode(String barcode);

	public List<Product> findBySubString(String containText);

	public List<Product> findMaxGapped(int count);
}
