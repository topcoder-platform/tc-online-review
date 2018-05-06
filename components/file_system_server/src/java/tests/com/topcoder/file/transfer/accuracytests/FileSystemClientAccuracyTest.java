/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.file.transfer.FileSystemClient;
import com.topcoder.file.transfer.FileSystemHandler;
import com.topcoder.file.transfer.FileTransferHandler;
import com.topcoder.file.transfer.FileUploadCheckStatus;
import com.topcoder.file.transfer.accuracytests.MockFileTransferHandler;
import com.topcoder.file.transfer.message.ResponseMessage;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for FileSystemClient class.
 * </p>
 * 
 * @author arylio, mayday
 * @version 1.1
 */
public class FileSystemClientAccuracyTest extends TestCase {
    /**
     * <p>
     * The server address.
     * </p>
     */
    private final static String ADDRESS = "127.0.0.1";

    /**
     * <p>
     * The server port.
     * </p>
     */
    private final static int PORT = 22233;

    /**
     * Represents the max request number.
     */
    private static final int MAX_REQUUEST = 5;

    /**
     * <p>
     * The max connection number.
     * </p>
     */
    private final static int MAX_CONNECTION = 10;

    /**
     * <p>
     * The handler Id used for test.
     * </p>
     */
    private final String handlerId = KeepAliveHandler.KEEP_ALIVE_ID;

    /**
     * <p>
     * Represents the files file for registry.
     * </p>
     */
    private final String FILES_FILE = "test_files/accuracy/files.xml";

    /**
     * <p>
     * Represents the groups file for registry.
     * </p>
     */
    private final String GROUPS_FILE = "test_files/accuracy/groups.xml";

    /**
     * <p>
     * Represents the FilePersistence instance used in tests.
     * </p>
     */
    private FilePersistence persistence;

    /**
     * <p>
     * Represents the upload FileTransferErrorHandler instance used in tests.
     * </p>
     */
    private FileTransferHandler uploadHandler = new MockFileTransferHandler();

    /**
     * <p>
     * Represents the retrieve FileTransferErrorHandler instance used in tests.
     * </p>
     */
    private FileTransferHandler retrieveHandler = new MockFileTransferHandler();

    /**
     * <p>
     * An file system server.
     * </p>
     */
    private IPServer server = null;

    /**
     * <p>
     * Represents an instance of FileSystemClient.
     * </p>
     */
    private FileSystemClient client;

    /**
     * <p>
     * Represents an instance of FileSystemXmlRegistry.
     * </p>
     */
    private FileSystemXmlRegistry registry;

    /**
     * <p>
     * Represents an instance of FilePersistence implementation.
     * </p>
     */
    private ObjectValidator validator;

    /**
     * <p>
     * Represents an instance of file searcher.
     * </p>
     */
    private RegexFileSearcher fileSearcher;

    /**
     * <p>
     * Represents an instance of group searcher.
     * </p>
     */
    private RegexGroupSearcher groupSearcher;

    /**
     * <p>
     * Represents an instance of SearchManager.
     * </p>
     */
    private SearchManager manager;

