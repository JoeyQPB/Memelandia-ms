package com.joey.updateservice.exceptions;

public class MissingFieldsException extends RuntimeException{
    public MissingFieldsException(String msg) {
        super(msg);
    }
}
