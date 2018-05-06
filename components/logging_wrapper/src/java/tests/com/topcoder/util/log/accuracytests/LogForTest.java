/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.AbstractLog;
import com.topcoder.util.log.Level;

/**
 * <p>
 * A mock class extends <code>AbstractLog</code> used for testing.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
class LogForTest extends AbstractLog {
    /**
     * <p>
     * Indicates whether the message has been logged.
     * </p>
     */
    private boolean isLogged;

    /**
     * <p>
     * Constructor.
     * </p>
     *
     * @param name
     *            A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     */
    public LogForTest(String name) {
        super(name);
    }

    /**
     * <p>
     * Returns whether the given level is enabled for a specific implementation.
     * </p>
     *
     * @return false if level is null, true otherwise
     * @param level
     *            A possibly null level to check
     */
    public boolean isEnabled(Level level) {
        return level == null ? false : true;
    }

    /**
     * <p>
     * Log the message.
     * </p>
     *
     * @param level
     *            A possibly null level at which the message should be logged
     * @param cause
     *            A possibly null throwable describing an error that occurred
     * @param message
     *            A possibly null, possibly empty (trim'd) string representing the logging message
     */
    protected void log(Level level, Throwable cause, String message) {
        isLogged = true;
    }

    /**
     * <p>
     * Getter of <tt>isLogged</tt>.
     * This will reset the flag.
     * </p>
     *
     * @return isLogged
     */
    public boolean isLogged() {
        boolean logged = this.isLogged;
        this.isLogged = false;
        return logged;
    }
}
