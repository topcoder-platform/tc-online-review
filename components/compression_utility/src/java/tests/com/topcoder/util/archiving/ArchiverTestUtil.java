/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * Utility methods and constants used in test cases.
 * </p>
 *
 * @author visualage
 * @version 2.0
 */
final class ArchiverTestUtil {
    /**
     * <p>
     * Directory containing all test files.
     * </p>
     */
    static final File TEST_FILES_DIR = new File("test_files");

    /**
     * <p>
     * <code>File</code> instance of a file with non-existing parent.
     * </p>
     */
    static final File PARENT_NON_EXIST_FILE = new File("not_exist", "somefile");

    /**
     * <p>
     * <code>File</code> instance of a existing directory.
     * </p>
     */
    static final File DIRECTORY_FILE = TEST_FILES_DIR;

    /**
     * <p>
     * <code>File</code> instance of a file whose parent is also a file.
     * </p>
     */
    static final File PARENT_FILE_FILE = new File("build.xml", "anyfile");

    /**
     * <p>
     * <code>File</code> instance of a non-existing file.
     * </p>
     */
    static final File REAL_FILE = new File(TEST_FILES_DIR, "realfile");

    /**
     * <p>
     * <code>File</code> instance of a existing file.
     * </p>
     */
    static final File FILE_FILE = new File(TEST_FILES_DIR, "somefile");

    /**
     * <p>
     * <code>File</code> instance of a non-existing file.
     * </p>
     */
    static final File NON_EXIST_FILE = new File("not_exist");

    /**
     * <p>
     * Array of <code>File</code> instances in which <code>null</code> is an element.
     * </p>
     */
    static final File[] FILES_ARRAY_WITH_NULL = new File[] { null, new File("somefile") };

    /**
     * <p>
     * Array of <code>File</code> instances in which an element represents a non-existing file.
     * </p>
     */
    static final File[] FILE_ARRAY_WITH_NON_EXIST_FILE = new File[] { new File("input.txt"), new File("not_exist") };

    /**
     * <p>
     * Array of <code>File</code> instances which is acceptable.
     * </p>
     */
    static final File[] FILE_ARRAY_CORRECT = new File[] { new File("somefile"), new File("input.txt") };

    /**
     * <p>
     * <code>List</code> of <code>File</code> instances in which <code>null</code> is an element.
     * </p>
     */
    static final List FILES_WITH_NULL = Arrays.asList(FILES_ARRAY_WITH_NULL);

    /**
     * <p>
     * <code>List</code> of <code>File</code> instances in which <code>Object</code> is an element.
     * </p>
     */
    static final List FILES_WITH_NOT_FILE = Arrays.asList(new Object[] { new Object(), new File("somefile") });

    /**
     * <p>
     * <code>List</code> of <code>File</code> instances in which a <code>File</code> instance represents a non-existing
     * file.
     * </p>
     */
    static final List FILES_WITH_NON_EXIST_FILE = Arrays.asList(FILE_ARRAY_WITH_NON_EXIST_FILE);

    /**
     * <p>
     * <code>List</code> of <code>File</code> instances which is acceptable.
     * </p>
     */
    static final List FILES_CORRECT = Arrays.asList(FILE_ARRAY_CORRECT);

    /**
     * <p>
     * Initialize all static members.
     * </p>
     */
    static {
        REAL_FILE.deleteOnExit();
    }

    /**
     * <p>
     * Constructs a new instance of <code>ArchiverTestUtil</code>.
     * </p>
     */
    private ArchiverTestUtil() {
    }

    /**
     * <p>
     * Returns <code>true</code> iff the given parts of the two arrays are equal.
     * </p>
     *
     * @param b1 first <code>String</code> array
     * @param offset1 first index of first byte array to compare
     * @param b2 second <code>String</code> array
     * @param offset2 first index of second byte array to compare
     * @param length number of <code>String</code> instances to compare
     *
     * @return <code>true</code> iff the given parts of the two arrays are equal
     */
    static boolean stringsEqual(final String[] b1, final int offset1, final String[] b2, final int offset2,
        final int length) {
        for (int i = 0; i < length; i++) {
            if (!b1[offset1 + i].equals(b2[offset2 + i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * Reads a File's contents as a String.
     * </p>
     *
     * @param f File to read
     *
     * @return File's contents as String
     *
     * @throws IOException if an I/O error has occurred
     */
    static String readFileAsString(final File f) throws IOException {
        final FileReader fr = new FileReader(f);
        final StringBuffer result = new StringBuffer();
        final char[] buffer = new char[1024];
        int charsRead;

        try {
            while ((charsRead = fr.read(buffer)) > 0) {
                result.append(buffer, 0, charsRead);
            }
        } finally {
            fr.close();
        }

        return result.toString();
    }

    /**
     * <p>
     * Compare two files.
     * </p>
     *
     * @param f1 first File to compare
     * @param f2 second File to compare
     *
     * @return <code>true</code> iff files are identical
     *
     * @throws IOException if an I/O error has occurred
     */
    static boolean filesMatch(final File f1, final File f2)
        throws IOException {
        return readFileAsString(f1).equals(readFileAsString(f2));
    }

    /**
     * <p>
     * Compare two directories.
     * </p>
     *
     * @param d1 first directory to compare
     * @param d2 second directory to compare
     *
     * @return <code>true</code> iff files and sub-directories rooted at the first directory are identical to the same
     *         file/sub-directory rooted at the second directory
     *
     * @throws IOException if an I/O error has occurred
     */
    static boolean directoryMatch(final File d1, final File d2)
        throws IOException {
        String[] files = d1.list();

        for (int fileIndex = 0; fileIndex < files.length; ++fileIndex) {
            File currentFile = new File(d1, files[fileIndex]);

            if (currentFile.isDirectory()) {
                // Recursively compare sub-directories
                if (!directoryMatch(currentFile, new File(d2, files[fileIndex]))) {
                    return false;
                }
            } else if (!filesMatch(currentFile, new File(d2, files[fileIndex]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * Delete a directory or file. If the given <code>File</code> instance represents a directory, the directory is
     * deleted recursively.
     * </p>
     *
     * @param file file or directory to be deleted
     */
    static void deleteRecursive(File file) {
        if (file.isFile()) {
            // If file, delete directly
            file.delete();
        } else if (file.isDirectory()) {
            // Directory, delete recursively
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; ++i) {
                deleteRecursive(files[i]);
            }

            file.delete();
        }
    }
}



