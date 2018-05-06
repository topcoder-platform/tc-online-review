/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.ScheduledEnable;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.SchedulingException;


/**
 * <p>This class is used to retrieve and run scheduled jobs that have specified scheduled time. It will maintain
 * two running tasks. One for reloading jobs from the {@link Scheduler}, and another for executing them. The current
 * and reloaded jobs will be held in one staging list, whereas executing jobs, or rather copies of them, will be held
 * in another.</p>
 *  <p>Thread safety:This class is mutable but thread-safe as all internal lists are synchronized</p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 1.0
 */
public class JobProcessor {
    /**
     * <p>Interval for the schedule list checking, measured by milliseconds.</p>
     */
    private int executeInternal = 60000;

    /**
     * <p>Represents the milliseconds of one day, 24x3600x1000.</p>
     */
    private static final int ONE_DAY = 86400000;

    /**
     * <p>Represents Job list containing all the running jobs. It will only have Job elements.</p>
     */
    private final List executingJobs = Collections.synchronizedList(new ArrayList());

    /**
     * <p>Represents the jobs to be scheduled. It's a list of Jobs populated in the start method.</p>
     */
    private final List jobs = Collections.synchronizedList(new ArrayList());

    /**
     * <p>Represents the Log that is used to report method entry/exit, as well as errors, in this processor as
     * well as the associated TriggerEventHandlers.</p>
     */
    private final Log log;

    /**
     * <p>Represents the scheduler that will be used to load jobs and other information needed to perform the
     * work.</p>
     */
    private final Scheduler scheduler;

    /**
     * <p>Represents java.util.Timer instance which holding the heartbeat thread to schedule the jobs.</p>
     */
    private Timer executeTimer;

    /**
     * <p>Represents java.util.Timer instance which holding the heartbeat thread to load/reload the jobs from
     * persistence. It will only replace Jobs (by name) if they have been updated, or removes jobs if they are
     * inactive.</p>
     */
    private Timer reloadTimer;

    /**
     * <p>Represents whether the JobProcessor is stopped.</p>
     */
    private boolean isStopped = true;

    /**
     * <p>Represents the interval, in milliseconds, between job reloads from the scheduler. This interval is
     * absolute, so it indicates times between the start of reloads. It is expected that the reloads will be scheduled
     * in intervals much greater than the time it takes to complete a reload. Since it is intended for only timestamp
     * intervals, and not dates, an int is sufficient. It is used in the startReloadHeartBeat method.</p>
     */
    private final int reloadInterval;

    private boolean firstTimeScheduled = true;

    /**
     * <p>
     * Creates a JobProcessor given with scheduler, reloadInterval, and log.
     * </p>
     *
     * @param scheduler The scheduler by which jobs are loaded
     * @param reloadInterval time in milliseconds between successive reloading task executions
     * @param log The Log instance for logging purposes
     * @throws IllegalArgumentException if scheduler or log is null, or reloadInterval <= 0 or is larger than
     * 24 hours
     */
    public JobProcessor(Scheduler scheduler, int reloadInterval, Log log) {

        if (log == null) {
            throw new IllegalArgumentException("log is null");
        }

        this.log = log;

        if (scheduler == null) {
            throw new IllegalArgumentException("scheduler is null");
        }

        if ((reloadInterval <= 0) || (reloadInterval > ONE_DAY)) {
            throw new IllegalArgumentException("reloadInterval should be >0 and <=86400000");
        }

        this.scheduler = scheduler;
        this.reloadInterval = reloadInterval;
    }

    /**
     * <p>Setter for executeInternal.</p>
     * @param executeInternal
     *                  the new internal value to be set.
     *
     * @throws IllegalArgumentException if the executeInterval is not positive.
     */
    public void setExecuteInternal(int executeInternal) {
        if (executeInternal <= 0) {
            throw new IllegalArgumentException("executeInternal should be larger than 0.");
        }
        this.executeInternal = executeInternal;
    }

    /**
     * <p>Gets the executing jobs list. A list of Jobs will be returned.</p>
     *
     * @return the executing jobs list
     */
    public List getExecutingJobs() {
        LogHelper.logEntry(log, "JobProcessor#getExecutingJobs()");
        LogHelper.logExit(log, "JobProcessor#getExecutingJobs()");

        return new ArrayList(this.executingJobs);
    }

