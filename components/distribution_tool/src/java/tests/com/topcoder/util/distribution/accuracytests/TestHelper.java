/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Some routines for tests.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class TestHelper {
    /**
     * Reads the first line from the file.
     *
     * @param fileName the name of provided file
     * @return the read line
     */
    public static String readLine(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            return reader.readLine();
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Deletes all files from temporary directory.
     */
    public static void clearTemp() {
        clearTemp("test_files/accuracy/temp");
    }

    /**
     * Deletes all files from temporary directory.
     */
    public static void clearTemp(String dir) {
        File directory = new File(dir);
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
    }

    /**
     * Deletes non-empty directory.
     *
     * @param path the directory to be deleted
     */
    private static void deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
            path.delete();
        }
    }
}
