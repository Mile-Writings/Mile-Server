package com.mile.curious.servIce.dto;

public record CuriousInfoResponse(boolean isCurious, Integer curiousCount) {
    public static CuriousInfoResponse of(
            final boolean isCurious,
            final Integer curiousCount
    ) {
        return new CuriousInfoResponse(isCurious, curiousCount);
    }
}
