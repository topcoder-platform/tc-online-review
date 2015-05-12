/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import java.util.Arrays;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.model.FormFile;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.cronos.onlinereview.util.StrutsRequestParser;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for uploading test cases.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UploadTestCaseAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -6858688672469246257L;

    /**
     * Creates a new instance of the <code>UploadTestCaseAction</code> class.
     */
    public UploadTestCaseAction() {
    }

    /**
     * This method is an implementation of &quot;Upload Test Case&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his test cases to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.
     *
     * @return an string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to uploadTestCase.jsp page, which
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
                this, request, Constants.UPLOAD_TEST_CASES_PERM_NAME, postBack);
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

        // Retrieve the current phase for the project, it should be either Review or Appeals Response
        Phase reviewPhase = ActionsHelper.getPhase(phases, true, Constants.REVIEW_PHASE_NAME);
        Phase appealsResponsePhase = ActionsHelper.getPhase(phases, true, Constants.APPEALS_RESPONSE_PHASE_NAME);

        if (reviewPhase == null && appealsResponsePhase == null) {
            return ActionsHelper.produceErrorReport(this,
                    request, Constants.UPLOAD_TEST_CASES_PERM_NAME, "Error.IncorrectPhase", null);
        }

        FormFile file = (FormFile) getModel().get("file");

        // Disallow uploading of empty files
        if (file == null || file.getFileSize() == 0) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.UPLOAD_TEST_CASES_PERM_NAME, "Error.EmptyFileUploaded", null);
        }

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForPhase(
                request, ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME));

        Filter filterProject = UploadFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterResource = UploadFilterBuilder.createResourceIdFilter(resource.getId());
        Filter filterStatus = UploadFilterBuilder.createUploadStatusIdFilter(
                LookupHelper.getUploadStatus("Active").getId());
        Filter filterType = UploadFilterBuilder.createUploadTypeIdFilter(
                LookupHelper.getUploadType("Test Case").getId());

        Filter filter = new AndFilter(
                Arrays.asList(filterProject, filterResource, filterStatus, filterType));

        UploadManager upMgr = ActionsHelper.createUploadManager();
        Upload[] oldUploads = upMgr.searchUploads(filter);

        // Set the status for old uploads to Deleted
        for (Upload oldUpload : oldUploads) {
            oldUpload.setUploadStatus(LookupHelper.getUploadStatus("Deleted"));
            upMgr.updateUpload(oldUpload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setProjectPhase(reviewPhase != null ? reviewPhase.getId() : appealsResponsePhase.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
        upload.setUploadType(LookupHelper.getUploadType("Test Case"));
        upload.setParameter(uploadedFile.getFileId());

        upMgr.createUpload(upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        this.setPid(project.getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }
}

