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
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for downloading specification submission.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class DownloadSpecificationSubmissionAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -1431820423156147151L;

    /**
     * Creates a new instance of the <code>DownloadSpecificationSubmissionAction</code> class.
     */
    public DownloadSpecificationSubmissionAction() {
    }

    /**
     * <p>This method is an implementation of &quot;Download Specification Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a submission from the server.</p>
     *
     * @return a <code>null</code> code if everything went fine, or a string forward to /jsp/userError.jsp page which
     *         will display the information about the cause of error.
     * @throws BaseException if any error occurs.
     * @throws IOException if some error occurs during disk input/output operation.
     */
    public String execute() throws BaseException, IOException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(request, "ViewSubmission");
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();
        // Verify that upload is a submission
        if (!upload.getUploadType().getName().equalsIgnoreCase("Submission")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(this, request, "ViewSubmission",
                "Error.NotASubmission", null);
        }

        // Verify the status of upload and check whether the user has permission to download old uploads
        boolean hasViewAllSpecSubmissionsPermission
            = AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SPECIFICATION_SUBMISSIONS_PERM_NAME);
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted") && !hasViewAllSpecSubmissionsPermission) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(
                    this, request, "ViewSubmission", "Error.UploadDeleted", null);
        }

        boolean noRights = true;

        if (hasViewAllSpecSubmissionsPermission) {
            noRights = false;
        }

        if (noRights
            && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SPECIFICATION_SUBMISSIONS_PERM_NAME)) {
            long owningResourceId = upload.getOwner();
            Resource[] myResources = (Resource[]) request.getAttribute("myResources");
            for (Resource myResource : myResources) {
                if (myResource.getId() == owningResourceId) {
                    noRights = false;
                    break;
                }
            }
        }

        if (noRights && AuthorizationHelper.hasUserPermission(
                            request, Constants.VIEW_RECENT_SPECIFICATION_SUBMISSIONS_PERM_NAME)) {
            Phase[] phases = ActionsHelper.getPhasesForProject(
                    ActionsHelper.createPhaseManager(false), verification.getProject());
            final boolean isReviewOpen = ActionsHelper.isInOrAfterPhase(phases, 0,
                Constants.SPECIFICATION_REVIEW_PHASE_NAME);
            if (AuthorizationHelper.hasUserRole(request, Constants.SPECIFICATION_REVIEWER_ROLE_NAME) && !isReviewOpen) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        this, request, "ViewSubmission", "Error.IncorrectPhase", null);
            }
            noRights = false;
        }

        ActionsHelper.logDownloadAttempt(request, upload, !noRights);

        if (noRights) {
            return ActionsHelper.produceErrorReport(this, request, "ViewSubmission",
                "Error.NoPermission", Boolean.FALSE);
        }

        processSubmissionDownload(upload, request, response);

        return null;
    }
}

