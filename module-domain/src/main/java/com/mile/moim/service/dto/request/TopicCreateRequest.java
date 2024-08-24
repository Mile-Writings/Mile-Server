package com.mile.moim.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TopicCreateRequest(
        @Size(max = 15, message = "글감은 최대 15자 이내로 작성해주세요.")
        @NotBlank(message = "글감 제목이 비어있습니다.")
        String topicName,
        @Size(max = 5, message = "글감 태그는 최대 5자 이내로 작성해주세요.")
        @NotBlank(message = "글감 태그가 비어있습니다.")
        String topicTag,
        @Size(max = 90, message = "글감 설명은 최대 90자 이내로 작성해주세요.")
        String topicDescription
) {
        public static TopicCreateRequest of(
                final String topicName,
                final String topicTag,
                final String topicDescription
        ) {
                return new TopicCreateRequest(topicName, topicTag, topicDescription);
        }
}
