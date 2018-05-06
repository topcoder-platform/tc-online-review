/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import com.topcoder.util.exec.Exec;
import com.topcoder.util.exec.ExecutionException;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;

/**
 * <p>
 * This is a test class designed to be loaded with reflection as an
 * internal job and used in the JUnit test cases. Implements ScheduledEnable.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class MyJob implements ScheduledEnable {
    /**
     * <p>
     * Whether the job is done.
     * </p>
     */
    private String status = ScheduledEnable.NOT_STARTED;

    /**
     * <p>
     * Represents the job name.
     * </p>
     */
    private String jobName;

    /**
     * <p>
     * Run the job.
     * </p>
     */
    public void run() {
        for (int j = 0; j < 1000; j++) {
            status = ScheduledEnable.RUNNING;
        }

        try {
            Thread.sleep(10);
            Exec.executeAsynchronously(new String[] {"dir > test_files/testMyJob.txt"});
            status = ScheduledEnable.SUCCESSFUL;
        } catch (InterruptedException e) {
            status = ScheduledEnable.FAILED;
        } catch (IllegalArgumentException e) {
            status = ScheduledEnable.FAILED;
        } catch (ExecutionException e) {
            status = ScheduledEnable.FAILED;
        }
    }

    /**
     * <p>
     * The date and time the job is to start running.
     * </p>
     *
     * @return true if the job is done, otherwise false.
     */
    public boolean isDone() {
        return (status.equals(ScheduledEnable.SUCCESSFUL) || status.equals(ScheduledEnable.FAILED));
    }

    /**
     * <p>
     * Return the status of the job.
     * </p>
     *
     * @return the status of the job.
     */
    public String getStatus() {
        return status;
    }

    /**
     * <p>
     * Stop the job.
     * </p>
     */
    public void close() {
        status = ScheduledEnable.SUCCESSFUL;
    }

    /**
     * <p>
     * Get message data.
     * </p>
     *
     * @return NodeList
     */
    public NodeList getMessageData() {
        Node[] nodes = new Node[] {new Field("name1", "value1", "desp1", true),
            new Field("name2", "value2", "desp2", true), new Field("name3", "value3", "desp3", true),
            new Field("name4", "value4", "desp4", true)};

        return new NodeList(nodes);
    }

    /**
     * <p>
     * Get job running status.
     * </p>
     *
     * @return status
     */
    public String getRunningStatus() {
        return status;
    }

    /**
     * <p>
     * Gets the job name.
     * </p>
     *
     * @return the job name.
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * <p>
     * Sets the job name.
     * </p>
     *
     * @param jobName The new job name.
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}