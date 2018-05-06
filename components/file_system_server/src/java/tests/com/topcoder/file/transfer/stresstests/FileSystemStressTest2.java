/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.stresstests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import com.topcoder.file.transfer.FileSystemClient;
import com.topcoder.file.transfer.FileSystemHandler;
import com.topcoder.file.transfer.FileTransferHandler;
import com.topcoder.file.transfer.FileUploadCheckStatus;
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

/**
 * <p>
 * Stress test for <code>FileSystemClient</code> class.
 * </p>
 * 
 * @author qiucx0161
 * @version 1.0
 */
public class FileSystemStressTest2 extends TestCase {
    /**
     * Represents the IPServer instance used in tests.
     */
    private IPServer ipServer;

    /**
     * Represents the FileSystemClient used in test.
     */
    private FileSystemClient client;

    /**
     * Represents the handler Id used for test.
     */
    private final String handlerId = "HanderId";

    /**
     * <p>
     * The times to execute.
     * </p>
     */
    private int times = 20;

    /**
     * <p>
     * Sets up the environment. This method is called before a test is executed.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigHelper.loadNamespaces();
        initServer();
        ConfigHelper.startServer(ipServer);
        client = createFileSystemClient();
        client.connect();
    }

    /**
     * <p>
     * Tears down the environment. This method is called after a test is executed.
     * </p>
     * 
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        client.disconnect();
        ConfigHelper.stopServer(ipServer);
        ConfigHelper.releaseNamespaces();
    }

    /**
     * Inits the IPServer with FileSystemHandler added.
     * 
     * @throws Exception if any exception occur.
     */
    private void initServer() throws Exception {
        ConfigHelper configHelper = ConfigHelper.getInstance();
        // create the IPServer
        ipServer = new IPServer(configHelper.getAddress(), configHelper.getPort(), configHelper
            .getMaxConnections(), 0, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // create a file registry
        IDGenerator idGenerator = IDGeneratorFactory
            .getIDGenerator(ConfigHelper.IDGENERATOR_NAMESPACE);
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
        FileSystemHandler fileSystemHandler = new FileSystemHandler(configHelper.getMaxRequests(),
            registry, serverPersistence, ConfigHelper.SERVER_FILE_LOCATION, validator,
            searchManager);
        ipServer.addHandler(handlerId, fileSystemHandler);
    }

    /**
     * Create the FileSystemClient instance used for testing.
     * 
     * @return the instance.
     * @throws Exception if any exception occur.
     */
    private FileSystemClient createFileSystemClient() throws Exception {
        ConfigHelper configHelper = ConfigHelper.getInstance();
        // create the file persistence
        FileSystemPersistence persistence = new FileSystemPersistence();

        // create the error handlers
        FileTransferHandler uploadErrorHandler = new FileTransferHandler() {
            public void handleError(String fileId, String fileLocation, String fileName,
                String finalRequestId, Exception exception) {
            }

            public void handleTransferProgress(String fileId, String fileLocation, String fileName,
                String finalRequestId, long transferBytesSize) {
            }
        };
        FileTransferHandler retrieveErrorHandler = new FileTransferHandler() {
            public void handleError(String fileId, String fileLocation, String fileName,
                String finalRequestId, Exception exception) {
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
     * <p>
     * Stress test for the method <code>uploadFile</code>, this method will be used several
     * times.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testUploadFileMultiply() throws Exception {
        InputStream dataStream = new FileInputStream(new File("test_files/stresstests/upload.xml"));

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            // upload file
            client.uploadFile("fileId", dataStream);
        }
        System.out.println("It takes " + (System.currentTimeMillis() - startTime)
            + " ms to excute retrieveFile " + times + " times");

        dataStream.close();
    }

    /**
     * <p>
     * Stress test for the method <code>uploadFile</code>, the big file will be uploaded.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testUploadFileBigFile() throws Exception {
        InputStream dataStream = new FileInputStream(new File("test_files/stresstests/bigfile.xml"));

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times / 50; i++) {
            // upload file
            client.uploadFile("fileId", dataStream);
        }

        System.out.println("It takes " + (System.currentTimeMillis() - startTime)
            + " ms to excute uploadFile " + times / 50 + " times");

        dataStream.close();
    }

    /**
     * <p>
     * Stress test for <code>retrieveFile</code>.
     * </p>
     */
    public void testRetrieveFile() throws Exception {
        InputStream dataStream = new FileInputStream(new File("test_files/stresstests/upload.xml"));

        // upload file
        String requestId = client.uploadFile(dataStream);

        // check the status
        FileUploadCheckStatus status = client.getFileUploadCheckStatus(requestId, true);
        assertEquals("upload status is incorrect.", FileUploadCheckStatus.UPLOAD_ACCEPTED, status);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            client.retrieveFile("fileId", new ByteArrayOutputStream());
        }
        System.out.println("It takes " + (System.currentTimeMillis() - startTime)
            + " ms to excute retrieveFile " + times + " times");

        dataStream.close();
    }

    /**
     * <p>
     * Stress test for the method <code>stopFileTransfer(String requestId)</code>..
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testStopFileTransfer() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times / 20; i++) {

            InputStream dataStream = new FileInputStream(new File(
                "test_files/stresstests/upload.xml"));

            // upload file
            String requestId = client.uploadFile("fileId", dataStream);

            // stop file transfer
            client.stopFileTransfer(requestId);

            dataStream.close();
        }

        System.out.println("It takes " + (System.currentTimeMillis() - startTime)
            + " ms to excute stopFileTransfer " + times + " times");
    }
}
