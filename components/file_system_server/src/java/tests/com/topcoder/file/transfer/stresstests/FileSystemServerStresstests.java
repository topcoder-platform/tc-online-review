/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.stresstests;

import com.topcoder.file.transfer.FileSystemClient;
import com.topcoder.file.transfer.FileSystemHandler;
import com.topcoder.file.transfer.FileTransferHandler;
import com.topcoder.file.transfer.FileUploadCheckStatus;
import com.topcoder.file.transfer.message.ResponseMessage;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.FileIdGroupSearcher;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;

import com.topcoder.processor.ipserver.IPServer;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * The stress test of File System Server component.
 *
 * @author brain_cn
 * @version 1.0
 */
public class FileSystemServerStresstests extends TestCase {
    /** Represents the stress test namespace. */
    private static final String STRESS_NAMESPACE = "com.topcoder.file.transfer.stresstests";

    /** Represents the stress test message namespace. */
    private static final String MESSAGE_NAMESPACE = "com.topcoder.file.transfer.stresstests.message";

    /** Represents the stress test DB connection factory namespace. */
    private static final String DB_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /** Represents the stress test namespaces. */
    private static final String[] NAMESPACES = new String[] { STRESS_NAMESPACE, MESSAGE_NAMESPACE, DB_NAMESPACE};
    
    /** Represents the stress test config file. */
    private static final String CONFIG_FILE = "stresstests/stress_test.xml";
    
    /** Represents the stress test database config file. */
    private static final String DB_CONFIG_FILE = "DBConnectionFactoryImpl.xml";

    /** Represents the current time. */
    private static long current = System.currentTimeMillis();

    /** Represents the cost time. */
    private static long cost = -1;

    /** Represents the test times. */
    private static final int COUNT = 20;

    /** Represents the wait time, measured as ms. */
    private static long WAIT_TIME = 10;

    /** Represent the test file size. */
    private static long FILE_SIZE = 1488;

    /** The group name prefix. */
    private static final String GROUP_PREFIX = "Group_";

    /** Represents the test server. */
    private IPServer server = null;

    /** Represents the test client. */
    private FileSystemClient client = null;

    /** Represent the test files which need to be remove after testing. */
    private List testFiles = new ArrayList();

    /** Represent the test groups which need to be remove after testing. */
    private Set testGroups = new HashSet();

    /**
     * Load test namespace
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        loadNamespace();
        server = getServer();
        startServer(server);
        client = getFileSystemClient();
        client.connect();
    }

    /**
     * Remove all test namespaces.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        for (Iterator iter = testFiles.iterator(); iter.hasNext();) {
            String fileId = (String) iter.next();
            String rid = client.removeFile(fileId);
            client.receiveResponse(rid, true);
            iter.remove();
        }

        for (Iterator iter = testGroups.iterator(); iter.hasNext();) {
            String groupId = (String) iter.next();

            // Remove group and all files
            String rid = client.removeGroup(groupId, true);
            client.receiveResponse(rid, true);
            iter.remove();
        }

        client.disconnect();
        stopServer(server);
        releaseNamespace();
    }

    /**
     * Test of upload file.
     *
     * @throws Exception to JUnit
     */
    public void testUploadFiles() throws Exception {
        String uploadFile = getUploadFile();
        start();

        for (int i = 0; i < COUNT; i++) {
            assertNotNull("Failed to upload file", uploadFiles(uploadFile));
        }

        stop();
        printResult("upload file", COUNT);
    }

    /**
     * Test of retrieve file, get file name and retrieve file length.
     *
     * @throws Exception to JUnit
     */
    public void testFileFunctionalility() throws Exception {
        String fileName = getUploadFile();
        String fileId = uploadFiles(fileName);
        start();

        for (int i = 0; i < COUNT; i++) {
            retrieveFile(fileId);
            assertEquals("Failed to get file name", fileName, getFileName(fileId));
            assertEquals("Failed to get correct file size", FILE_SIZE, getFileSize(fileId));
        }

        stop();
        printResult("File functionality", COUNT);
    }

