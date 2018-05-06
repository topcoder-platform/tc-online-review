/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * ArchiveUtilityFailureTestCases.java
 */
package com.topcoder.util.archiving.failuretests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import com.topcoder.util.archiving.ArchiveUtility;
import com.topcoder.util.archiving.ZipArchiver;


/**
 * Failure test cases for the <code>ArchiveUtility</code> class.
 *
 * @author roma
 * @version 2.0
 */
public class ArchiveUtilityFailureTestCases extends TestCase {
    /**
     * An instance of the <code>ArchiveUtility</code> class.
     * Used for reading from archive.
     */
    private ArchiveUtility utilityRead = null;

    /**
     * An instance of the <code>ArchiveUtility</code> class.
     * Used for writing to archive.
     */
    private ArchiveUtility utilityWrite = null;

    /**
     * An instance of the <code>ZipArchiver</code> class.
     */
    private ZipArchiver archive = new ZipArchiver();

    /**
     * Create an instances of <code>ArchiveUtility</code> class.
     * Create some temp files.
     */
    public void setUp() {
        // create ArchiveUtilitys
        utilityRead = new ArchiveUtility(TestFailureHelper.WHOLE_ZIP, archive);
        utilityWrite = new ArchiveUtility(TestFailureHelper.OUTPUT_ZIP, archive);
        // create some temp files
        TestFailureHelper.deleteDir(TestFailureHelper.OUTPUT_DIR);
        TestFailureHelper.createExistingFile();
    }

    /**
     * Delete output directory.
     */
    public void tearDown() {
        // Unlock all files.
        System.gc();
        // delete output dir
        TestFailureHelper.deleteDir(TestFailureHelper.OUTPUT_DIR);
    }

