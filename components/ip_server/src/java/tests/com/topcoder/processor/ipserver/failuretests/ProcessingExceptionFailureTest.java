/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.ProcessingException;

/**
 * <p>A failure test for <code>ProcessingException</code> class. Tests the proper handling of invalid input data by the
 * constructors. Passes the invalid arguments to the constructors and expects the appropriate exception to be thrown.
 * </p>
 *
 * @author isv
 * @version 1.0
 */
public class ProcessingExceptionFailureTest extends TestCase {

    /**
     * <p>A <code>Throwable</code> representing the sample cause of error to be used for testing.</p>
     */
    public static final Throwable CAUSE = new Exception("Error");

    /**
     * <p>A <code>String</code> providing a sample message to be used for testing.</p>
     */
    public static final String MESSAGE = "message";

    /**
     * <p>Gets the test suite for <code>ProcessingException</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>ProcessingException</code> class.
     */
    public static Test suite() {
        return new TestSuite(ProcessingExceptionFailureTest.class);
    }

    /**
     * <p>Tests the <code>ProcessingException(String)</code> constructor for proper handling invalid input data. Passes
     * the <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_1() {
        try {
            new ProcessingException(null);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>ProcessingException(String, Throwable)</code> constructor for proper handling invalid input
     * data. Passes the <code>null</code> message  and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_Throwable_1() {
        try {
            new ProcessingException(null, CAUSE);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>ProcessingException(String, Throwable)</code> constructor for proper handling invalid input
     * data. Passes the <code>null</code> cause and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_Throwable_2() {
        try {
            new ProcessingException(MESSAGE, null);
            fail("NullPointerException should be thrown in response to NULL cause");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }
}
