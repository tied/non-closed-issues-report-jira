package org.yandex.reports

import org.yandex.reports.date.utils.DateUtils
import spock.lang.Specification

/**
 * @author Shagbark
 *         16.07.2017
 */
class DateUtilsTest extends Specification {

    //
    // Tests for method "getStartOfDay(Calendar calendar)"
    //

    def "getStartOfDay() - should get start of current day"() {
        expect:
            new Date().clearTime() == DateUtils.getStartOfDay(Calendar.getInstance())
    }

    def "getStartOfDay() - should throw exception, if calendar is null"() {
        when:
            DateUtils.getStartOfDay(null)
        then:
            thrown NullPointerException.class
    }

    //
    // Tests for method "getEndOfDay(Calendar calendar)"
    //

    def "getEndOfDay() - should get end of current day"() {
        given:
            Calendar calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
        expect:
            calendar.getTime() == DateUtils.getEndOfDay(Calendar.getInstance());
    }

    def "getEndOfDay() - should throw exception, if calendar is null"() {
        when:
            DateUtils.getEndOfDay(null)
        then:
            thrown NullPointerException.class
    }

}
