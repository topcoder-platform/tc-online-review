/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.search;

import java.util.List;

import junit.framework.TestCase;

import com.topcoder.file.transfer.accuracytests.AccuracyTestHelper;
import com.topcoder.file.transfer.accuracytests.registry.FileSystemXmlRegistryAccuracyTest;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * <p>
 * Accuracy test for RegexGroupSearcher class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class RegexGroupSearcherAccuracyTest extends TestCase {
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
     * Test ctor RegexGroupSearcher(ileSystemRegistry registry), an instance
     * with the supplied registry should be created.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create instance of RegexGroupSearcher",
                new RegexGroupSearcher(registry));
    }

    /**
     * <p>
     * Test getGroups(String criteria), the files match the criteria, should be
     * returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetGroups() throws Exception {
        RegexGroupSearcher searcher = new RegexGroupSearcher(registry);
        List groups = searcher.getGroups("group.*");
        assertEquals("Failed to get the groups", 3, groups.size());
        assertTrue("Failed to get the groups", groups.contains("group1"));
        assertTrue("Failed to get the groups", groups.contains("group2"));
        assertTrue("Failed to get the groups", groups.contains("group3"));

        groups = searcher.getGroups(".*3");
        assertEquals("Failed to get the groups", 1, groups.size());
        assertTrue("Failed to get the groups", groups.contains("group3"));
    }
}