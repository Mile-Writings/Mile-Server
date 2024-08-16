package com.mile.post.service.dto;

public record PostAuthenticateResponse(
        String role
) {
    public static PostAuthenticateResponse of(
            final String role
    ) {
        return new PostAuthenticateResponse(role);
    }
}
