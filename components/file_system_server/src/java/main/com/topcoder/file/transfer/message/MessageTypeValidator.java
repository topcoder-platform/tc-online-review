/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

import java.util.Iterator;
import java.util.List;

/**
 * package-friendly class, used for Message args validation.
 * @author Luca, FireIce
 * @version 1.0
 */
class MessageTypeValidator {

    /**
     * empty constructor preventing inheritence.
     */
    private MessageTypeValidator() {
    }

    /**
     * validate the FilSystemMessage, if the argument is valid return true, else return false.
     * @param fileSystemMessage
     *            the FileSystemMessage instance to validate
     * @return true for valid, else return false.
     */
    public static boolean validate(FileSystemMessage fileSystemMessage) {
        if (fileSystemMessage instanceof RequestMessage) {
            return validateRequestMessage((RequestMessage) fileSystemMessage);
        } else if (fileSystemMessage instanceof ResponseMessage) {
            return validateResponseMessage((ResponseMessage) fileSystemMessage);
        }
        return false;
    }

    /**
     * validate the result of the ResponseMessage instance. if exception is not null, return true. else should check the
     * result field.
     * <ul>
     * <li>for MessageType.UPLOAD_FILE, MessageType.GET_FILE_NAME and MessageType.RETRIEVE_FILE, result should
     * non-Empty String instance</li>
     * <li>for MessageType.CHECK_UPLOAD_FILE, result should be Boolean instance.</li>
     * <li>for MessageType.REMOVE_FILE, MessageType.RENAME_FILE, MessageType.CREATE_GROUP, MessageType.UPDATE_GROUP,
     * MessageType.REMOVE_GROUP, MessageType.ADD_FILE_TO_GROUP, MessageType.REMOVE_FILE_FROM_GROUP,
     * BytesMessageType.UPLOAD_FILE_BYTES, BytesMessageType.STOP_UPLOAD_FILE_BYTES,
     * BytesMessageType.STOP_RETRIEVE_FILE_BYTES, result should null.</li>
     * <li>for BytesMessageType.START_RETRIEVE_FILE_BYTES and BytesMessageType.START_UPLOAD_FILE_BYTES, result should
     * Object array that have two non-empty String instance.</li>
     * <li>for MessageType.GET_FILE_SIZE, result should be Long type.</li>
     * <li>for MessageType.RETRIEVE_GROUP, MessageType.SEARCH_FILES and MessageType.SEARCH_GROUPS, result should be a
     * List contains non-empty String instance.</li>
     * <li>for BytesMessageType.RETRIEVE_FILE_BYTES, result should instanceof byte[], and the length should greate than
     * 0</li>
     * </ul>
     * @param responseMessage
     *            the response message
     * @return true for valid, else return false.
     */
    private static boolean validateResponseMessage(ResponseMessage responseMessage) {
        if (responseMessage == null) {
            return false;
        }
        MessageType messageType = responseMessage.getType();
        Object result = responseMessage.getResult();
        // if reponse with an exception, no need to check the result.
        if (responseMessage.getException() != null) {
            return true;
        }
        if (MessageType.UPLOAD_FILE.equals(messageType) || MessageType.GET_FILE_NAME.equals(messageType)
                || MessageType.RETRIEVE_FILE.equals(messageType)) {
            // result should be non-empty String
            return (result instanceof String && ((String) result).trim().length() > 0);
        } else if (BytesMessageType.START_RETRIEVE_FILE_BYTES.equals(messageType)
                || BytesMessageType.START_UPLOAD_FILE_BYTES.equals(messageType)) {
            // result should Object array that have two non-empty String instance.
            if (result instanceof Object[]) {
                Object[] results = (Object[]) result;
                return results.length == 2 && results[0] instanceof String && ((String) results[0]).trim().length() > 0
                        && results[1] instanceof String && ((String) results[1]).trim().length() > 0;
            }
            return false;
        } else if (MessageType.CHECK_UPLOAD_FILE.equals(messageType)) {
            // result should be Boolean instance.
            return result instanceof Boolean;
        } else if (MessageType.REMOVE_FILE.equals(messageType) || MessageType.RENAME_FILE.equals(messageType)
                || MessageType.CREATE_GROUP.equals(messageType) || MessageType.UPDATE_GROUP.equals(messageType)
                || MessageType.REMOVE_GROUP.equals(messageType) || MessageType.ADD_FILE_TO_GROUP.equals(messageType)
                || MessageType.REMOVE_FILE_FROM_GROUP.equals(messageType)
                || BytesMessageType.UPLOAD_FILE_BYTES.equals(messageType)
                || BytesMessageType.STOP_UPLOAD_FILE_BYTES.equals(messageType)
                || BytesMessageType.STOP_RETRIEVE_FILE_BYTES.equals(messageType)) {
            // result should be null
            return result == null;
        } else if (MessageType.GET_FILE_SIZE.equals(messageType)) {
            // result should be Long type
            return result instanceof Long;
        } else if (MessageType.RETRIEVE_GROUP.equals(messageType) || MessageType.SEARCH_FILES.equals(messageType)
                || MessageType.SEARCH_GROUPS.equals(messageType)) {
            // result should be a List contains non-Empty Strings.
            return validateListOfString(result);
        } else if (BytesMessageType.RETRIEVE_FILE_BYTES.equals(messageType)) {
            // result should instanceof byte[], and the length should greate than 0
            return (result instanceof byte[] && ((byte[]) result).length > 0);
        }
        return false;
    }

