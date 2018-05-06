/*
 * Copyright (C) 2006-2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.file.transfer.message.BytesMessageType;
import com.topcoder.file.transfer.message.MessageType;
import com.topcoder.file.transfer.message.RequestMessage;
import com.topcoder.file.transfer.message.ResponseMessage;
import com.topcoder.file.transfer.persistence.BytesIterator;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FilePersistenceException;
import com.topcoder.processor.ipserver.ConfigurationException;
import com.topcoder.processor.ipserver.IPClient;
import com.topcoder.processor.ipserver.message.MessageSerializationException;
import com.topcoder.util.generator.guid.Generator;
import com.topcoder.util.generator.guid.UUIDType;
import com.topcoder.util.generator.guid.UUIDUtility;

/**
 * <p>
 * The FileSystemClient extends the IPClient class from IP Server 2.0. The return type of its methods is of String
 * type the requestId to be used by the user to receive the response in blocking or non-blocking mode using
 * receiveResponse(requestId,blocking) method (the returned Message should be of ResponseMessage type). The user is
 * advised not to use ip client's receiveResponse(blocking) method, as it might lead to data corruption. The user
 * should use only the receiveResponse(requestId,blocking) method.
 * </p>
 *
 * <p>
 * <strong>Version 1.1. changes:</strong>
 * <ul>
 * <li>New methods to upload and retrieve files using streams;</li>
 * <li>New thread workers to perform the stream uploading and retrieving.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Sample usage for file in stream:</strong>
 *
 * <pre>
 *  //Upload a file in a stream, without over-writing an existing file, and give it a name
 *  // a stream with file data
 *  InputStream dataStream = new FileInputStream(
 *  new File(ConfigHelper.CLIENT_FILE_LOCATION + &quot;/&quot; + UPLOAD_FILE_NAME));
 *  String requestId = fileSystemClient.uploadFile(dataStream, UPLOAD_FILE_NAME);
 *  // check status
 *  FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
 *  if (status == FileUploadCheckStatus.UPLOAD_ACCEPTED) {
 *  while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
 *  Thread.sleep(500);
 *  }
 *  // get response message
 *  ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
 *  if (response != null &amp;&amp; response.getException() == null) {
 *  String fileId = (String) response.getResult();
 *  // use the file with the resulted file name and remove file
 *  requestId = fileSystemClient.removeFile(fileId);
 *  response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
 *  } else {
 *  // handle the exception
 *  }
 *  // Retrieve a file
 *  // a stream connected to the destination of the data
 *  OutputStream outStream = new ByteArrayOutputStream();
 *  requestId = fileSystemClient.retrieveFile(&quot;fileId&quot;, outStream);
 *  while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
 *  Thread.sleep(500);
 *  }
 *  // get response message
 *  ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
 *  if (response != null &amp;&amp; response.getException() == null) {
 *  String fileId = (String) response.getResult();
 *  // use the file with the resulted file name and remove file
 *  requestId = fileSystemClient.removeFile(fileId);
 *  response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
 *  } else {
 *  // handle the exception
 *  }
 * </pre>
 *
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong> This class is immutable and thread-safe.
 * </p>
 *
 * @author Luca, FireIce
 * @author argolite, TCSDEVELOPER
 * @version 1.1
 * @since 1.0
 */
public class FileSystemClient extends IPClient {

    /**
     * <p>
     * Represents the default size of byte chunks to send the data when using uploading directly from a stream. It is
     * used in the constructor if no value is provided.
     * </p>
     *
     * @since 1.1
     */
    public static final int DEFAULT_TRANSFER_BYTE_SIZE = 1024;

    /**
     * Represents the status sleep time for check.
     */
    private static final long CHECK_STATUS_SLEEP_TIME = 10;

    /**
     * Represents the id of the handler that will process requests on the server side. It is used to create request
     * messages and response messages for the non-blocking calls. Initialized in the constructor and never changed
     * later. Not null. Not empty.
     */
    private final String handlerId;

    /**
     * Represents the GUID generator used to generate unique request ids for the request messages. Initialized in the
     * constructor and never changed later. Not null.
     */
    private final Generator generator = UUIDUtility.getGenerator(UUIDType.TYPEINT32);

    /**
     * Represents the file persistence used to create files / delete files / retrieve bytes iterator over a file's
     * bytes. Initialized in the constructor and never changed later. Not null.
     */
    private final FilePersistence persistence;

    /**
     * Represents the error handler used by the file upload worker threads to signal an error. Initialized in the
     * constructor and never changed later. Not null.
     */
    private final FileTransferHandler uploadHandler;

    /**
     * Represents the error handler used by the file retrieve worker threads to signal an error. Initialized in the
     * constructor and never changed later. Not null.
     */
    private final FileTransferHandler retrieveHandler;

    /**
     * Represents the synchronized map of check upload statuses for uploaded files. The keys are of String type, not
     * null, not empty. The values are of FileUploadCheckStatus type, not null. Initialized in the constructor and
     * never changed. Can be empty.
     */
    private final Map checkUploadStatuses = Collections.synchronizedMap(new HashMap());

    /**
     * <p>
     * Represents the map of worker threads. The keys are Strings (request ids - the requestIds returned to the user,
     * so he can get his final message). The values are FileUploadWorkers or FileRetrieveWorkers).
     * </p>
     *
     * <p>
     * <strong>Version 1.1. change:</strong> Map now can also contain values of type DirectFileUploadWorker, and
     * DirectFileRetrieveWorker.
     * </p>
     *
     * <p>
     * It is initialized to a synchronized map on class construction. It is added to and removed by in the methods to
     * upload, retrieve, stop file transfers, and when checking if a transfer is on. It will not be null. It will
     * contain non-null/empty keys and no null values. It is managed as stated in usage section.
     * </p>
     */
    private final Map fileTransferWorkers = Collections.synchronizedMap(new HashMap());

    /**
     * <p>
     * Represents the size of byte chunks to send the data when using uploading directly from a stream.
     * </p>
     *
     * <p>
     * It is set in the constructor. It is run method of the DirectFileUploadWorker. It will be a positive number.
     * Default value will be 1024. Once set, it will not change.
     * </p>
     *
     * @since 1.1
     */
    private final int transferByteSize;

    /**
     * <p>
     * Creates an instance with the given arguments, and a transferByteSize equal to DEFAULT_TRANSFER_BYTE_SIZE.
     * </p>
     *
     * <p>
     * <strong>Version 1.1. change:</strong> Added default value for new parameter transferByteSize.
     * </p>
     *
     * @param address
     *            the address of the ip server (ip or textual form)
     * @param port
     *            the port of the ip server.
     * @param namespace
     *            the namespace used to create message factory.
     * @param handlerId
     *            the handler id
     * @param persistence
     *            the persistence
     * @param uploadHandler
     *            the upload handler
     * @param retrieveHandler
     *            the retrieve handler
     * @throws NullPointerException
     *             f any argument except port is null.
     * @throws IllegalArgumentException
     *             if the port is not within 0..65535, or the namespace or handleId is an empty string
     * @throws ConfigurationException
     *             if message factory cannot be created due to configuration error.
     */
    public FileSystemClient(String address, int port, String namespace, String handlerId,
            FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler)
            throws ConfigurationException {
        this(address, port, namespace, handlerId, persistence, uploadHandler, retrieveHandler,
                DEFAULT_TRANSFER_BYTE_SIZE);
    }

