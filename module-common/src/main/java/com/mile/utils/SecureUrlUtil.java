package com.mile.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SecureUrlUtil {

    public String encodeUrl(final Long id) {
        return  Base64.getUrlEncoder().encodeToString(id.toString().getBytes());
    }

    public Long decodeUrl(final String url) {
        return Long.parseLong(new String(Base64.getUrlDecoder().decode(url)));
    }

}
