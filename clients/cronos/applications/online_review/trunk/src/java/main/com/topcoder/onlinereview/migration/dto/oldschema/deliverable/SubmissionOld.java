/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.deliverable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.topcoder.onlinereview.migration.dto.oldschema.ScorecardOld;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RboardApplication;
import com.topcoder.onlinereview.migration.dto.oldschema.review.ScorecardQuestion;
import com.topcoder.onlinereview.migration.dto.oldschema.screening.ScreeningResults;


/**
 * The Submission DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class SubmissionOld {
    /** Represents the submission table name. */
    public static final String TABLE_NAME = "submission";

    /** Represents submission_v_id field name. */
    public static final String SUBMISSION_V_ID_NAME = "submission_v_id";

    /** Represents project_id field name. */
    public static final String PROJECT_ID_NAME = "project_id";

    /** Represents submitter_id field name. */
    public static final String SUBMITTER_ID_NAME = "submitter_id";

    /** Represents cur_version field name. */
    public static final String CUR_VERSION_NAME = "cur_version";

    /** Represents is_removed field name. */
    public static final String IS_REMOVED_NAME = "is_removed";

    /** Represents submission_url field name. */
    public static final String SUBMISSION_URL_NAME = "submission_url";

    /** Represents advanced_to_review field name. */
    public static final String ADVANCED_TO_REVIEW_NAME = "advanced_to_review";

    /** Represents submission_date field name. */
    public static final String SUBMISSION_DATE_NAME = "submission_date";

    /** Represents placement field name. */
    public static final String PLACEMENT_NAME = "placement";

    /** Represents submission_id field name. */
    public static final String SUBMISSION_ID_NAME = "submission_id";

    /** Represents passed_screening field name. */
    public static final String PASSED_SCREENING_NAME = "passed_screening";
    private int submissionVId;
    private int projectId;
    private int submitterId;
    private boolean curVersion;
    private boolean isRemoved;
    private String submissionUrl;
    private boolean advancedToReview;
    private int placement;
    private int submissionId;
    private boolean passedScreening;
    private Date submissionDate;

    private Collection screeningResults = new ArrayList();
    private Collection scorecards = new ArrayList();

    /**
     * Returns the curVersion.
     *
     * @return Returns the curVersion.
     */
    public boolean isCurVersion() {
        return curVersion;
    }

    /**
     * Set the curVersion.
     *
     * @param curVersion The curVersion to set.
     */
    public void setCurVersion(boolean curVersion) {
        this.curVersion = curVersion;
    }

    /**
     * Returns the isRemoved.
     *
     * @return Returns the isRemoved.
     */
    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     * Set the isRemoved.
     *
     * @param isRemoved The isRemoved to set.
     */
    public void setRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
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
     * Returns the submissionUrl.
     *
     * @return Returns the submissionUrl.
     */
    public String getSubmissionUrl() {
        return submissionUrl;
    }

    /**
     * Set the submissionUrl.
     *
     * @param submissionUrl The submissionUrl to set.
     */
    public void setSubmissionUrl(String submissionUrl) {
        this.submissionUrl = submissionUrl;
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

    /**
     * Returns the submitterId.
     *
     * @return Returns the submitterId.
     */
    public int getSubmitterId() {
        return submitterId;
    }

    /**
     * Set the submitterId.
     *
     * @param submitterId The submitterId to set.
     */
    public void setSubmitterId(int submitterId) {
        this.submitterId = submitterId;
    }

    /**
     * Returns the advancedToReview.
     *
     * @return Returns the advancedToReview.
     */
    public boolean isAdvancedToReview() {
        return advancedToReview;
    }

    /**
     * Set the advancedToReview.
     *
     * @param advancedToReview The advancedToReview to set.
     */
    public void setAdvancedToReview(boolean advancedToReview) {
        this.advancedToReview = advancedToReview;
    }

    /**
     * Returns the passed_screening.
     *
     * @return Returns the passed_screening.
     */
    public boolean isPassedScreening() {
        return passedScreening;
    }

    /**
     * Set the passed_screening.
     *
     * @param passedScreening The passedScreening to set.
     */
    public void setPassedScreening(boolean passedScreening) {
        this.passedScreening = passedScreening;
    }

    /**
     * Returns the placement.
     *
     * @return Returns the placement.
     */
    public int getPlacement() {
        return placement;
    }

    /**
     * Set the placement.
     *
     * @param placement The placement to set.
     */
    public void setPlacement(int placement) {
        this.placement = placement;
    }

    /**
     * Returns the submissionDate.
     *
     * @return Returns the submissionDate.
     */
    public Date getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Set the submissionDate.
     *
     * @param submissionDate The submissionDate to set.
     */
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
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
     * Returns the screeningResults.
     *
     * @return Returns the screeningResults.
     */
    public Collection getScreeningResults() {
        return screeningResults;
    }

    /**
     * Set the screeningResults.
     *
     * @param screeningResults The screeningResults to set.
     */
    public void setScreeningResults(Collection screeningResults) {
        this.screeningResults = screeningResults;
    }

    /**
     * add the screeningResult.
     *
     * @param screeningResult The screeningResult to add.
     */
    public void addScreeningResults(ScreeningResults screeningResult) {
        screeningResults.add(screeningResult);
    }

    /**
     * Returns the scorecards.
     *
     * @return Returns the scorecards.
     */
    public Collection getScorecards() {
        return scorecards;
    }

    /**
     * Set the scorecards.
     *
     * @param scorecards The scorecards to set.
     */
    public void setScorecards(Collection scorecards) {
        this.scorecards = scorecards;
    }

    /**
     * add the scorecard.
     *
     * @param scorecard The scorecard to add.
     */
    public void addScorecard(ScorecardOld scorecard) {
        scorecards.add(scorecard);
    }
	
	public boolean hasAggResponse() {
		for (Iterator iter = this.scorecards.iterator(); iter.hasNext();) {
			ScorecardOld instance = (ScorecardOld) iter.next();
			if (instance.hasAggResponse()) {
				return true;
			}
		}
		return false;
	}

    public boolean hasFinalFix() {
		for (Iterator iter = this.scorecards.iterator(); iter.hasNext();) {
			ScorecardOld instance = (ScorecardOld) iter.next();
			if (instance.hasFinalFix()) {
				return true;
			}
		}
		return false;
    }

    public ScorecardOld getScreeningScorecard() {
    	for (Iterator iter = this.scorecards.iterator(); iter.hasNext();) {
    		ScorecardOld pr = (ScorecardOld) iter.next();
    		if (pr.getScorecardType() == 1) {
    			return pr;
    		}
    	}
    	return null;
    }
}
