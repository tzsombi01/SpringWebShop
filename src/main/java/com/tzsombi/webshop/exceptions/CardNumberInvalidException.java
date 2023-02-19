package com.tzsombi.webshop.exceptions;

public class CardNumberInvalidException extends RuntimeException {
    public CardNumberInvalidException(String message) {
        super(message);
    }
}
