/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test SearchManager for correctness.
 *
 * @author FireIce
 * @version 1.0
 */
public class SearchManagerTestCase extends TestCase {

    /**
     * Represents the xml file for files.
     */
    private static final File FILES_FILE = new File("test_files/valid_files.xml");

    /**
     * Represents the xml file for files.
     */
    private static final File GROUPS_FILE = new File("test_files/valid_groups.xml");

    /**
     * Represents the FileSystemRegistry instance.
     */
    private FileSystemRegistry registry;

    /**
     * Represents the SearchManager instance used in tests.
     */
    private SearchManager searchManager;

    /**
     * create FileSystemRegistry instance.
     *
     * @return the FIleSYstemRegistry instance.
     * @throws Exception
     *             if any Exception occur.
     */
    private FileSystemRegistry getFileSystemRegistry() throws Exception {
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator("unit_test_id_sequence");
        return new FileSystemXmlRegistry(FILES_FILE, GROUPS_FILE, idGenerator);
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
        registry = getFileSystemRegistry();
        searchManager = new SearchManager();
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
        registry = null;
    }

    /**
     * Test Constructor with no argument.
     */
    public void testCtor() {
        new SearchManager();
    }

    /**
     * Test <code>SearchManager(Map, Map)</code> constructor, if any argument is null, throw NullPointerException.
     */
    public void testTwoArgsCtorNPE() {
        Map emptyMap = new HashMap();
        try {
            new SearchManager(null, emptyMap);
            fail("if fileSearchers map is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new SearchManager(emptyMap, null);
            fail("if groupSearchers map is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>SearchManager(Map, Map)</code> constructor, if the keys or values of the fileSearchers map are
     * illegal, throw IllegalArgumentException.
     */
    public void testTwoArgsCtorIAE1() {
        Map emptyMap = new HashMap();
        FileSearcher fileSearcher = new RegexFileSearcher(registry);
        Map invalidMap = new HashMap();
        // add null key
        invalidMap.put(null, fileSearcher);
        try {
            new SearchManager(invalidMap, emptyMap);
            fail("if fileSearchers map contains null key, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
        // add non-String key
        invalidMap.put(new Object(), fileSearcher);
        try {
            new SearchManager(invalidMap, emptyMap);
            fail("if fileSearchers map contains non-String key, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
        // add empty string key
        invalidMap.put(" ", fileSearcher);
        try {
            new SearchManager(invalidMap, emptyMap);
            fail("if fileSearchers map contains empty String key, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
        // add null instance value
        invalidMap.put("fileSearcherName", null);
        try {
            new SearchManager(invalidMap, emptyMap);
            fail("if fileSearchers map contains null value, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
        // add non-FileSearcher instance value
        invalidMap.put("fileSearcherName", new Object());
        try {
            new SearchManager(invalidMap, emptyMap);
            fail("if fileSearchers map contains non-FileSearcher instance value, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
    }

    /**
     * Test <code>SearchManager(Map, Map)</code> constructor, if the keys or values of the groupSearchers map are
     * illegal, throw IllegalArgumentException.
     */
    public void testTwoArgsCtorIAE2() {
        Map emptyMap = new HashMap();
        GroupSearcher groupSearcher = new RegexGroupSearcher(registry);
        Map invalidMap = new HashMap();
        // add null key
        invalidMap.put(null, groupSearcher);
        try {
            new SearchManager(emptyMap, invalidMap);
            fail("if groupSearchers map contains null key, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
        // add non-String key
        invalidMap.put(new Object(), groupSearcher);
        try {
            new SearchManager(emptyMap, invalidMap);
            fail("if groupSearchers map contains non-String key, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
        // add empty string key
        invalidMap.put(" ", groupSearcher);
        try {
            new SearchManager(emptyMap, invalidMap);
            fail("if groupSearchers map contains empty String key, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
        // add null instance value
        invalidMap.put("groupSearcherName", null);
        try {
            new SearchManager(emptyMap, invalidMap);
            fail("if groupSearchers map contains null value, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
        // add non-FileSearcher instance value
        invalidMap.put("groupSearcherName", new Object());
        try {
            new SearchManager(emptyMap, invalidMap);
            fail("if groupSearchers map contains non-GroupSearcher instance value, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        invalidMap.clear();
    }

    /**
     * Test <code>getFiles(String,String)</code> method, if any argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFilesNPE() throws Exception {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getFiles(null, "valid");
            fail("if fileSearcherName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            searchManager.getFiles("valid", null);
            fail("if criteria is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>getFiles(String,String)</code> method, if any argument is empty, throw IllegalArgumentException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFilesIAE() throws Exception {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getFiles(" ", "valid");
            fail("if fileSearcherName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        // the second argument's emptyness is allowed
    }

    /**
     * Test <code>getFiles(String,String)</code> method, if there is no file searcher associated with the file
     * searcher name, throw FileSearcherNotFoundException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFilesFileSearcherNotFoundException() throws Exception {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getFiles("notExist", "valid");
            fail("if there is no entry with the fileSearcherName, throw FileSearcherNotFoundException");
        } catch (FileSearcherNotFoundException e) {
            // good
        }
    }

    /**
     * Test <code>getGroups(String,String)</code> method, if any argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetGroupsNPE() throws Exception {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getGroups(null, "valid");
            fail("if groupSearcherName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            searchManager.getGroups("valid", null);
            fail("if criteria is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>getGroups(String,String)</code> method, if any argument is empty, throw IllegalArgumentException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetGroupsIAE() throws Exception {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getGroups(" ", "valid");
            fail("if groupSearcherName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        // the second argument's emptyness is allowed
    }

    /**
     * Test <code>getGroups(String,String)</code> method, if there is no group searcher associated with the file
     * searcher name, throw GroupSearcherNotFoundException.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetGroupsGroupSearcherNotFoundException() throws Exception {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getGroups("notExist", "valid");
            fail("if there is no entry with the groupSearcherName, throw GroupSearcherNotFoundException");
        } catch (GroupSearcherNotFoundException e) {
            // good
        }
    }

    /**
     * Test <code>addFileSearcher(String, FileSearcher)</code> method, if any argument is null, throw
     * NullPointerException.
     */
    public void testAddFileSearcherNPE() {
        assertNotNull("setup fails", searchManager);
        FileSearcher fileSearcher = new RegexFileSearcher(registry);
        try {
            searchManager.addFileSearcher(null, fileSearcher);
            fail("if any argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>addFileSearcher(String, FileSearcher)</code> method, if the string argument is empty, throw
     * IllegalArgumentException.
     */
    public void testAddFileSearcherIAE() {
        assertNotNull("setup fails", searchManager);
        FileSearcher fileSearcher = new RegexFileSearcher(registry);
        try {
            searchManager.addFileSearcher("  ", fileSearcher);
            fail("if the string argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>addFileSearcher(String, FileSearcher)</code> method, if the arguments are valid, successfully
     * added.
     */
    public void testAddFileSearcherSuccess() {
        assertNotNull("setup fails", searchManager);
        FileSearcher fileSearcher = new RegexFileSearcher(registry);
        searchManager.addFileSearcher("UnitTestFileSearcher", fileSearcher);
        assertSame("should return the fileSearcher previously added", fileSearcher, searchManager
                .getFileSearcher("UnitTestFileSearcher"));
    }

    /**
     * Test <code>removeFileSearcher(String)</code> method, if the argument is null, throw NullPointerException.
     */
    public void testRemoveFileSearcherNPE() {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.removeFileSearcher(null);
            fail("if any argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>removeFileSearcher(String)</code> method, if the string argument is empty, throw
     * IllegalArgumentException.
     */
    public void testRemoveFileSearcherIAE() {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.removeFileSearcher("  ");
            fail("if the string argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>removeFileSearcher(String)</code> method, if the arguments are valid, successfully added.
     */
    public void testRemoveFileSearcherSuccess() {
        assertNotNull("setup fails", searchManager);
        FileSearcher fileSearcher = new RegexFileSearcher(registry);
        searchManager.addFileSearcher("UnitTestFileSearcher", fileSearcher);
        assertSame("should return the fileSearcher previously added", fileSearcher, searchManager
                .getFileSearcher("UnitTestFileSearcher"));
        searchManager.removeFileSearcher("UnitTestFileSearcher");
        assertNull("that fileSearcher shouldn't exist", searchManager.getFileSearcher("UnitTestFileSearcher"));
    }

    /**
     * Test <code>removeAllFileSearchers()</code> method.
     */
    public void testRemoveAllFileSearchers() {
        assertNotNull("setup fails", searchManager);
        FileSearcher fileSearcher = new RegexFileSearcher(registry);
        searchManager.addFileSearcher("UnitTestFileSearcher", fileSearcher);
        assertFalse("the fileSearchers map shouldn't be empty", searchManager.getFileSearcherNames().isEmpty());
        searchManager.removeAllFileSearchers();
        assertTrue("the fileSearchers map should be empty", searchManager.getFileSearcherNames().isEmpty());
    }

    /**
     * Test <code>getFileSearcherNames()</code> method.
     */
    public void testGetFileSearcherNames() {
        assertNotNull("setup fails", searchManager);
        FileSearcher fileSearcher = new RegexFileSearcher(registry);
        searchManager.addFileSearcher("UnitTestFileSearcher", fileSearcher);
        List names = searchManager.getFileSearcherNames();
        assertTrue("should contains one element", names.size() == 1);
        assertTrue("should contains UnitTestFileSearcher name", names.contains("UnitTestFileSearcher"));
        assertNotSame("always return a copy", searchManager.getFileSearcherNames(), searchManager
                .getFileSearcherNames());
    }

    /**
     * Test <code>getFileSearcher(String)</code> method, if the argument is null, throw NullPointerException.
     */
    public void testGetFileSearcherNPE() {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getFileSearcher(null);
            fail("if any argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>getFileSearcher(String)</code> method, if the string argument is empty, throw
     * IllegalArgumentException.
     */
    public void testGetFileSearcherIAE() {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getFileSearcher("  ");
            fail("if the string argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>getFileSearcher(String)</code> method, if the arguments are valid, successfully added.
     */
    public void testGetFileSearcherSuccess() {
        assertNotNull("setup fails", searchManager);
        assertNull("if no pairing fileSearcher, should return null", searchManager.getFileSearcher("NoPair"));
        FileSearcher fileSearcher = new RegexFileSearcher(registry);
        searchManager.addFileSearcher("UnitTestFileSearcher", fileSearcher);
        assertSame("should return the fileSearcher previously added", fileSearcher, searchManager
                .getFileSearcher("UnitTestFileSearcher"));
    }

    // -----------------------------------------------------------------------------------------------------
    /**
     * Test <code>addGroupSearcher(String, GroupSearcher)</code> method, if any argument is null, throw
     * NullPointerException.
     */
    public void testAddGroupSearcherNPE() {
        assertNotNull("setup fails", searchManager);
        GroupSearcher groupSearcher = new RegexGroupSearcher(registry);
        try {
            searchManager.addGroupSearcher(null, groupSearcher);
            fail("if any argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>addGroupSearcher(String, GroupSearcher)</code> method, if the string argument is empty, throw
     * IllegalArgumentException.
     */
    public void testAddGroupSearcherIAE() {
        assertNotNull("setup fails", searchManager);
        GroupSearcher groupSearcher = new RegexGroupSearcher(registry);
        try {
            searchManager.addGroupSearcher("  ", groupSearcher);
            fail("if the string argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>addGroupSearcher(String, GroupSearcher)</code> method, if the arguments are valid, successfully
     * added.
     */
    public void testAddGroupSearcherSuccess() {
        assertNotNull("setup fails", searchManager);
        GroupSearcher groupSearcher = new RegexGroupSearcher(registry);
        searchManager.addGroupSearcher("UnitTestGroupSearcher", groupSearcher);
        assertSame("should return the groupSearcher previously added", groupSearcher, searchManager
                .getGroupSearcher("UnitTestGroupSearcher"));
    }

    /**
     * Test <code>removeGroupSearcher(String)</code> method, if the argument is null, throw NullPointerException.
     */
    public void testRemoveGroupSearcherNPE() {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.removeGroupSearcher(null);
            fail("if any argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>removeGroupSearcher(String)</code> method, if the string argument is empty, throw
     * IllegalArgumentException.
     */
    public void testRemoveGroupSearcherIAE() {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.removeGroupSearcher("  ");
            fail("if the string argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>removeGroupSearcher(String)</code> method, if the arguments are valid, successfully added.
     */
    public void testRemoveGroupSearcherSuccess() {
        assertNotNull("setup fails", searchManager);
        GroupSearcher groupSearcher = new RegexGroupSearcher(registry);
        searchManager.addGroupSearcher("UnitTestGroupSearcher", groupSearcher);
        assertSame("should return the groupSearcher previously added", groupSearcher, searchManager
                .getGroupSearcher("UnitTestGroupSearcher"));
        searchManager.removeGroupSearcher("UnitTestGroupSearcher");
        assertNull("that groupSearcher shouldn't exist", searchManager.getGroupSearcher("UnitTestGroupSearcher"));
    }

    /**
     * Test <code>removeAllGroupSearchers()</code> method.
     */
    public void testRemoveAllGroupSearchers() {
        assertNotNull("setup fails", searchManager);
        GroupSearcher groupSearcher = new RegexGroupSearcher(registry);
        searchManager.addGroupSearcher("UnitTestGroupSearcher", groupSearcher);
        assertFalse("the groupSearchers map shouldn't be empty", searchManager.getGroupSearcherNames().isEmpty());
        searchManager.removeAllGroupSearchers();
        assertTrue("the groupSearchers map should be empty", searchManager.getGroupSearcherNames().isEmpty());
    }

    /**
     * Test <code>getGroupSearcherNames()</code> method.
     */
    public void testGetGroupSearcherNames() {
        assertNotNull("setup fails", searchManager);
        GroupSearcher groupSearcher = new RegexGroupSearcher(registry);
        searchManager.addGroupSearcher("UnitTestGroupSearcher", groupSearcher);
        List names = searchManager.getGroupSearcherNames();
        assertTrue("should contains one element", names.size() == 1);
        assertTrue("should contains UnitTestGroupSearcher name", names.contains("UnitTestGroupSearcher"));
        assertNotSame("always return a copy", searchManager.getGroupSearcherNames(), searchManager
                .getGroupSearcherNames());
    }

    /**
     * Test <code>getGroupSearcher(String)</code> method, if the argument is null, throw NullPointerException.
     */
    public void testGetGroupSearcherNPE() {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getGroupSearcher(null);
            fail("if any argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>getGroupSearcher(String)</code> method, if the string argument is empty, throw
     * IllegalArgumentException.
     */
    public void testGetGroupSearcherIAE() {
        assertNotNull("setup fails", searchManager);
        try {
            searchManager.getGroupSearcher("  ");
            fail("if the string argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>getGroupSearcher(String)</code> method, if the arguments are valid, successfully added.
     */
    public void testGetGroupSearcherSuccess() {
        assertNotNull("setup fails", searchManager);
        assertNull("if no pairing groupSearcher, should return null", searchManager.getGroupSearcher("NoPair"));
        GroupSearcher groupSearcher = new RegexGroupSearcher(registry);
        searchManager.addGroupSearcher("UnitTestGroupSearcher", groupSearcher);
        assertSame("should return the fileSearcher previously added", groupSearcher, searchManager
                .getGroupSearcher("UnitTestGroupSearcher"));
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     *
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(SearchManagerTestCase.class);
    }
}
