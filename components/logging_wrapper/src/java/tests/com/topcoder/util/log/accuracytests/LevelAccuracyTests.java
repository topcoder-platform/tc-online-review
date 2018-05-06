/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.Level;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>Level</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class LevelAccuracyTests extends TestCase {

    /**
     * <p>
     * Tests that the <code>Level</code> actually represents an enumeration.
     * </p>
     */
    public void testEnumeration() {
        assertEquals(Level.ALL, Level.ALL);
        assertEquals(Level.FATAL, Level.FATAL);
        assertEquals(Level.ERROR, Level.ERROR);
        assertEquals(Level.WARN, Level.WARN);
        assertEquals(Level.INFO, Level.INFO);
        assertEquals(Level.CONFIG, Level.CONFIG);
        assertEquals(Level.DEBUG, Level.DEBUG);
        assertEquals(Level.TRACE, Level.TRACE);
        assertEquals(Level.FINEST, Level.FINEST);
        assertEquals(Level.OFF, Level.OFF);
    }

    /**
     * <p>
     * Tests the method <code>intValue()</code>.
     * </p>
     */
    public void testIntValue() {
        assertTrue(Level.ALL.intValue() == 900);
        assertTrue(Level.FATAL.intValue() == 800);
        assertTrue(Level.ERROR.intValue() == 700);
        assertTrue(Level.WARN.intValue() == 600);
        assertTrue(Level.INFO.intValue() == 500);
        assertTrue(Level.CONFIG.intValue() == 400);
        assertTrue(Level.DEBUG.intValue() == 300);
        assertTrue(Level.TRACE.intValue() == 200);
        assertTrue(Level.FINEST.intValue() == 100);
        assertTrue(Level.OFF.intValue() == 0);
    }

    /**
     * <p>
     * Tests the method <code>hashCode()</code>.
     * </p>
     */
    public void testHashCode() {
        assertTrue(Level.ALL.hashCode() == 900);
        assertTrue(Level.FATAL.hashCode() == 800);
        assertTrue(Level.ERROR.hashCode() == 700);
        assertTrue(Level.WARN.hashCode() == 600);
        assertTrue(Level.INFO.hashCode() == 500);
        assertTrue(Level.CONFIG.hashCode() == 400);
        assertTrue(Level.DEBUG.hashCode() == 300);
        assertTrue(Level.TRACE.hashCode() == 200);
        assertTrue(Level.FINEST.hashCode() == 100);
        assertTrue(Level.OFF.hashCode() == 0);
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * With some special comparison.
     * </p>
     */
    public void testEquals1() {
        assertFalse(Level.ALL.equals(null));
        assertFalse(Level.ALL.equals("not level object"));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.All.
     * </p>
     */
    public void testEquals2() {
        assertTrue(Level.ALL.equals(Level.ALL));
        assertFalse(Level.ALL.equals(Level.FATAL));
        assertFalse(Level.ALL.equals(Level.ERROR));
        assertFalse(Level.ALL.equals(Level.WARN));
        assertFalse(Level.ALL.equals(Level.INFO));
        assertFalse(Level.ALL.equals(Level.CONFIG));
        assertFalse(Level.ALL.equals(Level.DEBUG));
        assertFalse(Level.ALL.equals(Level.TRACE));
        assertFalse(Level.ALL.equals(Level.FINEST));
        assertFalse(Level.ALL.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.FATAL.
     * </p>
     */
    public void testEquals3() {
        assertFalse(Level.FATAL.equals(Level.ALL));
        assertTrue(Level.FATAL.equals(Level.FATAL));
        assertFalse(Level.FATAL.equals(Level.ERROR));
        assertFalse(Level.FATAL.equals(Level.WARN));
        assertFalse(Level.FATAL.equals(Level.INFO));
        assertFalse(Level.FATAL.equals(Level.CONFIG));
        assertFalse(Level.FATAL.equals(Level.DEBUG));
        assertFalse(Level.FATAL.equals(Level.TRACE));
        assertFalse(Level.FATAL.equals(Level.FINEST));
        assertFalse(Level.FATAL.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.ERROR.
     * </p>
     */
    public void testEquals4() {
        assertFalse(Level.ERROR.equals(Level.ALL));
        assertFalse(Level.ERROR.equals(Level.FATAL));
        assertTrue(Level.ERROR.equals(Level.ERROR));
        assertFalse(Level.ERROR.equals(Level.WARN));
        assertFalse(Level.ERROR.equals(Level.INFO));
        assertFalse(Level.ERROR.equals(Level.CONFIG));
        assertFalse(Level.ERROR.equals(Level.DEBUG));
        assertFalse(Level.ERROR.equals(Level.TRACE));
        assertFalse(Level.ERROR.equals(Level.FINEST));
        assertFalse(Level.ERROR.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.WARN.
     * </p>
     */
    public void testEquals5() {
        assertFalse(Level.WARN.equals(Level.ALL));
        assertFalse(Level.WARN.equals(Level.FATAL));
        assertFalse(Level.WARN.equals(Level.ERROR));
        assertTrue(Level.WARN.equals(Level.WARN));
        assertFalse(Level.WARN.equals(Level.INFO));
        assertFalse(Level.WARN.equals(Level.CONFIG));
        assertFalse(Level.WARN.equals(Level.DEBUG));
        assertFalse(Level.WARN.equals(Level.TRACE));
        assertFalse(Level.WARN.equals(Level.FINEST));
        assertFalse(Level.WARN.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.INFO.
     * </p>
     */
    public void testEquals6() {
        assertFalse(Level.INFO.equals(Level.ALL));
        assertFalse(Level.INFO.equals(Level.FATAL));
        assertFalse(Level.INFO.equals(Level.ERROR));
        assertFalse(Level.INFO.equals(Level.WARN));
        assertTrue(Level.INFO.equals(Level.INFO));
        assertFalse(Level.INFO.equals(Level.CONFIG));
        assertFalse(Level.INFO.equals(Level.DEBUG));
        assertFalse(Level.INFO.equals(Level.TRACE));
        assertFalse(Level.INFO.equals(Level.FINEST));
        assertFalse(Level.INFO.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.CONFIG.
     * </p>
     */
    public void testEquals7() {
        assertFalse(Level.CONFIG.equals(Level.ALL));
        assertFalse(Level.CONFIG.equals(Level.FATAL));
        assertFalse(Level.CONFIG.equals(Level.ERROR));
        assertFalse(Level.CONFIG.equals(Level.WARN));
        assertFalse(Level.CONFIG.equals(Level.INFO));
        assertTrue(Level.CONFIG.equals(Level.CONFIG));
        assertFalse(Level.CONFIG.equals(Level.DEBUG));
        assertFalse(Level.CONFIG.equals(Level.TRACE));
        assertFalse(Level.CONFIG.equals(Level.FINEST));
        assertFalse(Level.CONFIG.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.DEBUG.
     * </p>
     */
    public void testEquals8() {
        assertFalse(Level.DEBUG.equals(Level.ALL));
        assertFalse(Level.DEBUG.equals(Level.FATAL));
        assertFalse(Level.DEBUG.equals(Level.ERROR));
        assertFalse(Level.DEBUG.equals(Level.WARN));
        assertFalse(Level.DEBUG.equals(Level.INFO));
        assertFalse(Level.DEBUG.equals(Level.CONFIG));
        assertTrue(Level.DEBUG.equals(Level.DEBUG));
        assertFalse(Level.DEBUG.equals(Level.TRACE));
        assertFalse(Level.DEBUG.equals(Level.FINEST));
        assertFalse(Level.DEBUG.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.TRACE.
     * </p>
     */
    public void testEquals9() {
        assertFalse(Level.TRACE.equals(Level.ALL));
        assertFalse(Level.TRACE.equals(Level.FATAL));
        assertFalse(Level.TRACE.equals(Level.ERROR));
        assertFalse(Level.TRACE.equals(Level.WARN));
        assertFalse(Level.TRACE.equals(Level.INFO));
        assertFalse(Level.TRACE.equals(Level.CONFIG));
        assertFalse(Level.TRACE.equals(Level.DEBUG));
        assertTrue(Level.TRACE.equals(Level.TRACE));
        assertFalse(Level.TRACE.equals(Level.FINEST));
        assertFalse(Level.TRACE.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.FINEST.
     * </p>
     */
    public void testEquals10() {
        assertFalse(Level.FINEST.equals(Level.ALL));
        assertFalse(Level.FINEST.equals(Level.FATAL));
        assertFalse(Level.FINEST.equals(Level.ERROR));
        assertFalse(Level.FINEST.equals(Level.WARN));
        assertFalse(Level.FINEST.equals(Level.INFO));
        assertFalse(Level.FINEST.equals(Level.CONFIG));
        assertFalse(Level.FINEST.equals(Level.DEBUG));
        assertFalse(Level.FINEST.equals(Level.TRACE));
        assertTrue(Level.FINEST.equals(Level.FINEST));
        assertFalse(Level.FINEST.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>equals(Object level)</code>.
     * Test Level.OFF.
     * </p>
     */
    public void testEquals11() {
        assertFalse(Level.OFF.equals(Level.ALL));
        assertFalse(Level.OFF.equals(Level.FATAL));
        assertFalse(Level.OFF.equals(Level.ERROR));
        assertFalse(Level.OFF.equals(Level.WARN));
        assertFalse(Level.OFF.equals(Level.INFO));
        assertFalse(Level.OFF.equals(Level.CONFIG));
        assertFalse(Level.OFF.equals(Level.DEBUG));
        assertFalse(Level.OFF.equals(Level.TRACE));
        assertFalse(Level.OFF.equals(Level.FINEST));
        assertTrue(Level.OFF.equals(Level.OFF));
    }

    /**
     * <p>
     * Tests the method <code>toString()</code>.
     * </p>
     */
    public void testToString() {
        assertTrue(Level.ALL.toString().equals("ALL"));
        assertTrue(Level.FATAL.toString().equals("FATAL"));
        assertTrue(Level.ERROR.toString().equals("ERROR"));
        assertTrue(Level.WARN.toString().equals("WARN"));
        assertTrue(Level.INFO.toString().equals("INFO"));
        assertTrue(Level.CONFIG.toString().equals("CONFIG"));
        assertTrue(Level.DEBUG.toString().equals("DEBUG"));
        assertTrue(Level.TRACE.toString().equals("TRACE"));
        assertTrue(Level.FINEST.toString().equals("FINEST"));
        assertTrue(Level.OFF.toString().equals("OFF"));
    }

    /**
     * <p>
     * Tests of method <code>parseLevel(String level)</code>.
     * With valid digit level.
     * Should return the proper value.
     * </p>
     */
    public void testParseLevelValidDigitLevel1() {
        assertEquals(Level.ALL, Level.parseLevel("900"));
        assertEquals(Level.FATAL, Level.parseLevel("800"));
        assertEquals(Level.ERROR, Level.parseLevel("700"));
        assertEquals(Level.WARN, Level.parseLevel("600"));
        assertEquals(Level.INFO, Level.parseLevel("500"));
        assertEquals(Level.CONFIG, Level.parseLevel("400"));
        assertEquals(Level.DEBUG, Level.parseLevel("300"));
        assertEquals(Level.TRACE, Level.parseLevel("200"));
        assertEquals(Level.FINEST, Level.parseLevel("100"));
        assertEquals(Level.OFF, Level.parseLevel("0"));
    }

    /**
     * <p>
     * Tests of method <code>parseLevel(String level)</code>.
     * With valid digit level.
     * Should return the proper value.
     * </p>
     */
    public void testParseLevelValidDigitLevel2() {
        assertEquals(Level.ALL, Level.parseLevel("   900  "));
        assertEquals(Level.FATAL, Level.parseLevel("  800  "));
        assertEquals(Level.ERROR, Level.parseLevel(" \t\n 700"));
        assertEquals(Level.WARN, Level.parseLevel(" 600\r\n"));
        assertEquals(Level.INFO, Level.parseLevel("500\t"));
        assertEquals(Level.CONFIG, Level.parseLevel("\t400"));
        assertEquals(Level.DEBUG, Level.parseLevel("\t300"));
        assertEquals(Level.TRACE, Level.parseLevel("  200"));
        assertEquals(Level.FINEST, Level.parseLevel("100  "));
        assertEquals(Level.OFF, Level.parseLevel("0 "));
    }

    /**
     * <p>
     * Tests of method <code>parseLevel(String level)</code>.
     * With valid string level.
     * Should return the proper value.
     * </p>
     */
    public void testParseLevelValidStringLevel1() {
        assertEquals(Level.ALL, Level.parseLevel("ALL"));
        assertEquals(Level.FATAL, Level.parseLevel("FATAL"));
        assertEquals(Level.ERROR, Level.parseLevel("ERROR"));
        assertEquals(Level.WARN, Level.parseLevel("WARN"));
        assertEquals(Level.INFO, Level.parseLevel("INFO"));
        assertEquals(Level.CONFIG, Level.parseLevel("CONFIG"));
        assertEquals(Level.DEBUG, Level.parseLevel("DEBUG"));
        assertEquals(Level.TRACE, Level.parseLevel("TRACE"));
        assertEquals(Level.FINEST, Level.parseLevel("FINEST"));
        assertEquals(Level.OFF, Level.parseLevel("OFF"));
    }

    /**
     * <p>
     * Tests of method <code>parseLevel(String level)</code>.
     * With valid string level.
     * Should return the proper value.
     * </p>
     */
    public void testParseLevelValidStringLevel2() {
        assertEquals(Level.ALL, Level.parseLevel("   ALL"));
        assertEquals(Level.FATAL, Level.parseLevel("  FATAL"));
        assertEquals(Level.ERROR, Level.parseLevel("  ERROR"));
        assertEquals(Level.WARN, Level.parseLevel("  WARN"));
        assertEquals(Level.INFO, Level.parseLevel("  INFO"));
        assertEquals(Level.CONFIG, Level.parseLevel("  CONFIG"));
        assertEquals(Level.DEBUG, Level.parseLevel("  DEBUG"));
        assertEquals(Level.TRACE, Level.parseLevel(" TRACE"));
        assertEquals(Level.FINEST, Level.parseLevel("\tFINEST"));
        assertEquals(Level.OFF, Level.parseLevel("OFF\n\r"));
    }

}
