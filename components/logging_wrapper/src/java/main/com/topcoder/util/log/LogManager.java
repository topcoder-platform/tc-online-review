/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import com.topcoder.util.format.ObjectFormatter;
import com.topcoder.util.format.ObjectFormatterFactory;
import com.topcoder.util.log.basic.BasicLogFactory;

/**
 * <p>
 * This is the main class for the Logging Wrapper component. The Logging Wrapper component provides a standard logging
 * API with a pluggable back-end logging implementation. Utilization of the Logging Wrapper insures that components are
 * not tied to a specific logging solution. More importantly, a change to the back-end logging solution does not
 * require a code change to existing, tested components. Support exists for the console, log4j and java1.4 Logger as
 * back-end logging implementations.  This class will default to the console logger unless a new one is specified.
 * Additionally, logging a message object can be generically formatted via the Object Formatter component prior to
 * logging.
 * </p>
 *
 * <p>
 * <b>Changes for v2.0: </b>
 * The following changes have been made:
 * <ol>
 * <li>The class was renamed to LogManager.</li>
 * <li>The configuration manager was eliminated.</li>
 * <li>ConfigManager associated fields and methods were eliminated.</li>
 * <li>Support for a logging factory and object formatter was introduced.</li>
 * <li>The console was made as the default logging implementation.</li>
 * <li>The DEFAULT_NAME was changed to a public final constant.</li>
 * </ol>
 * </p>
 *
 * <p>
 * The LogManager will be used by the application to initially configure it (which includes not only specification of
 * the underlying logging factory but also the various formatters to use for message objects [if used]).  The
 * application can then use the LogManager to retrieve logging implementations throughout the application. Please note
 * that <code>setLogFactory</code> method should only be called once. Calling it after Log implementations have been
 * created will not affect the existing logging implementations (only any new ones produced). Additionally formatters
 * can be added after Log implementations have been created (and those formatters will be available to the existing
 * Log implementations), however - this is discouraged since the component assumes all the configuration work has been
 * done up front (see thread safety section).
 * </p>
 *
 * <p>
 * <b>Basic Usage demo:</b>
 * </p>
 *
 * <pre>
 * // set the underlying logging factory to use the Log4jLog
 * // the logging configuration of log4j must be specified according to log4j requirements
 * LogManager.setLogFactory(new Log4jLogFactory());
 * // set the message object formatting to use PrimitiveFormatter
 * ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
 * // get the Log instance, it should be Log4jLog
 * Log log = LogManager.getLog();
 * // log the object using PrimitiveFormatter
 * log.log(Level.INFO, new Integer(123456789), objectFormatter);
 * </pre>
 *
 * <p>
 * For more usage demos, please refer to the Demo Section of Component Specification.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * This class is reasonably thread safe.  This means that the component is generally thread safe but relies on two
 * things that can affect the thread safety:
 * <ol>
 * <li>The underlying logger implementation may or may not be thread safe.</li>
 * <li>It is assumed all the configuration (both log factory and formatting) will be done before a Log implementation
 *  is created.</li>
 * </ol>
 * Point 1 is in the hands of the application developer based on the logging system they choose. Point 2 is more of
 * a performance aid.  Since both the factory being used and the formats of any objects to log are fairly static, to
 * avoid synchronization overhead - it will be assumed this is all done prior to usage.
 * </p>
 *
 * @author StinkyCheeseMan, TCSDEVELOPER
 * @author adic, ShindouHikaru
 * @author Pops, TCSDEVELOPER
 * @since 1.2
 * @version 2.0
 */
public class LogManager {

    /**
     * <p>
     * The default name of the Log instance when no name is specified.
     * </p>
     *
     * <p>
     * This is a constant value that is used in the <code>getLog()</code> and <code>getLogWithExceptions()</code>
     * methods.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This has been made public.
     * </p>
     *
     * @since 1.2
     */
    public static final String DEFAULT_NAME = "SystemLog";

    /**
     * <p>
     * Represents the default object formatter that will be used by the Logging Wrapper component.
     * </p>
     *
     * <p>
     * This value is initially set to a Plain formatter (only has a toString() implementation) retrieved by the
     * <code>ObjectFormatterFactory</code>, is immutable (the reference only) and will never be null. This value will
     * only be accessed by the associate getter method.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new variable.
     * </p>
     *
     * @since 2.0
     */
    private static final ObjectFormatter OBJECT_FORMATTER = ObjectFormatterFactory.getPlainFormatter();

