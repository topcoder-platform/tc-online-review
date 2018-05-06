/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * ReviewEntityNotFoundExceptionAccuracyTest.java
 */
package com.topcoder.management.review.accuracytests;

import junit.framework.TestCase;

import com.topcoder.management.review.ReviewEntityNotFoundException;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Accuracy tests for <code>ReviewEntityNotFoundException</code> class.
 * </p>
 *
 * @author wiedzmin, Beijing2008
 * @version 1.2
 */
public class ReviewEntityNotFoundExceptionAccuracyTest extends TestCase {

    /**Message.*/
    private static final String message = "error message";

    /**Id.*/
    private static final long id = 100;

    /**Cause.*/
    private static final IllegalArgumentException cause = new IllegalArgumentException();

    /**
     * <p>
     * Test constructor and method getId().
     * </p>
     */
    public void testException() {
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(message, id);

        assertTrue("ReviewEntityNotFoundException is not derived from ReviewManagementException.",
                exception instanceof ReviewManagementException);

        //check message existence
        assertEquals("Exception message is not initialized correctly.",
                message, exception.getMessage());

        //check id
        assertEquals("Id is not initialized correctly.", id, exception.getId());
    }
    /**
     * <p>
     * Test constructor that accepts String, ExceptionData and id.
     * </p>
     */
    public void testCtor2() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("app");
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(message, data, id);

        //check message existence
        assertTrue("Exception message is not initialized correctly.",
                exception.getMessage().indexOf(message) != -1);
        
        //check data existence
        assertEquals("Exception data is not initialized correctly.",
                exception.getApplicationCode(), "app");
        
        //check id existence
        assertEquals("Exception id is not initialized correctly.",
                exception.getId(), id);
    }
    /**
     * <p>
     * Test constructor that accepts String, Throwable and id.
     * </p>
     */
    public void testCtor3() {
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(message, cause, id);

        //check message existence
        assertTrue("Exception message is not initialized correctly.",
                exception.getMessage().indexOf(message) != -1);
        
        //check id existence
        assertEquals("Exception id is not initialized correctly.",
                exception.getId(), id);
        
        //check cause existence
        assertEquals("Exception cause is not initialized correctly.",
                cause, exception.getCause());
    }
    /**
     * <p>
     * Test constructor that accepts String, Throwable, ExceptionData and id.
     * </p>
     */
    public void testCtor4() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("app");
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(message, cause, data, id);

        //check message existence
        assertTrue("Exception message is not initialized correctly.",
                exception.getMessage().indexOf(message) != -1);
        
        //check data existence
        assertEquals("Exception data is not initialized correctly.",
                exception.getApplicationCode(), "app");
        
        //check id existence
        assertEquals("Exception id is not initialized correctly.",
                exception.getId(), id);
        
        //check cause existence
        assertEquals("Exception cause is not initialized correctly.",
                cause, exception.getCause());
    }
}