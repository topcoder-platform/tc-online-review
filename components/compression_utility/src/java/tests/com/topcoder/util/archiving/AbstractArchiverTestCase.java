/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


/**
 * <p>
 * Test creation, extraction and examination functionality of an <code>Archiver</code> implementation.
 * </p>
 *
 * @author visualage
 * @version 2.0
 */
public abstract class AbstractArchiverTestCase extends TestCase {
    /**
     * <p>
     * List of temporary test files to be deleted.
     * </p>
     */
    private ArrayList filesToDelete;

    /**
     * <p>
     * <code>File</code> instance of a valid archive file.
     * </p>
     */
    private final File realArchiveFile;

    /**
     * <p>
     * <code>Archiver</code> instance used to test.
     * </p>
     */
    private final Archiver archiver;

    /**
     * <p>
     * <code>ArchiveCreator</code> instance used to test.
     * </p>
     */
    private ArchiveCreator ac;

    /**
     * <p>
     * <code>ArchiveExtractor</code> instance used to test.
     * </p>
     */
    private ArchiveExtractor ae;

    /**
     * <p>
     * Construct a new <code>AbstractArchiverTestCase</code>. Set the implementation of <code>Archiver</code> provided
     * by subclass. Set a valid archive file provided by subclass.
     * </p>
     */
    public AbstractArchiverTestCase() {
        archiver = getArchiverInstance();
        realArchiveFile = getValidArchiveFile();
    }

    /**
     * <p>
     * Return a new <code>Archiver</code> instance of a particular <code>Archiver</code> implementation.
     * </p>
     *
     * @return a new <code>Archiver</code> instance
     */
    abstract Archiver getArchiverInstance();

    /**
     * <p>
     * Return a valid archive file for a particular <code>Archiver</code> implementation.
     * </p>
     *
     * @return a valid archive file
     */
    abstract File getValidArchiveFile();