    /**
     * <p>Shutdown the processor. Execute and reload timer will be terminated and all jobs will be cleared.</p>
     */
    public void shutdown() {
        LogHelper.logEntry(log, "JobProcessor#shutdown()");

        if (!isStopped) {
            isStopped = true;

            //cancels and releases the timers
            this.executeTimer.cancel();
            this.executeTimer = null;

            this.reloadTimer.cancel();
            this.reloadTimer = null;

            //stops all executing jobs
            synchronized (executingJobs) {
                for (Iterator iter = executingJobs.iterator(); iter.hasNext();) {
                    ((Job) iter.next()).stop();
                }
                executingJobs.clear();
            }

            jobs.clear();
        }

        LogHelper.logExit(log, "JobProcessor#shutdown()");
    }

    /**
     * <p>Starts the processing, reload and execute heart beat will be started and jobs from Scheduler will be
     * automatically loaded and executed. Calling the start() method while it is running has no effect.</p>
     */
    public void start() {
        LogHelper.logEntry(log, "JobProcessor#start()");

        if (isStopped) {
            isStopped = false;

            //flushes the lists since there may be still some jobs that were not cleared on time at the
            //previous shutdown.
            this.jobs.clear();
            this.executingJobs.clear();

            startReloadHeartBeat();
            startExecuteHeartBeat();
        }

        LogHelper.logExit(log, "JobProcessor#start()");
    }

    /**
     * <p>Stop the job with the jobName if it is running. If there are several jobs with the same name exist,
     * all these jobs will be stopped.</p>
     *
     * @param jobName the name of the job to stop
     *
     * @throws IllegalArgumentException if jobName is null or empty string
     */
    public void stopJob(String jobName) {
        LogHelper.logEntry(log, "JobProcessor#stopJob(String jobName)");

        if ((jobName == null) || (jobName.trim().length() == 0)) {
            String msg = "jobName should be not null or empty";
            LogHelper.logError(log, "JobProcessor#stopJob(String jobName)", msg);
            throw new IllegalArgumentException(msg);
        }

        if (!isStopped) {
            synchronized (executingJobs) {
                //since it's possible that a dependent job may be scheduled to execute multi times
                //here we should search through the list to move all the possible jobs with the same name
                for (int i = executingJobs.size() - 1; i >= 0; i--) {
                    Job job = (Job) executingJobs.get(i);

                    if (job.getName().equals(jobName)) {
                        executingJobs.remove(i);
                        job.stop();
                    }
                }
            }
        }

        LogHelper.logExit(log, "JobProcessor#stopJob(String jobName)");
    }

    /**
     * <p>Gets the log instance. This method is package-private and will be used by the associated
     * TriggerEventHandler instances to log.</p>
     *
     * @return The log
     */
    Log getLog() {
        LogHelper.logEntry(log, "JobProcessor#getLog()");
        LogHelper.logExit(log, "JobProcessor#getLog()");

        return log;
    }

    /**
     * <p>Gets the scheduler. Used by the TriggerEventHandler.</p>
     *
     * @return the scheduler
     */
    Scheduler getScheduler() {
        LogHelper.logEntry(log, "JobProcessor#getScheduler()");

        LogHelper.logExit(log, "JobProcessor#getScheduler()");

        return this.scheduler;
    }

    /**
     * <p>Schedules a job for processing. This method is package private, to be used by the TriggerEventHandler
     * to schedule dependent jobs.</p>
     *
     * @param job Job to schedule
     */
    void scheduleJob(Job job) {
        LogHelper.logEntry(log, "JobProcessor#scheduleJob(Job job)");

        if (!isStopped) {
            jobs.add(prepareJob(job));
        }

        LogHelper.logExit(log, "JobProcessor#scheduleJob(Job job)");
    }

