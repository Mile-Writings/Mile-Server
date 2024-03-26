package com.mile.moim.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MoimInfoModifyRequest(
        @Max(value = 10, message = " 글모임 이름은 최대 10 글자 이내로 작성해주세요.")
        String moimTitle,
        @Max(value = 100, message = "글모임 이름은 최대 10 글자 이내로 작성해주세요.")
        String description,

        String imageUrl,

        @NotNull(message = "입력 값이 비어있습니다.")
        boolean isPublic
) {
}
