package com.mile.common.dto;

import com.mile.exception.message.ErrorMessage;

public record ErrorResponse(
        int status,
        String message
) {
    public static ErrorResponse of(final ErrorMessage errorMessage) {
        return new ErrorResponse(ErrorMessage.INTERNAL_SERVER_ERROR.getStatus(), ErrorMessage.INTERNAL_SERVER_ERROR.getMessage());
    }
}
