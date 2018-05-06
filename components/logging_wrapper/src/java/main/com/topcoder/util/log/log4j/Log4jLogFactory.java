/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.log4j;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * This is the implementation of the <code>LogFactory</code> interface that will create Log4jLog instances based on
 * the log4j logger for the given name.
 * </p>
 *
 * <p>
 * <b>Changes for version 2.0</b>
 * This is a new class.
 * </p>
 *
 * <p>
 * This <code>LogFactory</code> implementation will be used by the application to produce Log(s) that will write the
 * logging information to the log4j logging system.  This <code>LogFactory</code> implementation can be specified in
 * the LogManager.configure method.  The <code>LogManager</code> will then call the <code>createLog</code> method to
 * create a log (in this case, a Log4jLog) when a <code>Log</code> implementation is needed.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * This class is thread safe by having no mutable state information.
 * </p>
 *
 * @author Pops, TCSDEVELOPER
 * @version 2.0
 */
public class Log4jLogFactory implements LogFactory {

    /**
     * <p>
     * Represents whether the log4j layout will format the object (true) or if the object formatter component will be
     * used to format the object (false).
     * </p>
     *
     * <p>
     * This variable is set in the constructor and is immutable.  This variable will be referenced in the
     * <code>createLog</code> method.
     * </p>
     */
    private final boolean useLoggerLayout;

    /**
     * <p>
     * Constructs the Log4jLogFactory using the Object Formatter component as the way to format message objects.
     * </p>
     */
    public Log4jLogFactory() {
        this(false);
    }

    /**
     * <p>
     * Constructs the Log4jLogFactory using the flag to indicate whether to use the log4j Layout to format message
     * objects or to use the Object Formatter component to format message objects.
     * </p>
     *
     * @param useLoggerLayout True to use the log4j layout implementation to format the message object or false to use
     *  the object formatter component
     */
    public Log4jLogFactory(boolean useLoggerLayout) {
        this.useLoggerLayout = useLoggerLayout;
    }

    /**
     * <p>
     * Creates a Log implementation and returns it.  This factory will create and return a Log4jLog for the given name
     * and layout flag.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     * @return A non-null log implementation (in this case a Log4jLog)
     */
    public Log createLog(String name) {
        return new Log4jLog(name, useLoggerLayout);
    }
}
