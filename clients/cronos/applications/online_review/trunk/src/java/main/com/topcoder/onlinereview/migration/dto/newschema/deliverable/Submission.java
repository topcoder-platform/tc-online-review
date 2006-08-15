/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.deliverable;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The Submission DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Submission extends BaseDTO {
    /** Represents the submission table name. */
    public static final String TABLE_NAME = "submission";
    public static final int SUBMISSION_STATUS_ACTIVE = 1;
    public static final int SUBMISSION_STATUS_FAILED_SCREENING = 2;
    public static final int SUBMISSION_STATUS_FAILED_REVIEW = 3;
    public static final int SUBMISSION_STATUS_COMPLETED_WITHOUT_WIN = 4;
    public static final int SUBMISSION_STATUS_DELETED = 5;
    private int submissionId;
    private int uploadId;
    private int submissionStatusId;

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

    /**
     * Returns the submissionStatusId.
     *
     * @return Returns the submissionStatusId.
     */
    public int getSubmissionStatusId() {
        return submissionStatusId;
    }

    /**
     * Set the submissionStatusId.
     *
     * @param submissionStatusId The submissionStatusId to set.
     */
    public void setSubmissionStatusId(int submissionStatusId) {
        this.submissionStatusId = submissionStatusId;
    }

    /**
     * Returns the uploadId.
     *
     * @return Returns the uploadId.
     */
    public int getUploadId() {
        return uploadId;
    }

    /**
     * Set the uploadId.
     *
     * @param uploadId The uploadId to set.
     */
    public void setUploadId(int uploadId) {
        this.uploadId = uploadId;
    }
}
