package com.mile.moim.service.dto.response;

public record MoimNameConflictCheckResponse(
    boolean isValidate
) {
    public static MoimNameConflictCheckResponse of(
            boolean isValidate
    ) {
        return new MoimNameConflictCheckResponse(isValidate);
    }
}
