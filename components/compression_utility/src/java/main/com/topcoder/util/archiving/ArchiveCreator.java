/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import java.io.File;
import java.io.IOException;

import java.util.List;


/**
 * <p>
 * Implemenents of <code>ArchiveCreator</code> encapsulate the functionality to create archive for a particular archive
 * file type (e.g. ZIP, TAR, BZIP2).
 * </p>
 *
 * @author ThinMan, visualage
 * @version 2.0
 */
public interface ArchiveCreator {
    /**
     * <p>
     * Create an archive file containing all files and sub-directories rooted at the specified source directory,
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
     * @param targetFile target archive file to create
     *
     * @throws IllegalArgumentException if source directory does not exist, or is not a directory; or
     *         <code>targetFile</code> is a directory
     * @throws NullPointerException if <code>targetFile</code> is <code>null</code>
     * @throws IOException if the parent directory of <code>targetFile</code> does not exist or is not a directory; or
     *         an I/O exception has occurred
     */
    public void archiveFiles(File sourceDir, File targetFile)
        throws IOException;

    /**
     * <p>
     * Create an archive file containing specified files and sub-directories rooted at the specified source directory,
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
     * @param targetFile target archive file to create
     * @param files array representing the pathnames, relative to source directory, to archive
     *
     * @throws FileNotFoundException if any file or directory in <code>files</code> cannot be located
     * @throws IllegalArgumentException if source directory does not exist, or is not a directory; or
     *         <code>targetFile</code> is a directory; or any file or directory in <code>files</code> is absolute
     * @throws NullPointerException if <code>targetFile</code> is <code>null</code>; or <code>files</code> is
     *         <code>null</code>; or any element in <code>files</code> is <code>null</code>
     * @throws IOException if the parent directory of <code>targetFile</code> does not exist or is not a directory; or
     *         an I/O exception has occurred
     */
    public void archiveFiles(File sourceDir, File targetFile, File[] files)
        throws IOException;

    /**
     * <p>
     * Create an archive file containing specified files and sub-directories rooted at the specified source directory,
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
     * @param targetFile target archive file to create
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
        throws IOException;
}








