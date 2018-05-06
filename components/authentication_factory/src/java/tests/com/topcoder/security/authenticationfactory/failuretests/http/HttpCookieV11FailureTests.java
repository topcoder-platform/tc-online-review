/*
Copyright (c) 2004, TopCoder, Inc.  All rights reserved.
*/
package com.topcoder.security.authenticationfactory.failuretests.http;

import com.topcoder.security.authenticationfactory.http.HttpCookie;
import junit.framework.TestCase;

/**
 * Failure tests for HttpCookie V1.1.
 *
 * @author  WishingBone
 * @version 1.1
 */
public class HttpCookieV11FailureTests extends TestCase {

    /**
     * Create HttpCookie with null setCookie.
     */
    public void testCreateHttpCookie_NullSetCookie() {
        try {
            new HttpCookie(null);
            fail();
        } catch (NullPointerException npe) {
        }
    }

    /**
     * Create HttpCookie with invalid format.
     */
    public void testCreateHttpCookie_InvalidFormat() {
        try {
            new HttpCookie("hello cookie");
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create HttpCookie without name.
     */
    public void testCreateHttpCookie_MissingName() {
        try {
            new HttpCookie("Max-Age=1000; Secure");
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create HttpCookie with duplicate attribute.
     */
    public void testCreateHttpCookie_DuplicateAttribute() {
        try {
            new HttpCookie("name=value; Max-Age=1000; Max-Age=2000");
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create HttpCookie with invalid Max-Age.
     */
    public void testCreateHttpCookie_InvalidMaxAge() {
        try {
            new HttpCookie("name=value; Max-Age=one_year");
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create HttpCookie with invalid Secure.
     */
    public void testCreateHttpCookie_InvalidSecure() {
        try {
            new HttpCookie("name=value; Secure=yes");
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create HttpCookie with invalid version.
     */
    public void testCreateHttpCookie_InvalidVersion() {
        try {
            new HttpCookie("name=value; Version=update");
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

}
