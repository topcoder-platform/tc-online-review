/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link Helper} class.
 * </p>
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class HelperUnitTests extends TestCase {

    /**
     * <p>
     * Returns the test suite of <code>HelperUnitTests</code>.
     * </p>
     *
     * @return the test suite of <code>HelperUnitTests</code>.
     */
    public static Test suite() {
        return new TestSuite(HelperUnitTests.class);
    }

    /**
     * <p>
     * Tests failure of <code>checkNull(Object value, String name)</code> method with <code>value</code> is
     * <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void test_checkNull_Null() {
        Object value = null;

        try {
            Helper.checkNull(value, "name");

            // should not be here
            fail("IllegalArgumentException is expected here.");
        } catch (IllegalArgumentException e) {
            // should be here
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>checkNull(Object value, String name)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    public void test_checkNull() {
        Object value = new Object();

        Helper.checkNull(value, "name");

        // Good
    }
}
