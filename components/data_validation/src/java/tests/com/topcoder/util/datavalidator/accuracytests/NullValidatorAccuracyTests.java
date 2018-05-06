/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import java.util.Locale;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.NullValidator;

import junit.framework.TestCase;
/**
 * Accuracy tests for the NullValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class NullValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * NullValidator instance to test.
     * </p>
     */
    private NullValidator nullValidator;

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
        bundleInfo.setMessageKey("Null_Bundle_Key");
        nullValidator = new NullValidator(this.bundleInfo);
    }
    /**
     * <p>
     * tear down the test environment.
     * </p>
     */
    public void tearDown(){
        bundleInfo = null;
        nullValidator=null;
    }

    /**
     * <p>
     * test constructor1.
     * </p>
     */
    public void testCtor1() {
        assertNotNull(nullValidator);
    }

    /**
     * <p>
     * test constructor2.
     * </p>
     */
    public void testCtor2() {
        nullValidator = new NullValidator();
        assertNotNull(nullValidator);
    }

    /**
     * <p>
     * test method valid().
     * </p>
     */
    public void testValid() {
        assertTrue(nullValidator.valid(null));
        assertFalse(nullValidator.valid("7"));
    }

    /**
     * <p>
     * test method getMessage().
     * </p>
     */
    public void testGetMessage() {
        assertNull(nullValidator.getMessage(null));
        assertNotNull(nullValidator.getMessage("7"));
        assertEquals("The message should be the same.", "Null_Bundle_Key",nullValidator.getMessage("7"));
    }

    /**
     * <p>
     * test method getMessages().
     * </p>
     */
    public void testGetMessages_Accuracy() {
        assertNull(nullValidator.getMessages(null));
        assertNotNull(nullValidator.getMessages("7"));
    }

    /**
     * <p>
     * test method getAllMessages().
     * </p>
     */
    public void testGetAllMessages() {
        assertNull(nullValidator.getAllMessages(null));
        assertNotNull(nullValidator.getAllMessages("7"));
    }
}
