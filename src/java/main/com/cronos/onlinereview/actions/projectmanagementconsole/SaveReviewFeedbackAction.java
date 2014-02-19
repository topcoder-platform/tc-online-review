/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectmanagementconsole;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackDetail;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for saving review feedback.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveReviewFeedbackAction extends BaseProjectManagementConsoleAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -1751496382781019566L;

    /**
     * Creates a new instance of the <code>SaveReviewFeedbackAction</code> class.
     */
    public SaveReviewFeedbackAction() {
    }

    /**
     * <p>Processes the incoming request for saving the review performance feedbacks for requested project.</p>
     *
     * @return a <code>String</code> referencing the next view to be displayed to user.
     * @throws Exception if an unexpected error occurs.
     */
    public String execute() throws Exception {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false,
                request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        request.setAttribute("activeTabIdx", 3);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        verification = ActionsHelper.checkForCorrectProjectId(this, request,
                                                     Constants.PROJECT_MANAGEMENT_PERM_NAME, false);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        } else {
            // Validate the forms
            final Project project = verification.getProject();
            final boolean reviewFeedbackAllowed = isReviewFeedbackAllowed(project);

            long currentUserId = AuthorizationHelper.getLoggedInUserId(request);
            String currentUserIdString = Long.toString(currentUserId);

            if (reviewFeedbackAllowed) {
                ReviewFeedbackManager reviewFeedbackManager = ActionsHelper.createReviewFeedbackManager();
                List<ReviewFeedback> existingReviewFeedbacks = reviewFeedbackManager.getForProject(project.getId());
                if (existingReviewFeedbacks.size() > 1) {
                    throw new BaseException("These should be at most 1 review feedback");
                }

                boolean updated = existingReviewFeedbacks.size() == 1;
                request.setAttribute("toEdit", updated);

                if (updated) {
                    // if current user is in the existing feedback details, he/she can't edit the review feedback
                    for (ReviewFeedbackDetail detail : existingReviewFeedbacks.get(0).getDetails()) {
                        if (detail.getReviewerUserId() == AuthorizationHelper.getLoggedInUserId(request)) {
                            return ActionsHelper.produceErrorReport(this, request,
                                    Constants.PROJECT_MANAGEMENT_PERM_NAME,
                                    "Error.EditReviewFeedbackNotAllowed", Boolean.TRUE);
                        }
                    }
                }

                // Get the list of reviewers eligible for feedback and convert it to set of respective user IDs
                List<Resource> reviewerResources = getFeedbackEligibleReviewers(project.getId(), request);
                Set<Long> eligibleReviewerUserIds = new HashSet<Long>();
                for (Resource reviewer : reviewerResources) {
                    String reviewerUserId = (String) reviewer.getProperty("External Reference ID");
                    eligibleReviewerUserIds.add(Long.parseLong(reviewerUserId));
                }

                // Validate the input
                java.lang.Long[] reviewerUserIds = (java.lang.Long[]) getModel().get("reviewerUserId");
                java.lang.Integer[] reviewerScores = (java.lang.Integer[]) getModel().get("reviewerScore");
                java.lang.String[] reviewerFeedbacks = (java.lang.String[]) getModel().get("reviewerFeedback");
                Boolean unavailable = (Boolean) getModel().get("unavailable");
                unavailable = unavailable == null ? Boolean.FALSE : unavailable;
                String explanation = (String) getModel().get("explanation");

                if (!unavailable) {
                    for (int i = 0; i < reviewerUserIds.length; i++) {
                        if (reviewerScores.length <= i || reviewerScores[i] == null) {
                            ActionsHelper.addErrorToRequest(request, "reviewerScore[" + i + "]", 
                                "error.com.cronos.onlinereview.actions.manageProject.ReviewPerformance.Score.Empty");
                        } else if (reviewerScores[i] < 0 || reviewerScores[i] > 2) {
                            ActionsHelper.addErrorToRequest(request, "reviewerScore[" + i + "]", 
                                "error.com.cronos.onlinereview.actions.manageProject.ReviewPerformance.Score.Invalid");
                        }
                        if (reviewerFeedbacks.length <= i || reviewerFeedbacks[i] == null
                            || reviewerFeedbacks[i].trim().length() == 0) {
                            ActionsHelper.addErrorToRequest(request, "reviewerFeedback[" + i + "]", 
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.Feedback.Empty");
                        } else if (reviewerFeedbacks[i].length() > MAX_FEEDBACK_LENGTH) {
                            ActionsHelper.addErrorToRequest(request, "reviewerFeedback[" + i + "]", 
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.Feedback.MaxExceeded");
                        }
                        if (currentUserId == reviewerUserIds[i]) {
                            ActionsHelper.addErrorToRequest(request, "reviewerFeedback[" + i + "]", 
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.SelfFeedbackProhibited");
                        } else if (!eligibleReviewerUserIds.contains(reviewerUserIds[i])) {
                            ActionsHelper.addErrorToRequest(request, "reviewerFeedback[" + i + "]", 
                                "error.com.cronos.onlinereview.actions.manageProject.ReviewPerformance.WrongReviewer");
                        }
                        eligibleReviewerUserIds.remove(reviewerUserIds[i]);
                    }
                    if (eligibleReviewerUserIds.size() > 0) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.MissingReviewer");
                    }
                } else {
                    if (explanation == null || explanation.trim().length() == 0) {
                        ActionsHelper.addErrorToRequest(request, "explanation", 
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.Explanation.Empty");
                    } else if (explanation.length() > MAX_FEEDBACK_LENGTH) {
                        ActionsHelper.addErrorToRequest(request, "explanation", 
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.Explanation.MaxExceeded");
                    }
                }
                if (!ActionsHelper.isErrorsPresent(request)) {
                    ReviewFeedback feedback;
                    if (updated) {
                        feedback = existingReviewFeedbacks.get(0);
                    } else {
                        feedback = new ReviewFeedback();
                        feedback.setProjectId(project.getId());
                    }
                    List<ReviewFeedbackDetail> feedbackDetails = new ArrayList<ReviewFeedbackDetail>();
                    if (unavailable) {
                        feedback.setComment(explanation);
                    } else {
                        feedback.setComment(null);
                        // Save the feedback details
                        for (int i = 0; i < reviewerUserIds.length; i++) {
                            ReviewFeedbackDetail feedbackDetail = new ReviewFeedbackDetail();
                            feedbackDetail.setFeedbackText(reviewerFeedbacks[i]);
                            feedbackDetail.setReviewerUserId(reviewerUserIds[i]);
                            feedbackDetail.setScore(reviewerScores[i]);
                            feedbackDetails.add(feedbackDetail);
                        }
                    }
                    feedback.setDetails(feedbackDetails);
                    if (updated) {
                        reviewFeedbackManager.update(feedback, currentUserIdString);
                    } else {
                        reviewFeedbackManager.create(feedback, currentUserIdString);
                    }
                }
            } else {
                return ActionsHelper.produceErrorReport(this, request,
                                                        Constants.PROJECT_MANAGEMENT_PERM_NAME,
                                                        "Error.ReviewFeedbackNotAllowed", Boolean.TRUE);
            }

            if (ActionsHelper.isErrorsPresent(request)) {
                initProjectManagementConsole(request, project);
                return INPUT;
            } else {
                setPid(project.getId());
                return Constants.SUCCESS_FORWARD_NAME;
            }
        }
    }
}

