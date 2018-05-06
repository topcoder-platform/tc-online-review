/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.util.Arrays;

/**
 * <p>
 * This is a marker extension of <code>DateUnit</code> that defines specific days of the week
 * when the Job is to be run.
 * </p>
 *
 * <p>
 * It can define one to seven days of the week. There will exist two convenience implementations
 * that encompass weekdays and weekends.
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
public class DaysOfWeek implements DateUnit {
    /**
     * <p>
     * This is an array defining the week days, 1-5, to be plugged into the <code>WEEKDAYS</code> constant.
     * </p>
     */
    public static final int[] WEEKDAYS_VALUES = {1, 2, 3, 4, 5};

    /**
     * <p>
     * This is a convenience <code>DaysOfWeek</code> predefined for the five days defining the work days.
     * </p>
     */
    public static final DaysOfWeek WEEKDAYS = new DaysOfWeek(WEEKDAYS_VALUES);

    /**
     * <p>
     * This is an array defining the week days, 6-7, to be plugged into the <code>WEEKDEND</code> constant.
     * </p>
     */
    public static final int[] WEEKDEND_VALUES = {6, 7};

    /**
     * <p>
     * This is a convenience <code>DaysOfWeek</code> predefined for the two days defining the weekend.
     * </p>
     */
    public static final DaysOfWeek WEEKDEND = new DaysOfWeek(WEEKDEND_VALUES);

    /**
     * <p>
     * Represents the days of the week, all between 1 and 7, and unique, where 1 is Monday, and 7 is Sunday.
     * </p>
     *
     * <p>
     * This value is set in the constructor and will not be changed.
     * </p>
     *
     * <p>
     * It will be a copy of the array passed in the constructor, and will never be longer than 7, but at least 1.
     * The constructor will also sort them.
     * </p>
     */
    private final int[] days;

    /**
     * Constructor. Sorts the values and sets them in a new array in the days field.
     *
     * @param days integer array of days in the week
     *
     * @throws IllegalArgumentException if days == null or days.length < 1 or days.length > 7 or days having a day < 1
     * or days having a day > 7 or days having a duplicate day
     */
    public DaysOfWeek(int[] days) {
        Util.checkObjectNotNull(days, "days");
        if (days.length < 1 || days.length > Util.WEEK_COUNT) {
            throw new IllegalArgumentException("The length of days is not between 1 and 7, inclusively, it is "
                + days.length);
        }

        boolean[] flags = new boolean[Util.WEEK_COUNT];
        Arrays.fill(flags, false);
        for (int i = 0; i < days.length; i++) {
            if (days[i] < 1 || days[i] > Util.WEEK_COUNT) {
                throw new IllegalArgumentException(
                    "One day in the given days is not between 1 and 7, inclusively, it is " + days[i]);
            }

            if (flags[days[i] - 1]) {
                throw new IllegalArgumentException("The day [" + days[i] + "] occurs more than once "
                    + "in the given days.");
            }

            flags[days[i] - 1] = true;
        }

        this.days = new int[days.length];
        int index = 0;
        for (int i = 0; i < flags.length; i++) {
            if (flags[i]) {
                this.days[index++] = i + 1;
            }
        }
    }

    /**
     * <p>
     * Gets the days of the week.
     * </p>
     *
     * <p>
     * Note, the return array is sorted.
     * </p>
     *
     * @return the days of the week
     */
    public int[] getDays() {
        return (int[]) days.clone();
    }
}
