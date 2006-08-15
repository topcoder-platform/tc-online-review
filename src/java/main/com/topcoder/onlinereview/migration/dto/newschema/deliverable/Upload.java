/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.deliverable;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The Upload DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Upload extends BaseDTO {
    /** Represents the upload table name. */
    public static final String TABLE_NAME = "upload";
    public static final int UPLOAD_STATUS_ACTIVE = 1;
    public static final int UPLOAD_STATUS_DELETED = 2;
    public static final int UPLOAD_TYPE_SUBMISSION = 1;
    public static final int UPLOAD_TYPE_TEST_CASE = 2;
    public static final int UPLOAD_TYPE_FINAL_FIX = 3;
    public static final int UPLOAD_TYPE_REVIEW_DOCUMENT = 4;
    private int uploadId;
    private int projectId;
    private int resourceId;
    private int uploadTypeId;
    private int uploadStatusId;
    private String parameter;

    /**
     * Returns the parameter.
     *
     * @return Returns the parameter.
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Set the parameter.
     *
     * @param parameter The parameter to set.
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * Returns the projectId.
     *
     * @return Returns the projectId.
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Set the projectId.
     *
     * @param projectId The projectId to set.
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

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

    /**
     * Returns the uploadStatusId.
     *
     * @return Returns the uploadStatusId.
     */
    public int getUploadStatusId() {
        return uploadStatusId;
    }

    /**
     * Set the uploadStatusId.
     *
     * @param uploadStatusId The uploadStatusId to set.
     */
    public void setUploadStatusId(int uploadStatusId) {
        this.uploadStatusId = uploadStatusId;
    }

    /**
     * Returns the uploadTypeId.
     *
     * @return Returns the uploadTypeId.
     */
    public int getUploadTypeId() {
        return uploadTypeId;
    }

    /**
     * Set the uploadTypeId.
     *
     * @param uploadTypeId The uploadTypeId to set.
     */
    public void setUploadTypeId(int uploadTypeId) {
        this.uploadTypeId = uploadTypeId;
    }
}
