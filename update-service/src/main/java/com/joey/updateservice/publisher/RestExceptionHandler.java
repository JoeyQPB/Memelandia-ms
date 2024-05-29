package com.joey.updateservice.publisher;

import com.joey.updateservice.exceptions.MissingFieldsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(MissingFieldsException.class)
    private void missingFieldsExceptionHandler (MissingFieldsException exception) {
        LOGGER.error("Error: missingFieldsExceptionHandler [BAD_REQUEST] - " + exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    private void runtimeExceptionHandler (RuntimeException exception) {
        LOGGER.error("Error: runtimeExceptionHandler [INTERNAL_SERVER_ERROR]  - " + exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    private void exceptionHandler (Exception exception) {
        LOGGER.error("Error: exceptionHandler [INTERNAL_SERVER_ERROR] - " + exception.getMessage());
    }
}
