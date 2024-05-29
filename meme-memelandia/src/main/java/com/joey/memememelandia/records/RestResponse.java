package com.joey.memememelandia.records;

import org.springframework.http.HttpStatus;

public record RestResponse<E>(HttpStatus httpStatus, E body) {
}
