/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.io.File;
import java.util.List;

import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.registry.GroupNotFoundException;
import com.topcoder.file.transfer.registry.RegistryConfigurationException;
import com.topcoder.util.idgenerator.IDGenerator;

/**
 * Mock implementation that used for testing purpose.
 * @author FireIce
 * @version 1.0
 */
public class MockFileSystemXmlRegistry extends FileSystemXmlRegistry {
    /**
     * Creates an instance with the given arguments.
     * @param filesFile
     *            the files' xml file
     * @param groupsFile
     *            the groups' xml file
     * @param idGenerator
     *            the id generator
     * @throws NullPointerException
     *             if any argument is empty
     * @throws RegistryConfigurationException
     *             if there is an exception while initializing the instance
     */
    public MockFileSystemXmlRegistry(File filesFile, File groupsFile, IDGenerator idGenerator)
        throws RegistryConfigurationException {
        super(filesFile, groupsFile, idGenerator);
    }

    /**
     * Gets the list of group's file ids from the registry. The returned list contains String elements, non-null and
     * non-empty.
     * @param groupName
     *            the group name
     * @return the list of file ids
     * @throws GroupNotFoundException
     *             always
     */
    public synchronized List getGroupFiles(String groupName) throws GroupNotFoundException {
        throw new GroupNotFoundException("mock thrown", groupName);
    }
}
