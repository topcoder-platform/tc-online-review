/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to edit the approval.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EditApprovalAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 5769896916144075733L;

    /**
     * This method is an implementation of &quot;Edit Approval&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (approval and scorecard template)
     * and present it to editApproval.jsp page, which will fill the required fields and post them to
     * the &quot;Save Approval&quot; action. The action implemented by this method is executed to
     * edit approval that has already been created, but has not been submitted yet, and hence is
     * supposed to be edited.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/editApproval.jsp page (as
     *         defined in struts.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @throws BaseException
     *         if any error occurs.
     */
    public String execute() throws BaseException{
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        String genericForward = editGenericReview(request, "Approval");
        if (Constants.SUCCESS_FORWARD_NAME.equals(genericForward)) {
            Boolean fixesRejected = null;
            boolean fixesAcceptedButOtherFixesRequired = false;
            Review review = (Review) request.getAttribute("review");
            int numberOfComments = review.getNumberOfComments();
            for (int i = 0; i < numberOfComments; ++i) {
                Comment comment = review.getComment(i);
                if (comment.getCommentType().getName().equalsIgnoreCase("Approval Review Comment")) {
                    fixesRejected = ("Rejected".equalsIgnoreCase((String) comment.getExtraInfo()));
                } else if (comment.getCommentType().getName().equalsIgnoreCase("Approval Review Comment - Other Fixes")) {
                    fixesAcceptedButOtherFixesRequired = ("Required".equalsIgnoreCase((String) comment.getExtraInfo()));
                }
            }

            if (fixesRejected != null) {
                getModel().set("approve_fixes", !fixesRejected);
            }
            getModel().set("accept_but_require_fixes", fixesAcceptedButOtherFixesRequired);
        }
        return genericForward;
    }
}

