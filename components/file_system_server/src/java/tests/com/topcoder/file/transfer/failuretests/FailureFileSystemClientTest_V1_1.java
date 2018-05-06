/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

import com.topcoder.file.transfer.FileSystemClient;
import com.topcoder.file.transfer.FileTransferHandler;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.processor.ipserver.IPServer;

/**
 * Test <code>FileSystemClient</code> class for failure.
 *
 * @author hfx
 * @version 1.1
 */
public class FailureFileSystemClientTest_V1_1 extends TestCase {
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

    /** The argument used to construct <code>FileSystemClient</code>. */
    private int transferByteSize;

    /**
     * The IPServer used for <code>FileSystemClient</code> instance to connect.
     */
    private IPServer server;

    /** The main <code>FileSystemClient</code> instance used to test. */
    private FileSystemClient client;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception
     *             to Junit.
     */
    protected void setUp() throws Exception {
        FailureHelper.loadConfigs();
        persistence = new FileSystemPersistence();
        uploadHandler = new MockFileTransferHandler();
        retrieveHandler = new MockFileTransferHandler();
        transferByteSize = 1024;
        client = new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, persistence, uploadHandler, retrieveHandler,
                transferByteSize);

        server = new IPServer(ADDRESS, PORT, 50, 0, NAMESPACE);
        server.start();
        client.connect();
    }

    /**
     * Clear configurations.
     *
     * @throws Exception
     *             to Junit.
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
     * FileTransferHandler, int)</code>
     * method for failure. <code>NullPointerException</code> should be thrown if ADDRESS is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForNullPointerException_ADDRESS() throws Exception {
        try {
            new FileSystemClient(null, PORT, NAMESPACE, HANDLERID, persistence, uploadHandler, retrieveHandler,
                    transferByteSize);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>NullPointerException</code> should be thrown if NAMESPACE is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForNullPointerException_NAMESPACE() throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, null, HANDLERID, persistence, uploadHandler, retrieveHandler,
                    transferByteSize);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>NullPointerException</code> should be thrown if HANDLERIDt is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForNullPointerException_HANDLERID() throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, null, persistence, uploadHandler, retrieveHandler,
                    transferByteSize);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>NullPointerException</code> should be thrown if FilePersistence is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForNullPointerException_FilePersistence() throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, null, uploadHandler, retrieveHandler,
                    transferByteSize);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>NullPointerException</code> should be thrown if uploadHandler is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForNullPointerException_UploadHandler() throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, persistence, null, retrieveHandler,
                    transferByteSize);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>NullPointerException</code> should be thrown if retrieveHandler is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForNullPointerException_RetrieveHandler() throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, persistence, uploadHandler, null,
                    transferByteSize);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>IllegalArgumentException</code> should be thrown if the port is not within 0..65535.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForIllegalArgumentException() throws Exception {
        try {
            new FileSystemClient(ADDRESS, 65536, NAMESPACE, HANDLERID, persistence, uploadHandler, retrieveHandler,
                    transferByteSize);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>IllegalArgumentException</code> should be thrown if NAMESPACE is empty.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForIllegalArgumentException_NAMESPACE() throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, "  ", HANDLERID, persistence, uploadHandler, retrieveHandler,
                    transferByteSize);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>IllegalArgumentException</code> should be thrown if HANDLERID is empty.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForIllegalArgumentException_HANDLERID() throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, "  ", persistence, uploadHandler, retrieveHandler,
                    transferByteSize);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemClient(String, int, String, String, FilePersistence, FileTransferHandler,
     * FileTransferHandler, int)</code>
     * method for failure. <code>IllegalArgumentException</code> should be thrown if transferByteSize is empty.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testFileSystemClientForIllegalArgumentException_transferByteSize() throws Exception {
        try {
            new FileSystemClient(ADDRESS, PORT, NAMESPACE, HANDLERID, persistence, uploadHandler, retrieveHandler, 0);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(InputStream)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if dataStream is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFileForNullPointerException_dataStream() throws Exception {
        InputStream dataStream = null;
        try {
            client.uploadFile(dataStream);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(InputStream)</code> method for failure. <code>IllegalStateException</code> should be
     * thrown if the client is not connected.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFileForIllegalStateException() throws Exception {
        client.disconnect();
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        try {
            client.uploadFile(dataStream);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(InputStream, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if dataStream is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile2ForNullPointerException_dataStream() throws Exception {
        InputStream dataStream = null;
        String fileName = "fileupload.xml";
        try {
            client.uploadFile(dataStream, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(InputStream, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if fileName is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile2ForNullPointerException_fileName() throws Exception {
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileName = null;
        try {
            client.uploadFile(dataStream, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(InputStream, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if fileName is empty string.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile2ForIllegalArgumentException_fileName() throws Exception {
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileName = "  ";
        try {
            client.uploadFile(dataStream, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(InputStream, String)</code> method for failure. <code>IllegalStateException</code>
     * should be thrown if the client is not connected.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile2ForIllegalStateException() throws Exception {
        client.disconnect();
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileName = "fileupload.xml";
        try {
            client.uploadFile(dataStream, fileName);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if dataStream is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile3ForNullPointerException_dataStream() throws Exception {
        InputStream dataStream = null;
        String fileId = "111";
        try {
            client.uploadFile(fileId, dataStream);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if fileId is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile3ForNullPointerException_fileId() throws Exception {
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileId = null;
        try {
            client.uploadFile(fileId, dataStream);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if fileId is empty string.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile3ForIllegalArgumentException_fileId() throws Exception {
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileId = "  ";
        try {
            client.uploadFile(fileId, dataStream);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream)</code> method for failure. <code>IllegalStateException</code>
     * should be thrown if the client is not connected.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile3ForIllegalStateException() throws Exception {
        client.disconnect();
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileId = "111";
        try {
            client.uploadFile(fileId, dataStream);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if dataStream is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile4ForNullPointerException_dataStream() throws Exception {
        InputStream dataStream = null;
        String fileId = "111";
        String fileName = "fileupload.xml";
        try {
            client.uploadFile(fileId, dataStream, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if fileId is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile4ForNullPointerException_fileId() throws Exception {
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileId = null;
        String fileName = "fileupload.xml";
        try {
            client.uploadFile(fileId, dataStream, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream, String)</code> method for failure.
     * <code>IllegalArgumentException</code> should be thrown if fileId is empty string.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile4ForIllegalArgumentException_fileId() throws Exception {
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileId = "  ";
        String fileName = "fileupload.xml";
        try {
            client.uploadFile(fileId, dataStream, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if fileName is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile4ForNullPointerException_fileName() throws Exception {
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileId = "111";
        String fileName = null;
        try {
            client.uploadFile(fileId, dataStream, fileName);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream, String)</code> method for failure.
     * <code>IllegalArgumentException</code> should be thrown if fileName is empty string.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile4ForIllegalArgumentException_fileName() throws Exception {
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileId = "111";
        String fileName = " ";
        try {
            client.uploadFile(fileId, dataStream, fileName);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>uploadFile(String, InputStream, String)</code> method for failure.
     * <code>IllegalStateException</code> should be thrown if the client is not connected.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testUploadFile4ForIllegalStateException() throws Exception {
        client.disconnect();
        InputStream dataStream = new ByteArrayInputStream("bytearray".getBytes());
        String fileId = "111";
        String fileName = "fileupload.xml";
        try {
            client.uploadFile(fileId, dataStream, fileName);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, OutputStream)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if the fileId is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testretrieveFileForNullPointerException_fileId() throws Exception {
        OutputStream dataStream = new ByteArrayOutputStream();
        String fileId = null;
        try {
            client.retrieveFile(fileId, dataStream);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, OutputStream)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if the fileId is empty string.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testretrieveFileForIllegalArgumentException_fileId() throws Exception {
        OutputStream dataStream = new ByteArrayOutputStream();
        String fileId = " ";
        try {
            client.retrieveFile(fileId, dataStream);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, OutputStream)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if the dataStream is null.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testretrieveFileForNullPointerException_dataStream() throws Exception {
        OutputStream dataStream = null;
        String fileId = "111";
        try {
            client.retrieveFile(fileId, dataStream);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>retrieveFile(String, OutputStream)</code> method for failure. <code>IllegalStateException</code>
     * should be thrown if the client is not connected.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testretrieveFileForIllegalStateException() throws Exception {
        client.disconnect();
        OutputStream dataStream = new ByteArrayOutputStream();
        String fileId = "111";
        try {
            client.retrieveFile(fileId, dataStream);
            fail("the IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            // Good
        }
    }
}