    /**
     * Test of remove file from server.
     *
     * @throws Exception to JUnit
     */
    public void testRemoveFile() throws Exception {
    	// Prepare files
        String[] fileIds = new String[COUNT];        
        for (int i = 0; i < COUNT; i++) {
            String fileName = getUploadFile();
            fileIds[i] = uploadFiles(fileName);
        }

        start();
        for (int i = 0; i < COUNT; i++) {
            removeFile(fileIds[i]);

            // Verify the file should be removed
            String rid = client.getFileName(fileIds[i]);

            ResponseMessage response = (ResponseMessage) client.receiveResponse(rid, true);
            assertNotNull("Failed to remove", response);
            assertNotNull("Failed to remove", response.getException());
        }

        stop();
        printResult("removeFile", COUNT);
    }

    /**
     * Test of create group.
     *
     * @throws Exception to JUnit
     */
    public void testCreateGroup() throws Exception {
        start();

        List fileIds = new ArrayList();

        for (int i = 0; i < COUNT; i++) {
            String groupName = GROUP_PREFIX + i;
            createGroup(groupName, fileIds);
            assertEquals("Failed to create group with files", fileIds.size(), retrieveGroup(groupName).size());
        }

        stop();
        printResult("createGroup", COUNT);
    }

    /**
     * Test of add file to group.
     *
     * @throws Exception to JUnit
     */
    public void testAddFileToGroup() throws Exception {
        String groupName = "test_group";
        createGroup(groupName, new ArrayList());

        start();

        for (int i = 0; i < COUNT; i++) {
            String fileId = uploadFiles(getUploadFile());
            addFileToGroup(groupName, fileId);
            assertEquals("Failed to add file to group", i + 1, retrieveGroup(groupName).size());
        }

        stop();
        printResult("addFileToGroup", COUNT);
    }

    /**
     * Test of add file to group.
     *
     * @throws Exception to JUnit
     */
    public void testRemoveFileFromGroup() throws Exception {
        // Prepare files for group
        String groupName = "test_group";
        createGroup(groupName, new ArrayList());

        String[] fileIds = new String[COUNT];

        for (int i = 0; i < COUNT; i++) {
            fileIds[i] = uploadFiles(getUploadFile());
            addFileToGroup(groupName, fileIds[i]);
        }

        start();

        for (int i = 0; i < COUNT; i++) {
            removeFileFromGroup(groupName, fileIds[i]);
            assertEquals("Failed to create group with files", COUNT - i - 1, retrieveGroup(groupName).size());
        }

        stop();
        printResult("removeFileFromGroup", COUNT);
    }

    /**
     * Test of all functionality in one method.
     * 
     * @throws Exception to JUnit
     */
    public void testAllFunctionality() throws Exception {
        start();
        for (int i = 0; i < COUNT; i++) {
        	// File founctionality
        	String fileId = uploadFiles(getUploadFile());
        	retrieveFile(fileId);
        	getFileName(fileId);
        	getFileSize(fileId);
        	
        	// Group functionality
        	String groupName = GROUP_PREFIX + i;
        	
        	createGroup(groupName, new ArrayList()); //create group
        	addFileToGroup(groupName, fileId); // add file to group
        	List result = retrieveGroup(groupName);  // retrieve group
        	assertEquals("Failed to retrieve group", 1, result.size());
        	assertEquals("Failed to retrieve group", fileId, result.iterator().next());
        	removeFileFromGroup(groupName, fileId); // remove file from group
        	client.retrieveGroup(groupName); // remove group

        	// Remove file
        	removeFile(fileId);
        }
        stop();
        printResult("All functionality", COUNT);
    }

    /**
     * Create group with name and file ids.
     *
     * @param groupName the group name
     * @param fileIds the file ids array
     *
     * @throws Exception to JUnit
     */
    private void createGroup(String groupName, List fileIds)
        throws Exception {
        String requestId = client.createGroup(groupName, fileIds);
        client.receiveResponse(requestId, true);
        testGroups.add(groupName);
    }

