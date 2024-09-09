package com.mile.common.utils;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SecureUrlUtil {

    public String encodeUrl(final Long id) {
        return Base64.getUrlEncoder().encodeToString(id.toString().getBytes());
    }

    public Long decodeUrl(final String url) {
        try {
            return Long.parseLong(new String(Base64.getUrlDecoder().decode(url)));
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(ErrorMessage.INVALID_URL_EXCEPTION);
        }
    }

    public Long decodeIfNotNull(final String url) {
        return url == null ? null : decodeUrl(url);
    }

}