    /**
     * <p>
     * Creates an instance of this client with the given argument, and a transferByteSize equal to the passed namesake
     * parameter.
     * </p>
     *
     * @param address
     *            the address of the ip server (ip or textual form)
     * @param port
     *            the port of the ip server.
     * @param namespace
     *            the namespace used to create message factory.
     * @param handlerId
     *            the handler id
     * @param persistence
     *            the persistence
     * @param uploadHandler
     *            the upload handler
     * @param retrieveHandler
     *            the retrieve handler
     * @param transferByteSize
     *            the size of byte chunks to send the data when using uploading directly from a stream
     * @throws NullPointerException
     *             f any argument except port is null.
     * @throws IllegalArgumentException
     *             if the port is not within 0..65535, or the namespace or handleId is an empty string or
     *             transferByteSize is not positive
     * @throws ConfigurationException
     *             if message factory cannot be created due to configuration error.
     * @since 1.1
     */
    public FileSystemClient(String address, int port, String namespace, String handlerId,
            FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler,
            int transferByteSize) throws ConfigurationException {
        super(address, port, namespace);
        if (handlerId == null) {
            throw new NullPointerException("handlerId is null");
        } else if (handlerId.trim().length() == 0) {
            throw new IllegalArgumentException("handlerId is empty");
        }
        this.handlerId = handlerId;
        if (persistence == null) {
            throw new NullPointerException("persistence is null");
        }
        this.persistence = persistence;
        if (uploadHandler == null) {
            throw new NullPointerException("uploadHandler is null");
        }
        this.uploadHandler = uploadHandler;
        if (retrieveHandler == null) {
            throw new NullPointerException("retrieveErrorHandler is null");
        }
        this.retrieveHandler = retrieveHandler;

        if (transferByteSize <= 0) {
            throw new IllegalArgumentException("transferByteSize is not positive");
        }
        this.transferByteSize = transferByteSize;
    }

    /**
     * Uploads the file denoted by the arguments. The method returns the requestId to be used by the user to get the
     * actual response from the server using the receiveResponse(requestId,blocking) method. The user can check if the
     * file upload has been accepted using getFileUploadCheckStatus(requestId,blocking) method.
     *
     * @param fileLocation
     *            the file location
     * @param fileName
     *            the file name
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String uploadFile(String fileLocation, String fileName) throws IOException, MessageSerializationException {
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        if (fileName == null) {
            throw new NullPointerException("fileName is null");
        } else if (fileName.trim().length() == 0) {
            throw new IllegalArgumentException("fileName is empty");
        }
        if (!isConnected()) {
            throw new IllegalStateException("The client is not connected to the server");
        } else {
            String requestId = createUniqueRequestId();
            FileUploadWorker worker = new FileUploadWorker(fileLocation, fileName, requestId);
            fileTransferWorkers.put(requestId, worker);
            worker.start();
            return requestId;
        }
    }

    /**
     * Uploads the file denoted by the arguments, overwriting an existing file from the server. The method returns the
     * requestId to be used by the user to get the actual response from the server using the
     * receiveResponse(requestId,blocking) method. The user can check if the file upload has been accepted using
     * getFileUploadCheckStatus(requestId,blocking) method.
     *
     * @param fileId
     *            the file id
     * @param fileLocation
     *            the file location
     * @param fileName
     *            the file name
     * @return the message request id
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String uploadFile(String fileId, String fileLocation, String fileName) throws IOException,
            MessageSerializationException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        }
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        if (fileName == null) {
            throw new NullPointerException("fileName is null");
        } else if (fileName.trim().length() == 0) {
            throw new IllegalArgumentException("fileName is empty");
        }
        if (!isConnected()) {
            throw new IllegalStateException("The client is not connected to the server.");
        } else {
            String requestId = createUniqueRequestId();
            FileUploadWorker worker = new FileUploadWorker(fileId, fileLocation, fileName, requestId);
            fileTransferWorkers.put(requestId, worker);
            worker.start();
            return requestId;
        }
    }

    /**
     * <p>
     * Uploads the file denoted by the arguments. Since no name is provided, the server will use the generated fileId
     * as the file name.
     *
     * The method returns the requestId to be used by the user to get the actual response from the server using the
     * receiveResponse(requestId,blocking) method.
     *
     * The user can check if the file upload has been accepted using getFileUploadCheckStatus(requestId,blocking)
     * method.
     *
     * The stream will not be closed once data is retrieved.
     * </p>
     *
     * @param dataStream
     *            the stream with the data content
     * @return the requestId to be used by the user to get the actual response from the server
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @since 1.1
     */
    public String uploadFile(InputStream dataStream) {
        checkNull(dataStream, "dataStream");
        checkConnected();

        // Get request ID:
        String requestId = createUniqueRequestId();
        // Create a new worker:
        DirectFileUploadWorker worker = new DirectFileUploadWorker(dataStream, null, requestId);
        // Add to map:
        fileTransferWorkers.put(requestId, worker);
        // Start worker:
        worker.start();

        return requestId;
    }

    /**
     * <p>
     * Uploads the file denoted by the arguments.
     *
     * The method returns the requestId to be used by the user to get the actual response from the server using the
     * receiveResponse(requestId,blocking) method.
     *
     * The user can check if the file upload has been accepted using getFileUploadCheckStatus(requestId,blocking)
     * method.
     *
     * The stream will not be closed once data is retrieved.
     * </p>
     *
     * @param dataStream
     *            the stream with the data content
     * @param fileName
     *            the name of the file to use on the server
     * @return the requestId to be used by the user to get the actual response from the server
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @since 1.1
     */
    public String uploadFile(InputStream dataStream, String fileName) {
        checkNull(dataStream, "dataStream");
        checkString(fileName, "fileName");
        checkConnected();

        // Get request ID:
        String requestId = createUniqueRequestId();
        // Create a new worker:
        DirectFileUploadWorker worker = new DirectFileUploadWorker(dataStream, fileName, requestId);
        // Add to map:
        fileTransferWorkers.put(requestId, worker);
        // Start worker:
        worker.start();
        return requestId;
    }

    /**
     * <p>
     * Uploads the file denoted by the arguments, overwriting an existing file from the server. Since no name is
     * provided, the server will use the generated fileId as the file name.
     *
     * The method returns the requestId to be used by the user to get the actual response from the server using the
     * receiveResponse(requestId,blocking) method.
     *
     * The user can check if the file upload has been accepted using getFileUploadCheckStatus(requestId,blocking)
     * method.
     *
     * The stream will not be closed once data is retrieved.
     * </p>
     *
     * @param fileId
     *            the file id
     * @param dataStream
     *            the stream with the data content
     * @return the requestId to be used by the user to get the actual response from the server
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @since 1.1
     */
    public String uploadFile(String fileId, InputStream dataStream) {
        checkString(fileId, "fileId");
        checkNull(dataStream, "dataStream");
        checkConnected();

        // Get request ID:
        String requestId = createUniqueRequestId();
        // Create a new worker:
        DirectFileUploadWorker worker = new DirectFileUploadWorker(fileId, dataStream, null, requestId);
        // Add to map:
        fileTransferWorkers.put(requestId, worker);
        // Start worker:
        worker.start();
        return requestId;
    }

    /**
     * <p>
     * Uploads the file denoted by the arguments, overwriting an existing file from the server.
     *
     * The method returns the requestId to be used by the user to get the actual response from the server using the
     * receiveResponse(requestId,blocking) method.
     *
     * The user can check if the file upload has been accepted using getFileUploadCheckStatus(requestId,blocking)
     * method.
     *
     * The stream will not be closed once data is retrieved.
     * </p>
     *
     * @param fileId
     *            the file id
     * @param dataStream
     *            the stream with the data content
     * @param fileName
     *            the name of the file to use on the server
     * @return the requestId to be used by the user to get the actual response from the server
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @since 1.1
     */
    public String uploadFile(String fileId, InputStream dataStream, String fileName) {
        checkString(fileId, "fileId");
        checkNull(dataStream, "dataStream");
        checkString(fileName, "fileName");
        checkConnected();

        // Get request ID:
        String requestId = createUniqueRequestId();
        // Create a new worker:
        DirectFileUploadWorker worker = new DirectFileUploadWorker(fileId, dataStream, fileName, requestId);
        // Add to map:
        fileTransferWorkers.put(requestId, worker);
        // Start worker:
        worker.start();
        return requestId;
    }

