/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.DoubleValidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * Accuracy tests for the BooleanValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class DoubleValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * DoubleValidator instance to test.
     * <p>
     */
    private DoubleValidator doubleValidator;

    /**
     * <p>
     * BundleInfo case used to test.
     * </p>
     */
    private BundleInfo bundleInfo;

    /**
     * <p>
     * The message key for the bundle info.
     * </p>
     */
    String key;

    /**
     * <p>
     * setUp().
     * </p>
     */
    protected void setUp() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("accuracy.test", new Locale("en"));
        bundleInfo.setDefaultMessage("accuracy test");
    }

    /**
     * <p>
     * tear down the environment.
     * </p>
     */
    protected void tearDown() {
        bundleInfo = null;
        key = null;
        doubleValidator = null;
    }

    /**
     * <p>
     * test method setEpsilon().
     * </p>
     */
    public void testSetEpsilon() {
        doubleValidator = DoubleValidator.equalTo(23.23);
        doubleValidator.setEpsilon(0.1);
        assertEquals(doubleValidator.getEpsilon(), 0.1, 0.1);
    }

    /**
     * <p>
     * test method valid().
     * </p>
     */
    public void testValid() {
        doubleValidator = DoubleValidator.equalTo(18.97);
        doubleValidator.setEpsilon(0);
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertFalse(doubleValidator.valid(new Double(19.97)));
        assertTrue(doubleValidator.valid("18.97"));
        assertFalse(doubleValidator.valid("19.97"));
        assertFalse(doubleValidator.valid("18dot97"));
    }

    /**
     * <p>
     * test method greaterThan().
     * </p>
     */
    public void testGreaterThan() {
        doubleValidator = DoubleValidator.greaterThan(18.97);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(19.97)));
        assertFalse(doubleValidator.valid(new Double(17.97)));
        assertTrue(doubleValidator.valid(19.97));
        assertFalse(doubleValidator.valid(17.97));
        assertTrue(doubleValidator.valid("19.97"));
        assertFalse(doubleValidator.valid("17.97"));
        assertNull(doubleValidator.getMessage(19.97));
        assertNotNull(doubleValidator.getMessage(17.97));
    }

    /**
     * <p>
     * test method greaterThan() with 2 parameters.
     * </p>
     */
    public void testGreaterThan_Bundle() {
        key = "GreaterThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.greaterThan(18.97, bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(19.97)));
        assertFalse(doubleValidator.valid(new Double(17.97)));
        assertTrue(doubleValidator.valid(19.97));
        assertFalse(doubleValidator.valid(17.97));
        assertTrue(doubleValidator.valid("19.97"));
        assertFalse(doubleValidator.valid("17.97"));
        assertNull(doubleValidator.getMessage(19.97));
        assertNotNull(doubleValidator.getMessage(17.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(17.97));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo().
     * </p>
     */
    public void testGreaterThanOrEqualTo() {
        doubleValidator = DoubleValidator.greaterThanOrEqualTo(18.97);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertTrue(doubleValidator.valid(new Double(19.97)));
        assertFalse(doubleValidator.valid(new Double(17.97)));
        assertTrue(doubleValidator.valid(18.97));
        assertTrue(doubleValidator.valid(19.97));
        assertFalse(doubleValidator.valid(17.97));
        assertTrue(doubleValidator.valid("18.97"));
        assertTrue(doubleValidator.valid("19.97"));
        assertFalse(doubleValidator.valid("17.97"));
        assertNull(doubleValidator.getMessage(18.97));
        assertNull(doubleValidator.getMessage(19.97));
        assertNotNull(doubleValidator.getMessage(17.97));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo() with 2 parameters.
     * </p>
     */
    public void testGreaterThanOrEqualTo_Bundle() {
        key = "GreaterThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.greaterThanOrEqualTo(18.97, this.bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertTrue(doubleValidator.valid(new Double(19.97)));
        assertFalse(doubleValidator.valid(new Double(17.97)));
        assertTrue(doubleValidator.valid(18.97));
        assertTrue(doubleValidator.valid(19.97));
        assertFalse(doubleValidator.valid(17.97));
        assertTrue(doubleValidator.valid("18.97"));
        assertTrue(doubleValidator.valid("19.97"));
        assertFalse(doubleValidator.valid("17.97"));
        assertNull(doubleValidator.getMessage(18.97));
        assertNull(doubleValidator.getMessage(19.97));
        assertNotNull(doubleValidator.getMessage(17.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(17.97));
    }

    /**
     * <p>
     * test method lessThan().
     * </p>
     */
    public void testLessThan() {
        doubleValidator = DoubleValidator.lessThan(18.97);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(17.97)));
        assertFalse(doubleValidator.valid(new Double(19.97)));
        assertTrue(doubleValidator.valid(17.97));
        assertFalse(doubleValidator.valid(19.97));
        assertTrue(doubleValidator.valid("17.97"));
        assertFalse(doubleValidator.valid("19.97"));
        assertNull(doubleValidator.getMessage(17.97));
        assertNotNull(doubleValidator.getMessage(19.97));
    }

    /**
     * <p>
     * test method lessThan() with 2 parameters.
     * </p>
     */
    public void testLessThan_Bundle() {
        key = "LessThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.lessThan(18.97, this.bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(17.97)));
        assertFalse(doubleValidator.valid(new Double(19.97)));
        assertTrue(doubleValidator.valid(17.97));
        assertFalse(doubleValidator.valid(19.97));
        assertTrue(doubleValidator.valid("17.97"));
        assertFalse(doubleValidator.valid("19.97"));
        assertNull(doubleValidator.getMessage(17.97));
        assertNotNull(doubleValidator.getMessage(19.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(19.97));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo().
     * </p>
     */
    public void testLessThanOrEqualTo() {
        doubleValidator = DoubleValidator.lessThanOrEqualTo(18.97);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertTrue(doubleValidator.valid(new Double(17.97)));
        assertFalse(doubleValidator.valid(new Double(19.97)));
        assertTrue(doubleValidator.valid(18.97));
        assertTrue(doubleValidator.valid(17.97));
        assertFalse(doubleValidator.valid(19.97));
        assertTrue(doubleValidator.valid("18.97"));
        assertTrue(doubleValidator.valid("17.97"));
        assertFalse(doubleValidator.valid("19.97"));
        assertNull(doubleValidator.getMessage(18.97));
        assertNull(doubleValidator.getMessage(17.97));
        assertNotNull(doubleValidator.getMessage(19.97));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo() with 2 parameters.
     * </p>
     */
    public void testLessThanOrEqualTo_Bundle() {
        key = "LessThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.lessThanOrEqualTo(18.97, this.bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertTrue(doubleValidator.valid(new Double(17.97)));
        assertFalse(doubleValidator.valid(new Double(19.97)));
        assertTrue(doubleValidator.valid(18.97));
        assertTrue(doubleValidator.valid(17.97));
        assertFalse(doubleValidator.valid(19.97));
        assertTrue(doubleValidator.valid("18.97"));
        assertTrue(doubleValidator.valid("17.97"));
        assertFalse(doubleValidator.valid("19.97"));
        assertNull(doubleValidator.getMessage(18.97));
        assertNull(doubleValidator.getMessage(17.97));
        assertNotNull(doubleValidator.getMessage(19.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(19.97));
    }

    /**
     * <p>
     * test method equalTo().
     * </p>
     */
    public void testEqualTo() {
        doubleValidator = DoubleValidator.equalTo(18.97);
        doubleValidator.setEpsilon(0);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertFalse(doubleValidator.valid(new Double(19.97)));
        assertTrue(doubleValidator.valid(18.97));
        assertFalse(doubleValidator.valid(19.97));
        assertTrue(doubleValidator.valid("18.97"));
        assertFalse(doubleValidator.valid("19.97"));
        assertNull(doubleValidator.getMessage(18.97));
        assertNotNull(doubleValidator.getMessage(19.97));
    }

    /**
     * <p>
     * test method equalTo() with 2 parameters.
     * </p>
     */
    public void testEqualTo_Bundle() {
        key = "LessThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.equalTo(18.97, this.bundleInfo);
        doubleValidator.setEpsilon(0);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertFalse(doubleValidator.valid(new Double(19.97)));
        assertTrue(doubleValidator.valid(18.97));
        assertFalse(doubleValidator.valid(19.97));
        assertTrue(doubleValidator.valid("18.97"));
        assertFalse(doubleValidator.valid("19.97"));
        assertNull(doubleValidator.getMessage(18.97));
        assertNotNull(doubleValidator.getMessage(19.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(19.97));
    }

    /**
     * <p>
     * test method inRange().
     * </p>
     */
    public void testInRange() {
        doubleValidator = DoubleValidator.inRange(18.97, 38.97);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(28.97)));
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertTrue(doubleValidator.valid(new Double(38.97)));
        assertFalse(doubleValidator.valid(new Double(17.97)));
        assertFalse(doubleValidator.valid(new Double(39.97)));
        assertTrue(doubleValidator.valid(28.97));
        assertTrue(doubleValidator.valid(18.97));
        assertTrue(doubleValidator.valid(38.97));
        assertFalse(doubleValidator.valid(17.97));
        assertFalse(doubleValidator.valid(39.97));
        assertTrue(doubleValidator.valid("28.97"));
        assertTrue(doubleValidator.valid("18.97"));
        assertTrue(doubleValidator.valid("38.97"));
        assertFalse(doubleValidator.valid("17.97"));
        assertFalse(doubleValidator.valid("39.97"));
        assertNull(doubleValidator.getMessage(28.97));
        assertNull(doubleValidator.getMessage(18.97));
        assertNull(doubleValidator.getMessage(38.97));
        assertNotNull(doubleValidator.getMessage(17.97));
        assertNotNull(doubleValidator.getMessage(39.97));
    }

    /**
     * <p>
     * test method inRange() with 3 parameters.
     * </p>
     */
    public void testInRange_Bundle() {
        key = "InRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.inRange(18.97, 38.97, this.bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(18.97)));
        assertTrue(doubleValidator.valid(new Double(38.97)));
        assertFalse(doubleValidator.valid(new Double(17.97)));
        assertFalse(doubleValidator.valid(new Double(39.97)));
        assertTrue(doubleValidator.valid(18.97));
        assertTrue(doubleValidator.valid(38.97));
        assertFalse(doubleValidator.valid(17.97));
        assertFalse(doubleValidator.valid(39.97));
        assertTrue(doubleValidator.valid("18.97"));
        assertTrue(doubleValidator.valid("38.97"));
        assertFalse(doubleValidator.valid("17.97"));
        assertFalse(doubleValidator.valid("39.97"));
        assertNull(doubleValidator.getMessage(18.97));
        assertNull(doubleValidator.getMessage(38.97));
        assertNotNull(doubleValidator.getMessage(17.97));
        assertNotNull(doubleValidator.getMessage(39.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(17.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(39.97));
    }

    /**
     * <p>
     * test method inExclusiveRange().
     * </p>
     */
    public void testInExclusiveRange() {
        doubleValidator = DoubleValidator.inExclusiveRange(18.97, 38.97);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(28.97)));
        assertFalse(doubleValidator.valid(new Double(18.97)));
        assertFalse(doubleValidator.valid(new Double(38.97)));
        assertFalse(doubleValidator.valid(new Double(17.97)));
        assertFalse(doubleValidator.valid(new Double(39.97)));
        assertTrue(doubleValidator.valid(28.97));
        assertFalse(doubleValidator.valid(18.97));
        assertFalse(doubleValidator.valid(38.97));
        assertFalse(doubleValidator.valid(17.97));
        assertFalse(doubleValidator.valid(39.97));
        assertTrue(doubleValidator.valid("28.97"));
        assertFalse(doubleValidator.valid("18.97"));
        assertFalse(doubleValidator.valid("38.97"));
        assertFalse(doubleValidator.valid("17.97"));
        assertFalse(doubleValidator.valid("39.97"));
        assertNull(doubleValidator.getMessage(28.97));
        assertNotNull(doubleValidator.getMessage(18.97));
        assertNotNull(doubleValidator.getMessage(38.97));
        assertNotNull(doubleValidator.getMessage(17.97));
        assertNotNull(doubleValidator.getMessage(39.97));
    }

    /**
     * <p>
     * test method inExclusiveRange() with 3 parameters.
     * </p>
     */
    public void testInExclusiveRange_Bundle() {
        key = "InExclusiveRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.inExclusiveRange(18.97, 38.97, this.bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(new Double(28.97)));
        assertFalse(doubleValidator.valid(new Double(18.97)));
        assertFalse(doubleValidator.valid(new Double(38.97)));
        assertFalse(doubleValidator.valid(new Double(17.97)));
        assertFalse(doubleValidator.valid(new Double(39.97)));
        assertTrue(doubleValidator.valid(28.97));
        assertFalse(doubleValidator.valid(18.97));
        assertFalse(doubleValidator.valid(38.97));
        assertFalse(doubleValidator.valid(17.97));
        assertFalse(doubleValidator.valid(39.97));
        assertTrue(doubleValidator.valid("28.97"));
        assertFalse(doubleValidator.valid("18.97"));
        assertFalse(doubleValidator.valid("38.97"));
        assertFalse(doubleValidator.valid("17.97"));
        assertFalse(doubleValidator.valid("39.97"));
        assertNull(doubleValidator.getMessage(28.97));
        assertNotNull(doubleValidator.getMessage(18.97));
        assertNotNull(doubleValidator.getMessage(38.97));
        assertNotNull(doubleValidator.getMessage(17.97));
        assertNotNull(doubleValidator.getMessage(39.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(18.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(38.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(17.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(39.97));
    }

    /**
     * <p>
     * test method isPositive().
     * </p>
     */
    public void testIsPositive() {
        doubleValidator = DoubleValidator.isPositive();
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(18.97));
        assertFalse(doubleValidator.valid(-18.97));
        assertNull(doubleValidator.getMessage(18.97));
        assertNotNull(doubleValidator.getMessage(-18.97));
    }

    /**
     * <p>
     * test method isPositive() with 1 parameters.
     * </p>
     */
    public void testIsPositive_Bundle() {
        key = "IsPositive_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.isPositive(this.bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(18.97));
        assertFalse(doubleValidator.valid(-18.97));
        assertNull(doubleValidator.getMessage(18.97));
        assertNotNull(doubleValidator.getMessage(-18.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(-18.97));
    }

    /**
     * <p>
     * test method isNegative().
     * </p>
     */
    public void testIsNegative() {
        doubleValidator = DoubleValidator.isNegative();
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(-18.97));
        assertFalse(doubleValidator.valid(18.97));
        assertNull(doubleValidator.getMessage(-18.97));
        assertNotNull(doubleValidator.getMessage(18.97));
    }

    /**
     * <p>
     * test method isNegative() with 1 parameters.
     * </p>
     */
    public void testIsNegative_Bundle() {
        key = "IsNegative_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.isNegative(this.bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(-18.97));
        assertFalse(doubleValidator.valid(18.97));
        assertNull(doubleValidator.getMessage(-18.97));
        assertNotNull(doubleValidator.getMessage(18.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(18.97));
    }

    /**
     * <p>
     * test method isInteger().
     * </p>
     */
    public void testIsInteger() {
        doubleValidator = DoubleValidator.isInteger();
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(18));
        assertFalse(doubleValidator.valid(18.97));
        assertNull(doubleValidator.getMessage(18));
        assertNotNull(doubleValidator.getMessage(18.97));
    }

    /**
     * <p>
     * test method isInteger() with 1 parameters.
     * </p>
     */
    public void testIsInteger_Bundle() {
        key = "IsInteger_Bundle_Key";
        bundleInfo.setMessageKey(key);
        doubleValidator = DoubleValidator.isInteger(this.bundleInfo);
        assertNotNull("The instance should not be null.", doubleValidator);
        assertTrue(doubleValidator.valid(18));
        assertFalse(doubleValidator.valid(18.97));
        assertNull(doubleValidator.getMessage(18));
        assertNotNull(doubleValidator.getMessage(18.97));
        assertEquals("The message should be the same.", key, doubleValidator.getMessage(18.97));
    }
}
