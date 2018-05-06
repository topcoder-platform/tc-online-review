/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;

/**
 * <p>
 * This class provides methods for testing this component.
 * </p>
 *
 * @author sparemax
 * @version 2.2
 * @since 2.2
 */
public class TestsHelper {
    /**
     * <p>
     * Represents the path of test files.
     * </p>
     */
    public static final String TEST_FILES = "test_files" + File.separator;

    /**
     * <p>
     * Represents the path of invalid test files.
     * </p>
     */
    public static final String INVALID_FILES = TEST_FILES + "invalid" + File.separator;

    /**
     * <p>
     * Private constructor to prevent this class being instantiated.
     * </p>
     */
    private TestsHelper() {
        // empty
    }

    /**
     * <p>
     * Copies the source file to destination.
     * </p>
     *
     * @param src
     *            the source file to copy.
     * @param dest
     *            the destination file.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void copyFile(String src, String dest) throws Exception {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);

        try {
            byte[] buffer = new byte[1024];
            int k = 0;
            while ((k = in.read(buffer)) != -1) {
                out.write(buffer, 0, k);
            }
        } finally {
            // Close the stream
            in.close();
            // Close the stream
            out.close();
        }
    }

    /**
     * <p>
     * Gets value for field of given object.
     * </p>
     *
     * @param instance
     *            the instance.
     * @param field
     *            the field name.
     *
     * @return the field value.
     */
    public static Object getField(Object instance, String field) {
        Object value = null;
        try {
            Field fieldObj = getFieldObj(instance, field);
            fieldObj.setAccessible(true);

            value = fieldObj.get(instance);

            fieldObj.setAccessible(false);
        } catch (IllegalAccessException e) {
            // Ignore
        }

        return value;
    }

    /**
     * <p>
     * Reads the content of a given file.
     * </p>
     *
     * @param fileName
     *            the name of the file to read.
     *
     * @return a string represents the content.
     *
     * @throws IOException
     *             if any error occurs during reading.
     */
    public static String readFile(String fileName) throws IOException {
        Reader reader = new FileReader(fileName);

        try {
            // Create a StringBuilder instance
            StringBuilder sb = new StringBuilder();

            // Buffer for reading
            char[] buffer = new char[1024];

            // Number of read chars
            int k = 0;

            // Read characters and append to string builder
            while ((k = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, k);
            }

            // Return read content
            return sb.toString().replace("\r\n", "\n");
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                // Ignore
            }
        }
    }

    /**
     * <p>
     * Gets a Field object of the instance.
     * </p>
     *
     * @param instance
     *            the instance.
     * @param field
     *            the field name.
     *
     * @return the Field object.
     */
    private static Field getFieldObj(Object instance, String field) {
        Field fieldObj = null;
        try {
            try {
                fieldObj = instance.getClass().getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                // Ignore
            }
            if (fieldObj == null) {
                try {
                    // From the superclass
                    fieldObj = instance.getClass().getSuperclass().getDeclaredField(field);
                } catch (NoSuchFieldException e) {
                    // Ignore
                }
            }
            if (fieldObj == null) {
                fieldObj = instance.getClass().getSuperclass().getSuperclass().getDeclaredField(field);
            }
        } catch (IllegalArgumentException e) {
            // Ignore
        } catch (SecurityException e) {
            // Ignore
        } catch (NoSuchFieldException e) {
            // Ignore
        }

        return fieldObj;
    }
}
