package com.mudo.promoskop.exception;

public class InvalidRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public static final String ERROR_MESSAGE = "Gonderilen mesaj hatali";

	public InvalidRequestException() {
		super(ERROR_MESSAGE);
	}
}
