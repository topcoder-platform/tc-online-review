/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * This is a marker extension of <code>DateUnit</code> that defines a specific day in a month.
 * </p>
 *
 * <p>
 * For example, one can define the 15th day of a month using this class. This could be used for
 * monthly check generation for disgruntled TopCoder designers.
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
public class DayOfMonth implements DateUnit {
    /**
     * <p>
     * Represents the day of the month, at least 1.
     * </p>
     *
     * <p>
     * If the number corresponds to a day that is higher than the last day, then it will be
     * interpreted as the last day in the month.
     * </p>
     */
    private final int day;

    /**
     * <p>
     * Constructs a <code>DayOfMonth</code> with day given.
     * </p>
     *
     * @param day The day of the month, at least 1
     *
     * @throws IllegalArgumentException if day &lt; 1
     */
    public DayOfMonth(int day) {
        if (day < 1) {
            throw new IllegalArgumentException("The give day is less than 1, it is " + day);
        }

        this.day = day;
    }

    /**
     * <p>
     * Gets the day of the month.
     * </p>
     *
     * @return the day of the month
     */
    public int getDay() {
        return day;
    }
}
