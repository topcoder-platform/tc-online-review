/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.Util;

/**
 * <p>
 * Utility class only used within this sub package.
 * </p>
 *
 * <p>
 * <b>Thread safety:</b> This class is immutable and so thread safe.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
final class CommandsUtil {
    /**
     * <p>
     * Represent the default buffer size used when copying files.
     * </p>
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private CommandsUtil() {
    }

    /**
     * <p>
     * Replace the keywords {FILENAME} or {EXT} with corresponding value
     * (extracted from curSourcePath) in the curDestPath if any.
     * </p>
     *
     * @param curSourcePath
     *            the source path to retrieve filename and extension
     * @param curDestPath
     *            the destination path to process
     * @return the processed destination path
     */
    static String createDestPath(String curSourcePath, String curDestPath) {
        if (curDestPath.contains("{EXT}")) {
            String ext = getExtension(curSourcePath);
            curDestPath = curDestPath.replace("{EXT}", ext);
        }

        if (curDestPath.contains("{FILENAME}")) {
            String fileName = getFileName(curSourcePath);
            curDestPath = curDestPath.replace("{FILENAME}", fileName);
        }
        return curDestPath;
    }

    /**
     * <p>
     * Get extension of the file name.
     * </p>
     *
     * @param fileName
     *            the file name
     * @return the extension or empty if no extension
     */
    static String getExtension(String fileName) {
        // Note: should consider the case '/.../xxx.yyy/zzz', in which extension
        // is empty
        int extBeginIndex = getExtensionIndex(fileName);
        if (extBeginIndex == -1) {
            // no extension
            return "";
        } else {
            return fileName.substring(extBeginIndex);
        }
    }

    /**
     * <p>
     * Get the base file name(with directory prefix removed) of the given
     * fileName.
     *
     * @param fileName
     *            the file name
     * @return the base file name
     */
    static String getFileName(String fileName) {
        int nameBeginIndex = Math.max(fileName.lastIndexOf("/"), fileName
                .lastIndexOf("\\"));
        // Note: if no '/' or '\' found, nameBeginIndex is -1 (like 'file.ext')
        // so nameBeginIndex + 1 is always right beginning index
        int extBeginIndex = getExtensionIndex(fileName);
        if (extBeginIndex != -1) {
            return fileName.substring(nameBeginIndex + 1, extBeginIndex - 1);
        } else {
            return fileName.substring(nameBeginIndex + 1);
        }
    }

    /**
     * <p>
     * Copy the file represented by curSourcePath to the destination curDestPath.
     * </p>
     *
     * @param curSourcePath
     *            the source file name
     * @param curDestPath
     *            the destination file name
     * @throws DistributionScriptCommandExecutionException
     *             if error occurs while copying the file
     */
    static void copyFile(String curSourcePath, String curDestPath)
        throws DistributionScriptCommandExecutionException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(curSourcePath);
            outputStream = new FileOutputStream(curDestPath);
            transfer(inputStream, outputStream);
        } catch (IOException e) {
            throw new DistributionScriptCommandExecutionException(
                    "Error occurs while copying file " + curSourcePath + " to "
                            + curDestPath, e);
        } finally {
            Util.safeClose(inputStream);
            Util.safeClose(outputStream);
        }
    }

    /**
     * <p>
     * Get the beginning index of file's extension.
     * </p>
     *
     * @param fileName
     *            the file name
     * @return the beginning index of file's extension or -1 if no extension
     */
    private static int getExtensionIndex(String fileName) {
        int baseNameBegin = Math.max(Math.max(fileName.lastIndexOf("/"),
                fileName.lastIndexOf("\\")), 0);
        int extBegin = fileName.lastIndexOf(".");
        // for case like xxx.yyy/zzz
        if (baseNameBegin < extBegin) {
            return extBegin + 1;
        }
        return -1;
    }

    /**
     * <p>
     * Transfer the data bytes from the input stream to the output stream.
     * </p>
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     *
     * @throws IOException
     *             if any I/O error occurs
     */
    private static void transfer(InputStream in, OutputStream out)
        throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }
}
