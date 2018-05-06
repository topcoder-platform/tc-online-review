/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor.stresstests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.processor.JobProcessor;
import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Day;
import com.topcoder.util.scheduler.scheduling.DayOfMonth;
import com.topcoder.util.scheduler.scheduling.DayOfYear;
import com.topcoder.util.scheduler.scheduling.DaysOfWeek;
import com.topcoder.util.scheduler.scheduling.Hour;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Minute;
import com.topcoder.util.scheduler.scheduling.Month;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.Second;
import com.topcoder.util.scheduler.scheduling.Week;
import com.topcoder.util.scheduler.scheduling.WeekMonthOfYear;
import com.topcoder.util.scheduler.scheduling.WeekOfMonth;
import com.topcoder.util.scheduler.scheduling.Year;

import junit.framework.TestCase;

/**
 * The stress test for Job Processor component. In each case a bunch of jobs will be added to the processor, with a
 * unique DateUnit used. After reloaded this class will check those jobs' nextRun to verify.
 * @author zhuzeyuan
 * @version 1.0
 */
public class NextRunStressTest extends TestCase {

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
     * The number of jobs to be generated.
     */
    private static final int TIMES = 30;

    /**
     * TIMES number of jobs used for testing.
     */
    private Job[] jobs;

    /**
     * Prepare a scheduler for testing.
     */
    private Scheduler scheduler = new MyScheduler();

    /**
     * Prepare a processor for testing.
     */
    private JobProcessor processor;

    /**
     * Sets up test environment.
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
        ConfigManager.getInstance().add("stress/log.xml");

        jobs = new Job[TIMES];
        for (int i = 0; i < jobs.length; i++) {
            jobs[i] = new Job("job" + i, JobType.JOB_TYPE_JAVA_CLASS,
                "com.topcoder.util.scheduler.processor.stresstests.MyJob");
            jobs[i].setModificationDate(new GregorianCalendar());
            jobs[i].setActive(true);
            jobs[i].setRecurrence(1);
        }

        processor = new JobProcessor(scheduler, 1000, LogManager.getLog());
    }

    /**
     * Clears test environment.
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        // Shutdown the timers manually. Since some processor#shutdown() for some submissions will perform DEADLOCK
        Field field = JobProcessor.class.getDeclaredField("executeTimer");
        field.setAccessible(true);
        Timer timer = (Timer) field.get(processor);
        if (timer != null) {
            timer.cancel();
        }

        field = JobProcessor.class.getDeclaredField("reloadTimer");
        field.setAccessible(true);
        timer = (Timer) field.get(processor);
        if (timer != null) {
            timer.cancel();
        }

        // Sleep 3000ms to make sure we can close the log, and all jobs are stopped.
        Thread.sleep(3000);

        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }

    /**
     * Calculate the next run time for different DateUnit type.
     * @param startTime
     *            the base time.
     * @param unit
     *            the DateUnit instance.
     * @param value
     *            the interval value.
     * @return the target time to trigger next run.
     */
    private GregorianCalendar myCalculateNextRun(GregorianCalendar startTime, GregorianCalendar rightNow,
        DateUnit unit, int value) {
        GregorianCalendar temp = (GregorianCalendar) startTime.clone();
        while (rightNow.after(temp)) {
            if (unit instanceof Second) {
                temp.add(Calendar.SECOND, value);
            } else if (unit instanceof Minute) {
                temp.add(Calendar.MINUTE, value);
            } else if (unit instanceof Hour) {
                temp.add(Calendar.HOUR, value);
            } else if (unit instanceof Day) {
                temp.add(Calendar.DATE, value);
            } else if (unit instanceof Week) {
                temp.add(Calendar.WEEK_OF_YEAR, value);
            } else if (unit instanceof Month) {
                temp.add(Calendar.MONTH, value);
            } else if (unit instanceof Year) {
                temp.add(Calendar.YEAR, value);
            } else if (unit instanceof DayOfYear) {
                temp = myCalculateUnitDayOfYear(startTime, rightNow, temp, (DayOfYear) unit, value);
            } else if (unit instanceof DayOfMonth) {
                temp = myCalculateUnitDayOfMonth(startTime, rightNow, temp, (DayOfMonth) unit, value);
            } else if (unit instanceof DaysOfWeek) {
                temp = myCalculateUnitDaysOfWeek(startTime, rightNow, temp, (DaysOfWeek) unit, value);
            } else if (unit instanceof WeekOfMonth) {
                temp = myCalculateUnitWeekOfMonth(startTime, rightNow, temp, (WeekOfMonth) unit, value);
            } else if (unit instanceof WeekMonthOfYear) {
                temp = myCalculateUnitWeekMonthOfYear(startTime, rightNow, temp, (WeekMonthOfYear) unit, value);
            }
        }
        return temp;
    }

