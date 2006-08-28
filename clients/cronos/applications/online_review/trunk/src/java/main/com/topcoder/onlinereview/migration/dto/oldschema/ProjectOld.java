/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema;

import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.SubmissionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.Testcase;
import com.topcoder.onlinereview.migration.dto.oldschema.project.CompCatalog;
import com.topcoder.onlinereview.migration.dto.oldschema.project.CompForumXref;
import com.topcoder.onlinereview.migration.dto.oldschema.project.CompVersionDates;
import com.topcoder.onlinereview.migration.dto.oldschema.project.CompVersions;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.ProjectResult;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RUserRole;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RboardApplication;
import com.topcoder.onlinereview.migration.dto.oldschema.review.AggWorksheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * The Project DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectOld {
    /** Represents the project table name. */
    public static final String TABLE_NAME = "project";

    /** Represents project_id field name. */
    public static final String PROJECT_ID_NAME = "project_id";

    /** Represents project_stat_id field name. */
    public static final String PROJECT_STAT_ID_NAME = "project_stat_id";

    /** Represents project_type_id field name. */
    public static final String PROJECT_TYPE_ID_NAME = "project_type_id";

    /** Represents comp_vers_id field name. */
    public static final String COMP_VERS_ID_NAME = "comp_vers_id";

    /** Represents winner_id field name. */
    public static final String WINNER_ID_NAME = "winner_id";

    /** Represents autopilot_ind field name. */
    public static final String AUTOPILOT_IND_NAME = "autopilot_ind";

    /** Represents notes field name. */
    public static final String NOTES_NAME = "notes";

    /** Represents complete_date field name. */
    public static final String COMPLETE_DATE_NAME = "complete_date";

    /** Represents rating_date field name. */
    public static final String RATING_DATE_NAME = "rating_date";

    /** Represents modify_reason field name. */
    public static final String MODIFY_REASON_NAME = "modify_reason";

    /** Represents phase_instance_id field name. */
    public static final String PHASE_INSTANCE_ID_NAME = "phase_instance_id";

    /** Used in project package. */
    private int projectId;
    private int projectStatId;
    private int projectTypeId;
    private int compVersId;
    private int winnerId;
    private boolean autoPilotInd;
    private String notes;
    private Date completeDate;
    private Date ratingDate;
    /** TODO not load. */
    private int projectVId;
    private String modifyReason;

    /** Used in phase package */
    private int phaseInstanceId;
    private CompVersions compVersions;
    private CompForumXref compForumXref;
    private CompCatalog compCatalog;
    private CompVersionDates compVersionDates;
    private List phaseInstances = new ArrayList();
    private List rUserRoles = new ArrayList();
    private List submissions = new ArrayList();
    private List testcases = new ArrayList();
    private List projectResults = new ArrayList();
    private List rboardApplications = new ArrayList();
    private AggWorksheet aggWorksheet = null;
    private List modifiyReasons = new ArrayList();

    /**
     * Returns the autoPilotInd.
     *
     * @return Returns the autoPilotInd.
     */
    public boolean isAutoPilotInd() {
        return autoPilotInd;
    }

    /**
     * Set the autoPilotInd.
     *
     * @param autoPilotInd The autoPilotInd to set.
     */
    public void setAutoPilotInd(boolean autoPilotInd) {
        this.autoPilotInd = autoPilotInd;
    }

    /**
     * Returns the completionDate.
     *
     * @return Returns the completionDate.
     */
    public Date getCompleteDate() {
        return completeDate;
    }

    /**
     * Set the completionDate.
     *
     * @param completionDate The completionDate to set.
     */
    public void setCompleteDate(Date completionDate) {
        this.completeDate = completionDate;
    }

    /**
     * Returns the compVersId.
     *
     * @return Returns the compVersId.
     */
    public int getCompVersId() {
        return compVersId;
    }

    /**
     * Set the compVersId.
     *
     * @param compVersId The compVersId to set.
     */
    public void setCompVersId(int compVersId) {
        this.compVersId = compVersId;
    }

    /**
     * Returns the modifyReason.
     *
     * @return Returns the modifyReason.
     */
    public String getModifyReason() {
        return modifyReason;
    }

    /**
     * Set the modifyReason.
     *
     * @param modifyReason The modifyReason to set.
     */
    public void setModifyReason(String modifyReason) {
        this.modifyReason = modifyReason;
    }

    /**
     * Returns the notes.
     *
     * @return Returns the notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set the notes.
     *
     * @param notes The notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns the phaseInstanceId.
     *
     * @return Returns the phaseInstanceId.
     */
    public int getPhaseInstanceId() {
        return phaseInstanceId;
    }

    /**
     * Set the phaseInstanceId.
     *
     * @param phaseInstanceId The phaseInstanceId to set.
     */
    public void setPhaseInstanceId(int phaseInstanceId) {
        this.phaseInstanceId = phaseInstanceId;
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
     * Returns the projectStatId.
     *
     * @return Returns the projectStatId.
     */
    public int getProjectStatId() {
        return projectStatId;
    }

    /**
     * Set the projectStatId.
     *
     * @param projectStatId The projectStatId to set.
     */
    public void setProjectStatId(int projectStatId) {
        this.projectStatId = projectStatId;
    }

    /**
     * Returns the projectTypeId.
     *
     * @return Returns the projectTypeId.
     */
    public int getProjectTypeId() {
        return projectTypeId;
    }

    /**
     * Set the projectTypeId.
     *
     * @param projectTypeId The projectTypeId to set.
     */
    public void setProjectTypeId(int projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    /**
     * Returns the projectVId.
     *
     * @return Returns the projectVId.
     */
    public int getProjectVId() {
        return projectVId;
    }

    /**
     * Set the projectVId.
     *
     * @param projectVId The projectVId to set.
     */
    public void setProjectVId(int projectVId) {
        this.projectVId = projectVId;
    }

    /**
     * Returns the ratingDate.
     *
     * @return Returns the ratingDate.
     */
    public Date getRatingDate() {
        return ratingDate;
    }

    /**
     * Set the ratingDate.
     *
     * @param ratingDate The ratingDate to set.
     */
    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    /**
     * Returns the compVersions.
     *
     * @return Returns the compVersions.
     */
    public CompVersions getCompVersions() {
        return compVersions;
    }

    /**
     * Set the compVersions.
     *
     * @param compVersions The compVersions to set.
     */
    public void setCompVersions(CompVersions compVersions) {
        this.compVersions = compVersions;
    }

    /**
     * Returns the compForumXref.
     *
     * @return Returns the compForumXref.
     */
    public CompForumXref getCompForumXref() {
        return compForumXref;
    }

    /**
     * Set the compForumXref.
     *
     * @param compForumXref The compForumXref to set.
     */
    public void setCompForumXref(CompForumXref compForumXref) {
        this.compForumXref = compForumXref;
    }

    /**
     * Returns the compCatalog.
     *
     * @return Returns the compCatalog.
     */
    public CompCatalog getCompCatalog() {
        return compCatalog;
    }

    /**
     * Set the compCatalog.
     *
     * @param compCatalog The compCatalog to set.
     */
    public void setCompCatalog(CompCatalog compCatalog) {
        this.compCatalog = compCatalog;
    }

    /**
     * Returns the winnerId.
     *
     * @return Returns the winnerId.
     */
    public int getWinnerId() {
        return winnerId;
    }

    /**
     * Set the winnerId.
     *
     * @param winnerId The winnerId to set.
     */
    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    /**
     * Returns the compVersionDates.
     *
     * @return Returns the compVersionDates.
     */
    public CompVersionDates getCompVersionDates() {
        return compVersionDates;
    }

    /**
     * Set the compVersionDates.
     *
     * @param compVersionDates The compVersionDates to set.
     */
    public void setCompVersionDates(CompVersionDates compVersionDates) {
        this.compVersionDates = compVersionDates;
    }

    /**
     * Returns the phaseInstances.
     *
     * @return Returns the phaseInstances.
     */
    public List getPhaseInstances() {
        return phaseInstances;
    }

    /**
     * Set the phaseInstances.
     *
     * @param phaseInstances The phaseInstances to set.
     */
    public void setPhaseInstances(List phaseInstances) {
        this.phaseInstances = phaseInstances;
    }

    /**
     * Returns the rUserRoles.
     *
     * @return Returns the rUserRoles.
     */
    public List getRUserRoles() {
        return rUserRoles;
    }

    /**
     * Set the rUserRoles.
     *
     * @param userRoles The rUserRoles to set.
     */
    public void setRUserRoles(List userRoles) {
        rUserRoles = userRoles;
    }

    /**
     * add the rUserRole.
     *
     * @param userRole The userRole to add.
     */
    public void addRUserRole(RUserRole userRole) {
        if (this.rUserRoles == null) {
            this.rUserRoles = new ArrayList();
        }

        rUserRoles.add(userRole);
    }

    /**
     * Returns the submissions.
     *
     * @return Returns the submissions.
     */
    public List getSubmissions() {
        return submissions;
    }

    /**
     * Set the submissions.
     *
     * @param submissions The submissions to set.
     */
    public void setSubmissions(List submissions) {
        this.submissions = submissions;
    }

    /**
     * add the submission.
     *
     * @param submission The submission to add.
     */
    public void addSubmission(SubmissionOld submission) {
        if (this.submissions == null) {
            this.submissions = new ArrayList();
        }

        submissions.add(submission);
    }

    /**
     * Returns the testcases.
     *
     * @return Returns the testcases.
     */
    public List getTestcases() {
        return testcases;
    }

    /**
     * Set the testcases.
     *
     * @param testcases The testcases to set.
     */
    public void setTestcases(List testcases) {
        this.testcases = testcases;
    }

    /**
     * add the testcase.
     *
     * @param testcase The testcase to add.
     */
    public void addTestcase(Testcase testcase) {
        if (this.testcases == null) {
            this.testcases = new ArrayList();
        }

        testcases.add(testcase);
    }

    /**
     * Returns the projectResults.
     *
     * @return Returns the projectResults.
     */
    public List getProjectResults() {
        return projectResults;
    }

    /**
     * Set the projectResults.
     *
     * @param projectResults The projectResults to set.
     */
    public void setProjectResults(List projectResults) {
        this.projectResults = projectResults;
    }

    /**
     * add the projectResult.
     *
     * @param projectResult The projectResult to add.
     */
    public void addProjectResult(ProjectResult projectResult) {
        if (this.projectResults == null) {
            this.projectResults = new ArrayList();
        }

        projectResults.add(projectResult);
    }

    /**
     * Returns the rboardApplications.
     *
     * @return Returns the rboardApplications.
     */
    public List getRboardApplications() {
        return rboardApplications;
    }

    /**
     * Set the rboardApplications.
     *
     * @param rboardApplications The rboardApplications to set.
     */
    public void setRboardApplications(List rboardApplications) {
        this.rboardApplications = rboardApplications;
    }

    /**
     * add the rboardApplication.
     *
     * @param rboardApplication The rboardApplication to add.
     */
    public void addRboardApplication(RboardApplication rboardApplication) {
        if (this.rboardApplications == null) {
            this.rboardApplications = new ArrayList();
        }

        rboardApplications.add(rboardApplication);
    }
    /**
     * Returns the aggWorksheets.
     *
     * @return Returns the aggWorksheets.
     */
    public AggWorksheet getAggWorksheet() {
        return aggWorksheet;
    }

    /**
     * Set the aggWorksheets.
     *
     * @param aggWorksheets The aggWorksheets to set.
     */
    public void setAggWorksheet(AggWorksheet aggWorksheet) {
        this.aggWorksheet = aggWorksheet;
    }

    /**
     * Returns the modifiyReasons.
     *
     * @return Returns the modifiyReasons.
     */
    public List getModifiyReasons() {
        return modifiyReasons;
    }

    /**
     * Set the modifiyReasons.
     *
     * @param modifiyReasons The modifiyReasons to set.
     */
    public void setModifiyReasons(List modifiyReasons) {
        this.modifiyReasons = modifiyReasons;
    }

    /**
     * add the modifiyReason.
     *
     * @param modifiyReason The modifiyReason to add.
     */
    public void addModifiyReason(String modifiyReason) {
        if (modifiyReason == null) {
            return;
        }

        modifiyReasons.add(modifiyReason);
    }
    
    public ProjectResult getProjectResultByUserId(int userId) {
    	for (Iterator iter = this.projectResults.iterator(); iter.hasNext();) {
    		ProjectResult pr = (ProjectResult) iter.next();
    		if (pr.getUserId() == userId) {
    			return pr;
    		}
    	}
    	return null;
    }
    
    public RboardApplication getRboardApplicationByUserId(int userId) {
    	for (Iterator iter = this.rboardApplications.iterator(); iter.hasNext();) {
    		RboardApplication pr = (RboardApplication) iter.next();
    		if (pr.getUserId() == userId) {
    			return pr;
    		}
    	}
    	return null;
    }

    public ScorecardOld getScreeningScorecardBySubmitter(int userId) {
    	for (Iterator iter = this.submissions.iterator(); iter.hasNext();) {
    		SubmissionOld pr = (SubmissionOld) iter.next();
    		if (pr.getSubmissionId() == userId) {
    			return pr.getScreeningScorecard();
    		}
    	}
    	return null;
    }
}