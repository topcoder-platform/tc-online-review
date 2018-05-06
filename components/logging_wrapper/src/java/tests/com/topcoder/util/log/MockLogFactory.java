/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

/**
 * <p>
 * Mock class for {@link LogFactory} used for testing.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class MockLogFactory implements LogFactory {

    /**
     * <p>
     * Representing a boolean indicating whether a LogException should be thrown in createLog(String) method.
     * </p>
     *
     * <p>
     * If it is true, a LogException will be thrown in createLog(String) method, otherwise, a RuntimeException will
     * be thrown.
     * </p>
     */
    private boolean isThrownLogException = true;

    /**
     * <p>
     * Creates a {@link Log} implementation and assigns the name to it.
     * </p>
     *
     * <p>
     * Note that an Exception will always be thrown in this method.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     *
     * @return A non-null Log implementation
     *
     * @throws LogException if an exception occurs creating the log
     */
    public Log createLog(String name) throws LogException {
        if (isThrownLogException) {
            throw new LogException("LogException for testing.");
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * <p>
     * Sets whether a LogException should be thrown in createLog(String) method.
     * </p>
     *
     * <p>
     * If it is true, a LogException will be thrown in createLog(String) method, otherwise, a RuntimeException will
     * be thrown.
     * </p>
     *
     * @param isThrownLogException A boolean indicating whether a LogException should be thrown in createLog(String)
     *  method
     */
    public void setThrownLogException(boolean isThrownLogException) {
        this.isThrownLogException = isThrownLogException;
    }
}
