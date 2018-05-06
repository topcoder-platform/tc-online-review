/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.search;

import java.util.List;

import com.topcoder.file.transfer.accuracytests.AccuracyTestHelper;
import com.topcoder.file.transfer.accuracytests.registry.FileSystemXmlRegistryAccuracyTest;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.FileIdGroupSearcher;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy test for FileIdGroupSearcher class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class FileIdGroupSearcherAccuracyTest extends TestCase {
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
     * Test ctor FileIdGroupSearcher(FileSystemRegistry registry), an instance
     * with the supplied registry should be created.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create an instance of FileIdGroupSearcher",
                new FileIdGroupSearcher(registry));
    }

    /**
     * <p>
     * Test getGroups(String criteria), the groups match the criteria, should be
     * returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetGroups() throws Exception {
        FileIdGroupSearcher searcher = new FileIdGroupSearcher(registry);
        List groups = searcher.getGroups("4");
        assertEquals("Failed to get group name", 2, groups.size());
        assertTrue("Failed to get group name", groups.contains("group1"));
        assertTrue("Failed to get group name", groups.contains("group3"));
    }
}