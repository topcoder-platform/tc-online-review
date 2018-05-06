/*
 * Copyright (C) 2005-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

/**
 * <p>
 * Implementation of <code>Archiver</code> encapsulates the functionality to create and extract WinZIP-compatible ZIP
 * files through <code>ZipCreator</code> and <code>ZipExtractor</code> respectively.
 * </p>
 *
 * @author ThinMan, visualage
 * @version 2.0.2
 *
 * @see ZipCreator
 * @see ZipExtractor
 */
public class ZipArchiver implements Archiver {
    /**
     * <p>
     * Name separator used in zip entries.
     * </p>
     */
    static final char ZIP_ENTRY_SEPARATOR = '/';

    /**
     * <p>
     * Alternative name separator used in zip entries.
     * </p>
     */
    static final char ALTERNATIVE_ZIP_ENTRY_SEPARATOR = '\\';

    /**
     * <p>
     * Size of the buffer processed at each step during archiving and extraction.
     * </p>
     */
    static final int BUFFER_SIZE = 2048;

    /**
     * <p>
     * Return a new <code>ZipCreator</code> instance.
     * </p>
     *
     * @return a new <code>ZipCreator</code> instance
     */
    public ArchiveCreator createArchiveCreator() {
        return new ZipCreator();
    }

    /**
     * <p>
     * Return a new <code>ZipExtractor</code> instance.
     * </p>
     *
     * @return a new <code>ZipExtractor</code> instance
     */
    public ArchiveExtractor createArchiveExtractor() {
        return new ZipExtractor();
    }
}








