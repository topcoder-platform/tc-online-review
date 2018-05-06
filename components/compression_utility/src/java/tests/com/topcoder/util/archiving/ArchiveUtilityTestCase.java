/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * <p>
 * Test basic functionality of <code>ArchiveUtility</code> class, especially with illegal arguments. It does not test
 * creation and extraction functionality, since they are tested in {@link AbstractArchiverTestCase}.
 * </p>
 *
 * @author visualage
 * @version 2.0
 */
public class ArchiveUtilityTestCase extends TestCase {
    /**
     * <p>
     * Valid ZIP file used in test.
     * </p>
     */
    private static final File REAL_ZIP_FILE = new File("test_files", "somezip.zip");

    /**
     * <p>
     * <code>Archiver</code> implementation used in this test.
     * </p>
     */
    private Archiver archiver;

    /**
     * <p>
     * <code>ArchiveUtility</code> instance to test.
     * </p>
     */
    private ArchiveUtility archiveUtility;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     */
    protected void setUp() {
        archiver = new ZipArchiver();
    }

    /**
     * <p>
     * Clean up the test environment.
     * </p>
     */
    protected void tearDown() {
        archiver = null;
        archiveUtility = null;
    }

    /**
     * <p>
     * Test constructor when the given file is <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructorFileNull() throws Exception {
        final File dummyFile = new File(".");

        try {
            new ArchiveUtility(null, archiver);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test constructor when the given <code>Archiver</code> implementation is <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructorArchiverNull() throws Exception {
        final File dummyFile = new File(".");

        try {
            new ArchiveUtility(dummyFile, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir)</code> when archive file specified in constructor is a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileArchiveFileDirectory() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.DIRECTORY_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir)</code> when parent of the archive file specified in constructor does not
     * exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileArchiveFileParentNotExist()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.PARENT_NON_EXIST_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir)</code> when parent of the archive file specified in constructor is a
     * file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileArchiveFileParentFile() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.PARENT_FILE_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir)</code> when base dir does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileBaseDirNotExist() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.REAL_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.NON_EXIST_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir)</code> when base dir is a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileBaseDirFile() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.REAL_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.FILE_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when archive file specified in constructor is a
     * directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListArchiveFileDirectory()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.DIRECTORY_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when parent of the archive file specified in
     * constructor does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListArchiveFileParentNotExist()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.PARENT_NON_EXIST_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when parent of the archive file specified in
     * constructor is a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListArchiveFileParentFile()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.PARENT_FILE_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when base dir does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListBaseDirNotExist() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.REAL_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when base dir is a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListBaseDirFile() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.REAL_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when file list is <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListFilesNull() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.REAL_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when files contain <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListFilesWithNull() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.REAL_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NULL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when files contain instances other than a
     * <code>File</code> instance.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListFilesNotFile() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.REAL_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NOT_FILE);
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>createArchive(File baseDir, List files)</code> when files contain non-existing file.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFileListFileNotExist() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.REAL_FILE, archiver);
            archiveUtility.createArchive(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NON_EXIST_FILE);
            fail("Should have thrown FileNotFoundException");
        } catch (FileNotFoundException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir)</code> when target dir does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileTargetDirNotExist() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(REAL_ZIP_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.NON_EXIST_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir)</code> when target dir is a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileTargetDirFile() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(REAL_ZIP_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.FILE_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir)</code> when archive file in constructor does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileArchiveFileNotExist() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.NON_EXIST_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir)</code> when archive file in constructor is a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileArchiveFileDirectory() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.DIRECTORY_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir)</code> when archive file in constructor is an invalid archive.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileArchiveFileInvalid() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.FILE_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when target dir does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListTargetDirNotExist()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(REAL_ZIP_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when target dir is a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListTargetDirFile() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(REAL_ZIP_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when archive file in constructor does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListArchiveFileNotExist()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.NON_EXIST_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when archive file in constructor is a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListArchiveFileDirectory()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.DIRECTORY_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when archive file in constructor is an invalid
     * archive.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListArchiveFileInvalid()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.FILE_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when file list is <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListFilesNull() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(REAL_ZIP_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when files contain <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListFilesWithNull() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(REAL_ZIP_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NULL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when files contain instances other than a
     * <code>File</code> instance.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListFilesNotFile() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(REAL_ZIP_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NOT_FILE);
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>extractContents(File targetDir, List files)</code> when files contain non-existing file.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFileListFilesNotExist() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(REAL_ZIP_FILE, archiver);
            archiveUtility.extractContents(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NON_EXIST_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>listContents</code> when archive file in constructor does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testListContentsArchiveFileNotExist() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.NON_EXIST_FILE, archiver);
            archiveUtility.listContents();
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>listContents</code> when archive file in constructor is a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testListContentsArchiveFileDirectory()
        throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.DIRECTORY_FILE, archiver);
            archiveUtility.listContents();
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>listContents</code> when archive file in constructor is an invalid archive.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testListContentsArchiveFileInvalid() throws Exception {
        try {
            archiveUtility = new ArchiveUtility(ArchiverTestUtil.FILE_FILE, archiver);
            archiveUtility.listContents();
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>getFile</code> functionality.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testGetFile() throws Exception {
        final File dummyFile = new File(".");
        archiveUtility = new ArchiveUtility(dummyFile, archiver);

        assertTrue(dummyFile == archiveUtility.getFile());
    }

    /**
     * <p>
     * Return a new <code>TestSuite</code> containing all test cases in this class.
     * </p>
     *
     * @return a new <code>TestSuite</code> containing all test cases in this class
     */
    public static Test suite() {
        return new TestSuite(ArchiveUtilityTestCase.class);
    }
}




