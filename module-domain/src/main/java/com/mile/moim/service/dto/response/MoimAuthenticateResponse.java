package com.mile.moim.service.dto.response;

public record MoimAuthenticateResponse(
        boolean isMember,
        boolean isOwner
) {
    public static MoimAuthenticateResponse of(
            final boolean isMember,
            final boolean isOwner
    ) {
        return new MoimAuthenticateResponse(isMember, isOwner);
    }
}
