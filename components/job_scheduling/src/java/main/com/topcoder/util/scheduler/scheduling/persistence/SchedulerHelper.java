/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.persistence;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.topcoder.util.file.Template;
import com.topcoder.util.file.TemplateFormatException;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Day;
import com.topcoder.util.scheduler.scheduling.DayOfMonth;
import com.topcoder.util.scheduler.scheduling.DayOfYear;
import com.topcoder.util.scheduler.scheduling.DaysOfWeek;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Hour;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Minute;
import com.topcoder.util.scheduler.scheduling.Month;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.Second;
import com.topcoder.util.scheduler.scheduling.Util;
import com.topcoder.util.scheduler.scheduling.Week;
import com.topcoder.util.scheduler.scheduling.WeekMonthOfYear;
import com.topcoder.util.scheduler.scheduling.WeekOfMonth;
import com.topcoder.util.scheduler.scheduling.Year;

/**
 * <p>
 * This is a helper class for the two <code>Scheduler</code> implementations
 * in the current version.
 * </p>
 * <p>
 * This class is mainly used for created a <code>Job</code> instance with all
 * the attributes previously set. Besides, this class also provides some static
 * helper methods.
 * </p>
 * <p>
 * Changes in version 3.1: Two new job types are taken into consideration.
 * </p>
 * <p>
 * Thread Safety : This class is mutable and so is not thread safe.
 * </p>
 * @author TCSDEVELOPER
 * @author Standlove, fuyun
 * @version 3.1
 * @since 3.0
 */
class SchedulerHelper {
    /**
     * <p>
     * Represents the string value of <b>false</b> used in this component.
     * </p>
     */
    private static final String FALSE = "False";

    /**
     * <p>
     * Represents the string value of <b>true</b> used in this component.
     * </p>
     */
    private static final String TRUE = "True";

    /**
     * <p>
     * Represents the string value for the <code>JOB_TYPE_JAVA_CLASS</code>
     * job type.
     * </p>
     */
    private static final String JOB_TYPE_JAVA_CLASS = "JOB_TYPE_JAVA_CLASS";

    /**
     * <p>
     * Represents the string value for the
     * <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code> job type.
     * </p>
     * @since 3.1
     */
    private static final String JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY = "JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY";

    /**
     * <p>
     * Represents the string value for the
     * <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code> job type.
     * </p>
     * @since 3.1
     */
    private static final String JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE =
        "JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE";

    /**
     * <p>
     * Represents the string value for the <code>JOB_TYPE_EXTERNAL</code> job
     * type.
     * </p>
     */
    private static final String JOB_TYPE_EXTERNAL = "JOB_TYPE_EXTERNAL";

    /**
     * <p>
     * Represents the name of the job.
     * </p>
     */
    private String jobName;

    /**
     * <p>
     * Represents the startDate of the job.
     * </p>
     */
    private String startDate;

    /**
     * <p>
     * Represents the startTime of the job.
     * </p>
     */
    private String startTime;

    /**
     * <p>
     * Represents the endDate of the job.
     * </p>
     */
    private String endDate;

    /**
     * <p>
     * Represents the dateUnit of the job.
     * </p>
     */
    private String dateUnit;

    /**
     * <p>
     * Represents the dateUnitDays of the job.
     * </p>
     */
    private String dateUnitDays;

    /**
     * <p>
     * Represents the dateUnitWeek of the job.
     * </p>
     */
    private String dateUnitWeek;

    /**
     * <p>
     * Represents the dateUnitMonth of the job.
     * </p>
     */
    private String dateUnitMonth;

    /**
     * <p>
     * Represents the interval of the job.
     * </p>
     */
    private String interval;

    /**
     * <p>
     * Represents the recurrence of the job.
     * </p>
     */
    private String recurrence;

    /**
     * <p>
     * Represents the active of the job.
     * </p>
     */
    private String active;

    /**
     * <p>
     * Represents the jobType of the job.
     * </p>
     */
    private String jobType;

    /**
     * <p>
     * Represents the jobCommand of the job.
     * </p>
     */
    private String jobCommand;

    /**
     * <p>
     * Represents the dependenceJobName of the job.
     * </p>
     */
    private String dependenceJobName;

