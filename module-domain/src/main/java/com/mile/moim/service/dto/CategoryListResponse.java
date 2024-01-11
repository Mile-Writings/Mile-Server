package com.mile.moim.serivce.dto;

import com.mile.topic.serivce.dto.CategoryResponse;
import java.util.List;

public record CategoryListResponse(List<CategoryResponse> categoryList) {
    public static CategoryListResponse of(final List<CategoryResponse> categoryList) {
        return new CategoryListResponse(categoryList);
    }
}

