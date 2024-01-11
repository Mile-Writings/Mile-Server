package com.mile.moim.service.dto;


import java.util.List;

public record MoimCuriousPostListResponse(
        List<MoimMostCuriousPostResponse> postList
) {
    public static MoimCuriousPostListResponse of(
            List<MoimMostCuriousPostResponse> postList
    ) {
        return new MoimCuriousPostListResponse(postList);
    }
}
