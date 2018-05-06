/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.accuracytests;

import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>BaseException</code>.
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
public class BaseExceptionTestCases extends TestCase {
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
     * Represents an instance of <code>BaseException</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private BaseException exception = null;

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

        exception = new BaseException(message, cause, data);
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
        exception = null;
        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseException()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseException_ThrowableExceptionData_Accuracy() {
        exception = new BaseException(cause, data);
        assertTrue("Test accuracy for BaseException() failed.", exception instanceof Exception);

        ExceptionData dataField = (ExceptionData) AccuracyHelper.getPrivateField(BaseException.class, exception,
                "data");
        assertEquals("Test accuracy for BaseException() failed.", data, dataField);

        assertEquals("Test accuracy for BaseException() failed.", cause, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseException()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseException_StringExceptionData_Accuracy() {
        exception = new BaseException(message, data);
        assertTrue("Test accuracy for BaseException() failed.", exception instanceof Exception);

        ExceptionData dataField = (ExceptionData) AccuracyHelper.getPrivateField(BaseException.class, exception,
                "data");
        assertEquals("Test accuracy for BaseException() failed.", data, dataField);

        assertEquals("Test accuracy for BaseException() failed.", message, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseException()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseException_ExceptionData_Accuracy() {
        exception = new BaseException(data);
        assertTrue("Test accuracy for BaseException() failed.", exception instanceof Exception);

        ExceptionData dataField = (ExceptionData) AccuracyHelper.getPrivateField(BaseException.class, exception,
                "data");
        assertEquals("Test accuracy for BaseException() failed.", data, dataField);
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseException()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseException_StringThrowable_Accuracy() {
        exception = new BaseException(message, cause);
        assertTrue("Test accuracy for BaseException() failed.", exception instanceof Exception);

        ExceptionData data = (ExceptionData) AccuracyHelper.getPrivateField(BaseException.class, exception,
                "data");
        assertNotNull("Test accuracy for BaseException() failed.", data);

        assertEquals("Test accuracy for BaseException() failed.", message, exception.getMessage());
        assertEquals("Test accuracy for BaseException() failed.", cause, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseException()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseException_Throwable_Accuracy() {
        exception = new BaseException(cause);
        assertTrue("Test accuracy for BaseException() failed.", exception instanceof Exception);

        ExceptionData data = (ExceptionData) AccuracyHelper.getPrivateField(BaseException.class, exception,
                "data");
        assertNotNull("Test accuracy for BaseException() failed.", data);

        assertEquals("Test accuracy for BaseException() failed.", cause, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseException()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseException_String_Accuracy() {
        exception = new BaseException(message);
        assertTrue("Test accuracy for BaseException() failed.", exception instanceof Exception);

        ExceptionData data = (ExceptionData) AccuracyHelper.getPrivateField(BaseException.class, exception,
                "data");
        assertNotNull("Test accuracy for BaseException() failed.", data);

        assertEquals("Test accuracy for BaseException() failed.", message, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseException()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseException_Accuracy() {
        exception = new BaseException();
        assertTrue("Test accuracy for BaseException() failed.", exception instanceof Exception);

        ExceptionData data = (ExceptionData) AccuracyHelper.getPrivateField(BaseException.class, exception,
                "data");
        assertNotNull("Test accuracy for BaseException() failed.", data);
    }

    /**
     * <p>
     * Accuracy test case for method 'BaseException()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testBaseException_StringThrowableExceptionData_Accuracy1() {
        exception = new BaseException(message, cause, data);
        assertTrue("Test accuracy for BaseException() failed.", exception instanceof Exception);

        ExceptionData dataField = (ExceptionData) AccuracyHelper.getPrivateField(BaseException.class, exception,
                "data");
        assertEquals("Test accuracy for BaseException() failed.", data, dataField);
        assertEquals("Test accuracy for BaseException() failed.", message, exception.getMessage());
        assertEquals("Test accuracy for BaseException() failed.", cause, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test case for method 'getInformation()'.<br>The value of field 'information' should be return properly.
     * </p>
     */
    public void testGetSetInformation_Object_Accuracy() {
        exception.setInformation(null, "null1");
        assertEquals("Test accuracy for getInformation() failed, the value should be return properly.", "null1",
            exception.getInformation(null));

        exception.setInformation(null, "null2");
        assertEquals("Test accuracy for getInformation() failed, the value should be return properly.", "null2",
            exception.getInformation(null));

        exception.setInformation("key", "value");
        assertEquals("Test accuracy for getInformation() failed, the value should be return properly.", "value",
            exception.getInformation("key"));

        assertNull("Test accuracy for getInformation() failed, the value should be return properly.",
            exception.getInformation("Nokey"));
    }

    /**
     * <p>
     * Accuracy test case for method 'isLogged()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testLogged_Accuracy1() {
        assertFalse("Test accuracy for isLogged() failed.", exception.isLogged());

        exception.setLogged(true);
        assertTrue("Test accuracy for isLogged() failed.", exception.isLogged());

        exception.setLogged(false);
        assertFalse("Test accuracy for isLogged() failed.", exception.isLogged());
    }

    /**
     * <p>
     * Accuracy test case for method 'getErrorCode()'.<br>The value of field 'errorCode' should be return properly.
     * </p>
     */
    public void testGetErrorCode_Accuracy() {
        assertEquals("Test accuracy for getErrorCode() failed, the value should be return properly.", "1290",
            exception.getErrorCode());

        exception = new BaseException();
        assertNull("Test accuracy for getErrorCode() failed, the value should be return properly.",
            exception.getErrorCode());
    }

    /**
     * <p>
     * Accuracy test case for method 'getModuleCode()'.<br>The value of field 'moduleCode' should be return properly.
     * </p>
     */
    public void testGetModuleCode_Accuracy() {
        assertEquals("Test accuracy for getModuleCode() failed, the value should be return properly.", "XMI_Writer",
            exception.getModuleCode());

        exception = new BaseException();
        assertNull("Test accuracy for getModuleCode() failed, the value should be return properly.",
            exception.getModuleCode());
    }

    /**
     * <p>
     * Accuracy test case for method 'getCreationDate()'.<br>
     * The value of field 'creationDate' should be return properly.
     * </p>
     */
    public void testGetCreationDate_Accuracy() {
        assertNotNull("Test accuracy for getCreationDate() failed, the value should be return properly.",
            exception.getCreationDate());
    }

    /**
     * <p>
     * Accuracy test case for method 'getThreadName()'.<br>The value of field 'threadName' should be return properly.
     * </p>
     */
    public void testGetThreadName_Accuracy() {
        assertEquals("Test accuracy for getThreadName() failed, the value should be return properly.",
            Thread.currentThread().getName(), exception.getThreadName());
    }

    /**
     * <p>
     * Accuracy test case for method 'getApplicationCode()'.<br>
     * The value of field 'applicationCode' should be return properly.
     * </p>
     */
    public void testGetApplicationCode_Accuracy() {
        assertEquals("Test accuracy for getApplicationCode() failed, the value should be return properly.", "TCUML",
            exception.getApplicationCode());
        exception = new BaseException();
        assertNull("Test accuracy for getApplicationCode() failed, the value should be return properly.",
            exception.getApplicationCode());
    }
}