    /**
     * Retrieve group with name.
     *
     * @param groupName group name
     *
     * @return the file ids
     *
     * @throws Exception to JUnit
     */
    private List retrieveGroup(String groupName) throws Exception {
        String requestId = client.retrieveGroup(groupName);
        ResponseMessage response = (ResponseMessage) client.receiveResponse(requestId, true);

        return (List) response.getResult();
    }

    /**
     * Retrieve file with file id.
     *
     * @param fileId the file ids
     *
     * @throws Exception to JUnit
     */
    private void retrieveFile(String fileId) throws Exception {
        String rid = client.retrieveFile(fileId, getClientLoc());

        ResponseMessage response = null;
        int count = 20;
        int i = 0;
        while ((response = (ResponseMessage) client.receiveResponse(rid, false)) == null && i++ < count) {
            Thread.sleep(WAIT_TIME);
        }
        assertNotNull("Failed to upload", response);
        assertNull("Failed to process uploading", response.getException());

        String fileName = (String) response.getResult();
        assertEquals("Failed to retrieve file", fileId, fileName);
    }

    /**
     * Get file name with file id.
     *
     * @param fileId the file id
     *
     * @return the file name
     *
     * @throws Exception to JUnit
     */
    private String getFileName(String fileId) throws Exception {
        String rid = client.getFileName(fileId);

        ResponseMessage response = (ResponseMessage) client.receiveResponse(rid, true);
        assertNotNull("Failed to upload", response);
        assertNull("Failed to process uploading", response.getException());

        String fileName = (String) response.getResult();

        //  assertEquals("Failed to retrieve file", fileId, fileName);
        // System.out.println("fileId: " + fileId + " fileName: " + fileName);

        return fileName;
    }

    /**
     * Get file size with file id.
     *
     * @param fileId the file id
     *
     * @return the file size
     *
     * @throws Exception to JUnit
     */
    private long getFileSize(String fileId) throws Exception {
        String rid = client.getFileSize(fileId);

        ResponseMessage response = (ResponseMessage) client.receiveResponse(rid, true);
        assertNotNull("Failed to upload", response);
        assertNull("Failed to process uploading", response.getException());

        Long fileSize = (Long) response.getResult();

        //  assertEquals("Failed to retrieve file", fileId, fileName);
        // System.out.println("fileId: " + fileId + " fileSize: " + fileSize);

        return fileSize.longValue();
    }

    /**
     * Remove file with file id.
     *
     * @param fileId the file id
     *
     * @throws Exception to JUnit
     */
    private void removeFile(String fileId) throws Exception {
        String rid = client.removeFile(fileId);

        ResponseMessage response = (ResponseMessage) client.receiveResponse(rid, true);
        assertNotNull("Failed to remove", response);
        assertNull("Failed to process removing", response.getException());
        assertNull("Failed to remove file", response.getResult());
    }

    /**
     * Add file to group.
     *
     * @param groupName the group name
     * @param fileId the file id
     *
     * @throws Exception to JUnit
     */
    private void addFileToGroup(String groupName, String fileId)
        throws Exception {
        String requestId = client.addFileToGroup(groupName, fileId);
        client.receiveResponse(requestId, true);
    }

    /**
     * Remove file from group.
     *
     * @param groupName the group name
     * @param fileId the file id
     *
     * @throws Exception to JUnit
     */
    private void removeFileFromGroup(String groupName, String fileId)
        throws Exception {
        String requestId = client.removeFileFromGroup(groupName, fileId);
        client.receiveResponse(requestId, true);
    }

