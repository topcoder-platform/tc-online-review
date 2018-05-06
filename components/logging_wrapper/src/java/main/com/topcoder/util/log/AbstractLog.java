/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import java.text.MessageFormat;

import com.topcoder.util.format.ObjectFormatter;

/**
 * <p>
 * This is an abstract implementation of the {@link Log} interface that can provide common services to Log
 * implementations.
 * </p>
 *
 * <p>
 * This abstract base, currently, provides services to store and retrieve the name assigned to the logger and provides
 * default implementations to the various log methods in the Log interface.  This abstract base will be responsible
 * for converting the message into a string form and then calling an abstract method (that the subclass will provide)
 * to log the message. Please note that subclasses may override any of these methods to provide specific functionality
 * from the underlying implementation.
 * </p>
 *
 * <p>
 * <b>Changes to v2.0</b>
 * This is a new abstract class.
 * </p>
 *
 * <p>
 * The developer should strongly recommend that future implementations of the {@link Log} interface inherit from this
 * class to allow common services to be implemented more easily. Classes that inherit from this abstract base will need
 * to implement the protected log(level, Throwable, message) method and the isEnabled(level) (from Log).  The subclass
 * will not need to check if the level is enabled in the log(Level, Throwable, String) method since that will be done
 * by various methods in this class.
 * </p>
 *
 * <p>
 * <b>Thread safety: </b>
 * This abstract class is thread safe by having immutable variables.
 * </p>
 *
 * @author Pops, TCSDEVELOPER
 * @version 2.0
 */
public abstract class AbstractLog implements Log {

    /**
     * <p>
     * Represents the string representation for null value.
     * </p>
     */
    private static final String NULL = "null";

    /**
     * <p>
     * Represents the name that is assigned to super class logging implementation.
     * </p>
     *
     * <p>
     * This variable will be assigned in the constructor, is immutable and can be null or empty (trim'd) string.
     * This variable is referenced in the associated getter method.
     * </p>
     */
    private final String name;

    /**
     * <p>
     * This constructor will set the state information for this abstract class.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     */
    protected AbstractLog(String name) {
        this.name = name;
    }

    /**
     * <p>
     * Returns the name assigned to this log.
     * </p>
     *
     * @return A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Logs a given message using a given logging level and message object.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level is null. The message will be formated from the
     * <code>LogManager.getObjectFormatter</code> for the method or via the toString if the object formatter does not
     * have a formatter for the object or it has two or more equally applicable format methods to use to format the
     * message. If the message is null, a 'null' string will be logged as the formated message.
     * </p>
     *
     * <p>
     * Note that any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param message A possibly null message object to log
     */
    public void log(Level level, Object message) {
        log(level, (Throwable) null, message, LogManager.getObjectFormatter());
    }

    /**
     * <p>
     * Logs a given message using a given logging level, message object and object formatter.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level is null. The message will be formated from the given formatter or via the
     * toString if the object formatter does not have a formatter for the object or it has two or more equally
     * applicable format methods to use to format the message. If the message is null, a 'null' string will be logged
     * as the formated message.
     * </p>
     *
     * <p>
     * Note that any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param message A possibly null message object to log
     * @param objectFormatter A possibly null message object formatter
     */
    public void log(Level level, Object message, ObjectFormatter objectFormatter) {
        log(level, (Throwable) null, message, objectFormatter);
    }

    /**
     * <p>
     * Logs a given message using a given logging level, message format and a single parameter for the format.
     *
     * <p>
     * Nothing will be logged if the level or message format is null. This method will use the java MessageFormat to
     * produce the message based on the given format and argument.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     */
    public void log(Level level, String messageFormat, Object arg1) {
        log(level, (Throwable) null, messageFormat, new Object[] {arg1});
    }

    /**
     * <p>
     * Logs a given message using a given logging level, message format and two parameters for the format.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level or message format is null. This method will use the java
     * <code>MessageFormat</code> to produce the message based on the given format and arguments.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     * @param arg2 A possibly null argument for the message format
     */
    public void log(Level level, String messageFormat, Object arg1, Object arg2) {
        log(level, (Throwable) null, messageFormat, new Object[]{arg1, arg2});
    }

    /**
     * <p>
     * Logs a given message using a given logging level, message format and three parameters for the format.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level or message format is null. This method will use the java
     * <code>MessageFormat</code> to produce the message based on the given format and arguments.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     * @param arg2 A possibly null argument for the message format
     * @param arg3 A possibly null argument for the message format
     */
    public void log(Level level, String messageFormat, Object arg1, Object arg2, Object arg3) {
        log(level, (Throwable) null, messageFormat, new Object[] {arg1, arg2, arg3});
    }

