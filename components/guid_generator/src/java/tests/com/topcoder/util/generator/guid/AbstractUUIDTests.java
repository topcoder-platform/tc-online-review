/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the AbstractUUID class.
 * </p>
 * <p>
 * The method parse can be directly tested because it is static.
 * The constructor, getBitCount and toByteArray can't be directly testes because the AbstractUUID class is abstract.
 * The class ConcreteUUID, defined below, is used to test those methods.  That class just extends AbstractUUID and
 * provides the constructor.
 * </p>
 * <p>
 * The tests for this class include:
 * </p>
 * <ul>
 * <li>parse with a null parameter, empty string and strings that are not 8 nor 36 characters length. </li>
 * <li>parse with strings of valid length but containing invalid characters</li>
 * <li>parse 8 characters and 36 characters valid strings</li>
 * <li>the constructor with a null parameter</li>
 * <li>getBitCount and toByteArray methods when the constructor is called with different byte arrays</li>
 * </ul>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class AbstractUUIDTests extends TestCase {

    /**
     * return a suite of tests.
     *
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(AbstractUUIDTests.class);
    }

    /**
    * Test parse with null, it must throw NullPointerException.
    */
    public void testParseNull() {
        try {
            AbstractUUID.parse(null);
            fail("NullPointerException must be thrown when parse called with null");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     * Test parse with an empty string, it must throw IllegalArgumentException.
     */
    public void testParseEmtpy() {
        try {
            AbstractUUID.parse("");
            fail("IllegalArgumentException must be thrown when parse called with an empty string");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

    /**
     * Test parse with a string of invalid length (different from 8 and 36). 
     * It must throw IllegalArgumentException.
     */
    public void testParseInvalidLength1() {
        try {
            AbstractUUID.parse("1234");
            fail("IllegalArgumentException must be thrown when the string is not of length 8 or 36");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

    /**
     * Test parse with a string of invalid length (different from 8 and 36). Must throw IllegalArgumentException
     */
    public void testParseInvalidLength2() {
        try {
            AbstractUUID.parse("1234567890abdef");
            fail("IllegalArgumentException must be thrown when the string is not of length 8 or 36");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

   /**
    * Test to parse a string representing a 32 bits UUID with invalid characters.
    */
    public void testParse32bitsInvalid1() {
        try {
            AbstractUUID.parse("abcdefg0");
            fail("IllegalArgumentException must be thrown when the string has non valid characters");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

   /**
     * Test to parse a valid string representing a 32 bits UUID.
     */
    public void testParse32bits1() {
        byte[] expected = {0xab - 0x100, 0xcd - 0x100, 0xef - 0x100, 0xff - 0x100 };
        UUID uuid = AbstractUUID.parse("abcdefff");
        byte[] bytes = uuid.toByteArray();

        assertEquals("Invalid length in parsing.", 4, bytes.length);
        assertTrue("Bad parsing value", Arrays.equals(expected, bytes));
    }

    /**
     * Test to parse a valid string representing a 32 bits UUID.
     */
    public void testParse32bits2() {
        byte[] expected = {0x01, 0x23, 0x45, 0x67 };
        UUID uuid = AbstractUUID.parse("01234567");
        byte[] bytes = uuid.toByteArray();

        assertEquals("Invalid length in parsing.", 4, bytes.length);
        assertTrue("Bad parsing value", Arrays.equals(expected, bytes));
    }


    /**
     * Test to parse a string representing a 128 bits UUID with invalid characters.
     */
    public void testParse128bitsInvalid1() {
        try {
            AbstractUUID.parse("f81d4fae-7dec-11d0-a765-00a0c91e6bfx");
            fail("IllegalArgumentException must be thrown when the string has non valid characters");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

    /**
     * Test to parse a string representing a 128 bits UUID with a space instead of one of '-'.
     */
    public void testParse128bitsInvalid2() {
        try {
            AbstractUUID.parse("f81d4fae 7dec-11d0-a765-00a0c91e6bfx");
            fail("IllegalArgumentException must be thrown when the string has non valid characters");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

    /**
     * Test to parse a valid string representing a 128 bits UUID.
     */
    public void testParse128bits1() {
        byte[] expected = {0xf8 - 0x100, 0x1d, 0x4f, 0xae - 0x100, 0x7d, 0xec - 0x100,
                           0x11, 0xd0 - 0x100, 0xa7 - 0x100, 0x65, 0x00, 0xa0 - 0x100,
                           0xc9 - 0x100, 0x1e, 0x6b, 0xf6 - 0x100};
        UUID uuid = AbstractUUID.parse("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        byte[] bytes = uuid.toByteArray();

        assertEquals("Invalid length in parsing.", 16, bytes.length);

        assertTrue("Bad parsing value", Arrays.equals(expected, bytes));
    }

    /**
     * Test that   NullPointerException is thrown when the constructor is called with null.
     */
    public void testConstructorNull() {
        try {
            new ConcreteUUID(null);
            fail("NullPointerException must be thrown when the constructor is called with null");
        } catch (NullPointerException e) {
             // expected behavior
        }    
    }

    /**
     * Test the method getBitCount with a 24 bit UUID.
     */
    public void testGetBitCount1() {
        AbstractUUID uuid = new ConcreteUUID(new byte[]{1, 2, 3});
        assertEquals("Invalid getBitCount value", 24, uuid.getBitCount());
    }

    /**
     * Test the method getBitCount with a 48 bit UUID.
     */
    public void testGetBitCount2() {
        AbstractUUID uuid = new ConcreteUUID(new byte[]{1, 2, 3, 4, 5, 6});
        assertEquals("Invalid getBitCount value", 48, uuid.getBitCount());
    }

    /**
     * Test the method toByteArray with a 5 bytes array.
     */
    public void testtoByteArray1() {
        byte[] bytes = {(byte) 0xA0, 0x00, (byte) 0xFF, 0x45, 0x79};
        AbstractUUID uuid = new ConcreteUUID(bytes);
        assertTrue("Invalid toByteArray value", Arrays.equals(bytes, uuid.toByteArray()));
    }

    /**
     * Test the method toByteArray with a 6 bytes array.
     */
    public void testtoByteArray2() {
        byte[] bytes = {1, 2, 3, 4, 5, 6};
        AbstractUUID uuid = new ConcreteUUID(bytes);
        assertTrue("Invalid toByteArray value", Arrays.equals(bytes, uuid.toByteArray()));
    }

}

/**
 * This class is used just for testing the AbstractUUID.
 * It extends AbstractUUID but is not abstract in order to be instantiated.
 *
 * @author TCSDEVELOPER
 */
class ConcreteUUID extends AbstractUUID {

    /**
     * @param bytes the bytes representing this UUID
     */
    protected ConcreteUUID(byte[] bytes) {
        super(bytes);
    }
}