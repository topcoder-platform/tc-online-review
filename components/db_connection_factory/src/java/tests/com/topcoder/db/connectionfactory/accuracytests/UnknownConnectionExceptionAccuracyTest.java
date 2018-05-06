/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import com.topcoder.db.connectionfactory.UnknownConnectionException;

import junit.framework.TestCase;

/**
 * <p>Accuracy test for UnknownConnectionException class.</p>
 *
 * @author cosherx
 * @version 1.0
 */
public class UnknownConnectionExceptionAccuracyTest extends TestCase {
    /**
     * <p>
     * A String with a detail message.
     * </p>
     */
    private static final String DETAIL_MESSAGE = "detail";

    /**
     * <p>
     * A String with a name.
     * </p>
     */
    private static final String NAME = "name";

    /**
     * <p>
     * A Throwable cause.
     * </p>
     */
    private static final Exception CAUSE = new Exception();

    /**
     * <p>
     * Test <code>UnknownConnectionException</code> constructors with name, message.
     * </p>
     *
     * @throws Exception pass any unexpected exceptions to JUnit
     */
    public void testUnknownConnectionException1() throws Exception {
        //Construct UnknownConnectionException with a name and a detail message
        UnknownConnectionException exception = new UnknownConnectionException(NAME, DETAIL_MESSAGE);

        //Verify that there is a detail message
        assertEquals("Detailed error message should be identical", DETAIL_MESSAGE, exception.getMessage());

        //Verify that there is a name
        assertEquals("Name should be identical", NAME, exception.getName());
    }

    /**
     * <p>
     * Test <code>UnknownConnectionException</code> constructors with name, message, cause.
     * </p>
     *
     * @throws Exception pass any unexpected exceptions to JUnit
     */
    public void testUnknownConnectionException2() throws Exception {
        //Construct UnknownConnectionException with a name and a detail message
        UnknownConnectionException exception = new UnknownConnectionException(NAME, DETAIL_MESSAGE, CAUSE);

        //altered message is the result of a modification to the BaseException component.
        assertTrue("Detailed error message with cause should be correct",
            	exception.getMessage().indexOf(DETAIL_MESSAGE) >= 0);

        //Verify that there is a name
        assertEquals("Name should be identical", NAME, exception.getName());

        //Verify that there is a cause
        assertEquals("Cause should be identical", CAUSE, exception.getCause());
    }
}
