package com.mile.moim.service.dto;

import com.mile.moim.domain.Moim;

public record MoimInfoOwnerResponse(
        String moimTitle,
        String description,
        String imageUrl,
        boolean isPublic
) {

    public static MoimInfoOwnerResponse of(
            final Moim moim
    ) {
        return new MoimInfoOwnerResponse(moim.getName(), moim.getInformation(), moim.getImageUrl(), moim.isPublic());
    }
}