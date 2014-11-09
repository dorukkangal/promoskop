package com.mudo.promoskop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.exception.ResourceNotFoundException;
import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public Product findById(int id) {
		Product p = productDao.findById(id);
		if (p == null)
			throw new ResourceNotFoundException();
		return p;
	}

	@Override
	public List<Product> findBySubString(String containText) {
		return productDao.findBySubString(containText);
	}

	@Override
	public List<Product> findMaxQueried(int count) {
		return productDao.findMaxQueried(count);
	}
}
