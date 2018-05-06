/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.failuretests;

import java.util.ResourceBundle;

import com.topcoder.util.errorhandling.ExceptionUtils;

import junit.framework.TestCase;

/**
 * <p>
 * Failure test for <code>{@link ExceptionUtils}</code> class.
 * </p>
 * @author FireIce
 * @version 2.0
 */
public class ExceptionUtilsFailureTests extends TestCase {

    /**
     * <p>
     * Represents the resource bundle for failure test.
     * </p>
     */
    private ResourceBundle resourceBundle;

    /**
     * <p>
     * Setup the testing environment.
     * </p>
     */
    protected void setUp() throws Exception {
        super.setUp();

        resourceBundle = ResourceBundle.getBundle("failuretests.resources");
    }

    /**
     * <p>
     * Failure test for <code>{@link ExceptionUtils#checkNull(Object, ResourceBundle, String, String)}</code> method.
     * </p>
     */
    public void testCheckNullFailure() {
        try {
            ExceptionUtils.checkNull(null, resourceBundle, "NullObject", "the object is null.");
            fail("should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("incorrect exception message.", "null object.", e.getMessage());
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ExceptionUtils#checkNullOrEmpty(String, ResourceBundle, String, String)}</code>
     * method.
     * </p>
     */
    public void testCheckNullOrEmptyFailureNull() {
        try {
            ExceptionUtils.checkNullOrEmpty(null, resourceBundle, "NullString", "the string is null.");
            fail("should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("incorrect exception message.", "null string.", e.getMessage());
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ExceptionUtils#checkNullOrEmpty(String, ResourceBundle, String, String)}</code>
     * method.
     * </p>
     */
    public void testCheckNullOrEmptyFailureEmpty() {
        try {
            ExceptionUtils.checkNullOrEmpty("", resourceBundle, "EmptyString", "the string is empty.");
            fail("should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("incorrect exception message.", "empty string.", e.getMessage());
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ExceptionUtils#checkNullOrEmpty(String, ResourceBundle, String, String)}</code>
     * method.
     * </p>
     */
    public void testCheckNullOrEmptyFailureTrimmedEmpty() {
        try {
            ExceptionUtils.checkNullOrEmpty(" ", resourceBundle, "TrimmedEmptyString", "the string is trimmed empty.");
            fail("should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("incorrect exception message.", "trimmed empty string.", e.getMessage());
        }
    }
}
