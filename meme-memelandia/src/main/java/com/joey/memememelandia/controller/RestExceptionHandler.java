package com.joey.memememelandia.controller;

import com.joey.memememelandia.exceptions.CategoryNotFoundException;
import com.joey.memememelandia.exceptions.MemeNotFoundException;
import com.joey.memememelandia.exceptions.MissingFieldsException;
import com.joey.memememelandia.exceptions.UserNotFoundException;
import com.joey.memememelandia.records.RestErrorMessage;
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

    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> exceptionHandler (Exception exception) {
        LOGGER.error("Error: Exception - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<String> runtimeExceptionHandler (RuntimeException exception) {
        LOGGER.error("Error: RuntimeException - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<String> nullPointerExceptionHandler (NullPointerException exception) {
        LOGGER.error("Error: NullPointerException - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    private ResponseEntity<String> categoryNotFoundExceptionHandler (CategoryNotFoundException exception) {
        LOGGER.error("Error: CategoryNotFoundException - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(MemeNotFoundException.class)
    private ResponseEntity<String> memeNotFoundExceptionHandler (MemeNotFoundException exception) {
        LOGGER.error("Error: MemeNotFoundException - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(MissingFieldsException.class)
    private ResponseEntity<String> missingFieldsExceptionHandler (MissingFieldsException exception) {
        LOGGER.error("Error: MissingFieldsException - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<String> userNotFoundExceptionHandler (UserNotFoundException exception) {
        LOGGER.error("Error: UserNotFoundException - " + exception.getMessage());
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body("@ControllerAdvice: " + threatResponse.body());
    }
}
