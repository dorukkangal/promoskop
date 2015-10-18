package com.mudo.promoskop.jackson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JacksonFilterContext {
	private final List<String> excludePropertiesForAll = Arrays.asList("hibernate_lazy_initializer", "handler", "detail_message", "cause", "stack_trace",
			"suppressed_exceptions", "localized_message", "suppressed");

	private List<String> includeProperties = new ArrayList<String>();
	private List<String> excludeProperties = new ArrayList<String>();

	private List<String> includePaths = new ArrayList<String>();
	private List<String> excludePaths = new ArrayList<String>();

	public void includeProperty(String... propertyNames) {
		for (String propertyName : propertyNames) {
			includeProperties.add(propertyName);
		}
	}

	public void excludeProperty(String... propertyNames) {
		for (String propertyName : propertyNames) {
			excludeProperties.add(propertyName);
		}
	}

	public void includePath(String... pathNames) {
		for (String pathName : pathNames) {
			includePaths.add(pathName);
		}
	}

	public void excludePath(String... pathNames) {
		for (String pathName : pathNames) {
			excludePaths.add(pathName);
		}
	}

	public boolean ignore(String pathName, String propertyName) {
		pathName = pathName.concat(".").concat(propertyName);
		boolean ignore = !isIncludedProperty(propertyName) && isExcludedProperty(propertyName);
		ignore = ignore || !isIncludedPath(pathName) && isExcludedPath(pathName);
		return ignore;
	}

	public boolean isIncludedProperty(String propertyName) {
		return includeProperties.contains(propertyName);
	}

	public boolean isExcludedProperty(String propertyName) {
		return excludeProperties.contains(propertyName) || excludePropertiesForAll.contains(propertyName);
	}

	public boolean isIncludedPath(String pathName) {
		return includePaths.contains(pathName);
	}

	public boolean isExcludedPath(String pathName) {
		return excludePaths.contains(pathName);
	}
}