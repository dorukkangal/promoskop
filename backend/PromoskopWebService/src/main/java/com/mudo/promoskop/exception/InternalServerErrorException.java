package com.mudo.promoskop.exception;

public class InternalServerErrorException extends Exception {
	private static final long serialVersionUID = 1L;

	public static final String ERROR_MESSAGE = "Sistemde hata oluştu";

	public InternalServerErrorException() {
		super(ERROR_MESSAGE);
	}
}