    /**
     * Gets the job ready to execute. If a Job is just loaded from Scheduler, it's not ready to execute, since
     * it has not TriggerEventHandler attached with, and the nextRun is also not correct. This method is responsible
     * for fixing these up.
     *
     * @param job the Job to prepare
     *
     * @return the prepared job
     */
    private Job prepareJob(Job job) {
        LogHelper.logEntry(log, "JobProcessor#prepareJob(Job job)");

        GregorianCalendar rightNow = new GregorianCalendar();

        //calculates the nextRun date
        GregorianCalendar nextRun = NextRunCalculator.nextRun(job.getStartDate(), job.getStartTime(),
            rightNow, job.getIntervalUnit(), job.getIntervalValue());
        job.setNextRun(nextRun);

        Format df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        LogHelper.log(log, Level.TRACE, "JobProcessor#prepareJob(Job job)", "The job is to be run on: " + df.format(job.getNextRun().getTime()));

        //add a TriggerEventHandler to the job
        job.addHandler(new TriggerEventHandler(this));

        LogHelper.logExit(log, "JobProcessor#prepareJob(Job job)");

        return job;
    }

    /**
     * <p>Starts the heartbeat thread to schedule the jobs. There are two list in the processor, the jobs list
     * and the executingJobs list. When a job in jobs list is ready to start, a copy of it will be put into
     * executingJobs list. When a job in executingJobs list is completed, first the event will be raised, then, the
     * job is removed from the executingJobs list.</p>
     */
    private void startExecuteHeartBeat() {
        LogHelper.logEntry(log, "JobProcessor#startExecuteHeartBeat()");

        executeTimer = new Timer();
        executeTimer.schedule(new TimerTask() {
                public void run() {
                    LogHelper.logEntry(log, "executeTimerTask#run()");

                    //sifts through jobs list, for expired jobs, removes them, for due jobs, executes them
                    synchronized (jobs) {
                        GregorianCalendar rightNow = new GregorianCalendar();

                        for (int i = jobs.size() - 1; i >= 0; i--) {
                            Job job = (Job) jobs.get(i);
                            GregorianCalendar stopDate = job.getStopDate();

                            int executionCount = job.getExecutionCount();

                            if (((stopDate != null)
                                    && (rightNow.after(stopDate) || job.getNextRun().after(stopDate)))
                                  || (executionCount == 0)) {
                                // the job is expired, or reached its occurrence limit, remove it.
                                jobs.remove(i);
                                LogHelper.log(log, Level.TRACE, "executeTimerTask#run()", job.getName()
                                        + "is expired and removed");
                            } else if (!rightNow.before(job.getNextRun())) {
                                //the job is ready to start, records last run time
                                job.setLastRun(rightNow);

                                //calculates the next execution for the job
                                job.setNextRun(NextRunCalculator.nextRun(job.getStartDate(), job.getStartTime(),
                                    rightNow, job.getIntervalUnit(), job.getIntervalValue()));

                                if (executionCount != -1) {
                                    //if job is not a forever cycled job, decreases executionCount
                                    job.setExecutionCount(executionCount - 1);
                                }

                                //starts the job and adds it into execution jobs list
                                Job executingJob = new Job(job);

                                try {
                                    LogHelper.log(log, Level.TRACE, "executeTimerTask#run()", job.getName()
                                            + "is starting");
                                    executingJob.start();
                                    executingJobs.add(executingJob);
                                } catch (SchedulingException e) {
                                    LogHelper.logError(log, "executeTimerTask#run()", job.getName() + " start failed "
                                            + e.getMessage());
                                }
                            }
                        }
                    } // end of synchronized (jobs)

                    //sifts through executingJobs list, for finished jobs, removes them, and fires event if they
                    //are v2.0 jobs.
                    synchronized (executingJobs) {
                        for (int i = executingJobs.size() - 1; i >= 0; i--) {
                            Job job = (Job) executingJobs.get(i);

                            String status = job.getRunningStatus();
                            //if job is done
                            if (status.equals(ScheduledEnable.SUCCESSFUL) || status.equals(ScheduledEnable.FAILED)) {
                                //for ver2.0 job, fire the event on it
                                //if(job.getJobType() == JobType.)
                                if ((job.getJobType() == JobType.JOB_TYPE_EXTERNAL)
                                        || (job.getRunningJob() instanceof ScheduledEnable)) {
                                    job.fireEvent(status);
                                }

                                //removes the completed job
                                executingJobs.remove(job);
                                LogHelper.log(log, Level.TRACE, "executeTimerTask#run()", job.getName()
                                        + " is done and removed");
                            }
                        }
                    }

                    LogHelper.logExit(log, "executeTimerTask#run()");
                }
            }, 0, executeInternal);

        LogHelper.logExit(log, "JobProcessor#startExecuteHeartBeat()");
    }

