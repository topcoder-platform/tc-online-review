/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the UUID128Implementation class.
 * </p>
 * <p>
 * It will test:
 * </p>
 * <ul>
 * <li> the constructor with: null parameter, bad array size and right array size</li>
 * <li> toString () with different values passed to the constructor</li>
 * <li> toByteArray () and getBitCount () methods.</li>
 * </ul>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UUID128ImplementationTests extends TestCase {
    /**
     * Array of 16 bytes for testing.
     */
    private static final  byte[] BYTE_ARRAY1  = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    /**
     * Array of 16 bytes for testing.
     */
    private static final  byte[] BYTE_ARRAY2 = {(byte) 160, (byte) 161, (byte) 162, (byte) 163, (byte) 164, 
                                                (byte) 165, (byte) 166, (byte) 167, (byte) 168, (byte) 169, 
                                                (byte) 170, (byte) 171, (byte) 172, (byte) 173, (byte) 174, 
                                                (byte) 175};
    
    /**
     * return a suite of tests.
     * 
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(UUID128ImplementationTests.class);
    }

    /**
     * Test the toString conversion method to see if the string format is right.
     */
    public void testToString1() {
        UUID128Implementation uuid = new UUID128Implementation(BYTE_ARRAY1);
        assertEquals("Bad toString conversion", "01020304-0506-0708-090a-0b0c0d0e0f10", uuid.toString());
    }

    /**
     * Test the toString conversion method to see if the string format is right.
     */
    public void testToString2() {
        UUID128Implementation uuid = new UUID128Implementation(BYTE_ARRAY2);
        assertEquals("Bad toString conversion", "a0a1a2a3-a4a5-a6a7-a8a9-aaabacadaeaf", uuid.toString());
    }

    /**
     * Test the toByteArray method with BYTE_ARRAY1.
     */
    public void testToByteArray() {
        UUID128Implementation uuid = new UUID128Implementation(BYTE_ARRAY1);
        assertTrue("Bad toByteArray return value", BYTE_ARRAY1.equals(uuid.toByteArray()));
    }

    /**
     * Test the getBitCount () method with BYTE_ARRAY1.
     */
    public void testGetBitCount() {
        UUID128Implementation uuid = new UUID128Implementation(BYTE_ARRAY1);
        assertEquals("Bad bit count", 128, uuid.getBitCount());
    }

    /**
     *  Test the constructor with null parameter - it must throw NullPointerException.
     */
    public void testConstructorNull() {
        try {
            new UUID128Implementation(null);
            fail("UUID128Implementation constructor with null must throw NullPointerException");
        } catch (NullPointerException e) {
            // expected behavior
        }
     }

     /**
      *  Test the constructor with an array that is not 16 bytes in size.
      */
    public void testConstructorBadSize1() {
        byte[] b = {};
        try {
            new UUID128Implementation(b);
            fail("UUID128Implementation constructor with bad size must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

   /**
    *  Test the constructor with an array that is not 16 bytes in size.
    */
    public void testConstructorBadSize2() {
        byte[] b = {1, 2, 3, 4};
        try {
            new UUID128Implementation(b);
            fail("UUID128Implementation constructor with bad size must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
   }
}