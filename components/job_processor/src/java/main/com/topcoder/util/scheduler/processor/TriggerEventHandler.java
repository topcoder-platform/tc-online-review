/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.log.Log;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Schedulable;
import com.topcoder.util.scheduler.scheduling.ScheduledEnable;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.Year;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * <p>
 * An EventHandler used to start the jobs which are dependent on the completion of the job. The jobs can be
 * configured to be dependent on the success or failure or both of another job. Because every job can be
 * dependence of another job, this event handler will be added to every version 2.0 compatible job.
 * Note: version 2.0 compatible job is either an external job or a job class which implements ScheduledEnable
 * interface.
 * </p>
 * <p>
 * Thread safe: It is immutable and thread-safe.
 * </p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 1.0
  */
class TriggerEventHandler implements EventHandler {
    /**
     * <p>Represents the milliseconds of one day, 24x3600x1000.</p>
     */
    private static final int ONE_DAY = 86400000;

    /**
     * <p>Represents the processor the trigger will need for processing.</p>
     */
    private final JobProcessor processor;

    /**
     * <p>Represents the logger for this handler to log message.</p>
     */
    private final Log log;

    /**
     * <p>
     * Creates a new TriggerEventHandler given with a JobProcessor.
     * </p>
     *
     * @param processor The processor
     * @throws IllegalArgumentException if processor is null
     */
    public TriggerEventHandler(JobProcessor processor) {
        log = (processor == null) ? null : processor.getLog();

        LogHelper.logEntry(log, "TriggerEventHandler#TriggerEventHandler(JobProcessor processor)");

        if (processor == null) {
            String msg = "processor is null";
            LogHelper.logError(log, "TriggerEventHandler#TriggerEventHandler(JobProcessor processor)", msg);
            throw new IllegalArgumentException(msg);
        }

        this.processor = processor;

        LogHelper.logExit(log, "TriggerEventHandler#TriggerEventHandler(JobProcessor processor)");
    }

    /**
     * <p>Starts every job dependent on the event of handled job.</p>
     *
     * @param job the job raised the event.
     * @param event the event raised by the job
     *
     * @throws IllegalArgumentException if any parameter is null or event is not one of the three events of Job
     */
    public void handle(Job job, String event) {
        LogHelper.logEntry(log, "TriggerEventHandler#handle(Job job, String event)");

        if (job == null) {
            String msg = "job is null";
            LogHelper.logError(log, "TriggerEventHandler#handle(Job job, String event)", msg);
            throw new IllegalArgumentException(msg);
        }

        if (event == null) {
            String msg = "event is null";
            LogHelper.logError(log, "TriggerEventHandler#handle(Job job, String event)", msg);
            throw new IllegalArgumentException(msg);
        }

        if (!(FAILED.equalsIgnoreCase(event) || SUCCESSFUL.equalsIgnoreCase(event) ||
                NOT_STARTED.equalsIgnoreCase(event))) {
            String msg = "event is unrecognized:" + event;
            LogHelper.logError(log, "TriggerEventHandler#handle(Job job, String event)", msg);
            throw new IllegalArgumentException(msg);
        }

        if (!event.equalsIgnoreCase(EventHandler.NOT_STARTED)) {
            // get all the dependent jobs and start the jobs if the dependence matches.
            Job[] jobs = new Job[0];

            // Ver1.0 jobs have no dependence.
            if ((job.getJobType() == JobType.JOB_TYPE_EXTERNAL)
                                        || (job.getRunningJob() instanceof ScheduledEnable) ) {
                try {
                    jobs = processor.getScheduler().getAllDependentJobs(job);
                } catch (SchedulingException e) {
                    LogHelper.logError(log, "TriggerEventHandler#handle(Job job, String event)", e);
                }
            }

            for (int i = 0; i < jobs.length; i++) {
                Job depJob = jobs[i];
                Dependence dependence = depJob.getDependence();
                String triggerEvent = dependence.getDependentEvent();
                if (Dependence.BOTH.equals(triggerEvent) || triggerEvent.equalsIgnoreCase(event)) {
                    Job sjob = prepareDepJob(depJob);
                    try {
                        processor.getScheduler().addJob(sjob);
                    } catch (SchedulingException e) {
                        LogHelper.logError(log, "TriggerEventHandler#handle(Job job, String event)", e);
                    }
                    processor.scheduleJob(sjob);
                } else {
                    // if the depJob is not triggered to started, the NOT_STARTED
                    // event will be raised.
                    depJob.fireEvent(EventHandler.NOT_STARTED);
                }
            }
        }

        LogHelper.logExit(log, "TriggerEventHandler#handle(Job job, String event)");
    }

    /**
     *Prepares a job, the start/end time, DateUnit, interval will be set to this Job so that it can be scheduled by
     *the processor.
     *@param depJob dependent job
     *@return the job prepared
     */
    private Job prepareDepJob(Job depJob) {
        // set up the start/end time of the job to ensure the job starts to
        // execute in the depJob.getDependence().getDelay() milliseconds and
        // execute only once.
        Job sjob = new Job(depJob);
        int delay = depJob.getDependence().getDelay();

        GregorianCalendar startTime = new GregorianCalendar();

        startTime.add(Calendar.MILLISECOND, delay);

        sjob.setStartDate(startTime);

        //calculates how many milliseconds have elapsed since start of today
        GregorianCalendar startOfToday = (GregorianCalendar) startTime.clone();
        startOfToday.set(Calendar.HOUR_OF_DAY, 0);
        startOfToday.set(Calendar.MINUTE, 0);
        startOfToday.set(Calendar.SECOND, 0);
        startOfToday.set(Calendar.MILLISECOND, 0);
        sjob.setStartTime((int) (startTime.getTimeInMillis() - startOfToday.getTimeInMillis()));

        //delay will never be more than 24 hours, this will ensure that stopDate will in fact not
        //occur before the job is to start! And also, it makes sure the job is only run once.
        GregorianCalendar stopDate = (GregorianCalendar) startTime.clone();
        stopDate.add(Calendar.MILLISECOND, ONE_DAY);
        sjob.setStopDate(stopDate);

        sjob.setIntervalUnit(new Year());
        sjob.setIntervalValue(1);
        sjob.setRecurrence(1);
        return sjob;
    }
}
