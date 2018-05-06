/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.archiving.accuracytests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Accuracy tests for ZipExtractor.
 *
 * @author alanSunny
 * @version 2.0
 */
public class ZipExtractorAccuracyTest extends TestCase {
    /**
     * setup the test environment, archive a zip file for testing.
     */
    protected void setUp() throws Exception {
        AccuracyUtility.TEST_TARGET_DIR.mkdir();
        AccuracyUtility.creator.archiveFiles(AccuracyUtility.TEST_SOURCE_DIR, AccuracyUtility.ZIP_FILE);
    }
    /**
     * Remove zip file, extracted files for testing.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        AccuracyUtility.ZIP_FILE.delete();
        AccuracyUtility.deleteFile(AccuracyUtility.TEST_TARGET_DIR);
    }

    /**
     * Test of extractFiles(File sourceFile, File targetDir). All files in the zip file will be extracted.
     *
     * @throws IOException to JUnit
     */
    public void testExtractFiles() throws IOException {
        File[] files = new File[] {AccuracyUtility.TEST_FILE_1, AccuracyUtility.TEST_FILE_2, AccuracyUtility.TEST_FILE_3};

        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR);
        assertTrue("Extract less files.", AccuracyUtility.isFilesExist(files, AccuracyUtility.TEST_TARGET_DIR));
    }

    /**
     * Test of extractFiles(File sourceFile, File targetDir, File[] files). All files that indicated by array be
     * extracted.
     *
     * @throws IOException to JUnit
     */
    public void testExtractFilesWithFilesArray_AllFiles() throws IOException {
        File[] files = new File[] {AccuracyUtility.TEST_FILE_1, AccuracyUtility.TEST_FILE_2, AccuracyUtility.TEST_FILE_3};

        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR, files);
        assertTrue("Extract less files.", AccuracyUtility.isFilesExist(files, AccuracyUtility.TEST_TARGET_DIR));
    }

    /**
     * Test of extractFiles(File sourceFile, File targetDir, File[] files). All files that indicated by array be
     * extracted. The files that had not been indicated should not be extracted.
     *
     * @throws IOException to JUnit
     */
    public void testExtractFilesWithFilesArray_PartFiles() throws IOException {
        File[] files = new File[] {AccuracyUtility.TEST_FILE_1, AccuracyUtility.TEST_FILE_3};

        File[] nonExistFiles = new File[] {AccuracyUtility.TEST_FILE_2};
        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR, files);
        assertTrue("Extract less files.", AccuracyUtility.isFilesExist(files, AccuracyUtility.TEST_TARGET_DIR));
        assertTrue("Extract more files.", AccuracyUtility.isFilesNonExist(nonExistFiles, AccuracyUtility.TEST_TARGET_DIR));
    }

    /**
     * Test of extractFiles(File sourceDir, File targetFile, List files). All files that indicated by array be
     * extracted.
     *
     * @throws IOException to JUnit
     */
    public void testExtractFilesWithFilesList_AllFiles() throws IOException {
        List list = new ArrayList();
        list.add(AccuracyUtility.TEST_FILE_1);
        list.add(AccuracyUtility.TEST_FILE_2);
        list.add(AccuracyUtility.TEST_FILE_3);

        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR);
        assertTrue("Extract less files.", AccuracyUtility.isFilesExist((File[]) list.toArray(new File[list.size()]),
                AccuracyUtility.TEST_TARGET_DIR));
    }

    /**
     * Test of extractFiles(File sourceDir, File targetFile, List files).  All files that indicated by array be
     * extracted. The files that had not been indicated should not be extracted.
     *
     * @throws IOException to JUnit
     */
    public void testExtractFilesWithFilesListPartFiles() throws IOException {
        List list = new ArrayList();
        list.add(AccuracyUtility.TEST_FILE_1);
        list.add(AccuracyUtility.TEST_FILE_3);

        File[] nonExistFiles = new File[] {AccuracyUtility.TEST_FILE_2};
        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR, list);
        assertTrue("Extract less files.", AccuracyUtility.isFilesExist((File[]) list.toArray(new File[list.size()]),
                AccuracyUtility.TEST_TARGET_DIR));
        assertTrue("Extract more files.", AccuracyUtility.isFilesNonExist(nonExistFiles, AccuracyUtility.TEST_TARGET_DIR));
    }
}








