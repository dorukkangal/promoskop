package com.mudo.promoskop.jackson;

import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

public class BeanPropertyJacksonFilter implements BeanPropertyFilter {

	private JacksonFilterContext context = new JacksonFilterContext();

	public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov, BeanPropertyWriter writer) throws Exception {
		String pathName = getPathName(bean);
		String propertyName = writer.getName();
		if (!context.ignore(pathName, propertyName))
			writer.serializeAsField(bean, jgen, prov);
	}

	public void updateContext(JacksonFilter filter) {
		context.includeProperty(filter.includeProperties());
		context.excludeProperty(filter.excludeProperties());
		context.includePath(filter.includePaths());
		context.excludePath(filter.excludePaths());
	}

	public void clearContext() {
		setContext(new JacksonFilterContext());
	}

	public JacksonFilterContext getContext() {
		return context;
	}

	public void setContext(JacksonFilterContext context) {
		this.context = context;
	}

	@Override
	public void depositSchemaProperty(BeanPropertyWriter writer, ObjectNode propertiesNode, SerializerProvider provider) throws JsonMappingException {

	}

	@Override
	public void depositSchemaProperty(BeanPropertyWriter writer, JsonObjectFormatVisitor objectVisitor, SerializerProvider provider) throws JsonMappingException {

	}

	private String getPathName(Object bean) {
		Object path = bean;
		if (path instanceof HibernateProxy)
			path = ((HibernateProxy) path).getHibernateLazyInitializer().getImplementation();
		return path.getClass().getSimpleName().toLowerCase();
	}
}