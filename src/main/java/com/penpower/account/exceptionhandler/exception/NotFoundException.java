package com.penpower.account.exceptionhandler.exception;

public class NotFoundException extends Exception {
	private static final String DEFAULT_MESSAGE = "Resource not found";

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException() {
		super(DEFAULT_MESSAGE);
	}
}
