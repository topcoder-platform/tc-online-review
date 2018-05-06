/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import junit.framework.TestCase;
/**
 * The unit test of <code>AlwaysTrueValidator</code>.
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class AlwaysTrueValidatorTests extends TestCase {
    /**
     * The instance of AlwaysTrueValidator used to test.
     */
    private AlwaysTrueValidator instance = null;
    /**
     * The setUp.
     */
    protected void setUp() {
        instance = new AlwaysTrueValidator();
    }
    /**
     * Test the default constructor.
     *
     */
    public void testconstructor() {
        assertNotNull("can not construct the AlwaysTrueValidator.", new AlwaysTrueValidator());
    }
    /**
     * Test the method getMessage.
     *
     */
    public void testgetMessage_withObject() {
        assertNull("always should return null.", instance.getMessage(new Object()));
    }
    /**
     * Test the method getMessage.
     *
     */
    public void testgetMessage_withNull() {
        assertNull("always should return null.", instance.getMessage(null));
    }
    /**
     * Test the method valid, validated successfully.
     *
     */
    public void testgetvalid() {
        assertTrue("always should return true.", instance.valid(new Object()));
    }
}