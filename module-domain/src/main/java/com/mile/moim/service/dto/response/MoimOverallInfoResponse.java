package com.mile.moim.service.dto.response;

public record MoimOverallInfoResponse(
        MoimInfoResponse infoResponse,
        MoimCuriousPostListResponse mostCuriousPost,
        MoimMostCuriousWriterResponse mostCuriousWriter
) {
}
