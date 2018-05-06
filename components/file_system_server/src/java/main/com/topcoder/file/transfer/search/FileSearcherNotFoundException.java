/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

/**
 * This exception is thrown by the SearchManager if a given file searcher name cannot be found when performing a search.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FileSearcherNotFoundException extends SearcherException {

    /**
     * Represents the file searcher name that was not found by the SearchManager. Initialized in the constructor and
     * never changed later. Can be null or empty.
     */
    private final String fileSearcherName;

    /**
     * Creates an instance with the given arguments. Calls and assignes the other argument to the corresponding field.
     * @param message
     *            a descriptive message
     * @param fileSearcherName
     *            the file searcher name
     */
    public FileSearcherNotFoundException(String message, String fileSearcherName) {
        super(message);
        this.fileSearcherName = fileSearcherName;
    }

    /**
     * Returns the file searcher name that was not found by the SearchManager. Simply return the corresponding field.
     * @return the file searcher name
     */
    public String getFileSearcherName() {
        return fileSearcherName;
    }
}
