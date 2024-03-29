/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectmanagementconsole;


import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.exception.BaseException;

/**
 * This class is the struts action class which is used for viewing the management console page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ViewManagementConsoleAction extends BaseProjectManagementConsoleAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -8763791077599967611L;

    /**
     * Creates a new instance of the <code>ViewManagementConsoleAction</code> class.
     */
    public ViewManagementConsoleAction() {
    }

    /**
     * <p>Processes the incoming request which is a request for viewing the <code>Project Management Console</code> view
     * for requested project.</p>
     *
     * <p>Verifies that current user is granted access to this functionality and is granted a permission to access the
     * requested project details.</p>
     *
     * @return a <code>String</code> referencing the next view to be displayed to user.
     * @throws BaseException if an unexpected error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. Also check that current user is granted a
        // permission to access the details for requested project
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(this, request,
                Constants.VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME, false);

        if (!verification.isSuccessful()) {
            // If not then redirect the request to log-in page or report about the lack of permissions.
            return verification.getResult();
        } else {
            // User is granted appropriate permissions - set the list of available roles for resources and flags
            // affecting the Extend Registration/Submission Phase functionality
            Project project = verification.getProject();
            initProjectManagementConsole(request, project);

            request.setAttribute("projectStatus", project.getProjectStatus().getName());

            final String projectTypeName = project.getProjectCategory().getProjectType().getName();

            boolean hasForumType = project.getAllProperties().containsKey("Forum Type");

            long projectId = project.getId();
            long forumId = -1;
            String tempStr;

            tempStr = (String) project.getProperty("Developer Forum ID");
            if (tempStr != null && tempStr.trim().length() != 0) {
                forumId = Long.parseLong(tempStr, 10);
            }

            request.setAttribute("viewContestLink", ConfigHelper.getProjectTypeViewContestLink(projectTypeName, projectId));

            request.setAttribute("forumLink", ConfigHelper.getProjectTypeForumLink(
                (projectTypeName.equalsIgnoreCase("studio") && hasForumType) ? "NewStudio" : projectTypeName, forumId));
            request.setAttribute("isAllowedToContactPM",
                AuthorizationHelper.hasUserPermission(request, Constants.CONTACT_PM_PERM_NAME));

            return Constants.SUCCESS_FORWARD_NAME;
        }
    }
}