    /**
     * <p>
     * Logs a given message using a given logging level, message format and parameters array for the format.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level or message format is null. This method will use the java
     * <code>MessageFormat</code> to produce the message based on the given format and arguments.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param args A possibly null, possibly empty array of arguments for the message format
     */
    public void log(Level level, String messageFormat, Object[] args) {
        log(level, (Throwable) null, messageFormat, args);
    }

    /**
     * <p>
     * Logs a given message using a given logging level and message object.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level is null. The message will be formated from the
     * <code>LogManager.getObjectFormatter</code> for the method or via the toString if the object formatter does not
     * have a formatter for the object or it has two or more equally applicable format methods to use to format the
     * message. If the message is null, a 'null' string will be logged as the formated message.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param message A possibly null message object to log
     */
    public void log(Level level, Throwable cause, Object message) {
        log(level, cause, message, LogManager.getObjectFormatter());
    }

    /**
     * <p>
     * Logs a given message using a given logging level and message object.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level is null. The message will be formated from the
     * <code> LogManager.getObjectFormatter</code> for the method or via the toString if the object formatter does not
     * have a formatter for the object or it has two or more equally applicable format methods to use to format the
     * message. If the message is null, a 'null' string will be logged as the formated message.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param message A possibly null message object to log
     * @param objectFormatter A possibly null message object formatter
     */
    public void log(Level level, Throwable cause, Object message, ObjectFormatter objectFormatter) {
        if (!isEnabled(level)) {
            return;
        }
        // if the objectFormatter is null, use the LogManager.getObjectFormatter()
        if (objectFormatter == null) {
            objectFormatter = LogManager.getObjectFormatter();
        }
        String formattedMessage = null;
        if (message != null) {
            try {
                // format the message
                formattedMessage = objectFormatter.format(message);
            } catch (IllegalArgumentException e) {
                // there is no format method, or two or more equally applicable format methods to use to format message
                // ignore it since the formattedMessage is null now
            }
            // note that the format(Object) of the objectFormatters in ObjectFormatter component will not return null,
            // but users could implements ObjectFormatter interface to produce their format, which may be null.
            // we prevent this situation here
            if (formattedMessage == null) {
                formattedMessage = message.toString();
            }
        } else {
            formattedMessage = NULL;
        }
        log(level, cause, formattedMessage);
    }

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message format and a single parameter for the
     * format.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level or message format is null. This method will use the java
     * <code>MessageFormat</code> to produce the message based on the given format and argument.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     */
    public void log(Level level, Throwable cause, String messageFormat, Object arg1) {
        log(level, cause, messageFormat, new Object[] {arg1});
    }

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message format and two parameters for the format.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level or message format is null. This method will use the java
     * <code>MessageFormat</code> to produce the message based on the given format and arguments.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     * @param arg2 A possibly null argument for the message format
     */
    public void log(Level level, Throwable cause, String messageFormat, Object arg1, Object arg2) {
        log(level, cause, messageFormat, new Object[] {arg1, arg2});
    }

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message format and three parameters for the format.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level or message format is null. This method will use the java
     * <code>MessageFormat</code> to produce the message based on the given format and arguments.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param arg1 A possibly null argument for the message format
     * @param arg2 A possibly null argument for the message format
     * @param arg3 A possibly null argument for the message format
     */
    public void log(Level level, Throwable cause, String messageFormat, Object arg1, Object arg2, Object arg3) {
        log(level, cause, messageFormat, new Object[] {arg1, arg2, arg3});
    }

    /**
     * <p>
     * Logs a given message using a given logging level, throwable, message format and parameters array for the format.
     * </p>
     *
     * <p>
     * Nothing will be logged if the level or message format is null. This method will use the java
     * <code>MessageFormat</code> to produce the message based on the given format and arguments.
     * </p>
     *
     * <p>
     * Any exception that occurs should be silently ignored.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param messageFormat A possibly null, possibly empty (trim'd) message format string
     * @param args A possibly null, possibly empty array of arguments for the message format
     */
    public void log(Level level, Throwable cause, String messageFormat, Object[] args) {
        if (messageFormat == null || !isEnabled(level)) {
            return;
        }
        try {
            log(level, cause, MessageFormat.format(messageFormat, args));
        } catch (IllegalArgumentException e) {
            // if the pattern is invalid, or if an argument in the arguments array is not of the type expected by the
            // format element(s) that use it.
            // ignore it
        }
    }

    /**
     * <p>
     * This is the abstract method that will be called from the other log methods when the level is enabled and a
     * message should be logged.
     * </p>
     *
     * <p>
     * Subclasses will need to implement this method to log the message to the underlying logging system.
     * </p>
     *
     * @param level A possibly null level at which the message should be logged
     * @param cause A possibly null throwable describing an error that occurred
     * @param message A possibly null, possibly empty (trim'd) string representing the logging message
     */
    protected abstract void log(Level level, Throwable cause, String message);
}
