package com.mile.moim.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record MoimCreateRequest(
        @Max(value = 10, message = "글모임명은 최대 10자 이내로 작성해주세요.")
        @NotBlank(message = "글감 제목이 비어 있습니다.")
        String moimName,
        @Max(value = 90, message = "글모임 소개글은 90자 이내로 작성해주세요.")
        String moimDescription,
        @NotBlank(message = "공개 여부를 선택해 주세요.")
        Boolean isPublic,
        String imageUrl,

        @NotBlank(message = "필명이 입력되지 않았습니다.")
        @Max(value = 8, message = "필명은 최대 8자 이내로 작성해주세요.")
        String writerName,
        @Max(value = 100, message = "필명 소개글은 최대 100자 이내로 작성해주세요.")
        String writerNameDescription,

        @Max(value = 15, message = "글감은 최대 15자 이내로 작성해주세요.")
        @NotBlank(message = "글감 제목이 비어 있습니다.")
        String topic,
        @Max(value = 5, message = "글감 태그는 최대 5자 이내로 작성해주세요.")
        @NotBlank(message = "글감 태그가 비어 있습니다.")
        String topicTag,
        @Max(value = 90, message = "글감 소개글은 최대 90자 이내로 작성해주세요.")
        @NotBlank(message = "글감 설명은 비어 있습니다.")
        String topicDescription
) {
}
