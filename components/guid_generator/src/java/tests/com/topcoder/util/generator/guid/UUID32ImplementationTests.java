/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the UUID32Implementation class.
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
public class UUID32ImplementationTests extends TestCase {
    /**
     * Array of 4 bytes for testing.
     */
    private static final byte[] BYTE_ARRAY1 = {1 , 2, 3, 4};

    /**
     * Array of 4 bytes for testing.
     */
    private static final byte[] BYTE_ARRAY2 = {(byte) 160, (byte) 161, (byte) 162, (byte) 163};
    
    /**
     * return a suite of tests.
     * 
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(UUID32ImplementationTests.class);
    }

    /**
     * Test that the toString method produces the right format.  
     */
    public void testToString1() {
        UUID32Implementation uuid = new UUID32Implementation(BYTE_ARRAY1);
        assertEquals("Bad toString conversion", "01020304", uuid.toString());
    }

    /**
     * Test that the toString method produces the right format.
     */
    public void testToString2() {
        UUID32Implementation uuid = new UUID32Implementation(BYTE_ARRAY2);

        assertEquals("Bad toString conversion", "a0a1a2a3", uuid.toString());
    }

    /**
     * Test the toByteArray conversion method with BYTE_ARRAY1.
     */
    public void testToByteArray() {
        UUID32Implementation uuid = new UUID32Implementation(BYTE_ARRAY1);
        assertTrue("Bad toByteArray return value", BYTE_ARRAY1.equals(uuid.toByteArray()));
    }

    /**
     * Test the getBitCount () method with BYTE_ARRAY1.
     */
    public void testGetBitCount() {
        UUID32Implementation uuid = new UUID32Implementation(BYTE_ARRAY1);
        assertEquals("Bad bit count", 32, uuid.getBitCount());
    }

    /**
     *  Test the constructor with null parameter - it must throw NullPointerException.
     */
    public void testConstructorNull() {
        try {
            new UUID32Implementation(null);
            fail("UUID32Implementation constructor with null must throw NullPointerException");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     *  Test the constructor with an array that is not 4 bytes in size.
     */
    public void testConstructorBadSize1() {
        try {
            new UUID32Implementation(new byte[]{});
            fail("UUID32Implementation constructor with bad size must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

    /**
     *  Test the constructor with an array that is not 4 bytes size.
     */
    public void testConstructorBadSize2() {
        byte[] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        try {
            new UUID32Implementation(b);
            fail("UUID32Implementation constructor with bad size must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }
}