    /**
     * <p>Starts the heartbeat thread to reload the jobs. It will get all jobs from the scheduler, then add all
     * new and updated active jobs to the jobs List. The updated jobs will replace current jobs by name. In other
     * words, a job will be updated only if it has been updated since being previously loaded. This check is done by
     * comparing the existing and loaded job with the same name by their modificationDate field. If they differ, then
     * the 'update' proceeds.</p>
     */
    private void startReloadHeartBeat() {
        LogHelper.logEntry(log, "JobProcessor#startReloadHeartBeat()");

        reloadTimer = new Timer();

        //schedule a new task to reload the Jobs data
        reloadTimer.schedule(new TimerTask() {
                public void run() {
                    if (firstTimeScheduled) {
                        LogHelper.log(log, Level.TRACE, "reloadTimerTask#run()", "Going to wait for 10 seconds...");
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ie) {
                            // ignore
                        }
                        firstTimeScheduled = false;
                    }
                    LogHelper.logEntry(log, "reloadTimerTask#run()");

                    //gets all jobs
                    Job[] retrievedJobs = null;
                    try {
                        retrievedJobs = scheduler.getJobList();
                    } catch (SchedulingException se) {
                        LogHelper.logError(log, "reloadTimerTask#run()", se.getMessage());
                    } catch (Exception e) {
                        LogHelper.logError(log, "reloadTimerTask#run()", e.getMessage());
                    }

                    if (retrievedJobs != null) {
                        synchronized (jobs) {
                            //sifts through these jobs to get the ones that will be added.
                            List tempList = new ArrayList();
                            Set existingJobNames = new HashSet();
                            Set newJobNames = new HashSet();

                            for (int i = 0; i < retrievedJobs.length; ++i) {
                                newJobNames.add(retrievedJobs[i].getName());
                            }
                            for (int i = jobs.size()-1; i >= 0; --i) {
                                final String jobName = ((Job) jobs.get(i)).getName();
                                if (!newJobNames.contains(jobName)) {
                                    jobs.remove(i);
                                    LogHelper.log(log, Level.TRACE, "reloadTimerTask#run()", jobName
                                            + "does not exist anymore and removed");
                                } else {
                                    existingJobNames.add(jobName);
                                }
                            }

                            for (int i = 0; i < retrievedJobs.length; i++) {
                                Job retrievedJob = retrievedJobs[i];

                                //dependent job has no start date, should be filtered
                                if (retrievedJob.getStartDate() == null) {
                                    continue;
                                }

                                if (!retrievedJob.getActive()) {
                                    if (existingJobNames.contains(retrievedJob.getName())) {
                                        //removes inactive jobs
                                        jobs.remove(retrievedJob);
                                        LogHelper.log(log, Level.TRACE, "reloadTimerTask#run()", retrievedJob.getName()
                                                + "is inactive and removed");
                                    }
                                } else if (!existingJobNames.contains(retrievedJob.getName())) {
                                    // Adds if job is new
                                    tempList.add(prepareJob(retrievedJob));
                                    LogHelper.log(log, Level.TRACE, "reloadTimerTask#run()", retrievedJob.getName()
                                            + " is loaded");
                                } else {
                                    Job oldJob = (Job) jobs.get(jobs.indexOf(retrievedJob));

                                    // else if the job already exists in the job list and it's modified since last loaded
                                    if (oldJob.getModificationDate() == null ||
                                            !oldJob.getModificationDate().equals(retrievedJob.getModificationDate())) {
                                        tempList.add(prepareJob(retrievedJob));
                                        jobs.remove(oldJob);
                                        LogHelper.log(log, Level.TRACE, "reloadTimerTask#run()", retrievedJob.getName()
                                                + " is updated");
                                    }
                                }
                            }

                            //adds all updated jobs
                            jobs.addAll(tempList);
                        } // end of synchronized
                    }

                    LogHelper.logExit(log, "reloadTimerTask#run()");
                }
            }, 0, reloadInterval);

        LogHelper.logExit(log, "JobProcessor#startReloadHeartBeat()");
    }
}
