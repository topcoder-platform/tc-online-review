/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.util.List;

/**
 * Defines the contract that each file searcher should respect. This interface is used by the SearchManager. The
 * implementations of this interface should be thread-safe.
 * @author Luca, FireIce
 * @version 1.0
 */
public interface FileSearcher {
    /**
     * Returns the files that match the given criteria.
     * @param criteria
     *            the search criteria
     * @return a list with the files that match the given criteria
     * @throws NullPointerException
     *             if the argument is null
     * @throws SearchException
     *             if an exception occurs while performing the search
     */
    public List getFiles(String criteria) throws SearchException;
}
