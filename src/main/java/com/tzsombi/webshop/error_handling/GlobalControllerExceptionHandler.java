package com.tzsombi.webshop.error_handling;

import com.tzsombi.webshop.exceptions.StateMisMatchException;
import com.tzsombi.webshop.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        return checkExceptions(exception, request);
    }

    @ExceptionHandler(StateMisMatchException.class)
    public ResponseEntity<ApiError> handleStateMisMatchException(StateMisMatchException exception, WebRequest request) {
        return checkExceptions(exception, request);
    }

    private ResponseEntity<ApiError> checkExceptions(AbstractRuntimeException exception, WebRequest request) {
        if (exception != null) {
            exception.printStackTrace();
            ApiError apiError = new ApiError(exception.getErrorCode(), exception.getMessage());
            if (request != null) {
                try {
                    if (request instanceof ServletWebRequest) {
                        apiError.setRequestUrl(((ServletWebRequest) request).getRequest().getRequestURI());
                        apiError.setRequestType(((ServletWebRequest) request).getRequest().getMethod());
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            return ResponseEntity.status(exception.getErrorCode().getStatus()).body(apiError);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