    /**
     * Calculate new time by a interval type of DayOfYear.
     * @param nextRun
     *            base time.
     * @param unit
     *            DayOfYear instance.
     * @param value
     *            interval value.
     * @return the target time
     */
    private GregorianCalendar myCalculateUnitDayOfYear(GregorianCalendar startTime,
            GregorianCalendar rightNow, GregorianCalendar nextRun, DayOfYear unit, int value) {
        GregorianCalendar temp = (GregorianCalendar) nextRun.clone();
        int maxValue = temp.getActualMaximum(Calendar.DAY_OF_YEAR);
        temp.set(Calendar.DAY_OF_YEAR, Math.min(maxValue, unit.getDay()));
        while (rightNow.after(temp) || (startTime.after(rightNow) && temp.before(startTime))) {
            if (temp.before(startTime)) {
                temp.add(Calendar.YEAR, 1);
            } else {
                temp.add(Calendar.YEAR, value);
            }
            maxValue = temp.getActualMaximum(Calendar.DAY_OF_YEAR);
            temp.set(Calendar.DAY_OF_YEAR, Math.min(maxValue, unit.getDay()));
        }
        return temp;
    }

    /**
     * Calculate new time by a interval type of DayOfMonth.
     * @param nextRun
     *            base time.
     * @param unit
     *            DayOfMonth instance.
     * @param value
     *            interval value.
     * @return the target time
     */
    private GregorianCalendar myCalculateUnitDayOfMonth(GregorianCalendar startTime,
            GregorianCalendar rightNow, GregorianCalendar nextRun, DayOfMonth unit, int value) {
        GregorianCalendar temp = (GregorianCalendar) nextRun.clone();
        int maxValue = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
        temp.set(Calendar.DAY_OF_MONTH, Math.min(maxValue, unit.getDay()));
        while (rightNow.after(temp) || (startTime.after(rightNow) && temp.before(startTime))) {
            if (temp.before(startTime)) {
                temp.add(Calendar.MONTH, 1);
            } else {
                temp.add(Calendar.MONTH, value);
            }
            maxValue = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
            temp.set(Calendar.DAY_OF_MONTH, Math.min(maxValue, unit.getDay()));
        }
        return temp;
    }

    /**
     * Calculate new time by a interval type of DaysOfWeek.
     * @param nextRun
     *            base time.
     * @param unit
     *            DaysOfWeek instance.
     * @param value
     *            interval value.
     * @return the target time
     */
    private GregorianCalendar myCalculateUnitDaysOfWeek(GregorianCalendar startTime,
            GregorianCalendar rightNow, GregorianCalendar nextRun, DaysOfWeek unit, int value) {
        GregorianCalendar temp = (GregorianCalendar) nextRun.clone();
        setDateUnit(temp, unit, value);
        while (rightNow.after(temp) || (startTime.after(rightNow) && temp.before(startTime))) {
            if (temp.before(startTime)) {
                nextDayOfWeek(temp, unit, 1);
            } else {
                nextDayOfWeek(temp, unit, value);
            }
            setDateUnit(temp, unit, value);
        }
        return temp;
    }

