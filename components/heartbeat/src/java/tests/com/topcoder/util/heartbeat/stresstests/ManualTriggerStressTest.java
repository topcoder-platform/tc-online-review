/*
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.stresstests;

import java.io.*;
import java.net.*;
import com.topcoder.util.heartbeat.*;

import junit.framework.TestCase;

/**
 * Contains stress tests for the ManualTrigger class.
 * @author mathgodleo
 * @version 1.0
 */
public class ManualTriggerStressTest extends TestCase {

    private static final int NUM_HEARTBEATS = 10;
    private static final int NUM_TRIGGERS = 5;

    /** 
     * Tests manual trigger, to make sure it doesn't impose 
     * much of an overhead (should send >= 10 heartbeats/sec)
     */
    public void testManualTrigger(){
        try {
            ManualTrigger mt = new ManualTrigger();
            for (int i = 0; i < NUM_TRIGGERS; i++) {
                mt.add(new URLHeartBeat(new URL("http://www.google.com")));
            }

            long time = System.currentTimeMillis();

            for (int i = 0; i < NUM_HEARTBEATS; i++) {
                mt.fireKeepAlive();
            }

            time = System.currentTimeMillis() - time;

            assertTrue("should send >= 10.0 heartbeats / second",
                       NUM_HEARTBEATS * NUM_TRIGGERS / (time / 1000.0) >= 5.0);
        } catch (Exception ex) {
            fail("shouldn't have thrown exception: " + ex.getMessage());
        }
    }
}
