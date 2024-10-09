package com.mile.common.utils.thread;

import com.mile.writername.service.vo.WriterNameInfo;

import java.util.HashMap;

public class WriterNameContextUtil {
    private static final ThreadLocal<Long> writerNameContext = new ThreadLocal<>();
    private static final ThreadLocal<HashMap<Long, WriterNameInfo>> moimWriterNameMapContext = new ThreadLocal<>();

    public static void setMoimWriterNameMapContext(HashMap<Long, WriterNameInfo> moimInfoMap) {
        moimWriterNameMapContext.set(moimInfoMap);
    }

    public static void setWriterNameIdContext(Long writerNameId) {
        writerNameContext.set(writerNameId);
    }

    public static Long getWriterNameContext() {
        return writerNameContext.get();
    }
    public static HashMap<Long, WriterNameInfo> getMoimWriterNameMapContext() {return moimWriterNameMapContext.get();}

    public static void clear() {
        writerNameContext.remove();
    }
}
