/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import java.io.IOException;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for downloading final fix.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class DownloadFinalFixAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -1365278635350232141L;

    /**
     * Creates a new instance of the <code>DownloadFinalFixAction</code> class.
     */
    public DownloadFinalFixAction() {
    }

    /**
     * This method is an implementation of &quot;Download Final Fix&quot; Struts Action defined for
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
        verification = checkForCorrectUploadId(request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        boolean hasPermission = false, hasSubmitterRole = false;
        String[] roles = ConfigHelper.getRolesForPermission(Constants.DOWNLOAD_FINAL_FIX_PERM_NAME);
        for (String role : roles) {
            if (!AuthorizationHelper.hasUserRole(request, role)) {
                continue;
            }

            // For the Submitters we only allow to download final fixes if the user has at least passed screening.
            if (role.equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                hasSubmitterRole = true;

                // Get all submissions for this user.
                Resource resource = ActionsHelper.getMyResourceForRole(request, Constants.SUBMITTER_ROLE_NAME);
                UploadManager upMgr = ActionsHelper.createUploadManager();
                Long[] subIds = resource.getSubmissions();

                // Check that the user has a submission that passed screening.
                // We don't need to check the current phase because if there is a final fix submitted
                // it is already past the Appeals Response anyway.
                for (Long id : subIds) {
                    Submission submission = upMgr.getSubmission(id);
                    if (submission != null && submission.getSubmissionType().getName().equals(Constants.CONTEST_SUBMISSION_TYPE_NAME) &&
                            !submission.getSubmissionStatus().getName().equals(Constants.FAILED_SCREENING_SUBMISSION_STATUS_NAME)) {
                        hasPermission = true;
                        break;
                    }
                }
            } else {
                hasPermission = true;
                break;
            }
        }

        // Get the upload the user wants to download
        Upload upload = verification.getUpload();

        if (!hasPermission) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            if (hasSubmitterRole) {
                return ActionsHelper.produceErrorReport(
                        this, request, "ViewSubmission", "Error.NoScreeningPassed", null);

            } else {
                return ActionsHelper.produceErrorReport(this,
                        request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Verify that upload is a Final Fix
        if (!upload.getUploadType().getName().equalsIgnoreCase("Final Fix")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME, "Error.NotAFinalFix", null);
        }
        ActionsHelper.logDownloadAttempt(request, upload, true);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());
        outputDownloadedFile(uploadedFile, "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"", response);

        return null;
    }
}

