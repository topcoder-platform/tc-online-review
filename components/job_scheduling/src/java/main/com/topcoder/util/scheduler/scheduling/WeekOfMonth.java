/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * This is a marker extension of <code>DateUnit</code> that defines a specific day of the week in a month.
 * </p>
 *
 * <p>
 * For example, one can define the first Saturday of a month using this class. This might be ideal for
 * setting up notifications for a meeting that must occur on a specific day of the business week but
 * also at the start of a month.
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
public class WeekOfMonth implements DateUnit {
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
     * Represents the week of the month, at least 1, which corresponds to the first week with the
     * given day in the month.
     * </p>
     *
     * <p>
     * If the number corresponds to a week that is higher than the last week, then it will be interpreted
     * as the last week which holds the given day in the month.
     * </p>
     *
     * <p>
     * This value is set in the constructor and is immutable.
     * </p>
     */
    private final int week;

    /**
     * <p>
     * Constructs a <code>WeekOfMonth</code> with day and week given.
     * </p>
     *
     * @param day The day of the week, between 1 and 7
     * @param week The week of the month, at least 1
     *
     * @throws IllegalArgumentException if day &gt; 7 or day &lt; 1 or week &lt; 1
     */
    public WeekOfMonth(int day, int week) {
        if (day > Util.WEEK_COUNT || day < 1) {
            throw new IllegalArgumentException("The given day is not between 1 and 7, inclusively, it is " + day);
        }
        if (week < 1) {
            throw new IllegalArgumentException("The given week is less than 1, it is " + week);
        }

        this.day = day;
        this.week = week;
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
}
