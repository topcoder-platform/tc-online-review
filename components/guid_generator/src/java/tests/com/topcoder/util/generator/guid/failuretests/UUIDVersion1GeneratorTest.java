/**
 * Copyright &copy 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.util.generator.guid.failuretests;

import com.topcoder.util.generator.guid.*;

import junit.framework.TestCase;

import java.util.Random;


/**
 * This class tests UUIDVersion1Generator for proper behavior.
 * It covers all public & protected methods and public & protected constructors.
 * <p>
 * Copyright &copy 2004, TopCoder, Inc. All rights reserved.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.0
 *
 */
public class UUIDVersion1GeneratorTest extends TestCase {
    /**
     * Tests constructor UUIDVersion1Generator().
     */
    public void testUUIDVersion1Generator() {
        UUIDVersion1Generator generator = new UUIDVersion1Generator();
        assertNotNull("constructor did not construct an instance of UUIDVersion1Generator.",
            generator);
    }

    /**
     * Tests constructor UUIDVersion1Generator(Random random) for success.
     */
    public void testUUIDVersion1GeneratorRandomSuccess() {
        UUIDVersion1Generator generator = new UUIDVersion1Generator(new Random());
        assertNotNull("constructor did not construct an instance of UUIDVersion1Generator.",
            generator);
    }

    /**
     * Tests constructor UUIDVersion1Generator(Random random) for failure with null random argument.
     */
    public void testUUIDVersion1GeneratorRandomFailure() {
        try {
            UUIDVersion1Generator generator = new UUIDVersion1Generator(null);
            fail("constructor should throw NullPointerException.");
        } catch (NullPointerException ex) {
            // good
        }
    }

    /**
     * <p>
     * This test will attempt to check for reasonable behavior of the getNextUUID() method.It generate 10000 UUID
     * instances and check the validity(NotNull, Uniqueness, Version and Variant fields) using helper class
     * UUIDArrayChecker.
     * </p>
     */
    public void testGetNextUUID() {
        int testArraySize = 10000;
        Generator generator = new UUIDVersion1Generator();
        UUID[] ids = new UUID[testArraySize];

        // generate UUID array
        long startTime = System.currentTimeMillis();

        for (int index = 0; index < testArraySize; index++) {
            ids[index] = generator.getNextUUID();
        }

        long endTime = System.currentTimeMillis();

        // check validity of the generated UUIDs
        UUIDArrayChecker.checkNotNull(ids);
        UUIDArrayChecker.checkPresentation(ids);
        UUIDArrayChecker.checkUniqueness(ids);
        UUIDArrayChecker.checkVariantAndVersion(ids, 1);
        // UUIDArrayChecker.checkCreationTime(ids, startTime, endTime);
        UUIDArrayChecker.checkIEEEAddress(ids);
    }
}
