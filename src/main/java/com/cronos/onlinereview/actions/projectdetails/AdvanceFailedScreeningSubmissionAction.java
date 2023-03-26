/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.topcoder.onlinereview.component.deliverable.Submission;
import com.topcoder.onlinereview.component.deliverable.SubmissionStatus;
import com.topcoder.onlinereview.component.deliverable.Upload;
import com.topcoder.onlinereview.component.deliverable.UploadManager;
import com.topcoder.onlinereview.component.deliverable.SubmissionFilterBuilder;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.resource.Resource;
import com.topcoder.onlinereview.component.resource.ResourceManager;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.grpcclient.GrpcHelper;
import com.topcoder.onlinereview.component.grpcclient.sync.SyncServiceRpc;

/**
 * This class is the struts action class which is used for advancing failed screen submission.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class AdvanceFailedScreeningSubmissionAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 613970015458046770L;

    /**
     * Creates a new instance of the <code>AdvanceFailedScreeningSubmissionAction</code> class.
     */
    public AdvanceFailedScreeningSubmissionAction() {
    }

    /**
     * This method is an implementation of &quot;Advance Submission That Failed Screening&quot; Struts Action defined for
     * this assembly, which is supposed to advance submission that failed screening for particular upload
     * (denoted by <code>uid</code> parameter). This action gets executed twice &#x96; once to
     * display the page with the confirmation, and once to process the confirmed advance request to
     * actually advance the submission to pass screening.
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmAdvanceFailedScreeningSubmission.jsp
     *         or /jsp/confirmAdvanceFailedCheckpointScreeningSubmission.jsp page, which displays the confirmation dialog
     *         where user can confirm his intention to advance the submission. If this action was called during the post
     *         back (the second time), then this method verifies if everything is correct, and process the advance logic.
     *         After this it returns a forward to the View Project Details page.
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = checkForCorrectUploadId(request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Retrieve the upload user tries to advance
        Upload upload = verification.getUpload();
        Project project = verification.getProject();

        // Check that user has permissions to delete submission.
        boolean hasPermission = AuthorizationHelper.hasUserPermission(request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME);

        if (!hasPermission) {
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Verify that the user is attempting to advance submission
        if (!upload.getUploadType().getName().equalsIgnoreCase(Constants.SUBMISSION_UPLOAD_TYPE_NAME)) {
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME, "Error.NotASubmission3", null);
        }

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();
        Submission[] submissions = upMgr.searchSubmissions(SubmissionFilterBuilder.createUploadIdFilter(upload.getId()));
        Submission submission = (submissions.length != 0) ? submissions[0] : null;

        boolean isContestSubmission = submission.getSubmissionType().getName().equalsIgnoreCase(
                                        Constants.CONTEST_SUBMISSION_TYPE_NAME);

        // Check the submission status is Failed Screening
        String status = submission.getSubmissionStatus().getName();
        if (submission == null ||
            (!status.equalsIgnoreCase(Constants.FAILED_SCREENING_SUBMISSION_STATUS_NAME)
                && !status.equalsIgnoreCase(Constants.FAILED_CHECKPOINT_SCREENING_SUBMISSION_STATUS_NAME))) {
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME,
                    isContestSubmission ?
                            "Error.SubmissionNotFailedScreening" :"Error.SubmissionNotFailedCheckpointScreening",
                    null);
        }

        // Check the project status is Active
        if (!project.getProjectStatus().getName().equalsIgnoreCase(Constants.ACTIVE_PROJECT_STATUS_NAME)) {
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME, "Error.ProjectNotActive", null);
        }

        // Check the Review phase is not closed
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        Phase reviewPhase = ActionsHelper.findPhaseByTypeName(phases,
                isContestSubmission ? Constants.REVIEW_PHASE_NAME : Constants.CHECKPOINT_REVIEW_PHASE_NAME);
        if (reviewPhase == null || ActionsHelper.isPhaseClosed(reviewPhase.getPhaseStatus())) {
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME,
                    isContestSubmission ? "Error.ReviewClosed" : "Error.CheckpointReviewClosed",
                    null);
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("advance") != null);

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);
            // Place upload ID into the request as attribute
            request.setAttribute("uid", upload.getId());
            return Constants.DISPLAY_PAGE_FORWARD_NAME;
        }

        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        ProjectManager projectMgr = ActionsHelper.createProjectManager();
        String oldAutoPilotOption = (String) project.getProperty("Autopilot Option");
        if (!"Off".equalsIgnoreCase(oldAutoPilotOption)) {
            // Set AutoPilot status to Off
            project.setProperty("Autopilot Option", "Off");
            projectMgr.updateProject(project, "Turing AP off before advancing failed screening submission", operator);
        }
        boolean postMortenDeleted = false;
        if (isContestSubmission) {
            Phase postMortemPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.POST_MORTEM_PHASE_NAME);
            if (postMortemPhase != null) {
                ActionsHelper.deletePostMortem(project, postMortemPhase, operator);
                postMortenDeleted = true;
            }
        }
        // Set submission status to Active
        SubmissionStatus submissionActiveStatus = LookupHelper.getSubmissionStatus(Constants.ACTIVE_SUBMISSION_STATUS_NAME);
        submission.setSubmissionStatus(submissionActiveStatus);
        upMgr.updateSubmission(submission, operator);

        if (isContestSubmission) {
            // Update the project_result table
            ResourceManager resMgr = ActionsHelper.createResourceManager();
            Resource uploadOwner = resMgr.getResource(upload.getOwner());
            ActionsHelper.updateProjectResultForAdvanceScreening(project.getId(), uploadOwner.getUserId());
        }

        if (!"Off".equalsIgnoreCase(oldAutoPilotOption)) {
            // Restore the AutoPilot status
            project.setProperty("Autopilot Option", oldAutoPilotOption);
            projectMgr.updateProject(project, "Restoring the AP status after advancing failed screening submission", operator);
        }
        SyncServiceRpc syncServiceRpc = GrpcHelper.getSyncServiceRpc();
        if (isContestSubmission) {
            syncServiceRpc.advanceFailedScreeningSubmissionSync(project.getId(), submission.getId(), postMortenDeleted);
        } else {
            syncServiceRpc.advanceFailedCheckpointScreeningSubmissionSync(project.getId(), submission.getId());
        }
        

        setPid(verification.getProject().getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }
}

