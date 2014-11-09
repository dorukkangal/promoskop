package com.mudo.promoskop.controller;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlingController {
	private static Logger LOG = LoggerFactory.getLogger(ExceptionHandlingController.class);

	private static final String ENTITY_NOT_FOUND_MESSAGE = "Bu barkod numarası ile ürün bulunamadı";
	public static final String INTERNAL_ERROR_MESSAGE = "Sistemde hata oluştu";

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public @ResponseBody
	ErrorInfo handleEntityNotFoundException(HttpServletRequest request, Exception e) {
		LOG.info(request.getRequestURL().toString(), e);
		return new ErrorInfo(request.getRequestURL(), ENTITY_NOT_FOUND_MESSAGE);
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public @ResponseBody
	ErrorInfo handleException(HttpServletRequest request, Exception e) {
		LOG.error(request.getRequestURL().toString(), e);
		return new ErrorInfo(request.getRequestURL(), INTERNAL_ERROR_MESSAGE);
	}

	@SuppressWarnings("unused")
	private class ErrorInfo {
		private final String message;
		private final String url;

		public ErrorInfo(StringBuffer url, String message) {
			this.url = url.toString();
			this.message = message;
		}
	}
}
