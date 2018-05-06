/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

/**
 * <p>
 * This interface defines the contract for implementations that will produce {@link Log} instances.
 * </p>
 *
 * <p>
 * The {@link LogFactory} will implement the <code>createLog</code> method that takes the name to assign to the
 * {@link Log} and return a Log instance for it.
 * </p>
 *
 * <p>
 * <b>Changes for v2.0: </b>
 * This is a new interface.
 * </p>
 *
 * <p>
 * The implementations of this interface are either created by the application or by the {@link LogManager} directly.
 * The {@link LogManager} will then store the implementation in a field and call the <code>createLog</code> method
 * when a Log is needed.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * Implementations of this interface should be thread safe.
 * </p>
 *
 * @author Pops, TCSDEVELOPER
 * @version 2.0
 */
public interface LogFactory {

    /**
     * <p>
     * Creates a {@link Log} implementation and assigning the name to it.
     * </p>
     *
     * <p>
     * Implementors should create a {@link Log} implementation to log to the underlying logger system given the name of
     * the logger. If an exception is encountered creating the Log implementation, a {@link LogException} should be
     * thrown.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     *
     * @return A non-null Log implementation
     *
     * @throws LogException if an exception occurs creating the log
     */
    public Log createLog(String name) throws LogException;
}


