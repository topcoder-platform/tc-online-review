/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.keepalive;

import com.topcoder.processor.ipserver.IPClient;
import com.topcoder.processor.ipserver.ConfigurationException;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageCreationException;

import com.topcoder.util.heartbeat.HeartBeat;
import com.topcoder.util.heartbeat.HeartBeatManager;

import java.io.IOException;


/**
 * <p>
 * Subclass of the IPClient class that implements a client that keeps alive a connection by sending dummy messages at
 * regular intervals.
 * </p>
 *
 * <p>
 * The class overrides the connect and disconnect methods. The connect method calls the super connect and then starts
 * the keep alive thread in the background (using the Heartbeat component). The disconnect method calls the super
 * disconnect and stops the keep alive thread.
 * </p>
 *
 * <p>
 * The keepAlive method (called at regular intervals by Heartbeat) simply sends a dummy Message and reads the response.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>this class has been made thread safe by marking the keepAlive methods as synchronized to
 * avoid sending keep-alive messages before the responses are received (in case the latency is greater then the
 * keep-alive delay). The other methods inherit thread safety from the parent (see <code>IPClient</code>).
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class KeepAliveIPClient extends IPClient implements HeartBeat {
    /**
     * <p>
     * The name of the message used by this keep alive client.
     * </p>
     */
    private static final String MESSAGE_TYPE_NAME = "KeepAlive";

    /**
     * <p>
     * The delay in milliseconds between keep alive messages. Set in the constructor with a positive value.
     * </p>
     */
    private int delay = 0;

    /**
     * <p>
     * The heartbeat manager. Used in connect to start the heartbeat and in disconnect to stop the heartbeat. Created
     * in the constructor.
     * </p>
     */
    private HeartBeatManager heartBeatManager = null;

    /**
     * <p>
     * Represents the last exception that occured in keepAlive method.
     * </p>
     */
    private Exception lastException = null;

    /**
     * <p>
     * Constructor that sets up the client with host, port and delay (delay is for heartBeat). The heartBeat will
     * invoking keepAlive method in delay interval.
     * </p>
     *
     * @param host the address of the server
     * @param port the port of the server
     * @param delay the delay between keep alives
     * @param namespace the namespace to configure the message factory.
     *
     * @throws NullPointerException if the host or namespace is null.
     * @throws IllegalArgumentException if the port is not between 0..65535 or if the delay is not strictly positive
     *  or the namespace is an empty string.
     * @throws ConfigurationException if message factory cannot be created due to configuration error.
     */
    public KeepAliveIPClient(String host, int port, int delay, String namespace) throws ConfigurationException {
        super(host, port, namespace);

        // Delay cannot be non-positive
        if (delay <= 0) {
            throw new IllegalArgumentException("The given delay cannot be non-positive");
        }

        this.heartBeatManager = new HeartBeatManager();
        this.delay = delay;
    }

    /**
     * <p>
     * Connects to the server.
     * </p>
     *
     * @throws IllegalStateException if the client is already connected to the server.
     * @throws IOException if a socket exception occurs while connecting to the server.
     */
    public void connect() throws IOException {
        super.connect();
        this.heartBeatManager.add(this, delay);
    }

    /**
     * <p>
     * Disconnects from the server.
     * </p>
     *
     * @throws IllegalStateException if the client is not connected to the server.
     * @throws IOException if a socket exception occurs while disconnecting from the server.
     */
    public void disconnect() throws IOException {
        this.heartBeatManager.remove(this);
        super.disconnect();
    }

    /**
     * <p>
     * Method called by the Heartbeat component at regular time intervals (delay milliseconds).
     * </p>
     *
     * <p>
     * If the client is connected to the server, it will send a keep alive message and wait for a response. Any errors
     * will cause disconnect. Keep alives must not cause any problems since they are so basic. If an error occurs then
     * it means something serious has occured so everything should be stopped. The user can debug it using
     * getLastException.
     * </p>
     */
    public synchronized void keepAlive() {
        if (isConnected()) {
            Message response = null;

            try {
                sendRequest(messageFactory.getMessage(
                    MESSAGE_TYPE_NAME, KeepAliveHandler.KEEP_ALIVE_ID, KeepAliveHandler.KEEP_ALIVE_ID));

                response = receiveResponse(KeepAliveHandler.KEEP_ALIVE_ID, true);
            } catch (IOException e) {
                // disconnect on any problem
                this.lastException = e;
            } catch (MessageCreationException e) {
                // disconnect on any problem
                this.lastException = e;
            }

            // Cannot get reponse that means something serious has occured so everything should be stopped.
            // Both exception and cannot recieve response will cause response is null
            if (response == null) {
                try {
                    disconnect();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    /**
     * <p>
     * Returns the last thrown exception in keepAlive.
     * </p>
     *
     * @return last exception thrown in keepAlive,  may return NULL if no exception has occurred so far.
     */
    public Exception getLastException() {
        return this.lastException;
    }
}
