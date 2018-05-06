/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.security.authenticationfactory.failuretests;

import com.topcoder.security.authenticationfactory.Principal;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>A failure test for <code>Principal</code> class. Tests the proper handling of invalid input data by the methods.
 * Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 * @since Authentication Factory 2.0
 */
public class PrincipalFailureTest extends TestCase {

    /**
     * <p>A <code>String</code> providing a sample key to be used for testing.</p>
     */
    public static final String KEY = "key";

    /**
     * <p>A <code>String</code> providing a sample value to be used for testing.</p>
     */
    public static final String VALUE = "value";

    /**
     * <p>An instance of <code>Principal</code> which is tested.</p>
     */
    private Principal testedInstance = null;

    /**
     * <p>Gets the test suite for <code>Principal</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>Principal</code> class.
     */
    public static Test suite() {
        return new TestSuite(PrincipalFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        testedInstance = new Principal("ID");
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        testedInstance = null;
    }

    /**
     * <p>Tests the <code>Principal(Object)</code> constructor for proper handling invalid input data. Passes the
     * <code>null</code> ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_Object_1() {
        try {
            new Principal(null);
            fail("NullPointerException should be thrown in response to NULL id");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>addMapping(String, Object)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testAddMapping_String_Object_1() {
        try {
            testedInstance.addMapping(null, VALUE);
            fail("NullPointerException should be thrown in response to NULL key");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>addMapping(String, Object)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> value and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testAddMapping_String_Object_2() {
        try {
            testedInstance.addMapping(KEY, null);
            fail("NullPointerException should be thrown in response to NULL value");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>addMapping(String, Object)</code> method for proper handling invalid input data. Passes the
     * zero-length key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testAddMapping_String_Object_3() {
        try {
            testedInstance.addMapping("", VALUE);
            fail("IllegalArgumentException should be thrown in response to zero-length key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>addMapping(String, Object)</code> method for proper handling invalid input data. Passes empty
     * key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testAddMapping_String_Object_4() {
        try {
            testedInstance.addMapping("        ", VALUE);
            fail("IllegalArgumentException should be thrown in response to empty key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>removeMapping(String)</code> method for proper handling invalid input data. Passes the <code>
     * null</code> key and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testRemoveMapping_String_1() {
        try {
            testedInstance.removeMapping(null);
            fail("NullPointerException should be thrown in response to NULL key");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>removeMapping(String)</code> method for proper handling invalid input data. Passes the
     * zero-length key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testRemoveMapping_String_2() {
        try {
            testedInstance.removeMapping("");
            fail("IllegalArgumentException should be thrown in response to zero-length key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>removeMapping(String)</code> method for proper handling invalid input data. Passes the empty
     * key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testRemoveMapping_String_3() {
        try {
            testedInstance.removeMapping("        ");
            fail("IllegalArgumentException should be thrown in response to empty key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getValue(String)</code> method for proper handling invalid input data. Passes the <code>null
     * </code> key and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testGetValue_String_1() {
        try {
            testedInstance.getValue(null);
            fail("NullPointerException should be thrown in response to NULL key");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getValue(String)</code> method for proper handling invalid input data. Passes the zero-length
     * key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetValue_String_2() {
        try {
            testedInstance.getValue("");
            fail("IllegalArgumentException should be thrown in response to zero-length key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getValue(String)</code> method for proper handling invalid input data. Passes the empty key
     * and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetValue_String_3() {
        try {
            testedInstance.getValue("        ");
            fail("IllegalArgumentException should be thrown in response to empty key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>containsKey(String)</code> method for proper handling invalid input data. Passes the <code>
     * null</code> key and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testContainsKey_String_1() {
        try {
            testedInstance.containsKey(null);
            fail("NullPointerException should be thrown in response to NULL key");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>containsKey(String)</code> method for proper handling invalid input data. Passes the
     * zero-length key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testContainsKey_String_2() {
        try {
            testedInstance.containsKey("");
            fail("IllegalArgumentException should be thrown in response to zero-length key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>containsKey(String)</code> method for proper handling invalid input data. Passes the empty key
     * and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testContainsKey_String_3() {
        try {
            testedInstance.containsKey("        ");
            fail("IllegalArgumentException should be thrown in response to empty key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }
}
