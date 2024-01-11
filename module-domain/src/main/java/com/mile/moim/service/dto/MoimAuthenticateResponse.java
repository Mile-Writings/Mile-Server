package com.mile.moim.serivce.dto;

public record MoimAuthenticateResponse(
        boolean isMember
) {
    public static MoimAuthenticateResponse of(
            final boolean isMember
    ) {
        return new MoimAuthenticateResponse(isMember);
    }
}
