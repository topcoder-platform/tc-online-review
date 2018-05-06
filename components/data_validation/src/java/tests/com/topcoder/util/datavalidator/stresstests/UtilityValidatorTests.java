/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.stresstests;

import junit.framework.TestCase;

import com.topcoder.util.datavalidator.ByteValidator;
import com.topcoder.util.datavalidator.CharacterValidator;
import com.topcoder.util.datavalidator.DoubleValidator;
import com.topcoder.util.datavalidator.FloatValidator;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.ShortValidator;
import com.topcoder.util.datavalidator.StringValidator;

/**
 * This tests utility validators.
 *
 * @author TCSDEVELOPER, Psyho
 * @version 1.1
 * @since 1.0
 */
public class UtilityValidatorTests extends TestCase {

    /**
     * Sets up testing environment.
     */
    public void setUp() {
        // nothing
    }

    /**
     * Tears down testing environment.
     */
    public void tearDown() {
        // nothing
    }

    /**
     * Tests IntegerValidator.
     */
    public void testIntegerValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            IntegerValidator validator = IntegerValidator.greaterThan(7);
            assertTrue(validator.valid(8));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(8));
            assertNotNull(validator.getMessage(7));

            // if(true) return;

            validator = IntegerValidator.greaterThanOrEqualTo(7);
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(6));

            assertNull("Message for 7>=7 should be null", validator.getMessage(7));
            assertNotNull("Message for 6>=7 should not be null", validator.getMessage(6));

            validator = IntegerValidator.lessThan(7);
            assertTrue(validator.valid(6));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(6));
            assertNotNull(validator.getMessage(7));

            // if(true) return;

            validator = IntegerValidator.lessThanOrEqualTo(7);
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(8));

            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(8));

            validator = IntegerValidator.inRange(5, 7);
            assertTrue(validator.valid(5));
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(4));
            assertFalse(validator.valid(8));

            assertNull(validator.getMessage(5));
            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(4));
            assertNotNull(validator.getMessage(8));

            validator = IntegerValidator.inExclusiveRange(5, 7);
            assertTrue(validator.valid(6));
            assertFalse(validator.valid(5));
            assertFalse(validator.valid(7));

            // if(true) return;

            assertNull(validator.getMessage(6));
            assertNotNull(validator.getMessage(5));
            assertNotNull(validator.getMessage(7));

            validator = IntegerValidator.isPositive();
            assertTrue(validator.valid(1));
            assertFalse(validator.valid(0));

            assertNull(validator.getMessage(1));
            assertNotNull(validator.getMessage(0));

            validator = IntegerValidator.isNegative();
            assertTrue(validator.valid(-1));
            assertFalse(validator.valid(0));

            assertNull(validator.getMessage(-1));
            assertNotNull(validator.getMessage(0));

            validator = IntegerValidator.isOdd();
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(8));

            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(8));

            validator = IntegerValidator.isEven();
            assertTrue(validator.valid(8));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(8));
            assertNotNull(validator.getMessage(7));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Integer Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests LongValidator.
     */
    public void testLongValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            LongValidator validator = LongValidator.greaterThan(7);
            assertTrue(validator.valid(8));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(8));
            assertNotNull(validator.getMessage(7));

            validator = LongValidator.greaterThanOrEqualTo(7);
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(6));

            assertNull("Message for 7>=7 should be null", validator.getMessage(7));
            assertNotNull("Message for 6>=7 should not be null", validator.getMessage(6));

            validator = LongValidator.lessThan(7);
            assertTrue(validator.valid(6));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(6));
            assertNotNull(validator.getMessage(7));

            validator = LongValidator.lessThanOrEqualTo(7);
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(8));

            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(8));

            validator = LongValidator.inRange(5, 7);
            assertTrue(validator.valid(5));
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(4));
            assertFalse(validator.valid(8));

            assertNull(validator.getMessage(5));
            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(4));
            assertNotNull(validator.getMessage(8));

            validator = LongValidator.inExclusiveRange(5, 7);
            assertTrue(validator.valid(6));
            assertFalse(validator.valid(5));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(6));
            assertNotNull(validator.getMessage(5));
            assertNotNull(validator.getMessage(7));

            validator = LongValidator.isPositive();
            assertTrue(validator.valid(1));
            assertFalse(validator.valid(0));

            assertNull(validator.getMessage(1));
            assertNotNull(validator.getMessage(0));

            validator = LongValidator.isNegative();
            assertTrue(validator.valid(-1));
            assertFalse(validator.valid(0));

            assertNull(validator.getMessage(-1));
            assertNotNull(validator.getMessage(0));

            validator = LongValidator.isOdd();
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(8));

            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(8));

            validator = LongValidator.isEven();
            assertTrue(validator.valid(8));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(8));
            assertNotNull(validator.getMessage(7));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Long Validator time cost:" + (end - start) + "ms");

    }

    /**
     * Tests ByteValidator.
     */
    public void testByteValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            ByteValidator validator = ByteValidator.greaterThan((byte) 7);
            assertTrue(validator.valid((byte) 8));
            assertFalse(validator.valid((byte) 7));

            // if(true) return;
            assertNull("Message for 8>7 should be null", validator.getMessage((byte) 8));
            assertNotNull("Message for 7>7 should not be null", validator.getMessage((byte) 7));

            validator = ByteValidator.greaterThanOrEqualTo((byte) 7);
            assertTrue(validator.valid((byte) 7));
            assertFalse(validator.valid((byte) 6));

            assertNull("Message for 7>=7 should be null", validator.getMessage((byte) 7));
            assertNotNull("Message for 6>=7 should not be null", validator.getMessage((byte) 6));

            validator = ByteValidator.lessThan((byte) 7);
            assertTrue(validator.valid((byte) 6));
            assertFalse(validator.valid((byte) 7));

            assertNull(validator.getMessage((byte) 6));
            assertNotNull(validator.getMessage((byte) 7));

            // if(true) return;

            validator = ByteValidator.lessThanOrEqualTo((byte) 7);
            assertTrue(validator.valid((byte) 7));
            assertFalse(validator.valid((byte) 8));

            // if(true) return;

            assertNull(validator.getMessage((byte) 7));
            assertNotNull(validator.getMessage((byte) 8));

            validator = ByteValidator.inRange((byte) 5, (byte) 7);
            assertTrue(validator.valid((byte) 5));
            assertTrue(validator.valid((byte) 7));
            assertFalse(validator.valid((byte) 4));
            assertFalse(validator.valid((byte) 8));

            assertNull(validator.getMessage((byte) 5));
            assertNull(validator.getMessage((byte) 7));
            assertNotNull(validator.getMessage((byte) 4));
            assertNotNull(validator.getMessage((byte) 8));

            validator = ByteValidator.inExclusiveRange((byte) 5, (byte) 7);
            assertTrue(validator.valid((byte) 6));
            assertFalse(validator.valid((byte) 5));
            assertFalse(validator.valid((byte) 7));

            assertNull(validator.getMessage((byte) 6));
            assertNotNull(validator.getMessage((byte) 5));
            assertNotNull(validator.getMessage((byte) 7));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Byte Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests ShortValidator.
     */
    public void testShortValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            ShortValidator validator = ShortValidator.greaterThan((short) 7);
            assertTrue("Message for 8>7 should be null", validator.valid((short) 8));
            assertFalse("Message for 7>7 should not be null", validator.valid((short) 7));

            assertNull(validator.getMessage((short) 8));
            assertNotNull(validator.getMessage((short) 7));

            // if(true) return;

            validator = ShortValidator.greaterThanOrEqualTo((short) 7);
            assertTrue(validator.valid((short) 7));
            assertFalse(validator.valid((short) 6));

            assertNull("Message for 7>=7 should be null", validator.getMessage((short) 7));
            assertNotNull("Message for 6>=7 should not be null", validator.getMessage((short) 6));

            validator = ShortValidator.lessThan((short) 7);
            assertTrue("6<7 is valid", validator.valid((short) 6));
            assertFalse("7<7 is not valid", validator.valid((short) 7));

            // if(true) return;

            assertNull(validator.getMessage((short) 6));
            assertNotNull(validator.getMessage((short) 7));

            validator = ShortValidator.lessThanOrEqualTo((short) 7);
            assertTrue(validator.valid((short) 7));
            assertFalse(validator.valid((short) 8));

            assertNull(validator.getMessage((short) 7));
            assertNotNull(validator.getMessage((short) 8));

            validator = ShortValidator.inRange((short) 5, (short) 7);
            assertTrue(validator.valid((short) 5));
            assertTrue(validator.valid((short) 7));
            assertFalse(validator.valid((short) 4));
            assertFalse(validator.valid((short) 8));

            assertNull(validator.getMessage((short) 5));
            assertNull(validator.getMessage((short) 7));
            assertNotNull(validator.getMessage((short) 4));
            assertNotNull(validator.getMessage((short) 8));

            validator = ShortValidator.inExclusiveRange((short) 5, (short) 7);
            assertTrue(validator.valid((short) 6));
            assertFalse(validator.valid((short) 5));
            assertFalse(validator.valid((short) 7));

            assertNull(validator.getMessage((short) 6));
            assertNotNull(validator.getMessage((short) 5));
            assertNotNull(validator.getMessage((short) 7));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Short Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests FloatValidator.
     */
    public void testFloatValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            FloatValidator validator = FloatValidator.greaterThan(7);
            assertTrue(validator.valid(8));
            assertFalse(validator.valid(7));

            assertNull("Message for 8>7 should be null", validator.getMessage(8));
            assertNotNull("Message for 7>7 should not be null", validator.getMessage(7));

            validator = FloatValidator.greaterThanOrEqualTo(7);
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(6));

            // if(true) return;

            assertNull("Message for 7>=7 should be null", validator.getMessage(7));
            assertNotNull("Message for 6>=7 should not be null", validator.getMessage(6));

            validator = FloatValidator.lessThan(7);
            assertTrue(validator.valid(6));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(6));
            assertNotNull(validator.getMessage(7));

            // if(true) return;

            validator = FloatValidator.lessThanOrEqualTo(7);
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(8));

            assertNull("Message for 7<=7 should be null", validator.getMessage(7));
            assertNotNull("Message for 8<=7 should not be null", validator.getMessage(8));

            // if(true) return;

            validator = FloatValidator.inRange(5, 7);
            assertTrue(validator.valid(5));
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(4));
            assertFalse(validator.valid(8));

            // if(true) return;
            assertNull("message for 5 in [5,7] should be null!", validator.getMessage(5));
            assertNull("message for 7 in [5,7] should be null!", validator.getMessage(7));
            assertNotNull("message for 4 in [5,7] should not be null!", validator.getMessage(4));
            assertNotNull("message for 8 in [5,7] should not be null!", validator.getMessage(8));

            if (true) return;
            validator = FloatValidator.inExclusiveRange(5, 7);
            assertTrue(validator.valid(6));
            assertFalse(validator.valid(5));
            assertFalse(validator.valid(7));

            // if(true) return;

            assertNull(validator.getMessage(6));
            assertNotNull(validator.getMessage(5));
            assertNotNull(validator.getMessage(7));

            validator = FloatValidator.isPositive();
            assertTrue(validator.valid(1));
            assertFalse(validator.valid(0));

            assertNull(validator.getMessage(1));
            assertNotNull(validator.getMessage(0));

            // if(true) return;

            validator = FloatValidator.isNegative();
            assertTrue(validator.valid(-1));
            assertFalse(validator.valid(0));

            assertNull(validator.getMessage(-1));
            assertNotNull(validator.getMessage(0));

            validator = FloatValidator.isInteger();
            assertTrue(validator.valid(10));
            assertFalse(validator.valid((float) 5.5));

            // if(true) return;

            assertNull(validator.getMessage(10));
            assertNotNull(validator.getMessage((float) 5.5));

            validator.setEpsilon((float) 0.1);
            assertTrue("9.95 is an integer, if tolerance is 0.1", validator.valid((float) 9.95));

            assertNull("9.95 is an integer, if tolerance is 0.1", validator.getMessage((float) 9.95));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Float Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests DoubleValidator.
     */
    public void testDoubleValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            DoubleValidator validator = DoubleValidator.greaterThan(7);
            assertTrue(validator.valid(8));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(8));
            assertNotNull(validator.getMessage(7));

            validator = DoubleValidator.greaterThanOrEqualTo(7);
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(6));

            assertNull("Message for 7>=7 should be null", validator.getMessage(7));
            assertNotNull("Message for 6>=7 should not be null", validator.getMessage(6));

            validator = DoubleValidator.lessThan(7);
            assertTrue(validator.valid(6));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(6));
            assertNotNull(validator.getMessage(7));

            validator = DoubleValidator.lessThanOrEqualTo(7);
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(8));

            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(8));

            validator = DoubleValidator.inRange(5, 7);
            assertTrue(validator.valid(5));
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(4));
            assertFalse(validator.valid(8));

            assertNull("message for 5 in [5,7] should be null!", validator.getMessage(5));
            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(4));
            assertNotNull(validator.getMessage(8));

            validator = DoubleValidator.inExclusiveRange(5, 7);
            assertTrue(validator.valid(6));
            assertFalse(validator.valid(5));
            assertFalse(validator.valid(7));

            assertNull(validator.getMessage(6));
            assertNotNull(validator.getMessage(5));
            assertNotNull(validator.getMessage(7));

            validator = DoubleValidator.isPositive();
            assertTrue(validator.valid(1));
            assertFalse(validator.valid(0));

            assertNull(validator.getMessage(1));
            assertNotNull(validator.getMessage(0));

            validator = DoubleValidator.isNegative();
            assertTrue(validator.valid(-1));
            assertFalse(validator.valid(0));

            assertNull(validator.getMessage(-1));
            assertNotNull(validator.getMessage(0));

            validator = DoubleValidator.isInteger();
            assertTrue(validator.valid(10));
            assertFalse(validator.valid(5.5));

            assertNull(validator.getMessage(10));
            assertNotNull(validator.getMessage(5.5));

            validator.setEpsilon(0.1);
            assertTrue("9.95 is an integer, if tolerance is 0.1", validator.valid(9.95));

            assertNull("9.95 is an integer, if tolerance is 0.1", validator.getMessage(9.95));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Float Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests CharacterValidator.
     */
    public void testCharacterValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            CharacterValidator validator = CharacterValidator.isDigit();
            assertTrue(validator.valid('0'));
            assertFalse(validator.valid('a'));

            assertNull(validator.getMessage('0'));
            assertNotNull(validator.getMessage('a'));

            validator = CharacterValidator.isLetter();
            assertTrue(validator.valid('a'));
            assertFalse(validator.valid('0'));

            assertNull(validator.getMessage('a'));
            assertNotNull(validator.getMessage('0'));

            validator = CharacterValidator.isLetterOrDigit();
            assertTrue(validator.valid('a'));
            assertTrue(validator.valid('0'));
            assertFalse(validator.valid(' '));

            assertNull(validator.getMessage('a'));
            assertNull(validator.getMessage('0'));
            assertNotNull(validator.getMessage(' '));

            validator = CharacterValidator.isUpperCase();
            assertTrue(validator.valid('A'));
            assertFalse(validator.valid('a'));

            assertNull(validator.getMessage('A'));
            assertNotNull(validator.getMessage('a'));

            validator = CharacterValidator.isLowerCase();
            assertTrue(validator.valid('a'));
            assertFalse(validator.valid('A'));

            assertNull(validator.getMessage('a'));
            assertNotNull(validator.getMessage('A'));

            validator = CharacterValidator.isWhitespace();
            assertTrue(validator.valid(' '));
            assertFalse(validator.valid('a'));

            assertNull(validator.getMessage(' '));
            assertNotNull(validator.getMessage('a'));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Float Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests StringValidator.
     */
    public void testStringValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            StringValidator validator = StringValidator.startsWith("foo");
            assertTrue(validator.valid("foobar"));
            assertFalse(validator.valid("barfoo"));

            assertNull(validator.getMessage("foobar"));
            assertNotNull(validator.getMessage("barfoo"));

            validator = StringValidator.endsWith("foo");
            assertTrue(validator.valid("barfoo"));
            assertFalse(validator.valid("foobar"));

            assertNull(validator.getMessage("barfoo"));
            assertNotNull(validator.getMessage("foobar"));

            validator = StringValidator.containsSubstring("foo");
            assertTrue(validator.valid("sfoos"));
            assertFalse(validator.valid("fsoso"));

            assertNull(validator.getMessage("sfoos"));
            assertNotNull(validator.getMessage("fsoso"));

            validator = StringValidator.hasLength(IntegerValidator.inRange(5, 7));
            assertTrue(validator.valid("       "));
            assertFalse(validator.valid("   "));

            assertNull(validator.getMessage("       "));
            assertNotNull(validator.getMessage("   "));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Float Validator time cost:" + (end - start) + "ms");
    }

}
