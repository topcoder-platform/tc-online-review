/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import java.io.File;
import java.io.IOException;

import java.util.List;


/**
 * <p>
 * This utility class provides the functionality to create, examine and extract from archive files. Caller can use this
 * class with various <code>Archiver</code> implementations, which encapsulate manipulations on different archive file
 * types (e.g. ZIP, TAR, BZIP2).
 * </p>
 *
 * @author ThinMan, visualage
 * @version 2.0
 *
 * @see Archiver
 */
public class ArchiveUtility {
    /**
     * <p>
     * Archive file manipulated by this <code>ArchiveUtility</code> instance.
     * </p>
     */
    private final File archiveFile;

    /**
     * <p>
     * The <code>Archiver</code> implementation determining the archive format expected and used in this
     * <code>ArchiveUtility</code> instance.
     * </p>
     */
    private final Archiver archiver;

    /**
     * <p>
     * Construct a new <code>ArchiveUtility</code> instance which utilizes the given <code>Archiver</code> to
     * manipulate the specified archive file.
     * </p>
     *
     * <p>
     * If <code>archiveFile</code> is relative, the archive file to manipulate is interpreted relative to the current
     * user directory named by the system property <code>user.dir</code>.
     * </p>
     *
     * @param archiveFile archive file to manipulate
     * @param archiver the <code>Archiver</code> implementation to use
     *
     * @throws NullPointerException if <code>archiveFile</code> is <code>null</code>; or <code>archiver</code> is
     *         <code>null</code>
     */
    public ArchiveUtility(File archiveFile, Archiver archiver) {
        if (archiveFile == null) {
            throw new NullPointerException("Archive file cannot be null");
        }

        if (archiver == null) {
            throw new NullPointerException("Archiver cannot be null");
        }

        this.archiveFile = archiveFile;
        this.archiver = archiver;
    }

    /**
     * <p>
     * Create an archive file containing all files and sub-directories rooted at the specified base directory,
     * including their path information relative to the base directory.
     * </p>
     *
     * <p>
     * If <code>baseDir</code> is <code>null</code>, base directory is the current user directory, which is named by
     * the system property <code>user.dir</code>. If archive file already exists, it is replaced by the newly created
     * archive file. Any error during the creation may result the archive file partially created.
     * </p>
     *
     * @param baseDir base directory from which to archive files
     *
     * @throws IllegalStateException if the archive file is a directory, its parent directory does not exist, or its
     *         parent directory is not a directory
     * @throws IllegalArgumentException if base directory does not exist, or is not a directory
     * @throws IOException if an I/O error has occurred
     */
    public void createArchive(File baseDir) throws IOException {
        if (archiveFile.isDirectory()) {
            throw new IllegalStateException("Archive file cannot be a directory");
        }

        if (!archiveFile.getCanonicalFile().getParentFile().exists()) {
            throw new IllegalStateException("Parent of archive file must exist");
        }

        if (!archiveFile.getCanonicalFile().getParentFile().isDirectory()) {
            throw new IllegalStateException("Parent of archive file must be a directory");
        }

        ArchiveCreator archiveCreator = archiver.createArchiveCreator();
        archiveCreator.archiveFiles(baseDir, archiveFile);
    }

    /**
     * <p>
     * Create an archive file containing selected files and sub-directories rooted at the specified base directory,
     * including their path information relative to the base directory.
     * </p>
     *
     * <p>
     * If <code>baseDir</code> is <code>null</code>, base directory is the current user directory, which is named by
     * the system property <code>user.dir</code>. If archive file already exists, it is replaced by the newly created
     * archive file. Any error during the creation may result the archive file partially created.
     * </p>
     *
     * @param baseDir base directory from which to archive files
     * @param files <code>List</code> representing the pathnames, relative to base directory, to archive
     *
     * @throws ClassCastException if any element in <code>files</code> is not a <code>File</code> instance
     * @throws FileNotFoundException if any file or directory in <code>files</code> cannot be located
     * @throws IllegalStateException if the archive file is a directory, its parent directory does not exist, or its
     *         parent directory is not a directory
     * @throws IllegalArgumentException if base directory does not exist, or is not a directory; or any file or
     *         directory in <code>files</code> is absolute
     * @throws NullPointerException if <code>files</code> is <code>null</code>; or any element in <code>files</code> is
     *         <code>null</code>
     * @throws IOException if an I/O error has occurred
     */
    public void createArchive(File baseDir, List files)
        throws IOException {
        if (archiveFile.isDirectory()) {
            throw new IllegalStateException("Archive file cannot be a directory");
        }

        if (!archiveFile.getCanonicalFile().getParentFile().exists()) {
            throw new IllegalStateException("Parent of archive file must exist");
        }

        if (!archiveFile.getCanonicalFile().getParentFile().isDirectory()) {
            throw new IllegalStateException("Parent of archive file must be a directory");
        }

        ArchiveCreator archiveCreator = archiver.createArchiveCreator();
        archiveCreator.archiveFiles(baseDir, archiveFile, files);
    }

