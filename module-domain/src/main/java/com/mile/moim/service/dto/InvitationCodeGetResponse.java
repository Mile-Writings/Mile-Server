package com.mile.moim.service.dto;

public record InvitationCodeGetResponse(
    String invitationCode
) {
    public static InvitationCodeGetResponse of(
            final String invitationCode
    ) {
        return new InvitationCodeGetResponse(
                invitationCode
        );
    }
}
