/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

import java.io.Serializable;

/**
 * This class declares the types of messages currently supported by this component, except the types that involve bytes
 * transfer. Each instance of this class has a message type name associated with it (the type:String serves to determine
 * the hashCode and whether two instances are equal). The class is immutable. The public constants declare the rules of
 * validation that should be performed inside the RequestMessage and ResponseMessage's constructors.
 * @author Luca, FireIce
 * @version 1.0
 */
public class MessageType implements Serializable {

    /**
     * Represents the UPLOAD_FILE message type. The message of this type is sent by the client after the file upload has
     * been aproved (the client send a check upload message before) and the full file has been uploaded. The message
     * will contain the fileName and the fileId of the uploaded file. The server will reply with a message of this type,
     * containing the fileId. --- RequestMessage validation: args.length==2 and args[0] instanceof String and
     * args[0].length>0 and args[1] instanceof String and args[1].length>0 --- ResponseMessage validation: result
     * instanceof String and result.length>0
     */
    public static final MessageType UPLOAD_FILE = new MessageType("UPLOAD_FILE");

    /**
     * Represents the CHECK_UPLOAD_FILE message type. The message of this type is sent by the client to check if the
     * upload of the file is accepted. it is also sent automatically before the file upload. The message will contain
     * the fileName and the size of the file. The server will reply with a message of this type, containing a boolean.
     * --- RequestMessage validation: args.length==2 and args[0] instanceof String and args[0].length>0 and args[1]
     * instanceof Long and args[1]>0 --- ResponseMessage validation: result instanceof Boolean
     */
    public static final MessageType CHECK_UPLOAD_FILE = new MessageType("CHECK_UPLOAD_FILE");

    /**
     * Represents the REMOVE_FILE message type. The message of this type is sent by the client to remove a file upload
     * from the server. The message will contain the fileId. The server will reply with a message of this type,
     * containing nothing. --- RequestMessage validation: args.length==1 and args[0] instanceof String and
     * args[0].length>0 --- ResponseMessage validation: result==null
     */
    public static final MessageType REMOVE_FILE = new MessageType("REMOVE_FILE");

    /**
     * Represents the GET_FILE_NAME message type. The message of this type is sent by the client to get a file's name
     * from the server. The message will contain the fileId. The server will reply with a message of this type,
     * containing the fileName. --- RequestMessage validation: args.length==1 and args[0] instanceof String and
     * args[0].length>0 --- ResponseMessage validation: result instanceof String and result.length>0
     */
    public static final MessageType GET_FILE_NAME = new MessageType("GET_FILE_NAME");

    /**
     * Represents the GET_FILE_SIZE message type. The message of this type is sent by the client to get a file's size
     * from the server. The message will contain the fileId. The server will reply with a message of this type,
     * containing the file size. --- RequestMessage validation: args.length==1 and args[0] instanceof String and
     * args[0].length>0 --- ResponseMessage validation: result instanceof Long
     */
    public static final MessageType GET_FILE_SIZE = new MessageType("GET_FILE_SIZE");

    /**
     * Represents the RENAME_FILE message type. The message of this type is sent by the client to rename a file from the
     * server. The message will contain the fileId and the new fileName of the file. The server will reply with a
     * message of this type, containing nothing. --- RequestMessage validation: args.length==2 and args[0] instanceof
     * String and args[0].length>0 and args[1] instanceof String and args[1].length>0 --- ResponseMessage validation:
     * result==null
     */
    public static final MessageType RENAME_FILE = new MessageType("RENAME_FILE");

    /**
     * Represents the RETRIEVE_FILE message type. The message of this type is sent by the client at the beginning of the
     * file retrieval. At the end of the retrieval, the client will send a BytesMesageType.STOP_RETRIEVE_BYTES message,
     * to which the server will reply with a response message of this type. The message will contain the fileId of the
     * file to be retrieved. The server will reply with a message of this type at the end of the retrieve file process,
     * as a response to a message of BytesMesageType.STOP_RETRIEVE_BYTES type. The message will contain the fileName.
     * The server will reply to this message with a message of BytesMesageType.START_RETRIEVE_BYTES type ---
     * RequestMessage validation: args.length==1 and args[0] instanceof String and args[0].length>0 --- ResponseMessage
     * validation: result instanceof String and result.length>0
     */
    public static final MessageType RETRIEVE_FILE = new MessageType("RETRIEVE_FILE");

    /**
     * Represents the CREATE_GROUP message type. The message of this type is sent by the client to create a group. The
     * message will contain the groupName and the list of fileIds contained by the group. The server will reply with a
     * message of this type, containing nothing. --- RequestMessage validation: args.length==2 and args[0] instanceof
     * String and args[0].length>0 and ...args[1] instanceof List and args[1].item[i] instanceof String and
     * args[1].item[i] .length>0 --- ResponseMessage validation: result==null
     */
    public static final MessageType CREATE_GROUP = new MessageType("CREATE_GROUP");

