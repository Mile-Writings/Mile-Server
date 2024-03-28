package com.mile.topic.service.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record TopicPutRequest(
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
