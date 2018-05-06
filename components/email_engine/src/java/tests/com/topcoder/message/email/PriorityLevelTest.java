/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)PriorityLevelTest.java
 */
package com.topcoder.message.email;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>The unit test for <code>PriorityLevel</code> class.</p>
 *
 * @author  smell
 * @version 3.0
 */
public class PriorityLevelTest extends TestCase {

    /**
     * Tests PriorityLevel for no priority.
     */
    public void testNoPriority() {
        assertEquals("The name is not valid.", "none", PriorityLevel.NONE.getName());
        assertEquals("The return value of toString() is invalid", "none", PriorityLevel.NONE.toString());
    }

    /**
     * Tests PriorityLevel for highest priority.
     */
    public void testHighestPriority() {
        assertEquals("The name is not valid.", "highest", PriorityLevel.HIGHEST.getName());
        assertEquals("The return value of toString() is invalid", "highest", PriorityLevel.HIGHEST.toString());
    }

    /**
     * Tests PriorityLevel for high priority.
     */
    public void testHighPriority() {
        assertEquals("The name is not valid.", "high", PriorityLevel.HIGH.getName());
        assertEquals("The return value of toString() is invalid", "high", PriorityLevel.HIGH.toString());
    }

    /**
     * Tests PriorityLevel for normal priority.
     */
    public void testNormalPriority() {
        assertEquals("The name is not valid.", "normal", PriorityLevel.NORMAL.getName());
        assertEquals("The return value of toString() is invalid", "normal", PriorityLevel.NORMAL.toString());
    }

    /**
     * Tests PriorityLevel for low priority.
     */
    public void testLowPriority() {
        assertEquals("The name is not valid.", "low", PriorityLevel.LOW.getName());
        assertEquals("The return value of toString() is invalid", "low", PriorityLevel.LOW.toString());
    }

    /**
     * Tests PriorityLevel for lowest priority.
     */
    public void testLowestPriority() {
        assertEquals("The name is not valid.", "lowest", PriorityLevel.LOWEST.getName());
        assertEquals("The return value of toString() is invalid", "lowest", PriorityLevel.LOWEST.toString());
    }

    /**
     * Returns suite containing the tests.
     *
     * @return suite to run the test
     */
    public static Test suite() {
        return new TestSuite(PriorityLevelTest.class);
    }
}
