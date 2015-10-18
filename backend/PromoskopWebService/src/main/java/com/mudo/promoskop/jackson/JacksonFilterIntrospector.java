package com.mudo.promoskop.jackson;

import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class JacksonFilterIntrospector extends JacksonAnnotationIntrospector {
	private static final long serialVersionUID = 1L;

	private String filterName;

	public JacksonFilterIntrospector(String filterName) {
		this.filterName = filterName;
	}

	@Override
	public Object findFilterId(AnnotatedClass ac) {
		return filterName;
	}
}