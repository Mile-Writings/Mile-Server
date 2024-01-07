package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;

public class ForbiddenException extends MileException {
    public ForbiddenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
