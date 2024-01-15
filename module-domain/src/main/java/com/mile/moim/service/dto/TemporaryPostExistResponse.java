package com.mile.moim.service.dto;

public record TemporaryPostExistResponse(
        boolean isTemporaryPostExist,
        String postId
) {
    public static TemporaryPostExistResponse of(
            final boolean isTemporaryPostExist,
            final String postId
    ) {
        return new TemporaryPostExistResponse(isTemporaryPostExist, postId);
    }
}
