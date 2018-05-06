/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.keepalive;

import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.Handler;
import com.topcoder.processor.ipserver.ProcessingException;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageSerializationException;

import java.io.IOException;


/**
 * <p>
 * Handler subclass that implements keep alive handling on the server side.
 * </p>
 *
 * <p>
 * For servers that need to have such capability the user will have to add an instance of this class as handler.
 * </p>
 *
 * <p>
 * This class only overrides the onRequest method. It will simply send back the request message it gets.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>It inherits thread safety from the parent (see <code>Handler</code>).
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class KeepAliveHandler extends Handler {
    /**
     * <p>
     * Convenient public constant that is used as keep alive handler id and keep alive request id.
     * </p>
     */
    public static final String KEEP_ALIVE_ID = "keep_alive";

    /**
     * <p>
     * Constructor. Simply calls the super constructor with no request limits.
     * </p>
     */
    public KeepAliveHandler() {
        super(0);
    }

    /**
     * <p>
     * Notification method that is called when a new keep alive request arrives. Superclass should not override
     * this method.
     * </p>
     *
     * @param connection the connection on which the request arrived.
     * @param request the request message.
     *
     * @throws NullPointerException if any argument is null.
     * @throws IllegalStateException if the server is stopped, when trying to send a response or if the connection is
     *         closed.
     * @throws ProcessingException wraps a fatal application specific exception (note that normal exception should be
     *         reported to the user by wrapping them in the response message, only fatal exceptions that should
     *         terminate the server should throw this exception)
     * @throws IOException if a socket exception occurs while sending the response to the client.
     */
    protected void onRequest(Connection connection, Message request)
        throws ProcessingException, IOException {
        // Argument validation in super method
        super.onRequest(connection, request);

        // Just response with the given request to keeping alive
        try {
            connection.getIPServer().sendResponse(connection.getId(), request);
        } catch (MessageSerializationException e) {
            // wrap it with ProcessingException
            throw new ProcessingException("The message factory fails to serialize the message.", e);
        }
    }
}
