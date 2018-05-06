/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * TestUtility.java
 */
package com.topcoder.util.archiving.accuracytests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * A Helper class that is used to help test the accuracy of archiving files. This class provides
 * methods to compare the contents of two files, whether one is inside an archive or not.
 *
 * @author alanSunny
 * @version 2.0
 */
class AccuracyUtility {
    /** 
     * The constant of accuracy test root dir.
     */
    final static String TEST_ROOT_DIR = "test_files/accuracy";

    /** 
     * The constant of accuracy test source dir.
     */
    final static String TEST_SOURCE_DIR = TEST_ROOT_DIR + "/src";
    
    /** 
     * The constant of accuracy target dir.
     */ 
    final static String TEST_TARGET_DIR = TEST_ROOT_DIR + "/target";
    
    /**
     * The constant of zip file name.
     */
    final static String ZIP_FILE = TEST_ROOT_DIR + "/test.zip";

    /**
     * The constant of zip dir name.
     */
    final static String ZIP_DIR = TEST_ROOT_DIR + "/testDir";

    /**
     * The constant of test seed file. It will be copy to sub-dir for testing.
     */
    final static String TEST_SEED_FILE = "test.file";

    /**
     * Private constructor to prevent instantiation.  Since this is a utility class, instantiation
     * should be prevented.
     */
    private AccuracyUtility() {
        // empty implementation
    }

    /**
     * <p>
     * Compare two files for identical contents. One file is unzipped, and the other one is located
     * within an archive. This method will verify that both files actually have the same contents if
     * they were both unzipped.
     * </p>
     *
     * @param unzippedEntry The unzipped File to compare.
     * @param zipEntry the zip entry corresponding to the file to compare inside the archive.
     * @param zipFile the archive containing the file to compare.
     * @return true if the zipEntry file and the unzipped file have identical contents, false
     * otherwise.
     * @throws IOException if an I/O error occurs.
     */
    public static boolean compareFiles(File unzippedEntry, ZipEntry zipEntry, ZipFile zipFile)
        throws IOException {
        return compareStreams(zipFile.getInputStream(zipEntry), new FileInputStream(unzippedEntry));
    }

    /**
     * Compares the contents of two unzipped files. This will determine whether their contents are
     * identical.
     *
     * @param first the first file to compare.
     * @param second the second file to compare.
     * @return true if both files' contents are identical, false otherwise.
     * @throws IOException if an I/O error occurs.
     */
    public static boolean compareFiles(File first, File second)
        throws IOException {
        return compareStreams(new FileInputStream(first), new FileInputStream(second));
    }

    /**
     * Compares that the "content" of two input streams are identical. A strightforward bit by bit
     * comparison is used.
     *
     * @param first the first stream to compare.
     * @param second the second stream to compare.
     * @return true if both streams have identical "content", false otherwise.
     * @throws IOException if an I/O error occurs.
     */
    private static boolean compareStreams(InputStream first, InputStream second)
        throws IOException {
        try {
            int ch = 0;
    
            while ((ch = first.read()) != -1) {
                if (ch != second.read()) {
                    return false;
                }
            }
    
            return (second.read() == -1);
        } finally {
            second.close();
            first.close();
        }
    }
}
