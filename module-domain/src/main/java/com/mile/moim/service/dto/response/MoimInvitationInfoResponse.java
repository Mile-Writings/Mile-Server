package com.mile.moim.service.dto.response;

import com.mile.moim.domain.Moim;
import com.mile.utils.DateUtil;

public record MoimInvitationInfoResponse(
        String moimTitle,
        String imageUrl,
        String leader,
        String foundedDate,
        int memberCount,
        String description
) {
    public static MoimInvitationInfoResponse of(
            final Moim moim,
            final int memberCount
    ) {
        return new MoimInvitationInfoResponse(moim.getName(), moim.getImageUrl(), moim.getOwner().getName(),
                DateUtil.getStringDateOfDotFormat(moim.getCreatedAt()), memberCount, moim.getInformation());
    }
}
