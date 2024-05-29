package com.joey.memememelandia.records;

import org.springframework.http.HttpStatus;

public record RestErrorMessage<E>(HttpStatus httpStatus, E body) {
}
