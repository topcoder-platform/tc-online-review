/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ HelperTest.java
 */
package com.topcoder.naming.jndiutility;

import junit.framework.TestCase;


/**
 * Junit test class for Helper.
 *
 * @author Charizard
 * @version 2.0
 */
public class HelperTest extends TestCase {
    /**
     * Test method for {@link Helper#checkObject(Object, String)}. Normal case, call with normal object.
     */
    public void testCheckObjectNormal() {
        Helper.checkObject(new Object(), TestHelper.generateString());
    }

    /**
     * Test method for {@link Helper#checkObject(Object, String)}. Failure case, call with null and check the
     * error message.
     */
    public void testCheckObjectFailure() {
        String name = TestHelper.generateString();

        try {
            Helper.checkObject(null, name);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            assertTrue("wrong error message", e.getMessage().indexOf(name) >= 0);
        }
    }

    /**
     * Test method for {@link Helper#checkString(String, String)}. Normal case, call with a normal string.
     */
    public void testCheckStringNormal() {
        Helper.checkString(TestHelper.generateString(), TestHelper.generateString());
    }

    /**
     * Test method for {@link Helper#checkString(String, String)}. Failure case 1, call with null.
     */
    public void testCheckStringFailure1() {
        String name = TestHelper.generateString();

        try {
            Helper.checkString(null, name);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            assertTrue("wrong error message", e.getMessage().indexOf(name) >= 0);
        }
    }

    /**
     * Test method for {@link Helper#checkString(String, String)}. Failure case 2, call with empty string.
     */
    public void testCheckStringFailure2() {
        String name = TestHelper.generateString();

        try {
            Helper.checkString(" ", name);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            assertTrue("wrong error message", e.getMessage().indexOf(name) >= 0);
        }
    }
}
