package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;

public class NotFoundException extends MileException {
    public NotFoundException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
