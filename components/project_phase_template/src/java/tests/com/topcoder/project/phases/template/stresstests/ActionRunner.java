/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.stresstests;

/**
 * Run the specific operation in the thread.
 *
 * @author yuanyeyuanye
 * @version 1.0
 */
public class ActionRunner implements Runnable {
    /**
     * Instance of <code>Action</code>.
     */
    private Action action;

    /**
     * Last exception.
     */
    private Exception lastException = null;

    /**
     * Last Error in the thread.
     */
    private Error lastError = null;

    /**
     * Times to execute the specific action.
     */
    private int times;

    /**
     * Create the instance.
     *
     * @param action Action to be invoked.
     * @param times  times to execute the operation.
     */
    public ActionRunner(Action action, int times) {
        this.action = action;
        this.times = times;
    }

    /**
     * Run the specific operation.
     */
    public void run() {
        for (int i = 0; i < times; i++) {
            try {
                action.act();
            } catch (Exception e) {
                this.lastException = e;
            } catch (Error e) {
                this.lastError = e;
            }
        }
    }

    /**
     * Gets the last exception.
     *
     * @return Last exception.
     */
    public Exception getLastException() {
        return this.lastException;
    }

    /**
     * Gets the last error.
     *
     * @return Last exception.
     */
    public Error getLastError() {
        return this.lastError;
    }
}
