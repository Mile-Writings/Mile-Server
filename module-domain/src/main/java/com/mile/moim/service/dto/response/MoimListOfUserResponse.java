package com.mile.moim.service.dto.response;

import java.util.List;

public record MoimListOfUserResponse(
        List<MoimOfUserResponse> moims
) {
    public static MoimListOfUserResponse of (
            final List<MoimOfUserResponse> moims
    ) {
        return new MoimListOfUserResponse(
            moims
        );
    }
}
