/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema;

import com.topcoder.onlinereview.migration.dto.oldschema.review.ScorecardQuestion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * The Scorecard DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardOld {
    /** Represents the scorecard table name. */
    public static final String TABLE_NAME = "scorecard";

    /** Represents scorecard_id field name. */
    public static final String SCORECARD_ID_NAME = "scorecard_id";

    /** Represents score field name. */
    public static final String SCORE_NAME = "score";

    /** Represents author_id field name. */
    public static final String AUTHOR_ID_NAME = "author_id";

    /** Represents submission_id field name. */
    public static final String SUBMISSION_ID_NAME = "submission_id";

    /** Represents project_id field name. */
    public static final String PROJECT_ID_NAME = "project_id";

    /** Represents is_completed field name. */
    public static final String IS_COMPLETED_NAME = "is_completed";

    /** Represents scorecard_type field name. */
    public static final String SCORECARD_TYPE_NAME = "scorecard_type";

    /**
     * Used in resource package, for screening score in resource_info. Used in review package as well. for score of
     * review
     */
    private float score;

    /** Used in review package. */
    private int scorecardId;
    private int authorId;
    private int submissionId;
    private int projectId;
    private int templateId;
    private int scorecardType;
    private boolean isCompleted;
    private Collection scorecardQuestions = new ArrayList();

    /**
     * Returns the authorId.
     *
     * @return Returns the authorId.
     */
    public int getAuthorId() {
        return authorId;
    }

    /**
     * Set the authorId.
     *
     * @param authorId The authorId to set.
     */
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    /**
     * Returns the isCompleted.
     *
     * @return Returns the isCompleted.
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Set the isCompleted.
     *
     * @param isCompleted The isCompleted to set.
     */
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
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
     * Returns the score.
     *
     * @return Returns the score.
     */
    public float getScore() {
        return score;
    }

    /**
     * Set the score.
     *
     * @param score The score to set.
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Returns the scorecardId.
     *
     * @return Returns the scorecardId.
     */
    public int getScorecardId() {
        return scorecardId;
    }

    /**
     * Set the scorecardId.
     *
     * @param scorecardId The scorecardId to set.
     */
    public void setScorecardId(int scorecardId) {
        this.scorecardId = scorecardId;
    }

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
     * Returns the scorecardQuestions.
     *
     * @return Returns the scorecardQuestions.
     */
    public Collection getScorecardQuestions() {
        return scorecardQuestions;
    }

    /**
     * Set the scorecardQuestions.
     *
     * @param scorecardQuestions The scorecardQuestions to set.
     */
    public void setScorecardQuestions(Collection scorecardQuestions) {
        this.scorecardQuestions = scorecardQuestions;
    }

    /**
     * add the scorecardQuestion.
     *
     * @param scorecardQuestion The scorecardQuestion to add.
     */
    public void addScorecardQuestion(ScorecardQuestion scorecardQuestion) {
        if (this.scorecardQuestions == null) {
            this.scorecardQuestions = new ArrayList();
        }

        scorecardQuestions.add(scorecardQuestion);
    }

	/**
	 * @return Returns the templateId.
	 */
	public int getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId The templateId to set.
	 */
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	
	public boolean hasAggResponse() {
		for (Iterator iter = this.scorecardQuestions.iterator(); iter.hasNext();) {
			ScorecardQuestion instance = (ScorecardQuestion) iter.next();
			if (instance.hasAggResponse()) {
				return true;
			}
		}
		return false;
	}

    public boolean hasFinalFix() {
		for (Iterator iter = this.scorecardQuestions.iterator(); iter.hasNext();) {
			ScorecardQuestion instance = (ScorecardQuestion) iter.next();
			if (instance.hasFinalFix()) {
				return true;
			}
		}
		return false;
    }

	/**
	 * @return Returns the scorecardType.
	 */
	public int getScorecardType() {
		return scorecardType;
	}

	/**
	 * @param scorecardType The scorecardType to set.
	 */
	public void setScorecardType(int scorecardType) {
		this.scorecardType = scorecardType;
	}
}
