/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectmanagementconsole;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.phases.PaymentsHelper;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.payment.ProjectPaymentAdjustment;
import com.topcoder.management.payment.ProjectPaymentAdjustmentManager;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for saving the review payments.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveReviewPaymentsAction extends BaseProjectManagementConsoleAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -6332899924251058822L;

    /**
     * Creates a new instance of the <code>SaveReviewPaymentsAction</code> class.
     */
    public SaveReviewPaymentsAction() {
    }

    /**
     * Gets the available reviewer roles which we can set the review payments for a project.
     *
     * @param project the project.
     * @return the list of role ids which we can set the review payments for the project.
     * @throws BaseException if any error occurs.
     */
    private static List<Long> getAvailableReviewerRoles(Project project) throws BaseException {
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
        Phase[] phases = ActionsHelper.getPhasesForProject(phaseManager, project);
        List<Long> roleIds = new ArrayList<Long>();
        List<String> roleNames = new ArrayList<String>();
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.SCREENING_PHASE_NAME) != null) {
            roleNames.add(Constants.PRIMARY_SCREENER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.REVIEW_PHASE_NAME) != null) {
            if (project.getProjectCategory().getName().equals("Development")) {
                roleNames.add(Constants.ACCURACY_REVIEWER_ROLE_NAME);
                roleNames.add(Constants.STRESS_REVIEWER_ROLE_NAME);
                roleNames.add(Constants.FAILURE_REVIEWER_ROLE_NAME);
            } else {
                roleNames.add(Constants.REVIEWER_ROLE_NAME);
            }
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.AGGREGATION_PHASE_NAME) != null) {
            roleNames.add(Constants.AGGREGATOR_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.FINAL_REVIEW_PHASE_NAME) != null) {
            roleNames.add(Constants.FINAL_REVIEWER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.SPECIFICATION_REVIEW_PHASE_NAME) != null) {
            roleNames.add(Constants.SPECIFICATION_REVIEWER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.CHECKPOINT_SCREENING_PHASE_NAME) != null) {
            roleNames.add(Constants.CHECKPOINT_SCREENER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.CHECKPOINT_REVIEW_PHASE_NAME) != null) {
            roleNames.add(Constants.CHECKPOINT_REVIEWER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.ITERATIVE_REVIEW_PHASE_NAME) != null) {
            roleNames.add(Constants.ITERATIVE_REVIEWER_ROLE_NAME);
        }

        ResourceRole[] allRoles = ActionsHelper.createResourceManager().getAllResourceRoles();
        for (String roleName : roleNames) {
            for (ResourceRole role : allRoles) {
                if (role.getName().equals(roleName)) {
                    roleIds.add(role.getId());
                    break;
                }
            }
        }
        return roleIds;
    }

    /**
     * <p>Processes the incoming request which is a request for saving review payments for requested project.</p>
     *
     * <p>Verifies that current user is granted access to this functionality and is granted a permission to access the
     * requested project details.</p>
     *
     * @return a <code>String</code> referencing the next view to be displayed to user.
     * @throws Exception if an unexpected error occurs.
     */
    public String execute() throws Exception {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        request.setAttribute("activeTabIdx", 4);

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
            List<Long> availableRoleIds = getAvailableReviewerRoles(project);
            Long[] roleIds = (Long[]) getModel().get("resources_roles_id");
            String[] paymentRadios = (String[]) getModel().get("resource_payments_radio");
            String[] fixed = (String[]) getModel().get("resource_payments_fixed_amount");
            String[] percentage = (String[]) getModel().get("resource_payments_percent_amount");

            for (int i = 0; i < roleIds.length; i++) {
                if (!availableRoleIds.contains(roleIds[i])) {
                    ActionsHelper.addErrorToRequest(request,
                        "resources_roles_id[" + i + "]",
                            "error.com.cronos.onlinereview.actions.manageProject.ReviewPayments.ResourceRoleProhibited",
                            roleIds[i]);
                } else {
                    if (paymentRadios[i].equals("fixed")) {
                        ActionsHelper.checkNonNegDoubleWith2Decimal(fixed[i],
                            "resource_payments_fixed_amount[" + i + "]",
                            "error.com.cronos.onlinereview.actions.manageProject.ReviewPayments.FixedAmount.Invalid",
                            "error.com.cronos.onlinereview.actions.manageProject.ReviewPayments.FixedAmount.PrecisionInvalid",
                            request, true);
                    } else if (paymentRadios[i].equals("percentage")) {
                        Integer numPer = null;
                        try {
                            numPer = Integer.valueOf(percentage[i]);
                        } catch (NumberFormatException e) {}
                        if (numPer == null || numPer <= 0) {
                            ActionsHelper.addErrorToRequest(request,
                                "resource_payments_percent_amount[" + i + "]",
                                "error.com.cronos.onlinereview.actions.manageProject.ReviewPayments.Percentage.Invalid");
                        }
                    }
                }
            }

            if (ActionsHelper.isErrorsPresent(request)) {
                initProjectManagementConsole(request, project);
                return INPUT;
            } else {
                ProjectPaymentAdjustmentManager adjustmentManager = ActionsHelper.createProjectPaymentAdjustmentManager();
                Map<Long, ProjectPaymentAdjustment> existingAdjs = new HashMap<Long, ProjectPaymentAdjustment>();
                for (ProjectPaymentAdjustment adj : adjustmentManager.retrieveByProjectId(project.getId())) {
                    existingAdjs.put(adj.getResourceRoleId(), adj);
                }
                for (int i = 0; i < roleIds.length; i++) {
                    ProjectPaymentAdjustment adjustment = new ProjectPaymentAdjustment();
                    adjustment.setProjectId(project.getId());
                    adjustment.setResourceRoleId(roleIds[i]);
                    if (paymentRadios[i].equals("fixed")) {
                        adjustment.setFixedAmount(new BigDecimal(fixed[i]));
                    } else if (paymentRadios[i].equals("percentage")) {
                        adjustment.setMultiplier(Double.valueOf(percentage[i]) / 100.0);
                    } else {
                        // default
                        if (!existingAdjs.containsKey(roleIds[i])) {
                            adjustment = null;
                        }
                    }
                    if (adjustment != null) {
                        adjustmentManager.save(adjustment);
                    }
                }
                String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
                PaymentsHelper.processAutomaticPayments(project.getId(), operator);
                this.setPid(project.getId());
                return Constants.SUCCESS_FORWARD_NAME;
            }
        }
    }
}

