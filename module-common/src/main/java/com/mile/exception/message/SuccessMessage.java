package com.mile.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMessage {
    LOGIN_SUCCESS(HttpStatus.OK.value(), "소셜 로그인이 완료되었습니다."),
    ISSUE_ACCESS_TOKEN_SUCCESS(HttpStatus.OK.value(), "액세스 토큰 재발급이 완료되었습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK.value(), "회원 삭제가 완료되었습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK.value(), "로그아웃이 완료되었습니다."),

    COMMENT_DELETE_SUCCESS(HttpStatus.OK.value(), "댓글 삭제가 완료되었습니다."),
    ;

    int status;
    String message;
}
