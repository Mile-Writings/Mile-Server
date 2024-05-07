package com.mile.moim.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record WriterMemberJoinRequest(
        @NotBlank(message = "필명이 입력되지 않았습니다.")
        @Max(value = 8, message = "필명은 최대 8자 이내로 작성해주세요.")
        String writerName,

        @NotBlank(message = "소개 글이 입력되지 않았습니다.")
        @Max(value = 110, message = "소개 글은 최대 100자 이내로 작성해주세요.")
        String writerDescription
) {
        public static WriterMemberJoinRequest of(
                final String writerName,
                final String writerDescription
        ) {
                return new WriterMemberJoinRequest(writerName, writerDescription);
        }
}
