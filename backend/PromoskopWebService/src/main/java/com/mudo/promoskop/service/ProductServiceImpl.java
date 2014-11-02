package com.mudo.promoskop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.util.exception.ResourceNotFoundException;

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
		List<Product> l = productDao.findBySubString(containText);
		if (l.isEmpty())
			throw new ResourceNotFoundException();
		return l;
	}
}
