package com.mile.moim.service.dto;

public record MoimCreateResponse(
        String moimId,
        String inviteCode
) {
    public static MoimCreateResponse of(
            String moimId,
            String inviteCode
    ) {
        return new MoimCreateResponse(moimId, inviteCode);
    }
}
