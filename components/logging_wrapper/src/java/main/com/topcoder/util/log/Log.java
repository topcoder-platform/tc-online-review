/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import com.topcoder.util.format.ObjectFormatter;

/**
 * <p>
 * The Log interface should be extended by classes that wish to provide a custom logging implementation. The various
 * <tt>log</tt> method(s) are used to log a message using the underlying implementation, and the <tt>isEnabled</tt>
 * method is used to determine if a specific logging level is currently being logged.
 * </p>
 *
 * <p>
 * This class has various overridden methods to allow flexible logging. All log methods will attempt to delay the
 * formatting of the logging message to the latest possible moment (which may be in the underlying logging mechanism
 * if supported).
 * </p>
 *
 * <p>
 * At the highest level, the message will not be formatted if the logging level is not enabled. Beyond that level,
 * it's entirely dependent upon the underlying logger.
 * </p>
 *
 * <p>
 * Example: the java logger can delay message format processing until the last possible moment natively and the log4j
 * logger could delay message object formatting until the last possible moment.
 * </p>
 *
 * <p>
 * <b>Changes to v2.0: </b>
 * This interface was extended extensively to add in a number of new logging methods that deal with the object
 * formatting or with string message formatting.
 * </p>
 *
 * <p>
 * Implementations of this interface will be created by the {@link LogFactory} and will be used by the application
 * directly for logging.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * Implementations of this interface should be thread safe themselves but this interface does not attempt to guarantee
 * the thread safety of the underlying logging system.
 * </p>
 *
 * @author StinkyCheeseMan, TCSDEVELOPER
 * @author adic, ShindouHikaru
 * @author Pops, TCSDEVELOPER
 * @since 1.2
 * @version 2.0
 */
public interface Log {

    /**
     * <p>
     * Returns whether the given level is enabled for a specific implementation.
     * </p>
     *
     * <p>
     * A null level should simply return false.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * No changes to this method contract.
     * </p>
     *
     * @param level A possibly null level to check
     *
     * @return true if the level is enabled, false otherwise;
     *
     * @since 1.2
     */
    public boolean isEnabled(Level level);

    /**
     * <p>
     * Logs a given message using a given logging level and message object.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level is null. The message should be formated from the
     * <code>LogManager.getObjectFormatter</code> unless the underlying implementation can do the formatting.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * Added to the contract that the message should be formatted via the LogManager.getObjectFormatter (rather than
     * toString()).
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param message A possibly null message object to log
     *
     * @since 1.2
     */
    public void log(Level level, Object message);

    /**
     * <p>
     * Logs a given message using a given logging level, message object and formatter for the object.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level is null.  The message should be formated with the specified formatter (if
     * specified) regardless if the underlying implementation supports object formatting.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored. If the objectFormatter is null, this method should
     * behave the same as the log(level,message) method.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param message A possibly null message object to log
     * @param objectFormatter A possibly null message object formatter
     *
     * @since 2.0
     */
    public void log(Level level, Object message, ObjectFormatter objectFormatter);

    /**
     * <p>
     * Logs a given message using a given logging level, message format and a single parameter for the format.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level or message format is null (null arg1 can be passed to the message format
     * however).
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     *
     * @since 2.0
     */
    public void log(Level level, String messageFormat, Object arg1);

    /**
     * <p>
     * Logs a given message using a given logging level, message format and two parameters for the format.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level or message format is null (null arg1/arg2 can be used for the message
     * format however).
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     * @param arg2 A possibly null argument for the message format
     *
     * @since 2.0
     */
    public void log(Level level, String messageFormat, Object arg1, Object arg2);

    /**
     * <p>
     * Logs a given message using a given logging level, message format and three parameters for the format.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level or message format is null (null arg1/arg2/arg3 can be used for the
     * message format however).
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     * @param arg2 A possibly null argument for the message format
     * @param arg3 A possibly null argument for the message format
     *
     * @since 2.0
     */
    public void log(Level level, String messageFormat, Object arg1, Object arg2, Object arg3);

    /**
     * <p>
     * Logs a given message using a given logging level, message format and an array of parameters to use with the
     * format.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level or message format is null (null arguments can be used for the message
     * format however).
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param args A possibly null, possibly empty array of arguments for the message format
     *
     * @since 2.0
     */
    public void log(Level level, String messageFormat, Object[] args);

    /**
     * <p>
     * Logs a given message  using a given logging level, throwable and message object.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level is null. The message should be formated from the
     * <code>LogManager.getObjectFormatter</code> unless the underlying implementation can do the formatting.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored. If the throwable is null, this method should log in the
     * same way as the <code>log(Level, Object)</code> method.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * Added to the contract that the message should be formatted via the <code>LogManager.getObjectFormatter</code>
     * (rather thantoString()).
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param message A possibly null message object to log
     *
     * @since 2.0
     */
    public void log(Level level, Throwable cause, Object message);

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message object and formatter for the object.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level is null.  The message should be formated with the specified formatter (if
     * specified) regardless if the underlying implementation supports object formatting.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.  If the objectFormatter is null, this method should
     * behave the same as the <code>log(Level, Object)</code> method. If the throwable is null, this method should log
     * in the same way as the <code>log(Level, Object, ObjectFormatter)</code> method.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param message A possibly null message object to log
     * @param objectFormatter A possibly null message object formatter
     *
     * @since 2.0
     */
    public void log(Level level, Throwable cause, Object message, ObjectFormatter objectFormatter);

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message format and a single parameter for the
     * format.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level or message format is null (null arg1 can be passed to the message format
     * however).
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.  If the throwable is null, this method should log in the
     * same way as the <code>log(Level, String, Object)</code> method.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     *
     * @since 2.0
     */
    public void log(Level level, Throwable cause, String messageFormat, Object arg1);

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message format and a two parameters for the format.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level or message format is null (null arg1/arg2 can be used for the message
     * format however).
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored. If the throwable is null, this method should log in the
     * same way as the <code>log(Level, String, Object, Object)</code> method.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     * @param arg2 A possibly null argument for the message format
     *
     * @since 2.0
     */
    public void log(Level level, Throwable cause, String messageFormat, Object arg1, Object arg2);

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message format and three parameters for the format.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level or message format is null (null arg1/arg2/arg3 can be used for the
     * message format however). Any exception that occurs should be silently ignored.
     * </p>
     *
     * <p>
     * If the throwable is null, this method should log in the same way as the
     * <code>log(Level, String, Object, Object, Object)</code> method.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     * @param arg2 A possibly null argument for the message format
     * @param arg3 A possibly null argument for the message format
     *
     * @since 2.0
     */
    public void log(Level level, Throwable cause, String messageFormat, Object arg1, Object arg2, Object arg3);

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message format and an array of parameters to use
     * with the format.
     * </p>
     *
     * <p>
     * Nothing should be logged if the level or message format is null (null arguments can be used for the message
     * format however).
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.  If the throwable is null, this method should log in the
     * same way as the <code>log(Level, String, Object[])</code> method.</p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new method for the interface.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param args A possibly null, possibly empty array of arguments for the message format
     *
     * @since 2.0
     */
    public void log(Level level, Throwable cause, String messageFormat, Object[] args);
}


