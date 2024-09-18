package com.mile.moim.service.dto;

public record MoimIdValueDto<T>(
        Long moimId,
        Long writerNameId,
        T data
) {
    public static <T> MoimIdValueDto<T> of(
            final Long moimId,
            final Long writerNameId,
            final T data
    ) {
        return new MoimIdValueDto<>(moimId, writerNameId, data);
    }
}
