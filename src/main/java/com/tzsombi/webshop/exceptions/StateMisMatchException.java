package com.tzsombi.webshop.exceptions;

import com.tzsombi.webshop.error_handling.AbstractRuntimeException;
import com.tzsombi.webshop.error_handling.ErrorCode;

public class StateMisMatchException extends AbstractRuntimeException {
    public StateMisMatchException(ErrorCode errorCode, String message, String param) {
        super(errorCode, message, param);
    }

    public StateMisMatchException(ErrorCode errorCode, String message) {
        super(errorCode, message, null);
    }

    public static StateMisMatchException ofCode(final ErrorCode errorCode) {
        String message = String.format("Reason: '%s'", errorCode);

        return new StateMisMatchException(errorCode, message);
    }
}