    /**
     * <p>
     * Create an server to listen.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    private void createServer() throws Exception {
        fileSearcher = new RegexFileSearcher(registry);
        groupSearcher = new RegexGroupSearcher(registry);
        Map fileSearchers = new Hashtable();
        Map groupSearchers = new Hashtable();
        fileSearchers.put("file", fileSearcher);
        groupSearchers.put("group", groupSearcher);
        manager = new SearchManager(fileSearchers, groupSearchers);
        FileSystemHandler handler = new FileSystemHandler(MAX_REQUUEST,
                registry, persistence, AccuracyTestHelper.SERVER_PATH,
                validator, manager);

        // create a server
        server = new IPServer(ADDRESS, PORT, MAX_CONNECTION, 0,
                AccuracyTestHelper.MESSAGE_FACTORY_NAMESPACE);
        server.addHandler(handlerId, handler);
        server.start();
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
        AccuracyTestHelper.addConfigFiles();

        IDGenerator idGenerator = IDGeneratorFactory
                .getIDGenerator(AccuracyTestHelper.IDGENERATOR_NAMESPACE);
        registry = new FileSystemXmlRegistry(FILES_FILE, GROUPS_FILE,
                idGenerator);
        persistence = new FileSystemPersistence();
        validator = new UploadRequestValidator(
                new FreeDiskSpaceNativeChecker(
                        AccuracyTestHelper.SERVER_PATH));

        createServer();

        client = new FileSystemClient(ADDRESS, PORT,
                AccuracyTestHelper.MESSAGE_FACTORY_NAMESPACE, handlerId,
                persistence, uploadHandler, retrieveHandler);
        client.connect();
    }

    /**
     * <p>
     * Tear down for each test.
     * </p>
     */
    protected void tearDown() throws Exception {
        persistence = null;
        if (client.isConnected()) {
            try {
                client.disconnect();
            } catch (IOException e) {
            }
        }
        client = null;
        if (server.isStarted()) {
            server.stop();
        }

        // Loop until server is really stopped
        while (server.isStarted()) {
            synchronized (server) {
                try {
                    server.wait(100);
                } catch (InterruptedException e) {
                    // Clear the interrupted status
                    Thread.interrupted();
                }
            }
        }
        server = null;
        AccuracyTestHelper.removeConfigFiles();
        AccuracyTestHelper.copy(AccuracyTestHelper.CLIENT_PATH + "/9999999",
                AccuracyTestHelper.SERVER_PATH + "/9999999");
        registry.renameFile("9999999", "9999999.dat");
        registry.renameFile("9999998", "9999998.dat");
        if (registry.getGroupNames().contains("testGroup")) {
            registry.removeGroup("testGroup");
        }
        registry.removeFileFromGroup("group1", "9999998");
        new File(AccuracyTestHelper.CLIENT_PATH + "/9999998.dat").delete();
        super.tearDown();
    }

    /**
     * <p>
     * Test FileSystemClient(String address, int port, String namespace, String
     * handlerId, FilePersistence persistence, FileTransferHandler
     * uploadHandler, FileTransferHandler retrieveHandler)
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create instance", client);
        assertEquals("Failed to create instance", persistence, client
                .getPersistence());
        assertEquals("Failed to create instance", uploadHandler, client
                .getUploadHandler());
        assertEquals("Failed to create instance", retrieveHandler, client
                .getRetrieveHandler());
    }

    /**
     * <p>
     * Test FileSystemClient(String address, int port, String namespace, String
     * handlerId, FilePersistence persistence, FileTransferHandler
     * uploadHandler, FileTransferHandler retrieveHandler, int transferByteSize)
     * </p>
     * @throws Exception
     *             Exception to JUnit
     */
    public void testCtor1() throws Exception {
        FileSystemClient aclient = new FileSystemClient(ADDRESS, PORT, AccuracyTestHelper.MESSAGE_FACTORY_NAMESPACE,
            handlerId, persistence, uploadHandler, retrieveHandler, 2048);
        assertNotNull("Failed to create instance", aclient);
        assertEquals("Failed to create instance", persistence, client.getPersistence());
        assertEquals("Failed to create instance", uploadHandler, client.getUploadHandler());
        assertEquals("Failed to create instance", retrieveHandler, client.getRetrieveHandler());
    }

    /**
     * Test uploadFile(String fileLocation, String fileName), with file is small.
     * @throws Exception Exception to JUnit
     */
    public void testUploadFile_Small() throws Exception {
        innerUploadFile("01.txt", null, true);
    }

    /**
     * Test uploadFile(String fileLocation, String fileName), with the file is
     * some big.
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testUploadFile_Large() throws Exception {
        innerUploadFile("02.dat", null, true);
    }

    /**
     * <p>
     * Test uploadFile(String fileId, String fileLocation, String fileName),
     * with the fileId already existd.
     * </p>
     * 
     * @throws Exception
     *             if any exception occur.
     */
    public void testUploadFile2() throws Exception {
        String fileId = "9999999";
        assertNotNull("this fileId should in registry", registry
                .getFile(fileId));
        innerUploadFile("01.txt", fileId, false);
    }

