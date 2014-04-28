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
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.ProjectPaymentManager;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import com.topcoder.management.resource.Resource;
import com.topcoder.util.errorhandling.BaseException;

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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(
                false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(this, request,
                Constants.VIEW_PAYMENTS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        long projectId = verification.getProject().getId();
        ProjectPaymentManager projectPaymentManager = ActionsHelper.createProjectPaymentManager();
        List<ProjectPayment> payments = projectPaymentManager.search(
                ProjectPaymentFilterBuilder.createProjectIdFilter(projectId));
        Collections.sort(payments, new Comparators.ProjectPaymentComparator());

        Resource[] resources = ActionsHelper.getAllResourcesForProject(verification.getProject());
        populateResourcesMap(request, resources);
        request.setAttribute("payments", payments);
        request.setAttribute("isAllowedToEditPayments",
                AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PAYMENTS_PERM_NAME));
        request.setAttribute("pactsPaymentDetailBaseURL", ConfigHelper.getPactsPaymentDetailBaseURL());

        return Constants.SUCCESS_FORWARD_NAME;
    }
}

