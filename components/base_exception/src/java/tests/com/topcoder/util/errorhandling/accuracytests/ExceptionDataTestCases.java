/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.accuracytests;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

import java.io.Serializable;


/**
 * <p>
 * Test the functionality of class <code>ExceptionData</code>.
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
public class ExceptionDataTestCases extends TestCase {
    /**
     * <p>
     * Represents an instance of <code>ExceptionData</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private ExceptionData data = null;

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
        data = null;
        super.tearDown();
    }

    /**
     * <p>
     * Test accuracy for method 'data()'.<br>
     * </p>
     */
    public void testExceptionData_Accuracy() {
        assertTrue("Test accuracy for method data() failed.", data instanceof Serializable);
    }

    /**
     * <p>
     * Accuracy test case for method 'isLogged()'.<br>
     * The method should work properly according to the use case, all the fields should be set as expected.
     * </p>
     */
    public void testLogged_Accuracy1() {
        assertFalse("Test accuracy for isLogged() failed.", data.isLogged());

        ExceptionData handle = data.setLogged(true);
        assertSame("Test accuracy for isLogged() failed.", data, handle);
        
        assertTrue("Test accuracy for isLogged() failed.", data.isLogged());

        data.setLogged(false);
        assertFalse("Test accuracy for isLogged() failed.", data.isLogged());
    }

    /**
     * <p>
     * Accuracy test case for method 'getErrorCode()'.<br>The value of field 'errorCode' should be return properly.
     * </p>
     */
    public void testGetErrorCode_Accuracy() {
        assertEquals("Test accuracy for getErrorCode() failed, the value should be return properly.", "1290",
            data.getErrorCode());

        data = new ExceptionData();
        assertNull("Test accuracy for getErrorCode() failed, the value should be return properly.", data.getErrorCode());
        
        ExceptionData handle = data.setErrorCode("code");
        assertSame("Test accuracy for isLogged() failed.", data, handle);
    }

    /**
     * <p>
     * Accuracy test case for method 'getModuleCode()'.<br>The value of field 'moduleCode' should be return properly.
     * </p>
     */
    public void testGetModuleCode_Accuracy() {
        assertEquals("Test accuracy for getModuleCode() failed, the value should be return properly.", "XMI_Writer",
            data.getModuleCode());

        data = new ExceptionData();
        assertNull("Test accuracy for getModuleCode() failed, the value should be return properly.",
            data.getModuleCode());
        
        ExceptionData handle = data.setModuleCode("code");
        assertSame("Test accuracy for isLogged() failed.", data, handle);
    }

    /**
     * <p>
     * Accuracy test case for method 'getCreationDate()'.<br>
     * The value of field 'creationDate' should be return properly.
     * </p>
     */
    public void testGetCreationDate_Accuracy() {
        assertNotNull("Test accuracy for getCreationDate() failed, the value should be return properly.",
            data.getCreationDate());
    }

    /**
     * <p>
     * Accuracy test case for method 'getThreadName()'.<br>The value of field 'threadName' should be return properly.
     * </p>
     */
    public void testGetThreadName_Accuracy() {
        assertEquals("Test accuracy for getThreadName() failed, the value should be return properly.",
            Thread.currentThread().getName(), data.getThreadName());
    }

    /**
     * <p>
     * Accuracy test case for method 'getApplicationCode()'.<br>
     * The value of field 'applicationCode' should be return properly.
     * </p>
     */
    public void testGetApplicationCode_Accuracy() {
        assertEquals("Test accuracy for getApplicationCode() failed, the value should be return properly.", "TCUML",
            data.getApplicationCode());
        
        data = new ExceptionData();
        assertNull("Test accuracy for getApplicationCode() failed, the value should be return properly.",
            data.getApplicationCode());
        
        ExceptionData handle = data.setApplicationCode("code");
        assertSame("Test accuracy for isLogged() failed.", data, handle);

    }
}
