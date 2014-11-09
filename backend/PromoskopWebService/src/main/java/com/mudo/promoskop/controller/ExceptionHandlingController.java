package com.mudo.promoskop.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mudo.promoskop.exception.InternalServerErrorException;
import com.mudo.promoskop.exception.ResourceNotFoundException;
import com.mudo.promoskop.service.JsonService;

@ControllerAdvice
public class ExceptionHandlingController {
	private static Logger LOG = LoggerFactory.getLogger(ExceptionHandlingController.class);

	@Autowired
	private JsonService jsonService;

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody
	String handleEntityNotFoundException(HttpServletRequest request, Exception e) {
		LOG.info(request.getRequestURL().toString(), e);
		return jsonService.generateJsonForException(e);
	}

	@ExceptionHandler(InternalServerErrorException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	String handleInternalServerErrorException(HttpServletRequest request, Exception e) {
		LOG.info(request.getRequestURL().toString(), e);
		return jsonService.generateJsonForException(new InternalServerErrorException());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	String handleException(HttpServletRequest request, Exception e) {
		LOG.info(request.getRequestURL().toString(), e);
		return jsonService.generateJsonForException(e);
	}
}
