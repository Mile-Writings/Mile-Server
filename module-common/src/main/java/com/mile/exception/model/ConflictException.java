package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;

public class ConflictException extends MileException {
    public ConflictException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}