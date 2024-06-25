package com.mile.strategy.dto;

import com.mile.external.client.SocialType;

public record UserInfoResponse(
        String socialId,
        SocialType socialType,
        String email
) {
    public static UserInfoResponse of(
            final String socialId,
            final SocialType socialType,
            final String email
    ) {
        return new UserInfoResponse(socialId, socialType, email);
    }
}