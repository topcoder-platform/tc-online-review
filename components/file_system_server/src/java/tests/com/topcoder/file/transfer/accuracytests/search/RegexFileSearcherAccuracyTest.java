/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.search;

import java.util.List;

import com.topcoder.file.transfer.accuracytests.AccuracyTestHelper;
import com.topcoder.file.transfer.accuracytests.registry.FileSystemXmlRegistryAccuracyTest;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.FileSearcher;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy test for RegexFileSearcher class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class RegexFileSearcherAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents an instance of FileSystemXmlRegistry.
     * </p>
     */
    private FileSystemXmlRegistry registry;

    /**
     * Rerepsents an instance of IDGenerator.
     */
    private IDGenerator idGenerator;

    /**
     * <p>
     * Set up for each test.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        AccuracyTestHelper.addConfigFiles();

        idGenerator = IDGeneratorFactory
                .getIDGenerator(AccuracyTestHelper.IDGENERATOR_NAMESPACE);
        registry = new FileSystemXmlRegistry(
                FileSystemXmlRegistryAccuracyTest.FILES_FILE,
                FileSystemXmlRegistryAccuracyTest.GROUPS_FILE, idGenerator);
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
     * Test ctor RegexFileSearcher(FileSystemRegistry registry), an instance
     * with the supplied registry should be created.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create instance of RegexFileSearcher",
                new RegexFileSearcher(registry));
    }

    /**
     * <p>
     * Test getFiles(String criteria), the files match the criteria, should be
     * returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetFiles() throws Exception {
        FileSearcher searcher = new RegexFileSearcher(registry);
        List files = searcher.getFiles(".*txt");
        assertEquals("Failed to get the correct result", 3, files.size());
        assertTrue("Failed to get the correct result", files.contains("1"));
        assertTrue("Failed to get the correct result", files.contains("2"));
        assertTrue("Failed to get the correct result", files.contains("3"));
    }
}