package com.smt.base;

/**
 * 
 * @author DougLei
 */
public class SmtBaseException extends RuntimeException {

	public SmtBaseException() {}
	public SmtBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public SmtBaseException(String message, Throwable cause) {
		super(message, cause);
	}
	public SmtBaseException(String message) {
		super(message);
	}
	public SmtBaseException(Throwable cause) {
		super(cause);
	}
}
