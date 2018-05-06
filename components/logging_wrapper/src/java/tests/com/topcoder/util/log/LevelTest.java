/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link Level} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class LevelTest extends TestCase {
    /**
     * <p>
     * Represents the human-readable representations of all the levels, whose sequence must be the same as the LEVELS
     * array.
     * </p>
     */
    private static final String[] LEVEL_NAMES = new String[] {
        "OFF", "FINEST", "TRACE", "DEBUG", "CONFIG", "INFO", "WARN", "ERROR", "FATAL", "ALL"
    };

    /**
     * <p>
     * Represents the constant int values of all the levels, whose sequence must be the same as the LEVELS
     * array.
     * </p>
     */
    private static final int[] LEVEL_VALUES = new int[] {
        0, 100, 200, 300, 400, 500, 600, 700, 800, 900
    };

    /**
     * <p>
     * Represents an array containing all the levels.
     * </p>
     */
    private static final Level[] LEVELS = new Level[] {
        Level.OFF, Level.FINEST, Level.TRACE, Level.DEBUG, Level.CONFIG, Level.INFO, Level.WARN, Level.ERROR,
        Level.FATAL, Level.ALL
    };

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(LevelTest.class);
    }

    /**
     * <p>
     * Tests method for hashCode() for accuracy.
     * </p>
     *
     * <p>
     * Verify that hashCode() is correct.
     * </p>
     */
    public void testHashCode() {
        for (int i = 0; i < LEVELS.length; i++) {
            assertEquals("The hash code of " + LEVELS[i] + " should be " + LEVEL_VALUES[i],
                    LEVEL_VALUES[i], LEVELS[i].hashCode());
        }
    }


    /**
     * <p>
     * Tests method for intValue() for accuracy.
     * </p>
     *
     * <p>
     * Verify that intValue() is correct.
     * </p>
     */
    public void testIntValue() {
        for (int i = 0; i < LEVELS.length; i++) {
            assertEquals("The int value of " + LEVELS[i] + " should be " + LEVEL_VALUES[i],
                    LEVEL_VALUES[i], LEVELS[i].intValue());
        }
    }

    /**
     * <p>
     * Tests method for equals(Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that equals(Object) is correct.
     * </p>
     */
    public void testEqualsObject() {
        for (int i = 0; i < LEVELS.length; i++) {
            assertFalse("The level should not equal to null.", LEVELS[i].equals(null));
            assertTrue("The level should equal to itself.", LEVELS[i].equals(LEVELS[i]));
            assertFalse("The level should equal to itself.", LEVELS[i].equals(LEVELS[(i + 1) % LEVELS.length]));
        }
    }

    /**
     * <p>
     * Tests method for toString() for accuracy.
     * </p>
     *
     * <p>
     * Verify that toString() is correct.
     * </p>
     */
    public void testToString() {
        for (int i = 0; i < LEVELS.length; i++) {
            assertEquals("The string representing of " + LEVELS[i] + " should be " + LEVEL_NAMES[i],
                    LEVEL_NAMES[i], LEVELS[i].toString());
        }
    }

    /**
     * <p>
     * Tests method for parseLevel(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the string is the name of level and verifies that parseLevel(String) is correct.
     * </p>
     */
    public void testParseLevel() {
        for (int i = 0; i < LEVELS.length; i++) {
            assertEquals("The level whose string representing is " + LEVEL_NAMES[i] + " should be "
                    + LEVELS[i], LEVELS[i], Level.parseLevel(LEVEL_NAMES[i]));
        }
    }

    /**
     * <p>
     * Tests method for parseLevel(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the string is with leading and trailing whitespace and verifies that parseLevel(String)
     *  is correct.
     * </p>
     */
    public void testParseLevelWithTrimmed() {
        assertEquals("The level should be FATAL.", Level.FATAL, Level.parseLevel("  FATAL "));
    }

    /**
     * <p>
     * Tests method for parseLevel(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the string is lower case and verifies that parseLevel(String)
     *  is correct.
     * </p>
     */
    public void testParseLevelWithLowerCase() {
        assertEquals("The level should be FATAL.", Level.FATAL, Level.parseLevel("fatal"));
    }

    /**
     * <p>
     * Tests method for parseLevel(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the string is null and verifies null will be returned.
     * </p>
     */
    public void testParseLevelWithNullLevel() {
        assertNull("The result should be null.", Level.parseLevel(null));
    }

    /**
     * <p>
     * Tests method for parseLevel(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the string is empty and verifies null will be returned.
     * </p>
     */
    public void testParseLevelWithEmptyLevel() {
        assertNull("The result should be null.", Level.parseLevel("  "));
    }

    /**
     * <p>
     * Tests method for parseLevel(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the string is the int value of level and verifies that parseLevel(String) is correct.
     * </p>
     */
    public void testParseLevelWithIntegerString() {
        for (int i = 0; i < LEVELS.length; i++) {
            assertEquals("The level whose integer value is " + LEVEL_VALUES[i] + " should be "
                    + LEVELS[i], LEVELS[i], Level.parseLevel(String.valueOf(LEVEL_VALUES[i])));
        }
    }

    /**
     * <p>
     * Tests method for parseLevel(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the string is lower case and verifies that parseLevel(String)
     *  is correct.
     * </p>
     */
    public void testParseLevelWithTrimmedInteger() {
        assertEquals("The level should be FATAL.", Level.FATAL, Level.parseLevel(" 800 "));
    }

    /**
     * <p>
     * Tests method for parseLevel(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the string is not found of level and verifies that parseLevel(String) is correct and
     * return null.
     * </p>
     */
    public void testParseLevelWithIllegalString() {
        assertNull("The level should be null.", Level.parseLevel("Not found"));
    }
}
