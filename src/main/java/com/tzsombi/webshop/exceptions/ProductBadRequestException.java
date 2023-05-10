package com.tzsombi.webshop.exceptions;


public class ProductBadRequestException extends RuntimeException {

    public ProductBadRequestException(String message) {
        super(message);
    }
}
