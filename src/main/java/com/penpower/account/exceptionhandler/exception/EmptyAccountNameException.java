package com.penpower.account.exceptionhandler.exception;

public class EmptyAccountNameException extends Exception {
	private static final String DEFAULT_MESSAGE = "AccountName can't be blank";

	public EmptyAccountNameException(String message) {
		super(message);
	}

	public EmptyAccountNameException() {
		super(DEFAULT_MESSAGE);
	}
}
