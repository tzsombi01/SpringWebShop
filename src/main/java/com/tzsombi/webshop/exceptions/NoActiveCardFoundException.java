package com.tzsombi.webshop.exceptions;

public class NoActiveCardFoundException extends RuntimeException {
    public NoActiveCardFoundException(String message) {
        super(message);
    }
}
