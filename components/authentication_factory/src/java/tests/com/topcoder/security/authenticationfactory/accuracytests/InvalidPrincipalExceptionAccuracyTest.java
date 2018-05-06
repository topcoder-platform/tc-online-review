package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import junit.framework.TestCase;


/**
 * Accuracy tests for InvalidPrincipalException class.
 */
public class InvalidPrincipalExceptionAccuracyTest extends TestCase {
    /**
     * Test the functionality of the constructor.
     */
    public void testConstructor() {
        InvalidPrincipalException e = new InvalidPrincipalException("test");
        assertEquals("Not the expected message.", "test", e.getMessage());
    }

    /**
     * Test the functionality of the constructor with null message.
     */
    public void testConstructorNull() {
        InvalidPrincipalException e = new InvalidPrincipalException(null);
        assertNull("Not the expected message.", e.getMessage());
    }

    /**
     * Test the functionality of the constructor with null message.
     */
    public void testConstructorEmpty() {
        InvalidPrincipalException e = new InvalidPrincipalException(" ");
        assertEquals("Not the expected message.", " ", e.getMessage());
    }
}
