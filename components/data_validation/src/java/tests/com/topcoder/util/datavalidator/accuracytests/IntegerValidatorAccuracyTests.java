/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import java.util.Locale;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.IntegerValidator;

import junit.framework.TestCase;
/**
 * Accuracy tests for the IntegerValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class IntegerValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * IntegerValidator instance to test.
     * <p>
     */
    private IntegerValidator integerValidator;

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
        integerValidator = null;
    }

    /**
     * <p>
     * test method valid().
     * </p>
     */
    public void testValid() {
        integerValidator = IntegerValidator.equalTo(18);
        assertTrue(integerValidator.valid(new Float(18)));
        assertFalse(integerValidator.valid(new Float(19)));
        assertTrue(integerValidator.valid("18"));
        assertFalse(integerValidator.valid("19"));
        assertFalse(integerValidator.valid("18dot97"));
    }

    /**
     * <p>
     * test method greaterThan().
     * </p>
     */
    public void testGreaterThan() {
        integerValidator = IntegerValidator.greaterThan(18);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Float(19)));
        assertFalse(integerValidator.valid(new Float(17)));
        assertTrue(integerValidator.valid(19));
        assertFalse(integerValidator.valid(17));
        assertTrue(integerValidator.valid("19"));
        assertFalse(integerValidator.valid("17"));
        assertNull(integerValidator.getMessage(19));
        assertNotNull(integerValidator.getMessage(17));
    }

    /**
     * <p>
     * test method greaterThan() with 2 parameters.
     * </p>
     */
    public void testGreaterThan_Bundle() {
        key = "GreaterThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.greaterThan(18, bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(19)));
        assertFalse(integerValidator.valid(new Integer(17)));
        assertTrue(integerValidator.valid(19));
        assertFalse(integerValidator.valid(17));
        assertTrue(integerValidator.valid("19"));
        assertFalse(integerValidator.valid("17"));
        assertNull(integerValidator.getMessage(19));
        assertNotNull(integerValidator.getMessage(17));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(17));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo().
     * </p>
     */
    public void testGreaterThanOrEqualTo() {
        integerValidator = IntegerValidator.greaterThanOrEqualTo(18);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(18)));
        assertTrue(integerValidator.valid(new Integer(19)));
        assertFalse(integerValidator.valid(new Integer(17)));
        assertTrue(integerValidator.valid(18));
        assertTrue(integerValidator.valid(19));
        assertFalse(integerValidator.valid(17));
        assertTrue(integerValidator.valid("18"));
        assertTrue(integerValidator.valid("19"));
        assertFalse(integerValidator.valid("17"));
        assertNull(integerValidator.getMessage(18));
        assertNull(integerValidator.getMessage(19));
        assertNotNull(integerValidator.getMessage(17));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo() with 2 parameters.
     * </p>
     */
    public void testGreaterThanOrEqualTo_Bundle() {
        key = "GreaterThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.greaterThanOrEqualTo(18, this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(18)));
        assertTrue(integerValidator.valid(new Integer(19)));
        assertFalse(integerValidator.valid(new Integer(17)));
        assertTrue(integerValidator.valid(18));
        assertTrue(integerValidator.valid(19));
        assertFalse(integerValidator.valid(17));
        assertTrue(integerValidator.valid("18"));
        assertTrue(integerValidator.valid("19"));
        assertFalse(integerValidator.valid("17"));
        assertNull(integerValidator.getMessage(18));
        assertNull(integerValidator.getMessage(19));
        assertNotNull(integerValidator.getMessage(17));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(17));
    }

    /**
     * <p>
     * test method lessThan().
     * </p>
     */
    public void testLessThan() {
        integerValidator = IntegerValidator.lessThan(18);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(17)));
        assertFalse(integerValidator.valid(new Integer(19)));
        assertTrue(integerValidator.valid(17));
        assertFalse(integerValidator.valid(19));
        assertTrue(integerValidator.valid("17"));
        assertFalse(integerValidator.valid("19"));
        assertNull(integerValidator.getMessage(17));
        assertNotNull(integerValidator.getMessage(19));
    }

    /**
     * <p>
     * test method lessThan() with 2 parameters.
     * </p>
     */
    public void testLessThan_Bundle() {
        key = "LessThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.lessThan(18, this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(17)));
        assertFalse(integerValidator.valid(new Integer(19)));
        assertTrue(integerValidator.valid(17));
        assertFalse(integerValidator.valid(19));
        assertTrue(integerValidator.valid("17"));
        assertFalse(integerValidator.valid("19"));
        assertNull(integerValidator.getMessage(17));
        assertNotNull(integerValidator.getMessage(19));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(19));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo().
     * </p>
     */
    public void testLessThanOrEqualTo() {
        integerValidator = IntegerValidator.lessThanOrEqualTo(18);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(18)));
        assertTrue(integerValidator.valid(new Integer(17)));
        assertFalse(integerValidator.valid(new Integer(19)));
        assertTrue(integerValidator.valid(18));
        assertTrue(integerValidator.valid(17));
        assertFalse(integerValidator.valid(19));
        assertTrue(integerValidator.valid("18"));
        assertTrue(integerValidator.valid("17"));
        assertFalse(integerValidator.valid("19"));
        assertNull(integerValidator.getMessage(18));
        assertNull(integerValidator.getMessage(17));
        assertNotNull(integerValidator.getMessage(19));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo() with 2 parameters.
     * </p>
     */
    public void testLessThanOrEqualTo_Bundle() {
        key = "LessThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.lessThanOrEqualTo(18, this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(18)));
        assertTrue(integerValidator.valid(new Integer(17)));
        assertFalse(integerValidator.valid(new Integer(19)));
        assertTrue(integerValidator.valid(18));
        assertTrue(integerValidator.valid(17));
        assertFalse(integerValidator.valid(19));
        assertTrue(integerValidator.valid("18"));
        assertTrue(integerValidator.valid("17"));
        assertFalse(integerValidator.valid("19"));
        assertNull(integerValidator.getMessage(18));
        assertNull(integerValidator.getMessage(17));
        assertNotNull(integerValidator.getMessage(19));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(19));
    }

    /**
     * <p>
     * test method equalTo().
     * </p>
     */
    public void testEqualTo() {
        integerValidator = IntegerValidator.equalTo(18);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(18)));
        assertFalse(integerValidator.valid(new Integer(19)));
        assertTrue(integerValidator.valid(18));
        assertFalse(integerValidator.valid(19));
        assertTrue(integerValidator.valid("18"));
        assertFalse(integerValidator.valid("19"));
        assertNull(integerValidator.getMessage(18));
        assertNotNull(integerValidator.getMessage(19));
    }

    /**
     * <p>
     * test method equalTo() with 2 parameters.
     * </p>
     */
    public void testEqualTo_Bundle() {
        key = "LessThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.equalTo(18, this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(18)));
        assertFalse(integerValidator.valid(new Integer(19)));
        assertTrue(integerValidator.valid(18));
        assertFalse(integerValidator.valid(19));
        assertTrue(integerValidator.valid("18"));
        assertFalse(integerValidator.valid("19"));
        assertNull(integerValidator.getMessage(18));
        assertNotNull(integerValidator.getMessage(19));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(19));
    }

    /**
     * <p>
     * test method inRange().
     * </p>
     */
    public void testInRange() {
        integerValidator = IntegerValidator.inRange(18, 38);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(28)));
        assertTrue(integerValidator.valid(new Integer(18)));
        assertTrue(integerValidator.valid(new Integer(38)));
        assertFalse(integerValidator.valid(new Integer(17)));
        assertFalse(integerValidator.valid(new Integer(39)));
        assertTrue(integerValidator.valid(28));
        assertTrue(integerValidator.valid(18));
        assertTrue(integerValidator.valid(38));
        assertFalse(integerValidator.valid(17));
        assertFalse(integerValidator.valid(39));
        assertTrue(integerValidator.valid("28"));
        assertTrue(integerValidator.valid("18"));
        assertTrue(integerValidator.valid("38"));
        assertFalse(integerValidator.valid("17"));
        assertFalse(integerValidator.valid("39"));
        assertNull(integerValidator.getMessage(28));
        assertNull(integerValidator.getMessage(18));
        assertNull(integerValidator.getMessage(38));
        assertNotNull(integerValidator.getMessage(17));
        assertNotNull(integerValidator.getMessage(39));
    }

    /**
     * <p>
     * test method inRange() with 3 parameters.
     * </p>
     */
    public void testInRange_Bundle() {
        key = "InRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.inRange(18, 38, this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(18)));
        assertTrue(integerValidator.valid(new Integer(38)));
        assertFalse(integerValidator.valid(new Integer(17)));
        assertFalse(integerValidator.valid(new Integer(39)));
        assertTrue(integerValidator.valid(18));
        assertTrue(integerValidator.valid(38));
        assertFalse(integerValidator.valid(17));
        assertFalse(integerValidator.valid(39));
        assertTrue(integerValidator.valid("18"));
        assertTrue(integerValidator.valid("38"));
        assertFalse(integerValidator.valid("17"));
        assertFalse(integerValidator.valid("39"));
        assertNull(integerValidator.getMessage(18));
        assertNull(integerValidator.getMessage(38));
        assertNotNull(integerValidator.getMessage(17));
        assertNotNull(integerValidator.getMessage(39));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(17));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(39));
    }

    /**
     * <p>
     * test method inExclusiveRange().
     * </p>
     */
    public void testInExclusiveRange() {
        integerValidator = IntegerValidator.inExclusiveRange(18, 38);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(28)));
        assertFalse(integerValidator.valid(new Integer(18)));
        assertFalse(integerValidator.valid(new Integer(38)));
        assertFalse(integerValidator.valid(new Integer(17)));
        assertFalse(integerValidator.valid(new Integer(39)));
        assertTrue(integerValidator.valid(28));
        assertFalse(integerValidator.valid(18));
        assertFalse(integerValidator.valid(38));
        assertFalse(integerValidator.valid(17));
        assertFalse(integerValidator.valid(39));
        assertTrue(integerValidator.valid("28"));
        assertFalse(integerValidator.valid("18"));
        assertFalse(integerValidator.valid("38"));
        assertFalse(integerValidator.valid("17"));
        assertFalse(integerValidator.valid("39"));
        assertNull(integerValidator.getMessage(28));
        assertNotNull(integerValidator.getMessage(18));
        assertNotNull(integerValidator.getMessage(38));
        assertNotNull(integerValidator.getMessage(17));
        assertNotNull(integerValidator.getMessage(39));
    }

    /**
     * <p>
     * test method inExclusiveRange() with 3 parameters.
     * </p>
     */
    public void testInExclusiveRange_Bundle() {
        key = "InExclusiveRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.inExclusiveRange(18, 38, this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(new Integer(28)));
        assertFalse(integerValidator.valid(new Integer(18)));
        assertFalse(integerValidator.valid(new Integer(38)));
        assertFalse(integerValidator.valid(new Integer(17)));
        assertFalse(integerValidator.valid(new Integer(39)));
        assertTrue(integerValidator.valid(28));
        assertFalse(integerValidator.valid(18));
        assertFalse(integerValidator.valid(38));
        assertFalse(integerValidator.valid(17));
        assertFalse(integerValidator.valid(39));
        assertTrue(integerValidator.valid("28"));
        assertFalse(integerValidator.valid("18"));
        assertFalse(integerValidator.valid("38"));
        assertFalse(integerValidator.valid("17"));
        assertFalse(integerValidator.valid("39"));
        assertNull(integerValidator.getMessage(28));
        assertNotNull(integerValidator.getMessage(18));
        assertNotNull(integerValidator.getMessage(38));
        assertNotNull(integerValidator.getMessage(17));
        assertNotNull(integerValidator.getMessage(39));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(18));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(38));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(17));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(39));
    }

    /**
     * <p>
     * test method isPositive().
     * </p>
     */
    public void testIsPositive() {
        integerValidator = IntegerValidator.isPositive();
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(18));
        assertFalse(integerValidator.valid(-18));
        assertNull(integerValidator.getMessage(18));
        assertNotNull(integerValidator.getMessage(-18));
    }

    /**
     * <p>
     * test method isPositive() with 1 parameters.
     * </p>
     */
    public void testIsPositive_Bundle() {
        key = "IsPositive_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.isPositive(this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(18));
        assertFalse(integerValidator.valid(-18));
        assertNull(integerValidator.getMessage(18));
        assertNotNull(integerValidator.getMessage(-18));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(-18));
    }

    /**
     * <p>
     * test method isNegative().
     * </p>
     */
    public void testIsNegative() {
        integerValidator = IntegerValidator.isNegative();
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(-18));
        assertFalse(integerValidator.valid(18));
        assertNull(integerValidator.getMessage(-18));
        assertNotNull(integerValidator.getMessage(18));
    }

    /**
     * <p>
     * test method isNegative() with 1 parameters.
     * </p>
     */
    public void testIsNegative_Bundle() {
        key = "IsNegative_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.isNegative(this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(-18));
        assertFalse(integerValidator.valid(18));
        assertNull(integerValidator.getMessage(-18));
        assertNotNull(integerValidator.getMessage(18));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(18));
    }
    /**
     * <p>
     * test the method isOdd().
     * </p>
     */
    public void testIsOdd(){
        integerValidator = IntegerValidator.isOdd();
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(19));
        assertFalse(integerValidator.valid(18));
        assertNull(integerValidator.getMessage(19));
        assertNotNull(integerValidator.getMessage(18));
    }
    /**
     * <p>
     * test the method isOdd().
     * </p>
     */
    public void testIsOdd_Bundle(){
        key = "IsOdd_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.isOdd(this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(19));
        assertFalse(integerValidator.valid(18));
        assertNull(integerValidator.getMessage(19));
        assertNotNull(integerValidator.getMessage(18));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(18));
    }
    /**
     * <p>
     * accuracy test for the method isEven().
     * </p>
     */
    public void testIsEven(){
        integerValidator = IntegerValidator.isEven();
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(18));
        assertFalse(integerValidator.valid(19));
        assertNull(integerValidator.getMessage(18));
        assertNotNull(integerValidator.getMessage(19));
    }
    public void testIsEven_Bundle(){
        key = "IsEven_Bundle_Key";
        bundleInfo.setMessageKey(key);
        integerValidator = IntegerValidator.isEven(this.bundleInfo);
        assertNotNull("The instance should not be null.", integerValidator);
        assertTrue(integerValidator.valid(18));
        assertFalse(integerValidator.valid(19));
        assertNull(integerValidator.getMessage(18));
        assertNotNull(integerValidator.getMessage(19));
        assertEquals("The message should be the same.", key, integerValidator.getMessage(19));
    }
}
