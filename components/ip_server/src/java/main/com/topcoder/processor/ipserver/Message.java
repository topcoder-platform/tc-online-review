/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import java.io.Serializable;


/**
 * <p>
 * The Message class represents the messages that are sent between client and server.
 * </p>
 *
 * <p>
 * A message can be either a request (client to server) or response (server to client). But this only applies to a
 * typical client server architecture where the server responds to requests. It can be the other way around in some
 * situations or even mixed. For example, the client connects and the server sends the id yourself request.
 * </p>
 *
 * <p>
 * For this reason and because requests and responses have the same structure, we have a general Message class instead
 * of two Request and Response classes.
 * </p>
 *
 * <p>
 * Note that almost always the applications will wish to subclass this class and add useful information in them. This
 * class will only be useful for requests and responses that carry no data (a good example is the KeepAlive
 * implementation). The subclasses will have to contain only serializable attributes (or mark with transient the
 * unserializable ones) and must have a default constructor that can be used for deserialization.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>this class has been made thread safe by being immutable.
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 */
public class Message implements Serializable {
    /**
     * <p>
     * The id of the handler that is supposed to handle this request. Set in the constructor.
     * </p>
     *
     * <p>
     * If the message is a request, the client provides this value. If the message is a response, the handler will copy
     * it from the request.
     * </p>
     */
    private String handlerId = null;

    /**
     * <p>
     * The id of the request. Since all client-server communication is asychronous, multiple requests can be send
     * before receiving any responses. So when a response does come back, the request id will be copied here so that
     * the client know which request was responded to.
     * </p>
     *
     * <p>
     * It is very important to understand the double role of this attribute. If the message is a request, then the
     * client provides this value. If the message is a response, then the handler will copy the id from the request in
     * this attribute.
     * </p>
     */
    private String requestId = null;

    /**
     * <p>
     * Empty Constructor. Needed for deserialization. Not to be called directly.
     * </p>
     */
    public Message() {
    }

    /**
     * <p>
     * Construct a new Message with given handlerId, requestId arguments.
     * </p>
     *
     * <p>
     * This construct can be called either by the user for a request or by the handler subclasses for a response to a
     * request.
     * </p>
     *
     * @param handlerId the id of handler to handle the request.
     * @param requestId the request id.
     *
     * @throws NullPointerException if any argument is null.
     */
    public Message(String handlerId, String requestId) {
        if (handlerId == null) {
            throw new NullPointerException("handlerId cannot be null.");
        }

        if (requestId == null) {
            throw new NullPointerException("requestId cannot be null.");
        }

        this.handlerId = handlerId;
        this.requestId = requestId;
    }

    /**
     * <p>
     * Gets the id of the handler that is supposed to handle this request.
     * </p>
     *
     * @return the id of the handler that is supposed to handle this request
     */
    public String getHandlerId() {
        return this.handlerId;
    }

    /**
     * <p>
     * Gets the id of the request (the value of the requestId attribute).
     * </p>
     *
     * <p>
     * Since all client-server communication is asychronous, multiple requests can be send before receiving any
     * responses. So when a response does come back, the request id will be copied here so that the client know which
     * request was responded to.
     * </p>
     *
     * <p>
     * It is very important to understand the double role of this attribute. If the message is a request, then the
     * client provides this value. If the message is a response, then the handler will copy the id from the request in
     * this attribute.
     * </p>
     *
     * @return the id of the request
     */
    public String getRequestId() {
        return this.requestId;
    }
}
