/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * ZipExtractorFailureTestCases.java
 */
package com.topcoder.util.archiving.failuretests;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import com.topcoder.util.archiving.ZipExtractor;

/**
 * Failure test cases for the <code>ZipExtractor</code> class.
 *
 * @author roma
 * @version 2.0
 */
public class ZipExtractorFailureTestCases extends TestCase {
    /**
     * An instance of the <code>ZipExtractor</code> class.
     */
    private ZipExtractor extractor = null;

    /**
     * Create an instance of <code>ZipExtractor</code> class.
     * Create some temp files.
     */
    public void setUp() {
        // create ZipExtractor
        extractor = new ZipExtractor();
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
     * Test the <code>extractFiles(File, File)</code> with first <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileWithFirstNull() throws Exception {
        try {
            extractor.extractFiles(null, TestFailureHelper.OUTPUT_DIR);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File)</code> with corrupted archive.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileWithBadZip() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.BAD_ZIP, TestFailureHelper.OUTPUT_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File)</code> with access denied.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileWithAccessDenied() throws Exception {
        // create some file from archive and lock it for writing
        TestFailureHelper.FILE_IN_ARCHIVE.createNewFile();
        TestFailureHelper.FILE_IN_ARCHIVE.setReadOnly();
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File)</code> with bad source name.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileWithBadSourceName() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.BAD_NAME, TestFailureHelper.OUTPUT_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File)</code> with bad target name.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileWithBadTargetName() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.BAD_NAME);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File)</code> with target as file.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileWithTargetFile() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.EXISTING_FILE);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File)</code> with non exist target directory.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileWithNonExistTarget() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.NON_EXIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with first <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithFirstNull() throws Exception {
        try {
            extractor.extractFiles(null, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.FILES);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with third <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithThirdNull() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR, (File[]) null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with <code>null</code> element.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithNullElement() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR,
                TestFailureHelper.FILES_WITH_NULL);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with corrupted archive.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithBadZip() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.BAD_ZIP, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.FILES);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with access denied.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithAccessDenied() throws Exception {
        // create some file from archive and lock it for writing
        TestFailureHelper.FILE_IN_ARCHIVE.createNewFile();
        TestFailureHelper.FILE_IN_ARCHIVE.setReadOnly();
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.FILES);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with bad source name.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithBadSourceName() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.BAD_NAME, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.FILES);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with absolute file names.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithNonExistFile() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR,
                TestFailureHelper.ABSOLUTE_FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with bad target name.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithBadTargetName() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.BAD_NAME, TestFailureHelper.FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with target as file.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithTargetFile() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.EXISTING_FILE,
                    TestFailureHelper.FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with non exist target directory.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileFilesWithNonExistTarget() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.NON_EXIST, TestFailureHelper.FILES);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, File[])</code> with empty array of files.
     *
     * @throws Exception shuld not throw
     */
    public void testExtractFilesFileFileFilesWithEmptyFiles() throws Exception {
        extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR,
            TestFailureHelper.EMPTY_FILES);
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with first <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithFirstNull() throws Exception {
        try {
            extractor.extractFiles(null, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with third <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithThirdNull() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR, (List) null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with <code>null</code> element.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithNullElement() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR,
                TestFailureHelper.LIST_WITH_NULL);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with corrupted archive.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithBadZip() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.BAD_ZIP, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with access denied.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithAccessDenied() throws Exception {
        // create some file from archive and lock it for writing
        TestFailureHelper.FILE_IN_ARCHIVE.createNewFile();
        TestFailureHelper.FILE_IN_ARCHIVE.setReadOnly();
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with bad source name.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithBadSourceName() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.BAD_NAME, TestFailureHelper.OUTPUT_DIR, TestFailureHelper.LIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with absolute file names.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithNonExistFile() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR,
                TestFailureHelper.ABSOLUTE_LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with bad target name.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithBadTargetName() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.BAD_NAME, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with target as file.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithTargetFile() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.EXISTING_FILE,
                    TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with non exist target directory.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithNonExistTarget() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.NON_EXIST, TestFailureHelper.LIST);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with empty array of files.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithEmptyFiles() throws Exception {
        extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR,
            TestFailureHelper.EMPTY_LIST);
    }

    /**
     * Test the <code>extractFiles(File, File, List)</code> with list with non file objects.
     *
     * @throws Exception should not throw
     */
    public void testExtractFilesFileFileListWithNonFileObject() throws Exception {
        try {
            extractor.extractFiles(TestFailureHelper.WHOLE_ZIP, TestFailureHelper.OUTPUT_DIR,
                TestFailureHelper.NOT_FILES_LIST);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // success
        }
    }

    /**
     * Test the <code>listFiles(File)</code> with <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testListFilesWithNull() throws Exception {
        try {
            extractor.listFiles(null);
            fail("NullPointerException expected.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the <code>listFiles(File)</code> with bad file name.
     *
     * @throws Exception should not throw
     */
    public void testListFilesWithBadName() throws Exception {
        try {
            extractor.listFiles(TestFailureHelper.BAD_NAME);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>listFiles(File)</code> with bad archive.
     *
     * @throws Exception should not throw
     */
    public void testListFilesWithBadArchive() throws Exception {
        try {
            extractor.listFiles(TestFailureHelper.BAD_ZIP);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>listFiles(File)</code> with non exist archive.
     *
     * @throws Exception should not throw
     */
    public void testListFilesWithNonExistArchive() throws Exception {
        try {
            extractor.listFiles(TestFailureHelper.NON_EXIST);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }

    /**
     * Test the <code>listFiles(File)</code> with dir instead archive.
     *
     * @throws Exception should not throw
     */
    public void testListFilesWithDir() throws Exception {
        try {
            extractor.listFiles(TestFailureHelper.OUTPUT_DIR);
            fail("IOException expected.");
        } catch (IOException e) {
            // success
        }
    }
}








