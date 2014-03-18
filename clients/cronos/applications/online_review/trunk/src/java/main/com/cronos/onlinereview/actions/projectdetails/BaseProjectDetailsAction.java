/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.actions.DynamicModelDrivenAction;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.model.FormFile;
import com.cronos.onlinereview.phases.AmazonSNSHelper;
import com.cronos.onlinereview.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.DisallowedDirectoryException;
import com.topcoder.servlet.request.FileDoesNotExistException;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.PersistenceException;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This is the base class for project details actions classes.
 * It provides the basic functions which will be used by all project detail actions.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseProjectDetailsAction extends DynamicModelDrivenAction  {
    /**
     * Represents the project id.
    */
    private long pid;

    /**
     * This method verifies the request for certain conditions to be met. This includes verifying if
     * the user has specified an ID of the upload he wants to perform an operation on (most often
     * &#x96; to download), and whether the ID of the upload specified by user denotes existing
     * upload.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which specifies whether the
     *         check was successful and, in the case it was, contains additional information
     *         retrieved during the check operation, which might be of some use for the calling
     *         method.
     * @param request
     *            the http request.
     * @param errorMessageKey
     *            permission to check against, or <code>null</code> if no check is required.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if
     *             <code>errorMessageKey</code> parameter is an empty string.
     * @throws BaseException
     *             if any error occurs.
     */
    protected CorrectnessCheckResult checkForCorrectUploadId(HttpServletRequest request, String errorMessageKey) throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterStringNotEmpty(errorMessageKey, "errorMessageKey");

        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        // Verify that Upload ID was specified and denotes correct upload
        String uidParam = request.getParameter("uid");
        if (uidParam == null || uidParam.trim().length() == 0) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, errorMessageKey, "Error.UploadIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long uid;

        try {
            // Try to convert specified uid parameter to its integer representation
            uid = Long.parseLong(uidParam, 10);
        } catch (NumberFormatException nfe) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, errorMessageKey, "Error.UploadNotFound", null));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();
        // Get Upload by its ID
        Upload upload = upMgr.getUpload(uid);
        // Verify that upload with given ID exists
        if (upload == null) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, errorMessageKey, "Error.UploadNotFound", null));
            // Return the result of the check
            return result;
        }

        // Store Upload object in the result bean
        result.setUpload(upload);

        // Obtain an instance of Project Manager
        ProjectManager projMgr = ActionsHelper.createProjectManager();
        // Get a Project for this upload
        Project project = projMgr.getProject(upload.getProject());

        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, project.getId());

        return result;
    }

    /**
     * <p>Sends the content of specified file for downloading by client.</p>
     *
     * @param upload an <code>Upload</code> providing the details for the filr to be downloaded by client.
     * @param request an <code>HttpServletRequest</code> representing the incoming request.
     * @param response an <code>HttpServletResponse</code> representing the outgoing response.
     * @throws UploadPersistenceException if an unexpected error occurs.
     * @throws SearchBuilderException if an unexpected error occurs.
     * @throws DisallowedDirectoryException if an unexpected error occurs.
     * @throws ConfigurationException if an unexpected error occurs.
     * @throws PersistenceException if an unexpected error occurs.
     * @throws FileDoesNotExistException if an unexpected error occurs.
     * @throws IOException if an unexpected error occurs.
     */
    protected void processSubmissionDownload(Upload upload, HttpServletRequest request, HttpServletResponse response) throws UploadPersistenceException, SearchBuilderException, DisallowedDirectoryException,
    ConfigurationException, PersistenceException, FileDoesNotExistException, IOException{

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());

        UploadManager upMgr = ActionsHelper.createUploadManager();
        Submission[] submissions = upMgr.searchSubmissions(SubmissionFilterBuilder.createUploadIdFilter(upload.getId()));
        Submission submission = (submissions.length != 0) ? submissions[0] : null;

        String contentDisposition;
        if (submission != null) {
            contentDisposition = "attachment; filename=\"submission-" + submission.getId() + "-"
                                 + uploadedFile.getRemoteFileName() + "\"";
        } else {
            contentDisposition = "attachment; filename=\"upload-" + upload.getId() + "-"
                                 + uploadedFile.getRemoteFileName() + "\"";
        }

        outputDownloadedFile(uploadedFile, contentDisposition, response);
    }

    /**
     *
     * <p>Outputs the content of specified file to specified response for downloading by client.</p>
     *
     * @param uploadedFile an <code>UploadedFile</code> providing the details for the filr to be downloaded by client.
     * @param contentDisposition a <code>String</code> providing the value for <code>Content-Disposition</code> header.
     * @param response an <code>HttpServletResponse</code> representing the outgoing response.
     * @throws PersistenceException if an unexpected error occurs.
     * @throws FileDoesNotExistException if an unexpected error occurs.
     * @throws IOException if an unexpected error occurs.
     */
    protected void outputDownloadedFile(UploadedFile uploadedFile, String contentDisposition, HttpServletResponse response) 
    throws PersistenceException, FileDoesNotExistException, IOException {

        InputStream in = uploadedFile.getInputStream();

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) uploadedFile.getSize());
        response.setHeader("Content-Disposition", contentDisposition);

        response.flushBuffer();

        OutputStream out = null;

        try {
            out = response.getOutputStream();
            byte[] buffer = new byte[65536];

            for (;;) {
                int numOfBytesRead = in.read(buffer);
                if (numOfBytesRead == -1) {
                    break;
                }
                out.write(buffer, 0, numOfBytesRead);
            }
        } finally {
            in.close();
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * <p>Handles the request for downloading the submission of desired type.</p>
     *
     *
     * @param request an <code>HttpServletRequest</code> providing the details for incoming request.
     * @param response an <code>HttpServletResponse</code> providing the details for outgoing response.
     * @param errorMessageKey a <code>String</code> providing the key in message bundle for the error to be displayed
     *        to user.
     * @param viewAllSubmissionsPermName a <code>String</code> providing the name for permission for viewing all
     *        submissions.
     * @param viewMySubmissionsPermissionName a <code>String</code> providing the name for permission for viewing own
     *        submissions
     * @param viewSubmissionByScreenerPermissionName a <code>String</code> providing the name for permission for viewing
     *        submissions by screener.
     * @param viewMostRecentSubmissionsPermissionName a <code>String</code> providing the name for permission for
     *        viewing most recent submissions.
     * @param downloadCustomSubmissionPermissionName a <code>String</code> providing the name for permission for
     *        downloading submission for custom catalog.
     * @param viewWinningSubmissionPermissionName a <code>String</code> providing the name for permission for viewing
     *        winning submissions.
     * @param screeningPhaseName a <code>String</code> providing the name for screening phase type.
     * @param reviewPhaseName a <code>String</code> providing the name for review phase type.
     * @param screenerRoleNames a <code>String</code> array listing the names for screener roles.
     * @param reviewerRoleNames a <code>String</code> array listing the names for reviewer roles.
     * @param submissionType a <code>long</code> referencing the type of the submission being downloaded.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user in case of errors or
     *         <code>null</code> if submission is downloaded successfully.
     * @throws BaseException if an unexpected error occurs.
     * @throws IOException if an I/O error occurs.
     */
    protected String handleDownloadSubmission(HttpServletRequest request,
                                                   HttpServletResponse response, String errorMessageKey,
                                                   String viewAllSubmissionsPermName,
                                                   String viewMySubmissionsPermissionName,
                                                   String viewSubmissionByScreenerPermissionName,
                                                   String viewMostRecentSubmissionsPermissionName,
                                                   String downloadCustomSubmissionPermissionName,
                                                   String viewWinningSubmissionPermissionName,
                                                   String screeningPhaseName,
                                                   String reviewPhaseName,
                                                   String[] screenerRoleNames,
                                                   String[] reviewerRoleNames,
                                                   long submissionType)
        throws BaseException, IOException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(request, errorMessageKey);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();

        // Verify that upload is a submission
        if (!upload.getUploadType().getName().equalsIgnoreCase("Submission")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(
                    this, request, errorMessageKey, "Error.NotASubmission", null);
        }

        // Verify the status of upload and check whether the user has permission to download old uploads
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted")
            && !AuthorizationHelper.hasUserPermission(request, viewAllSubmissionsPermName)) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(
                    this, request, errorMessageKey, "Error.UploadDeleted", null);
        }

        // Get all phases for the current project (needed to do permission checks)
        Project project = verification.getProject(); 
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(false), verification.getProject());

        boolean noRights = true;

        // Check if it is the Thurgood server trying to get the submission.
        // If it's Thurgood, authorize the request.
        boolean useThurgood = !isEmptyOrNull(project.getProperty("Thurgood Platform")) &&
                !isEmptyOrNull(project.getProperty("Thurgood Language"));
        if (useThurgood && !AuthorizationHelper.isUserLoggedIn(request)) {
            if (ConfigHelper.getThurgoodUsername().equals(request.getParameter("username")) &&
                ConfigHelper.getThurgoodPassword().equals(request.getParameter("password"))) {
                noRights = false;
            }
        }

        if (AuthorizationHelper.hasUserPermission(request, viewAllSubmissionsPermName)) {
            noRights = false;
        }

        if (AuthorizationHelper.hasUserPermission(request, viewMySubmissionsPermissionName)) {
            long owningResourceId = upload.getOwner();
            Resource[] myResources = (Resource[]) request.getAttribute("myResources");
            for (Resource myResource : myResources) {
                if (myResource.getId() == owningResourceId) {
                    noRights = false;
                    break;
                }
            }
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, viewSubmissionByScreenerPermissionName)) {
            // Determine whether Screening phase has already been opened (does not have Scheduled status)
            final boolean isScreeningOpen = ActionsHelper.isInOrAfterPhase(phases, 0, screeningPhaseName);
            // If screener tries to download submission before Screening phase opens,
            // notify him about this wrong-doing and do not let perform the action
            if (AuthorizationHelper.hasUserRole(request, screenerRoleNames) && !isScreeningOpen) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        this, request, errorMessageKey, "Error.IncorrectPhase", null);
            }
            noRights = false;
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, viewMostRecentSubmissionsPermissionName)) {
            // If reviewer tries to download submission before Review phase opens,
            // notify him about this wrong-doing and do not let perform the action
            if (AuthorizationHelper.hasUserRole(request, reviewerRoleNames)
                    && !ActionsHelper.isInOrAfterPhase(phases, 0, reviewPhaseName)) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        this, request, errorMessageKey, "Error.IncorrectPhase", null);
            }

            // Regular reviewers can view checkpoint submissions only after the Review phase has started
            if (submissionType == 3
                    && !AuthorizationHelper.hasUserRole(request, Constants.CHECKPOINT_REVIEWER_ROLE_NAME)
                    && AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES)
                    && !ActionsHelper.isInOrAfterPhase(phases, 0, Constants.REVIEW_PHASE_NAME)) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        this, request, errorMessageKey, "Error.IncorrectPhase", null);
            }
            noRights = false;
        }

        // For the Submitters we only allow to download others' submissions if the user has at least passed screening.
        if (noRights && AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) {
            // Get all submissions for this user.
            Resource resource = ActionsHelper.getMyResourceForRole(request, Constants.SUBMITTER_ROLE_NAME);
            UploadManager upMgr = ActionsHelper.createUploadManager();
            Long[] subIds = resource.getSubmissions();

            // Check that the user has a submission that passed screening.
            // We don't need to check the current phase because if it is still prior to the Appeals Response
            // the user won't be able to download others' submissions anyway.
            boolean passedScreening = false;
            for (Long id : subIds) {
                Submission submission = upMgr.getSubmission(id);
                if (submission != null && submission.getSubmissionType().getName().equals(Constants.CONTEST_SUBMISSION_TYPE_NAME) &&
                    !submission.getSubmissionStatus().getName().equals(Constants.FAILED_SCREENING_SUBMISSION_STATUS_NAME)) {
                    passedScreening = true;
                    break;
                }
            }

            if (!passedScreening) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        this, request, "ViewSubmission", "Error.NoScreeningPassed", null);
            }

            // Submitters can download others' contest submissions and checkpoint submissions only
            // after the Appeals Response phase (or Review phase if Appeals Response phase is not present)
            // is closed. This also takes care of contests with Iterative Review phase, for which submitters
            // can't download others' submissions at all.
            if (submissionType == 1 || submissionType == 3) {
                Phase reviewPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.APPEALS_RESPONSE_PHASE_NAME);
                if (reviewPhase == null) {
                    reviewPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.REVIEW_PHASE_NAME);
                }
                boolean isReviewFinished = (reviewPhase != null) && (reviewPhase.getPhaseStatus().getId() == 3);
                if (isReviewFinished) {
                    noRights = false;
                }
            }
        }

        if (noRights && submissionType == 1
                && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_CURRENT_ITERATIVE_REVIEW_SUBMISSION)) {
            Submission[] submissions = ActionsHelper.getProjectSubmissions(upload.getProject(),
                    Constants.CONTEST_SUBMISSION_TYPE_NAME, null, false);
            Submission earliestSubmission = null;
            Resource resource = ActionsHelper.getMyResourceForRole(request, Constants.ITERATIVE_REVIEWER_ROLE_NAME);
            List<Filter> filters = new ArrayList<Filter>();
            Filter filterScorecard = new EqualToFilter("scorecardType", LookupHelper.getScorecardType("Iterative Review").getId());
            Filter filterReviewer = new EqualToFilter("reviewer", resource.getId());
            filters.add(filterScorecard);
            filters.add(filterReviewer);
            Filter filterForReviews = new AndFilter(filters);
            ReviewManager revMgr = ActionsHelper.createReviewManager();
            Review[] reviews = revMgr.searchReviews(filterForReviews, false);
            for (Submission submission : submissions) {
                // Find the earliest active submission, i.e. the next one in the queue.
                if (submission.getSubmissionStatus().getName().equals("Active") && (earliestSubmission == null
                        || earliestSubmission.getCreationTimestamp().compareTo(submission.getCreationTimestamp()) > 0)) {
                    earliestSubmission = submission;
                }
                // If this reviewer already has a review scorecard submitted for this submission, that reviewer can
                // download the submission.
                if (submission.getUpload().getId() == upload.getId()) {
                    for (Review review : reviews) {
                        if (review.getSubmission() == submission.getId()) {
                            noRights = false;
                        }
                    }
                }
            }

            // If it's the "current" submission (i.e. the next one in the queue), the reviewer can download the submission.
            if (earliestSubmission != null && earliestSubmission.getUpload().getId() == upload.getId()) {
                noRights = false;
            }
        }

        ActionsHelper.logDownloadAttempt(request, upload, !noRights);

        if (noRights) {
            return ActionsHelper.produceErrorReport(this, request,
                    errorMessageKey, "Error.NoPermission", Boolean.FALSE);
        }

        processSubmissionDownload(upload, request, response);

        return null;
    }

    /**
     * <p>Handles the request for uploading a submission to project.</p>
     *
     * @param uploadSubmissionForm a model providing the form submitted by user.
     * @param request an <code>HttpServletRequest</code> providing the details for incoming request.
     * @param submissionTypeName a <code>String</code> providing the name of submission type.
     * @param submitPermissionName a <code>String</code> providing the permission to be used for authorizing users to
     *        perform submission upload.
     * @param phaseName a <code>String</code> providing the name of the type of project phase which submission maps to.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws BaseException if an unexpected error occurs.
     */
    protected String handleUploadSubmission(DynamicModel uploadSubmissionForm, HttpServletRequest request,
                                                 String submissionTypeName, String submitPermissionName,
                                                 String phaseName)
        throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Determine if this request is a post back
        final boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                this, request, submitPermissionName, postBack);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Retrieve current project
        Project project = verification.getProject();
        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);

        Phase currentPhase = ActionsHelper.getPhase(phases, true, phaseName);
        if (currentPhase == null) {
            return ActionsHelper.produceErrorReport(this, request,
                    submitPermissionName, "Error.IncorrectPhase", null);
        }

        // We don't allow user to upload contest submissions/checkpoint submissions for studio contest
        if ("Studio".equalsIgnoreCase(project.getProjectCategory().getProjectType().getName())
                && (submissionTypeName.equals(Constants.CONTEST_SUBMISSION_TYPE_NAME) ||
                    submissionTypeName.equals(Constants.CHECKPOINT_SUBMISSION_TYPE_NAME))) {
            return ActionsHelper.produceErrorReport(this, request,
                    submitPermissionName, "Error.UploadForStudio", null);
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);
            return Constants.DISPLAY_PAGE_FORWARD_NAME;
        }

        FormFile file = (FormFile) getModel().get("file");

        // Disallow uploading of empty files
        if (file == null || file.getFileSize() == 0) {
            return ActionsHelper.produceErrorReport(this, request,
                                                    submitPermissionName, "Error.EmptyFileUploaded", null);
        }

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        // Obtain an instance of File Upload Manager
        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForRole(request, "Submitter");

        Submission submission = new Submission();
        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setProjectPhase(currentPhase.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
        upload.setUploadType(LookupHelper.getUploadType("Submission"));
        upload.setParameter(uploadedFile.getFileId());

        submission.setUpload(upload);
        submission.setSubmissionStatus(LookupHelper.getSubmissionStatus("Active"));
        submission.setSubmissionType(LookupHelper.getSubmissionType(submissionTypeName));

        // Get the name (id) of the user performing the operations
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));

        // If the project DOESN'T allow multiple submissions hence its property "Allow
        // multiple submissions" will be false
        Boolean allowOldSubmissions = Boolean.parseBoolean((String) project.getProperty("Allow multiple submissions"));

        UploadManager upMgr = ActionsHelper.createUploadManager();
        upMgr.createUpload(upload, operator);

        // set the Thurgood job id if the contest is from CloudSpokes
        String thurgoodPlatform = (String) project.getProperty("Thurgood Platform");
        String thurgoodLanguage = (String) project.getProperty("Thurgood Language");
        // Thurgood will be used only for Contest Submissions
        if (!isEmptyOrNull(thurgoodPlatform) && !isEmptyOrNull(thurgoodLanguage)
                && submissionTypeName.equals(Constants.CONTEST_SUBMISSION_TYPE_NAME)) {

            Map<String, String> parameters =
                buildCreateThurgoodParameters(project, thurgoodPlatform, thurgoodLanguage, upload.getId(), request);
            if (parameters != null) {
                String thurgoodJobId = submitThurgoodJob(createThurgoodJob(parameters));
                submission.setThurgoodJobId(thurgoodJobId);
            }
        }

        upMgr.createSubmission(submission, operator);
        resource.addSubmission(submission.getId());
        ActionsHelper.createResourceManager().updateResource(resource, operator);

        Submission[] activeSubmissions = ActionsHelper.getResourceSubmissions(resource.getId(), submissionTypeName,
                Constants.ACTIVE_SUBMISSION_STATUS_NAME, false);

        // Now depending on whether the project allows multiple submissions or not mark the old submission
        // and the upload as deleted.
        if (activeSubmissions.length > 1 && !allowOldSubmissions) {
            SubmissionStatus deleteSubmissionStatus = LookupHelper.getSubmissionStatus("Deleted");
            UploadStatus deleteUploadStatus = LookupHelper.getUploadStatus("Deleted");
            for (Submission activeSubmission : activeSubmissions) {
                if (activeSubmission.getId() != submission.getId()) {
                    activeSubmission.getUpload().setUploadStatus(deleteUploadStatus);
                    activeSubmission.setSubmissionStatus(deleteSubmissionStatus);
                    upMgr.updateUpload(activeSubmission.getUpload(), operator);
                    upMgr.updateSubmission(activeSubmission, operator);
                }
            }
        }

        AmazonSNSHelper.publishProjectUpdateEvent(project);

        this.pid = project.getId();
        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * Build the parameters for creating Thurgood job.
     * @param project the current project
     * @param thurgoodPlatform the Thurgood platform
     * @param thurgoodLanguage the Thurgood language
     * @param uploadId the upload id
     * @param request the http servlet request
     * @return the parameters map
     * @throws BaseException if any error
     */
    private Map<String, String> buildCreateThurgoodParameters(Project project, String thurgoodPlatform,
        String thurgoodLanguage, long uploadId, HttpServletRequest request) throws BaseException {

        // Get the ID of the sender
        long senderId = AuthorizationHelper.getLoggedInUserId(request);

        // Obtain an instance of User Retrieval
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        // Retrieve information about an external user by its ID
        ExternalUser user = userRetrieval.retrieveUser(senderId);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", user.getEmail());
        parameters.put("language", thurgoodLanguage);
        parameters.put("userId", user.getHandle());
        parameters.put("notification", "email");
        parameters.put("codeUrl", ConfigHelper.getThurgoodCodeURL() + uploadId);
        parameters.put("platform", thurgoodPlatform);
        return parameters;
    }

    /**
     * Check if the given object is a null or empty string.
     * As it is called, it is guaranteed that the value is of string type.
     * @param value the value to be checked.
     * @return true if the string object is null or empty.
     */
    protected boolean isEmptyOrNull(Object value) {
        return value == null || ((String) value).trim().length() == 0;
    }

    /**
     * Submit the Thurgood job.
     * @param id the Thurgood job id.
     * @return the submitted job id, null if submitting fails
     */
    private static String submitThurgoodJob(String id) {
        String authHeader = "Token token=\"%s\"";
        if (id != null) {
            HttpURLConnection con = null;
            BufferedReader in = null;
            try {
                URL obj = new URL(ConfigHelper.getThurgoodApiURL() + "/" + id + "/submit");
                con = (HttpURLConnection) obj.openConnection();
                // set connection timeout
                con.setConnectTimeout(ConfigHelper.getThurgoodTimeout());

                // optional default is GET
                con.setRequestMethod("PUT");

                //add request header
                con.setRequestProperty("Authorization", String.format(authHeader, ConfigHelper.getThurgoodApiKey()));
                int responseCode = con.getResponseCode();

                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    LoggingHelper.logError("Server backend error happened with response " + response);
                    return null;
                }
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode root = (ObjectNode) mapper.readTree(response.toString());
                if (root.get("success") == null || !"true".equalsIgnoreCase(root.get("success").asText())) {
                    LoggingHelper.logError("Submitting Thurgood job failed with response " + response);
                    return null;
                }
                return id;
            } catch (MalformedURLException e) {
                LoggingHelper.logException("The requested url is malformed", e);
            } catch (IOException e) {
                LoggingHelper.logException("IO error while submitting the Thurgood job", e);
            } catch (Exception e) {
                LoggingHelper.logException("Error occurs while submitting the Thurgood job", e);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                closeResource(in);
            }
        }
        return null;
    }

    /**
     * Create the Thurgood Job.
     * @param parameters the submitted parameters
     * @return the created job id, null if creation fails
     */
    private static String createThurgoodJob(Map<String, String> parameters) {
        String authHeader = "Token token=\"%s\"";
        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        try {
            URL obj = new URL(ConfigHelper.getThurgoodApiURL());
            con = (HttpURLConnection) obj.openConnection();
            // set connection timeout
            con.setConnectTimeout(ConfigHelper.getThurgoodTimeout());

            //set header
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", String.format(authHeader, ConfigHelper.getThurgoodApiKey()));

            StringBuilder urlParameters = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                urlParameters.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }

            // Send post request
            con.setDoOutput(true);
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters.toString());
            wr.flush();

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                LoggingHelper.logError("Server backend error happened with response " + response);
                return null;
            }

            //get the job id.
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = (ObjectNode) mapper.readTree(response.toString());
            if (root.get("success") == null || !"true".equalsIgnoreCase(root.get("success").asText())
                    || root.get("data") == null) {
                LoggingHelper.logError("Creating Thurgood job failed with response " + response);
                return null;
            }
            JsonNode idNode = root.get("data").get("_id");
            if (idNode == null || idNode.asText().trim().length() == 0) {
                LoggingHelper.logError("No Created Job ID returned with response " + response);
                return null;
            }
            return idNode.asText();
        } catch (MalformedURLException e) {
            LoggingHelper.logException("The requested url is malformed", e);
        } catch (IOException e) {
            LoggingHelper.logException("IO error while creating the Thurgood job", e);
        } catch (Exception e) {
            LoggingHelper.logException("Error occurs while creating the Thurgood job", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
            closeResource(wr);
            closeResource(in);
        }
        return null;
    }

    /**
     * Close the resource if it is not null.
     * @param resource the resource to be closed.
     */
    private static void closeResource(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                LoggingHelper.logException("IO error while closing the resource", e);
            }
        }
    }
    /**
     * Getter of pid.
     * @return the pid
     */
    public long getPid() {
        return pid;
    }
    /**
     * Setter of pid.
     * @param pid the pid to set
     */
    public void setPid(long pid) {
        this.pid = pid;
    }
}

