/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * The class contain testcase for test <code>Response</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ResponseTest extends TestCase {

    /**
     * <p>
     * Response instance.
     * </p>
     */
    private Response r = null;

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.
     * </p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(ResponseTest.class);
    }

    /**
     * <p>
     * test for void Response(boolean).
     * </p>
     */
    public void testResponseboolean() {
        boolean successfull = true;
        r = new Response(successfull);
        assertEquals("these two value should be equal", successfull, r.isSuccessful());
    }

    /**
     * <p>
     * test for void Response(boolean, String), pass it with null String, should throw NullPointerException.
     * </p>
     */
    public void testResponsebooleanStringNPE() {
        try {
            r = new Response(true, null);
            fail("null 'message' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    /**
     * <p>
     * test for void Response(boolean, String), pass it with empty String, should throw IllegalArgumentException.
     * </p>
     */
    public void testResponsebooleanStringIAE() {
        try {
            r = new Response(true, "  ");
            fail("empty 'message' is not acceptable, should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * accuracy test for void Response(boolean, String).
     * </p>
     */
    public void testResponsebooleanString() {
        boolean successful = true;
        String message = "message";

        r = new Response(successful, message);
        assertEquals("two value should be equal", successful, r.isSuccessful());
        assertSame("two value should be same", message, r.getMessage());
    }

    /**
     * <p>test for void Response(boolean, String, Object) will null parameter, should throw
     * NullPointerException.</p>
     */
    public void testResponsebooleanStringObjectNPE() {
        try {
            r = new Response(true, null, new Object());
            fail("null 'message' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }

        try {
            r = new Response(true, "null", null);
            fail("null 'details' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    /**
     * <p>test for void Response(boolean, String, Object) will empty String, should throw
     * IllegalArgumentException.</p>
     */
    public void testResponsebooleanStringObjectIAE() {
        try {
            r = new Response(true, "  ", new Object());
            fail("empty 'message' is not acceptable, should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>test for void Response(boolean, String, Object).</p>
     */
    public void testResponsebooleanStringObject() {
        boolean successful = false;
        String message = "message";
        Object obj = new Object();

        r = new Response(successful, message, obj);
        assertEquals("two value should equal", successful, r.isSuccessful());
        assertSame("two should same", message, r.getMessage());
        assertSame("two should same", obj, r.getDetails());
    }

    /**
     * <p>test for void Response(boolean, Object), pass it null parameter,
     * should throw NullPointerException.</p>
     */
    public void testResponsebooleanObjectNPE() {
        try {
            r = new Response(true, (Object) null);
            fail("null object is not acceptable, should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        }
    }

    /**
     * <p>test for void Response(boolean, Object).</p>
     */
    public void testResponsebooleanObject() {
        boolean successful = false;
        Object obj = new Object();

        r = new Response(successful, obj);
        assertEquals("two value should equal", successful, r.isSuccessful());
        assertSame("two should same", obj, r.getDetails());
    }
}