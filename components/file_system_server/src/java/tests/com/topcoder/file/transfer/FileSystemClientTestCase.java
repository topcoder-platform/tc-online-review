/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import java.io.IOException;
import java.util.ArrayList;

import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.processor.ipserver.IPClient;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test FileSystemClient for correctness.
 *
 * @author FireIce
 * @version 1.0
 */
public class FileSystemClientTestCase extends TestCase {

    /** The ConfigHelper instance used for testing. */
    private ConfigHelper helper;

    /** The address used for testing. */
    private String address;

    /** The port used for testing. */
    private int port;

    /** The IPServer used for testing. */
    private IPServer server;

    /** The handler Id used for test. */
    private final String handlerId = KeepAliveHandler.KEEP_ALIVE_ID;

    /**
     * Represents the FilePersistence instance used in tests.
     */
    private FilePersistence persistence;

    /**
     * Represents the upload FileTransferErrorHandler instance used in tests.
     */
    private FileTransferHandler uploadErrorHandler = new MockFileTransferHandler();

    /**
     * Represents the retrieve FileTransferErrorHandler instance used in tests.
     */
    private FileTransferHandler retrieveErrorHandler = new MockFileTransferHandler();

    /**
     * Represents the FileSystemClient instance used in tests.
     */
    private FileSystemClient fileSystemClient;

    /**
     * Initialize the fields except client.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void initExceptClient() throws Exception {
        ConfigHelper.loadNamespaces();

        this.helper = ConfigHelper.getInstance();
        this.address = helper.getAddress();
        this.port = helper.getPort();
        this.server = helper.getIPServer();
        ConfigHelper.startServer(this.server);
    }

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     *
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        initExceptClient();
        persistence = new FileSystemPersistence();
        fileSystemClient = new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId,
                persistence, uploadErrorHandler, retrieveErrorHandler);
        fileSystemClient.connect();
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        persistence = null;
        // Make sure the test client is disconnect with server and set to null
        if (this.fileSystemClient.isConnected()) {
            try {
                this.fileSystemClient.disconnect();
            } catch (IOException e) {
                // ignore
            }
        }

        this.fileSystemClient = null;

        // Stop the test server
        ConfigHelper.stopServer(this.server);
        ConfigHelper.releaseNamespaces();
    }

    /**
     * Test Inheritence.
     */
    public void testInheritence() {
        assertNotNull("setup fails", fileSystemClient);
        assertTrue("should extends IPClient class", fileSystemClient instanceof IPClient);
    }

