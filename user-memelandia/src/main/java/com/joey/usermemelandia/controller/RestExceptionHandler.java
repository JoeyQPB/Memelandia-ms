package com.joey.usermemelandia.controller;

import com.joey.usermemelandia.exceptions.BadGatewayException;
import com.joey.usermemelandia.exceptions.EntityNotFoundException;
import com.joey.usermemelandia.exceptions.MissingFieldsException;
import com.joey.usermemelandia.exceptions.UnableToCallUpdateService;
import com.joey.usermemelandia.records.RestErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> entityNotFoundExceptionHandler (EntityNotFoundException exception) {
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body(threatResponse.body());
    }

    @ExceptionHandler(MissingFieldsException.class)
    private ResponseEntity<String> missingFieldsExceptionHandler (MissingFieldsException exception) {
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body(threatResponse.body());
    }

    @ExceptionHandler(UnableToCallUpdateService.class)
    private ResponseEntity<String> unableToCallUpdateServiceHandler (UnableToCallUpdateService exception) {
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body(threatResponse.body());
    }

    @ExceptionHandler(BadGatewayException.class)
    private ResponseEntity<String> badGatewayExceptionHandler (BadGatewayException exception) {
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.BAD_GATEWAY, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body(threatResponse.body());
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<String> runtimeExceptionHandler (RuntimeException exception) {
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body(threatResponse.body());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> exceptionHandler (Exception exception) {
        RestErrorMessage<String> threatResponse = new RestErrorMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.httpStatus()).body(threatResponse.body());
    }
}
