/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;
import com.topcoder.management.review.scorecalculator.CalculationException;
import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;
/**
 * Tests for CalculationException class.
 * @author qiucx0161
 * @version 1.0
 */
public class TestCalculationException extends TestCase {
    /**
     * <p>
     * Error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "error message";

    /**
     * <p>
     * Cause used for testing.
     * </p>
     */
    private static final Exception CAUSE = new Exception();

    /**
     * <p>
     * Tests that CalculationException(String) instance is created and message argument is correctly
     * propagated.
     * </p>
     */
    public void testConstructor1_1() {
        CalculationException e = new CalculationException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate CalculationException", e);
        assertTrue("Exception should be extension of BaseException class", e instanceof BaseException);
        assertEquals("Error message is not properly set", ERROR_MESSAGE, e.getMessage());
    }

    /**
     * <p>
     * Tests that CalculationException(String, Throwable) instance is created and cause is correctly
     * propagated.
     * </p>
     */
    public void testConstructor2_1() {
        CalculationException e = new CalculationException(ERROR_MESSAGE, CAUSE);

        assertNotNull("Unable to instantiate CalculationException", e);
        assertEquals("Cause is not properly set", CAUSE, e.getCause());
    }
}
