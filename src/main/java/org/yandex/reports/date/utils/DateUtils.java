package org.yandex.reports.date.utils;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * @author Shagbark
 *         16.07.2017
 */
public class DateUtils {

    private DateUtils() {}

    /**
     * Returns {@link Date} instance for passed day with time - 00:00:00:000.
     *
     * @param calendar {@link Calendar} instance
     * @return {@link Date} which has time - 00:00:00:000.
     */
    public static Date getStartOfDay(Calendar calendar) {
        setTime(calendar, 0, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * Returns {@link Date} instance for passed day with time - 23:59:59:999.
     *
     * @param calendar {@link Calendar} instance
     * @return {@link Date} which has time - 23:59:59:999
     */
    public static Date getEndOfDay(Calendar calendar) {
        setTime(calendar, 23, 59, 59, 999);
        return calendar.getTime();
    }

    /**
     * Sets the fields of time for passed {@link Calendar} calendar.
     *
     * @param calendar calendar, where need to setup time fileds
     * @param hour hour of day
     * @param minutes minutes
     * @param seconds seconds
     * @param milliseconds milliseconds
     */
    private static void setTime(Calendar calendar, int hour, int minutes, int seconds, int milliseconds) {
        calendar.set(HOUR_OF_DAY, hour);
        calendar.set(MINUTE, minutes);
        calendar.set(SECOND, seconds);
        calendar.set(MILLISECOND, milliseconds);
    }

}
