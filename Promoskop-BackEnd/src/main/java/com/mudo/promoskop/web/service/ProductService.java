package com.mudo.promoskop.web.service;

import java.util.List;

import com.mudo.promoskop.web.model.Product;

public interface ProductService {

	public Product find(int id);

	public List<Product> findBySubString(String containText);
}
