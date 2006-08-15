/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

/**
 * The SubjectiveResp DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class SubjectiveResp {
    /** Represents the subjective_resp table name. */
    public static final String TABLE_NAME = "subjective_resp";

    /** Represents subjective_resp_id field name. */
    public static final String SUBJECTIVE_RESP_ID_NAME = "subjective_resp_id";

    /** Represents question_id field name. */
    public static final String QUESTION_ID_NAME = "question_id";

    /** Represents response_type_id field name. */
    public static final String RESPONSE_TYPE_ID_NAME = "response_type_id";

    /** Represents response_text field name. */
    public static final String RESPONSE_TEXT_NAME = "response_text";
    private int subjectiveRespId;
    private int questionId;
    private int responseTypeId;
    private String responseText;
    private AggResponse aggResponse = null;

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
     * Returns the responseText.
     *
     * @return Returns the responseText.
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * Set the responseText.
     *
     * @param responseText The responseText to set.
     */
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    /**
     * Returns the responseTypeId.
     *
     * @return Returns the responseTypeId.
     */
    public int getResponseTypeId() {
        return responseTypeId;
    }

    /**
     * Set the responseTypeId.
     *
     * @param responseTypeId The responseTypeId to set.
     */
    public void setResponseTypeId(int responseTypeId) {
        this.responseTypeId = responseTypeId;
    }

    /**
     * Returns the subjectiveRespId.
     *
     * @return Returns the subjectiveRespId.
     */
    public int getSubjectiveRespId() {
        return subjectiveRespId;
    }

    /**
     * Set the subjectiveRespId.
     *
     * @param subjectiveRespId The subjectiveRespId to set.
     */
    public void setSubjectiveRespId(int subjectiveRespId) {
        this.subjectiveRespId = subjectiveRespId;
    }

    /**
     * Returns the aggResponses.
     *
     * @return Returns the aggResponses.
     */
    public AggResponse getAggResponse() {
        return aggResponse;
    }

    /**
     * add the aggResponse.
     *
     * @param aggResponse The aggResponse to add.
     */
    public void setAggResponse(AggResponse aggResponse) {
    	this.aggResponse = aggResponse;
    }
    
    public boolean hasFinalFix() {
    	if (this.aggResponse == null) {
    		return false;
    	}
		if (this.aggResponse.getAggRespStatId() == 1 && aggResponse.getFixItem() != null) {
			// 1 means accept
			return true;
		}
		return false;
    }
}