    /**
     * <p>
     * Retrieves the file identified on the server by the fileId and writes the data into the passed output stream.
     *
     * The method returns the requestId that can be used by the user to confirm when the download is complete by
     * getting the actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * The stream will not be closed once data is populated.
     * </p>
     *
     * @param fileId
     *            the file id
     * @param dataStream
     *            the stream with the data content
     * @return the requestId to be used by the user to get the actual response from the server
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @since 1.1
     */
    public String retrieveFile(String fileId, OutputStream dataStream) {
        checkString(fileId, "fileId");
        checkNull(dataStream, "dataStream");
        checkConnected();

        // Get request ID:
        String requestId = createUniqueRequestId();
        // Create a new worker:
        DirectFileRetrieveWorker worker = new DirectFileRetrieveWorker(fileId, dataStream, requestId);
        // Add to map:
        fileTransferWorkers.put(requestId, worker);
        // Start worker:
        worker.start();
        return requestId;
    }

    /**
     * Returns the file upload check status. The status is removed if found, and returned to the user. If blocking is
     * true, it returns the status from the map, looping until there is a record in the map with the requestId as the
     * key. If blocking is false, it returns the status from the map, or null (signaling that the check has not been
     * performed yet).
     *
     * @param requestId
     *            the request id
     * @param blocking
     *            whether to block, or not
     * @return the file upload status
     * @throws InterruptedException
     *             if Thread throw this exception
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     */
    public FileUploadCheckStatus getFileUploadCheckStatus(String requestId, boolean blocking)
            throws InterruptedException {
        if (requestId == null) {
            throw new NullPointerException("requestId is null");
        } else if (requestId.trim().length() == 0) {
            throw new IllegalArgumentException("requestId is empty");
        }
        FileUploadCheckStatus status = (FileUploadCheckStatus) checkUploadStatuses.get(requestId);
        if (blocking) {
            while (status == null) {
                Thread.sleep(CHECK_STATUS_SLEEP_TIME);
                status = (FileUploadCheckStatus) checkUploadStatuses.get(requestId);
            }
        }
        if (status != null) {
            checkUploadStatuses.remove(requestId);
        }
        return status;
    }

    /**
     * Checks if the upload of the file denoted by the arguments is accepted. The method returns the requestId to be
     * used by the user to get the actual response from the server using the receiveResponse(requestId,blocking)
     * method.
     *
     * @param fileLocation
     *            the file location
     * @param fileName
     *            the file name
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String checkUploadFile(String fileLocation, String fileName) throws IOException,
            MessageSerializationException {
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        if (fileName == null) {
            throw new NullPointerException("fileName is null");
        } else if (fileName.trim().length() == 0) {
            throw new IllegalArgumentException("fileName is empty");
        }
        if (!isConnected()) {
            throw new IllegalStateException("The client is not connected to the server.");
        }
        Object[] args = new Object[2];
        args[0] = fileName;
        args[1] = new Long(new File(fileLocation, fileName).length());
        return sendRequestMessage(MessageType.CHECK_UPLOAD_FILE, args);
    }

    /**
     * Retrieves the file identified on the server by the fileId and saves it in the file denoted by the other
     * arguments. The method returns the requestId to be used by the user to get the actual response from the server
     * using the receiveResponse(requestId,blocking) method.
     *
     * @param fileId
     *            the file id
     * @param fileLocation
     *            the file location
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String retrieveFile(String fileId, String fileLocation) throws IOException, MessageSerializationException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        }
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        if (!isConnected()) {
            throw new IllegalStateException("The client is not connected to the server.");
        }
        String requestId = createUniqueRequestId();
        FileRetrieveWorker worker = new FileRetrieveWorker(fileId, fileLocation, requestId);
        fileTransferWorkers.put(requestId, worker);
        worker.start();
        return requestId;
    }

    /**
     * Returns the name of the file with the given file id. The method returns the requestId to be used by the user to
     * get the actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param fileId
     *            the file id
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String getFileName(String fileId) throws IOException, MessageSerializationException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        }
        return sendRequestMessage(MessageType.GET_FILE_NAME, fileId);
    }

    /**
     * Returns the size of the file with the given file id. The method returns the requestId to be used by the user to
     * get the actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param fileId
     *            the file id
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String getFileSize(String fileId) throws IOException, MessageSerializationException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        }
        return sendRequestMessage(MessageType.GET_FILE_SIZE, fileId);
    }

    /**
     * Renames the file identified by the fileId to the fileName. The method returns the requestId to be used by the
     * user to get the actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param fileId
     *            the file id
     * @param fileName
     *            the file name
     * @return the message request id
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String renameFile(String fileId, String fileName) throws IOException, MessageSerializationException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        }
        if (fileName == null) {
            throw new NullPointerException("fileName is null");
        } else if (fileName.trim().length() == 0) {
            throw new IllegalArgumentException("fileName is empty");
        }
        Object[] args = new Object[2];
        args[0] = fileId;
        args[1] = fileName;
        return sendRequestMessage(MessageType.RENAME_FILE, args);
    }

    /**
     * Removes from the server the file identified by the fileId The method returns the requestId to be used by the
     * user to get the actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param fileId
     *            the file id
     * @return the message request id
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String removeFile(String fileId) throws IOException, MessageSerializationException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        }
        return sendRequestMessage(MessageType.REMOVE_FILE, fileId);
    }

    /**
     * Creates a group with the given name and the given list of file ids. The method returns the requestId to be used
     * by the user to get the actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param groupName
     *            the group name
     * @param fileIds
     *            the file ids
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String createGroup(String groupName, List fileIds) throws IOException, MessageSerializationException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        }
        if (fileIds == null) {
            throw new NullPointerException("fileIds is null");
        }
        Object[] args = new Object[2];
        args[0] = groupName;
        args[1] = fileIds;
        return sendRequestMessage(MessageType.CREATE_GROUP, args);
    }

    /**
     * Updates a group identified by the given groupName. Its files are changed to the given fileIds list. The method
     * returns the requestId to be used by the user to get the actual response from the server using the
     * receiveResponse(requestId,blocking) method.
     *
     * @param groupName
     *            the group name
     * @param fileIds
     *            the file ids
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String updateGroup(String groupName, List fileIds) throws IOException, MessageSerializationException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        }
        if (fileIds == null) {
            throw new NullPointerException("fileIds is null");
        }
        Object[] args = new Object[2];
        args[0] = groupName;
        args[1] = fileIds;
        return sendRequestMessage(MessageType.UPDATE_GROUP, args);
    }

    /**
     * Retrieves the file ids list of the given group. The method returns the requestId to be used by the user to get
     * the actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param groupName
     *            the group name
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String retrieveGroup(String groupName) throws IOException, MessageSerializationException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        }
        return sendRequestMessage(MessageType.RETRIEVE_GROUP, groupName);
    }

    /**
     * Removes the given group from the server. The method returns the requestId to be used by the user to get the
     * actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param groupName
     *            the group name
     * @param removeFiles
     *            the remove files flag
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String removeGroup(String groupName, boolean removeFiles) throws IOException,
            MessageSerializationException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        }
        Object[] args = new Object[2];
        args[0] = groupName;
        args[1] = Boolean.valueOf(removeFiles);
        return sendRequestMessage(MessageType.REMOVE_GROUP, args);
    }

    /**
     * Adds the given file id to the given group. The method returns the requestId to be used by the user to get the
     * actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param groupName
     *            the group name
     * @param fileId
     *            the file id
     * @return the message request id
     * @throws IOException
     * @throws MessageSerializationException
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String addFileToGroup(String groupName, String fileId) throws IOException, MessageSerializationException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        }
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        }
        Object[] args = new Object[2];
        args[0] = groupName;
        args[1] = fileId;
        return sendRequestMessage(MessageType.ADD_FILE_TO_GROUP, args);
    }

    /**
     * Removes the given file id from the given group. The method returns the requestId to be used by the user to get
     * the actual response from the server using the receiveResponse(requestId,blocking) method.
     *
     * @param groupName
     *            the group name
     * @param fileId
     *            the file id
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String removeFileFromGroup(String groupName, String fileId) throws IOException,
            MessageSerializationException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        }
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        }
        Object[] args = new Object[2];
        args[0] = groupName;
        args[1] = fileId;
        return sendRequestMessage(MessageType.REMOVE_FILE_FROM_GROUP, args);
    }

    /**
     * Searches for files using the given file searcher name and the given criteria. The method returns the requestId
     * to be used by the user to get the actual response from the server using the receiveResponse(requestId,blocking)
     * method.
     *
     * @param fileSearcherName
     *            the file searcher name
     * @param criteria
     *            the search criteria
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string except the criteria
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String searchFiles(String fileSearcherName, String criteria) throws IOException,
            MessageSerializationException {
        if (fileSearcherName == null) {
            throw new NullPointerException("fileSearcherName is null");
        } else if (fileSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("fileSearcherName is empty");
        }
        if (criteria == null) {
            throw new NullPointerException("criteria is null");
        } else if (criteria.trim().length() == 0) {
            throw new IllegalArgumentException("criteria is empty");
        }
        Object[] args = new Object[2];
        args[0] = fileSearcherName;
        args[1] = criteria;
        return sendRequestMessage(MessageType.SEARCH_FILES, args);
    }

    /**
     * Searches for groups using the given group searcher name and the given criteria. The method returns the
     * requestId to be used by the user to get the actual response from the server using the
     * receiveResponse(requestId,blocking) method.
     *
     * @param groupSearcherName
     *            the group searcher name
     * @param criteria
     *            the search criteria
     * @return the message request id
     * @throws NullPointerException
     *             if any object argument is null
     * @throws IllegalArgumentException
     *             if any string argument is an empty string except the criteria
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if the message factory fails to serialize the request
     */
    public String searchGroups(String groupSearcherName, String criteria) throws IOException,
            MessageSerializationException {
        if (groupSearcherName == null) {
            throw new NullPointerException("groupSearcherName is null");
        } else if (groupSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("groupSearcherName is empty");
        }
        if (criteria == null) {
            throw new NullPointerException("criteria is null");
        } else if (criteria.trim().length() == 0) {
            throw new IllegalArgumentException("criteria is empty");
        }
        Object[] args = new Object[2];
        args[0] = groupSearcherName;
        args[1] = criteria;
        return sendRequestMessage(MessageType.SEARCH_GROUPS, args);
    }

