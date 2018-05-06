/*
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.stresstests;

import java.io.*;
import java.net.*;
import java.util.*;
import com.topcoder.util.heartbeat.*;

import junit.framework.TestCase;

/**
 * Contains stress tests for the OutputStreamHeartBeat class.
 * @author mathgodleo
 * @version 1.0
 */
public class OutputStreamHeartBeatStressTest extends TestCase {

    /**
     * The number of heartbeats to send
     */
    private static final int NUM_HEARTBEATS = 500;

    /**
     * The message each heartbeat should send
     */
    private static final byte[] MESSAGE = "hello".getBytes();

    /**
     * Stress tests OutputStreamHeartBeat by sending NUM_HEARTBEATS keepAlive()'s
     * and making sure that at least 100 / second are executed. Also makes sure the
     * heartbeat messages are written correctly.
     */
    public void testOutputStreamHeartBeat(){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamHeartBeat heartbeat = new OutputStreamHeartBeat(out, MESSAGE);


            // time NUM_HEARTBEATS heartbeats
            long time = System.currentTimeMillis();

            for (int i = 0; i < NUM_HEARTBEATS; i++) {
                heartbeat.keepAlive();
            }

            out.close();

            time = System.currentTimeMillis() - time;

            assertTrue("at least 100 heartbeats/seconds should be sent", 
                       NUM_HEARTBEATS / (time / 1000.0) >= 100.0);


            // check that heartbeat messages were properly written
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));

            for (int i = 0; i < NUM_HEARTBEATS; i++) {
                assertTrue("incorrect heartbeat message read", 
                           Arrays.equals(MESSAGE, (byte[])in.readObject()));
            }
        } catch (Exception ex) {
            fail("shouldn't have thrown exception: " + ex.getMessage());
        }
    }
}   
