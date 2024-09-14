package com.mile.moim.service.dto;

public record MoimIdValueDto<T>(
        Long moimId,
        T data
) {
    public static <T> MoimIdValueDto<T> of(
           final Long moimId,
           final T data
    ) {
        return new MoimIdValueDto<>(moimId, data);
    }
}
