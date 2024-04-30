package com.mile.user.service.dto;

public record LoginSuccessResponse(
        String accessToken,
        String refreshToken
) {
    public static LoginSuccessResponse of(
            final String accessToken,
            final String refreshToken
    ) {
        return new LoginSuccessResponse(accessToken, refreshToken);
    }
}