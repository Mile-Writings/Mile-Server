package com.mile.moim.service.dto.response;

public record MoimCreateResponse(
        String moimId,
        String inviteCode
) {
    public static MoimCreateResponse of(
            final String moimId,
            final String inviteCode
    ) {
        return new MoimCreateResponse(moimId, inviteCode);
    }

}
