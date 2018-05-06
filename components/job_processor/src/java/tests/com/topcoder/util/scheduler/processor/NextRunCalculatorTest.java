/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Day;
import com.topcoder.util.scheduler.scheduling.DayOfMonth;
import com.topcoder.util.scheduler.scheduling.DayOfYear;
import com.topcoder.util.scheduler.scheduling.DaysOfWeek;
import com.topcoder.util.scheduler.scheduling.Hour;
import com.topcoder.util.scheduler.scheduling.Minute;
import com.topcoder.util.scheduler.scheduling.Month;
import com.topcoder.util.scheduler.scheduling.Second;
import com.topcoder.util.scheduler.scheduling.Week;
import com.topcoder.util.scheduler.scheduling.WeekMonthOfYear;
import com.topcoder.util.scheduler.scheduling.WeekOfMonth;
import com.topcoder.util.scheduler.scheduling.Year;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Test NextRunCalculator. NextRunCalculator is used as a helper class in this component, though the most complex
 * date scheduling algorithm is involved in it. This test will cover those scenarios for all the DateUnit subclasses,
 * the next run time should be calculated correctly as expected.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class NextRunCalculatorTest extends TestCase {
    /**
     * The error tolerant in this test. Due to the CPU timing reason, the calculated run time may not be exact
     * as what is expected(may larger), so a number of milliseconds is used to evaluated the error. The difference
     * between expected date and calculated one should not larger than this value.
     */
    private static final int ERROR = 50;

    /**
     * Test calculating with Day.
     */
    public void testNextRunDay() {
        DateUnit dateUnit = new Day();
        int interval = 2;

        testSimpleDateUnit(dateUnit, interval);
    }

    /**
     * Test calculating with DayOfMonth. In this scenario, DateUnit is set to the 10th day every other month,
     * the start time is Monday Jan 1 2007, thus the available dates should be Jan 10, 2007 and Feb 10, 2007.
     */
    public void testNextRunDayOfMonth() {
        //the 10th day every other month
        DateUnit dateUnit = new DayOfMonth(10);
        int interval = 2;

        int field = getField(dateUnit);
        GregorianCalendar startDate = getJan12007();
        GregorianCalendar rightNow = (GregorianCalendar) startDate.clone();

        GregorianCalendar nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);
        GregorianCalendar expectedDate = (GregorianCalendar) startDate.clone();
        expectedDate.set(Calendar.DAY_OF_MONTH, 10);

        //next run should be the Jan 10, 2007
        assertDateIsExpected(expectedDate, nextRun);

        //time elapses, but not exceeds the next interval, and recalculates the next run
        rightNow.add(field, interval - 1);
        nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the next run should be the next interval since start, Mar 10, 2007
        expectedDate.add(field, interval);

        //assert
        assertDateIsExpected(expectedDate, nextRun);
    }

    /**
     * Test calculating with DayOfYear. In this scenario, DateUnit is set to the 10th day every other year, the
     * start time is Monday Jan 1 2007, thus the available dates should be Jan 10, 2007 and Jan 10, 2008.
     */
    public void testNextRunDayOfYear() {
        //the 10th day every other year
        DateUnit dateUnit = new DayOfYear(10);
        int interval = 2;

        int field = getField(dateUnit);
        GregorianCalendar startDate = getJan12007();
        GregorianCalendar rightNow = (GregorianCalendar) startDate.clone();

        GregorianCalendar nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);
        GregorianCalendar expectedDate = (GregorianCalendar) startDate.clone();
        expectedDate.set(Calendar.DAY_OF_YEAR, 10);

        //next run should be the Jan 10, 2007
        assertDateIsExpected(expectedDate, nextRun);

        //time elapses, but not exceeds the next interval, and recalculates the next run
        rightNow.add(field, interval - 1);
        nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the next run should be the next interval since start
        expectedDate.add(field, interval);

        //assert
        assertDateIsExpected(expectedDate, nextRun);
    }

    /**
     * Test calculating with DaysOfWeek. In this scenario, DateUnit is set to every weekdays, the start time is
     * Thurs Feb 1 2007, thus the available date should be Thurs Feb 1,Fri Feb 2 etc.
     */
    public void testNextRunDaysOfWeek() {
        //weekdays every week
        DateUnit dateUnit = DaysOfWeek.WEEKDAYS;
        int interval = 1;

        //starts at Thurs Feb 1 2007
        GregorianCalendar startDate = getJan12007();
        startDate.set(Calendar.MONTH, 1);
        startDate.set(Calendar.DAY_OF_MONTH, 1);

        GregorianCalendar rightNow = (GregorianCalendar) startDate.clone();

        GregorianCalendar nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the next run should be Thursday Feb 1, as it's weekday
        GregorianCalendar expectedDate = (GregorianCalendar) startDate.clone();
        expectedDate.set(Calendar.DAY_OF_MONTH, 1);
        expectedDate.set(Calendar.MONTH, 1);

        assertDateIsExpected(expectedDate, nextRun);
    }

    /**
     * Test calculating with DaysOfWeek. In this scenario, DateUnit is set to every other day of Mon, Fri, Sat,
     * the start time is Thurs Feb 1 2007, thus the available date should be Fri Feb 2, Mon Feb 5
     */
    public void testNextRunDaysOfWeek2() {
        //every other day of Mon, Fri, Sat
        DateUnit dateUnit = new DaysOfWeek(new int[] {1, 5, 6});
        int interval = 2;

        //starts at Thurs Feb 1 2007
        GregorianCalendar startDate = getJan12007();
        startDate.set(Calendar.MONTH, 1);
        startDate.set(Calendar.DAY_OF_MONTH, 1);

        //and right now is the same day as startDate
        GregorianCalendar rightNow = (GregorianCalendar) startDate.clone();

        GregorianCalendar nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the next run should be Fri Feb 2
        GregorianCalendar expectedDate = (GregorianCalendar) startDate.clone();
        expectedDate.set(Calendar.DAY_OF_MONTH, 2);

        assertDateIsExpected(expectedDate, nextRun);

        //and now it's Sun Feb 4
        rightNow.set(Calendar.DAY_OF_MONTH, 4);

        //calculates the next run time based on the new rightNow
        nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the next run should be Mon Feb 5
        expectedDate.set(Calendar.DAY_OF_MONTH, 5);

        assertDateIsExpected(expectedDate, nextRun);
    }

    /**
     * Test calculating with Hour.
     */
    public void testNextRunHour() {
        DateUnit dateUnit = new Hour();
        int interval = 2;

        testSimpleDateUnit(dateUnit, interval);
    }

    /**
     * Test calculating with Minute.
     */
    public void testNextRunMinute() {
        DateUnit dateUnit = new Minute();
        int interval = 2;

        testSimpleDateUnit(dateUnit, interval);
    }

    /**
     * Test calculating with Month.
     */
    public void testNextRunMonth() {
        DateUnit dateUnit = new Month();
        int interval = 2;

        testSimpleDateUnit(dateUnit, interval);
    }

    /**
     * Test {@link NextRunCalculator#nextRun(GregorianCalendar, GregorianCalendar, DateUnit, int)} for a Second
     * unit. The calculation result should be same as expected.
     */
    public void testNextRunSecond() {
        DateUnit dateUnit = new Second();
        int interval = 2;

        testSimpleDateUnit(dateUnit, interval);
    }

    /**
     * Test calculating with Week.
     */
    public void testNextRunWeek() {
        DateUnit dateUnit = new Week();
        int interval = 2;

        testSimpleDateUnit(dateUnit, interval);
    }

    /**
     * Test calculating with WeekMonthOfYear. In this scenario, DateUnit is set to the first Monday of Jan
     * every other year, the start time is Monday Jan 1 2007, thus the available date should be  Monday Jan 1 2007 and
     * Monday Jan 5 2009.
     */
    public void testNextRunWeekMonthOfYear() {
        //the first Monday of Jan every other year
        DateUnit dateUnit = new WeekMonthOfYear(1, 1, 1);
        int interval = 2;

        int field = getField(dateUnit);
        GregorianCalendar startDate = getJan12007();
        GregorianCalendar rightNow = (GregorianCalendar) startDate.clone();

        GregorianCalendar nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the first Monday of Jan 2007 is Jan 1
        GregorianCalendar expectedDate = (GregorianCalendar) startDate.clone();

        //next run should be the Jan 1, 2007
        assertDateIsExpected(expectedDate, nextRun);

        //time elapses, but not exceeds the next interval, and recalculates the next run
        rightNow.add(field, interval - 1);
        nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the next run is expected to be Jan 5 2009, the first Monday of Jan
        expectedDate.set(Calendar.YEAR, 2009);
        expectedDate.set(Calendar.MONTH, 0);
        expectedDate.set(Calendar.DAY_OF_MONTH, 5);

        //assert
        assertDateIsExpected(expectedDate, nextRun);
    }

    /**
     * Test calculating with WeekOfMonth. In this scenario, DateUnit is set to the first Monday of every other
     * month, the start time is Monday Jan 1 2007, thus the available dates should be Mon Jan 1, 2007 and Mon Mar 5,
     * 2007.
     */
    public void testNextRunWeekOfMonth() {
        //the first Monday of every other week
        DateUnit dateUnit = new WeekOfMonth(1, 1);
        int interval = 2;

        int field = getField(dateUnit);
        GregorianCalendar startDate = getJan12007();
        GregorianCalendar rightNow = (GregorianCalendar) startDate.clone();

        GregorianCalendar nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the first Monday of Jan 2007 is Jan 1
        GregorianCalendar expectedDate = (GregorianCalendar) startDate.clone();

        //next run should be the Jan 1, 2007
        assertDateIsExpected(expectedDate, nextRun);

        //time elapses, but not exceeds the next interval, and recalculates the next run
        rightNow.add(field, interval - 1);
        nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the next run is expected to be Mar 5, the first Monday of Mar
        expectedDate.set(Calendar.MONTH, 2);
        expectedDate.set(Calendar.DAY_OF_MONTH, 5);

        //assert
        assertDateIsExpected(expectedDate, nextRun);
    }

    /**
     * Test calculating with WeekOfMonth. In this scenario, DateUnit is set to the last Sun of every other
     * month, the start time is Monday Jan 1 2007, thus the available date should be Sun Jan 28.
     */
    public void testNextRunWeekOfMonth2() {
        //last Sunday of every other week
        DateUnit dateUnit = new WeekOfMonth(7, 5);
        int interval = 1;

        GregorianCalendar startDate = getJan12007();
        GregorianCalendar rightNow = (GregorianCalendar) startDate.clone();

        GregorianCalendar nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the last Sunday of Jan 2007 is Jan 28
        GregorianCalendar expectedDate = (GregorianCalendar) startDate.clone();
        expectedDate.set(Calendar.DAY_OF_MONTH, 28);

        assertDateIsExpected(expectedDate, nextRun);
    }

    /**
     * Test calculating with Year.
     */
    public void testNextRunYear() {
        DateUnit dateUnit = new Year();
        int interval = 2;

        testSimpleDateUnit(dateUnit, interval);
    }

    /**
     * Asserts whether the calculated date is expected.
     *
     * @param expectedDate the expected date
     * @param theDate the calculated date
     */
    private void assertDateIsExpected(GregorianCalendar expectedDate, GregorianCalendar theDate) {
        long diff = theDate.getTimeInMillis() - expectedDate.getTimeInMillis();
        assertTrue("the expected date should be " + expectedDate.getTime() + " but was " + theDate.getTime(),
            (diff) <= ERROR && diff >= 0);
    }

    /**
     * Translates the DateUnit to corresponding Calendar constant.
     *
     * @param dateUnit DateUnit
     *
     * @return corresponding Calendar constant value
     */
    private static int getField(DateUnit dateUnit) {
        if (dateUnit instanceof Second) {
            return Calendar.SECOND;
        } else if (dateUnit instanceof Minute) {
            return Calendar.MINUTE;
        } else if (dateUnit instanceof Hour) {
            return Calendar.HOUR_OF_DAY;
        } else if (dateUnit instanceof Week) {
            return Calendar.WEEK_OF_YEAR;
        } else if (dateUnit instanceof Month) {
            return Calendar.MONTH;
        } else if (dateUnit instanceof Year) {
            return Calendar.YEAR;
        } else if (dateUnit instanceof DayOfYear) {
            return Calendar.YEAR;
        } else if (dateUnit instanceof DayOfMonth) {
            return Calendar.MONTH;
        } else if (dateUnit instanceof WeekOfMonth) {
            return Calendar.WEEK_OF_MONTH;
        } else if (dateUnit instanceof DaysOfWeek) {
            return Calendar.DAY_OF_YEAR;
        } else if (dateUnit instanceof WeekMonthOfYear) {
            return Calendar.YEAR;
        } else if (dateUnit instanceof Day) {
            return Calendar.DAY_OF_YEAR;
        } else {
            throw new IllegalArgumentException("unrecognized DateUnit");
        }
    }

    /**
     * Get the date of Mon January 1 2007.
     *
     * @return the date of Mon January 1 2007
     */
    private static GregorianCalendar getJan12007() {
        GregorianCalendar startDate = new GregorianCalendar();
        startDate.set(Calendar.YEAR, 2007);
        startDate.set(Calendar.MONTH, 0);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        startDate.set(Calendar.MILLISECOND, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.HOUR_OF_DAY, 0);

        return startDate;
    }

    /**
     * Test the given simple DateUnit. The scenario will go like this, start date and current time are set to
     * Monday Jan 1 2007, the next run of the date unit with a interval will be calculated, it should be the start
     * date. And then simulates the time elapses for a while but not exceeds the next interval, and recalculates the
     * next run time. The result should be the next interval of that DateUnit since start date. This can used to test
     * those simple DateUnit such as Second, Minute, Hour, Day, Week, Month, Year.
     *
     * @param dateUnit simple date unit to test
     * @param interval the interval
     */
    private void testSimpleDateUnit(DateUnit dateUnit, int interval) {
        int field = getField(dateUnit);
        GregorianCalendar startDate = getJan12007();
        GregorianCalendar rightNow = (GregorianCalendar) startDate.clone();

        GregorianCalendar nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);
        GregorianCalendar expectedDate = (GregorianCalendar) startDate.clone();

        //next run should be the start date (the first run)
        assertDateIsExpected(expectedDate, nextRun);

        //time elapses, but not exceeds the next interval, and recalculates the next run
        rightNow.add(field, interval - 1);
        nextRun = NextRunCalculator.nextRun(startDate, 0, rightNow, dateUnit, interval);

        //the next run should be the next interval since start
        expectedDate.add(field, interval);

        //assert
        assertDateIsExpected(expectedDate, nextRun);
    }
}
