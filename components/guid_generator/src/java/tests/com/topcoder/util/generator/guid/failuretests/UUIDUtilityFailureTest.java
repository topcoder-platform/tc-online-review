/**
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.generator.guid.failuretests;

import com.topcoder.util.generator.guid.*;

import junit.framework.TestCase;


/**
 * Failure test for the UUIDUtility class.
 *
 * @author Standlove
 * @version 1.0
 */
public class UUIDUtilityFailureTest extends TestCase {
    /**
     * Test getGenerator method with null type,
     * NPE is expected.
     */
    public void testGetGeneratorNull() {
        try {
            UUIDUtility.getGenerator(null);
            fail("The given type is null.");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test getNextUUID method with null uuid,
     * NPE is expected.
     */
    public void testGetNextUUIDNull() {
        try {
            UUIDUtility.getNextUUID(null);
            fail("The given uuid is null.");
        } catch (NullPointerException npe) {
            // good
        }
    }
}
