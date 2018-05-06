/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.validator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test class FreeDiskSpaceNonNativeChecker for correctness.
 * @author FireIce
 * @version 1.0
 */
public class FreeDiskSpaceNonNativeCheckerTestCase extends TestCase {
    /**
     * Represents the free disk space checker instance used in test.
     */
    private FreeDiskSpaceNonNativeChecker checker = new FreeDiskSpaceNonNativeChecker(".");

    /**
     * Test constructor, if the location argument is null, expect throw NullPointerException.
     */
    public void testCtorWithNullParam() {
        try {
            new FreeDiskSpaceNonNativeChecker(null);
            fail("if location argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test constructor, if the location argument is empty string, expect throw IllegalArgumentException.
     */
    public void testCtorWithEmptyStringParam() {
        try {
            new FreeDiskSpaceNonNativeChecker("  ");
            fail("if location argument is empty string, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test constructor, if the location argument is valid(not null, not empty), successfully created.
     */
    public void testCtorWithValidParam() {
        new FreeDiskSpaceNonNativeChecker(".");
    }

    /**
     * Test freeDiskSpaceExceedsSize method, if the argument is not positive, throw IllegalArgumentException.
     * @throws Exception if FreeDiskSpaceCheckerException thrown.
     */
    public void testFreeDiskSpaceExceedsSizeWithNonPositiveValue() throws Exception {
        assertNotNull("setup fails", checker);
        try {
            checker.freeDiskSpaceExceedsSize(-1);
            fail("if the argument is negative, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            checker.freeDiskSpaceExceedsSize(0);
            fail("if the argument is zero, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test freeDiskSpaceExceedsSize method, if the argument is valid, return the boolean result.
     * true for have enough, or false.
     * @throws Exception if execution fails because of exception.
     */
    public void testFreeDiskSpaceExceedsSizeWithPostiveValue() throws Exception {
        assertNotNull("setup fails", checker);
        assertTrue("should always return true", checker.freeDiskSpaceExceedsSize(1));
        assertFalse("normally, no disk should have so huge size", checker.freeDiskSpaceExceedsSize(Long.MAX_VALUE));
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(FreeDiskSpaceNonNativeCheckerTestCase.class);
    }
}
