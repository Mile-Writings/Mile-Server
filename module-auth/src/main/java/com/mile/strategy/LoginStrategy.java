package com.mile.strategy;

import com.mile.external.client.SocialType;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.strategy.dto.UserInfoResponse;

public interface LoginStrategy {

    UserInfoResponse login(final String authorizationCode, final UserLoginRequest loginRequest);

    default UserInfoResponse getLoginDto(final SocialType socialType, final String clientId, final String email) {
        return UserInfoResponse.of(clientId, socialType, email);
    }

    SocialType getSocialType();
}
