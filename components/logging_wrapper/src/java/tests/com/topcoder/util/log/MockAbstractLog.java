/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

/**
 * <p>
 * Mock class for {@link AbstractLog} class used for testing AbstractLog class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class MockAbstractLog extends AbstractLog {

    /**
     * <p>
     * Represents the Level instance for logging message.
     * </p>
     */
    private Level level = null;

    /**
     * <p>
     * Represents the Throwable instance for logging message.
     * </p>
     */
    private Throwable cause = null;

    /**
     * <p>
     * Represents the message to log.
     * </p>
     */
    private String message = null;

    /**
     * <p>
     * Constructs a MockAbstractLog.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     */
    protected MockAbstractLog(String name) {
        super(name);
    }

    /**
     * <p>
     * Logs the given message with the given level and cause.
     * </p>
     *
     * <p>
     * Note that it just simply saves the arguments.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param message A possibly null, possibly empty (trim'd) string representing the logging message
     */
    protected void log(Level level, Throwable cause, String message) {
        this.level = level;
        this.cause = cause;
        this.message = message;
    }

    /**
     * <p>
     * Returns whether the given level is enabled for a specific implementation.
     * </p>
     *
     * <p>
     * Note that it will always return true.
     * </p>
     *
     * @param level A possibly null level to check
     *
     * @return true if the level is enabled, false otherwise;
     */
    public boolean isEnabled(Level level) {
        if (level == null) {
            return false;
        }
        return level.intValue() > Level.INFO.intValue();
    }

    /**
     * <p>
     * Gets the cause of the logging message.
     * </p>
     *
     * @return the cause of the logging message
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * <p>
     * Gets the level of the logging message.
     * </p>
     *
     * @return the level of the logging message
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @return the message
     */
    /**
     * <p>
     * Gets the message to log.
     * </p>
     *
     * @return the message to log
     */
    public String getMessage() {
        return message;
    }
}
