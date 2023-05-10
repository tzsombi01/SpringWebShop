package com.tzsombi.webshop.exceptions;


import com.tzsombi.webshop.error_handling.AbstractRuntimeException;
import com.tzsombi.webshop.error_handling.ErrorCode;

public class ProductNotFoundException extends AbstractRuntimeException {

    public ProductNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message, null);
    }

    public static ProductNotFoundException of(final ErrorCode errorCode) {
        return new ProductNotFoundException(errorCode, errorCode.getMessage());
    }
}
