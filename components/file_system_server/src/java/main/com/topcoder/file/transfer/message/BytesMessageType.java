/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

/**
 * This class declares the types of messages currently supported by this component that involve bytes transfer. Each
 * instance of this class has a message type name associated with it (the type:String serves to determine the hashCode
 * and whether two instances are equal). The class is immutable. The public constants declare the rules of validation
 * that should be performed inside the RequestMessage and ResponseMessage's constructors.
 * @author Luca, FireIce
 * @version 1.0
 */
public class BytesMessageType extends MessageType {

    /**
     * Represents the START_UPLOAD_FILE_BYTES message type. The message of this type is sent by the client exactly after
     * the file upload has been aproved (the client send a check upload message before) in order to start the upload.
     * The message will contain the fileName and, in case a file is overwritten, the fileId of the overwritten file. The
     * server will reply with a message of this type with an array containing fileId and the fileCreationId result. ---
     * RequestMessage validation: args.length==1 and args[0] instanceof String and args[0].length>0 or args.length==2
     * and args[0] instanceof String and args[0].length>0 and args[1] instanceof String and args[1].length>0 ---
     * ResponseMessage validation: result instanceof Object[] and result.length==2 and result[0] instanceof String and
     * result[0].length>0 ...and result[1] instanceof String and result[1].length>0
     */
    public static final BytesMessageType START_UPLOAD_FILE_BYTES = new BytesMessageType("START_UPLOAD_FILE_BYTES");

    /**
     * Represents the UPLOAD_FILE_BYTES message type. The message of this type is sent by the client several times after
     * the start upload file bytes message. The message will contain the fileCreationId and the array of bytes to be
     * appended to the file on the server. The server will reply with a message of this type containing nothing. ---
     * RequestMessage validation: args.length==2 and args[0] instanceof String and args[0].length>0 and args[1]
     * instanceof byte[] and args[1].length>0 --- ResponseMessage validation: result==null
     */
    public static final BytesMessageType UPLOAD_FILE_BYTES = new BytesMessageType("UPLOAD_FILE_BYTES");

    /**
     * Represents the STOP_UPLOAD_FILE_BYTES message type. The message of this type is sent by the client after all
     * upload file bytes messages have been sent. The message will contain the fileCreationId. The server will reply
     * with a message of this type containing nothing. --- RequestMessage validation: args.length==1 and args[0]
     * instanceof String and args[0].length>0 --- ResponseMessage validation: result==null
     */
    public static final BytesMessageType STOP_UPLOAD_FILE_BYTES = new BytesMessageType("STOP_UPLOAD_FILE_BYTES");

    /**
     * Represents the START_RETRIVE_FILE_BYTES message type. The message of this type is sent by the client after the
     * message of MessageType.RETRIEVE_FILE type it sends and after the message of this type received from the server as
     * a response. The message will contain the bytesIteratorId. The message from the server (sent before the client
     * sends this message) contains the fileName and the bytesIteratorId. --- RequestMessage validation: args.length==1
     * and args[0] instanceof String and args[0].length>0 --- ResponseMessage validation: result instanceof Object[] and
     * result.length==2 and result[0] instanceof String and result[0].length>0 ...and result[1] instanceof String and
     * result[1].length>0
     */
    public static final BytesMessageType START_RETRIEVE_FILE_BYTES = new BytesMessageType("START_RETRIEVE_FILE_BYTES");

    /**
     * Represents the RETRIVE_FILE_BYTES message type. The message of this type is sent by the client as a response to
     * messages of this type received from the server. If the server has no more bytes to send, it will send a message
     * of BytesMessageType.STOP_RETRIEVE_FILE_BYTES type. The message will contains the byteIteratorId. The message from
     * the server (sent before the client sends this message) the array of bytes to be appended to the file on the
     * client. --- RequestMessage validation: args.length==1 and args[0] instanceof String and args[0].length>0 ---
     * ResponseMessage validation: result result instanceof byte[] and result.length>0
     */
    public static final BytesMessageType RETRIEVE_FILE_BYTES = new BytesMessageType("RETRIEVE_FILE_BYTES");

    /**
     * Represents the STOP_RETRIVE_FILE_BYTES message type. The message of this type is sent by the client as a response
     * to messages of this type received from the server. The server will reply to this messagewith a message of
     * MessageType.RETRIEVE_FILE type. The message will contain the byteIteratorId. The message from the server (sent
     * before the client sends this message) contains nothing. --- RequestMessage validation: args.length==1 and args[0]
     * instanceof String and args[0].length>0 --- ResponseMessage validation: result==null
     */
    public static final BytesMessageType STOP_RETRIEVE_FILE_BYTES = new BytesMessageType("STOP_RETRIEVE_FILE_BYTES");

    /**
     * Creates an instance with the given argument.
     * @param type
     *            the message type name
     * @throws NullPointerException
     *             if type is null
     * @throws IllegalArgumentException
     *             if type is an empty string
     */
    public BytesMessageType(String type) {
        super(type);
    }
}
