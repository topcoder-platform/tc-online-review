/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;

import junit.framework.TestCase;

/**
 * <p>
 * Failure tests for the added and modified methods of <code>SimpleLockingFileSystemPersistence</code> class in
 * version 1.1, besides two accuracy test for the two constructors.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class FileSystemClient2ExpTest extends TestCase {

    /**
     * Represents the ConfigHelper instance used for testing.
     */
    private ConfigHelper helper;

    /**
     * Represents the address used for testing.
     */
    private String address;

    /**
     * Represents the port used for testing.
     */
    private int port;

    /**
     * Represents the IPServer used for testing.
     */
    private IPServer server;

    /**
     * Represents the handler Id used for test.
     */
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
     * <p>
     * Accuracy test for the constructor FileSystemClient(String address, int port, String namespace, String
     * handlerId, FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler
     * retrieveHandler), expects the instance is created properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1Accuracy() throws Exception {
        assertNotNull("Failed to create the FileSystemClient instance.", fileSystemClient);
        assertEquals("Exception the transferByteSize is default 1024.", new Integer(1024), (Integer) getPrivateField(
                FileSystemClient.class, fileSystemClient, "transferByteSize"));
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * address is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithAddressNull() throws Exception {
        try {
            new FileSystemClient(null, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if address is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * namespace is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithNamespaceNull() throws Exception {
        try {
            new FileSystemClient(address, port, null, handlerId, persistence, uploadErrorHandler,
                    retrieveErrorHandler);
            fail("if namespace is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * handlerId is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithHandlerIdNull() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, null, persistence,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if handlerId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * persistence is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithPersistenceNull() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if persistence is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * uploadHandler is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithUploadHandlerNull() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence, null,
                    retrieveErrorHandler);
            fail("if uploadErrorHandler is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * retrieveHandler is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithRetrieveHandlerNull() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence,
                    uploadErrorHandler, null);
            fail("if retrieveErrorHandler is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * port is not within 0..65535, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithPortInvalid1() throws Exception {
        try {
            new FileSystemClient(address, -1, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if port is not within 0..65535, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * port is not within 0..65535, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithPortInvalid2() throws Exception {
        try {
            new FileSystemClient(address, 65536, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if port is not within 0..65535, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * namespace is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithNamespaceEmpty() throws Exception {
        try {
            new FileSystemClient(address, port, "  ", handlerId, persistence, uploadErrorHandler,
                    retrieveErrorHandler);
            fail("if namespace is empty string, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler) with the
     * handlerId is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor1WithHandlerIdEmpty() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, "  ", persistence,
                    uploadErrorHandler, retrieveErrorHandler);
            fail("if handlerId is empty string, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Accuracy test for the constructor FileSystemClient(String address, int port, String namespace, String
     * handlerId, FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler,
     * int transferByteSize), expects the instance is created properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2Accuracy() throws Exception {
        fileSystemClient = new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId,
                persistence, uploadErrorHandler, retrieveErrorHandler, 2048);
        assertNotNull("Failed to create the FileSystemClient instance.", fileSystemClient);
        assertEquals("Exception the transferByteSize is set properly.", new Integer(2048), (Integer) getPrivateField(
                FileSystemClient.class, fileSystemClient, "transferByteSize"));
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the address is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithAddressNull() throws Exception {
        try {
            new FileSystemClient(null, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence,
                    uploadErrorHandler, retrieveErrorHandler, 2048);
            fail("if address is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the namespace is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithNamespaceNull() throws Exception {
        try {
            new FileSystemClient(address, port, null, handlerId, persistence, uploadErrorHandler,
                    retrieveErrorHandler, 2048);
            fail("if namespace is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the handlerId is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithHandlerIdNull() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, null, persistence,
                    uploadErrorHandler, retrieveErrorHandler, 2048);
            fail("if handlerId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the persistence is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithPersistenceNull() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler, 2048);
            fail("if persistence is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the uploadHandler is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithUploadHandlerNull() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence, null,
                    retrieveErrorHandler, 2048);
            fail("if uploadErrorHandler is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the retrieveHandler is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithRetrieveHandlerNull() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence,
                    uploadErrorHandler, null, 2048);
            fail("if retrieveErrorHandler is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the port is not within 0..65535, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithPortInvalid1() throws Exception {
        try {
            new FileSystemClient(address, -1, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler, 2048);
            fail("if port is not within 0..65535, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the port is not within 0..65535, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithPortInvalid2() throws Exception {
        try {
            new FileSystemClient(address, 65536, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, null,
                    uploadErrorHandler, retrieveErrorHandler, 2048);
            fail("if port is not within 0..65535, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the namespace is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithNamespaceEmpty() throws Exception {
        try {
            new FileSystemClient(address, port, "  ", handlerId, persistence, uploadErrorHandler,
                    retrieveErrorHandler, 2048);
            fail("if namespace is empty string, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the handlerId is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithHandlerIdEmpty() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, "  ", persistence,
                    uploadErrorHandler, retrieveErrorHandler, 2048);
            fail("if handlerId is empty string, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the transferByteSize is not positive, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithTransferByteSizeNotPositive1() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence,
                    uploadErrorHandler, retrieveErrorHandler, -1);
            fail("if transferByteSize is not positive, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the constructor FileSystemClient(String address, int port, String namespace, String handlerId,
     * FilePersistence persistence, FileTransferHandler uploadHandler, FileTransferHandler retrieveHandler, int
     * transferByteSize) with the transferByteSize is not positive, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithTransferByteSizeNotPositive2() throws Exception {
        try {
            new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence,
                    uploadErrorHandler, retrieveErrorHandler, 0);
            fail("if transferByteSize is not positive, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(InputStream dataStream)</code> with the dataStream is null,
     * NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile1WithDataStreamNull() throws Exception {
        try {
            fileSystemClient.uploadFile(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(InputStream dataStream)</code> with the client is not connected
     * to the server, IllegalStateException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile1WithNotConnected() throws Exception {
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }

        try {
            fileSystemClient.uploadFile(new ByteArrayInputStream(new byte[1024]));
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(InputStream dataStream, String fileName)</code> with the
     * dataStream is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile2WithDataStreamNull() throws Exception {
        try {
            fileSystemClient.uploadFile((InputStream) null, "fileName");
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(InputStream dataStream, String fileName)</code> with the
     * fileName is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile2WithFileNameNull() throws Exception {
        try {
            fileSystemClient.uploadFile(new ByteArrayInputStream(new byte[1024]), null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(InputStream dataStream, String fileName)</code> with the
     * fileName is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile2WithFileNameEmpty() throws Exception {
        try {
            fileSystemClient.uploadFile(new ByteArrayInputStream(new byte[1024]), " ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(InputStream dataStream, String fileName)</code> with the client
     * is not connected to the server, IllegalStateException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile2WithNotConnected() throws Exception {
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }

        try {
            fileSystemClient.uploadFile(new ByteArrayInputStream(new byte[1024]), "fileName");
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream)</code> with the fileId is
     * null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile3WithFileIdNull() throws Exception {
        try {
            fileSystemClient.uploadFile(null, new ByteArrayInputStream(new byte[1024]));
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream)</code> with the
     * dataStream is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile3WithDataStreamNull() throws Exception {
        try {
            fileSystemClient.uploadFile("fileId", (InputStream) null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream)</code> with the fileId is
     * empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile3WithFileIdEmpty() throws Exception {
        try {
            fileSystemClient.uploadFile(" ", new ByteArrayInputStream(new byte[1024]));
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream)</code> with the client is
     * not connected to the server, IllegalStateException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile3WithNotConnected() throws Exception {
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }

        try {
            fileSystemClient.uploadFile("fileId", new ByteArrayInputStream(new byte[1024]));
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream, String fileName)</code>
     * with the fileId is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile4WithFileIdNull() throws Exception {
        try {
            fileSystemClient.uploadFile(null, new ByteArrayInputStream(new byte[1024]), "fileName");
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream, String fileName)</code>
     * with the dataStream is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile4WithDataStreamNull() throws Exception {
        try {
            fileSystemClient.uploadFile("fileId", (InputStream) null, "fileName");
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream, String fileName)</code>
     * with the fileName is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile4WithFileNameNull() throws Exception {
        try {
            fileSystemClient.uploadFile("fileId", new ByteArrayInputStream(new byte[1024]), null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream, String fileName)</code>
     * with the fileId is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile4WithFileIdEmpty() throws Exception {
        try {
            fileSystemClient.uploadFile(" ", new ByteArrayInputStream(new byte[1024]), "fileName");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream, String fileName)</code>
     * with the fileName is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile4WithFileNameEmpty() throws Exception {
        try {
            fileSystemClient.uploadFile("fileId", new ByteArrayInputStream(new byte[1024]), " ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>uploadFile(String fileId, InputStream dataStream, String fileName)</code>
     * with the client is not connected to the server, IllegalStateException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFile4WithNotConnected() throws Exception {
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }

        try {
            fileSystemClient.uploadFile("fileId", new ByteArrayInputStream(new byte[1024]), "fileName");
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>retrieveFile(String fileId, OutputStream dataStream)</code> with the fileId
     * is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testRetrieveFileWithFileIdNull() throws Exception {
        try {
            fileSystemClient.retrieveFile(null, new ByteArrayOutputStream());
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>retrieveFile(String fileId, OutputStream dataStream)</code> with the
     * dataStream is null, NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testRetrieveFileWithDataStreamNull() throws Exception {
        try {
            fileSystemClient.retrieveFile("fileId", (OutputStream) null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>retrieveFile(String fileId, OutputStream dataStream)</code> with the fileId
     * is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testRetrieveFileWithFileIdEmpty() throws Exception {
        try {
            fileSystemClient.retrieveFile(" ", new ByteArrayOutputStream());
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>retrieveFile(String fileId, OutputStream dataStream)</code> with the client
     * is not connected to the server, IllegalStateException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testRetrieveFileWithNotConnected() throws Exception {
        if (fileSystemClient.isConnected()) {
            fileSystemClient.disconnect();
        }

        try {
            fileSystemClient.retrieveFile("fileId", new ByteArrayOutputStream());
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>stopFileTransfer(String requestId)</code> with the requestId is null,
     * NullPointerException is expected.
     * </p>
     */
    public void testStopFileTransferWithRequestIdNull() {
        try {
            fileSystemClient.stopFileTransfer(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>stopFileTransfer(String requestId)</code> with the requestId is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testStopFileTransferWithRequestIdEmpty() {
        try {
            fileSystemClient.stopFileTransfer(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>stopFileTransfer(String requestId)</code> with no FileWorker associate with
     * the requestId, IllegalArgumentException is expected.
     * </p>
     */
    public void testStopFileTransferWithNoFileWorker() {
        try {
            fileSystemClient.stopFileTransfer("mockRequestId");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Gets the value of a private field in the given class. The field has the given name. The value is retrieved from
     * the given instance. If the instance is null, the field is a static field. If any error occurs, null is
     * returned.
     * </p>
     *
     * @param type
     *            the class which the private field belongs to
     * @param instance
     *            the instance which the private field belongs to
     * @param name
     *            the name of the private field to be retrieved
     *
     * @return the value of the private field
     */
    static Object getPrivateField(Class type, Object instance, String name) {
        Field field = null;
        Object obj = null;

        try {
            // get the reflection of the field
            field = type.getDeclaredField(name);

            // set the field accessible
            field.setAccessible(true);

            // get the value
            obj = field.get(instance);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // reset the accessibility
                field.setAccessible(false);
            }
        }

        return obj;
    }
}
