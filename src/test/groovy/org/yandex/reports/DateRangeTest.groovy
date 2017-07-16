package org.yandex.reports

import org.yandex.reports.config.TestDateObjects
import org.yandex.reports.date.utils.DateRange
import spock.lang.Specification
/**
 * @author Shagbark
 *         16.07.2017
 */
class DateRangeTest extends Specification {

    def "newDateRange() - current week"() {
        when:
            DateRange result = DateRange.newDateRange(DateRange.WEEK)
        then:
            result.lower == TestDateObjects.getWeekStart()
        and:
            result.upper == TestDateObjects.getWeekEnd()
    }

    def "newDateRange() - current month"() {
        when:
            DateRange result = DateRange.newDateRange(DateRange.MONTH)
        then:
            result.lower == TestDateObjects.getStartOfMonth()
        and:
            result.upper == TestDateObjects.getEndOfMonth()
    }

    def "newDateRange() - current year"() {
        when:
            DateRange result = DateRange.newDateRange(DateRange.YEAR)
        then:
            result.lower == TestDateObjects.getStartOfYear()
        and:
            result.upper == TestDateObjects.getEndOfYear()
    }

}
