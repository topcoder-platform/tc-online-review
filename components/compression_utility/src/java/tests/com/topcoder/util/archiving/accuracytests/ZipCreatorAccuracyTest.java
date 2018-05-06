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
 * Accuracy tests for ZipCreator.
 *
 * @author alanSunny
 * @version 2.0
 */
public class ZipCreatorAccuracyTest extends TestCase {
    /**
     * setup the test environment, create target dir for testing.
     */
    protected void setUp() throws Exception {
        AccuracyUtility.TEST_TARGET_DIR.mkdir();
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
     * Test of archiveFiles(File sourceDir, File targetFile). All files in the target dir will be archived.
     *
     * @throws IOException to JUnit
     */
    public void testArchiveFiles() throws IOException {
        AccuracyUtility.creator.archiveFiles(AccuracyUtility.TEST_SOURCE_DIR, AccuracyUtility.ZIP_FILE);

        File[] files = new File[] {AccuracyUtility.TEST_FILE_1, AccuracyUtility.TEST_FILE_2, AccuracyUtility.TEST_FILE_3};

        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR);
        assertTrue("Archive less files to zip.", AccuracyUtility.isFilesExist(files, AccuracyUtility.TEST_TARGET_DIR));
    }

    /**
     * Test of archiveFiles(File sourceDir, File targetFile, File[] files). All files in the target dir will be archived.
     *
     * @throws IOException to JUnit
     */
    public void testArchiveFilesWithFilesArray_AllFiles() throws IOException {
        File[] files = new File[] {AccuracyUtility.TEST_FILE_1, AccuracyUtility.TEST_FILE_2, AccuracyUtility.TEST_FILE_3};

        AccuracyUtility.creator.archiveFiles(AccuracyUtility.TEST_SOURCE_DIR, AccuracyUtility.ZIP_FILE, files);

        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR);
        assertTrue("Archive less files to zip.", AccuracyUtility.isFilesExist(files, AccuracyUtility.TEST_TARGET_DIR));
    }

    /**
     * Test of archiveFiles(File sourceDir, File targetFile, File[] files). The files indicated in the target dir will be archived.
     *
     * @throws IOException to JUnit
     */
    public void testArchiveFilesWithFilesArray_PartFiles() throws IOException {
        File[] files = new File[] {AccuracyUtility.TEST_FILE_1, AccuracyUtility.TEST_FILE_3};

        AccuracyUtility.creator.archiveFiles(AccuracyUtility.TEST_SOURCE_DIR, AccuracyUtility.ZIP_FILE, files);

        File[] nonExistFiles = new File[] {AccuracyUtility.TEST_FILE_2};
        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR);
        assertTrue("Archive less files to zip.", AccuracyUtility.isFilesExist(files, AccuracyUtility.TEST_TARGET_DIR));
        assertTrue("Archive more files to zip.", AccuracyUtility.isFilesNonExist(nonExistFiles, AccuracyUtility.TEST_TARGET_DIR));
    }

    /**
     * Test of archiveFiles(File sourceDir, File targetFile, List files). All files in the target dir will be archived.
     *
     * @throws IOException to JUnit
     */
    public void testArchiveFilesWithFilesList_AllFiles() throws IOException {
        List list = new ArrayList();
        list.add(AccuracyUtility.TEST_FILE_1);
        list.add(AccuracyUtility.TEST_FILE_2);
        list.add(AccuracyUtility.TEST_FILE_3);

        AccuracyUtility.creator.archiveFiles(AccuracyUtility.TEST_SOURCE_DIR, AccuracyUtility.ZIP_FILE, list);
        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR);
        assertTrue("Archive less files to zip.", AccuracyUtility.isFilesExist((File[]) list.toArray(new File[list.size()]),
                AccuracyUtility.TEST_TARGET_DIR));
    }

    /**
     * Test of archiveFiles(File sourceDir, File targetFile, List files). The files indicated in the target dir will be archived.
     *
     * @throws IOException to JUnit
     */
    public void testArchiveFilesWithFilesListPartFiles() throws IOException {
        List list = new ArrayList();
        list.add(AccuracyUtility.TEST_FILE_1);
        list.add(AccuracyUtility.TEST_FILE_3);

        AccuracyUtility.creator.archiveFiles(AccuracyUtility.TEST_SOURCE_DIR, AccuracyUtility.ZIP_FILE, list);

        File[] nonExistFiles = new File[] {AccuracyUtility.TEST_FILE_2};
        AccuracyUtility.extractor.extractFiles(AccuracyUtility.ZIP_FILE, AccuracyUtility.TEST_TARGET_DIR);
        assertTrue("Archive less files to zip.", AccuracyUtility.isFilesExist((File[]) list.toArray(new File[list.size()]),
                AccuracyUtility.TEST_TARGET_DIR));
        assertTrue("Archive more files to zip.", AccuracyUtility.isFilesNonExist(nonExistFiles, AccuracyUtility.TEST_TARGET_DIR));
    }
}








