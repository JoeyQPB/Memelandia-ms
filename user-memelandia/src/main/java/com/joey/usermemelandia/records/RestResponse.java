package com.joey.usermemelandia.records;

import org.springframework.http.HttpStatus;

public record RestResponse<E> (HttpStatus httpStatus, E body) {}
