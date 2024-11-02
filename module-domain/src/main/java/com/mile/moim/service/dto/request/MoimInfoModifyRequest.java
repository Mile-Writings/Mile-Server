package com.mile.moim.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MoimInfoModifyRequest(

        @NotBlank(message = "모임 제목이 비어져 있습니다.")
        @Size(max = 10, message = " 글모임 이름은 최대 10 글자 이내로 작성해주세요.")
        String moimTitle,

        @Size(max = 100, message = "글모임 설명은 최대 100 글자 이내로 작성해주세요.")
        String description,

        String imageUrl,

        @NotNull(message = "입력 값이 비어있습니다.")
        boolean isPublic
) {
}
