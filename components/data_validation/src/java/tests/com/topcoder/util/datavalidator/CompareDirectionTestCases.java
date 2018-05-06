/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>CompareDirection</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class CompareDirectionTestCases extends TestCase {
    /**
     * Test method for 'CompareDirection.toString()'.
     */
    public void testToString() {
        assertEquals("Test method for 'CompareDirection.toString()' failed.", "equal to",
            CompareDirection.EQUAL.toString());
        assertEquals("Test method for 'CompareDirection.toString()' failed.", "greater than",
            CompareDirection.GREATER.toString());
        assertEquals("Test method for 'CompareDirection.toString()' failed.", "greater than or equal to",
            CompareDirection.GREATER_OR_EQUAL.toString());
        assertEquals("Test method for 'CompareDirection.toString()' failed.", "less than",
            CompareDirection.LESS.toString());
        assertEquals("Test method for 'CompareDirection.toString()' failed.", "less than or equal to",
            CompareDirection.LESS_OR_EQUAL.toString());
    }
}
