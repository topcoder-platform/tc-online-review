/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.topcoder.file.transfer.FileSystemClient;
import com.topcoder.file.transfer.FileTransferHandler;
import com.topcoder.file.transfer.FileUploadCheckStatus;
import com.topcoder.file.transfer.message.ResponseMessage;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;

import com.topcoder.processor.ipserver.message.MessageSerializationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.UnresolvedAddressException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;


/**
 * <p>
 * This class represents the persistent storage of the uploaded files in the remote file system. The remote file system
 * is provided by the File System Server component. This class works by saving the uploaded files using unique file
 * names into a temporary directory in the local file system first, then calling the File System Server to upload the
 * files. These locally saved files act as a cache, and should be removed from the temporary directory upon the exit
 * of JVM. The file id is provided by the File System Server (assigned during upload). This will allow uploads with
 * the same remote file name under a request or different requests. Overwriting of files is not allowed.
 * </p>
 *
 * <p>
 * <code>RemoteFileUpload</code> can be used in a clustered environment when the request is served from another node.
 * However the upload and download time will be slower because the data is transferred over the network, especially
 * when the file size is large.
 * </p>
 *
 * <p>
 * Note that content type information is not stored permanently in the persistence. When retrieving the uploaded file
 * with the file id, the content type will not be available.
 * </p>
 *
 * <p>
 * The <code>FileSystemClient</code> connects to the File System Server in the constructor. Once constructed the
 * communication becomes transparent to the user. It is important to explicitly call the disconnect() method after use
 * (with the <code>RemoteFileUpload</code> instance) to release the resources.
 * </p>
 *
 * <p>
 * This class should be initialized from a configuration namespace which should be preloaded. In addition to the base
 * class, this class supports extra properties:
 *
 * <ul>
 * <li>
 * temp_dir (optional) - the temporary directory to write uploaded files under. Default temporary directory is provided
 * by the system property.
 * </li>
 * <li>
 * ip_address (required) - the ip address of the file system server.
 * </li>
 * <li>
 * port (required) - the port that the file system server is listening at.
 * </li>
 * <li>
 * message_namespace (required) - the namespace used to create message factory.
 * </li>
 * <li>
 * handler_id (required) - the id of the handler that will process requests on the server side.
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is thread-safe by being immutable. The FileSystemClient can be used by multiple
 * threads once connected.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class RemoteFileUpload extends FileUpload {
    /**
     * <p>
     * The property key specifying the temporary directory to write uploaded files under. This property is optional.
     * Default temporary directory is provided by the system property.
     * </p>
     */
    public static final String TEMP_DIR_PROPERTY = "temp_dir";

    /**
     * <p>
     * The property key specifying the ip address of the file system server. This should be a valid ip address. This
     * property is required.
     * </p>
     */
    public static final String IP_ADDRESS_PROPERTY = "ip_address";

    /**
     * <p>
     * The property key specifying the port that the file system server is listening at. This should be a valid port
     * number between 0 and 65535. This property is required.
     * </p>
     */
    public static final String PORT_PROPERTY = "port";

    /**
     * <p>
     * The property key specifying the namespace used to create message factory. This property is required.
     * </p>
     */
    public static final String MESSAGE_NAMESPACE_PROPERTY = "message_namespace";

    /**
     * <p>
     * The property key specifying the id of the handler that will process requests on the server side. This property
     * is required.
     * </p>
     */
    public static final String HANDLER_ID_PROPERTY = "handler_id";

    /**
     * <p>
     * Represents the ip address to connect to the file system server.
     * </p>
     */
    private final String ipAddress;

    /**
     * <p>
     * Represents the port to connect to the file system server.
     * </p>
     */
    private final long port;

    /**
     * <p>
     * Represents the message namespace to connect to the file system server.
     * </p>
     */
    private final String messageNamespace;

    /**
     * <p>
     * Represents the handler id to connect to the file system server.
     * </p>
     */
    private final String handlerId;

    /**
     * <p>
     * Represents the temporary directory to write uploaded files under. It cannot be null or empty string.
     * </p>
     */
    private final String tempDir;

    /**
     * <p>
     * Creates a new instance of <code>RemoteFileUpload</code> class to load configuration from the specified
     * namespace. The namespace should be preloaded.
     * </p>
     *
     * @param namespace the configuration namespace to use.
     *
     * @throws IllegalArgumentException if namespace is null or empty string.
     * @throws ConfigurationException if any configuration error occurs.
     * @throws PersistenceException if it fails to connect to the persistence.
     */
    public RemoteFileUpload(String namespace) throws ConfigurationException, PersistenceException {
        super(namespace);

        // read the extra property values
        String dirValue = FileUploadHelper.getStringPropertyValue(namespace, TEMP_DIR_PROPERTY, false);

        if (dirValue == null) {
            // Default temporary directory is provided by the system property.
            tempDir = System.getProperty("java.io.tmpdir");
        } else {
            tempDir = dirValue;
        }

        FileUploadHelper.validateDirectory(tempDir);

        this.ipAddress = FileUploadHelper.getStringPropertyValue(namespace, IP_ADDRESS_PROPERTY, true);
        this.port = FileUploadHelper.getLongPropertyValue(namespace, PORT_PROPERTY);

        if ((port < 0) || (port > 65535)) {
            throw new ConfigurationException("The port should be in [0, 65535]");
        }

        this.messageNamespace = FileUploadHelper.getStringPropertyValue(namespace, MESSAGE_NAMESPACE_PROPERTY, true);
        this.handlerId = FileUploadHelper.getStringPropertyValue(namespace, HANDLER_ID_PROPERTY, true);
        FileSystemPersistence persistence = new FileSystemPersistence();
        FileTransferHandler handle = new DefaultFileTransferHandler();

        // create the file system client instance and connect to the server
        try {
            FileSystemClient client = new FileSystemClient(ipAddress, (int) port, messageNamespace,
                    handlerId, persistence, handle, handle);
            client.connect();
            client.disconnect();
        } catch (IOException e) {
            throw new PersistenceException("Can not connect to the server successfully.", e);
        } catch (com.topcoder.processor.ipserver.ConfigurationException e) {
            throw new ConfigurationException("The configuration value is not correct.", e);
        } catch (UnresolvedAddressException e) {
            throw new ConfigurationException("The ip address is not correct.", e);
        }
    }

    /**
     * <p>
     * Gets the temporary directory to write uploaded files under.
     * </p>
     *
     * @return the temporary directory to write uploaded files unders.
     */
    public String getTempDir() {
        return this.tempDir;
    }

    /**
     * <p>
     * Parses the uploaded files and parameters from the given request and parser. It saves each uploaded file using a
     * unique file name under the temporary directory in the local file system, uploads the files to the remote file
     * system, and removes them from the temporary directory upon the exit of JVM.
     * </p>
     *
     * <p>
     * Note that if there is any failure, the file just written will be deleted. All the previously written files are
     * kept.
     * </p>
     *
     * @param request the servlet request to be parsed.
     * @param parser the parser to use.
     *
     * @return the <code>FileUploadResult</code> containing uploaded files and parameters information.
     *
     * @throws IllegalArgumentException if request or parser is null.
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints,
     *         e.g. file size limit is exceeded.
     * @throws PersistenceException if it fails to upload the files to the persistence.
     */
    public FileUploadResult uploadFiles(ServletRequest request, RequestParser parser)
        throws RequestParsingException, PersistenceException {
        FileUploadHelper.validateNotNull(request, "request");
        FileUploadHelper.validateNotNull(parser, "parser");

        synchronized (parser) {
            FileSystemClient client = createClient();
            try {
                // parse the request with the parse
                parser.parseRequest(request);

                Map uploadedFiles = new HashMap();
                long currentTotalSize = 0;

                while (parser.hasNextFile()) {
                    // get the unique file name
                    String fileName = getUniqueFileName(parser.getRemoteFileName());

                    // get the form file name
                    String formFileName = parser.getFormFileName();

                    // get the content type
                    String contentType = parser.getContentType();

                    File uploadedFile = null;
                    OutputStream output = null;
                    uploadedFile = new File(tempDir, fileName);
                    uploadedFile.deleteOnExit();

                    try {
                        output = new FileOutputStream(uploadedFile);
                        parser.writeNextFile(output,
                                Math.min(getSingleFileLimit(), getTotalFileLimit() - currentTotalSize));
                    } catch (IOException e) {
                        // if there is any failure, the file just written will be deleted.
                        throw new PersistenceException("Fails to upload the files to the persistence.", e);
                    } finally {
                        FileUploadHelper.closeOutputStream(output);
                    }

                    // update the current total size of the uploaded files
                    currentTotalSize += uploadedFile.length();

                    // upload the file to the file system server
                    String fileId = this.uploadFile(client, tempDir, fileName);

                    // add the new updated file
                    List files = (List) uploadedFiles.get(formFileName);

                    if (files == null) {
                        files = new ArrayList();
                        uploadedFiles.put(formFileName, files);
                    }

                    files.add(new LocalUploadedFile(uploadedFile, fileId, contentType));
                }

                return new FileUploadResult(parser.getParameters(), uploadedFiles);
            } finally {
                this.disconnect(client);
            }
        }
    }

    /**
     * <p>
     * Uploads the file to the file server.
     * </p>
     *
     * @param client the FileSystemClient to connect to the file system server.
     * @param fileLocation the location of the file. It will be the directory name.
     * @param fileName the name of the file.
     *
     * @return the fileId generated by the file server which will be used to identify the file in the file sever.
     *
     * @throws PersistenceException if it fails to upload the files to the file server.
     */
    private String uploadFile(FileSystemClient client, String fileLocation, String fileName)
        throws PersistenceException {
        try {
            String requestId = client.uploadFile(fileLocation, fileName);

            FileUploadCheckStatus status = client.getFileUploadCheckStatus(requestId, true);

            if (status == FileUploadCheckStatus.UPLOAD_ACCEPTED) {
                // if the file is accepted by the file server
                while (client.isFileTransferWorkerAlive(requestId)) {
                    Thread.sleep(500);
                }

                ResponseMessage response = (ResponseMessage) client.receiveResponse(requestId, true);

                if ((response != null) && (response.getException() == null)) {
                    return (String) response.getResult();
                } else {
                    if (response != null) {
                        throw new PersistenceException("Can not successfully upload the file.",
                                response.getException());
                    } else {
                        throw new PersistenceException(
                            "Can not successfully upload the file since the response is null.");
                    }
                }
            }

            throw new PersistenceException(
                "Can not successfully upload the file since the file is not accepted by the file server.");
        } catch (MessageSerializationException e) {
            throw new PersistenceException("Message factory fails to serialize the request.", e);
        } catch (IOException e) {
            throw new PersistenceException("A socket error occurs during sending data.", e);
        } catch (InterruptedException e) {
            throw new PersistenceException("Current thread is interrupted.", e);
        }
    }

    /**
     * <p>
     * Retrieves the uploaded file from the persistence with the specified file id. In this case, retrieve the file
     * from the remote file system. The file id could be used directly by the file system server to retrieve the file.
     * </p>
     *
     * <p>
     * To improve performance, this method will first check if the file has been downloaded in the temporary directory
     * (through upload or previous call). If it does and refresh is false, there is no need to download the whole file
     * contents again from the file system server (assuming the file is not modified externally).
     * </p>
     *
     * <p>
     * Note that content type information is not stored permanently in the persistence. When retrieving the uploaded
     * file with the file id, the content type will not be available.
     * </p>
     *
     * @param fileId the id of the file to retrieve.
     * @param refresh whether to refresh the cached file copy.
     *
     * @return the uploaded file.
     *
     * @throws IllegalArgumentException if fileId is null or empty string.
     * @throws PersistenceException if any error occurs in retrieving the file from persistence.
     * @throws FileDoesNotExistException if the file does not exist in persistence.
     */
    public UploadedFile getUploadedFile(String fileId, boolean refresh)
        throws PersistenceException, FileDoesNotExistException {
        FileUploadHelper.validateString(fileId, "fileId");

        FileSystemClient client = createClient();

        try {
            // get the file name
            String fileName = getFileName(client, fileId);

            File file = new File(this.tempDir, fileName);

            if (!file.exists() || refresh) {
                String requestId = client.retrieveFile(fileId, tempDir);

                while (client.isFileTransferWorkerAlive(requestId)) {
                    Thread.sleep(500);
                }

                ResponseMessage response = (ResponseMessage) client.receiveResponse(requestId, true);

                if (response == null) {
                    throw new PersistenceException("Can not successfully get the file since the response is null.");
                } else if (response.getException() != null) {
                    throw new PersistenceException("Can not successfully get the file.", response.getException());
                }

                file = new File(tempDir, fileName);
            }

            file.deleteOnExit();

            return new LocalUploadedFile(file, fileId, null);
        } catch (FileDoesNotExistException e) {
            throw e;
        } catch (MessageSerializationException e) {
            throw new PersistenceException("Message factory fails to serialize the request.", e);
        } catch (IOException e) {
            throw new PersistenceException("A socket error occurs during sending data.", e);
        } catch (InterruptedException e) {
            throw new PersistenceException("Current thread is interrupted.", e);
        } finally {
            this.disconnect(client);
        }
    }

    /**
     * <p>
     * Removes the uploaded file from the persistence with the specified file id. Once removed the file contents are
     * lost. In this case, simply remove the file from the remote file system. The file id could be used directly by
     * the file system server to remove the file.
     * </p>
     *
     * @param fileId the id of the file to remove
     *
     * @throws IllegalArgumentException if fileId is null or empty string
     * @throws PersistenceException if any error occurs in removing the file from persistence.
     * @throws FileDoesNotExistException if the file does not exist in persistence.
     */
    public void removeUploadedFile(String fileId) throws PersistenceException, FileDoesNotExistException {
        FileUploadHelper.validateString(fileId, "fileId");

        FileSystemClient client = createClient();
        try {
            // get the file name
            String fileName = getFileName(client, fileId);

            // remove the locally cached file under the tempDir using the file name.
            new File(this.tempDir, fileName).delete();

            // remove the file from the server
            String requestId;

            requestId = client.removeFile(fileId);

            ResponseMessage response = (ResponseMessage) client.receiveResponse(requestId, true);

            if (response == null) {
                throw new PersistenceException("Can not successfully remove the file since the response is null.");
            } else if (response.getException() != null) {
                throw new PersistenceException("Can not successfully remove the file.", response.getException());
            }
        } catch (FileDoesNotExistException e) {
            throw e;
        } catch (MessageSerializationException e) {
            throw new PersistenceException("Message factory fails to serialize the request.", e);
        } catch (IOException e) {
            throw new PersistenceException("A socket error occurs during sending data.", e);
        } finally {
            this.disconnect(client);
        }
    }

    /**
     * <p>
     * Gets the file name with the given fileId from the file server system.
     * </p>
     *
     * @param client the FileSystemClient to connect the file system server.
     * @param fileId the fileId to get the file name.
     *
     * @return the file name with the given fileId from the file server system.
     *
     * @throws FileDoesNotExistException if the file does not exist in persistence.
     * @throws PersistenceException if any error occurs in getting the file name from persistence.
     */
    private String getFileName(FileSystemClient client, String fileId)
        throws FileDoesNotExistException, PersistenceException {
        String requestId;

        try {
            requestId = client.getFileName(fileId);

            ResponseMessage response = (ResponseMessage) client.receiveResponse(requestId, true);

            if (response == null) {
                throw new FileDoesNotExistException(fileId);
            }
            if (response.getException() == null) {
                return (String) response.getResult();
            } else {
                throw new FileDoesNotExistException(fileId);
            }
        } catch (MessageSerializationException e) {
            throw new PersistenceException("Message factory fails to serialize the request.", e);
        } catch (IOException e) {
            throw new PersistenceException("A socket error occurs during sending data.", e);
        }
    }

    /**
     * <p>
     * Creates the FileSystemClient instance and connect it to the file system server.
     * </p>
     *
     * @return the created FileSystemClient instance.
     *
     * @throws PersistenceException if it fails to connect to the file system server.
     */
    private FileSystemClient createClient() throws PersistenceException {
        FileSystemClient client = null;
        try {
            FileSystemPersistence persistence = new FileSystemPersistence();
            FileTransferHandler handle = new DefaultFileTransferHandler();
            client = new FileSystemClient(ipAddress, (int) port, messageNamespace, handlerId, persistence, handle,
                    handle);
        } catch (Exception e) {
            // ignore since will never happen
        }

        try {
            client.connect();
        } catch (IOException e) {
            throw new PersistenceException("Fails to connect to the persistence.", e);
        }

        return client;
    }
    /**
     * <p>
     * Disconnects from the file system server. After this, calling other methods that attempt to connect to the server
     * will result in an IllegalStateException.
     * </p>
     *
     * @param client the FileSystemClient to disconnect.
     *
     * @throws PersistenceException if it fails to disconnect from the persistence.
     */
    private void disconnect(FileSystemClient client) throws PersistenceException {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (IOException e) {
            throw new PersistenceException("Fails to disconnect from the persistence.", e);
        }
    }
}
