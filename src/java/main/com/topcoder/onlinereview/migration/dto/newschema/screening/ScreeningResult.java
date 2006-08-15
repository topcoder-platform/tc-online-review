/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.screening;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The ScreeningResult DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScreeningResult extends BaseDTO {
    /** Represents the screening_result table name. */
    public static final String TABLE_NAME = "screening_result";
    private int screeningResultId;
    private int screeningTaskId;
    private int screeningResponseId;
    private String dynamicResponseText;

    /**
     * Returns the dynamicResponseText.
     *
     * @return Returns the dynamicResponseText.
     */
    public String getDynamicResponseText() {
        return dynamicResponseText;
    }

    /**
     * Set the dynamicResponseText.
     *
     * @param dynamicResponseText The dynamicResponseText to set.
     */
    public void setDynamicResponseText(String dynamicResponseText) {
        this.dynamicResponseText = dynamicResponseText;
    }

    /**
     * Returns the screeningResponseId.
     *
     * @return Returns the screeningResponseId.
     */
    public int getScreeningResponseId() {
        return screeningResponseId;
    }

    /**
     * Set the screeningResponseId.
     *
     * @param screeningResponseId The screeningResponseId to set.
     */
    public void setScreeningResponseId(int screeningResponseId) {
        this.screeningResponseId = screeningResponseId;
    }

    /**
     * Returns the screeningResultId.
     *
     * @return Returns the screeningResultId.
     */
    public int getScreeningResultId() {
        return screeningResultId;
    }

    /**
     * Set the screeningResultId.
     *
     * @param screeningResultId The screeningResultId to set.
     */
    public void setScreeningResultId(int screeningResultId) {
        this.screeningResultId = screeningResultId;
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
}
