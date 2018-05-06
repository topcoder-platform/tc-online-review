/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;


/**
 * <p>Helper class to provide logging functions. If a external logger is provided, the methods will log the message
 * into the given logger, otherwise they will use System.out to output the log message, which is useful especially the
 * external logger is failed to initialized.</p>
 *  <p>Thread safety:This class is thread-safe.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
final class LogHelper {
    /**
     * Private ctor preventing this class from being instantiated.
     */
    private LogHelper() {
    }

    /**
     * Log the message with specified level to the given logger or System.out if the logger is null.
     *
     * @param log external logger to log the message, if null, message will be logged into System.out
     * @param level error level
     * @param method the method who invokes this log method
     * @param obj the message to log
     */
    public static void log(Log log, Level level, String method, Object obj) {
        StringBuffer sb = new StringBuffer(level.toString()).append("-").append(method).append(":").append(obj);

        if (log != null) {
            log.log(level, sb);
        } else {
            System.out.println(sb);
        }
    }

    /**
     * Log an entry event message.
     *
     * @param log external logger to log the message, if null, message will be logged into System.out
     * @param method the method who invokes this log method
     */
    public static void logEntry(Log log, String method) {
        log(log, Level.TRACE, method, "enter");
    }

    /**
     * Log an error event message.
     *
     * @param log external logger to log the message, if null, message will be logged into System.out
     * @param method the method who invokes this log method
     * @param obj error message
     */
    public static void logError(Log log, String method, Object obj) {
        log(log, Level.ERROR, method, obj);
    }

    /**
     * Log an exit event message.
     *
     * @param log external logger to log the message, if null, message will be logged into System.out
     * @param method the method who invokes this log method
     */
    public static void logExit(Log log, String method) {
        log(log, Level.TRACE, method, "exit");
    }
}
