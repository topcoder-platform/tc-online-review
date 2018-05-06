/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests;

import com.topcoder.search.builder.*;
import com.topcoder.search.builder.filter.*;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy test cases for ValidationResult.
 * </p>
 *
 * @author zjq
 * @version 1.1
 */
public class ValidationResultAccuracyTests extends TestCase {
    /**
     * The String of message for test.
     */
    private static final String MESSAGE = "message";

    /** A Filter used in some tests. */
    private static final Filter FILTER = new EqualToFilter("name", "value");

    /**
     * The param filter for test ValidationResult.
     */
    private Filter filter = null;

    /**
     * The invalid instance of ValidationResult for test.
     */
    private ValidationResult invalid = null;

    /**
     * The valid instance of ValidationResult for test.
     */
    private ValidationResult valid = null;

    /** A ValidationResult to run the tests on. */
    private ValidationResult result;

    /**
     * setUp.
     */
    protected void setUp() {
        filter = new EqualToFilter("age", new Integer(1));

        invalid = ValidationResult.createInvalidResult(MESSAGE, filter);

        valid = ValidationResult.createValidResult();
        result = ValidationResult.createInvalidResult(MESSAGE, FILTER);
    }

    /**
     * test the ValidationResultCreate.
     *
     */
    public void testValidationResultCreate1() {
        assertNotNull("The create of invalid ValidationResult should be success", invalid);

        assertNotNull("The create of valid ValidationResult should be success", valid);
    }

    /**
     * test the ValidationResultCreate.
     *
     */
    public void testValidationResultCreate2() {
        try {
            ValidationResult.createInvalidResult(MESSAGE, filter);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test the ValidationResultCreate.
     *
     */
    public void testValidationResultCreate3() {
        try {
            ValidationResult.createValidResult();
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test the invalid ValidationResult with value.
     *
     */
    public void tesstInvalid1() {
        //isValid return false with invalid result
        assertFalse("The isValid should return false.", invalid.isValid());

        //assertEquals with the message
        assertEquals("The message should be same as the set.", MESSAGE, invalid.getMessage());

        //assertFilter Equals
        assertEquals("The filter should be same as the set.", filter, invalid.getFailedFilter());
    }

    /**
     * test the valid ValidationResult.
     *
     */
    public void testvalid1() {
        //isvalid return true
        assertTrue("The isValid should return true", valid.isValid());
    }

    /**
     * Verify behavior of the getMessage method.
     */
    public void testGetMessage() {
        assertEquals("Incorrect message returned", MESSAGE, result.getMessage());
    }

    /**
     * Verify behavior of the getFailedFilter method.
     */
    public void testGetFailedFilter() {
        assertEquals("Incorrect filter returned", FILTER.getClass(), result.getFailedFilter().getClass());
    }

    /**
     * Verify behavior of the isValid method.
     */
    public void testIsValid() {
        assertFalse("Incorrect value returned", result.isValid());
    }
}