    /**
     * <p>
     * Represents the dependenceJobStatus of the job.
     * </p>
     */
    private String dependenceJobStatus;

    /**
     * <p>
     * Represents the dependenceJobDelay of the job.
     * </p>
     */
    private String dependenceJobDelay;

    /**
     * <p>
     * Represents the modificationDate of the job.
     * </p>
     */
    private String modificationDate;

    /**
     * <p>
     * Represents the async mode of the job.
     * </p>
     * @since BUGR-1577
     */
    private String asyncMode = TRUE;

    /**
     * <p>
     * Sets the name of the job.
     * </p>
     * @param jobName the name of the job to set
     */
    void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * <p>
     * Sets the active flag of the job.
     * </p>
     * @param active the new active value of the job to set
     */
    void setActive(String active) {
        this.active = active;
    }

    /**
     * <p>
     * Sets the date unit of the job.
     * </p>
     * @param dateUnit the new date unit value of the job to set
     */
    void setDateUnit(String dateUnit) {
        this.dateUnit = dateUnit;
    }

    /**
     * <p>
     * Sets the days of the date unit of the job.
     * </p>
     * @param dateUnitDays the new days of the date unit of the job to set
     */
    void setDateUnitDays(String dateUnitDays) {
        this.dateUnitDays = dateUnitDays;
    }

    /**
     * <p>
     * Sets the month of the date unit of the job.
     * </p>
     * @param dateUnitMonth the new month of the date unit of the job to set
     */
    void setDateUnitMonth(String dateUnitMonth) {
        this.dateUnitMonth = dateUnitMonth;
    }

    /**
     * <p>
     * Sets the week of the date unit of the job.
     * </p>
     * @param dateUnitWeek the new week of the date unit of the job to set
     */
    void setDateUnitWeek(String dateUnitWeek) {
        this.dateUnitWeek = dateUnitWeek;
    }

    /**
     * <p>
     * Sets the the delay of the dependence job of the job.
     * </p>
     * @param dependenceJobDelay the delay of the dependence job of the job to
     *            set
     */
    void setDependenceJobDelay(String dependenceJobDelay) {
        this.dependenceJobDelay = dependenceJobDelay;
    }

    /**
     * <p>
     * Sets the dependence job name of the job.
     * </p>
     * @param dependenceJobName the new dependence job name of the job to set
     */
    void setDependenceJobName(String dependenceJobName) {
        this.dependenceJobName = dependenceJobName;
    }

    /**
     * <p>
     * Sets the dependence job status of the job.
     * </p>
     * @param dependenceJobStatus the new dependence job status of the job to
     *            set
     */
    void setDependenceJobStatus(String dependenceJobStatus) {
        this.dependenceJobStatus = dependenceJobStatus;
    }

    /**
     * <p>
     * Sets the end date of the job.
     * </p>
     * @param endDate the name end date of the job to set
     */
    void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * <p>
     * Sets the interval of the job.
     * </p>
     * @param interval the interval of the job to set
     */
    void setInterval(String interval) {
        this.interval = interval;
    }

    /**
     * <p>
     * Sets the job command of the job.
     * </p>
     * @param jobCommand the job command of the job to set
     */
    void setJobCommand(String jobCommand) {
        this.jobCommand = jobCommand;
    }

    /**
     * <p>
     * Sets the job type of the job.
     * </p>
     * @param jobType the job type of the job to set
     */
    void setJobType(String jobType) {
        this.jobType = jobType;
    }

    /**
     * <p>
     * Sets the recurrence of the job.
     * </p>
     * @param recurrence the recurrence of the job to set
     */
    void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    /**
     * <p>
     * Sets the start date of the job.
     * </p>
     * @param startDate the start date of the job to set
     */
    void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * <p>
     * Sets the start time of the job.
     * </p>
     * @param startTime the start time of the job to set
     */
    void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * <p>
     * Sets the async mode of the job.
     * </p>
     * @param asyncMode the async mode of the job to set
     * @since BUGR-1577
     */
    void setAsyncMode(String asyncMode) {
        this.asyncMode = asyncMode == null ? TRUE : (Boolean.parseBoolean(asyncMode) ? TRUE : FALSE);
    }

