/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.security.authenticationfactory.failuretests;

import com.topcoder.security.authenticationfactory.TimeoutCacheFactory;
import com.topcoder.security.authenticationfactory.ConfigurationException;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

/**
 * <p>A failure test for <code>TimeoutCacheFactory</code> class. Tests the proper handling of invalid input data by the
 * methods. Passes the invalid arguments to the constructors and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 * @since Authentication Factory 2.0
 */
public class TimeoutCacheFactoryFailureTest extends FailureTestCase {

    /**
     * <p>Gets the test suite for <code>TimeoutCacheFactory</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>TimeoutCacheFactory</code> class.
     */
    public static Test suite() {
        return new TestSuite(TimeoutCacheFactoryFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        releaseNamespaces();
        loadConfiguration(new File(FAILURE_ROOT, "TimeoutCacheFactoryConfig.xml"));
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
     * <p>Tests the <code>TimeoutCacheFactory(long)</code> constructor for proper handling invalid input data. Passes
     * the negative timeout value and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_long_1() {
        try {
            new TimeoutCacheFactory(-1);
            fail("IllegalArgumentException should be thrown in response to negative timeout");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(long)</code> constructor for proper handling invalid input data. Passes
     * the negative timeout and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_long_2() {
        try {
            new TimeoutCacheFactory(-1000000);
            fail("IllegalArgumentException should be thrown in response to negative timeout");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(String)</code> constructor for proper handling invalid input data. Passes
     * the <code>null</code> namespace and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_1() {
        try {
            new TimeoutCacheFactory(null);
            fail("NullPointerException should be thrown in response to NULL namespace");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(String)</code> constructor for proper handling invalid input data. Passes
     * a zero-length namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_2() {
        try {
            new TimeoutCacheFactory("");
            fail("IllegalArgumentException should be thrown in response to zero-length namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(String)</code> constructor for proper handling invalid input data. Passes
     * an empty namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_3() {
        try {
            new TimeoutCacheFactory("         ");
            fail("IllegalArgumentException should be thrown in response to empty namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(String)</code> constructor for proper handling invalid input data. Passes
     * a non-existing namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_4() {
        try {
            new TimeoutCacheFactory("         ");
            fail("IllegalArgumentException should be thrown in response to empty namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(String)</code> constructor for proper handling invalid input data. Passes
     * a namespace missing the <code>timeout</code> property and expects the <code>ConfigurationException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_5() {
        try {
            new TimeoutCacheFactory("MissingTimeout");
            fail("ConfigurationException should be thrown in response to namespace missing the 'timeout' property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(String)</code> constructor for proper handling invalid input data. Passes
     * a namespace providing the negative <code>timeout</code> property and expects the <code>ConfigurationException
     * </code> to be thrown.</p>
     */
    public void testConstructor_String_6() {
        try {
            new TimeoutCacheFactory("NegativeTimeout");
            fail("ConfigurationException should be thrown in response to namespace providing the negative timeout");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(String)</code> constructor for proper handling invalid input data. Passes
     * a namespace providing the <code>timeout</code> exceeding the Long.MAX_VALUE and expects the <code>
     * ConfigurationException </code> to be thrown.</p>
     */
    public void testConstructor_String_7() {
        try {
            new TimeoutCacheFactory("TooLargeTimeout");
            fail("ConfigurationException should be thrown in response to namespace providing the timeout exceeding "
                + "the Long.MAX_VALUE");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>TimeoutCacheFactory(String)</code> constructor for proper handling invalid input data. Passes
     * a namespace providing the unparsable <code>timeout</code> property and expects the <code>ConfigurationException
     * </code> to be thrown.</p>
     */
    public void testConstructor_String_8() {
        try {
            new TimeoutCacheFactory("UnparsableTimeout");
            fail("ConfigurationException should be thrown in response to namespace providing the unparsable timeout");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }
}
