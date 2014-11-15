package com.mudo.promoskop.dao;

import java.util.List;

import com.mudo.promoskop.model.Product;

public interface ProductDao {

	public Product findByBarcode(String barcode);

	public List<Product> findBySubString(String containText);

	public List<Product> findMaxGapped(int count);
}
