package com.bookstore.exception;

public class DuplicateRecourseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateRecourseException(String message) {
		super(message);
	}

}
