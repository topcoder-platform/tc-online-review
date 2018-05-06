/*
 * Copyright (C) 2007 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * <p>
 * Helper class for this component.
 * </p>
 *
 * <p>
 * Thread Safety : This class is immutable and so thread safe.
 * </p>
 *
 * @author biotrail
 * @author TCSDEVELOPER
 * @version 3.0
 * @since 2.1
 */
public final class Util {
    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private Util() {
        // empty
    }

    /**
     * <p>
     * Checks whether the given Object is null.
     * </p>
     *
     * @param arg the argument to check
     * @param name the name of the argument to check
     *
     * @throws IllegalArgumentException if the given Object is null
     */
    public static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given String is null or empty.
     * </p>
     *
     * @param arg the String to check
     * @param name the name of the String argument to check
     *
     * @throws IllegalArgumentException if the given string is null or empty (trimmed)
     */
    public static void checkString(String arg, String name) {
        checkNull(arg, name);

        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }

    /**
     * <p>
     * Reads all data from reader and save it to string.
     * </p>
     * @return string, which was read
     * @param reader
     *            the reader
     * @throws IllegalArgumentException
     *             if reader is <code>null</code>
     * @throws IOException
     *             if an I/O error occurs while reading from the reader
     * @since 2.0
     */
    static String readString(Reader reader) throws IOException {
        // Check for null.
        if (reader == null) {
            throw new IllegalArgumentException("The argument is null.");
        }

        // Accumulate string.
        StringBuffer content = new StringBuffer();
        // Buffer for reading.
        char[] buffer = new char[1024];
        // Number of read chars.
        int n = 0;
        // Read all characters to string.
        while ((n = reader.read(buffer)) != -1) {
            content.append(buffer, 0, n);
        }

        return content.toString();
    }

    /**
     * <p>
     * Converts a path string written with the local system-dependent separator sequence to a path name written with
     * "/" as it separator
     * </p>
     *
     * @param path the path String to process, written with the local separator character
     * @return a String representing the same path as the input string, but using "/" as its separator
     * @since 3.1
     */
    public static String changeSeparator(String path) {
        if (File.separator.equals("/")) {
            return path;
        } else {
            /* 
             * Replace single backslashes in the file separator with quadruple backslashes, forming a string that can be
             * compiled into a regex pattern matching the separator as a literal.  Quoting with \Q...\E doesn't work for
             * this case under Java 1.4, and the Java 1.4 version of Pattern doesn't have a quote() method.
             */
            String pattern = File.separator.replaceAll("\\\\", "\\\\\\\\");
            return path.replaceAll(pattern, "/");
        }
    }

    /**
     * <p>
     * Get a <code>BufferedReader</code> from a file or resource from jar file.
     * </p>
     *
     * @param fileName
     *          the name of the file or the resource
     * @return the instance of BufferedReader
     * @throws IOException
     *          If any error occurs
     * @since 3.1
     */
    public static BufferedReader getBufferedReaderFromFileOrResource(String fileName) throws IOException {
        // first find fileName from class loader resource
        String resourcePath = changeSeparator(fileName);
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        BufferedReader reader;
        if (inputStream != null) {
            // to be read from an accessible resource other than a directly-accessible file
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } else {
            // to be read from a file accessible on the file system, but not as a resource
            reader = new BufferedReader(new FileReader(fileName));
        }
        return reader;
    }
}
