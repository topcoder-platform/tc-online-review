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

import com.topcoder.util.archiving.ZipCreator;
import com.topcoder.util.archiving.ZipExtractor;


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
    final static File TEST_SOURCE_DIR = new File(TEST_ROOT_DIR + "/src");

    /**
     * The constant of accuracy target dir.
     */
    final static File TEST_TARGET_DIR = new File(TEST_ROOT_DIR + "/target");

    /**
     * The constant of zip file name.
     */
    final static File ZIP_FILE = new File(TEST_ROOT_DIR + "/test.zip");

    /**
     * The constant of test seed file. It will be copy to sub-dir for testing.
     */
    final static File TEST_FILE_1 = new File("test1.java");

    /**
     * The constant of test seed file. It will be copy to sub-dir for testing.
     */
    final static File TEST_FILE_2 = new File("test2.java");

    /**
     * The constant of test seed file. It will be copy to sub-dir for testing.
     */
    final static File TEST_FILE_3 = new File("subdir/test3.java");
    /**
     * The <code>ZipCreator</code> instance.
     */
    static ZipCreator creator =  new ZipCreator();

    /**
     * the <code>ZipExtractor</code> used for testing.
     */
    static ZipExtractor extractor = new ZipExtractor();

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

    /**
     * Check if all given files exists
     *
     * @param files the files to be check.
     * @param baseDir the baseDir to be check.
     * @return true if all given files exist
     */
    static boolean isFilesExist(File[] files, File baseDir) {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (baseDir != null) {
                file = new File(baseDir, file.getPath());
            }

            if (!file.exists()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if all given files do not exists
     *
     * @param files the files to be check.
     * @param baseDir the baseDir to be check.
     * @return true if all given files do not exist
     */
    static boolean isFilesNonExist(File[] files, File baseDir) {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (baseDir != null) {
                file = new File(baseDir, file.getPath());
            }

            if (file.exists()) {
                return false;
            }
        }

        return true;
    }

    /**
     * The helper method used for deleting a directory or normal file. No matter dir is empty or not.
     * @param file
     */
    static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] lists = file.listFiles();

            for (int i = 0; i < lists.length; i++) {
                deleteFile(lists[i]);
            }
        }

        file.delete();
    }
}








