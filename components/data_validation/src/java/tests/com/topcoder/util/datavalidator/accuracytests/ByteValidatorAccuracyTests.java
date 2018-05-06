/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.ByteValidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * Accuracy tests for the ByteValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class ByteValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * BundleInfo case used to test.
     * </p>
     */
    private BundleInfo bundleInfo;

    /**
     * <p>
     * The ByteValidator instance for testing.
     * </p>
     */
    private ByteValidator byteValidator;

    /**
     * <p>
     * set up the test environment.
     * </p>
     */
    protected void setUp() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("accuracy.test", new Locale("en"));
        bundleInfo.setDefaultMessage("accuracy test");

        //bundleInfo.setMessageKey("key");
    }

    /**
     * <p>
     * tear down the test environment.
     * </p>
     */
    protected void tearDown() {
        bundleInfo = null;
        byteValidator = null;
    }

    /**
     * <p>
     * Accuracy test for valid().
     * use the object type parameter.
     * </p>
     */
    public void testValid() {
        byteValidator = ByteValidator.greaterThan("a".getBytes()[0]);
        assertFalse(byteValidator.valid(new Object()));
        assertFalse(byteValidator.valid("not number"));
        assertFalse(byteValidator.valid(byteValidator));
        assertFalse(byteValidator.valid(ByteValidator.class));
    }

    /**
     * <p>
     * test method greaterThan().
     * </p>
     *
     */
    public void testGreaterThan() {
        byteValidator = ByteValidator.greaterThan("a".getBytes()[0]);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("b".getBytes()[0]));
        assertFalse(byteValidator.valid("0".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("0".getBytes()[0])));
        // the Byte value for "b"
        assertTrue(byteValidator.valid("98"));
        assertFalse(byteValidator.valid("0"));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo().
     * </p>
     *
     */
    public void testGreaterThanOrEqualTo() {
        byteValidator = ByteValidator.greaterThanOrEqualTo("a".getBytes()[0]);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("a".getBytes()[0]));
        assertTrue(byteValidator.valid("b".getBytes()[0]));
        assertFalse(byteValidator.valid("0".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertTrue(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("0".getBytes()[0])));
        assertTrue(byteValidator.valid("97"));
        assertTrue(byteValidator.valid("98"));
        assertFalse(byteValidator.valid("0"));
    }

    /**
     * <p>
     * test method lessThan().
     * </p>
     *
     */
    public void testLessThan() {
        byteValidator = ByteValidator.lessThan("b".getBytes()[0]);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("a".getBytes()[0]));
        assertFalse(byteValidator.valid("c".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("c".getBytes()[0])));
        assertTrue(byteValidator.valid("97"));
        assertFalse(byteValidator.valid("99"));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo().
     * </p>
     *
     */
    public void testLessThanOrEqualTo() {
        byteValidator = ByteValidator.lessThanOrEqualTo("b".getBytes()[0]);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("a".getBytes()[0]));
        assertTrue(byteValidator.valid("b".getBytes()[0]));
        assertFalse(byteValidator.valid("c".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertTrue(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("c".getBytes()[0])));
        assertTrue(byteValidator.valid("97"));
        assertTrue(byteValidator.valid("98"));
        assertFalse(byteValidator.valid("99"));
    }

    /**
     * <p>
     * test method equal().
     * </p>
     *
     */
    public void testEqual() {
        byteValidator = ByteValidator.equal("a".getBytes()[0]);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("a".getBytes()[0]));
        assertFalse(byteValidator.valid("b".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertTrue(byteValidator.valid("97"));
        assertFalse(byteValidator.valid("98"));
    }

    /**
     * <p>
     * test method inRange().
     * </p>
     *
     */
    public void testInRange() {
        byteValidator = ByteValidator.inRange("b".getBytes()[0], "d".getBytes()[0]);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("c".getBytes()[0]));
        assertTrue(byteValidator.valid("b".getBytes()[0]));
        assertTrue(byteValidator.valid("d".getBytes()[0]));
        assertFalse(byteValidator.valid("a".getBytes()[0]));
        assertFalse(byteValidator.valid("e".getBytes()[0]));

        assertTrue(byteValidator.valid(new Byte("c".getBytes()[0])));
        assertTrue(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertTrue(byteValidator.valid(new Byte("d".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("e".getBytes()[0])));

        assertTrue(byteValidator.valid("99"));
        assertTrue(byteValidator.valid("98"));
        assertTrue(byteValidator.valid("100"));
        assertFalse(byteValidator.valid("97"));
        assertFalse(byteValidator.valid("101"));
    }

    /**
     * <p>
     * test method inExclusiveRange().
     * </p>
     *
     */
    public void testInExclusiveRange() {
        byteValidator = ByteValidator.inExclusiveRange("b".getBytes()[0], "d".getBytes()[0]);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("c".getBytes()[0]));
        assertFalse(byteValidator.valid("b".getBytes()[0]));
        assertFalse(byteValidator.valid("d".getBytes()[0]));
        assertFalse(byteValidator.valid("a".getBytes()[0]));
        assertFalse(byteValidator.valid("e".getBytes()[0]));

        assertTrue(byteValidator.valid(new Byte("c".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("d".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("e".getBytes()[0])));

        assertTrue(byteValidator.valid("99"));
        assertFalse(byteValidator.valid("98"));
        assertFalse(byteValidator.valid("100"));
        assertFalse(byteValidator.valid("97"));
        assertFalse(byteValidator.valid("101"));
    }

    /**
     * <p>
     * test method greaterThan() with bundle.
     * </p>
     *
     */
    public void testGreaterThan_Bundle() {
        bundleInfo.setMessageKey("GreaterThan_Bundle_Key");
        byteValidator = ByteValidator.greaterThan("a".getBytes()[0], this.bundleInfo);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("b".getBytes()[0]));
        assertFalse(byteValidator.valid("0".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("0".getBytes()[0])));
        // the Byte value for "b"
        assertTrue(byteValidator.valid("98"));
        assertFalse(byteValidator.valid("0"));
        assertNull(byteValidator.getMessage("98"));
        assertEquals("The message should be equal.", "GreaterThan_Bundle_Key",
            byteValidator.getMessage("0"));
    }

    /**
     * <p>
     * test method greaterThanOrEqualTo() with bundle.
     * </p>
     *
     */
    public void testGreaterThanOrEqualTo_Bundle() {
        String key = "GreaterThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        byteValidator = ByteValidator.greaterThanOrEqualTo("a".getBytes()[0], this.bundleInfo);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("a".getBytes()[0]));
        assertTrue(byteValidator.valid("b".getBytes()[0]));
        assertFalse(byteValidator.valid("0".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertTrue(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("0".getBytes()[0])));
        assertTrue(byteValidator.valid("97"));
        assertTrue(byteValidator.valid("98"));
        assertFalse(byteValidator.valid("0"));
        assertNull(byteValidator.getMessage("97"));
        assertNull(byteValidator.getMessage("98"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("0"));
    }

    /**
     * <p>
     * test method lessThan() with bundle.
     * </p>
     *
     */
    public void testLessThan_Bundle() {
        String key = "LessThan_Bundle_Key";
        bundleInfo.setMessageKey(key);
        byteValidator = ByteValidator.lessThan("b".getBytes()[0], this.bundleInfo);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("a".getBytes()[0]));
        assertFalse(byteValidator.valid("c".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("c".getBytes()[0])));
        assertTrue(byteValidator.valid("97"));
        assertFalse(byteValidator.valid("99"));
        assertNull(byteValidator.getMessage("97"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("99"));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo() with bundle.
     * </p>
     *
     */
    public void testLessThanOrEqualTo_Bundle() {
        String key = "LessThanOrEqualTo_Bundle_Key";
        bundleInfo.setMessageKey(key);
        byteValidator = ByteValidator.lessThanOrEqualTo("b".getBytes()[0], this.bundleInfo);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("a".getBytes()[0]));
        assertTrue(byteValidator.valid("b".getBytes()[0]));
        assertFalse(byteValidator.valid("c".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertTrue(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("c".getBytes()[0])));
        assertTrue(byteValidator.valid("97"));
        assertTrue(byteValidator.valid("98"));
        assertFalse(byteValidator.valid("99"));
        assertNull(byteValidator.getMessage("97"));
        assertNull(byteValidator.getMessage("98"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("99"));
    }

    /**
     * <p>
     * test method lessThanOrEqualTo() with bundle.
     * </p>
     *
     */
    public void testEqual_Bundle() {
        String key = "Equal_Bundle_Key";
        bundleInfo.setMessageKey(key);
        byteValidator = ByteValidator.equal("a".getBytes()[0], this.bundleInfo);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("a".getBytes()[0]));
        assertFalse(byteValidator.valid("b".getBytes()[0]));
        assertTrue(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertTrue(byteValidator.valid("97"));
        assertFalse(byteValidator.valid("98"));
        assertNull(byteValidator.getMessage("97"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("98"));
    }

    /**
     * <p>
     * test method inRange() with bundle.
     * </p>
     *
     */
    public void testInRange_Bundle() {
        String key = "InRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        byteValidator = ByteValidator.inRange("b".getBytes()[0], "d".getBytes()[0], this.bundleInfo);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("c".getBytes()[0]));
        assertTrue(byteValidator.valid("b".getBytes()[0]));
        assertTrue(byteValidator.valid("d".getBytes()[0]));
        assertFalse(byteValidator.valid("a".getBytes()[0]));
        assertFalse(byteValidator.valid("e".getBytes()[0]));

        assertTrue(byteValidator.valid(new Byte("c".getBytes()[0])));
        assertTrue(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertTrue(byteValidator.valid(new Byte("d".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("e".getBytes()[0])));

        assertTrue(byteValidator.valid("99"));
        assertTrue(byteValidator.valid("98"));
        assertTrue(byteValidator.valid("100"));
        assertFalse(byteValidator.valid("97"));
        assertFalse(byteValidator.valid("101"));

        assertNull(byteValidator.getMessage("99"));
        assertNull(byteValidator.getMessage("98"));
        assertNull(byteValidator.getMessage("100"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("97"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("101"));
    }

    /**
     * <p>
     * test method inExclusiveRange() with bundle.
     * </p>
     *
     */
    public void testinExclusiveRange_Bundle() {
        String key = "InRange_Bundle_Key";
        bundleInfo.setMessageKey(key);
        byteValidator = ByteValidator.inExclusiveRange("b".getBytes()[0], "d".getBytes()[0],
                this.bundleInfo);
        assertNotNull("The instance should not be null.", byteValidator);
        assertTrue(byteValidator.valid("c".getBytes()[0]));
        assertFalse(byteValidator.valid("b".getBytes()[0]));
        assertFalse(byteValidator.valid("d".getBytes()[0]));
        assertFalse(byteValidator.valid("a".getBytes()[0]));
        assertFalse(byteValidator.valid("e".getBytes()[0]));

        assertTrue(byteValidator.valid(new Byte("c".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("b".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("d".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("a".getBytes()[0])));
        assertFalse(byteValidator.valid(new Byte("e".getBytes()[0])));

        assertTrue(byteValidator.valid("99"));
        assertFalse(byteValidator.valid("98"));
        assertFalse(byteValidator.valid("100"));
        assertFalse(byteValidator.valid("97"));
        assertFalse(byteValidator.valid("101"));

        assertNull(byteValidator.getMessage("99"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("98"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("100"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("97"));
        assertEquals("The message should be equal.", key, byteValidator.getMessage("101"));
    }
}
