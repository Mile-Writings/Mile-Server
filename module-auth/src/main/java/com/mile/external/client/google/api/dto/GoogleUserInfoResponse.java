package com.mile.external.client.google.api.dto;

public record GoogleUserInfoResponse(
        String id,
        String email,
        String picture
) {
}
