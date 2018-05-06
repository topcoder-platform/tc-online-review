/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.log4j;

import org.apache.log4j.Logger;

import com.topcoder.util.log.AbstractLog;
import com.topcoder.util.log.Level;

/**
 * <p>
 * This is the implementation of the <code>Log</code> interface that will write the logging messages to the specified
 * log4j logging system.
 * </p>
 *
 * <p>
 * <b>Changes for version 2.0</b>
 * The following changes have been made:
 * <ol>
 * <li>Class inherits from AbstractLog now.</li>
 * <li>Configuration 'stuff' was removed.</li>
 * <li>Removed the currentLevel variable and it's usage.</li>
 * <li>Removed the prior static fields (no longer needed).</li>
 * <li>Constructor changed to package private.</li>
 * <li>Constructor calls super constructor to store name and was simplified.</li>
 * <li>The log method was changed to package private and it's method signature changed to match the new abstract method
 *  from AbstractLog.</li>
 * <li>The log met hod calls the appropriate logging method based on whether an exception was passed or not.</li>
 * <li>Overrides the log message object method in abstract log to provide an implementation specific version if we are
 *  using log4j object formatting.</li>
 * <li>Made the logger variable final.</li>
 * <li>Fixed error in level mapping (finest/trace not mapped to trace).</li>
 * </ol>
 * </p>
 * <p>
 * This <code>Log</code> implementation can be used to log information to the java logging system.  This class can only
 * be created by the <code>Log4jLogFactory</code>. Log4j has the native ability to format the passed message object
 * via the log4j layout implementation.  A flag has been included to allow this <code>Log</code> implementation to use
 * the log4j Layout rather than the object formatter component in this case.  Please note that this flag only
 * affects the log calls that do not explicitly specify an object formatter to use (in which case, the explicit
 * formatter will be override this flag).
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * This class is thread safe by having no mutable state information. However, the underlying log4j logger may or may
 * not be thread safe and is dependent upon what the application has specified.
 * </p>
 *
 *
 * @author StinkyCheeseMan, TCSDEVELOPER
 * @author adic, ShindouHikaru
 * @author Pops, TCSDEVELOPER
 * @since 1.2
 * @version 2.0
 */
public class Log4jLog extends AbstractLog {

    /**
     * <p>
     * Represents whether the log4j layout will format the object (true) or if the object formatter component will be
     * used to format the object (false).
     * </p>
     *
     * <p>
     * This variable is set in the constructor and is immutable.  This variable will be referenced in the overridden
     * log methods [<code>log(level, throwable, object)</code> or <code>log(level, object)</code>].
     * </p>
     *
     * @since 2.0
     */
    private final boolean useLoggerLayout;

    /**
     * <p>
     * Represents the underlying log4j logger that will be used.
     * </p>
     *
     * <p>
     * This variable is set in the constructor, is immutable (the reference) and cannot be null. This variable is
     * referenced in the logging methods.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * This variable was made final.
     * </p>
     *
     * @since 1.2
     */
    private final Logger logger;

    /**
     * <p>
     * Constructs the Log4jLog implementation from the given name and flag.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * Constructor was made package private and rewritten.
     * </p>
     *
     * <p>
     * Note that if name is null, <code>Logger.getRootLogger()</code> will be used as the underlying log4j logger for
     * this class.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     * @param useLoggerLayout true to use the log4j layout to format the message object or false to use the object
     *  formatter component
     *
     * @since 1.2
     */
    Log4jLog(String name, boolean useLoggerLayout) {
        super(name);
        if (name == null) {
            this.logger = Logger.getRootLogger();
        } else {
            this.logger = Logger.getLogger(name);
        }
        this.useLoggerLayout = useLoggerLayout;
    }

    /**
     * <p>
     * This method checks if a certain logging <code>level</code> is presently enabled and will return true if it is,
     *  false otherwise.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * Eliminated the whacky currentLevel variable.
     * </p>
     *
     * @param level A possibly null level indicating the message level
     *
     * @return true if the <code>level</code> is enabled, false otherwise
     *
     * @since 1.2
     */
    public final boolean isEnabled(Level level) {
        if (level == null) {
            return false;
        }
        return logger.isEnabledFor(getLog4jEquivalentLevel(level));
    }

    /**
     * <p>
     * Logs a message using the given level and object.
     * </p>
     *
     * <p>
     * This method overrides the same method in the AbstractLog to allow log4j to handle the object formatting when
     * specified by useLoggerLayout.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method to this class.
     * </p>
     *
     * <p>
     * If useLoggerLayout is false, the object formatter component will be used to format the object, otherwise the
     * log4j layout will format the object. Note that it will simply return if the level is null or equal to OFF or the
     * log4j level equivalent of the level is not found.
     * </p>
     *
     * @param level A possibly null level indicating the message level
     * @param message A possibly null message object to log
     *
     * @since 2.0
     */
    public void log(Level level, Object message) {
        log(level, (Throwable) null, message);
    }

