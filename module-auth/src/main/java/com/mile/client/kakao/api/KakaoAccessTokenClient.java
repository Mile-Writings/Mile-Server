package com.mile.client.kakao.api;

import com.mile.client.kakao.api.dto.KakaoAccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoAccessTokenClient", url = "https://kauth.kakao.com")
public interface KakaoAccessTokenClient {
    @PostMapping(value = "/oauth/token")
    KakaoAccessTokenResponse getOAuth2AccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code
    );
}