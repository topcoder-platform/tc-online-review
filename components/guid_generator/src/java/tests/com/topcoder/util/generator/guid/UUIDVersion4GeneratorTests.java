/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the UUIDVersion4Generator class.
 * </p>
 * <p>
 * Because this testCase extends UUIDGeneratorTests, all the tests provided on that class will be executed
 * over the UUIDVersion1Generator class.  Please see to UUIDGeneratorTests. 
 * </p>
 * <p>
 * It will also test some version 4 specific features:
 * </p>
 * <ul>
 * <li>The layout of the returned UUID (to look for version and variant)</li>
 * <li>using the constructor with ZeroRandom to test if this parameter is used fine,
 *     and which bytes are random generated</li>
 * </ul>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */

public class UUIDVersion4GeneratorTests extends UUIDGeneratorTests {
    /**
     * The instance of UUIDVersion4Generator that will be used for testing.
     * The UUIDGeneratorTests class will ask for it using getGenerator() method.
     */
    private static AbstractGenerator generator = new UUIDVersion4Generator();

    /**
     * return a suite of tests.
     *
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(UUIDVersion4GeneratorTests.class);
    }
    /**
     * get the UUID version 4 generator.
     *
     * @return an instance of a kind of AbstractGenerator
     */
    protected AbstractGenerator getGenerator() {
        return generator;
    } 

    
    /**
     * get the UUID version 4 generator.
     * 
     * @param random object for generating random numbers
     * @return an instance of a kind of AbstractGenerator
     */
    protected AbstractGenerator getGenerator(Random random) {
        return new UUIDVersion4Generator(random);
    } 

    /**
     * Test that the values generated have the rigth Variant value (1 in bit 7; 0 in bit 6 of byte 8) and
     * rigth version value (0100 in the most significant nibble of byte 6). 
     *
     */
    public void testValues() {
        byte[] bytes;

        for (int i = 0; i < 10000; i++) {
            bytes = generator.getNextUUID().toByteArray();

            assertEquals("Bad Variant value", 0x80, bytes[8] & 0xC0);
            assertEquals("Bad Version value", 0x40, bytes[6] & 0xF0);
        }
    }

    /**
     * Test using the ZeroRandom class that when the constructor is called with a Random object, it is used
     * for generating numbers.
     * It is also checking that the bit layout of the UUID is fine
     */
    public void testCustomRandom() {
        // When using the ZeroRandom class, all the "random" values are 0's, so for a Version 4 UUID,
        // all bytes will be set to 0, except for the version number (4) and the reserved bits. 
        assertEquals("getNextUUID bad result", "00000000-0000-4000-8000-000000000000",
                new UUIDVersion4Generator(new ZeroRandom()).getNextUUID().toString());
    }

}