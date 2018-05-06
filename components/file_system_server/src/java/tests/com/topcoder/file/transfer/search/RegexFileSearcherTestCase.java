/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.io.File;
import java.util.List;

import com.topcoder.file.transfer.ConfigHelper;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.idgenerator.IDGenerator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests functionality of RegexFileSearcher. All consturctors and methods are tested.
 * </p>
 * @author FireIce
 * @version 1.0
 */
public class RegexFileSearcherTestCase extends TestCase {

    /**
     * Represents the xml file for files.
     */
    private static final File FILES_FILE = new File("test_files/valid_files1.xml");

    /**
     * Represents the xml file for files.
     */
    private static final File GROUPS_FILE = new File("test_files/valid_groups1.xml");

    /**
     * Represents the FileSystemRegistry instance.
     */
    private FileSystemRegistry registry;

    /**
     * Represents the RegexFileSearcher instance used in tests.
     */
    private RegexFileSearcher regexFileSearcher;

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        registry = getFileSystemRegistry();
        registry.addFile("aaa", "aaa");
        registry.addFile("aabb", "aabb");
        registry.addFile("aaaaa", "aaaaa");
        registry.addFile("baabb", "baabbb");
        regexFileSearcher = new RegexFileSearcher(registry);
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        registry.removeFile("aaa");
        registry.removeFile("aabb");
        registry.removeFile("aaaaa");
        registry.removeFile("baabb");
        registry = null;
        regexFileSearcher = null;
    }

    /**
     * Tests Implementation.
     */
    public void testImplementation() {
        assertTrue("this class should implements FileSearcher interface", regexFileSearcher instanceof FileSearcher);
    }

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
     * Tests <code>RegexFileSearcher(FileSystemRegistry)</code> constructor, if the argument is null, throw
     * NullPointerException.
     */
    public void testCtorFileSystemRegistryNPE() {
        try {
            new RegexFileSearcher(null);
            fail("if the argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>RegexFileSearcher(FileSystemRegistry)</code> constructor, while the argument is valid.
     */
    public void testCtorFileSystemRegistrySuccess() {
        new RegexFileSearcher(registry);
    }

    /**
     * Tests <code>getFiles()</code> method, if the argument is null, throw NullPointerException.
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFilesNullPointerException() throws Exception {
        assertNotNull("setup fails", regexFileSearcher);
        try {
            regexFileSearcher.getFiles(null);
            fail("if the argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>getFiles()</code> method, if an exception occurs while performing the search, throw
     * SearchException.
     * @throws Exception
     *             if any exception occurs.
     */
    public void testGetFilesSearchException() throws Exception {
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator(ConfigHelper.IDGENERATOR_NAMESPACE);
        FileSystemRegistry fileSystemRegistry = new MockFileSystemXmlRegistry2(FILES_FILE, GROUPS_FILE, idGenerator);
        FileSearcher searcher = new RegexFileSearcher(fileSystemRegistry);
        String criteria = "regex";
        try {
            searcher.getFiles(criteria);
            fail("if an exception occurs, throw SearchException");
        } catch (SearchException e) {
            // good
        }
    }

    /**
     * Tests <code>getFiles()</code> method, while performing the search successfully, return the fileId list.
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFilesSuccess() throws Exception {
        assertNotNull("setup fails", regexFileSearcher);
        List result = regexFileSearcher.getFiles(".*");
        assertTrue("should have all four fileIds", result.size() == 4);
        assertTrue("should containe fileId aaa", result.contains("aaa"));
        assertTrue("should containe fileId aabb", result.contains("aabb"));
        assertTrue("should containe fileId aaaaa", result.contains("aaaaa"));
        assertTrue("should containe fileId baabb", result.contains("baabb"));
        result = regexFileSearcher.getFiles("a*b*");
        assertTrue("should have all three fileIds", result.size() == 3);
        assertTrue("should containe fileId aaa", result.contains("aaa"));
        assertTrue("should containe fileId aabb", result.contains("aabb"));
        assertTrue("should containe fileId aaaaa", result.contains("aaaaa"));
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(RegexFileSearcherTestCase.class);
    }
}
