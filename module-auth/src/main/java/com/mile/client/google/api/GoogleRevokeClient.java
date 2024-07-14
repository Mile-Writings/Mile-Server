package com.mile.client.google.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleRevokeClient", url = "https://oauth2.googleapis.com")
public interface GoogleRevokeClient {

    @PostMapping(value = "/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void revokeUserFromSocialService(@RequestParam(value = "token") final String accessToken);

}
