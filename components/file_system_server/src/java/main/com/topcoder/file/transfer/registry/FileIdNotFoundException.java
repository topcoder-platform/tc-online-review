/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

/**
 * This exception is thrown by the FileSystemRegistry if a given file id cannot be found.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FileIdNotFoundException extends FileIdException {

    /**
     * Creates an instance with the given arguments.
     * @param message
     *            a descriptive message
     * @param fileId
     *            the file id
     */
    public FileIdNotFoundException(String message, String fileId) {
        super(message, fileId);
    }
}
