/*
 * Copyright (C) 2005-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;


/**
 * <p>
 * This subclass of <code>ArchiveCreator</code> provides the functionality to create WinZIP-compatible ZIP archives.
 * </p>
 *
 * @author ThinMan, visualage
 * @version 2.0.2
 */
public class ZipCreator implements ArchiveCreator {
    /**
     * <p>
     * Create a ZIP archive containing all files and sub-directories rooted at the specified source directory,
     * including their path information relative to the source directory.
     * </p>
     *
     * <p>
     * If <code>sourceDir</code> is <code>null</code>, source directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>targetFile</code> is relative, archive file is created
     * relative to the current user directory.  If archive file already exists, it is replaced by the newly created
     * archive file. Any error during the creation may result the archive file partially created.
     * </p>
     *
     * @param sourceDir source directory from which to archive files
     * @param targetFile target ZIP archive to create
     *
     * @throws IllegalArgumentException if source directory does not exist, or is not a directory; or
     *         <code>targetFile</code> is a directory
     * @throws NullPointerException if <code>targetFile</code> is <code>null</code>
     * @throws IOException if the parent directory of <code>targetFile</code> does not exist or is not a directory; or
     *         an I/O exception has occurred
     */
    public void archiveFiles(File sourceDir, File targetFile)
        throws IOException {
        if (sourceDir == null) {
            sourceDir = new File(System.getProperty("user.dir"));
        }

        if (!sourceDir.exists()) {
            throw new IllegalArgumentException("Source directory must exist");
        }

        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Source directory must be a directory");
        }

        // Enumerate all files and sub-directories in source directory
        String[] fileNames = sourceDir.list();
        File[] files = new File[fileNames.length];

        for (int fileIndex = 0; fileIndex < fileNames.length; ++fileIndex) {
            files[fileIndex] = new File(fileNames[fileIndex]);
        }