    /**
     * <p>
     * Stops the worker from fileTransferWorkers mapped under requestId.
     * </p>
     *
     * <p>
     * <strong>Version 1.1. change:</strong> Method is modified to work with fileTransferWorkers map values of type
     * DirectFileUploadWorker, and DirectFileRetrieveWorker.
     * </p>
     *
     * @param requestId
     *            the requestId
     * @throws NullPointerException
     *             if requestId is null
     * @throws IllegalArgumentException
     *             if requestId is empty or no mapping exist with the requestId
     */
    public void stopFileTransfer(String requestId) {
        checkString(requestId, "requestId");

        Object obj = fileTransferWorkers.get(requestId);
        if (obj == null) {
            throw new IllegalArgumentException("no FileWorker assiociate with RequestId " + requestId + " exists.");
        } else if (obj instanceof FileRetrieveWorker) {
            ((FileRetrieveWorker) obj).stopTransfer();
        } else if (obj instanceof DirectFileUploadWorker) {
            ((DirectFileUploadWorker) obj).stopTransfer();
        } else if (obj instanceof DirectFileRetrieveWorker) {
            ((DirectFileRetrieveWorker) obj).stopTransfer();
        } else {
            ((FileUploadWorker) obj).stopTransfer();
        }
    }

    /**
     * Check the alive state of the file worker corresponding to the requestId. In other words, whether the process of
     * the file transfer for the given request still on.
     *
     * @param requestId
     *            the requestId
     * @return true if the FileTransferWorker exist, or false.
     * @throws NullPointerException
     *             if requestId is null
     * @throws IllegalArgumentException
     *             if requestId is empty
     */
    public boolean isFileTransferWorkerAlive(String requestId) {
        if (requestId == null) {
            throw new NullPointerException("requestId is null");
        } else if (requestId.trim().length() == 0) {
            throw new IllegalArgumentException("requestId is empty");
        }
        return fileTransferWorkers.containsKey(requestId);
    }

    /**
     * Returns the file persistence used to create files / delete files / retrieve bytes iterator over a file's bytes.
     * Simply return the corresponding field.
     *
     * @return the FilePersistence instance
     */
    public FilePersistence getPersistence() {
        return persistence;
    }

    /**
     * Returns the transfer handler used by the file upload worker threads to signal an error or upload bytes
     * successfully. Simply return the corresponding field.
     *
     * @return the uploadHandler
     */
    public FileTransferHandler getUploadHandler() {
        return uploadHandler;
    }

    /**
     * Returns the error handler used by the file retrieve worker threads to signal an error. Simply return the
     * corresponding field.
     *
     * @return the retriveHandler
     */
    public FileTransferHandler getRetrieveHandler() {
        return retrieveHandler;
    }

    /**
     * Returns the id of the handler that will process requests on the server side. It is used to create request
     * messages and response messages for the non-blocking calls. Simply return the corresponding field.
     *
     * @return the handlerId
     */
    protected String getHandlerId() {
        return handlerId;
    }

    /**
     * Returns the GUID generator used to generate unique request ids for the request mesages. Simply return the
     * corresponding field.
     *
     * @return the generator
     */
    protected Generator getGenerator() {
        return generator;
    }

    /**
     * Creates a unique String id using the GUID Generator.
     *
     * @return a unique String id
     */
    private String createUniqueRequestId() {
        return generator.getNextUUID().toString();
    }

    /**
     * Send the request message to server with proper MessageType and argument. each calling of this method will
     * create a unique request id to lately receive response.
     *
     * @param type
     *            the type of message.
     * @param arg
     *            the argument to send.
     * @return the request Id.
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if a socket error occurs during sending data
     */
    private String sendRequestMessage(MessageType type, Object arg) throws MessageSerializationException, IOException {
        String requestId = createUniqueRequestId();
        sendRequest(new RequestMessage(handlerId, requestId, type, arg));
        return requestId;
    }

    /**
     * Send the request message to server with proper message type and argument array. each calling of this method
     * will create a unique request id to lately receive response.
     *
     * @param type
     *            the type of message.
     * @param args
     *            the arguments array to send.
     * @return the request Id.
     * @throws IllegalStateException
     *             if the client is not connected to the server
     * @throws IOException
     *             if a socket error occurs during sending data
     * @throws MessageSerializationException
     *             if a socket error occurs during sending data
     */
    private String sendRequestMessage(MessageType type, Object[] args) throws MessageSerializationException,
            IOException {
        String requestId = createUniqueRequestId();
        sendRequest(new RequestMessage(handlerId, requestId, type, args));
        return requestId;
    }

