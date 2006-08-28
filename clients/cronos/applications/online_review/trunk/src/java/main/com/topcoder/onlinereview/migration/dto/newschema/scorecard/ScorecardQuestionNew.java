/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.scorecard;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The ScorecardQuestion dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardQuestionNew extends BaseDTO {
    /** Represents the scorecard_question table name. */
    public static String TABLE_NAME = "scorecard_question";
    private int scorecardQuestionId;
    private int scorecardQuestionTypeId;
    private int scorecardSectionId;
    private String description;
    private String guideline;
    private float weight;
    private int sort;
    private boolean uploadDocument;
    private boolean uploadDocumentRequired;

    /**
     * Returns the description.
     *
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the guideline.
     *
     * @return Returns the guideline.
     */
    public String getGuideline() {
        return guideline;
    }

    /**
     * Set the guideline.
     *
     * @param guideline The guideline to set.
     */
    public void setGuideline(String guideline) {
        this.guideline = guideline;
    }

    /**
     * Returns the scorecardQuestionId.
     *
     * @return Returns the scorecardQuestionId.
     */
    public int getScorecardQuestionId() {
        return scorecardQuestionId;
    }

    /**
     * Set the scorecardQuestionId.
     *
     * @param scorecardQuestionId The scorecardQuestionId to set.
     */
    public void setScorecardQuestionId(int scorecardQuestionId) {
        this.scorecardQuestionId = scorecardQuestionId;
    }

    /**
     * Returns the scorecardQuestionTypeId.
     *
     * @return Returns the scorecardQuestionTypeId.
     */
    public int getScorecardQuestionTypeId() {
        return scorecardQuestionTypeId;
    }

    /**
     * Set the scorecardQuestionTypeId.
     *
     * @param scorecardQuestionTypeId The scorecardQuestionTypeId to set.
     */
    public void setScorecardQuestionTypeId(int scorecardQuestionTypeId) {
        this.scorecardQuestionTypeId = scorecardQuestionTypeId;
    }

    /**
     * Returns the scorecardSectionId.
     *
     * @return Returns the scorecardSectionId.
     */
    public int getScorecardSectionId() {
        return scorecardSectionId;
    }

    /**
     * Set the scorecardSectionId.
     *
     * @param scorecardSectionId The scorecardSectionId to set.
     */
    public void setScorecardSectionId(int scorecardSectionId) {
        this.scorecardSectionId = scorecardSectionId;
    }

    /**
     * Returns the sort.
     *
     * @return Returns the sort.
     */
    public int getSort() {
        return sort;
    }

    /**
     * Set the sort.
     *
     * @param sort The sort to set.
     */
    public void setSort(int sort) {
        this.sort = sort;
    }

    /**
     * Returns the uploadDocument.
     *
     * @return Returns the uploadDocument.
     */
    public boolean isUploadDocument() {
        return uploadDocument;
    }

    /**
     * Set the uploadDocument.
     *
     * @param uploadDocument The uploadDocument to set.
     */
    public void setUploadDocument(boolean uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    /**
     * Returns the uploadDocumentRequired.
     *
     * @return Returns the uploadDocumentRequired.
     */
    public boolean isUploadDocumentRequired() {
        return uploadDocumentRequired;
    }

    /**
     * Set the uploadDocumentRequired.
     *
     * @param uploadDocumentRequired The uploadDocumentRequired to set.
     */
    public void setUploadDocumentRequired(boolean uploadDocumentRequired) {
        this.uploadDocumentRequired = uploadDocumentRequired;
    }

    /**
     * Returns the weight.
     *
     * @return Returns the weight.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Set the weight.
     *
     * @param weight The weight to set.
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
