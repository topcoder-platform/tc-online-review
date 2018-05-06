/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import com.topcoder.processor.ipserver.message.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <p>An implementation of a "malicious" client initiating a "buffer overflow" attack on the tested server.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class MaliciousIPClient {

    /**
     * <p>A <code>String</code> providing the address of the server to connect to.</p>
     */
    private String address = null;

    /**
     * <p>An <code>int</code> providing a port of the ip server.</p>
     */
    private int port;

    /**
     * <p>A <code>SocketChannel</code> providing the connection to server.</p>
     */
    private SocketChannel channel = null;

    /**
     * <p>Constructs new <code>MaliciousIPClient</code>.</p>
     *
     * @param address the address of the server.
     * @param port the port of the server.
     */
    public MaliciousIPClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * <p>Connects to the server.</p>
     * @throws IOException to JUnit
     */
    public synchronized void connect() throws IOException {
        channel = SocketChannel.open();
        channel.configureBlocking(true);
        channel.connect(new InetSocketAddress(address, port));
    }

    /**
     * <p>Disconnects from the server.</p>
     * @throws IOException to JUnit
     */
    public synchronized void disconnect() throws IOException {
        channel.close();
        channel = null;
    }

    /**
     * <p>Sends a request to the server. Sets a large value as a size of the message.</p>
     *
     * @param request the request to send.
     * @throws IOException if a socket error occurs during sending request.
     */
    public synchronized void sendRequest(Message request) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(request);
        byte[] bytes = out.toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(4 + bytes.length);
        buffer.putInt(Integer.MAX_VALUE);
        buffer = buffer.put(bytes);
        buffer.flip();

        channel.write(buffer);
    }

    /**
     * <p>Gets response from the server.</p>
     *
     * @param requestId the request id.
     * @return the read response or null if none is available immediately in non-blocking mode.
     * @throws IOException if a socket error occurs while reading data.
     */
    public Message receiveResponse(String requestId) throws IOException {
        channel.configureBlocking(false);

        while (true) {

            ByteBuffer buffer = ByteBuffer.allocate(4);

            int size = channel.read(buffer);
            buffer.flip();
            if (size != 4) {
                try {
                    Thread.sleep(100);
                } catch (Exception e){
                }
                continue;
            }
            size = buffer.getInt();
            if (size <= 0) {
                return null;
            }
            buffer = ByteBuffer.allocate(size);
            size = channel.read(buffer);
            if (size <= 0) {
                return null;
            }

            buffer.flip();

            try {
                Message response =
                    (Message) new ObjectInputStream(new ByteArrayInputStream(buffer.array())).readObject();

                if (response.getRequestId().equals(requestId)) {
                    return response;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }
}
