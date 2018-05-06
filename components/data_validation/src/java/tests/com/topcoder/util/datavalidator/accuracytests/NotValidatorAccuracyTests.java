/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.NotValidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * Accuracy tests for the NotValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class NotValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * NotValidator instance to test.
     * </p>
     */
    private NotValidator notValidator;

    /**
     * <p>
     * IntegerValidator notValidator used to test.
     * </p>
     */
    private IntegerValidator validator;

    /**
     * <p>
     * BundleInfo case used to test.
     * </p>
     */
    private BundleInfo bundleInfo;

    /**
     * <p>
     * setup().
     * </p>
     */
    public void setUp() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("accuracy.test", new Locale("en"));
        bundleInfo.setDefaultMessage("accuracy test");
        bundleInfo.setMessageKey("IsOdd_Bundle_Key");
        validator = IntegerValidator.isOdd();
        notValidator = new NotValidator(validator);
    }

    /**
     * <p>
     * tear down the environment.
     * </p>
     */
    public void tearDown() {
        notValidator=null;
        validator = null;
        bundleInfo = null;
    }

    /**
     * <p>
     * test constructor1.
     * </p>
     */
    public void testCtor1() {
        assertNotNull(notValidator);
    }

    /**
     * <p>
     * test constructor2.
     * </p>
     */
    public void testCtor2() {
        notValidator = new NotValidator(validator, bundleInfo);
        assertNotNull(notValidator);
    }

    /**
     * <p>
     * test method valid().
     * </p>
     */
    public void testValid() {
        assertTrue(notValidator.valid("8"));
        assertFalse(notValidator.valid("7"));
    }

    /**
     * <p>
     * test method getMessage().
     * </p>
     */
    public void testGetMessage() {
        assertNull(notValidator.getMessage("8"));
        assertNotNull(notValidator.getMessage("7"));
    }

    /**
     * <p>
     * test method getMessages().
     * </p>
     */
    public void testGetMessages() {
        assertNull(notValidator.getMessages("8"));
        assertNotNull(notValidator.getMessages("7"));
    }

    /**
     * <p>
     * test method getAllMessages().
     * </p>
     */
    public void testGetAllMessages() {
        assertNull(notValidator.getAllMessages("8"));
        assertNotNull(notValidator.getAllMessages("7"));
    }
}
