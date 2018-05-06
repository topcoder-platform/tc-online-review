/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.topcoder.file.transfer.message.ResponseMessage;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.FileIdGroupSearcher;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNonNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for the added and modified methods of <code>SimpleLockingFileSystemPersistence</code> class in
 * version 1.1.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class FileSystemClient2AccTest extends TestCase {

    /**
     * Represents the fileName that will be uploaded.
     */
    private static final String UPLOAD_FILE_NAME = "nant-0.85-rc3-bin.zip";

    /**
     * Represents the handler Id used for test.
     */
    private final String handlerId = "HanderId";

    /**
     * Represents the IPServer instance used in tests.
     */
    private IPServer ipServer;

    /**
     * Represents the FileSystemClient used in test.
     */
    private FileSystemClient fileSystemClient;

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     *
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        ConfigHelper.loadNamespaces();
        initServer();
        ConfigHelper.startServer(ipServer);
        fileSystemClient = createFileSystemClient();
        fileSystemClient.connect();
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     *
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        fileSystemClient.disconnect();
        ConfigHelper.stopServer(ipServer);
        ConfigHelper.releaseNamespaces();
    }

    /**
     * Create the FileSystemClient instance used for testing.
     *
     * @return the instance.
     * @throws Exception
     *             if any exception occur.
     */
    private FileSystemClient createFileSystemClient() throws Exception {
        ConfigHelper configHelper = ConfigHelper.getInstance();
        // create the file persistence
        FileSystemPersistence persistence = new FileSystemPersistence();

        // create the error handlers
        FileTransferHandler uploadErrorHandler = new FileTransferHandler() {
            public void handleError(String fileId, String fileLocation, String fileName, String finalRequestId,
                    Exception exception) {
            }

            public void handleTransferProgress(String fileId, String fileLocation, String fileName,
                    String finalRequestId, long transferBytesSize) {
            }
        };
        FileTransferHandler retrieveErrorHandler = new FileTransferHandler() {
            public void handleError(String fileId, String fileLocation, String fileName, String finalRequestId,
                    Exception exception) {
            }

            public void handleTransferProgress(String fileId, String fileLocation, String fileName,
                    String finalRequestId, long transferBytesSize) {
            }
        };

        // create the client
        return new FileSystemClient(configHelper.getAddress(), configHelper.getPort(),
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence, uploadErrorHandler,
                retrieveErrorHandler);
    }

    /**
     * Inits the IPServer with FileSystemHandler added.
     *
     * @throws Exception
     *             if any exception occur.
     */
    private void initServer() throws Exception {
        ConfigHelper configHelper = ConfigHelper.getInstance();
        // create the IPServer
        ipServer = new IPServer(configHelper.getAddress(), configHelper.getPort(), configHelper.getMaxConnections(),
                0, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // create a file registry
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator(ConfigHelper.IDGENERATOR_NAMESPACE);
        FileSystemRegistry registry = new FileSystemXmlRegistry(ConfigHelper.SERVER_FILES_FILE,
                ConfigHelper.SERVER_GROUPS_FILE, idGenerator);
        // create the file persistence
        FileSystemPersistence serverPersistence = new FileSystemPersistence();
        // create the upload request validator
        ObjectValidator validator = new UploadRequestValidator(new FreeDiskSpaceNonNativeChecker(
                ConfigHelper.SERVER_FILE_LOCATION));
        // create the search manager
        SearchManager searchManager = new SearchManager();
        searchManager.addFileSearcher("regex", new RegexFileSearcher(registry));
        searchManager.addGroupSearcher("regex", new RegexGroupSearcher(registry));
        searchManager.addGroupSearcher("fileId", new FileIdGroupSearcher(registry));
        // create the handler
        FileSystemHandler fileSystemHandler = new FileSystemHandler(configHelper.getMaxRequests(), registry,
                serverPersistence, ConfigHelper.SERVER_FILE_LOCATION, validator, searchManager);
        ipServer.addHandler(handlerId, fileSystemHandler);
    }

    /**
     * <p>
     * Accuracy test for the method <code>uploadFile(InputStream dataStream)</code>, expects the file is uploaded
     * properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile1Accuracy() throws Exception {
        InputStream dataStream = new FileInputStream(new File(ConfigHelper.CLIENT_FILE_LOCATION + "/"
                + UPLOAD_FILE_NAME));

        // upload file
        String requestId = fileSystemClient.uploadFile(dataStream);

        // check the status
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        assertEquals(FileUploadCheckStatus.UPLOAD_ACCEPTED, status);

        while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
            Thread.sleep(500);
        }

        // receive response
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNotNull("Expects the response is not null.", response);
        assertNull("Expects no exception occurs.", response.getException());

        String fileId = (String) response.getResult();
        // use the file with the resulted file name and remove file
        requestId = fileSystemClient.removeFile(fileId);
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("Expects no exception occurs.", response.getException());
    }

    /**
     * <p>
     * Accuracy test for the method <code>uploadFile(InputStream dataStream, String fileName)</code>, expects the
     * file is uploaded properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile2Accuracy() throws Exception {
        InputStream dataStream = new FileInputStream(new File(ConfigHelper.CLIENT_FILE_LOCATION + "/"
                + UPLOAD_FILE_NAME));

        // upload file
        String requestId = fileSystemClient.uploadFile(dataStream, UPLOAD_FILE_NAME);

        // check the status
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        assertEquals(FileUploadCheckStatus.UPLOAD_ACCEPTED, status);

        while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
            Thread.sleep(500);
        }

        // receive response
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNotNull("Expects the response is not null.", response);
        assertNull("Expects no exception occurs.", response.getException());

        String fileId = (String) response.getResult();
        // use the file with the resulted file name and remove file
        requestId = fileSystemClient.removeFile(fileId);
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
    }

    /**
     * <p>
     * Accuracy test for the method <code>uploadFile(String fileId, InputStream dataStream)</code>, expects the
     * file is uploaded properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile3Accuracy() throws Exception {
        InputStream dataStream = new FileInputStream(new File(ConfigHelper.CLIENT_FILE_LOCATION + "/"
                + UPLOAD_FILE_NAME));

        // upload file
        String requestId = fileSystemClient.uploadFile("fileId", dataStream);

        // check the status
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        assertEquals(FileUploadCheckStatus.UPLOAD_ACCEPTED, status);

        while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
            Thread.sleep(500);
        }

        // receive response
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNotNull("Expects the response is not null.", response);
        assertNull("Expects no exception occurs.", response.getException());

        String fileId = (String) response.getResult();
        // use the file with the resulted file name and remove file
        requestId = fileSystemClient.removeFile(fileId);
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("Expects no exception occurs.", response.getException());
    }

    /**
     * <p>
     * Accuracy test for the method <code>uploadFile(String fileId, InputStream dataStream, String fileName)</code>,
     * expects the file is uploaded properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile4Accuracy() throws Exception {
        InputStream dataStream = new FileInputStream(new File(ConfigHelper.CLIENT_FILE_LOCATION + "/"
                + UPLOAD_FILE_NAME));

        // upload file
        String requestId = fileSystemClient.uploadFile("fileId", dataStream, UPLOAD_FILE_NAME);

        // check the status
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        assertEquals(FileUploadCheckStatus.UPLOAD_ACCEPTED, status);

        while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
            Thread.sleep(500);
        }

        // receive the file
        fileSystemClient.receiveResponse(requestId, true);
    }

    /**
     * <p>
     * Accuracy test for the method <code>retrieveFile(String fileId, OutputStream dataStream)</code>, expects the
     * file is retrieved properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testRetrieveFileAccuracy() throws Exception {
        InputStream dataStream = new FileInputStream(new File(ConfigHelper.CLIENT_FILE_LOCATION + "/"
                + UPLOAD_FILE_NAME));

        // upload file
        String requestId = fileSystemClient.uploadFile(dataStream);

        // check the status
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        assertEquals(FileUploadCheckStatus.UPLOAD_ACCEPTED, status);

        OutputStream outStream = new ByteArrayOutputStream();
        String retrieveId = fileSystemClient.retrieveFile("fileId", outStream);

        while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
            Thread.sleep(500);
        }

        assertNotNull("Expects the file is retrieved properly.", retrieveId);

        // receive response
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNotNull("Expects the response is not null.", response);
        assertNull("Expects no exception occurs.", response.getException());

        String fileId = (String) response.getResult();
        // use the file with the resulted file name and remove file
        requestId = fileSystemClient.removeFile(fileId);
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("Expects no exception occurs.", response.getException());
    }
}
