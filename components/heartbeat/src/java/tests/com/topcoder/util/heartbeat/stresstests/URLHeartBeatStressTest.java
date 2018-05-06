/*
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.stresstests;

import java.io.*;
import java.net.*;
import com.topcoder.util.heartbeat.*;

import junit.framework.TestCase;

/**
 * Contains stress tests for the URLHeartBeat class.
 * @author mathgodleo
 * @version 1.0
 */
public class URLHeartBeatStressTest extends TestCase {

    /**
     * Number of heartbeats to send
     */
    private static final int NUM_HEARTBEATS = 50;

    /**
     * Tests the URL hearbeat by using keepAlive on a URL NUM_HEARTBEATS times. The 
     * component should be able to send >= 5 heartbeats/second
     */
    public void testURLHeartBeat(){
        try {
            URLHeartBeat urlHeartBeat1 = new URLHeartBeat(new URL("http://www.google.com"));

            long time = System.currentTimeMillis();

            for (int i = 0; i < NUM_HEARTBEATS; i++) {
                urlHeartBeat1.keepAlive();
            }

            time = System.currentTimeMillis() - time;

            assertTrue("should take send >= 5 heartbeats / second", 
                       NUM_HEARTBEATS / (time / 1000.0) >= 5.0);
        } catch (Exception ex) {
            fail("shouldn't have thrown exception: " + ex.getMessage());
        }
    }
}
