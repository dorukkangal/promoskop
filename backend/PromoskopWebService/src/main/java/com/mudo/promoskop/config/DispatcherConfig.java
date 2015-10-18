package com.mudo.promoskop.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.mudo.promoskop.jackson.BeanPropertyJacksonFilter;
import com.mudo.promoskop.jackson.JacksonFilterInterceptor;
import com.mudo.promoskop.jackson.JacksonFilterIntrospector;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.mudo.promoskop" })
public class DispatcherConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJacksonHttpMessageConverter());
		super.configureMessageConverters(converters);
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper());
		return converter;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setFilters(new SimpleFilterProvider().addFilter("pathFilter", jacksonFilter()));
		mapper.setAnnotationIntrospector(new JacksonFilterIntrospector("pathFilter"));
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		return mapper;
	}

	@Bean
	public BeanPropertyJacksonFilter jacksonFilter() {
		return new BeanPropertyJacksonFilter();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jacksonFilterContextInterceptor());
	}

	@Bean
	public JacksonFilterInterceptor jacksonFilterContextInterceptor() {
		return new JacksonFilterInterceptor();
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
}
