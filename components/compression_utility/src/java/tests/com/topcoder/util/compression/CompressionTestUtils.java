/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;

/**
 * <p>Utility methods used in test cases.</p>
 *
 * @author  srowen
 * @version 2.0
 *
 * @since 1.0
 */
final class CompressionTestUtils {

    private CompressionTestUtils() {
    }

    /**
     * <p>Returns <code>true</code> iff the given parts of the two arrays
     * are equal.</p>
     *
     * @param  b1     first byte array
     * @param  offset1 first index of first byte array to compare
     * @param  b2     second byte array
     * @param  offset2 first index of second byte array to compare
     * @param  length  number of byte to compare
     * @return <code>true</code> iff the given parts of the two arrays
     *  are equal
     */
    static boolean arraysEqual(final byte[] b1,
                               final int offset1,
                               final byte[] b2,
                               final int offset2,
                               final int length) {
        for (int i = 0; i < length; i++) {
            if (b1[offset1 + i] != b2[offset2 + i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Returns a useful String representation of a byte array.</p>
     *
     * @param  b      byte array
     * @param  offset first index to output
     * @param  length number of bytes to output
     * @return String representation of part of the byte array
     */
    static String arrayToString(final byte[] b,
                                final int offset,
                                final int length) {
        final StringBuffer result = new StringBuffer(5 * length);
        result.append('{');
        boolean first = true;
        for (int i = 0; i < length; i++) {
            if (first) {
                first = false;
            } else {
                result.append(',');
            }
            result.append(b[offset + i]);
        }
        result.append('}');
        return result.toString();
    }

    /**
     * <p>Reads a File's contents as a String.</p>
     *
     * @param  f File to read
     * @return File's contents as String
     * @throws IOException
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
     * @param  f1 first File to compare
     * @param  f2 second File to compare
     * @return <code>true</code> iff files are identical
     * @throws IOException
     */
    static boolean filesMatch(final File f1, final File f2) throws IOException {
        return readFileAsString(f1).equals(readFileAsString(f2));
    }
}








