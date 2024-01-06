package com.mile.exception.model;

import com.mile.exception.message.ErrorMessage;
import lombok.Getter;

@Getter
public class MileException extends RuntimeException {
    private ErrorMessage errorMessage;

    public MileException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
