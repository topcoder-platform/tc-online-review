/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.topcoder.file.transfer.message.ResponseMessage;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileIdNotFoundException;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.registry.GroupNotFoundException;
import com.topcoder.file.transfer.search.FileIdGroupSearcher;
import com.topcoder.file.transfer.search.FileSearcherNotFoundException;
import com.topcoder.file.transfer.search.GroupSearcherNotFoundException;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNonNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test the comunication between FileSystemClient and FileSystemHandler for correctness. If client
 * send a request message, server will proccess this message and send a response with an exception or the result.
 *
 * @author FireIce
 * @version 1.0
 */
public class ClientServerTestCase extends TestCase {

    /**
     * Represents the max requests value.
     */
    private static final int MAXREQUUEST = 100;

    /** The address used for testing. */
    private String address = "127.0.0.1";

    /** The port used for testing. */
    private int port = 9999;

    /** The IPServer used for testing. */
    private IPServer server;

    /** The handler Id used for test. */
    private final String handlerId = KeepAliveHandler.KEEP_ALIVE_ID;

    /** Represents the maxConnections used for Handler test. */
    private int maxConnections = 100;

    /**
     * Represents the FileSystemRegistry instance used in tests.
     */
    private FileSystemRegistry registry;

    /**
     * Represents the FilePersistence instance used in tests for client.
     */
    private FilePersistence clientPersistence;

    /**
     * Represents the FilePersistence instance used in tests for server.
     */
    private FilePersistence serverPersistence;

    /**
     * Represents the ObjectValidator instance used in tests.
     */
    private ObjectValidator validator;

    /**
     * Represents the SearchManager instance used in tests.
     */
    private SearchManager searchManager;

