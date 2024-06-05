package com.mile.commentreply.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReplyCreateRequest(
        @NotBlank(message = "대댓글이 비어있습니다.")
        @Size(max = 1500, message = "대댓글은 1500자를 넘을 수 없습니다.")
        String content,
        @NotNull(message = "익명 여부가 비어있습니다.")
        boolean isAnonymous
) {
}
