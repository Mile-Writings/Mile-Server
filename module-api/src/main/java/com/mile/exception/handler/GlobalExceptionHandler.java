package com.mile.exception.handler;

import com.mile.dto.ErrorResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ConflictException;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.JwtValidationException;
import com.mile.exception.model.NotFoundException;
import com.mile.exception.model.TooManyRequestException;
import com.mile.exception.model.UnauthorizedException;
import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final int INDEX_ZERO = 0;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorMessage.ENUM_VALUE_BAD_REQUEST));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(final HandlerMethodValidationException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(e.getAllValidationResults().get(INDEX_ZERO).getResolvableErrors().get(INDEX_ZERO).getDefaultMessage())));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorMessage.REQUEST_URL_WRONG_ERROR));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(final UnauthorizedException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ErrorResponse> handleJwtValidationException(final JwtValidationException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ErrorResponse.of(ErrorMessage.METHOD_NOT_SUPPORTED));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        Sentry.captureException(e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorMessage.VALIDATION_REQUEST_MISSING_EXCEPTION));

        return switch (fieldError.getCode()) {
            case "NotNull", "NotBlank" ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorMessage.VALIDATION_REQUEST_NULL_OR_BLANK_EXCEPTION));
            case "Size" ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorMessage.VALIDATION_REQUEST_LENGTH_EXCEPTION));
            default ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorMessage.VALIDATION_REQUEST_MISSING_EXCEPTION));
        };
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(final ForbiddenException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(final ConflictException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<ErrorResponse> handleTooManyRequestException(final TooManyRequestException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ErrorResponse.of(e.getErrorMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(final NoHandlerFoundException e) {
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ErrorMessage.HANDLER_NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception error, final HttpServletRequest request) {
        Sentry.captureException(error);
        log.error("================================================NEW===============================================");
        log.error(error.getMessage(), error);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(ErrorMessage.INTERNAL_SERVER_ERROR));
    }
}