    /**
     * Test constructor with first <code>null</code>.
     */
    public void testConstructorWithFirstNull() {
        try {
            new ArchiveUtility(null, archive);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test constructor with second <code>null</code>.
     */
    public void testConstructorWithSecondNull() {
        try {
            new ArchiveUtility(TestFailureHelper.WHOLE_ZIP, null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>listContents()</code> with bad file name.
     *
     * @throws Exception should not throw
     */
    public void testListContentsWithBadName() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.BAD_NAME, archive).listContents();
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>listContents()</code> with bad archive.
     *
     * @throws Exception should not throw
     */
    public void testListContentsWithBadArchive() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.BAD_ZIP, archive).listContents();
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>listContents()</code> with non exist archive.
     *
     * @throws Exception should not throw
     */
    public void testListContentsWithNonExistArchive() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.NON_EXIST, archive).listContents();
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>listContents()</code> with dir instead archive.
     *
     * @throws Exception should not throw
     */
    public void testListContentsWithDir() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.OUTPUT_DIR, archive).listContents();
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File)</code> with empty file instead base directory.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileWithFirstFile() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.EMPTY_FILE);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File)</code> with non exist base directory.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileWithNonExist() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.NON_EXIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File)</code> with bad name for source dir.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileWithBadName() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.BAD_NAME);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Test the <code>createArchive(File)</code> with bad name for target file passed in the constructor.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileWithBadName2() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.BAD_NAME, archive).createArchive(TestFailureHelper.SOURCE_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success.
        }
    }

    /**
     * Test the <code>createArchive(File)</code> with existing dir as target file passed int the constructor.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileWithExistingDir() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.OUTPUT_DIR, archive).createArchive(TestFailureHelper.SOURCE_DIR);
            fail("IllegalStateException expected.");
        } catch (IllegalStateException e) {
            // success
        }
    }

    /**
     * Test <code>createArchive(File)</code> with empty source dir.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileWithEmptyDir() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.EMPTY_DIR);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>createArchive(File)</code> with read-only target file.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileWithAccessDenied() throws Exception {
        TestFailureHelper.OUTPUT_ZIP.createNewFile();
        TestFailureHelper.OUTPUT_ZIP.setReadOnly();
        try {
            utilityWrite.createArchive(TestFailureHelper.SOURCE_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with second <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithThirdNull() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.SOURCE_DIR, null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with <code>null</code> element.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithNullElement() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.SOURCE_DIR, TestFailureHelper.LIST_WITH_NULL);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with empty list.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithEmptyList() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.SOURCE_DIR, TestFailureHelper.EMPTY_LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with empty file instead base directory.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithFirstFile() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.EMPTY_FILE, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with non exist base directory.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithNonExist() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.NON_EXIST, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with bad name for base dir.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithBadName() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.BAD_NAME, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with bad name for target file passed in the constructor.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithBadName2() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.BAD_NAME, archive).
                createArchive(TestFailureHelper.SOURCE_DIR, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success.
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with existing dir as target file.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithExistingDir() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.OUTPUT_DIR, archive).
                createArchive(TestFailureHelper.SOURCE_DIR, TestFailureHelper.LIST);
            fail("IllegalStateException expected.");
        } catch (IllegalStateException e) {
            // success
        }
    }

    /**
     * Test the <code>createArchive(File, List)</code> with absolute files.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithAbsoluteFiles() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.SOURCE_DIR, TestFailureHelper.ABSOLUTE_LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>createArchive(File, List)</code> with empty source dir.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithEmptyDir() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.EMPTY_DIR, TestFailureHelper.LIST);
            fail("FileNotFoundException expected.");
        } catch (FileNotFoundException e) {
            // success
        }
    }

    /**
     * Test <code>createArchive(File, List)</code> with read-only target file.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListWithAccessDenied() throws Exception {
        TestFailureHelper.OUTPUT_ZIP.createNewFile();
        TestFailureHelper.OUTPUT_ZIP.setReadOnly();
        try {
            utilityWrite.createArchive(TestFailureHelper.SOURCE_DIR, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test <code>createArchive(File, List)</code> with not <code>File</code> objects.
     *
     * @throws Exception should not throw
     */
    public void testCreateArchiveFileListNonFileObject() throws Exception {
        try {
            utilityWrite.createArchive(TestFailureHelper.SOURCE_DIR, TestFailureHelper.NOT_FILES_LIST);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File)</code> with corrupted archive.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileWithBadZip() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.BAD_ZIP, archive).extractContents(TestFailureHelper.OUTPUT_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File)</code> with access denied.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileWithAccessDenied() throws Exception {
        // create some file from archive and lock it for writing
        TestFailureHelper.FILE_IN_ARCHIVE.createNewFile();
        TestFailureHelper.FILE_IN_ARCHIVE.setReadOnly();
        try {
            utilityRead.extractContents(TestFailureHelper.OUTPUT_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File)</code> with bad source name.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileWithBadSourceName() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.BAD_NAME, archive).extractContents(TestFailureHelper.OUTPUT_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File)</code> with bad target name.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileWithBadTargetName() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.BAD_NAME);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File)</code> with target as file.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileWithTargetFile() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.EXISTING_FILE);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File)</code> with non exist target directory.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileWithNonExistTarget() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.NON_EXIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with second <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithThirdNull() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.OUTPUT_DIR, (List) null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with <code>null</code> element.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithNullElement() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST_WITH_NULL);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with corrupted archive.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithBadZip() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.BAD_ZIP, archive).
                extractContents(TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with access denied.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithAccessDenied() throws Exception {
        // create some file from archive and lock it for writing
        TestFailureHelper.FILE_IN_ARCHIVE.createNewFile();
        TestFailureHelper.FILE_IN_ARCHIVE.setReadOnly();
        try {
            utilityRead.extractContents(TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with bad source name.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithBadSourceName() throws Exception {
        try {
            new ArchiveUtility(TestFailureHelper.BAD_NAME, archive).
                extractContents(TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with absolute file names.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithNonExistFile() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.OUTPUT_DIR, TestFailureHelper.ABSOLUTE_LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with bad target name.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithBadTargetName() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.BAD_NAME, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with target as file.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithTargetFile() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.EXISTING_FILE, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with non exist target directory.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithNonExistTarget() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.NON_EXIST, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractContents(File, List)</code> with empty array of files.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithEmptyFiles() throws Exception {
        utilityRead.extractContents(TestFailureHelper.OUTPUT_DIR, TestFailureHelper.EMPTY_LIST);
    }

    /**
     * Test the <code>extractContents(File, List)</code> with list with non file objects.
     *
     * @throws Exception should not throw
     */
    public void testExtractContentsFileListWithNonFileObject() throws Exception {
        try {
            utilityRead.extractContents(TestFailureHelper.OUTPUT_DIR, TestFailureHelper.NOT_FILES_LIST);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // success
        }
    }
}








