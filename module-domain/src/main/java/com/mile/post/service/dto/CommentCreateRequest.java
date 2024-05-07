package com.mile.post.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(

        @Schema(description = "댓글 내용", example = "댓글 내용을 입력해주세요")
        @NotBlank(message = "댓글에 내용이 없습니다.")
        @Size(max = 500, message = "댓글 최대 입력 길이(500자)를 초과하였습니다.")
        String content,

        @NotNull(message = "익명 여부가 입력되지 않았습니다.")
        boolean isAnonymous
) {
}
