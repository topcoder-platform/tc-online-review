/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This class contains the unit tests for MissingPrincipalKeyException.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class MissingPrincipalKeyExceptionTest extends TestCase {

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(MissingPrincipalKeyExceptionTest.class);
    }

    /**
     * <p>
     * Test construct it with null key, should throw NullPointerException.</p>
     *
     */
    public void testMissingPrincipalKeyExceptionNPE() {
        try {
            MissingPrincipalKeyException mpke = new MissingPrincipalKeyException(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        }
    }
   /**
    * <p>
    * Test construct it with empty key, should throw IllegalArgumentException.</p>
    */
    public void testMissingPrincipalKeyExceptionIAE() {
        try {
            MissingPrincipalKeyException mpke = new MissingPrincipalKeyException("  ");
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Construct it with valid parameter.</p>
     */
    public void testMissingPrincipalKeyException() {
        MissingPrincipalKeyException mpke = new MissingPrincipalKeyException("mpke");
        assertTrue(mpke instanceof InvalidPrincipalException);
    }

    /**
     * <p>Test getKey method.</p>
     */
    public void testGetKey() {
        String key = "key";
        MissingPrincipalKeyException mpke = new MissingPrincipalKeyException(key);
        assertEquals(mpke.getKey(), key);
    }
}
