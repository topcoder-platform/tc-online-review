/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.search;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.topcoder.file.transfer.accuracytests.AccuracyTestHelper;
import com.topcoder.file.transfer.accuracytests.registry.FileSystemXmlRegistryAccuracyTest;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy test for SearchManager class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class SearchManagerAccuracyTest extends TestCase {
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
     * Represents an instance of search manager.
     * </p>
     */
    private SearchManager manager;

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

        fileSearcher = new RegexFileSearcher(registry);
        groupSearcher = new RegexGroupSearcher(registry);

        Map fileSearchers = new Hashtable();
        Map groupSearchers = new Hashtable();
        fileSearchers.put("file", fileSearcher);
        groupSearchers.put("group", groupSearcher);
        manager = new SearchManager(fileSearchers, groupSearchers);
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
     * Test ctor SearchManager(), instance with empty fileSearchers and
     * groupSearchers will be created.
     * </p>
     */
    public void testCtor1() {
        SearchManager manager = new SearchManager();
        assertNotNull("Failed to create an instance of SearchManager", manager);
        assertTrue("Failed to create an instance of SearchManager",
                0 == manager.getFileSearcherNames().size());
        assertTrue("Failed to create an instance of SearchManager",
                0 == manager.getGroupSearcherNames().size());
    }

    /**
     * <p>
     * Test ctor SearchManager(Map fileSearchers, Map groupSearchers), instance
     * should be created.
     * </p>
     */
    public void testCtor2() {
        assertNotNull("Failed to create an instance of SearchManager", manager);
        assertTrue("Failed to create an instance of SearchManager",
                1 == manager.getFileSearcherNames().size());
        assertTrue("Failed to create an instance of SearchManager",
                1 == manager.getGroupSearcherNames().size());
        assertEquals("Failed to create an instance of SearchManager",
                fileSearcher, manager.getFileSearcher("file"));
        assertEquals("Failed to create an instance of SearchManager",
                groupSearcher, manager.getGroupSearcher("group"));
    }

    /**
     * <p>
     * Test getFiles(String fileSearcherName, String criteria), the files
     * matched the criteria should be returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetFiles() throws Exception {
        List files = manager.getFiles("file", ".*txt");
        assertEquals("Failed to get the correct result", 3, files.size());
        assertTrue("Failed to get the correct result", files.contains("1"));
        assertTrue("Failed to get the correct result", files.contains("2"));
        assertTrue("Failed to get the correct result", files.contains("3"));
    }

    /**
     * <p>
     * Test getGroups(String groupSearcherName, String criteria), the group
     * matched the criteria should be returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetGroups() throws Exception {
        List groups = manager.getGroups("group", ".*3");
        assertEquals("Failed to get the groups", 1, groups.size());
        assertTrue("Failed to get the groups", groups.contains("group3"));
    }

    /**
     * <p>
     * Test addFileSearcher(String fileSearcherName, FileSearcher fileSearcher),
     * the file searcher should be added.
     * </p>
     */
    public void testAddFileSearcher() {
        assertNull("the file searcher should not eixsted", manager
                .getFileSearcher("add"));
        manager.addFileSearcher("add", fileSearcher);
        assertEquals("Failed to add the groups", fileSearcher, manager
                .getFileSearcher("add"));
    }

    /**
     * <p>
     * Test removeFileSearcher(String fileSearcherName), the file searcher
     * should be removed.
     * </p>
     */
    public void testRemoveFileSearcher() {
        assertNotNull("the file searcher should not eixsted", manager
                .getFileSearcher("file"));
        manager.removeFileSearcher("file");
        assertNull("the file searcher should not eixsted", manager
                .getFileSearcher("file"));
    }

    /**
     * <p>
     * Test removeAllFileSearchers(), all file searcher should be removed.
     * </p>
     */
    public void testRemoveAllFileSearchers() {
        assertTrue("The searcher Manager should contians som file manager",
                manager.getFileSearcherNames().size() > 0);
        manager.removeAllFileSearchers();
        assertEquals("The searcher Manager should contians som file manager",
                0, manager.getFileSearcherNames().size());
    }

    /**
     * <p>
     * Test getFileSearcherNames(), all names of file search should returned.
     * </p>
     */
    public void testGetFileSearcherNames() {
        List names = manager.getFileSearcherNames();
        assertEquals("The returned name list is not correct", 1, names.size());
        assertTrue("The returned name list is not correct", names
                .contains("file"));
    }

    /**
     * <p>
     * Test getFileSearcher(String fileSearcherName), the filesearcher with the
     * name should be returned.
     * </p>
     */
    public void testGetFileSearcher() {
        assertNull("The file searcher returned is not correct", manager
                .getFileSearcher("group"));
        assertEquals("The file searcher returned is not correct", fileSearcher,
                manager.getFileSearcher("file"));
    }

    /**
     * <p>
     * Test addGroupSearcher(String groupSearcherName, GroupSearcher
     * groupSearcher), add a group searcher then check it.
     * </p>
     */
    public void testAddGroupSearcher() {
        assertNull("The group seacher should not existed", manager
                .getGroupSearcher("add"));
        manager.addGroupSearcher("add", groupSearcher);
        assertEquals("The group seacher should be added", groupSearcher,
                manager.getGroupSearcher("add"));
    }

    /**
     * <p>
     * Test removeGroupSearcher(String groupSearcherName), remove a group and
     * then test it removed correctly.
     * </p>
     */
    public void testRemoveGroupSearcher() {
        assertNotNull("The group seacher should exist", manager
                .getGroupSearcher("group"));
        manager.removeGroupSearcher("group");
        assertNull("The group seacher should exist", manager
                .getGroupSearcher("group"));
    }

    /**
     * <p>
     * Test removeAllGroupSearchers(), all group searcher should be removed.
     * </p>
     */
    public void testRemoveAllGroupSearchers() {
        assertTrue("The group seacher should exist", manager
                .getGroupSearcherNames().size() > 0);
        manager.removeGroupSearcher("group");
        assertEquals("The group seacher should be removed", 0, manager
                .getGroupSearcherNames().size());
    }

    /**
     * <p>
     * Test getGroupSearcherNames(), all names of group searcher should be
     * returned.
     * </p>
     */
    public void testGetGroupSearcherNames() {
        List names = manager.getGroupSearcherNames();
        assertEquals("The returned group searcher names is not correctly.", 1,
                names.size());
        assertEquals("The returned group searcher names is not correctly.",
                "group", names.get(0));
    }

    /**
     * <p>
     * Test getGroupSearcher(String groupSearcherName), the group searcher with
     * the name should be returned.
     * </p>
     */
    public void testGetGroupSearcher() {
        assertEquals("The returned group searcher names is not correctly.",
                groupSearcher, manager.getGroupSearcher("group"));
        assertNull("The returned group searcher names is not correctly.",
                manager.getGroupSearcher("file"));
    }
}