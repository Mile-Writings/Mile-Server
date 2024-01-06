package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;

public class BadRequestException extends MileException {
    public BadRequestException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
