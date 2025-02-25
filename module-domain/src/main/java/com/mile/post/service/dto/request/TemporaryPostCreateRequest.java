package com.mile.post.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TemporaryPostCreateRequest(
        @NotNull(message = "모임 Id가 입력되지 않았습니다.")
        @Schema(description = "모임 Id", example = "MQ==")
        String moimId,

        @NotNull(message = "글감 Id가 입력되지 않았습니다.")
        @Schema(description = "글감 Id", example = "NQ==")
        String topicId,

        @Size(max = 29, message = "제목 최대 글자를 초과했습니다.")
        @Schema(description = "글 제목", example = "편안한 글쓰기")
        String title,

        @Size(max = 10000, message = "내용 최대 글자를 초과했습니다.")
        @Schema(description = "글 내용", example = "내용입니다.")
        String content,

        @Schema(description = "이미지 url", example = "String https://")
        String imageUrl,

        @Schema(description = "익명 여부", example = "true")
        boolean anonymous
) {
}