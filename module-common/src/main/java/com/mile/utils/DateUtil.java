package com.mile.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {

    private static final String YEAR = "년";
    private static final String MONTH = "월";
    private static final String DAY = "일";
    private static final String STRING_DATE_WITH_TIME = "yy.MM.dd HH:mm";
    private static final String STRING_DATE = "yy-MM-dd";

    /*
        yyyy년 MM월 dd일 이 필요할 때 사용
    */
    public static String getKoreanStringOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        return getKoreanString(localDateTime);
    }

    /*
        yy.MM.dd HH:mm 이 필요할 때 사용
    */
    public static String getStringWithTimeOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(STRING_DATE_WITH_TIME);
        return localDateTime.format(formatter);
    }

    /*
           yy-MM-dd 이 필요할 때 사용
    */
    public static String getStringDateOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(STRING_DATE);
        return localDateTime.format(formatter);
    }

    private static String getKoreanString(
            final LocalDateTime localDateTime
    ) {
        return getYearStringValueOfLocalDate(localDateTime) + YEAR
                + getMonthStringValueOfLocalDate(localDateTime) + MONTH
                + getDayOfMonthStringValueOfLocalDate(localDateTime) + DAY;
    }

    private static String getYearStringValueOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        return localDateTime.getMonth().toString();
    }

    private static String getMonthStringValueOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        return localDateTime.getMonth().toString();
    }

    private static String getDayOfMonthStringValueOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        return String.valueOf(localDateTime.getDayOfMonth());
    }
}
