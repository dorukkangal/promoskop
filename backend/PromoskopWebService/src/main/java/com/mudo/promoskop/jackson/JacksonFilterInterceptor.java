package com.mudo.promoskop.jackson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mudo.promoskop.config.ApplicationBeanAware;

public class JacksonFilterInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod method = (HandlerMethod) handler;
		JacksonFilter filter = method.getMethodAnnotation(JacksonFilter.class);
		if (filter != null) {
			ApplicationBeanAware.getBean(BeanPropertyJacksonFilter.class).updateContext(filter);
		} else {
			ApplicationBeanAware.getBean(BeanPropertyJacksonFilter.class).clearContext();
		}
		return super.preHandle(request, response, handler);
	}
}