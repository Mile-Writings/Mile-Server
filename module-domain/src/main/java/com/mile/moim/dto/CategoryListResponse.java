package com.mile.moim.dto;

import com.mile.topic.service.dto.CategoryResponse;
import java.util.List;

public record CategoryListResponse(List<CategoryResponse> categoryList) {
    public static CategoryListResponse of(final List<CategoryResponse> categoryList) {
        return new CategoryListResponse(categoryList);
    }
}

