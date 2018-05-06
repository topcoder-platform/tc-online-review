/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * ZipCreatorFailureTestCases.java
 */
package com.topcoder.util.archiving.failuretests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import com.topcoder.util.archiving.ZipCreator;

/**
 * Failure test cases for the <code>ZipCreator</code> class.
 *
 * @author roma
 * @version 2.0
 */
public class ZipCreatorFailureTestCases extends TestCase {
    /**
     * An instance of the <code>ZipCreator</code> class.
     */
    private ZipCreator creator = null;

    /**
     * Create an instance of <code>ZipCreator</code> class.
     * Create some temp files.
     */
    public void setUp() {
        // create ZipCreator
        creator = new ZipCreator();
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
     * Test the <code>archiveFiles(File, File)</code> with second <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileWithSecondNull() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Test the <code>ahchiveFiles(File, File)</code> with empty file instead source directory.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileWithFirstFile() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.EMPTY_FILE, TestFailureHelper.OUTPUT_ZIP);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File)</code> with non exist source directory.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileWithNonExist() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.NON_EXIST, TestFailureHelper.OUTPUT_ZIP);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File)</code> with bad name for source dir.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileWithBadName() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.BAD_NAME, TestFailureHelper.OUTPUT_ZIP);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Test the <code>archiveFiles(File, File)</code> with bad name for target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileWithBadName2() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.BAD_NAME);
            fail("IOException expected.");
        } catch (IOException e) {
            // success.
        }
    }

    /**
     * Test the <code>archiveFiles(File, File)</code> with existing dir as target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileWithExistingDir() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_DIR);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>archiveFiles(File, File)</code> with empty source dir.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileWithEmptyDir() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.EMPTY_DIR, TestFailureHelper.OUTPUT_ZIP);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>archiveFiles(File, File)</code> with read-only target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileWithAccessDenied() throws Exception {
        TestFailureHelper.EXISTING_FILE.setReadOnly();
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.EXISTING_FILE);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with second <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithSecondNull() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, null, TestFailureHelper.FILES);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with third <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithThirdNull() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP, (File[]) null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with <code>null</code> element.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithNullElement() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP,
                TestFailureHelper.FILES_WITH_NULL);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with empty array.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithEmptyArray() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP,
                TestFailureHelper.EMPTY_FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with empty file instead source directory.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithFirstFile() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.EMPTY_FILE, TestFailureHelper.OUTPUT_ZIP, TestFailureHelper.FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with non exist source directory.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithNonExist() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.NON_EXIST, TestFailureHelper.OUTPUT_ZIP, TestFailureHelper.FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with bad name for source dir.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithBadName() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.BAD_NAME, TestFailureHelper.OUTPUT_ZIP, TestFailureHelper.FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with bad name for target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithBadName2() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.BAD_NAME, TestFailureHelper.FILES);
            fail("IOException expected.");
        } catch (IOException e) {
            // success.
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with existing dir as target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithExistingDir() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, File[])</code> with absolute files.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithAbsoluteFiles() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP,
                    TestFailureHelper.ABSOLUTE_FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>archiveFiles(File, File, File[])</code> with empty source dir.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithEmptyDir() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.EMPTY_DIR, TestFailureHelper.OUTPUT_ZIP, TestFailureHelper.FILES);
            fail("FileNotFoundException expected.");
        } catch (FileNotFoundException e) {
            // success
        }
    }

    /**
     * Test <code>archiveFiles(File, File, File[])</code> with read-only target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileFilesWithAccessDenied() throws Exception {
        TestFailureHelper.EXISTING_FILE.setReadOnly();
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.EXISTING_FILE,
                    TestFailureHelper.FILES);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with second <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithSecondNull() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, null, TestFailureHelper.LIST);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with third <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithThirdNull() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP, (List) null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with <code>null</code> element.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithNullElement() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP,
                TestFailureHelper.LIST_WITH_NULL);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with empty array.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithEmptyList() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP,
                TestFailureHelper.EMPTY_LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with empty file instead source directory.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithFirstFile() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.EMPTY_FILE, TestFailureHelper.OUTPUT_ZIP, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with non exist source directory.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithNonExist() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.NON_EXIST, TestFailureHelper.OUTPUT_ZIP, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with bad name for source dir.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithBadName() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.BAD_NAME, TestFailureHelper.OUTPUT_ZIP, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with bad name for target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithBadName2() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.BAD_NAME, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success.
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with existing dir as target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithExistingDir() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>archiveFiles(File, File, List)</code> with absolute files.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithAbsoluteFiles() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP,
                    TestFailureHelper.ABSOLUTE_LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>archiveFiles(File, File, List)</code> with empty source dir.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithEmptyDir() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.EMPTY_DIR, TestFailureHelper.OUTPUT_ZIP, TestFailureHelper.LIST);
            fail("FileNotFoundException expected.");
        } catch (FileNotFoundException e) {
            // success
        }
    }

    /**
     * Test <code>archiveFiles(File, File, List)</code> with read-only target file.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListWithAccessDenied() throws Exception {
        TestFailureHelper.EXISTING_FILE.setReadOnly();
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.EXISTING_FILE,
                    TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test <code>archiveFiles(File, File, List)</code> with not <code>File</code> objects.
     *
     * @throws Exception should not throw
     */
    public void testArchiveFilesFileFileListNonFileObject() throws Exception {
        try {
            creator.archiveFiles(TestFailureHelper.SOURCE_DIR, TestFailureHelper.OUTPUT_ZIP,
                TestFailureHelper.NOT_FILES_LIST);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // success
        }
    }
}








