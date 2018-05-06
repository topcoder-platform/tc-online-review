/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

/**
 * This class represents the request message sent by the FileSystemClient in order to perform an operation on the server
 * side. It has an array of objects initialized in the constructor. The constructor that receives the args argument
 * validates the array according to the message type provided. The class is immutable.
 * @author Luca, FireIce
 * @version 1.0
 */
public class RequestMessage extends FileSystemMessage {

    /**
     * Represents the arguments of this message. Initialized in the constructor and never changed later. Can be null.
     * Accessible through the getter.
     */
    private final Object[] args;

    /**
     * Creates an instance with the given arguments. This constructor is not used in this component. It is provided
     * because the message factory uses this constructor.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @throws NullPointerException
     *             if any argument is null
     */
    public RequestMessage(String handlerId, String requestId) {
        super(handlerId, requestId);
        args = null;
    }

    /**
     * Creates an instance with the given arguments. Performs checking for the args, according to the message type and
     * throws IllegalArgumentException in case the args should be something different than a null object array.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @param type
     *            the type of the message
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if args should be something different than a null object array.
     */
    public RequestMessage(String handlerId, String requestId, MessageType type) {
        super(handlerId, requestId, type);
        args = null;
        if (!MessageTypeValidator.validate(this)) {
            throw new IllegalArgumentException("args should be null object array for RequestMessage " + type.getType());
        }
    }

    /**
     * Creates an instance with the given arguments. Performs checking of the arg according to the cacheRequestType and
     * throws IllegalArgumentException in case the arguments for the request type should be something else than the
     * provided arg.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @param type
     *            the type of the message
     * @param arg
     *            the argument of the message
     * @throws NullPointerException
     *             if any argument (except the arg) is null
     * @throws IllegalArgumentException
     *             in case the arg is not valid
     */
    public RequestMessage(String handlerId, String requestId, MessageType type, Object arg) {
        super(handlerId, requestId, type);
        args = new Object[1];
        args[0] = arg;
        if (!MessageTypeValidator.validate(this)) {
            throw new IllegalArgumentException("args is not valid for RequestMessage " + type.getType());
        }
    }

    /**
     * Creates an instance with the given arguments. Performs checking for the args, according to the message type and
     * throws IllegalArgumentException in case the args are not valid.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @param type
     *            the type of the message
     * @param args
     *            the arguments of the message
     * @throws NullPointerException
     *             if any argument (except the args) is null
     * @throws IllegalArgumentException
     *             in case the args are not valid
     */
    public RequestMessage(String handlerId, String requestId, MessageType type, Object[] args) {
        super(handlerId, requestId, type);
        this.args = args;
        if (!MessageTypeValidator.validate(this)) {
            throw new IllegalArgumentException("args is not valid for RequestMessage " + type.getType());
        }
    }

    /**
     * Returns the arguments of this message. It can return null. Returns a new array, with the same elements.
     * @return the arguments of this message
     */
    public Object[] getArgs() {
        if (args == null) {
            return null;
        } else {
            Object[] result = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                result[i] = args[i];
            }
            return result;
        }
    }
}
