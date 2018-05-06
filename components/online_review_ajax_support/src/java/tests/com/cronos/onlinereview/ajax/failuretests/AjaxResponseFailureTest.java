/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.failuretests;

import com.cronos.onlinereview.ajax.AjaxResponse;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link AjaxResponse} class. Tests the proper handling of invalid
 * input data by the methods. Passes the invalid arguments to the methods and expects the appropriate exception to be
 * thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AjaxResponseFailureTest extends AbstractTestCase {

    /**
     * <p>Gets the test suite for {@link AjaxResponse} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AjaxResponse} class.
     */
    public static Test suite() {
        return new TestSuite(AjaxResponseFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>Failure test. Tests the {@link AjaxResponse#AjaxResponse(String,String,Object)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>type</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_String_Object_type_null() {
        try {
            new AjaxResponse(null, TestDataFactory.AJAX_RESPONSE_SUCCESS_STATUS, null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxResponse#AjaxResponse(String,String,Object)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>type</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_String_Object_type_ZERO_LENGTH_STRING() {
        try {
            new AjaxResponse(TestDataFactory.ZERO_LENGTH_STRING, TestDataFactory.AJAX_RESPONSE_SUCCESS_STATUS, null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxResponse#AjaxResponse(String,String,Object)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>type</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_String_Object_type_WHITESPACE_ONLY_STRING() {
        try {
            new AjaxResponse(TestDataFactory.WHITESPACE_ONLY_STRING, TestDataFactory.AJAX_RESPONSE_SUCCESS_STATUS, null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxResponse#AjaxResponse(String,String,Object)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>status</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_String_Object_status_null() {
        try {
            new AjaxResponse(TestDataFactory.VALID_AJAX_REQUEST_TYPE, null, null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxResponse#AjaxResponse(String,String,Object)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>status</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_String_Object_status_ZERO_LENGTH_STRING() {
        try {
            new AjaxResponse(TestDataFactory.VALID_AJAX_REQUEST_TYPE, TestDataFactory.ZERO_LENGTH_STRING, null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxResponse#AjaxResponse(String,String,Object)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>status</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_String_Object_status_WHITESPACE_ONLY_STRING() {
        try {
            new AjaxResponse(TestDataFactory.VALID_AJAX_REQUEST_TYPE, TestDataFactory.WHITESPACE_ONLY_STRING, null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }
}
