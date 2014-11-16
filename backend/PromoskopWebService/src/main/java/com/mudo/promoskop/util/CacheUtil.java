package com.mudo.promoskop.util;

import javax.persistence.EntityManager;

import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

public class CacheUtil {
	private static Logger LOG = LoggerFactory.getLogger("cache");

	public static void displayStatistics(EntityManager em) {
		EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
		EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl) emfi.getNativeEntityManagerFactory();
		Statistics statistics = empImpl.getSessionFactory().getStatistics();
		LOG.debug("\n" + statistics.toString().replaceAll(",", ",\n   "));
		statistics.clear();
	}

	public static void isCached(EntityManager em, Class<?> clazz, Object id) {
		LOG.debug("class: {} id: {} cached: {}", clazz.getSimpleName(), id, em.getEntityManagerFactory().getCache().contains(clazz, id));
	}
}
