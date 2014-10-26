package com.mudo.promoskop.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.util.HibernateUtil;

public class HibernateService {

	public static Session session = null;
	static {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		session = sf.openSession();
		session.beginTransaction();
	}

	public static void saveProducts(List<Product> productList) {
		for (Product product : productList)
			saveProduct(product);
	}

	public static void saveProduct(Product product) {
		session.save(product);
	}

	public static Product findProduct(int id) {
		return (Product) findById(Product.class, id);
	}

	@SuppressWarnings("unchecked")
	public static List<Product> findAllProducts() {
		return session.createQuery("from Product").list();
	}

	private static Object findById(Class<?> clazz, Number id) {
		return session.get(clazz, id);
	}
}
