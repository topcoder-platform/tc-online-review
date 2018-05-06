/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.security.authenticationfactory.http.HttpCookie;
import com.topcoder.security.authenticationfactory.http.basicimpl.HttpResourceImpl;

/**
 * <p>
 * This class contains the unit tests for HttpResourceImpl.</p>
 *
 * @author TCSDEVELOPER
 *
 * @version 2.0
 */
public class HttpResourceImplTest extends TestCase {

    /**
     * <p>
     * Represents an instance of HttpResourceImpl to be used for testing.</p>
     */
    private static HttpResourceImpl httpRes = null;

    /**
     * <p>
     * Represents an instance of HttpURLConnection to be used for testing.</p>
     */
    private static HttpURLConnection conn = null;

    /**
     * <p>
     * Represents an instance of httpURL String to be used for testing.</p>
     */
    private static String httpURL = "http://www.topcoder.com";

    /**
     * <p>
     * this variable allow the setUp() to set both this.conn and
     * this.httpRes only once.
     * </p>
     */
    private static boolean lockFlag = false;

    /**
     * <p>
     * Creates an instance for the Test.</p>
     *
     * @param name the name of the TestCase.
     */
    public HttpResourceImplTest(String name) {
        super(name);
    }

    /**
     *<p>
     * Sets up the fixture.</p>
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        if (!lockFlag) {
            lockFlag = true;
            URL url = new URL(httpURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            httpRes = new HttpResourceImpl(conn, httpURL);
        }
    } // end setUp()


    /**
     *<p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(HttpResourceImplTest.class);
    } // end suite()

    /**
     *<p>
     * Tests accuracy of the method
     * HttpResourceImpl(HttpURLConnection conn, String originalURL).</p>
     *
     */
    public void testHttpResourceImplAccuracy() {
        HttpResourceImpl test =
            new HttpResourceImpl(conn, "http://www.topcoder.com");
        assertTrue(test.getActualURL().equals("http://www.topcoder.com"));
    } // end testHttpResourceImplAccuracy()

    /**
     *<p>
     * Tests all exceptions of the method
     * HttpResourceImpl(HttpURLConnection conn, String originalURL), which
     * includes testing NullPointerException.</p>
     *
     */
    public void testHttpResourceImplException() {
        // test NullPointerException
        try {
            new HttpResourceImpl(conn, null);
            fail("If originalURL is null, throws NullPointerException.");
        } catch (NullPointerException ne) {
            // pass
        }
        try {
            new HttpResourceImpl(null, "www.topcoder.com");
            fail("If http conn is null, throws NullPointerException.");
        } catch (NullPointerException ne) {
            // pass
        }

    } // end testHttpResourceImplException()

    /**
     *<p>
     * Tests accuracy of the method getHttpConnection().</p>
     *
     */
    public void testGetHttpConnectionAccuracy() {
        assertTrue(httpRes.getHttpConnection() == conn);
    } // end testGetHttpConnectionAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getOriginalURL().</p>
     *
     */
    public void testGetOriginalURLAccuracy() {
        assertEquals(httpRes.getOriginalURL(), "http://www.topcoder.com");
    } // end testGetOriginalURLAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getActualURL().
     * 'http://www.topcoder.com' should be returned by this method.</p>
     *
     */
    public void testGetActualURLAccuracy() {
        assertEquals("'http://www.topcoder.com' expected",
                httpRes.getActualURL(), "http://www.topcoder.com");
    } // end testGetActualURLAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getContentType().</p>
     *
     */
    public void testGetContentTypeAccuracy() {
        assertTrue(httpRes.getContentType().trim().indexOf("text/html") >= 0);
    } // end testGetContentTypeAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getContent().</p>
     *
     * @throws IOException to JUnit
     */
    public void testGetContentAccuracy() throws IOException {
        assertNotNull(httpRes.getContent());
    } // end testGetContentAccuracy()

    /**
     *<p>
     * Tests accuracy of the method getHeaderField(String name).</p>
     *
     */
    public void testGetHeaderFieldAccuracy() {
        String cookie = httpRes.getHeaderField("Set-CoOkie");
        // output the result into the standard err
        System.out.println(
                "The SetCookie attribute in header field is: " + cookie);
    } // end testGetHeaderFieldAccuracy()

    /**
     *<p>
     * Tests all exceptions of the method getHeaderField(String name), which
     * includes testing NullPointerException.</p>
     *
     */
    public void testGetHeaderFieldException() {
        // test IllegalArgumentException
        try {
            httpRes.getHeaderField(null);
            fail("If name is null, throws NullPointerException.");
        } catch (NullPointerException ne) {
            // pass
        }
    } // end testGetHeaderFieldException()

    /**
     *<p>
     * Tests accuracy of the method getSetCookie().</p>
     *
     */
    public void testGetSetCookieAccuracy() {
        assertNotNull(httpRes.getSetCookie());
        // output the cookie content into the standard err
        System.out.println(
                "The set-Cookie content of 'www.topcoder.com' is "
                + httpRes.getSetCookie());
    } // end testGetSetCookieAccuracy()


    /**
     *<p>
     * Tests accuracy of the method getCookie().</p>
     *
     * @throws AuthenticateException to JUnit
     */
    public void testGetCookieAccuracy() throws AuthenticateException {
        HttpCookie httpCookie = httpRes.getCookie();
        assertTrue(httpCookie.getName().startsWith("JSESSIONID"));
        assertTrue(httpCookie.getPath().trim().equals("/"));
    } // end testGetCookieAccuracy()

} // end HttpResourceImplTest