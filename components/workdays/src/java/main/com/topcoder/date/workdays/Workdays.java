/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * The Workdays Interface provides a set of generic functions to define a workday schedule (set holidays and other
 * non-work days, set whether or not weekend days are to be included as a normal workday, set the start and end hours
 * and minutes of a work day: for example, work day starts at 8:00AM and ends at 5:30PM) and a function to add days,
 * hours or minutes to an existing date and return the date that specifies how many work days it would take to
 * complete).
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong> Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * <p>
 * <strong> Change log:</strong> Added generic parameters for Java collection types as a part of code upgrade from Java
 * 1.4 to Java 1.5. Add() method should allow negative amount arguments.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.1
 */
public interface Workdays extends Serializable {
    /**
     * <p>
     * Adds a non-workday to the list of non-work days.
     * </p>
     *
     * @param nonWorkday
     *            the date to add as a non work day.
     *
     * @throws NullPointerException
     *             if nonWorkDay is null.
     */
    public void addNonWorkday(Date nonWorkday);

    /**
     * <p>
     * Removes a non-workday from the list of non-work days.
     * </p>
     *
     * @param nonWorkday
     *            the date to remove from the list.
     *
     * @throws NullPointerException
     *             is thrown if nonWorkDay is null.
     * @throws IllegalArgumentException
     *             is thrown if nonWorkDay does not exist.
     */
    public void removeNonWorkday(Date nonWorkday);

    /**
     * <p>
     * Returns a Set of non-workdays.
     * </p>
     *
     * @return a Set of non-workdays.
     */
    public Set<Date> getNonWorkdays();

    /**
     * <p>
     * Clears the non-workdays.
     * </p>
     */
    public void clearNonWorkdays();

    /**
     * <p>
     * Sets whether or not Saturday is to be considered a work day.
     * </p>
     *
     * @param isSaturdayWorkday
     *            true if Saturday is to be considered a workday.
     */
    public void setSaturdayWorkday(boolean isSaturdayWorkday);

    /**
     * <p>
     * Returns whether or not Saturday is considered a workday.
     * </p>
     *
     * @return true if Saturday is considered a workday.
     */
    public boolean isSaturdayWorkday();

    /**
     * <p>
     * Sets whether or not Sunday is to be considered a work day.
     * </p>
     *
     * @param isSundayWorkday
     *            true if Sunday is to be considered a workday.
     */
    public void setSundayWorkday(boolean isSundayWorkday);

    /**
     * <p>
     * Returns whether or not Sunday is considered a workday.
     * </p>
     *
     * @return true if Sunday is to be considered a workday.
     */
    public boolean isSundayWorkday();

    /**
     * <p>
     * Sets the hours of the workday start time. This is to be in 24 hour mode.
     * </p>
     *
     * @param startTimeHours
     *            the hours of the workday start time (24 hour mode).
     *
     * @throws IllegalArgumentException
     *             if startTimeHours is not a valid hour.
     */
    public void setWorkdayStartTimeHours(int startTimeHours);

    /**
     * <p>
     * Returns the hours of the workday start time, in 24 hour mode.
     * </p>
     *
     * @return the hours of the workday start time
     */
    public int getWorkdayStartTimeHours();

    /**
     * <p>
     * Sets the minutes of the workday start time.
     * </p>
     *
     * @param startTimeMinutes
     *            the minutes of the workday start time.
     *
     * @throws IllegalArgumentException
     *             if startTimeMinutes is not a valid minute.
     */
    public void setWorkdayStartTimeMinutes(int startTimeMinutes);

    /**
     * <p>
     * Returns the minutes of the workday start time.
     * </p>
     *
     * @return the minutes of the workday start time.
     */
    public int getWorkdayStartTimeMinutes();

    /**
     * <p>
     * Sets the hours of the workday end time. This is to be in 24 hour mode.
     * </p>
     *
     * @param endTimeHours
     *            the hours of the workday end time (24 hour mode).
     *
     * @throws IllegalArgumentException
     *             if endTimeHours is not a valid hour.
     */
    public void setWorkdayEndTimeHours(int endTimeHours);

    /**
     * <p>
     * Returns the hours of the workday end time, in 24 hour mode.
     * <p>
     *
     * @return the hours of the workday end time
     */
    public int getWorkdayEndTimeHours();

    /**
     * <p>
     * Sets the minutes of the workday end time.
     * </p>
     *
     * @param endTimeMinutes
     *            the minutes of the workday end time.
     *
     * @throws IllegalArgumentException
     *             if endTimeMinutes is not a valid minute.
     */
    public void setWorkdayEndTimeMinutes(int endTimeMinutes);

    /**
     * <p>
     * Returns the minutes of the workday end time.
     * </p>
     *
     * @return the minutes of the workday end time.
     */
    public int getWorkdayEndTimeMinutes();

    /**
     * <p>
     * Method to add or remove a certain amount of time to a Date to calculate the number of work days that it would
     * take to complete.
     * </p>
     *
     * @param amount
     *            the amount of time to add (if negative, indicates the amount of time to subtract).
     * @param unitOfTime
     *            the unit of time to add (minutes, hours, days).
     * @param startDate
     *            the date to perform the addition to.
     *
     * @return the Date result of adding/subtracting the amount of time to the startDate taking into consideration the
     *         workdays definition.
     *
     * @throws NullPointerException
     *             if startDate or unitOfTime is null.
     * @throws IllegalArgumentException
     *             if the start/end time set incorrectly.
     */
    public Date add(Date startDate, WorkdaysUnitOfTime unitOfTime, int amount);
}
