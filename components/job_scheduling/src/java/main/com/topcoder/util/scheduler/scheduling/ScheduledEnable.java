/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import com.topcoder.util.file.fieldconfig.NodeList;

/**
 * <p>
 * The ScheduledEnable interface is the only required interface for classes run as a job in Job
 * Scheduler 2.0. It extends Schedulable interface(v1.0) and Runnable interface, And forces the
 * implementation classes return running status and message data at runtime.
 * </p>
 *
 * <p>
 * Note: The classes only implementing Schedulable and Runnable interfaces can run in version 2.0.
 * But they can't use the additional functions provided by version 2.0.
 * </p>
 *
 * <p>
 * Thread safety : the implementations of ScheduledEnable should be thread-safe, that means when
 * the job is running, the isDone, getRunningStatus, etc methods can be called in thread-safe way.
 * </p>
 *
 * @author dmks, singlewood
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 2.0
 */
public interface ScheduledEnable extends Runnable, Schedulable {
    /**
     * <p>
     * Represents the successful running status of a job. It means the job executed successfully.
     * </p>
     */
    public static final String SUCCESSFUL = "Successful";

    /**
     * <p>
     * Represents the failed running status of a job. It means the job executed non-successfully.
     * </p>
     */
    public static final String FAILED = "Failed";

    /**
     * <p>
     * Represents the not-started running status of a job. It means the job is not started.
     * </p>
     */
    public static final String NOT_STARTED = "NotStarted";

    /**
     * <p>
     * Represents the "running" running status of a job. It means the job is running.
     * </p>
     */
    public static final String RUNNING = "Running";

    /**
     * <p>
     * Returns the message data of the job at runtime.
     * </p>
     *
     * <p>
     * The message data is used to generate message in Document Generator component.
     * </p>
     *
     * <p>
     * It can return null, however, the Job Scheduler will always add the
     * &lt;JobName&gt;the_job_name&lt;/JobName&gt; node to the node list.
     * </p>
     *
     * @return the message data of the job at runtime.
     */
    public NodeList getMessageData();

    /**
     * <p>
     * Returns the running status of the job at runtime.
     * </p>
     *
     * <p>
     * The returned value must be one of <code>SUCCESSFUL</code>, <code>FAILED</code>, <code>NOTSTARED</code>,
     * and <code>RUNNING</code>.
     * </p>
     *
     * @return the running status of the job at runtime.
     */
    public String getRunningStatus();
}
