/**
 * TCS Heartbeat
 *
 * OutputStreamHeartBeatTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import junit.framework.TestCase;

import java.util.Arrays;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;

/**
 * Tests functionality of OutputStreamHeartBeat.
 *
 * @author TCSDeveloper
 * @version 1.0
 */
public class OutputStreamHeartBeatTestCase extends TestCase {

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
     * Test create OutputStreamHeartBeat.
     */
    public void testCreateOutputStreamHeartBeat() {
        assertNotNull(new OutputStreamHeartBeat(
            new ByteArrayOutputStream(),
            new byte[1]));

        try {
            new OutputStreamHeartBeat(
                null,
                new byte[1]);
            fail();
        } catch (NullPointerException npe) {
            ;
        }

        try {
            new OutputStreamHeartBeat(
                new ByteArrayOutputStream(),
                null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test getOutputStream.
     */
    public void testGetOutputStream() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamHeartBeat heartBeat = new OutputStreamHeartBeat(
            stream,
            new byte[1]);
        assertTrue(heartBeat.getOutputStream() == stream);
    }

    /**
     * Test getMessage.
     */
    public void testGetMessage() {
        byte[] message = "foo".getBytes();
        OutputStreamHeartBeat heartBeat = new OutputStreamHeartBeat(
            new ByteArrayOutputStream(),
            message);
        assertTrue(heartBeat.getMessage() == message);
    }

    /**
     * Test getLastException.
     */
    public void testGetLastException() {
        OutputStreamHeartBeat heartBeat = new OutputStreamHeartBeat(
            new ByteArrayOutputStream(),
            new byte[1]);
        heartBeat.keepAlive();
        assertNull(heartBeat.getLastException());

        heartBeat = new OutputStreamHeartBeat(
            new MyOutputStream(),
            new byte[1]);
        heartBeat.keepAlive();
        assertTrue(heartBeat.getLastException() instanceof RuntimeException);
    }

    /**
     * Test keepAlive.
     *
     * @throws IOException to Junit
     * @throws ClassNotFoundException to JUnit
     */
    public void testKeepAlive() throws IOException, ClassNotFoundException {
        byte[] message = "foo".getBytes();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamHeartBeat heartBeat = new OutputStreamHeartBeat(
            stream,
            message);

        heartBeat.keepAlive();

        ByteArrayInputStream bais =
                new ByteArrayInputStream(stream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        assertTrue(Arrays.equals(message, (byte[]) ois.readObject()));
        ois.close();
        bais.close();
    }

}

/**
 * An OutputStream that always throws Exception when writing to.
 */
class MyOutputStream extends ByteArrayOutputStream {

    /**
     * Override write method to throw Exception.
     *
     * @param b byte array to write
     * @param off offset
     * @param len length
     */
    public void write(byte[] b, int off, int len) {
        throw new RuntimeException();
    }

}
