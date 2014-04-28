/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectpayments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.model.ProjectPaymentsForm;
import com.cronos.onlinereview.model.ProjectPaymentsForm.ResourcePayments;
import com.cronos.onlinereview.phases.PaymentsHelper;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.ProjectPaymentManager;
import com.topcoder.management.payment.ProjectPaymentType;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for saving project payments page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveProjectPaymentsAction extends BaseProjectPaymentAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -748043655031719342L;
    /**
     * Represents the project id.
     */
    private long pid;

    /**
     * Creates a new instance of the <code>SaveProjectPaymentsAction</code> class.
     */
    public SaveProjectPaymentsAction() {
    }

    /**
     * This method is an implementation of &quot;Save Project Payments&quot; Struts Action defined for this
     * assembly, which is supposed to save the project payments data submitted by the end user to database.
     *
     * @return &quot;success&quot; string result that forwards to view project payments details page (as
     *         defined in struts-config.xml file) in the case of successful processing,
     *         &quot;input&quot; forward in the case of the form data submitted by end user is invalid,
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

        Map<Long, ProjectPayment> existingPayments = new HashMap<Long, ProjectPayment>();
        for (ProjectPayment payment : payments) {
            existingPayments.put(payment.getProjectPaymentId(), payment);
        }

        ProjectPaymentsForm originalForm = new ProjectPaymentsForm();
        populatePaymentsForm(request, verification.getProject(), originalForm, payments);

        List<ProjectPayment> toBeCreated = new ArrayList<ProjectPayment>();
        List<ProjectPayment> toBeUpdated = new ArrayList<ProjectPayment>();
        List<Long> toBeRemoved = new ArrayList<Long>();

        String tabName = null;
        validateFormPayments(request, "submitterPayments", originalForm.getSubmitterPayments(),
                getModel().getSubmitterPayments(), existingPayments, SUBMITTER_PROJECT_PAYMENT_TYPES,
                true, toBeCreated, toBeUpdated, toBeRemoved);
        if (ActionsHelper.isErrorsPresent(request)) {
            tabName = "submitters";
        }
        validateFormPayments(request, "reviewerPayments", originalForm.getReviewerPayments(),
                getModel().getReviewerPayments(), existingPayments, REVIEWER_PROJECT_PAYMENT_TYPES,
                false, toBeCreated, toBeUpdated, toBeRemoved);
        if (tabName == null && ActionsHelper.isErrorsPresent(request)) {
            tabName = "reviewers";
        }
        validateFormPayments(request, "copilotPayments", originalForm.getCopilotPayments(),
                getModel().getCopilotPayments(), existingPayments, COPILOT_PROJECT_PAYMENT_TYPES,
                false, toBeCreated, toBeUpdated, toBeRemoved);
        if (tabName == null && ActionsHelper.isErrorsPresent(request)) {
            tabName = "copilots";
        }

        // Check if there are any validation errors and return appropriate forward
        if (ActionsHelper.isErrorsPresent(request)) {
            request.setAttribute("tabName", tabName);
            return INPUT;
        }

        // update the payments to DB
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        for (ProjectPayment payment : toBeCreated) {
            projectPaymentManager.create(payment, operator);
        }
        for (ProjectPayment payment : toBeUpdated) {
            projectPaymentManager.update(payment, operator);
        }
        for (Long paymentId : toBeRemoved) {
            projectPaymentManager.delete(paymentId);
        }

        // update resources "Manual Payments" property
        ResourceManager resourceManager = ActionsHelper.createResourceManager();
        Map<Long, Resource> allResources = new HashMap<Long, Resource>();
        for (Resource resource : resources) {
            allResources.put(resource.getId(), resource);
        }
        updateResourceProperties(resourceManager, operator, getModel().getSubmitterPayments(), allResources);
        updateResourceProperties(resourceManager, operator, getModel().getReviewerPayments(), allResources);
        updateResourceProperties(resourceManager, operator, getModel().getCopilotPayments(), allResources);

        PaymentsHelper.processAutomaticPayments(projectId, operator);
        PaymentsHelper.updateProjectResultPayments(projectId);

        setPid(projectId);
        // Return success forward
        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * Update the resources' &quot;Manual Payments&quot; properties.
     *
     * @param resourceManager the instance of <code>ResourceManager</code>.
     * @param operator the operator.
     * @param resourcePayments the list of <code>ResourcePayments</code> instances containing the
     *                         resources which need to be updated.
     * @param allResources the mapping from resource id to <code>Resource</code> instance.
     * @throws BaseException if any error occurs.
     */
    private static void updateResourceProperties(ResourceManager resourceManager, String operator,
                                                 List<ResourcePayments> resourcePayments,
                                                 Map<Long, Resource> allResources)
        throws BaseException {
        for (ResourcePayments payments : resourcePayments) {
            Resource resource = allResources.get(payments.getResourceId());
            boolean isAutomatic = !("true".equalsIgnoreCase((String) resource.getProperty("Manual Payments")));
            if (payments.getAutomatic() != isAutomatic) {
                resource.setProperty("Manual Payments", payments.getAutomatic() ? "false" : "true");
                resourceManager.updateResource(resource, operator);
            }
        }
    }

    /**
     * This method will validate the form data submitted by end user, also it will gather all the project payments
     * which will be created, updated and removed.
     *
     * @param request the http request.
     * @param property the form property prefix.
     * @param originalResourcePayments original resource payments.
     * @param resourcePayments the resource payments submitted by end user.
     * @param existingPayments the mapping from project payment id to <code>ProjectPayment</code> instance.
     * @param allowedPaymentTypes the allowed payment types.
     * @param isSubmitter true if the resources are submitters, false otherwise.
     * @param toBeCreated all the <code>ProjectPayment</code>s which should be created will be added to this list.
     * @param toBeUpdated all the <code>ProjectPayment</code>s which should be updated will be added to this list.
     * @param toBeRemoved all the project payment ids which should be removed will be added to this list.
     */
    private static void validateFormPayments(HttpServletRequest request, String property,
                                             List<ResourcePayments> originalResourcePayments,
                                             List<ResourcePayments> resourcePayments,
                                             Map<Long, ProjectPayment> existingPayments,
                                             List<Long> allowedPaymentTypes, boolean isSubmitter,
                                             List<ProjectPayment> toBeCreated,
                                             List<ProjectPayment> toBeUpdated,
                                             List<Long> toBeRemoved) {
        // resource -> payment ids of this resource
        Map<Long, Set<Long>> resourceExistingPaymentIds = new HashMap<Long, Set<Long>>();
        for (ResourcePayments resourcePayment : originalResourcePayments) {
            resourceExistingPaymentIds.put(resourcePayment.getResourceId(),
                    new HashSet<Long>(toLongList(resourcePayment.getPaymentIds())));
        }

        Map<Long, List<Long>> resourceContestSubmissions =
                (Map<Long, List<Long>>) request.getAttribute("resourceContestSubmissions");
        Map<Long, List<Long>> resourceCheckpointSubmissions =
                (Map<Long, List<Long>>) request.getAttribute("resourceCheckpointSubmissions");
        for (int i = 0; i < resourcePayments.size(); i++) {
            String propertyPrefix = property + "[" + i + "].";

            ResourcePayments resourcePayment = resourcePayments.get(i);
            Long resourceId = resourcePayment.getResourceId();
            boolean resourceIdValid = true;
            if (!resourceExistingPaymentIds.containsKey(resourceId)) {
                // resource id is invalid
                ActionsHelper.addErrorToRequest(request, propertyPrefix + "resourceId",
                        "error.com.cronos.onlinereview.actions.editPayments.ResourceId.Invalid");
                resourceIdValid = false;
            }

            if (resourcePayment.getAutomatic()) {
                // automatic, skip the payments
                continue;
            }

            // check payment types
            List<Long> paymentTypes = toLongList(resourcePayment.getPaymentTypes());
            for (int j = 0; j < paymentTypes.size(); j++) {
                if (!allowedPaymentTypes.contains(paymentTypes.get(j))) {
                    // payment type is invalid
                    ActionsHelper.addErrorToRequest(request, propertyPrefix + "paymentTypes[" + j + "]",
                            "error.com.cronos.onlinereview.actions.editPayments.PaymentType.Invalid");
                }
                if (isSubmitter) {
                    List<Long> subIds = paymentTypes.get(j).equals(Constants.CONTEST_PAYMENT_TYPE_ID) ?
                            resourceContestSubmissions.get(resourceId) : resourceCheckpointSubmissions.get(resourceId);
                    if (subIds == null) {
                        // payment type is invalid
                        ActionsHelper.addErrorToRequest(request, propertyPrefix + "paymentTypes[" + j + "]",
                                "error.com.cronos.onlinereview.actions.editPayments.PaymentType.Invalid");
                    }
                }
            }

            // check payment amounts
            List<String> amounts = resourcePayment.getAmounts();
            for (int j = 0; j < amounts.size(); j++) {
                ActionsHelper.checkNonNegDoubleWith2Decimal(amounts.get(j), propertyPrefix + "amounts[" + j + "]",
                        "error.com.cronos.onlinereview.actions.editPayments.amount.Invalid",
                        "error.com.cronos.onlinereview.actions.editPayments.amount.PrecisionInvalid", request, false);
            }

            // check submission ids
            List<Long> subIds = toLongList(resourcePayment.getSubmissionIds());
            for (int j = 0; j < subIds.size(); j++) {
                Long subId = subIds.get(j);
                if (isSubmitter) {
                    if (subId == null || subId == 0) {
                        // submission id is required
                        ActionsHelper.addErrorToRequest(request, propertyPrefix + "submissionIds[" + j + "]",
                                "error.com.cronos.onlinereview.actions.editPayments.SubmissionId.required");
                    } else {
                        List<Long> allowedIds = paymentTypes.get(j).equals(Constants.CONTEST_PAYMENT_TYPE_ID) ?
                                resourceContestSubmissions.get(resourceId) :
                                resourceCheckpointSubmissions.get(resourceId);
                        if (allowedIds != null && !allowedIds.contains(subId)) {
                            // submission id is invalid
                            ActionsHelper.addErrorToRequest(request, propertyPrefix + "submissionIds[" + j + "]",
                                    "error.com.cronos.onlinereview.actions.editPayments.SubmissionId.Invalid");
                        }
                    }
                }
            }

            if (!resourceIdValid) {
                // resourceId is not valid, no need to check it's payments
                continue;
            }
            // check payment ids
            List<Long> paymentIds = toLongList(resourcePayment.getPaymentIds());
            Set<Long> existingPaymentIds = resourceExistingPaymentIds.get(resourceId);
            Set<Long> updated = new HashSet<Long>();
            for (int j = 0; j < paymentIds.size(); j++) {
                Long paymentId = paymentIds.get(j);
                if (paymentId != null && paymentId > 0 && !existingPaymentIds.contains(paymentId)) {
                    // payment id is invalid
                    ActionsHelper.addErrorToRequest(request, propertyPrefix + "paymentIds[" + j + "]",
                            "error.com.cronos.onlinereview.actions.editPayments.PaymentId.Invalid");
                } else {
                    if (paymentId == null || paymentId == 0) {
                        // new
                        if (!ActionsHelper.isErrorsPresent(request)) {
                            ProjectPayment payment = new ProjectPayment();
                            payment.setResourceId(resourceId);
                            ProjectPaymentType type = new ProjectPaymentType();
                            type.setProjectPaymentTypeId(paymentTypes.get(j));
                            payment.setProjectPaymentType(type);
                            payment.setAmount(new BigDecimal(amounts.get(j)));
                            if (isSubmitter) {
                                payment.setSubmissionId(subIds.get(j));
                            }
                            toBeCreated.add(payment);
                        }
                    } else {
                        // update
                        ProjectPayment payment = existingPayments.get(paymentId);
                        if (payment.getPactsPaymentId() == null && !ActionsHelper.isErrorsPresent(request)) {
                            // only update payment if it's not paid
                            ProjectPaymentType type = new ProjectPaymentType();
                            type.setProjectPaymentTypeId(paymentTypes.get(j));
                            payment.setProjectPaymentType(type);
                            payment.setAmount(new BigDecimal(amounts.get(j)));
                            if (isSubmitter) {
                                payment.setSubmissionId(subIds.get(j));
                            }
                            toBeUpdated.add(payment);
                        }
                        updated.add(paymentId);
                    }
                }
            }
            existingPaymentIds.removeAll(updated);
            // now existingPaymentIds contains the payments which should be removed
            for (Long paymentId : existingPaymentIds) {
                if (existingPayments.get(paymentId).getPactsPaymentId() != null) {
                    // paid payment can't be removed
                    ActionsHelper.addErrorToRequest(request, propertyPrefix + "resourceId",
                            "error.com.cronos.onlinereview.actions.editPayments.Payments.RemovePaid");
                    break;
                }
            }
            toBeRemoved.addAll(existingPaymentIds);
        }
    }

    /**
     * Convert a list of <code>String</code> to a list of <code>Long</code>. Null of empty string will remain null.
     *
     * @param values the list of <code>String</code> to be converted.
     * @return the converted result.
     */
    private static List<Long> toLongList(List<String> values) {
        List<Long> result = new ArrayList<Long>(values.size());
        for (String value : values) {
            if (value != null && value.trim().length() > 0) {
                result.add(Long.valueOf(value));
            } else {
                result.add(null);
            }
        }
        return result;
    }

    /**
     * Getter of pid.
     * @return the pid
     */
    public long getPid() {
        return pid;
    }

    /**
     * Setter of pid.
     * @param pid the pid to set
     */
    public void setPid(long pid) {
        this.pid = pid;
    }
}

