package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.model.Product;

public interface ProductService {

	public Product find(int id);

	public List<Product> findBySubString(String containText);
}
