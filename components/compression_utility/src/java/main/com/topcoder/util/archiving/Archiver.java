/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

/**
 * <p>
 * Implementations of <code>Archiver</code> encapsulate <code>ArchiveCreator</code> and <code>ArchiveExtractor</code>
 * pairs to archive and extract a particular file type.
 * </p>
 *
 * @author ThinMan, visualage
 * @version 2.0
 *
 * @see ArchiveCreator
 * @see ArchiveExtractor
 */
public interface Archiver {
    /**
     * <p>
     * Return a <code>ArchiveCreator</code> instance that encapsulates archiving functionality for this
     * <code>Archiver</code>'s file type
     * </p>
     *
     * <p>
     * Note: The returned <code>ArchiveCreator</code> may not be newly created if it is thread-safe.
     * </p>
     *
     * @return a <code>ArchiveCreator</code> instance that encapsulates archiving functionality for this
     *         <code>Archiver</code>'s file type
     */
    public ArchiveCreator createArchiveCreator();

    /**
     * <p>
     * Return a <code>ArchiveExtractor</code> instance that encapsulates extraction functionality for this
     * <code>Archiver</code>'s file type
     * </p>
     *
     * <p>
     * Note: The returned <code>ArchiveExtractor</code> may not be newly created if it is thread-safe.
     * </p>
     *
     * @return a <code>ArchiveExtractor</code> instance that encapsulates extraction functionality for this
     *         <code>Archiver</code>'s file type
     */
    public ArchiveExtractor createArchiveExtractor();
}








