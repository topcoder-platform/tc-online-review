/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.OrValidator;

import junit.framework.TestCase;
/**
 * Accuracy tests for the OrValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class OrValidatorAccuracyTests extends TestCase {
    /**
     * OrValidator instance to test.
     */
    private OrValidator orValidator;

    /**
     * <p>
     * setup().
     * </p>
     */
    protected void setUp() {
        orValidator = new OrValidator(IntegerValidator.greaterThan(8), IntegerValidator.lessThan(4));
    }
    /**
     * <p>
     * tear down the test evironment.
     * </p>
     */
    protected void tearDown(){
        orValidator=null;
    }

    /**
     * <p>
     * test the default constructor.
     * </p>
     */
    public void testCtor() {
        assertNotNull(orValidator);
    }

    /**
     * <p>
     * test method valid().
     * </p>
     */
    public void testValid() {
        assertTrue(orValidator.valid("1"));
        assertTrue(orValidator.valid("9"));
        assertFalse(orValidator.valid("5"));
        assertFalse(orValidator.valid("6"));
    }

    /**
     * <p>
     * test method getMessage().
     * </p>
     */
    public void testGetMessage() {
        assertNull(orValidator.getMessage("1"));
        assertNull(orValidator.getMessage("9"));
        assertTrue(orValidator.getMessage("5").indexOf("not") >= 0);
    }

    /**
     * <p>
     * test method getMessages().
     * </p>
     */
    public void testGetMessages() {
        assertNull(orValidator.getMessages("1"));
        assertNull(orValidator.getMessages("9"));
        assertNotNull(orValidator.getMessages("5"));
    }

    /**
     * <p>
     * test method getAllMessages().
     * </p>
     */
    public void testGetAllMessages() {
        assertNull(orValidator.getAllMessages("1"));
        assertNull(orValidator.getAllMessages("9"));
        assertNotNull(orValidator.getAllMessages("5"));
    }
}
