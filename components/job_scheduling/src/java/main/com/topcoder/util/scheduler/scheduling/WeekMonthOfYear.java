/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * This is a marker extension of <code>DateUnit</code> that defines a specific day of the week in a month
 * in a given year.
 * </p>
 *
 * <p>
 * For example, one can define the first Saturday of January. This might be ideal for post-new year's
 * celebrations pink slip generation for rowdy employees.
 * </p>
 *
 * <p>
 * Thread Safety : This class is immutable and thread-safe.
 * </p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 3.0
 */
public class WeekMonthOfYear implements DateUnit {
    /**
     * <p>
     * Represents the total months of a year.
     * </p>
     */
    private static final int MONTH_COUNT = 12;

    /**
     * <p>
     * Represents the day of the week, between 1 and 7, where 1 is Monday, and 7 is Sunday.
     * </p>
     *
     * <p>
     * This value is set in the constructor and is immutable.
     * </p>
     */
    private final int day;

    /**
     * <p>
     * Represents the week of the month, at least 1, which corresponds to the first week with
     * the given day of the month.
     * </p>
     *
     * <p>
     * If the number corresponds to a week that is higher than the last week, then it will be
     * interpreted as the last week which holds the given day in the month.
     * <p>
     *
     * <p>
     * This value is set in the constructor and is immutable.
     * </p>
     */
    private final int week;

    /**
     * <p>
     * Represents the month of the year, between 1 and 12, where 1 is January, and 7 is December.
     * </p>
     *
     * <p>
     * This value is set in the constructor and is immutable.
     * </p>
     */
    private final int month;

    /**
     * <p>
     * Constructs a <code>WeekMonthOfYear</code> with day, week and month given.
     * </p>
     *
     * @param day The day of the week, between 1 and 7
     * @param week The week of the month, at least 1
     * @param month The month of the year, between 1 and 12
     *
     * @throws IllegalArgumentException if day &gt; 7 or day &lt; 1 or week &lt; 1 or month &lt; 1 or month &gt; 12
     */
    public WeekMonthOfYear(int day, int week, int month) {
        if (day > Util.WEEK_COUNT || day < 1) {
            throw new IllegalArgumentException("The given day is not between 1 and 7, inclusively, it is " + day);
        }
        if (week < 1) {
            throw new IllegalArgumentException("The given week is less than 1, it is " + week);
        }
        if (month > MONTH_COUNT || month < 1) {
            throw new IllegalArgumentException("The given month is not between 1 and 12, inclusively, it is " + month);
        }

        this.day = day;
        this.week = week;
        this.month = month;
    }

    /**
     * <p>
     * Gets the day of the week.
     * </p>
     *
     * @return the day of the week
     */
    public int getDay() {
        return day;
    }

    /**
     * <p>
     * Gets the week of the month.
     * </p>
     *
     * @return the week of the month
     */
    public int getWeek() {
        return week;
    }

    /**
     * <p>
     * Gets the month of the year.
     * </p>
     *
     * @return the month of the year
     */
    public int getMonth() {
        return month;
    }
}