    /**
     * helper method that validate the result is a list that contains non-empty String instance.
     * @param result
     *            the result to validate
     * @return true for it is a list of Strings and not empty, else return false.
     */
    private static boolean validateListOfString(Object result) {
        if (result instanceof List) {
            for (Iterator iter = ((List) result).iterator(); iter.hasNext();) {
                Object obj = iter.next();
                if (!(obj instanceof String) || ((String) obj).trim().length() == 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * validate the args of the RequestMessage instance.
     * <ul>
     * <li>for MessageType.UPLOAD_FILE, MessageType.RENAME_FILE, MessageType.ADD_FILE_TO_GROUP and
     * MessageType.REMOVE_FILE_FROM_GROUP, args should be Object array that contains exactly two non-empty String
     * instance.</li>
     * <li>for MessageType.CHECK_UPLOAD_FILE, args should be Object array that the first arg is non-empty String, the
     * second arg is a Long instance whose value is greate than 0.</li>
     * <li>for MessageType.REMOVE_FILE, MessageType.GET_FILE_NAME, MessageType.GET_FILE_SIZE,
     * MessageType.RETRIEVE_FILE, MessageType.RETRIEVE_GROUP, BytesMessageType.STOP_UPLOAD_FILE_BYTES,
     * BytesMessageType.START_RETRIEVE_FILE_BYTES , BytesMessageType.RETRIEVE_FILE_BYTES and
     * BytesMessageType.STOP_RETRIEVE_FILE_BYTES, args should be Object array which only contains one element that is a
     * non-empty String.</li>
     * <li>for MessageType.REMOVE_GROUP, args should be Object array that the first arg is non-empty String and the
     * second arg is Boolean type.</li>
     * <li>for MessageType.CREATE_GROUP and MessageType.UPDATE_GROUP, the args should satisfy args.length==2 and
     * args[0] instanceof String and args[0].length>0 and args[1] instanceof List and args[1].item[i] instanceof String
     * and args[1].item[i] .length>0</li>
     * <li>for MessageType.SEARCH_FILES and MessageType.SEARCH_GROUPS, args should be Object array that contains two
     * String instance, the first cann't be empty, the second can be empty.</li>
     * <li>for BytesMessageType.START_UPLOAD_FILE_BYTES, args should be Object array that contains one non-empty String
     * pr two non-empty Strings.</li>
     * <li>for BytesMessageType.UPLOAD_FILE_BYTES, the args should be Object arrray that contains two element, the
     * first element is non-empty String, the second is byte array which length is greate than 0.</li>
     * </ul>
     * @param requestMessage
     *            the request message to validate
     * @return true for valid, else return false.
     */
    private static boolean validateRequestMessage(RequestMessage requestMessage) {
        if (requestMessage == null) {
            return false;
        }
        MessageType messageType = requestMessage.getType();
        Object[] args = requestMessage.getArgs();
        if (MessageType.UPLOAD_FILE.equals(messageType) || MessageType.RENAME_FILE.equals(messageType)
                || MessageType.ADD_FILE_TO_GROUP.equals(messageType)
                || MessageType.REMOVE_FILE_FROM_GROUP.equals(messageType)) {
            // args should be Object array that contains exactly two non-empty String instance.
            return args != null && args.length == 2 && args[0] instanceof String
                    && ((String) args[0]).trim().length() > 0 && args[1] instanceof String
                    && ((String) args[1]).trim().length() > 0;
        } else if (MessageType.CHECK_UPLOAD_FILE.equals(messageType)) {
            // args should be Object array that the first arg is non-empty String, the second arg is a Long instance
            // whose value is greate than 0.
            return args != null && args.length == 2 && args[0] instanceof String
                    && ((String) args[0]).trim().length() > 0 && args[1] instanceof Long
                    && ((Long) args[1]).longValue() > 0;
        } else if (MessageType.REMOVE_FILE.equals(messageType) || MessageType.GET_FILE_NAME.equals(messageType)
                || MessageType.GET_FILE_SIZE.equals(messageType) || MessageType.RETRIEVE_FILE.equals(messageType)
                || MessageType.RETRIEVE_GROUP.equals(messageType)
                || BytesMessageType.STOP_UPLOAD_FILE_BYTES.equals(messageType)
                || BytesMessageType.START_RETRIEVE_FILE_BYTES.equals(messageType)
                || BytesMessageType.RETRIEVE_FILE_BYTES.equals(messageType)
                || BytesMessageType.STOP_RETRIEVE_FILE_BYTES.equals(messageType)) {
            // args should be Object array which only contains one element that is a non-empty String.
            return args != null && args.length == 1 && args[0] instanceof String
                    && ((String) args[0]).trim().length() > 0;
        } else if (MessageType.REMOVE_GROUP.equals(messageType)) {
            // args should be Object array that the first arg is non-empty String and the second arg is Boolean type.
            return args != null && args.length == 2 && args[0] instanceof String
                    && ((String) args[0]).trim().length() > 0 && args[1] instanceof Boolean;
        } else if (MessageType.CREATE_GROUP.equals(messageType) || MessageType.UPDATE_GROUP.equals(messageType)) {
            // the args should satisfy args.length==2 and args[0] instanceof String and args[0].length>0 and args[1]
            // instanceof List and args[1].item[i] instanceof String and args[1].item[i] .length>0
            return validateArgsStringList(args);
        } else if (MessageType.SEARCH_FILES.equals(messageType) || MessageType.SEARCH_GROUPS.equals(messageType)) {
            // args should be Object array that contains two String instance, the first cann't be empty, the second can
            // be empty.
            return args != null && args.length == 2 && args[0] instanceof String
                    && ((String) args[0]).trim().length() > 0 && args[1] instanceof String;
        } else if (BytesMessageType.START_UPLOAD_FILE_BYTES.equals(messageType)) {
            // args should be Object array that contains one non-empty String pr two non-empty Strings.
            return args != null
                    && ((args.length == 1 && args[0] instanceof String && ((String) args[0]).trim().length() > 0)
                            || (args.length == 2
                            && args[0] instanceof String
                            && ((String) args[0]).trim().length() > 0
                            && args[1] instanceof String && ((String) args[1]).trim().length() > 0));
        } else if (BytesMessageType.UPLOAD_FILE_BYTES.equals(messageType)) {
            // the args should be Object arrray that contains two element, the first element is non-empty String, the
            // second is byte array which length is greate than 0.
            return args != null && args.length == 2 && args[0] instanceof String
                    && ((String) args[0]).trim().length() > 0 && args[1] instanceof byte[]
                    && ((byte[]) args[1]).length > 0;
        }
        return false;
    }

    /**
     * validation of the args list, if args.length==2 and args[0] instanceof String and args[0].length>0 and args[1]
     * instanceof List and args[1].item[i] instanceof String and args[1].item[i].length>0, return true, else return
     * false.
     * @param args
     *            the rags array to validate
     * @return true for valid, false for invalid
     */
    private static boolean validateArgsStringList(Object[] args) {
        if (args != null && args.length == 2 && args[0] instanceof String && ((String) args[0]).trim().length() > 0
                && args[1] instanceof List) {
            List items = (List) args[1];
            for (Iterator iter = items.iterator(); iter.hasNext();) {
                Object obj = iter.next();
                if (!(obj instanceof String) || ((String) obj).trim().length() == 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
