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
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.processor.JobProcessor;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Second;

import junit.framework.TestCase;

/**
 * The stress test for Job Processor component. In each case a bunch of jobs will be added to the processor, with a
 * dependence job attached to each single job. Later the number of jobs totally run will be verified.
 * @author zhuzeyuan
 * @version 1.0
 */
public class TriggerStressTest extends TestCase {

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
    private MyScheduler scheduler = new MyScheduler();

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
        System.gc();
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
     * A bunch of jobs will be added to the processor, with a dependence job attached to each single job. Later the
     * number of jobs totally run will be verified.
     * @throws Exception
     */
    public void testTrigger() throws Exception {
        // Prepare a random generator
        Random ran = new Random(-1);

        jobs = new Job[TIMES];
        for (int i = 0; i < jobs.length; i++) {
            jobs[i] = new Job("job" + i, JobType.JOB_TYPE_JAVA_CLASS,
                "com.topcoder.util.scheduler.processor.stresstests.MyJob");
            jobs[i].setModificationDate(new GregorianCalendar());
            jobs[i].setActive(true);
            jobs[i].setRecurrence(1);
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

            Job depJob = new Job("depJob" + i, JobType.JOB_TYPE_JAVA_CLASS,
                "com.topcoder.util.scheduler.processor.stresstests.MyJob");
            depJob.setModificationDate(new GregorianCalendar());
            depJob.setActive(true);
            depJob.setRecurrence(1);
            depJob.setDependence(new Dependence(depJob.getName(), EventHandler.SUCCESSFUL, 100));
            scheduler.addDependentJob(jobs[i], depJob);
            System.out.println("added");
        }

        processor = new JobProcessor(scheduler, 1000, LogManager.getLog());
        processor.setExecuteInternal(1000);

        MyJob.COUNT = 0;
        processor.start();

        Thread.sleep(1000 * 20);

        assertEquals("dependent jobs not started", TIMES * 2, MyJob.COUNT);
    }
}
