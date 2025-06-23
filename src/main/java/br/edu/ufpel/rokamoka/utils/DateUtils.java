package br.edu.ufpel.rokamoka.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mauriciomucci
 */
public final class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_MILLIS_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private DateUtils() {
        throw new IllegalStateException("DateUtils is a utility class; thus, it should not be instantiated");
    }

    public static String formatDate(long timestamp) {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date(timestamp));
    }

    public static String formatDateTime(long timestamp) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(timestamp));
    }

    public static String formatDateTimeMillis(long timestamp) {
        return new SimpleDateFormat(DATE_TIME_MILLIS_FORMAT).format(new Date(timestamp));
    }
}