    /**
     * <p>
     * Checks whether the given Object is null.
     * </p>
     *
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument to check
     * @throws NullPointerException
     *             if the given Object is null
     */
    private static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given Object is null.
     * </p>
     *
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument to check
     * @throws IllegalArgumentException
     *             if the given Object is null
     */
    private static void checkNullIAE(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given String is null or empty.
     * </p>
     *
     * @param arg
     *            the String to check
     * @param name
     *            the name of the String argument to check
     * @throws NullPointerException
     *             if the given string is null
     * @throws IllegalArgumentException
     *             if the given string is empty
     */
    private static void checkString(String arg, String name) {
        checkNull(arg, name);

        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }

    /**
     * <p>
     * Checks whether the given String is null or empty.
     * </p>
     *
     * @param arg
     *            the String to check
     * @param name
     *            the name of the String argument to check
     * @throws IllegalArgumentException
     *             if the given string is null or empty
     */
    private static void checkStringIAE(String arg, String name) {
        checkNullIAE(arg, name);

        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }

    /**
     * <p>
     * Checks if the client is connected to the server.
     * </p>
     *
     * @throws IllegalStateException
     *             if the client is not connected to the server
     */
    private void checkConnected() {
        if (!isConnected()) {
            throw new IllegalStateException("The client is not connected to the server.");
        }
    }

    /**
     * This thread is used by the client to retrieve a file from the server, in order to perform the job in background
     * and not expose the protocol to the user.
     */
    private class FileRetrieveWorker extends Thread {

        /**
         * boolean flag for the while loop in run method.
         */
        private boolean stopped;

        /**
         * Represents the file id this worker is retrieving from the server. Initialized in the constructor and never
         * changed later. Not null. Not empty.
         */
        private final String fileId;

        /**
         * Represents the file location this worker is saving the file to. Initialized in the constructor and never
         * changed later. Not null. Not empty.
         */
        private final String fileLocation;

        /**
         * Represents the request id for the final message this worker will send. It is the requestId returned to the
         * user by the retrieve(..) method. Initialized in the constructor and never changed later. Not null. Not
         * empty.
         */
        private final String finalRequestId;

        /**
         * Creates an instance with the given arguments.
         *
         * @param fileId
         *            the file id
         * @param fileLocation
         *            the file location
         * @param finalRequestId
         *            the final message request id
         * @throws NullPointerException
         *             if any argument is null
         * @throws IllegalArgumentException
         *             if any string argument is empty
         */
        public FileRetrieveWorker(String fileId, String fileLocation, String finalRequestId) {
            if (fileId == null) {
                throw new NullPointerException("fileId is null");
            } else if (fileId.trim().length() == 0) {
                throw new IllegalArgumentException("fileId is empty");
            }
            this.fileId = fileId;
            if (fileLocation == null) {
                throw new NullPointerException("fileLocation is null");
            } else if (fileLocation.trim().length() == 0) {
                throw new IllegalArgumentException("fileLocation is empty");
            }
            this.fileLocation = fileLocation;
            if (finalRequestId == null) {
                throw new NullPointerException("finalRequestId is null");
            } else if (finalRequestId.trim().length() == 0) {
                throw new IllegalArgumentException("finalRequestId is empty");
            }
            this.finalRequestId = finalRequestId;
        }

        /**
         * Stops the transfer performed by this thread. Called by FileSystemClient#stopFileTransfer(..) method.
         * Changes stopped to true.
         */
        public void stopTransfer() {
            synchronized (this) {
                stopped = true;
            }
        }

        /**
         * Receives the file denoted by the fileId from the server. The fileName is retrieved from the server, and the
         * file will be saved to the file denoted by fileLocation and fileName If any exception occurs, or an errored
         * message is received from the server, it calls retrieveErrorhandler.handleError(..) method. After sending a
         * message, the thread will receive the response in blocking mode. The auxiliary messages sent will require to
         * generate a unique requestId.
         */
        public void run() {
            stopped = false;
            String fileCreationId = null;
            try {
                // send a MessageType.RETRIEVE_FILE request message to get the fileName
                // and the bytesIteratorId from the server
                String requestId = sendRequestMessage(MessageType.RETRIEVE_FILE, fileId);
                // receive the BytesMessageType.START_RETRIEVE_FILE_BYTES response message
                ResponseMessage response = (ResponseMessage) receiveResponse(requestId, true);
                if (response.getException() != null) {
                    retrieveHandler.handleError(fileId, fileLocation, null, finalRequestId, response.getException());
                } else if (BytesMessageType.START_RETRIEVE_FILE_BYTES.equals(response.getType())) {
                    // get the fileName and bytesIteratorId from the response message
                    Object[] results = (Object[]) response.getResult();
                    String fileName = (String) results[0];
                    String bytesIteratorId = (String) results[1];
                    // create a new file in the persistence and get the creationFileId
                    fileCreationId = persistence.createFile(fileLocation, fileName);
                    // send a BytesMessageType.START_RETRIEVE_FILE_BYTES mesage, with the bytesIteratorid received
                    // in the previous message
                    requestId = sendRequestMessage(BytesMessageType.START_RETRIEVE_FILE_BYTES, bytesIteratorId);
                    while (!stopped) {
                        response = (ResponseMessage) receiveResponse(requestId, true);
                        if (response.getException() != null) {
                            retrieveHandler.handleError(fileId, fileLocation, fileName, finalRequestId, response
                                    .getException());
                            stopTransfer();
                        } else if (BytesMessageType.RETRIEVE_FILE_BYTES.equals(response.getType())) {
                            // get the bytes and append them in the persistence and send a new
                            // BytesMessageType.RETRIEVE_FILE_BYTES message to the server
                            persistence.appendBytes(fileCreationId, (byte[]) response.getResult());
                            // handle transfer progress
                            retrieveHandler.handleTransferProgress(fileId, fileCreationId, fileName, finalRequestId,
                                    ((byte[]) response.getResult()).length);
                            requestId = sendRequestMessage(BytesMessageType.START_RETRIEVE_FILE_BYTES,
                                    bytesIteratorId);
                        } else if (BytesMessageType.STOP_RETRIEVE_FILE_BYTES.equals(response.getType())) {
                            // close the file in the persistence and send a
                            // BytesMessageType.STOP_RETRIEVE_FILE_BYTES message with the finalRequestId
                            persistence.closeFile(fileCreationId);
                            sendRequest(new RequestMessage(handlerId, finalRequestId,
                                    BytesMessageType.STOP_RETRIEVE_FILE_BYTES, bytesIteratorId));
                            break;
                        }
                    }
                }
                fileTransferWorkers.remove(finalRequestId);
            } catch (IOException e) {
                // ignore
            } catch (MessageSerializationException e) {
                // ignore
            } catch (FilePersistenceException e) {
                // ignore
            } finally {
                fileTransferWorkers.remove(finalRequestId);
            }
        }
    }

    /**
     * This thread is used by the client to upload a file on the server, in order to perform the job in background and
     * not expose the protocol to the user.
     */
    private class FileUploadWorker extends Thread {

        /**
         * boolean flag for the while loop in run method.
         */
        private boolean stopped;

        /**
         * Represents the file id of the file this worker is sending. Initialized in the constructor and never changed
         * later. Can be null. Not empty.
         */
        private final String fileId;

        /**
         * Represents the file location this worker is sending the file from. Initialized in the constructor and never
         * changed later. Not null. Not empty.
         */
        private final String fileLocation;

        /**
         * Represents the file name of the file this worker is sending. Initialized in the constructor and never
         * changed later. Not null. Not empty.
         */
        private final String fileName;

        /**
         * Represents the request id for the final message this worker will send. It is the requestId returned to the
         * user by the upload(..) methods. Initialized in the constructor and never changed later. Not null. Not
         * empty.
         */
        private final String finalRequestId;

        /**
         * Creates an instance with the given arguments.
         *
         * @param fileLocation
         *            the file location
         * @param fileName
         *            the file name
         * @param finalRequestId
         *            the final message request id
         * @throws NullPointerException
         *             if any argument is null
         * @throws IllegalArgumentException
         *             if any string argument is empty
         */
        public FileUploadWorker(String fileLocation, String fileName, String finalRequestId) {
            if (fileLocation == null) {
                throw new NullPointerException("fileLocation is null");
            } else if (fileLocation.trim().length() == 0) {
                throw new IllegalArgumentException("fileLocation is empty");
            }
            this.fileLocation = fileLocation;
            if (fileName == null) {
                throw new NullPointerException("fileName is null");
            } else if (fileName.trim().length() == 0) {
                throw new IllegalArgumentException("fileName is empty");
            }
            this.fileName = fileName;
            if (finalRequestId == null) {
                throw new NullPointerException("finalRequestId is null");
            } else if (finalRequestId.trim().length() == 0) {
                throw new IllegalArgumentException("finalRequestId is empty");
            }
            this.finalRequestId = finalRequestId;
            this.fileId = null;
        }

        /**
         * Creates an instance with the given arguments.
         *
         * @param fileId
         *            the file id
         * @param fileLocation
         *            the file location
         * @param fileName
         *            the file name
         * @param finalRequestId
         *            the final message request id
         * @throws NullPointerException
         *             if any argument is null
         * @throws IllegalArgumentException
         *             if any string argument is empty
         */
        public FileUploadWorker(String fileId, String fileLocation, String fileName, String finalRequestId) {
            if (fileId == null) {
                throw new NullPointerException("fileId is null");
            } else if (fileId.trim().length() == 0) {
                throw new IllegalArgumentException("fileId is empty");
            }
            this.fileId = fileId;
            if (fileLocation == null) {
                throw new NullPointerException("fileLocation is null");
            } else if (fileLocation.trim().length() == 0) {
                throw new IllegalArgumentException("fileLocation is empty");
            }
            this.fileLocation = fileLocation;
            if (fileName == null) {
                throw new NullPointerException("fileName is null");
            } else if (fileName.trim().length() == 0) {
                throw new IllegalArgumentException("fileName is empty");
            }
            this.fileName = fileName;
            if (finalRequestId == null) {
                throw new NullPointerException("finalRequestId is null");
            } else if (finalRequestId.trim().length() == 0) {
                throw new IllegalArgumentException("finalRequestId is empty");
            }
            this.finalRequestId = finalRequestId;
        }

        /**
         * Stops the transfer performed by this thread. Called by FileSystemClient#stopFileTransfer(..) method.
         * Changes stopped to true.
         */
        public void stopTransfer() {
            synchronized (this) {
                stopped = true;
            }
        }

        /**
         * Uploads the file denoted by the given arguments on the server. If any exception occurs, or an errored
         * message is received from the server, it calls uploadErrorhandler.handleError(..) method. After sending a
         * message, the thread will receive the response in blocking mode. The auxiliary messages sent will require to
         * generate a unique requestId.
         */
        public void run() {
            stopped = false;
            try {
                Object[] args = new Object[2];
                args[0] = fileName;
                args[1] = new Long(new File(fileLocation, fileName).length());
                // send a MessageType.CHECK_UPLOAD request message to see if the upload is accepted
                String requestId = sendRequestMessage(MessageType.CHECK_UPLOAD_FILE, args);
                ResponseMessage response = (ResponseMessage) receiveResponse(requestId, true);
                if (MessageType.CHECK_UPLOAD_FILE.equals(response.getType())) {
                    Boolean result = (Boolean) response.getResult();
                    if (result != null && result.booleanValue()) {
                        // upload accepted
                        checkUploadStatuses.put(finalRequestId, FileUploadCheckStatus.UPLOAD_ACCEPTED);
                        uploadFile();
                    } else {
                        // recently CHECK_UPLOAD_FILE message will not response with a exception
                        // upload unaccepted
                        checkUploadStatuses.put(finalRequestId, FileUploadCheckStatus.UPLOAD_NOT_ACCEPTED);
                    }
                }
                // remove the file transfer worker associate with the final request id
                fileTransferWorkers.remove(finalRequestId);
            } catch (IOException e) {
                // ignore
            } catch (MessageSerializationException e) {
                // ignore
            } finally {
                fileTransferWorkers.remove(finalRequestId);
            }
        }

        /**
         * help method to upload file, use BytesIterator to looply get bytes to upload. it will stop if all bytes are
         * uploaded, or client stop the transfer session. if it gets a exception response, call
         * FileTransferHandler#handleError, or call FileTransferHandler#handlerTransferProgress method, if bytes send
         * correctly.
         *
         * @throws MessageSerializationException
         *             if Message fail to serialize
         * @throws IOException
         *             if I/O error occur.
         */
        private void uploadFile() throws MessageSerializationException, IOException {
            BytesIterator bytesIterator = null;
            try {
                stopped = false;
                Object[] args = createStartUploadFileBytesArgs();
                // start uploading file bytes
                String requestId = sendRequestMessage(BytesMessageType.START_UPLOAD_FILE_BYTES, args);
                ResponseMessage response = (ResponseMessage) receiveResponse(requestId, true);
                if (response.getException() != null) {
                    uploadHandler
                            .handleError(fileId, fileLocation, fileName, finalRequestId, response.getException());
                } else if (BytesMessageType.START_UPLOAD_FILE_BYTES.equals(response.getType())) {
                    // get the BytesIterator of the file
                    bytesIterator = persistence.getFileBytesIterator(fileLocation, fileName);
                    Object[] result = (Object[]) response.getResult();
                    String newFileId = (String) result[0];
                    String fileCreationId = (String) result[1];
                    // loop for send file bytes
                    while (!stopped && bytesIterator.hasNextBytes()) {
                        args = new Object[2];
                        args[0] = fileCreationId;
                        args[1] = bytesIterator.nextBytes();
                        requestId = sendRequestMessage(BytesMessageType.UPLOAD_FILE_BYTES, args);
                        response = (ResponseMessage) receiveResponse(requestId, true);
                        if (response.getException() != null) {
                            uploadHandler.handleError(fileId, fileLocation, fileName, finalRequestId, response
                                    .getException());
                            // as an exception returned, should stop the file transfer session
                            stopTransfer();
                        } else {
                            uploadHandler.handleTransferProgress(fileId, fileLocation, fileName, finalRequestId,
                                    ((byte[]) args[1]).length);
                        }
                    }
                    // send BytesMessageType.STOP_UPLOAD_FILE_BYTES message to stop file uploading.
                    stopUploadTransfer(fileCreationId, newFileId);
                }
            } catch (FilePersistenceException e) {
                // ignore
            } finally {
                if (bytesIterator != null) {
                    try {
                        bytesIterator.dispose();
                    } catch (FilePersistenceException e) {
                        // ignore
                    }
                }
            }
        }

        /**
         * stop upload file transfer session.
         *
         * @param fileCreationId
         *            the fileCreationId
         * @param newFileId
         *            the new generated file Id.
         * @throws MessageSerializationException
         *             if Message fail to serialize
         * @throws IOException
         *             if I/O error occur.
         */
        private void stopUploadTransfer(String fileCreationId, String newFileId)
                throws MessageSerializationException, IOException {
            String requestId = sendRequestMessage(BytesMessageType.STOP_UPLOAD_FILE_BYTES, fileCreationId);
            ResponseMessage response = (ResponseMessage) receiveResponse(requestId, true);
            if (BytesMessageType.STOP_UPLOAD_FILE_BYTES.equals(response.getType())) {
                Object[] args = new Object[2];
                args[0] = fileName;
                args[1] = newFileId;
                sendRequest(new RequestMessage(handlerId, finalRequestId, MessageType.UPLOAD_FILE, args));
            }
        }

        /**
         * create args need by BytesMessageType.START_UPLOAD_FILE_BYTES, depending on the value of the fileId and
         * fileName.
         *
         * @return the object array.
         */
        private Object[] createStartUploadFileBytesArgs() {
            Object[] args;
            if (fileId == null) {
                args = new Object[1];
                args[0] = fileName;
            } else {
                args = new Object[2];
                args[0] = fileName;
                args[1] = fileId;
            }
            return args;
        }
    }

    /**
     * <p>
     * This thread is used by the client to retrieve a file from the server and put it directly into a stream, in
     * order to perform the job in background and not expose the protocol to the user.
     * </p>
     *
     * @since 1.1
     */
    private class DirectFileRetrieveWorker extends Thread {
        /**
         * <p>
         * Represents the boolean flag for the while loop in run method.
         * </p>
         */
        private boolean stopped;

        /**
         * <p>
         * Represents the file id of the file this worker is receiving. It is set in the constructor. It is used in
         * the run method. Will not be null/empty. Once set, it will not change.
         * </p>
         */
        private final String fileId;

        /**
         * <p>
         * Represents the stream into which the worker will put the received data. It is set in the constructor. It is
         * used in the run method. Will not be null. Once set, it will not change.
         * </p>
         */
        private final OutputStream dataStream;

        /**
         * <p>
         * Represents the request id for the final message this worker will send. It is the requestId returned to the
         * user by the retrieve(..) method. It is set in the constructor. It is used in the run method. Will not be
         * null/empty. Once set, it will not change.
         * </p>
         */
        private final String finalRequestId;

        /**
         * <p>
         * Creates an instance of this worker with the given parameters.
         * </p>
         *
         * @param fileId
         *            the file id of the file this worker is receiving
         * @param dataStream
         *            the stream into which the worker will put the received data
         * @param finalRequestId
         *            the request id for the final message this worker will send
         * @throws IllegalArgumentException
         *             if fileId, dataStream or finalRequestId argument is null, or any string argument is empty
         */
        public DirectFileRetrieveWorker(String fileId, OutputStream dataStream, String finalRequestId) {
            checkStringIAE(fileId, "fileId");
            checkNullIAE(dataStream, "dataStream");
            checkStringIAE(finalRequestId, "finalRequestId");

            this.fileId = fileId;
            this.dataStream = dataStream;
            this.finalRequestId = finalRequestId;
        }

        /**
         * Stops the transfer performed by this thread. Called by FileSystemClient#stopFileTransfer(..) method.
         * Changes stopped to true.
         */
        public void stopTransfer() {
            synchronized (this) {
                stopped = true;
            }
        }

        /**
         * <p>
         * Receives the file denoted by the fileId from the server and fills the data stream with it.
         *
         * If any exception occurs, or an error message is received from the server, it calls
         * uploadErrorHandler.handleError(..) method.
         *
         * After sending a message, the thread will receive the response in blocking mode. The auxiliary messages sent
         * will require to generate a unique requestId.
         * </p>
         */
        public void run() {
            stopped = false;
            try {
                // send a MessageType.RETRIEVE_FILE request message to get the fileName and the bytesIteratorId from
                // the servers
                String requestId = sendRequestMessage(MessageType.RETRIEVE_FILE, fileId);
                // receive the BytesMessageType.START_RETRIEVE_FILE_BYTES response message
                ResponseMessage response = (ResponseMessage) receiveResponse(requestId, true);
                if (response.getException() != null) {
                    retrieveHandler.handleError(fileId, null, null, finalRequestId, response.getException());
                } else if (BytesMessageType.START_RETRIEVE_FILE_BYTES.equals(response.getType())) {
                    // get the fileName and bytesIteratorId from the response message
                    Object[] results = (Object[]) response.getResult();
                    String fileName = (String) results[0];
                    String bytesIteratorId = (String) results[1];

                    // send a BytesMessageType.START_RETRIEVE_FILE_BYTES message, with the bytesIteratorid received
                    // in the previous message
                    requestId = sendRequestMessage(BytesMessageType.START_RETRIEVE_FILE_BYTES, bytesIteratorId);

                    while (!stopped) {
                        response = (ResponseMessage) receiveResponse(requestId, true);
                        if (response.getException() != null) {
                            retrieveHandler.handleError(fileId, null, fileName, finalRequestId, response
                                    .getException());
                            stopTransfer();
                        } else if (BytesMessageType.RETRIEVE_FILE_BYTES.equals(response.getType())) {
                            // get the bytes and append them to dataStream and send a new
                            // BytesMessageType.RETRIEVE_FILE_BYTES message to the server
                            dataStream.write((byte[]) response.getResult());

                            // handle transfer progress
                            retrieveHandler.handleTransferProgress(fileId, null, fileName, finalRequestId,
                                    ((byte[]) response.getResult()).length);
                            requestId = sendRequestMessage(BytesMessageType.START_RETRIEVE_FILE_BYTES,
                                    bytesIteratorId);
                        } else if (BytesMessageType.STOP_RETRIEVE_FILE_BYTES.equals(response.getType())) {
                            // send a BytesMessageType.STOP_RETRIEVE_FILE_BYTES message with the finalRequestId
                            sendRequest(new RequestMessage(handlerId, finalRequestId,
                                    BytesMessageType.STOP_RETRIEVE_FILE_BYTES, bytesIteratorId));
                            break;
                        }
                    }
                }
                fileTransferWorkers.remove(finalRequestId);
            } catch (IOException e) {
                // ignore
            } catch (MessageSerializationException e) {
                // ignore
            } finally {
                fileTransferWorkers.remove(finalRequestId);
            }
        }
    }

    /**
     * <p>
     * This thread is used by the client to upload a file on the server directly from a stream, in order to perform
     * the job in background and not expose the protocol to the user.
     *
     * If no fileName is provided, then it will make the fileId as the fileName.
     * </p>
     *
     * @since 1.1
     */
    private class DirectFileUploadWorker extends Thread {

        /**
         * <p>
         * Represents the boolean flag for the while loop in run method.
         * </p>
         */
        private boolean stopped;

        /**
         * <p>
         * Represents the file id of the file this worker is sending. It is set in the constructor. It is used in the
         * run method. Can be null. Will not be empty. Once set, it will not change.
         * </p>
         */
        private final String fileId;

        /**
         * <p>
         * Represents the data of the file this worker is sending. It is set in the constructor. It is used in the run
         * method. Will not be null. Once set, it will not change.
         * </p>
         */
        private final InputStream dataStream;

        /**
         * <p>
         * Represents the name of the file this worker is sending. It is set in the constructor. It is used in the run
         * method. Can be null. Will not be empty. But if initially null, it will be eventually set to the fileId
         * during the run method. Once set, it will not change.
         * </p>
         */
        private final String fileName;

        /**
         * <p>
         * Represents the request id for the final message this worker will send. It is the requestId returned to the
         * user by the upload(..) methods. It is set in the constructor. It is used in the run method. Will not be
         * null/empty. Once set, it will not change.
         * </p>
         */
        private final String finalRequestId;

        /**
         * <p>
         * Creates an instance of this worker with the given parameters and a null fileId.
         * </p>
         *
         * @param dataStream
         *            the data of the file this worker is sending.
         * @param fileName
         *            the file name of the file this worker is sending
         * @param finalRequestId
         *            the request id for the final message this worker will send
         * @throws IllegalArgumentException
         *             if dataStream or finalRequestId argument is null, or if any string argument is empty
         */
        public DirectFileUploadWorker(InputStream dataStream, String fileName, String finalRequestId) {
            checkNullIAE(dataStream, "dataStream");
            checkStringIAE(finalRequestId, "finalRequestId");
            if (fileName != null && fileName.trim().length() == 0) {
                throw new IllegalArgumentException("fileName is empty");
            }

            this.dataStream = dataStream;
            this.fileName = fileName;
            this.finalRequestId = finalRequestId;
            this.fileId = null;
        }

        /**
         * <p>
         * Creates an instance of this worker with the given parameters.
         * </p>
         *
         * @param fileId
         *            the file id of the file this worker is sending
         * @param dataStream
         *            the data of the file this worker is sending.
         * @param fileName
         *            the file name of the file this worker is sending
         * @param finalRequestId
         *            the request id for the final message this worker will send
         * @throws IllegalArgumentException
         *             if fileId, dataStream or finalRequestId argument is null, or if any string argument is empty
         */
        public DirectFileUploadWorker(String fileId, InputStream dataStream, String fileName, String finalRequestId) {
            checkStringIAE(fileId, "fileId");
            checkNullIAE(dataStream, "dataStream");
            checkStringIAE(finalRequestId, "finalRequestId");
            if (fileName != null && fileName.trim().length() == 0) {
                throw new IllegalArgumentException("fileName is empty");
            }

            this.dataStream = dataStream;
            this.fileName = fileName;
            this.finalRequestId = finalRequestId;
            this.fileId = fileId;
        }

        /**
         * Stops the transfer performed by this thread. Called by FileSystemClient#stopFileTransfer(..) method.
         * Changes stopped to true.
         */
        public void stopTransfer() {
            synchronized (this) {
                stopped = true;
            }
        }

        /**
         * <p>
         * Uploads the file denoted by the given arguments on the server.
         *
         * If any exception occurs, or an error message is received from the server, it calls
         * uploadErrorHandler.handleError(..) method.
         *
         * After sending a message, the thread will receive the response in blocking mode. The auxiliary messages sent
         * will require to generate a unique requestId.
         * </p>
         */
        public void run() {
            stopped = false;
            try {
                if (fileName != null) {
                    Object[] args = new Object[2];
                    args[0] = fileName;
                    args[1] = new Long(dataStream.available());

                    // send a MessageType.CHECK_UPLOAD request message to see if the upload is accepted
                    String requestId = sendRequestMessage(MessageType.CHECK_UPLOAD_FILE, args);
                    ResponseMessage response = (ResponseMessage) receiveResponse(requestId, true);
                    if (MessageType.CHECK_UPLOAD_FILE.equals(response.getType())) {
                        Boolean result = (Boolean) response.getResult();
                        if (result != null && result.booleanValue()) {
                            // upload accepted
                            checkUploadStatuses.put(finalRequestId, FileUploadCheckStatus.UPLOAD_ACCEPTED);
                            uploadFile();
                        } else {
                            // recently CHECK_UPLOAD_FILE message will not response with a exception
                            // upload not accepted
                            checkUploadStatuses.put(finalRequestId, FileUploadCheckStatus.UPLOAD_NOT_ACCEPTED);
                        }
                    }
                } else {
                    // upload accepted
                    checkUploadStatuses.put(finalRequestId, FileUploadCheckStatus.UPLOAD_ACCEPTED);
                    uploadFile();
                }

                // remove the file transfer worker associate with the final request id
                fileTransferWorkers.remove(finalRequestId);
            } catch (IOException e) {
                // ignore
            } catch (MessageSerializationException e) {
                // ignore
            } finally {
                fileTransferWorkers.remove(finalRequestId);
            }
        }

        /**
         * <p>
         * Help method to upload file, use BytesIterator to looply get bytes to upload. it will stop if all bytes are
         * uploaded, or client stop the transfer session. if it gets a exception response, call
         * FileTransferHandler#handleError, or call FileTransferHandler#handlerTransferProgress method, if bytes send
         * correctly.
         * </p>
         *
         * @throws MessageSerializationException
         *             if Message fail to serialize
         * @throws IOException
         *             if I/O error occur.
         */
        private void uploadFile() throws MessageSerializationException, IOException {
            stopped = false;
            Object[] args = createStartUploadFileBytesArgs();
            // start uploading file bytes
            String requestId = sendRequestMessage(BytesMessageType.START_UPLOAD_FILE_BYTES, args);
            ResponseMessage response = (ResponseMessage) receiveResponse(requestId, true);
            if (response.getException() != null) {
                uploadHandler.handleError(fileId, null, fileName, finalRequestId, response.getException());
            } else if (BytesMessageType.START_UPLOAD_FILE_BYTES.equals(response.getType())) {
                // get the BytesIterator of the file
                Object[] result = (Object[]) response.getResult();
                String newFileId = (String) result[0];
                String fileCreationId = (String) result[1];

                // loop for send file bytes
                while (!stopped && dataStream.available() != 0) {
                    args = new Object[2];
                    args[0] = fileCreationId;

                    byte[] buffer = null;
                    if (dataStream.available() >= transferByteSize) {
                        buffer = new byte[transferByteSize];
                    } else {
                        buffer = new byte[dataStream.available()];
                    }
                    dataStream.read(buffer);
                    args[1] = buffer;

                    requestId = sendRequestMessage(BytesMessageType.UPLOAD_FILE_BYTES, args);
                    response = (ResponseMessage) receiveResponse(requestId, true);
                    if (response.getException() != null) {
                        uploadHandler.handleError(fileId, null, fileName, finalRequestId, response.getException());
                        // as an exception returned, should stop the file transfer session
                        stopTransfer();
                    } else {
                        uploadHandler.handleTransferProgress(fileId, null, fileName, finalRequestId,
                                ((byte[]) args[1]).length);
                    }
                }
                // send BytesMessageType.STOP_UPLOAD_FILE_BYTES message to stop file uploading.
                stopUploadTransfer(fileCreationId, newFileId);
            }
        }

        /**
         * <p>
         * Stop upload file transfer session.
         * </p>
         *
         * @param fileCreationId
         *            the fileCreationId
         * @param newFileId
         *            the new generated file Id.
         * @throws MessageSerializationException
         *             if Message fail to serialize
         * @throws IOException
         *             if I/O error occur.
         */
        private void stopUploadTransfer(String fileCreationId, String newFileId)
                throws MessageSerializationException, IOException {
            String requestId = sendRequestMessage(BytesMessageType.STOP_UPLOAD_FILE_BYTES, fileCreationId);
            ResponseMessage response = (ResponseMessage) receiveResponse(requestId, true);
            if (BytesMessageType.STOP_UPLOAD_FILE_BYTES.equals(response.getType())) {
                Object[] args = new Object[2];
                args[0] = (fileName == null) ? "null" : fileName;
                args[1] = newFileId;
                sendRequest(new RequestMessage(handlerId, finalRequestId, MessageType.UPLOAD_FILE, args));
            }
        }

        /**
         * <p>
         * Creates args need by BytesMessageType.START_UPLOAD_FILE_BYTES, depending on the value of the fileId and
         * fileName.
         * </p>
         *
         * @return the object array.
         */
        private Object[] createStartUploadFileBytesArgs() {
            Object[] args;
            if (fileName == null && fileId == null) {
                args = new Object[1];
                args[0] = "null";
            } else if (fileId == null) {
                args = new Object[1];
                args[0] = fileName;
            } else if (fileName == null) {
                args = new Object[1];
                args[0] = fileId;
            } else {
                args = new Object[2];
                args[0] = fileName;
                args[1] = fileId;
            }
            return args;
        }
    }

}
