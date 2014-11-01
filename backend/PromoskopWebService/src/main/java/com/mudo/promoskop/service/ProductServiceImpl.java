package com.mudo.promoskop.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.model.Product;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public Product findById(int id) {
		try {
			return productDao.findById(id);
		} catch (NoResultException e) {
			// TODO 404
			return null;
		}
	}

	@Override
	public List<Product> findBySubString(String containText) {
		try {
			return productDao.findBySubString(containText);
		} catch (NoResultException e) {
			// TODO 404
			return null;
		}
	}
}
