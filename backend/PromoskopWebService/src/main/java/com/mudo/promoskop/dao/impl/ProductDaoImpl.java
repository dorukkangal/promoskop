package com.mudo.promoskop.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.util.CacheUtil;

@Repository
public class ProductDaoImpl implements ProductDao {
	private static Logger LOG = LoggerFactory.getLogger(ProductDao.class);

	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	public Product findByBarcode(String barcode) {
		LOG.debug("find by barcode: {} from product", barcode);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> from = query.from(Product.class);
		query.where(builder.equal(from.get("barcode").as(String.class), barcode));
		Product p = em.createQuery(query).setHint(QueryHints.CACHEABLE, true).getSingleResult();
		updateQueryCount(p);
		CacheUtil.displayStatistics(em);
		return p;
	}

	@Override
	public List<Product> findBySubString(String containText) {
		containText = "%".concat(containText).concat("%");
		LOG.debug("find by substring: {} from Product", containText);

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> from = query.from(Product.class);
		query.where(builder.like(from.get("name").as(String.class), containText));
		CacheUtil.displayStatistics(em);
		return em.createQuery(query).setHint(QueryHints.CACHEABLE, true).getResultList();
	}

	@Override
	public List<Product> findMaxGapped(int count) {
		LOG.debug("find max queried Product");

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> from = query.from(Product.class);
		query.orderBy(builder.desc(from.get("queryCount")));
		CacheUtil.displayStatistics(em);
		return em.createQuery(query).setHint(QueryHints.CACHEABLE, true).setMaxResults(count).getResultList();
	}

	public void updateQueryCount(Product p) {
		p.setQueryCount(p.getQueryCount() + 1);
		update(p);
	}

	public void update(Product p) {
		em.merge(p);
	}
}
