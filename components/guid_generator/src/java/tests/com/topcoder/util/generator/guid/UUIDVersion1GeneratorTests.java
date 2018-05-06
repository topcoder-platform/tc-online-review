/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the UUIDVersion1Generator class.
 * </p>
 * <p>
 * Because this testCase extends UUIDGeneratorTests, all the tests provided on that class will be executed
 * over the UUIDVersion1Generator class.  Please see UUIDGeneratorTests.
 * </p>
 * <p>
 * It will also test some version1 specific features:
 * </p>
 * <ul>
 * <li>The layout of the returned UUID (to look for version, variant and multicast)</li>
 * <li>using the constructor with ZeroRandom to test if this parameter is used fine,
 *     and which bytes are random generated</li>
 * </ul>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UUIDVersion1GeneratorTests extends UUIDGeneratorTests {
    /**
     * The instance of UUIDVersion1Generator that will be used for testing.
     * The UUIDGeneratorTests class will ask for it using getGenerator() method.
     */
    private static AbstractGenerator generator = new UUIDVersion1Generator();

    /**
     * return a suite of tests.
     *
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(UUIDVersion1GeneratorTests.class);
    }
    /**
     * get the UUID version 1 generator.
     *
     * @return an instance of a kind of AbstractGenerator
     */
    protected AbstractGenerator getGenerator() {
        return generator;
    }

    /**
     * get the UUID version 1 generator.
     *
     * @param random object for generating random numbers
     * @return an instance of a kind of AbstractGenerator
     */
    protected AbstractGenerator getGenerator(Random random) {
        return new UUIDVersion1Generator(random);
    }

    /**
     * Test that the values generated have the rigth Variant value (1 in bit 7; 0 in bit 6 of byte 8) and
     * rigth version value (0001 in the most significant nibble of byte 6).
     *
     */
    public void testValues() {
        byte[] bytes;

        for (int i = 0; i < 10000; i++) {
            bytes = generator.getNextUUID().toByteArray();

            assertEquals("Bad Variant value", 0x80, bytes[8] & 0xC0);
            assertEquals("Bad Version value", 0x10, bytes[6] & 0xF0);
            assertEquals("Multicast bit not set", 0x80, bytes[10] & 0x80);
        }
    }

    /**
     * Test using the ZeroRandom class that when the constructor is called with a Random object, it is used
     * for generating numbers.
     * It is also checking that the bit layout of the UUID is fine.
     */
    public void testCustomRandom() {
        // When using the ZeroRandom class, all the "random" values are 0's, so for a Version 1 UUID,
        // it this implementation, bytes 10 and 11 are "random" (to complete the 6 bytes needed for a MAC
        // address) except for the MSB of byte 10 that should be set to indicate that is not a real MAC address.
        // Also the clock sequence should be 0 when this is called for the first time.
        byte[] bytes = new UUIDVersion1Generator(new ZeroRandom()).getNextUUID().toByteArray();
        assertEquals("bad clock sequence hi & reserved", 0, bytes [8] & 0x3f);
        assertEquals("bad clock sequence low", 0, bytes [9]);
        assertEquals("bad node byte (10)", (byte) 0x80, bytes [10]);
        assertEquals("bad node byte (11)", 0, bytes [11]);
    }  
}