    /**
     * <p>
     * Represents the current Log factory being used by this component.
     * </p>
     *
     * <p>
     * This value is initially set to a <code>BasicLogFactory</code>, is mutable and will never be null.
     * This value will only be referenced in the <code>getLogWithExceptions(string)</code> method and the associated
     * setter method.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This is a new variable.
     * </p>
     *
     * @since 2.0
     */
    private static LogFactory logFactory = new BasicLogFactory();

    /**
     * <p>
     * Private constructor used to prevent outside instantiation.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This constructor was not changed.
     * </p>
     *
     * @since 1.2
     */
    private LogManager() {
        // empty
    }

    /**
     * <p>
     * Sets the current Log factory being used by this component.
     * </p>
     *
     * <p>
     * It is strongly advised to only call this method a single time to configure the log manager (see thread safety
     * section of the class documents).
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This method is new.
     * </p>
     *
     * @param logFactory A non-null logFactory implementation to use
     *
     * @throws IllegalArgumentException if the logFactory is null
     *
     * @since 2.0
     */
    public static final void setLogFactory(LogFactory logFactory) {
        if (logFactory == null) {
            throw new IllegalArgumentException("logFactory should not be null.");
        }
        LogManager.logFactory = logFactory;
    }

    /**
     * <p>
     * Gets the object formatter being used by the Logging Wrapper component.
     * </p>
     *
     * <p>
     * It is strongly advised to setup the object formatter prior to creating Log implementations (see the thread
     * safety section of the class document). Uses can get the repository reference of the object formatter using the
     * getter to add/remove/set other formatters into the object formatter before they create their first Log
     * implementation.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This method is new.
     * </p>
     *
     * @return A non-null ObjectFormatter to use
     *
     * @since 2.0
     */
    public static ObjectFormatter getObjectFormatter() {
        return OBJECT_FORMATTER;
    }

    /**
     * <p>
     * Creates a new Log instance based on the DEFAULT_NAME.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * No changes made to this method.
     * </p>
     *
     * @return A non-null, implementation-specific instance of the Log interface
     *
     * @since 1.2
     */
    public static final Log getLog() {
        return getLog(DEFAULT_NAME);
    }

    /**
     * <p>
     * Creates a new Log instance based on the specified name.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * Method changed to use the <code>BasicLogFactory</code> rather than create the <code>BasicLog</code> directly.
     * </p>
     *
     * <p>
     * Note that if any exception is thrown, It will print a message to System.err, print the stack trace and
     * return a new <code>BasicLog</code> instance with the specified name.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name of the Log instance
     *
     * @return A non-null, implementation-specific instance of the Log interface
     *
     * @since 1.2
     */
    public static final Log getLog(String name) {
        try {
            return getLogWithExceptions(name);
        } catch (LogException e) {
            System.err.println("An error occurred while trying to retrieve the " + name + " log:");
            e.printStackTrace();
            System.err.println("The BasicLogFactory will be used instead.");
            return new BasicLogFactory().createLog(name);
        }
    }

    /**
     * <p>
     * Creates a new Log instance based on the DEFAULT_NAME and throws any exception that occurs creating the log.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * No changes made to this method.</p>
     *
     * @return A non-null implementation-specific instance of the Log interface
     *
     * @throws LogException wraps any exception thrown during the process (reflection related, dynamic invocation
     *  related and cast exceptions)
     *
     * @since 1.2
     */
    public static final Log getLogWithExceptions() throws LogException {
        return getLogWithExceptions(DEFAULT_NAME);
    }

    /**
     * <p>
     * Creates a new Log instance based on the specified name and throws any exception that occurs creating the log.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * This method has been rewritten.
     * </p>
     *
     * <p>
     * This method will:
     * <ol>
     * <li>Create and return the log using logFactory.createLog(name).</li>
     * <li>If an exception is thrown and that exception is of LogException type, simply throw it again.</li>
     * <li>If an exception is thrown and it's not of LogException type, create a new LogException(otherException) and
     *  throw it instead.</li>
     * </ol>
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name of the Log instance
     *
     * @return A non-null implementation-specific instance of the Log interface
     *
     * @throws LogException wraps any exception thrown during the process (reflection related, dynamic invocation
     *  related and cast exceptions)
     *
     * @since 1.2
     */
    public static final Log getLogWithExceptions(String name) throws LogException {
        try {
            return logFactory.createLog(name);
        } catch (LogException le) {
            // throw it again
            throw le;
         // note that there are some other RuntimeExceptions will be thrown here
        } catch (Exception e) {
            // if it's not of LogException type, create a new LogException(otherException) and throw it instead
            throw new LogException("Exception occurs while creating a Log instance using LogFactory.", e);
        }
    }
}
