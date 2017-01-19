/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.model.FormFile;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.cronos.onlinereview.util.StrutsRequestParser;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for uploading final fix.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UploadFinalFixAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 728844235166218947L;

    /**
     * Creates a new instance of the <code>UploadFinalFixAction</code> class.
     */
    public UploadFinalFixAction() {
    }

    /**
     * This method is an implementation of &quot;Upload Final Fix&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to uploadFinalFix.jsp page, which
     *         displays the form where user can specify the file he/she wants to upload. If this
     *         action was called during the post back (the second time), then the request should
     *         contain the file uploaded by user. In this case, this method verifies if everything
     *         is correct, stores the file on file server and returns a forward to the View Project
     *         Details page.
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                this, request, Constants.PERFORM_FINAL_FIX_PERM_NAME, postBack);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);
            return Constants.DISPLAY_PAGE_FORWARD_NAME;
        }

        // Retrieve current project
        Project project = verification.getProject();

        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        // Retrieve the current phase for the project
        Phase currentPhase = ActionsHelper.getPhase(phases, true, Constants.FINAL_FIX_PHASE_NAME);
        // Check that active phase is Final Fix
        if (currentPhase == null) {
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.PERFORM_FINAL_FIX_PERM_NAME, "Error.IncorrectPhase", null);
        }

        FormFile file = (FormFile) getModel().get("file");

        // Disallow uploading of empty files
        if (file == null || file.getFileSize() == 0) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.PERFORM_FINAL_FIX_PERM_NAME, "Error.EmptyFileUploaded", null);
        }

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForRole(request, "Submitter");

        Upload[] uploads = ActionsHelper.getPhaseUploads(currentPhase.getId(), "Final Fix");
        if (uploads.length > 0) {
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.PERFORM_FINAL_FIX_PERM_NAME, "Error.OnlyOneFinalFix", null);
        }

        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setProjectPhase(currentPhase.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
        upload.setUploadType(LookupHelper.getUploadType("Final Fix"));
        upload.setParameter(uploadedFile.getFileId());

        ActionsHelper.createUploadManager().createUpload(
                upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        this.setPid(project.getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }
}

