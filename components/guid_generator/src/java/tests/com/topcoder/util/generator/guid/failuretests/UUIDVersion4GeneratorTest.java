/**
 * Copyright &copy 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.util.generator.guid.failuretests;

import com.topcoder.util.generator.guid.*;

import junit.framework.TestCase;

import java.util.Random;


/**
 * This class tests UUIDVersion4Generator for proper behavior. It covers all public & protected methods and public &
 * protected constructors.
 *
 * <p>
 * Copyright &copy 2004, TopCoder, Inc. All rights reserved.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UUIDVersion4GeneratorTest extends TestCase {
    /**
     * Tests constructor UUIDVersion4Generator().
     */
    public void testUUIDVersion4Generator() {
        UUIDVersion4Generator generator = new UUIDVersion4Generator();
        assertNotNull("constructor did not construct an instance of UUIDVersion4Generator.",
            generator);
    }

    /**
     * Tests constructor UUIDVersion4Generator(Random random) for success.
     */
    public void testUUIDVersion4GeneratorRandomSuccess() {
        UUIDVersion4Generator generator = new UUIDVersion4Generator(new Random());
        assertNotNull("constructor did not construct an instance of UUIDVersion4Generator.",
            generator);
    }

    /**
     * Tests constructor UUIDVersion4Generator(Random random) for failure with null random argument.
     */
    public void testUUIDVersion4GeneratorRandomFailure() {
        try {
            UUIDVersion4Generator generator = new UUIDVersion4Generator(null);
            fail("constructor should throw NullPointerException.");
        } catch (NullPointerException ex) {
            // good
        }
    }

    /**
     * <p>
     * This test will attempt to check for reasonable behavior of the getNextUUID() method.It generate 5000 UUID
     * instances and check the validity(NotNull, Uniqueness, Version and Variant fields) using helper class
     * UUIDArrayChecker.
     * </p>
     */
    public void testGetNextUUID() {
        int testArraySize = 5000;
        Generator generator = new UUIDVersion4Generator();
        UUID[] ids = new UUID[testArraySize];

        // generate UUID array
        for (int index = 0; index < testArraySize; index++) {
            ids[index] = generator.getNextUUID();
        }

        // check validity of the generated UUIDs
        UUIDArrayChecker.checkNotNull(ids);
        UUIDArrayChecker.checkUniqueness(ids);
        UUIDArrayChecker.checkVariantAndVersion(ids, 4);
    }

    /**
     * <p>
     * This test will attempt to check for reasonable behavior of the getNextUUID() method.It generate 10000 UUID
     * instances and check the validity(NotNull, Uniqueness, Version and Variant fields) using helper class
     * UUIDArrayChecker.
     * </p>
     */
    public void testGetNextUUID1() {
        int testArraySize = 10000;
        Generator generator = new UUIDVersion4Generator(new Random());
        UUID[] ids = new UUID[testArraySize];

        // generate UUID array
        for (int index = 0; index < testArraySize; index++) {
            ids[index] = generator.getNextUUID();
        }

        // check validity of the generated UUIDs
        UUIDArrayChecker.checkNotNull(ids);
        UUIDArrayChecker.checkPresentation(ids);
        UUIDArrayChecker.checkUniqueness(ids);
        UUIDArrayChecker.checkVariantAndVersion(ids, 4);
    }
}
