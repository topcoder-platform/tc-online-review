/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import com.topcoder.processor.ipserver.Handler;
import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageSerializationException;
import com.topcoder.processor.ipserver.ProcessingException;

import java.io.IOException;


/**
 * A simple implementation of Handler.
 *
 * @author air2cold, WishingBone
 * @version 2.0
 */
public class MaxHandler extends Handler {
    /**
     * Creates a new MaxHandler object.
     *
     * @param maxRequests the maximum requests.
     */
    public MaxHandler(int maxRequests) {
        super(maxRequests);
    }

    /**
     * <p> Notification method that is called when a new client request arrives. The subclasses should implement this
     * method. They should do whatever processing is required and then use connection.getIPServer().sendResponse (zero
     * to many times as needed) to send the response back to the client. </p>
     *
     * @param connection the connection on which the request arrived
     * @param request the request message
     * @throws NullPointerException if any argument is null
     * @throws IllegalStateException if the server is stopped, when trying to send a response or if the connection is
     * closed
     * @throws com.topcoder.processor.ipserver.ProcessingException wraps a fatal application specific exception (note
     * that normal exception should be reported to the user by wrapping them in the response message, only fatal
     * exceptions that should terminate the server should throw this exception)
     * @throws java.io.IOException if a socket exception occurs while sending the response to the client
     */
    protected void onRequest(Connection connection, Message request) throws ProcessingException, IOException {
        super.onRequest(connection, request);

        // Just respond with the given request to keeping alive
        try {
            connection.getIPServer().sendResponse(connection.getId(), request);
        } catch (MessageSerializationException mse) {
            throw new ProcessingException("Failed to process the message.", mse);
        }
    }
}
