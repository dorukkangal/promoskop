package com.mudo.promoskop.web.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.mudo.promoskop.web.service.ProductService;

public class AppContext implements ApplicationContextAware {

	private static ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		AppContext.ctx = ctx;
	}

	public static Object getBean(String beanName) {
		return ctx.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		return ctx.getBean(beanName, clazz);
	}

	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}

	public static ProductService getProductService() {
		return getBean("productService", ProductService.class);
	}
}