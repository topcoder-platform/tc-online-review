/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import java.util.Locale;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.StringValidator;

import junit.framework.TestCase;
/**
 * <p>
 * Accuracy tests for StringValidator.
 * <p>
 * @author KLW
 * @version 1.1
 *
 */
public class StringValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * The StringValidator instance for test.
     * </p>
     */
    private StringValidator stringValidator;

    /**
     * <p>
     * The BundleInfo instance for testing.
     * </p>
     */
    private BundleInfo bundleInfo;
    /**
     * <p>
     * the message key for bundle info.
     * </p>
     */
    private String key;

    /**
     * <p>
     * set up the environment.
     * </p>
     */
    protected void setUp() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("accuracy.test", Locale.ENGLISH);
        bundleInfo.setDefaultMessage("accuracy test");
    }

    /**
     * <p>
     * tear down the test environment.
     * </p>
     */
    protected void tearDown() {
        bundleInfo = null;
        key=null;
        stringValidator = null;
    }

    /**
     * <p>
     * Test the method validObject() with a String object for accuracy test.
     * <p>
     */
    public void testValid() {
        stringValidator = StringValidator.startsWith("sss");
        Object str = new String("test");
        assertFalse(stringValidator.valid(str));
        assertFalse(stringValidator.valid(new Object()));
    }
    /**
     * <p>
     * Test the method startsWith with a valid string.
     * So it will return an StringValidator stringValidator.
     * </p>
     * <p>
     * Test for accuracy.
     * </p>
     */
    public void testStartsWith() {
        stringValidator= StringValidator.startsWith("test");
        assertTrue(stringValidator.valid("test true"));
        assertFalse(stringValidator.valid("wrong test"));
        assertNull(stringValidator.getMessage("test true"));
        assertNotNull(stringValidator.getMessage("wrong test"));
        
    }

    /**
     * <p>
     * Test the method startsWith with two valid parameters for accuracy.
     * </p>
     *
     */
    public void testStartsWith_Bundle() {
        key = "StartsWith_Bundle_Key";
        bundleInfo.setMessageKey(key);
        stringValidator = StringValidator.startsWith("test", bundleInfo);
        assertTrue(stringValidator.valid("test true"));
        assertFalse(stringValidator.valid("false test"));
        assertNull(stringValidator.getMessage("test true"));
        assertNotNull(stringValidator.getMessage("wrong test"));
        assertEquals("The message should be equal.",key,stringValidator.getMessage("wrong test"));
    }

    /**
     * <p>
     * test the method matchesRegexp for accuracy.
     * </p>
     *
     */
    public void testMatchesRegexp() {
        stringValidator= StringValidator.matchesRegexp("java");
        assertTrue(stringValidator.valid("java"));
        assertFalse(stringValidator.valid("cava"));
        assertNull(stringValidator.getMessage("java"));
        assertNotNull(stringValidator.getMessage("cava"));
    }

    /**
     * <p>
     * test the method containsSubstring for accuracy.
     * </p>
     */
    public void testContainsSubstring() {
        stringValidator= StringValidator.containsSubstring("java");
        assertTrue(stringValidator.valid("i love java."));
        assertFalse(stringValidator.valid("i love cava"));
        assertNull(stringValidator.getMessage("i love java."));
        assertNotNull(stringValidator.getMessage("i love cava"));
    }

    /**
     * <p>
     * test the method hasLength for accuracy, with a valid parameter.
     * <p>
     */
    public void testHasLength() {
        IntegerValidator validator = IntegerValidator.equalTo(4);
        stringValidator= StringValidator.hasLength(validator);
        assertTrue(stringValidator.valid("java"));
        assertFalse(stringValidator.valid("i love java."));
    }




}
