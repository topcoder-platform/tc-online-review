/*
Copyright (c) 2004, TopCoder, Inc.  All rights reserved.
*/
package com.topcoder.security.authenticationfactory.failuretests.http.basicimpl;

import com.topcoder.security.authenticationfactory.http.basicimpl.HttpResourceImpl;
import junit.framework.TestCase;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Failure tests for HttpResourceImpl V1.1.
 *
 * @author  WishingBone
 * @version 1.1
 */
public class HttpResourceImplV11FailureTests extends TestCase {

    /**
     * The test url.
     */
    private static final String TEST_URL = "http://www.topcoder.com";

    /**
     * Create HttpResourceImpl with null conn.
     */
    public void testCreateHttpResourceImpl_NullConn() {
        try {
            new HttpResourceImpl(null, TEST_URL);
            fail();
        } catch (NullPointerException npe) {
        }
    }

    /**
     * Create HttpResourceImpl with null url.
     *
     * @throws Exception to JUnit.
     */
    public void testCreateHttpResourceImpl_NullUrl() throws Exception {
        try {
            new HttpResourceImpl((HttpURLConnection) new URL(TEST_URL).openConnection(), null);
            fail();
        } catch (NullPointerException npe) {
        }
    }

    /**
     * getHeaderField() with null name.
     *
     * @throws Exception to JUnit.
     */
    public void testGetHeaderField_NullName() throws Exception {
        try {
            new HttpResourceImpl((HttpURLConnection) new URL(TEST_URL).openConnection(), TEST_URL).getHeaderField(null);
            fail();
        } catch (NullPointerException npe) {
        }
    }

}
