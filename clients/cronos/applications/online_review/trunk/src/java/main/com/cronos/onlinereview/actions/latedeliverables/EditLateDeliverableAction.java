/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.latedeliverables;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for rendering the editing late deliverable page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EditLateDeliverableAction extends BaseLateDeliverableAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -7830948309817705434L;

    /**
     * Creates a new instance of the <code>EditLateDeliverableAction</code> class.
     */
    public EditLateDeliverableAction() {
    }

    /**
     * <p>This method is an implementation of &quot;Edit Late Deliverable&quot; Struts Action, which is supposed to
     * show the form for editing the selected late deliverable.</p>
     *
     * @return &quot;success&quot; string result that forwards to the /jsp/editLateDeliverable.jsp page (as defined in
     *         struts-config.xml file) in the case of successfully processing, &quot;notAuthorized&quot; forward in the
     *         case of user not being authorized to perform the action.
     * @throws BaseException when any error happens while processing in TCS components
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // check user login
        if (!AuthorizationHelper.isUserLoggedIn(request)) {
            AuthorizationHelper.setLoginRedirect(request, false);
            return Constants.NOT_AUTHORIZED_FORWARD_NAME;
        }

        // remove login redirect
        AuthorizationHelper.removeLoginRedirect(request);

        // Get requested late deliverable ID and raise an exception if it is not found
        Long lateDeliverableId = (Long) getModel().get("late_deliverable_id");
        LateDeliverable lateDeliverable;
        if (lateDeliverableId <= 0 || ((lateDeliverable = getLateDeliverable(lateDeliverableId)) == null)) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_LATE_DELIVERABLE_PERM_NAME, "Error.UnknownLateDeliverable", null);
        } else {
            // Gather the roles the user has for current request and project
            AuthorizationHelper.gatherUserRoles(request, lateDeliverable.getProjectId());

            // Check if user has a permission to view the late deliverable. The users can view late deliverables
            // either if the they are granted View Late Deliverable permission or if they are the source of the late
            // deliverable
            long lateDeliverableUserId = getLateDeliverableUserId(lateDeliverable);
            long currentUserId = AuthorizationHelper.getLoggedInUserId(request);
            boolean isLateDeliverableOwner = (currentUserId == lateDeliverableUserId);

            boolean canViewLateDeliverable
                = AuthorizationHelper.hasUserPermission(request, Constants.VIEW_LATE_DELIVERABLE_PERM_NAME);

            if (!canViewLateDeliverable && !isLateDeliverableOwner) {
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.VIEW_LATE_DELIVERABLE_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }

            // Populate form properties with data from late deliverable
            getModel().set("justified", lateDeliverable.isForgiven());
            getModel().set("explanation", lateDeliverable.getExplanation());
            getModel().set("response", lateDeliverable.getResponse());
            getModel().set("late_deliverable_id", lateDeliverable.getId());

            // Set additional request attributes to be consumed by JSP
            setEditLateDeliverableRequest(request, lateDeliverable, isLateDeliverableOwner);

            return Constants.SUCCESS_FORWARD_NAME;
        }
    }
}

