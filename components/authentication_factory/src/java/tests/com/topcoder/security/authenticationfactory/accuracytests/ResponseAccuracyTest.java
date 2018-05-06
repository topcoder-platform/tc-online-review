package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.Response;
import junit.framework.TestCase;


/**
 * Accuracy tests for Response class.
 */
public class ResponseAccuracyTest extends TestCase {
    /**
     * Test the constructor that receives bool input.
     */
    public void testConstructorB() {
        Response r = new Response(true);
        assertEquals("Not the expected successful flag.", true, r.isSuccessful());
        assertNull("Message should be null.", r.getMessage());
        assertNull("Details should be null.", r.getDetails());
    }

    /**
     * Test the constructor that receives bool and string input.
     */
    public void testConstructorBS() {
        Response r = new Response(true, "test");
        assertEquals("Not the expected successful flag.", true, r.isSuccessful());
        assertEquals("Not the expected message.", "test", r.getMessage());
        assertNull("Details should be null.", r.getDetails());
    }

    /**
     * Test the constructor that receives bool and object input.
     */
    public void testConstructorBO() {
        Object details = new Object();
        Response r = new Response(true, details);
        assertEquals("Not the expected successful flag.", true, r.isSuccessful());
        assertNull("Message should be null.", r.getMessage());
        assertSame("Not the expected details.", details, r.getDetails());
    }

    /**
     * Test the constructor that receives bool, string and object input.
     */
    public void testConstructorBSO() {
        Object details = new Object();
        Response r = new Response(true, "test", details);
        assertEquals("Not the expected successful flag.", true, r.isSuccessful());
        assertEquals("Not the expected message.", "test", r.getMessage());
        assertSame("Not the expected details.", details, r.getDetails());
    }
}
