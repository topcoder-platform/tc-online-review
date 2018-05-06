/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.FloatValidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * Accuracy tests for the FloatValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class FloatValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * FloatValidator instance to test.
     * <p>
     */
    private FloatValidator floatValidator;

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
        floatValidator = null;
    }

    /**
     * <p>
     * test method setEpsilon().
     * </p>
     */
    public void testSetEpsilon() {
        floatValidator = FloatValidator.equalTo(23.23f);
        floatValidator.setEpsilon(0.1f);
        assertEquals(floatValidator.getEpsilon(), 0.1, 0.1);
    }

    /**
     * <p>
     * test method valid().
     * </p>
     */
    public void testValid() {
        floatValidator = FloatValidator.equalTo(18.97f);
        floatValidator.setEpsilon(0);
        assertTrue(floatValidator.valid(new Float(18.97)));
        assertFalse(floatValidator.valid(new Float(19.97)));
        assertTrue(floatValidator.valid("18.97"));
        assertFalse(floatValidator.valid("19.97"));
        assertFalse(floatValidator.valid("18dot97"));
    }

    /**
     * <p>
     * test method greaterThan().
     * </p>
     */
    public void testGreaterThan() {
        floatValidator = FloatValidator.greaterThan(18.97f);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(19.97f)));
        assertFalse(floatValidator.valid(new Float(17.97f)));
        assertTrue(floatValidator.valid(19.97f));
        assertFalse(floatValidator.valid(17.97f));
        assertTrue(floatValidator.valid("19.97"));
        assertFalse(floatValidator.valid("17.97"));
        assertNull(floatValidator.getMessage(19.97f));
        assertNotNull(floatValidator.getMessage(17.97f));
    }

    /**
     * <p>
     * test method greaterThan() with 2 parameters.
     * </p>
     */
    public void testGreaterThan_Bundle() {
        key = "GreaterThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.greaterThan(18.97f, bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(19.97)));
        assertFalse(floatValidator.valid(new Float(17.97)));
        assertTrue(floatValidator.valid(19.97f));
        assertFalse(floatValidator.valid(17.97f));
        assertTrue(floatValidator.valid("19.97"));
        assertFalse(floatValidator.valid("17.97"));
        assertNull(floatValidator.getMessage(19.97f));
        assertNotNull(floatValidator.getMessage(17.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(17.97f));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo().
     * </p>
     */
    public void testGreaterThanOrEqualTo() {
        floatValidator = FloatValidator.greaterThanOrEqualTo(18.97f);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(18.97)));
        assertTrue(floatValidator.valid(new Float(19.97)));
        assertFalse(floatValidator.valid(new Float(17.97)));
        assertTrue(floatValidator.valid(18.97f));
        assertTrue(floatValidator.valid(19.97f));
        assertFalse(floatValidator.valid(17.97f));
        assertTrue(floatValidator.valid("18.97"));
        assertTrue(floatValidator.valid("19.97"));
        assertFalse(floatValidator.valid("17.97"));
        assertNull(floatValidator.getMessage(18.97f));
        assertNull(floatValidator.getMessage(19.97f));
        assertNotNull(floatValidator.getMessage(17.97f));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo() with 2 parameters.
     * </p>
     */
    public void testGreaterThanOrEqualTo_Bundle() {
        key = "GreaterThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.greaterThanOrEqualTo(18.97f, this.bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(18.97)));
        assertTrue(floatValidator.valid(new Float(19.97)));
        assertFalse(floatValidator.valid(new Float(17.97)));
        assertTrue(floatValidator.valid(18.97f));
        assertTrue(floatValidator.valid(19.97f));
        assertFalse(floatValidator.valid(17.97f));
        assertTrue(floatValidator.valid("18.97"));
        assertTrue(floatValidator.valid("19.97"));
        assertFalse(floatValidator.valid("17.97"));
        assertNull(floatValidator.getMessage(18.97f));
        assertNull(floatValidator.getMessage(19.97f));
        assertNotNull(floatValidator.getMessage(17.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(17.97f));
    }

    /**
     * <p>
     * test method lessThan().
     * </p>
     */
    public void testLessThan() {
        floatValidator = FloatValidator.lessThan(18.97f);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(17.97)));
        assertFalse(floatValidator.valid(new Float(19.97)));
        assertTrue(floatValidator.valid(17.97f));
        assertFalse(floatValidator.valid(19.97f));
        assertTrue(floatValidator.valid("17.97"));
        assertFalse(floatValidator.valid("19.97"));
        assertNull(floatValidator.getMessage(17.97f));
        assertNotNull(floatValidator.getMessage(19.97f));
    }

    /**
     * <p>
     * test method lessThan() with 2 parameters.
     * </p>
     */
    public void testLessThan_Bundle() {
        key = "LessThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.lessThan(18.97f, this.bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(17.97)));
        assertFalse(floatValidator.valid(new Float(19.97)));
        assertTrue(floatValidator.valid(17.97f));
        assertFalse(floatValidator.valid(19.97f));
        assertTrue(floatValidator.valid("17.97"));
        assertFalse(floatValidator.valid("19.97"));
        assertNull(floatValidator.getMessage(17.97f));
        assertNotNull(floatValidator.getMessage(19.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(19.97f));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo().
     * </p>
     */
    public void testLessThanOrEqualTo() {
        floatValidator = FloatValidator.lessThanOrEqualTo(18.97f);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(18.97)));
        assertTrue(floatValidator.valid(new Float(17.97)));
        assertFalse(floatValidator.valid(new Float(19.97)));
        assertTrue(floatValidator.valid(18.97f));
        assertTrue(floatValidator.valid(17.97f));
        assertFalse(floatValidator.valid(19.97f));
        assertTrue(floatValidator.valid("18.97"));
        assertTrue(floatValidator.valid("17.97"));
        assertFalse(floatValidator.valid("19.97"));
        assertNull(floatValidator.getMessage(18.97f));
        assertNull(floatValidator.getMessage(17.97f));
        assertNotNull(floatValidator.getMessage(19.97f));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo() with 2 parameters.
     * </p>
     */
    public void testLessThanOrEqualTo_Bundle() {
        key = "LessThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.lessThanOrEqualTo(18.97f, this.bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(18.97)));
        assertTrue(floatValidator.valid(new Float(17.97)));
        assertFalse(floatValidator.valid(new Float(19.97)));
        assertTrue(floatValidator.valid(18.97f));
        assertTrue(floatValidator.valid(17.97f));
        assertFalse(floatValidator.valid(19.97f));
        assertTrue(floatValidator.valid("18.97"));
        assertTrue(floatValidator.valid("17.97"));
        assertFalse(floatValidator.valid("19.97"));
        assertNull(floatValidator.getMessage(18.97f));
        assertNull(floatValidator.getMessage(17.97f));
        assertNotNull(floatValidator.getMessage(19.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(19.97f));
    }

    /**
     * <p>
     * test method equalTo().
     * </p>
     */
    public void testEqualTo() {
        floatValidator = FloatValidator.equalTo(18.97f);
        floatValidator.setEpsilon(0);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(18.97)));
        assertFalse(floatValidator.valid(new Float(19.97)));
        assertTrue(floatValidator.valid(18.97f));
        assertFalse(floatValidator.valid(19.97f));
        assertTrue(floatValidator.valid("18.97"));
        assertFalse(floatValidator.valid("19.97"));
        assertNull(floatValidator.getMessage(18.97f));
        assertNotNull(floatValidator.getMessage(19.97f));
    }

    /**
     * <p>
     * test method equalTo() with 2 parameters.
     * </p>
     */
    public void testEqualTo_Bundle() {
        key = "LessThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.equalTo(18.97f, this.bundleInfo);
        floatValidator.setEpsilon(0);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(18.97)));
        assertFalse(floatValidator.valid(new Float(19.97)));
        assertTrue(floatValidator.valid(18.97f));
        assertFalse(floatValidator.valid(19.97f));
        assertTrue(floatValidator.valid("18.97"));
        assertFalse(floatValidator.valid("19.97"));
        assertNull(floatValidator.getMessage(18.97f));
        assertNotNull(floatValidator.getMessage(19.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(19.97f));
    }

    /**
     * <p>
     * test method inRange().
     * </p>
     */
    public void testInRange() {
        floatValidator = FloatValidator.inRange(18.97f, 38.97f);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(28.97)));
        assertTrue(floatValidator.valid(new Float(18.97)));
        assertTrue(floatValidator.valid(new Float(38.97)));
        assertFalse(floatValidator.valid(new Float(17.97)));
        assertFalse(floatValidator.valid(new Float(39.97)));
        assertTrue(floatValidator.valid(28.97f));
        assertTrue(floatValidator.valid(18.97f));
        assertTrue(floatValidator.valid(38.97f));
        assertFalse(floatValidator.valid(17.97f));
        assertFalse(floatValidator.valid(39.97f));
        assertTrue(floatValidator.valid("28.97"));
        assertTrue(floatValidator.valid("18.97"));
        assertTrue(floatValidator.valid("38.97"));
        assertFalse(floatValidator.valid("17.97"));
        assertFalse(floatValidator.valid("39.97"));
        assertNull(floatValidator.getMessage(28.97f));
        assertNull(floatValidator.getMessage(18.97f));
        assertNull(floatValidator.getMessage(38.97f));
        assertNotNull(floatValidator.getMessage(17.97f));
        assertNotNull(floatValidator.getMessage(39.97f));
    }

    /**
     * <p>
     * test method inRange() with 3 parameters.
     * </p>
     */
    public void testInRange_Bundle() {
        key = "InRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.inRange(18.97f, 38.97f, this.bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(18.97f)));
        assertTrue(floatValidator.valid(new Float(38.97f)));
        assertFalse(floatValidator.valid(new Float(17.97f)));
        assertFalse(floatValidator.valid(new Float(39.97f)));
        assertTrue(floatValidator.valid(18.97f));
        assertTrue(floatValidator.valid(38.97f));
        assertFalse(floatValidator.valid(17.97f));
        assertFalse(floatValidator.valid(39.97f));
        assertTrue(floatValidator.valid("18.97"));
        assertTrue(floatValidator.valid("38.97"));
        assertFalse(floatValidator.valid("17.97"));
        assertFalse(floatValidator.valid("39.97"));
        assertNull(floatValidator.getMessage(18.97f));
        assertNull(floatValidator.getMessage(38.97f));
        assertNotNull(floatValidator.getMessage(17.97f));
        assertNotNull(floatValidator.getMessage(39.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(17.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(39.97f));
    }

    /**
     * <p>
     * test method inExclusiveRange().
     * </p>
     */
    public void testInExclusiveRange() {
        floatValidator = FloatValidator.inExclusiveRange(18.97f, 38.97f);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(28.97f)));
        assertFalse(floatValidator.valid(new Float(18.97f)));
        assertFalse(floatValidator.valid(new Float(38.97f)));
        assertFalse(floatValidator.valid(new Float(17.97f)));
        assertFalse(floatValidator.valid(new Float(39.97f)));
        assertTrue(floatValidator.valid(28.97f));
        assertFalse(floatValidator.valid(18.97f));
        assertFalse(floatValidator.valid(38.97f));
        assertFalse(floatValidator.valid(17.97f));
        assertFalse(floatValidator.valid(39.97f));
        assertTrue(floatValidator.valid("28.97"));
        assertFalse(floatValidator.valid("18.97"));
        assertFalse(floatValidator.valid("38.97"));
        assertFalse(floatValidator.valid("17.97"));
        assertFalse(floatValidator.valid("39.97"));
        assertNull(floatValidator.getMessage(28.97f));
        assertNotNull(floatValidator.getMessage(18.97f));
        assertNotNull(floatValidator.getMessage(38.97f));
        assertNotNull(floatValidator.getMessage(17.97f));
        assertNotNull(floatValidator.getMessage(39.97f));
    }

    /**
     * <p>
     * test method inExclusiveRange() with 3 parameters.
     * </p>
     */
    public void testInExclusiveRange_Bundle() {
        key = "InExclusiveRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.inExclusiveRange(18.97f, 38.97f, this.bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(new Float(28.97f)));
        assertFalse(floatValidator.valid(new Float(18.97f)));
        assertFalse(floatValidator.valid(new Float(38.97f)));
        assertFalse(floatValidator.valid(new Float(17.97f)));
        assertFalse(floatValidator.valid(new Float(39.97f)));
        assertTrue(floatValidator.valid(28.97f));
        assertFalse(floatValidator.valid(18.97f));
        assertFalse(floatValidator.valid(38.97f));
        assertFalse(floatValidator.valid(17.97f));
        assertFalse(floatValidator.valid(39.97f));
        assertTrue(floatValidator.valid("28.97f"));
        assertFalse(floatValidator.valid("18.97f"));
        assertFalse(floatValidator.valid("38.97f"));
        assertFalse(floatValidator.valid("17.97f"));
        assertFalse(floatValidator.valid("39.97f"));
        assertNull(floatValidator.getMessage(28.97f));
        assertNotNull(floatValidator.getMessage(18.97f));
        assertNotNull(floatValidator.getMessage(38.97f));
        assertNotNull(floatValidator.getMessage(17.97f));
        assertNotNull(floatValidator.getMessage(39.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(18.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(38.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(17.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(39.97f));
    }

    /**
     * <p>
     * test method isPositive().
     * </p>
     */
    public void testIsPositive() {
        floatValidator = FloatValidator.isPositive();
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(18.97f));
        assertFalse(floatValidator.valid(-18.97f));
        assertNull(floatValidator.getMessage(18.97f));
        assertNotNull(floatValidator.getMessage(-18.97f));
    }

    /**
     * <p>
     * test method isPositive() with 1 parameters.
     * </p>
     */
    public void testIsPositive_Bundle() {
        key = "IsPositive_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.isPositive(this.bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(18.97f));
        assertFalse(floatValidator.valid(-18.97f));
        assertNull(floatValidator.getMessage(18.97f));
        assertNotNull(floatValidator.getMessage(-18.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(-18.97f));
    }

    /**
     * <p>
     * test method isNegative().
     * </p>
     */
    public void testIsNegative() {
        floatValidator = FloatValidator.isNegative();
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(-18.97f));
        assertFalse(floatValidator.valid(18.97f));
        assertNull(floatValidator.getMessage(-18.97f));
        assertNotNull(floatValidator.getMessage(18.97f));
    }

    /**
     * <p>
     * test method isNegative() with 1 parameters.
     * </p>
     */
    public void testIsNegative_Bundle() {
        key = "IsNegative_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.isNegative(this.bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(-18.97f));
        assertFalse(floatValidator.valid(18.97f));
        assertNull(floatValidator.getMessage(-18.97f));
        assertNotNull(floatValidator.getMessage(18.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(18.97f));
    }

    /**
     * <p>
     * test method isInteger().
     * </p>
     */
    public void testIsInteger() {
        floatValidator = FloatValidator.isInteger();
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(18));
        assertFalse(floatValidator.valid(18.97f));
        assertNull(floatValidator.getMessage(18));
        assertNotNull(floatValidator.getMessage(18.97f));
    }

    /**
     * <p>
     * test method isInteger() with 1 parameters.
     * </p>
     */
    public void testIsInteger_Bundle() {
        key = "IsInteger_Bundle_Key";
        bundleInfo.setMessageKey(key);
        floatValidator = FloatValidator.isInteger(this.bundleInfo);
        assertNotNull("The instance should not be null.", floatValidator);
        assertTrue(floatValidator.valid(18));
        assertFalse(floatValidator.valid(18.97f));
        assertNull(floatValidator.getMessage(18));
        assertNotNull(floatValidator.getMessage(18.97f));
        assertEquals("The message should be the same.", key, floatValidator.getMessage(18.97f));
    }
}
