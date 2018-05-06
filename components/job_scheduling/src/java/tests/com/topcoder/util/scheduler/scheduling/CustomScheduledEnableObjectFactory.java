/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;

/**
 * This is a simple class implementing the
 * <code>ScheduledEnableObjectFactory</code> used for testing purpose.
 * @author fuyun
 * @version 3.1
 */
public class CustomScheduledEnableObjectFactory implements
        ScheduledEnableObjectFactory {

    /**
     * Represents the flag to indicate if the
     * <code>ScheduledEnableObjectCreationException</code> should be thrown in
     * <code>createScheduledEnableObject()</code> method.
     */
    private boolean throwException;

    /**
     * Creates the <code>SimpleScheduledEnableObjectFactory</code> instance.
     */
    public CustomScheduledEnableObjectFactory() {
    }

    /**
     * Forces the <code>createScheduledEnableObject()</code> method throw
     * <code>ScheduledEnableObjectCreationException</code>.
     */
    public void setThrowException() {
        this.throwException = true;
    }

    /**
     * Returns the <code>SimpleJob</code> instance for testing.
     * @return a <code>ScheduledEnable</code> instance.
     * @throws ScheduledEnableObjectCreationException if
     *             <code>throwException</code> is set <code>true</code>.
     */
    public ScheduledEnable createScheduledEnableObject()
        throws ScheduledEnableObjectCreationException {
        if (throwException) {
            throw new ScheduledEnableObjectCreationException("Test");
        }
        return new CustomScheduledJobRunner();
    }

}
