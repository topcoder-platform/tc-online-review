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

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * <p>Helper class to calculate the next run time of a Job. The next run time is calculated based on the start
 * date, dateUnit and execution interval of the job. More detail of the calculation is documented in CS 1.3.3.</p>
 *  <p>Thread safety: This class is thread-safety.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
final class NextRunCalculator {
    /**
     * The array maps Monday based weekdays to Sunday based weekdays. Sunday based weekdays is used by {@link
     * Calendar} while Monday based weekdays is used by this component.
     */
    private static final int[] TO_SUNDAY_BASED_DAYS = {0, 2, 3, 4, 5, 6, 7, 1};

    /**
     * The array maps Sunday based weekdays to Monday based weekdays. Sunday based weekdays is used by {@link
     * Calendar} while Monday based weekdays is used by this component.
     */
    private static final int[] TO_MONDAY_BASED_DAYS = {0, 7, 1, 2, 3, 4, 5, 6};


    /**
     * Private ctor preventing this class from being instantiated.
     */
    private NextRunCalculator() {

    }

    /**
     * Calculates the next runtime for a Job, given with the startDate, now time, date unit and interval.
     *
     * @param startDate start date of the job
     * @param startTime start time in a day from midnight of the job
     * @param rightNow the reference of right now
     * @param dateUnit date unit of the job
     * @param interval interval of date unit
     *
     * @return the calculated next run time
     */
    public static GregorianCalendar nextRun(GregorianCalendar startDate, int startTime, GregorianCalendar rightNow,
        DateUnit dateUnit, int interval) {
        GregorianCalendar nextRun = (GregorianCalendar) startDate.clone();
        int time = startTime;
        setField(nextRun, Calendar.MILLISECOND, time % 1000);
        time /= 1000;
        setField(nextRun, Calendar.SECOND, time % 60);
        time /= 60;
        setField(nextRun, Calendar.MINUTE, time % 60);
        time /= 60;
        setField(nextRun, Calendar.HOUR_OF_DAY, time);
        
        //sets the next run time with date unit
        setDateUnit(nextRun, dateUnit, interval);
        //if the next run time is before now, or the start date is in the future but next run is before that
        while (rightNow.after(nextRun) || (startDate.after(rightNow) && nextRun.before(startDate))) {
            //rolls to the next interval of unit
            if (nextRun.before(startDate)) {
                nextRun = rollNextUnit(nextRun, dateUnit, 1);
            } else {
                nextRun = rollNextUnit(nextRun, dateUnit, interval);
            }

            //and set the next run time of current unit
            setDateUnit(nextRun, dateUnit, interval);
        }

        return nextRun;
    }

    /**
     * <p>Roll to the current date to the given interval number of DateUnit. For example, current time is Jan
     * 10, DateUnit is DayOfMonth(10), and interval is 2, this method will roll the date to Mar 10.</p>
     *  <p>For most of the DateUnit, this method will just add the interval number of the unit to the given
     * date. Whereas DaysOfWeek is a bit complex, the date should be set to the next interval number weekdays from
     * current weekday defined by DateUnit. e.g, DaysOfWeek(1,2,3,4,5) and interval 2, current day of week is 2, and
     * rollNextUnit will skip one day and set the day to 4.</p>
     *
     * @param lastRun the last calculated run time
     * @param dateUnit date unit
     * @param interval interval for date unit
     *
     * @return the interval of DateUnit after the lastRun date
     */
    private static GregorianCalendar rollNextUnit(GregorianCalendar lastRun, DateUnit dateUnit, int interval) {
        if (dateUnit instanceof DaysOfWeek) {
            DaysOfWeek unit = (DaysOfWeek) dateUnit;
            int[] days = unit.getDays();

            //gets the day of week for lastRun
            int dayOfWeek = TO_MONDAY_BASED_DAYS[lastRun.get(Calendar.DAY_OF_WEEK)];
            int index = Arrays.binarySearch(days, dayOfWeek);

            //if the last run is not in the predefined days, skip the process
            if (index != -1) {
                //calculates the next index for days, according to the interval
                index = (index + interval) % days.length;

                //sets date to the next day of week according to the calculated index
                setDayOfWeek(lastRun, dayOfWeek, days[index]);
            }
        } else if (dateUnit instanceof DayOfYear) {
            lastRun.add(Calendar.YEAR, interval);
        } else if (dateUnit instanceof DayOfMonth) {
            lastRun.add(Calendar.MONTH, interval);
        } else if (dateUnit instanceof WeekOfMonth) {
            lastRun.add(Calendar.MONTH, interval);
        } else if (dateUnit instanceof WeekMonthOfYear) {
            lastRun.add(Calendar.YEAR, interval);
        } else if (dateUnit instanceof Second) {
            lastRun.add(Calendar.SECOND, interval);
        } else if (dateUnit instanceof Minute) {
            lastRun.add(Calendar.MINUTE, interval);
        } else if (dateUnit instanceof Hour) {
            lastRun.add(Calendar.HOUR_OF_DAY, interval);
        } else if (dateUnit instanceof Day) {
            lastRun.add(Calendar.DAY_OF_YEAR, interval);
        } else if (dateUnit instanceof Month) {
            lastRun.add(Calendar.MONTH, interval);
        } else if (dateUnit instanceof Week) {
            lastRun.add(Calendar.WEEK_OF_YEAR, interval);
        } else if (dateUnit instanceof Year) {
            lastRun.add(Calendar.YEAR, interval);
        }

        return lastRun;
    }

