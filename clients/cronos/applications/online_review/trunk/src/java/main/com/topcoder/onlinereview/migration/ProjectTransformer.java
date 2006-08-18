/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Submission;
import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Upload;
import com.topcoder.onlinereview.migration.dto.newschema.phase.Phase;
import com.topcoder.onlinereview.migration.dto.newschema.phase.PhaseCriteria;
import com.topcoder.onlinereview.migration.dto.newschema.phase.PhaseDependency;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectAudit;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectInfo;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.newschema.resource.Notification;
import com.topcoder.onlinereview.migration.dto.newschema.resource.Resource;
import com.topcoder.onlinereview.migration.dto.newschema.resource.ResourceInfo;
import com.topcoder.onlinereview.migration.dto.newschema.resource.ResourceSubmission;
import com.topcoder.onlinereview.migration.dto.newschema.screening.ScreeningResult;
import com.topcoder.onlinereview.migration.dto.newschema.screening.ScreeningTask;
import com.topcoder.onlinereview.migration.dto.oldschema.ProjectOld;
import com.topcoder.onlinereview.migration.dto.oldschema.ScorecardOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.SubmissionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.Testcase;
import com.topcoder.onlinereview.migration.dto.oldschema.phase.PhaseInstance;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.PaymentInfo;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.ProjectResult;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RUserRole;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RboardApplication;
import com.topcoder.onlinereview.migration.dto.oldschema.screening.ScreeningResults;

