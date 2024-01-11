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
    COMMENT_DELETE_SUCCESS(HttpStatus.OK.value(), "댓글 삭제가 완료되었습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK.value(), "로그아웃이 완료되었습니다."),
    TOPIC_SEARCH_SUCCESS(HttpStatus.OK.value(), "주제 조회가 완료되었습니다."),
    COMMENT_SEARCH_SUCCESS(HttpStatus.OK.value(), "댓글 조회가 완료되었습니다."),
    MOIM_AUTHENTICATE_SUCCESS(HttpStatus.OK.value(), "사용자의 권한이 확인되었습니다."),
    CURIOUS_INFO_SEARCH_SUCCESS(HttpStatus.OK.value(), "궁금해요 개수 및 궁금해요 여부 조회가 완료되었습니다."),
    CURIOUS_DELETE_SUCCESS(HttpStatus.OK.value(), "궁금해요 삭제가 완료되었습니다."),
    WRITER_AUTHENTIACTE_SUCCESS(HttpStatus.OK.value(), "게시글 권한이 확인되었습니다."),
    POST_PUT_SUCCESS(HttpStatus.OK.value(), "글 수정이 완료되었습니다."),
    MOIM_INFO_SUCCESS(HttpStatus.OK.value(), "글모임 조회가 완료되었습니다."),
    PRESIGNED_URL_GET_SUCCESS(HttpStatus.OK.value(), "이미지를 업로드할 url이 발행되었습니다."),
    POST_DELETE_SUCCESS(HttpStatus.OK.value(), "글 삭제가 완료되었습니다."),
    CATEGORY_LIST_SEARCH_SUCCESS(HttpStatus.OK.value(), "카테고리 리스트 조회가 완료되었습니다."),
    /*
    201 CREATED
     */
    COMMENT_CREATE_SUCCESS(HttpStatus.CREATED.value(), "댓글 등록이 완료되었습니다."),
    CURIOUS_CREATE_SUCCESS(HttpStatus.CREATED.value(), "궁금해요 생성이 완료되었습니다."),

    ;

    int status;
    String message;
}
