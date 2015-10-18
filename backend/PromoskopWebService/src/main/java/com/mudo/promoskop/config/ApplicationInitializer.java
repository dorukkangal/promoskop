package com.mudo.promoskop.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.Log4jConfigListener;

public class ApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext context) {
		// Manage the lifecycle of the root application context
		registerContextLoaderListener(context);
		registerDispatcherServlet(context);
		registerOpenEntityManagerInViewFilter(context);
		registerLog4jConfigListener(context);
	}

	private void registerContextLoaderListener(ServletContext context) {
		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext rootContext = createContext(ApplicationConfig.class);
		context.addListener(new ContextLoaderListener(rootContext));
	}

	private void registerDispatcherServlet(ServletContext context) {
		// Create the dispatcher servlet's Spring application context
		AnnotationConfigWebApplicationContext dispatcherContext = createContext(DispatcherConfig.class);
		ServletRegistration.Dynamic dispatcher = context.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

	private void registerOpenEntityManagerInViewFilter(ServletContext context) {
		FilterRegistration.Dynamic registration = context.addFilter("openEntityManagerInView", OpenEntityManagerInViewFilter.class);
		registration.setInitParameter("entityManagerFactoryBeanName", "entityManagerFactory");
		registration.addMappingForUrlPatterns(null, true, "/*");
	}

	private void registerLog4jConfigListener(ServletContext context) {
		// listener for logging
		context.addListener(Log4jConfigListener.class);
	}

	private AnnotationConfigWebApplicationContext createContext(final Class<?>... annotatedClasses) {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(annotatedClasses);
		return context;
	}
}
