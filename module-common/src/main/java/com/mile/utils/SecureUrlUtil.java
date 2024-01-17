package com.mile.utils;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SecureUrlUtil {

    public String encodeUrl(final Long id) {
        return  Base64.getUrlEncoder().encodeToString(id.toString().getBytes());
    }

    public Long decodeUrl(final String url) {
        checkURLValue(url);
        return Long.parseLong(new String(Base64.getUrlDecoder().decode(url)));
    }

    private void checkURLValue(final String url) {
        if(url.length() < 4) {
            throw new BadRequestException(ErrorMessage.PATH_PARAMETER_INVALID_ERROR);
        }
    }

}
