/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * There are three events of a job:
 * </p>
 *
 * <p>
 * <ul>
 * <li>Not Started</li>
 * <li>Executed Successful</li>
 * <li>Executed Failed</li>
 * </ul>
 * </p>
 *
 * <p>
 * The <code>EventHandler</code> is designed to handle these events of jobs.
 * </p>
 *
 * <P>
 * Any class implementing the <code>EventHandler</code> interface can be added to a job at the
 * beginning or the runtime of the <code>Scheduler</code>.
 * </p>
 *
 * <p>
 * Thread Safety : Implementations are not forced to be thread-safe, but they should not block the current thread.
 * </p>
 *
 * @author dmks, singlewood
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 2.0
 */
public interface EventHandler {
    /**
     * <p>
     * Represents the &quot;NOT_STARTED&quot; event of a running job.
     * </p>
     *
     * <p>
     * It means the job fails to execute.
     * </p>
     */
    public static final String NOT_STARTED = "NOT_STARTED";

    /**
     * <p>
     * Represents the &quot;SUCCESSFUL&quot; event of a running job.
     * </p>
     *
     * <p>
     * It means the job executed successfully.
     * </p>
     */
    public static final String SUCCESSFUL = "SUCCESSFUL";

    /**
     * <p>
     * Represents the &quot;FAILED&quot; event of a running job.
     * </p>
     *
     * <p>
     * It means the job executed unsuccessfully.
     * </p>
     */
    public static final String FAILED = "FAILED";

    /**
     * <p>
     * Handles the event of the job.
     * </p>
     *
     * <p>
     * The implementation method should not block current thread.
     * </p>
     *
     * <p>
     * This method should not throw any exception. Log them if necessary.
     * </p>
     *
     * @param job The job raising the event.
     * @param event The event raised by the job
     */
    public void handle(Job job, String event);
}