package org.yandex.reports.date.utils;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * @author Shagbark
 *         16.07.2017
 */
public class DateRange {

    public final static String YEAR = "year";
    public final static String MONTH = "month";
    public static final String WEEK = "week";

    private Date lower;
    private Date upper;

    private DateRange(Date lower, Date upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public static DateRange newDateRange(String period) {
        switch (period) {
            case WEEK:
                return weekRange();
            case MONTH:
                return monthRange();
            case YEAR:
                return yearRange();
            default:
                throw new IllegalArgumentException("No such period - " + period);
        }
    }

    private static DateRange weekRange() {
        Calendar calendar = getInstance();
        // Set up lower date - start of current week
        calendar.set(DAY_OF_WEEK, Calendar.MONDAY);
        Date lower = DateUtils.getStartOfDay(calendar);

        // Set up upper date - end of current week
        calendar.set(DAY_OF_WEEK, Calendar.SUNDAY);
        Date upper = DateUtils.getEndOfDay(calendar);

        if (lower.after(upper)) {
            calendar.add(WEEK_OF_YEAR, -1);
            calendar.set(DAY_OF_WEEK, Calendar.MONDAY);
            lower = DateUtils.getStartOfDay(calendar);
        }

        return new DateRange(lower, upper);
    }

    private static DateRange monthRange() {
        Calendar calendar = getInstance();
        // lower date - start of current month
        calendar.set(DAY_OF_MONTH, 1);
        Date lower = DateUtils.getStartOfDay(calendar);

        // upper date - end of current month
        calendar.set(DAY_OF_MONTH, calendar.getActualMaximum(DAY_OF_MONTH));
        Date upper = DateUtils.getEndOfDay(calendar);

        return new DateRange(lower, upper);
    }

    private static DateRange yearRange() {
        Calendar calendar = getInstance();
        calendar.set(DAY_OF_YEAR, 1);
        Date lower = DateUtils.getStartOfDay(calendar);

        calendar.set(DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        Date upper = DateUtils.getEndOfDay(calendar);

        return new DateRange(lower, upper);
    }

    public Date getLower() {
        return lower;
    }

    public Date getUpper() {
        return upper;
    }

}
