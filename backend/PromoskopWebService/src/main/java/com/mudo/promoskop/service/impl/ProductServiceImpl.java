package com.mudo.promoskop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public Product findByBarcode(String barcode) {
		return productDao.findByBarcode(barcode);
	}

	@Override
	public List<Product> findBySubString(String containText) {
		return productDao.findBySubString(containText);
	}

	@Override
	public List<Product> findMaxGapped(int count) {
		return productDao.findMaxGapped(count);
	}
}
