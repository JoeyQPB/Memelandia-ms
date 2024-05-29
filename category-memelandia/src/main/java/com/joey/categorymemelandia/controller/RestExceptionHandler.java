package com.joey.categorymemelandia.controller;

import com.joey.categorymemelandia.exceptions.BadGatewayException;
import com.joey.categorymemelandia.exceptions.CategoryNotFoundException;
import com.joey.categorymemelandia.exceptions.MissingFieldsException;
import com.joey.categorymemelandia.exceptions.UnableToCallUpdateService;
import com.joey.categorymemelandia.records.RestErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(CategoryNotFoundException.class)
    private ResponseEntity<String> categoryNotFoundExceptionHandler (CategoryNotFoundException exception) {
        LOGGER.error("Error: Category Not Found Exception - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<String> runtimeExceptionHandler (RuntimeException exception) {
        LOGGER.error("Error: RuntimeException - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> exceptionHandler (Exception exception) {
        LOGGER.error("Error: Exception - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(MissingFieldsException.class)
    private ResponseEntity<String> missingFieldsExceptionHandler (MissingFieldsException exception) {
        LOGGER.error("Error: Missing Fields Exception - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<String> nullPointerExceptionHandler (NullPointerException exception) {
        LOGGER.error("Error: NullPointerExceptionHandler - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(BadGatewayException.class)
    private ResponseEntity<String> badGatewayExceptionHandler (BadGatewayException exception) {
        LOGGER.error("Error: BadGatewayException - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.BAD_GATEWAY, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(UnableToCallUpdateService.class)
    private ResponseEntity<String> unableToCallUpdateServiceHandler (UnableToCallUpdateService exception) {
        LOGGER.error("Error: unableToCallUpdateService - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }
}
