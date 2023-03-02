package com.tzsombi.webshop.error_handling;

import com.tzsombi.webshop.constants.ErrorConstants;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(ErrorConstants.USER_NOT_FOUND_MSG, HttpStatus.NOT_FOUND),
    STATE_MISMATCH(ErrorConstants.ERROR_OCCURRED_WHEN_SETTING_THE_CARD_MSG, HttpStatus.CONFLICT);

    private final String message;

    private final HttpStatus status;

    ErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
