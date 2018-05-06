/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

/**
 * This interface provides the contract that each bytes iterator should respect. Instances of this interface can be
 * obtained from the FilePersistences. This interface is needed because large files cannot be sent over the network in
 * just one message using the IPServer. And the files need to be split and sent in smaller consecutive mesages. The
 * subclasses need not to be thread safe, as instances of this interface will be used just by one thread. More threads
 * using this interface will lead to file corruption after the file is reconstructed.
 * @author Luca, FireIce
 * @version 1.0
 */
public interface BytesIterator {
    /**
     * Returns true if there are more bytes to return. If an exception occurs inside this method, dispose() is
     * automatically called before rethrowing the exception. If there are no more bytes to return, dispose() is
     * automatically called before returning.
     * @return true if there are more bytes to return
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public boolean hasNextBytes() throws FilePersistenceException;

    /**
     * Returns the next byte array. If an exception occurs inside this method, dispose() is automatically called before
     * rethrowing the exception. If there are no more bytes to return, dispose() is automatically called before
     * returning.
     * @return the next byte array
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     * @throws NoSuchElementException
     *             there are no more bytes to return
     */
    public byte[] nextBytes() throws FilePersistenceException;

    /**
     * Disposes this instance. This method is needed as the connection between the client and server could be closed or
     * some exception could occur, and the resources have to be disposed.
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void dispose() throws FilePersistenceException;
}
