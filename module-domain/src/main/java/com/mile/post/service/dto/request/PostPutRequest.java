package com.mile.post.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostPutRequest(

        @NotNull(message = "글감 id가 입력되지 않았습니다.")
        @Schema(description = "글감 Id", example = "NQ==")
        String topicId,

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 34, message = "제목 최대 글자를 초과했습니다.")
        @Schema(description = "글 제목", example = "편안한 글쓰기")
        String title,


        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 10000, message = "내용 최대 글자를 초과했습니다.")
        @Schema(description = "글 내용", example = "내용입니다.")
        String content,

        @NotBlank(message = "이미지 url을 입력해주세요.")
        @Schema(description = "이미지 url", example = "String https://")
        String imageUrl,

        @NotNull(message = "익명 여부를 입력해주세요.")
        @Schema(description = "익명 여부", example = "true")
        boolean anonymous
) {
}
