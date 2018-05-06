/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.accuracytests;

import com.topcoder.util.errorhandling.ExceptionUtils;

import junit.framework.TestCase;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * <p>
 * Test the functionality of class <code>ExceptionUtils</code>.
 * </p>
 * 
 * <p>
 * This test suite contains multiple failure and accuracy test cases that addressed different aspects for each public
 * methods and constructors.<br>
 * Various real data is used to ensure that both the valid and invalid inputs are handled properly as defined in the
 * documentation.<br>
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class ExceptionUtilsTestCases extends TestCase {
    /**
     * <p>
     * An instance of <code>ResourceBundle</code> for testing.<br>
     * </p>
     */
    private ResourceBundle bundle = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * 
     * <p>
     * The test instance is initialized and all the need configuration are loaded.
     * </p>
     *
     * @throws Exception Any exception thrown by this method is propagated to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        bundle = ResourceBundle.getBundle("accuracy/messages", Locale.ENGLISH);
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     * 
     * <p>
     * The test instance is released and the configuration is clear.
     * </p>
     *
     * @throws Exception Any exception thrown by this method is propagated to JUnit
     */
    protected void tearDown() throws Exception {
        bundle = null;

        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage1_Accuracy1() {
        String key = "author";
        String defaultMessage = "The default message";
        String result = ExceptionUtils.getMessage(null, key, defaultMessage);
        assertEquals("Test accuracy for method getMessage() failed.", defaultMessage, result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage1_Accuracy2() {
        String key = null;
        String defaultMessage = "The default message";
        String result = ExceptionUtils.getMessage(bundle, key, defaultMessage);
        assertEquals("Test accuracy for method getMessage() failed.", defaultMessage, result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage1_Accuracy3() {
        String key = "author";
        String defaultMessage = "The default message";
        String result = ExceptionUtils.getMessage(bundle, key, defaultMessage);
        assertEquals("Test accuracy for method getMessage() failed.", "TCSDEVELOPER", result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage1_Accuracy4() {
        String key = "unknown_key";
        String defaultMessage = "The default message";
        String result = ExceptionUtils.getMessage(bundle, key, defaultMessage);
        assertEquals("Test accuracy for method getMessage() failed.", defaultMessage, result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage2_Accuracy1() {
        String appCode = "TCUML";
        String moduleCode = "XMI_Writer";
        String errorCode = "1290";
        String result = ExceptionUtils.getMessage(bundle, appCode, moduleCode, errorCode);
        String expected = "TCUML-XMI_Writer-1290: Message for TCUML_XMI_Writer_1290";
        assertEquals("Test accuracy for method getMessage() failed.", expected, result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage2_Accuracy2() {
        String appCode = "";
        String moduleCode = "XMI_Writer";
        String errorCode = "1290";
        String result = ExceptionUtils.getMessage(bundle, appCode, moduleCode, errorCode);
        String expected = "XMI_Writer-1290: Message for XMI_Writer_1290";
        assertEquals("Test accuracy for method getMessage() failed.", expected, result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage2_Accuracy3() {
        String appCode = "TCUML";
        String moduleCode = "";
        String errorCode = "1290";
        String result = ExceptionUtils.getMessage(bundle, appCode, moduleCode, errorCode);
        String expected = "TCUML-1290: Message for TCUML_1290";
        assertEquals("Test accuracy for method getMessage() failed.", expected, result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage2_Accuracy4() {
        String appCode = "TCUML";
        String moduleCode = "XMI_Writer";
        String errorCode = "";
        String result = ExceptionUtils.getMessage(bundle, appCode, moduleCode, errorCode);
        String expected = "TCUML-XMI_Writer: Message for TCUML_XMI_Writer";
        assertEquals("Test accuracy for method getMessage() failed.", expected, result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage2_Accuracy5() {
        String appCode = "";
        String moduleCode = "";
        String errorCode = "";
        String result = ExceptionUtils.getMessage(bundle, appCode, moduleCode, errorCode);
        String expected = "Message for empty key";
        assertEquals("Test accuracy for method getMessage() failed.", expected, result);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testGetMessage2_Accuracy6() {
        String appCode = "TCUML";
        String moduleCode = "XMI_Writer";
        String errorCode = "1290_for_empty";
        String result = ExceptionUtils.getMessage(bundle, appCode, moduleCode, errorCode);
        String expected = "TCUML-XMI_Writer-1290_for_empty";
        assertEquals("Test accuracy for method getMessage() failed.", expected, result);
    }    

    /**
     * <p>
     * Accuracy test case for method 'checkNull()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testCheckNull_Accuracy1() {
        try {
            ExceptionUtils.checkNull(null, bundle, "null", "default message");
            fail("Test accuracy for method 'checkNull()' failed.");
        } catch (IllegalArgumentException iae) {
            assertEquals("Test accuracy for method checkNull() failed.", "The argument should not be null",
                iae.getMessage());
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'checkNull()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testCheckNull_Accuracy2() {
        ExceptionUtils.checkNull(new Object(), bundle, "key1", "default message");
    }

    /**
     * <p>
     * Accuracy test case for method checkNullOrEmpty()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testCheckNullOrEmpty_Accuracy1() {
        try {
            ExceptionUtils.checkNullOrEmpty(null, bundle, "empty", "default message");
            fail("Test accuracy for method 'checkNull()' failed.");
        } catch (IllegalArgumentException iae) {
            assertEquals("Test accuracy for method checkNullOrEmpty() failed.", "The argument should not be empty",
                iae.getMessage());
        }
    }

    /**
     * <p>
     * Accuracy test case for method checkNullOrEmpty()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testCheckNullOrEmpty_Accuracy2() {
        try {
            ExceptionUtils.checkNullOrEmpty("   ", bundle, "empty", "default message");
            fail("Test accuracy for method 'checkNull()' failed.");
        } catch (IllegalArgumentException iae) {
            assertEquals("Test accuracy for method checkNullOrEmpty() failed.", "The argument should not be empty",
                iae.getMessage());
        }
    }

    /**
     * <p>
     * Accuracy test case for method checkNullOrEmpty()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testCheckNullOrEmpty_Accuracy3() {
        ExceptionUtils.checkNullOrEmpty("String", bundle, "key1", "default message");
    }
}
