package com.mile.moim.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MoimCreateRequest(
        @Size(max = 10, message = "글모임명은 최대 10자 이내로 작성해주세요.")
        @NotBlank(message = "글감 제목이 비어 있습니다.")
        String moimName,
        @Size(max = 90, message = "글모임 소개글은 90자 이내로 작성해주세요.")
        String moimDescription,
        @NotNull(message = "공개 여부를 선택해 주세요.")
        Boolean isPublic,
        String imageUrl,

        @NotBlank(message = "필명이 입력되지 않았습니다.")
        @Size(max = 8, message = "필명은 최대 8자 이내로 작성해주세요.")
        String writerName,
        @Size(max = 100, message = "필명 소개글은 최대 100자 이내로 작성해주세요.")
        String writerNameDescription,

        @Size(max = 15, message = "글감은 최대 15자 이내로 작성해주세요.")
        @NotBlank(message = "글감 제목이 비어 있습니다.")
        String topic,
        @Size(max = 5, message = "글감 태그는 최대 5자 이내로 작성해주세요.")
        @NotBlank(message = "글감 태그가 비어 있습니다.")
        String topicTag,
        @Size(max = 90, message = "글감 소개글은 최대 90자 이내로 작성해주세요.")
        String topicDescription
) {
}
