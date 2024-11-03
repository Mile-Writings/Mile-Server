package com.mile.common.log;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.slf4j.MDC;

import java.util.Locale;

public class P6spySqlFormat implements MessageFormattingStrategy {
    private final String MDC_KEY = "SQL_START";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        return MDC.get(MDC_KEY) + "|" + sql;
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().equals("")) return sql;

        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }

        return sql;
    }
}