    /**
     * <p>
     * Sets the modification date of the job.
     * </p>
     * @param modificationDate the modification date of the job to set
     */
    void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * <p>
     * Sets the start date of the job.
     * </p>
     * @param date the start date of the job to set
     */
    void setStartDate(Date date) {
        if (date != null) {
            this.startDate = getDateFormat().format(date);
        }
    }

    /**
     * <p>
     * Sets the end date of the job.
     * </p>
     * @param date the end date of the job to set
     */
    void setEndDate(Date date) {
        if (date != null) {
            this.endDate = getDateFormat().format(date);
        }
    }

    /**
     * <p>
     * Sets the active flag of the job.
     * </p>
     * @param active the active flag of the job to set
     */
    void setActive(boolean active) {
        this.active = parseBooleanValue(active);
    }

    /**
     * <p>
     * This method creates a <code>Job</code> instance from all the attributes
     * set previously.
     * </p>
     * @return a <code>Job</code> instance from all the attributes set
     *         previously.
     * @throws SchedulingException to JUnit
     */
    Job createJob() throws SchedulingException {
        try {
            Job job = new Job(jobName, parseJobType(jobType), jobCommand);

            // get the date format
            DateFormat df = getDateFormat();

            // set the start date when the dependence job is not present
            if (dependenceJobName == null) {
                job.setStartDate(parseDate(startDate, df));
            }

            // set the end date when the dependence job is not present
            if (dependenceJobName == null) {
                job.setStopDate(parseDate(endDate, df));
            }

            // set the start time when the dependence job is not present
            if (dependenceJobName == null) {
                int value = Integer.parseInt(startTime);
                if (value <= 0 || value > Util.ONE_DAY) {
                    throw new SchedulingException(
                            "The value for StartTime property is not positive or large "
                                    + "than 24 hours in milliseconds, it is "
                                    + value);
                }
                job.setStartTime(value);
            }

            // set the modification date
            if (modificationDate != null) {
                job.setModificationDate(parseDate(modificationDate, df));
            }

            // set the active flag
            if (TRUE.equals(active) || FALSE.equals(active)) {
                job.setActive(TRUE.equals(active));
            } else {
                throw new SchedulingException("The value for Active property ["
                        + active + "] is unrecognized.");
            }

            // set the async mode flag
            if (TRUE.equals(asyncMode) || FALSE.equals(asyncMode)) {
                job.setAsyncMode(TRUE.equals(asyncMode));
            } else {
                throw new SchedulingException("The value for AsyncMode property ["
                        + asyncMode + "] is unrecognized.");
            }

            // set the recurrence value
            int value = Integer.parseInt(recurrence);
            if (value <= 0) {
                throw new SchedulingException(
                        "The value for Recurrence property is not positive, it is "
                                + value);
            }
            job.setRecurrence(value);

            // set the interval value
            int intervalValue = Integer.parseInt(interval);
            if (intervalValue <= 0) {
                throw new SchedulingException(
                        "The value for interval is not positive, it is "
                                + value);
            }
            job.setIntervalValue(intervalValue);

            // set the date unit
            job.setIntervalUnit(createDateUnit());

            // set the dependence if present
            Dependence dependence = createDependence();
            if (dependence == null) {
                if (startDate == null) {
                    throw new SchedulingException(
                            "StartDate and Dependence are both not present.");
                }
                if (startTime == null) {
                    throw new SchedulingException(
                            "StartTime and Dependence are both not present.");
                }
                if (endDate == null) {
                    throw new SchedulingException(
                            "EndDate and Dependence are both not present.");
                }
            } else {
                if (startDate != null) {
                    throw new SchedulingException(
                            "StartDate and Dependence are both present.");
                }

                job.setDependence(dependence);
            }

            return job;
        } catch (NumberFormatException e) {
            throw new SchedulingException(
                    "NumberFormatException occurs during parsing integer vale.",
                    e);
        } catch (IllegalArgumentException e) {
            throw new SchedulingException(
                    "IllegalArgumentException occurs during creating job instance.",
                    e);
        }
    }

