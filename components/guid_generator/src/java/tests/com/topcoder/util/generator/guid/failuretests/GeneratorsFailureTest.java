/**
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.generator.guid.failuretests;

import com.topcoder.util.generator.guid.*;

import junit.framework.TestCase;


/**
 * Failure test for all the generators.
 *
 * @author Standlove
 * @version 1.0
 */
public class GeneratorsFailureTest extends TestCase {
    /**
     * Test Int32Generator ctor with null arg, NPE is expected.
     */
    public void testInt32GeneratorNullRandom() {
        try {
            new Int32Generator(null);
            fail("The given random arg is null.");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test UUIDVersion1Generator ctor with null arg, NPE is expected.
     */
    public void testUUIDVersion1GeneratorNullRandom() {
        try {
            new UUIDVersion1Generator(null);
            fail("The given random arg is null.");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test UUIDVersion4Generator ctor with null arg, NPE is expected.
     */
    public void testUUIDVersion4GeneratorNullRandom() {
        try {
            new UUIDVersion4Generator(null);
            fail("The given random arg is null.");
        } catch (NullPointerException npe) {
            // good
        }
    }
}
