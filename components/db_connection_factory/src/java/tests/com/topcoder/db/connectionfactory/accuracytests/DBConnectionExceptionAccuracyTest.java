/*
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import com.topcoder.db.connectionfactory.DBConnectionException;

import junit.framework.TestCase;

/**
 * <p>Accuracy test for DBConnectionException class.</p>
 *
 * @author cosherx
 * @version 1.0
 */
public class DBConnectionExceptionAccuracyTest extends TestCase {
    /**
     * <p>
     * A String with a detail message.
     * </p>
     */
    private static final String DETAIL_MESSAGE = "detail";

    /**
     * <p>
     * A Throwable cause.
     * </p>
     */
    private static final Exception CAUSE = new Exception();


    /**
     * <p>
     * Test <code>DBConnectionException</code> constructors with message.
     * </p>
     *
     * @throws Exception pass any unexpected exceptions to JUnit
     */
    public void testDBConnectionException1() throws Exception {
        //Construct DBConnectionException with a detail message
        DBConnectionException exception = new DBConnectionException(DETAIL_MESSAGE);

        //Verify that there is a detail message
        assertEquals("Detailed error message should be identical", DETAIL_MESSAGE, exception.getMessage());
    }
    /**
     * <p>
     * Test <code>DBConnectionException</code> constructors with message, cause.
     * </p>
     *
     * @throws Exception pass any unexpected exceptions to JUnit
     */
    public void testDBConnectionException2() throws Exception {
        //Construct DBConnectionException with a detail message
        DBConnectionException exception = new DBConnectionException(DETAIL_MESSAGE, CAUSE);

        //altered message is the result of a modification to the BaseException component.
        assertTrue("Detailed error message with cause should be correct",
            	exception.getMessage().indexOf(DETAIL_MESSAGE) >= 0);

        //Verify that there is a cause
        assertEquals("Cause should be identical", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Test <code>DBConnectionException</code> constructors with cause.
     * </p>
     *
     * @throws Exception pass any unexpected exceptions to JUnit
     */
    public void testDBConnectionException3() throws Exception {
        //Construct DBConnectionException with a detail message
        DBConnectionException exception = new DBConnectionException(CAUSE);

        //Verify that there is a cause
        assertEquals("Cause should be identical", CAUSE, exception.getCause());
    }
}