    /**
     * Upload files to server.
     *
     * @param fileName the file name to upload
     *
     * @return the file id
     *
     * @throws Exception to JUnit
     */
    private String uploadFiles(String fileName) throws Exception {
        String rid = client.uploadFile(getClientLoc(), fileName);
        FileUploadCheckStatus status = client.getFileUploadCheckStatus(rid, true);
        assertEquals("Failed to upload", status, FileUploadCheckStatus.UPLOAD_ACCEPTED);

        ResponseMessage response = null;
        int count = 20;
        int i = 0;
        while ((response = (ResponseMessage) client.receiveResponse(rid, false)) == null && i++ < count) {
            Thread.sleep(WAIT_TIME);
        }

        assertNotNull("Failed to upload", response);
        assertNull("Failed to process uploading", response.getException());

        String fileId = response.getResult().toString();
        testFiles.add(fileId);

        return fileId;
    }

    /**
     * Print the test result.
     *
     * @param name the test name
     * @param times the times to test
     */
    private static void printResult(String name, int times) {
        System.out.println("Test " + name + " " + times + " times, took time " + cost + " ms");
    }

    /**
     * Start to time.
     */
    private static void start() {
        current = System.currentTimeMillis();
        cost = -1;
    }

    /**
     * Stop time.
     */
    private static void stop() {
        cost = System.currentTimeMillis() - current;
    }

    /**
     * Remove all test namespaces.
     *
     * @throws Exception to JUnit
     */
    private void loadNamespace() throws Exception {
        releaseNamespace();

        ConfigManager cm = ConfigManager.getInstance();
        cm.add(CONFIG_FILE);
        cm.add(DB_CONFIG_FILE);
    }

