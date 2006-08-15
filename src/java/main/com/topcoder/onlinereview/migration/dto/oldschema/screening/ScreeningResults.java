/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.screening;

/**
 * The ScreeningResults DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScreeningResults {
    /** Represents the screening_results table name. */
    public static final String TABLE_NAME = "screening_results";

    /** Represents submission_v_id field name. */
    public static final String SUBMISSION_V_ID_NAME = "submission_v_id";

    /** Represents screening_results_id field name. */
    public static final String SCREENING_RESULTS_ID_NAME = "screening_results_id";

    /** Represents screening_response_id field name. */
    public static final String SCREENING_RESPONSE_ID_NAME = "screening_response_id";

    /** Represents dynamic_response_text field name. */
    public static final String DYNAMIC_RESPONSE_NAME = "dynamic_response_text";
    private int submissionVId;
    private int screeningResultsId;
    private int screeningResponse;
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
     * Returns the screeningResponse.
     *
     * @return Returns the screeningResponse.
     */
    public int getScreeningResponse() {
        return screeningResponse;
    }

    /**
     * Set the screeningResponse.
     *
     * @param screeningResponse The screeningResponse to set.
     */
    public void setScreeningResponse(int screeningResponse) {
        this.screeningResponse = screeningResponse;
    }

    /**
     * Returns the screeningResultsId.
     *
     * @return Returns the screeningResultsId.
     */
    public int getScreeningResultsId() {
        return screeningResultsId;
    }

    /**
     * Set the screeningResultsId.
     *
     * @param screeningResultsId The screeningResultsId to set.
     */
    public void setScreeningResultsId(int screeningResultsId) {
        this.screeningResultsId = screeningResultsId;
    }

    /**
     * Returns the submissionVId.
     *
     * @return Returns the submissionVId.
     */
    public int getSubmissionVId() {
        return submissionVId;
    }

    /**
     * Set the submissionVId.
     *
     * @param submissionVId The submissionVId to set.
     */
    public void setSubmissionVId(int submissionVId) {
        this.submissionVId = submissionVId;
    }
}
