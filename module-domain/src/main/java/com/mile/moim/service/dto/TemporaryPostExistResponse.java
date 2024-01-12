package com.mile.moim.service.dto;

public record TemporaryPostExistResponse(
        boolean isTemporaryPostExist,
        Long postId
) {
    public static TemporaryPostExistResponse of(
            final boolean isTemporaryPostExist,
            final Long postId
    ) {
        return new TemporaryPostExistResponse(isTemporaryPostExist, postId);
    }
}
