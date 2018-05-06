/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.processor.ipserver.failuretests;

import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.Handler;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.ProcessingException;

import java.io.IOException;

/**
 * <p>A subclass of <code>Handler</code> class to be used to test the protected methods of the super class. Overrides
 * the protected methods declared by a super-class. The overridden methods are declared with package private access so
 * only the failure test cases could invoke them. The overridden methods simply call the corresponding method of a
 * super-class.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
class HandlerSubclass extends Handler {

    /**
     * <p>Constructs new <code>HandlerSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @param maxRequests maximum number of requests that can be serviced by onRequest at one time (0 means no limit)
     * @throws IllegalArgumentException if the argument is less than 0
     */
    HandlerSubclass(int maxRequests) {
        super(maxRequests);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param connection the new connection
     * @throws NullPointerException if the argument is null
     * @throws ProcessingException wraps a fatal application specific exception (note that normal exception should be
     * reported to the user by wrapping them in the response message, only fatal exceptions that should terminate the
     * server should throw this exception)
     */
    public void onConnect(Connection connection) throws ProcessingException {
        super.onConnect(connection);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
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
    public void onRequest(Connection connection, Message request) throws ProcessingException, IOException {
        super.onRequest(connection, request);
    }
}
