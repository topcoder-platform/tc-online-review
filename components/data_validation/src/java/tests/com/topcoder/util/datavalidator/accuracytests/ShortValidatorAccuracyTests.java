/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.ShortValidator;

import junit.framework.TestCase;


/**
 * Accuracy tests for the IntegerValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class ShortValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * ShortValidator instance to test.
     * <p>
     */
    private ShortValidator shortValidator;

    /**
     * <p>
     * tear down the environment.
     * </p>
     */
    protected void tearDown() {
        shortValidator = null;
    }

    /**
     * <p>
     * test method valid().
     * </p>
     */
    public void testValid() {
        shortValidator = ShortValidator.equalTo((short) 18);
        assertTrue(shortValidator.valid(new Float(18)));
        assertFalse(shortValidator.valid(new Float(19)));
        assertTrue(shortValidator.valid("18"));
        assertFalse(shortValidator.valid("19"));
        assertFalse(shortValidator.valid("18dot97"));
    }

    /**
     * <p>
     * test method greaterThan().
     * </p>
     */
    public void testGreaterThan() {
        shortValidator = ShortValidator.greaterThan((short) 18);
        assertNotNull("The instance should not be null.", shortValidator);
        assertTrue(shortValidator.valid(new Float(19)));
        assertFalse(shortValidator.valid(new Float(17)));
        assertTrue(shortValidator.valid((short) 19));
        assertFalse(shortValidator.valid((short) 17));
        assertTrue(shortValidator.valid("19"));
        assertFalse(shortValidator.valid("17"));
        assertNull(shortValidator.getMessage((short) 19));
        assertNotNull(shortValidator.getMessage((short) 17));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo().
     * </p>
     */
    public void testGreaterThanOrEqualTo() {
        shortValidator = ShortValidator.greaterThanOrEqualTo((short) 18);
        assertNotNull("The instance should not be null.", shortValidator);
        assertTrue(shortValidator.valid(new Integer(18)));
        assertTrue(shortValidator.valid(new Integer(19)));
        assertFalse(shortValidator.valid(new Integer(17)));
        assertTrue(shortValidator.valid((short) 18));
        assertTrue(shortValidator.valid((short) 19));
        assertFalse(shortValidator.valid((short) 17));
        assertTrue(shortValidator.valid("18"));
        assertTrue(shortValidator.valid("19"));
        assertFalse(shortValidator.valid("17"));
        assertNull(shortValidator.getMessage((short) 18));
        assertNull(shortValidator.getMessage((short) 19));
        assertNotNull(shortValidator.getMessage((short) 17));
    }

    /**
     * <p>
     * test method lessThan().
     * </p>
     */
    public void testLessThan() {
        shortValidator = ShortValidator.lessThan((short) 18);
        assertNotNull("The instance should not be null.", shortValidator);
        assertTrue(shortValidator.valid(new Integer(17)));
        assertFalse(shortValidator.valid(new Integer(19)));
        assertTrue(shortValidator.valid((short) 17));
        assertFalse(shortValidator.valid((short) 19));
        assertTrue(shortValidator.valid("17"));
        assertFalse(shortValidator.valid("19"));
        assertNull(shortValidator.getMessage((short) 17));
        assertNotNull(shortValidator.getMessage((short) 19));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo().
     * </p>
     */
    public void testLessThanOrEqualTo() {
        shortValidator = ShortValidator.lessThanOrEqualTo((short) 18);
        assertNotNull("The instance should not be null.", shortValidator);
        assertTrue(shortValidator.valid(new Integer(18)));
        assertTrue(shortValidator.valid(new Integer(17)));
        assertFalse(shortValidator.valid(new Integer(19)));
        assertTrue(shortValidator.valid((short) 18));
        assertTrue(shortValidator.valid((short) 17));
        assertFalse(shortValidator.valid((short) 19));
        assertTrue(shortValidator.valid("18"));
        assertTrue(shortValidator.valid("17"));
        assertFalse(shortValidator.valid("19"));
        assertNull(shortValidator.getMessage((short) 18));
        assertNull(shortValidator.getMessage((short) 17));
        assertNotNull(shortValidator.getMessage((short) 19));
    }

    /**
     * <p>
     * test method equalTo().
     * </p>
     */
    public void testEqualTo() {
        shortValidator = ShortValidator.equalTo((short) 18);
        assertNotNull("The instance should not be null.", shortValidator);
        assertTrue(shortValidator.valid(new Integer(18)));
        assertFalse(shortValidator.valid(new Integer(19)));
        assertTrue(shortValidator.valid((short) 18));
        assertFalse(shortValidator.valid((short) 19));
        assertTrue(shortValidator.valid("18"));
        assertFalse(shortValidator.valid("19"));
        assertNull(shortValidator.getMessage((short) 18));
        assertNotNull(shortValidator.getMessage((short) 19));
    }

    /**
     * <p>
     * test method inRange().
     * </p>
     */
    public void testInRange() {
        shortValidator = ShortValidator.inRange((short) 18, (short) 38);
        assertNotNull("The instance should not be null.", shortValidator);
        assertTrue(shortValidator.valid(new Integer(28)));
        assertTrue(shortValidator.valid(new Integer(18)));
        assertTrue(shortValidator.valid(new Integer(38)));
        assertFalse(shortValidator.valid(new Integer(17)));
        assertFalse(shortValidator.valid(new Integer(39)));
        assertTrue(shortValidator.valid((short) 28));
        assertTrue(shortValidator.valid((short) 18));
        assertTrue(shortValidator.valid((short) 38));
        assertFalse(shortValidator.valid((short) 17));
        assertFalse(shortValidator.valid((short) 39));
        assertTrue(shortValidator.valid("28"));
        assertTrue(shortValidator.valid("18"));
        assertTrue(shortValidator.valid("38"));
        assertFalse(shortValidator.valid("17"));
        assertFalse(shortValidator.valid("39"));
        assertNull(shortValidator.getMessage((short) 28));
        assertNull(shortValidator.getMessage((short) 18));
        assertNull(shortValidator.getMessage((short) 38));
        assertNotNull(shortValidator.getMessage((short) 17));
        assertNotNull(shortValidator.getMessage((short) 39));
    }

    /**
     * <p>
     * test method inExclusiveRange().
     * </p>
     */
    public void testInExclusiveRange() {
        shortValidator = ShortValidator.inExclusiveRange((short) 18, (short) 38);
        assertNotNull("The instance should not be null.", shortValidator);
        assertTrue(shortValidator.valid(new Integer(28)));
        assertFalse(shortValidator.valid(new Integer(18)));
        assertFalse(shortValidator.valid(new Integer(38)));
        assertFalse(shortValidator.valid(new Integer(17)));
        assertFalse(shortValidator.valid(new Integer(39)));
        assertTrue(shortValidator.valid((short) 28));
        assertFalse(shortValidator.valid((short) 18));
        assertFalse(shortValidator.valid((short) 38));
        assertFalse(shortValidator.valid((short) 17));
        assertFalse(shortValidator.valid((short) 39));
        assertTrue(shortValidator.valid("28"));
        assertFalse(shortValidator.valid("18"));
        assertFalse(shortValidator.valid("38"));
        assertFalse(shortValidator.valid("17"));
        assertFalse(shortValidator.valid("39"));
        assertNull(shortValidator.getMessage((short) 28));
        assertNotNull(shortValidator.getMessage((short) 18));
        assertNotNull(shortValidator.getMessage((short) 38));
        assertNotNull(shortValidator.getMessage((short) 17));
        assertNotNull(shortValidator.getMessage((short) 39));
    }
}
