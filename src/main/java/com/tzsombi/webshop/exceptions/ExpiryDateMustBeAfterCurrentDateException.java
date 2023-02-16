package com.tzsombi.webshop.exceptions;

public class ExpiryDateMustBeAfterCurrentDateException extends RuntimeException {
    public ExpiryDateMustBeAfterCurrentDateException(String message) {
        super(message);
    }
}
