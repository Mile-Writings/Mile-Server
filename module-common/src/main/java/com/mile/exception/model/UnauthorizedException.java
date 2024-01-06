package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;

public class UnauthorizedException extends MileException {
    public UnauthorizedException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
