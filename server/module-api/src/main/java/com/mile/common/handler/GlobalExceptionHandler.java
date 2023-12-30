package com.mile.common.handler;

import com.mile.common.dto.ErrorResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.MileException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MileException.class)
    public ErrorResponse handleRuntimeException(final MileException e) {
        return ErrorResponse.of(ErrorMessage.INTERNAL_SERVER_ERROR);
    }
}
