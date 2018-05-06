/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;


/**
 * <p>
 * Test creation, extraction and examination functionality of the <code>ZipArchiver</code> implementation.
 * </p>
 *
 * @author visualage
 * @version 2.0
 */
public class ZipArchiverTestCase extends AbstractArchiverTestCase {
    /**
     * <p>
     * Return a new <code>ZipArchiver</code> instance.
     * </p>
     *
     * @return a new <code>Archiver</code> instance
     */
    Archiver getArchiverInstance() {
        return new ZipArchiver();
    }

    /**
     * <p>
     * Return a valid ZIP archive.
     * </p>
     *
     * @return a valid ZIP archive
     */
    File getValidArchiveFile() {
        return new File("test_files", "somezip.zip");
    }

    /**
     * <p>
     * Test <code>createNameFromFile</code> with <code>null</code> as argument.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateNameFromFileNull() throws Exception {
        try {
            ZipCreator.createNameFromFile(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test accuracy of <code>createNameFromFile</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateNameFromFileAccuracy() throws Exception {
        // Test relative path
        assertEquals(ZipCreator.createNameFromFile(new File("test_files", "dir1")), "test_files/dir1");

        // Test empty path
        assertEquals(ZipCreator.createNameFromFile(new File("")), "");
    }

    /**
     * <p>
     * Test <code>createFileFromName</code> with <code>null</code> as argument.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileFromNameNull() throws Exception {
        try {
            ZipExtractor.createFileFromName(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test accuracy of <code>createFileFromName</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileFromNameAccuracy() throws Exception {
        // Test relative path
        assertEquals(ZipExtractor.createFileFromName("test_files/dir1/dir2").getPath(),
            "test_files" + File.separator + "dir1" + File.separator + "dir2");

        // Test empty path
        assertEquals(ZipExtractor.createFileFromName("").getPath(), "");
    }

    /**
     * <p>
     * Return a new <code>TestSuite</code> containing all test cases in this class.
     * </p>
     *
     * @return a new <code>TestSuite</code> containing all test cases in this class
     */
    public static Test suite() {
        return new TestSuite(ZipArchiverTestCase.class);
    }
}




