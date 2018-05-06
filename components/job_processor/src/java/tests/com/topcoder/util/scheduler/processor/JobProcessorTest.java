/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Second;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;


/**
 * Test case for JobProcessor.
 *
 * @author TCSDEVELOPER
 * @version 1.0
  */
public class JobProcessorTest extends TestCase {
    /** Default JobProcessor used in this test. */
    private JobProcessor processor;

    /** Default log used in this test. */
    private Log log;

    /**
     *Default Scheduler used in this test.
     */
    private MyScheduler scheduler;

    /**
     * Test {@link JobProcessor#getExecutingJobs()}. In this test, a Job,is scheduled to run immediately, and
     * will stop 4 seconds after. Before it goes stop, getExecutingJobs() will be invoked to obtain the executing
     * jobs. The specified job is expected to be in the list.
     *
     * @throws Exception to junit
     */
    public void testGetExecutingJobs() throws Exception {
        //creates a immediately run job
        Job job = createImmediateJob();

        //adds to scheduler
        scheduler.addJob(job);

        //start process, the job will be started immediately
        processor.start();
        //sleep for a while, but not exceeds the job's finish
        Thread.sleep(12000);

        //now obtains the executing jobs list
        List jobs = processor.getExecutingJobs();

        assertNotNull("getExecutingJobs() should never return null", jobs);
        assertEquals("only one job is executing", 1, jobs.size());

        Job theJob = (Job) jobs.get(0);
        assertEquals("the executing job should equal to", job.getName(), theJob.getName());
    }

    /**
     * Test {@link JobProcessor#getLog()}, the log should be obtained.
     */
    public void testGetLog() {
        assertEquals("log should equal", log, processor.getLog());
    }

    /**
     * Test {@link JobProcessor#getScheduler()}, the scheduler should be obtained.
     */
    public void testGetScheduler() {
        assertEquals("scheduler should equal", scheduler, processor.getScheduler());
    }

    /**
     * Test {@link JobProcessor#JobProcessor(com.topcoder.util.scheduler.scheduling.Scheduler, int, Log)} with
     * valid arguments, it should be instantiated successfully.
     */
    public void testJobProcessor() {
        assertNotNull("process should be instantiated successfully", processor);
    }

    /**
     * Test {@link JobProcessor#JobProcessor(com.topcoder.util.scheduler.scheduling.Scheduler, int, Log)} with
     * invalid interval and IAE is expected.
     */
    public void testJobProcessorInvalidInterval() {
        try {
            new JobProcessor(scheduler, 0, log);
            fail("interval is invalid and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test {@link JobProcessor#JobProcessor(com.topcoder.util.scheduler.scheduling.Scheduler, int, Log)} with
     * null log and IAE is expected.
     */
    public void testJobProcessorNullLog() {
        try {
            new JobProcessor(scheduler, 1000, null);
            fail("log is null and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test {@link JobProcessor#JobProcessor(com.topcoder.util.scheduler.scheduling.Scheduler, int, Log)} with
     * null scheduler and IAE is expected.
     */
    public void testJobProcessorNullScheduler() {
        try {
            new JobProcessor(null, 1000, log);
            fail("scheduler is null and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test {@link JobProcessor#scheduleJob(Job)}. This will test the method indirectly by getExecutingJobs().
     * First the processor is started, and a job is scheduled into it by scheduleJob(), after a while, the job should
     * be started.
     *
     * @throws Exception to junit
     */
    public void testScheduleJob() throws Exception {

        //creates a immediately run job
        Job job = createImmediateJob();

        processor.getScheduler().addJob(job);
        processor.start();
        processor.scheduleJob(job);

        //sleep for a while, but not exceeds the job's finish
        Thread.sleep(2000);

        //now obtains the executing jobs list
        List jobs = processor.getExecutingJobs();

        //the job should exist
        assertNotNull("getExecutingJobs() should never return null", jobs);
        assertEquals("only one job is executing", 1, jobs.size());

        Job theJob = (Job) jobs.get(0);
        assertEquals("the executing job should equal to", job.getName(), theJob.getName());
    }

    /**
     * Test {@link JobProcessor#shutdown()}.
     */
    public void testShutdown() {
        processor.shutdown();

        //success
    }

    /**
     * Test {@link JobProcessor#start()}.
     */
    public void testStart() {
        processor.start();

        //success
    }

    /**
     * Test {@link JobProcessor#stopJob(String)}. In this test, a Job,is scheduled to run immediately, and will
     * stop 4 seconds after. Before it goes stop, stopJob() is called to stop it, the obtained executing jobs list
     * should not contain the job.
     *
     * @throws Exception to junit
     */
    public void testStopJob() throws Exception {
        //creates a immediately run job
        Job job = createImmediateJob();

        //adds to scheduler
        scheduler.addJob(job);

        //start process, the job will be started immediately
        processor.start();
        //sleep for a while, but not exceeds the job's finish
        Thread.sleep(12000);

        //now obtains the executing jobs list
        List jobs = processor.getExecutingJobs();

        //the job should exist
        assertNotNull("getExecutingJobs() should never return null", jobs);
        assertEquals("only one job is executing", 1, jobs.size());

        Job theJob = (Job) jobs.get(0);
        assertEquals("the executing job should equal to", job.getName(), theJob.getName());

        //stop the job
        processor.stopJob(job.getName());
        jobs = processor.getExecutingJobs();
        assertTrue("not job should be in running state now", jobs.size() == 0);
    }

    /**
     * Test {@link JobProcessor#stopJob(String)} with empty string, IAE is expected.
     */
    public void testStopJobEmpty() {
        try {
            processor.stopJob(" ");
            fail("job name is empty and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test {@link JobProcessor#stopJob(String)} with null, IAE is expected.
     */
    public void testStopJobNull() {
        try {
            processor.stopJob(null);
            fail("job name is null and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Sets up test environment.
     *
     * @throws Exception to juint
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigManager.getInstance().add("log.xml");

        log = LogManager.getLog();

        scheduler = new MyScheduler();
        processor = new JobProcessor(scheduler, 1000, log);
        processor.setExecuteInternal(1000);
    }

    /**
     * Clears test environment.
     *
     * @throws Exception to juint
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        processor.shutdown();
        //wait for the processor to shutdown gracefully
        Thread.sleep(20);

        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }

    /**
     * Creates a job which will execute immediately after it is added to processor, and only execute one time.
     *
     * @return predefined Job
     */
    private Job createImmediateJob() {
        Job job = new Job("test", JobType.JOB_TYPE_JAVA_CLASS, "com.topcoder.util.scheduler.processor.MyJob");
        GregorianCalendar startDate = new GregorianCalendar();
        job.setStartDate(startDate);
        job.setModificationDate(startDate);

        GregorianCalendar stopDate = (GregorianCalendar) startDate.clone();
        stopDate.add(Calendar.HOUR, 1);
        job.setActive(true);
        job.setStopDate(stopDate);
        job.setIntervalUnit(new Second());
        job.setIntervalValue(1);
        job.setRecurrence(1);

        return job;
    }
}
