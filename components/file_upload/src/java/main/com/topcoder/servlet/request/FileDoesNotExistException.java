/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

/**
 * <p>
 * This is an exception class to indicate that the requested file (specified with file id) does not exist in the
 * persistence.
 * </p>
 *
 * <p>
 * This exception is thrown in the <code>FileUpload</code> and <code>UploadedFile</code> classes. These classes
 * retrieve file information using the specified file id.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is immutable and therefore thread safe.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class FileDoesNotExistException extends FileUploadException {
    /**
     * <p>
     * Represents the id of the non-existing file.
     * </p>
     */
    private final String fileId;

    /**
     * <p>
     * Creates a new instance of <code>FileDoesNotExistException</code> class with the non-existing file id.
     * </p>
     *
     * @param fileId the id of the non-existing file.
     */
    public FileDoesNotExistException(String fileId) {
        super("The file for " + fileId + " does not exist.");
        this.fileId = fileId;
    }

    /**
     * <p>
     * Gets the id of the non-existing file.
     * </p>
     *
     * @return the id of the non-existing file.
     */
    public String getFileId() {
        return this.fileId;
    }
}
