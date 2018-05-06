/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.accuracytests;

import com.topcoder.util.errorhandling.BaseError;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>BaseError</code>.
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
 * @version 1.0
 */
public class BaseErrorTestCases extends TestCase {
    /**
     * <p>
     * Represents an instance of <code>String</code> for testing.<br>
     * </p>
     */
    private String message = "The error message";

    /**
     * <p>
     * Represents an instance of <code>Throwable</code> for testing.<br>
     * </p>
     */
    private Throwable cause = new Throwable("The cause");

    /**
     * <p>
     * Represents an instance of <code>ExceptionData</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private ExceptionData data = null;

    /**
     * <p>
     * Represents an instance of <code>BaseError</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private BaseError error = null;

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

        data = new ExceptionData();
        data.setApplicationCode("TCUML");
        data.setModuleCode("XMI_Writer");
        data.setErrorCode("1290");

        error = new BaseError(message, cause, data);
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
        message = null;
        cause = null;
        data = null;
        error = null;
        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseError()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseError_ThrowableExceptionData_Accuracy() {
        error = new BaseError(cause, data);
        assertTrue("Test accuracy for BaseError() failed.", error instanceof Error);

        ExceptionData dataField = (ExceptionData) AccuracyHelper.getPrivateField(BaseError.class, error,
                "data");
        assertEquals("Test accuracy for BaseError() failed.", data, dataField);

        assertEquals("Test accuracy for BaseError() failed.", cause, error.getCause());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseError()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseError_StringExceptionData_Accuracy() {
        error = new BaseError(message, data);
        assertTrue("Test accuracy for BaseError() failed.", error instanceof Error);

        ExceptionData dataField = (ExceptionData) AccuracyHelper.getPrivateField(BaseError.class, error,
                "data");
        assertEquals("Test accuracy for BaseError() failed.", data, dataField);

        assertEquals("Test accuracy for BaseError() failed.", message, error.getMessage());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseError()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseError_ExceptionData_Accuracy() {
        error = new BaseError(data);
        assertTrue("Test accuracy for BaseError() failed.", error instanceof Error);

        ExceptionData dataField = (ExceptionData) AccuracyHelper.getPrivateField(BaseError.class, error,
                "data");
        assertEquals("Test accuracy for BaseError() failed.", data, dataField);
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseError()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseError_StringThrowable_Accuracy() {
        error = new BaseError(message, cause);
        assertTrue("Test accuracy for BaseError() failed.", error instanceof Error);

        ExceptionData data = (ExceptionData) AccuracyHelper.getPrivateField(BaseError.class, error,
                "data");
        assertNotNull("Test accuracy for BaseError() failed.", data);

        assertEquals("Test accuracy for BaseError() failed.", message, error.getMessage());
        assertEquals("Test accuracy for BaseError() failed.", cause, error.getCause());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseError()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseError_Throwable_Accuracy() {
        error = new BaseError(cause);
        assertTrue("Test accuracy for BaseError() failed.", error instanceof Error);

        ExceptionData data = (ExceptionData) AccuracyHelper.getPrivateField(BaseError.class, error,
                "data");
        assertNotNull("Test accuracy for BaseError() failed.", data);

        assertEquals("Test accuracy for BaseError() failed.", cause, error.getCause());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseError()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseError_String_Accuracy() {
        error = new BaseError(message);
        assertTrue("Test accuracy for BaseError() failed.", error instanceof Error);

        ExceptionData data = (ExceptionData) AccuracyHelper.getPrivateField(BaseError.class, error,
                "data");
        assertNotNull("Test accuracy for BaseError() failed.", data);

        assertEquals("Test accuracy for BaseError() failed.", message, error.getMessage());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseError()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseError_Accuracy() {
        error = new BaseError();
        assertTrue("Test accuracy for BaseError() failed.", error instanceof Error);

        ExceptionData data = (ExceptionData) AccuracyHelper.getPrivateField(BaseError.class, error,
                "data");
        assertNotNull("Test accuracy for BaseError() failed.", data);
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseError()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseError_StringThrowableExceptionData_Accuracy1() {
        error = new BaseError(message, cause, data);
        assertTrue("Test accuracy for BaseError() failed.", error instanceof Error);

        ExceptionData dataField = (ExceptionData) AccuracyHelper.getPrivateField(BaseError.class, error,
                "data");
        assertEquals("Test accuracy for BaseError() failed.", data, dataField);
        assertEquals("Test accuracy for BaseError() failed.", message, error.getMessage());
        assertEquals("Test accuracy for BaseError() failed.", cause, error.getCause());
    }

    /**
     * <p>
     * Accuracy test case for method 'getInformation()'.<br>The value of field 'information' should be return properly.
     * </p>
     */
    public void testGetSetInformation_Object_Accuracy() {
        error.setInformation(null, "null1");
        assertEquals("Test accuracy for getInformation() failed, the value should be return properly.", "null1",
            error.getInformation(null));

        error.setInformation(null, "null2");
        assertEquals("Test accuracy for getInformation() failed, the value should be return properly.", "null2",
            error.getInformation(null));

        error.setInformation("key", "value");
        assertEquals("Test accuracy for getInformation() failed, the value should be return properly.", "value",
            error.getInformation("key"));

        assertNull("Test accuracy for getInformation() failed, the value should be return properly.",
            error.getInformation("Nokey"));
    }

