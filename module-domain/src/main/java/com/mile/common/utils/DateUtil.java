package com.mile.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {

    private static final String YEAR = "년";
    private static final String MONTH = "월";
    private static final String DAY = "일";
    private static final String STRING_DATE_WITH_TIME = "yy.MM.dd HH:mm";
    private static final String TIME_FORMAT = "HH:mm";
    private static final String STRING_DATE = "yy-MM-dd";
    private static final String DOT_STRING_DATE = "yy.MM.dd";
    private static final String YEAR_DATE_STRING = "yyyy-MM-dd";

    /*
        yyyy년 MM월 dd일 hh:mm 이 필요할 때 사용
     */

    public static String getKoreanStringOfLocalDateWithTime(
            final LocalDateTime localDateTime
    ) {
        return getKoreanStringWithTime(localDateTime);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DOT_STRING_DATE);
        return localDateTime.format(formatter);
    }
    /*
           yy.MM.dd 이 필요할 때 사용
    */
    public static String getStringDateOfDotFormat(
            final LocalDateTime localDateTime
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(STRING_DATE);
        return localDateTime.format(formatter);
    }

    /*
    YYYY-MM-dd 가 필요할 때 사용
     */
    public static String getStringDateWithYear(
            final LocalDateTime localDateTime
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YEAR_DATE_STRING);
        return localDateTime.format(formatter);
    }

    private static String getKoreanStringWithTime(
            final LocalDateTime localDateTime
    ) {
        return getKoreanString(localDateTime) + " " + localDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }


    private static String getKoreanString(
            final LocalDateTime localDateTime
    ) {
        return getYearStringValueOfLocalDate(localDateTime) + YEAR + " "
                + getMonthStringValueOfLocalDate(localDateTime) + MONTH + " "
                + getDayOfMonthStringValueOfLocalDate(localDateTime) + DAY;
    }

    private static String getYearStringValueOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        return String.valueOf(localDateTime.getYear());
    }

    private static String getMonthStringValueOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        return String.valueOf(localDateTime.getMonthValue());
    }

    private static String getDayOfMonthStringValueOfLocalDate(
            final LocalDateTime localDateTime
    ) {
        return String.valueOf(localDateTime.getDayOfMonth());
    }

}
