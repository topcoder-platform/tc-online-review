/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.ConfigurationException;

/**
 * <p>A failure test for <code>ConfigurationException</code> class. Tests the proper handling of invalid input data by
 * the constructors. Passes the invalid arguments to the constructors and expects the appropriate exception to be
 * thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class ConfigurationExceptionFailureTest extends TestCase {

    /**
     * <p>A <code>Throwable</code> representing the sample cause of error to be used for testing.</p>
     */
    public static final Throwable CAUSE = new Exception("Error");

    /**
     * <p>A <code>String</code> providing a sample message to be used for testing.</p>
     */
    public static final String MESSAGE = "message";

    /**
     * <p>Gets the test suite for <code>ConfigurationException</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>ConfigurationException</code> class.
     */
    public static Test suite() {
        return new TestSuite(ConfigurationExceptionFailureTest.class);
    }

    /**
     * <p>Tests the <code>ConfigurationException(String)</code> constructor for proper handling invalid input data.
     * Passes the <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_1() {
        try {
            new ConfigurationException(null);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>ConfigurationException(String, Throwable)</code> constructor for proper handling invalid input
     * data. Passes the <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_Throwable_1() {
        try {
            new ConfigurationException(null, CAUSE);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>ConfigurationException(String, Throwable)</code> constructor for proper handling invalid input
     * data. Passes he <code>null</code> cause and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_Throwable_2() {
        try {
            new ConfigurationException(MESSAGE, null);
            fail("NullPointerException should be thrown in response to NULL cause");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }
}
