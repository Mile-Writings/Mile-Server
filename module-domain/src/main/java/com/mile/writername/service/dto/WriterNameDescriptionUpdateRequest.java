package com.mile.writername.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WriterNameDescriptionUpdateRequest(
        @NotBlank(message = "소개 글이 입력되지 않았습니다.")
        @Size(max = 110, message = "소개 글은 최대 110자 이내로 작성해주세요.")
        String description
) {
}
