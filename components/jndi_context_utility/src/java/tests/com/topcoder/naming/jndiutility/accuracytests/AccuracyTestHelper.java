/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.accuracytests;

import com.topcoder.naming.jndiutility.JNDIUtils;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Iterator;


/**
 * Helper class used in accuracy tests.
 *
 * @author KKD
 * @version 2.0
 */
public final class AccuracyTestHelper {

    /**
     * <p>Empty private constructor.</p>
     */
    private AccuracyTestHelper() {
    }

    /**
     * Get a file instance from the given file name.
     *
     * @param fileName name of the file
     * @return instance of the file.
     */
    public static File getFile(String fileName) {
        return new File(fileName);
    }

    /**
     * Get an InputStream instance of the given file name.
     *
     * @param fileName name of the file
     * @return InputStream of the file
     * @throws Exception exception to JUnit
     */
    public static InputStream getInputStream(String fileName) throws Exception {
        return new FileInputStream(getFile(fileName));
    }
    /**
     * Load a configuration file.
     *
     * @param fileName name of the configuration file
     *
     * @throws IOException If error occurs
     */
    public static void loadConfig(String fileName) throws IOException {
        ConfigManager.getInstance().add(new File(fileName).getCanonicalPath());
    }

    /**
     * Clear all configurations except the default namespace of JNDIUtils.
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
}