    /**
     * <p>
     * This method creates a <code>Dependence</code> instance from the
     * attributes set previously.
     * </p>
     * <p>
     * This method may return null if the <code>Dependence</code> doesn't
     * present.
     * </p>
     * @return a <code>Dependence</code> instance from the attributes set
     *         previously, or null if it is not present
     * @throws SchedulingException if any of the attributes for constructing
     *             <code>Dependence</code> instance is invalid
     */
    private Dependence createDependence() throws SchedulingException {
        if (dependenceJobName == null) {
            return null;
        }

        try {
            // Get status, can be SUCCESSFUL, FAILED or BOTH.
            if (!(dependenceJobStatus.equals(EventHandler.SUCCESSFUL)
                    || dependenceJobStatus.equals(EventHandler.FAILED) || dependenceJobStatus
                    .equals(Dependence.BOTH))) {
                throw new SchedulingException("dependence job "
                        + dependenceJobName
                        + "'s Status is not correct, it is "
                        + dependenceJobStatus + " now.");
            }

            // Get delay, must be int.
            int delayInt = Integer.parseInt(dependenceJobDelay);
            if (delayInt < 0) {
                throw new SchedulingException("dependence job "
                        + dependenceJobName + "'s Delay must >= 0, it is "
                        + delayInt + " now.");
            }

            return new Dependence(dependenceJobName, dependenceJobStatus,
                    delayInt);
        } catch (NumberFormatException e) {
            throw new SchedulingException(
                    "NumberFormatException occurs during parsing integer vale.",
                    e);
        }
    }

    /**
     * <p>
     * This method creates a <code>DateUnit</code> instance from the
     * attributes set previously.
     * </p>
     * @return creates a <code>DateUnit</code> instance from the attributes
     *         set previously.
     * @throws SchedulingException if fails to construct the
     *             <code>DateUnit</code> instance
     */
    private DateUnit createDateUnit() throws SchedulingException {
        try {
            if (Day.class.getName().equals(dateUnit)) {
                return new Day();
            } else if (DayOfMonth.class.getName().equals(dateUnit)) {
                return new DayOfMonth(parseSingleDay());
            } else if (DayOfYear.class.getName().equals(dateUnit)) {
                return new DayOfYear(parseSingleDay());
            } else if (DaysOfWeek.class.getName().equals(dateUnit)) {
                return new DaysOfWeek(parseDays());
            } else if (Hour.class.getName().equals(dateUnit)) {
                return new Hour();
            } else if (Minute.class.getName().equals(dateUnit)) {
                return new Minute();
            } else if (Month.class.getName().equals(dateUnit)) {
                return new Month();
            } else if (Second.class.getName().equals(dateUnit)) {
                return new Second();
            } else if (Week.class.getName().equals(dateUnit)) {
                return new Week();
            } else if (WeekMonthOfYear.class.getName().equals(dateUnit)) {
                return new WeekMonthOfYear(parseSingleDay(), parseWeek(),
                        parseMonth());
            } else if (WeekOfMonth.class.getName().equals(dateUnit)) {
                return new WeekOfMonth(parseSingleDay(), parseWeek());
            } else if (Year.class.getName().equals(dateUnit)) {
                return new Year();
            } else {
                throw new SchedulingException("The given dateUnit value ["
                        + dateUnit + "] is unknown.");
            }
        } catch (IllegalArgumentException e) {
            throw new SchedulingException(
                    "The given days, week or year are invalid.", e);
        }
    }

    /**
     * <p>
     * This method parses the week string to an integer.
     * </p>
     * @return the week value in integer presentation
     * @throws SchedulingException if the week value is not an integer or it is
     *             null
     */
    private int parseWeek() throws SchedulingException {
        if (dateUnitWeek == null) {
            throw new SchedulingException(
                    "The dateUnitWeek attribute is missing, but it is required for "
                            + dateUnit + " class.");
        }

        return parseInteger(dateUnitWeek);
    }

    /**
     * <p>
     * This method parses the month string to an integer.
     * </p>
     * @return the month value in integer presentation
     * @throws SchedulingException if the month value is not an integer or it is
     *             null
     */
    private int parseMonth() throws SchedulingException {
        if (dateUnitMonth == null) {
            throw new SchedulingException(
                    "The dateUnitMonth attribute is missing, but it is required for "
                            + dateUnit + " class.");
        }

        return parseInteger(dateUnitMonth);
    }

