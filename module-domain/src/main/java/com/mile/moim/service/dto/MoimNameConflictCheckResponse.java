package com.mile.moim.service.dto;

public record MoimNameConflictCheckResponse(
    boolean isValidate
) {
    public static MoimNameConflictCheckResponse of(
            boolean isValidate
    ) {
        return new MoimNameConflictCheckResponse(isValidate);
    }
}
