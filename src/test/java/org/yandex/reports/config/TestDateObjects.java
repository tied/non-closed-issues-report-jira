package org.yandex.reports.config;

import org.yandex.reports.date.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * @author Shagbark
 *         16.07.2017
 */
public class TestDateObjects {

    private TestDateObjects() {}

    public static Date getWeekStart() {
        Calendar calendar = getInstance();
        calendar.set(DAY_OF_WEEK, Calendar.MONDAY);
        return DateUtils.getStartOfDay(calendar);
    }

    public static Date getWeekEnd() {
        Calendar calendar = getInstance();
        calendar.set(DAY_OF_WEEK, Calendar.SUNDAY);
        return DateUtils.getEndOfDay(calendar);
    }

    public static Date getStartOfYear() {
        Calendar calendar = getInstance();
        calendar.set(DAY_OF_YEAR, 1);
        return DateUtils.getStartOfDay(calendar);
    }

    public static Date getEndOfYear() {
        Calendar calendar = getInstance();
        calendar.set(DAY_OF_YEAR, calendar.getActualMaximum(DAY_OF_YEAR));
        return DateUtils.getEndOfDay(calendar);
    }

    public static Date getStartOfMonth() {
        Calendar calendar = getInstance();
        calendar.set(DAY_OF_MONTH, 1);
        return DateUtils.getStartOfDay(calendar);
    }

    public static Date getEndOfMonth() {
        Calendar calendar = getInstance();
        calendar.set(DAY_OF_MONTH, calendar.getActualMaximum(DAY_OF_MONTH));
        return DateUtils.getEndOfDay(calendar);
    }

}
