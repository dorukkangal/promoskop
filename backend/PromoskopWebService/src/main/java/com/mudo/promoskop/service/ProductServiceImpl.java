package com.mudo.promoskop.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mudo.promoskop.model.Product;

@Service
public class ProductServiceImpl implements ProductService {
	protected static Logger LOG = LoggerFactory.getLogger(ProductService.class);

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager em;

	@Override
	public Product findById(int id) {
		try {
			LOG.debug("find by id " + id + "from product");
			return em.find(Product.class, id);
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<Product> findBySubString(String containText) {
		containText = "%".concat(containText).concat("%");
		LOG.debug("find by substring: ".concat(containText).concat("from Product"));

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
		Root<Product> root = criteria.from(Product.class);
		criteria.where(builder.like(root.get("name").as(String.class), containText));
		List<Product> results = em.createQuery(criteria).getResultList();
		return results;
	}
}
