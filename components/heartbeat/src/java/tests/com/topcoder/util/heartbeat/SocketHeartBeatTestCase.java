/**
 * TCS Heartbeat
 *
 * SocketHeartBeatTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import junit.framework.TestCase;

import java.io.InputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Tests functionality of SocketHeartBeat.
 *
 * @author TCSDeveloper
 * @version 1.0
 */
public class SocketHeartBeatTestCase extends TestCase {

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
     * Test create SocketHeartBeat.
     */
    public void testCreateSocketHeartBeat() {
        assertNotNull(new SocketHeartBeat(
            new Socket(),
            new byte[1]));

        try {
            new SocketHeartBeat(
                null,
                new byte[1]);
            fail();
        } catch (NullPointerException npe) {
            ;
        }

        try {
            new SocketHeartBeat(
                new Socket(),
                null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test getSocket.
     */
    public void testGetSocket() {
        Socket socket = new Socket();
        SocketHeartBeat heartBeat = new SocketHeartBeat(
            socket,
            new byte[1]);
        assertTrue(socket == heartBeat.getSocket());
    }

    /**
     * Test getMessage.
     */
    public void testGetMessage() {
        byte[] message = "foo".getBytes();
        SocketHeartBeat heartBeat = new SocketHeartBeat(
            new Socket(),
            message);
        assertTrue(message == heartBeat.getMessage());
    }

    /**
     * Test getLastException.
     *
     * @throws IOException to JUnit
     */
    public void testGetLastException() throws IOException {
        Socket socket = new Socket();
        SocketHeartBeat heartBeat = new SocketHeartBeat(
            socket,
            new byte[1]);
        assertTrue(heartBeat.getLastException() instanceof IOException);

        ServerThread server = new ServerThread();
        server.start();
        server.yield();
        socket = new Socket("localhost", ServerThread.PORT);
        heartBeat = new SocketHeartBeat(
            socket,
            new byte[1]);
        heartBeat.keepAlive();
        server.stopThread();
        socket.close();
        assertNull(heartBeat.getLastException());
    }

    /**
     * Test keepAlive.
     *
     * @throws IOException to JUnit
     * @throws ClassNotFoundException to JUnit
     */
    public void testKeepAlive() throws IOException, ClassNotFoundException {
        byte[] message = "foo".getBytes();
        ServerThread server = new ServerThread();
        server.start();
        server.yield();
        Socket socket = new Socket("localhost", ServerThread.PORT);
        SocketHeartBeat heartBeat = new SocketHeartBeat(
            socket,
            message);
        heartBeat.keepAlive();
        server.stopThread();
        synchronized (this) {
            try {
                wait(20);
            } catch (InterruptedException ie) {
                ;
            }
        }
        socket.close();

        ByteArrayInputStream bais =
                new ByteArrayInputStream(server.getMessage());
        ObjectInputStream ois = new ObjectInputStream(bais);
        assertTrue(Arrays.equals(message, (byte[]) ois.readObject()));
        ois.close();
        bais.close();
    }

}

/**
 * A server thread to create a socket and accepts incoming message.
 */
class ServerThread extends Thread {

    /**
     * ServerSocket listening port.
     */
    public static final int PORT = 12723;

    /**
     * Whether the server is active.
     */
    private boolean active = true;

    /**
     * The incoming message.
     */
    private byte[] message = null;

    /**
     * Stop the thread.
     */
    public void stopThread() {
        active = false;
    }

    /**
     * Get incoming message.
     *
     * @return incoming message
     */
    public byte[] getMessage() {
        return message;
    }

    /**
     * Thread body.
     */
    public void run() {
        try {
            ServerSocket ssocket = new ServerSocket(PORT);
            Socket socket = ssocket.accept();
            ssocket.close();
            while (active) {
                synchronized (this) {
                    wait(10);
                }
            }
            InputStream stream = socket.getInputStream();
            message = new byte[stream.available()];
            stream.read(message);
            stream.close();
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
