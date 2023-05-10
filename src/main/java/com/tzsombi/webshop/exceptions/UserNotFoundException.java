package com.tzsombi.webshop.exceptions;

import com.tzsombi.webshop.error_handling.AbstractRuntimeException;
import com.tzsombi.webshop.error_handling.ErrorCode;

public class UserNotFoundException extends AbstractRuntimeException {
    public UserNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static UserNotFoundException ofUserId(final ErrorCode errorCode, String param) {
        String message = String.format("Reason: '%s' Id: '%s'", errorCode, param);

        return new UserNotFoundException(errorCode, message);
    }
}
