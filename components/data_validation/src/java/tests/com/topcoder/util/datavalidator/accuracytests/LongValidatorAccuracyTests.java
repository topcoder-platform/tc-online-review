/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.LongValidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * Accuracy tests for the LongValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class LongValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * LongValidator instance to test.
     * <p>
     */
    private LongValidator longValidator;

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
        longValidator = null;
    }

    /**
     * <p>
     * test method valid().
     * </p>
     */
    public void testValid() {
        longValidator = LongValidator.greaterThan(18L);
        assertTrue(longValidator.valid(new Long(19L)));
        assertFalse(longValidator.valid(new Long(17L)));
        assertTrue(longValidator.valid("19"));
        assertFalse(longValidator.valid("18"));
        assertFalse(longValidator.valid("18Ldot9L7"));
    }

    /**
     * <p>
     * test method greaterThan().
     * </p>
     */
    public void testGreaterThan() {
        longValidator = LongValidator.greaterThan(18L);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(19L)));
        assertFalse(longValidator.valid(new Long(17)));
        assertTrue(longValidator.valid(19L));
        assertFalse(longValidator.valid(17));
        assertTrue(longValidator.valid("19"));
        assertFalse(longValidator.valid("17"));
        assertNull(longValidator.getMessage(19L));
        assertNotNull(longValidator.getMessage(17));
    }

    /**
     * <p>
     * test method greaterThan() with 2 parameters.
     * </p>
     */
    public void testGreaterThan_Bundle() {
        key = "GreaterThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.greaterThan(18L, bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(19L)));
        assertFalse(longValidator.valid(new Long(17)));
        assertTrue(longValidator.valid(19L));
        assertFalse(longValidator.valid(17));
        assertTrue(longValidator.valid("19"));
        assertFalse(longValidator.valid("17"));
        assertNull(longValidator.getMessage(19L));
        assertNotNull(longValidator.getMessage(17));
        assertEquals("The message should be the same.", key, longValidator.getMessage(17));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo().
     * </p>
     */
    public void testGreaterThanOrEqualTo() {
        longValidator = LongValidator.greaterThanOrEqualTo(18L);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(18L)));
        assertTrue(longValidator.valid(new Long(19L)));
        assertFalse(longValidator.valid(new Long(17)));
        assertTrue(longValidator.valid(18L));
        assertTrue(longValidator.valid(19L));
        assertFalse(longValidator.valid(17));
        assertTrue(longValidator.valid("18"));
        assertTrue(longValidator.valid("19"));
        assertFalse(longValidator.valid("17"));
        assertNull(longValidator.getMessage(18L));
        assertNull(longValidator.getMessage(19L));
        assertNotNull(longValidator.getMessage(17));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo() with 2 parameters.
     * </p>
     */
    public void testGreaterThanOrEqualTo_Bundle() {
        key = "GreaterThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.greaterThanOrEqualTo(18L, this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(18L)));
        assertTrue(longValidator.valid(new Long(19L)));
        assertFalse(longValidator.valid(new Long(17)));
        assertTrue(longValidator.valid(18L));
        assertTrue(longValidator.valid(19L));
        assertFalse(longValidator.valid(17));
        assertTrue(longValidator.valid("18"));
        assertTrue(longValidator.valid("19"));
        assertFalse(longValidator.valid("17"));
        assertNull(longValidator.getMessage(18));
        assertNull(longValidator.getMessage(19));
        assertNotNull(longValidator.getMessage(17));
        assertEquals("The message should be the same.", key, longValidator.getMessage(17));
    }

    /**
     * <p>
     * test method lessThan().
     * </p>
     */
    public void testLessThan() {
        longValidator = LongValidator.lessThan(18);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(17)));
        assertFalse(longValidator.valid(new Long(19)));
        assertTrue(longValidator.valid(17));
        assertFalse(longValidator.valid(19));
        assertTrue(longValidator.valid("17"));
        assertFalse(longValidator.valid("19"));
        assertNull(longValidator.getMessage(17));
        assertNotNull(longValidator.getMessage(19));
    }

    /**
     * <p>
     * test method lessThan() with 2 parameters.
     * </p>
     */
    public void testLessThan_Bundle() {
        key = "LessThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.lessThan(18, this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(17)));
        assertFalse(longValidator.valid(new Long(19)));
        assertTrue(longValidator.valid(17));
        assertFalse(longValidator.valid(19));
        assertTrue(longValidator.valid("17"));
        assertFalse(longValidator.valid("19"));
        assertNull(longValidator.getMessage(17));
        assertNotNull(longValidator.getMessage(19));
        assertEquals("The message should be the same.", key, longValidator.getMessage(19));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo().
     * </p>
     */
    public void testLessThanOrEqualTo() {
        longValidator = LongValidator.lessThanOrEqualTo(18);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(18)));
        assertTrue(longValidator.valid(new Long(17)));
        assertFalse(longValidator.valid(new Long(19)));
        assertTrue(longValidator.valid(18));
        assertTrue(longValidator.valid(17));
        assertFalse(longValidator.valid(19));
        assertTrue(longValidator.valid("18"));
        assertTrue(longValidator.valid("17"));
        assertFalse(longValidator.valid("19"));
        assertNull(longValidator.getMessage(18));
        assertNull(longValidator.getMessage(17));
        assertNotNull(longValidator.getMessage(19));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo() with 2 parameters.
     * </p>
     */
    public void testLessThanOrEqualTo_Bundle() {
        key = "LessThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.lessThanOrEqualTo(18, this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(18)));
        assertTrue(longValidator.valid(new Long(17)));
        assertFalse(longValidator.valid(new Long(19)));
        assertTrue(longValidator.valid(18));
        assertTrue(longValidator.valid(17));
        assertFalse(longValidator.valid(19));
        assertTrue(longValidator.valid("18"));
        assertTrue(longValidator.valid("17"));
        assertFalse(longValidator.valid("19"));
        assertNull(longValidator.getMessage(18));
        assertNull(longValidator.getMessage(17));
        assertNotNull(longValidator.getMessage(19));
        assertEquals("The message should be the same.", key, longValidator.getMessage(19));
    }

    /**
     * <p>
     * test method inRange().
     * </p>
     */
    public void testInRange() {
        longValidator = LongValidator.inRange(18, 38);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(28)));
        assertTrue(longValidator.valid(new Long(18)));
        assertTrue(longValidator.valid(new Long(38)));
        assertFalse(longValidator.valid(new Long(17)));
        assertFalse(longValidator.valid(new Long(39)));
        assertTrue(longValidator.valid(28));
        assertTrue(longValidator.valid(18));
        assertTrue(longValidator.valid(38));
        assertFalse(longValidator.valid(17));
        assertFalse(longValidator.valid(39));
        assertTrue(longValidator.valid("28"));
        assertTrue(longValidator.valid("18"));
        assertTrue(longValidator.valid("38"));
        assertFalse(longValidator.valid("17"));
        assertFalse(longValidator.valid("39"));
        assertNull(longValidator.getMessage(28));
        assertNull(longValidator.getMessage(18));
        assertNull(longValidator.getMessage(38));
        assertNotNull(longValidator.getMessage(17));
        assertNotNull(longValidator.getMessage(39));
    }

    /**
     * <p>
     * test method inRange() with 3 parameters.
     * </p>
     */
    public void testInRange_Bundle() {
        key = "InRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.inRange(18, 38, this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(18)));
        assertTrue(longValidator.valid(new Long(38)));
        assertFalse(longValidator.valid(new Long(17)));
        assertFalse(longValidator.valid(new Long(39)));
        assertTrue(longValidator.valid(18));
        assertTrue(longValidator.valid(38));
        assertFalse(longValidator.valid(17));
        assertFalse(longValidator.valid(39));
        assertTrue(longValidator.valid("18"));
        assertTrue(longValidator.valid("38"));
        assertFalse(longValidator.valid("17"));
        assertFalse(longValidator.valid("39"));
        assertNull(longValidator.getMessage(18));
        assertNull(longValidator.getMessage(38));
        assertNotNull(longValidator.getMessage(17));
        assertNotNull(longValidator.getMessage(39));
        assertEquals("The message should be the same.", key, longValidator.getMessage(17));
        assertEquals("The message should be the same.", key, longValidator.getMessage(39));
    }

    /**
     * <p>
     * test method inExclusiveRange().
     * </p>
     */
    public void testInExclusiveRange() {
        longValidator = LongValidator.inExclusiveRange(18, 38);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(28)));
        assertFalse(longValidator.valid(new Long(18)));
        assertFalse(longValidator.valid(new Long(38)));
        assertFalse(longValidator.valid(new Long(17)));
        assertFalse(longValidator.valid(new Long(39)));
        assertTrue(longValidator.valid(28));
        assertFalse(longValidator.valid(18));
        assertFalse(longValidator.valid(38));
        assertFalse(longValidator.valid(17));
        assertFalse(longValidator.valid(39));
        assertTrue(longValidator.valid("28"));
        assertFalse(longValidator.valid("18"));
        assertFalse(longValidator.valid("38"));
        assertFalse(longValidator.valid("17"));
        assertFalse(longValidator.valid("39"));
        assertNull(longValidator.getMessage(28));
        assertNotNull(longValidator.getMessage(18));
        assertNotNull(longValidator.getMessage(38));
        assertNotNull(longValidator.getMessage(17));
        assertNotNull(longValidator.getMessage(39));
    }

    /**
     * <p>
     * test method inExclusiveRange() with 3 parameters.
     * </p>
     */
    public void testInExclusiveRange_Bundle() {
        key = "InExclusiveRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.inExclusiveRange(18, 38, this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(new Long(28)));
        assertFalse(longValidator.valid(new Long(18)));
        assertFalse(longValidator.valid(new Long(38)));
        assertFalse(longValidator.valid(new Long(17)));
        assertFalse(longValidator.valid(new Long(39)));
        assertTrue(longValidator.valid(28));
        assertFalse(longValidator.valid(18));
        assertFalse(longValidator.valid(38));
        assertFalse(longValidator.valid(17));
        assertFalse(longValidator.valid(39));
        assertTrue(longValidator.valid("28"));
        assertFalse(longValidator.valid("18"));
        assertFalse(longValidator.valid("38"));
        assertFalse(longValidator.valid("17"));
        assertFalse(longValidator.valid("39"));
        assertNull(longValidator.getMessage(28));
        assertNotNull(longValidator.getMessage(18));
        assertNotNull(longValidator.getMessage(38));
        assertNotNull(longValidator.getMessage(17));
        assertNotNull(longValidator.getMessage(39));
        assertEquals("The message should be the same.", key, longValidator.getMessage(18));
        assertEquals("The message should be the same.", key, longValidator.getMessage(38));
        assertEquals("The message should be the same.", key, longValidator.getMessage(17));
        assertEquals("The message should be the same.", key, longValidator.getMessage(39));
    }

    /**
     * <p>
     * test method isPositive().
     * </p>
     */
    public void testIsPositive() {
        longValidator = LongValidator.isPositive();
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(18));
        assertFalse(longValidator.valid(-18));
        assertNull(longValidator.getMessage(18));
        assertNotNull(longValidator.getMessage(-18));
    }

    /**
     * <p>
     * test method isPositive() with 1 parameters.
     * </p>
     */
    public void testIsPositive_Bundle() {
        key = "IsPositive_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.isPositive(this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(18));
        assertFalse(longValidator.valid(-18));
        assertNull(longValidator.getMessage(18));
        assertNotNull(longValidator.getMessage(-18));
        assertEquals("The message should be the same.", key, longValidator.getMessage(-18));
    }

    /**
     * <p>
     * test method isNegative().
     * </p>
     */
    public void testIsNegative() {
        longValidator = LongValidator.isNegative();
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(-18));
        assertFalse(longValidator.valid(18));
        assertNull(longValidator.getMessage(-18));
        assertNotNull(longValidator.getMessage(18));
    }

    /**
     * <p>
     * test method isNegative() with 1 parameters.
     * </p>
     */
    public void testIsNegative_Bundle() {
        key = "IsNegative_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.isNegative(this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(-18));
        assertFalse(longValidator.valid(18));
        assertNull(longValidator.getMessage(-18));
        assertNotNull(longValidator.getMessage(18));
        assertEquals("The message should be the same.", key, longValidator.getMessage(18));
    }

    /**
     * <p>
     * test the method isOdd().
     * </p>
     */
    public void testIsOdd() {
        longValidator = LongValidator.isOdd();
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(19));
        assertFalse(longValidator.valid(18));
        assertNull(longValidator.getMessage(19));
        assertNotNull(longValidator.getMessage(18));
    }

    /**
     * <p>
     * test the method isOdd().
     * </p>
     */
    public void testIsOdd_Bundle() {
        key = "IsOdd_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.isOdd(this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(19));
        assertFalse(longValidator.valid(18));
        assertNull(longValidator.getMessage(19));
        assertNotNull(longValidator.getMessage(18));
        assertEquals("The message should be the same.", key, longValidator.getMessage(18));
    }

    /**
     * <p>
     * accuracy test for the method isEven().
     * </p>
     */
    public void testIsEven() {
        longValidator = LongValidator.isEven();
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(18));
        assertFalse(longValidator.valid(19));
        assertNull(longValidator.getMessage(18));
        assertNotNull(longValidator.getMessage(19));
    }

    /**
     * DOCUMENT ME!
     */
    public void testIsEven_Bundle() {
        key = "IsEven_Bundle_Key";
        bundleInfo.setMessageKey(key);
        longValidator = LongValidator.isEven(this.bundleInfo);
        assertNotNull("The instance should not be null.", longValidator);
        assertTrue(longValidator.valid(18));
        assertFalse(longValidator.valid(19));
        assertNull(longValidator.getMessage(18));
        assertNotNull(longValidator.getMessage(19));
        assertEquals("The message should be the same.", key, longValidator.getMessage(19));
    }
}
