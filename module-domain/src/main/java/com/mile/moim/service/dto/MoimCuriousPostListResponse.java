package com.mile.moim.service.dto;


import java.util.List;

public record MoimCuriousPostListResponse(
        List<MoimMostCuriousPostResponse> postList
) {
    public static MoimCuriousPostListResponse of(
            final List<MoimMostCuriousPostResponse> postList
    ) {
        return new MoimCuriousPostListResponse(postList);
    }
}
