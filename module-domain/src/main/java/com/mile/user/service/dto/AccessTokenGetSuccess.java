package com.mile.user.service.dto;

public record AccessTokenGetSuccess(
        String accessToken
) {
    public static AccessTokenGetSuccess of(
            final String accessToken
    ) {
        return new AccessTokenGetSuccess(accessToken);
    }
}
