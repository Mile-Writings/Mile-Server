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
    USER_NOT_FOUND(40400, "해당 유저는 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(40401, "해당 유저의 리프레시 토큰이 존재하지 않습니다."),
    POST_NOT_FOUND(40402, "해당 글은 존재하지 않습니다."),
    MOIM_NOT_FOUND(40403, "해당 글모임이 존재하지 않습니다."),
    CONTENT_NOT_FOUND(40404, "해당 모임의 주제가 존재하지 않습니다."),
    HANDLER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "요청하신 URL은 정보가 없습니다."),
    COMMENT_NOT_FOUND(40406, "해당 댓글이 존재하지 않습니다."),
    CURIOUS_NOT_FOUND(40407, "해당 궁금해요는 존재하지 않습니다."),
    TOPIC_NOT_FOUND(40408, "해당 글감이 존재하지 않습니다."),
    KEYWORD_NOT_FOUND(40409, "해당 글모임의 글감 키워드가 존재하지 않습니다."),
    WRITERS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 모임의 작가가 요청한 개수 이상 존재하지 않습니다"),
    WRITER_NOT_FOUND(40411, "해당 작가는 존재하지 않습니다."),
    RECOMMEND_NOT_FOUND(40412, "추천 글감을 받아오는데 실패했습니다."),
    MOIM_TOPIC_NOT_FOUND(40413, "해당 모임의 글감이 존재하지 않습니다."),
    TOPIC_POST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 글감의 글이 존재하지 않습니다."),
    MOIM_POST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 모임의 글이 존재하지 않습니다."),
    RANDOM_VALUE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "랜덤 글 이름을 생성하는데 실패했습니다."),
    REPLY_NOT_FOUND(40416, "Id에 해당하는 대댓글이 없습니다."),
    /*
    Bad Request
     */
    ENUM_VALUE_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "요청한 값이 유효하지 않습니다."),
    AUTHENTICATION_CODE_EXPIRED(40001, "인가 코드가 만료되었습니다."),
    SOCIAL_TYPE_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "로그인 요청이 유효하지 않습니다."),
    INVALID_BUCKET_PREFIX(HttpStatus.BAD_REQUEST.value(), "유효하지 않는 S3 버킷 디렉터리 이름입니다."),
    VALIDATION_REQUEST_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "요청 값이 유효하지 않습니다."),
    VALIDATION_REQUEST_NULL_OR_BLANK_EXCEPTION(40005,"요청 값이 비어있습니다."),
    VALIDATION_REQUEST_LENGTH_EXCEPTION(40006,"요청 값이 길이를 초과했습니다."),
    BEARER_LOST_ERROR(HttpStatus.BAD_REQUEST.value(), "토큰의 요청에 Bearer이 담겨 있지 않습니다."),
    POST_NOT_TEMPORARY_ERROR(40008, "해당 글은 임시저장 글이 아닙니다."),
    POST_TEMPORARY_ERROR(HttpStatus.BAD_REQUEST.value(), "해당 글은 임시저장글입니다."),
    PATH_PARAMETER_INVALID_ERROR(HttpStatus.BAD_REQUEST.value(), "요청 URI를 다시 확인해주세요."),
    REQUEST_URL_WRONG_ERROR(HttpStatus.BAD_REQUEST.value(), "요청 URL를 다시 확인해주세요"),
    IMAGE_EXTENSION_INVALID_ERROR(HttpStatus.BAD_REQUEST.value(), "이미지 확장자는 jpg, png, webp만 가능합니다."),
    IMAGE_SIZE_INVALID_ERROR(HttpStatus.BAD_REQUEST.value(), "이미지 사이즈는 5MB를 넘을 수 없습니다."),
    INVALID_URL_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "요청된 URL을 다시 확인해주세요"),
    LEAST_TOPIC_SIZE_OF_MOIM_ERROR(40015, "모임에는 최소 하나의 글감이 있어야 합니다."),
    USER_MOIM_ALREADY_JOIN(HttpStatus.BAD_REQUEST.value(), "사용자는 이미 모임에 가입했습니다."),
    WRITER_NAME_LENGTH_WRONG(HttpStatus.BAD_REQUEST.value(), "사용 불가능한 필명입니다."),
    MOIM_NAME_VALIDATE_ERROR(40018, "사용 불가능한 모임명입니다."),
    EXCEED_MOIM_MAX_SIZE(40019, "최대 가입 가능 모임 개수(5개)를 초과하였습니다."),
    /*
    Conflict
     */
    CURIOUS_ALREADY_EXISTS_EXCEPTION(40900, "'궁금해요'는 이미 존재합니다."),
    WRITER_NAME_ALREADY_EXIST(40901, "이미 가입한 모임입니다."),
    WRITER_NAME_OF_MOIM_ALREADY_EXISTS_EXCEPTION(40902, "모임에 이미 존재하는 필명입니다."),
    /*
    Unauthorized
     */
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "액세스 토큰이 만료되었습니다."),
    TOKEN_INCORRECT_ERROR(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰이 유효하지 않습니다."),
    TOKEN_VALIDATION_ERROR(HttpStatus.UNAUTHORIZED.value(), "사용자 검증 토큰이 유효하지 않습니다."),
    UN_LOGIN_EXCEPTION(40103, "로그인 후 진행해주세요."),
    /*
    Forbidden
     */
    USER_MOIM_AUTHENTICATE_ERROR(40300, "해당 사용자는 모임에 접근 권한이 없습니다."),
    REPLY_USER_FORBIDDEN(40301, "사용자에게 해당 대댓글에 대한 권한이 없습니다."),
    WRITER_AUTHENTICATE_ERROR(40302, "해당 사용자는 글 생성/수정/삭제 권한이 없습니다."),
    MOIM_OWNER_AUTHENTICATION_ERROR(40303, "사용자는 해당 모임의 모임장이 아닙니다."),
    WRITER_NAME_INFO_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "해당 사용자는 필명에 접근 권한이 없습니다."),
    COMMENT_ACCESS_ERROR(40305, "해당 사용자는 댓글에 접근 권한이 없습니다."),
    /*
    Method Not Supported
     */
    METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED.value(), "요청을 다시 확인해주세요."),
    /*
    Too Many Requests
     */
    TOO_MANY_REQUESTS_EXCEPTION(HttpStatus.TOO_MANY_REQUESTS.value(), "요청이 중복되었습니다."),
    /*
    Internal Server Error
     */
    IMAGE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "S3 버킷에 이미지를 업로드에 실패했습니다."),
    PRESIGNED_URL_GET_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "S3 PRESIGNED URL을 받아오기에 실패했습니다."),
    IMAGE_DELETE_ERROR(50002, "S3 버킷으로부터 이미지를 삭제하는 데 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류입니다."),
    DISCORD_LOG_APPENDER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "디스코드 로그 전송에 실패하였습니다"),
    TIME_OUT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "락을 획득하는 과정에서 Time Out이 발생했습니다."),
    ;

    final int status;
    final String message;
}
