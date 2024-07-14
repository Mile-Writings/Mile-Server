package com.mile.strategy;

import com.mile.client.SocialType;
import com.mile.client.dto.UserLoginRequest;
import com.mile.strategy.dto.UserInfoResponse;

public interface LoginStrategy {

    UserInfoResponse login(final String authorizationCode, final UserLoginRequest loginRequest);
    default UserInfoResponse getLoginDto(final SocialType socialType, final String clientId, final String email) {
        return UserInfoResponse.of(clientId, socialType, email);
    }
    void revokeUser(final String authorizationCode,final UserLoginRequest userLoginRequest);
    SocialType getSocialType();
}
