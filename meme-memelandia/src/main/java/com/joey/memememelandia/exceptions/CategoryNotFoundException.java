package com.joey.memememelandia.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(String msg) {
        super(msg);
    }
}
