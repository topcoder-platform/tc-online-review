/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

/**
 * This is the base exception for the exceptions related to a file id.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FileIdException extends RegistryException {

    /**
     * Represents the file id for which the exception was raised. Initialized in the constructor and never changed
     * later. Can be null. Can be empty.
     */
    private final String fileId;

    /**
     * Creates an instance with the given arguments. Calls super(message) and assigns the argument to the corresponding
     * field.
     * @param message
     *            a descriptive message
     * @param fileId
     *            the file id
     */
    public FileIdException(String message, String fileId) {
        super(message);
        this.fileId = fileId;
    }

    /**
     * Returns the file id for which the exception was raised. Simply return the corresponding field.
     * @return the file id for which the exception was raised
     */
    public String getFileId() {
        return fileId;
    }
}
