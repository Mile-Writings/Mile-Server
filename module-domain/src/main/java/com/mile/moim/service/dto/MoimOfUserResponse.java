package com.mile.moim.service.dto;

import com.mile.moim.domain.Moim;

public record MoimOfUserResponse(
        String moimName,
        String moimId
) {
    public static MoimOfUserResponse of(
            final Moim moim
    ) {
        return new MoimOfUserResponse(
                moim.getName(),
                moim.getIdUrl()
        );
    }
}
