package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.MileException;

public class JwtValidationException extends MileException {
    public JwtValidationException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
