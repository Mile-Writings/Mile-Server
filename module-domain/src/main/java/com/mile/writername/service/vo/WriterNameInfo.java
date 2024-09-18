package com.mile.writername.service.vo;

import com.mile.writername.domain.MoimRole;

public record WriterNameInfo(
        Long writerNameId,
        MoimRole moimRole
) {
    public static WriterNameInfo of(final Long writerNameId, final MoimRole moimRole) {
        return new WriterNameInfo(writerNameId, moimRole);
    }
}