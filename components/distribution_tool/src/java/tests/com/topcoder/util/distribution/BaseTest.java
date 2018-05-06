/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

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
 * Base test class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class BaseTest extends TestCase {
    /**
     * <p>
     * Represent the file separator.
     * </p>
     */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * <p>
     * Represent the line separator.
     * </p>
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * <p>
     * Gets the value of a private field in the given class. The field has the given name. The value is retrieved from
     * the given instance.
     * </p>
     *
     * @param type
     *            the class which the private field belongs to.
     * @param instance
     *            the instance which the private field belongs to.
     * @param name
     *            the name of the private field to be retrieved.
     *
     * @return the value of the private field or <code>null</code> if any error occurs.
     * @throws Exception
     *             to JUnit
     */
    public Object getPrivateField(Class<?> type, Object instance, String name) throws Exception {
        Field field = null;
        Object obj = null;
        try {
            // Get the reflection of the field and get the value
            field = type.getDeclaredField(name);
            field.setAccessible(true);
            obj = field.get(instance);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // Reset the accessibility
                field.setAccessible(false);
            }
        }
        return obj;
    }

    /**
     * <p>
     * Delete directory recursively (including the files in it).
     * </p>
     *
     * @param dir
     *            the directory to delete
     */
    public void deleteFolder(String dir) {
        File f = new File(dir);
        if (f.exists() && f.isDirectory()) {
            // delete it directly
            if (f.listFiles().length == 0) {
                f.delete();
            } else {
                // if has sub dir
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        // call it recursively
                        deleteFolder(delFile[j].getAbsolutePath());
                    }
                    // delete the file
                    delFile[j].delete();
                }
            }
            f.delete();
        }
    }

    /**
     * Returns file content as string.
     *
     * @param file
     *            file to get its content.
     * @return content of file.
     * @throws Exception
     *             to JUnit
     */
    public static String getFileAsString(String file) throws Exception {
        StringBuffer buf = new StringBuffer();
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
     * Check if the given file is pdf or not.
     * </p>
     *
     * @param fileName
     *            the file name
     * @return true if the given file is type of pdf, false otherwise
     */
    public boolean isPDF(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            // the first 4 bytes of a PDF file is 0x25, 0x50, 0x44, 0x46
            byte[] pdfBytes = new byte[4];
            InputStream in = null;
            try {
                in = new FileInputStream(fileName);
                in.read(pdfBytes, 0, 4);
//                System.out.println("The bytes read : " + pdfBytes.length);
//                for (int i = 0; i < 4; ++i) {
//                    System.out.println("The " + i + "th byte is : " + pdfBytes[i]);
//                }
                return Arrays.equals(pdfBytes, new byte[] {0x25, 0x50, 0x44, 0x46});
            } catch (IOException e) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        // ignore
                    }
                }
            }
        }
        return false;
    }
}
