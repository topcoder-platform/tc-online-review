/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.FileSystemClient;
import com.topcoder.file.transfer.FileTransferHandler;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;

import com.topcoder.processor.ipserver.IPServer;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;


/**
 * Test <code>FileSystemClient</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureFileSystemClientTest extends TestCase {
    /** The argument used to construct <code>FileSystemClient</code>. */
    private static final String ADDRESS = "127.0.0.1";

    /** The argument used to construct <code>FileSystemClient</code>. */
    private static final int PORT = 9874;

    /** The argument used to construct <code>FileSystemClient</code>. */
    private static final String NAMESPACE = "com.topcoder.processor.ipserver.message.MessageFactory";

    /** The argument used to construct <code>FileSystemClient</code>. */
    private static final String HANDLERID = "4321";

    /** The argument used to construct <code>FileSystemClient</code>. */
    private FilePersistence persistence;

    /** The argument used to construct <code>FileSystemClient</code>. */
    private FileTransferHandler uploadHandler;

    /** The argument used to construct <code>FileSystemClient</code>. */
    private FileTransferHandler retrieveHandler;

    /** The IPServer used for <code>FileSystemClient</code> instance to connect. */
    private IPServer server;

    /** The main <code>FileSystemClient</code> instance used to test. */
    private FileSystemClient client;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        FailureHelper.loadConfigs();
        persistence = new FileSystemPersistence();
        uploadHandler = new MockFileTransferHandler();
        retrieveHandler = new MockFileTransferHandler();
        client = new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, persistence, uploadHandler, retrieveHandler);

        server = new IPServer(ADDRESS, PORT, 50, 0, NAMESPACE);
        server.start();
        client.connect();
    }

    /**
     * Clear configurations.
     *
     * @throws Exception to Junit.
     */
    protected void tearDown() throws Exception {
        if (server.isStarted()) {
            server.stop();
        }

        // Loop until server is really stopped
        while (server.isStarted()) {
            synchronized (server) {
                try {
                    server.wait(100);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
        }

        FailureHelper.clearConfigs();
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>NullPointerException</code> should be thrown if ADDRESS
     * is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForNullPointerException_ADDRESS()
        throws Exception {
        try {
            new FileSystemClient(null, PORT, NAMESPACE, HANDLERID, persistence, uploadHandler, retrieveHandler);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>NullPointerException</code> should be thrown if NAMESPACE
     * is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForNullPointerException_NAMESPACE()
        throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, null, HANDLERID, persistence, uploadHandler, retrieveHandler);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * HANDLERIDt is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForNullPointerException_HANDLERID()
        throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, null, persistence, uploadHandler, retrieveHandler);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * FilePersistence is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForNullPointerException_FilePersistence()
        throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, null, uploadHandler, retrieveHandler);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * uploadHandler is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForNullPointerException_UploadHandler()
        throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, persistence, null, retrieveHandler);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * retrieveHandler is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForNullPointerException_RetrieveHandler()
        throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, persistence, uploadHandler, null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>IllegalArgumentException</code> should be thrown if the
     * port is not within 0..65535.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForIllegalArgumentException()
        throws Exception {
        try {
            new FileSystemClient(ADDRESS, 65536, NAMESPACE, HANDLERID, persistence, uploadHandler, retrieveHandler);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>IllegalArgumentException</code> should be thrown if
     * NAMESPACE is empty.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForIllegalArgumentException_NAMESPACE()
        throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, "  ", HANDLERID, persistence, uploadHandler, retrieveHandler);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler)</code> method for failure. <code>IllegalArgumentException</code> should be thrown if
     * HANDLERID is empty.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemClientForIllegalArgumentException_HANDLERID()
        throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, "  ", persistence, uploadHandler, retrieveHandler);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForNullPointerException_Loc()
        throws Exception {
        try {
            String fileLocation = null;
            String fileName = "empty_input_stream";
            client.uploadFile(fileLocation, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String fileLocation = "test_files/failure";
            String fileName = "empty_input_stream";
            client.uploadFile(fileLocation, fileName);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForNullPointerException_Filename()
        throws Exception {
        try {
            String fileLocation = "test_files/failure";
            String fileName = null;
            client.uploadFile(fileLocation, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForIllegalArgumentException_Loc()
        throws Exception {
        try {
            String fileLocation = "  ";
            String fileName = "empty_input_stream";
            client.uploadFile(fileLocation, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForIllegalArgumentException_Filename()
        throws Exception {
        try {
            String fileLocation = "test_files/failure";
            String fileName = "  ";
            client.uploadFile(fileLocation, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if id is null.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForNullPointerException_Id()
        throws Exception {
        try {
            String id = null;
            String fileLocation = "test_files/failure";
            String fileName = "empty_input_stream";
            client.uploadFile(id, fileLocation, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if fileLocation is null.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForNullPointerException_FileLocation()
        throws Exception {
        try {
            String id = "1234";
            String fileLocation = null;
            String fileName = "empty_input_stream";
            client.uploadFile(id, fileLocation, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if fileName is null.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForNullPointerException_FileName()
        throws Exception {
        try {
            String id = "1234";
            String fileLocation = "test_files/failure";
            String fileName = null;
            client.uploadFile(id, fileLocation, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if id is empty.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForIllegalArgumentException_Id()
        throws Exception {
        try {
            String id = " ";
            String fileLocation = "test_files/failure";
            String fileName = "empty_input_stream";
            client.uploadFile(id, fileLocation, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be fileLocation if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForIllegalArgumentException_FileLocation()
        throws Exception {
        try {
            String id = "1234";
            String fileLocation = " ";
            String fileName = "empty_input_stream";
            client.uploadFile(id, fileLocation, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if fileName is empty.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForIllegalArgumentException_FileName()
        throws Exception {
        try {
            String id = "1234";
            String fileLocation = "test_files/failure";
            String fileName = " ";
            client.uploadFile(id, fileLocation, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, String, String)</code> method for failure. <code>IllegalStateException</code>
     * should be thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testUploadFileForIllegalStateException_2()
        throws Exception {
        try {
            client.disconnect();

            String id = "1234";
            String fileLocation = "test_files/failure";
            String fileName = "empty_input_stream";
            client.uploadFile(id, fileLocation, fileName);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileUploadCheckStatus(String, boolean)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if id is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileUploadCheckStatusForNullPointerException_Id()
        throws Exception {
        try {
            String requestId = null;
            boolean blocking = true;
            client.getFileUploadCheckStatus(requestId, blocking);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileUploadCheckStatus(String, boolean)</code> method for failure.
     * <code>IllegalArgumentException</code> should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileUploadCheckStatusForIllegalArgumentException()
        throws Exception {
        try {
            String requestId = "  ";
            boolean blocking = true;
            client.getFileUploadCheckStatus(requestId, blocking);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>checkUploadFile(String, String)</code> method for failure. <code>NullPointerException</code> should
     * be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testCheckUploadFileForNullPointerException_Loc()
        throws Exception {
        try {
            String fileLocation = null;
            String fileName = "empty_input_stream";
            client.checkUploadFile(fileLocation, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>checkUploadFile(String, String)</code> method for failure. <code>NullPointerException</code> should
     * be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testCheckUploadFileForNullPointerException_Filename()
        throws Exception {
        try {
            String fileLocation = "test_files/failure";
            String fileName = null;
            client.checkUploadFile(fileLocation, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>checkUploadFile(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testCheckUploadFileForIllegalArgumentException_Loc()
        throws Exception {
        try {
            String fileLocation = "  ";
            String fileName = "empty_input_stream";
            client.checkUploadFile(fileLocation, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>checkUploadFile(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testCheckUploadFileForIllegalArgumentException_Filename()
        throws Exception {
        try {
            String fileLocation = "test_files/failure";
            String fileName = "  ";
            client.checkUploadFile(fileLocation, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>checkUploadFile(String, String)</code> method for failure. <code>IllegalStateException</code> should
     * be thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testCheckUploadFileForIllegalStateException_2()
        throws Exception {
        try {
            client.disconnect();

            String fileLocation = "test_files/failure";
            String fileName = "empty_input_stream";
            client.checkUploadFile(fileLocation, fileName);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRetrieveFileForNullPointerException_Loc()
        throws Exception {
        try {
            String fileId = null;
            String fileLocation = "empty_input_stream";
            client.retrieveFile(fileId, fileLocation);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRetrieveFileForNullPointerException_Filename()
        throws Exception {
        try {
            String fileId = "test_files/failure";
            String fileLocation = null;
            client.retrieveFile(fileId, fileLocation);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should
     * be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRetrieveFileForIllegalArgumentException_Loc()
        throws Exception {
        try {
            String fileId = "  ";
            String fileLocation = "empty_input_stream";
            client.retrieveFile(fileId, fileLocation);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should
     * be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRetrieveFileForIllegalArgumentException_Filename()
        throws Exception {
        try {
            String fileId = "test_files/failure";
            String fileLocation = "  ";
            client.retrieveFile(fileId, fileLocation);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testRetrieveFileForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String fileId = "1234";
            String fileLocation = "test_files/failure";
            client.retrieveFile(fileId, fileLocation);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileName(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileNameForNullPointerException()
        throws Exception {
        try {
            client.getFileName(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileName(String)</code> method for failure. <code>IllegalArgumentException</code> should be thrown
     * if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileNameForIllegalArgumentException()
        throws Exception {
        try {
            client.getFileName(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileSize(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileSizeForNullPointerException()
        throws Exception {
        try {
            client.getFileSize(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileSize(String)</code> method for failure. <code>IllegalArgumentException</code> should be thrown
     * if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileSizeForIllegalArgumentException()
        throws Exception {
        try {
            client.getFileSize("  ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileName(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileNameForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();
            client.getFileName("somefile");
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForNullPointerException_Loc()
        throws Exception {
        try {
            String fileId = null;
            String fileName = "empty_input_stream";
            client.renameFile(fileId, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForNullPointerException_Filename()
        throws Exception {
        try {
            String fileId = "test_files/failure";
            String fileName = null;
            client.renameFile(fileId, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForIllegalArgumentException_Loc()
        throws Exception {
        try {
            String fileId = "  ";
            String fileName = "empty_input_stream";
            client.renameFile(fileId, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForIllegalArgumentException_Filename()
        throws Exception {
        try {
            String fileId = "test_files/failure";
            String fileName = "  ";
            client.renameFile(fileId, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String fileId = "12345";
            String fileName = "empty_input_stream";
            client.renameFile(fileId, fileName);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFile(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileForNullPointerException()
        throws Exception {
        try {
            client.removeFile(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFile(String)</code> method for failure. <code>IllegalArgumentException</code> should be thrown
     * if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileForIllegalArgumentException()
        throws Exception {
        try {
            client.removeFile(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFile(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();
            client.removeFile("somefile");
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, List)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupName is null.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForNullPointerException_Name()
        throws Exception {
        try {
            String groupName = null;
            List fileIds = new ArrayList();
            client.createGroup(groupName, fileIds);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, List)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if fileIds is null.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForNullPointerException_Fids()
        throws Exception {
        try {
            String groupName = "groupname";
            List fileIds = null;
            client.createGroup(groupName, fileIds);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, List)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForIllegalArgumentException()
        throws Exception {
        try {
            String groupName = "  ";
            List fileIds = new ArrayList();
            client.createGroup(groupName, fileIds);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String groupName = "groupname";
            List fileIds = new ArrayList();
            client.createGroup(groupName, fileIds);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, List)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupName is null.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForNullPointerException_Name()
        throws Exception {
        try {
            String groupName = null;
            List fileIds = new ArrayList();
            client.updateGroup(groupName, fileIds);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, List)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if fileIds is null.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForNullPointerException_Fids()
        throws Exception {
        try {
            String groupName = "groupname";
            List fileIds = null;
            client.updateGroup(groupName, fileIds);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, List)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForIllegalArgumentException()
        throws Exception {
        try {
            String groupName = "  ";
            List fileIds = new ArrayList();
            client.updateGroup(groupName, fileIds);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String groupName = "groupname";
            List fileIds = new ArrayList();
            client.updateGroup(groupName, fileIds);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveGroup(String)</code> method for failure. <code>NullPointerException</code> should be thrown
     * if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRetrieveGroupForNullPointerException()
        throws Exception {
        try {
            client.retrieveGroup(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveGroup(String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRetrieveGroupForIllegalArgumentException()
        throws Exception {
        try {
            client.retrieveGroup("  ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveGroup(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testRetrieveGroupForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();
            client.retrieveGroup("1234");
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>removeGroup(String, boolean)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupName is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveGroupForNullPointerException()
        throws Exception {
        try {
            String groupName = null;
            boolean removeFiles = false;
            client.removeGroup(groupName, removeFiles);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeGroup(String, boolean)</code> method for failure. <code>IllegalArgumentException</code> should
     * be thrown if groupName is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveGroupForIllegalArgumentException()
        throws Exception {
        try {
            String groupName = "  ";
            boolean removeFiles = false;
            client.removeGroup(groupName, removeFiles);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeGroup(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveGroupForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String groupName = "somegroup";
            boolean removeFiles = false;
            client.removeGroup(groupName, removeFiles);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupName is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForNullPointerException_Name()
        throws Exception {
        try {
            String groupName = null;
            String fileId = "1234";
            client.addFileToGroup(groupName, fileId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if fileId is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForNullPointerException_Fid()
        throws Exception {
        try {
            String groupName = "groupname";
            String fileId = null;
            client.addFileToGroup(groupName, fileId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if groupName is empty.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForIllegalArgumentException_Name()
        throws Exception {
        try {
            String groupName = " ";
            String fileId = "1234";
            client.addFileToGroup(groupName, fileId);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if fileId is empty.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForIllegalArgumentException_Fid()
        throws Exception {
        try {
            String groupName = "groupname";
            String fileId = " ";
            client.addFileToGroup(groupName, fileId);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>IllegalStateException</code> should
     * be thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String groupName = "groupname";
            String fileId = "1234";
            client.addFileToGroup(groupName, fileId);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if groupName is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForNullPointerException_Name()
        throws Exception {
        try {
            String groupName = null;
            String fileId = "1234";
            client.removeFileFromGroup(groupName, fileId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if fileId is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForNullPointerException_Fid()
        throws Exception {
        try {
            String groupName = "groupname";
            String fileId = null;
            client.removeFileFromGroup(groupName, fileId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if groupName is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForIllegalArgumentException_Name()
        throws Exception {
        try {
            String groupName = " ";
            String fileId = "1234";
            client.removeFileFromGroup(groupName, fileId);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if fileId is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForIllegalArgumentException_Fid()
        throws Exception {
        try {
            String groupName = "groupname";
            String fileId = " ";
            client.removeFileFromGroup(groupName, fileId);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>IllegalStateException</code>
     * should be thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String groupName = "groupname";
            String fileId = "1234";
            client.removeFileFromGroup(groupName, fileId);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>searchFiles(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if fileSearcherName is null.
     *
     * @throws Exception to Junit.
     */
    public void testSearchFilesForNullPointerException_Name()
        throws Exception {
        try {
            String fileSearcherName = null;
            String criteria = "1234";
            client.searchFiles(fileSearcherName, criteria);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>searchFiles(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if criteria is null.
     *
     * @throws Exception to Junit.
     */
    public void testSearchFilesForNullPointerException_Fid()
        throws Exception {
        try {
            String fileSearcherName = "groupname";
            String criteria = null;
            client.searchFiles(fileSearcherName, criteria);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>searchFiles(String, String)</code> method for failure. <code>IllegalArgumentException</code> should
     * be thrown if fileSearcherName is empty.
     *
     * @throws Exception to Junit.
     */
    public void testSearchFilesForIllegalArgumentException_Name()
        throws Exception {
        try {
            String fileSearcherName = " ";
            String criteria = "1234";
            client.searchFiles(fileSearcherName, criteria);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>searchFiles(String, String)</code> method for failure. <code>IllegalArgumentException</code> should
     * be thrown if criteria is empty.
     *
     * @throws Exception to Junit.
     */
    public void testSearchFilesForIllegalArgumentException_Fid()
        throws Exception {
        try {
            String fileSearcherName = "groupname";
            String criteria = " ";
            client.searchFiles(fileSearcherName, criteria);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>searchFiles(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testSearchFilesForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String fileSearcherName = "groupname";
            String criteria = "1234";
            client.searchFiles(fileSearcherName, criteria);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>searchGroups(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupSearcherName is null.
     *
     * @throws Exception to Junit.
     */
    public void testSearchGroupsForNullPointerException_Name()
        throws Exception {
        try {
            String groupSearcherName = null;
            String criteria = "1234";
            client.searchGroups(groupSearcherName, criteria);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>searchGroups(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if criteria is null.
     *
     * @throws Exception to Junit.
     */
    public void testSearchGroupsForNullPointerException_Fid()
        throws Exception {
        try {
            String groupSearcherName = "groupname";
            String criteria = null;
            client.searchGroups(groupSearcherName, criteria);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>searchGroups(String, String)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected to the server.
     *
     * @throws Exception to Junit.
     */
    public void testSearchGroupsForIllegalStateException()
        throws Exception {
        try {
            client.disconnect();

            String groupSearcherName = "groupname";
            String criteria = "1234";
            client.searchGroups(groupSearcherName, criteria);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>searchGroups(String, String)</code> method for failure. <code>IllegalArgumentException</code> should
     * be thrown if groupSearcherName is empty.
     *
     * @throws Exception to Junit.
     */
    public void testSearchGroupsForIllegalArgumentException_Name()
        throws Exception {
        try {
            String groupSearcherName = " ";
            String criteria = "1234";
            client.searchGroups(groupSearcherName, criteria);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>searchGroups(String, String)</code> method for failure. <code>IllegalArgumentException</code> should
     * be thrown if criteria is empty.
     *
     * @throws Exception to Junit.
     */
    public void testSearchGroupsForIllegalArgumentException_Fid()
        throws Exception {
        try {
            String groupSearcherName = "groupname";
            String criteria = " ";
            client.searchGroups(groupSearcherName, criteria);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>stopFileTransfer(String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testStopFileTransferForNullPointerException()
        throws Exception {
        try {
            client.stopFileTransfer(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>stopFileTransfer(String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testStopFileTransferForIllegalArgumentException()
        throws Exception {
        try {
            client.stopFileTransfer(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }
}
