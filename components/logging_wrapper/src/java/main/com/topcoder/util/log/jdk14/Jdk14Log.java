/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.jdk14;

import com.topcoder.util.log.AbstractLog;
import com.topcoder.util.log.Level;
import java.util.logging.Logger;

/**
 * <p>
 * This is the implementation of the <tt>Log</tt> interface that will write the logging messages to the specified java
 * logging system.
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
 * <li>The log method was changed to package private and its method signature changed to match the new abstract
 *  method from AbstractLog.</li>
 * <li>The log method calls the appropriate logging method based on whether an exception was passed or not.</li>
 * <li>Overrides the message formatting method in abstract log to provide an implementation specific version.</li>
 * <li>Made the logger variable final.</li>
 * </ol>
 * </p>
 *
 * <p>
 * This Log implementation can be used to log information to the java logging system.  This class can only be
 * created by the {@link Jdk14LogFactory}.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * This class is thread safe by having no mutable state information. However, the underlying java logger may or may
 * not be thread safe and is dependent upon what the application has specified.
 * </p>
 *
 * @author StinkyCheeseMan, TCSDEVELOPER
 * @author adic, ShindouHikaru
 * @author Pops, TCSDEVELOPER
 * @since 1.2
 * @version 2.0
 */
public class Jdk14Log extends AbstractLog {

    /**
     * <p>
     * Represents the underlying java logger that will be used.
     * </p>
     *
     * <p>
     * This variable is set in the constructor, is immutable (the reference) and cannot be null.  This variable is
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
     * Constructs the Jdk14Log implementation from the given name.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * Constructor was made package private and rewritten.
     * </p>
     *
     * <p>
     * This constructor will:
     * <ol>
     * <li>Call the super constructor with the name.</li>
     * <li>Call the Logger.getLogger(name) if the name is not null or Logger.getLogger("") if the name is null.</li>
     * <li>The returned logger is then saved to the logger variable.</li>
     * </ol>
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     *
     * @since 1.2
     */
    Jdk14Log(String name) {
        super(name);
        if (name == null) {
            name = "";
        }
        this.logger = Logger.getLogger(name);
    }

    /**
     * <p>
     * Checks if a certain logging <code>level</code> is presently enabled and will return true if it is, false
     * otherwise.
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
        return logger.isLoggable(getJdk14EquivalentLevel(level));
    }

    /**
     * <p>
     * Logs a message using the given format and single argument.
     * </p>
     *
     * <p>
     * This method overrides the same method in the <code>AbstractLog</code> to provide an implementation specific
     * version of it.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method to this class.
     * </p>
     *
     * @param level A possibly null level indicating the message level
     * @param messageFormat A possibly null, possibly empty (trim'd) string containing the message format to log
     * @param arg1 A possibly null argument for the message format
     *
     * @since 2.0
     */
    public void log(Level level, String messageFormat, Object arg1) {
        log(level, messageFormat, new Object[]{arg1});
    }

    /**
     * <p>
     * Logs a message using the given format and an array of arguments.
     * </p>
     *
     * <p>
     * This method overrides the same method in the <code>AbstractLog</code> to provide an implementation specific
     * version of it.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method to this class.
     * </p>
     *
     * <p>
     * Note that it will simply return if the messageFormat is null, or the level is null or equal to OFF or the jdk14
     * level equivalent of the level is not found.
     * </p>
     *
     * @param level A possibly null level indicating the message level
     * @param messageFormat A possibly null, possibly empty (trim'd) string containing the message format to log
     * @param args A possibly null or empty array of arguments for the message format
     *
     * @since 2.0
     */
    public void log(Level level, String messageFormat, Object[] args) {
        if (messageFormat == null) {
            return;
        }
        // get the jdk14 level equivalent of the level
        java.util.logging.Level jdkLevel = getJdk14Level(level);
        if (jdkLevel == null) {
            return;
        }
        logger.log(jdkLevel, messageFormat, args);
    }

