/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.util.errorhandling.ExceptionData;


/**
 * <p>
 * Unit test case of {@link ProjectPaymentCalculatorException}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ProjectPaymentCalculatorExceptionTest {
    /**
     * <p>
     * Represents the error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "error message";

    /**
     * <p>
     * Represents the error code used for testing.
     * </p>
     */
    private static final String ERROR_CODE = "error code";

    /**
     * <p>
     * Represents ExceptionData instance used for testing.
     * </p>
     */
    private ExceptionData data;

    /**
     * <p>
     * Represents Throwable instance used for testing.
     * </p>
     */
    private Throwable cause;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ProjectPaymentCalculatorExceptionTest.class);
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @Before
    public void setUp() throws Exception {
        data = new ExceptionData();
        data.setErrorCode(ERROR_CODE);
        cause = new NullPointerException();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @After
    public void tearDown() throws Exception {
        data = null;
        cause = null;
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>ProjectPaymentCalculatorException#ProjectPaymentCalculatorException(java.lang.String)</code> .
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    @Test
    public void testCtor1() throws Exception {
        ProjectPaymentCalculatorException e = new ProjectPaymentCalculatorException(ERROR_MESSAGE);
        Assert.assertNotNull("Unable to instantiate ProjectPaymentCalculatorException", e);
        Assert.assertEquals("Error message is not properly propagated to super class", ERROR_MESSAGE, e
            .getMessage());
        Assert.assertNull("Error cause should be null", e.getCause());
    }

    /**
     * <p>
     * Accuracy test method for <code>ProjectPaymentCalculatorException
     * #ProjectPaymentCalculatorException(java.lang.String, java.lang.Throwable)</code>.
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    @Test
    public void testCtor2() throws Exception {
        ProjectPaymentCalculatorException e = new ProjectPaymentCalculatorException(ERROR_MESSAGE, cause);
        Assert.assertNotNull("Unable to instantiate ProjectPaymentCalculatorException", e);
        Assert.assertEquals("Error message is not properly propagated to super class", ERROR_MESSAGE, e
            .getMessage());
        Assert.assertEquals("Error cause is not properly propagated to super class", cause, e.getCause());
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>com.topcoder.reliability.ProjectPaymentCalculatorException#ProjectPaymentCalculatorException
     * (java.lang.String, com.topcoder.util.errorhandling.ExceptionData)</code>.
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    @Test
    public void testCtor3() throws Exception {
        ProjectPaymentCalculatorException e = new ProjectPaymentCalculatorException(ERROR_MESSAGE, data);
        Assert.assertNotNull("Unable to instantiate ProjectPaymentCalculatorException", e);
        Assert.assertEquals("Error message is not properly propagated to super class", ERROR_MESSAGE, e
            .getMessage());
        Assert.assertEquals("Exception data is not properly propagated to super class", ERROR_CODE, e
            .getErrorCode());
    }

    /**
     * <p>
     * Accuracy test method for <code>ProjectPaymentCalculatorException#ProjectPaymentCalculatorException(
     * java.lang.String, java.lang.Throwable, com.topcoder.util.errorhandling.ExceptionData)</code>.
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    @Test
    public void testCtor4() throws Exception {
        ProjectPaymentCalculatorException e = new ProjectPaymentCalculatorException(ERROR_MESSAGE, cause, data);
        Assert.assertNotNull("Unable to instantiate ProjectPaymentCalculatorException", e);
        Assert.assertEquals("Error message is not properly propagated to super class", ERROR_MESSAGE, e
            .getMessage());
        Assert.assertEquals("Error cause is not properly propagated to super class", cause, e.getCause());
        Assert.assertEquals("Exception data is not properly propagated to super class", ERROR_CODE, e
            .getErrorCode());
    }
}
