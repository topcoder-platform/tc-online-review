/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.resource;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The test of ResourceSubmission.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ResourceSubmission extends BaseDTO {
    /** Represents the resource_info table name. */
    public static final String TABLE_NAME = "resource_submission";
    private int resourceId;
    private int submissionId;

    /**
     * Returns the resourceId.
     *
     * @return Returns the resourceId.
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * Set the resourceId.
     *
     * @param resourceId The resourceId to set.
     */
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Returns the submissionId.
     *
     * @return Returns the submissionId.
     */
    public int getSubmissionId() {
        return submissionId;
    }

    /**
     * Set the submissionId.
     *
     * @param submissionId The submissionId to set.
     */
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }
}
