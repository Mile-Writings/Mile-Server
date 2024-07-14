package com.mile.client.kakao.api;

import com.mile.client.kakao.api.dto.KakaoAccessTokenResponse;
import com.mile.client.kakao.api.dto.KakaoUserResponse;
import com.mile.client.kakao.api.dto.KakaoUserUnlinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoRequestApi", url = "https://kauth.kakao.com")
public interface KakaoRequestApi {
    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoAccessTokenResponse getOAuth2AccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code
    );

    @GetMapping(value = "/v2/user/me")
    KakaoUserResponse getUserInformation(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

    @PostMapping(value = "/unlink")
    KakaoUserUnlinkResponse revokeWithSocialService(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}