    /**
     * <p>
     * This method parses the days string to an integer.
     * </p>
     * @return the days value in integer presentation
     * @throws SchedulingException if the days value is not an integer or it is
     *             null or it contains more than one day
     */
    private int parseSingleDay() throws SchedulingException {
        int[] days = parseDays();

        if (days.length != 1) {
            throw new SchedulingException(
                    "Only one day should be present for dateUnitDays attribute.");
        }

        return days[0];
    }

    /**
     * <p>
     * This method parses the days string to an integer array.
     * </p>
     * <p>
     * Each day in the days string should be separated with a comma.
     * </p>
     * @return the days value in an integer array presentation
     * @throws SchedulingException if the days value is not an integer or it is
     *             null or it contains more than one day
     */
    private int[] parseDays() throws SchedulingException {
        if (dateUnitDays == null) {
            throw new SchedulingException(
                    "The dateUnitDays attribute is missing, but it is required for "
                            + dateUnit + " class.");
        }

        return parseIntegers(dateUnitDays);
    }

    /**
     * <p>
     * This method parses a string value to an integer array.
     * </p>
     * <p>
     * Each integer in the string should be separated with a comma.
     * </p>
     * @param value the string value
     * @return an integer array.
     * @throws SchedulingException if fails to convert the string to an integer
     *             array
     */
    private static int[] parseIntegers(String value) throws SchedulingException {
        String[] values = value.split(",");
        int[] intValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            intValues[i] = parseInteger(values[i]);
        }

