package com.mile.moim.serivce.dto;

public record MoimAuthenticateResponse(
        boolean isMember
) {
    public static MoimAuthenticateResponse of(
            boolean isMember
    ) {
        return new MoimAuthenticateResponse(isMember);
    }
}
