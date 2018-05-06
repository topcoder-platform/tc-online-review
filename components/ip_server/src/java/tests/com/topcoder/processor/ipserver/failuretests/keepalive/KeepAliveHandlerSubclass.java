/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.processor.ipserver.failuretests.keepalive;

import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.ProcessingException;
import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;

import java.io.IOException;

/**
 * <p>A subclass of <code>KeepAliveHandler</code> class to be used to test the protected methods of the super class.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private
 * access so only the failure test cases could invoke them. The overridden methods simply call the corresponding method
 * of a super-class.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
class KeepAliveHandlerSubclass extends KeepAliveHandler {

    /**
     * <p>Constructs new <code>KeepAliveHandlerSubclass</code> instance. Nothing special occurs here.</p>
     */
    KeepAliveHandlerSubclass() {
        super();
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
     * @throws ProcessingException wraps a fatal application specific exception (note that normal exception should be
     * reported to the user by wrapping them in the response message, only fatal exceptions that should terminate the
     * server should throw this exception)
     * @throws IOException if a socket exception occurs while sending the response to the client
     */
    public void onRequest(Connection connection, Message request) throws ProcessingException, IOException {
        super.onRequest(connection, request);
    }

}
