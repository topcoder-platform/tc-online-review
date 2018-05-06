/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor.stresstests;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.processor.JobProcessor;
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
 * The stress test for Job Processor component. In each case a bunch of jobs with unique DateUnit will be added to the
 * processor, with nextRun time all set to 1 second later. Thread.sleep(2000) will be called to make sure all the
 * newly-added jobs are executed. MyJob.COUNT will be fetched to verify if MyJobs have been actually running.
 * @author zhuzeyuan
 * @version 1.0
 */
public class ExecutionStressTest extends TestCase {
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
        processor.setExecuteInternal(1000);
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
     * A private helper method to start and shutdown a given processor. The correctness of the number of execution for
     * MyJob will be verified here.
     * @param processor
     *            the given processor to handle
     * @throws Exception
     *             to JUnit
     */
    private void runProcessor(JobProcessor processor) throws Exception {
        // Start...
        Thread.sleep(10);
        MyJob.COUNT = 0;
        processor.start();

        // Sleep 65000ms to make sure jobs are reloaded.
        Thread.sleep(15000);

        // Check if the number of MyJob executions is correct
        assertEquals("Number of execution incorrect!", TIMES, MyJob.COUNT);

        // Shutdown...
        processor.shutdown();
    }

    /**
     * Get the WeekOfMonth instance of today.
     * @return the WeekOfMonth instance of today.
     */
    private WeekOfMonth myGetWeekOfMonth() {
        GregorianCalendar today = new GregorianCalendar();
        int day = today.get(Calendar.DAY_OF_WEEK);
        // Calculate from the beginning of the month.
        GregorianCalendar temp = (GregorianCalendar) today.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int count = 0;
        while (!temp.after(today)) {
            if (temp.get(Calendar.DAY_OF_WEEK) == day) {
                count++;
            }
            temp.add(Calendar.DATE, 1);
        }
        // The 'count'ed day of this month is today.
        return new WeekOfMonth(day, count);
    }

