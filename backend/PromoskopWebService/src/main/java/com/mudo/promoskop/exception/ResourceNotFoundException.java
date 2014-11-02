package com.mudo.promoskop.exception;

import org.codehaus.jackson.map.annotate.JsonFilter;

@JsonFilter("filterResponseBean")
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public static final String ERROR_MESSAGE = "Bu barkod numarası ile ürün bulunamadı";

	public ResourceNotFoundException() {
		super(ERROR_MESSAGE);
	}
}