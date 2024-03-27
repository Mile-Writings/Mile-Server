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
    MOIM_POPULAR_WRITER_SEARCH_SUCCESS(HttpStatus.OK.value(), "궁금해요 TOP 2 작가 조회가 완료되었습니다."),
    WRITER_AUTHENTIACTE_SUCCESS(HttpStatus.OK.value(), "게시글 권한이 확인되었습니다."),
    POST_PUT_SUCCESS(HttpStatus.OK.value(), "글 수정이 완료되었습니다."),
    MOIM_INFO_SUCCESS(HttpStatus.OK.value(), "글모임 조회가 완료되었습니다."),
    RECOMMENDATION_GET_SUCCESS(HttpStatus.OK.value(), "추천 글감이 조회되었습니다."),
    PRESIGNED_URL_GET_SUCCESS(HttpStatus.OK.value(), "이미지를 업로드할 url이 발행되었습니다."),
    POST_DELETE_SUCCESS(HttpStatus.OK.value(), "글 삭제가 완료되었습니다."),
    TOPIC_LIST_SEARCH_SUCCESS(HttpStatus.OK.value(), "글감 리스트 조회가 완료되었습니다."),
    MOIM_TOPIC_GET_SUCCESS(HttpStatus.OK.value(), "글감 조회가 완료되었습니다."),
    TEMPORARY_POST_GET_SUCCESS(HttpStatus.OK.value(), "임시저장글 조회가 완료되었습니다."),
    MODIFY_POST_GET_SUCCESS(HttpStatus.OK.value(), "글 수정 시 글 조회가 완료되었습니다."),
    MOIM_TOP_2_POST_GET_SUCCESS(HttpStatus.OK.value(), "궁금해요 상위 2개의 글이 조회 완료되었습니다."),
    POST_GET_SUCCESS(HttpStatus.OK.value(), "글 조회가 완료되었습니다."),
    MOIM_POST_GET_SUCCESS(HttpStatus.OK.value(), "카테고리별 글 리스트 조회가 완료되었습니다."),
    BEST_MOIM_POSTS_GET_SUCCESS(HttpStatus.OK.value(), "베스트 활동 모임과 글 조회가 완료되었습니다."),
    IS_TEMPORARY_POST_EXIST_GET_SUCCESS(HttpStatus.OK.value(), "임시저장 글 존재 여부 조회가 완료되었습니다."),
    MOIM_INVITE_INFO_GET_SUCCESS(HttpStatus.OK.value(), "모임의 초대 정보 조회가 완료되었습니다."),
    IS_CONFLICT_WRITER_NAME_GET_SUCCESS(HttpStatus.OK.value(), "댓글 중복 여부가 조회되었습니다."),
    MOIM_INFORMATION_PUT_SUCCESS(HttpStatus.OK.value(), "모임 정보 수정이 완료되었습니다."),
    IS_CONFLICT_MOIM_NAME_GET_SUCCESS(HttpStatus.OK.value(), "글모임 이름 중복 확인이 완료되었습니다."),
    MOIM_MEMBER_DELETE_SUCCESS(HttpStatus.OK.value(), "멤버 삭제가 완료되었습니다."),
    /*
    201 CREATED
     */
    WRITER_JOIN_SUCCESS(HttpStatus.CREATED.value(), "모임 가입에 완료되었습니다"),
    COMMENT_CREATE_SUCCESS(HttpStatus.CREATED.value(), "댓글 등록이 완료되었습니다."),
    CURIOUS_CREATE_SUCCESS(HttpStatus.CREATED.value(), "궁금해요 생성이 완료되었습니다."),
    POST_CREATE_SUCCESS(HttpStatus.OK.value(), "글 생성이 완료되었습니다."),
    TEMPORARY_POST_CREATE_SUCCESS(HttpStatus.OK.value(), "임시저장 글 생성이 완료되었습니다."),
    ;

    final int status;
    final String message;
}
