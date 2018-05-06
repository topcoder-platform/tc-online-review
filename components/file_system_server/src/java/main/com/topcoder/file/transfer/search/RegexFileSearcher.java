/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.RegistryPersistenceException;

/**
 * Implements the contract defined by the FileSearcher interface. This class returns a list of file ids whose
 * corresponding file names match the given criteria. The criteria is interpreted as a java regex pattern (this class
 * uses java.util.regex package). It is thread safe. However, it uses a FileSystemRegistry instance which should be
 * thread-safe also.
 * @author Luca, FireIce
 * @version 1.0
 */
public class RegexFileSearcher implements FileSearcher {

    /**
     * Represents the file system registry used by this class to get the required information to perform the searching.
     * Initialized in the constructor and never changed later. Not null.
     */
    private final FileSystemRegistry registry;

    /**
     * Creates an intance with the given registry. Simply assign the argument to the corresponding field.
     * @param registry
     *            the file system registry
     * @throws NullPointerException
     *             if the argument is null
     */
    public RegexFileSearcher(FileSystemRegistry registry) {
        if (registry == null) {
            throw new NullPointerException("registry is null");
        } else {
            this.registry = registry;
        }
    }

    /**
     * Returns a list of file ids whose corresponding file names match the given criteria. The criteria is interpreted
     * as a java regex pattern (this class uses java.util.regex package).
     * @param criteria
     *            the search criteria
     * @return a list with the files that match the given criteria
     * @throws NullPointerException
     *             if the argument is null
     * @throws SearchException
     *             if an exception occurs while performing the search
     */
    public List getFiles(String criteria) throws SearchException {
        if (criteria == null) {
            throw new NullPointerException("criteria is null");
        }
        List fileIds = new ArrayList();
        Pattern p = Pattern.compile(criteria);
        try {
            Map files = registry.getFiles();
            for (Iterator entryIter = files.entrySet().iterator(); entryIter.hasNext();) {
                Entry entry = (Entry) entryIter.next();
                Matcher m = p.matcher((String) entry.getValue());
                if (m.matches()) {
                    fileIds.add((String) entry.getKey());
                }
            }
        } catch (RegistryPersistenceException e) {
            throw new SearchException("registry persistence error", e);
        }
        return fileIds;
    }

}
