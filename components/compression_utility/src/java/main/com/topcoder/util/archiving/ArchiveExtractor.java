/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import java.io.File;
import java.io.IOException;


/**
 * <p>
 * Implemenents of <code>ArchiveCreator</code> encapsulate the functionality to examine and extract archive files for a
 * particular archive file type (e.g. ZIP, TAR, BZIP2).
 * </p>
 *
 * @author ThinMan, visualage
 * @version 2.0
 */
public interface ArchiveExtractor {
    /**
     * <p>
     * Extract all files from specified archive file to the specified target directory.  The directory structure in
     * archive file is created, and files are placed accordingly.
     * </p>
     *
     * <p>
     * If <code>targetDir</code> is <code>null</code>, target directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>sourceFile</code> is relative, archive file is
     * interpreted relative to the current user directory.
     * </p>
     *
     * @param sourceFile archive file from which to extract files
     * @param targetDir directory to which to place the extracted files
     *
     * @throws NullPointerException if <code>sourceFile</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>targetDir</code> does not exist, or is not a directory
     * @throws IOException if <code>sourceFile</code> does not exist, is not a file, or is not in the format expected
     *         by this <code>ArchiveExtractor</code>; or an I/O error has occurred
     */
    public void extractFiles(File sourceFile, File targetDir)
        throws IOException;

    /**
     * <p>
     * Extract selected files from specified archive file to the specified target directory.  The directory structure
     * in archive file is created, and files are placed accordingly.
     * </p>
     *
     * <p>
     * If <code>targetDir</code> is <code>null</code>, target directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>sourceFile</code> is relative, archive file is
     * interpreted relative to the current user directory.
     * </p>
     *
     * @param sourceFile archive file from which to extract files
     * @param targetDir directory to which to place the extracted files
     * @param files array representing the relative pathnames to extract from archive file
     *
     * @throws NullPointerException if <code>sourceFile</code> is <code>null</code>; or <code>files</code> is null; or
     *         an element in <code>files</code> is null
     * @throws IllegalArgumentException if <code>targetDir</code> does not exist, or is not a directory; or a file in
     *         <code>files</code> cannot be found, or is a directory in archive file
     * @throws IOException if <code>sourceFile</code> does not exist, is not a file, or is not in the format expected
     *         by this <code>ArchiveExtractor</code>; or an I/O error has occurred
     */
    public void extractFiles(File sourceFile, File targetDir, File[] files)
        throws IOException;

    /**
     * <p>
     * Extract selected files from specified archive file to the specified target directory.  The directory structure
     * in archive file is created, and files are placed accordingly.
     * </p>
     *
     * <p>
     * If <code>targetDir</code> is <code>null</code>, target directory is the current user directory, which is named
     * by the system property <code>user.dir</code>. If <code>sourceFile</code> is relative, archive file is
     * interpreted relative to the current user directory.
     * </p>
     *
     * @param sourceFile archive file from which to extract files
     * @param targetDir directory to which to place the extracted files
     * @param files <code>List</code> representing the relative pathnames to extract from archive file
     *
     * @throws ClassCastException if any element in <code>files</code> is not a <code>File</code> instance
     * @throws NullPointerException if <code>sourceFile</code> is <code>null</code>; or <code>files</code> is null; or
     *         an element in <code>files</code> is null
     * @throws IllegalArgumentException if <code>targetDir</code> does not exist, or is not a directory; or a file in
     *         <code>files</code> cannot be found, or is a directory in archive file
     * @throws IOException if <code>sourceFile</code> does not exist, is not a file, or is not in the format expected
     *         by this <code>ArchiveExtractor</code>; or an I/O error has occurred
     */
    public void extractFiles(java.io.File sourceFile, java.io.File targetDir, java.util.List files)
        throws IOException;

    /**
     * <p>
     * Examine the specified archive file. Return a <code>List</code> of <code>File</code> containing relative
     * pathnames of all files in the archive file. Directories are ignored. If <code>sourceFile</code> is relative,
     * archive file is interpreted relative to the current user directory.
     * </p>
     *
     * @param sourceFile file from which to generate the list
     *
     * @return <code>List</code> of files in the specified archive file
     *
     * @throws NullPointerException if <code>sourceFile</code> is <code>null</code>
     * @throws IOException if <code>sourceFile</code> does not exist, is not a file, or is not in the format expected
     *         by this <code>ArchiveExtractor</code>
     */
    public java.util.List listFiles(File sourceFile) throws IOException;
}