    /**
     * <p>
     * Examine the archive file manipulated by this <code>ArchiveUtility</code> instance. Return a <code>List</code> of
     * <code>File</code> instances containing relative pathnames of all files and/or directories in the archive file.
     * </p>
     *
     * @return <code>List</code> of files in the archive file
     *
     * @throws IOException if the archive file does not exist, is not a file, or is not in the format supported by the
     *         <code>Archiver</code> of this <code>ArchiveUtility</code> instance; or an I/O error has occurred
     */
    public List listContents() throws IOException {
        ArchiveExtractor archiveExtractor = archiver.createArchiveExtractor();

        return archiveExtractor.listFiles(archiveFile);
    }

    /**
     * <p>
     * Extract all files from the archive file manipulated by this <code>ArchiveUtility</code> instance to the
     * specified target directory. The directory structure in archive file is created, and files are placed
     * accordingly.
     * </p>
     *
     * <p>
     * If <code>targetDir</code> is <code>null</code>, target directory is the current user directory, which is named
     * by the system property <code>user.dir</code>.
     * </p>
     *
     * @param targetDir directory to which to place the extracted files
     *
     * @throws IllegalArgumentException if <code>targetDir</code> does not exist, or is not a directory
     * @throws IOException if archive file does not exist, is not a file, or is not in the format supported by the
     *         <code>Archiver</code> of this <code>ArchiveUtility</code> instance; or an I/O error has occurred
     */
    public void extractContents(File targetDir) throws IOException {
        ArchiveExtractor archiveExtractor = archiver.createArchiveExtractor();
        archiveExtractor.extractFiles(archiveFile, targetDir);
    }

    /**
     * <p>
     * Extract selected files from the archive file manipulated by this <code>ArchiveUtility</code> instance to the
     * specified target directory.  The directory structure in archive file is created, and files are placed
     * accordingly.
     * </p>
     *
     * <p>
     * If <code>targetDir</code> is <code>null</code>, target directory is the current user directory, which is named
     * by the system property <code>user.dir</code>.
     * </p>
     *
     * @param targetDir directory to which to place the extracted files
     * @param files array representing the relative pathnames to extract from archive file
     *
     * @throws ClassCastException if any element in <code>files</code> is not a <code>File</code> instance
     * @throws NullPointerException if <code>files</code> is null; or an element in <code>files</code> is null
     * @throws IllegalArgumentException if <code>targetDir</code> does not exist, or is not a directory; or a file in
     *         <code>files</code> cannot be found, or is a directory in archive file
     * @throws IOException if archive file does not exist, is not a file, or is not in the format supported by the
     *         <code>Archiver</code> of this <code>ArchiveUtility</code> instance; or an I/O error has occurred
     */
    public void extractContents(File targetDir, List files)
        throws IOException {
        ArchiveExtractor archiveExtractor = archiver.createArchiveExtractor();
        archiveExtractor.extractFiles(archiveFile, targetDir, files);
    }

    /**
     * <p>
     * Returns a <code>File</code> referring to the archive file manipulated by this <code>ArchiveUtility</code>
     * instance.
     * </p>
     *
     * @return a <code>File</code> referring to the archive file manipulated by this <code>ArchiveUtility</code>
     *         instance
     */
    public File getFile() {
        return archiveFile;
    }
}








