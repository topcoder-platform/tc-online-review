package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import junit.framework.TestCase;


/**
 * Accuracy tests for MissingPrincipalKeyException class.
 */
public class MissingPrincipalKeyExceptionAccuracyTest extends TestCase {
    /**
     * Test the functionality of the constructor.
     */
    public void testConstructor() {
        MissingPrincipalKeyException e = new MissingPrincipalKeyException("test");
        assertTrue("Should inherit InvalidPrincipalException.", e instanceof InvalidPrincipalException);
        assertTrue("Message must exist.", e.getMessage().trim().length() > 0);
    }

    /**
     * Test get the missing key.
     */
    public void testGetKey() {
        MissingPrincipalKeyException e = new MissingPrincipalKeyException("test");
        assertEquals("Not the expected key.", "test", e.getKey());
    }
}
