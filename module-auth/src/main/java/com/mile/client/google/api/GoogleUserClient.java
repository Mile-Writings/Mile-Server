package com.mile.client.google.api;


import com.mile.client.google.api.dto.GoogleUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleUserClient", url = "https://www.googleapis.com")
public interface GoogleUserClient {

    @GetMapping("/userinfo/v2/me")
    GoogleUserInfoResponse getGoogleUserInfo(@RequestParam(value = "access_token") final String accessToken);
}
