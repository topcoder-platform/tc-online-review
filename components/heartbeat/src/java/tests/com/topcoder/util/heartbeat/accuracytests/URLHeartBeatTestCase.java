/**
 * TCS Heartbeat
 *
 * URLHeartBeatTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.accuracytests;

import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Tests functionality of URLHeartBeat.
 *
 * @author valeriy
 * @version 1.0
 */
public class URLHeartBeatTestCase extends TestCase {

    /**
     * A good URL string.
     */
    private URL testURL = null;

    /**
     * Set up testing environment.
     */
    public void setUp() {
        try {
            testURL = new File(new File("test_files", "accuracy"), "test").toURL();
        } catch (MalformedURLException e) {}
    }

    /**
     * Test create URLHeartBeat.
     *
     */
    public void testCreateURLHeartBeat() throws Exception {
        URL url = testURL;
        URLHeartBeat beat = new URLHeartBeat(url);
        assertEquals(url,  beat.getURL());
    }

    /**
     * Test keepAlive.
     *
     */
    public void testKeepAlive() throws Exception {
        URLHeartBeat heartBeat = new URLHeartBeat(testURL);
        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());
        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());
        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());
    }

}
