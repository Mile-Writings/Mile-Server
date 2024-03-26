package com.mile.moim.service.dto;

import com.mile.moim.domain.Moim;

public record MoimInvitationInfoResponse(
        String moimTitle,
        String imageUrl,
        String leader,
        int memberCount,
        String description
) {
    public static MoimInvitationInfoResponse of(
            final Moim moim,
            final int memberCount
    ) {
        return new MoimInvitationInfoResponse(moim.getName(), moim.getImageUrl(), moim.getOwner().getName(), memberCount, moim.getInformation());
    }
}
