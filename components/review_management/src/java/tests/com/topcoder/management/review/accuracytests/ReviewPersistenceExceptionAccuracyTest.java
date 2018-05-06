/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * ReviewPersistenceExceptionAccuracyTest.java
 */
package com.topcoder.management.review.accuracytests;

import junit.framework.TestCase;

import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewPersistenceException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Accuracy tests for <code>ReviewPersistenceException</code> class.
 * </p>
 *
 * @author wiedzmin, Beijing2008
 * @version 1.2
 */
public class ReviewPersistenceExceptionAccuracyTest extends TestCase {

    /**Message.*/
    private static final String message = "error message";

    /**Cause.*/
    private static final IllegalArgumentException cause = new IllegalArgumentException();

    /**
     * <p>
     * Test constructor that accepts String.
     * </p>
     */
    public void testCtor1() {
        ReviewPersistenceException exception = new ReviewPersistenceException(message);

        assertTrue("ReviewPersistenceException is not derived from ReviewManagementException.",
                exception instanceof ReviewManagementException);

        //check message existence
        assertEquals("Exception message is not initialized correctly.",
                message, exception.getMessage());
    }

    /**
     * <p>
     * Test constructor that accepts String and Throwable.
     * </p>
     */
    public void testCtor2() {
        ReviewPersistenceException exception = new ReviewPersistenceException(message, cause);

        //check cause existence
        assertEquals("Exception cause is not initialized correctly.",
                cause, exception.getCause());

        //check message existence
        assertTrue("Exception message is not initialized correctly.",
                exception.getMessage().indexOf(message) != -1);
    }
    /**
     * <p>
     * Test constructor that accepts String, Throwable and ExceptionData.
     * </p>
     */
    public void testCtor3() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("app");
        ReviewPersistenceException exception = new ReviewPersistenceException(message, cause, data);

        //check cause existence
        assertEquals("Exception cause is not initialized correctly.",
                cause, exception.getCause());

        //check message existence
        assertTrue("Exception message is not initialized correctly.",
                exception.getMessage().indexOf(message) != -1);
        
        //check data existence
        assertEquals("Exception data is not initialized correctly.",
                exception.getApplicationCode(), "app");
    }
    /**
     * <p>
     * Test constructor that accepts String and ExceptionData.
     * </p>
     */
    public void testCtor4() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("app");
        ReviewPersistenceException exception = new ReviewPersistenceException(message, data);

        //check message existence
        assertTrue("Exception message is not initialized correctly.",
                exception.getMessage().indexOf(message) != -1);
        
        //check data existence
        assertEquals("Exception data is not initialized correctly.",
                exception.getApplicationCode(), "app");
    }
}