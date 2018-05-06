/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import java.io.File;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNonNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;
import com.topcoder.processor.ipserver.Handler;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test FileSystemHandler for correctness.
 * @author FireIce
 * @version 1.0
 */
public class FileSystemHandlerTestCase extends TestCase {
    /**
     * Represents the max requests value.
     */
    private static final int MAXREQUUEST = 100;

    /**
     * Represents the file location.
     */
    private static final String FILE_LOCATION = "test_files/";

    /**
     * Represents the xml file for files.
     */
    private static final File FILES_FILE = new File("test_files/valid_files.xml");

    /**
     * Represents the xml file for groups.
     */
    private static final File GROUPS_FILE = new File("test_files/valid_groups.xml");

    /**
     * Represents the FileSystemRegistry instance used in tests.
     */
    private FileSystemRegistry registry;

    /**
     * Represents the FilePersistence instance used in tests.
     */
    private FilePersistence persistence;

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
     * create FileSystemRegistry instance.
     * @return the FIleSYstemRegistry instance.
     * @throws Exception
     *             if any Exception occur.
     */
    private FileSystemRegistry getFileSystemRegistry() throws Exception {
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator(ConfigHelper.IDGENERATOR_NAMESPACE);
        return new FileSystemXmlRegistry(FILES_FILE, GROUPS_FILE, idGenerator);
    }

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        registry = getFileSystemRegistry();
        persistence = new FileSystemPersistence();
        validator = new UploadRequestValidator(new FreeDiskSpaceNonNativeChecker(FILE_LOCATION));
        searchManager = new SearchManager();
        fileSystemHandler = new FileSystemHandler(MAXREQUUEST, registry, persistence, FILE_LOCATION, validator,
                searchManager);
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        registry = null;
        persistence = null;
        searchManager = null;
        validator = null;
        fileSystemHandler = null;
    }

    /**
     * Test inheritence.
     */
    public void testInheritence() {
        assertTrue("should extends Handler class", fileSystemHandler instanceof Handler);
    }

    /**
     * Test constructor,if any argument is null, throw NullPointerException.
     */
    public void testCtorNullPointerException() {
        try {
            new FileSystemHandler(MAXREQUUEST, null, persistence, FILE_LOCATION, validator, searchManager);
            fail("if registry is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemHandler(MAXREQUUEST, registry, null, FILE_LOCATION, validator, searchManager);
            fail("if persistence is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemHandler(MAXREQUUEST, registry, persistence, null, validator, searchManager);
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemHandler(MAXREQUUEST, registry, persistence, FILE_LOCATION, null, searchManager);
            fail("if validator is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemHandler(MAXREQUUEST, registry, persistence, FILE_LOCATION, validator, null);
            fail("if searchManager is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test constructor,if the maxRequests argument is negative or any string is empty, throw IllegalArgumentException.
     */
    public void testCtorIllegalArgumentException() {
        try {
            new FileSystemHandler(-1, registry, persistence, FILE_LOCATION, validator, searchManager);
            fail("if the maxRequests argument is negative, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new FileSystemHandler(MAXREQUUEST, registry, persistence, " ", validator, searchManager);
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test constructor,while all arguments are valid.
     */
    public void testCtorSuccess() {
        new FileSystemHandler(MAXREQUUEST, registry, persistence, FILE_LOCATION, validator, searchManager);
    }

    /**
     * Test getRegistry method, return the instance setted in constructor.
     */
    public void testGetRegistry() {
        assertNotNull("setup fails", fileSystemHandler);
        assertSame("should be the same instance", fileSystemHandler.getRegistry(), registry);
    }

    /**
     * Test getPersistence method, return the instance setted in constructor.
     */
    public void testGetPersistence() {
        assertNotNull("setup fails", fileSystemHandler);
        assertSame("should be the same instance", fileSystemHandler.getPersistence(), persistence);
    }

    /**
     * Test getFileLocation method, return the instance setted in constructor.
     */
    public void testGetFileLocation() {
        assertNotNull("setup fails", fileSystemHandler);
        assertSame("should be the same instance", fileSystemHandler.getFileLocation(), FILE_LOCATION);
    }

    /**
     * Test getUploadRequestValidator method, return the instance setted in constructor.
     */
    public void testGetUploadRequestValidator() {
        assertNotNull("setup fails", fileSystemHandler);
        assertSame("should be the same instance", fileSystemHandler.getUploadRequestValidator(), validator);
    }

    /**
     * Test getSearchManager method, return the instance setted in constructor.
     */
    public void testGetSearchManager() {
        assertNotNull("setup fails", fileSystemHandler);
        assertSame("should be the same instance", fileSystemHandler.getSearchManager(), searchManager);
    }

    /**
     * Test getGenerator method, return the instance setted in constructor.
     */
    public void testGetGenerator() {
        assertNotNull("setup fails", fileSystemHandler);
        assertNotNull("generator shouldn't be null", fileSystemHandler.getGenerator());
    }

    /**
     * Test getBytesIterators method, return the instance setted in constructor.
     */
    public void testGetBytesIterators() {
        assertNotNull("setup fails", fileSystemHandler);
        assertNotNull("bytesIterators map shouldn't be null", fileSystemHandler.getBytesIterators());
    }

    /**
     * Test getFilesToRetrieve method, return the instance setted in constructor.
     */
    public void testGetFilesToRetrieve() {
        assertNotNull("setup fails", fileSystemHandler);
        assertNotNull("filesToRetrieve map shouldn't be null", fileSystemHandler.getFilesToRetrieve());
    }

    /**
     * Test getFilesToUpload method, return the instance setted in constructor.
     */
    public void testGetFilesToUpload() {
        assertNotNull("setup fails", fileSystemHandler);
        assertNotNull("filesToRetrieve map shouldn't be null", fileSystemHandler.getFilesToUpload());
    }

    /**
     * Test getFilesToRetrieveLastAccessDates method, return the instance setted in constructor.
     */
    public void testGetFilesToRetrieveLastAccessDates() {
        assertNotNull("setup fails", fileSystemHandler);
        assertNotNull("filesToRetrieveLastAccessDates map shouldn't be null", fileSystemHandler
                .getFilesToRetrieveLastAccessDates());
    }

    /**
     * Test getFilesToUploadLastAccessDates method, return the instance setted in constructor.
     */
    public void testGetFilesToUploadLastAccessDates() {
        assertNotNull("setup fails", fileSystemHandler);
        assertNotNull("filesToUploadLastAccessDates map shouldn't be null", fileSystemHandler
                .getFilesToUploadLastAccessDates());
    }

    /**
     * Test getTimeOutTransferSessionsTimer method, return the instance setted in constructor.
     */
    public void testGetTimeOutTransferSessionsTimer() {
        assertNotNull("setup fails", fileSystemHandler);
        assertNotNull("timeOutTransferSessionsTimer map shouldn't be null", fileSystemHandler
                .getTimeOutTransferSessionsTimer());
    }

    /**
     * Test getTimeOutTransferSessionsTask method, return the instance setted in constructor.
     */
    public void testGetTimeOutTransferSessionsTask() {
        assertNotNull("setup fails", fileSystemHandler);
        assertNotNull("timeOutTransferSessionsTask map shouldn't be null", fileSystemHandler
                .getTimeOutTransferSessionsTask());
    }

    /**
     * Test getTransferSessionTimeOut and setTransferSessionTimeOut methods.
     */
    public void testGetAndSetTransferSessionTimeOut() {
        assertNotNull("setup fails", fileSystemHandler);
        long timeout = 1200;
        assertFalse("be sure not equal to timeout value to set",
                fileSystemHandler.getTransferSessionTimeOut() == timeout);
        fileSystemHandler.setTransferSessionTimeOut(timeout);
        assertTrue("be sure equal to timeout value now", fileSystemHandler.getTransferSessionTimeOut() == timeout);
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileSystemHandlerTestCase.class);
    }
}
