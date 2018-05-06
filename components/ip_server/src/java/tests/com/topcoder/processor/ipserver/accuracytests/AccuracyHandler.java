/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.ProcessingException;
import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;

import java.io.IOException;


/**
 * A KeepAliveHandler subclass, which simply saves the arguments in the
 * onRequest and onConnect methods.
 *
 * @author air2cold, WishingBone
 * @version 2.0
 */
public class AccuracyHandler extends KeepAliveHandler {
    /**
     * The connection set in the onConnect method.
     */
    private Connection onConnect = null;

    /**
     * The connection set in the onRequest method.
     */
    private Connection onRequest = null;

    /**
     * The request set in the onRequest method.
     */
    private Message request = null;

    /**
     * Empty constructor.
     */
    public AccuracyHandler() {
    }

    /**
     * Return the connection set in the onConnect method.
     *
     * @return the connection set in the onConnect method.
     */
    public Connection getOnConnect() {
        return onConnect;
    }

    /**
     * Return the connection set in the onRequest method.
     *
     * @return the connection set in the onRequest method.
     */
    public Connection getOnRequest() {
        return onRequest;
    }

    /**
     * Return the request set in the onRequest method.
     *
     * @return the request set in the onRequest method.
     */
    public Message getRequest() {
        return request;
    }

    /**
     * Called in the handleRequest method.
     *
     * @param conn the Connection.
     * @param request the request message.
     */
    protected void onRequest(Connection conn, Message request)
        throws ProcessingException, IOException {
        super.onRequest(conn, request);
        this.onRequest = conn;
        this.request = request;
    }

    /**
     * Called in the IPServer.run method.
     *
     * @param conn the connection.
     */
    protected void onConnect(Connection conn) throws ProcessingException {
        super.onConnect(conn);

        this.onConnect = conn;
    }
}
