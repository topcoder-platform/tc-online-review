/**
 * Copyright &copy 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.util.generator.guid.failuretests;

import com.topcoder.util.generator.guid.*;

import junit.framework.TestCase;

import java.util.Random;


/**
 * This class tests Int32Generator for proper behavior. It covers all public & protected methods and public & protected
 * constructors.
 *
 * <p>
 * Copyright &copy 2004, TopCoder, Inc. All rights reserved.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Int32GeneratorTest extends TestCase {
    /**
     * Tests constructor Int32Generator().
     */
    public void testInt32Generator() {
        Generator generator = new Int32Generator();
        assertNotNull("constructor Int32Generator() constructed a null instance.",
            generator);
    }

    /**
     * Tests constructor Int32Generator(Random) for failure with null random argument.
     */
    public void testInt32GeneratorRandomFailure() {
        try {
            Generator generator = new Int32Generator(null);
            fail(
                "constructor Int32Generator(Random) should throw NullPointerException for null random argument");
        } catch (NullPointerException ex) {
            // good
        }
    }

    /**
     * Tests constructor Int32Generator(Random) for success.
     */
    public void testInt32GeneratorRandomSuccess() {
        Generator generator = new Int32Generator(new Random());
        assertNotNull("constructor Int32Generator(Random) constructed a null instance.",
            generator);
    }

    /**
     * Tests method getNextUUID().
     */
    public void testGetNextUUID() {
        int arraySize = 5000;
        Generator generator = new Int32Generator();
        UUID[] ids = new UUID[arraySize];

        for (int i = 0; i < arraySize; i++) {
            ids[i] = generator.getNextUUID();
        }

        UUIDArrayChecker.checkNotNull(ids);
        UUIDArrayChecker.checkPresentation(ids);
        UUIDArrayChecker.checkUniqueness(ids);
    }
}
