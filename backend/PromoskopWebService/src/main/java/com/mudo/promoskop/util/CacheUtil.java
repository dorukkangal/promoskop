package com.mudo.promoskop.util;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheUtil {
	private static Logger LOG = LoggerFactory.getLogger("cache");

	public static void displayStatistics(EntityManager em) {
		try {
			SessionFactory sf = ((HibernateEntityManagerFactory) em.getEntityManagerFactory()).getSessionFactory();
			Statistics statistics = sf.getStatistics();
			LOG.debug("\n" + statistics.toString().replaceAll(",", ",\n   "));
			System.out.println("\n" + statistics.toString().replaceAll(",", ",\n   "));
			// statistics.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void isCached(EntityManager em, Class<?> clazz, Object id) {
		LOG.debug("class: {} id: {} cached: {}", clazz.getSimpleName(), id, em.getEntityManagerFactory().getCache().contains(clazz, id));
	}
}
