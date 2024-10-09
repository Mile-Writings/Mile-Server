package com.mile.util;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.writername.service.vo.WriterNameInfo;

import java.util.HashMap;

public class MoimWriterNameMapUtil {

    public static Long getWriterNameIdMoimWriterNameMap(
            final Long moimId,
            final HashMap<Long, WriterNameInfo> moimWriterInfoMap
    ) {
        if(!moimWriterInfoMap.containsKey(moimId)) {
            throw new ForbiddenException(ErrorMessage.WRITER_NAME_NON_AUTHENTICATE);
        }
        return moimWriterInfoMap.get(moimId).writerNameId();
    }

    public static void authenticateWriterName(
            final Long moimId,
            final HashMap<Long, WriterNameInfo> moimWriterInfoMap
    ) {
        if(!moimWriterInfoMap.containsKey(moimId)) {
            throw new ForbiddenException(ErrorMessage.WRITER_NAME_NON_AUTHENTICATE);
        }
    }
}
