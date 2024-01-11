package com.mile.moim.service.dto;

public record MoimAuthenticateResponse(
        boolean isMember
) {
    public static MoimAuthenticateResponse of(
            final boolean isMember
    ) {
        return new MoimAuthenticateResponse(isMember);
    }
}
