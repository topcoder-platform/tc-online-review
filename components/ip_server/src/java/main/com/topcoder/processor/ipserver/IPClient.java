/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import java.io.IOException;

import java.net.InetSocketAddress;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageFactory;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;
import com.topcoder.processor.ipserver.message.MessageSerializationException;

/**
 * <p>
 * The IPClient class represents the client side of this component. This class contains method that can be used by the
 * client side to communicate with the server.
 * </p>
 *
 * <p>
 * There are configuration methods, connect, disconnect and connection query methods and methods for sending and
 * receiving messages in a synchronous or asynchronous manner (blocking or non-blocking).
 * </p>
 *
 * <p>
 * This class also implements a response queueing mechanism. Since all client-server communication can be asychronous,
 * multiple requests can be send before receiving any responses. Even in synchronous mode, the client can send
 * multiple requests to server. The server may respond to them in any order (even for the same handler, because the
 * server calls the handler using different threads). The user has a method to get a message with given the requestId.
 * But if the first available message is not for that request, it must be skipped and another read. Skipping does not
 * mean losing it. It is simply put in the responses queue. Note that queue is not an appropriate term since there is
 * no order for the responses. That's because the server does not guarantee any order for the responses.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>this class has been made thread safe by making access to the connected attribute
 * synchronized on the instance.  The configuration methods are executed inside the synchronized block for the
 * connected attribute set. The access to responses is synchronized too. Checking the size and taking a message is
 * executed atomically.
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class IPClient {
    /**
     * <p>
     * The timeout constant used for blocking mode. Use timeout instead of blocking mode. If we use blocking mode, then
     * the client will never be back if error occurs in the server.
     * </p>
     */
    private static final int TIME_OUT = 500;

    /** The error message while the socket writing is very frequently. */
    private static final String WRITE_ERROR_MESSAGE =
        "A non-blocking socket operation could not be completed immediately";

    /**
     * <p>Represents the message factory to create various types of messages.</p>
     */
    protected MessageFactory messageFactory = null;

    /**
     * <p>
     * The address of the ip server (ip or textual form).
     * </p>
     */
    private String address = null;

    /**
     * <p>
     * The port of the ip server.
     * </p>
     */
    private int port = 0;

    /**
     * <p>
     * Indicates the logical state of the connection (not the actual state). Basically it keeps track of which of the
     * connect or disconnect methods was last called and finished successfully. the initial value should be false.
     * </p>
     */
    private boolean connected = false;

    /**
     * <p>
     * The map that stores all responses that were skipped (that were read during a read with a given requestId).
     * </p>
     *
     * <p>
     * It maps Strings (the request ids of the responses) to the actual responses (Message instances).
     * </p>
     */
    private Map responses = null;

    /** The SocketChannel instance used to connect to IPServer. */
    private SocketChannel sChannel = null;

    /** The selector instance used for blocking mode for timeout implementation. */
    private Selector selector = null;

    /**
     * <p>
     * Constructor that sets the connection parameters (address, port).
     * </p>
     *
     * @param address the address of the ip server (ip or textual form)
     * @param port the port of the ip server.
     * @param namespace the namespace used to create message factory.
     *
     * @throws NullPointerException if the address or namespace argument is null.
     * @throws IllegalArgumentException if the port is not within 0..65535, or the namespace is an empty
     * string (trimmed).
     * @throws ConfigurationException if message factory cannot be created due to configuration error.
     */
    public IPClient(String address, int port, String namespace) throws ConfigurationException {
        if (address == null) {
            throw new NullPointerException("The given address cannot be null.");
        }

        if ((port < 0) || (port > IOHelper.MAX_PORT)) {
            throw new IllegalArgumentException("The given port should be within 0..65535, given port: " + port);
        }

        this.address = address;
        this.port = port;
        this.responses = new HashMap();
        // The constructor of DefaultMessageFactory will validate the namespace and throw ConfigurationException
        // due to configuration error.
        this.messageFactory = new DefaultMessageFactory(namespace);
    }

    /**
     * <p>
     * Gets the address of ip the server.
     * </p>
     *
     * @return returns the address of the ip server.
     */
    public synchronized String getAddress() {
        return this.address;
    }

    /**
     * <p>
     * Set the address of the server. Can only be called if not connected to the server.
     * </p>
     *
     * @param address the address of the server (ip or textual form)
     *
     * @throws NullPointerException if the String argument is null
     * @throws IllegalStateException if the client is connected to the server
     */
    public void setAddress(String address) {
        if (address == null) {
            throw new NullPointerException("The given address cannot be null.");
        }

        synchronized (this) {
            if (isConnected()) {
                throw new IllegalStateException("The client is connected to the server.");
            }

            this.address = address;
        }
    }

    /**
     * <p>
     * Gets the port of the server.
     * </p>
     *
     * @return returns the port of the server.
     */
    public synchronized int getPort() {
        return this.port;
    }

    /**
     * <p>
     * Set the port of the server. Can only be called if not connected to the server.
     * </p>
     *
     * @param port the port of the server
     *
     * @throws IllegalArgumentException if the port is not within 0..65535
     * @throws IllegalStateException if the client is connected to the server
     */
    public void setPort(int port) {
        if ((port < 0) || (port > IOHelper.MAX_PORT)) {
            throw new IllegalArgumentException("The given port should be within 0..65535, given port: " + port);
        }

        synchronized (this) {
            if (isConnected()) {
                throw new IllegalStateException("The client is connected to the server.");
            }

            this.port = port;
        }
    }

    /**
     * <p>
     * Connects to the server.
     * </p>
     *
     * @throws IllegalStateException if the client has already connected to the server.
     * @throws IOException if a socket errors occurs while connecting to the server.
     */
    public void connect() throws IOException {
        synchronized (this) {
            // cannot call connect method if the client had already connected to the server.
            if (isConnected()) {
                throw new IllegalStateException("The client has already connected to the server.");
            }

            // open a socketChannel for connect to the ip server with address, port properties.
            this.sChannel = SocketChannel.open();

            /**
             * Used for blocking implementation. We not use Channel's blocking, for it will block forever for the read
             * method.
             */
            this.selector = Selector.open();

            // configure SocketChannel with blocking style to ensure connect successful
            this.sChannel.configureBlocking(true);

            this.sChannel.connect(new InetSocketAddress(address, port));

            // Register selector only under non-blocking mode
            this.sChannel.configureBlocking(false);
            this.sChannel.register(selector, SelectionKey.OP_READ);

            // well done, then set connected status with true value
            this.connected = true;
        }
    }

    /**
     * <p>
     * Disconnects from the server using SocketChannel.close().
     * </p>
     *
     * @throws IllegalStateException if the client is not connected to the server.
     * @throws IOException if a socket error occurs during closing.
     */
    public void disconnect() throws IOException {
        synchronized (this) {
            if (!isConnected()) {
                throw new IllegalStateException("The client was already disconnected to the server.");
            }

            // Finish current connecting socket
            this.responses.clear();
            this.connected = false;
            this.sChannel.close();
            this.sChannel = null;
            this.selector.close();
            this.selector = null;
        }
    }

    /**
     * <p>
     * Returns whether the client is connected to the server.
     * </p>
     *
     * <p>
     * Indicates the logical state of the connection (not the actual state). Basically it keeps track of which of the
     * connect or disconnect methods was last called and finished successfully.
     * </p>
     *
     * @return true if connected, false otherwise.
     */
    public synchronized boolean isConnected() {
        return this.connected;
    }

    /**
     * Sends a request to the server. Do not wait until the response back.
     *
     * The implementation detail pls refer to IOHelper class.
     *
     * @param request the request to send.
     *
     * @throws NullPointerException if the argument is null.
     * @throws IllegalStateException if the client is not connected to the ip server.
     * @throws IOException if a socket error occurs during sending data.
     * @throws MessageSerializationException if the message factory fails to serialize the request.
     */
    public void sendRequest(Message request)
        throws IOException, MessageSerializationException {
        if (request == null) {
            throw new NullPointerException("The given request cannot be null.");
        }

        if (!isConnected()) {
            throw new IllegalStateException("The client was disconnected to the server.");
        }

        // Wraps byte array size and byte array contents into a buffer and write the result to the channel.
        try {
            this.sChannel.write(IOHelper.wrapMessage(request, messageFactory));
        } catch (IOException e) {
            // In stress test. While sending request, recieve response in very frequently speed. The below exception
            // will be thrown, In these cases. We should slow down the send request speed of this client a little.
            if (e.getMessage().indexOf(WRITE_ERROR_MESSAGE) >= 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } else {
                throw e;
            }
        }
    }

    /**
     * <p>
     * Reads from the server the first available response.
     * </p>
     *
     * <p>
     * If the response queue is not empty, a message is taken from there. Only if the queue is empty, the message is
     * read from the server.
     * </p>
     *
     * @param blocking whether the call is blocking (waits until a response is available) or non-blocking (returns null
     *        if a response is not immediately available)
     *
     * @return the read response or null if none is available immediately in non-blocking mode
     *
     * @throws IllegalStateException IllegalStateException if the client is not connected to the server
     * @throws IOException if a socket error occurs while reading data.
     */
    public synchronized Message receiveResponse(boolean blocking) throws IOException {
        if (!isConnected()) {
            throw new IllegalStateException("cannot receive response from server while the client was disconnected "
                    + "to the server.");
        }

        if (this.responses.size() == 0) {
            // Try to read from server if responses queue size is 0
            if (blocking) {
                this.readResponses(null);
            } else {
                return IOHelper.readMessage(this.sChannel, messageFactory);
            }
        }

        return getResponse();
    }

    /**
     * <p>
     * Reads from the server a response given the request id.
     * </p>
     *
     * <p>
     * If the response queue is not empty, a message is looked up there first. If the queue is empty or no message was
     * found, messages are read from the server until one is found. In non-blocking mode messages are read but only as
     * long as they are available. If a message is read but does not match the requestId, it is put in the queue for
     * future receiveResponse calls.
     * </p>
     *
     * @param requestId the request id.
     * @param blocking whether the call is blocking (waits until a response is available) or non-blocking (returns null
     *        if a response is not immediately available)
     *
     * @return the read response or null if none is available immediately in non-blocking mode. may return NULL in
     * blocking mode if there was no matching response received within the specified timeout.
     *
     * @throws NullPointerException if the requestId is null.
     * @throws IllegalStateException IllegalStateException if the client is not connected to the server.
     * @throws IOException if a socket error occurs while reading data.
     */
    public Message receiveResponse(String requestId, boolean blocking)
        throws IOException {
        if (requestId == null) {
            throw new NullPointerException("requestId cannot be null.");
        }

        synchronized (this) {
            if (!isConnected()) {
                throw new IllegalStateException("cannot receive response from server while the client was disconnected "
                        + "to the server.");
            }

            // Try to get response with given requestId from reponses queue
            Message response = (Message) this.responses.remove(requestId);
            if (response != null) {
                return response;
            }

            if (blocking) {
                // read response by using selector for given requestId
                readResponses(requestId);
            } else {
                // read response in non-blocking mode
                if ((response = IOHelper.readMessage(sChannel, messageFactory)) != null) {
                    // Put response to queue, needn't to check if it is corresponding response
                    this.responses.put(response.getRequestId(), response);
                }
            }

            return (Message) responses.remove(requestId);
        }
    }

    /**
     * Remove and return the first response from responses.
     *
     * @return the first response from responses map. Null will be returned if size is 0.
     */
    private synchronized Message getResponse() {
        if (this.responses.size() == 0) {
            return null;
        }
        // Pick the first message from queue and remove it
        Iterator it = this.responses.entrySet().iterator();
        Message result = (Message) ((Map.Entry) it.next()).getValue();
        it.remove();

        return result;
    }

    /**
     * Read response from ipserver with given requestId, if requestId is null, any responses can be read
     * and return immediately. Put all responses that recieved to responses map.
     *
     * @param requestId the requestId of corresponding response that will be recieved, null means
     * any responses from ipserver.
     * @throws IOException if error occurs while reading response from ipserver.
     */
    private void readResponses(String requestId) throws IOException {
        // try to get response in timeout. 0 means no response from ipserver
        boolean find = false;
        while (!find && this.selector.select(IPClient.TIME_OUT) > 0) {
            Iterator it = this.selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();
                it.remove();

                if (key.isReadable()) {
                    final SocketChannel client = (SocketChannel) key.channel();
                    Message response = null;
                    while ((response = IOHelper.readMessage(client, messageFactory)) != null) {
                        synchronized (this) {
                            this.responses.put(response.getRequestId(), response);
                        }

                        if (requestId == null || (requestId.equals(response.getRequestId()))) {
                            find = true;
                        }
                    }
                }
            }
        }
    }
}
