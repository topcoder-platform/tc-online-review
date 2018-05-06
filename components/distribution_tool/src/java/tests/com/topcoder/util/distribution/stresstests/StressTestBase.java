/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>
 * The base class for stress test.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class StressTestBase extends TestCase {
    /**
     * <p>
     * The base directory for stress test files.
     * </p>
     */
    public static final String STRESSTEST_BASE = "test_files/stresstests/";

    /**
     * <p>
     * Get the required private field using reflection.
     * </p>
     *
     * @param type
     *            the class which the private field belongs to.
     * @param instance
     *            the instance which the private field belongs to.
     * @param fieldName
     *            the name of the private field to be retrieved.
     *
     * @return the value of the private field or <code>null</code> if any error occurs.
     * @throws Exception
     *             to JUnit
     */
    public Object getPrivateField(Class<?> type, Object instance, String fieldName) throws Exception {
        try {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(instance);
            return obj;
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        }
        return null;
    }

    /**
     * <p>
     * Delete the given directory.
     * </p>
     *
     * @param dirName
     *            the name of the directory to delete
     */
    public void del(String dirName) {
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            if (dir.listFiles().length == 0) {
                dir.delete();
            } else {
                File delFile[] = dir.listFiles();
                int i = dir.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());
                    }
                    delFile[j].delete();
                }
            }
            dir.delete();
        }
    }

    /**
     * <p>
     * Returns file content and store the content in a String object.
     * </p>
     *
     * @param file
     *            file to get its content.
     * @return content of file.
     * @throws Exception
     *             to JUnit
     */
    public static String getFileContent(String file) throws Exception {
        StringBuilder buf = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(file)));

        while (true) {
            String s = in.readLine();
            if (s == null) {
                break;
            }
            buf.append(s);
        }
        in.close();
        return buf.toString();
    }

    /**
     * <p>
     * Check if a file is pdf format.
     * </p>
     *
     * @param file
     *            the file name
     * @return true if the file is pdf
     */
    public boolean checkPDF(String name) throws Exception {
        File file = new File(name);
        if (file.isFile()) {
            byte[] pdfBytes = new byte[4];
            InputStream in = null;
            try {
                in = new FileInputStream(name);
                in.read(pdfBytes, 0, 4);
                return Arrays.equals(pdfBytes, new byte[] {0x25, 0x50, 0x44, 0x46});
            } catch (IOException e) {
                in.close();
            }
        }
        return false;
    }
}
