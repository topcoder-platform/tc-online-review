/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test case for <code>Helper</code>.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class HelperUnitTests extends TestCase {
    /**
     * <p>
     * Test the method <code>checkNotNull(Object argument, String name)</code> with null Argument.
     * <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testCheckNotNullWithNullArgument() {
        try {
            Helper.checkNotNull(null, "test");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Test the method <code>checkNotNull(Object argument, String name)</code> with non-null
     * Argument.
     * </p>
     */
    public void testCheckNotNull() {
        try {
            Helper.checkNotNull(new Object(), "test");
            Helper.checkNotNull(new Integer(1), "1");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown when object is not null.");
        }
    }

    /**
     * <p>
     * Test the method <code>checkNotNullOrEmpty(String argument, String name)</code> with null
     * Argument. <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testCheckNotNullOrEmptyWithNullArgument() {
        try {
            Helper.checkNotNullOrEmpty(null, "test");
            fail("IllegalArgumentException should be thrown when argument is null");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Test the method <code>checkNotNullOrEmpty(String argument, String name)</code> with empty
     * Argument. <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testCheckNotNullOrEmptyWithEmptyArgument() {
        try {
            Helper.checkNotNullOrEmpty("\r \t \n", "test");
            fail("IllegalArgumentException should be thrown when argument is empty");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Test the method <code>checkNotNull(String argument, String name)</code> with non null,
     * non-empty Argument.
     * </p>
     */
    public void testCheckNotNullOrEmpty() {
        try {
            Helper.checkNotNull("hello", "test");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should be thrown when argument is non null or non empty.");
        }
    }
}