    /**
     * Test Constructor, if any argument except port is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCtorNullPointerException() throws Exception {
        try {
            new FileSystemClient(null, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if address is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemClient(address, port, null, handlerId, persistence, uploadErrorHandler,
                    retrieveErrorHandler);
            fail("if namespace is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, null, persistence,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if handlerId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if persistence is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence,
                    uploadErrorHandler, null);
            fail("if retrieveErrorHandler is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence, null,
                    retrieveErrorHandler);
            fail("if uploadErrorHandler is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test constructor, if the port is not within 0..65535, or the namespace or handleId is an empty string, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCtorIllegalArgumentException() throws Exception {
        try {
            new FileSystemClient(address, -1, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if port is not within 0..65535, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new FileSystemClient(address, 65536, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if port is not within 0..65535, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new FileSystemClient(address, port, "  ", handlerId, persistence, uploadErrorHandler,
                    retrieveErrorHandler);
            fail("if namespace is empty string, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, "  ", persistence,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if handlerId is empty string, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test uploadFile(String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUploadFile2StrNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.uploadFile((String) null, "fileName");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.uploadFile("test_files/", (String) null);
            fail("if fileName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test uploadFile(String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUploadFile2StrIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.uploadFile(" ", "fileName");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileSystemClient.uploadFile("test_files/", " ");
            fail("if fileName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test uploadFile(String, String) method, if not connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpload2StringIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.uploadFile(ConfigHelper.CLIENT_FILE_LOCATION, "nant-0.85-rc3-bin.zip");
            fail("if not connect to the server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test uploadFile(String,String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUploadFile3StrNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.uploadFile(null, "test_files/", "fileName");
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            fileSystemClient.uploadFile("fileId", (String) null, "fileName");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.uploadFile("test_files/", (String) null);
            fail("if fileName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test uploadFile(String,String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUploadFile3StrIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.uploadFile(" ", "test_files/", "fileName");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            fileSystemClient.uploadFile("fileId", " ", "fileName");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileSystemClient.uploadFile("fileId", "test_files/", " ");
            fail("if fileName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test uploadFile(String, String, String) method, if not connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpload3StrArgIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.uploadFile("1001", ConfigHelper.CLIENT_FILE_LOCATION, "nant-0.85-rc3-bin.zip");
            fail("if not connect to the server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test getFileUploadCheckStatus(String, boolean) method, if any object argument is null, throw
     * NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFileUploadCheckStatusNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.getFileUploadCheckStatus(null, false);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test getFileUploadCheckStatus(String, boolean) method, if any string argument is empty, throw
     * NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFileUploadCheckStatusIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.getFileUploadCheckStatus(" ", false);
            fail("if requestId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test checkUploadFile(String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCheckUploadFileNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.checkUploadFile(null, "fileName");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.checkUploadFile("test_files/", null);
            fail("if fileName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test checkUploadFile(String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCheckUploadFileIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.checkUploadFile(" ", "fileName");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileSystemClient.checkUploadFile("test_files/", " ");
            fail("if fileName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test checkUploadFile(String, String) method, if not connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testCheckUploadFileIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.checkUploadFile(ConfigHelper.CLIENT_FILE_LOCATION, "file4.jpg");
            fail("if not connect to server, should throw IllegalStateException.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test retrieveFile(String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRetrieveFileNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.retrieveFile(null, "test_files/");
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.retrieveFile("fileId", (String) null);
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test retrieveFile(String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRetrieveFileIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.retrieveFile(" ", "test_files/");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileSystemClient.retrieveFile("fileId", " ");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test retrieveFile(String, String) method, if client doesn't connect to server, should throw
     * IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testRetrieveFileIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.retrieveFile("1001", ConfigHelper.CLIENT_FILE_LOCATION);
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test getFileName(String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFileNameNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.getFileName(null);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test getFileName(String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFileNameIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.getFileName(" ");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test getFileName(String) method, if client doesn't connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testGetFileNameIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.getFileName("1001");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test getFileSize(String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFileSizeNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.getFileSize(null);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test getFileSize(String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFileSizeIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.getFileSize(" ");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test getFileSize(String) method, if client doesn't connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testGetFileSizeIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.getFileSize("1001");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test renameFile(String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRenameFileNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.renameFile(null, "fileName");
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.renameFile("fileId", null);
            fail("if fileName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test renameFile(String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRenameFileIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.renameFile(" ", "fileName");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileSystemClient.renameFile("fileId", " ");
            fail("if fileName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test renameFile(String, String) method, if client doesn't connect to server, should throw
     * IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testRenameFileIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.renameFile("1001", "newFileName");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test removeFile(String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.removeFile(null);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test removeFile(String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.removeFile(" ");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test removeFile(String) method, if client doesn't connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testRemoveFileIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.removeFile("1001");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test createGroup(String, List) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCreateGroupNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.createGroup(null, new ArrayList());
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            fileSystemClient.createGroup("GroupName", null);
            fail("if fileIds is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test createGroup(String, List) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCreateGroupIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.createGroup(" ", new ArrayList());
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test createGroup(String, List) method, if client doesn't connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testCreateGroupIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.createGroup("group1", new ArrayList());
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test updateGroup(String, List) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpdateGroupNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.updateGroup(null, new ArrayList());
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            fileSystemClient.updateGroup("GroupName", null);
            fail("if fileIds is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test updateGroup(String, List) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpdateGroupIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.updateGroup(" ", new ArrayList());
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test updateGroup(String, List) method, if client doesn't connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testUpdateGroupIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.updateGroup("group1", new ArrayList());
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test retrieveGroup(String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRetrieveGroupNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.retrieveGroup(null);
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test retrieveGroup(String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRetrieveGroupIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.retrieveGroup(" ");
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test retrieveGroup(String) method, if client doesn't connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testRetrieveGroupIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.retrieveGroup("group1");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test removeGroup(String, boolean) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveGroupNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.removeGroup(null, false);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test removeGroup(String, boolean) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveGroupIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.removeGroup(" ", false);
            fail("if requestId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test removeGroup(String) method, if client doesn't connect to server, should throw IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testRemoveGroupIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.removeGroup("group1", false);
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test addFileToGroup(String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileToGroupNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.addFileToGroup(null, "fileId");
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.addFileToGroup("groupName", null);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test addFileToGroup(String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileToGroupIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.addFileToGroup(" ", "fileId");
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileSystemClient.addFileToGroup("groupName", " ");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test addFileToGroup(String, String) method, if client doesn't connect to server, should throw
     * IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testAddFileToGroupIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.addFileToGroup("group1", "1001");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test removeFileFromGroup(String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileFromGroupNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.removeFileFromGroup(null, "fileId");
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.removeFileFromGroup("groupName", null);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test removeFileFromGroup(String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileFromGroupIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.removeFileFromGroup(" ", "fileId");
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileSystemClient.removeFileFromGroup("groupName", " ");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test removeFileFromGroup(String, String) method, if client doesn't connect to server, should throw
     * IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testRemoveFileFromGroupIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.removeFileFromGroup("group1", "1001");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test searchFiles(String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testSearchFilesNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.searchFiles(null, "criteria");
            fail("if fileSearcherName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.searchFiles("fileSearcherName", null);
            fail("if criteria is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test searchFiles(String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testSearchFilesIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.searchFiles(" ", "criteria");
            fail("if fileSearcherName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test searchFiles(String, String) method, if client doesn't connect to server, should throw
     * IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testSearchFilesIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.searchFiles("regex", "criteria");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test searchGroups(String,String) method, if any object argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testSearchGroupsNullPointerException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.searchGroups(null, "criteria");
            fail("if groupSearcherName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }

        try {
            fileSystemClient.searchGroups("groupSearcherName", null);
            fail("if criteria is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test searchGroups(String,String) method, if any string argument is empty, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testSearchGroupsIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.searchGroups(" ", "criteria");
            fail("if groupSearcherName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test searchGroups(String, String) method, if client doesn't connect to server, should throw
     * IllegalStateException.
     *
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testSearchGroupsIllegalStateException() throws Exception {
        assertNotNull("setup fails", fileSystemClient);
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }
        try {
            fileSystemClient.searchGroups("regex", "criteria");
            fail("if client doesn't connect to server, should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test isFileTransferWorkerAlive mehtod, if there is an mapping in the map, corresponding to the requestId,
     * return true, else return false.
     */
    public void testIsFileTranserWorkerAlive() {
        assertNotNull("setup fails", fileSystemClient);
        String requestId = "mockRequestId";
        assertFalse("no mapping should return false", fileSystemClient.isFileTransferWorkerAlive(requestId));
    }

