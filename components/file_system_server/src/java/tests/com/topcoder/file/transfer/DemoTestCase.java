/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.topcoder.file.transfer.message.ResponseMessage;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence;
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Demo to show how to use this components programmatic.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 * @since 1.0
 */
public class DemoTestCase extends TestCase {

    /**
     * Represents the fileName that will be uploaded.
     */
    private static final String UPLOAD_FILE_NAME = "nant-0.85-rc3-bin.zip";

    /** The handler Id used for test. */
    private final String handlerId = "HanderId";

    /** the IPServer instance used in tests. */
    private IPServer ipServer;

    /** the FileSystemClient used in test. */
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
     * create the FileSystemClient instance used for testing.
     *
     * @return the instance.
     * @throws Exception
     *             if any exception occur.
     */
    private FileSystemClient createFileSystemClient() throws Exception {
        ConfigHelper configHelper = ConfigHelper.getInstance();
        // create the file persistence
        SimpleLockingFileSystemPersistence persistence = new SimpleLockingFileSystemPersistence(
                new FileSystemPersistence());

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
     * init the IPServer with FileSystemHandler added.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void initServer() throws Exception {
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
        // creat the search manager
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
     * demo that registers a FileSystemHandler to the IPServer programatically.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testRegisterFileSystemHandler() throws Exception {
        ConfigHelper configHelper = ConfigHelper.getInstance();
        // create the IPServer
        IPServer server = new IPServer(configHelper.getAddress(), configHelper.getPort(), configHelper
                .getMaxConnections(), 0, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

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
        server.addHandler(handlerId, fileSystemHandler);
    }

    /**
     * demo that shows how to create a FileSystemClient class.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testCreateFileSystemClient() throws Exception {
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
        FileSystemClient fsClient = new FileSystemClient(configHelper.getAddress(), configHelper.getPort(),
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE, handlerId, persistence, uploadErrorHandler,
                retrieveErrorHandler);
        // connect and disconnect the client, as specified in IP Server 2.0 docs.
        // fsClient.connect();
        // fsClient.disconnect();
        // get the address
        fsClient.getAddress();
    }

    /**
     * Demo that shwos how to upload a new file.
     *
     * @throws Exception
     *             if nayexception occurs.
     */
    public void testUploadFile() throws Exception {
        String requestId = fileSystemClient.uploadFile(ConfigHelper.CLIENT_FILE_LOCATION, UPLOAD_FILE_NAME);
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        if (status == FileUploadCheckStatus.UPLOAD_ACCEPTED) {
            while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
                Thread.sleep(500);
            }
            ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
            if (response != null && response.getException() == null) {
                String fileId = (String) response.getResult();
                // use the file with the resulted file name
                // remove file
                requestId = fileSystemClient.removeFile(fileId);
                response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
                if (response.getException() == null) {
                    // more process.
                } else {
                    // handle exception
                }
            } else {
                // handle the exception.
            }
        } else {
            // file upload unaccepted
        }
    }

    /**
     * Demo that shows how to overwrite a file with a fileId.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testUploadFileWithOverwrite() throws Exception {
        String fileId = "notExist";
        String requestId = fileSystemClient.uploadFile(fileId, ConfigHelper.CLIENT_FILE_LOCATION, UPLOAD_FILE_NAME);
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        if (status == FileUploadCheckStatus.UPLOAD_ACCEPTED) {
            while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
                Thread.sleep(500);
            }
            ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
            if (response != null && response.getException() == null) {
                // String fileId = (String) response.getResult();
                // use the file with the resulted file name
            } else {
                // handle the exception.
            }
        } else {
            // file upload unaccepted
        }
    }

    /**
     * Demo that shows how to retrieve a file.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testRetrieveFile() throws Exception {
        String fileId = "fileId";
        String requestId = fileSystemClient.retrieveFile(fileId, ConfigHelper.CLIENT_FILE_LOCATION);
        while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
            Thread.sleep(500);
        }
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response != null && response.getException() == null) {
            // String fileName = (String) response.getResult();
            // use the file with the resulted file name
        } else {
            // handle the exception.
        }
    }

    /**
     * demo that shows how to get file name.
     *
     * @throws Exception
     *             if any exception occur
     */
    public void testGetFileName() throws Exception {
        String fileName = "1001";
        String requestId = fileSystemClient.getFileName(fileName);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // String fileName = (String) response.getResult();
        } else {
            // handle the exception
        }
    }

    /**
     * demo that shows how to get file size.
     *
     * @throws Exception
     *             if any exception occur
     */
    public void testGetFileSize() throws Exception {
        String fileId = "1003";
        String requestId = fileSystemClient.getFileSize(fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // Long fileSize = (Long) response.getResult();
        } else {
            // handle the exception
        }
    }

    /**
     * demo that shows how to rename file.
     *
     * @throws Exception
     *             if any exception occur
     */
    public void testRenameFile() throws Exception {
        String fileId = "fileId";
        String requestId = fileSystemClient.renameFile(fileId, "RenamedFile");
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // response.getResult() == null;
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to remove file.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testRemoveFile() throws Exception {
        // the main demo part part
        String fileId = "fileId";
        // remove the file
        String requestId = fileSystemClient.removeFile(fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // response.getResult() == null
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to create a group.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testCreateGroup() throws Exception {
        String groupName = "group2";
        // empty fileIds list
        List fileIds = new ArrayList();
        String requestId = fileSystemClient.createGroup(groupName, fileIds);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // response.getResult() == null
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to update a group.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testUpdateGroup() throws Exception {
        String groupName = "groupXXX";
        // empty fileIds list
        List newFileIds = new ArrayList();
        String requestId = fileSystemClient.updateGroup(groupName, newFileIds);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // response.getResult() == null
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to retrieve a group.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testRetrieveGroup() throws Exception {
        String groupName = "groupXXX";
        String requestId = fileSystemClient.retrieveGroup(groupName);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // List fileIds = (List) response.getResult();
            // iterate over the list of file ids.
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to remove a group.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testRemoveGroup() throws Exception {
        String groupName = "groupXXX";
        // the user can permanently remove the files from this group
        boolean removeFiles = true;
        // or, it can remove just the group and leave the files.
        removeFiles = false;
        String requestId = fileSystemClient.removeGroup(groupName, removeFiles);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // response.getResult() == null
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to add file to a group.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testAddFileToGroup() throws Exception {
        String groupName = "groupXXX";
        String fileId = "fileId";
        String requestId = fileSystemClient.addFileToGroup(groupName, fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // response.getResult() == null
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to remove file from a group.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testRemoveFileFromGroup() throws Exception {
        String groupName = "groupXXX";
        String fileId = "fileId";
        String requestId = fileSystemClient.removeFileFromGroup(groupName, fileId);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // response.getResult() == null
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to search files using a regex pattern.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testSearchFilesUsingRegex() throws Exception {
        // we must know the searcher's name
        String searcherName = "regex";
        String criteria = ".*\\.txt";
        String requestId = fileSystemClient.searchFiles(searcherName, criteria);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // List fileIds = (List) response.getResult();
            // iterate over the list of file ids.
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to search groups using a regex pattern.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testSearchGroupsUsingRegex() throws Exception {
        // we must know the searcher's name
        String searcherName = "regex";
        String criteria = "TopCoder.*";
        String requestId = fileSystemClient.searchGroups(searcherName, criteria);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // List groupNames = (List) response.getResult();
            // iterate over the list of group names.
        } else {
            // handle the exception
        }
    }

    /**
     * Demo that shows how to search groups contains some file id.
     *
     * @throws Exception
     *             if any exception occurs.
     */
    public void testSearchGroupsContainsFileId() throws Exception {
        // we must know the searcher's name
        String searcherName = "fileId";
        String fileId = "fileId";
        String criteria = fileId;
        String requestId = fileSystemClient.searchGroups(searcherName, criteria);
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response.getException() == null) {
            // List groupNames = (List) response.getResult();
            // iterate over the list of group names.
        } else {
            // handle the exception
        }
    }

    /**
     * <p>
     * Upload a file in a stream, without over-writing an existing file, and give it a name.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void testUploadFileInStream() throws Exception {
        // a stream with file data
        InputStream dataStream = new FileInputStream(new File(ConfigHelper.CLIENT_FILE_LOCATION + "/"
                + UPLOAD_FILE_NAME));

        String requestId = fileSystemClient.uploadFile(dataStream, UPLOAD_FILE_NAME);

        // check status
        FileUploadCheckStatus status = fileSystemClient.getFileUploadCheckStatus(requestId, true);
        if (status == FileUploadCheckStatus.UPLOAD_ACCEPTED) {
            while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
                Thread.sleep(500);
            }

            // get response message
            ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
            if (response != null && response.getException() == null) {
                // more process.
            } else {
                // handle the exception
            }
        }
    }

    /**
     * <p>
     * Retrieve a file in stream.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void testRetrieveFileAccuracy() throws Exception {
        // a stream connected to the destination of the data
        OutputStream outStream = new ByteArrayOutputStream();
        String requestId = fileSystemClient.retrieveFile("fileId", outStream);

        while (fileSystemClient.isFileTransferWorkerAlive(requestId)) {
            Thread.sleep(500);
        }

        // get response message
        ResponseMessage response = (ResponseMessage) fileSystemClient.receiveResponse(requestId, true);
        if (response != null && response.getException() == null) {
            // more process.
        } else {
            // handle the exception
        }
    }

    /**
     * <p>
     * Aggregates all tests in this class.
     * </p>
     *
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(DemoTestCase.class);
    }
}
