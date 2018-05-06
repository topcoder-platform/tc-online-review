/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.topcoder.file.transfer.message.BytesMessageType;
import com.topcoder.file.transfer.message.MessageType;
import com.topcoder.file.transfer.message.RequestMessage;
import com.topcoder.file.transfer.message.ResponseMessage;
import com.topcoder.file.transfer.persistence.BytesIterator;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FilePersistenceException;
import com.topcoder.file.transfer.registry.FileIdExistsException;
import com.topcoder.file.transfer.registry.FileIdNotFoundException;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.GroupExistsException;
import com.topcoder.file.transfer.registry.GroupNotFoundException;
import com.topcoder.file.transfer.registry.RegistryException;
import com.topcoder.file.transfer.registry.RegistryPersistenceException;
import com.topcoder.file.transfer.search.FileSearcherNotFoundException;
import com.topcoder.file.transfer.search.GroupSearcherNotFoundException;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.search.SearcherException;
import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.Handler;
import com.topcoder.processor.ipserver.ProcessingException;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageSerializationException;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.generator.guid.Generator;
import com.topcoder.util.generator.guid.UUIDType;
import com.topcoder.util.generator.guid.UUIDUtility;

/**
 * This is the file system handler required; it extends the Handler class from IP Server 2.0. It provides methods for
 * processing requests and sending responses back to the client. It uses a file registry to maintain files and groups, a
 * file persistence to persist files, it has the file location configured, it uses an ObjectValidator to validate check
 * upload requests, a search manager to search for files or groups and a GUID generator to generate unique ids. The
 * class is immutable.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FileSystemHandler extends Handler {

    /**
     * Represents the file system registry used to maintain the files and the groups. Initialized in the constructor and
     * never changed later. Not null.
     */
    private final FileSystemRegistry registry;

    /**
     * Represents the file persistence used to create files / delete files / retrieve bytes iterator over a file's
     * bytes. Initialized in the constructor and never changed later. Not null.
     */
    private final FilePersistence persistence;

    /**
     * Represents the file location used for the file persistence. Initialized in the constructor and never changed
     * later. Not null.
     */
    private final String fileLocation;

    /**
     * Represents the file check file upload request validator. It is used to validate a message of
     * MessageType.CHECK_UPLOAD_FILE type. Initialized in the constructor and never changed later. Not null.
     */
    private final ObjectValidator uploadRequestValidator;

    /**
     * Represents the search manager used to perform searches for files or groups. It is used for messages of
     * MessageType.SEARCH_FILES and MessageType.SEARCH_GROUPS type. Initialized in the constructor and never changed
     * later. Not null.
     */
    private final SearchManager searchManager;

    /**
     * Represents the GUID generator used to generate unique ids for the bytes iterators. Initialized in the constructor
     * and never changed later. Not null.
     */
    private final Generator generator;

    /**
     * Represents the map for the active bytes iterators. It contains (unique byte iterator ids - byte iterator) pairs.
     * The keys are Strings, non-null, non-empty. The keys are the same as those used for the filesToRetrieve map. The
     * values are ByteIterators, non-null. Initialized in the constructor and never changed later. Not null. Can be
     * empty. Synchronized.
     */
    private final Map bytesIterators;

    /**
     * Represents the map for the files that are currently retrieved by clients. It contains (unique byte iterator ids -
     * fileId) pairs. The keys are Strings, non-null, non-empty. The values are Strings, non-null, non-empty. The keys
     * are the same as those used for the bytesIterators map. Initialized in the constructor and never changed later.
     * Not null. Can be empty. Synchronized.
     */
    private final Map filesToRetrieve;

    /**
     * Represents the map for the last accessed dates of the files that are currently retrieved by clients. It contains
     * (unique byte iterator ids - lastAccessDate) pairs. The keys are Strings, non-null, non-empty. The keys are the
     * same as those used for the bytesIterators and filesToRetrieve maps. The values are of Date type. Initialized in
     * the constructor and never changed later. Not null. Can be empty. Synchronized.
     */
    private final Map filesToRetrieveLastAccessDates;

    /**
     * Represents the map for the files that are currently uploaded by clients. It contains (unique file creation ids -
     * fileId) pairs. The keys are Strings, non-null, non-empty. The values are Strings, non-null, non-empty.
     * Initialized in the constructor and never changed later. Not null. Can be empty. Synchronized.
     */
    private final Map filesToUpload;

    /**
     * Represents the map for the last accessed dates of the files that are currently uploaded by clients. It contains
     * (unique file creation ids - lastAccessDate) pairs. The keys are Strings, non-null, non-empty. The keys are the
     * same as those used for the filesToUpload maps. The values are of Date type. Initialized in the constructor and
     * never changed later. Not null. Can be empty. Synchronized.
     */
    private final Map filesToUploadLastAccessDates;

    /**
     * Represents the timer to which the timeOutTransferSessionsTask that disposes the expired transfer sessions is
     * added. Initialized in the constructor. Not null.
     */
    private Timer timeOutTransferSessionsTimer;

    /**
     * Represents the timer task that disposes the expired transfer sessions. Initialized in the constructor. Not null.
     * It is added to a timeOutTransferSessionsTimer in the constructor.
     */
    private final TimeOutTransferSessionsTask timeOutTransferSessionsTask;

    /**
     * Creates an instance with the given arguments.
     * @param maxRequests
     *            the maximum number of requests
     * @param registry
     *            the file system registry
     * @param persistence
     *            the file persistence
     * @param fileLocation
     *            the file location
     * @param uploadRequestValidator
     *            the check upload request validator
     * @param searchManager
     *            the search manager
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the maxRequests argument is negative or any string is empty
     */
    public FileSystemHandler(int maxRequests, FileSystemRegistry registry, FilePersistence persistence,
            String fileLocation, ObjectValidator uploadRequestValidator, SearchManager searchManager) {
        super(maxRequests);
        if (registry == null) {
            throw new NullPointerException("registry is null");
        }
        this.registry = registry;
        if (persistence == null) {
            throw new NullPointerException("persistence is null");
        }
        this.persistence = persistence;
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        this.fileLocation = fileLocation;
        if (uploadRequestValidator == null) {
            throw new NullPointerException("uploadRequestValidator is null");
        }
        this.uploadRequestValidator = uploadRequestValidator;
        if (searchManager == null) {
            throw new NullPointerException("uploadRequestValidator is null");
        }
        this.searchManager = searchManager;
        generator = UUIDUtility.getGenerator(UUIDType.TYPEINT32);
        bytesIterators = Collections.synchronizedMap(new HashMap());
        filesToRetrieve = Collections.synchronizedMap(new HashMap());
        filesToRetrieveLastAccessDates = Collections.synchronizedMap(new HashMap());
        filesToUpload = Collections.synchronizedMap(new HashMap());
        filesToUploadLastAccessDates = Collections.synchronizedMap(new HashMap());
        timeOutTransferSessionsTimer = new Timer();
        timeOutTransferSessionsTask = new TimeOutTransferSessionsTask();
        timeOutTransferSessionsTimer.schedule(timeOutTransferSessionsTask, TimeOutTransferSessionsTask.CHECK_INTERVAL,
                TimeOutTransferSessionsTask.CHECK_INTERVAL);
    }

    /**
     * Process the request message sent by the client. The request message must be of RequestMessage type, or a
     * ProcessingException will be thrown. Using the request type and the request args from the request message, the
     * appropriate action is performed. The method performs the required operation and wraps the result, if any, in a
     * ResponseMessage. It also sets the message type on the response message. For the methods that return void, there
     * is no need to set a result. If an exception is raised, the exception is wrapped in the result message. The
     * message is sent using the IPServer obtained from the connection. Any exception raised by the IPServer is wrapped
     * in a ProcessingException and thrown. This method should be thread safe. In order to achieve this, the method
     * should synchronize on the file registry field for requests that require transaction thread safety. The registry
     * is thread safe for atomic operations, as all its methods are synchronized.
     * @param connection
     *            the connection on which the request arrived
     * @param request
     *            the request message
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalStateException
     *             if the server is stopped, when trying to send a response or if the connection is closed
     * @throws ProcessingException
     *             wraps a fatal application specific exception (note that normal exception should be reported to the
     *             user by wrapping them in the response message, only fatal exceptions that should terminate the server
     *             should throw this exception)
     * @throws IOException
     *             if I/O error occur.
     */
    protected void onRequest(Connection connection, Message request) throws ProcessingException, IOException {
        super.onRequest(connection, request);
        if (request instanceof RequestMessage) {
            try {
                connection.getIPServer().sendResponse(connection.getId(), proccessRequest((RequestMessage) request));
            } catch (MessageSerializationException e) {
                throw new ProcessingException("cann't be serialize", e);
            }
        } else {
            throw new ProcessingException("the message is not of RequestMessage type");
        }

    }

    /**
     * process RequestMessage according to its type, and generate the ResponseMessage instance.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance.
     * @throws ProcessingException
     *             if the message type is unknown.
     */
    private ResponseMessage proccessRequest(RequestMessage requestMessage) throws ProcessingException {
        if (requestMessage.getType().equals(MessageType.UPLOAD_FILE)) {
            return proccessUploadFile(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.CHECK_UPLOAD_FILE)) {
            return proccessCheckUploadFile(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.REMOVE_FILE)) {
            return proccessRemoveFile(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.GET_FILE_NAME)) {
            return proccessGetFileName(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.GET_FILE_SIZE)) {
            return proccessGetFileSize(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.RENAME_FILE)) {
            return proccessRenameFile(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.RETRIEVE_FILE)) {
            return proccessRetreiveFile(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.CREATE_GROUP)) {
            return processCreateGroup(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.UPDATE_GROUP)) {
            return proccessUpdateGroup(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.RETRIEVE_GROUP)) {
            return proccessRetrieveGroup(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.REMOVE_GROUP)) {
            return proccessRemoveGroup(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.ADD_FILE_TO_GROUP)) {
            return proccessAddFileToGroup(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.REMOVE_FILE_FROM_GROUP)) {
            return proccessRemoveFileFromGroup(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.SEARCH_FILES)) {
            return proccessSearchFiles(requestMessage);
        } else if (requestMessage.getType().equals(MessageType.SEARCH_GROUPS)) {
            return proccessSearchGroups(requestMessage);
        } else if (requestMessage.getType().equals(BytesMessageType.START_UPLOAD_FILE_BYTES)) {
            return proccessStartUploadFileBytes(requestMessage);
        } else if (requestMessage.getType().equals(BytesMessageType.UPLOAD_FILE_BYTES)) {
            return proccesspUploadFileBytes(requestMessage);
        } else if (requestMessage.getType().equals(BytesMessageType.STOP_UPLOAD_FILE_BYTES)) {
            return proccessStopUploadFileBytes(requestMessage);
        } else if (requestMessage.getType().equals(BytesMessageType.START_RETRIEVE_FILE_BYTES)) {
            return proccessStartRetrieveFileBytes(requestMessage);
        } else if (requestMessage.getType().equals(BytesMessageType.RETRIEVE_FILE_BYTES)) {
            return proccessRetrieveFileBytes(requestMessage);
        } else if (requestMessage.getType().equals(BytesMessageType.STOP_RETRIEVE_FILE_BYTES)) {
            return proccessStopRetrieveFileBytes(requestMessage);
        } else {
            throw new ProcessingException("unknow Message type");
        }
    }

    /**
     * proccess the RequestMessage with BytesMessageType.STOP_RETRIEVE_FILE_BYTES type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.RETRIEVE_FILE type.
     */
    private ResponseMessage proccessStopRetrieveFileBytes(RequestMessage requestMessage) {
        ResponseMessage response;
        // get the bytestIteratorId from the request.
        String bytesIteratorId = (String) requestMessage.getArgs()[0];
        // remove the iterator from the bytesIterators map
        bytesIterators.remove(bytesIteratorId);
        String fileId = (String) filesToRetrieve.get(bytesIteratorId);
        // remvoe the fileId from the files to retrieve
        filesToRetrieve.remove(bytesIteratorId);
        // remove the transfer sessoin last access date
        filesToRetrieveLastAccessDates.remove(bytesIteratorId);
        response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                MessageType.RETRIEVE_FILE, fileId);
        return response;
    }

    /**
     * proccess the RequestMessage with BytesMessageType.RETRIEVE_FILE_BYTES type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with BytesMessageType.RETRIEVE_FILE_BYTES or
     *         BytesMessageType.STOP_RETRIEVE_FILE_BYTES type.
     */
    private ResponseMessage proccessRetrieveFileBytes(RequestMessage requestMessage) {
        return retrieveFileBytes(requestMessage);
    }

    /**
     * proccess the RequestMessage with BytesMessageType.RETRIEVE_FILE_BYTES type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with BytesMessageType.RETRIEVE_FILE_BYTES or
     *         BytesMessageType.STOP_RETRIEVE_FILE_BYTES type.
     */
    private ResponseMessage retrieveFileBytes(RequestMessage requestMessage) {
        ResponseMessage response;
        // get the bytestIteratorId and the fileCreationId from the request.
        Object[] args = requestMessage.getArgs();
        String bytesIteratorId = (String) args[0];
        // get the next bytes from the iter
        BytesIterator bytesIterator = (BytesIterator) bytesIterators.get(bytesIteratorId);
        try {
            if (bytesIterator.hasNextBytes()) {
                response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                        BytesMessageType.RETRIEVE_FILE_BYTES, bytesIterator.nextBytes());
            } else {
                response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                        BytesMessageType.STOP_RETRIEVE_FILE_BYTES);
            }
            // update the transfer session last access date
            filesToRetrieveLastAccessDates.put(bytesIteratorId, new Date());
        } catch (FilePersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.STOP_RETRIEVE_FILE_BYTES, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with BytesMessageType.RETRIEVE_FILE_BYTES type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with BytesMessageType.RETRIEVE_FILE_BYTES or
     *         BytesMessageType.STOP_RETRIEVE_FILE_BYTES type.
     */
    private ResponseMessage proccessStartRetrieveFileBytes(RequestMessage requestMessage) {
        return retrieveFileBytes(requestMessage);
    }

    /**
     * proccess the RequestMessage with BytesMessageType.STOP_UPLOAD_FILE_BYTES type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with BytesMessageType.STOP_UPLOAD_FILE_BYTES type.
     */
    private ResponseMessage proccessStopUploadFileBytes(RequestMessage requestMessage) {
        ResponseMessage response;
        // get the fileCreationId
        String fileCreationId = (String) requestMessage.getArgs()[0];
        try {
            // close the file in the persistence
            persistence.closeFile(fileCreationId);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.STOP_UPLOAD_FILE_BYTES);
            // remove the transfer session last access date
            filesToUpload.remove(fileCreationId);
            filesToUploadLastAccessDates.remove(fileCreationId);
        } catch (FilePersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.STOP_UPLOAD_FILE_BYTES, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with BytesMessageType.UPLOAD_FILE_BYTES type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with BytesMessageType.UPLOAD_FILE_BYTES type.
     */
    private ResponseMessage proccesspUploadFileBytes(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the fileCreationId
        String fileCreationId = (String) args[0];
        byte[] bytes = (byte[]) args[1];
        try {
            // append the bytes to the file
            persistence.appendBytes(fileCreationId, bytes);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.UPLOAD_FILE_BYTES);
            // update the transfer session last access date
            filesToUploadLastAccessDates.put(fileCreationId, new Date());
        } catch (FilePersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.UPLOAD_FILE_BYTES, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with BytesMessageType.START_UPLOAD_FILE_BYTES type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with BytesMessageType.START_UPLOAD_FILE_BYTES type.
     */
    private ResponseMessage proccessStartUploadFileBytes(RequestMessage requestMessage) {
        ResponseMessage response = null;
        // get the fileName and the fileId from request
        Object[] args = requestMessage.getArgs();
        String fileId = null;
        try {
            if (args.length == 1) {
                // create a new fileId in case it is not provided
                fileId = registry.getNextFileId();
            } else if (args.length == 2) {
                fileId = (String) args[1];
                if (!registry.getFileIds().contains(fileId)) {
                    return new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                            BytesMessageType.START_UPLOAD_FILE_BYTES, new FileIdNotFoundException("id not exist",
                                    fileId));
                }
            }
            // create the file in the persistence and get the creation id
            String fileCreationId = persistence.createFile(fileLocation, fileId);

            Object[] results = new Object[2];
            results[0] = fileId;
            results[1] = fileCreationId;
            filesToUpload.put(fileCreationId, fileId);
            // initialize the transfer session last access date
            filesToUploadLastAccessDates.put(fileCreationId, new Date());
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.START_UPLOAD_FILE_BYTES, results);
        } catch (RegistryException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.START_UPLOAD_FILE_BYTES, e);
        } catch (FilePersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.START_UPLOAD_FILE_BYTES, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.SEARCH_GROUPS type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.SEARCH_GROUPS type.
     */
    private ResponseMessage proccessSearchGroups(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the new searcherName from request
        String searcherName = (String) args[0];
        // get the criteria from request
        String criteria = (String) args[1];
        try {
            // search groups using the searchManager
            List groups = searchManager.getGroups(searcherName, criteria);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.SEARCH_GROUPS, groups);
        } catch (GroupSearcherNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.SEARCH_GROUPS, e);
        } catch (SearcherException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.SEARCH_GROUPS, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.SEARCH_FILES type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.SEARCH_FILES type.
     */
    private ResponseMessage proccessSearchFiles(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the new searcherName from request
        String searcherName = (String) args[0];
        // get the criteria from request
        String criteria = (String) args[1];
        try {
            // search files using the searchManager
            List files = searchManager.getFiles(searcherName, criteria);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.SEARCH_FILES, files);
        } catch (FileSearcherNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.SEARCH_FILES, e);
        } catch (SearcherException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.SEARCH_FILES, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.REMOVE_FILE_FROM_GROUP type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.REMOVE_FILE_FROM_GROUP type.
     */
    private ResponseMessage proccessRemoveFileFromGroup(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the new fileName from request
        String groupName = (String) args[0];
        // get the fileId from request
        String fileId = (String) args[1];
        try {
            // remvoe the file to the group in the registry
            registry.removeFileFromGroup(groupName, fileId);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_FILE_FROM_GROUP);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_FILE_FROM_GROUP, e);
        } catch (GroupNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_FILE_FROM_GROUP, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_FILE_FROM_GROUP, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.ADD_FILE_TO_GROUP type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.ADD_FILE_TO_GROUP type.
     */
    private ResponseMessage proccessAddFileToGroup(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the new fileName from request
        String groupName = (String) args[0];
        // get the fileId from request
        String fileId = (String) args[1];
        try {
            // add the file to the group in the registry
            registry.addFileToGroup(groupName, fileId);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.ADD_FILE_TO_GROUP);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.ADD_FILE_TO_GROUP, e);
        } catch (GroupNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.ADD_FILE_TO_GROUP, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.ADD_FILE_TO_GROUP, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.REMOVE_GROUP type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.REMOVE_GROUP type.
     */
    private ResponseMessage proccessRemoveGroup(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the new groupName from request
        String groupName = (String) args[0];
        Boolean removeFilesFlag = (Boolean) args[1];
        try {
            if (removeFilesFlag.booleanValue()) {
                // get the file ids of the group and remove them
                List fileIds = (List) registry.getGroupFiles(groupName);
                for (Iterator iter = fileIds.iterator(); iter.hasNext();) {
                    String fileId = (String) iter.next();
                    persistence.deleteFile(fileLocation, fileId);
                    registry.removeFile(fileId);
                }
            }
            // remove the group from the registry
            registry.removeGroup(groupName);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_GROUP);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_GROUP, e);
        } catch (FilePersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_GROUP, e);
        } catch (GroupNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_GROUP, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_GROUP, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.RETRIEVE_GROUP type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.RETRIEVE_GROUP type.
     */
    private ResponseMessage proccessRetrieveGroup(RequestMessage requestMessage) {
        ResponseMessage response;
        // get the new fileName from request
        String groupName = (String) requestMessage.getArgs()[0];
        try {
            // create the group in the registry
            List fileIds = registry.getGroupFiles(groupName);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.RETRIEVE_GROUP, fileIds);
        } catch (GroupNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.RETRIEVE_GROUP, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.RETRIEVE_GROUP, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.UPDATE_GROUP type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.UPDATE_GROUP type.
     */
    private ResponseMessage proccessUpdateGroup(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the new fileName from request
        String groupName = (String) args[0];
        // get the fileId from the request
        List fileIds = (List) args[1];
        try {
            // create the group in the registry
            registry.updateGroup(groupName, fileIds);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.UPDATE_GROUP);
        } catch (GroupNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.UPDATE_GROUP, e);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.UPDATE_GROUP, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.UPDATE_GROUP, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.CREATE_GROUP type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.CREATE_GROUP type.
     */
    private ResponseMessage processCreateGroup(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the new fileName from request
        String groupName = (String) args[0];
        // get the fileId from the request
        List fileIds = (List) args[1];
        try {
            // create the group in the registry
            registry.createGroup(groupName, fileIds);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.CREATE_GROUP);
        } catch (GroupExistsException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.CREATE_GROUP, e);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.CREATE_GROUP, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.CREATE_GROUP, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.RETRIEVE_FILE type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with BytesMessageType.START_RETRIEVE_FILE_BYTES type.
     */
    private ResponseMessage proccessRetreiveFile(RequestMessage requestMessage) {
        ResponseMessage response;
        try {
            // get the fileId from the request
            String fileId = (String) requestMessage.getArgs()[0];
            // get fileName from the registry
            String fileName = registry.getFile(fileId);
            // get a bytes iterator from the persistence over file
            BytesIterator bytesIterator = persistence.getFileBytesIterator(fileLocation, fileId);
            // get a unique bytse iterator id
            String bytesIteratorId = createUniqueBytesIteratorId();
            // put the byts iterator in the map of bytes iterators and the fileId in the map of files to retrieve
            bytesIterators.put(bytesIteratorId, bytesIterator);
            filesToRetrieve.put(bytesIteratorId, fileId);
            // initialize the transfer session last access date
            filesToRetrieveLastAccessDates.put(bytesIteratorId, new Date());
            // create a new response message
            Object[] results = new Object[2];
            results[0] = fileName;
            results[1] = bytesIteratorId;
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.START_RETRIEVE_FILE_BYTES, results);
        } catch (FilePersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.START_RETRIEVE_FILE_BYTES, e);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.START_RETRIEVE_FILE_BYTES, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    BytesMessageType.START_RETRIEVE_FILE_BYTES, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.RENAME_FILE type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.RENAME_FILE type.
     */
    private ResponseMessage proccessRenameFile(RequestMessage requestMessage) {
        ResponseMessage response;
        Object[] args = requestMessage.getArgs();
        // get the fileId from the request
        String fileId = (String) args[0];
        // get the new fileName from request
        String newFileName = (String) args[1];
        try {
            registry.renameFile(fileId, newFileName);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.RENAME_FILE);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.RENAME_FILE, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.RENAME_FILE, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.GET_FILE_SIZE type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.GET_FILE_SIZE type.
     */
    private ResponseMessage proccessGetFileSize(RequestMessage requestMessage) {
        ResponseMessage response;
        // get the fileId from the request
        String fileId = (String) requestMessage.getArgs()[0];
        // get the fileName from the registry
        try {
            long fileSize = persistence.getFileSize(fileLocation, fileId);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.GET_FILE_SIZE, new Long(fileSize));
        } catch (FilePersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.GET_FILE_SIZE, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.GET_FILE_NAME type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.GET_FILE_NAME type.
     */
    private ResponseMessage proccessGetFileName(RequestMessage requestMessage) {
        ResponseMessage response;
        // get the fileId from the request
        String fileId = (String) requestMessage.getArgs()[0];
        // get the fileName from the registry
        try {
            String fileName = registry.getFile(fileId);
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.GET_FILE_NAME, fileName);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.GET_FILE_NAME, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.GET_FILE_NAME, e);
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.REMOVE_FILE type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.REMOVE_FILE type.
     */
    private ResponseMessage proccessRemoveFile(RequestMessage requestMessage) {
        ResponseMessage response;
        // get the fileId from the request
        String fileId = (String) requestMessage.getArgs()[0];
        // check whether this file is retrieving other client
        if (filesToRetrieve.containsValue(fileId)) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.REMOVE_FILE, new IllegalStateException("the file is retrieving by other client"));
        } else {
            // remove the file from the registry and persistence.
            synchronized (registry) {
                try {
                    persistence.deleteFile(fileLocation, fileId);
                    registry.removeFile(fileId);
                    response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                            MessageType.REMOVE_FILE, (Object) null);
                } catch (FilePersistenceException e) {
                    response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                            MessageType.REMOVE_FILE, e);
                } catch (FileIdNotFoundException e) {
                    response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                            MessageType.REMOVE_FILE, e);
                } catch (RegistryPersistenceException e) {
                    response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                            MessageType.REMOVE_FILE, e);
                }
            }
        }
        return response;
    }

    /**
     * proccess the RequestMessage with MessageType.CHECK_UPLOAD_FILE type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.CHECK_UPLOAD_FILE type.
     */
    private ResponseMessage proccessCheckUploadFile(RequestMessage requestMessage) {
        // check if the file upload request is accepted
        return new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                MessageType.CHECK_UPLOAD_FILE, new Boolean(uploadRequestValidator.valid(requestMessage)));
    }

    /**
     * proccess the RequestMessage with MessageType.UPLOAD_FILE type.
     * @param requestMessage
     *            the RequestMessage to proccess.
     * @return the ResponseMessage instance with MessageType.UPLOAD_FILE type.
     */
    private ResponseMessage proccessUploadFile(RequestMessage requestMessage) {
        Object[] args = requestMessage.getArgs();
        String fileName = (String) args[0];
        String fileId = (String) args[1];
        ResponseMessage response;
        try {
            if (registry.getFileIds().contains(fileId)) {
                registry.renameFile(fileId, fileName);
            } else {
                registry.addFile(fileId, fileName);
            }
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.UPLOAD_FILE, fileId);
        } catch (FileIdNotFoundException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.UPLOAD_FILE, e);
        } catch (RegistryPersistenceException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.UPLOAD_FILE, e);
        } catch (FileIdExistsException e) {
            response = new ResponseMessage(requestMessage.getHandlerId(), requestMessage.getRequestId(),
                    MessageType.UPLOAD_FILE, e);
        }
        return response;
    }

    /**
     * Returns the file system registry used to maintain the files and the groups. Simply return the corresponding
     * field.
     * @return the file system registry used to maintain the files and the groups
     */
    public FileSystemRegistry getRegistry() {
        return registry;
    }

    /**
     * Returns the file persistence used to create files / delete files / retrieve bytes iterator over a file's bytes.
     * Simply return the corresponding field.
     * @return the file persistence used to create files / delete files / retrieve bytes iterator over a file's bytes
     */
    public FilePersistence getPersistence() {
        return persistence;
    }

    /**
     * Returns the file location used for the file persistence. Simply return the corresponding field.
     * @return the file location used for the file persistence
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * Returns the file check file upload request validator. It is used to validate a message of
     * MessageType.CHECK_UPLOAD_FILE type. Simply return the corresponding field.
     * @return the file check file upload request validator
     */
    public ObjectValidator getUploadRequestValidator() {
        return uploadRequestValidator;
    }

    /**
     * Represents the search manager used to perform searches for files or groups. It is used for messages of
     * MessageType.SEARCH_FILES and MessageType.SEARCH_GROUPS type. Simply return the corresponding field.
     * @return the search manager used to perform searches for files or groups
     */
    public SearchManager getSearchManager() {
        return searchManager;
    }

    /**
     * Returns the GUID generator used to generate unique ids for the bytes iterators. Simply return the corresponding
     * field.
     * @return the GUID generator used to generate unique ids for the bytes iterators
     */
    protected Generator getGenerator() {
        return generator;
    }

    /**
     * Returns the map for the active bytes iterators. It contains (unique ids - byte iterator) pairs. The keys are
     * Strings, non-null, non-empty, and the values are bytes iterators. The keys are the same as those used for the
     * filesToRetrieve map. Simply return the corresponding field.
     * @return the map for the active bytes iterators
     */
    protected Map getBytesIterators() {
        return bytesIterators;
    }

    /**
     * Returns the map for the files that are currently retrieved by clients. It contains (unique ids - fileId) pairs.
     * The keys and values are Strings, non-null, non-empty. The keys are the same as those used for the bytesIterators
     * map. Simply return the corresponding field.
     * @return the map for the files that are currently retrieved by clients
     */
    protected Map getFilesToRetrieve() {
        return filesToRetrieve;
    }

    /**
     * Returns the map for the files that are currently uploaded by clients. It contains (unique ids - fileId) pairs.
     * The keys and values are Strings, non-null, non-empty. The keys are the same as those used for the bytesIterators
     * map. Simply return the corresponding field.
     * @return the map for the files that are currently uploaded by clients
     */
    protected Map getFilesToUpload() {
        return filesToUpload;
    }

    /**
     * Returns the map for the last accessed dates of the files that are currently retrieved by clients. It contains
     * (unique byte iterator ids - lastAccessDate) pairs. The keys are Strings, non-null, non-empty. The keys are the
     * same as those used for the bytesIterators and filesToRetrieve maps. The values are of Date type. Simply return
     * the corresponding field.
     * @return the map for the last accessed dates of the files that are currently retrieved by clients
     */
    protected Map getFilesToRetrieveLastAccessDates() {
        return filesToRetrieveLastAccessDates;
    }

    /**
     * Returns the map for the last accessed dates of the files that are currently uploaded by clients. It contains
     * (unique file creation ids - lastAccessDate) pairs. The keys are Strings, non-null, non-empty. The values are of
     * Date type. Simply return the corresponding field.
     * @return the map for the last accessed dates of the files that are currently uploaded by clients
     */
    protected Map getFilesToUploadLastAccessDates() {
        return filesToUploadLastAccessDates;
    }

    /**
     * Returns the timer to which the timeOutTransferSessionsTask that disposes the expired transfer sessions is added.
     * Simply return the corresponding field.
     * @return the timer to which the timeOutTransferSessionsTask that disposes the expired transfer sessions is added
     */
    protected Timer getTimeOutTransferSessionsTimer() {
        return timeOutTransferSessionsTimer;
    }

    /**
     * Returns the timer task that disposes the expired transfer sessions. Simply return the corresponding field.
     * @return the timer task that disposes the expired transfer sessions
     */
    protected TimeOutTransferSessionsTask getTimeOutTransferSessionsTask() {
        return timeOutTransferSessionsTask;
    }

    /**
     * Returns the timeout in milliseconds for the transfer sessions (upload and retrieve files).
     * @return the timeout for the transfer sessions
     */
    public long getTransferSessionTimeOut() {
        return timeOutTransferSessionsTask.getTransferSessionTimeOut();
    }

    /**
     * Sets a new timeout in milliseconds for the transfer sessions (upload and retrieve files).
     * @param timeOut
     *            the timeout for the transfer sessions
     * @throws IllegalArgumentException
     *             if the argument is negative or 0
     */
    public void setTransferSessionTimeOut(long timeOut) {
        timeOutTransferSessionsTask.setTransferSessionTimeOut(timeOut);
    }

    /**
     * Disposes this instance. This method is needed as the connection between the client and server could be closed or
     * some exception could occur, and the resources have to be disposed.
     * @throws FilePersistenceException
     *             if a persistence exception occurs while performing the operation
     */
    public void dispose() throws FilePersistenceException {
        timeOutTransferSessionsTask.cancel();
        timeOutTransferSessionsTimer.cancel();
        for (Iterator iter = bytesIterators.values().iterator(); iter.hasNext();) {
            ((BytesIterator) iter.next()).dispose();
        }
        bytesIterators.clear();
        filesToRetrieve.clear();
        filesToRetrieveLastAccessDates.clear();
        filesToUploadLastAccessDates.clear();
        persistence.dispose();
    }

    /**
     * Creates a unique String id using the GUID Generator. Simply return generator.getNextUUID().toString();
     * @return a unique String id
     */
    private String createUniqueBytesIteratorId() {
        return generator.getNextUUID().toString();
    }

    /**
     * This task is used by the FileSystemHandler to clear up expired sessions. It should clear both upload and retrieve
     * sessions using the filesToRetrieveLastAccessDates and filesToUploadLastAccessDates maps to detect expired
     * sessions.
     */
    private class TimeOutTransferSessionsTask extends TimerTask {
        /**
         * Represents the default transfer session timout.
         */
        private static final long DEFAULT_TRANSFER_SESSION_TIMEOUT = 10 * 60 * 1000;

        /**
         * Represents the check interval for this task. It will be used by the FileSystemHandler as the 'period' when it
         * will schedule this task.
         */
        private static final int CHECK_INTERVAL = 60 * 1000;

        /**
         * Represents the timeout in milliseconds for the transfer sessions (upload and retrieve files). Initialized in
         * constructor. Can be changed. Positive.
         */
        private long transferSessionTimeOut = DEFAULT_TRANSFER_SESSION_TIMEOUT;

        /**
         * Creates a new instance. Empty constructor.
         */
        public TimeOutTransferSessionsTask() {
        }

        /**
         * Returns the timeout in milliseconds for the transfer sessions (upload and retrieve files). Simply return the
         * corresponding field.
         * @return the timeout in milliseconds for the transfer sessions
         */
        public long getTransferSessionTimeOut() {
            return transferSessionTimeOut;
        }

        /**
         * Sets the timeout in milliseconds for the transfer sessions (upload and retrieve files). Simply assign the
         * argument to the corresponding field.
         * @param timeOut
         *            the timeout in milliseconds for the transfer sessions
         * @throws IllegalArgumentException
         *             if the argument is negative or 0
         */
        public void setTransferSessionTimeOut(long timeOut) {
            if (timeOut <= 0) {
                throw new IllegalArgumentException("argument is negative or 0");
            }
            transferSessionTimeOut = timeOut;
        }

        /**
         * This method should clear both upload and retrieve sessions using the filesToRetrieveLastAccessDates and
         * filesToUploadLastAccessDates maps to detect expired sessions.
         */
        public void run() {
            try {
                Date currentDate = new Date();
                for (Iterator bytesIter = filesToRetrieveLastAccessDates.keySet().iterator(); bytesIter.hasNext();) {
                    Object bytesIteratorId = bytesIter.next();
                    Date lastDate = (Date) filesToRetrieveLastAccessDates.get(bytesIteratorId);
                    if (currentDate.getTime() - lastDate.getTime() > transferSessionTimeOut) {
                        ((BytesIterator) bytesIterators.get(bytesIteratorId)).dispose();
                        bytesIterators.remove(bytesIteratorId);
                        filesToRetrieve.remove(bytesIteratorId);
                        filesToRetrieveLastAccessDates.remove(bytesIteratorId);
                    }
                }

                for (Iterator iter = filesToUploadLastAccessDates.keySet().iterator(); iter.hasNext();) {
                    String fileCreationId = (String) iter.next();
                    Date lastDate = (Date) filesToUploadLastAccessDates.get(fileCreationId);
                    if (currentDate.getTime() - lastDate.getTime() > transferSessionTimeOut) {
                        String fileId = (String) filesToUpload.get(fileCreationId);
                        persistence.closeFile(fileCreationId);
                        persistence.deleteFile(fileLocation, fileId);
                        filesToRetrieveLastAccessDates.remove(fileCreationId);
                        filesToUpload.remove(fileCreationId);
                        registry.removeFile(fileId);
                    }
                }
            } catch (FilePersistenceException e) {
                // ignore
            } catch (RegistryPersistenceException e) {
                // ignore
            } catch (FileIdNotFoundException e) {
                // ignore
            }
        }
    }
}
