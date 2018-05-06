/**
 * TCS Heartbeat
 *
 * URLHeartBeatTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Tests functionality of URLHeartBeat.
 *
 * @author TCSDeveloper
 * @version 1.0
 */
public class URLHeartBeatTestCase extends TestCase {

    /**
     * A good URL string.
     */
    public static final String TEST_URL = "http://www.topcoder.com";

    /**
     * A bad URL string (but not malformed).
     */
    public static final String BAD_URL =  "http://error.topcoder.com/bad.html";

    /**
     * Set up testing environment.
     */
    public void setUp() {
    }

    /**
     * Tear down testing environment.
     */
    public void tearDown() {
    }

    /**
     * Test create URLHeartBeat.
     *
     * @throws MalformedURLException to JUnit
     */
    public void testCreateURLHeartBeat() throws MalformedURLException {
        assertNotNull(new URLHeartBeat(new URL(TEST_URL)));

        try {
            new URLHeartBeat(null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test getURL.
     *
     * @throws MalformedURLException to JUnit
     */
    public void testGetURL() throws MalformedURLException {
        URL url = new URL(TEST_URL);
        URLHeartBeat heartBeat = new URLHeartBeat(url);
        assertTrue(heartBeat.getURL() == url);
    }

    /**
     * Test getLastException.
     *
     * @throws MalformedURLException to JUnit
     */
    public void testGetLastException() throws MalformedURLException {
        URLHeartBeat heartBeat = new URLHeartBeat(new URL(TEST_URL));
 
        heartBeat.keepAlive();
 
        assertNull(heartBeat.getLastException());
 
        heartBeat = new URLHeartBeat(new URL(BAD_URL));
 
        heartBeat.keepAlive();

        assertTrue(heartBeat.getLastException() instanceof IOException);

    }

    /**
     * Test keepAlive.
     *
     * @throws MalformedURLException to JUnit
     */
    public void testKeepAlive() throws MalformedURLException {
        // same test code as the one above
        // I cannot verify whether the url is read through
        // but since a bad url causes exception
        // we know the url is at least read 
        URLHeartBeat heartBeat = new URLHeartBeat(new URL(TEST_URL));
        heartBeat.keepAlive();
        assertNull(heartBeat.getLastException());


        heartBeat = new URLHeartBeat(new URL(BAD_URL));
        heartBeat.keepAlive();
        assertTrue(heartBeat.getLastException() instanceof IOException);
    }

}
