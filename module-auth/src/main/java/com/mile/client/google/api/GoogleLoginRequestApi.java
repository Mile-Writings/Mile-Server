package com.mile.client.google.api;


import com.mile.client.google.api.dto.GoogleAccessTokenResponse;
import com.mile.client.google.api.dto.GoogleUserInfoResponse;
import feign.form.ContentType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleUserClient", url = "https://www.googleapis.com")
public interface GoogleRequestApi {
    @PostMapping("/token")
    GoogleAccessTokenResponse getAccessToken(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "clientId") String clientId,
            @RequestParam(name = "clientSecret") String clientSecret,
            @RequestParam(name = "redirectUri") String redirectUri,
            @RequestParam(name = "grantType") String grantType
    );

    @GetMapping("/userinfo/v2/me")
    GoogleUserInfoResponse getGoogleUserInfo(@RequestParam(value = "access_token") final String accessToken);

    @PostMapping(value = "/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void revokeUserFromSocialService(@RequestParam(value = "token") final String accessToken);


}
