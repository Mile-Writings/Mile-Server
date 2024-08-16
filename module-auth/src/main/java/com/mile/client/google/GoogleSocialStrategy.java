package com.mile.client.google;

import com.mile.client.SocialType;
import com.mile.client.dto.UserLoginRequest;
import com.mile.client.google.api.GoogleAccessTokenClient;
import com.mile.client.google.api.GoogleRevokeClient;
import com.mile.client.google.api.GoogleUserClient;
import com.mile.client.google.api.dto.GoogleUserInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.strategy.LoginStrategy;
import com.mile.strategy.dto.UserInfoResponse;
import feign.FeignException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GoogleSocialStrategy implements LoginStrategy {

    private final GoogleAccessTokenClient googleAccessTokenClient;
    private final GoogleRevokeClient googleRevokeClient;
    private final GoogleUserClient googleUserClient;
    @Getter
    private final SocialType socialType = SocialType.GOOGLE;

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${google.clientId}")
    private String clientId;
    @Value("${google.clientSecret}")
    private String clientSecret;

    @Override
    public UserInfoResponse login(final String authorizationCode, final UserLoginRequest loginRequest) {
        String accessToken = "";
        try {
            accessToken = getOAuth2Authentication(authorizationCode, loginRequest.redirectUri());
        } catch (FeignException e) {
            throw new BadRequestException(ErrorMessage.AUTHENTICATION_CODE_EXPIRED);
        }
        GoogleUserInfoResponse response = getGoogleUserInfo(accessToken);
        return getLoginDto(loginRequest.socialType(), response.id(), response.email());
    }

    private String getOAuth2Authentication(
            final String authorizationCode,
            final String redirectUri
    ) {
        return googleAccessTokenClient.getAccessToken(
                authorizationCode,
                clientId,
                clientSecret,
                redirectUri,
                GRANT_TYPE
        ).accessToken();
    }

    private GoogleUserInfoResponse getGoogleUserInfo(
            final String accessToken
    ) {
        return googleUserClient.getGoogleUserInfo( accessToken);
    }

    @Override
    public void revokeUser(final String authorizationCode, final UserLoginRequest userLoginRequest) {
        final String accessToken = getOAuth2Authentication(authorizationCode, userLoginRequest.redirectUri());
        googleRevokeClient.revokeUserFromSocialService(accessToken);
    }
}