    /**
     * <p>Sets the nextRun date with current DateUnit value. For example, a DateUnit of DayOfMonth(10), this
     * method will set the date to 10.</p>
     *  <p>For most DateUnit, this method will just simply set the date with the date specified by DateUnit.
     * Whereas, for DaysOfWeek it's a bit different. This method is just set the date to the closest day of week that
     * occurs in the weekdays of given DateUnit if it's not corresponding previously. If the day of week for current
     * date is one of the value defined in DaysOfWeek, this method will do nothing, and it's now the rollNextUnit()'s
     * responsibility to skip to interval to the next day.</p>
     *
     * @param nextRun the calculated date
     * @param dateUnit DateUnit to set
     * @param interval interval
     */
    private static void setDateUnit(GregorianCalendar nextRun, DateUnit dateUnit, int interval) {
        if (dateUnit instanceof DaysOfWeek) {
            DaysOfWeek unit = (DaysOfWeek) dateUnit;
            int[] days = unit.getDays();

            //gets day of week for nextRun
            int dayOfWeek = TO_MONDAY_BASED_DAYS[nextRun.get(Calendar.DAY_OF_WEEK)];

            int nextDayIndex = 0;

            //if it's not one of the day in DateUnit, the closest future day will be selected
            //e.g days = {1,2,4}, and now is 3, so the next run will be set to 4
            if (dayOfWeek < days[0]) {
                nextDayIndex = 0;
            } else {
                for (int i = 1; i < days.length; i++) {
                    if ((days[i - 1] < dayOfWeek) && (dayOfWeek <= days[i])) {
                        nextDayIndex = i;

                        break;
                    }
                }
            }

            setDayOfWeek(nextRun, dayOfWeek, days[nextDayIndex]);
        } else if (dateUnit instanceof DayOfYear) {
            setField(nextRun, Calendar.DAY_OF_YEAR, ((DayOfYear) dateUnit).getDay());
        } else if (dateUnit instanceof DayOfMonth) {
            setField(nextRun, Calendar.DAY_OF_MONTH, ((DayOfMonth) dateUnit).getDay());
        } else if (dateUnit instanceof WeekOfMonth) {
            WeekOfMonth unit = (WeekOfMonth) dateUnit;
            int day = unit.getDay();
            int week = unit.getWeek();

            //since the all days in the second week are in the same month, set the week to this one
            //will prevent the overflow
            nextRun.set(Calendar.WEEK_OF_MONTH, 2);
            nextRun.set(Calendar.DAY_OF_WEEK, TO_SUNDAY_BASED_DAYS[day]);

            setField(nextRun, Calendar.DAY_OF_WEEK_IN_MONTH, week);
        } else if (dateUnit instanceof WeekMonthOfYear) {
            WeekMonthOfYear unit = (WeekMonthOfYear) dateUnit;
            int day = unit.getDay();
            int week = unit.getWeek();
            int month = unit.getMonth();

            nextRun.set(Calendar.MONTH, month - 1);

            //since the all days in the second week are in the same month, set the week to this one
            //will prevent the overflow
            nextRun.set(Calendar.WEEK_OF_MONTH, 2);
            nextRun.set(Calendar.DAY_OF_WEEK, TO_SUNDAY_BASED_DAYS[day]);

            setField(nextRun, Calendar.DAY_OF_WEEK_IN_MONTH, week);
        }
    }

    /**
     * Sets day of week to the given date. This method will roll forward to set the day of week, but never
     * backward. For example, today is Thursday 8, and the expectedDay is 1, this method will roll forward to set the
     * date to next week, which is Mon 12.
     *
     * @param theDate the date to set
     * @param nowDay current day of week
     * @param expectedDay the expected day of week in the future
     */
    private static void setDayOfWeek(GregorianCalendar theDate, int nowDay, int expectedDay) {
        //roll the the next week if expectedDay is smaller than nowDay
        theDate.add(Calendar.DAY_OF_YEAR, ((expectedDay + 7) - nowDay) % 7);
    }

    /**
     * Sets value to specified field of date. If the value exceeds the max value, a max value will be set.
     *
     * @param date date to set
     * @param field the field to set
     * @param value the value to set
     */
    private static void setField(GregorianCalendar date, int field, int value) {
        int maxValue = date.getActualMaximum(field);
        date.set(field, Math.min(maxValue, value));
    }
}
