/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.RegistryPersistenceException;

/**
 * Implements the contract defined by the GroupSearcher interface. This class returns a list of group names which match
 * the given criteria. The criteria is interpreted as a java regex pattern (this class uses java.util.regex package). It
 * is thread safe. However, it uses a FileSystemRegistry instance which should be thread-safe also.
 * @author Luca, FireIce
 * @version 1.0
 */
public class RegexGroupSearcher implements GroupSearcher {

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
    public RegexGroupSearcher(FileSystemRegistry registry) {
        if (registry == null) {
            throw new NullPointerException("registry is null");
        } else {
            this.registry = registry;
        }
    }

    /**
     * Returns a list of group names which match the given criteria. The criteria is interpreted as a java regex pattern
     * (this class uses java.util.regex package).
     * @param criteria
     *            the search criteria
     * @return a list with the groups that match the given criteria
     * @throws NullPointerException
     *             if the argument is null
     * @throws SearchException
     *             if an exception occurs while performing the search
     */
    public List getGroups(String criteria) throws SearchException {
        if (criteria == null) {
            throw new NullPointerException("criteria is null");
        }
        List groupNameList = new ArrayList();
        Pattern p = Pattern.compile(criteria);
        try {
            List groupNames = registry.getGroupNames();
            for (Iterator groupNameIter = groupNames.iterator(); groupNameIter.hasNext();) {
                String groupName = (String) groupNameIter.next();
                Matcher m = p.matcher(groupName);
                if (m.matches()) {
                    groupNameList.add(groupName);
                }
            }
        } catch (RegistryPersistenceException e) {
            throw new SearchException("registry persistence error", e);
        }
        return groupNameList;
    }
}
