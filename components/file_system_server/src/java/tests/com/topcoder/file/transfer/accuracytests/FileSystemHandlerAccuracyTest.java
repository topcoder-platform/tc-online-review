/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests;

import java.util.Hashtable;
import java.util.Map;

import com.topcoder.file.transfer.FileSystemHandler;
import com.topcoder.file.transfer.accuracytests.registry.FileSystemXmlRegistryAccuracyTest;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;
import com.topcoder.processor.ipserver.Handler;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy test for FileSystemHandler class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class FileSystemHandlerAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents the maximum number of requests.
     * </p>
     */
    private static int MAX_CONNECTIONS = 10;

    /**
     * <p>
     * An instance of FileSystemRegistry implementation.
     * </p>
     */
    private FileSystemRegistry registry;

    /**
     * <p>
     * Represents an instance of FilePersistence implementation.
     * </p>
     */
    private FilePersistence persistence;

    /**
     * <p>
     * Represents an instance of FilePersistence implementation.
     * </p>
     */
    private ObjectValidator validator;

    /**
     * <p>
     * Represents an instance of SearchManager.
     * </p>
     */
    private SearchManager manager;

    /**
     * Rerepsents an instance of IDGenerator.
     */
    private IDGenerator idGenerator;

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
     * Represents an instance of FileSystemHandler.
     * </p>
     */
    private FileSystemHandler handler;

    /**
     * <p>
     * Set up for each test.
     * </p>
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        AccuracyTestHelper.addConfigFiles();
        idGenerator = IDGeneratorFactory
                .getIDGenerator(AccuracyTestHelper.IDGENERATOR_NAMESPACE);
        registry = new FileSystemXmlRegistry(
                FileSystemXmlRegistryAccuracyTest.FILES_FILE,
                FileSystemXmlRegistryAccuracyTest.GROUPS_FILE, idGenerator);

        fileSearcher = new RegexFileSearcher(registry);
        groupSearcher = new RegexGroupSearcher(registry);

        Map fileSearchers = new Hashtable();
        Map groupSearchers = new Hashtable();
        fileSearchers.put("file", fileSearcher);
        groupSearchers.put("group", groupSearcher);
        manager = new SearchManager(fileSearchers, groupSearchers);

        persistence = new FileSystemPersistence();

        validator = new UploadRequestValidator(
                new FreeDiskSpaceNativeChecker(
                        AccuracyTestHelper.SERVER_PATH));
        handler = new FileSystemHandler(MAX_CONNECTIONS, registry, persistence,
                AccuracyTestHelper.SERVER_PATH, validator, manager);
    }
    
    /**
     * <p>
     * Tear down for each test.
     * </p>
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.removeConfigFiles();
        
        super.tearDown();
    }

    /**
     * <p>
     * Test FileSystemHandler(int maxRequests, FileSystemRegistry registry,
     * FilePersistence persistence, String fileLocation, ObjectValidator
     * uploadRequestValidator, SearchManager searchManager), an instance with
     * the supplied arguments will be created.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testCtor() throws Exception {
        assertNotNull("Failed to create instance of FileSystemHandler", handler);
        assertEquals("Failed to create instance of FileSystemHandler",
                AccuracyTestHelper.SERVER_PATH, handler.getFileLocation());
        assertEquals("Failed to create instance of FileSystemHandler",
                persistence, handler.getPersistence());
        assertEquals("Failed to create instance of FileSystemHandler",
                registry, handler.getRegistry());
        assertEquals("Failed to create instance of FileSystemHandler", manager,
                handler.getSearchManager());
        assertEquals("Failed to create instance of FileSystemHandler",
                validator, handler.getUploadRequestValidator());
        assertEquals("Failed to create instance of FileSystemHandler",
                MAX_CONNECTIONS, handler.getMaxConnections());
    }

    /**
     * <p>
     * Test dispose(), simple call it.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testDispose() throws Exception {
        handler.dispose();
    }

    /**
     * <p>
     * Test setTransferSessionTimeOut(long timeOut),
     * </p>
     */
    public void testSetTransferSessionTimeOut() {
        handler.setTransferSessionTimeOut(10000);
        assertEquals("Failed to set the time out", 10000, handler
                .getTransferSessionTimeOut());
    }

    /**
     * Test FileSystemHandler is extended from Handler.
     */
    public void testInheritence() {
        assertTrue("FileSystemHandler should extends Handler class",
                handler instanceof Handler);
    }
}