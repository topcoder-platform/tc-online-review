/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.screening;

import com.topcoder.onlinereview.migration.dto.BaseDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The ScreeningTask DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScreeningTask extends BaseDTO {
    /** Represents the screening_task table name. */
    public static final String TABLE_NAME = "screening_task";
    public static final int SCREENING_STATUS_PENDING = 1;
    public static final int SCREENING_STATUS_SCREENING = 2;
    public static final int SCREENING_STATUS_FAILED = 3;
    public static final int SCREENING_STATUS_PASSED = 4;
    public static final int SCREENING_STATUS_PASSED_WITH_WARNING = 5;
    private int screeningTaskId;
    private int uploadId;
    private int screeningStatusId;
    private int screenerId;
    private Date startTimestamp;
    private List screeningResults = new ArrayList();

    /**
     * Returns the screenerId.
     *
     * @return Returns the screenerId.
     */
    public int getScreenerId() {
        return screenerId;
    }

    /**
     * Set the screenerId.
     *
     * @param screenerId The screenerId to set.
     */
    public void setScreenerId(int screenerId) {
        this.screenerId = screenerId;
    }

    /**
     * Returns the screeningStatusId.
     *
     * @return Returns the screeningStatusId.
     */
    public int getScreeningStatusId() {
        return screeningStatusId;
    }

    /**
     * Set the screeningStatusId.
     *
     * @param screeningStatusId The screeningStatusId to set.
     */
    public void setScreeningStatusId(int screeningStatusId) {
        this.screeningStatusId = screeningStatusId;
    }

    /**
     * Returns the screeningTaskId.
     *
     * @return Returns the screeningTaskId.
     */
    public int getScreeningTaskId() {
        return screeningTaskId;
    }

    /**
     * Set the screeningTaskId.
     *
     * @param screeningTaskId The screeningTaskId to set.
     */
    public void setScreeningTaskId(int screeningTaskId) {
        this.screeningTaskId = screeningTaskId;
    }

    /**
     * Returns the startTimestamp.
     *
     * @return Returns the startTimestamp.
     */
    public Date getStartTimestamp() {
        return startTimestamp;
    }

    /**
     * Set the startTimestamp.
     *
     * @param startTimestamp The startTimestamp to set.
     */
    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
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
     * Returns the screeningResults.
     *
     * @return Returns the screeningResults.
     */
    public List getScreeningResults() {
        return screeningResults;
    }

    /**
     * Add the screeningResult.
     *
     * @param screeningResult The screeningResult to add.
     */
    public void addScreeningResult(ScreeningResult screeningResult) {
        if (screeningResults != null) {
            this.screeningResults.add(screeningResult);
        }
    }
}
