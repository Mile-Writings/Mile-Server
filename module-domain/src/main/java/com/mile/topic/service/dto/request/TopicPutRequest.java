package com.mile.topic.service.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TopicPutRequest(
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
