package com.postman.calendify.exception;

public class CalendifyException extends Exception {
	
	private int statusCode;
	
	public CalendifyException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
