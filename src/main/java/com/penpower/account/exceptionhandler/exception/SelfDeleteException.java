package com.penpower.account.exceptionhandler.exception;

public class SelfDeleteException extends Exception {
	private static final String DEFAULT_MESSAGE = "Can't delete yourself";

	public SelfDeleteException(String message) {
		super(message);
	}

	public SelfDeleteException() {
		super(DEFAULT_MESSAGE);
	}
}
