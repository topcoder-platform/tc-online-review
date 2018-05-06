/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

/**
 * This exception is thrown by the SearchManager if a given group searcher name cannot be found when performing a
 * search.
 * @author Luca, FireIce
 * @version 1.0
 */
public class GroupSearcherNotFoundException extends SearcherException {

    /**
     * Represents the file searcher name that was not found by the SearchManager. Initialized in the constructor and
     * never changed later. Can be null. Can be empty.
     */
    private final String groupSearcherName;

    /**
     * Creates an instance with the given arguments.
     * @param message
     *            a descriptive message
     * @param groupSearcherName
     *            the group searcher name
     */
    public GroupSearcherNotFoundException(String message, String groupSearcherName) {
        super(message);
        this.groupSearcherName = groupSearcherName;
    }

    /**
     * Returns the file searcher name that was not found by the SearchManager. Simply return the corresponding field.
     * @return the group searcher name
     */
    public String getGroupSearcherName() {
        return groupSearcherName;
    }
}
