package com.mile.post.service.dto.response;

public record PostAuthenticateResponse(
        String role
) {
    public static PostAuthenticateResponse of(
            final String role
    ) {
        return new PostAuthenticateResponse(role);
    }
}
