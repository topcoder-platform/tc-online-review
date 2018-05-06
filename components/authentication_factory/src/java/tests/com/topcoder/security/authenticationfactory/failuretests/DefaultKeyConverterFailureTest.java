/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.security.authenticationfactory.failuretests;

import com.topcoder.security.authenticationfactory.DefaultKeyConverter;
import com.topcoder.security.authenticationfactory.ConfigurationException;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

/**
 * <p>A failure test for <code>DefaultKeyConverter</code> class. Tests the proper handling of invalid input data by the
 * methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 * @since Authentication Factory 2.0
 */
public class DefaultKeyConverterFailureTest extends FailureTestCase {

    /**
     * <p>An instance of <code>DefaultKeyConverter</code> which is tested.</p>
     */
    private DefaultKeyConverter testedInstance = null;

    /**
     * <p>Gets the test suite for <code>DefaultKeyConverter</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>DefaultKeyConverter</code> class.
     */
    public static Test suite() {
        return new TestSuite(DefaultKeyConverterFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        releaseNamespaces();
        loadConfiguration(new File(FAILURE_ROOT, "DefaultKeyConverterConfig.xml"));
        testedInstance = new DefaultKeyConverter("Good");
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        releaseNamespaces();
    }

    /**
     * <p>Tests the <code>DefaultKeyConverter(String)</code> constructor for proper handling invalid input data. Passes
     * the <code>null</code> namespace and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_1() {
        try {
            new DefaultKeyConverter(null);
            fail("NullPointerException should be thrown in response to NULL namespace");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>DefaultKeyConverter(String)</code> constructor for proper handling invalid input data. Passes
     * the zero-length namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_2() {
        try {
            new DefaultKeyConverter("");
            fail("IllegalArgumentException should be thrown in response to zero-length namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>DefaultKeyConverter(String)</code> constructor for proper handling invalid input data. Passes
     * the empty namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_3() {
        try {
            new DefaultKeyConverter("            ");
            fail("IllegalArgumentException should be thrown in response to empty namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>DefaultKeyConverter(String)</code> constructor for proper handling invalid input data. Passes
     * the non-existing namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_4() {
        try {
            new DefaultKeyConverter("            ");
            fail("IllegalArgumentException should be thrown in response to empty namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>DefaultKeyConverter(String)</code> constructor for proper handling invalid input data. Passes
     * the namespace providing an empty value for a property and expects the <code>ConfigurationException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_5() {
        try {
            new DefaultKeyConverter("EmptyValue");
            fail("ConfigurationException should be thrown in response to empty value of the property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>convert(String)</code> method for proper handling invalid input data. Passes the <code>null
     * </code> key and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConvert_String_1() {
        try {
            testedInstance.convert(null);
            fail("NullPointerException should be thrown in response to NULL key");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>convert(String)</code> method for proper handling invalid input data. Passes the zero-length
     * key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConvert_String_2() {
        try {
            testedInstance.convert("");
            fail("IllegalArgumentException should be thrown in response to zero-length key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>convert(String)</code> method for proper handling invalid input data. Passes the empty key and
     * expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConvert_String_3() {
        try {
            testedInstance.convert("            ");
            fail("IllegalArgumentException should be thrown in response to empty key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

}
