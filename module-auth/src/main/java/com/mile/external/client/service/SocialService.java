package com.mile.external.client.service;

import com.mile.external.client.dto.UserLoginRequest;
import com.mile.external.client.service.dto.UserInfoResponse;

public abstract class SocialService {
    protected abstract UserInfoResponse login(final String authorizationToken, final UserLoginRequest loginRequest);

}
