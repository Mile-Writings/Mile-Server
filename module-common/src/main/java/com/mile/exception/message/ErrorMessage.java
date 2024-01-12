package com.mile.exception.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {
    /*
    Not Found
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저는 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저의 리프레시 토큰이 존재하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 글은 존재하지 않습니다."),
    COMMENTS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 글에 대한 댓글이 존재하지 않습니다."),
    MOIM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 글모임이 존재하지 않습니다."),
    CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 모임의 주제가 존재하지 않습니다."),
    HANDLER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "요청하신 URL은 정보가 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 댓글이 존재하지 않습니다."),
    CURIOUS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 궁금해요는 존재하지 않습니다."),
    TOPIC_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 글감이 존재하지 않습니다."),
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 글모임의 글감 키워드가 존재하지 않습니다."),
    WRITERS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 모임의 작가가 요청한 개수 이상 존재하지 않습니다"),
    WRITER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 작가는 존재하지 않습니다."),
    RECCOMEND_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "추천 글감을 받아오는데 실패했습니다."),
    MOIM_TOPIC_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 모임의 글감이 존재하지 않습니다."),
    TOPIC_POST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 글감의 글이 존재하지 않습니다."),
    MOIM_POST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 모임의 글이 존재하지 않습니다."),
    /*
    Bad Request
     */
    ENUM_VALUE_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "요청한 값이 유효하지 않습니다."),
    AUTHENTICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST.value(), "인가 코드가 만료되었습니다."),
    SOCIAL_TYPE_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "로그인 요청이 유효하지 않습니다."),
    INVALID_BUCKET_PREFIX(HttpStatus.BAD_REQUEST.value(), "유효하지 않는 S3 버킷 디렉터리 이름입니다."),
    VALIDATION_REQUEST_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "요청 값이 유효하지 않습니다."),
    BEARER_LOST_ERROR(HttpStatus.BAD_REQUEST.value(), "토큰의 요청에 Bearer이 담겨 있지 않습니다."),
    POST_NOT_TEMPORARY_ERROR(HttpStatus.BAD_REQUEST.value(), "해당 글은 임시저장글이 아닙니다."),

    IMAGE_EXTENSION_INVALID_ERROR(HttpStatus.BAD_REQUEST.value(), "이미지 확장자는 jpg, png, webp만 가능합니다."),
    IMAGE_SIZE_INVALID_ERROR(HttpStatus.BAD_REQUEST.value(), "이미지 사이즈는 5MB를 넘을 수 없습니다."),

    /*
    Conflict
     */
    CURIOUS_ALREADY_EXISTS_EXCEPTION(HttpStatus.CONFLICT.value(), "'궁금해요'는 이미 존재합니다."),

    /*
    Unauthorized
     */
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "액세스 토큰이 만료되었습니다."),
    TOKEN_INCORRECT_ERROR(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰이 유효하지 않습니다."),
    TOKEN_VALIDATION_ERROR(HttpStatus.UNAUTHORIZED.value(), "사용자 검증 토큰이 유효하지 않습니다."),
    /*
    Forbidden
     */
    USER_AUTHENTICATE_ERROR(HttpStatus.FORBIDDEN.value(), "해당 사용자는 모임에 접근 권한이 없습니다."),
    WRITER_AUTHENTICATE_ERROR(HttpStatus.FORBIDDEN.value(), "해당 사용자는 글 생성/수정/삭제 권한이 없습니다."),
    /*
    Forbidden
     */
    COMMENT_ACCESS_ERROR(HttpStatus.FORBIDDEN.value(), "해당 사용자는 댓글에 접근 권한이 없습니다."),
    /*
    Internal Server Error
     */
    IMAGE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "S3 버킷에 이미지를 업로드에 실패했습니다."),
    PRESIGNED_URL_GET_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "S3 PRESIGNED URL을 받아오기에 실패했습니다."),
    IMAGE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "S3 버킷으로부터 이미지를 삭제하는 데 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류입니다.");

    final int status;
    final String message;
}
