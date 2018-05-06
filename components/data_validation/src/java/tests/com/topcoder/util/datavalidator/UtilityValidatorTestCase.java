/*
 * TCS Data Validation
 *
 * UtilityValidatorTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import com.topcoder.util.datavalidator.*;
import junit.framework.TestCase;

/**
 * This tests utility validators.
 *
 * @author WishingBone
 * @author zimmy
 * @version 1.0
 */
public class UtilityValidatorTestCase extends TestCase {

    /**
     * Set up testing environment.
     */
    public void setUp() {
    }

    /**
     * Tear down testing environment.
     */
    public void tearDown() {
    }

    /**
     * Tests IntegerValidator.
     */
    public void testIntegerValidator() {
        IntegerValidator validator = IntegerValidator.greaterThan(7);
        assertTrue(validator.valid(8));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(8));
        assertEquals(validator.getMessage(7), "not greater than 7");

        validator = IntegerValidator.greaterThanOrEqualTo(7);
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(6));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(6), "not greater than or equal to 7");

        validator = IntegerValidator.lessThan(7);
        assertTrue(validator.valid(6));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(6));
        assertEquals(validator.getMessage(7), "not less than 7");

        validator = IntegerValidator.lessThanOrEqualTo(7);
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(8), "not less than or equal to 7");

        validator = IntegerValidator.inRange(5, 7);
        assertTrue(validator.valid(5));
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(4));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(5));
        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(4), "not greater than or equal to 5");
        assertEquals(validator.getMessage(8), "not less than or equal to 7");

        validator = IntegerValidator.inExclusiveRange(5, 7);
        assertTrue(validator.valid(6));
        assertFalse(validator.valid(5));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(6));
        assertEquals(validator.getMessage(5), "not greater than 5");
        assertEquals(validator.getMessage(7), "not less than 7");

        validator = IntegerValidator.isPositive();
        assertTrue(validator.valid(1));
        assertFalse(validator.valid(0));

        assertNull(validator.getMessage(1));
        assertEquals(validator.getMessage(0), "not greater than 0");

        validator = IntegerValidator.isNegative();
        assertTrue(validator.valid(-1));
        assertFalse(validator.valid(0));

        assertNull(validator.getMessage(-1));
        assertEquals(validator.getMessage(0), "not less than 0");

        validator = IntegerValidator.isOdd();
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(8), "not odd");

        validator = IntegerValidator.isEven();
        assertTrue(validator.valid(8));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(8));
        assertEquals(validator.getMessage(7), "not even");
    }

    /**
     * Tests LongValidator.
     */
    public void testLongValidator() {
        LongValidator validator = LongValidator.greaterThan(7);
        assertTrue(validator.valid(8));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(8));
        assertEquals(validator.getMessage(7), "not greater than 7");

        validator = LongValidator.greaterThanOrEqualTo(7);
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(6));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(6), "not greater than or equal to 7");

        validator = LongValidator.lessThan(7);
        assertTrue(validator.valid(6));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(6));
        assertEquals(validator.getMessage(7), "not less than 7");

        validator = LongValidator.lessThanOrEqualTo(7);
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(8), "not less than or equal to 7");

        validator = LongValidator.inRange(5, 7);
        assertTrue(validator.valid(5));
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(4));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(5));
        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(4), "not greater than or equal to 5");
        assertEquals(validator.getMessage(8), "not less than or equal to 7");

        validator = LongValidator.inExclusiveRange(5, 7);
        assertTrue(validator.valid(6));
        assertFalse(validator.valid(5));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(6));
        assertEquals(validator.getMessage(5), "not greater than 5");
        assertEquals(validator.getMessage(7), "not less than 7");

        validator = LongValidator.isPositive();
        assertTrue(validator.valid(1));
        assertFalse(validator.valid(0));

        assertNull(validator.getMessage(1));
        assertEquals(validator.getMessage(0), "not greater than 0");

        validator = LongValidator.isNegative();
        assertTrue(validator.valid(-1));
        assertFalse(validator.valid(0));

        assertNull(validator.getMessage(-1));
        assertEquals(validator.getMessage(0), "not less than 0");

        validator = LongValidator.isOdd();
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(8), "not odd");

        validator = LongValidator.isEven();
        assertTrue(validator.valid(8));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(8));
        assertEquals(validator.getMessage(7), "not even");
    }

    /**
     * Tests ByteValidator.
     */
    public void testByteValidator() {
        ByteValidator validator = ByteValidator.greaterThan((byte) 7);
        assertTrue(validator.valid((byte) 8));
        assertFalse(validator.valid((byte) 7));

        assertNull(validator.getMessage((byte) 8));
        assertEquals(validator.getMessage((byte) 7), "not greater than 7");

        validator = ByteValidator.greaterThanOrEqualTo((byte) 7);
        assertTrue(validator.valid((byte) 7));
        assertFalse(validator.valid((byte) 6));

        assertNull(validator.getMessage((byte) 7));
        assertEquals(validator.getMessage((byte) 6),
                "not greater than or equal to 7");

        validator = ByteValidator.lessThan((byte) 7);
        assertTrue(validator.valid((byte) 6));
        assertFalse(validator.valid((byte) 7));

        assertNull(validator.getMessage((byte) 6));
        assertEquals(validator.getMessage((byte) 7), "not less than 7");

        validator = ByteValidator.lessThanOrEqualTo((byte) 7);
        assertTrue(validator.valid((byte) 7));
        assertFalse(validator.valid((byte) 8));

        assertNull(validator.getMessage((byte) 7));
        assertEquals(validator.getMessage((byte) 8), "not less than or equal to 7");

        validator = ByteValidator.inRange((byte) 5, (byte) 7);
        assertTrue(validator.valid((byte) 5));
        assertTrue(validator.valid((byte) 7));
        assertFalse(validator.valid((byte) 4));
        assertFalse(validator.valid((byte) 8));

        assertNull(validator.getMessage((byte) 5));
        assertNull(validator.getMessage((byte) 7));
        assertEquals(validator.getMessage((byte) 4),
                "not greater than or equal to 5");
        assertEquals(validator.getMessage((byte) 8), "not less than or equal to 7");

        validator = ByteValidator.inExclusiveRange((byte) 5, (byte) 7);
        assertTrue(validator.valid((byte) 6));
        assertFalse(validator.valid((byte) 5));
        assertFalse(validator.valid((byte) 7));

        assertNull(validator.getMessage((byte) 6));
        assertEquals(validator.getMessage((byte) 5), "not greater than 5");
        assertEquals(validator.getMessage((byte) 7), "not less than 7");
    }

    /**
     * Tests ShortValidator.
     */
    public void testShortValidator() {
        ShortValidator validator = ShortValidator.greaterThan((short) 7);
        assertTrue(validator.valid((short) 8));
        assertFalse(validator.valid((short) 7));

        assertNull(validator.getMessage((short) 8));
        assertEquals(validator.getMessage((short) 7), "not greater than 7");

        validator = ShortValidator.greaterThanOrEqualTo((short) 7);
        assertTrue(validator.valid((short) 7));
        assertFalse(validator.valid((short) 6));

        assertNull(validator.getMessage((short) 7));
        assertEquals(validator.getMessage((short) 6),
                "not greater than or equal to 7");

        validator = ShortValidator.lessThan((short) 7);
        assertTrue(validator.valid((short) 6));
        assertFalse(validator.valid((short) 7));

        assertNull(validator.getMessage((short) 6));
        assertEquals(validator.getMessage((short) 7), "not less than 7");

        validator = ShortValidator.lessThanOrEqualTo((short) 7);
        assertTrue(validator.valid((short) 7));
        assertFalse(validator.valid((short) 8));

        assertNull(validator.getMessage((short) 7));
        assertEquals(validator.getMessage((short) 8), "not less than or equal to 7");

        validator = ShortValidator.inRange((short) 5, (short) 7);
        assertTrue(validator.valid((short) 5));
        assertTrue(validator.valid((short) 7));
        assertFalse(validator.valid((short) 4));
        assertFalse(validator.valid((short) 8));

        assertNull(validator.getMessage((short) 5));
        assertNull(validator.getMessage((short) 7));
        assertEquals(validator.getMessage((short) 4),
                "not greater than or equal to 5");
        assertEquals(validator.getMessage((short) 8), "not less than or equal to 7");

        validator = ShortValidator.inExclusiveRange((short) 5, (short) 7);
        assertTrue(validator.valid((short) 6));
        assertFalse(validator.valid((short) 5));
        assertFalse(validator.valid((short) 7));

        assertNull(validator.getMessage((short) 6));
        assertEquals(validator.getMessage((short) 5), "not greater than 5");
        assertEquals(validator.getMessage((short) 7), "not less than 7");
    }

    /**
     * Tests FloatValidator.
     */
    public void testFloatValidator() {
        FloatValidator validator = FloatValidator.greaterThan(7);
        assertTrue(validator.valid(8));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(8));
        assertEquals(validator.getMessage(7), "not greater than 7.0");

        validator = FloatValidator.greaterThanOrEqualTo(7);
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(6));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(6), "not greater than or equal to 7.0");

        validator = FloatValidator.lessThan(7);
        assertTrue(validator.valid(6));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(6));
        assertEquals(validator.getMessage(7), "not less than 7.0");

        validator = FloatValidator.lessThanOrEqualTo(7);
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(8), "not less than or equal to 7.0");

        validator = FloatValidator.inRange(5, 7);
        assertTrue(validator.valid(5));
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(4));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(5));
        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(4), "not greater than or equal to 5.0");
        assertEquals(validator.getMessage(8), "not less than or equal to 7.0");

        validator = FloatValidator.inExclusiveRange(5, 7);
        assertTrue(validator.valid(6));
        assertFalse(validator.valid(5));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(6));
        assertEquals(validator.getMessage(5), "not greater than 5.0");
        assertEquals(validator.getMessage(7), "not less than 7.0");

        validator = FloatValidator.isPositive();
        assertTrue(validator.valid(1));
        assertFalse(validator.valid(0));

        assertNull(validator.getMessage(1));
        assertEquals(validator.getMessage(0), "not greater than 0.0");

        validator = FloatValidator.isNegative();
        assertTrue(validator.valid(-1));
        assertFalse(validator.valid(0));

        assertNull(validator.getMessage(-1));
        assertEquals(validator.getMessage(0), "not less than 0.0");

        validator = FloatValidator.isInteger();
        assertTrue(validator.valid(10));
        assertFalse(validator.valid((float) 5.5));

        assertNull(validator.getMessage(10));
        assertEquals(validator.getMessage((float) 5.5), "not an integer");

        validator.setEpsilon((float) 0.1);
        assertTrue(validator.valid((float) 9.95));

        assertNull(validator.getMessage((float) 9.95));
    }

    /**
     * Tests DoubleValidator.
     */
    public void testDoubleValidator() {
        DoubleValidator validator = DoubleValidator.greaterThan(7);
        assertTrue(validator.valid(8));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(8));
        assertEquals(validator.getMessage(7), "not greater than 7.0");

        validator = DoubleValidator.greaterThanOrEqualTo(7);
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(6));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(6), "not greater than or equal to 7.0");

        validator = DoubleValidator.lessThan(7);
        assertTrue(validator.valid(6));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(6));
        assertEquals(validator.getMessage(7), "not less than 7.0");

        validator = DoubleValidator.lessThanOrEqualTo(7);
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(8), "not less than or equal to 7.0");

        validator = DoubleValidator.inRange(5, 7);
        assertTrue(validator.valid(5));
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(4));
        assertFalse(validator.valid(8));

        assertNull(validator.getMessage(5));
        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(4), "not greater than or equal to 5.0");
        assertEquals(validator.getMessage(8), "not less than or equal to 7.0");

        validator = DoubleValidator.inExclusiveRange(5, 7);
        assertTrue(validator.valid(6));
        assertFalse(validator.valid(5));
        assertFalse(validator.valid(7));

        assertNull(validator.getMessage(6));
        assertEquals(validator.getMessage(5), "not greater than 5.0");
        assertEquals(validator.getMessage(7), "not less than 7.0");

        validator = DoubleValidator.isPositive();
        assertTrue(validator.valid(1));
        assertFalse(validator.valid(0));

        assertNull(validator.getMessage(1));
        assertEquals(validator.getMessage(0), "not greater than 0.0");

        validator = DoubleValidator.isNegative();
        assertTrue(validator.valid(-1));
        assertFalse(validator.valid(0));

        assertNull(validator.getMessage(-1));
        assertEquals(validator.getMessage(0), "not less than 0.0");

        validator = DoubleValidator.isInteger();
        assertTrue(validator.valid(10));
        assertFalse(validator.valid(5.5));

        assertNull(validator.getMessage(10));
        assertEquals(validator.getMessage(5.5), "not an integer");

        validator.setEpsilon(0.1);
        assertTrue(validator.valid(9.95));

        assertNull(validator.getMessage(9.95));
    }

    /**
     * Tests CharacterValidator.
     */
    public void testCharacterValidator() {
        CharacterValidator validator = CharacterValidator.isDigit();
        assertTrue(validator.valid('0'));
        assertFalse(validator.valid('a'));

        assertNull(validator.getMessage('0'));
        assertEquals(validator.getMessage('a'), "not a digit");

        validator = CharacterValidator.isLetter();
        assertTrue(validator.valid('a'));
        assertFalse(validator.valid('0'));

        assertNull(validator.getMessage('a'));
        assertEquals(validator.getMessage('0'), "not a letter");

        validator = CharacterValidator.isLetterOrDigit();
        assertTrue(validator.valid('a'));
        assertTrue(validator.valid('0'));
        assertFalse(validator.valid(' '));

        assertNull(validator.getMessage('a'));
        assertNull(validator.getMessage('0'));
        assertEquals(validator.getMessage(' '), "not a letter AND not a digit");

        validator = CharacterValidator.isUpperCase();
        assertTrue(validator.valid('A'));
        assertFalse(validator.valid('a'));

        assertNull(validator.getMessage('A'));
        assertEquals(validator.getMessage('a'), "not an upper case letter");

        validator = CharacterValidator.isLowerCase();
        assertTrue(validator.valid('a'));
        assertFalse(validator.valid('A'));

        assertNull(validator.getMessage('a'));
        assertEquals(validator.getMessage('A'), "not a lower case letter");

        validator = CharacterValidator.isWhitespace();
        assertTrue(validator.valid(' '));
        assertFalse(validator.valid('a'));

        assertNull(validator.getMessage(' '));
        assertEquals(validator.getMessage('a'), "not a whitespace");
    }

    /**
     * Tests StringValidator.
     */
    public void testStringValidator() {
        StringValidator validator = StringValidator.startsWith("foo");
        assertTrue(validator.valid("foobar"));
        assertFalse(validator.valid("barfoo"));

        assertNull(validator.getMessage("foobar"));
        assertEquals(validator.getMessage("barfoo"), "does not start with \"foo\"");

        validator = StringValidator.endsWith("foo");
        assertTrue(validator.valid("barfoo"));
        assertFalse(validator.valid("foobar"));

        assertNull(validator.getMessage("barfoo"));
        assertEquals(validator.getMessage("foobar"), "does not end with \"foo\"");

        validator = StringValidator.containsSubstring("foo");
        assertTrue(validator.valid("sfoos"));
        assertFalse(validator.valid("fsoso"));

        assertNull(validator.getMessage("sfoos"));
        assertEquals(validator.getMessage("fsoso"), "does not contain \"foo\"");

        validator = StringValidator.hasLength(IntegerValidator.inRange(5, 7));
        assertTrue(validator.valid("       "));
        assertFalse(validator.valid("   "));

        assertNull(validator.getMessage("       "));
        assertEquals(validator.getMessage("   "),
                "string length not greater than or equal to 5");
    }

}
