/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to edit the aggregation.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EditAggregationAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 2182093149561888573L;

    /**
     * This method is an implementation of &quot;Edit Aggregation&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (aggregation and review
     * scorecard template) and present it to editAggregation.jsp page, which will fill the required
     * fields and post them to the &quot;Save Aggregation&quot; action. The action implemented by
     * this method is executed to edit aggregation that has already been created (by the system),
     * but has not been submitted yet, and hence is supposed to be edited.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/editAggregation.jsp page (as
     *         defined in struts.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
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
                Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to edit
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

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                LookupHelper.getCommentType("Comment"),
                LookupHelper.getCommentType("Required"),
                LookupHelper.getCommentType("Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);

        int allCommentsNum = 0;

        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                if (ActionsHelper.isReviewerComment(item.getComment(j))) {
                    ++allCommentsNum;
                }
            }
        }

        String[] aggregatorResponses = new String[review.getNumberOfItems()];
        String[] aggregateFunctions = new String[allCommentsNum];
        Long[] responseTypeIds = new Long[allCommentsNum];
        int commentIdx = 0;
        int itemIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                    Question question = section.getQuestion(questionIdx);
                    long questionId = question.getId();

                    for (int i = 0; i < aggregatorResponses.length; ++i) {
                        if (review.getItem(i).getQuestion() != questionId) {
                            continue;
                        }

                        // Get a review's item
                        Item item = review.getItem(i);
                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);

                            if (ActionsHelper.isReviewerComment(comment)) {
                                String aggregFunction = (String) comment.getExtraInfo();
                                if ("Reject".equalsIgnoreCase(aggregFunction)) {
                                    aggregateFunctions[commentIdx] = "Reject";
                                } else if ("Accept".equalsIgnoreCase(aggregFunction)) {
                                    aggregateFunctions[commentIdx] = "Accept";
                                } else if ("Duplicate".equalsIgnoreCase(aggregFunction)) {
                                    aggregateFunctions[commentIdx] = "Duplicate";
                                } else {
                                    aggregateFunctions[commentIdx] = "";
                                }
                                responseTypeIds[commentIdx] = comment.getCommentType().getId();
                                ++commentIdx;
                            }

                            final String commentType = comment.getCommentType().getName();

                            if (commentType.equalsIgnoreCase("Aggregation Comment")) {
                                aggregatorResponses[itemIdx++] = comment.getComment();
                            }
                        }
                    }
                }
            }
        }

        getModel().set("aggregator_response", aggregatorResponses);
        getModel().set("aggregate_function", aggregateFunctions);
        getModel().set("aggregator_response_type", responseTypeIds);

        request.setAttribute("tableTitle", getText("editReview.EditAggregation.title"));

        return Constants.SUCCESS_FORWARD_NAME;
    }
}

