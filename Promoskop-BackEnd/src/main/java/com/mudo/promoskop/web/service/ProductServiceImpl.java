package com.mudo.promoskop.web.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.mudo.promoskop.web.model.Product;

@Service
public class ProductServiceImpl implements ProductService {

	protected static Log LOG = LogFactory.getLog(ProductService.class);

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager em;

	public Product find(int id) {
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
}