    /**
     * <p>
     * Do upload file operation and verify it.
     * </p>
     * 
     * @param fileName
     *            the file to upload
     * @param fileId
     *            the file id of the file
     * @param removed
     *            remove the file or not
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    private void innerUploadFile(String fileName, String fileId, boolean removed)
            throws Exception {
        String requestId;
        if (fileId == null) {
            requestId = client.uploadFile(AccuracyTestHelper.CLIENT_PATH,
                    fileName);
        } else {
            requestId = client.uploadFile(fileId,
                    AccuracyTestHelper.CLIENT_PATH, fileName);
        }
        ResponseMessage response = null;
        do {
            response = (ResponseMessage) client
                    .receiveResponse(requestId, true);
        } while (response == null);

        assertNull("failed to upload file", response.getException());
        assertTrue("failed to upload file", AccuracyTestHelper.diffFile(
                AccuracyTestHelper.CLIENT_PATH + "/" + fileName,
                AccuracyTestHelper.SERVER_PATH + "/" + response.getResult()));
        if (removed) {
            requestId = client.removeFile((String) response.getResult());
            response = (ResponseMessage) client
                    .receiveResponse(requestId, true);
            assertNull("fail to remove file", response.getException());
        }
    }

    /**
     * Test uploadFile(InputStream dataStream), with file is small.
     * @throws Exception Exception to JUnit
     */
    public void testUploadFile_Stream1() throws Exception {
        String requestId = "";

        String fileName = "01.txt";
        InputStream dataStream = new FileInputStream(new File(AccuracyTestHelper.CLIENT_PATH, fileName));
        requestId = client.uploadFile(dataStream);
        ResponseMessage response = null;
        do {
            response = (ResponseMessage) client.receiveResponse(requestId, true);
        } while (response == null);

        assertNull("failed to upload file", response.getException());
        assertTrue("failed to upload file", AccuracyTestHelper.diffFile(
            AccuracyTestHelper.CLIENT_PATH + "/" + fileName, AccuracyTestHelper.SERVER_PATH + "/"
                + response.getResult()));

        requestId = client.removeFile((String) response.getResult());
        response = (ResponseMessage) client.receiveResponse(requestId, true);
        assertNull("fail to remove file", response.getException());
    }

    /**
     * Test uploadFile(InputStream dataStream, String fileName), with file is small.
     * @throws Exception Exception to JUnit
     */
    public void testUploadFile_Stream2() throws Exception {
        String requestId = "";

        String fileName = "01.txt";
        InputStream dataStream = new FileInputStream(new File(AccuracyTestHelper.CLIENT_PATH, fileName));
        requestId = client.uploadFile(dataStream, fileName);
        ResponseMessage response = null;
        do {
            response = (ResponseMessage) client.receiveResponse(requestId, true);
        } while (response == null);

        assertNull("failed to upload file", response.getException());
        assertTrue("failed to upload file", AccuracyTestHelper.diffFile(
            AccuracyTestHelper.CLIENT_PATH + "/" + fileName, AccuracyTestHelper.SERVER_PATH + "/"
                + response.getResult()));

        requestId = client.removeFile((String) response.getResult());
        response = (ResponseMessage) client.receiveResponse(requestId, true);
        assertNull("fail to remove file", response.getException());
    }

