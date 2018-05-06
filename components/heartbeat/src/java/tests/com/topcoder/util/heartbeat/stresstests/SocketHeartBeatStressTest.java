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
 * Contains stress tests for the SocketHeartBeat class.
 * @author mathgodleo
 * @version 1.0
 */
public class SocketHeartBeatStressTest extends TestCase {
    /**
     * The number of servers to run
     */
    private static final int NUM_SERVERS = 5;

    /**
     * The port number to start the servers at (1st server will be on PORT, 2nd on PORT + 1, etc)
     */
    private static final int PORT = 23523;

    /**
     * # of times/server to send a heartbeat
     */
    private static final int NUM_ITERATIONS = 100;

    /**
     * The heartbeat message
     */
    private static final byte[] MESSAGE = "HELLO".getBytes();

    /**
     * Creates NUM_SERVERS and sends NUM_ITERATIONS heartbeats to each server. Makes sure each
     * heartbeat is recieved and that at least 20 heartbeats / server / second are processed.
     */
    public void testSocketHeartBeat(){

        try {
            Server[] servers = new Server[NUM_SERVERS];
            Socket[] sockets = new Socket[NUM_SERVERS];
            SocketHeartBeat[] heartbeats = new SocketHeartBeat[NUM_SERVERS];

            // initialize servers, sockets, and heartbeats
            for (int i = 0; i < NUM_SERVERS; i++) {
                servers[i] = new Server(PORT + i);
                servers[i].start();
                sockets[i] = new Socket(InetAddress.getByName("127.0.0.1"), PORT + i);
                heartbeats[i] = new SocketHeartBeat(sockets[i], MESSAGE);
            }

            // time sending NUM_ITERATIONS heartbeats to each server
            long time = System.currentTimeMillis();

            for (int i = 0; i < NUM_ITERATIONS; i++) {
                for (int j = 0; j < NUM_SERVERS; j++) {
                    heartbeats[j].keepAlive();
                }
            }

            time = System.currentTimeMillis() - time;

            assertTrue("should do >= 20 heartbeats / server / seconds", 
                       NUM_ITERATIONS / (time / 1000.0) >= 20.0);

            // get bytes read by each server and compare them to what was sent
            byte[][] heartbeatMessages = new byte[NUM_SERVERS][];
            for (int i = 0; i < NUM_SERVERS; i++) {
                heartbeatMessages[i] = servers[i].out.toByteArray();
            }
            
            
            for (int i = 0; i < NUM_SERVERS; i++) {
                    
                // make sure number of bytes is correct
                assertEquals("wrong length of recieved messages", 
                             MESSAGE.length * NUM_ITERATIONS, 
                             heartbeatMessages[i].length);

                // make sure actual bytes are correct
                for (int j = 0; j < NUM_ITERATIONS; j++) {
                    for (int k = 0; k < MESSAGE.length; k++) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(k).append("th byte of ").append(j);
                        sb.append("th iteration of server ").append(i);
                        sb.append(" is incorrect");
                        assertEquals(sb.toString(), MESSAGE[k], heartbeatMessages[i][j * MESSAGE.length + k]);
                    }
                }
            }

        } catch (Exception ex) {
            fail("shouldn't have thrown exception: " + ex.getMessage());
        }
    }

    /**
     * A simple server used to stress test the SocketHeartBeat class. Simply
     * accepts one connection and then writes object that it reads from the connection
     * to the bpulic 'out' field.
     */
    private class Server extends Thread {
        /**
         * The port that this server is open on
         */
        private int port;

        /**
         * The output stream to which to write all input from the connected socket
         */
        public ByteArrayOutputStream out = new ByteArrayOutputStream();

        /**
         * Starts a server on the specified port
         */
        public Server(int port){
            this.port = port;
        }

        /**
         * Accepts a single connection and then writes all input from that connection to the 'out' field
         */
        public void run(){
            ServerSocket server = null;
            Socket socket = null;
            try {
                server = new ServerSocket(port);
                socket = server.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    out.write((byte[])in.readObject());                    
                }
            } catch (Exception ex) {
            }
        }
    }
}
