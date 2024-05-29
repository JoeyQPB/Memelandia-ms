package com.joey.categorymemelandia.records;

import org.springframework.http.HttpStatus;

public record RestResponse<E> (HttpStatus httpStatus, E body) {}