    /**
     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Second</code>. The expected nextRun
     * time is the next second. Thread.sleep will be called and then verify if those jobs have really been running.
     * @throws Exception
     *             to JUnit
     */
    public void testJobSecond() throws Exception {
        // Prepare a random generator
        Random ran = new Random(1);
        // Create the processor with a bunch of jobs
        for (int i = 0; i < TIMES; i++) {
            GregorianCalendar startDate = new GregorianCalendar();
            startDate.add(Calendar.SECOND, -ran.nextInt(100) * 2 - 1);
            jobs[i].setStartDate(startDate);
            jobs[i].setStartTime(myGetTime(startDate));
            GregorianCalendar stopDate = new GregorianCalendar();
            stopDate.add(Calendar.YEAR, 1);
            jobs[i].setStopDate(stopDate);
            jobs[i].setIntervalUnit(new Second());
            jobs[i].setIntervalValue(2);
            scheduler.addJob(jobs[i]);
        }

        runProcessor(processor);
    }

//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Minute</code>. The expected nextRun
//     * time is the next second. Thread.sleep will be called and then verify if those jobs have really been running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobMinute() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(2);
//
//        // Create the processor with a bunch of jobs
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(1000) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.MINUTE, -interval * ran.nextInt(1000));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(new Minute());
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Hour</code>. The expected nextRun
//     * time is the next second. Thread.sleep will be called and then verify if those jobs have really been running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobHour() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(3);
//
//        // Create the processor with a bunch of jobs
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(1000) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.HOUR, -interval * ran.nextInt(1000));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(new Hour());
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Day</code>. The expected nextRun
//     * time is the next second. Thread.sleep will be called and then verify if those jobs have really been running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobDay() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(4);
//
//        // Create the processor with a bunch of jobs
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(1000) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.DATE, -interval * ran.nextInt(1000));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(new Day());
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Week</code>. The expected nextRun
//     * time is the next second. Thread.sleep will be called and then verify if those jobs have really been running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobWeek() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(5);
//
//        // Create the processor with a bunch of jobs
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(1000) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.WEEK_OF_YEAR, -interval * ran.nextInt(1000));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(new Week());
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Month</code>. The expected nextRun
//     * time is the next second. Thread.sleep will be called and then verify if those jobs have really been running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobMonth() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(6);
//
//        // Create the processor with a bunch of jobs
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(100) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.MONTH, -interval * ran.nextInt(100));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(new Month());
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>Year</code>. The expected nextRun
//     * time is the next second. Thread.sleep will be called and then verify if those jobs have really been running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobYear() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(7);
//
//        // Create the processor with a bunch of jobs
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(10) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.YEAR, -interval * ran.nextInt(10));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(new Year());
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>DayOfYear</code>. The expected
//     * nextRun time is the next second. Thread.sleep will be called and then verify if those jobs have really been
//     * running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobDayOfYear() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(8);
//
//        // Create the processor with a bunch of jobs
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(10) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.YEAR, -interval * ran.nextInt(10));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(new DayOfYear(new GregorianCalendar().get(Calendar.DAY_OF_YEAR)));
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>DayOfMonth</code>. The expected
//     * nextRun time is the next second. Thread.sleep will be called and then verify if those jobs have really been
//     * running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobDayOfMonth() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(9);
//
//        // Create the processor with a bunch of jobs
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(10) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.MONTH, -interval * ran.nextInt(100));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(new DayOfMonth(new GregorianCalendar().get(Calendar.DAY_OF_MONTH)));
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>DaysOfWeek</code>. The expected
//     * nextRun time is the next second. Thread.sleep will be called and then verify if those jobs have really been
//     * running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobDaysOfWeek() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(10);
//
//        // Create the processor with a bunch of jobs
//        DaysOfWeek unit = new DaysOfWeek(new int[] {TO_MONDAY_BASED_DAYS[new GregorianCalendar()
//            .get(Calendar.DAY_OF_WEEK)]});
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(10) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.WEEK_OF_YEAR, -interval * ran.nextInt(100));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(unit);
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>WeekOfMonth</code>. The expected
//     * nextRun time is the next second. Thread.sleep will be called and then verify if those jobs have really been
//     * running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobWeekOfMonth() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(11);
//
//        // Create the processor with a bunch of jobs
//        WeekOfMonth unit = myGetWeekOfMonth();
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(10) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.MONTH, -interval * ran.nextInt(10));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(unit);
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
//
//    /**
//     * Test the processor with a bunch of jobs, in which IntervalUnit is <code>WeekMonthOfYear</code>. The expected
//     * nextRun time is the next second. Thread.sleep will be called and then verify if those jobs have really been
//     * running.
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testJobWeekMonthOfYear() throws Exception {
//        // Prepare a random generator
//        Random ran = new Random(12);
//
//        // Create the processor with a bunch of jobs
//        WeekOfMonth unit1 = myGetWeekOfMonth();
//        WeekMonthOfYear unit = new WeekMonthOfYear(unit1.getDay(), unit1.getWeek(), new GregorianCalendar()
//            .get(Calendar.MONTH));
//        for (int i = 0; i < TIMES; i++) {
//            int interval = ran.nextInt(10) + 5;
//            GregorianCalendar startDate = new GregorianCalendar();
//            startDate.add(Calendar.SECOND, 1);
//            startDate.add(Calendar.YEAR, -interval * ran.nextInt(10));
//            jobs[i].setStartDate(startDate);
//            jobs[i].setStartTime(myGetTime(startDate));
//            GregorianCalendar stopDate = new GregorianCalendar();
//            stopDate.add(Calendar.YEAR, 1);
//            jobs[i].setStopDate(stopDate);
//            jobs[i].setIntervalUnit(unit);
//            jobs[i].setIntervalValue(interval);
//            scheduler.addJob(jobs[i]);
//        }
//
//        runProcessor(processor);
//    }
}
