package com.mudo.promoskop.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private static String ERROR_MESSAGE = "not found";

	public ResourceNotFoundException() {
		super(ERROR_MESSAGE);
	}
}