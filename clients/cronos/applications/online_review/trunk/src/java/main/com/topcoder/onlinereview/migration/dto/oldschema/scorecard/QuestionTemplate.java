/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.scorecard;

/**
 * The QuestionTemplate dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class QuestionTemplate {
    /** Represents the question_template table name. */
    public static final String TABLE_NAME = "question_template";

    /** Represents q_template_id field name. */
    public static final String Q_TEMPLATE_ID_NAME = "q_template_id";

    /** Represents q_template_v_id field name. */
    public static final String Q_TEMPLATE_V_ID_NAME = "q_template_v_id";

    /** Represents question_type field name. */
    public static final String QUESTION_TYPE_NAME = "question_type";

    /** Represents question_text field name. */
    public static final String QUESTION_TEXT_NAME = "question_text";

    /** Represents question_weight field name. */
    public static final String QUESTION_WEIGHT_NAME = "question_weight";

    /** Represents question_seq_loc field name. */
    public static final String QUESTION_SEC_LOC_NAME = "question_seq_loc";
    private int qTemplateId;
    private int questionType;
    private String questionText;
    private int questionWeight;
    private int questionSecLoc;
    private int qTemplateVid;

    /**
     * Returns the qTemplateId.
     *
     * @return Returns the qTemplateId.
     */
    public int getQTemplateId() {
        return qTemplateId;
    }

    /**
     * Set the qTemplateId.
     *
     * @param templateId The qTemplateId to set.
     */
    public void setQTemplateId(int templateId) {
        qTemplateId = templateId;
    }

    /**
     * Returns the questionSecLoc.
     *
     * @return Returns the questionSecLoc.
     */
    public int getQuestionSecLoc() {
        return questionSecLoc;
    }

    /**
     * Set the questionSecLoc.
     *
     * @param questionSecLoc The questionSecLoc to set.
     */
    public void setQuestionSecLoc(int questionSecLoc) {
        this.questionSecLoc = questionSecLoc;
    }

    /**
     * Returns the questionText.
     *
     * @return Returns the questionText.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Set the questionText.
     *
     * @param questionText The questionText to set.
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Returns the questionType.
     *
     * @return Returns the questionType.
     */
    public int getQuestionType() {
        return questionType;
    }

    /**
     * Set the questionType.
     *
     * @param questionType The questionType to set.
     */
    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    /**
     * Returns the questionWeight.
     *
     * @return Returns the questionWeight.
     */
    public int getQuestionWeight() {
        return questionWeight;
    }

    /**
     * Set the questionWeight.
     *
     * @param questionWeight The questionWeight to set.
     */
    public void setQuestionWeight(int questionWeight) {
        this.questionWeight = questionWeight;
    }

    /**
     * Returns the qTemplateVid.
     *
     * @return Returns the qTemplateVid.
     */
    public int getQTemplateVid() {
        return qTemplateVid;
    }

    /**
     * Set the qTemplateVid.
     *
     * @param templateVid The qTemplateVid to set.
     */
    public void setQTemplateVid(int templateVid) {
        qTemplateVid = templateVid;
    }
}
