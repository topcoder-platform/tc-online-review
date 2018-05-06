/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.GroupNotFoundException;
import com.topcoder.file.transfer.registry.RegistryPersistenceException;

/**
 * Implements the contract defined by the GroupSearcher interface. This class returns a list of group names which
 * contain a file id equal with the given criteria. The criteria is interpreted as a file id string. It is thread safe
 * as the access to the FileSystemRegistry instance is done inside a synchronized block.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FileIdGroupSearcher implements GroupSearcher {

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
    public FileIdGroupSearcher(FileSystemRegistry registry) {
        if (registry == null) {
            throw new NullPointerException("registery is null");
        }
        this.registry = registry;
    }

    /**
     * Returns a list of group names which contain a file id equal with the given criteria. The criteria is interpreted
     * as a file id string. This method involves several calls to the registry, so the code should be synchronized.
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
        synchronized (registry) {
            List groupNames;
            try {
                groupNames = registry.getGroupNames();
                for (Iterator iter = groupNames.iterator(); iter.hasNext();) {
                    String groupName = (String) iter.next();
                    List fileIds = registry.getGroupFiles(groupName);
                    if (fileIds.contains(criteria)) {
                        groupNameList.add(groupName);
                    }
                }
            } catch (RegistryPersistenceException e) {
                throw new SearchException("registry persistence error", e);
            } catch (GroupNotFoundException e) {
                // this exception should never happen
                throw new SearchException("group not exist", e);
            }

        }
        return groupNameList;
    }
}
