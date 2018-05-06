/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.topcoder.util.format.ObjectFormatter;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

/**
 * <p>
 * Mock logger that extends Logger and implements Log interface tests.
 * </p>
 *
 * @author dingying131
 * @version 1.0
 */
public class MockLogger extends Logger implements Log {

    /**
     * <p>
     * The level queue for log wrapper.
     * </p>
     */
    private Queue<Level> logLevel = new LinkedList<Level>();

    /**
     * <p>
     * The level queue for log wrapper.
     * </p>
     */
    private Queue<Priority> logPriority = new LinkedList<Priority>();

    /**
     * <p>
     * The level queue for log wrapper.
     * </p>
     */
    private Queue<Object> logObject = new LinkedList<Object>();

    /**
     * <p>
     * Constructor that does nothing.
     * </p>
     */
    public MockLogger() {
        super("test");
    }

    /**
     * <p>
     * Gets the last log level.
     * </p>
     *
     * @return the logLevel
     */
    public Level getLogLevel() {
        return logLevel.poll();
    }

    /**
     * <p>
     * Gets the last log priority.
     * </p>
     *
     * @return the logLevel
     */
    public Priority getLogPriority() {
        return logPriority.poll();
    }

    /**
     * <p>
     * Gets the last log message.
     * </p>
     *
     * @return the logObject
     */
    public Object getLogObject() {
        return logObject.poll();
    }

    /**
     * <p>
     * Override method that always returns false.
     * </p>
     *
     * @param arg0
     *            the log level
     * @return always return false
     */
    public boolean isEnabled(Level arg0) {
        return false;
    }

    /**
     * <p>
     * Logs the message and in specified level.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     */
    public void log(Level level, Object arg1) {
        this.logLevel.offer(level);
        this.logObject.offer(arg1);
    }

    /**
     * <p>
     * Logs the message at given level.
     * </p>
     * @param pro the log level
     * @param arg1 the message to be logged
     */
    public void log(Priority pro, Object arg1) {
        this.logPriority.offer(pro);
        this.logObject.offer(arg1);
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     */
    public void log(Level level, Object arg1, ObjectFormatter arg2) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     */
    public void log(Level level, String arg1, Object arg2) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     */
    public void log(Level level, String arg1, Object[] arg2) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     */
    public void log(Level level, Throwable arg1, Object arg2) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     * @param arg3
     *            not used
     */
    public void log(Level level, String arg1, Object arg2, Object arg3) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     * @param arg3
     *            not used
     */
    public void log(Level level, Throwable arg1, Object arg2, ObjectFormatter arg3) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     * @param arg3
     *            not used
     */
    public void log(Level level, Throwable arg1, String arg2, Object arg3) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     * @param arg3
     *            not used
     */
    public void log(Level level, Throwable arg1, String arg2, Object[] arg3) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     * @param arg3
     *            not used
     * @param arg4
     *            not used
     */
    public void log(Level level, String arg1, Object arg2, Object arg3, Object arg4) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     * @param arg3
     *            not used
     * @param arg4
     *            not used
     */
    public void log(Level level, Throwable arg1, String arg2, Object arg3, Object arg4) {
    }

    /**
     * <p>
     * Mock method that never used.
     * </p>
     *
     * @param level
     *            not used
     * @param arg1
     *            not used
     * @param arg2
     *            not used
     * @param arg3
     *            not used
     * @param arg4
     *            not used
     * @param arg5
     *            not used
     */
    public void log(Level level, Throwable arg1, String arg2, Object arg3, Object arg4, Object arg5) {
    }

}
