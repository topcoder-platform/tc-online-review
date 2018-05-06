/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;

import junit.framework.TestCase;

import java.util.Iterator;


/**
 * Test case for TriggerEventHandler.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class TriggerEventHandlerTest extends TestCase {
    /**
     *Default job name.
     */
    private static final String CMD = "com.topcoder.util.scheduler.processor.MyJob";

    /** Default log used in this test. */
    private Log log;

    /** Default JobProcessor used in this test. */
    private MyJobProcessor processor;

    /** Default TriggerEventHandler used in this test. */
    private TriggerEventHandler handler;

    /**
     * Test {@link TriggerEventHandler#handle(Job, String)} with valid job, the job dependent by this one is
     * expected to schedule successfully.
     *
     * @throws Exception to junit
     */
    public void testHandle() throws Exception {
        //creates a job
        Job job = new Job("test", JobType.JOB_TYPE_JAVA_CLASS, CMD);
        job.start();
        Thread.sleep(MyJob.DEFAULT_RUNTIME);

        //creates a job that depends on the previous one
        Job depJob = new Job("depJob", JobType.JOB_TYPE_JAVA_CLASS, CMD);

        //the depJob will be schedule 1000 milliseconds after job is done
        int delay = 1000;
        depJob.setDependence(new Dependence("test", TriggerEventHandler.SUCCESSFUL, delay));

        //uses mock scheduler to register the job and it's dependent job
        MyScheduler myScheduler = (MyScheduler) processor.getScheduler();
        myScheduler.addJob(job);
        myScheduler.addDependentJob(job, depJob);

        //tolerant error due to CPU execution delay
        long error = 100;

        //the expected start time of depJob is current time plus delay milliseconds
        //it will be recorded out side the handler
        long startTimeExpected = System.currentTimeMillis() + delay;

        //handler start to handle the job, depJob will be retrieved and scheduled
        handler.handle(job, TriggerEventHandler.SUCCESSFUL);

        //the mock processor will remember the last job scheduled, now retrieve it
        Job scheduledJob = processor.getScheduledJob();

        //the last scheduled job should be depJob
        assertEquals("the depJob should be scheduled", depJob.getName(), scheduledJob.getName());

        //the start time scheduled by handler
        long startTime = scheduledJob.getStartDate().getTimeInMillis();

        //the startTime should be the time handle() is invoked, a error tolerance is considered due to
        //CPU execution delay
        assertTrue("the startTime should equal", (startTime - startTimeExpected) <= error);

        //the stop time should be one day after the start time
        long stopTimeExpected = startTimeExpected + (24 * 3600 * 1000);
        long stopTime = scheduledJob.getStopDate().getTimeInMillis();
        //the stop time is asserted in the same manner as startTime, it should equal to the expected value
        assertTrue("the stopTime should equal", (stopTime - stopTimeExpected) <= error);
    }

    /**
     * Test {@link TriggerEventHandler#handle(com.topcoder.util.scheduler.scheduling.Job, String)} with invalid
     * event and IAE is expected.
     */
    public void testHandleInvalidEvent() {
        try {
            handler.handle(new Job("test", JobType.JOB_TYPE_JAVA_CLASS, CMD), "no_such_event");
            fail("event is invalid and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test {@link TriggerEventHandler#handle(com.topcoder.util.scheduler.scheduling.Job, String)} with null
     * event, IAE is expected.
     */
    public void testHandleNullEvent() {
        try {
            handler.handle(new Job("test", JobType.JOB_TYPE_JAVA_CLASS, CMD), null);
            fail("event is null and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test {@link TriggerEventHandler#handle(com.topcoder.util.scheduler.scheduling.Job, String)} with null
     * job, IAE is expected.
     */
    public void testHandleNullJob() {
        try {
            handler.handle(null, TriggerEventHandler.FAILED);
            fail("job is null and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test {@link TriggerEventHandler#TriggerEventHandler(JobProcessor)} with valid processor, it should be
     * instantiated successfully.
     */
    public void testTriggerEventHandler() {
        assertNotNull("TriggerEventHandler should be instantiated successfully", handler);
    }

    /**
     * Test {@link TriggerEventHandler#TriggerEventHandler(JobProcessor)} with null and IAE is expected.
     */
    public void testTriggerEventHandlerNull() {
        try {
            new TriggerEventHandler(null);
            fail("processor is null and IAE is expected");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Sets up test environment.
     *
     * @throws Exception to junit
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigManager.getInstance().add("log.xml");

        log = LogManager.getLog();

        MyScheduler scheduler = new MyScheduler();
        processor = new MyJobProcessor(scheduler, 1000, log);
        this.handler = new TriggerEventHandler(processor);
    }

    /**
     * Clears test environment.
     *
     * @throws Exception to junit
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }
}
