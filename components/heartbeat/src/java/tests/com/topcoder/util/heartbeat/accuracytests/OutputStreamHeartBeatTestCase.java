/**
 * TCS Heartbeat
 *
 * OutputStreamHeartBeatTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.accuracytests;

import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;
import java.util.Arrays;
import java.io.*;

/**
 * Tests functionality of OutputStreamHeartBeat.
 *
 * @author valeriy
 * @version 1.0
 */
public class OutputStreamHeartBeatTestCase extends TestCase {

    private static String testFile = "testOutputStream";
    
    /**
     * Tear down testing environment.
     */
    public void tearDown() {
        File test = new File(testFile);
        if (!test.delete()) test.deleteOnExit();
    }

    /**
     * Test create OutputStreamHeartBeat.
     */
    public void testCreateOutputStreamHeartBeat() {
        OutputStream os = new ByteArrayOutputStream();
        String s = "test";
        OutputStreamHeartBeat beat = new OutputStreamHeartBeat(os, s);
        assertEquals(os,  beat.getOutputStream());
        assertEquals(s, beat.getMessage());
    }

    /**
     * Tests keepAlive.
     *
     */
    public void testKeepAlive() throws Exception {
        String message = "test";
        OutputStream stream = new FileOutputStream(testFile);
        OutputStreamHeartBeat heartBeat = new OutputStreamHeartBeat(stream, message);

        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());
        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());
        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());
        stream.close();

        InputStream bais = new FileInputStream(testFile);
        ObjectInputStream ois = new ObjectInputStream(bais);
        assertEquals(message, ois.readObject());
        //ois = new ObjectInputStream(bais);
        assertEquals(message, ois.readObject());
        //ois = new ObjectInputStream(bais);
        assertEquals(message, ois.readObject());
        bais.close();
    }

}

