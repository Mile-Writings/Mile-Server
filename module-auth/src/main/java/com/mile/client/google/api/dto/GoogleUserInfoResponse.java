package com.mile.client.google.api.dto;

public record GoogleUserInfoResponse(
        String id,
        String email,
        String picture
) {
}
