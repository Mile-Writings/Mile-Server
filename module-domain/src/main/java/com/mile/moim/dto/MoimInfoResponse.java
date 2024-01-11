package com.mile.moim.dto;

public record MoimInfoResponse(

        String imageUrl,
        String moimName,
        String ownerName,
        String startDate,
        int writerCount,
        String description
) {
    public static MoimInfoResponse of(
            final String imageUrl,
            final String moimName,
            final String ownerName,
            final String startDate,
            final int writerCount,
            final String description
    ) {
        return new MoimInfoResponse(imageUrl, moimName, ownerName, startDate, writerCount, description);
    }
}
