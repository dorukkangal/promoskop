package com.mudo.promoskop.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class ApplicationBeanAware implements BeanFactoryAware {

	private static BeanFactory beanFactory;

	public static Object getBean(String name) {
		return beanFactory.getBean(name);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		return beanFactory.getBean(beanName, clazz);
	}

	public static <T> T getBean(Class<T> clazz) {
		return beanFactory.getBean(clazz);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ApplicationBeanAware.beanFactory = beanFactory;
	}
}