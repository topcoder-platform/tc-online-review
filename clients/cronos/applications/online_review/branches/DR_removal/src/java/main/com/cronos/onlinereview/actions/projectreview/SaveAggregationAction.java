/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.topcoder.management.resource.Resource;
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
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to save the aggregation.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveAggregationAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -1773931904048336330L;

    /**
     * This method is an implementation of &quot;Save Aggregation&quot; Struts Action defined for
     * this assembly, which is supposed to save information posted from /jsp/editAggregation.jsp
     * page. This method will update (edit) existing aggregation.
     *
     * @return &quot;success&quot;, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent review id, or the lack of permissions, etc.).
     * @throws BaseException
     *         if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(request, Constants.PERFORM_AGGREGATION_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that user has the permission to perform aggregation
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to save
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewCommitted", null);
        }

        // Get an array of all phases for current project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(false), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.AGGREGATION_PHASE_NAME);
        // Check that Aggregation phase is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(this,
                request, Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.IncorrectPhase", null);
        }

        // This variable determines if 'Save and Mark Complete' button has been clicked
        final boolean commitRequested = "submit".equalsIgnoreCase(request.getParameter("save"));
        // This variable determines if Preview button has been clicked
        final boolean previewRequested = "preview".equalsIgnoreCase(request.getParameter("save"));

        // Retrieve a resource for the Aggregation phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);

        // Get form's fields
        String[] responses = (String[]) getModel().get("aggregator_response");
        String[] aggregateFunctions = (String[]) getModel().get("aggregate_function");
        Long[] responseTypeIds = (Long[]) getModel().get("aggregator_response_type");
        int commentIndex = 0;
        int itemIdx = 0;

        int numberOfItems = review.getNumberOfItems();

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++ questionIdx) {
                    Question question = section.getQuestion(questionIdx);
                    long questionId = question.getId();

                    for (int i = 0; i < numberOfItems; ++i) {
                        if (review.getItem(i).getQuestion() != questionId) {
                            continue;
                        }

                        // Get an item
                        Item item = review.getItem(i);
                        Comment aggregatorComment = null;

                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);
                            String typeName = comment.getCommentType().getName();

                            if (typeName.equalsIgnoreCase("Comment") || typeName.equalsIgnoreCase("Required") ||
                                typeName.equalsIgnoreCase("Recommended")) {
                                if (commentIndex < aggregateFunctions.length && aggregateFunctions[commentIndex] != null &&
                                        aggregateFunctions[commentIndex].trim().length() != 0) {
                                    comment.setExtraInfo(aggregateFunctions[commentIndex]);
                                } else {
                                    comment.setExtraInfo(null);
                                }
                                comment.setCommentType(LookupHelper.getCommentType(responseTypeIds[commentIndex]));
                                ++commentIndex;
                            }
                            if (typeName.equalsIgnoreCase("Aggregation Comment")) {
                                aggregatorComment = comment;
                            }
                        }

                        if (aggregatorComment == null) {
                            aggregatorComment = new Comment();
                            aggregatorComment.setCommentType(LookupHelper.getCommentType("Aggregation Comment"));
                            item.addComment(aggregatorComment);
                        }

                        aggregatorComment.setComment(responses[itemIdx++]);
                        aggregatorComment.setAuthor(resource.getId());
                    }
                }
            }
        }

        boolean validationSucceeded =
                (!commitRequested) || validateAggregationScorecard(request, scorecardTemplate, review);

        // If the user has requested to complete the review
        if (validationSucceeded && commitRequested) {
            // Set the completed status of the review
            review.setCommitted(true);
        } else if (previewRequested) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Retrieve some basic aggregation info and store it into the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

            // Notify View page that this is actually a preview operation
            request.setAttribute("isPreview", Boolean.TRUE);

            // Forward to preview page
            return Constants.PREVIEW_FORWARD_NAME;
        }

        // Update (save) edited Aggregation
        ReviewManager revMgr = ActionsHelper.createReviewManager();
        revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        if (!validationSucceeded) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");
            // Retrieve some look-up data and store it into the request
            retrieveAndStoreReviewLookUpData(request);

            return INPUT;
        }

        // Forward to project details page
        setPid(verification.getProject().getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * This static method validates Aggregation scorecard. In order to pass validation, Aggregation
     * must have all its aggregate functions to be specified. Per-item comments ae not required.
     *
     * @return <code>true</code> if aggregation scorecard passes validation, <code>false</code>
     *         if it fails it.
     * @param request
     *        an <code>HttpServletRequest</code> object where validation error messages will
     *        be placed to in case there are any.
     * @param scorecardTemplate
     *        a scorecard template of type &quot;Review&quot; that was used to generate the
     *        aggregation scorecard to be validated.
     * @param aggregation
     *        an aggregation scorecard to be validated.
     * @throws IllegalArgumentException
     *         if parameters <code>request</code>, <code>scorecardTemplate</code>, or
     *         <code>aggregation</code> are <code>null</code>.
     */
    private static boolean validateAggregationScorecard(
        HttpServletRequest request, Scorecard scorecardTemplate, Review aggregation) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(scorecardTemplate, "scorecardTemplate");
        ActionsHelper.validateParameterNotNull(aggregation, "aggregation");

        final int numberOfItems = aggregation.getNumberOfItems();
        int itemIdx = 0;
        int commentIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                    Question question = section.getQuestion(questionIdx);
                    long questionId = question.getId();

                    for (int i = 0; i < numberOfItems; ++i) {
                        if (aggregation.getItem(i).getQuestion() != questionId) {
                            continue;
                        }

                        // Get a review's item
                        Item item = aggregation.getItem(i);

                        // Validate item's aggregate functions
                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);
                            String commentType = comment.getCommentType().getName();

                            if (commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                                commentType.equalsIgnoreCase("Recommended")) {
                                validateAggregateFunction(request, item.getComment(j), commentIdx++);
                            }

                            if (commentType.equalsIgnoreCase("Aggregation Comment")) {
                                /* Request from David Messinger [11/06/2006]:
                                   No need to verify presence of comments
                                validateScorecardComment(request, comment, "aggregator_response[" + itemIdx + "]");
                                */

                                // But we still need to verify comment's maximum length.
                                String commentText = comment.getComment();
                                if (commentText != null && commentText.length() > MAX_COMMENT_LENGTH) {
                                    ActionsHelper.addErrorToRequest(request, "aggregator_response[" + itemIdx + "]",
                                        "Error.saveAggregation.Comment.MaxExceeded");
                                }
                            }
                        }
                    }
                }
            }
        }

        return !ActionsHelper.isErrorsPresent(request);
    }

    /**
     * This static method validates a reviewer's comment to have an aggregate function. The
     * aggregate function must be specified (non-null and non-empty string), and must be equal to
     * one of the following values:
     * <ul>
     * <li>&quot;<code>Accept</code>&quot;</li>
     * <li>&quot;<code>Reject</code>&quot;</li>
     * <li>&quot;<code>Duplicate</code>&quot;</li>
     * </ul>
     *
     * @return <code>true</code> if validation was successful, <code>false</code> if it wasn't.
     * @param request
     *        an <code>HttpServletRequest</code> object where validation error messages will
     *        be placed to in case there are any.
     * @param comment
     *        a reviewer's comment to validate.
     * @param commentIdx
     *        absolute index of the comment on the page.
     * @throws IllegalArgumentException
     *         if parameters <code>request</code> or <code>comment</code> are
     *         <code>null</code>, or if comment specified by parameter <code>comment</code>
     *         is not a reviewer's comment.
     */
    private static boolean validateAggregateFunction(HttpServletRequest request, Comment comment, int commentIdx) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        if (!(commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
            commentType.equalsIgnoreCase("Recommended"))) {
            throw new IllegalArgumentException(
                    "Specified comment is not a reviewer's comment. Comment type: '" + commentType + "'.");
        }

        String aggregationFunction = (String) comment.getExtraInfo();

        if (aggregationFunction == null || aggregationFunction.trim().length() == 0) {
            ActionsHelper.addErrorToRequest(request,
                    "aggregate_function[" + commentIdx + "]", "Error.saveAggregation.Function.Absent");
            return false;
        }

        if (!(aggregationFunction.equalsIgnoreCase("Accept") || aggregationFunction.equalsIgnoreCase("Reject") ||
            aggregationFunction.equalsIgnoreCase("Duplicate"))) {
            ActionsHelper.addErrorToRequest(request,
                    "aggregate_function[" + commentIdx + "]", "Error.saveAggregation.Function.Invalid");
            return false;
        }

        return true;
    }
}