import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * The Transformer which is used to transform project data.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectTransformer extends MapUtil{
    private static final String PROJECT_ID_SEQ_NAME = "project_id_seq";
    private static final String PROJECT_AUDIT_ID_SEQ_NAME = "project_audit_id_seq";
    private static final String PHASE_ID_SEQ_NAME = "phase_id_seq";
    private static final String RESOURCE_ID_SEQ_NAME = "resource_id_seq";
    private static final String SCREENING_TASK_ID_SEQ_NAME = "screening_task_id_seq";
    private static final String SCREENING_RESULTS_ID_SEQ_NAME = "screening_result_id_seq";
    private static final String SUBMISSION_ID_SEQ_NAME = "submission_id_seq";
    private static final String UPLOAD_ID_SEQ_NAME = "upload_id_seq";
    private static final String REVIEW_ID_SEQ_NAME = "review_id_seq";
    private static final String REVIEW_ITEM_ID_SEQ_NAME = "review_item_id_seq";
    private static final String REVIEW_COMMENT_ID_SEQ_NAME = "review_comment_id_seq";
    private static final String REVIEW_ITEM_COMMENT_ID_SEQ_NAME = "review_item_comment_id_seq";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
    private IDGenerator projectIdGenerator = null;
    private IDGenerator projectAuditIdGenerator = null;
    private IDGenerator phaseIdGenerator = null;
    private IDGenerator resourceIdGenerator = null;
    private IDGenerator screeningTaskIdGenerator = null;
    private IDGenerator screeningResultsIdGenerator = null;
    private IDGenerator submissionIdGenerator = null;
    private IDGenerator uploadIdGenerator = null;
    private IDGenerator reviewIdGenerator = null;
    private IDGenerator reviewItemIdGenerator = null;
    private IDGenerator reviewCommentIdGenerator = null;
    private IDGenerator reviewItemCommentIdGenerator = null;
    private static Map roleToPhaseTypes = new HashMap();
    
    static {
    	// Later it will retrieve from resource_role_lu
    	// resource_role_id to phase_type_id
    	// Notes: the phase_type_id can be null
    	roleToPhaseTypes.put(new Integer(2), new Integer(3));
    	roleToPhaseTypes.put(new Integer(3), new Integer(3));
    	roleToPhaseTypes.put(new Integer(4), new Integer(4));
    	roleToPhaseTypes.put(new Integer(5), new Integer(4));
    	roleToPhaseTypes.put(new Integer(6), new Integer(4));
    	roleToPhaseTypes.put(new Integer(7), new Integer(4));
    	roleToPhaseTypes.put(new Integer(8), new Integer(7));
    	roleToPhaseTypes.put(new Integer(9), new Integer(10));
    	roleToPhaseTypes.put(new Integer(10), new Integer(11));
    }

    /**
     * Creates a new ProjectTransformer object.
     *
     * @throws IDGenerationException if error occurs while get idgenerator
     */
    public ProjectTransformer() throws IDGenerationException {
        projectIdGenerator = IDGeneratorFactory.getIDGenerator(PROJECT_ID_SEQ_NAME);
        projectAuditIdGenerator = IDGeneratorFactory.getIDGenerator(PROJECT_AUDIT_ID_SEQ_NAME);
        phaseIdGenerator = IDGeneratorFactory.getIDGenerator(PHASE_ID_SEQ_NAME);
        resourceIdGenerator = IDGeneratorFactory.getIDGenerator(RESOURCE_ID_SEQ_NAME);
        screeningTaskIdGenerator = IDGeneratorFactory.getIDGenerator(SCREENING_TASK_ID_SEQ_NAME);
        screeningResultsIdGenerator = IDGeneratorFactory.getIDGenerator(SCREENING_RESULTS_ID_SEQ_NAME);
        submissionIdGenerator = IDGeneratorFactory.getIDGenerator(SUBMISSION_ID_SEQ_NAME);
        uploadIdGenerator = IDGeneratorFactory.getIDGenerator(UPLOAD_ID_SEQ_NAME);
        reviewIdGenerator = IDGeneratorFactory.getIDGenerator(REVIEW_ID_SEQ_NAME);
        reviewItemIdGenerator = IDGeneratorFactory.getIDGenerator(REVIEW_ITEM_ID_SEQ_NAME);
        reviewCommentIdGenerator = IDGeneratorFactory.getIDGenerator(REVIEW_COMMENT_ID_SEQ_NAME);
        reviewItemCommentIdGenerator = IDGeneratorFactory.getIDGenerator(REVIEW_ITEM_COMMENT_ID_SEQ_NAME);
    }

    /**
     * Transform data to Project
     *
     * @param inputs the ScorecardTemplate data
     *
     * @return Scorecard data
     *
     * @throws IDGenerationException
     */
    public List transformProject(List inputs) throws Exception {
		Util.start("transformProject");
        List list = new ArrayList(inputs.size());

        for (Iterator iter = inputs.iterator(); iter.hasNext();) {
            ProjectOld input = (ProjectOld) iter.next();
            ProjectNew output = new ProjectNew();
            list.add(output);

            // Prepare project
            prepareProject(input, output);
            prepareProjectAudit(input, output);
            prepareProjectInfos(input, output);
            
            // Prepare phase:
            preparePhases(input, output);
            preparePhaseDependencys(output);

        	// Prepare Resources, dependent on transformed phase information
        	prepareResources(input, output);

            // Prepare deliverable: 
        	// dependent on resource 
            // submission/testcase to upload
            // submission to submission
            prepareUpload(input, output);

            // Prepare screening
            // screening_results to screening_task
            // screening_results to screening_result
            // Notes: is done in upload->submission

            // it will be done in upload prepare review
        }

        Util.logAction(list.size(), "transformProject");
        return list;
    }

    /**
     * Prepare Review info for this project.
     * 
     * @param input the old project
     * @param output the new project
     * @throws IDGenerationException error occurs while generate new id
     */
    private void prepareReview(ProjectOld oldProject, ProjectNew project, SubmissionOld oldSubmission, Submission submission) throws Exception {
		Util.start("prepareReview");    	
		ReviewConverter converter = new ReviewConverter(
				oldProject,
				project,
				oldSubmission,
				submission);
		converter.setReviewIdGenerator(reviewIdGenerator);
		converter.setReviewItemIdGenerator(reviewItemIdGenerator);
		converter.setReviewCommentIdGenerator(reviewCommentIdGenerator);
		converter.setReviewItemCommentIdGenerator(reviewItemCommentIdGenerator);
		project.addReviews(converter.convert());
		Util.logAction("prepareReview");
    }

    /**
     * Prepare Screening info for this project.
     * 
     * @throws IDGenerationException error occurs while generate new id
     */
    private ScreeningTask prepareScreening(SubmissionOld oldSubmission, int uploadId) throws IDGenerationException {
		Util.start("prepareScreening");   
    	Collection results = oldSubmission.getScreeningResults();
    	ScreeningTask task = new ScreeningTask();
    	task.setScreeningTaskId((int) screeningTaskIdGenerator.getNextID());
    	// from submission_v_id
    	task.setUploadId(uploadId);
    	task.setScreeningStatusId(getScreeningStatus(results));
    	task.setScreenerId(0); // always 0
    	task.setStartTimestamp(new Date()); // current timestamp
    	setBaseDTO(task);
    	for (Iterator iter = results.iterator(); iter.hasNext();) {
    		task.addScreeningResult(prepareScreeningResult((ScreeningResults) iter.next(), task.getScreeningTaskId()));
    	}
		Util.logAction("prepareScreening");
    	return task;
    }

    /**
     * Prepare Screening result for this project.
     * 
     * @return results
     * @throws IDGenerationException error occurs while generate new id
     */
    private ScreeningResult prepareScreeningResult(ScreeningResults old, int taskId) throws IDGenerationException {
    	// Prepare all new screening results
    	Util.start("prepareScreeningResult");   
    	ScreeningResult result = new ScreeningResult();
    	result.setScreeningResultId((int) screeningResultsIdGenerator.getNextID());
    	result.setScreeningTaskId(taskId);
    	result.setScreeningResponseId(old.getScreeningResponse());
    	result.setDynamicResponseText(old.getDynamicResponseText());
    	setBaseDTO(result);
		Util.logAction("prepareScreeningResult");
    	return result;
    }

    /**
     * Return screening status with given results.
     * 
     * @param results the results
     * @return screening status
     */
    private static int getScreeningStatus(Collection results) {
    	int status = ScreeningTask.SCREENING_STATUS_PASSED;
    	for (Iterator iter = results.iterator(); iter.hasNext();) {
    		ScreeningResults result = (ScreeningResults) iter.next();
    		int middle = MapUtil.getResponseSeverity(result.getScreeningResponse());
    		if (middle == 1) {
    			// maps to 'Failed' if there is a response with severity 'Fatal', 
    			// needn't to check further
    			return ScreeningTask.SCREENING_STATUS_FAILED;
    		} else if (middle == 3) {
    	    	// otherwise to 'Passed'
    			continue;
    		} else {
    			// to 'Passed with Warning' if there is a response with severity of 'Warning', 
    			status = ScreeningTask.SCREENING_STATUS_PASSED_WITH_WARNING;
    		}
    	}
    	return status;
    }

    /**
     * Prepare Upload info for this project.
     * 
     * @param input the old project
     * @param output the new project
     * @throws IDGenerationException error occurs while generate new id
     */
    private void prepareUpload(ProjectOld input, ProjectNew output) throws Exception {
    	Util.start("prepareUpload");   
    	Collection submissions = input.getSubmissions();
    	for (Iterator iter = submissions.iterator(); iter.hasNext();) {
    		SubmissionOld submission = (SubmissionOld) iter.next();
    		
    		Upload upload = new Upload();

    		// submission.project_id
    		upload.setProjectId(output.getProjectId());

    		// submission.submitter_id
    		upload.setResourceId(MapUtil.getResourceId(output, submission.getSubmitterId()));

    		// all submissions made in the submission phase map to 'Submission', otherwise map to 'Final Fix'
    		if (isSubmissionPhase(input, submission.getSubmissionDate())) {
    			upload.setUploadTypeId(Upload.UPLOAD_TYPE_SUBMISSION);
    		} else {
    			upload.setUploadTypeId(Upload.UPLOAD_TYPE_FINAL_FIX);
    		}

    		if (upload.getUploadTypeId() == Upload.UPLOAD_TYPE_SUBMISSION) {
    			// submission
    			if (!submission.isRemoved()) {
    				// submissions not removed map to 'Active'
    				upload.setUploadStatusId(Upload.UPLOAD_STATUS_ACTIVE);
    			} else {
    				// others map to 'Deleted'
    				upload.setUploadStatusId(Upload.UPLOAD_STATUS_DELETED);
    			}
    		} else {
    			// Final fix
    			if (submission.isCurVersion()) {
    				// the current 'Final Fix' maps to 'Active'
    				upload.setUploadStatusId(Upload.UPLOAD_STATUS_ACTIVE);
    			} else {
    				// others map to 'Deleted'
    				upload.setUploadStatusId(Upload.UPLOAD_STATUS_DELETED);
    			}
    		}
    		upload.setParameter(submission.getSubmissionUrl());
        	setBaseDTO(upload);
    		// submission.submission_v_id
    		upload.setUploadId((int) uploadIdGenerator.getNextID());
    		output.addUpload(upload);
    		
    		if (upload.getUploadTypeId() == Upload.UPLOAD_TYPE_SUBMISSION && submission.isCurVersion()) {
    			Submission newSubmission = prepareSubmission(input, submission, upload.getUploadId());
    			
    			// add submission
    			output.addSubmission(newSubmission);
    			
    			// Prepare resource submission
    			ResourceSubmission rs = new ResourceSubmission();
    			rs.setSubmissionId(newSubmission.getSubmissionId());
    			rs.setResourceId(upload.getResourceId());
            	setBaseDTO(rs);
            	output.addResourceSubmission(rs);
            	
    			prepareReview(input, output, submission, newSubmission);
    		}

			// add screeningTask
			output.addScreeningTask(prepareScreening(submission, upload.getUploadId()));
    	}

    	for (Iterator iter = input.getTestcases().iterator(); iter.hasNext();) {
    		Testcase testcase = (Testcase) iter.next();
    		
    		Upload upload = new Upload();

    		// test_case
    		upload.setProjectId(output.getProjectId());
    		
    		// test_case.reviewer_id
    		upload.setResourceId(MapUtil.getResourceId(output, testcase.getReviewerId()));

    		upload.setUploadTypeId(Upload.UPLOAD_TYPE_TEST_CASE);
    		upload.setUploadStatusId(testcase.isCurVersion() ? Upload.UPLOAD_STATUS_ACTIVE : Upload.UPLOAD_STATUS_DELETED);
    		upload.setParameter(testcase.getTestcaseUrl());
        	setBaseDTO(upload);
    		upload.setUploadId((int) uploadIdGenerator.getNextID());
    		output.addUpload(upload);
    	}
		Util.logAction("prepareUpload");
    }
    
    /**
     * Check if given date is among submission phase.
     * 
     * @param old the old project
     * @param date the submit date
     * @return true if the date is after submission start date and before end date
     */
    private static boolean isSubmissionPhase(ProjectOld old, Date date) {
    	for (Iterator iter = old.getPhaseInstances().iterator(); iter.hasNext();) {
    		PhaseInstance phase = (PhaseInstance) iter.next();
    		if (phase.getPhaseId() == 2) {
    	    	// submission's phase id is 2
    			return phase.getStartDate().before(date) && phase.getEndDate().after(date);
    		}
    	}
    	return false;
    }

    /**
     * Prepare Upload info for this project.
     * 
     * @param input the old project
     * @param output the new project
     * @throws IDGenerationException error occurs while generate new id
     */
    private Submission prepareSubmission(ProjectOld input, SubmissionOld old, int uploadId) throws IDGenerationException {    		
    	Util.start("prepareSubmission");   
    	Submission submission = new Submission();

		submission.setUploadId(uploadId);
		if (old.isRemoved()) {
    		// if current submission is removed, set to 'Deleted', 
			submission.setSubmissionStatusId(Submission.SUBMISSION_STATUS_DELETED);
		} else {
    		// otherwise if submission hasn't advanced to review, set to 'Failed Screening', 
			if (!old.isAdvancedToReview()) {
				submission.setSubmissionStatusId(Submission.SUBMISSION_STATUS_FAILED_SCREENING);
			} else {
	    		// otherwise if submission failed review, set to 'Failed Review', 
				ProjectResult pr = input.getProjectResultByUserId(old.getSubmitterId());				
				if (pr != null && !pr.isPassedReviewInd()) {
    				submission.setSubmissionStatusId(Submission.SUBMISSION_STATUS_FAILED_REVIEW);
				} else {
		    		// otherwise if submission ends up with placement other than 1, set to 'Completed Without Win', 
					if (old.getPlacement() != 1) {
        				submission.setSubmissionStatusId(Submission.SUBMISSION_STATUS_COMPLETED_WITHOUT_WIN);
					} else {
			    		// otherwise set to 'Active'
			    		// submission.setSubmissionStatusId()
        				submission.setSubmissionStatusId(Submission.SUBMISSION_STATUS_ACTIVE);
					}    					
				}
			}
		}
		
    	setBaseDTO(submission);
		// submission.submission_v_id
		submission.setSubmissionId((int) submissionIdGenerator.getNextID());	

		Util.logAction("prepareSubmission");
		return submission;
    }

    /**
     * Prepare ProjectNew from ProjectOld data.
     * 
     * @param input the ProjectOld
     * @param output ProjectNew
     */
    private void prepareProject(ProjectOld input, ProjectNew output) throws IDGenerationException {
    	Util.start("prepareProject");   
    	output.setProjectStatusId(MapUtil.getProjectStatusId(input.getProjectStatId()));
        output.setProjectCategoryId(MapUtil.getProjectCategoryId(input.getProjectTypeId()));
    	setBaseDTO(output);
        output.setProjectId((int) projectIdGenerator.getNextID());    	
		Util.logAction("prepareProject");
    }

    /**
     * Prepare ProjectAudit from ProjectOld data.
     * 
     * @param input the ProjectOld
     * @param output ProjectNew
     */
    private void prepareProjectAudit(ProjectOld input, ProjectNew output) throws IDGenerationException {
    	Util.start("prepareProjectAudit");   
    	// Prepare audit data
    	for (Iterator iter = input.getModifiyReasons().iterator(); iter.hasNext();) {
            ProjectAudit audit = new ProjectAudit();
            audit.setUpdateReason((String) iter.next());
        	setBaseDTO(audit);
            audit.setProjectAuditId((int) projectAuditIdGenerator.getNextID());
            audit.setProjectid(output.getProjectId());
            output.addProjectAudit(audit);    
    	}	
		Util.logAction("prepareProjectAudit");
    }

    /**
     * Prepare projectInfos from ProjectOld data.
     * 
     * @param input the ProjectOld
     * @param output ProjectNew
     */
    private void prepareProjectInfos(ProjectOld input, ProjectNew output) {
        // Prepare project infos
        // External Reference ID project.comp_vers_id
    	Util.start("prepareProjectInfos");   
    	 ProjectInfo erid = new ProjectInfo();
        erid.setProjectId(output.getProjectId());
        erid.setProjectInfoTypeId(ProjectInfo.EXTERNAL_REFERENCE_ID);
        erid.setValue(String.valueOf(input.getCompVersId()));
    	setBaseDTO(erid);
        output.addProjectInfo(erid);

        if (input.getCompVersions() != null) {
            // Component ID
            ProjectInfo cid = new ProjectInfo();
            cid.setProjectId(output.getProjectId());
            cid.setProjectInfoTypeId(ProjectInfo.COMPONENT_ID);
            cid.setValue(String.valueOf(input.getCompVersions().getComponentId()));
        	setBaseDTO(cid);
            output.addProjectInfo(cid);

            // Version ID
            ProjectInfo vid = new ProjectInfo();
            vid.setProjectId(output.getProjectId());
            vid.setProjectInfoTypeId(ProjectInfo.VERSION_ID);
            vid.setValue(String.valueOf(input.getCompVersions().getVersion()));
        	setBaseDTO(vid);
            output.addProjectInfo(vid);

            // Project Version
            ProjectInfo info = new ProjectInfo();
            info.setProjectId(output.getProjectId());
            info.setProjectInfoTypeId(ProjectInfo.PROJECT_VERSION);
            info.setValue(convert(input.getCompVersions().getVersionText()));
        	setBaseDTO(info);
            output.addProjectInfo(info);
        }

        if (input.getCompForumXref() != null) {
            // Developer Forum ID
        	// forum_type = 2
            ProjectInfo dfid = new ProjectInfo();
            dfid.setProjectId(output.getProjectId());
            dfid.setProjectInfoTypeId(ProjectInfo.DEVELOPER_FORUM_ID);
            dfid.setValue(String.valueOf(input.getCompForumXref().getForumId()));
        	setBaseDTO(dfid);
            output.addProjectInfo(dfid);
        }

        if (input.getCompCatalog() != null) {
            // Root Catalog ID
            ProjectInfo info = new ProjectInfo();
            info.setProjectId(output.getProjectId());
            info.setProjectInfoTypeId(ProjectInfo.ROOT_CATALOG_ID);
            info.setValue(String.valueOf(input.getCompCatalog().getRootCategoryId()));
        	setBaseDTO(info);
            output.addProjectInfo(info);

            // Project Name
            info = new ProjectInfo();
            info.setProjectId(output.getProjectId());
            info.setProjectInfoTypeId(ProjectInfo.PROJECT_NAME);
            info.setValue(convert(input.getCompCatalog().getComponentName()));
        	setBaseDTO(info);
            output.addProjectInfo(info);
        }

        // Auto Pilot Option 'On', 'Off'
        ProjectInfo info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.AUTOPILOT_OPTION);
        info.setValue(input.isAutoPilotInd() ? "On" : "Off");
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // Status Notification always 'On'
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.STATUS_NOTIFICATION);
        info.setValue("On");
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // Timeline Notification always 'On'
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.TIMELINE_NOTIFICATION);
        info.setValue("On");
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // Public always 'Yes'
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.PUBLIC);
        info.setValue("Yes");
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // Rated always 'Yes'
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.RATIED);
        info.setValue("Yes");
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // Payments Required always 'Yes'
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.PAYMENTS_REQUIRED);
        info.setValue("Yes");
    	setBaseDTO(info);
        output.addProjectInfo(info);

        if (input.getCompVersionDates() != null) {
            // Payments comp_version_dates			price
        	// identified by phase_id (112 - design, 113 - dev)
            info = new ProjectInfo();
            info.setProjectId(output.getProjectId());
            info.setProjectInfoTypeId(ProjectInfo.PAYMENTS);
            info.setValue(String.valueOf(input.getCompVersionDates().getPrice()));
        	setBaseDTO(info);
            output.addProjectInfo(info);
        }

        // Notes project			notes
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.NOTES);
        info.setValue(convert(input.getNotes()));
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // Completion Timestamp MM/dd/yyyy hh:mm a  project			completion_date
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.COMPLETION_TIMESTAP);
        info.setValue(convert(input.getCompleteDate()));
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // Rated Timestamp project			rating_date MM/dd/yyyy hh:mm a 
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.RATED_TIMESTAP);
        info.setValue(convert(input.getRatingDate()));
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // Winner External Reference ID project
        info = new ProjectInfo();
        info.setProjectId(output.getProjectId());
        info.setProjectInfoTypeId(ProjectInfo.WINNER_EXTERNAL_REFERENCE_ID);
        info.setValue(String.valueOf(input.getWinnerId()));
    	setBaseDTO(info);
        output.addProjectInfo(info);

        // SVN Module TODO not evaluated
        // Eligibility TODO not evaluated
        // Deactivation Timestamp not evaluated MM/dd/yyyy hh:mm a TODO 
        // Deactivation Phase not evaluated TODO 
        // Deactivation Reason not evaluated TODO 
        // Runner-up External Reference ID not evaluated TODO 
		Util.logAction("prepareProjectInfos");
    }

    /**
     * Prepare phase from ProjectOld data.
     * 
     * @param input the ProjectOld
     * @param output ProjectNew
     * @throws IDGenerationException error occurs while generate new id
     */
    private void preparePhases(ProjectOld input, ProjectNew output) throws IDGenerationException {
    	Util.start("preparePhases");   
    	// Prepare project phase
    	for (Iterator iter = input.getPhaseInstances().iterator(); iter.hasNext();) {
            PhaseInstance pi = (PhaseInstance) iter.next();
            if (pi.getPhaseId() < 1 || pi.getPhaseId() > 11) {
            	// From Registration: 1 to Approval: 11, others ignore
            	continue;
            }

            Phase phase = new Phase();
            phase.setPhaseId((int) phaseIdGenerator.getNextID());
            phase.setProjectId(output.getProjectId());
            
            // maps to corresponding phase type, 'Component Preparation' is ignored
            // if old phase id is same as phase type
            phase.setPhaseTypeId(pi.getPhaseId());

            // sort phases in a single project, the phases before current phase 
            // is 'Closed', the current phase is 'Open' 
            // and the phases beyond is 'Scheduled'
            phase.setPhaseStatusId(pi.getPhaseStatusId());

            phase.setFixedStartTime(pi.getStartDate());
            phase.setScheduledStartTime(pi.getStartDate());
            phase.setScheduledEndTime(pi.getEndDate());
            phase.setActualStartTime(pi.getStartDate());
            phase.setActualEndTime(pi.getEndDate());

            // the difference between the start time and end time
            phase.setDuration((int) (pi.getEndDate().getTime() - pi.getStartDate().getTime()));
        	setBaseDTO(phase);
            preparePhaseCriterias(phase, pi.getTemplateId());
            output.addPhase(phase);    		
    	}
		Util.logAction("preparePhases");
    }
    
    /**
     * Prepare resources info for this project.
     * 
     * @param input the old project
     * @param output the new project
     * @throws IDGenerationException error occurs while generate new id
     */
    private void prepareResources(ProjectOld input, ProjectNew output) throws IDGenerationException {
    	Util.start("prepareResources");   
    	Collection rUserRoles = input.getRUserRoles();
    	Collection phases = output.getPhases();
    	for (Iterator iter = rUserRoles.iterator(); iter.hasNext();) {
    		RUserRole role = (RUserRole) iter.next();
    		int phaseId = getPhaseId(phases, role.getRRoleId());
    		Resource resource = new Resource();
    		resource.setProjectId(output.getProjectId());

    		// maps to corresponding resource role, 
    		// test case reviewer should be further mapped with responsibility id
    		resource.setResourceRoleId(MapUtil.getResourceRole(role.getRRoleId(), role.getRRespId()));

    		// resources should be associated with corresponding phases in the project
    		resource.setPhaseId(phaseId);
        	setBaseDTO(resource);
    		resource.setResourceId((int) resourceIdGenerator.getNextID());
    		output.addResource(resource);
    		
    		// Prepare resource info
    		prepareResourceInfosNotification(input, output, resource, role);
    	}
		Util.logAction("prepareResources");
    }

    /**
     * Prepare resource infos for given resource.
     * 
     * @param resource the resource
     */
    private void prepareResourceInfosNotification(ProjectOld old, ProjectNew project, Resource resource, RUserRole role) {
    	Util.start("prepareResourceInfosNotification");   
    	// External Reference ID r_user_role			login_id
    	ResourceInfo info = new ResourceInfo();
    	info.setResourceId(resource.getResourceId());
    	info.setResourceInfoTypeId(ResourceInfo.EXTERNAL_REFERENCE_ID);
    	info.setValue(String.valueOf(role.getLoginId()));
    	setBaseDTO(info);
    	project.addResourceInfo(info);

    	Notification noti = new Notification();
    	noti.setProjectId(resource.getProjectId());
    	noti.setExternalRefId(role.getLoginId());
    	noti.setNotificationTypeId(Notification.TIMELINE_NOTIFICATION_ID);
    	setBaseDTO(noti);
    	project.addNotification(noti);

    	if (MapUtil.isSubmitter(role.getRRoleId())) {
	    	// Rating project_result			old_rating
    		ProjectResult pr = old.getProjectResultByUserId(role.getLoginId());
    		if (pr == null) {
    			Util.warn("Failed to find project result for Submitter: " + role.getRUserRoleId());
    		} else {
		    	// Registration Date project_result			create_date
		    	// or  rboard_application			create_date
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.REGISTRATION_DATE);
		    	// if it's submitter use project_result, if it's reviewer, use rboard_application
		    	info.setValue(convert(pr.getCreateDate()));
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);
		    	
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.RATING);
		    	if (pr.getOldRating() == 0) {
		    		info.setValue("Not Rated");
		    	} else {
		    		info.setValue(String.valueOf(pr.getOldRating()));
		    	}
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);
		    	
		    	// Reliability project_result			old_reliability
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.RELIABILITY);
		    	if (pr.getOldReliability() == 0) {
		    		info.setValue("'N/A");
		    	} else {
		    		// convert to percentage, if not present, use 'N/A'
		    		info.setValue(String.valueOf(pr.getOldReliability() * 100));
		    	}
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);

		    	// Placement project_result			placed
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.PLACEMENT);
		    	info.setValue(String.valueOf(pr.getPlaced()));
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);

		    	// Initial Score project_result			raw_score
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.INITIAL_SCORE);
		    	info.setValue(String.valueOf(pr.getRawScore()));
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);

		    	// Final Score project_result			final_score
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.FINAL_SCORE);
		    	info.setValue(String.valueOf(pr.getFinalScore()));
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);
    		}

    		ScorecardOld scorecard = old.getScreeningScorecardBySubmitter(role.getLoginId());
    		if (scorecard == null) {
    			Util.warn("Failed to find screening scorecard for submitter: " + role.getLoginId() + " rUserId: " + role.getRUserRoleId());
    		} else {
		    	// Screening Score scorecard			score    		
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.SCREENING_SCORE);
		    	info.setValue(String.valueOf(scorecard.getScore()));
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);
	    	}
    	}

    	if (MapUtil.isReviewer(role.getRRoleId())) {
	    	// Payment payment_info			payment
    		PaymentInfo pi = role.getPaymentInfo();
    		if (pi == null) {
    			Util.warn("Failed to find PaymentInfo for reviewer: " + role.getRUserRoleId());
    		} else {
		    	// for reviewer
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.PAYMENT);
		    	info.setValue(String.valueOf(pi.getPayment()));
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);
	
		    	// Payment Status payment_info			payment_stat_id
		    	// for reviewer
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.PAYMENT_STATUS);
		    	// 1 maps to 'No', 2 maps to 'Yes'
		    	switch (pi.getPaymentStatId()) {
		    	case 1:
			    	info.setValue("No");
		    		break;
		    	case 2:
			    	info.setValue("Yes");
		    		break;
		    	default:
	    			Util.warn("Invalid PaymentStatId: " + pi.getPaymentStatId());
		    		break;
		    	}
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);
    		}

    		RboardApplication ra = old.getRboardApplicationByUserId(role.getLoginId());
    		if (pi == null) {
    			Util.warn("Failed to find RboardApplication for reviewer: " + role.getRUserRoleId());
    		} else {
		    	// Registration Date project_result			create_date
		    	// or  rboard_application			create_date
		    	info = new ResourceInfo();
		    	info.setResourceId(resource.getResourceId());
		    	info.setResourceInfoTypeId(ResourceInfo.REGISTRATION_DATE);
		    	// if it's submitter use project_result, if it's reviewer, use rboard_application
		    	info.setValue(convert(ra.getCreateDate()));
		    	setBaseDTO(info);
		    	project.addResourceInfo(info);
    		}
    	}
		Util.logAction("prepareResourceInfosNotification");
    }

    /**
     * Return the phase id according to given role id and phases collection.
     * 
     * @param phases the phases
     * @param roleId the roleId
     * @return phaseId
     */
    private int getPhaseId(Collection phases, int roleId) {
    	Integer phaseTypeId = (Integer) roleToPhaseTypes.get(new Integer(roleId));
    	if (phaseTypeId == null) {
    		return 0;
    	}
    	for (Iterator iter = phases.iterator(); iter.hasNext();) {
    		Phase phase = (Phase) iter.next();
    		if (phase.getPhaseTypeId() == phaseTypeId.intValue()) {
    			return phase.getPhaseId();
    		}
    	}
    	return 0;
    }
    

    /**
     * Prepare PhaseCriteria for certain phase.
     * 
     * @param phase the phase
     * @param templateId the template id used for Scorecard ID
     */
    private void preparePhaseCriterias(Phase phase, int templateId) {
    	Util.start("preparePhaseCriterias");   
    	// Scorecard ID
    	PhaseCriteria criteria = new PhaseCriteria();
    	criteria.setPhaseId(phase.getPhaseId());
    	criteria.setPhaseCriteriaTypeId(PhaseCriteria.SCORECARD_ID);
    	criteria.setParameter(String.valueOf(templateId));
    	setBaseDTO(criteria);
    	phase.addCriteria(criteria);

    	// Registration Number not evaluated TODO
    	// Submission Number not evaluated TODO
    	// View Response During Appeals not evaluated 'Yes', 'No' TODO

    	// Manual Screening 'Yes' for each submission phase 'Yes', 'No'
    	criteria = new PhaseCriteria();
    	criteria.setPhaseId(phase.getPhaseId());
    	criteria.setPhaseCriteriaTypeId(PhaseCriteria.MANUAL_SCREENING);
    	// Submission: 2
    	criteria.setParameter(phase.getPhaseStatusId() == 2 ? "Yes" : "No");
    	setBaseDTO(criteria);
    	phase.addCriteria(criteria);
		Util.logAction("preparePhaseCriterias");
    }

    /**
     * Prepare PhaseDependency from ProjectOld data.
     * 
     * @param project ProjectNew
     */
    private void preparePhaseDependencys(ProjectNew project) {
    	Util.start("preparePhaseDependencys");   
    	// Prepare project phase
    	List phases = project.getPhases();
    	if (phases.size() < 2) {
    		// PhaseDependency should exist in two phases
    		return;
    	}

    	// Sort against phasestatus
    	Collections.sort(phases, new PhaseComparator());
    	
    	Iterator iter = phases.iterator();
    	Phase previousPhase = (Phase) iter.next();
    	
    	while(iter.hasNext()) {
        	Phase nextPhase = (Phase) iter.next();
        	PhaseDependency pd = new PhaseDependency();
        	pd.setDependencyPhaseId(previousPhase.getPhaseId());
        	pd.setDependentPhaseId(nextPhase.getPhaseId());
        	pd.setDependencyStart(false);
        	pd.setDependentStart(true);
        	pd.setLagTime(0);
        	setBaseDTO(pd);
            project.addPhaseDependencys(pd);   
            
            // Change to next dependency
            previousPhase = nextPhase;
    	}
		Util.logAction("preparePhaseDependencys");
    }

    /**
     * Convert given object to string.
     *
     * @param obj the obj to convert
     *
     * @return the string representation
     */
    private static String convert(Object obj) {
        if (obj == null) {
            return null;
        } else {
            if (obj instanceof Date) {
                return DATE_FORMAT.format((Date) obj);
            }

            return String.valueOf(obj);
        }
    }
}
