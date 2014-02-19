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
import com.cronos.onlinereview.util.TextContentRequestParser;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.SubmissionType;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for uploading specification submission.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UploadSpecificationSubmissionAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -2440952082616304018L;

    /**
     * Creates a new instance of the <code>UploadSpecificationSubmissionAction</code> class.
     */
    public UploadSpecificationSubmissionAction() {
    }

    /**
     * <p>This method is an implementation of &quot;Upload Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.</p>
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to uploadSubmission.jsp page, which
     *         displays the form where user can specify the file he/she wants to upload. If this
     *         action was called during the post back (the second time), then the request should
     *         contain the file uploaded by user. In this case, this method verifies if everything
     *         is correct, stores the file on file server and returns a forward to the View Project
     *         Details page.
     * @throws BaseException if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Determine if this request is a post back
        final boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
            this, request, Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME,
            postBack);

        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Retrieve current project
        Project project = verification.getProject();
        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);

        Phase specificationPhase = ActionsHelper.getPhase(phases, true, Constants.SPECIFICATION_SUBMISSION_PHASE_NAME);
        if (specificationPhase == null) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME, "Error.IncorrectPhase", null);
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);
            return Constants.DISPLAY_PAGE_FORWARD_NAME;
        }

        // Check if specification is already submitted
        Submission[] oldSubmissions = ActionsHelper.getProjectSubmissions(
                project.getId(), Constants.SPECIFICATION_SUBMISSION_TYPE_NAME,
                Constants.ACTIVE_SUBMISSION_STATUS_NAME, false);

        if (oldSubmissions != null && oldSubmissions.length > 0) {
            // Disallow submitting more than one Specification Submission for project
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME, "Error.SpecificationAlreadyUploaded", null);
        }

        // Analyze the nature of the submitted specification - uploaded file vs plain text and construct appropriate
        // request parser based on that
        String specFormatType = (String) getModel().get("specificationType");
        RequestParser parser;
        if ("file".equalsIgnoreCase(specFormatType)) {
            FormFile file = (FormFile) getModel().get("file");
            // Disallow uploading of empty files
            if (file == null || file.getFileSize() == 0) {
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME, "Error.EmptyFileUploaded", null);
            }
            StrutsRequestParser strutsParser = new StrutsRequestParser();
            strutsParser.AddFile(file);
            parser = strutsParser;
        } else {
            String specificationText = (String) getModel().get("specificationText");
            if ((specificationText == null) || (specificationText.trim().length() == 0)) {
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME, "Error.EmptySpecification", null);
            }
            parser = new TextContentRequestParser(specificationText);
        }

        // Parse request and save specification as uploaded file
        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        Resource resource = ActionsHelper.getMyResourceForRole(request, "Specification Submitter");
        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Always create a new submission/ upload
        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setProjectPhase(specificationPhase.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
        upload.setUploadType(LookupHelper.getUploadType("Submission"));
        upload.setParameter(uploadedFile.getFileId());

        Submission submission = new Submission();
        submission.setUpload(upload);

        SubmissionType specSubmissionType = LookupHelper.getSubmissionType(Constants.SPECIFICATION_SUBMISSION_TYPE_NAME);
        SubmissionStatus submissionActiveStatus = LookupHelper.getSubmissionStatus("Active");
        submission.setSubmissionStatus(submissionActiveStatus);
        submission.setSubmissionType(specSubmissionType);

        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        UploadManager upMgr = ActionsHelper.createUploadManager();
        upMgr.createUpload(upload, operator);
        upMgr.createSubmission(submission, operator);
        resource.addSubmission(submission.getId());
        ActionsHelper.createResourceManager().updateResource(resource, operator);

        this.setPid(project.getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }
}

