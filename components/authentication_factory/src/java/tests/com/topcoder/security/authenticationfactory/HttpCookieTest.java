/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import com.topcoder.security.authenticationfactory.http.HttpCookie;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This class contains the unit tests for HttpCookie.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class HttpCookieTest extends TestCase {

    /**
     * <p>
     * Represents an instance of HttpCookie to be used for testing.</p>
     */
    private HttpCookie httpCookie = null;

    /**
     * Represents a cookie string for parsing.
     * Note: the domain attribute must start with '.'
     */
    private String cookieStr = "name=value; path=/index.HTML; version=1; "
        + "max-Age=1234567; domain=.topcoder.com; comment=no;Secure";

    /**
     * <p>
     * Creates an instance for the Test.</p>
     *
     * @param name the name of the TestCase.
     */
    public HttpCookieTest(String name) {
        super(name);
    }

    /**
     *<p>
     * Sets up the fixture.</p>
     */
    protected void setUp() {
        httpCookie = new HttpCookie(cookieStr);
    } // end setUp()

    /**
     *<p>
     * Tears down the fixture.</p>
     */
    protected void tearDown() {
        httpCookie = null;
    } // end tearDown()

    /**
     *<p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(HttpCookieTest.class);
    } // end suite()

    /**
     *<p>
     * Tests accuracy of the method HttpCookie(String setCookie).</p>
     *
     */
    public void testHttpCookieAccuracy() {
        HttpCookie coo = new HttpCookie("sessionID=1234; Path=/");
        assertTrue(coo.getName().equals("sessionID"));
        assertTrue(coo.getPath().equals("/"));
    } // end testHttpCookieAccuracy()

    /**
     *<p>
     * Tests all exceptions of the method HttpCookie(String setCookie), which
     * includes testing NullPointerException, IllegalArgumentException.</p>
     *
     */
    public void testHttpCookieException() {
        // test NullPointerException
        try {
            new HttpCookie(null);
            fail("If cookie string is null, "
                 + "throws IllegalArgumentException.");
        } catch (NullPointerException ne) {
            // pass
        }

        // test IllegalArgumentException
        try {
            new HttpCookie("ht=valu; comment=");
            fail("If cookie string is invalid,"
                 + "throws IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // pass
        }

        try {
            new HttpCookie("ht=valu; comment=no #name=value");
            fail("If cookie string is a multi-cookies,"
                 + "throws IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // pass
        }

        try {
            new HttpCookie("ht=valu; comment=no; Path=/; path=/go;");
            fail("If cookie string contains duplicate attributes,"
                 + "throws IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // pass
        }

    } // end testHttpCookieException()

    /**
     *<p>
     * Tests accuracy of the method getName().</p>
     *
     */
    public void testGetNameAccuracy() {
        assertTrue(httpCookie.getName().equals("name"));
    } // end testGetNameAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getValue().</p>
     *
     */
    public void testGetValueAccuracy() {
        assertTrue(httpCookie.getValue().equals("value"));
    } // end testGetValueAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getPath().</p>
     *
     */
    public void testGetPathAccuracy() {
        assertTrue(httpCookie.getPath().equals("/index.HTML"));
    } // end testGetPathAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getDomain().</p>
     *
     */
    public void testGetDomainAccuracy() {
        // note: an invalid domain in Set-Cookie must starts with '.'
        assertEquals(httpCookie.getDomain(), ".topcoder.com");
    } // end testGetDomainAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getMaxAge().</p>
     *
     */
    public void testGetMaxAgeAccuracy() {
        assertTrue(httpCookie.getMaxAge() == 1234567);
    } // end testGetMaxAgeAccuracy()

    /**
     *<p>
     * Tests accuracy of the method isSecure().</p>
     *
     */
    public void testIsSecureAccuracy() {
        assertTrue(httpCookie.isSecure());
    } // end testIsSecureAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getComment().</p>
     *
     */
    public void testGetCommentAccuracy() {
        assertTrue(httpCookie.getComment().equals("no"));
    } // end testGetCommentAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getVersion().</p>
     *
     */
    public void testGetVersionAccuracy() {
        assertTrue(httpCookie.getVersion() == 1);
    } // end testGetVersionAccuracy()

} // end HttpCookieTest