    /**
     * Represents the FileSystemHandler instance used in tests.
     */
    private FileSystemHandler fileSystemHandler;

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
     * Init IP server. It first creates the IPServer instance, and creates the FileSystemHandler instance, then add
     * this handler to server.
     *
     * @throws Exception
     *             if any exception occur.
     */
    private void initServer() throws Exception {
        // create the IPServer
        server = new IPServer(address, port, maxConnections, 0, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // create a file registry
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator(ConfigHelper.IDGENERATOR_NAMESPACE);
        registry = new FileSystemXmlRegistry(ConfigHelper.SERVER_FILES_FILE, ConfigHelper.SERVER_GROUPS_FILE,
                idGenerator);
        // create the file persistence
        serverPersistence = new FileSystemPersistence();
        // create the upload request validator
        validator = new UploadRequestValidator(new FreeDiskSpaceNonNativeChecker(ConfigHelper.SERVER_FILE_LOCATION));
        // creat the search manager
        searchManager = new SearchManager();
        searchManager.addFileSearcher("regex", new RegexFileSearcher(registry));
        searchManager.addGroupSearcher("regex", new RegexGroupSearcher(registry));
        searchManager.addGroupSearcher("fileId", new FileIdGroupSearcher(registry));
        // create the handler
        fileSystemHandler = new FileSystemHandler(MAXREQUUEST, registry, serverPersistence,
                ConfigHelper.SERVER_FILE_LOCATION, validator, searchManager);
        server.addHandler(handlerId, fileSystemHandler);
    }

    /**
     * Init client. Simply create an FileSystemClient instance.
     *
     * @throws Exception
     *             if any error occur.
     */
    private void initClient() throws Exception {
        // create the file persistence
        clientPersistence = new FileSystemPersistence();
        // create the client
        fileSystemClient = new FileSystemClient(address, port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId,
                clientPersistence, uploadErrorHandler, retrieveErrorHandler);
    }

    /**
     * <p>
     * Sets up the test environment. The test instances should be created here.
     * </p>
     *
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        ConfigHelper.loadNamespaces();
        initServer();
        initClient();
        ConfigHelper.startServer(server);
        fileSystemClient.connect();
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances should be disposed here.
     * </p>
     *
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        fileSystemClient.disconnect();
        fileSystemClient = null;
        ConfigHelper.stopServer(server);
        server = null;
        ConfigHelper.releaseNamespaces();
    }

    /**
     * helper method to test for the correctness of setup.
     */
    private void assertSetup() {
        assertNotNull("FileSystemHandler not successfully created", fileSystemHandler);
        assertTrue("handler not added", server.containsHandler(handlerId));
        assertTrue("server is not started", server.isStarted());
        assertNotNull("FileSystemClient not successfully created", fileSystemClient);
    }

    /**
     * Test the get file name communication. If client calls getFileName method and the fileId do exist in registry,
     * the server should return the fileName corresponding to the fileId.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFileName() throws Exception {
        assertSetup();
        String fileId = "1001";
        assertTrue("this fileId should in registry", fileSystemHandler.getRegistry().getFileIds().contains("1001"));
        String requestId = fileSystemClient.getFileName(fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            assertEquals("the file name should file1.txt", "file1.txt", response.getResult());
        } else {
            fail("not expect have exception this time");
        }
    }

    /**
     * Test the get file name communication. If client calls getFileName method and the fileId doesn't exist in
     * registry, it should return a response with the FileIdNotFoundException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFileNameException() throws Exception {
        assertSetup();
        String fileId = "1005";
        assertFalse("this fileId should not exist", fileSystemHandler.getRegistry().getFileIds().contains("1005"));
        String requestId = fileSystemClient.getFileName(fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() != null) {
            Exception exception = response.getException();
            assertTrue("the exception should be type of FileIdNotFoundException",
                    exception instanceof FileIdNotFoundException);
        } else {
            fail("should have exception this time");
        }
    }

    /**
     * Test get file size communication. If client calls getFileSize method and the fileId do exist in registry, the
     * server should return the file size corresponding to the fileId.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFileSize() throws Exception {
        assertSetup();
        String fileId = "1001";
        assertTrue("this fileId should in registry", fileSystemHandler.getRegistry().getFileIds().contains("1001"));
        String requestId = fileSystemClient.getFileSize(fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            long fileSize = ((Long) response.getResult()).longValue();
            assertEquals("incorrect file size", new File(ConfigHelper.SERVER_FILE_LOCATION, "1001").length(),
                    fileSize);
        } else {
            fail("not expect have exception this time");
        }
    }

    /**
     * Test rename file communication. If client calls renameFile method and the fileId do exist in registry, the
     * server should rename the file name corresponding to the fileId.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testRenameFile() throws Exception {
        assertSetup();
        String fileId = "1001";
        assertTrue("this fileId should in registry", fileSystemHandler.getRegistry().getFileIds().contains("1001"));
        String requestId = fileSystemClient.getFileName(fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        String oldFileName = (String) response.getResult();
        String newFileName = "newFileName";
        assertFalse("two file name shouldn't be same", oldFileName.equals(newFileName));
        requestId = fileSystemClient.renameFile(fileId, newFileName);
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("be sure no exception occur", response.getException());
        requestId = fileSystemClient.getFileName(fileId);
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("be sure no exception occur", response.getException());
        String fileName = (String) response.getResult();
        assertEquals("file name should be the new one", fileName, newFileName);
        requestId = fileSystemClient.renameFile(fileId, oldFileName);
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("be sure no exception occur", response.getException());
    }

    /**
     * Test check upload operation, should always return true, as most of time, there is enough space.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testCheckUploadFile() throws Exception {
        assertSetup();
        assertSetup();
        String fileName = "nant-0.85-rc3-bin.zip";
        String requestId = fileSystemClient.checkUploadFile(ConfigHelper.CLIENT_FILE_LOCATION, fileName);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("fail to remove file", response.getException());
        assertTrue("should return true, if there is enough free disk space", ((Boolean) response.getResult())
                .booleanValue());
    }

    /**
     * Test retrieve group communication, Client simply calls retriveGroup method, if group name does exist, server
     * should return a response with the file id list of this group.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testRetrieveGroup() throws Exception {
        assertSetup();
        String groupName = "group1";
        assertTrue("be sure that group exist", fileSystemHandler.getRegistry().getGroupNames().contains(groupName));
        String requestId = fileSystemClient.retrieveGroup(groupName);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("no exception thrown", response.getException());
        List fileIds = (List) response.getResult();
        List compareFileIds = fileSystemHandler.getRegistry().getGroupFiles(groupName);
        assertEquals("return size not correct", fileIds.size(), compareFileIds.size());
        for (Iterator iter = fileIds.iterator(); iter.hasNext();) {
            assertTrue("the fileId returned not correct", compareFileIds.contains(iter.next()));
        }
    }

    /**
     * Test retrieve group operation, Client simply calls retriveGroup method, if groupname does not exist, server
     * should return a response with GroupNotFoundException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testRetrieveGroupException() throws Exception {
        assertSetup();
        String groupName = "groupNo";
        assertFalse("be sure the group not exist", fileSystemHandler.getRegistry().getGroupNames()
                .contains(groupName));
        String requestId = fileSystemClient.retrieveGroup(groupName);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertTrue("should return exception that is type of GroupNotFoundException",
                response.getException() instanceof GroupNotFoundException);
    }

    /**
     * Test SearchFiles operation. Client calls searchFiles method, if the fileSearcher exist, server should response
     * with the resulted file id list.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testSearchFiles() throws Exception {
        assertSetup();
        assertNotNull("file searcher not exist", fileSystemHandler.getSearchManager().getFileSearcher("regex"));
    
        String requestId = fileSystemClient.searchFiles("regex", "file1.txt");
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("no exception thrown", response.getException());
        List fileIds = (List) response.getResult();
        assertEquals("the size of list should be 1", fileIds.size(), 1);
        assertTrue("the item should be 1001", fileIds.contains("1001"));
    }

    /**
     * Test SearchFiles operation. Client calls searchFiles method, if the fileSearcher not exist, server should
     * response with FileSearcherNotFoundException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testSearchFilesFileSearcherNotFoundException() throws Exception {
        assertSetup();
        assertNull("file searcher should not exist", fileSystemHandler.getSearchManager().getFileSearcher("regex1"));
        String requestId = fileSystemClient.searchFiles("regex1", ".*");
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertTrue("should return FileSearcherNotFoundException",
                response.getException() instanceof FileSearcherNotFoundException);
    }

    /**
     * Test SearchGroups operation. Client calls searchGroups method, if the groupSearcher exist, server should
     * response with the group name list.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testSearchGroups() throws Exception {
        assertSetup();
        assertNotNull("group searcher not exist", fileSystemHandler.getSearchManager().getGroupSearcher("regex"));
        String requestId = fileSystemClient.searchGroups("regex", ".*");
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("no exception thrown", response.getException());
        List groupNames = (List) response.getResult();
        assertEquals("should return all fileId", groupNames.size(), fileSystemHandler.getRegistry().getGroupNames()
                .size());
        for (Iterator iter = fileSystemHandler.getRegistry().getGroupNames().iterator(); iter.hasNext();) {
            Object obj = iter.next();
            assertTrue("should contains " + obj, groupNames.contains(obj));
        }
        List gIds = fileSystemHandler.getRegistry().getGroupNames();
        for (Iterator iter = groupNames.iterator(); iter.hasNext();) {
            Object obj = iter.next();
            assertTrue("should contains " + obj, gIds.contains(obj));
        }
    }

    /**
     * Test SearchFiles operation. Client calls searchGroups method, if the groupSearcher does not exist, server
     * should response with GroupSearcherNotFoundException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testSearchGroupsFileSearcherNotFoundException() throws Exception {
        assertSetup();
        assertNull("group searcher should not exist", fileSystemHandler.getSearchManager().getGroupSearcher("regex1"));
        String requestId = fileSystemClient.searchGroups("regex1", ".*");
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertTrue("should return GroupSearcherNotFoundException",
                response.getException() instanceof GroupSearcherNotFoundException);
    }

    /**
     * Test retrieve file operation. Client calls retrieveFile method, if the fileId do exist, server should response
     * with the file byte array.
     *
     * @throws Exception
     *             if any excption occur.
     */
    public void testRetrieveFile() throws Exception {
        assertSetup();
        String requestId = fileSystemClient.retrieveFile("1004", ConfigHelper.CLIENT_FILE_LOCATION);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, false);
        while (response == null) {
            response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, false);
        }
        assertNull("retrieve file not success", response.getException());
        assertTrue("file size should equals", diffFile(new File(ConfigHelper.SERVER_FILE_LOCATION, "1004"), new File(
                ConfigHelper.CLIENT_FILE_LOCATION, "file4.jpg")));
    }

    /**
     * helper method to compare files. Simply using byte to byte comparing.
     *
     * @param one
     *            the first file to compare.
     * @param another
     *            the second file to compare.
     * @return true for the two file are same, else false.
     */
    private static boolean diffFile(File one, File another) {
        InputStream oneIs = null;
        InputStream anotherIs = null;
        try {
            if (one.length() == another.length()) {
                oneIs = new FileInputStream(one);
                anotherIs = new FileInputStream(another);
                int x = -1;
                int y = -1;
                do {
                    x = oneIs.read();
                    y = anotherIs.read();
                } while (x != -1 && y != -1 && x == y);
                if (x == -1 && y == -1) {
                    return true;
                }
            }
        } catch (IOException e) {
            // ignore
        } finally {
            // close the opened stream
            try {
                if (oneIs != null) {
                    oneIs.close();
                }
                if (anotherIs != null) {
                    anotherIs.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
        return false;
    }

    /**
     * Test upload file operation. Client calls uploadFile(String, String) method, as fileId isn't provided, server
     * will will persist the file in server end, and create new file entry in registry to register this file.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testUploadFile() throws Exception {
        assertSetup();
        String fileName = "nant-0.85-rc3-bin.zip";
        String requestId = fileSystemClient.uploadFile(ConfigHelper.CLIENT_FILE_LOCATION, fileName);
        ResponseMessage response = null;
        do {
            response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, false);
        } while (response == null);
        assertNull("retrieve file not success", response.getException());
        assertTrue("uploaded file is not same as the orginal file", diffFile(new File(
                ConfigHelper.CLIENT_FILE_LOCATION, fileName), new File(ConfigHelper.SERVER_FILE_LOCATION,
                (String) response.getResult())));
        requestId = fileSystemClient.removeFile((String) response.getResult());
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("fail to remove file", response.getException());
    }

    /**
     * Test upload file with overwritten. Client calss uploadFile(String, String, String) method, as the fileId is
     * provided, server will overwrite the file exist in server end, and update the file name in registry.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testUploadFileWithOverwritten() throws Exception {
        assertSetup();
        String fileId = "1001";
        assertTrue("this fileId should in registry", fileSystemHandler.getRegistry().getFileIds().contains(fileId));
        // retrieve that file name for restore
        String requestId = fileSystemClient.getFileName(fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("no exception should occur", response.getException());
        String retrieveFileName = (String) response.getResult();
        requestId = fileSystemClient.retrieveFile(fileId, ConfigHelper.CLIENT_FILE_LOCATION);
        response = null;
        do {
            response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, false);
        } while (response == null);
        assertNull("no exception should occur", response.getException());
        String fileName = "nant-0.85-rc3-bin.zip";
        requestId = fileSystemClient.uploadFile(fileId, ConfigHelper.CLIENT_FILE_LOCATION, fileName);
        response = null;
        do {
            response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, false);
        } while (response == null);
        assertNull("no exception should occur", response.getException());
        assertTrue("uploaded file is not same as the orginal file", diffFile(new File(
                ConfigHelper.CLIENT_FILE_LOCATION, fileName), new File(ConfigHelper.SERVER_FILE_LOCATION,
                (String) response.getResult())));
        requestId = fileSystemClient.getFileName(fileId);
        response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        assertNull("no exception should occur", response.getException());
        assertEquals("file name should be the new one", response.getResult(), fileName);
        // restore file
        requestId = fileSystemClient.uploadFile(fileId, ConfigHelper.CLIENT_FILE_LOCATION, retrieveFileName);
        response = null;
        do {
            response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, false);
        } while (response == null);
        assertNull("no exception should occur", response.getException());
        new File(ConfigHelper.CLIENT_FILE_LOCATION, retrieveFileName).delete();
    }

    /**
     * Test upload file with overwritten. Client calls uploadFile(String, String, String) method, if the fileId
     * provied does not exist in registry, server will not accept the upload requirement.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testUploadFileWithOverwrittenException() throws Exception {
        assertSetup();
        String fileId = "notExist";
        assertFalse("this fileId should in registry", fileSystemHandler.getRegistry().getFileIds().contains(fileId));
        String fileName = "nant-0.85-rc3-bin.zip";
        String requestId = fileSystemClient.uploadFile(fileId, ConfigHelper.CLIENT_FILE_LOCATION, fileName);
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        if (status == FileUploadCheckStatus.UPLOAD_ACCEPTED) {
            while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
                Thread.sleep(500);
            }
            ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, false);
            assertNull("should timeout and return null", response);
        }
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     *
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(ClientServerTestCase.class);
    }
}
