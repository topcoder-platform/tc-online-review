/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import java.util.Arrays;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to edit the final review.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EditFinalReviewAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 527774219543077504L;

    /**
     * This method is an implementation of &quot;Edit Final Review&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (final fix review and review
     * scorecard template) and present it to editFinalReview.jsp page, which will fill the required
     * fields and post them to the &quot;Save Final Review&quot; action. The action implemented by
     * this method is executed to edit final fix review that has already been created (by the
     * system), but has not been submitted yet, and hence is supposed to be edited.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/editFinalReview.jsp page (as
     *         defined in struts.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that user has the permission to perform final review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Check if project has SVN Module property set
        String svnModule = (String) verification.getProject().getProperty("SVN Module");
        request.setAttribute("projectHasSVNModuleSet", (svnModule != null) && (svnModule.trim().length() > 0));

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review") &&
            !scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Approval")) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");
        request.setAttribute("approvalBased", scorecardTemplate.getScorecardType().getName().equals("Approval"));

        int reviewerCommentsNum = 0;
        int[] lastCommentIdxs = new int[review.getNumberOfItems()];

        Arrays.fill(lastCommentIdxs, 0);

        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                String commentType = item.getComment(j).getCommentType().getName();
                if (commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                    commentType.equalsIgnoreCase("Recommended")) {
                    ++reviewerCommentsNum;
                    ++lastCommentIdxs[i];
                } else if (commentType.equalsIgnoreCase("Manager Comment") ||
                    commentType.equalsIgnoreCase("Appeal") ||
                    commentType.equalsIgnoreCase("Appeal Response") ||
                    commentType.equalsIgnoreCase("Aggregation Comment") ||
                    commentType.equalsIgnoreCase("Aggregation Review Comment") ||
                    commentType.equalsIgnoreCase("Submitter Comment")) {
                    ++lastCommentIdxs[i];
                }
            }
        }

        request.setAttribute("lastCommentIdxs", lastCommentIdxs);

        boolean fixesApproved = false;

        for (int i = 0; i < review.getNumberOfComments(); ++i) {
            Comment comment = review.getComment(i);
            if (comment.getCommentType().getName().equalsIgnoreCase("Final Review Comment")) {
                fixesApproved = ("Approved".equalsIgnoreCase((String) comment.getExtraInfo()));
                break;
            }
        }

        String[] fixStatuses = new String[reviewerCommentsNum];
        String[] finalComments = new String[review.getNumberOfItems()];
        Boolean approveFixes = fixesApproved;

        Arrays.fill(fixStatuses, "");
        Arrays.fill(finalComments, "");

        int commentIdx = 0;
        int itemIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                    // Get the ID of the current scorecard template's question
                    final long questionId = section.getQuestion(questionIdx).getId();

                    // Iterate over the items of existing review that needs editing
                    for (int i = 0; i < review.getNumberOfItems(); ++i) {
                        // Get an item for the current iteration
                        Item item = review.getItem(i);
                        // Verify that this item is for the current scorecard template question
                        if (item.getQuestion() != questionId) {
                            continue;
                        }

                        boolean finalReviewCommentNotFound = true;

                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);
                            String commentType = comment.getCommentType().getName();

                            if (ActionsHelper.isReviewerComment(comment)) {
                                String fixStatus = (String) comment.getExtraInfo();
                                if ("Fixed".equalsIgnoreCase(fixStatus)) {
                                    fixStatuses[commentIdx] = "Fixed";
                                } else if ("Not Fixed".equalsIgnoreCase(fixStatus)) {
                                    fixStatuses[commentIdx] = "Not Fixed";
                                }
                                ++commentIdx;
                            }
                            if (finalReviewCommentNotFound && commentType.equalsIgnoreCase("Final Review Comment")) {
                                finalComments[itemIdx++] = comment.getComment();
                                finalReviewCommentNotFound = false;
                            }
                        }
                    }
                }
            }
        }

        getModel().set("fix_status", fixStatuses);
        getModel().set("final_comment", finalComments);
        getModel().set("approve_fixes", approveFixes);

        request.setAttribute("tableTitle", getText("editFinalReview.Scorecard.title"));

        return Constants.SUCCESS_FORWARD_NAME;
    }
}