        return intValues;
    }

    /**
     * <p>
     * Converts a string to an integer.
     * </p>
     * @param value the string value to convert
     * @return the integer value
     * @throws SchedulingException if fails to convert the string to an integer
     */
    private static int parseInteger(String value) throws SchedulingException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new SchedulingException(
                    "NumberFormatException occurs during parsing integer value ["
                            + value + "].", e);
        }
    }

    /**
     * <p>
     * Gets the <code>DateFormat</code> instance used in this component to
     * format <code>Date</code> instance.
     * </p>
     * @return the <code>DateFormat</code> instance used in this component to
     *         format <code>Date</code> instance.
     */
    static DateFormat getDateFormat() {
        return DateFormat.getDateTimeInstance(DateFormat.LONG,
                DateFormat.MEDIUM, Locale.US);
    }

    /**
     * <p>
     * Parses the given string to a <code>JobType</code> instance.
     * </p>
     * <p>
     * Changes in version 3.1: Two new job type are taken into consideration:
     * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>
     * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>.
     * </p>
     * @param value the string value to convert
     * @return the <code>JobType</code> instance.
     * @throws SchedulingException if fails to convert the string value to
     *             <code>JobType</code> instance.
     * @since 3.0
     */
    static JobType parseJobType(String value) throws SchedulingException {
        if (JOB_TYPE_EXTERNAL.equals(value)) {
            return JobType.JOB_TYPE_EXTERNAL;
        }

        if (JOB_TYPE_JAVA_CLASS.equals(value)) {
            return JobType.JOB_TYPE_JAVA_CLASS;
        }

        if (JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY.equals(value)) {
            return JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY;
        }

        if (JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE.equals(value)) {
            return JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE;
        }

        throw new SchedulingException("The value for JobType property ["
                + value + "] is unrecognized.");
    }

    /**
     * <p>
     * Parses the <code>JobType</code> instance to a string.
     * </p>
     * <p>
     * Changes in version 3.1: Two new job type are taken into consideration:
     * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>
     * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>.
     * </p>
     * @param type the <code>JobType</code> instance.
     * @return the string presentation of the <code>JobType</code> instance.
     * @since 3.0
     */
    static String parseJobType(JobType type) {
        if (type == JobType.JOB_TYPE_EXTERNAL) {
            return JOB_TYPE_EXTERNAL;
        } else if (type == JobType.JOB_TYPE_JAVA_CLASS) {
            return JOB_TYPE_JAVA_CLASS;
        } else if (type == JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY) {
            return JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY;
        } else {
            return JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE;
        }
    }

    /**
     * <p>
     * Converts a boolean value to a string.
     * </p>
     * @param value a boolean value
     * @return the string representation for the boolean value
     */
    static String parseBooleanValue(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * <p>
     * This method parses the <code>DateUnit</code> instance to a string
     * array.
     * </p>
     * <p>
     * The length of the string array is <code>4</code>, the first element is
     * the date unit type, the second element is the days of the date unit, the
     * third element is the week of the date unit and the fourth element if the
     * month of the date unit
     * </p>
     * @param dateUnit the <code>DateUnit</code> instance to convert
     * @return a string array of length 4, the first element is the date unit
     *         type, the second element is the days of the date unit, the third
     *         element is the week of the date unit and the fourth element if
     *         the month of the date unit
     * @throws SchedulingException if the given date unit is null
     */
    static String[] parseDateUnit(DateUnit dateUnit) throws SchedulingException {
        if (dateUnit == null) {
            throw new SchedulingException(
                    "The date unit of the job can not be null.");
        }

        String[] values = new String[4];
        final String dateUnitName = dateUnit.getClass().getName();

        values[0] = dateUnitName;
        if (DayOfMonth.class.getName().equals(dateUnitName)) {
            DayOfMonth dayOfMonth = (DayOfMonth) dateUnit;
            values[1] = String.valueOf(dayOfMonth.getDay());
        } else if (DayOfYear.class.getName().equals(dateUnitName)) {
            DayOfYear dayOfYear = (DayOfYear) dateUnit;
            values[1] = String.valueOf(dayOfYear.getDay());
        } else if (DaysOfWeek.class.getName().equals(dateUnitName)) {
            DaysOfWeek daysOfWeek = (DaysOfWeek) dateUnit;

            // get all the days using comma separated
            StringBuffer sb = new StringBuffer();
            int[] days = daysOfWeek.getDays();
            for (int i = 0; i < days.length; i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(days[i]);
            }

            values[1] = sb.toString();
        } else if (WeekMonthOfYear.class.getName().equals(dateUnitName)) {
            WeekMonthOfYear weekMonthOfYear = (WeekMonthOfYear) dateUnit;
            values[1] = String.valueOf(weekMonthOfYear.getDay());
            values[2] = String.valueOf(weekMonthOfYear.getWeek());
            values[3] = String.valueOf(weekMonthOfYear.getMonth());
        } else if (WeekOfMonth.class.getName().equals(dateUnitName)) {
            WeekOfMonth weekOfMonth = (WeekOfMonth) dateUnit;
            values[1] = String.valueOf(weekOfMonth.getDay());
            values[2] = String.valueOf(weekOfMonth.getWeek());
        }

        return values;
    }

    /**
     * <p>
     * Parses a string value to a <code>GregorianCalendar</code> instance.
     * </p>
     * @param value the string value to convert
     * @param df the <code>DateFormat</code> instance for parsing
     * @return the <code>GregorianCalendar</code> instance representing the
     *         string value
     * @throws SchedulingException if fails to parse the string value to a
     *             <code>GregorianCalendar</code> instance.
     */
    private static GregorianCalendar parseDate(String value, DateFormat df)
        throws SchedulingException {
        GregorianCalendar date = new GregorianCalendar();
        try {
            date.setTime(df.parse(value));
        } catch (ParseException e) {
            throw new SchedulingException(
                    "ParseException occurs during parsing date string ["
                            + value + "]", e);
        }

        return date;
    }

    /**
     * <p>
     * Creates the default <code>Template</code> used in this component.
     * </p>
     * @return the default <code>Template</code> used in this component.
     */
    static Template createDefaultTemplate() {
        Template defaultTemplate = new XsltTemplate();
        try {
            defaultTemplate
                    .setTemplate("Hi,\nThis email notifies you that the job %JobName%"
                            + " has the status %JobStatus% now.");
        } catch (TemplateFormatException e) {
            // ignore
        }

        return defaultTemplate;
    }

    /**
     * <p>
     * Reads the given text into a <code>Template</code>.
     * </p>
     * @param templateText the templateText value
     * @return the template generated from the given text
     * @throws SchedulingException if any error occur when generating the
     *             template from the text
     */
    static Template readTextToTemplate(String templateText)
        throws SchedulingException {
        Template template = new XsltTemplate();
        try {
            template.setTemplate(templateText);
        } catch (TemplateFormatException e) {
            throw new SchedulingException(
                    "The format of the given template text is not correct.", e);
        }

        return template;
    }
}
