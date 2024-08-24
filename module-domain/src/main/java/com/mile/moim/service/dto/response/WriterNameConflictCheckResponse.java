package com.mile.moim.service.dto.response;

public record WriterNameConflictCheckResponse(
        boolean isConflict
) {

    public static WriterNameConflictCheckResponse of(boolean isConflict) {
        return new WriterNameConflictCheckResponse(isConflict);
    }
}
