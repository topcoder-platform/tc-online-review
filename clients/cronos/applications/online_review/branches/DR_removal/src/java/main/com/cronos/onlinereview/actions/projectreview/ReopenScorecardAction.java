/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.project.Project;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to reopen the scorecard.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ReopenScorecardAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 8068099599269935143L;

    /**
     * This method is an implementation of &quot;Reopen Scorecard&quot; Struts Action defined for this
     * assembly, which is supposed to reopen a scorecard.
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmReopenScorecard.jsp page,
     *         which displays the confirmation dialog where user can confirm his intention to reopen the scorecard.
     *         If this action was called during the post back (the second time), then this method verifies if
     *         everything is correct, and process the reopen logic. After this it returns a forward to the
     *         View Project Details page.
     * @throws BaseException
     *         if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false,
                request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(request, Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        if (!AuthorizationHelper.hasUserPermission(request, Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(this,
                request, Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        Review review = verification.getReview();
        Project project = verification.getProject();
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME, "Error.ReopenReviewNotCommitted", null);
        }

        Phase[] allPhases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        if (!ActionsHelper.isPhaseOpen(ActionsHelper.findPhaseById(allPhases,
                review.getProjectPhase()).getPhaseStatus())) {
            // phase is not open
            return ActionsHelper.produceErrorReport(this, request,
                Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME, "Error.ReopenReviewPhaseNotOpen", null);
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("reopen") != null);

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);
            return Constants.DISPLAY_PAGE_FORWARD_NAME;
        }

        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        review.setCommitted(false);
        ActionsHelper.createReviewManager().updateReview(review, operator);

        setPid(project.getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }
}

