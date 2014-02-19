/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectmanagementconsole;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * This class is the struts action class which is used for displaying editing review feedback page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EditReviewFeedbackAction extends BaseProjectManagementConsoleAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -6938493234315995910L;

    /**
     * Creates a new instance of the <code>EditReviewFeedbackAction</code> class.
     */
    public EditReviewFeedbackAction() {
    }

    /**
     * <p>Processes the incoming request which is a request for viewing the <code>Edit Review Feedback</code> view
     * for requested project.</p>
     *
     * <p>Verifies that current user is granted access to this functionality and is granted a permission to access the
     * requested project details, also the user doesn't exist in the existing feedback details.</p>
     *
     * @return a <code>String</code> referencing the next view to be displayed to user.
     * @throws BaseException if an unexpected error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false,
                request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        request.setAttribute("activeTabIdx", 3);
        request.setAttribute("toEdit", Boolean.TRUE);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. Also check that current user is granted a
        // permission to access the details for requested project
        verification = ActionsHelper.checkForCorrectProjectId(this, request,
                Constants.VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME, false);

        if (!verification.isSuccessful()) {
            // If not then redirect the request to log-in page or report about the lack of permissions.
            return verification.getResult();
        } else {
            final Project project = verification.getProject();
            if (!isReviewFeedbackAllowed(project)) {
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.PROJECT_MANAGEMENT_PERM_NAME,
                        "Error.ReviewFeedbackNotAllowed", Boolean.TRUE);
            }

            ReviewFeedbackManager reviewFeedbackManager = ActionsHelper.createReviewFeedbackManager();
            List<ReviewFeedback> existingReviewFeedbacks = reviewFeedbackManager.getForProject(project.getId());
            if (existingReviewFeedbacks.size() != 1) {
                throw new BaseException("These should be exactly one existing review feedback");
            }
            ReviewFeedback feedback = existingReviewFeedbacks.get(0);
            // if current user is in the existing feedback details, he/she can't edit the review feedback
            for (ReviewFeedbackDetail detail : feedback.getDetails()) {
                if (detail.getReviewerUserId() == AuthorizationHelper.getLoggedInUserId(request)) {
                    return ActionsHelper.produceErrorReport(this, request,
                            Constants.PROJECT_MANAGEMENT_PERM_NAME,
                            "Error.EditReviewFeedbackNotAllowed", Boolean.TRUE);
                }
            }

            initProjectManagementConsole(request, project);

            // Populate the form properties
            if (feedback.getComment() != null) {
                getModel().set("unavailable", Boolean.TRUE);
            }
            getModel().set("explanation", feedback.getComment());
            Map<String, Resource> reviewerResourcesMap =
                    (Map<String, Resource>) request.getAttribute("reviewerResourcesMap");
            Map<String, ReviewFeedbackDetail> reviewerFeedbackDetail = new HashMap<String, ReviewFeedbackDetail>();
            for (ReviewFeedbackDetail detail : feedback.getDetails()) {
                reviewerFeedbackDetail.put(String.valueOf(detail.getReviewerUserId()), detail);
            }
            int idx = -1;
            for (Map.Entry<String, Resource> entry : reviewerResourcesMap.entrySet()) {
                idx++;
                getModel().set("reviewerUserId", idx, Long.parseLong(entry.getKey()));
                ReviewFeedbackDetail detail = reviewerFeedbackDetail.get(entry.getKey());
                if (detail == null) {
                    continue;
                }
                getModel().set("reviewerScore", idx, detail.getScore());
                getModel().set("reviewerFeedback", idx, detail.getFeedbackText());
            }

            return Constants.SUCCESS_FORWARD_NAME;
        }
    }
}

