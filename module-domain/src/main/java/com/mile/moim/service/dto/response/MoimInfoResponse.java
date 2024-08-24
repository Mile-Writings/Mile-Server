package com.mile.moim.service.dto.response;

public record MoimInfoResponse(
        String imageUrl,
        String moimName,
        String ownerName,
        String description,
        int writerCount,
        String startDate
) {
    public static MoimInfoResponse of(
            final String imageUrl,
            final String moimName,
            final String ownerName,
            final String description,
            final int writerCount,
            final String startDate
    ) {
        return new MoimInfoResponse(imageUrl, moimName, ownerName, description, writerCount, startDate);
    }
}
