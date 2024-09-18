package com.mile.common.auth.thread;

public class WriterNameContext {
    private static final ThreadLocal<Long> writerNameContext = new ThreadLocal<>();

    public static void setWriterNameContext(Long writerNameId) {
        writerNameContext.set(writerNameId);
    }

    public static Long getWriterNameContext() {
        return writerNameContext.get();
    }

    public static void clear() {
        writerNameContext.remove();
    }
}
