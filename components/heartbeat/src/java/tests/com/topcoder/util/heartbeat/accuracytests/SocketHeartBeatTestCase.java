/**
 * TCS Heartbeat
 *
 * SocketHeartBeatTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.accuracytests;

import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * Tests functionality of SocketHeartBeat.
 *
 * @author valeriy
 * @version 1.0
 */
public class SocketHeartBeatTestCase extends TestCase {

    private ServerThread server;
    
    /**
     * Set up testing environment.
     */
    public void setUp() {
        server = new ServerThread();
        server.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    /**
     * Tear down testing environment.
     */
    public void tearDown() {
        server.stopThread();
    }

    /**
     * Test create SocketHeartBeat.
     */
    public void testCreateSocketHeartBeat() {
        Socket socket = new Socket();
        String s = "test";
        SocketHeartBeat beat = new SocketHeartBeat(socket, s);
        assertEquals(socket,  beat.getSocket());
        assertEquals(s, beat.getMessage());
    }

    /**
     * Test keepAlive.
     *
     */
    public void testKeepAlive() throws Exception {
        String message = "test";
        Socket socket = new Socket("localhost", ServerThread.PORT);
        SocketHeartBeat heartBeat = new SocketHeartBeat(socket, message);

        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());
        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());
        heartBeat.keepAlive();
        assertNull("Unexpected exception :"+heartBeat.getLastException(), heartBeat.getLastException());

        socket.close();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        
        ByteArrayInputStream bais = new ByteArrayInputStream(server.getMessage());
        ObjectInputStream ois = new ObjectInputStream(bais);
        assertEquals(message, ois.readObject());
        //ois = new ObjectInputStream(bais);
        assertEquals(message, ois.readObject());
        //ois = new ObjectInputStream(bais);
        assertEquals(message, ois.readObject());
    }

}

/**
 * A server thread to create a socket and accepts incoming message.
 */
class ServerThread extends Thread {

    /**
     * ServerSocket listening port.
     */
    public static final int PORT = 12724;

    /**
     * Whether the server is active.
     */
    private boolean active = true;

    /**
     * The incoming messages.
     */
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private ServerSocket ssocket = null;

    /**
     * Stop the thread.
     */
    public void stopThread() {
        active = false;
        try {
            // breaks accept() method
            ssocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get incoming message.
     *
     * @return incoming message
     */
    public byte[] getMessage() {
        return baos.toByteArray();
    }

    /**
     * Thread body.
     */
    public void run() {
        try {
            ssocket = new ServerSocket(PORT);
            while (active) {
                Socket socket = ssocket.accept();
                InputStream stream = socket.getInputStream();
                int c;
                while ((c = stream.read()) != -1) {
                    baos.write(c);
                }
                stream.close();
                socket.close();
            }
        } catch (IOException e) {
            if (active) {
                e.printStackTrace();
            }
        }
    }

}
