/**
 * TCS Heartbeat
 *
 * SocketHeartBeat.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Implementation of a HeartBeat for a specific socket that will send the
 * specified message and is immutable to avoiding threading issues.
 * <p>
 * See the keepAlive() method for details on how the HeartBeat works.
 * </p>
 *
 * @author TCSSubmitter, TCSDeveloper
 * @version 1.0
 */
public class SocketHeartBeat implements HeartBeat {

    /**
     * The socket that will be used by this HeartBeat to send the message.
     */
    private Socket socket = null;

    /**
     * The message that will be written to the socket
     */
    private Serializable message = null;

    /**
     * The object output stream that the message will be written into
     */
    private ObjectOutputStream oos = null;

    /**
     * Represents the last exception that occurred.
     */
    private Exception lastException = null;

    /**
     * Constructs the SocketHeartBeat from the specified socket and message.
     *
     * @param socket the socket to use
     * @param message the message to send
     *
     * @throws NullPointerException if socket or message is null
     */
    public SocketHeartBeat(Socket socket,
                           Serializable message)
            throws NullPointerException {
        if (socket == null) {
            throw new NullPointerException("socket is null");
        }
        if (message == null) {
            throw new NullPointerException("message is null");
        }
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception exception) {
            lastException = exception;
        }
        this.message = message;
    }

    /**
     * Returns the socket that will be used by this HeartBeat.
     *
     * @return the socket that will be used by this HeartBeat.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Returns the message that will be sent on each heart beat.
     *
     * @return the message that will be sent on each heart beat.
     */
    public Serializable getMessage() {
        return message;
    }

    /**
     * Returns the last exception that occurred in the keepAlive() method. Will
     * return null if the last keepAlive() method invocation was successful.
     *
     * @return the last exception that occurred or null if none occurred.
     */
    public Exception getLastException() {
        return lastException;
    }

    /**
     * The implementation of the HeartBeat for a Socket. This implementation
     * will:
     * <ul>
     * <li>Write the message to the socket.</li>
     * <li>Flush the output stream.</li>
     * <li>Set the lastException variable to null.</li>
     * </ul>
     * <p>
     * If an exception occurs during this process, the output stream (if
     * created) will be closed and the exception will be saved in the
     * lastException variable.
     * </p>
     */
    public void keepAlive() {
        try {
            oos.writeObject(message);
            oos.flush();
            lastException = null;
        } catch (Exception exception) {
            lastException = exception;
        }
    }

}
