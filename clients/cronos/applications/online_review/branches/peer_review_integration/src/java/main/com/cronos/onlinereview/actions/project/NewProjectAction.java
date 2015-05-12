/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.project;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.util.errorhandling.BaseException;


/**
 * This class is the struts action class which is used for rendering the creating new project page.
 * <p>
 * Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class NewProjectAction extends BaseProjectAction {
    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 2736003377094175464L;

    /**
     * This method is an implementation of &quot;New Project&quot; Struts Action defined for this
     * assembly, which is supposed to fetch lists of project types and categories from the database
     * and pass it to the JSP page to use it for populating appropriate drop down lists.
     *
     * @return &quot;success&quot; result that forwards to the /jsp/editProject.jsp page (as
     *         defined in struts.xml file) in the case of successful processing,
     *         &quot;notAuthorized&quot; result in the case of user not being authorized to perform
     *         the action.
     * @throws BaseException
     *             when any error happens while processing in TCS components
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        // Call the ActionsHelper.checkThrottle using new parameters
        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);

        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check if the user has the permission to perform this action
        if (!AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)) {
            // If he doesn't, redirect the request to login page or report about the lack of permissions
            return ActionsHelper.produceErrorReport(this, request,
                Constants.CREATE_PROJECT_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Place the index of the active tab into the request
        request.setAttribute("projectTabIndex", 3);
        // Place the flag, indicating that we are creating a new project, into request
        request.setAttribute("newProject", Boolean.TRUE);


        getModel().set("email_notifications", Boolean.TRUE);
        getModel().set("timeline_notifications", Boolean.TRUE);

        boolean isAdmin = AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME);
        request.setAttribute("allowBillingEdit", isAdmin);
        request.setAttribute("allowCockpitProjectEdit", isAdmin);
        request.setAttribute("canEditContestPrize", true);
        request.setAttribute("canEditCheckpointPrize", true);

        // Load the look up data
        loadProjectEditLookups(request);

        // Populate the default values of some project form fields
        populateProjectFormDefaults(request);

        // Populate default last modification timestamp
        getModel().set("last_modification_time", 0L);

        // Return the success forward
        return Constants.SUCCESS_FORWARD_NAME;
    }
}
