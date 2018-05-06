/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.accuracytests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Helper class, provides functionality for copying files.
 *
 * @author fairytale
 * @version 3.0
 */
class AccuracyConfigHelper {
    /**
     * Empty constructor.
     */
    private AccuracyConfigHelper() {
        // empty
    }

    /**
     * Delete a target file.
     *
     * @param location the location of the file.
     */
    public static void deleteFile(String location) {
        File file = new File(location);

        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Copy a file to another location. If the file does not exist, do nothing.
     *
     * @param src the src location of the file.
     * @param dest the destination location of the file.
     */
    public static void copyFile(String src, String dest) {
        File destFile = new File(dest);
        File srcFile = new File(src);

        if (!srcFile.exists()) {
            return;
        }
        // make parent directories if not exist.
        destFile.getParentFile().mkdirs();

        InputStream fin = null;
        OutputStream fout = null;

        try {
            int size = 4096;
            int length = 0;
            fin = new FileInputStream(srcFile);
            fout = new FileOutputStream(destFile);

            // write data from source file to target file.
            byte[] data = new byte[size];

            while ((length = fin.read(data)) != -1) {
                fout.write(data, 0, length);
            }
        } catch (Exception e) {
            try {
                if (fin != null) {
                    fin.close();
                }

                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e1) {
                // ignore
            }
        }
    }
}
