/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.Message;

/**
 * <p>A failure test for <code>Message</code> class. Tests the proper handling of invalid input data by the
 * constructors. Passes the invalid arguments to the constructors and expects the appropriate exception to be thrown.
 * </p>
 *
 * @author isv
 * @version 1.0
 */
public class MessageFailureTest extends TestCase {

    /**
     * <p>A <code>String</code> providing the valid ID of a sample handler to be used for testing.</p>
     */
    public static final String HANDLER_ID = "01010101";

    /**
     * <p>A <code>String</code> providing the valid ID of a sample request to be used for testing.</p>
     */
    public static final String REQUEST_ID = "10101010";

    /**
     * <p>Gets the test suite for <code>Message</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>Message</code> class.
     */
    public static Test suite() {
        return new TestSuite(MessageFailureTest.class);
    }

    /**
     * <p>Tests the <code>Message(String, String)</code> constructor for proper handling invalid input data. Passes the
     * <code>null</code> handler ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_String_1() {
        try {
            new Message(null, REQUEST_ID);
            fail("NullPointerException should be thrown in response to NULL handler ID");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Message(String, String)</code> constructor for proper handling invalid input data. Passes the
     * <code>null</code> request ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_String_2() {
        try {
            new Message(HANDLER_ID, null);
            fail("NullPointerException should be thrown in response to NULL request ID");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }
}
