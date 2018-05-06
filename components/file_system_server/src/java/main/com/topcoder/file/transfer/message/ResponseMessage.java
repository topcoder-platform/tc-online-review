/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

/**
 * This class represents the response message sent by the IPServer, using FileSystemHandler as a response to an
 * operation on the server side. It has an object result initialized in the constructor, or an exception, in case the
 * operation was errored. The constructor that receives the result argument validates the object according to the
 * message type provided. The class is immutable.
 * @author Luca, FireIce
 * @version 1.0
 */
public class ResponseMessage extends FileSystemMessage {

    /**
     * Represents the result of this message. initialized in the constructor and never changed later. Can be null.
     * Accessible through the getter.
     */
    private final Object result;

    /**
     * Represents the exception of this message. initialized in the constructor and never changed later. Can be null.
     * Accessible through the getter.
     */
    private final Exception exception;

    /**
     * Creates an instance with the given arguments. this constructor is not used in this component. It is provided
     * because the message factory uses this constructor.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @throws NullPointerException
     *             if any argument is null
     */
    public ResponseMessage(String handlerId, String requestId) {
        super(handlerId, requestId);
        result = null;
        exception = null;
    }

    /**
     * Creates an instance with the given arguments. Performs checking for the result, according to the message type and
     * throws IllegalArgumentException in case the result should not be null.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @param type
     *            the type of the message
     * @throws NullPointerException
     *             if any argument is null except for the result
     * @throws IllegalArgumentException
     *             in case the result is not valid
     */
    public ResponseMessage(String handlerId, String requestId, MessageType type) {
        this(handlerId, requestId, type, (Object) null);
    }

    /**
     * Creates an instance with the given arguments. Performs checking for the result, according to the message type and
     * throws IllegalArgumentException in case the result is not valid.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @param type
     *            the type of the message
     * @param result
     *            the result of the message
     * @throws NullPointerException
     *             if any argument, except result, is null
     * @throws IllegalArgumentException
     *             in case the result is not valid
     */
    public ResponseMessage(String handlerId, String requestId, MessageType type, Object result) {
        super(handlerId, requestId, type);
        this.result = result;
        // performed the result argument checking according to the rules specified in the
        // public constants declared by the MessageType and BytesMessageType
        if (!MessageTypeValidator.validate(this)) {
            throw new IllegalArgumentException("result is not valid");
        }
        exception = null;
    }

    /**
     * Creates an instance with the given arguments. This constructor should be used on the server side if the request
     * raised an exception, in order to send the exception to the client.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @param type
     *            the type of the message
     * @param exception
     *            the exception of the message
     * @throws NullPointerException
     *             if any argument is null
     */
    public ResponseMessage(String handlerId, String requestId, MessageType type, Exception exception) {
        super(handlerId, requestId, type);
        if (exception == null) {
            throw new NullPointerException("exception is null");
        }
        this.exception = exception;
        result = null;
        // performed the result argument checking according to the rules specified in the
        // public constants declared by the MessageType and BytesMessageType
        if (!MessageTypeValidator.validate(this)) {
            throw new IllegalArgumentException("result is not valid");
        }
    }

    /**
     * Returns the result of this message. It can return null.
     * @return the result of this message
     */
    public Object getResult() {
        return result;
    }

    /**
     * Returns the exception of this message. It can return null. Simply return the corresponding field.
     * @return the exception of this message
     */
    public Exception getException() {
        return exception;
    }
}
