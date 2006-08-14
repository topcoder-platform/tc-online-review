/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Question;

/**
 * <p>
 * <code>QuestionBean</code> extends
 * <code>com.topcoder.management.scorecard.data.Question</code>, holds both
 * "question" model data and facility data which bridge the gap between
 * <code>Scorecard Data Structure</code> and the struts front-end mechanism.
 * </p>
 * <p>
 * The facility data introduced in this subclass is:
 * <code>documentUploadValue</code>(which holds the text representation of
 * the "Document Upload" dropdown).
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 * 
 */
public class QuestionAdapter extends Question {
    /** Constant for Document Upload value "Required". */
    public static final String REQUIRED = "Required";

    /** Constant for Document Upload value "Optional". */
    public static final String OPTIONAL = "Optional";

    /** Constant for Document Upload value "N/A". */
    public static final String NA = "N/A";

    /**
     * Value of Document Upload.
     */
    private String documentUploadValue;

    /**
     * <p>
     * Empty constructor.
     * </p>
     */
    public QuestionAdapter() {
        super();
        this.setDescription("Description goes here.");
        this.setGuideline("Guideline goes here.");
        this.setDocumentUploadValue(QuestionAdapter.NA);
        this.setQuestionType(ScorecardActionsHelper.getInstance()
                .getQuestionType(1));
        this.setWeight(100);
    }

    /**
     * <p>
     * Create a QuestionAdapter with given question.
     * </p>
     * 
     * @param question
     *            the question
     */
    public QuestionAdapter(Question question) {
        super();
        if (question.getId() > 0) {
            this.setId(question.getId());
        }
        this.setName(question.getName());
        this.setDescription(question.getDescription());
        this.setGuideline(question.getGuideline());
        this.setQuestionType(question.getQuestionType());
        this.setWeight(question.getWeight());
        this.setUploadDocument(question.isUploadDocument());
        this.setUploadRequired(question.isUploadRequired());
        this.updateDocumentUploadValue();
    }

    /**
     * <p>
     * Return the document upload value.
     * </p>
     * 
     * @return the document upload value
     */
    public String getDocumentUploadValue() {
        return documentUploadValue;
    }

    /**
     * <p>
     * Set the document upload value.
     * </p>
     * 
     * @param documentUploadValue
     *            the document upload value
     */
    public void setDocumentUploadValue(String documentUploadValue) {
        this.documentUploadValue = documentUploadValue;
        if (REQUIRED.equals(documentUploadValue)) {
            this.setUploadDocument(true);
            this.setUploadRequired(true);
        } else if (OPTIONAL.equals(documentUploadValue)) {
            this.setUploadRequired(false);
            this.setUploadDocument(true);
        } else if (NA.equals(documentUploadValue)) {
            this.setUploadRequired(false);
            this.setUploadDocument(false);
        }
    }

    /**
     * <p>
     * Update the Document Upload value according to
     * <code>isUploadDocument()</code> and <code>isUploadRequired()</code>.
     * </p>
     */
    public void updateDocumentUploadValue() {
        if (this.isUploadDocument() && this.isUploadRequired()) {
            this.setDocumentUploadValue(REQUIRED);
        } else if (this.isUploadDocument() && !this.isUploadRequired()) {
            this.setDocumentUploadValue(OPTIONAL);
        } else {
            this.setDocumentUploadValue(NA);
        }
    }
}
