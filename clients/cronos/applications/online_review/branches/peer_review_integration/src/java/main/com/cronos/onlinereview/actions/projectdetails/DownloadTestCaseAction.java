/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import java.io.IOException;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.project.phases.Phase;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;


/**
 * This class is the struts action class which is used for downloading test cases.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class DownloadTestCaseAction extends BaseProjectDetailsAction {
    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of the <code>DownloadTestCaseAction</code> class.
     */
    public DownloadTestCaseAction() {
    }

    /**
     * This method is an implementation of &quot;Download Test Case&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a final fixes from the server.
     *
     * @return a <code>null</code> code if everything went fine, or a string forward to
     *         /jsp/userError.jsp page which will display the information about the cause of error.
     * @throws BaseException
     *             if any error occurs.
     * @throws IOException
     *             if some error occurs during disk input/output operation.
     */
    public String execute() throws BaseException, IOException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(false), verification.getProject());

        boolean isReviewClosed = false;
        boolean isAppealsOpen = false;

        for (int i = 0; i < phases.length; ++i) {
            // Get phase's type name for the current iteration
            String phaseName = phases[i].getPhaseType().getName();
            // Skip the phase if it is not a Review phase
            if (!phaseName.equalsIgnoreCase(Constants.REVIEW_PHASE_NAME)) {
                continue;
            }
            // Check if this phase is closed
            if (phases[i].getPhaseStatus().getName().equalsIgnoreCase(Constants.CLOSED_PH_STATUS_NAME)) {
                isReviewClosed = true;
                // Review phase is closed, not need to check anything else
                break;
            }
            // Check that there are more phases, and exit from cycle if current phase is the last
            if (i + 1 == phases.length) {
                break;
            }
            // Get next phase
            Phase nextPhase = phases[i + 1];
            // Check that next phase is Appeals and it is open
            if (nextPhase.getPhaseType().getName().equalsIgnoreCase(Constants.APPEALS_PHASE_NAME) &&
                    nextPhase.getPhaseStatus().getName().equalsIgnoreCase(Constants.OPEN_PH_STATUS_NAME)) {
                isAppealsOpen = true;
            }
            // No need to proceed with the cycle anymore
            break;
        }

        final boolean canDownload =
            AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME);
        final boolean canDownloadDuringReview =
            AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_TC_DUR_REVIEW_PERM_NAME);
        final boolean canPlaceAppeals =
            AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_PERM_NAME);

        // Get the upload the user wants to download
        Upload upload = verification.getUpload();

        // If Review phase is not closed yet, there is a need to check whether the user that is
        // attempting to download test cases is a Submitter and an Appeals phase is open
        if (canDownload && canPlaceAppeals && !isReviewClosed && !canDownloadDuringReview && !isAppealsOpen) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.IncorrectPhase", null);
        }
        // Verify that user can download test cases during Review
        if (canDownload && !isReviewClosed && !canDownloadDuringReview) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.IncorrectPhase", null);
        }
        // Check that the user is allowed to download test cases in general
        if (!canDownload) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Verify that upload is Test Cases
        if (!upload.getUploadType().getName().equalsIgnoreCase("Test Case")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.NotTestCases", null);
        }
        // Verify the status of upload
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.UploadDeleted", null);
        }

        ActionsHelper.logDownloadAttempt(request, upload, true);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());
        outputDownloadedFile(uploadedFile, "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"", response);

        return null;
    }
}

