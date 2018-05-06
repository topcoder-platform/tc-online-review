/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ TestHelper.java
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;


/**
 * Helper class used in tests.
 *
 * @author Charizard
 * @version 2.0
 */
public final class TestHelper {
    /** The name of directory stores test files. */
    public static final String TEST_FILES_DIRECTORY = "test_files";

    /**
     * <p>Empty private constructor.</p>
     */
    private TestHelper() {
    }

    /**
     * Get a File instance with given file name.
     *
     * @param fileName name of the file (under test_files folder)
     *
     * @return File instance of the given file name
     */
    public static File getFile(String fileName) {
        return new File(TEST_FILES_DIRECTORY + File.separator + fileName);
    }

    /**
     * Load a configuration file.
     *
     * @param fileName name of the configuration file
     *
     * @throws IOException If error occurs
     */
    public static void loadConfig(String fileName) throws IOException {
        ConfigManager.getInstance().add(getFile(fileName).getCanonicalPath());
    }

    /**
     * Clear all configurations except the default namespace of JNDIUtils, which is pre-loaded by
     * ConfigManager and used in all test cases in version 1.0.
     *
     * @throws UnknownNamespaceException If error occurs
     */
    public static void clearConfig() throws UnknownNamespaceException {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator i = cm.getAllNamespaces(); i.hasNext();) {
            String nextNamespace = (String) i.next();

            if (!nextNamespace.equals(JNDIUtils.NAMESPACE)) {
                cm.removeNamespace(nextNamespace);
            }
        }
    }

    /**
     * Generate a random string with length between 8 and 31, and contains only upper-case letters.
     *
     * @return the generated string
     */
    public static String generateString() {
        int length = (int) (Math.random() * 24) + 8;
        StringBuffer buffer = new StringBuffer(length);

        for (int i = 0; i < length; i++) {
            buffer.append((char) ((Math.random() * 26) + 'A'));
        }

        return buffer.toString();
    }
}