    /**
     * Test uploadFile(String fileId, InputStream dataStream), with file is small.
     * @throws Exception Exception to JUnit
     */
    public void testUploadFile_Stream3() throws Exception {
        String fileId = "9999999";
        assertNotNull("this fileId should in registry", registry.getFile(fileId));
        String requestId = "";

        String fileName = "01.txt";
        InputStream dataStream = new FileInputStream(new File(AccuracyTestHelper.CLIENT_PATH, fileName));
        requestId = client.uploadFile(fileId, dataStream);
        ResponseMessage response = null;
        do {
            response = (ResponseMessage) client.receiveResponse(requestId, true);
        } while (response == null);

        assertNull("failed to upload file", response.getException());
        assertTrue("failed to upload file", AccuracyTestHelper.diffFile(
            AccuracyTestHelper.CLIENT_PATH + "/" + fileName, AccuracyTestHelper.SERVER_PATH + "/"
                + response.getResult()));
    }

    /**
     * Test uploadFile(String fileId, InputStream dataStream, String fileName), with file is small.
     * @throws Exception Exception to JUnit
     */
    public void testUploadFile_Stream4() throws Exception {
        String fileId = "9999999";
        assertNotNull("this fileId should in registry", registry.getFile(fileId));
        String requestId = "";

        String fileName = "01.txt";
        InputStream dataStream = new FileInputStream(new File(AccuracyTestHelper.CLIENT_PATH, fileName));
        requestId = client.uploadFile(fileId, dataStream, fileName);
        ResponseMessage response = null;
        do {
            response = (ResponseMessage) client.receiveResponse(requestId, true);
        } while (response == null);

        assertNull("failed to upload file", response.getException());
        assertTrue("failed to upload file", AccuracyTestHelper.diffFile(
            AccuracyTestHelper.CLIENT_PATH + "/" + fileName, AccuracyTestHelper.SERVER_PATH + "/"
                + response.getResult()));
    }

    /**
     * <p>
     * Test getFileUploadCheckStatus(String requestId, boolean blocking), it should return an valid status.
     * </p>
     * @throws Exception Exception to JUnit
     */
    public void testGetFileUploadCheckStatus() throws Exception {
        String requestId = client.uploadFile(AccuracyTestHelper.CLIENT_PATH, "02.dat");
        Thread.sleep(10);
        FileUploadCheckStatus status = client.getFileUploadCheckStatus(
                requestId, true);
        assertNotNull("Failed to get the status", status);
        
        ResponseMessage response = null;
        do {
            response = (ResponseMessage) client
                    .receiveResponse(requestId, true);
        } while (response == null);
        requestId = client.removeFile((String) response.getResult());
        response = (ResponseMessage) client
                .receiveResponse(requestId, true);
        assertNull("fail to remove file", response.getException());
    }

    /**
     * <p>
     * Test checkUploadFile(String fileLocation, String fileName), check upload
     * operation, should always return true, as most of time, there is enough
     * space.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testCheckUploadFile() throws Exception {
        String fileName = "01.txt";
        String requestId = client.checkUploadFile(
                AccuracyTestHelper.CLIENT_PATH, fileName);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("fail to check upload file status", response.getException());
        assertTrue("True is expected, in current test enviroment",
                ((Boolean) response.getResult()).booleanValue());
    }

    /**
     * <p>
     * Test retrieveFile(String fileId, String fileLocation), the file should be
     * returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRetrieveFile() throws Exception {
        String requestId = client.retrieveFile("9999998",
                AccuracyTestHelper.CLIENT_PATH);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        while (response == null) {
            response = (ResponseMessage) client
                    .receiveResponse(requestId, true);
        }
        assertNull("retrieve file not success", response.getException());
        assertTrue("file size should equals", AccuracyTestHelper.diffFile(
                AccuracyTestHelper.SERVER_PATH + "/9999998",
                AccuracyTestHelper.CLIENT_PATH + "/9999998.dat"));
    }

    /**
     * <p>
     * Test retrieveFile(String fileId, OutputStream dataStream), the file should be
     * returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRetrieveFile1() throws Exception {
        OutputStream os = new FileOutputStream(new File(AccuracyTestHelper.CLIENT_PATH, "9999998.dat"));
        String requestId = client.retrieveFile("9999998", os);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(requestId, true);
        while (response == null) {
            response = (ResponseMessage) client.receiveResponse(requestId, true);
        }
        assertNull("retrieve file not success", response.getException());
        assertTrue("file size should equals", AccuracyTestHelper.diffFile(AccuracyTestHelper.SERVER_PATH + "/9999998",
            AccuracyTestHelper.CLIENT_PATH + "/9999998.dat"));
    }

    /**
     * <p>
     * Test getFileName(String fileId), the file name of the file should be returned.
     * </p>
     * @throws Exception Exception to JUnit
     */
    public void testGetFileName() throws Exception {
        String fileId = "9999998";
        String requestId = client.getFileName(fileId);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        if (response.getException() == null) {
            assertEquals("the file name is not expected", fileId + ".dat",
                    response.getResult());
        } else {
            fail("Exception occured when get file name");
        }
    }

