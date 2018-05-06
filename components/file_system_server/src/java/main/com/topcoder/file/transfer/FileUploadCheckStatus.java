/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import com.topcoder.util.collection.typesafeenum.Enum;

/**
 * This class represents the upload check status of an upload file request to the server. The FileSystemClient delegated
 * the upload of the file to a FileUploadWorker. The FIleUploadWorker sends a CHECK_UPLOAD message. If the upload is
 * approved by the server, it will add a (requestId,FileUploadCheckStatus.UPLOAD_ACCEPTED) pair to the statuses map. If
 * the upload is not approved by the server, it will add a (requestId,FileUploadCheckStatus.UPLOAD_NOT_ACCEPTED) pair to
 * the statuses map. The user can query this status by using the getFileUploadCheckStatus(requestId,blocking) method of
 * the client.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FileUploadCheckStatus extends Enum {

    /**
     * Represents the upload accepted status.
     */
    public static final FileUploadCheckStatus UPLOAD_ACCEPTED = new FileUploadCheckStatus("UPLOAD_ACCEPTED");

    /**
     * Represents the upload not accepted status.
     */
    public static final FileUploadCheckStatus UPLOAD_NOT_ACCEPTED = new FileUploadCheckStatus("UPLOAD_NOT_ACCEPTED");

    /**
     * Represents the value of the check upload status. Initialized in the constructor and never changed later. Not
     * null. Not empty.
     */
    private final String value;

    /**
     * Creates an instance with the given argument.
     * @param value
     *            the value of the check upload status
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the string argument is an empty string
     */
    private FileUploadCheckStatus(String value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        } else if (value.trim().length() == 0) {
            throw new IllegalArgumentException("value is empty");
        }
        this.value = value;
    }

    /**
     * Returns the value of the check upload status.
     * @return the value of the check upload status
     */
    public String getValue() {
        return value;
    }
}
