package com.mile.exception.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {
    /*
    Not Found
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저는 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저의 리프레시 토큰이 존재하지 않습니다."),
    /*
    Bad Request
     */
    ENUM_VALUE_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "요청한 값이 유효하지 않습니다."),
    AUTHENTICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST.value(), "인가 코드가 만료되었습니다."),
    SOCIAL_TYPE_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "로그인 요청이 유효하지 않습니다."),
    /*
    Unauthorized
     */
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "액세스 토큰이 만료되었습니다."),
    TOKEN_INCORRECT_ERROR(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰이 유효하지 않습니다."),

    /*
    Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류입니다.");

    final int status;
    final String message;
}
