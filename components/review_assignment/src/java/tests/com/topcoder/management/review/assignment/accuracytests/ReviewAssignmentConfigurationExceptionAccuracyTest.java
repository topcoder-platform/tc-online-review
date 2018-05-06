/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests;

import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.util.errorhandling.ExceptionData;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>A test case for {@link ReviewAssignmentConfigurationException} class.</p>
 * 
 * @author isv
 * @version 1.0
 */
public class ReviewAssignmentConfigurationExceptionAccuracyTest {

    /**
     * <p>A <code>ReviewAssignmentConfigurationException</code> instance to run the test against.</p>
     */
    private ReviewAssignmentConfigurationException testedInstance;

    /**
     * <p>Gets the test suite with the tests for this test case.</p>
     *
     * @return a <code>Test</code> providing the test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ReviewAssignmentConfigurationExceptionAccuracyTest.class);
    }

    /**
     * <p>Cleans up the test environment.</p>
     */
    @After
    public void tearDown() {
        this.testedInstance = null;
    }

    /**
     * <p>Tests the {@link ReviewAssignmentConfigurationException#ReviewAssignmentConfigurationException(String)} 
     * constructor for accuracy.</p>
     */
    @Test
    public void testConstructor_String() {
        String message = TestDataFactory.getExceptionMessage();
        this.testedInstance = new ReviewAssignmentConfigurationException(message);
        
        Assert.assertEquals("Wrong message", message, this.testedInstance.getMessage());
        Assert.assertNull("Application code is set by mistake", this.testedInstance.getApplicationCode());
        Assert.assertNull("Error code is set by mistake", this.testedInstance.getErrorCode());
        Assert.assertNull("Module code is set by mistake", this.testedInstance.getModuleCode());

    }

    /**
     * <p>Tests the 
     * {@link ReviewAssignmentConfigurationException#ReviewAssignmentConfigurationException(String, Throwable)}
     * constructor for accuracy.</p>
     */
    @Test
    public void testConstructor_String_Throwable() {
        String message = TestDataFactory.getExceptionMessage();
        Throwable cause = TestDataFactory.getExceptionCause();
        this.testedInstance = new ReviewAssignmentConfigurationException(message, cause);

        Assert.assertEquals("Wrong message", message, this.testedInstance.getMessage());
        Assert.assertSame("Wrong cause", cause, this.testedInstance.getCause());
        Assert.assertNull("Application code is set by mistake", this.testedInstance.getApplicationCode());
        Assert.assertNull("Error code is set by mistake", this.testedInstance.getErrorCode());
        Assert.assertNull("Module code is set by mistake", this.testedInstance.getModuleCode());

    }

    /**
     * <p>Tests the 
     * {@link ReviewAssignmentConfigurationException#ReviewAssignmentConfigurationException(String, ExceptionData)}
     * constructor for accuracy.</p>
     */
    @Test
    public void testConstructor_String_ExceptionData() {
        String message = TestDataFactory.getExceptionMessage();
        ExceptionData data = TestDataFactory.getExceptionData();
        this.testedInstance = new ReviewAssignmentConfigurationException(message, data);

        Assert.assertEquals("Wrong message", message, this.testedInstance.getMessage());
        Assert.assertEquals("Wrong application code", data.getApplicationCode(), this.testedInstance.getApplicationCode());
        Assert.assertEquals("Wrong module code", data.getModuleCode(), this.testedInstance.getModuleCode());
        Assert.assertEquals("Wrong error code", data.getErrorCode(), this.testedInstance.getErrorCode());
        Assert.assertEquals("Wrong logged flag", data.isLogged(), this.testedInstance.isLogged());

    }

    /**
     * <p>Tests the
     * {@link ReviewAssignmentConfigurationException#ReviewAssignmentConfigurationException(String, Throwable, 
     * ExceptionData)} constructor for accuracy.</p>
     */
    @Test
    public void testConstructor_String_Throwable_ExceptionData() {
        String message = TestDataFactory.getExceptionMessage();
        Throwable cause = TestDataFactory.getExceptionCause();
        ExceptionData data = TestDataFactory.getExceptionData();
        this.testedInstance = new ReviewAssignmentConfigurationException(message, cause, data);

        Assert.assertEquals("Wrong message", message, this.testedInstance.getMessage());
        Assert.assertSame("Wrong cause", cause, this.testedInstance.getCause());
        Assert.assertEquals("Wrong application code", data.getApplicationCode(), this.testedInstance.getApplicationCode());
        Assert.assertEquals("Wrong module code", data.getModuleCode(), this.testedInstance.getModuleCode());
        Assert.assertEquals("Wrong error code", data.getErrorCode(), this.testedInstance.getErrorCode());
        Assert.assertEquals("Wrong logged flag", data.isLogged(), this.testedInstance.isLogged());

    }
}
