package com.mile.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SecureUrlUtil {

    public String encodeUrl(Long meetingId) {
        return  Base64.getUrlEncoder().encodeToString(meetingId.toString().getBytes());
    }

    public Long decodeUrl(String url) {
        return Long.parseLong(new String(Base64.getUrlDecoder().decode(url)));
    }

}