    /**
     * <p>
     * Logs the given message (and throwable if specified) to the java logger.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * Method signature has changed and the method was rewritten.
     * </p>
     *
     * <p>
     * Note that it will simply return if the messageFormat is null, or the level is null or equal to OFF or the jdk14
     * level equivalent of the level is not found.
     * </p>
     *
     * @param level A possibly null level indicating the message level
     * @param t A possibly null throwable containing a stack trace to log
     * @param message A possibly null, possibly empty (trim'd) string containing the message to log
     *
     * @since 2.0
     */
    protected final void log(Level level, Throwable t, String message) {
        if (message == null) {
            return;
        }
        // get the jdk14 level equivalent of the level
        java.util.logging.Level jdkLevel = getJdk14Level(level);
        if (jdkLevel == null) {
            return;
        }
        if (t == null) {
            logger.log(jdkLevel, message);
        } else {
            logger.log(jdkLevel, message, t);
        }
    }

    /**
     * <p>
     * Maps a {@link com.topcoder.util.log.Level} object into a {@link java.util.logging.Level}
     * object.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * There were no changes to this method.
     * </p>
     *
     * <p>
     * The following mappings will occur (and the appropriate returned):
     * <table>
     * <tr><th>Log Level</th><th>Jdk Level</th></tr>
     * <tr><td>Finest</td><td>Finest</td></tr>
     * <tr><td>Trace</td><td>Finer</td></tr>
     * <tr><td>Debug</td><td>Fine</td></tr>
     * <tr><td>Config</td><td>Config</td></tr>
     * <tr><td>Info</td><td>Info</td></tr>
     * <tr><td>Warn</td><td>Warning</td></tr>
     * <tr><td>Error</td><td>Severe</td></tr>
     * <tr><td>Fatal</td><td>Severe</td></tr>
     * <tr><td>All</td><td>ALL</td></tr>
     * <tr><td>Off</td><td>Off</td></tr>
     * </table>
     * Return null if it's anything else
     * </p>
     *
     * @param level a possibly null com.topcoder.util.log.Level object that will be mapped to a java.util.logging.Level
     *  object
     *
     * @return a non-null java.util.logging.Level - the mapped level or null if mapping not found
     *
     * @since 1.2
     */
    private final java.util.logging.Level getJdk14EquivalentLevel(Level level) {
        if (level.equals(Level.FINEST)) {
            return java.util.logging.Level.FINEST;
        } else if (level.equals(Level.TRACE)) {
            return java.util.logging.Level.FINER;
        } else if (level.equals(Level.DEBUG)) {
            return java.util.logging.Level.FINE;
        } else if (level.equals(Level.CONFIG)) {
            return java.util.logging.Level.CONFIG;
        } else if (level.equals(Level.INFO)) {
            return java.util.logging.Level.INFO;
        } else if (level.equals(Level.WARN)) {
            return java.util.logging.Level.WARNING;
        } else if (level.equals(Level.ERROR)) {
            return java.util.logging.Level.SEVERE;
        } else if (level.equals(Level.FATAL)) {
            return java.util.logging.Level.SEVERE;
        } else if (level.equals(Level.ALL)) {
            return java.util.logging.Level.ALL;
        } else if (level.equals(Level.OFF)) {
            return java.util.logging.Level.OFF;
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Gets the jdk14 level equivalent of the given level.
     * </p>
     *
     * <p>
     * Note that it will simply return null if the level is null or equal to Level.OFF.
     * </p>
     *
     * @param level the com.topcoder.util.log.Level that will be mapped to a java.util.logging.Level
     *
     * @return the jdk14 level equivalent of the given level
     */
    private java.util.logging.Level getJdk14Level(Level level) {
        if (level == null || level.equals(Level.OFF)) {
            return null;
        }
        // get the jdk14 level equivalent of the level
        return getJdk14EquivalentLevel(level);
    }
}