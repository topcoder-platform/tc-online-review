/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * <p>
 * The helper class used in unit tests of this component.
 * The modifier of this class is public, because classes in other packages will use this helper class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public final class AccuracyTestsHelper {

    /**
     * <p>
     * Creates a new instance of <code>UnitTestsHelper</code> class.
     * This private constructor prevents the creation of a new instance.
     * </p>
     */
    private AccuracyTestsHelper() {
    }

    /**
     * <p>
     * Get the file content.
     * </p>
     *
     * @param file the file path
     * @return the file content
     */
    public static String getFileContent(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuffer content = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }

            return content.toString();

        } catch (Exception e) {
            throw new RuntimeException("Cannot access redirected output file.");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {}
        }
    }

    /**
     * <p>
     * Clear the file.
     * </p>
     *
     * @param path the file path
     */
    public static void clearFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
