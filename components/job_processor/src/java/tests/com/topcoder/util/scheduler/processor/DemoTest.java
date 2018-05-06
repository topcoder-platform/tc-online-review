/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.Second;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Demo for this component. This component is quite simple, the JobProcessor is responsible to load all job from
 * Scheduler, and schedule the jobs to start with their predefined DateUnits and intervals. Here users only need to
 * know how to get the processor start, obtain the executing jobs from it, stop a job and shut down the processor.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DemoTest extends TestCase {
    /**
     * Demo.
     *
     * @throws Exception to junit.
     */
    public void testDemo() throws Exception {
        ConfigManager.getInstance().add("log.xml");

        //here is a predefined Scheduler
        Scheduler scheduler = new MyScheduler();
        //we can add a job to the scheduler so that the processor can load and execute it
        scheduler.addJob(createImmediateJob());

        //defines the reload interval
        int reloadInterval = 15 * 60 * 1000;

        //defines a logger
        Log log = LogManager.getLog();
        
        //creates a processor
        JobProcessor processor = new JobProcessor(scheduler, reloadInterval, log);

        //starts the processor, jobs from scheduler can be automatically scheduled
        processor.start();

        //we can also get those jobs on execution
        List executingJobs = processor.getExecutingJobs();

        //now stop the job
        processor.stopJob("test");

        //and shut down the processor
        processor.shutdown();
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
