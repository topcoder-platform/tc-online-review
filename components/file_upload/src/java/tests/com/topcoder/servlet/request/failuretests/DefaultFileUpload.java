/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import javax.servlet.ServletRequest;

import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.FileDoesNotExistException;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.PersistenceException;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.RequestParsingException;
import com.topcoder.servlet.request.UploadedFile;

/**
 * Default implementation of the <code>FileUpload</code> class, only for testing purpose.
 * @author FireIce
 * @version 2.0
 */
class DefaultFileUpload extends FileUpload {

    /**
     * default implementation.
     * @param namespace
     *            the namespace.
     * @throws ConfigurationException
     *             if any configuration probem occurs.
     */
    protected DefaultFileUpload(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * mock implementation.
     */
    public FileUploadResult uploadFiles(ServletRequest request, RequestParser parser) throws RequestParsingException,
        PersistenceException {
        return null;
    }

    /**
     * mock implementation.
     */
    public UploadedFile getUploadedFile(String fileId, boolean refresh) throws PersistenceException,
        FileDoesNotExistException {
        return null;
    }

    /**
     * mock implementation.
     */
    public void removeUploadedFile(String fileId) throws PersistenceException, FileDoesNotExistException {

    }

    /**
     * mock implementation.
     */
    public void disconnect() throws PersistenceException {
    }

}
