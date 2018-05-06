/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.validator.UploadRequestValidator;

import junit.framework.TestCase;


/**
 * Test <code>UploadRequestValidator</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureUploadRequestValidatorTest extends TestCase {
    /** The main <code>UploadRequestValidator</code> instance used to test. */
    private UploadRequestValidator validator;

    /**
     * Initialization for all tests here.
     */
    protected void setUp() {
        validator = new UploadRequestValidator(new MockFreeDiskSpaceChecker());
    }

    /**
     * Test <code>UploadRequestValidator(FreeDiskSpaceChecker)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if argument is null.
     */
    public void testUploadRequestValidatorForNullPointerException() {
        try {
            new UploadRequestValidator(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>valid(Object)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     */
    public void testValidForNullPointerException() {
        try {
            validator.valid(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getMessage(Object)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     */
    public void testGetMessageForNullPointerException() {
        try {
            validator.getMessage(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }
}
