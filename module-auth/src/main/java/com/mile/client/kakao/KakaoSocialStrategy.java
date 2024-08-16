package com.mile.client.kakao;

import com.mile.client.SocialType;
import com.mile.client.dto.UserLoginRequest;
import com.mile.client.kakao.api.KakaoAccessTokenClient;
import com.mile.client.kakao.api.KakaoUnlinkClient;
import com.mile.client.kakao.api.KakaoUserClient;
import com.mile.client.kakao.api.dto.KakaoAccessTokenResponse;
import com.mile.client.kakao.api.dto.KakaoUserResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.strategy.LoginStrategy;
import com.mile.strategy.dto.UserInfoResponse;
import feign.FeignException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class KakaoSocialStrategy implements LoginStrategy {

    private static final String AUTH_CODE = "authorization_code";
    @Value("${kakao.clientId}")
    private String clientId;
    private final KakaoAccessTokenClient kakaoAccessTokenClient;
    private final KakaoUserClient kakaoUserClient;
    private final KakaoUnlinkClient kakaoUnlinkClient;
    @Getter
    private final SocialType socialType = SocialType.KAKAO;



    @Transactional
    @Override
    public UserInfoResponse login(
            final String authorizationCode,
            final UserLoginRequest loginRequest
    ) {
        String accessToken;
        try {
            accessToken = getOAuth2Authentication(loginRequest.redirectUri(), authorizationCode);
        } catch (FeignException e) {
            throw new BadRequestException(ErrorMessage.AUTHENTICATION_CODE_EXPIRED);
        }
        KakaoUserResponse response = getUserInfo(accessToken);
        return getLoginDto(loginRequest.socialType(), response.id(), response.kakaoAccount().profile().accountEmail());
    }

    private String getOAuth2Authentication(
            final String redirectUri,
            final String authorizationCode
    ) {
        KakaoAccessTokenResponse response = kakaoAccessTokenClient.getOAuth2AccessToken(
                AUTH_CODE,
                clientId,
                redirectUri,
                authorizationCode
        );
        return "Bearer " + response.accessToken();
    }

    @Override
    public void revokeUser(final String authorizationCode, final UserLoginRequest userLoginRequest) {
        final String accessToken = getOAuth2Authentication(userLoginRequest.redirectUri(), authorizationCode);
        kakaoUnlinkClient.revokeWithSocialService(accessToken);
    }
    private KakaoUserResponse getUserInfo(
            final String accessToken
    ) {
        return kakaoUserClient.getUserInformation(accessToken);
    }

    public UserInfoResponse getLoginDto(
            final SocialType socialType,
            final Long socialId,
            final String email
    ) {
        return UserInfoResponse.of(
                socialId.toString(),
                socialType,
                email
        );
    }

}

