/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * This is a demo of the archiving part in this component.
 * </p>
 *
 * @author visualage
 * @version 2.0
 */
public class DemoTestCase extends TestCase {
    /**
     * <p>
     * List of temporary test files to be deleted.
     * </p>
     */
    private ArrayList filesToDelete;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     */
    protected void setUp() {
        filesToDelete = new ArrayList();
    }

    /**
     * <p>
     * Clean up the test environment.
     * </p>
     */
    protected void tearDown() {
        Iterator iter = filesToDelete.iterator();

        while (iter.hasNext()) {
            File file = (File) iter.next();

            ArchiverTestUtil.deleteRecursive(file);
        }
    }

    /**
     * <p>
     * A demo of the usage of archiving.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testDemo() throws Exception {
        File archive = new File("test_files", "demo.zip");
        filesToDelete.add(archive);
        archive.deleteOnExit();

        // Create an archive utility instance, using Zip as the format
        ArchiveUtility au = new ArchiveUtility(archive, new ZipArchiver());

        // Create the archive file, from a directory
        au.createArchive(new File("test_files", "stresstests"));

        // List the contents
        List archivedFiles = au.listContents();

        // Get the archive file again
        assertEquals(archive, au.getFile());

        // Extract selected files
        List extractFiles = new ArrayList();
        File extractDir = new File("test_files", "extract");
        assertTrue(extractDir.mkdir());
        filesToDelete.add(extractDir);
        extractDir.deleteOnExit();

        extractFiles.add(new File(new File("archive1", "dir1"), "build.xml"));
        extractFiles.add(new File(new File(new File("archive1", "dir1"), "dir2"), "build.xml"));
        au.extractContents(extractDir, extractFiles);
    }

    /**
     * <p>
     * Return a new <code>TestSuite</code> containing all test cases in this class.
     * </p>
     *
     * @return a new <code>TestSuite</code> containing all test cases in this class
     */
    public static Test suite() {
        return new TestSuite(DemoTestCase.class);
    }
}