    /**
     * Test stopFileTransfer method, if the arugment is null, should throw NullPointerException.
     */
    public void testStopFileTransferNullPointerException() {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.stopFileTransfer(null);
            fail("if the argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test stopFileTransfer method, if the arugment is empty string, should throw IllegalArgumentException.
     */
    public void testStopFileTransferIllegalArgumentException1() {
        assertNotNull("setup fails", fileSystemClient);
        try {
            fileSystemClient.stopFileTransfer("  ");
            fail("if the argument is empty string, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test stopFileTransfer method, if there is no mapping with the requestId, should throw IllegalArgumentException.
     */
    public void testStopFileTransferIllegalArgumentException2() {
        assertNotNull("setup fails", fileSystemClient);
        String requestId = "mockRequestId";
        try {
            fileSystemClient.stopFileTransfer(requestId);
            fail("if there is no mapping with the requestId, should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test getPersistence() method, return the persistence isntance setted in constructor.
     */
    public void testGetPersistence() {
        assertNotNull("setup fails", fileSystemClient);
        assertSame("returned the incorrect persistence instance", persistence, fileSystemClient.getPersistence());
    }

    /**
     * Test getUploadHandler() method, return the uploadHandler isntance setted in constructor.
     */
    public void testGetUploadHandler() {
        assertNotNull("setup fails", fileSystemClient);
        assertSame("returned the incorrect uploadErrorHandler instance", uploadErrorHandler, fileSystemClient
                .getUploadHandler());
    }

    /**
     * Test getRetrieveHandler() method, return the retrieveHandler isntance setted in constructor.
     */
    public void testGetRetrieveHandler() {
        assertNotNull("setup fails", fileSystemClient);
        assertSame("returned the incorrect retrieveErrorHandler instance", retrieveErrorHandler, fileSystemClient
                .getRetrieveHandler());
    }

    /**
     * Test getHandlerId() method, return the handlerId isntance setted in constructor.
     */
    public void testGetHandlerId() {
        assertNotNull("setup fails", fileSystemClient);
        assertSame("returned the incorrect handlerId instance", handlerId, fileSystemClient.getHandlerId());
    }

    /**
     * Test getGenerator() method, return the generator isntance setted in constructor.
     */
    public void testGetGenerator() {
        assertNotNull("setup fails", fileSystemClient);
        assertNotNull("generator should not be null", fileSystemClient.getGenerator());
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     *
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileSystemClientTestCase.class);
    }
}
