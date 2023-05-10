package com.tzsombi.webshop.exceptions;

public class CanNotBuyOwnProductExeption extends RuntimeException {
    public CanNotBuyOwnProductExeption(String message) {
        super(message);
    }
}
