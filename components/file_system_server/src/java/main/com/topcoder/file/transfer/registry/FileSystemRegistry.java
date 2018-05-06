/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

import java.util.List;
import java.util.Map;

/**
 * Defines the contract that each file system should respect. This interface is used by the FileSystemHandler to
 * maintain a mapping between the file ids and file names, and to maintain a list of groups and their associated files.
 * The registry should have a persistent storage, so it can function after an application has restarted. The
 * implementations should be thread safe. More, all the methods should be synchronized - this is required because some
 * operations require several calls to this instance and, in order to achieve transactional thread safety, the
 * application should perform the calls inside a synchronized block using this instance's lock.
 * @author Luca, FireIce
 * @version 1.0
 */
public interface FileSystemRegistry {
    /**
     * Returns the next unique file id. It has not been used in the registry and it will be used by the file system
     * handler to put a new file in the registry.
     * @return the file id
     * @throws RegistryException
     *             if an exception occured while performing the operation
     */
    public String getNextFileId() throws RegistryException;

    /**
     * Adds a new file record with the given fileId and fileName in the registry.
     * @param fileId
     *            the file id
     * @param fileName
     *            the file name
     * @throws NullPointerException
     *             if any argument is empty
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws FileIdExistsException
     *             if the file id already exists
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void addFile(String fileId, String fileName) throws FileIdExistsException, RegistryPersistenceException;

    /**
     * Updates the file record with the given fileId, by setting the new fileName value in the registry.
     * @param fileId
     *            the file id
     * @param fileName
     *            the file name
     * @throws NullPointerException
     *             if any argument is empty
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void renameFile(String fileId, String fileName) throws FileIdNotFoundException, RegistryPersistenceException;

    /**
     * Removes the file record with the file id equal with the given fileId from the registry. The files are removed
     * from the groups they belong to.
     * @param fileId
     *            the file id
     * @throws NullPointerException
     *             if the argument is empty
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void removeFile(String fileId) throws FileIdNotFoundException, RegistryPersistenceException;

    /**
     * Gets the fileName with the given fileId from the registry.
     * @param fileId
     *            the file id
     * @return the file name
     * @throws NullPointerException
     *             if the argument is empty
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public String getFile(String fileId) throws FileIdNotFoundException, RegistryPersistenceException;

    /**
     * Gets the list of the file ids from the registry. The returned list contains String elements, non-null and
     * non-empty.
     * @return the list of file ids
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public List getFileIds() throws RegistryPersistenceException;

    /**
     * Gets the map of files from the registry. The returned map contains listId-listName pairs. Both the keys and the
     * values are Strings, non-null, non-empty.
     * @return the map of files
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public Map getFiles() throws RegistryPersistenceException;

    /**
     * Creates a new group in the registry. The provided group name must be unique. The file ids must exist and they
     * will be set as the group's files.
     * @param groupName
     *            the group name
     * @param fileIds
     *            the list of file ids
     * @throws NullPointerException
     *             if any argument is empty
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws GroupExistsException
     *             if the group name already exists
     * @throws FileIdNotFoundException
     *             if a file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void createGroup(String groupName, List fileIds) throws GroupExistsException, FileIdNotFoundException,
        RegistryPersistenceException;

    /**
     * Updates the group in the registry. The file ids must exist and they will be set as the group's files.
     * @param groupName
     *            the group name
     * @param fileIds
     *            the list of file ids
     * @throws NullPointerException
     *             if any argument is empty
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws FileIdNotFoundException
     *             if a file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void updateGroup(String groupName, List fileIds) throws GroupNotFoundException, FileIdNotFoundException,
        RegistryPersistenceException;

    /**
     * Removes the group from the registry.
     * @param groupName
     *            the group name
     * @throws NullPointerException
     *             if the argument is empty
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void removeGroup(String groupName) throws GroupNotFoundException, RegistryPersistenceException;

    /**
     * Gets the list of the group names from the registry. The returned list contains String elements, non-null and
     * non-empty.
     * @return the list of group names
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public List getGroupNames() throws RegistryPersistenceException;

    /**
     * Gets the list of group's file ids from the registry. The returned list contains String elements, non-null and
     * non-empty.
     * @param groupName
     *            the group name
     * @return the list of file ids
     * @throws NullPointerException
     *             if the argument is empty
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public List getGroupFiles(String groupName) throws RegistryPersistenceException, GroupNotFoundException;

    /**
     * Adds the file id to the groups list of files in the registry.
     * @param groupName
     *            the group name
     * @param fileId
     *            the file id
     * @throws NullPointerException
     *             if any argument is empty
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void addFileToGroup(String groupName, String fileId) throws GroupNotFoundException, FileIdNotFoundException,
        RegistryPersistenceException;

    /**
     * Remove the file id from the group's list of files in the registry.
     * @param groupName
     *            the group name
     * @param fileId
     *            the file id
     * @throws NullPointerException
     *             if any argument is empty
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void removeFileFromGroup(String groupName, String fileId) throws GroupNotFoundException,
        FileIdNotFoundException, RegistryPersistenceException;
}
