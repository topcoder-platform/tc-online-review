/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import junit.framework.TestCase;

/**
 * <p>
 * This class contains test cases for Helper.
 * </p>
 * @author sokol
 * @version 1.0
 */
public class HelperTest extends TestCase {

    /**
     * <p>
     * Tests {@link Helper#checkState(boolean, String)} with true state passed.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testState() {
        try {
            Helper.checkState("".trim().length() == 0, "someMessage");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
