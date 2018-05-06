/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

/**
 * This interface defines the contract that eac file persistence should respect. It declares methods to create a file
 * (createFile, appendBytes and closeFile), a method to delete a file and a method to retrieve a BytesIterator for a
 * given file. The creation of the file is performed in 3 steps: 1. createFile() - the file is created and the user gets
 * a file id to use in the next steps. 2. appendBytes() - this method should be used several times by the user to write
 * all the bytes. 3. closeFile() - the file is closed The sub-classes do not need to be fully thread safe as the
 * creation of the file should be performed by only one thread, or the file could be corrupted (and no other user should
 * be aware of the file's id in order to try to access the file). The deleteFile and getFileBytesIterator could be
 * accessed by multiple threads, so the methods should be synchronized.
 * @author Luca, FireIce
 * @version 1.0
 */
public interface FilePersistence {
    /**
     * Creates a new file using the given arguments. It returns the file creation id to be used by the user to append
     * the bytes and to close the file.
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @return the file creation id
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public String createFile(String fileLocation, String persistenceFileName) throws FilePersistenceException;

    /**
     * Appends the array of bytes to the file denoted by the fileCreationId argument. If any exception occurs, the
     * resouces related to the fileId should be disposed.
     * @param fileCreationId
     *            the file creation id
     * @param bytes
     *            the bytes to be appended to the file
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if the string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void appendBytes(String fileCreationId, byte[] bytes) throws FilePersistenceException;

    /**
     * Closes the file denoted by the fileCreationId argument. The resouces related to the fileId should be disposed.
     * @param fileCreationId
     *            the file creation id
     * @throws NullPointerException
     *             if the object is null
     * @throws IllegalArgumentException
     *             if the string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void closeFile(String fileCreationId) throws FilePersistenceException;

    /**
     * Deletes the file denoted by the given arguments.
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void deleteFile(String fileLocation, String persistenceFileName) throws FilePersistenceException;

    /**
     * Returns a BytesIterator instance that will iterate over the bytes of the file denoted by the given arguments.
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @return the bytes iterator
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public BytesIterator getFileBytesIterator(String fileLocation, String persistenceFileName)
        throws FilePersistenceException;

    /**
     * Returns the size of the file denoted by the given arguments.
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @return the size of the file
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public long getFileSize(String fileLocation, String persistenceFileName) throws FilePersistenceException;

    /**
     * Disposes this instance. This method is needed as the connection between the client and server could be closed or
     * some exception could occur, and the resources have to be disposed.
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void dispose() throws FilePersistenceException;
}
