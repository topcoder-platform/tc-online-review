/**
 * TCS Heartbeat
 *
 * OutputStreamHeartBeat.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import java.io.Serializable;
import java.io.OutputStream;
import java.io.ObjectOutputStream;

/**
 * Implementation of a HeartBeat for a specific output stream that will
 * send the specified message and is immutable to avoid threading issues.
 * <p>
 * See the keepAlive() method for details on how the HeartBeat works.
 * </p>
 * @author TCSSubmitter, TCSDeveloper
 * @version 1.0
 */
public class OutputStreamHeartBeat implements HeartBeat {

    /**
     * The output stream that the message will be written to
     */
    private OutputStream outputStream = null;

    /**
     * The object output stream that the message will be written to
     */
    private ObjectOutputStream oos = null;

    /**
     * The message that will be written to the output stream
     */
    private Serializable message = null;

    /**
     * Represents the last exception that occurred.
     */
    private Exception lastException = null;

    /**
     * Constructs the OutputStreamHeartBeat from the specified output stream
     * and message.
     *
     * @param outputStream the output stream to use
     * @param message the message to send
     *
     * @throws NullPointerException if outputStream or message is null
     */
    public OutputStreamHeartBeat(OutputStream outputStream,
                                 Serializable message)
                throws NullPointerException {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is null");
        }
        if (message == null) {
            throw new NullPointerException("message is null");
        }
        this.outputStream = outputStream;
        try {
            oos = new ObjectOutputStream(outputStream);
        } catch (Exception exception) {
            lastException = exception;
        }
        this.message = message;
    }

    /**
     * Returns the output stream that will be used by this HeartBeat.
     *
     * @return the output stream that will be used by this HeartBeat.
     */
    public OutputStream getOutputStream() {
        return outputStream;
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
     * The implementation of the HeartBeat for a Output Stream. This
     * implementation will:
     * <ul>
     * <li>Write the message to the output stream</li>
     * <li>Flush the stream.</li>
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
