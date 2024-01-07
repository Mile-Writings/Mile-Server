package com.mile.dto;

import com.mile.exception.message.ErrorMessage;

public record ErrorResponse(
        int status,
        String message
) {
    public static ErrorResponse of(final ErrorMessage errorMessage) {
        return new ErrorResponse(errorMessage.getStatus(), errorMessage.getMessage());
    }

    public static ErrorResponse of(final int status, final String message) {
        return new ErrorResponse(status, message);
    }
}
