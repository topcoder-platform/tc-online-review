/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import java.io.File;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.scheduler.scheduling.ScheduledEnable;

/**
 * <p>
 * A simple task for make directory command. Only for testing purpose.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class MakeDirectory implements ScheduledEnable {

    /**
     * Represents the status.
     */
    private String status = NOT_STARTED;

    /**
     * <p>
     * Represents the job name.
     * </p>
     */
    private String jobName;

    /**
     * @see com.topcoder.util.scheduler.scheduling.ScheduledEnable#getMessageData()
     */
    public NodeList getMessageData() {
        return new NodeList(new Node[] {new Field("name", "value", "from junit", true)});
    }

    /**
     * @see com.topcoder.util.scheduler.scheduling.ScheduledEnable#getRunningStatus()
     */
    public String getRunningStatus() {
        return status;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        status = RUNNING;
        try {
            File temp = new File("test_files\\accuracytests\\temp");
            temp.mkdir();
        } catch (Exception e) {
            status = FAILED;
        }
    }

    /**
     * @see com.topcoder.util.scheduler.scheduling.Schedulable#close()
     */
    public void close() {
        status = SUCCESSFUL;
    }

    /**
     * @see com.topcoder.util.scheduler.scheduling.Schedulable#getStatus()
     */
    public String getStatus() {
        return status;
    }

    /**
     * @see com.topcoder.util.scheduler.scheduling.Schedulable#isDone()
     */
    public boolean isDone() {
        return (status.equals(SUCCESSFUL) || status.equals(FAILED));
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
     * @param name The new job name.
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
