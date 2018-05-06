/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * This is a marker interface representing a unit of time to be used in configuring the schedule of the Job.
 * </p>
 *
 * <p>
 * Many of the implementations will refer to simply intervals, such as every second, day, week, or so, based
 * on the start date and time. Some will be more complex, however, like referring to a specific day of the year
 * or month.
 * </p>
 *
 * <p>
 * The <code>Job</code> will use this to indicate a specific date or time to do the job, or a date or time
 * from the start date to perform it. It will work in conjunction with the interval and recurrence to track
 * when and how often the job is done.
 * </p>
 *
 * <p>
 * Thread Safety : Implementations of this class are required to be thread safe.
 * </p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 3.0
 */
public interface DateUnit {
}
