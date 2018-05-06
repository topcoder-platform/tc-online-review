/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.failuretests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.RequestParsingException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Map;
import java.io.Reader;
import java.io.IOException;
import java.text.ParseException;

/**
 * <p>A failure test for {@link AjaxRequest} class. Tests the proper handling of invalid
 * input data by the methods. Passes the invalid arguments to the methods and expects the appropriate exception to be
 * thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AjaxRequestFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link AjaxRequest} which are tested. These instances are initialized in {@link #setUp()}
     * method and released in {@link #tearDown()} method. Each instance is initialized using a separate constructor
     * provided by the tested class.<p>
     */
    private AjaxRequest[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link AjaxRequest} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AjaxRequest} class.
     */
    public static Test suite() {
        return new TestSuite(AjaxRequestFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new AjaxRequest[1];
        this.testedInstances[0] = new AjaxRequest(TestDataFactory.VALID_AJAX_REQUEST_TYPE,
                                                  TestDataFactory.VALID_AJAX_REQUEST_PARAMETERS);
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        super.tearDown();
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>type</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_Map_type_null() {
        try {
            new AjaxRequest(null, TestDataFactory.VALID_AJAX_REQUEST_PARAMETERS);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>type</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_Map_type_ZERO_LENGTH_STRING() {
        try {
            new AjaxRequest(TestDataFactory.ZERO_LENGTH_STRING, TestDataFactory.VALID_AJAX_REQUEST_PARAMETERS);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>type</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_Map_type_WHITESPACE_ONLY_STRING() {
        try {
            new AjaxRequest(TestDataFactory.WHITESPACE_ONLY_STRING, TestDataFactory.VALID_AJAX_REQUEST_PARAMETERS);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>parameters</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_Map_parameters_null() {
        try {
            new AjaxRequest(TestDataFactory.VALID_AJAX_REQUEST_TYPE, null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getStringStringMapWithNullKey()} as <code>parameters</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_Map_parameters_StringStringMapWithNullKey() {
        try {
            new AjaxRequest(TestDataFactory.VALID_AJAX_REQUEST_TYPE, TestDataFactory.getStringStringMapWithNullKey());
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getStringStringMapWithZeroLengthKey()} as <code>parameters</code> and expects
     * the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_Map_parameters_StringStringMapWithZeroLengthKey() {
        try {
            new AjaxRequest(TestDataFactory.VALID_AJAX_REQUEST_TYPE, TestDataFactory.getStringStringMapWithZeroLengthKey());
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getStringStringMapWithWhitespaceKey()} as <code>parameters</code> and expects
     * the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_Map_parameters_StringStringMapWithWhitespaceKey() {
        try {
            new AjaxRequest(TestDataFactory.VALID_AJAX_REQUEST_TYPE,
                            TestDataFactory.getStringStringMapWithWhitespaceKey());
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getStringStringMapWithNonSrtringKey()} as <code>parameters</code> and expects
     * the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_Map_parameters_StringStringMapWithNonSrtringKey() {
        try {
            new AjaxRequest(TestDataFactory.VALID_AJAX_REQUEST_TYPE,
                            TestDataFactory.getStringStringMapWithNonSrtringKey());
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getStringStringMapWithNullValue()} as <code>parameters</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_Map_parameters_StringStringMapWithNullValue() {
        try {
            new AjaxRequest(TestDataFactory.VALID_AJAX_REQUEST_TYPE, TestDataFactory.getStringStringMapWithNullValue());
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#AjaxRequest(String,Map)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getStringStringMapWithNonSrtringValue()} as <code>parameters</code> and expects
     * the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_Map_parameters_StringStringMapWithNonSrtringValue() {
        try {
            new AjaxRequest(TestDataFactory.VALID_AJAX_REQUEST_TYPE,
                            TestDataFactory.getStringStringMapWithNonSrtringValue());
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#parse(Reader)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link null} as <code>input</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testParse_Reader_input_null() {
        try {
            AjaxRequest.parse(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#parse(Reader)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getAjaxRequestInvalidIOReader()} as <code>input</code> and expects the
     * <code>IOException</code> to be thrown.</p>
     */
    public void testParse_Reader_input_AjaxRequestInvalidIOReader() {
        try {
            AjaxRequest.parse(TestDataFactory.getAjaxRequestInvalidIOReader());
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IOException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#parse(Reader)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getAjaxRequestMalformedXmlReader()} as <code>input</code> and expects the
     * <code>RequestParsingException</code> to be thrown.</p>
     */
    public void testParse_Reader_input_AjaxRequestMalformedXmlReader() {
        try {
            AjaxRequest.parse(TestDataFactory.getAjaxRequestMalformedXmlReader());
            Assert.fail("RequestParsingException should have been thrown");
        } catch (RequestParsingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("RequestParsingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#parse(Reader)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getAjaxRequestInvalidXmlReader()} as <code>input</code> and expects the
     * <code>RequestParsingException</code> to be thrown.</p>
     */
    public void testParse_Reader_input_AjaxRequestInvalidXmlReader() {
        try {
            AjaxRequest.parse(TestDataFactory.getAjaxRequestInvalidXmlReader());
            Assert.fail("RequestParsingException should have been thrown");
        } catch (RequestParsingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("RequestParsingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#hasParameter(String)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link null} as <code>name</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testHasParameter_String_name_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].hasParameter(null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#hasParameter(String)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>name</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testHasParameter_String_name_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].hasParameter(TestDataFactory.ZERO_LENGTH_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#hasParameter(String)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>name</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testHasParameter_String_name_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].hasParameter(TestDataFactory.WHITESPACE_ONLY_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameter(String)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link null} as <code>name</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testGetParameter_String_name_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameter(null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameter(String)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>name</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetParameter_String_name_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameter(TestDataFactory.ZERO_LENGTH_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameter(String)} method for proper handling the invalid input
     * arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>name</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetParameter_String_name_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameter(TestDataFactory.WHITESPACE_ONLY_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameterAsLong(String)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes {@link null} as <code>name</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testGetParameterAsLong_String_name_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameterAsLong(null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameterAsLong(String)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>name</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetParameterAsLong_String_name_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameterAsLong(TestDataFactory.ZERO_LENGTH_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameterAsLong(String)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>name</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetParameterAsLong_String_name_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameterAsLong(TestDataFactory.WHITESPACE_ONLY_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameterAsLong(String)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes <code>Status</code> as <code>name</code> and expects the <code>NumberFormatException</code> to be
     * thrown.</p>
     */
    public void testGetParameterAsLong_String_name_NAME_OF_NON_NUMERIC_PARAMETER() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameterAsLong("Status");
                Assert.fail("NumberFormatException should have been thrown");
            } catch (NumberFormatException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("NumberFormatException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameterAsDate(String)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes {@link null} as <code>name</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testGetParameterAsDate_String_name_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameterAsDate(null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameterAsDate(String)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>name</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetParameterAsDate_String_name_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameterAsDate(TestDataFactory.ZERO_LENGTH_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameterAsDate(String)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>name</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetParameterAsDate_String_name_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameterAsDate(TestDataFactory.WHITESPACE_ONLY_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxRequest#getParameterAsDate(String)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes <code>Status</code> as <code>name</code> and expects the <code>ParseException</code> to be thrown.</p>
     */
    public void testGetParameterAsDate_String_name_NAME_OF_NON_DATE_PARAMETER() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].getParameterAsDate("Status");
                Assert.fail("ParseException should have been thrown");
            } catch (ParseException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("ParseException was expected but the original exception is : " + e);
            }
        }
    }
}
