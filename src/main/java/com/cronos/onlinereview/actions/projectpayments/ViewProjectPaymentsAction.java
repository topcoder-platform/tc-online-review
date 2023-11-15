/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectpayments;

import java.util.Collections;
import java.util.List;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.Comparators;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.payment.ProjectPayment;
import com.topcoder.onlinereview.component.project.payment.ProjectPaymentManager;
import com.topcoder.onlinereview.component.project.payment.ProjectPaymentFilterBuilder;
import com.topcoder.onlinereview.component.resource.Resource;
import com.topcoder.onlinereview.component.exception.BaseException;

/**
 * This class is the struts action class which is used for viewing the project payments page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ViewProjectPaymentsAction extends BaseProjectPaymentAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 7443644705618093480L;

    /**
     * Creates a new instance of the <code>ViewProjectPaymentsAction</code> class.
     */
    public ViewProjectPaymentsAction() {
    }

    /**
     * This method is an implementation of &quot;View Project Payments&quot; Struts Action defined
     * for this assembly, which is supposed to gather all possible information about the project payments and
     * display it to user.
     *
     * @return a string forward to the appropriate page. If no error has occurred, the forward will
     *         be to viewProjectPayments.jsp page.
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(this, request,
                Constants.VIEW_PAYMENTS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        Project project = verification.getProject();

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
        ProjectPaymentManager projectPaymentManager = ActionsHelper.createProjectPaymentManager();
        List<ProjectPayment> payments = projectPaymentManager.search(
                ProjectPaymentFilterBuilder.createProjectIdFilter(projectId));
        Collections.sort(payments, new Comparators.ProjectPaymentComparator());

        Resource[] resources = ActionsHelper.getAllResourcesForProject(verification.getProject());
        populateResourcesMap(request, resources);
        request.setAttribute("payments", payments);
        request.setAttribute("isAllowedToContactPM",
                AuthorizationHelper.hasUserPermission(request, Constants.CONTACT_PM_PERM_NAME));
        boolean isAllowedToEditPayments = false;
        if (project.getProjectStatus().getName() == Constants.COMPLETED_PROJECT_STATUS_NAME) {
            AuthorizationHelper.gatherUserJwtRoles(request);
            isAllowedToEditPayments = AuthorizationHelper.hasUserJwtPermission(request,
                    Constants.EDIT_PAYMENTS_COMPLETED_PROJECT_PERM_NAME);

        } else {
            isAllowedToEditPayments = AuthorizationHelper.hasUserPermission(request,
                    Constants.EDIT_PAYMENTS_PERM_NAME);
        }
        request.setAttribute("isAllowedToEditPayments", isAllowedToEditPayments);
        request.setAttribute("pactsPaymentDetailBaseURL", ConfigHelper.getPactsPaymentDetailBaseURL());

        return Constants.SUCCESS_FORWARD_NAME;
    }
}

