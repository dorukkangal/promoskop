package com.mudo.promoskop.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.exception.ResourceNotFoundException;
import com.mudo.promoskop.model.Product;

@Repository
public class ProductDaoImpl implements ProductDao {
	private static Logger LOG = LoggerFactory.getLogger(ProductDao.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public Product findById(int id) {
		LOG.debug("find by id " + id + "from product");
		Product p = em.find(Product.class, id, LockModeType.PESSIMISTIC_WRITE);
		if (p == null)
			throw new ResourceNotFoundException();
		p.setQueryCount(p.getQueryCount() + 1);
		em.flush();
		return p;
	}

	@Override
	public List<Product> findBySubString(String containText) {
		containText = "%".concat(containText).concat("%");
		LOG.debug("find by substring: ".concat(containText).concat("from Product"));

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> from = query.from(Product.class);
		query.where(builder.like(from.get("name").as(String.class), containText));
		return em.createQuery(query).setHint(QueryHints.CACHEABLE, true).getResultList();
	}

	@Override
	public List<Product> findMaxQueried(int count) {
		LOG.debug("find max queried Product");

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> from = query.from(Product.class);
		query.orderBy(builder.desc(from.get("queryCount")));
		return em.createQuery(query).setHint(QueryHints.CACHEABLE, true).setMaxResults(count).getResultList();
	}
}
