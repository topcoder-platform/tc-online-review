/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.security.authenticationfactory.failuretests;

import com.topcoder.security.authenticationfactory.Response;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>A failure test for <code>Response</code> class. Tests the proper handling of invalid input data by the methods.
 * Passes the invalid arguments to the constructors and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 * @since Authentication Factory 2.0
 */
public class ResponseFailureTest extends TestCase {

    /**
     * <p>A <code>String</code> providing a sample message to be used for testing.</p>
     */
    public static final String MESSAGE = "message";

    /**
     * <p>An <code>Object</code> providing a sample details to be used for testing.</p>
     */
    public static final Object DETAILS = new Object();

    /**
     * <p>Gets the test suite for <code>Response</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>Response</code> class.
     */
    public static Test suite() {
        return new TestSuite(ResponseFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
    }

    /**
     * <p>Tests the <code>Response(boolean, String)</code> constructor for proper handling invalid input data. Passes
     * the <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_1() {
        try {
            new Response(false, (String) null);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String)</code> constructor for proper handling invalid input data. Passes
     * the <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_2() {
        try {
            new Response(true, (String) null);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String)</code> constructor for proper handling invalid input data. Passes
     * the zero-length message and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_3() {
        try {
            new Response(false, "");
            fail("IllegalArgumentException should be thrown in response to zero-length message");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String)</code> constructor for proper handling invalid input data. Passes
     * the empty message and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_4() {
        try {
            new Response(false, "     ");
            fail("IllegalArgumentException should be thrown in response to empty message");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String)</code> constructor for proper handling invalid input data. Passes
     * the zero-length message and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_5() {
        try {
            new Response(true, "");
            fail("IllegalArgumentException should be thrown in response to zero-length message");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String)</code> constructor for proper handling invalid input data. Passes
     * the empty message and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_6() {
        try {
            new Response(true, "     ");
            fail("IllegalArgumentException should be thrown in response to empty message");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String, Object)</code> constructor for proper handling invalid input data.
     * Passes the <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_Object_1() {
        try {
            new Response(false, null, DETAILS);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String, Object)</code> constructor for proper handling invalid input data.
     * Passes the <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_Object_2() {
        try {
            new Response(true, null, DETAILS);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String, Object)</code> constructor for proper handling invalid input data.
     * Passes the zero-length message and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_Object_3() {
        try {
            new Response(false, "", DETAILS);
            fail("IllegalArgumentException should be thrown in response to zero-length message");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String, Object)</code> constructor for proper handling invalid input data.
     * Passes the empty message and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_Object_4() {
        try {
            new Response(false, "     ", DETAILS);
            fail("IllegalArgumentException should be thrown in response to empty message");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String, Object)</code> constructor for proper handling invalid input data.
     * Passes the zer-length message and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_Object_5() {
        try {
            new Response(true, "", DETAILS);
            fail("IllegalArgumentException should be thrown in response to zero-length message");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String, Object)</code> constructor for proper handling invalid input data.
     * Passes the empty message and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_Object_6() {
        try {
            new Response(true, "     ", DETAILS);
            fail("IllegalArgumentException should be thrown in response to empty message");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String, Object)</code> constructor for proper handling invalid input data.
     * Passes the <code>null</code> details and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_Object_7() {
        try {
            new Response(false, MESSAGE, null);
            fail("NullPointerException should be thrown in response to NULL details");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, String, Object)</code> constructor for proper handling invalid input data.
     * Passes the <code>null</code> details and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_String_Object_8() {
        try {
            new Response(true, MESSAGE, null);
            fail("NullPointerException should be thrown in response to NULL details");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, Object)</code> constructor for proper handling invalid input data. Passes
     * the <code>null</code> details and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_Object_1() {
        try {
            new Response(false, (Object) null);
            fail("NullPointerException should be thrown in response to NULL details");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Response(boolean, Object)</code> constructor for proper handling invalid input data. Passes
     * the <code>null</code> details and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_boolean_Object_2() {
        try {
            new Response(true, (Object) null);
            fail("NullPointerException should be thrown in response to NULL details");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }
}
