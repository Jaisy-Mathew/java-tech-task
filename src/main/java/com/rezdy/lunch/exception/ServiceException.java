package com.rezdy.lunch.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String exception) {
        super(exception);
    }
}
