package com.mudo.promoskop.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mudo.promoskop.model.Product;

@Repository
public class ProductDaoImpl implements ProductDao {
	private static Logger LOG = LoggerFactory.getLogger(ProductDao.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public Product findById(int id) {
		LOG.debug("find by id " + id + "from product");
		return em.find(Product.class, id);
	}

	@Override
	public List<Product> findBySubString(String containText) {
		containText = "%".concat(containText).concat("%");
		LOG.debug("find by substring: ".concat(containText).concat("from Product"));

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
		Root<Product> root = criteria.from(Product.class);
		criteria.where(builder.like(root.get("name").as(String.class), containText));
		List<Product> results = em.createQuery(criteria).setHint(QueryHints.CACHEABLE, true).getResultList();
		return results;
	}
}
