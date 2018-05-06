/*
 * Copyright (C) 2005-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * <p>
 * This subclass of <code>ArchiveExtractor</code> provides the functionality to examine and extract WinZIP-compatible
 * ZIP archives.
 * </p>
 *
 * @author ThinMan, visualage
 * @version 2.0.2
 */
public class ZipExtractor implements ArchiveExtractor {
    /**
     * <p>
     * Extract all files from specified ZIP archive to the specified target directory. The directory structure in ZIP
     * archive is created, and files are placed accordingly.
     * </p>
     *
     * <p>
     * If <code>targetDir</code> is <code>null</code>, target directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>sourceFile</code> is relative, ZIP archive is
     * interpreted relative to the current user directory.
     * </p>
     *
     * @param sourceFile ZIP archive from which to extract files
     * @param targetDir directory to which to place the extracted files
     *
     * @throws NullPointerException if <code>sourceFile</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>targetDir</code> does not exist, or is not a directory
     * @throws IOException if <code>sourceFile</code> does not exist, is not a file, or is not a valid ZIP archive; or
     *         an I/O error has occurred
     */
    public void extractFiles(File sourceFile, File targetDir)
        throws IOException {
        List files = listFiles(sourceFile);
        extractFiles(sourceFile, targetDir, files);
    }