    /**
     * <p>
     * Logs a message using the given level, throwable and object.
     * </p>
     *
     * <p>
     * This method overrides the same method in the AbstractLog to allow log4j to handle the object formatting when
     * specified by useLoggerLayout.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method to this class.
     * </p>
     *
     * <p>
     * If useLoggerLayout is false, the object formatter component will be used to format the object, otherwise the
     * log4j layout will format the object. Note that it will simply return if the level is null or equal to OFF or the
     * log4j level equivalent of the level is not found.
     * </p>
     *
     * @param level A possibly null level indicating the message level
     * @param t A possibly null throwable containing a stack trace to log
     * @param message A possibly null message object to log
     *
     * @since 2.0
     */
    public void log(Level level, Throwable t, Object message) {
        if (useLoggerLayout) {
            logMessage(level, t, message);
            return;
        }
        super.log(level, t, message);
    }

    /**
     * <p>
     * Logs the given message (and throwable if specified) to the log4j logger.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * Method signature has changed and the method was rewritten.
     * </p>
     *
     * <p>
     * Note that it will simply return if the level is null or equal to OFF or the log4j level equivalent of the level
     * is not found.
     * </p>
     *
     * @param level A possibly null level indicating the message level
     * @param t A possibly null throwable containing a stack trace to log
     * @param message A possibly null, possibly empty (trim'd) string containing the message to log
     *
     * @since 2.0
     */
    protected final void log(Level level, Throwable t, String message) {
        logMessage(level, t, message);
    }

    /**
     * <p>
     * Maps a com.topcoder.util.log.Level object into a org.apache.log4j.Level object.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * Fixed error in mapping for finest and trace (to trace rather than debug).
     * </p>
     *
     * <p>
     * The following mappings will occur (and the appropriate returned):
     * <table>
     * <tr><th>Log Level</th><th>Log4j Level</th></tr>
     * <tr><td>Finest</td><td>Trace</td></tr>
     * <tr><td>Trace</td><td>Trace</td></tr>
     * <tr><td>Debug</td><td>Debug</td></tr>
     * <tr><td>Config</td><td>Info</td></tr>
     * <tr><td>Info</td><td>Info</td></tr>
     * <tr><td>Warn</td><td>Warn</td></tr>
     * <tr><td>Error</td><td>Error</td></tr>
     * <tr><td>Fatal</td><td>Fatal</td></tr>
     * <tr><td>All</td><td>ALL</td></tr>
     * <tr><td>Off</td><td>Off</td></tr>
     * </table>
     * Return null if it's anything else
     * </p>
     *
     * @param level a possibly null com.topcoder.util.log.Level object that will be mapped to a org.apache.log4j.Level
     *  object
     *
     * @return a non-null org.apache.log4j.Level - the mapped level or null if mapping not found
     *
     * @since 1.2
     */
    private final org.apache.log4j.Level getLog4jEquivalentLevel(Level level) {
        if (level.equals(Level.FINEST)) {
            return org.apache.log4j.Level.TRACE;
        } else if (level.equals(Level.TRACE)) {
            return org.apache.log4j.Level.TRACE;
        } else if (level.equals(Level.DEBUG)) {
            return org.apache.log4j.Level.DEBUG;
        } else if (level.equals(Level.CONFIG)) {
            return org.apache.log4j.Level.INFO;
        } else if (level.equals(Level.INFO)) {
            return org.apache.log4j.Level.INFO;
        } else if (level.equals(Level.WARN)) {
            return org.apache.log4j.Level.WARN;
        } else if (level.equals(Level.ERROR)) {
            return org.apache.log4j.Level.ERROR;
        } else if (level.equals(Level.FATAL)) {
            return org.apache.log4j.Level.FATAL;
        } else if (level.equals(Level.ALL)) {
            return org.apache.log4j.Level.ALL;
        } else if (level.equals(Level.OFF)) {
            return org.apache.log4j.Level.OFF;
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Logs the given message (and throwable if specified) to the log4j logger.
     * </p>
     *
     * <p>
     * Note that it will simply return if the level is null or equal to OFF or the log4j level equivalent of the level
     * is not found.
     * </p>
     *
     * @param level A possibly null level indicating the message level
     * @param t A possibly null throwable containing a stack trace to log
     * @param message A possibly null, possibly empty (trim'd) string containing the message to log
     *
     * @since 2.0
     */
    private void logMessage(Level level, Throwable t, Object message) {
        if (level == null || level.equals(Level.OFF)) {
            return;
        }
        org.apache.log4j.Level log4jLevel = getLog4jEquivalentLevel(level);
        if (log4jLevel == null) {
            return;
        }
        // note that log(Priority priority, Object message) is the equivalent of
        // log(Priority priority, Object message, null), please refer to the source.
        logger.log(log4jLevel, message, t);
    }
}
