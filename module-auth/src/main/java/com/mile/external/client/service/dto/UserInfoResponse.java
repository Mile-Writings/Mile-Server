package com.mile.external.client.service.dto;

import com.mile.external.client.SocialType;

public record UserInfoResponse(
        Long socialId,
        SocialType socialType,
        String email
) {
    public static UserInfoResponse of(
            final Long socialId,
            final SocialType socialType,
            final String email
    ) {
        return new UserInfoResponse(socialId, socialType, email);
    }
}