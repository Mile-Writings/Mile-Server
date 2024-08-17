package com.mile.writername.service.dto;

import jakarta.validation.constraints.Size;

public record WriterNameDescriptionUpdateRequest(
        
        @Size(max = 110, message = "소개 글은 최대 110자 이내로 작성해주세요.")
        String description
) {
}