    /**
     * <p>
     * Test getFileSize(String fileId), the file size of the file should be
     * returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetFileSize() throws Exception {
        String fileId = "9999998";
        String requestId = client.getFileSize(fileId);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        if (response.getException() == null) {
            assertEquals("The returned file size is not expected", new File(
                    AccuracyTestHelper.SERVER_PATH, "/9999998").length(),
                    ((Long) response.getResult()).longValue());
        } else {
            fail("Exception occured when get file size");
        }
    }

    /**
     * <p>
     * Test rrenameFile(String fileId, String fileName), the filename should be
     * updated to new value.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRenameFile() throws Exception {
        String fileId = "9999998";
        String requestId = client.getFileName(fileId);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        String oldFileName = (String) response.getResult();
        String newFileName = "xxxxxxxxx";
        requestId = client.renameFile(fileId, newFileName);
        response = (ResponseMessage) client.receiveResponse(requestId, true);
        assertNull("Exception occured when rename file", response
                .getException());

        requestId = client.getFileName(fileId);
        response = (ResponseMessage) client.receiveResponse(requestId, true);
        assertNull("Exception occured when rename file", response
                .getException());
        assertEquals("file name should be the new one", newFileName,
                (String) response.getResult());
    }

    /**
     * <p>
     * Test removeFile(String fileId), first upload a file, then remove it.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRemoveFile() throws Exception {
        innerUploadFile("01.txt", null, true);
        assertFalse("The file should be removed", registry.getFiles().values()
                .contains("01.txt"));
    }

    /**
     * <p>
     * Test createGroup(String groupName, List fileIds), a group holding the
     * files should be created.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testCreateGroup() throws Exception {
        String groupName = "testGroup";
        List groups = new ArrayList();
        groups.add("9999999");
        String requestId = client.createGroup(groupName, groups);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);

        assertNull("Failed to create group", response.getException());
        List returned = (List) registry.getGroupFiles(groupName);
        assertNotNull("Failed to create group", returned);
        assertEquals("The group is not create correctly", 1, returned.size());
        assertTrue("The group is not create correctly", returned
                .contains("9999999"));
    }

    /**
     * <p>
     * Test updateGroup(String groupName, List fileIds), the group list should
     * be updated.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testUpdateGroup() throws Exception {
        String groupName = "testGroup";
        List groups = new ArrayList();
        String requestId = client.createGroup(groupName, groups);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("Failed to update group", response.getException());
        List returned = (List) registry.getGroupFiles(groupName);
        assertNotNull("Failed to create group", returned);
        assertEquals("The group is not update correctly", 0, returned.size());

        groups.add("9999999");
        requestId = client.updateGroup(groupName, groups);
        response = (ResponseMessage) client.receiveResponse(requestId, true);
        assertNull("Failed to update group", response.getException());

        returned = (List) registry.getGroupFiles(groupName);
        assertNotNull("Failed to create group", returned);
        assertEquals("The group is not create correctly", 1, returned.size());
        assertTrue("The group is not create correctly", returned
                .contains("9999999"));
    }

    /**
     * <p>
     * Test retrieveGroup(String groupName), the group list should be updated.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRetrieveGroup() throws Exception {
        String groupName = "group1";
        String requestId = client.retrieveGroup(groupName);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("Failed to retrieve group correctly", response
                .getException());
        List fileIds = (List) response.getResult();
        List expected = registry.getGroupFiles(groupName);
        assertEquals("return size not correct", expected.size(), fileIds.size());
        for (Iterator itor = fileIds.iterator(); itor.hasNext();) {
            assertTrue("Failed to retrieve group correctly", expected
                    .contains(itor.next()));
        }
    }

    /**
     * <p>
     * Test removeGroup(String groupName, boolean removeFiles), the group should
     * be removed.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRemoveGroup() throws Exception {
        String groupName = "testGroup";
        List groups = new ArrayList();
        String requestId = client.createGroup(groupName, groups);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);

        assertNull("Failed to create group", response.getException());
        List returned = (List) registry.getGroupFiles(groupName);
        assertNotNull("Failed to create group", returned);
        assertEquals("The group is not create correctly", 0, returned.size());

        requestId = client.removeGroup(groupName, false);
        response = (ResponseMessage) client.receiveResponse(requestId, true);
        assertNull("Failed to remove group", response.getException());

        assertFalse("Failed to remove group", registry.getGroupNames()
                .contains(groupName));
    }

    /**
     * <p>
     * Test addFileToGroup(String groupName, String fileId), a file should be
     * added to group.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testAddFileToGroup() throws Exception {
        String groupName = "group1";

        String requestId = client.addFileToGroup(groupName, "9999998");
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("Failed to add file to group", response.getException());

        List files = registry.getGroupFiles(groupName);
        assertTrue("Failed to add file to group", files.contains("9999998"));

        registry.removeFileFromGroup(groupName, "9999998");
    }

    /**
     * <p>
     * Test removeFileFromGroup(String groupName, String fileId), a file should
     * be removed from group.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRemoveFileFromGroup() throws Exception {
        String groupName = "group1";
        registry.addFileToGroup(groupName, "9999998");

        String requestId = client.removeFileFromGroup(groupName, "9999998");
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("Failed to remove file from group", response.getException());

        List files = registry.getGroupFiles(groupName);
        assertFalse("Failed to remove file from group", files
                .contains("9999998"));

        registry.removeFileFromGroup(groupName, "9999998");
    }

    /**
     * <p>
     * Test searchFiles(String fileSearcherName, String criteria), files matches
     * the criteria should be returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testSearchFiles1() throws Exception {
        String requestId = client.searchFiles("file", "9999998.dat");
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("Failed to remove file from group", response.getException());

        List files = (List) response.getResult();
        assertEquals("Failed to search file", 1, files.size());
        assertTrue("Failed to search file", files.contains("9999998"));
    }

    /**
     * <p>
     * Test searchFiles(String fileSearcherName, String criteria), files matches
     * the criteria should be returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testSearchFiles2() throws Exception {
        String requestId = client.searchFiles("file", ".*");
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("Failed to remove file from group", response.getException());

        List files = (List) response.getResult();
        assertTrue("Failed to search file", files.contains("9999998"));
        assertTrue("Failed to search file", files.contains("9999999"));
    }

    /**
     * <p>
     * Test searchGroups(String groupSearcherName, String criteria), groups
     * matches the criteria should be returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testSearchGroup1() throws Exception {
        String requestId = client.searchGroups("group", ".*");
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("Failed to remove file from group", response.getException());

        List groups = (List) response.getResult();
        assertEquals("Failed to search file", 1, groups.size());
        assertTrue("Failed to search file", groups.contains("group1"));
    }

    /**
     * <p>
     * Test searchGroups(String groupSearcherName, String criteria), groups
     * matches the criteria should be returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testSearchGroup2() throws Exception {
        String requestId = client.searchGroups("group", "no");
        ResponseMessage response = (ResponseMessage) client.receiveResponse(
                requestId, true);
        assertNull("Failed to remove file from group", response.getException());

        List groups = (List) response.getResult();
        assertEquals("Failed to search file", 0, groups.size());
    }
}