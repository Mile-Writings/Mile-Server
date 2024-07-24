package com.mile.client.kakao.api;


import com.mile.client.kakao.api.dto.KakaoUserUnlinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoUnlinkClient", url = "https://kapi.kakao.com")
public interface KakaoUnlinkClient {

    @PostMapping(value = "/v1/user/unlink")
    KakaoUserUnlinkResponse revokeWithSocialService(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

}
