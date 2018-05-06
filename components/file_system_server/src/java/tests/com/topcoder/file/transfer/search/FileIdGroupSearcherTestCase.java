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
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests functionality of FileIdGroupSearcher. All consturctors and methods are tested.
 * </p>
 * @author FireIce
 * @version 1.0
 */
public class FileIdGroupSearcherTestCase extends TestCase {

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
     * Represents the FileIdGroupSearcher instance used in tests.
     */
    private FileIdGroupSearcher fileIdGroupSearcher;

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        registry = getFileSystemRegistry();
        registry.createGroup("group11", new ArrayList());
        registry.createGroup("group12", new ArrayList());
        registry.createGroup("group13", new ArrayList());
        registry.addFile("aaaa", "aaabb");
        registry.addFile("aabb", "aasdf");
        registry.addFileToGroup("group11", "aaaa");
        registry.addFileToGroup("group12", "aabb");
        registry.addFileToGroup("group13", "aaaa");
        registry.addFileToGroup("group13", "aabb");
        fileIdGroupSearcher = new FileIdGroupSearcher(registry);
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        registry.removeGroup("group11");
        registry.removeGroup("group12");
        registry.removeGroup("group13");
        registry.removeFile("aaaa");
        registry.removeFile("aabb");
        registry = null;
        fileIdGroupSearcher = null;
    }

    /**
     * Tests Implementation.
     */
    public void testImplementation() {
        assertTrue("should implements GroupSearcher interface", fileIdGroupSearcher instanceof GroupSearcher);
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
     * Tests <code>FileIdGroupSearcher(FileSystemRegistry)</code> constructor, if the argument is null, throw
     * NullPointerException.
     */
    public void testCtorFileSystemRegistryNPE() {
        try {
            new FileIdGroupSearcher(null);
            fail("if the argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>FileIdGroupSearcher(FileSystemRegistry)</code> constructor, while the argument is valid.
     */
    public void testCtorFileSystemRegistrySuccess() {
        new FileIdGroupSearcher(registry);
    }

    /**
     * Tests <code>getFiles()</code> method, if the argument is null, throw NullPointerException.
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetFilesNullPointerException() throws Exception {
        assertNotNull("setup fails", fileIdGroupSearcher);
        try {
            fileIdGroupSearcher.getGroups(null);
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
        FileSystemRegistry fileSystemRegistry = new MockFileSystemXmlRegistry(FILES_FILE, GROUPS_FILE, idGenerator);
        FileIdGroupSearcher searcher = new FileIdGroupSearcher(fileSystemRegistry);
        String criteria = "regex";
        try {
            searcher.getGroups(criteria);
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
        assertNotNull("setup fails", fileIdGroupSearcher);
        List result = fileIdGroupSearcher.getGroups("aaaa");
        assertTrue("should have all 2 groups", result.size() == 2);
        assertTrue("should containe group group11", result.contains("group11"));
        assertTrue("should containe group group13", result.contains("group13"));
        result = fileIdGroupSearcher.getGroups("aabb");
        assertTrue("should have all 2 groups", result.size() == 2);
        assertTrue("should containe group group12", result.contains("group12"));
        assertTrue("should containe group group13", result.contains("group13"));
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileIdGroupSearcherTestCase.class);
    }
}
