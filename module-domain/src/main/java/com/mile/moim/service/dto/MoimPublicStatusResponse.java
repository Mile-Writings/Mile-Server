package com.mile.moim.service.dto;

public record MoimPublicStatusResponse(
        boolean isPublic
) {
    public static MoimPublicStatusResponse of(
            final boolean isPublic
    ) {
        return new MoimPublicStatusResponse(isPublic);
    }
}

