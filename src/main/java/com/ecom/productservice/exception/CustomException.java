package com.ecom.productservice.exception;



public class CustomException extends Exception {

	public CustomException(String string, Exception e) {
        super();
    }

    public CustomException(String message) {
        super(message);
    }
}
