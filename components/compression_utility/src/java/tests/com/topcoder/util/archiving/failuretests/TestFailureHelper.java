/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * TestFailureUtility.java
 */
package com.topcoder.util.archiving.failuretests;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
/**
 * A helper class, which contain usefull constants and methods.
 *
 * @author roma
 * @version 2.0
 */
public class TestFailureHelper {
    /**
     * Directory with test files.
     */
    public static final File TEST_DIR = new File("test_files/failure2");

    /**
     * File with bad name.
     */
    public static final File BAD_NAME = new File(TEST_DIR, "bad::: dsfsadf ??? f*** f+FJ*(D)(F*Fd(SF)*&) FDS():name");

    /**
     * Non exist file.
     */
    public static final File NON_EXIST = new File(TEST_DIR, "not_exist");

    /**
     * Directory with uncompressed files.
     */
    public static final File SOURCE_DIR = new File(TEST_DIR, "source");

    /**
     * Empty directory.
     */
    public static final File EMPTY_DIR = new File(SOURCE_DIR, "emptyDir");

    /**
     * Temporary output directory.
     */
    public static final File OUTPUT_DIR = new File(TEST_DIR, "output");

    /**
     * An existing file.
     */
    public static final File EXISTING_FILE = new File(OUTPUT_DIR, "exist.txt");

    /**
     * Directory with compressed good and corrupted files.
     */
    public static final File DIST_DIR = new File(TEST_DIR, "dist");

    /**
     * An empty file in the File System.
     */
    public static final File EMPTY_FILE = new File(SOURCE_DIR, "empty");

    /**
     * Target file. We will try comress to this file.
     */
    public static final File OUTPUT_ZIP = new File(OUTPUT_DIR, "output.zip");

    /**
     * Array of source files from source dir.
     */
    public static final File[] FILES = new File[] {
        new File("empty"),
        new File("stylesheet.css"),
        new File("subdir1/subdir11/subdir11.alt"),
        new File("subdir1/subdir11/subdir11.txt")};

    /**
     * Array of source files with <code>null</code>.
     */
    public static final File[] FILES_WITH_NULL = new File[] {
        new File("empty"),
        new File("stylesheet.css"),
        null,
        new File("subdir1/subdir11")};

    /**
     * Empty array of files.
     */
    public static final File[] EMPTY_FILES = new File[0];

    /**
     * Array of files whith absolute filename.
     */
    public static final File[] ABSOLUTE_FILES = new File[] {
        new File("c:\\"),
        new File("/files")};

    /**
     * List of source files from source dir.
     */
    public static final List LIST = Arrays.asList(new File[] {
        new File("empty"),
        new File("stylesheet.css"),
        new File("subdir1/subdir11/subdir11.alt"),
        new File("subdir1/subdir11/subdir11.txt")});

    /**
     * List of source files with <code>null</code>.
     */
    public static final List LIST_WITH_NULL = Arrays.asList(new File[] {
        new File("empty"),
        new File("stylesheet.css"),
        null,
        new File("subdir1/subdir11")});

    /**
     * Empty list of files.
     */
    public static final List EMPTY_LIST = Arrays.asList(new File[0]);

    /**
     * List of files whith absolute filename.
     */
    public static final List ABSOLUTE_LIST = Arrays.asList(new File[] {
        new File("c:\\"),
        new File("/files")});

    /**
     * List of object.
     */
    public static final List NOT_FILES_LIST = Arrays.asList(new Object[] {new Object()});

    /**
     * Archive with all files.
     */
    public static final File WHOLE_ZIP = new File(DIST_DIR, "source.zip");

    /**
     * Bad archive.
     */
    public static final File BAD_ZIP = new File(DIST_DIR, "bad.zip");

    /**
     * One file in archive located in the output dir, used for locking.
     */
    public static final File FILE_IN_ARCHIVE = new File(OUTPUT_DIR, "stylesheet.css");

    /**
     * Private constructor to prevent instantiation.  Since this is a utility class, instantiation
     * should be prevented.
     */
    private TestFailureHelper() {
        // empty implementation
    }

    /**
     * Create <code>EXISTING_FILE</code> an parent dir.
     */
    public static void createExistingFile() {
        try {
            OUTPUT_DIR.mkdirs();
            EXISTING_FILE.createNewFile();
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * Recursivle delete directory.
     *
     * @param dir directory to delete
     */
    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        dir.delete();
    }
}








