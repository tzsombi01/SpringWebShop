package com.tzsombi.webshop.error_handling;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRuntimeException extends RuntimeException {
    private ErrorCode errorCode;

    private String message;

    private String param;

    public AbstractRuntimeException(final ErrorCode errorCode, final String message) {
        this(errorCode, message, null);
    }

    public AbstractRuntimeException(final ErrorCode errorCode, final String message, final String param) {
        this.errorCode = errorCode;
        this.message = message;
        this.param = param;
    }
}
