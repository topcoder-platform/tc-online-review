/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.basic;

import java.io.PrintStream;

import com.topcoder.util.log.AbstractLog;
import com.topcoder.util.log.Level;

/**
 * <p>
 * This is the basic implementation of the <code>Log</code> interface that will write the logging message to the
 * specified print stream.
 * </p>
 *
 * <p>
 * <b>Changes for version 2.0</b>
 * The following changes have been made:
 * <ol>
 * <li>Class inherits from AbstractLog now.</li>
 * <li>Configuration 'stuff' was removed.</li>
 * <li>Removed the prior static fields (no longer needed).</li>
 * <li>Basic log was changed to write to any type of PrintStream.</li>
 * <li>Constructor changed to package private with additional arguments.</li>
 * <li>Constructor calls super constructor to store name and then will store the passed print stream to an instance
 *  variable.</li>
 * <li>The log method was changed to package private and it's method signature changed to match the new abstract
 *  method from AbstractLog.</li>
 * <li>The log method will first write the error message and then print the stack trace.</li>
 * </ol>
 * </p>
 *
 * <p>
 * This Log implementation can be used to print logging information to any PrintStream source (whether it be a file,
 * the console, whatever).  Please note that the level is always ignored. This class can only be created by the
 * {@link BasicLogFactory}.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * This class is thread safe by having no mutable state information. However, the underlying PrintStream may or may
 * not be thread safe and is dependent upon what the application has specified.
 * </p>
 *
 * @author StinkyCheeseMan, TCSDEVELOPER
 * @author adic, ShindouHikaru
 * @author Pops, TCSDEVELOPER
 * @since 1.2
 * @version 2.0
 */
public final class BasicLog extends AbstractLog {

    /**
     * <p>
     * Represents the print stream that logging information is written to.
     * </p>
     *
     * <p>
     * This variable is set in the constructor, is immutable (the reference) and can never be null. The variable is
     * referenced in the log method.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0</b>
     * This is a new variable definition.
     * </p>
     *
     * @since 2.0
     */
    private final PrintStream printStream;

    /**
     * <p>
     * Constructs the basic log implementation from the given name and the given print stream.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * The constructor is now package private, accepts a print stream argument and will call the super constructor.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     * @param printStream A non-null print stream to use
     *
     * @throws IllegalArgumentException if the printStream is null
     *
     * @since 2.0
     */
    BasicLog(String name, PrintStream printStream) {
        super(name);
        if (printStream == null) {
            throw new IllegalArgumentException("printStream should not be null.");
        }
        this.printStream = printStream;
    }

    /**
     * <p>
     * Logs the given message to the given print stream and then provides the stack trace dump if specified.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * Method signature changed and method rewritten.
     * </p>
     *
     * <p>
     * This method will:
     * <ol>
     * <li>Write the given message to the print stream.</li>
     * <li>If the throwable is non-null, print its stack trace.</li>
     * <li>Silently ignore any exception that is thrown.</li>
     * </ol>
     * </p>
     *
     * @param level ignored
     * @param t A possibly null throwable containing a stack trace to print
     * @param message A possibly null, possibly empty (trim'd) string containing the message to print
     *
     * @since 2.0
     */
    protected final void log(Level level, Throwable t, String message) {
        printStream.println(message);
        if (t != null) {
            t.printStackTrace(printStream);
        }
    }

    /**
     * <p>
     * This method should return true, because levels are meaningless for the basic logger.
     * </p>
     *
     * <p>
     * No exceptions are thrown from this method. If the argument is null, false
     * should be returned, instead of true.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>no changes were made.
     * </p>
     *
     * @param level ignored
     * @return false if the argument is null, true otherwise
     *
     * @since 1.2
     */
    public boolean isEnabled(Level level) {
        return level != null;
    }
}
