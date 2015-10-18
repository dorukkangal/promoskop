package com.mudo.promoskop.controller;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mudo.promoskop.exception.InternalServerErrorException;
import com.mudo.promoskop.exception.InvalidRequestException;
import com.mudo.promoskop.exception.ResourceNotFoundException;

@ControllerAdvice
public class ExceptionHandlingController {
	private static Logger LOG = LoggerFactory.getLogger(ExceptionHandlingController.class);

	@ExceptionHandler(NoResultException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody
	Exception handleEntityNotFoundException(HttpServletRequest request, Exception e) {
		LOG.info(request.getRequestURL().toString(), e);
		return new ResourceNotFoundException();
	}

	@ExceptionHandler(InvalidRequestException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	Exception handleInvalidRequestException(HttpServletRequest request, Exception e) {
		LOG.info(request.getRequestURL().toString(), e);
		return new InvalidRequestException();
	}

	@ExceptionHandler(InternalServerErrorException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Exception handleInternalServerErrorException(HttpServletRequest request, Exception e) {
		LOG.error(request.getRequestURL().toString(), e);
		return e;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Exception handleException(HttpServletRequest request, Exception e) {
		LOG.error(request.getRequestURL().toString(), e);
		return new InternalServerErrorException();
	}
}