    /**
     * <p>
     * Set up the test environment.
     * </p>
     */
    protected void setUp() {
        filesToDelete = new ArrayList();

        ac = archiver.createArchiveCreator();
        ae = archiver.createArchiveExtractor();
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

        ac = null;
        ae = null;
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile)</code> when target file is
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesTargetFileNull() throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile)</code> when the parent of target file
     * does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesTargetFileParentNotExist()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.PARENT_NON_EXIST_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile)</code> when the parent of target file is
     * a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesTargetFileParentFile()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.PARENT_FILE_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile)</code> when target file is a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesTargetFileDirectory() throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile)</code> when source dir does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesSourceDirNotExist() throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.REAL_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile)</code> when source dir is a file, not
     * directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesSourceDirFile() throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.REAL_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when target file is
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArrayTargetFileNull()
        throws Exception {
        // Test archiveFiles(sourceDir, targetFile, files), File[] version
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, null, ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when the parent of
     * target file does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArrayTargetFileParentNotExist()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.PARENT_NON_EXIST_FILE,
                ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when the parent of
     * target file is a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArrayTargetFileParentFile()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.PARENT_FILE_FILE,
                ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when target file is
     * a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArrayTargetFileDirectory()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.DIRECTORY_FILE,
                ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when source dir
     * does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArraySourceDirNotExist()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.REAL_FILE,
                ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when source dir is
     * a file, not a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArraySourceDirFile()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.REAL_FILE, ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when files array is
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArrayFilesNull()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.REAL_FILE, (File[]) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when files contains
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArrayFilesWithNull()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.REAL_FILE,
                ArchiverTestUtil.FILES_ARRAY_WITH_NULL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, File[] files)</code> when files contains
     * non-existing file.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileArrayFilesNotExist()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.REAL_FILE,
                ArchiverTestUtil.FILE_ARRAY_WITH_NON_EXIST_FILE);
            fail("Should have thrown FileNotFoundException");
        } catch (FileNotFoundException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when target file is
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListTargetFileNull()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, null, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when parent of target
     * file does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListTargetFileParentNotExist()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.PARENT_NON_EXIST_FILE,
                ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when parent of target
     * file is a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListTargetFileParentFile()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.PARENT_FILE_FILE,
                ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when target file is a
     * directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListTargetFileDirectory()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.DIRECTORY_FILE,
                ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when source dir does
     * not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListSourceDirNotExist()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.REAL_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when source dir is a
     * file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListSourceDirFile()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.REAL_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when file list is
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListFilesNull()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.REAL_FILE, (List) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when files contain
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListFilesWithNull()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.REAL_FILE,
                ArchiverTestUtil.FILES_WITH_NULL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when files contain
     * instances other than a <code>File</code> instance.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListFilesNotFile()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.REAL_FILE,
                ArchiverTestUtil.FILES_WITH_NOT_FILE);
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveCreator.archiveFiles(File sourceDir, File targetFile, List files)</code> when files contain
     * non-existing file.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testArchiveFilesWithFileListFilesNotExist()
        throws Exception {
        try {
            ac.archiveFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.REAL_FILE,
                ArchiverTestUtil.FILES_WITH_NON_EXIST_FILE);
            fail("Should have thrown FileNotFoundException");
        } catch (FileNotFoundException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir)</code> when source file is
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesSourceFileNull() throws Exception {
        try {
            ae.extractFiles(null, ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir)</code> when target dir does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesTargetDirNotExist() throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.NON_EXIST_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir)</code> when target dir is a file, not
     * directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesTargetDirFile() throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.FILE_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir)</code> when source file does not
     * exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesSourceFileNotExist() throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir)</code> when source file is a
     * directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesSourceFileDirectory() throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir)</code> when source file is an invalid
     * archive.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesSourceFileInvalid() throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when source file
     * is <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArraySourceFileNull()
        throws Exception {
        try {
            ae.extractFiles(null, ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when target dir
     * does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArrayTargetDirNotExist()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when target dir
     * is a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArrayTargetDirFile()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when source file
     * does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArraySourceFileNotExist()
        throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.DIRECTORY_FILE,
                ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when source file
     * is a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArraySourceFileDirectory()
        throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.DIRECTORY_FILE,
                ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when source file
     * is an invalid archive.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArraySourceFileInvalid()
        throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.DIRECTORY_FILE,
                ArchiverTestUtil.FILE_ARRAY_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when file array
     * is <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArrayFilesNull()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.DIRECTORY_FILE, (File[]) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when files
     * contain <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArrayFilesWithNull()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_ARRAY_WITH_NULL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, File[] files)</code> when files
     * contain non-existing file.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileArrayFilesNotExist()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.DIRECTORY_FILE,
                ArchiverTestUtil.FILE_ARRAY_WITH_NON_EXIST_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when source file is
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListSourceFileNull()
        throws Exception {
        try {
            ae.extractFiles(null, ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when target dir
     * does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListTargetDirNotExist()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when target dir is
     * a file, not directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListTargetDirFile()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when source file
     * does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListSourceFileNotExist()
        throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.NON_EXIST_FILE, ArchiverTestUtil.DIRECTORY_FILE,
                ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when source file is
     * a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListSourceFileDirectory()
        throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.DIRECTORY_FILE,
                ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when source file is
     * an invalid archive.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListSourceFileInvalid()
        throws Exception {
        try {
            ae.extractFiles(ArchiverTestUtil.FILE_FILE, ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_CORRECT);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when file list is
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListFilesNull()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.DIRECTORY_FILE, (List) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when files contain
     * <code>null</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListFilesWithNull()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NULL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when files contain
     * instances other than a <code>File</code> instance.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListFilesNotFile()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NOT_FILE);
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.extractFiles(File sourceFile, File targetDir, List files)</code> when files contain
     * non-existing file.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractFilesWithFileListFilesNotExist()
        throws Exception {
        try {
            ae.extractFiles(realArchiveFile, ArchiverTestUtil.DIRECTORY_FILE, ArchiverTestUtil.FILES_WITH_NON_EXIST_FILE);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.listFiles</code> when source file is null.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testListFilesSourceFileNull() throws Exception {
        try {
            ae.listFiles(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.listFiles</code> when source file does not exist.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testListFilesSourceFileNotExist() throws Exception {
        try {
            ae.listFiles(ArchiverTestUtil.NON_EXIST_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.listFiles</code> when source file is a directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testListFilesSourceFileDirectory() throws Exception {
        try {
            ae.listFiles(ArchiverTestUtil.DIRECTORY_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>ArchiveExtractor.listFiles</code> when source file is an invalid archive.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testListFilesSourceFileInvalid() throws Exception {
        try {
            ae.listFiles(ArchiverTestUtil.FILE_FILE);
            fail("Should have thrown IOException");
        } catch (IOException iae) {
            // good
        }
    }

    /**
     * <p>
     * Test creation and extraction functionality (all files/sub-directories).
     * </p>
     *
     * <p>
     * A demostration of creating an archive from/extracting an archive to a directory with all files/sub-directories
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateExtractAll() throws Exception {
        File archived = File.createTempFile("archived", "");
        filesToDelete.add(archived);
        archived.deleteOnExit();

        // Create an instance of ArchiveUtility with the archive file to create/extract
        // and a proper Archiver implementation supporting the file format
        ArchiveUtility au = new ArchiveUtility(archived, archiver);

        // Create an archive file from a base directory
        // Files/directories are archived recursively
        au.createArchive(ArchiverTestUtil.DIRECTORY_FILE);

        // Target directory must exist before extraction
        File extracted = File.createTempFile("extracted", null);

        // Get a temporary directory, instead of a file
        // First delete the temporary file and then create a directory with the same name
        extracted.delete();
        extracted.mkdirs();
        filesToDelete.add(extracted);
        extracted.deleteOnExit();

        // Extract from an archive file to a target directory with a list of file
        // Directories are created when necessary
        au.extractContents(extracted);

        // The extracted files and directory structures should be identical to the source directory
        assertTrue(ArchiverTestUtil.directoryMatch(ArchiverTestUtil.DIRECTORY_FILE, extracted));

        extracted.delete();
        archived.delete();
    }

    /**
     * <p>
     * Test creation and extraction functionality (selected files/sub-directories by <code>List</code>).
     * </p>
     *
     * <p>
     * A demostration of creating an archive from/extracting an archive to a directory with selected
     * files/sub-directories
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateExtractList() throws Exception {
        File archived = File.createTempFile("archived", "");
        filesToDelete.add(archived);
        archived.deleteOnExit();

        // Create an instance of ArchiveUtility with the archive file to create/extract
        // and a proper Archiver implementation supporting the file format
        ArchiveUtility au = new ArchiveUtility(archived, archiver);

        // Create a List of Files
        // The elements in the list are pathnames relative to the base directory representing the files to archive
        // The files/directories to archive must exist
        Vector files = new Vector();
        files.add(new File("dir1\\dir2\\compressed.txt"));
        files.add(new File("somefile"));
        files.add(new File("input.txt"));
        files.add(new File("failure"));

        // Create an archive file from a base directory with a list of file
        // Files/directories are archived recursively
        au.createArchive(ArchiverTestUtil.DIRECTORY_FILE, files);

        // Target directory must exist before extraction
        File extracted = File.createTempFile("extracted", null);

        // Get a temporary directory, instead of a file
        // First delete the temporary file and then create a directory with the same name
        extracted.delete();
        extracted.mkdirs();
        extracted.deleteOnExit();
        filesToDelete.add(extracted);

        // Create a List of Files
        // Elements in the list are relative pathnames representing the files to extract
        // Files to extract must exist in the archive file
        Vector filesExtract = new Vector();
        filesExtract.add(new File("dir1\\dir2\\compressed.txt"));
        filesExtract.add(new File("somefile"));
        filesExtract.add(new File("input.txt"));
        filesExtract.add(new File("failure\\compressed.txt"));
        filesExtract.add(new File("failure\\decompressed.txt"));

        // Extract from an archive file to a target directory with a list of file
        // Directories are created when necessary
        au.extractContents(extracted, filesExtract);

        // Extract the files for comparison
        File key = File.createTempFile("correct", null);
        File keyFile = realArchiveFile;
        key.delete();
        key.mkdirs();
        key.deleteOnExit();
        filesToDelete.add(key);

        au = new ArchiveUtility(keyFile, archiver);
        au.extractContents(key);

        // The files and directory structures should be the same
        assertTrue(ArchiverTestUtil.directoryMatch(key, extracted));

        key.delete();
        extracted.delete();
        archived.delete();
    }

    /**
     * <p>
     * Test replacing a existing file with a newly created archive file.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateReplace() throws Exception {
        // Create a new empty file named
        File existFile = new File(ArchiverTestUtil.TEST_FILES_DIR, "existarchive");
        assertTrue(existFile.createNewFile());
        existFile.deleteOnExit();
        filesToDelete.add(existFile);

        long previousLength = existFile.length();

        // Create an archive file with the same name
        File archived = new File(ArchiverTestUtil.TEST_FILES_DIR, "existarchive");
        filesToDelete.add(archived);

        ArchiveUtility au = new ArchiveUtility(archived, archiver);
        au.createArchive(new File(ArchiverTestUtil.TEST_FILES_DIR, "failure"));

        // The archive file should not be empty
        assertTrue(previousLength != archived.length());

        archived.delete();
    }

    /**
     * <p>
     * Test archive creation with absolute pathnames.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateAbsolute() throws Exception {
        // Get absolute pathnames
        File dirFile = ArchiverTestUtil.TEST_FILES_DIR.getCanonicalFile();
        File archived = new File(dirFile, "absolute");
        File sourceDir = new File(dirFile, "dir1");
        filesToDelete.add(archived);
        archived.deleteOnExit();

        assertTrue(archived.isAbsolute());
        assertTrue(sourceDir.isAbsolute());
        assertTrue(!archived.exists());

        // Create an archive file
        ArchiveUtility au = new ArchiveUtility(archived, archiver);
        au.createArchive(sourceDir);

        // The archive file should not be created
        assertTrue(archived.exists());

        archived.delete();
    }

    /**
     * <p>
     * Test archive creation with relative pathnames.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateRelative() throws Exception {
        // Get relative pathnames
        File archived = new File(ArchiverTestUtil.TEST_FILES_DIR, "relative");
        File sourceDir = ArchiverTestUtil.TEST_FILES_DIR;
        List files = Arrays.asList(new File[] { new File("aaa"), new File("bbb"), new File("ccc") });
        filesToDelete.add(archived);
        archived.deleteOnExit();

        assertTrue(!archived.isAbsolute());
        assertTrue(!sourceDir.isAbsolute());
        assertTrue(!archived.exists());

        // Create an archive file
        ArchiveUtility au = new ArchiveUtility(archived, archiver);
        au.createArchive(sourceDir, files);

        // The archive file should not be created
        assertTrue(archived.exists());

        archived.delete();
    }

    /**
     * <p>
     * Test archive extraction with absolute pathnames.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractAbsolute() throws Exception {
        // Get absolute pathnames
        File dirFile = ArchiverTestUtil.TEST_FILES_DIR.getCanonicalFile();
        File archived = realArchiveFile.getCanonicalFile();
        File targetDir = new File(dirFile, "dir1");
        File extractedFile = new File(targetDir, "input.txt");
        List files = Arrays.asList(new File[] { new File("input.txt") });
        filesToDelete.add(extractedFile);
        extractedFile.deleteOnExit();

        assertTrue(archived.isAbsolute());
        assertTrue(targetDir.isAbsolute());
        assertTrue(!extractedFile.exists());

        // Extract a file from a archive file
        ArchiveUtility au = new ArchiveUtility(archived, archiver);
        au.extractContents(targetDir, files);

        // The extracted file should not be created
        assertTrue(extractedFile.exists());

        extractedFile.delete();
    }

    /**
     * <p>
     * Test archive creation from <code>null</code> base directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateFromNull() throws Exception {
        File archived = File.createTempFile("archived", "");
        List files = Arrays.asList(new File[] { new File("test_files", "input.txt") });
        filesToDelete.add(archived);
        archived.delete();
        archived.deleteOnExit();

        assertTrue(!archived.exists());

        // Create an archive from null (current user directory)
        ArchiveUtility au = new ArchiveUtility(archived, archiver);
        au.createArchive(null, files);

        // The archive file should be created
        assertTrue(archived.exists());

        archived.delete();
    }

    /**
     * <p>
     * Test archive extraction to <code>null</code> target directory.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractToNull() throws Exception {
        File archived = realArchiveFile;
        File extractedFile = new File("input.txt");
        List files = Arrays.asList(new File[] { new File("input.txt") });
        filesToDelete.add(extractedFile);
        extractedFile.deleteOnExit();

        assertTrue(!extractedFile.exists());

        // Extract a file from an archive to null (current user directory)
        ArchiveUtility au = new ArchiveUtility(archived, archiver);
        au.extractContents(null, files);

        // The extracted file should be created
        assertTrue(extractedFile.exists());

        extractedFile.delete();
    }

    /**
     * <p>
     * Test archive extraction with relative pathnames.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExtractRelative() throws Exception {
        // Get relative pathnames
        File archived = realArchiveFile;
        File targetDir = new File(ArchiverTestUtil.TEST_FILES_DIR, "dir1");
        File extractedFile = new File(targetDir, "input.txt");
        List files = Arrays.asList(new File[] { new File("input.txt") });
        filesToDelete.add(extractedFile);
        extractedFile.deleteOnExit();

        assertTrue(!archived.isAbsolute());
        assertTrue(!targetDir.isAbsolute());
        assertTrue(!extractedFile.exists());

        // Extract a file from a archive file
        ArchiveUtility au = new ArchiveUtility(archived, archiver);
        au.extractContents(targetDir, files);

        // The extracted file should not be created
        assertTrue(extractedFile.exists());

        extractedFile.delete();
    }

    /**
     * <p>
     * Test examination functionality.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testExamination() throws Exception {
        File archived = File.createTempFile("archived", "");
        ArchiveUtility au = new ArchiveUtility(archived, archiver);
        Vector files = new Vector();
        String[] fileNames = new String[] {
                "dir1\\dir2\\compressed.txt", "somefile", "input.txt", "failure\\compressed.txt",
                "failure\\decompressed.txt"
            };

        filesToDelete.add(archived);
        archived.deleteOnExit();

        files.add(new File("dir1\\dir2\\compressed.txt"));
        files.add(new File("somefile"));
        files.add(new File("input.txt"));
        files.add(new File("failure"));

        // Test creation and extraction of all files/sub-directories
        au.createArchive(ArchiverTestUtil.DIRECTORY_FILE, files);

        List contents = au.listContents();
        String[] contentNames = new String[contents.size()];

        for (int contentIndex = 0; contentIndex < contents.size(); ++contentIndex) {
            contentNames[contentIndex] = ((File) contents.get(contentIndex)).getPath();
        }

        Arrays.sort(fileNames);
        Arrays.sort(contentNames);

        assertEquals(contentNames.length, fileNames.length);
        assertTrue(ArchiverTestUtil.stringsEqual(contentNames, 0, fileNames, 0, contentNames.length));

        archived.delete();
    }
}




