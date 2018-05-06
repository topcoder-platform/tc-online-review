/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.basic;

import java.io.PrintStream;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * This is the implementation of the <code>LogFactory</code> interface that will create {@link BasicLog} instances
 * based on the print stream given.
 * </p>
 *
 * <p>
 * <b>Changes for version 2.0</b>
 * This is a new class.
 * </p>
 *
 * <p>
 * This <code>LogFactory</code> implementation will be used by the application to produce Log(s) that will write the
 * logging information to a print stream.  This <code>LogFactory</code> implementation is used, by default, in the
 * <code>LogManager</code> or can be specified in the LogManager.configure method.  The <code>LogManager</code> will
 * then call the <code>createLog</code> method to create a log (in this case, a <code>BasicLog</code>) when a Log
 * implementation is needed.
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
public class BasicLogFactory implements LogFactory {

    /**
     * <p>
     * Represents the print stream that logging information will be written to.
     * </p>
     *
     * <p>
     * This variable is set in the constructor, is immutable (the reference) and can never be null. The variable is
     * referenced in the <code>createLog</code> method.
     * </p>
     */
    private final PrintStream printStream;

    /**
     * <p>
     * Constructs the <code>BasicLogFactory</code> using the <code>System.out</code> print stream.
     * </p>
     */
    public BasicLogFactory() {
        this(System.out);
    }

    /**
     * <p>
     * Constructs the BasicLogFactory using the specified print stream.
     * </p>
     *
     * @param printStream A non-null print stream to use
     *
     * @throws IllegalArgumentException if the printStream is null
     */
    public BasicLogFactory(PrintStream printStream) {
        if (printStream == null) {
            throw new IllegalArgumentException("printStream should not be null.");
        }
        this.printStream = printStream;
    }

    /**
     * <p>
     * Creates a Log implementation and returns it.
     * </p>
     *
     * <p>
     * This factory will create and return a <code>BasicLog</code> for the given print stream.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     *
     * @return A non-null log implementation (in this case a BasicLog)
     */
    public Log createLog(String name) {
        return new BasicLog(name, printStream);
    }
}
