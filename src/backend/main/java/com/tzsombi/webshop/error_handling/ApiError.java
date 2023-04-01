package com.tzsombi.webshop.error_handling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ApiError {

    private final ErrorCode errorCode;

    private final String message;

    private String requestUrl;

    private String requestType;

    private Instant timeStamp = Instant.now();

    public ApiError(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
