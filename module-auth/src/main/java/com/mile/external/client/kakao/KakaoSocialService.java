package com.mile.external.client.kakao;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.external.client.SocialType;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.external.client.kakao.response.KakaoAccessTokenResponse;
import com.mile.external.client.kakao.response.KakaoUserResponse;
import com.mile.external.client.service.SocialService;
import com.mile.external.client.service.dto.UserInfoResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoSocialService implements SocialService {

    private static final String AUTH_CODE = "authorization_code";
    private static final String REDIRECT_URI = "https://www.milewriting.com/redirect-kakao";


    @Value("${kakao.clientId}")
    private String clientId;
    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthApiClient kakaoAuthApiClient;


    @Transactional
    @Override
    public UserInfoResponse login(
            final String authorizationCode,
            final UserLoginRequest loginRequest
    ) {
        String accessToken;
        try {
            // 인가 코드로 Access Token + Refresh Token 받아오기
            accessToken = getOAuth2Authentication(authorizationCode);
        } catch (FeignException e) {
            throw new BadRequestException(ErrorMessage.AUTHENTICATION_CODE_EXPIRED);
        }
        // Access Token으로 유저 정보 불러오기
        return getLoginDto(loginRequest.socialType(), getUserInfo(accessToken));
    }

    private String getOAuth2Authentication(
            final String authorizationCode
    ) {
        KakaoAccessTokenResponse response = kakaoAuthApiClient.getOAuth2AccessToken(
                AUTH_CODE,
                clientId,
                REDIRECT_URI,
                authorizationCode
        );
        return response.accessToken();
    }

    private KakaoUserResponse getUserInfo(
            final String accessToken
    ) {
        return kakaoApiClient.getUserInformation("Bearer " + accessToken);
    }

    private UserInfoResponse getLoginDto(
            final SocialType socialType,
            final KakaoUserResponse userResponse
    ) {
        return UserInfoResponse.of(
                userResponse.id(),
                socialType,
                userResponse.kakaoAccount().profile().accountEmail()
        );
    }

}

