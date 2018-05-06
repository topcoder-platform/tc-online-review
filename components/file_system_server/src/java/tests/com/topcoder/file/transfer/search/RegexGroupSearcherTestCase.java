/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.io.File;
import java.util.ArrayList;
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
 * Tests functionality of RegexGroupSearcher. All consturctors and methods are tested.
 * </p>
 * @author FireIce
 * @version 1.0
 */
public class RegexGroupSearcherTestCase extends TestCase {

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
     * Represents the RegexGroupSearcher instance used in tests.
     */
    private RegexGroupSearcher regexGroupSearcher;

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        registry = getFileSystemRegistry();
        registry.createGroup("aaaa", new ArrayList());
        registry.createGroup("aabb", new ArrayList());
        registry.createGroup("bbaa", new ArrayList());
        registry.createGroup("bbbb", new ArrayList());
        regexGroupSearcher = new RegexGroupSearcher(registry);
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        registry.removeGroup("aaaa");
        registry.removeGroup("aabb");
        registry.removeGroup("bbaa");
        registry.removeGroup("bbbb");
        registry = null;
        regexGroupSearcher = null;
    }

    /**
     * Tests Implementation.
     */
    public void testImplementation() {
        assertTrue("this class should implements GroupSearcher interface", regexGroupSearcher instanceof GroupSearcher);
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
     * Tests <code>RegexGroupSearcher(FileSystemRegistry)</code> constructor, if the argument is null, throw
     * NullPointerException.
     */
    public void testCtorFileSystemRegistryNPE() {
        try {
            new RegexGroupSearcher(null);
            fail("if the argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>RegexGroupSearcher(FileSystemRegistry)</code> constructor, while the argument is valid.
     */
    public void testCtorFileSystemRegistrySuccess() {
        new RegexGroupSearcher(registry);
    }

    /**
     * Tests <code>getFiles()</code> method, if the argument is null, throw NullPointerException.
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFilesNullPointerException() throws Exception {
        assertNotNull("setup fails", regexGroupSearcher);
        try {
            regexGroupSearcher.getGroups(null);
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
        FileSystemRegistry fileSystemRegistry = new MockFileSystemXmlRegistry3(FILES_FILE, GROUPS_FILE, idGenerator);
        GroupSearcher groupSearcher = new RegexGroupSearcher(fileSystemRegistry);
        try {
            groupSearcher.getGroups("regex");
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
        assertNotNull("setup fails", regexGroupSearcher);
        List result = regexGroupSearcher.getGroups(".*");
        assertTrue("should have all four groups", result.size() == 4);
        assertTrue("should containe fileId aaaa", result.contains("aaaa"));
        assertTrue("should containe fileId aabb", result.contains("aabb"));
        assertTrue("should containe fileId bbbb", result.contains("bbbb"));
        assertTrue("should containe fileId bbaa", result.contains("bbaa"));
        result = regexGroupSearcher.getGroups("a+b*");
        assertTrue("should have all three fileIds", result.size() == 2);
        assertTrue("should containe fileId aaaa", result.contains("aaaa"));
        assertTrue("should containe fileId aabb", result.contains("aabb"));
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(RegexGroupSearcherTestCase.class);
    }
}