    /**
     * <p>
     * Accuracy test case for method 'isLogged()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testLogged_Accuracy1() {
        assertFalse("Test accuracy for isLogged() failed.", error.isLogged());

        error.setLogged(true);
        assertTrue("Test accuracy for isLogged() failed.", error.isLogged());

        error.setLogged(false);
        assertFalse("Test accuracy for isLogged() failed.", error.isLogged());
    }

    /**
     * <p>
     * Accuracy test case for method 'getErrorCode()'.<br>The value of field 'errorCode' should be return properly.
     * </p>
     */
    public void testGetErrorCode_Accuracy() {
        assertEquals("Test accuracy for getErrorCode() failed, the value should be return properly.", "1290",
            error.getErrorCode());

        error = new BaseError();
        assertNull("Test accuracy for getErrorCode() failed, the value should be return properly.",
            error.getErrorCode());
    }

    /**
     * <p>
     * Accuracy test case for method 'getModuleCode()'.<br>The value of field 'moduleCode' should be return properly.
     * </p>
     */
    public void testGetModuleCode_Accuracy() {
        assertEquals("Test accuracy for getModuleCode() failed, the value should be return properly.", "XMI_Writer",
            error.getModuleCode());

        error = new BaseError();
        assertNull("Test accuracy for getModuleCode() failed, the value should be return properly.",
            error.getModuleCode());
    }

    /**
     * <p>
     * Accuracy test case for method 'getCreationDate()'.<br>
     * The value of field 'creationDate' should be return properly.
     * </p>
     */
    public void testGetCreationDate_Accuracy() {
        assertNotNull("Test accuracy for getCreationDate() failed, the value should be return properly.",
            error.getCreationDate());
    }

    /**
     * <p>
     * Accuracy test case for method 'getThreadName()'.<br>The value of field 'threadName' should be return properly.
     * </p>
     */
    public void testGetThreadName_Accuracy() {
        assertEquals("Test accuracy for getThreadName() failed, the value should be return properly.",
            Thread.currentThread().getName(), error.getThreadName());
    }

    /**
     * <p>
     * Accuracy test case for method 'getApplicationCode()'.<br>
     * The value of field 'applicationCode' should be return properly.
     * </p>
     */
    public void testGetApplicationCode_Accuracy() {
        assertEquals("Test accuracy for getApplicationCode() failed, the value should be return properly.", "TCUML",
            error.getApplicationCode());
        error = new BaseError();
        assertNull("Test accuracy for getApplicationCode() failed, the value should be return properly.",
            error.getApplicationCode());
    }
}
