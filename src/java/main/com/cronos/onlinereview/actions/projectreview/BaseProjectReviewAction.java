/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.actions.DynamicModelDrivenAction;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.model.FormFile;
import com.cronos.onlinereview.phases.OnlineReviewServices;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LookupHelper;
import com.cronos.onlinereview.util.StrutsRequestParser;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.review.ReviewEntityNotFoundException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.data.ReviewEditor;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This is the base class for project review actions classes.
 * It provides the basic functions which will be used by all project review actions.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseProjectReviewAction extends DynamicModelDrivenAction {
    /**
     * The maximum size of the comment length.
     */
    protected static final int MAX_COMMENT_LENGTH = 4096;

    protected static final long ACTIVE_SCORECARD = 1;

    /**
     * This member variable is a constant that specifies the count of comments displayed for each
     * item by default on Edit Screening, Edit Review, and Edit Approval, Edit Post-Mortem pages.
     */
    protected static final int DEFAULT_COMMENTS_NUMBER = 1;

    /**
     * This member variable is a constant that specifies the count of comments displayed for each
     * item when Manager opens either Edit Screening, Edit Review, or Edit Approval or Edit Post-Mortem page.
     */
    protected static final int MANAGER_COMMENTS_NUMBER = 1;

    /**
     * This member variable holds the all possible values of answers to &#39;Scale&#160;(1-4)&#39;
     * and &#39;Scale&#160;(1-10)&#39; types of scorecard question.
     */
    protected static final Map<String, Set<String>> correctAnswers = new HashMap<String, Set<String>>();

    /**
     * Represents the project id.
     */
    private long pid;

    /**
     * Represents the review id.
     */
    private long rid;

    // Initialize static fields
    static {
        String scale1_4 = "Scale (1-4)";
        String scale1_10 = "Scale (1-10)";
        String scale0_3 = "Scale (0-3)";
        String scale0_9 = "Scale (0-9)";
        String scale0_4 = "Scale (0-4)";
        correctAnswers.put(scale1_4, new HashSet<String>());
        correctAnswers.put(scale1_10, new HashSet<String>());
        correctAnswers.put(scale0_3, new HashSet<String>());
        correctAnswers.put(scale0_9, new HashSet<String>());
        correctAnswers.put(scale0_4, new HashSet<String>());
        for (int i = 0; i <= 10; i++) {
            if (i <= 3) {
                correctAnswers.get(scale0_3).add(i + "/3");
            }
            if (i >= 1 && i <= 4) {
                correctAnswers.get(scale1_4).add(i + "/4");
            }
            if (i >= 0 && i <= 4) {
                correctAnswers.get(scale0_4).add(i + "/4");
            }
            if (i >= 1 && i <= 10) {
                correctAnswers.get(scale1_10).add(i + "/10");
            }
            if (i <= 9) {
                correctAnswers.get(scale0_9).add(i + "/9");
            }
        }
    }
    
    /**
     * This method verifies the request for ceratins conditions to be met. This includes verifying
     * if the user has specified an ID of the submission he wants to perform an operation on, if the
     * ID of the submission specified by user denotes an existing submission, and whether the user
     * has enough rights to perform the operation specified by <code>permission</code> parameter.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which specifies whether the
     *         check was successful and, in the case the check was successful, contains additional
     *         information retrieved during the check operation, which might be of some use for the
     *         calling method.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is required.
     * @throws BaseException
     *             if any error occurs.
     */
    protected CorrectnessCheckResult checkForCorrectSubmissionId(HttpServletRequest request, String permission)
        throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Submission ID was specified and denotes correct submission
        String sidParam = request.getParameter("sid");
        if (sidParam == null || sidParam.trim().length() == 0) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.SubmissionIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long sid;

        try {
            // Try to convert specified sid parameter to its integer representation
            sid = Long.parseLong(sidParam, 10);
        } catch (NumberFormatException e) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.SubmissionNotFound", null));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Deliverable Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();
        // Get Submission by its id
        Submission submission = upMgr.getSubmission(sid);
        // Verify that submission with specified ID exists
        if (submission == null) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.SubmissionNotFound", null));
            // Return the result of the check
            return result;
        }

        // Store Submission object in the result bean
        result.setSubmission(submission);
        // Place the id of the submission as attribute in the request
        request.setAttribute("sid", sid);

        // Retrieve the project following submission's information chain
        Project project = ActionsHelper.getProjectForSubmission(submission);
        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, project.getId());

        // Return the result of the check
        return result;
    }

    /**
     * This method verifies the request for certain conditions to be met. This includes verifying
     * if the user has specified an ID of project he wants to perform an operation on, if the
     * ID of the project specified by user denotes an existing project, and whether the user
     * has enough rights to perform the operation specified by <code>permission</code> parameter.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which specifies whether the
     *         check was successful and, in the case the check was successful, contains additional
     *         information retrieved during the check operation, which might be of some use for the
     *         calling method.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is required.
     * @throws BaseException
     *             if any error occurs.
     */
    private CorrectnessCheckResult checkForCorrectProjectId(HttpServletRequest request,
                                                            String permission) throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Project ID was specified and denotes correct project
        String pidParam = request.getParameter("pid");
        if (pidParam == null || pidParam.trim().length() == 0) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.ProjectIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long pid;

        try {
            // Try to convert specified pid parameter to its integer representation
            pid = Long.parseLong(pidParam, 10);
        } catch (NumberFormatException e) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.ProjectNotFound", null));
            // Return the result of the check
            return result;
        }

        // Retrieve the project following submission's information chain
        Project project = ActionsHelper.createProjectManager().getProject(pid);
        if (project == null) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.ProjectNotFound", null));
            // Return the result of the check
            return result;
        }

        request.setAttribute("pid", pid);

        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, project.getId());

        // Return the result of the check
        return result;
    }

    /**
     * This method verifies the request for certain conditions to be met. This includes verifying
     * if the user has specified an ID of the review he wants to perform an operation on, if the
     * ID of the review specified by user denotes an existing review, and whether the user
     * has enough rights to perform the operation specified by <code>permission</code> parameter.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which specifies whether the
     *         check was successful and, in the case the check was successful, contains additional
     *         information retrieved during the check operation, which might be of some use for the
     *         calling method.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is required.
     * @throws BaseException
     *             if any error occurs.
     */
    protected CorrectnessCheckResult checkForCorrectReviewId(HttpServletRequest request, String permission)
        throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Review ID was specified and denotes correct review
        String ridParam = request.getParameter("rid");
        if (ridParam == null || ridParam.trim().length() == 0) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.ReviewIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long rid;

        try {
            // Try to convert specified rid parameter to its integer representation
            rid = Long.parseLong(ridParam, 10);
        } catch (NumberFormatException e) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.ReviewNotFound", null));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager();

        /*
         * Review Management Persistence component throws an exception
         * if the review with specified ID does not exist in the database,
         * so this exception should be handled correctly
         */

        Review review = null;
        try {
            // Get Review by its id
            review = revMgr.getReview(rid);
        } catch (ReviewEntityNotFoundException e) {
            // Eat the exception
        }

        // Verify that review with specified ID exists
        if (review == null) {
            result.setResult(ActionsHelper.produceErrorReport(
                    this, request, permission, "Error.ReviewNotFound", null));
            // Return the result of the check
            return result;
        }

        // Store Review object in the result bean
        result.setReview(review);
        // Place the review object as attribute in the request
        request.setAttribute("review", review);
        Project project;

        // Review may not be associated to submission
        if (review.getSubmission() > 0) {
            // Obtain an instance of Deliverable Manager
            UploadManager upMgr = ActionsHelper.createUploadManager();
            // Get Submission by its id
            Submission submission = upMgr.getSubmission(review.getSubmission());

            // Store Submission object in the result bean
            result.setSubmission(submission);
            // Place the id of the submission as attribute in the request
            request.setAttribute("sid", submission.getId());

            // Retrieve the project following submission's information chain
            project = ActionsHelper.getProjectForSubmission(submission);
        } else {
            long reviewAuthorId = review.getAuthor();
            Resource resource = ActionsHelper.createResourceManager().getResource(reviewAuthorId);
            project = ActionsHelper.createProjectManager().getProject(resource.getProject());
        }


        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, project.getId());

        // Return the result of the check
        return result;
    }

    /**
     * This static method gathers some valuable information for aggregation scorecard. This
     * information includes: the user ID of submitter, the user ID of aggregator, the reviewer
     * resources (who initially did the reviews which was later combined into one single
     * aggregation), individual reviews made by those reviewers, etc.
     *
     * @param request
     *            an <code>HttpServletRequest</code> object.
     * @param verification
     *            an instance of <code>CorrectnessCheckResult</code> class that must specify valid
     *            current project and aggregation for this method to succeed.
     * @param scorecardTemplate
     *            a scorecard template that describes questions (items) of the aggregation.
     * @param reviewType
     *            a type of the review, can be one of "Aggregation", "AggregationReview", "FinalReview"
     * @throws BaseException
     *             if any error occurs.
     */
    protected void retrieveAndStoreBasicAggregationInfo(HttpServletRequest request,
                                                      CorrectnessCheckResult verification, Scorecard scorecardTemplate, String reviewType)
        throws BaseException {
        // Retrieve a project from verification-result bean
        Project project = verification.getProject();
        // Retrieve a review from verification-result bean
        Review review = verification.getReview();

        // Retrieve a submission to edit an aggregation scorecard for
        Submission submission = verification.getSubmission();

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        // Get an array of all phases for current project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(false), project);

        // Get a Review phase
        Phase reviewPhase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);
        // Retrieve all resources (reviewers) for that phase
        Resource[] reviewResources = ActionsHelper.getAllResourcesForPhase(reviewPhase);
        for (Resource reviewResource : reviewResources) {
            ActionsHelper.populateEmailProperty(request, reviewResource);
        }
        // Place information about reviews into the request
        request.setAttribute("reviewResources", reviewResources);

        // Prepare a list of reviewer IDs. This list will later be used to build filter
        List<Long> reviewerIds = new ArrayList<Long>();
        for (Resource reviewResource : reviewResources) {
            reviewerIds.add(reviewResource.getId());
        }

        // Build filters to fetch the reviews that were used to form current Aggregation
        Filter filterResources = new InFilter("reviewer", reviewerIds);
        Filter filterCommitted = new EqualToFilter("committed", 1);
        Filter filterSubmission = new EqualToFilter("submission", submission.getId());
        Filter filterProject = new EqualToFilter("project", project.getId());
        Filter filterScorecard = new EqualToFilter(
                "scorecardType", scorecardTemplate.getScorecardType().getId());

        // Prepare final filter that combines all the above filters
        Filter filter = new AndFilter(Arrays.asList(filterResources, filterCommitted, filterSubmission, filterProject, filterScorecard));

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager();
        // Fetch reviews (only basic review info is fetched, no items/comments)
        Review[] reviews = revMgr.searchReviews(filter, false);
        // Place reviews into the request. This will be used to provide links to individual reviews
        request.setAttribute("reviews", reviews);

        int[] lastCommentIdxs = new int[review.getNumberOfItems()];

        for (int i = 0; i < lastCommentIdxs.length; ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                String commentType = item.getComment(j).getCommentType().getName();
                if (!commentType.equalsIgnoreCase("Aggregation Comment")) {
                    lastCommentIdxs[i] = j;
                }
            }
        }
        request.setAttribute("lastCommentIdxs", lastCommentIdxs);
    }

    /**
     * Retrieve and store the review lookup data.
     *
     * @param request the http servlet request
     * @throws BaseException if any error
     */
    protected void retrieveAndStoreReviewLookUpData(HttpServletRequest request) throws BaseException {
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                LookupHelper.getCommentType("Comment"),
                LookupHelper.getCommentType("Required"),
                LookupHelper.getCommentType("Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);
    }

    /**
     * Retrieve and store the review author info.
     *
     * @param request the http servlet request
     * @param review the review data
     * @throws BaseException if any error
     */
    private void retrieveAndStoreReviewAuthorInfo(HttpServletRequest request, Review review)
        throws BaseException {
        // TODO: Remove this and other functions to a separate helper class. Name it ProjectReviewActionsHelper

        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(review, "review");

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager();
        // Get review author's resource
        Resource author = resMgr.getResource(review.getAuthor());
        ActionsHelper.populateEmailProperty(request, author);

        // Place submitter's user ID into the request
        request.setAttribute("authorId", author.getProperty("External Reference ID"));
        // Place submitter's resource into the request
        request.setAttribute("authorResource", author);
    }

    /**
     * Retrieve and store the basic review info.
     *
     * @param request the http servlet request
     * @param verification the verification
     * @param reviewType the review type
     * @param scorecardTemplate the scorecard template
     * @throws BaseException if any error
     */
    protected void retrieveAndStoreBasicReviewInfo(HttpServletRequest request,
                                                 CorrectnessCheckResult verification, String reviewType, Scorecard scorecardTemplate)
        throws BaseException {
        boolean isSubmissionDependentPhase = !reviewType.equals("Post-Mortem");

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);
        // Retrieve an information about my role(s) and place it into the request
        ActionsHelper.retrieveAndStoreMyRole(request, this);
        // Retrieve the information about the submitter and place it into the request
        if (isSubmissionDependentPhase) {
            ActionsHelper.retrieveAndStoreSubmitterInfo(request, verification.getSubmission().getUpload());
        }

        Review review = verification.getReview();
        if (review != null) {
            // Retrieve the information about the review author and place it into the request
            retrieveAndStoreReviewAuthorInfo(request, review);
            request.setAttribute("modificationDate", review.getModificationTimestamp());
        }
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);
        // Place the type of the review into the request
        if (reviewType.equals("Post-Mortem")) {
            request.setAttribute("reviewType", "PostMortem");
        } else if (reviewType.equals("Specification Review")) {
            request.setAttribute("reviewType", "SpecificationReview");
        } else if (reviewType.equals("Checkpoint Screening")) {
            request.setAttribute("reviewType", "CheckpointScreening");
        } else if (reviewType.equals("Checkpoint Review")) {
            request.setAttribute("reviewType", "CheckpointReview");
        } else if (reviewType.equals("Iterative Review")) {
            request.setAttribute("reviewType", "IterativeReview");
        } else {
            request.setAttribute("reviewType", reviewType);
        }
    }


    /**
     * <p>Handles the request for creating the generic review details.</p>
     *
     *
     * @param request an <code>HttpServletRequest</code> representing the incoming request from the client.
     * @param reviewType a <code>String</code> referencing the type of the review.
     * @return a <code>String</code> referencing the next view to be used for processing the request.
     * @throws BaseException if an unexpected error occurs.
     */
    protected String createGenericReview(HttpServletRequest request, String reviewType) throws BaseException {

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        String permName;
        String phaseName;
        boolean isPostMortemPhase = false;
        // Determine permission name and phase name from the review type
        if ("Screening".equals(reviewType)) {
            permName = Constants.PERFORM_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
        } else if ("Review".equals(reviewType)) {
            permName = Constants.PERFORM_REVIEW_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
        } else if ("Approval".equals(reviewType)) {
            permName = Constants.PERFORM_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
        } else if ("Specification Review".equals(reviewType)) {
            permName = Constants.PERFORM_SPECIFICATION_REVIEW_PERM_NAME;
            phaseName = Constants.SPECIFICATION_REVIEW_PHASE_NAME;
        } else if ("Checkpoint Screening".equals(reviewType)) {
            permName = Constants.PERFORM_CHECKPOINT_SCREENING_PERM_NAME;
            phaseName = Constants.CHECKPOINT_SCREENING_PHASE_NAME;
        } else if ("Checkpoint Review".equals(reviewType)) {
            permName = Constants.PERFORM_CHECKPOINT_REVIEW_PERM_NAME;
            phaseName = Constants.CHECKPOINT_REVIEW_PHASE_NAME;
        } else if ("Iterative Review".equals(reviewType)) {
            permName = Constants.PERFORM_ITERATIVE_REVIEW_PERM_NAME;
            phaseName = Constants.ITERATIVE_REVIEW_PHASE_NAME;
        } else {
            isPostMortemPhase = true;
            permName = Constants.PERFORM_POST_MORTEM_REVIEW_PERM_NAME;
            phaseName = Constants.POST_MORTEM_PHASE_NAME;
        }

        // Verify that certain requirements are met before proceeding with the Action
        // If any error has occurred, return action forward contained in the result bean
        if (isPostMortemPhase) {
            verification = checkForCorrectProjectId(request, permName);
        } else {
            verification = checkForCorrectSubmissionId(request, permName);
        }
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that user has the permission to perform review
        if (!AuthorizationHelper.hasUserPermission(request, permName)) {
            return ActionsHelper.produceErrorReport(
                    this, request, permName, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);

        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, phaseName);
        // Check that the phase in question is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(
                    this, request, permName, "Error.IncorrectPhase", null);
        }

        // Get "My" resource for the appropriate phase
        // For Post-Mortem and Approval phases resource is retrieved by role but not by phase
        Resource myResource;
        if (phase.getPhaseType().getName().equals(Constants.POST_MORTEM_PHASE_NAME)) {
            myResource = ActionsHelper.getMyResourceForRole(request, Constants.POST_MORTEM_REVIEWER_ROLE_NAME);
        } else if (phase.getPhaseType().getName().equals(Constants.APPROVAL_PHASE_NAME)) {
            myResource = ActionsHelper.getMyResourceForRole(request, Constants.APPROVER_ROLE_NAME);
        } else if (phase.getPhaseType().getName().equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)) {
            myResource = ActionsHelper.getMyResourceForRole(request, Constants.ITERATIVE_REVIEWER_ROLE_NAME);
        } else {
            myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        }
        // Retrieve a scorecard template for the appropriate phase
        Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(phase, false);

        /*
         * Verify that the user is not trying to create review that already exists
         */
        // Prepare filters
        Filter filterResource = new EqualToFilter("reviewer", myResource.getId());
        Filter filterScorecard = new EqualToFilter("scorecardType", scorecardTemplate.getScorecardType().getId());
        Filter filterPhase = new EqualToFilter("projectPhase", phase.getId());

        Filter filter;
        if (isPostMortemPhase) {
            // Prepare final combined filter
            filter = new AndFilter(Arrays.asList(filterResource, filterScorecard, filterPhase));
        } else {
            // Prepare final combined filter
            Filter filterSubmission = new EqualToFilter("submission", verification.getSubmission().getId());
            filter = new AndFilter(Arrays.asList(filterResource, filterSubmission, filterScorecard, filterPhase));
        }

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager();
        // Retrieve an array of reviews
        Review[] reviews = revMgr.searchReviews(filter, false);

        // Non-empty array of reviews indicates that user is trying to create review that already exists
        if (reviews.length != 0) {
            // Forward to Edit Screening page
            this.rid = reviews[0].getId();
            return Constants.EDIT_FORWARD_NAME;
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);
        // Place current user's id as author's id
        request.setAttribute("authorId", AuthorizationHelper.getLoggedInUserId(request));
        // Retrieve some look-up data and store it into the request
        retrieveAndStoreReviewLookUpData(request);

        /*
         * Populate the form
         */

        // Determine the number of questions in scorecard template
        int questionsCount = ActionsHelper.getScorecardQuestionsCount(scorecardTemplate);

        String[] emptyStrings = new String[questionsCount];
        Arrays.fill(emptyStrings, "");

        // Populate form properties
        getModel().set("answer", emptyStrings);

        CommentType typeComment = LookupHelper.getCommentType("Comment");

        Integer[] commentCounts = new Integer[questionsCount];
        Arrays.fill(commentCounts, DEFAULT_COMMENTS_NUMBER);
        getModel().set("comment_count", commentCounts);

        for (int i = 0; i < questionsCount; i++) {
            for (int j = 0; j <= DEFAULT_COMMENTS_NUMBER; j++) {
                getModel().set("comment", i + "." + j, "");
                getModel().set("comment_type", i + "." + j, typeComment);
            }
        }

        request.setAttribute("tableTitle", scorecardTemplate.getName());

        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * Edit the generic review.
     *
     *
     * @param request the http servlet request
     * @param reviewType the review type
     * @return the string result
     * @throws BaseException if any error
     */
    protected String editGenericReview(HttpServletRequest request, String reviewType) throws BaseException {

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        String scorecardTypeName;
        // Determine permission name and phase name from the review type
        boolean isSpecReviewPhase = false;
        if ("Screening".equals(reviewType)) {
            scorecardTypeName = "Screening";
        } else if ("Review".equals(reviewType)) {
            scorecardTypeName = "Review";
        } else if ("Approval".equals(reviewType)) {
            scorecardTypeName = "Approval";
        } else if ("Specification Review".equals(reviewType)) {
            scorecardTypeName = "Specification Review";
            isSpecReviewPhase = true;
        } else if ("Checkpoint Screening".equals(reviewType)) {
            scorecardTypeName = "Checkpoint Screening";
        } else if ("Checkpoint Review".equals(reviewType)) {
            scorecardTypeName = "Checkpoint Review";
        } else if ("Iterative Review".equals(reviewType)) {
            scorecardTypeName = "Iterative Review";
        } else {
            scorecardTypeName = "Post-Mortem";
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification =
                checkForCorrectReviewId(request, Constants.EDIT_MY_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorMgr = ActionsHelper.createScorecardManager();
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scorMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }

        boolean managerEdit = false;
        // Check if review has been committed
        if (review.isCommitted()) {
            // If user has a Manager or Global Manager role, put special flag to the request
            // indicating that we need "Manager Edit"
            // Nobody is allowed to edit committed iterative review scorecard
            if (!scorecardTypeName.equals("Iterative Review") && AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                request.setAttribute("managerEdit", Boolean.TRUE);
                managerEdit = true;
            } else {
                return ActionsHelper.produceErrorReport(this, request,
                            Constants.EDIT_ANY_SCORECARD_PERM_NAME, "Error.ReviewCommitted", null);
            }
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        // Verify that the user has permission to edit review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_ANY_SCORECARD_PERM_NAME)) {
            if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_MY_REVIEW_PERM_NAME)) {
                return ActionsHelper.produceErrorReport(this,
                    request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            } else if (verification.getReview().getAuthor() !=
                    ((Resource) request.getAttribute("authorResource")).getId()) {
                return ActionsHelper.produceErrorReport(this,
                        request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve some look-up data and store it into the request
        retrieveAndStoreReviewLookUpData(request);

        // Prepare the arrays
        String[] answers = new String[review.getNumberOfItems()];

        int itemIdx = 0;

        // Walk the items in the review setting appropriate values in the arrays
        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                    Item item = review.getItem(itemIdx);
                    Comment[] comments = (managerEdit) ? getItemManagerComments(item) : getItemReviewerComments(item);

                    answers[itemIdx] = (String) item.getAnswer();

                    getModel().set("comment_type", itemIdx + ".0", null);
                    getModel().set("comment", itemIdx + ".0", "");

                    final int commentCount =
                        Math.max(comments.length, (managerEdit) ? MANAGER_COMMENTS_NUMBER : DEFAULT_COMMENTS_NUMBER);

                    getModel().set("comment_count", itemIdx, commentCount);

                    for (int i = 0; i < commentCount; ++i) {
                        String commentKey = itemIdx + "." + (i + 1);
                        Comment comment = (i < comments.length) ? comments[i] : null;

                        getModel().set("comment", commentKey, (comment != null) ? comment.getComment() : "");
                        getModel().set("comment_type", commentKey,
                                (comment != null) ? comment.getCommentType().getId() :
                                        LookupHelper.getCommentType("Comment").getId());
                    }
                }
            }
        }

        if (!managerEdit) {
            request.setAttribute("uploadedFileIds", collectUploadedFileIds(scorecardTemplate, review));
        }

        /*
         * Populate the form properties which weren't populated above
         */

        // Populate form properties
        getModel().set("answer", answers);

        if (isSpecReviewPhase) {
            // set the approve specification check box.
            boolean specReviewApproved = false;
            Comment[] allComments = review.getAllComments();
            for (Comment comment : allComments) {
                if ("Specification Review Comment".equalsIgnoreCase(comment.getCommentType().getName())) {
                    specReviewApproved = "Approved".equals(comment.getExtraInfo());
                    break;
                }
            }
            getModel().set("approve_specification", specReviewApproved);
        }

        request.setAttribute("tableTitle", scorecardTemplate.getName());

        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * Get the manager comments.
     *
     * @param item the item to be found
     * @return the comments array
     */
    private static Comment[] getItemManagerComments(Item item) {
        List<Comment> result = new ArrayList<Comment>();
        for (int i = 0; i < item.getNumberOfComments(); i++) {
            Comment comment = item.getComment(i);
            if (ActionsHelper.isManagerComment(comment)) {
                result.add(comment);
            }
        }
        return result.toArray(new Comment[result.size()]);
    }

    /**
     * Get the reviewer comments.
     *
     * @param item the item to be found
     * @return the comments array
     */
    private static Comment[] getItemReviewerComments(Item item) {
        List<Comment> result = new ArrayList<Comment>();
        for (int i = 0; i < item.getNumberOfComments(); i++) {
            Comment comment = item.getComment(i);
            if (ActionsHelper.isReviewerComment(comment)) {
                result.add(comment);
            }
        }
        return result.toArray(new Comment[result.size()]);
    }

    /**
     * Save the generic review.
     * @param request http servlet request
     * @param reviewType the review type
     * @param reviewForm the review form
     * @return the string result
     * @throws BaseException if any error.
     */
    protected String saveGenericReview(DynamicModel reviewForm, HttpServletRequest request, String reviewType) throws BaseException {
        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        String permName;
        String phaseName;
        String scorecardTypeName;
        boolean isApprovalPhase = false;
        boolean isSpecReviewPhase = false;
        boolean isSubmissionDependentPhase = true;
        // Determine permission name and phase name from the review type
        if ("Screening".equals(reviewType)) {
            permName = Constants.PERFORM_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
            scorecardTypeName = "Screening";
        } else if ("Checkpoint Screening".equals(reviewType)) {
            permName = Constants.PERFORM_CHECKPOINT_SCREENING_PERM_NAME;
            phaseName = Constants.CHECKPOINT_SCREENING_PHASE_NAME;
            scorecardTypeName = "Checkpoint Screening";
        } else if ("Checkpoint Review".equals(reviewType)) {
            permName = Constants.PERFORM_CHECKPOINT_REVIEW_PERM_NAME;
            phaseName = Constants.CHECKPOINT_REVIEW_PHASE_NAME;
            scorecardTypeName = "Checkpoint Review";
        } else if ("Review".equals(reviewType)) {
            permName = Constants.PERFORM_REVIEW_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
            scorecardTypeName = "Review";
        } else if ("Approval".equals(reviewType)) {
            isApprovalPhase = true;
            permName = Constants.PERFORM_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
            scorecardTypeName = "Approval";
        } else if ("Specification Review".equals(reviewType)) {
            isSpecReviewPhase = true;
            permName = Constants.PERFORM_SPECIFICATION_REVIEW_PERM_NAME;
            phaseName = Constants.SPECIFICATION_REVIEW_PHASE_NAME;
            scorecardTypeName = "Specification Review";
        } else if ("Iterative Review".equals(reviewType)) {
            permName = Constants.PERFORM_ITERATIVE_REVIEW_PERM_NAME;
            phaseName = Constants.ITERATIVE_REVIEW_PHASE_NAME;
            scorecardTypeName = "Iterative Review";
        } else {
            isSubmissionDependentPhase = false;
            permName = Constants.PERFORM_POST_MORTEM_REVIEW_PERM_NAME;
            phaseName = Constants.POST_MORTEM_PHASE_NAME;
            scorecardTypeName = "Post-Mortem";
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(request, permName);
        }
        if (verification == null && request.getParameter("pid") != null) {
            verification = checkForCorrectProjectId(request, permName);
        }
        if (verification == null && request.getParameter("sid") != null) {
            verification = checkForCorrectSubmissionId(request, permName);
        }

        // If neither "sid" nor "rid" was specified, return an action forward to the error page
        if (verification == null) {
            return ActionsHelper.produceErrorReport(this, request,
                    permName, "Error.SubmissionAndReviewIdNotSpecified", null);
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, phaseName);
        // Check that the phase in question is really active (open)
        if (phase == null) {
            if (!scorecardTypeName.equals("Iterative Review") && AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                // Managers can edit reviews in any phase, except iterative review
                phase = ActionsHelper.getPhase(phases, false, phaseName);
            } else {
                return ActionsHelper.produceErrorReport(
                        this, request, permName, "Error.IncorrectPhase", null);
            }
        }

        // Get "My" resource for the appropriate phase
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        // If no resource found for particular phase, try to find resource without phase assigned
        if (myResource == null) {
            myResource = ActionsHelper.getMyResourceForPhase(request, null);
        }
        if (myResource == null) {
            myResource = (Resource) request.getAttribute("global_resource");
        }

        // Retrieve the review to edit (if any)
        Review review = verification.getReview();
        Scorecard scorecardTemplate = null;

        if (review == null) {
            // Verify that user has the permission to perform the review
            if (!AuthorizationHelper.hasUserPermission(request, permName)) {
                return ActionsHelper.produceErrorReport(
                        this, request, permName, "Error.NoPermission", Boolean.TRUE);
            }
            // At this point, redirect-after-login attribute should be removed (if it exists)
            AuthorizationHelper.removeLoginRedirect(request);

            /*
             * Verify that the user is not trying to create review that already exists
             */

            // Retrieve a scorecard template for the appropriate phase
            scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(phase, false);

            // Prepare filters
            Filter filterResource = new EqualToFilter("reviewer", myResource.getId());
            Filter filterScorecard = new EqualToFilter("scorecardType", scorecardTemplate.getScorecardType().getId());
            Filter filterPhase = new EqualToFilter("projectPhase", phase.getId());

            Filter filter;
            if (isSubmissionDependentPhase) {
                Filter filterSubmission = new EqualToFilter("submission", verification.getSubmission().getId());
                filter = new AndFilter(Arrays.asList(filterResource, filterSubmission, filterScorecard, filterPhase));
            } else {
                // Prepare final combined filter
                filter = new AndFilter(Arrays.asList(filterResource, filterScorecard, filterPhase));
            }

            // Obtain an instance of Review Manager
            ReviewManager revMgr = ActionsHelper.createReviewManager();
            // Retrieve an array of reviews
            Review[] reviews = revMgr.searchReviews(filter, false);

            // Non-empty array of reviews indicates that user is trying to create review that already exists
            if (reviews.length != 0) {
                review = reviews[0];
                verification.setReview(review);
            }
        }

        if (review != null) {
            // Verify that the user has permission to edit review
            if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_ANY_SCORECARD_PERM_NAME)) {
                if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_MY_REVIEW_PERM_NAME)) {
                    return ActionsHelper.produceErrorReport(this,
                        request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
                } else if(verification.getReview().getAuthor() != myResource.getId()) {
                    return ActionsHelper.produceErrorReport(this,
                            request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
                }
            }
            // At this point, redirect-after-login attribute should be removed (if it exists)
            AuthorizationHelper.removeLoginRedirect(request);

            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
            // Retrieve a scorecard template for the review
            scorecardTemplate = scrMgr.getScorecard(review.getScorecard());
        }

        // Verify that the scorecard template for this review is of correct type
        if (scorecardTemplate == null || !scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(this, request,
                    permName, "Error.ReviewTypeIncorrect", null);
        }

        boolean managerEdit = false;
        // Check if review has been committed
        if (review != null && review.isCommitted()) {
            // If user has a Manager role, put special flag to the request,
            // indicating that we need "Manager Edit"
            if(!scorecardTypeName.equals("Iterative Review") && AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                request.setAttribute("managerEdit", Boolean.TRUE);
                managerEdit = true;
            } else {
                return ActionsHelper.produceErrorReport(this, request,
                            Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
            }
        }

        // This variable determines if 'Save and Mark Complete' button has been clicked
        boolean commitRequested = "submit".equalsIgnoreCase(request.getParameter("save"));
        // This variable determines if Preview button has been clicked
        boolean previewRequested = "preview".equalsIgnoreCase(request.getParameter("save"));

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        Integer[] commentCounts = (Integer[]) reviewForm.get("comment_count");
        Map<String, String> replies = (Map<String, String>) reviewForm.get("comment");
        Map<String, String> commentTypeIds = (Map<String, String>) reviewForm.get("comment_type");
        FormFile[] files = (FormFile[]) reviewForm.get("file");

        // Uploaded files will be held here
        UploadedFile[] uploadedFiles = null;

        // Files won't be uploaded, if a mere Preview operation was requested
        if (!previewRequested) {
            StrutsRequestParser parser = new StrutsRequestParser();

            // Collect uploaded files and add them to adapter
            for (FormFile file : files) {
                if (file != null && file.getFileName() != null && file.getFileName().trim().length() != 0) {
                    parser.AddFile(file);
                }
            }

            // Obtain an instance of File Upload Manager
            FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

            // Upload files to the file server
            FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
            // Get an information about uploaded files (an uploaded files' ID in particular)
            uploadedFiles = uploadResult.getUploadedFiles("file");
        }

        ReviewManager revMgr = ActionsHelper.createReviewManager();
        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();

        int itemIdx = 0;
        int fileIdx = 0;
        int uploadedFileIdx = 0;

        // If the review hasn't been created yet
        if (review == null) {
            // Create a convenient review editor
            ReviewEditor reviewEditor =
                new ReviewEditor(Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

            // Iterate over the scorecard template's questions,
            // so items will be created for every question
            for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
                Group group = scorecardTemplate.getGroup(groupIdx);
                for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                    Section section = group.getSection(sectionIdx);
                    for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                        Question question = section.getQuestion(questionIdx);

                        // Create review item
                        Item item = new Item();

                        // Populate the review item comments
                        int newCommentCount = populateItemComments(item, itemIdx, replies, commentTypeIds,
                                commentCounts[itemIdx], myResource, managerEdit);

                        if (newCommentCount != commentCounts[itemIdx]) {
                            commentCounts[itemIdx] = newCommentCount;
                        }

                        // Set required fields of the item
                        item.setAnswer(answers[itemIdx]);
                        item.setQuestion(question.getId());

                        // Handle uploads
                        if (!previewRequested && question.isUploadDocument()) {
                            if (fileIdx < files.length && files[fileIdx] != null &&
                                    files[fileIdx].getFileName() != null &&
                                    files[fileIdx].getFileName().trim().length() != 0) {
                                Upload upload = new Upload();

                                upload.setOwner(myResource.getId());
                                upload.setProject(project.getId());
                                upload.setProjectPhase(phase.getId());
                                upload.setParameter(uploadedFiles[uploadedFileIdx++].getFileId());
                                upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
                                upload.setUploadType(LookupHelper.getUploadType("Review Document"));

                                upMgr.createUpload(upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

                                item.setDocument(upload.getId());
                            }
                            ++fileIdx;
                        }

                        // Add item to the review
                        reviewEditor.addItem(item);

                        ++itemIdx;
                    }
                }
            }

            // Finally, set required fields of the review
            reviewEditor.setAuthor(myResource.getId());
            reviewEditor.setProjectPhase(phase.getId());
            // Skip setting submission ID for Post-Mortem phase
            if (isSubmissionDependentPhase) {
                reviewEditor.setSubmission(verification.getSubmission().getId());
            }
            reviewEditor.setScorecard(scorecardTemplate.getId());

            review = reviewEditor.getReview();
        } else {
            for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
                Group group = scorecardTemplate.getGroup(groupIdx);
                for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                    Section section = group.getSection(sectionIdx);
                    for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                        // Get an item
                        Item item = review.getItem(itemIdx);

                        // Populate the review item comments
                        int newCommentCount = populateItemComments(item, itemIdx, replies, commentTypeIds,
                                commentCounts[itemIdx], myResource, managerEdit);

                        if (newCommentCount != commentCounts[itemIdx]) {
                            commentCounts[itemIdx] = newCommentCount;
                        }

                        // Update the answer
                        item.setAnswer(answers[itemIdx]);

                        // Handle uploads
                        if (!previewRequested && !managerEdit && section.getQuestion(questionIdx).isUploadDocument()) {
                            if (fileIdx < files.length && files[fileIdx] != null &&
                                    files[fileIdx].getFileName() != null &&
                                    files[fileIdx].getFileName().trim().length() != 0) {
                                Upload oldUpload = null;
                                // If this item has already had uploaded file,
                                // it is going to be updated
                                if (item.getDocument() != null) {
                                    oldUpload = upMgr.getUpload(item.getDocument());
                                }

                                Upload upload = new Upload();

                                // Set fields of the new upload
                                upload.setOwner(myResource.getId());
                                upload.setParameter(uploadedFiles[uploadedFileIdx++].getFileId());
                                upload.setProject(project.getId());
                                upload.setProjectPhase(phase.getId());
                                upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
                                upload.setUploadType(LookupHelper.getUploadType("Review Document"));

                                // Update and store old upload (if there was any)
                                if (oldUpload != null) {
                                    oldUpload.setUploadStatus(LookupHelper.getUploadStatus("Deleted"));
                                    upMgr.updateUpload(oldUpload,
                                            Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                                }

                                // Save information about current upload
                                upMgr.createUpload(upload,
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

                                item.setDocument(upload.getId());
                            }
                            ++fileIdx;
                        }
                    }
                }
            }
        }

        // For Manager Edits this variable indicates whether recomputation of
        // final aggregated score for the submitter may be required
        boolean possibleFinalScoreUpdate = false;


        if (isApprovalPhase) {
            Comment reviewLevelComment1 = null;
            Comment reviewLevelComment2 = null;
            boolean approveFixes;
            boolean acceptButRequireOtherFixes;

            Resource resource = ActionsHelper.getMyResourceForRole(request, Constants.APPROVER_ROLE_NAME);

            Boolean approveFixesObj = (Boolean) reviewForm.get("approve_fixes");
            if (approveFixesObj == null) {
                ActionsHelper.addErrorToRequest(request, "approve_status", "Error.saveApproval.Absent");
            }

            Boolean acceptButRequireOtherFixesObj = (Boolean) reviewForm.get("accept_but_require_fixes");
            approveFixes = (approveFixesObj != null && approveFixesObj);
            acceptButRequireOtherFixes = (acceptButRequireOtherFixesObj != null && acceptButRequireOtherFixesObj);

            for (int i = 0; i < review.getNumberOfComments(); ++i) {
                Comment comment = review.getComment(i);
                if (comment.getCommentType().getName().equalsIgnoreCase("Approval Review Comment")) {
                    reviewLevelComment1 = comment;
                } else if (comment.getCommentType().getName().equalsIgnoreCase("Approval Review Comment - Other Fixes")) {
                    reviewLevelComment2 = comment;
                }
            }

            if (reviewLevelComment1 == null) {
                reviewLevelComment1 = new Comment();
            }

            if (approveFixesObj != null) {
                reviewLevelComment1.setCommentType(
                    LookupHelper.getCommentType("Approval Review Comment"));
                reviewLevelComment1.setAuthor(resource.getId());
                reviewLevelComment1.setExtraInfo(approveFixes ? "Approved" : "Rejected");
                reviewLevelComment1.setComment("");
                review.addComment(reviewLevelComment1);
            }

            if (reviewLevelComment2 == null) {
                reviewLevelComment2 = new Comment();
            }

            reviewLevelComment2.setCommentType(
                LookupHelper.getCommentType("Approval Review Comment - Other Fixes"));
            reviewLevelComment2.setAuthor(resource.getId());
            reviewLevelComment2.setExtraInfo(acceptButRequireOtherFixes ? "Required" : "");
            reviewLevelComment2.setComment("");
            review.addComment(reviewLevelComment2);
        }

        if (isSpecReviewPhase) {
            Comment reviewLevelComment1 = null;

            Boolean approveSpecObj = (Boolean) reviewForm.get("approve_specification");
            boolean approveSpecification;
            approveSpecification = (approveSpecObj != null && approveSpecObj);

            for (int i = 0; i < review.getNumberOfComments(); ++i) {
                Comment comment = review.getComment(i);
                if (comment.getCommentType().getName().equalsIgnoreCase("Specification Review Comment")) {
                    reviewLevelComment1 = comment;
                }
            }

            if (reviewLevelComment1 == null) {
                reviewLevelComment1 = new Comment();
            }

            reviewLevelComment1.setCommentType(
                LookupHelper.getCommentType("Specification Review Comment"));
            reviewLevelComment1.setAuthor(myResource.getId());
            reviewLevelComment1.setExtraInfo(approveSpecification ? "Approved" : "Rejected");
            reviewLevelComment1.setComment("");
            review.addComment(reviewLevelComment1);
        }

        boolean validationSucceeded = (!commitRequested && !managerEdit) || validateGenericScorecard(request, scorecardTemplate, review, managerEdit);

        if (!validationSucceeded) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Put the review object into the bean (it may not always be there by default)
            verification.setReview(review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);
            // Retrieve some look-up data and store it into the request
            retrieveAndStoreReviewLookUpData(request);
            // Need to store uploaded file IDs, so that the user will be able to download them
            if (!managerEdit) {
                request.setAttribute("uploadedFileIds", collectUploadedFileIds(scorecardTemplate, review));
            }

            return INPUT;
        }

        // At this point going forward we assume the validation succeeded
        // If the user has requested to complete the review
        if (commitRequested || managerEdit) {
            // Obtain an instance of CalculationManager
            CalculationManager scoreCalculator = new CalculationManager();
            // Compute scorecard's score
            double newScore = scoreCalculator.getScore(scorecardTemplate, review);
            // If score has been updated during Manager Edit, additional actions may need to be taken
            if ((reviewType.equals("Review") || reviewType.equals("Checkpoint Review")) &&
                    managerEdit && (review.getScore() == null || review.getScore() != newScore)) {
                possibleFinalScoreUpdate = true;
            }

            // Update scorecard's score
            review.setScore(newScore);
            if (review.getInitialScore() == null) {
                review.setInitialScore(review.getScore());
            }
            // Set the completed status of the review
            if (commitRequested) {

                // Make sure that each item has at least one comment before committing the review.
                // If not, create an empty one.
                for (int i = 0; i < review.getNumberOfItems(); ++i) {
                    Item item = review.getItem(i);
                    if (item.getNumberOfComments() == 0) {
                        Comment comment = new Comment();
                        comment.setAuthor(myResource.getId());
                        comment.setComment("");
                        comment.setCommentType(LookupHelper.getCommentType("Comment"));
                        item.addComment(comment);
                    }
                }

                review.setCommitted(true);
            }
        } else if (previewRequested) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Put the review object into the bean (it may not always be there by default)
            verification.setReview(review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

            // Notify View page that this is actually a preview operation
            request.setAttribute("isPreview", Boolean.TRUE);
            // Forward to preview page
            return Constants.PREVIEW_FORWARD_NAME;
        }

        // Determine which action should be performed - creation or updating
        if (verification.getReview() == null) {
            revMgr.createReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        } else {
            revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        // This operation will possibly update final aggregated score for the submitter
        if (possibleFinalScoreUpdate) {
            updateFinalAggregatedScore(request, project, phase, verification.getSubmission());
        }

        if (commitRequested) {
            // Put the review object into the bean (it may not always be there by default)
            verification.setReview(review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

            // Place information about the final score into the request
            request.setAttribute("reviewScore", review.getScore());
            // Place review ID into the request
            request.setAttribute("rid", review.getId());

            // Forward to the page that says that scorecard has been committed
            return Constants.REVIEW_COMMITTD_FORWARD_NAME;
        }

        // Forward to project details page
        this.pid = verification.getProject().getId();
        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * This static method updates final score for the particular submitter. The score is updated
     * only in the case, there was originally such score for the submitter (i.e. the project in the
     * Aggregation or past-Aggregation phase). This operation is needed in the situation when
     * Manager edits review scorecard and this operation results in scorecard's score change.
     * Current version of the method does not take into account the possibility of changing places
     * for submitters.
     *
     * @param request
     *            the http request. Used internally by some helper functions.
     * @param project
     *            a project the submission was originally made for.
     * @param reviewPhase
     *            phase of type &quot;Review&quot; or &quot;Checkpoint Review&quot; used internally to retrieve
     *            reviewers' resources.
     * @param submission
     *            a submission in question, i.e. the one that needs its final score updated.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws BaseException
     *             if any unexpected error occurs during final score update.
     */
    private static void updateFinalAggregatedScore(HttpServletRequest request, Project project,
        Phase reviewPhase, Submission submission) throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(project, "project");
        ActionsHelper.validateParameterNotNull(reviewPhase, "reviewPhase");
        ActionsHelper.validateParameterNotNull(submission, "submission");

        // Get final aggregated score for this submitter, if any
        Double finalScore = submission.getFinalScore();
        // If there is no final (post Appeals Response) score for the submission yet, there is nothing to do anymore
        // log.log(Level.INFO, "Final Score: " + finalScore);
        if (finalScore == null) {
            return;
        }

        OnlineReviewServices orServices = new OnlineReviewServices();
        orServices.updateSubmissionsResults(reviewPhase,
            String.valueOf(AuthorizationHelper.getLoggedInUserId(request)), false, true);
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        ActionsHelper.resetProjectResultWithChangedScores(project, operator);
    }

    /**
     * Populate the item comments.
     *
     * @return the comment count
     * @param myResource my resource
     * @param item the item
     * @param itemIdx the item index
     * @param replies replies map
     * @param commentTypeIds the comment type ids
     * @param commentCount the comment count
     * @param managerEdit flag if manager edit
     * @throws IllegalArgumentException
     *             if any of the parameters <code>item</code>, <code>replies</code>,
     *             <code>commentTypeIds</code> or
     *             <code>myResource</code> is <code>null</code>, or if parameters
     *             <code>itemIdx</code> or <code>commentCount</code> are negative.
     * @throws BaseException if any other error occur
     */
    private static int populateItemComments(Item item, int itemIdx, Map<String, String> replies,
                                            Map<String, String> commentTypeIds, int commentCount, Resource myResource,
                                            boolean managerEdit) throws BaseException {

        // Validate parameters
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(itemIdx, "itemIdx", 0, Integer.MAX_VALUE);
        ActionsHelper.validateParameterNotNull(replies, "replies");
        ActionsHelper.validateParameterNotNull(commentTypeIds, "commentTypeIds");
        ActionsHelper.validateParameterInRange(commentCount, "commentCount", 0, Integer.MAX_VALUE);
        ActionsHelper.validateParameterNotNull(myResource, "myResource");

        int newCommentCount = 1;

        int commentIdx = 0;
        Comment currentComment = null;

        for (int i = 0; i < commentCount; ++i) {
            if (currentComment == null) {
                for (;commentIdx < item.getNumberOfComments();) {
                    // Get a comment for the current iteration
                    Comment comment = item.getComment(commentIdx++);

                    // Locate next manager's comment for Manager edits or
                    // next reviewer's comment for non-Manager edits
                    if ((managerEdit && ActionsHelper.isManagerComment(comment)) ||
                            (!managerEdit && ActionsHelper.isReviewerComment(comment))) {
                        currentComment = comment;
                        break;
                    }
                }
            }

            String commentKey = itemIdx + "." + (i + 1);
            CommentType commentType;

            if (managerEdit) {
                commentType = LookupHelper.getCommentType("Manager Comment");
            } else {
                // Check that a mapping for the commentKey actually exists.
                // It may not exist when the browser is confused when the user clicks 'Back' button after validation fails (probably an error in the JS).
                if (!commentTypeIds.containsKey(commentKey)) {
                    replies.remove(commentKey);
                    continue;
                }

                commentType = LookupHelper.getCommentType(Long.parseLong(commentTypeIds.get(commentKey)));
            }
            // Check that correct comment type ID has been specified
            // (user may intentionally submit malformed form data)
            if (commentType == null) {
                replies.remove(commentKey);
                commentTypeIds.remove(commentKey);
                continue;
            }

            // Get user's reply, i.e. comment's text
            String reply = replies.get(commentKey);
            // Do not let user's reply to be null
            if (reply == null) {
                reply = "";
            }

            // Skip empty comments of type "Comment" or any empty comment for Manager edits
            if ((managerEdit || commentType.getName().equalsIgnoreCase("Comment")) && reply.trim().length() == 0) {
                replies.remove(commentKey);
                commentTypeIds.remove(commentKey);
                continue;
            }

            // Determine if new comment must be added
            boolean newComment = false;
            // If new comment needs to be added, create new Comment object
            if (currentComment == null) {
                commentIdx = Integer.MAX_VALUE;
                currentComment = new Comment();
                newComment = true;
            }

            String updatedCommentKey = itemIdx + "." + newCommentCount;

            // Update form's fields (so they will be up to date in case scorecard fails validation)
            replies.put(updatedCommentKey, reply);
            if (!managerEdit) {
                commentTypeIds.put(updatedCommentKey, Long.toString(commentType.getId()));
            }
            // Increase the counter of the processed comments
            ++newCommentCount;

            // Do not update unchanged comments (skip them)
            if (!newComment && reply.equals(currentComment.getComment()) &&
                    currentComment.getCommentType().getId() == commentType.getId()) {
                // Indicate that there are no current comment anymore,
                // so it will be located or created during next iteration
                currentComment = null;
                continue;
            }

            // Update the author of the comment
            currentComment.setAuthor(myResource.getId());
            // Set (possibly new) comment's text
            currentComment.setComment(reply);
            // Set (possibly new) comment's type
            currentComment.setCommentType(commentType);

            // If new comment needs to be added to review's item, add it
            if (newComment) {
                item.addComment(currentComment);
            }
            // Indicate that there are no current comment anymore,
            // so it will be located or created during next iteration
            currentComment = null;
        }

        // If last current comment was not used, the most likely it is needed to be deleted
        if (currentComment != null) {
            --commentIdx;
        }

        // At this point there may exist excess comments, which need to be removed.
        // So, remove them, starting from the last
        for (int i = item.getNumberOfComments() - 1; i >= commentIdx; --i) {
            // Get a comment for the current iteration
            Comment comment = item.getComment(i);

            if ((managerEdit && ActionsHelper.isManagerComment(comment)) ||
                    (!managerEdit && ActionsHelper.isReviewerComment(comment))) {
                item.removeComment(comment);
            }
        }

        final int properCommentCount = (managerEdit) ? 1 : DEFAULT_COMMENTS_NUMBER;

        for (;newCommentCount <= properCommentCount; ++newCommentCount) {
            String emptyCommentKey = itemIdx + "." + newCommentCount;
            replies.put(emptyCommentKey, "");
            commentTypeIds.put(emptyCommentKey, Long.toString(LookupHelper.getCommentType("Comment").getId()));
        }

        return newCommentCount - 1;
    }

    /**
     * Collect the uploaded file ids.
     *
     * @return all uploaded file ids.
     * @param scorecardTemplate the scorecard template
     * @param review the review
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    private static Long[] collectUploadedFileIds(Scorecard scorecardTemplate, Review review) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(scorecardTemplate, "scorecardTemplate");
        ActionsHelper.validateParameterNotNull(review, "review");

        int uploadsCount = ActionsHelper.getScorecardUploadsCount(scorecardTemplate);
        if (uploadsCount == 0) {
            return null; // No upload IDs
        }

        Long[] uploadedFileIds = new Long[uploadsCount];
        int itemIdx = 0;
        int fileIdx = 0;

        for (int iGroup = 0; iGroup < scorecardTemplate.getNumberOfGroups(); ++iGroup) {
            Group group = scorecardTemplate.getGroup(iGroup);
            for (int iSection = 0; iSection < group.getNumberOfSections(); ++iSection) {
                Section section = group.getSection(iSection);
                for (int iQuestion = 0; iQuestion < section.getNumberOfQuestions(); ++iQuestion, ++itemIdx) {
                    Question question = section.getQuestion(iQuestion);
                    if (question.isUploadDocument()) {
                        uploadedFileIds[fileIdx++] = review.getItem(itemIdx).getDocument();
                    }
                }
            }
        }

        return uploadedFileIds;
    }

    /**
     * Validate the generic scorecard.
     *
     * @return true if the scorecard is valid
     * @param request the http servlet request
     * @param scorecardTemplate the scorecard template
     * @param review the review
     * @param managerEdit flag if mangaer edit
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    private static boolean validateGenericScorecard(HttpServletRequest request,
                                                    Scorecard scorecardTemplate, Review review, boolean managerEdit) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(scorecardTemplate, "scorecardTemplate");
        ActionsHelper.validateParameterNotNull(review, "review");

        int itemIdx = 0;
        int fileIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                    Question question = section.getQuestion(questionIdx);
                    Item item = review.getItem(itemIdx);

                    validateScorecardItemAnswer(request, question, item, itemIdx);

                    if (managerEdit) {
                        validateManagerComments(request, item, itemIdx);
                    } else {
                        validateScorecardComments(request, item, itemIdx);
                        if (question.isUploadDocument()) {
                            validateScorecardItemUpload(request, question, item, fileIdx++);
                        }
                    }
                }
            }
        }

        return !ActionsHelper.isErrorsPresent(request);
    }

    /**
     * Validate the scorecard item answer.
     *
     * @return true if valid
     * @param request the http servlet request
     * @param question the question
     * @param item the item
     * @param answerNum the answer number
     * @throws IllegalArgumentException
     *             if <code>request</code>, <code>question</code>, or <code>item</code> parameters are
     *             <code>null</code>, or if <code>answerNum</code> parameter is negative (less
     *             than zero).
     */
    private static boolean validateScorecardItemAnswer(
            HttpServletRequest request, Question question, Item item, int answerNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(question, "question");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(answerNum, "answerNum", 0, Integer.MAX_VALUE);

        String errorKey = "answer[" + answerNum + "]";

        // Validate that answer is not null
        if (item.getAnswer() == null || !(item.getAnswer() instanceof String)) {
            ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Absent");
            return false;
        }

        String answer = (String) item.getAnswer();

        // Validate that answer is not empty
        if (answer.trim().length() == 0) {
            ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Absent");
            return false;
        }

        // Success indicator
        boolean success = true;
        // Get a type of the question for the current answer
        String questionType = question.getQuestionType().getName();

        if (correctAnswers.containsKey(questionType)) {
            if (!(correctAnswers.get(questionType).contains(answer))) {
                ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Incorrect");
                success = false;
            }
        } else if (questionType.equalsIgnoreCase("Test Case")) {
            String[] answers = answer.split("/");
            // The number of answers for Testcase type of question must be exactly 2
            if (answers.length < 2) {
                ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.TestCase.LessTwo");
            } else if (answers.length > 2) {
                ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.TestCase.GreaterTwo");
            } else {
                try {
                    // Try to convert strings to integer value (and validate whether they are convertible)
                    int answer1 = Integer.parseInt(answers[0], 10);
                    int answer2 = Integer.parseInt(answers[1], 10);

                    // Validate some more circumstances
                    if (answer1 < 0 || answer2 < 0) {
                        ActionsHelper.addErrorToRequest(
                                request, errorKey, "Error.saveReview.Answer.TestCase.Negative");
                        success = false;
                    } else if (answer1 > answer2) {
                        ActionsHelper.addErrorToRequest(
                                request, errorKey, "Error.saveReview.Answer.TestCase.FirstGreaterSecond");
                        success = false;
                    }
                } catch (NumberFormatException nfe) {
                    // eat the exception and report about validation error
                    ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.TestCase.NotInt");
                    success = false;
                }
            }
        } else if (questionType.equalsIgnoreCase("Yes/No")) {
            // For 'Yes/No' type of question the two possible values for answer are either "0" or "1"
            if (!(answer.equals("0") || answer.equals("1"))) {
                ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Incorrect");
                success = false;
            }
        }

        return success;
    }

    /**
     * Validate the scorecard comments.
     * @param request the http servlet request
     * @param item the item
     * @param itemNum the item number
     * @throws IllegalArgumentException
     *             if <code>request</code> or <code>item</code> parameters are
     *             <code>null</code>, or if <code>itemNum</code> parameter is negative (less
     *             than zero).
     */
    private static void validateScorecardComments(HttpServletRequest request, Item item, int itemNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(itemNum, "itemNum", 0, Integer.MAX_VALUE);

        /* Request from Jessica Williams [Sep 16 2011]:
           No need to verify presence of review item comments (for Studio, but we do it for everything).
        */
        //boolean noCommentsEntered = true;

        //for (int i = 0; i < item.getNumberOfComments(); ++i) {
        //    if (ActionsHelper.isReviewerComment(item.getComment(i))) {
        //        noCommentsEntered = false;
        //        break;
        //    }
        //}

        //if (noCommentsEntered) {
        //    ActionsHelper.addErrorToRequest(request,
        //            "comment(" + itemNum + ".1)", "Error.saveReview.Comment.AtLeastOne");
        //    return;
        //}

        for (int i = 0; i < item.getNumberOfComments(); ++i) {
            Comment comment = item.getComment(i);

            if (ActionsHelper.isReviewerComment(comment)) {
                validateScorecardComment(request, comment, "comment(" + itemNum + "." + (i + 1) + ")");
            }
        }
    }

    /**
     * Validate the manager comments.
     * @param request the http servlet request
     * @param item the item
     * @param itemNum the item number
     * @throws IllegalArgumentException
     *             if <code>request</code> or <code>item</code> parameters are
     *             <code>null</code>, or if <code>itemNum</code> parameter is negative (less
     *             than zero).
     */
    private static void validateManagerComments(HttpServletRequest request, Item item, int itemNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(itemNum, "itemNum", 0, Integer.MAX_VALUE);

        int managerCommentIdx = 0;
        for (int i = 0; i < item.getNumberOfComments(); ++i) {
            Comment comment = item.getComment(i);
            if (ActionsHelper.isManagerComment(comment)) {
                managerCommentIdx++;
                if (comment.getComment() != null && comment.getComment().length() > MAX_COMMENT_LENGTH) {
                    ActionsHelper.addErrorToRequest(request, "comment(" + itemNum + "." + managerCommentIdx + ")",
                            "Error.saveReview.Comment.MaxExceeded");
                }
            }
        }
    }

    /**
     * This static method validates single comment at a time. The comment must have its text to be
     * non-null and non-empty string to be regarded as passing validation.
     *
     * @param request
     *            an <code>HttpServletRequest</code> object where validation error messages will
     *            be placed to in case there are any.
     * @param comment
     *            a comment to validate.
     * @param errorMessageProperty
     *            a string parameter that determines which key an error message will be stored
     *            under.
     * @throws IllegalArgumentException
     *             if parameters <code>request</code>, <code>comment</code>, or
     *             <code>errorMessageProperty</code> are <code>null</code>, or if parameter
     *             <code>errorMessageProperty</code> is empty string.
     */
    private static void validateScorecardComment(
            HttpServletRequest request, Comment comment, String errorMessageProperty) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(comment, "comment");
        ActionsHelper.validateParameterStringNotEmpty(errorMessageProperty, "errorMessageProperty");

        String commentText = comment.getComment();
        if (commentText == null || commentText.trim().length() == 0) {
            ActionsHelper.addErrorToRequest(request, errorMessageProperty, "Error.saveReview.Comment.Absent");
        } else if (commentText.length() > MAX_COMMENT_LENGTH) {
            ActionsHelper.addErrorToRequest(request, errorMessageProperty, "Error.saveReview.Comment.MaxExceeded");
        }
    }

    /**
     * Validate the scorecard upload item.
     *
     * @return true if valid
     * @param request the http servlet request
     * @param question the question
     * @param item the item
     * @param fileNum the file number
     * @throws IllegalArgumentException
     *             if <code>request</code>, <code>question</code>, or <code>item</code>
     *             parameters are <code>null</code>, or if <code>fileNum</code> parameter is
     *             negative (less than zero).
     */
    private static boolean validateScorecardItemUpload(
            HttpServletRequest request, Question question, Item item, int fileNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(question, "question");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(fileNum, "fileNum", 0, Integer.MAX_VALUE);

        if (!question.isUploadDocument() || !question.isUploadRequired()) {
            return true;
        }

        if (item.getDocument() == null) {
            ActionsHelper.addErrorToRequest(request, "file[" + fileNum + "]", "Error.saveReview.File.Absent");
            return false;
        }

        return true;
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

    /**
     * Getter of rid.
     * @return the rid
     */
    public long getRid() {
        return rid;
    }

    /**
     * Setter of rid.
     * @param rid the rid to set
     */
    public void setRid(long rid) {
        this.rid = rid;
    }
}

