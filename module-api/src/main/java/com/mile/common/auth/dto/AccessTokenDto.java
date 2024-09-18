package com.mile.common.auth.dto;

public record AccessTokenDto<T>(
        String accessToken,
        T response
) {
    public static <T> AccessTokenDto<T> of(final T data, final String accessToken) {
        return new AccessTokenDto<>(accessToken, data);
    }
    public static AccessTokenDto of(final String accessToken) {
        return new AccessTokenDto(accessToken, null);
    }
}