    /**
     * Remove all test namespaces.
     *
     * @throws Exception to JUnit
     */
    private void releaseNamespace() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (int i = 0; i < NAMESPACES.length; i++) {
            if (cm.existsNamespace(NAMESPACES[i])) {
                cm.removeNamespace(NAMESPACES[i]);
            }
        }
    }

    /**
     * Create and return IPServer.
     *
     * @return IPServer
     *
     * @throws Exception to JUnit
     */
    private IPServer getServer() throws Exception {
        IPServer server = new IPServer(getAddress(), getPort(), 0, 0, MESSAGE_NAMESPACE);
        server.addHandler(getHandlerId(), getHandler());

        return server;
    }

    /**
     * Start IPServer.
     *
     * @param server the IPServer to start
     *
     * @throws Exception to JUnit
     */
    private static void startServer(IPServer server) throws Exception {
        if (!server.isStarted()) {
            server.start();
        }
    }

    /**
     * Stop IPServer.
     *
     * @param server the IPServer to stop.
     *
     * @throws Exception to JUnit
     */
    private static void stopServer(IPServer server) throws Exception {
        while (server.isStarted()) {
            server.stop();
            Thread.sleep(WAIT_TIME);
        }
    }

    /**
     * Return address.
     *
     * @return address
     *
     * @throws Exception to JUnit
     */
    private String getAddress() throws Exception {
        return ConfigManager.getInstance().getString(STRESS_NAMESPACE, "address");
    }

    /**
     * Return port.
     *
     * @return port
     *
     * @throws Exception to JUnit
     */
    private int getPort() throws Exception {
        return Integer.parseInt(ConfigManager.getInstance().getString(STRESS_NAMESPACE, "port"));
    }

    /**
     * Return max_requests.
     *
     * @return max_requests
     *
     * @throws Exception to JUnit
     */
    private int getMaxRequests() throws Exception {
        return Integer.parseInt(ConfigManager.getInstance().getString(STRESS_NAMESPACE, "max_requests"));
    }

    /**
     * Return id_sequence_name.
     *
     * @return id_sequence_name
     *
     * @throws Exception to JUnit
     */
    private IDGenerator getIDGenerator() throws Exception {
        return IDGeneratorFactory.getIDGenerator(ConfigManager.getInstance()
                                                              .getString(STRESS_NAMESPACE, "id_sequence_name"));
    }

    /**
     * Return server_location.
     *
     * @return server_location
     *
     * @throws Exception to JUnit
     */
    private String getServerLoc() throws Exception {
        return ConfigManager.getInstance().getString(STRESS_NAMESPACE, "server_location");
    }

    /**
     * Return client_location.
     *
     * @return client_location
     *
     * @throws Exception to JUnit
     */
    private String getClientLoc() throws Exception {
        return ConfigManager.getInstance().getString(STRESS_NAMESPACE, "client_location");
    }

    /**
     * Return files_file.
     *
     * @return files_file
     *
     * @throws Exception to JUnit
     */
    private File getFilesFile() throws Exception {
        return new File(getServerLoc(), ConfigManager.getInstance().getString(STRESS_NAMESPACE, "files_file"));
    }

    /**
     * Return group_file.
     *
     * @return group_file
     *
     * @throws Exception to JUnit
     */
    private File getGroupsFile() throws Exception {
        return new File(getServerLoc(), ConfigManager.getInstance().getString(STRESS_NAMESPACE, "group_file"));
    }

    /**
     * Create and return FileSystemRegistry.
     *
     * @return FileSystemRegistry
     *
     * @throws Exception to JUnit
     */
    private FileSystemRegistry getFileSystemRegistry()
        throws Exception {
        FileSystemRegistry registry = new FileSystemXmlRegistry(getFilesFile(), getGroupsFile(), getIDGenerator());

        return registry;
    }

    /**
     * Create and return FileSystemPersistence.
     *
     * @return FileSystemPersistence
     *
     * @throws Exception to JUnit
     */
    private FileSystemPersistence getPersistence() throws Exception {
        FileSystemPersistence persistence = new FileSystemPersistence();

        return persistence;
    }

    /**
     * Create and return SearchManager.
     *
     * @return SearchManager
     *
     * @throws Exception to JUnit
     */
    private SearchManager getSearchManager() throws Exception {
        SearchManager sm = new SearchManager();
        FileSystemRegistry registry = getFileSystemRegistry();
        sm.addFileSearcher("regex", new RegexFileSearcher(registry));
        sm.addGroupSearcher("regex", new RegexGroupSearcher(registry));
        sm.addGroupSearcher("fileId", new FileIdGroupSearcher(registry));

        return sm;
    }

    /**
     * Create and return validator.
     *
     * @return validator
     *
     * @throws Exception to JUnit
     */
    private ObjectValidator getValidator() throws Exception {
        ObjectValidator validator = new UploadRequestValidator(new FreeDiskSpaceNativeChecker(getServerLoc()));

        return validator;
    }

    /**
     * return handler_id.
     *
     * @return handler_id
     *
     * @throws Exception to JUnit
     */
    private String getHandlerId() throws Exception {
        return ConfigManager.getInstance().getString(STRESS_NAMESPACE, "handler_id");
    }

    /**
     * Create and return FileSystemHandler.
     *
     * @return FileSystemHandler instance
     *
     * @throws Exception to JUnit
     */
    private FileSystemHandler getHandler() throws Exception {
        FileSystemHandler handler = new FileSystemHandler(getMaxRequests(), getFileSystemRegistry(), getPersistence(),
                getServerLoc(), getValidator(), getSearchManager());

        return handler;
    }

    /**
     * return upload_files.
     *
     * @return upload_files
     *
     * @throws Exception to JUnit
     */
    private String getUploadFile() throws Exception {
        return ConfigManager.getInstance().getString(STRESS_NAMESPACE, "upload_files");
    }

    /**
     * create the FileSystemClient instance used for testing.
     *
     * @return the instance.
     *
     * @throws Exception to JUnit
     */
    private FileSystemClient getFileSystemClient() throws Exception {
        // create the error handlers
        FileTransferHandler uploadErrorHandler = new DefaultFileTransferErrorHandler();
        FileTransferHandler retrieveErrorHandler = new DefaultFileTransferErrorHandler();

        // create the client
        return new FileSystemClient(getAddress(), getPort(), MESSAGE_NAMESPACE, getHandlerId(), getPersistence(),
            uploadErrorHandler, retrieveErrorHandler);
    }
}
