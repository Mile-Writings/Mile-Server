package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;

public class MileException extends RuntimeException{
    public MileException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