    /**
     * <p>
     * Get Next Day Of week.
     * </p>
     * @param lastRun
     *              the base time.
     * @param unit
     *              DaysOfWeek instance.
     * @param interval
     *              interval value.
     */
    private void nextDayOfWeek(GregorianCalendar lastRun, DaysOfWeek unit, int interval) {
        int[] days = unit.getDays();

        //gets the day of week for lastRun
        int dayOfWeek = TO_MONDAY_BASED_DAYS[lastRun.get(Calendar.DAY_OF_WEEK)];
        int index = Arrays.binarySearch(days, dayOfWeek);

        //if the last run is not in the predefined days, skip the process
        if (index != -1) {
            //calculates the next index for days, according to the interval
            index = (index + interval) % days.length;

            //sets date to the next day of week according to the calculated index
            lastRun.add(Calendar.DAY_OF_YEAR, ((days[index] + 7) - dayOfWeek) % 7);
        }
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
    private void setDateUnit(GregorianCalendar nextRun, DateUnit dateUnit, int interval) {
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
            nextRun.add(Calendar.DAY_OF_YEAR, ((days[nextDayIndex] + 7) - dayOfWeek) % 7);
        } else if (dateUnit instanceof DayOfYear) {
            //setField(nextRun, Calendar.DAY_OF_YEAR, ((DayOfYear) dateUnit).getDay());
        } else if (dateUnit instanceof DayOfMonth) {
            //setField(nextRun, Calendar.DAY_OF_MONTH, ((DayOfMonth) dateUnit).getDay());
        } else if (dateUnit instanceof WeekOfMonth) {
            WeekOfMonth unit = (WeekOfMonth) dateUnit;
            int day = unit.getDay();
            int week = unit.getWeek();

            //since the all days in the second week are in the same month, set the week to this one
            //will prevent the overflow
            nextRun.set(Calendar.WEEK_OF_MONTH, 2);
            nextRun.set(Calendar.DAY_OF_WEEK, TO_SUNDAY_BASED_DAYS[day]);

            int maxValue = nextRun.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH);
            nextRun.set(Calendar.DAY_OF_WEEK_IN_MONTH, Math.min(maxValue, week));
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

            int maxValue = nextRun.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH);
            nextRun.set(Calendar.DAY_OF_WEEK_IN_MONTH, Math.min(maxValue, week));
        }
    }

    /**
     * Calculate new time by a interval type of WeekOfMonth.
     * @param nextRun
     *            base time.
     * @param unit
     *            WeekOfMonth instance.
     * @param value
     *            interval value.
     * @return the target time
     */
    private GregorianCalendar myCalculateUnitWeekOfMonth(GregorianCalendar startTime,
            GregorianCalendar rightNow, GregorianCalendar nextRun, WeekOfMonth unit, int value) {
        GregorianCalendar temp = (GregorianCalendar) nextRun.clone();
        setDateUnit(temp, unit, value);
        while (rightNow.after(temp) || (startTime.after(rightNow) && temp.before(startTime))) {
            if (temp.before(startTime)) {
                temp.add(Calendar.MONTH, 1);
            } else {
                temp.add(Calendar.MONTH, value);
            }
            setDateUnit(temp, unit, value);
        }
        return temp;
    }

    /**
     * Calculate new time by a interval type of WeekMonthOfYear.
     * @param nextRun
     *            base time.
     * @param unit
     *            WeekMonthOfYear instance.
     * @param value
     *            interval value.
     * @return the target time
     */
    private GregorianCalendar myCalculateUnitWeekMonthOfYear(GregorianCalendar startTime,
            GregorianCalendar rightNow, GregorianCalendar nextRun, WeekMonthOfYear unit,
        int value) {
        GregorianCalendar temp = (GregorianCalendar) nextRun.clone();
        setDateUnit(temp, unit, value);
        while (rightNow.after(temp) || (startTime.after(rightNow) && temp.before(startTime))) {
            if (temp.before(startTime)) {
                temp.add(Calendar.YEAR, 1);
            } else {
                temp.add(Calendar.YEAR, value);
            }
            setDateUnit(temp, unit, value);
        }
        return temp;
    }

    /**
     * A private helper method to retrieve time (an int of milliseconds calculated from midnight) from a given date.
     * This is used to call job#setStartTime.
     * @param date
     *            the given date
     * @return the time retrieved from the date.
     */
    private int myGetTime(GregorianCalendar date) {
        GregorianCalendar midNight = (GregorianCalendar) date.clone();
        midNight.set(Calendar.SECOND, 0);
        midNight.set(Calendar.MILLISECOND, 0);
        midNight.set(Calendar.MINUTE, 0);
        midNight.set(Calendar.HOUR_OF_DAY, 0);
        return (int) (date.getTimeInMillis() - midNight.getTimeInMillis());
    }

    /**
     * A private helper method to start and shutdown a given processor. The correctness of the nextRuns generated will
     * be verified here.
     * @param processor
     *            the given processor to handle
     * @throws Exception
     *             to JUnit
     */
    private void runProcessor(JobProcessor processor) throws Exception {
          Thread.sleep(10);
        // Check if the nextRuns are calculated correctly
        GregorianCalendar rightNow = new GregorianCalendar();
        
        for (int i = 0; i < TIMES; i++) {
            String s1 = myCalculateNextRun(jobs[i].getStartDate(),
                    rightNow, jobs[i].getIntervalUnit(), jobs[i].getIntervalValue()).toString();
            String s2 = jobs[i].getNextRun().toString();
            assertEquals("nextRun on jobs[" + i + "] incorrect", s1, s2);
        }

        // Shutdown...
        processor.shutdown();
    }

    /**
     * <p>
     * Invoke the prepareJob to init the job.
     * </p>
     *
     * @throws Exception if any error happen.
     */
    private void invokePrepareJob(Job job) throws Exception {
        Thread.sleep(100);
        Method method = processor.getClass().getDeclaredMethod("prepareJob", new Class[]{Job.class});
        method.setAccessible(true);
        method.invoke(processor, new Object[]{job});
        method.setAccessible(false);
    }
    
    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Second</code>. The correctness of
     * the nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobSecond() throws Exception {
        // Prepare a random generator
        Random ran = new Random(1);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.SECOND, -ran.nextInt(100) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 1);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new Second());
            jobs[i].setIntervalValue(200);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Minute</code>. The correctness of
     * the nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobMinute() throws Exception {
        // Prepare a random generator
        Random ran = new Random(2);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.MINUTE, -ran.nextInt(10000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 1);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new Minute());
            jobs[i].setIntervalValue(ran.nextInt(100) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Hour</code>. The correctness of the
     * nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobHour() throws Exception {
        // Prepare a random generator
        Random ran = new Random(3);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.HOUR, -ran.nextInt(10000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 1);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new Hour());
            jobs[i].setIntervalValue(ran.nextInt(100) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Day</code>. The correctness of the
     * nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobDay() throws Exception {
        // Prepare a random generator
        Random ran = new Random(4);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.DATE, -ran.nextInt(10000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 1);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new Day());
            jobs[i].setIntervalValue(ran.nextInt(100) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Week</code>. The correctness of the
     * nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobWeek() throws Exception {
        // Prepare a random generator
        Random ran = new Random(5);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.WEEK_OF_YEAR, -ran.nextInt(1000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 1);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new Week());
            jobs[i].setIntervalValue(ran.nextInt(10) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Month</code>. The correctness of the
     * nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobMonth() throws Exception {
        // Prepare a random generator
        Random ran = new Random(6);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.MONTH, -ran.nextInt(200) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 10);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new Month());
            jobs[i].setIntervalValue(ran.nextInt(10) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Year</code>. The correctness of the
     * nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobYear() throws Exception {
        // Prepare a random generator
        Random ran = new Random(7);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.YEAR, -ran.nextInt(60) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 100);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new Year());
            jobs[i].setIntervalValue(ran.nextInt(3) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>DayOfYear</code>. The correctness of
     * the nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobDayOfYear() throws Exception {
        // Prepare a random generator
        Random ran = new Random(8);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.DATE, -ran.nextInt(2000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 100);
            jobs[i].setStopDate(stopDate);
            if (i == 0) {
                jobs[0].setIntervalUnit(new DayOfYear(366));
            } else {
                jobs[i].setIntervalUnit(new DayOfYear(ran.nextInt(365) + 1));
            }
            jobs[i].setIntervalValue(ran.nextInt(4) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }
        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>DayOfMonth</code>. The correctness
     * of the nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobDayOfMonth() throws Exception {
        // Prepare a random generator
        Random ran = new Random(9);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.DATE, -ran.nextInt(2000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 100);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new DayOfMonth(ran.nextInt(31) + 1));
            jobs[i].setIntervalValue(ran.nextInt(50) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>DaysOfWeek</code>. The correctness
     * of the nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobDaysOfWeek() throws Exception {
        // Prepare a random generator
        Random ran = new Random(10);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.DATE, -ran.nextInt(2000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 100);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new DaysOfWeek(new int[] {1, 2, 3, 4, 5}));
            jobs[i].setIntervalValue(2);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>WeekOfMonth</code>. The correctness
     * of the nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobWeekOfMonth() throws Exception {
        // Prepare a random generator
        Random ran = new Random(11);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.DATE, -ran.nextInt(2000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 100);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new WeekOfMonth(ran.nextInt(7) + 1, ran.nextInt(5) + 1));
            jobs[i].setIntervalValue(ran.nextInt(100) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>WeekMonthOfYear</code>. The
     * correctness of the nextRun calculated by the processor will be verified.
     * @throws Exception
     */
    public void testJobWeekMonthOfYear() throws Exception {
        // Prepare a random generator
        Random ran = new Random(12);

        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.DATE, -ran.nextInt(2000) - 10);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 100);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new WeekMonthOfYear(ran.nextInt(7) + 1, ran.nextInt(5) + 1,
                ran.nextInt(12) + 1));
            jobs[i].setIntervalValue(ran.nextInt(20) + 1);
            scheduler.addJob(jobs[i]);
            this.invokePrepareJob(jobs[i]);
        }

        runProcessor(processor);
    }
}
