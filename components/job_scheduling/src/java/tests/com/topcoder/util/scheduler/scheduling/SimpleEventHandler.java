/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * This is a class implements EventHandler interface.
 * It is only used for testing.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class SimpleEventHandler implements EventHandler {
    /**
     * <p>
     * Represents the handle(Job, String) method is executed or not.
     * </P>
     */
    private boolean isExecute = false;

    /**
     * <p>
     * Implements the handle(Job, String) method.
     * </p>
     *
     * @param job The job raising the event.
     * @param event The event raised by the job
     */
    public void handle(Job job, String event) {
        isExecute = true;

    }

    /**
     * <p>
     * Returns the state of the isExecute for testing.
     * </p>
     *
     * @return the state of the isExecute for testing.
     */
    public boolean getIsExecute() {
        return isExecute;
    }
}
