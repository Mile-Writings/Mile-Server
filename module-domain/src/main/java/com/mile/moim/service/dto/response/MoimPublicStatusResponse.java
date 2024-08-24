package com.mile.moim.service.dto.response;

public record MoimPublicStatusResponse(
        boolean isPublic
) {
    public static MoimPublicStatusResponse of(
            final boolean isPublic
    ) {
        return new MoimPublicStatusResponse(isPublic);
    }
}

