/*
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.scheduler.processor.accuracytests;

import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Log;
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
import com.topcoder.util.scheduler.scheduling.ScheduledEnable;
import com.topcoder.util.scheduler.scheduling.Second;
import com.topcoder.util.scheduler.scheduling.Week;
import com.topcoder.util.scheduler.scheduling.WeekMonthOfYear;
import com.topcoder.util.scheduler.scheduling.WeekOfMonth;
import com.topcoder.util.scheduler.scheduling.Year;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author bbskill
 * @version 1.0
 */
public class JobProcessorTest extends TestCase {
    /**
     * <p>
     * JobProcessor instance used for testing.
     * </p>
     */
    private JobProcessor processor;

    /**
     * <p>
     * Log instance used for testing.
     * </p>
     */
    private Log log;

    /**
     * <p>
     * Scheduler instance used for testing.
     * </p>
     */
    private MockScheduler scheduler;

    /**
     * <p>
     * Job instance used for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * Dependent Job instance used for testing.
     * </p>
     */
    private Job depJob;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(JobProcessorTest.class);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to junit
     */
    protected void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        if (!cm.existsNamespace("com.topcoder.util.log")) {
            ConfigManager.getInstance().add("log.xml");
        }
        log = LogManager.getLog();
        scheduler = new MockScheduler();
        job = new Job("Job Name", JobType.JOB_TYPE_EXTERNAL, "Run Command");
        job.setRecurrence(1);
        job.setModificationDate(new GregorianCalendar());
        job.setStartDate(new GregorianCalendar());
        GregorianCalendar nextRun = job.getStartDate();
        nextRun = job.getStartDate();
        nextRun .set(GregorianCalendar.HOUR_OF_DAY, 0);
        nextRun .set(GregorianCalendar.MINUTE, 0);
        nextRun .set(GregorianCalendar.SECOND, 0);
        nextRun .set(GregorianCalendar.MILLISECOND, 0);
        job.setStartTime((int)(job.getStartDate().getTimeInMillis() - nextRun.getTimeInMillis()));
        depJob = new Job("depJob Name", JobType.JOB_TYPE_EXTERNAL, "Run Command");
        scheduler.addJob(job);
        scheduler.addDependentJob(job, depJob);
        processor = new JobProcessor(scheduler, 1000, log);
        processor.setExecuteInternal(1000);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception to junit
     */
    protected void tearDown() throws Exception {
        processor.shutdown();
        ConfigManager cm = ConfigManager.getInstance();
        // clear all the name space.
        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
        job = null;
        depJob = null;
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     */
    public void testJobProcessor() {
        assertNotNull("The JobProcessor instance should not be null.", processor);
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is Year and verify the JobProcessor is ok.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testJobProcessorWithYear() throws Exception {
        job.setIntervalUnit(new Year());
        job.setIntervalValue(2);
        invokePrepareJob();
        validateGregorianCalendar("Year Unit with 2 intervalValue", GregorianCalendar.YEAR, 2);
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is Month and verify the JobProcessor is ok.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testJobProcessorWithMonth() throws Exception {
        job.setIntervalUnit(new Month());
        job.setIntervalValue(2);
        invokePrepareJob();
        validateGregorianCalendar("Month Unit with 2 intervalValue", GregorianCalendar.MONTH, 2);
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is Week and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithWeek() throws Exception {
        job.setIntervalUnit(new Week());
        job.setIntervalValue(2);
        invokePrepareJob();
        validateGregorianCalendar("Week Unit with 2 intervalValue", GregorianCalendar.WEEK_OF_YEAR, 2);
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is Day and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithDay() throws Exception {
        job.setIntervalUnit(new Day());
        job.setIntervalValue(3);
        invokePrepareJob();
        validateGregorianCalendar("Day Unit with 3 intervalValue", GregorianCalendar.DAY_OF_YEAR, 3);
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is Hour and verify the JobProcessor is ok.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testJobProcessorWithHour() throws Exception {
        job.setIntervalUnit(new Hour());
        job.setIntervalValue(3);
        invokePrepareJob();
        validateGregorianCalendar("Hour Unit with 3 intervalValue", GregorianCalendar.HOUR_OF_DAY, 3);
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is Minute and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithMinute() throws Exception {
        job.setIntervalUnit(new Minute());
        job.setIntervalValue(4);
        invokePrepareJob();
        validateGregorianCalendar("Minute Unit with 4 intervalValue", GregorianCalendar.MINUTE, 4);
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is Second and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithSecond() throws Exception {
        job.setIntervalUnit(new Second());
        job.setIntervalValue(45);
        invokePrepareJob();
        validateGregorianCalendar("Second Unit with 45 intervalValue", GregorianCalendar.SECOND, 45);
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is DayOfMonth and verify the JobProcessor is ok.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testJobProcessorWithDayOfMonth() throws Exception {
        job.setIntervalUnit(new DayOfMonth(4));
        job.setIntervalValue(2);
        invokePrepareJob();
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1000);
        GregorianCalendar nextRun = job.getStartDate();
        if(nextRun.get(GregorianCalendar.DAY_OF_MONTH) >= 4) {
            nextRun.add(GregorianCalendar.MONTH, 2);
        }
        else {
            nextRun.add(GregorianCalendar.MONTH, 1);
        }
        nextRun.set(GregorianCalendar.DAY_OF_MONTH, 4);
        System.out.println("Test Log : DayOfMonth(4) with 2 intervalValue. The nextRun time you calculated is "
                + job.getNextRun().getTime());
        assertEquals("The nextRun is incorrect.", nextRun, job.getNextRun());
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is DayOfMonth and it is overflow and verify the JobProcessor is ok.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testJobProcessorWithDayOfMonthOverflow() throws Exception {
        job.setIntervalUnit(new DayOfMonth(35));
        job.setIntervalValue(2);
        invokePrepareJob();
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1000);
        GregorianCalendar nextRun = job.getStartDate();
        //nextRun.add(GregorianCalendar.MONTH, 1);
        nextRun.set(GregorianCalendar.DAY_OF_MONTH, nextRun.getMaximum(GregorianCalendar.DAY_OF_MONTH));
        System.out.println("Test Log : DayOfMonth(35) with 2 intervalValue. The nextRun time you calculated is "
                + job.getNextRun().getTime());
        assertEquals("The nextRun is incorrect.", nextRun, job.getNextRun());
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is DayOfYear and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithDayOfYear() throws Exception {
        job.setIntervalUnit(new DayOfYear(4));
        job.setIntervalValue(1);
        invokePrepareJob();
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1000);
        GregorianCalendar nextRun = job.getStartDate();
        if(nextRun.get(GregorianCalendar.DAY_OF_YEAR) > 4) {
            nextRun.add(GregorianCalendar.YEAR, 1);
        }
        nextRun.set(GregorianCalendar.DAY_OF_YEAR, 4);
        System.out.println("Test Log : DayOfYear(4) with 1 intervalValue. The nextRun time you calculated is "
                + job.getNextRun().getTime());
        assertEquals("The nextRun is incorrect.", nextRun, job.getNextRun());
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is DayOfYear and the day is overflow and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithDayOfYearOverflow() throws Exception {
        job.setIntervalUnit(new DayOfYear(366));
        job.setIntervalValue(2);
        invokePrepareJob();
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1000);
        GregorianCalendar nextRun = job.getStartDate();
        nextRun.set(GregorianCalendar.DAY_OF_YEAR, nextRun.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
        //nextRun.add(GregorianCalendar.YEAR, 2);
        System.out.println("Test Log : DayOfYear(366) with 2 intervalValue. The nextRun time you calculated is "
                + job.getNextRun().getTime());
        assertEquals("The nextRun is incorrect.", nextRun, job.getNextRun());
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is DaysOfWeek and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithDaysOfWeek() throws Exception {
        job.setIntervalUnit(new DaysOfWeek(new int[] {1, 2, 3, 4, 5}));
        job.setIntervalValue(2);
        invokePrepareJob();
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1000);
        System.out.println("Test Log : DaysOfWeek(new int[]{1, 2, 3, 4, 5}) with 2 intervalValue. The nextRun time"
                + " you calculated is " + job.getNextRun().getTime());
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is WeekMonthOfYear and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithWeekMonthOfYear() throws Exception {
        job.setIntervalUnit(new WeekMonthOfYear(4, 1, 1));
        job.setIntervalValue(1);
        invokePrepareJob();
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1000);
        GregorianCalendar nextRun = job.getStartDate();
        if(nextRun.get(GregorianCalendar.MONTH) > 0 || nextRun.get(GregorianCalendar.WEEK_OF_MONTH) > 1
                || nextRun.get(GregorianCalendar.DAY_OF_WEEK) > 5) {
            nextRun.add(GregorianCalendar.YEAR, 1);
        }
        nextRun.set(GregorianCalendar.MONTH, 0);
        nextRun.set(GregorianCalendar.WEEK_OF_MONTH, 1);
        nextRun.set(GregorianCalendar.DAY_OF_WEEK, 5);
        System.out.println("Test Log : WeekMonthOfYear(4, 1, 1) with 1 intervalValue. The nextRun time you calculated" +
                " is " + job.getNextRun().getTime());
        assertEquals("The nextRun is incorrect.", nextRun, job.getNextRun());
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is WeekMonthOfYear and it is overflow and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithWeekMonthOfYearOverflow() throws Exception {
        job.setIntervalUnit(new WeekMonthOfYear(5, 1, 12));
        job.setIntervalValue(1);
        invokePrepareJob();
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1000);
        GregorianCalendar nextRun = job.getStartDate();
        if(nextRun.get(GregorianCalendar.MONTH) >= 11 && nextRun.get(GregorianCalendar.DAY_OF_WEEK) > 5) {
            nextRun.add(GregorianCalendar.YEAR, 1);
        }
        nextRun.set(GregorianCalendar.MONTH, 11);
        nextRun.set(GregorianCalendar.WEEK_OF_MONTH, 1);//hard coding.
        nextRun.set(GregorianCalendar.DAY_OF_WEEK, 6);
        System.out.println("Test Log : WeekMonthOfYear(5, 1, 12) with 1 intervalValue. The nextRun time you calculated" +
                " is " + job.getNextRun().getTime() + nextRun.getTime());
        assertEquals("The nextRun is incorrect.", nextRun, job.getNextRun());
    }

    /**
     * <p>
     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
     * </p>
     *
     * <p>
     * It tests the IntervalUnit is WeekOfMonth and verify the JobProcessor is ok.
     * </p>
     * @throws Exception to Junit
     */
    public void testJobProcessorWithWeekOfMonth() throws Exception {
        job.setIntervalUnit(new WeekOfMonth(2, 1));
        job.setIntervalValue(1);
        invokePrepareJob();
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1000);
        GregorianCalendar nextRun = job.getStartDate();
        GregorianCalendar lastRun = nextRun;
        System.out.println(nextRun.getTime());
        
        lastRun.set(GregorianCalendar.WEEK_OF_MONTH, 1);
        lastRun.set(GregorianCalendar.DAY_OF_WEEK, 3);
        if(lastRun.get(GregorianCalendar.DAY_OF_MONTH) > 15) {
            lastRun.add(GregorianCalendar.WEEK_OF_MONTH, 1);
        }
        
        System.out.println("last run is " + lastRun.getTime());
        if(lastRun.before(nextRun)) {
            nextRun.add(GregorianCalendar.MONTH, 1);
            nextRun.set(GregorianCalendar.WEEK_OF_MONTH, 1);
            nextRun.set(GregorianCalendar.DAY_OF_WEEK, 3);
            int day = nextRun.get(GregorianCalendar.DAY_OF_MONTH);
            if (day > 15) {
                nextRun.add(GregorianCalendar.WEEK_OF_MONTH, 1);
            }
        }
        else {
            nextRun = lastRun;
        }
        System.out.println("Test Log : WeekOfMonth(2, 1) with 1 intervalValue. The nextRun time you calculated" +
                " is " + job.getNextRun().getTime() + nextRun.getTime());
        assertEquals("The nextRun is incorrect.", nextRun, job.getNextRun());
    }

//    /**
//     * <p>
//     * Tests the constructor for JobProcessor(Scheduler, int, Log) for accuracy.
//     * </p>
//     *
//     * <p>
//     * It tests the IntervalUnit is WeekOfMonth and it is overflow and verify the JobProcessor is ok.
//     * </p>
//     * @throws Exception to Junit
//     */
//    public void testJobProcessorWithWeekOfMonthOverflow() throws Exception {
//        job.setIntervalUnit(new WeekOfMonth(35, 10));
//        job.setIntervalValue(1);
//        invokePrepareJob();
//        Thread.sleep(500);
//        processor.start();
//        // wait for reload.
//        Thread.sleep(1000);
//        GregorianCalendar nextRun = job.getStartDate();
//        System.out.println(nextRun.getTime());
//        // it should be 2-26
//        nextRun.set(GregorianCalendar.DAY_OF_MONTH, 25);
//        System.out.println("Test Log : WeekOfMonth(1, 10) with 1 intervalValue. The nextRun time you calculated" +
//                " is " + job.getNextRun().getTime());
//        assertEquals("The nextRun is incorrect.", nextRun, job.getNextRun());
//    }

    /**
     * <p>
     * Tests method for getExecutingJobs() for accuracy.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testGetExecutingJobs() throws Exception {
        // after 1 second for execute.
        job.setIntervalUnit(new Second());
        job.setIntervalValue(1);
        job.setActive(true);
        processor.start();
        Thread.sleep(12000);
        assertTrue("The job should be in the executing jobs list.", processor.getExecutingJobs().size() == 1);
    }

    /**
     * <p>
     * Tests method for getExecutingJobs() for accuracy.
     * </p>
     *
     * <p>
     * Verify that each job loaded should have a TriggerEventHandler instance.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testGetHandlerForJob() throws Exception {
        // after 1 second for execute.
        job.setIntervalUnit(new Second());
        job.setIntervalValue(1);
        job.setActive(true);
        // we stop it.
        processor.start();
        Thread.sleep(12000);
        Job job = (Job)processor.getExecutingJobs().get(0);
        assertNotNull("The handle should not be null.", job.getHandlers()[0]);
    }

    /**
     * <p>
     * Tests method for stopJob(String) for accuracy.
     * </p>
     * @throws Exception to Junit
     */
    public void testStopJob() throws Exception {
        // after 1 year for execute.
        job.setIntervalUnit(new Second());
        job.setIntervalValue(2);
        // we stop it.
        processor.start();
        Thread.sleep(12000);
        processor.stopJob(job.getName());
        Thread.sleep(2000);
        assertEquals("The running status is incorrect.", ScheduledEnable.NOT_STARTED, job.getRunningStatus());
    }

    /**
     * <p>
     * Tests method for TriggerEventHandler.handle(Job, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests TriggerEventHandler.handle(Job, String) is correct and depJob is added to the scheduler.
     * </p>
     *
     * @throws Exception to Junit
     */
    /*
    public void testDependentJob() throws Exception {
        depJob.setDependence(new Dependence("depJob", EventHandler.SUCCESSFUL, 60*1000));
        scheduler.addDependentJob(job, depJob);
        // after 1 second for execute.
        job.setIntervalUnit(new Second());
        job.setIntervalValue(2);
        processor.start();
        Thread.sleep(2000);
        assertTrue("The dependent job should be in the executing jobs list.",
                processor.getExecutingJobs().contains(depJob));
    }
    */

    /**
     * <p>
     * Tests method for TriggerEventHandler.handle(Job, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests TriggerEventHandler.handle(Job, String) is correct and depJob is added to the scheduler.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testInactiveJob() throws Exception {
        // after 1 second for execute.
        job.setIntervalUnit(new Second());
        job.setIntervalValue(1);
        job.setActive(false);
        processor.start();
        Thread.sleep(1000);
        assertFalse("The job should be removed from the executing job.",processor.getExecutingJobs().contains(job));
    }

    /**
     * <p>
     * Validates the GregorianCalendar of the Job given the field and the intervalValue.
     * </p>
     *
     * @param description the description to print out.
     * @param field the GregorianCalendar field
     * @param intervalvalue the interval value.
     *
     * @throws Exception to Junit
     */
    private void validateGregorianCalendar(String description, int field, int intervalValue) throws Exception {
        Thread.sleep(500);
        processor.start();
        // wait for reload.
        Thread.sleep(1500);
        GregorianCalendar nextRun = job.getStartDate();
        nextRun.add(field, intervalValue);
        GregorianCalendar next = job.getNextRun();
        System.out.println("Test Log : " + description + "The nextRun time you calculated is " + next.getTime());
        assertEquals("The nextRun is incorrect.", nextRun, next);
    }

    /**
     * <p>
     * Invoke the prepareJob to init the job.
     * </p>
     *
     * @throws Exception if any error happen.
     */
    private void invokePrepareJob() throws Exception {
        Thread.sleep(100);
        Method method = processor.getClass().getDeclaredMethod("prepareJob", new Class[]{Job.class});
        method.setAccessible(true);
        method.invoke(processor, new Object[]{job});
        method.setAccessible(false);
    }
}
