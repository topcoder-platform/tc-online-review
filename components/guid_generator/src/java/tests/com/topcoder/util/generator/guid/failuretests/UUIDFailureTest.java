/**
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.generator.guid.failuretests;

import com.topcoder.util.generator.guid.*;

import junit.framework.TestCase;


/**
 * Failure test for all the UUID implementation classes.
 *
 * @author Standlove
 * @version 1.0
 */
public class UUIDFailureTest extends TestCase {
    /**
     * Test UUID128Implementation ctor with null bytes,
     * NPE is expected.
     */
    public void testUUID128ImplementationNull() {
        try {
            new UUID128Implementation(null);
            fail("The given bytes arg is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test UUID128Implementation ctor with bytes of invalid
     * length, IAE is expected.
     */
    public void testUUID128ImplementationInvaildLen() {
        try {
            new UUID128Implementation(new byte[0]);
            fail("The length is invalid");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * Test UUID32Implementation ctor with null bytes,
     * NPE is expected.
     */
    public void testUUID32ImplementationNull() {
        try {
            new UUID32Implementation(null);
            fail("The given bytes arg is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test UUID32Implementation ctor with bytes of invalid
     * length, IAE is expected.
     */
    public void testUUID32ImplementationInvaildLen() {
        try {
            new UUID32Implementation(new byte[0]);
            fail("The length is invalid");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }
}
