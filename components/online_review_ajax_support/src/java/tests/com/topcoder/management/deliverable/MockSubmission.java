/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * Mock subclass of Submission.
 *
 * @author assistant
 * @version 1.0
 */
public class MockSubmission extends Submission {

    /**
     * Represents the id.
     */
    private long id;

    /**
     * Represents the upload.
     */
    private Upload upload;

    /**
     * Get the id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the upload.
     * @return upload.
     */
    public Upload getUpload() {
        return upload;
    }

    /**
     * Set the upload.
     * @param upload the upload
     */
    public void setUpload(Upload upload) {
        this.upload = upload;
    }
}