    /**
     * Represents the UPDATE_GROUP message type. The message of this type is sent by the client to update a group. The
     * message will contain the groupName and the list of fileIds contained by the group. The server will reply with a
     * message of this type, containing nothing. --- RequestMessage validation: args.length==2 and args[0] instanceof
     * String and args[0].length>0 and ...args[1] instanceof List and args[1].item[i] instanceof String and
     * args[1].item[i] .length>0 --- ResponseMessage validation: result==null
     */
    public static final MessageType UPDATE_GROUP = new MessageType("UPDATE_GROUP");

    /**
     * Represents the RETRIEVE_GROUP message type. The message of this type is sent by the client to retrieve a group's
     * files. The message will contain the groupName. The server will reply with a message of this type, containing the
     * list of fileIds contained by the group. --- RequestMessage validation: args.length==1 and args[0] instanceof
     * String and args[0].length>0 --- ResponseMessage validation: result instanceof List and result.item[i] instanceof
     * String and result.item[i] .length>0
     */
    public static final MessageType RETRIEVE_GROUP = new MessageType("RETRIEVE_GROUP");

    /**
     * Represents the REMOVE_GROUP message type. The message of this type is sent by the client to remove a group. The
     * message will contain the groupName and the removeFiles flag. The server will reply with a message of this type,
     * containing nothing. --- RequestMessage validation: args.length==2 and args[0] instanceof String and
     * args[0].length>0 and args[1] instanceof Boolean --- ResponseMessage validation: result==null
     */
    public static final MessageType REMOVE_GROUP = new MessageType("REMOVE_GROUP");

    /**
     * Represents the ADD_FILE_TO_GROUP message type. The message of this type is sent by the client to add a file to a
     * group. The message will contain the groupName and the fileId to be added to the group. The server will reply with
     * a message of this type, containing nothing. --- RequestMessage validation: args.length==2 and args[0] instanceof
     * String and args[0].length>0 and args[1] instanceof String and args[1].length>0 --- ResponseMessage validation:
     * result==null
     */
    public static final MessageType ADD_FILE_TO_GROUP = new MessageType("ADD_FILE_TO_GROUP");

    /**
     * Represents the REMOVE_FILE_FROM_GROUP message type. The message of this type is sent by the client to remove a
     * file from a group. The message will contain the groupName and the fileId to be removed from the group. The server
     * will reply with a message of this type, containing nothing. --- RequestMessage validation: args.length==2 and
     * args[0] instanceof String and args[0].length>0 and args[1] instanceof String and args[1].length>0 ---
     * ResponseMessage validation: result==null
     */
    public static final MessageType REMOVE_FILE_FROM_GROUP = new MessageType("REMOVE_FILE_FROM_GROUP");

    /**
     * Represents the SEARCH_FILES message type. The message of this type is sent by the client to search the files
     * using a searcher and a criteria. The message will contain the searcherName and the criteria to be used for
     * searching. The server will reply with a message of this type, containing the list of file ids that match the
     * criteria. --- RequestMessage validation: args.length==2 and args[0] instanceof String and args[0].length>0 and
     * args[1] instanceof String --- ResponseMessage validation: result instanceof List and result.item[i] instanceof
     * String and result.item[i] .length>0
     */
    public static final MessageType SEARCH_FILES = new MessageType("SEARCH_FILES");

    /**
     * Represents the SEARCH_GROUPS message type. The message of this type is sent by the client to search the groups
     * using a searcher and a criteria. The message will contain the searcherName and the criteria to be used for
     * searching. The server will reply with a message of this type, containing the list of group names that match the
     * criteria. --- RequestMessage validation: args.length==2 and args[0] instanceof String and args[0].length>0 and
     * args[1] instanceof String --- ResponseMessage validation: result instanceof List and result.item[i] instanceof
     * String and result.item[i] .length>0
     */
    public static final MessageType SEARCH_GROUPS = new MessageType("SEARCH_GROUPS");

    /**
     * Represents the request message type name. Initialized in the constructor and never changed. Not null. Not empty.
     */
    private final String type;

    /**
     * Creates an instance with the given argument. The constructor is public because other request message types should
     * be allowed to be created. Simply assign the argument to the corresponding field.
     * @param type
     *            the message type name
     * @throws NullPointerException
     *             if type is null
     * @throws IllegalArgumentException
     *             if type is an empty string
     */
    public MessageType(String type) {
        if (type == null) {
            throw new NullPointerException("type is null");
        } else if (type.trim().length() == 0) {
            throw new IllegalArgumentException("type is empty");
        }
        this.type = type;
    }

    /**
     * Returns the message type name. Simply return the corresponding field.
     * @return the request message type name
     */
    public String getType() {
        return type;
    }

    /**
     * Returns whether or not the provided object is equal to this instance.
     * @param obj
     *            the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof MessageType) || !getClass().equals(obj.getClass())) {
            return false;
        }
        MessageType typeObj = (MessageType) obj;
        return this.type.equals(typeObj.getType());
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object
     */
    public int hashCode() {
        return this.type.hashCode();
    }
}
