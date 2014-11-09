package com.mudo.promoskop.util;

public enum JsonFilter {

	PRODUCT_BY_ID_FILTER(new String[] { "branch_id" }),
	PRODUCT_BY_NAME_FILTER(new String[] { "branches" }),
	BASKET_FILTER(new String[] { "branch_id", "branches" }),
	EXCEPTION_FILTER(new String[] { "detail_message", "cause", "stack_trace", "suppressed_exceptions", "localized_message", "suppressed" });

	private String[] ignoredFields;

	private JsonFilter(String[] ignoredFields) {
		this.ignoredFields = ignoredFields;
	}

	public String[] getFilteredFields() {
		return ignoredFields;
	}
}