/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

/**
 * The Appeal dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Appeal {
    /** Represents the appeal table name. */
    public static final String TABLE_NAME = "appeal";

    /** Represents appeal_id field name. */
    public static final String APPEAL_ID_NAME = "appeal_id";

    /** Represents appealer_id field name. */
    public static final String APPEALER_ID_NAME = "appealer_id";

    /** Represents question_id field name. */
    public static final String QUESTION_ID_NAME = "question_id";

    /** Represents appeal_text field name. */
    public static final String APPEAL_TEXT_NAME = "appeal_text";

    /** Represents successful_ind field name. */
    public static final String SUCCESSFUL_IND_NAME = "successful_ind";

    /** Represents appeal_response field name. */
    public static final String APPEAL_RESPONSE_NAME = "appeal_response";

    /** Represents raw_evaluation_id field name. */
    public static final String RAW_EVALUATION_ID_NAME = "raw_evaluation_id";

    /** Represents raw_total_tests field name. */
    public static final String RAW_TOTAL_TESTS_NAME = "raw_total_tests";

    /** Represents raw_total_pass field name. */
    public static final String RAW_TOTLA_PASS_NAME = "raw_total_pass";
    private int appealId;
    private int appealerId;
    private int questionId;
    private String appealText;
    private boolean successful;
    private String appealResponse;
    private int rawEvaluationId;
    private int rawTotalTests;
    private int rawTotalPass;

    /**
     * Returns the appealerId.
     *
     * @return Returns the appealerId.
     */
    public int getAppealerId() {
        return appealerId;
    }

    /**
     * Set the appealerId.
     *
     * @param appealerId The appealerId to set.
     */
    public void setAppealerId(int appealerId) {
        this.appealerId = appealerId;
    }

    /**
     * Returns the appealId.
     *
     * @return Returns the appealId.
     */
    public int getAppealId() {
        return appealId;
    }

    /**
     * Set the appealId.
     *
     * @param appealId The appealId to set.
     */
    public void setAppealId(int appealId) {
        this.appealId = appealId;
    }

    /**
     * Returns the appealResponse.
     *
     * @return Returns the appealResponse.
     */
    public String getAppealResponse() {
        return appealResponse;
    }

    /**
     * Set the appealResponse.
     *
     * @param appealResponse The appealResponse to set.
     */
    public void setAppealResponse(String appealResponse) {
        this.appealResponse = appealResponse;
    }

    /**
     * Returns the appealText.
     *
     * @return Returns the appealText.
     */
    public String getAppealText() {
        return appealText;
    }

    /**
     * Set the appealText.
     *
     * @param appealText The appealText to set.
     */
    public void setAppealText(String appealText) {
        this.appealText = appealText;
    }

    /**
     * Returns the questionId.
     *
     * @return Returns the questionId.
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Set the questionId.
     *
     * @param questionId The questionId to set.
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * Returns the rawEvaluationId.
     *
     * @return Returns the rawEvaluationId.
     */
    public int getRawEvaluationId() {
        return rawEvaluationId;
    }

    /**
     * Set the rawEvaluationId.
     *
     * @param rawEvaluationId The rawEvaluationId to set.
     */
    public void setRawEvaluationId(int rawEvaluationId) {
        this.rawEvaluationId = rawEvaluationId;
    }

    /**
     * Returns the rawTotalPass.
     *
     * @return Returns the rawTotalPass.
     */
    public int getRawTotalPass() {
        return rawTotalPass;
    }

    /**
     * Set the rawTotalPass.
     *
     * @param rawTotalPass The rawTotalPass to set.
     */
    public void setRawTotalPass(int rawTotalPass) {
        this.rawTotalPass = rawTotalPass;
    }

    /**
     * Returns the rawTotalTests.
     *
     * @return Returns the rawTotalTests.
     */
    public int getRawTotalTests() {
        return rawTotalTests;
    }

    /**
     * Set the rawTotalTests.
     *
     * @param rawTotalTests The rawTotalTests to set.
     */
    public void setRawTotalTests(int rawTotalTests) {
        this.rawTotalTests = rawTotalTests;
    }

    /**
     * Returns the successful.
     *
     * @return Returns the successful.
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Set the successful.
     *
     * @param successful The successful to set.
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
