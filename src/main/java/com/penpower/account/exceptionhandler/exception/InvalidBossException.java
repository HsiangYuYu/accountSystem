package com.penpower.account.exceptionhandler.exception;

public class InvalidBossException extends Exception{
	public InvalidBossException(String message) {
		super("BossName:" + message + " is not valid");
	}
}
