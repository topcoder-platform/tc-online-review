/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;

/**
 * <p>
 * A Mock implementation of <code>{@link EventHandler}</code>. Only for testing purpose.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class MockEventHandler implements EventHandler {
    /**
     * Represents the job.
     */
    private Job job;

    /**
     * Represents the event.
     */
    private String event;

    /**
     * @see com.topcoder.util.scheduler.scheduling.EventHandler#handle
     *      (com.topcoder.util.scheduler.scheduling.Job, java.lang.String)
     */
    public void handle(Job job, String event) {
        this.job = job;
        this.event = event;
    }

    /**
     * Gets the event.
     *
     * @return the event.
     */
    public String getEvent() {
        return event;
    }

    /**
     * Gets the job.
     *
     * @return the job.
     */
    public Job getJob() {
        return job;
    }

}
