package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;

public class TooManyRequestException extends MileException{
    public TooManyRequestException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
