package com.mile.post.service.dto;

public record WriterAuthenticateResponse(
        boolean canEdit
) {
    public static WriterAuthenticateResponse of(
            final boolean canEdit
    ) {
        return new WriterAuthenticateResponse(canEdit);
    }
}