    /**
     * <p>
     * Extract selected files from specified ZIP archive to the specified target directory.  The directory structure in
     * ZIP archive is created, and files are placed accordingly. Empty directories are ignored and not created.
     * </p>
     *
     * <p>
     * If <code>targetDir</code> is <code>null</code>, target directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>sourceFile</code> is relative, ZIP archive is
     * interpreted relative to the current user directory.
     * </p>
     *
     * @param sourceFile ZIP archive from which to extract files
     * @param targetDir directory to which to place the extracted files
     * @param files array representing the relative pathnames to extract from archive file
     *
     * @throws NullPointerException if <code>sourceFile</code> is <code>null</code>; or <code>files</code> is null; or
     *         an element in <code>files</code> is null
     * @throws IllegalArgumentException if <code>targetDir</code> does not exist, or is not a directory; or a file in
     *         <code>files</code> cannot be found
     * @throws IOException if <code>sourceFile</code> does not exist, is not a file, or is not a vlid ZIP archive; or
     *         an I/O error has occurred
     */
    public void extractFiles(File sourceFile, File targetDir, File[] files)
        throws IOException {
        if (sourceFile == null) {
            throw new NullPointerException("Source file cannot be null");
        }

        // Convert absolute and relative pathnames into canonical pathnames
        // Remove the "." and ".." stuff in order to get a correct parent path
        // System behaves exactly as defined in specification
        sourceFile = sourceFile.getCanonicalFile();

        if (!sourceFile.exists()) {
            throw new IOException("Source file must exist");
        }

        if (!sourceFile.isFile()) {
            throw new IOException("Source file must be a file");
        }

        if (targetDir == null) {
            targetDir = new File(System.getProperty("user.dir"));
        }

        if (!targetDir.exists()) {
            throw new IllegalArgumentException("Target directory must exist");
        }

        if (!targetDir.isDirectory()) {
            throw new IllegalArgumentException("Target directory must be a directory");
        }

        InputStream input = null;
        FileOutputStream output = null;

        try {
            ZipFile archiveFile = new ZipFile(sourceFile);

            // Enumerate files
            for (int fileIndex = 0; fileIndex < files.length; ++fileIndex) {
                if (files[fileIndex] == null) {
                    throw new NullPointerException("Element in files cannot be null");
                }

                // Look for the ZipEntry
                String zipEntryName = ZipCreator.createNameFromFile(files[fileIndex], false);

                ZipEntry zipEntry = archiveFile.getEntry(zipEntryName);

                if (zipEntry == null) {
                    zipEntry = archiveFile.getEntry(ZipCreator.createNameFromFile(files[fileIndex], true));
                }

                if (zipEntry == null) {
                    throw new IllegalArgumentException("File in files cannot be found in ZIP archive");
                }

                File currentFile = new File(targetDir, zipEntryName);

                if (!zipEntry.isDirectory()) {
                    // Extract files only
                    File parentDir = currentFile.getParentFile();

                    if (!parentDir.isDirectory()) {
                        if (!parentDir.mkdirs()) {
                            throw new IOException("Directory cannot be created");
                        }
                    }

                    input = archiveFile.getInputStream(zipEntry);

                    // Problem: ZIP entry names created by createNameFromFile do not have trailing '/'
                    //   even if it is a directory in ZIP archive.
                    // Thus, zipEntry.isDirectory() cannot detect whether it is a directory or not.
                    // Solution: if input is null, it is a directory
                    if (input != null) {
                        output = new FileOutputStream(currentFile);

                        byte[] buffer = new byte[ZipArchiver.BUFFER_SIZE];

                        // Extract the file
                        int byteRead;

                        while ((byteRead = input.read(buffer)) > 0) {
                            output.write(buffer, 0, byteRead);
                        }

                        input.close();
                        output.close();

                        input = null;
                        output = null;
                    }
                }
            }
            archiveFile.close();
        }
        // If IOException occurs
        finally {
            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * <p>
     * Extract selected files from specified ZIP archive to the specified target directory.  The directory structure in
     * ZIP archive is created, and files are placed accordingly.
     * </p>
     *
     * <p>
     * If <code>targetDir</code> is <code>null</code>, target directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>sourceFile</code> is relative, ZIP archive is
     * interpreted relative to the current user directory.
     * </p>
     *
     * @param sourceFile ZIP archive from which to extract files
     * @param targetDir directory to which to place the extracted files
     * @param files <code>List</code> representing the relative pathnames to extract from archive file
     *
     * @throws ClassCastException if any element in <code>files</code> is not a <code>File</code> instance
     * @throws NullPointerException if <code>sourceFile</code> is <code>null</code>; or <code>files</code> is null; or
     *         an element in <code>files</code> is null
     * @throws IllegalArgumentException if <code>targetDir</code> does not exist, or is not a directory; or a file in
     *         <code>files</code> cannot be found, or is a directory in archive file
     * @throws IOException if <code>sourceFile</code> does not exist, is not a file, or is not a valid ZIP archive; or
     *         an I/O error has occurred
     */
    public void extractFiles(File sourceFile, File targetDir, List files)
        throws IOException {
        if (files == null) {
            throw new NullPointerException("Files cannot be null");
        }

        // Call overload function to extract files
        try {
            extractFiles(sourceFile, targetDir, (File[]) files.toArray(new File[0]));
        } catch (ArrayStoreException e) {
            // Any element in files is not a File
            throw new ClassCastException("Element in files must be java.io.File");
        }
    }

    /**
     * <p>
     * Examine the specified ZIP archive. Return a <code>List</code> of <code>File</code> containing relative pathnames
     * of all files (except entries representing directories) in the ZIP archive. If <code>sourceFile</code> is
     * relative, ZIP archive is interpreted relative to the current user directory.
     * </p>
     *
     * @param sourceFile file from which to generate the list
     *
     * @return <code>List</code> of files in the specified ZIP archive
     *
     * @throws NullPointerException if <code>sourceFile</code> is <code>null</code>
     * @throws IOException if <code>sourceFile</code> does not exist, is not a file, or is not a valid ZIP archive
     */
    public List listFiles(File sourceFile) throws IOException {
        if (sourceFile == null) {
            throw new NullPointerException("Source file cannot be null");
        }

        if (!sourceFile.isAbsolute()) {
            sourceFile = new File(System.getProperty("user.dir"), sourceFile.getPath());
        }

        if (!sourceFile.exists()) {
            throw new IOException("Source file must exist");
        }

        ArrayList files = new ArrayList();

        try {
            ZipFile archiveFile = new ZipFile(sourceFile);

            // Enumerate every ZipEntry and get the relative name
            for (Enumeration zipEntries = archiveFile.entries(); zipEntries.hasMoreElements();) {
                ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();

                // If ZipEntry is a file, it is added into the list
                if (!zipEntry.isDirectory()) {
                    files.add(createFileFromName(zipEntry.getName()));
                }
            }
            archiveFile.close();
        }
        // Catch bad ZIP file format exception
        catch (ZipException e) {
            throw new IOException("Source file is not a ZIP file");
        }

        return files;
    }

    /**
     * <p>
     * Create a relative <code>File</code> instance from a ZIP entry name. Directories are ignored.
     * </p>
     *
     * @param name ZIP entry name from which to create a <code>File</code> instance
     *
     * @return a relative <code>File</code> instance representing the specified ZIP entry name
     *
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     */
    static File createFileFromName(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }

        StringBuffer fileName = new StringBuffer();

        for (int strPos = 0; strPos < name.length(); ++strPos) {
            if (name.charAt(strPos) == ZipArchiver.ZIP_ENTRY_SEPARATOR) {
                // Avoid adding trailing '/' if the entry name is a directory
                if (strPos != (name.length() - 1)) {
                    fileName.append(File.separatorChar);
                }
            } else {
                fileName.append(name.charAt(strPos));
            }
        }

        return new File(fileName.toString());
    }
}








