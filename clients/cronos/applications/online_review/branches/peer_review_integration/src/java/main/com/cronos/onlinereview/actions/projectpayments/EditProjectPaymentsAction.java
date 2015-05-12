/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectpayments;

import java.util.List;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.ProjectPaymentManager;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import com.topcoder.management.resource.Resource;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for rendering the editing project payments page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EditProjectPaymentsAction extends BaseProjectPaymentAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -155908769369445859L;

    /**
     * Creates a new instance of the <code>EditProjectPaymentsAction</code> class.
     */
    public EditProjectPaymentsAction() {
    }

    /**
     * This method is an implementation of &quot;Edit Project Payments&quot; Struts Action defined for this
     * assembly, which is supposed to fetch all possible information about project payments and pass it to
     * the JSP page for user editing the project payments.
     *
     * @return &quot;success&quot; string result that forwards to the /jsp/editProjectPayments.jsp page (as
     *         defined in struts-config.xml file) in the case of successful processing,
     *         &quot;notAuthorized&quot; forward in the case of user not being authorized to perform
     *         the action.
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
                Constants.EDIT_PAYMENTS_PERM_NAME, false);
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
        Resource[] resources = ActionsHelper.getAllResourcesForProject(verification.getProject());

        populateCommonAttributes(request, resources, payments);
        populatePaymentsForm(request, verification.getProject(), getModel(), payments);

        return Constants.SUCCESS_FORWARD_NAME;
    }
}

