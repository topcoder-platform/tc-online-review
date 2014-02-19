/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.Comparators;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LookupHelper;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This is the base class for project review view or export actions classes.
 * It provides the basic functions which will be used by all project review view/export actions.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseViewOrExportGenericReviewAction extends BaseProjectReviewAction {

    /**
     * Represents the mapping between comment type and the cell color when exporting the comment.
     */
    private static final Map<String, Integer> commentExportColor = new HashMap<String, Integer>();

    /**
     * Represents the review comment types.
     */
    private static final Set<String> reviewerCommentTypes = new HashSet<String>();

    /**
     * Represents the comment types which are exported for final review.
     */
    private static final Set<String> finalReviewCommentTypes = new HashSet<String>();

    /**
     * Represents the comment types which are exported for aggregation.
     */
    private static final Set<String> aggregationCommentTypes = new HashSet<String>();

    // Initialize static fields
    static {
        commentExportColor.put("Group", 0xA6A6A6);
        commentExportColor.put("Section", 0xD9D9D9);
        commentExportColor.put("Question", 0xFFFFFF);
        commentExportColor.put("Attachment", 0xDCE6F1);
        commentExportColor.put("Comment", 0xDCE6F1);
        commentExportColor.put("Recommended", 0xDCE6F1);
        commentExportColor.put("Required", 0xDCE6F1);
        commentExportColor.put("Appeal", 0xF2DCDB);
        commentExportColor.put("Appeal Response", 0xB8CCE4);
        commentExportColor.put("Aggregation Comment", 0xDCE6F1);
        commentExportColor.put("Aggregation Review Comment", 0xDCE6F1);
        commentExportColor.put("Submitter Comment", 0xDCE6F1);
        commentExportColor.put("Final Fix Comment", 0xDCE6F1);
        commentExportColor.put("Final Review Comment", 0xDCE6F1);
        commentExportColor.put("Manager Comment", 0xFFC000);
        commentExportColor.put("Approval Review Comment", 0xDCE6F1);
        commentExportColor.put("Approval Review Comment - Other Fixes", 0xDCE6F1);
        commentExportColor.put("Specification Review Comment", 0xDCE6F1);

        reviewerCommentTypes.add("Required");
        reviewerCommentTypes.add("Recommended");
        reviewerCommentTypes.add("Comment");

        finalReviewCommentTypes.addAll(reviewerCommentTypes);
        finalReviewCommentTypes.add("Appeal");
        finalReviewCommentTypes.add("Appeal Response");
        finalReviewCommentTypes.add("Aggregation Comment");
        finalReviewCommentTypes.add("Final Review Comment");
        finalReviewCommentTypes.add("Manager Comment");
        finalReviewCommentTypes.add("Submitter Comment");

        aggregationCommentTypes.addAll(reviewerCommentTypes);
        aggregationCommentTypes.add("Appeal");
        aggregationCommentTypes.add("Appeal Response");
        aggregationCommentTypes.add("Aggregation Comment");
        aggregationCommentTypes.add("Manager Comment");
    }

    /**
     * View or export aggregation.
     * @param response the http servlet response
     * @param export if it is export
     * @param request the http servlet request
     * @return the string result
     * @throws BaseException if any error
     */
    protected String viewOrExportAggregation(HttpServletRequest request, HttpServletResponse response, boolean export) throws BaseException {
        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request,
                this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(request, Constants.VIEW_AGGREGATION_PERM_NAME);

        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            // Need to support view aggregation just by specifying the project id
            verification = ActionsHelper.checkForCorrectProjectId(
                    this, request, Constants.VIEW_AGGREGATION_PERM_NAME, false);
            if (!verification.isSuccessful()) {
                return verification.getResult();
            }

            // Create filters to select Aggregators for the project
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(verification.getProject().getId());
            Filter filterRole = ResourceFilterBuilder.createResourceRoleIdFilter(
                    LookupHelper.getResourceRole(Constants.AGGREGATOR_ROLE_NAME).getId());
            // Combine the upper two filter
            Filter filterAggregators = new AndFilter(filterProject, filterRole);
            // Fetch all Aggregators for the project
            Resource[] aggregators = ActionsHelper.createResourceManager().searchResources(filterAggregators);

            // If the project does not have any Aggregators,
            // there cannot be any Aggregation worksheets. Signal about the error to the user
            if (aggregators.length == 0) {
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.VIEW_AGGREGATION_PERM_NAME, "Error.NoAggregations", null);
            }

            List<Long> resourceIds = new ArrayList<Long>();

            for (Resource aggregator : aggregators) {
                resourceIds.add(aggregator.getId());
            }

            Filter filterReviewers = new InFilter("reviewer", resourceIds);
            Filter filterCommitted = new EqualToFilter("committed", 1);
            // Build final combined filter
            Filter filter = new AndFilter(filterReviewers, filterCommitted);
            // Obtain an instance of Review Manager
            ReviewManager reviewMgr = ActionsHelper.createReviewManager();
            // Fetch all reviews (Aggregations only) for the project
            Review[] reviews = reviewMgr.searchReviews(filter, true);

            if (reviews.length == 0) {
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.VIEW_AGGREGATION_PERM_NAME, "Error.NoAggregations", null);
            }

            // Sort reviews in array to find the latest Aggregation worksheet for the project
            Arrays.sort(reviews, new Comparators.ReviewComparer());
            // Fetch the most recent review from the array
            Review review = reviews[reviews.length - 1];

            verification.setReview(review);
            // Place the review object as attribute in the request
            request.setAttribute("review", review);

            // Obtain an instance of Deliverable Manager
            UploadManager upMgr = ActionsHelper.createUploadManager();
            // Get Submission by its id
            Submission submission = upMgr.getSubmission(review.getSubmission());

            // Store Submission object in the result bean
            verification.setSubmission(submission);
            // Place the id of the submission as attribute in the request
            request.setAttribute("sid", submission.getId());
        }

        // Verify that user has the permission to view aggregation
        if (!AuthorizationHelper.hasUserPermission(request, Constants.VIEW_AGGREGATION_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review (aggregation) to view
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Make sure that the user is trying to view Aggregation Review, not Aggregation
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.ReviewNotCommitted", null);
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

        if (export) {
            exportGenericReview(request, response, "Aggregation");
        } else {
            request.setAttribute("tableTitle", getText("viewAggregation.AggregationWorksheet"));
            request.setAttribute("canExport", true);
        }
        return null;
    }

    /**
     * View or export final review result.
     * @param response the http servlet response
     * @param request the http servlet request
     * @return the string result
     * @throws BaseException if any error
     */
    protected String viewOrExportFinalReview(HttpServletRequest request, HttpServletResponse response, boolean export) throws BaseException {
        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request,
                this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(request, Constants.VIEW_FINAL_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that user has the permission to view final review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.VIEW_FINAL_REVIEW_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_FINAL_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to view
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review") &&
            !scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Approval")) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Make sure that the user is not trying to view unfinished review
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_FINAL_REVIEW_PERM_NAME, "Error.ReviewNotCommitted", null);
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");
        request.setAttribute("approvalBased", scorecardTemplate.getScorecardType().getName().equals("Approval"));

        int[] lastCommentIdxs = new int[review.getNumberOfItems()];

        Arrays.fill(lastCommentIdxs, 0);

        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                String commentType = item.getComment(j).getCommentType().getName();
                if (commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                        commentType.equalsIgnoreCase("Recommended") ||
                        commentType.equalsIgnoreCase("Appeal") || commentType.equalsIgnoreCase("Appeal Response") ||
                        commentType.equalsIgnoreCase("Manager Comment") ||
                        commentType.equalsIgnoreCase("Aggregation Comment") ||
                        commentType.equalsIgnoreCase("Aggregation Review Comment") ||
                        commentType.equalsIgnoreCase("Submitter Comment") ||
                        commentType.equalsIgnoreCase("Final Review Comment")) {
                    ++lastCommentIdxs[i];
                }
            }
        }

        request.setAttribute("lastCommentIdxs", lastCommentIdxs);

        if (export) {
            exportGenericReview(request, response, "Final Review");
        } else {
            request.setAttribute("tableTitle", getText("editFinalReview.Scorecard.title"));
            request.setAttribute("canExport", true);
        }
        return null;
    }

    /**
     * View or export generic review result.
     *
     * @param request the http servlet request
     * @param response the http servlet response
     * @param reviewType the review type
     * @param export if it is export
     * @return the string result
     * @throws BaseException if any error
     */
    protected String viewOrExportGenericReview(HttpServletRequest request,
                                               HttpServletResponse response, String reviewType, boolean export) throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterStringNotEmpty(reviewType, "reviewType");

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        String permName;
        String phaseName;
        String scorecardTypeName;

        // Determine permission name and phase name from the review type
        boolean isSubmissionDependentPhase = true;
        if (reviewType.equals("Screening")) {
            permName = Constants.VIEW_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
            scorecardTypeName = "Screening";
        } else if (reviewType.equals("Checkpoint Screening") || reviewType.equals("CheckpointScreening")) {
            permName = Constants.VIEW_CHECKPOINT_SCREENING_PERM_NAME;
            phaseName = Constants.CHECKPOINT_SCREENING_PHASE_NAME;
            scorecardTypeName = "Checkpoint Screening";
            reviewType = "Checkpoint Screening";
        } else if (reviewType.equals("Checkpoint Review") || reviewType.equals("CheckpointReview")) {
            permName = Constants.VIEW_CHECKPOINT_REVIEW_PERM_NAME;
            phaseName = Constants.CHECKPOINT_REVIEW_PHASE_NAME;
            scorecardTypeName = "Checkpoint Review";
            reviewType = "Checkpoint Review";
        } else if (reviewType.equals("Review")) {
            permName = Constants.VIEW_ALL_REVIEWS_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
            scorecardTypeName = "Review";
        } else if (reviewType.equals("Approval")) {
            permName = Constants.VIEW_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
            scorecardTypeName = "Approval";
        } else if (reviewType.equals("Specification Review") || reviewType.equals("SpecificationReview")) {
            permName = Constants.VIEW_SPECIFICATION_REVIEW_PERM_NAME;
            phaseName = Constants.SPECIFICATION_REVIEW_PHASE_NAME;
            scorecardTypeName = "Specification Review";
            reviewType = "Specification Review";
        } else if (reviewType.equals("Iterative Review") || reviewType.equals("IterativeReview")) {
            permName = Constants.VIEW_ITERATIVE_REVIEW_PERM_NAME;
            phaseName = Constants.ITERATIVE_REVIEW_PHASE_NAME;
            scorecardTypeName = "Iterative Review";
            reviewType = "Iterative Review";
        } else if (reviewType.equals("Post-Mortem") || reviewType.equals("PostMortem")) {
            isSubmissionDependentPhase = false;
            permName = Constants.VIEW_POST_MORTEM_PERM_NAME;
            phaseName = Constants.POST_MORTEM_PHASE_NAME;
            scorecardTypeName = "Post-Mortem";
            reviewType = "Post-Mortem";
        } else {
            throw new IllegalArgumentException("Incorrect review type specified: " + reviewType + ".");
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(request, permName);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        // Get active (opened) phases names
        List<String> activePhases = new ArrayList<String>();
        for (Phase phase : phases) {
            if (phase.getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName())) {
                activePhases.add(phase.getPhaseType().getName());
            }
        }

        // Get a phase with the specified name
        Phase phase = null;
        if (reviewType.equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)) {
            long phaseId = verification.getReview().getProjectPhase();
            for (Phase ph : phases) {
                if (phaseId == ph.getId()) {
                    phase = ph;
                    break;
                }
            }
        } else {
            phase = ActionsHelper.getPhase(phases, false, phaseName);
        }

        // Get "My" resource for the appropriate phase (for reviewers actually)
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        if (myResource == null) {
            myResource = ActionsHelper.getMyResourceForPhase(request, null);
        }
        Resource mySubmitterResource = getMySubmitterResource(request);

        /*
         *  Verify that user has the permission to view the review
         */
        boolean isAllowed = false;
        if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES) ||
                AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME) ||
                AuthorizationHelper.hasUserRole(request, Constants.CLIENT_MANAGER_ROLE_NAME) ||
                AuthorizationHelper.hasUserRole(request, Constants.COPILOT_ROLE_NAME) ||
                AuthorizationHelper.hasUserRole(request, Constants.OBSERVER_ROLE_NAME)) {
            // User is manager or observer
            isAllowed = true;
        } else if (myResource != null && verification.getReview().getAuthor() == myResource.getId()) {
            // User is authorized to view review authored by him
            isAllowed = true;
        } else if (isSubmissionDependentPhase && mySubmitterResource != null &&
                   verification.getSubmission().getUpload().getOwner() == mySubmitterResource.getId()) {
            // User is authorized to view review for his own submission after the phase has closed
            if (phase.getPhaseStatus().getName().equals(Constants.CLOSED_PH_STATUS_NAME)) {
                isAllowed = true;
            }
        } else if (AuthorizationHelper.hasUserPermission(request, permName)) {
            if (reviewType.equals(Constants.REVIEW_PHASE_NAME)) {
                // User is authorized to view all reviews (when not in Review, Appeals or Appeals Response)
                if (!activePhases.contains(Constants.REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    isAllowed = true;
                }
            } else if (reviewType.equals(Constants.SCREENING_PHASE_NAME)) {
                if (!activePhases.contains(Constants.SCREENING_PHASE_NAME) &&
                        !activePhases.contains(Constants.REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    isAllowed = true;
                } else {
                    //if any of those phases are open, a user can see the screening only if he is not a submitter
                    isAllowed = !AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME);
                }
            } else if (reviewType.equals(Constants.CHECKPOINT_SCREENING_PHASE_NAME) ||
                    reviewType.equals(Constants.CHECKPOINT_REVIEW_PHASE_NAME)) {
                if (!activePhases.contains(Constants.CHECKPOINT_SCREENING_PHASE_NAME) &&
                        !activePhases.contains(Constants.CHECKPOINT_REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.SUBMISSION_PHASE_NAME) &&
                        !activePhases.contains(Constants.SCREENING_PHASE_NAME) &&
                        !activePhases.contains(Constants.REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    isAllowed = true;
                } else {
                    //if any of those phases are open, a user can see the checkpoint screening/review only if he is not a submitter
                    isAllowed = !AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME);
                }
            } else if (reviewType.equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)) {
                // For Iterative Reviews, submitters can view reviews only for their own submissions.
                if (mySubmitterResource == null ||
                        verification.getSubmission().getUpload().getOwner() == mySubmitterResource.getId()) {
                    // User is authorized to view all reviews after the phase has closed.
                    if (phase.getPhaseStatus().getName().equals(Constants.CLOSED_PH_STATUS_NAME)) {
                        isAllowed = true;
                    }
                }
            } else {
                isAllowed = true;
            }
        }

        if (!isAllowed) {
            return ActionsHelper.produceErrorReport(
                    this, request, permName, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
        // Retrieve a scorecard template for this review
        Scorecard scorecardTemplate = scrMgr.getScorecard(verification.getReview().getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(this, request,
                    permName, "Error.ReviewTypeIncorrect", null);
        }
        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return ActionsHelper.produceErrorReport(this, request,
                    permName, "Error.ReviewNotCommitted", null);
        } else {
            // If user has a Manager role, put special flag to the request,
            // indicating that we can edit the review
            // But for iterative review, no one can edit it once it is committed
            if (!reviewType.equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)
                    && AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                request.setAttribute("canEditScorecard", Boolean.TRUE);
            }

            if (AuthorizationHelper.hasUserPermission(request, Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME)
                    && ActionsHelper.isPhaseOpen(phase.getPhaseStatus())) {
                request.setAttribute("canReopenScorecard", Boolean.TRUE);
            }
        }

        // Check that the type of the review is Review,
        // as appeals and responses to them can only be placed to that type of scorecard
        if (scorecardTypeName.equals("Review")) {
            boolean canPlaceAppeal = false;
            boolean canPlaceAppealResponse = false;

            // Check if user can place appeals or appeal responses
            if (activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                    AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_PERM_NAME)) {
                if(mySubmitterResource != null && verification.getSubmission() != null &&
                    verification.getSubmission().getUpload().getOwner() == mySubmitterResource.getId()) {
                    // Can place appeal, put an appropriate flag to request
                    request.setAttribute("canPlaceAppeal", Boolean.TRUE);
                    canPlaceAppeal = true;
                }
            } else if (activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)  &&
                    AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_RESP_PERM_NAME)) {
                // Can place response, put an appropriate flag to request
                request.setAttribute("canPlaceAppealResponse", Boolean.TRUE);
                canPlaceAppealResponse = true;
            }

            if (canPlaceAppeal || canPlaceAppealResponse) {
                // Gather the appeal statuses and item answers
                String[] appealStatuses = new String[verification.getReview().getNumberOfItems()];
                String[] answers = new String[verification.getReview().getNumberOfItems()];
                for (int i = 0; i < appealStatuses.length; i++) {
                    Comment appeal = getCommentAppeal(verification.getReview().getItem(i).getAllComments());
                    Comment appealResponse = getCommentAppealResponse(verification.getReview().getItem(i).getAllComments());
                    if (appeal != null && appealResponse == null) {
                        appealStatuses[i] = getText("editReview.Appeal.Unresolved");
                    } else if (appeal != null) {
                        appealStatuses[i] = getText("editReview.Appeal.Resolved." + appeal.getExtraInfo());
                    } else {
                        appealStatuses[i] = "";
                    }

                    answers[i] = verification.getReview().getItem(i).getAnswer().toString();
                }
                // Set review item answers form property
                if (!export) {
                    getModel().set("answer", answers);
                }
                // Place appeal statuses to request
                request.setAttribute("appealStatuses", appealStatuses);

                // Retrieve some look-up data and store it into the request
                retrieveAndStoreReviewLookUpData(request);
            }
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        // Now all the information regarding the review is stored in request. We can forward to the view or export
        if (!export) {
            request.setAttribute("canExport", true);
            request.setAttribute("tableTitle", scorecardTemplate.getName());
            return Constants.SUCCESS_FORWARD_NAME;
        } else {
            exportGenericReview(request, response, reviewType);
            return null;
        }
    }

    /**
     * Export the generic review.
     * @param response the http servlet response
     * @param request the http servlet request
     * @param reviewType the review type
     * @throws BaseException if any error
     */
    protected void exportGenericReview(HttpServletRequest request, HttpServletResponse response, String reviewType) throws BaseException {
        String fileName = reviewType.toLowerCase().replace(' ', '_').replace('-', '_') + "_"
                + request.getParameter("rid");
        // create workbook and sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        sheet.setRowSumsBelow(false);
        // create font, color, and cell style
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        XSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        XSSFCellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setTopBorderColor(new XSSFColor(Color.BLACK));
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBottomBorderColor(new XSSFColor(Color.BLACK));
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setLeftBorderColor(new XSSFColor(Color.BLACK));
        borderStyle.setBorderRight(BorderStyle.THIN);
        borderStyle.setRightBorderColor(new XSSFColor(Color.BLACK));
        XSSFCellStyle boldBorderStyle = (XSSFCellStyle) borderStyle.clone();
        boldBorderStyle.setFont(boldFont);

        // get review related objects
        Project project = (Project) request.getAttribute("project");
        Review review = (Review) request.getAttribute("review");
        Resource reviewer = (Resource) request.getAttribute("authorResource");
        Scorecard scorecard = (Scorecard) request.getAttribute("scorecardTemplate");

        int rowNum = 0;
        // header
        rowNum = exportHeader(sheet, rowNum, request, reviewType, project, review, reviewer, boldStyle);

        // table header
        rowNum += 2;
        rowNum = exportTableHeader(sheet, rowNum, reviewType, boldBorderStyle);

        // table content
        exportTableContent(sheet, rowNum, request, reviewType, scorecard, review, borderStyle, boldBorderStyle);

        sheet.autoSizeColumn(0);
        sheet.setColumnWidth(1, (int) (12.5 * sheet.getColumnWidth(1)));
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            workbook.write(stream);
            stream.flush();
        } catch (IOException e) {
            throw new BaseException("Error occurs while exporting Excel sheet.", e);
        }
    }

    /**
     * Exports the header for the Excel sheet.
     * @param sheet
     *            the Excel sheet.
     * @param rowNum
     *            the starting row number.
     * @param request
     *            the HTTP request.
     * @param reviewType
     *            the review type.
     * @param project
     *            the project.
     * @param review
     *            the review.
     * @param reviewer
     *            the reviewer resource.
     * @param boldStyle
     *            the style with bold text.
     * @return the finishing row number.
     */
    private int exportHeader(Sheet sheet, int rowNum, HttpServletRequest request, String reviewType, Project project,
            Review review, Resource reviewer, XSSFCellStyle boldStyle) {
        Row row = null;
        XSSFCellStyle leftAlignCellStyle = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
        leftAlignCellStyle.setAlignment(HorizontalAlignment.LEFT);
        // header - project name
        row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldStyle, null, getText("exportReview.Header.Project"));
        fillCell(row, 1, null, null, (String) project.getProperty("Project Name"));
        // header - review id
        row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldStyle, null, getText("exportReview.Header.ReviewID"));
        fillCell(row, 1, leftAlignCellStyle, null, Long.parseLong(request.getParameter("rid")));
        // header - review type
        row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldStyle, null, getText("exportReview.Header.ReviewType"));
        fillCell(row, 1, null, null, reviewType);
        // header - author
        row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldStyle, null, getText("exportReview.Header.Author"));
        fillCell(row, 1, null, null, (String) reviewer.getProperty("Handle"));
        // header - submission
        if (request.getAttribute("sid") != null) {
            row = sheet.createRow(rowNum++);
            fillCell(row, 0, boldStyle, null, getText("exportReview.Header.Submission"));
            fillCell(row, 1, leftAlignCellStyle, null, (Long) request.getAttribute("sid"));
        }
        // header - total score
        if (!reviewType.equals("Aggregation") && !reviewType.equals("Final Review")) {
            row = sheet.createRow(rowNum++);
            fillCell(row, 0, boldStyle, null, getText("exportReview.Header.TotalScore"));
            fillCell(row, 1, leftAlignCellStyle, null, (double) review.getScore());
        }
        // header - status
        if (reviewType.equals("Specification Review") || reviewType.equals("Final Review")
                || reviewType.equals("Approval")) {
            row = sheet.createRow(rowNum++);
            fillCell(row, 0, boldStyle, null, getText("exportReview.Header.Status"));
            if (reviewType.equals("Specification Review")) {
                fillCell(row, 1, null, null, getReviewStatus(review, "Specification Review Comment"));
            } else if (reviewType.equals("Final Review")) {
                fillCell(row, 1, null, null, getReviewStatus(review, "Final Review Comment"));
            } else if (reviewType.equals("Approval")) {
                fillCell(row, 1, null, null, getReviewStatus(review, "Approval Review Comment"));
            }
        }
        return rowNum;
    }

    /**
     * Gets the review status. It is either approved or rejected.
     * @param review
     *            the review.
     * @param commentType
     *            the comment type.
     * @return the review status.
     */
    private String getReviewStatus(Review review, String commentType) {
        for (Comment comment : review.getAllComments()) {
            if (comment.getCommentType().getName().equals(commentType)) {
                if (comment.getExtraInfo() != null && comment.getExtraInfo().toString().length() > 0) {
                    if (comment.getExtraInfo().equals("Rejected")) {
                        return getText("exportReview.TableHeader.Rejected");
                    } else {
                        return getText("exportReview.TableHeader.Approved");
                    }
                }
                break;
            }
        }
        return "";
    }

    /**
     * Exports the table header for the Excel sheet.
     * @param sheet
     *            the Excel sheet.
     * @param rowNum
     *            the starting row number.
     * @param reviewType
     *            the review type.
     * @param boldBorderStyle
     *            the style with bold text and border.
     * @return the finishing row number.
     */
    private int exportTableHeader(Sheet sheet, int rowNum, String reviewType, XSSFCellStyle boldBorderStyle) {
        Row row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldBorderStyle, null, getText("exportReview.TableHeader.Name"));
        fillCell(row, 1, boldBorderStyle, null, getText("exportReview.TableHeader.Description"));
        if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
            fillCell(row, 2, boldBorderStyle, null, getText("exportReview.TableHeader.Reviewer"));
        } else {
            fillCell(row, 2, boldBorderStyle, null, getText("exportReview.TableHeader.Weight"));
        }
        fillCell(row, 3, boldBorderStyle, null, getText("exportReview.TableHeader.ResponseType"));
        if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
            fillCell(row, 4, boldBorderStyle, null, getText("exportReview.TableHeader.Status"));
        } else {
            fillCell(row, 4, boldBorderStyle, null, getText("exportReview.TableHeader.Score"));
        }
        return rowNum;
    }

    /**
     * Exports the table content for the Excel sheet.
     * @param sheet
     *            the Excel sheet.
     * @param rowNum
     *            the starting row number.
     * @param request
     *            the HTTP request.
     * @param reviewType
     *            the review type.
     * @param scorecard
     *            the scorecard.
     * @param review
     *            the review.
     * @param borderStyle
     *            the style with border.
     * @param boldBorderStyle
     *            the style with bold text and border.
     */
    private void exportTableContent(Sheet sheet, int rowNum, HttpServletRequest request, String reviewType,
            Scorecard scorecard, Review review, XSSFCellStyle borderStyle, XSSFCellStyle boldBorderStyle) {
        XSSFColor groupColor = new XSSFColor(new Color(commentExportColor.get("Group")));
        XSSFColor sectionColor = new XSSFColor(new Color(commentExportColor.get("Section")));
        XSSFColor questionColor = new XSSFColor(new Color(commentExportColor.get("Question")));
        XSSFColor attachmentColor = new XSSFColor(new Color(commentExportColor.get("Attachment")));
        XSSFCellStyle wrapTextStyle = (XSSFCellStyle) borderStyle.clone();
        wrapTextStyle.setWrapText(true);

        Row row = null;
        int groupIdx = 0;
        for (Group group : scorecard.getAllGroups()) {
            row = sheet.createRow(rowNum++);
            groupIdx++;
            fillCell(row, 0, boldBorderStyle, groupColor, getText("exportReview.Table.Group", new String[]{String.valueOf(groupIdx)} ));
            fillCell(row, 1, wrapTextStyle, groupColor, group.getName());
            if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
                fillCell(row, 2, borderStyle, groupColor, "");
            } else {
                fillCell(row, 2, borderStyle, groupColor, group.getWeight());
            }
            fillCell(row, 3, borderStyle, groupColor, "");
            fillCell(row, 4, borderStyle, groupColor, "");
            int sectionIdx = 0;
            for (Section section : group.getAllSections()) {
                row = sheet.createRow(rowNum++);
                sectionIdx++;
                fillCell(row, 0, boldBorderStyle, sectionColor,
                        getText("exportReview.Table.Section", new String[] { String.valueOf(groupIdx), String.valueOf(sectionIdx) }));
                fillCell(row, 1, wrapTextStyle, sectionColor, section.getName());
                if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
                    fillCell(row, 2, borderStyle, sectionColor, "");
                } else {
                    fillCell(row, 2, borderStyle, sectionColor, section.getWeight());
                }
                fillCell(row, 3, borderStyle, sectionColor, "");
                fillCell(row, 4, borderStyle, sectionColor, "");
                int questionIdx = 0;
                for (Question question : section.getAllQuestions()) {
                    int startRow = rowNum;
                    if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
                        row = sheet.createRow(rowNum++);
                        questionIdx++;
                        fillCell(
                                row,
                                0,
                                boldBorderStyle,
                                questionColor,
                                getText("exportReview.Table.Question", new String[] { String.valueOf(groupIdx), String.valueOf(sectionIdx),
                                        String.valueOf(questionIdx) }));
                        fillCell(row, 1, wrapTextStyle, questionColor, question.getDescription());
                        fillCell(row, 2, borderStyle, questionColor, "");
                        fillCell(row, 3, borderStyle, questionColor, "");
                        fillCell(row, 4, borderStyle, questionColor, "");
                    }
                    for (Item item : review.getAllItems()) {
                        if (item.getQuestion() == question.getId()) {
                            if (!reviewType.equals("Aggregation") && !reviewType.equals("Final Review")) {
                                row = sheet.createRow(rowNum++);
                                questionIdx++;
                                fillCell(
                                        row,
                                        0,
                                        boldBorderStyle,
                                        questionColor,
                                        getText("exportReview.Table.Question", new String[] { String.valueOf(groupIdx),
                                                String.valueOf(sectionIdx), String.valueOf(questionIdx) }));
                                fillCell(row, 1, wrapTextStyle, questionColor, question.getDescription());
                                fillCell(row, 2, borderStyle, questionColor, question.getWeight());
                                fillCell(row, 3, borderStyle, questionColor, "");
                                fillCell(row, 4, borderStyle, questionColor, getReviewAnswer(question, item));
                            }
                            // attached document
                            if (!reviewType.equals("Aggregation") && !reviewType.equals("Final Review")) {
                                if (item.getDocument() != null) {
                                    row = sheet.createRow(rowNum++);
                                    fillCell(row, 0, boldBorderStyle, attachmentColor,
                                            getText("exportReview.Table.AttachedDocument"));
                                    String url = request.getRequestURL().toString();
                                    fillCell(
                                            row,
                                            1,
                                            borderStyle,
                                            attachmentColor,
                                            url.substring(0, url.lastIndexOf("/"))
                                                    + getText("exportReview.Table.DownloadDocumentURL")
                                                    + item.getDocument());
                                    fillCell(row, 2, borderStyle, attachmentColor, "");
                                    fillCell(row, 3, borderStyle, attachmentColor, "");
                                    fillCell(row, 4, borderStyle, attachmentColor, "");
                                }
                            }
                            // comments
                            rowNum = exportComments(sheet, rowNum, request, reviewType, item, borderStyle,
                                    boldBorderStyle);
                        }
                    }
                    // grouping
                    sheet.groupRow(startRow + 1, rowNum - 1);
                }
            }
        }
    }

    /**
     * Exports comments for one review item.
     * @param sheet
     *            the Excel sheet.
     * @param rowNum
     *            the starting row number.
     * @param request
     *            the HTTP request.
     * @param reviewType
     *            the review type.
     * @param item
     *            the review item.
     * @param boldBorderStyle
     *            the style with bold text and border.
     * @param borderStyle
     *            the style with border.
     * @return the finishing row number.
     */
    private int exportComments(Sheet sheet, int rowNum, HttpServletRequest request, String reviewType, Item item,
            XSSFCellStyle borderStyle, XSSFCellStyle boldBorderStyle) {
        int commentIdx = 1;
        Resource reviewer = null;
        Row row = null;
        XSSFCellStyle commentStyle = (XSSFCellStyle) borderStyle.clone();
        commentStyle.setWrapText(true);
        for (Comment comment : item.getAllComments()) {
            // skip empty comment
            if (comment.getComment().length() == 0) {
                continue;
            }
            String commentType = comment.getCommentType().getName();
            if ((reviewType.equals("Aggregation") && !aggregationCommentTypes.contains(commentType))
                    || (reviewType.equals("Final Review") && !finalReviewCommentTypes.contains(commentType))) {
                continue;
            }
            row = sheet.createRow(rowNum++);
            XSSFColor color = new XSSFColor(new Color(commentExportColor.get(comment.getCommentType().getName())));
            if (reviewerCommentTypes.contains(commentType)) {
                fillCell(row, 0, boldBorderStyle, color,
                        getText("exportReview.Table.ReviewerResponse", new String[]{String.valueOf(commentIdx)} ));
                commentIdx++;
            } else {
                fillCell(row, 0, boldBorderStyle, color, commentType);
            }
            fillCell(row, 1, commentStyle, color, comment.getComment());
            if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
                Resource[] reviewers = (Resource[]) request.getAttribute("reviewResources");
                boolean resourceFound = false;
                for (Resource resource : reviewers) {
                    if (resource.getId() == comment.getAuthor()) {
                        if (reviewer != null && reviewer != resource) {
                            commentIdx = 1;
                        }
                        reviewer = resource;
                        resourceFound = true;
                        fillCell(row, 2, borderStyle, color, (String) resource.getProperty("Handle"));
                        break;
                    }
                }
                if (!resourceFound) {
                    fillCell(row, 2, borderStyle, color, "");
                }
            } else {
                fillCell(row, 2, borderStyle, color, "");
            }
            fillCell(row, 3, borderStyle, color, commentType);
            if (reviewType.equals("Aggregation")) {
                if (reviewerCommentTypes.contains(commentType)) {
                    if (comment.getExtraInfo() != null && comment.getExtraInfo().toString().length() > 0) {
                        fillCell(row, 4, borderStyle, color,
                                getText("AggregationItemStatus." + comment.getExtraInfo()));
                    }
                } else {
                    fillCell(row, 4, borderStyle, color, "");
                }
            } else if (reviewType.equals("Final Review")) {
                if (reviewerCommentTypes.contains(commentType)) {
                    if (comment.getExtraInfo() != null && comment.getExtraInfo().toString().length() > 0) {
                        if (comment.getExtraInfo().equals("Fixed")) {
                            fillCell(row, 4, borderStyle, color, getText("FinalReviewItemStatus.Fixed"));
                        } else {
                            fillCell(row, 4, borderStyle, color, getText("FinalReviewItemStatus.NotFixed"));
                        }
                    }
                } else {
                    fillCell(row, 4, borderStyle, color, "");
                }
            } else {
                fillCell(row, 4, borderStyle, color, "");
            }
        }
        return rowNum;
    }

    /**
     * Fills a spreadsheet cell using the given style and value.
     * @param row
     *            the row.
     * @param column
     *            the column number.
     * @param cellStyle
     *            the cell style.
     * @param fillColor
     *            the fill background color.
     * @param value
     *            the cell value.
     */
    private void fillCell(Row row, int column, XSSFCellStyle cellStyle, XSSFColor fillColor, Object value) {
        Cell cell = row.createCell(column);
        if (cellStyle != null) {
            if (fillColor != null) {
                cellStyle = (XSSFCellStyle) cellStyle.clone();
                cellStyle.setFillForegroundColor(fillColor);
                cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
            cell.setCellStyle(cellStyle);
        } else {
            cellStyle = (XSSFCellStyle) row.getSheet().getWorkbook().createCellStyle();
        }
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cell.setCellStyle(cellStyle);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            XSSFCellStyle numberCellStyle = (XSSFCellStyle) cellStyle.clone();
            DataFormat dataFormat = row.getSheet().getWorkbook().createDataFormat();
            numberCellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        }
    }

    /**
     * Gets review answer.
     * @param question
     *            the review question.
     * @param item
     *            the review item.
     * @return the review answer.
     */
    private String getReviewAnswer(Question question, Item item) {
        String questionType = question.getQuestionType().getName();
        String answer = item.getAnswer().toString();
        if (questionType.equals("Yes/No")) {
            return answer.equals("1") ? getText("global.answer.Yes") : getText("global.answer.No");
        } else if (questionType.equals("Scale (1-4)")) {
            answer = answer.replace("/4", "");
            if (answer.length() > 0) {
                return getText("Answer.Score4.ans" + answer);
            }
        } else if (questionType.equals("Scale (1-10)")) {
            answer = answer.replace("/10", "");
            return getText("Answer.Score10.Rating.title") + " " + answer;
        } else if (questionType.equals("Test Case")) {
            return answer.replace("/", " " + getText("editReview.Question.Response.TestCase.of") + " ");
        } else if (questionType.equals("Scale (0-3)")) {
            answer = answer.replace("/3", "");
            if (answer.length() > 0) {
                return getText("Answer.Score3.ans" + answer);
            }
        } else if (questionType.equals("Scale (0-9)")) {
            answer = answer.replace("/9", "");
            return getText("Answer.Score9.Rating.title") + " " + answer;
        } else if (questionType.equals("Scale (0-4)")) {
            answer = answer.replace("/4", "");
            if (answer.length() > 0) {
                return getText("Answer.Score0_4.ans" + answer);
            }
        } else {
            return answer;
        }
        return "";
    }

    /**
     * Gets my 'submitter' resource.
     *
     * @param request the HttpServletRequest
     * @return my submitter resource
     */
    protected static Resource getMySubmitterResource(HttpServletRequest request) {
        Resource[] myResources = ActionsHelper.getMyResourcesForPhase(request, null);
        for (Resource resource : myResources) {
            if (resource.getResourceRole().getName().equals("Submitter")) {
                return resource;
            }
        }
        return null;
    }

    /**
     * Get the comment appeal.
     *
     * @param allComments all comments
     * @return the appeal comment
     */
    private static Comment getCommentAppeal(Comment[] allComments) {
        for (Comment comment : allComments) {
            if (comment.getCommentType().getName().equals("Appeal")) {
                return comment;
            }
        }
        return null;
    }

    /**
     * Get the comment appeal response.
     *
     * @param allComments all comments
     * @return the appeal response comment
     */
    private static Comment getCommentAppealResponse(Comment[] allComments) {
        for (Comment comment : allComments) {
            if (comment.getCommentType().getName().equals("Appeal Response")) {
                return comment;
            }
        }
        return null;
    }
}