        // Call overload function to perform archiving
        archiveFiles(sourceDir, targetFile, files);
    }

    /**
     * <p>
     * Create a ZIP archive containing specified files and sub-directories rooted at the specified source directory,
     * including their path information relative to the source directory.
     * </p>
     *
     * <p>
     * If <code>sourceDir</code> is <code>null</code>, source directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>targetFile</code> is relative, archive file is created
     * relative to the current user directory.  If archive file already exists, it is replaced by the newly created
     * archive file. Any error during the creation may result the archive file partially created.
     * </p>
     *
     * @param sourceDir source directory from which to archive files
     * @param targetFile target ZIP archive to create
     * @param files array representing the pathnames, relative to source directory, to archive
     *
     * @throws FileNotFoundException if any file or directory in <code>files</code> cannot be located
     * @throws IllegalArgumentException if source directory does not exist, or is not a directory; or
     *         <code>targetFile</code> is a directory; or any file or directory in <code>files</code> is absolute; or
     *         no file to compress
     * @throws NullPointerException if <code>targetFile</code> is <code>null</code>; or <code>files</code> is
     *         <code>null</code>; or any element in <code>files</code> is <code>null</code>
     * @throws IOException if the parent directory of <code>targetFile</code> does not exist or is not a directory; or
     *         an I/O exception has occurred
     */
    public void archiveFiles(File sourceDir, File targetFile, File[] files)
        throws IOException {
        if (targetFile == null) {
            throw new NullPointerException("Target file cannot be null");
        }

        if (files == null) {
            throw new NullPointerException("Files cannot be null");
        }

        if (sourceDir == null) {
            sourceDir = new File(System.getProperty("user.dir"));
        }

        if (!sourceDir.exists()) {
            throw new IllegalArgumentException("Source directory must exist");
        }

        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Source directory must be a directory");
        }

        // Convert absolute and relative pathnames into canonical pathnames
        // Remove the "." and ".." stuff
        // System behaves exactly as defined in specification
        targetFile = targetFile.getCanonicalFile();

        if (targetFile.isDirectory()) {
            throw new IllegalArgumentException("Target file cannot be a directory");
        }

        File parentFile = targetFile.getParentFile();

        if (parentFile == null) {
            throw new IOException("Parent directory of the target file must exist");
        }

        if (!parentFile.exists()) {
            throw new IOException("Parent directory of the target file must exist");
        }

        if (!parentFile.isDirectory()) {
            throw new IOException("Parent directory of the target file must be a directory");
        }

        // Create ZIP file
        ZipOutputStream archiveFile = new ZipOutputStream(new FileOutputStream(targetFile));

        // Call worker method to archive
        archiveFiles(sourceDir, archiveFile, files);

        try {
            archiveFile.close();
        } catch (ZipException e) {
            throw new IllegalArgumentException("Source directory must contain at least one file");
        }
    }

    /**
     * <p>
     * Create a ZIP archive containing specified files and sub-directories rooted at the specified source directory,
     * including their path information relative to the source directory.
     * </p>
     *
     * <p>
     * If <code>sourceDir</code> is <code>null</code>, source directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>targetFile</code> is relative, archive file is created
     * relative to the current user directory.  If archive file already exists, it is replaced by the newly created
     * archive file. Any error during the creation may result the archive file partially created.
     * </p>
     *
     * @param sourceDir source directory from which to archive files
     * @param targetFile target ZIP archive to create
     * @param files <code>List</code> representing the pathnames, relative to source directory, to archive
     *
     * @throws ClassCastException if any element in <code>files</code> is not a <code>File</code> instance
     * @throws FileNotFoundException if any file or directory in <code>files</code> cannot be located
     * @throws IllegalArgumentException if source directory does not exist, or is not a directory; or
     *         <code>targetFile</code> is a directory; or any file or directory in <code>files</code> is absolute
     * @throws NullPointerException if <code>targetFile</code> is <code>null</code>; or <code>files</code> is
     *         <code>null</code>; or any element in <code>files</code> is <code>null</code>
     * @throws IOException if the parent directory of <code>targetFile</code> does not exist or is not a directory; or
     *         an I/O exception has occurred
     */
    public void archiveFiles(File sourceDir, File targetFile, List files)
        throws IOException {
        if (files == null) {
            throw new NullPointerException("Files cannot be null");
        }

        // Call overload function to perform archiving
        try {
            archiveFiles(sourceDir, targetFile, (File[]) files.toArray(new File[0]));
        } catch (ArrayStoreException e) {
            // Any element in files is not a File
            throw new ClassCastException("Element in files must be java.io.File");
        }
    }

    /**
     * <p>
     * Create a ZIP entry name from a <code>File</code> instance. If the given file is an empty pathname, an empty
     * string is returned. If the given file is absolute, ZIP entry name relative to its filesystem root is returned.
     * </p>
     *
     * @param file file to create ZIP entry name from
     * @param alternative true if the alternative entry separator should be used
     *
     * @return ZIP entry name created from <code>file</code>
     *
     * @throws NullPointerException if <code>file</code> is <code>null</code>
     */
    static String createNameFromFile(File file, boolean alternative) {
        if (file == null) {
            throw new NullPointerException("File cannot be null");
        }

        StringBuffer zipEntryName = new StringBuffer(file.getName());
        File currentDir = file.getParentFile();

        while (currentDir != null) {
            zipEntryName.insert(0, (alternative) ?
                ZipArchiver.ALTERNATIVE_ZIP_ENTRY_SEPARATOR : ZipArchiver.ZIP_ENTRY_SEPARATOR);
            zipEntryName.insert(0, currentDir.getName());
            currentDir = currentDir.getParentFile();
        }

        return zipEntryName.toString();
    }

    /**
     * <p>
     * Worker method to archive specified files and sub-directories in the given directory recursively to the given
     * <code>ZipOutputStream</code>, including their path information relative to the source directory.
     * </p>
     *
     * @param sourceDir a <code>File</code> representing the directory from which to archive files.  All paths stored
     *        in the archive will be relative to this one.
     * @param archiveFile a<code>File</code> representing the relative or absolute pathname of the ZIP archive to
     *        create
     * @param files a <code>List</code> of <code>File</code>s representing the pathnames, relative to
     *        <code>sourceDir</code>, to archive
     *
     * @throws FileNotFoundException if a file in <code>files</code> cannot be located
     * @throws NullPointerException if an element in <code>files</code> is <code>null</code>
     * @throws IllegalArgumentException if any file or directory in <code>files</code> is absolute
     * @throws IOException if an I/O exception has occurred; or a ZIP file error has occurred
     */
    private void archiveFiles(File sourceDir, ZipOutputStream archiveFile, File[] files)
        throws IOException {
        int fileIndex;

        for (fileIndex = 0; fileIndex < files.length; ++fileIndex) {
            if (files[fileIndex] == null) {
                throw new NullPointerException("Element in files cannot be null");
            } else if (files[fileIndex].isAbsolute()) {
                throw new IllegalArgumentException("File in files cannot be a file with absolute path");
            } else {
                File currentFile = new File(sourceDir, files[fileIndex].getPath());

                if (!currentFile.exists()) {
                    throw new FileNotFoundException("File in files must exist");
                }

                if (!currentFile.isDirectory()) {
                    // Deflate file
                    FileInputStream input = new FileInputStream(currentFile);

                    try {
                        ZipEntry zipEntry = new ZipEntry(createNameFromFile(files[fileIndex], false));
                        byte[] buffer = new byte[ZipArchiver.BUFFER_SIZE];
                        archiveFile.putNextEntry(zipEntry);

                        int byteRead;

                        while ((byteRead = input.read(buffer)) > 0) {
                            archiveFile.write(buffer, 0, byteRead);
                        }
                    } finally {
                        input.close();
                        archiveFile.closeEntry();
                    }
                } else {
                    // Enumerate files in sub-directory
                    String[] fileNamesInSubdir = currentFile.list();
                    File[] filesInSubdir = new File[fileNamesInSubdir.length];

                    for (int fileInSubdirIndex = 0; fileInSubdirIndex < fileNamesInSubdir.length;
                            ++fileInSubdirIndex) {
                        filesInSubdir[fileInSubdirIndex] = new File(files[fileIndex],
                                fileNamesInSubdir[fileInSubdirIndex]);
                    }

                    // Recursively archive sub-directory
                    archiveFiles(sourceDir, archiveFile, filesInSubdir);
                }
            }
        }
    }
}







