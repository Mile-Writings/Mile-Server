package com.mile.handler;

import com.mile.dto.ErrorResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.JwtValidationException;
import com.mile.exception.model.NotFoundException;
import com.mile.exception.model.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorMessage.ENUM_VALUE_BAD_REQUEST));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(final UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ErrorResponse> handleJwtValidationException(final JwtValidationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorMessage.VALIDATION_REQUEST_MISSING_EXCEPTION));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), fieldError.getDefaultMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(final ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(final NoHandlerFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ErrorMessage.HANDLER_NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception error, final HttpServletRequest request) {
        log.error("================================================NEW===============================================");
        log.error(error.getMessage(), error);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(ErrorMessage.INTERNAL_SERVER_ERROR));
    }
}
