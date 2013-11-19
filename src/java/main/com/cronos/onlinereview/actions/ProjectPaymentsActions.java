/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.cronos.onlinereview.forms.ProjectPaymentsForm;
import com.cronos.onlinereview.forms.ProjectPaymentsForm.ResourcePayments;
import com.cronos.onlinereview.phases.PaymentsHelper;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.ProjectPaymentManager;
import com.topcoder.management.payment.ProjectPaymentType;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.util.errorhandling.BaseException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class contains Struts Actions that are meant to deal with Project Payments. There are following
 * Actions defined in this class:
 * <ul>
 *     <li>View Project Payments</li>
 *     <li>Edit Project Payments</li>
 *     <li>Save Project Payments</li>
 * </ul>
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * <p>
 * Version 1.1 (Online Review - Project Payments Integration Part 3 v1.0) Change notes:
 *   <ol>
 *       <li>Updated {@link #saveProjectPayments(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *       method to call PaymentsHelper.processAutomaticPayments and PaymentsHelper.updateProjectResultPayments to
 *       process automatic payments.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2 (Online Review - Iterative Review v1.0) Change notes:
 *   <ol>
 *       <li>Added iterative reviewer to reviewer list.</li>
 *   </ol>
 * </p>
 *
 * @author flexme, duxiaoyang
 * @version 1.2
 * @since 1.0
 */
public class ProjectPaymentsActions extends DispatchAction {
    /**
     * The submitter resource roles.
     */
    private static final Set<String> SUBMITTER_ROLES = new HashSet<String>(Arrays.asList(Constants.SUBMITTER_ROLE_NAME));
    /**
     * The reviewer resource roles.
     */
    private static final Set<String> REVIEWER_ROLES = new HashSet<String>(Arrays.asList(
            Constants.SPECIFICATION_REVIEWER_ROLE_NAME,
            Constants.CHECKPOINT_SCREENER_ROLE_NAME,
            Constants.CHECKPOINT_REVIEWER_ROLE_NAME,
            Constants.PRIMARY_SCREENER_ROLE_NAME,
            Constants.REVIEWER_ROLE_NAME,
            Constants.ACCURACY_REVIEWER_ROLE_NAME,
            Constants.FAILURE_REVIEWER_ROLE_NAME,
            Constants.STRESS_REVIEWER_ROLE_NAME,
            Constants.AGGREGATOR_ROLE_NAME,
            Constants.FINAL_REVIEWER_ROLE_NAME,
            Constants.POST_MORTEM_REVIEWER_ROLE_NAME,
            Constants.ITERATIVE_REVIEWER_ROLE_NAME));
    /**
     * The copilot resource roles.
     */
    private static final Set<String> COPILOT_ROLES = new HashSet<String>(Arrays.asList(Constants.COPILOT_ROLE_NAME));
    /**
     * The allowed project payment type ids of submitter.
     */
    private static final List<Long> SUBMITTER_PROJECT_PAYMENT_TYPES = Arrays.asList(
            Constants.CONTEST_PAYMENT_TYPE_ID,
            Constants.CONTEST_CHECKPOINT_PAYMENT_TYPE_ID);
    /**
     * The allowed project payment type ids of reviewer.
     */
    private static final List<Long> REVIEWER_PROJECT_PAYMENT_TYPES = Arrays.asList(Constants.REVIEW_PAYMENT_TYPE_ID);
    /**
     * The allowed project payment type ids of copilot.
     */
    private static final List<Long> COPILOT_PROJECT_PAYMENT_TYPES = Arrays.asList(Constants.COPILOT_PAYMENT_TYPE_ID);

    /**
     * Creates a new instance of the <code>ProjectPaymentsActions</code> class.
     */
    public ProjectPaymentsActions() {
    }

    /**
     * This method is an implementation of &quot;View Project Payments&quot; Struts Action defined
     * for this assembly, which is supposed to gather all possible information about the project payments and
     * display it to user.
     *
     * @return an action forward to the appropriate page. If no error has occurred, the forward will
     *         be to viewProjectPayments.jsp page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewProjectPayments(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse response)
            throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(
                false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                Constants.VIEW_PAYMENTS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
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

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Edit Project Payments&quot; Struts Action defined for this
     * assembly, which is supposed to fetch all possible information about project payments and pass it to
     * the JSP page for user editing the project payments.
     *
     * @return &quot;success&quot; forward that forwards to the /jsp/editProjectPayments.jsp page (as
     *         defined in struts-config.xml file) in the case of successful processing,
     *         &quot;notAuthorized&quot; forward in the case of user not being authorized to perform
     *         the action.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward editProjectPayments(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse response)
            throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(
                false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                Constants.EDIT_PAYMENTS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        long projectId = verification.getProject().getId();
        ProjectPaymentManager projectPaymentManager = ActionsHelper.createProjectPaymentManager();
        List<ProjectPayment> payments = projectPaymentManager.search(
                ProjectPaymentFilterBuilder.createProjectIdFilter(projectId));
        Resource[] resources = ActionsHelper.getAllResourcesForProject(verification.getProject());

        populateCommonAttributes(request, resources, payments);
        populatePaymentsForm(request, verification.getProject(), (ProjectPaymentsForm) form, payments);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Save Project Payments&quot; Struts Action defined for this
     * assembly, which is supposed to save the project payments data submitted by the end user to database.
     *
     * @return &quot;success&quot; forward that forwards to view project payments details page (as
     *         defined in struts-config.xml file) in the case of successful processing,
     *         &quot;input&quot; forward in the case of the form data submitted by end user is invalid,
     *         &quot;notAuthorized&quot; forward in the case of user not being authorized to perform
     *         the action.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward saveProjectPayments(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse response)
            throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(
                false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                Constants.EDIT_PAYMENTS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        ProjectPaymentsForm paymentsForm = (ProjectPaymentsForm) form;
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
                paymentsForm.getSubmitterPayments(), existingPayments, SUBMITTER_PROJECT_PAYMENT_TYPES,
                true, toBeCreated, toBeUpdated, toBeRemoved);
        if (ActionsHelper.isErrorsPresent(request)) {
            tabName = "submitters";
        }
        validateFormPayments(request, "reviewerPayments", originalForm.getReviewerPayments(),
                paymentsForm.getReviewerPayments(), existingPayments, REVIEWER_PROJECT_PAYMENT_TYPES,
                false, toBeCreated, toBeUpdated, toBeRemoved);
        if (tabName == null && ActionsHelper.isErrorsPresent(request)) {
            tabName = "reviewers";
        }
        validateFormPayments(request, "copilotPayments", originalForm.getCopilotPayments(),
                paymentsForm.getCopilotPayments(), existingPayments, COPILOT_PROJECT_PAYMENT_TYPES,
                false, toBeCreated, toBeUpdated, toBeRemoved);
        if (tabName == null && ActionsHelper.isErrorsPresent(request)) {
            tabName = "copilots";
        }

        // Check if there are any validation errors and return appropriate forward
        if (ActionsHelper.isErrorsPresent(request)) {
            request.setAttribute("tabName", tabName);
            return mapping.getInputForward();
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
        updateResourceProperties(resourceManager, operator, paymentsForm.getSubmitterPayments(), allResources);
        updateResourceProperties(resourceManager, operator, paymentsForm.getReviewerPayments(), allResources);
        updateResourceProperties(resourceManager, operator, paymentsForm.getCopilotPayments(), allResources);

        PaymentsHelper.processAutomaticPayments(projectId, operator);
        PaymentsHelper.updateProjectResultPayments(projectId);

        // Return success forward
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + projectId);
    }

    /**
     * Populate the resources to request attribute by resource role. Currently only "submitter", "reviewer",
     * and "copilot" will be populated.
     *
     * @param request the http request.
     * @param resources all the project resources.
     * @throws BaseException if any error occurs.
     */
    private static void populateResourcesByRole(HttpServletRequest request, Resource[] resources)
            throws BaseException {
        List<Resource> submitters = getResourcesByRoles(resources, SUBMITTER_ROLES);
        populateSubmitterSubmissions(request, submitters);
        Map<Long, List<Long>> resourceContestSubmissions =
                (Map<Long, List<Long>>) request.getAttribute("resourceContestSubmissions");
        Map<Long, List<Long>> resourceCheckpointSubmissions =
                (Map<Long, List<Long>>) request.getAttribute("resourceCheckpointSubmissions");

        // remove the submitters who has no submissions
        List<Resource> newSubmitters = new ArrayList<Resource>();
        for (Resource resource : submitters) {
            if (resourceContestSubmissions.containsKey(resource.getId())
                    || resourceCheckpointSubmissions.containsKey(resource.getId())) {
                newSubmitters.add(resource);
            }
        }

        request.setAttribute("submitters", newSubmitters);
        request.setAttribute("reviewers", getResourcesByRoles(resources, REVIEWER_ROLES));
        request.setAttribute("copilots", getResourcesByRoles(resources, COPILOT_ROLES));
    }

    /**
     * Gets the resources whose role belongs to the specific allowed role set.
     *
     * @param resources all the project resources.
     * @param roles the allowed resource role set.
     * @return the resources whose role belongs to the allowed role set.
     */
    private static List<Resource> getResourcesByRoles(Resource[] resources, Set<String> roles) {
        List<Resource> result = new ArrayList<Resource>();
        for (Resource resource : resources) {
            if (roles.contains(resource.getResourceRole().getName())) {
                result.add(resource);
            }
        }
        return result;
    }

    /**
     * Populate all the project payments information into the action form <code>ProjectPaymentsForm</code>.
     *
     * @param request the http request.
     * @param project the project.
     * @param form the instance of the action form.
     * @param payments all the project payments.
     */
    private static void populatePaymentsForm(HttpServletRequest request, Project project,
                                             ProjectPaymentsForm form, List<ProjectPayment> payments) {
        form.setPid(project.getId());
        form.setSubmitterPayments(getResourcePayments(payments, (List<Resource>) request.getAttribute("submitters")));
        form.setReviewerPayments(getResourcePayments(payments, (List<Resource>) request.getAttribute("reviewers")));
        form.setCopilotPayments(getResourcePayments(payments, (List<Resource>) request.getAttribute("copilots")));
    }

    /**
     * Gets the list of <code>ProjectPayment</code> instances for the list of <code>Resource</code>s.
     *
     * @param payments a <code>List</code> providing all the project payments.
     * @param resources the list of <code>Resource</code>s.
     * @return a list of <code>ResourcePayments</code> which will be filled into the action
     *      form <code>ProjectPaymentsForm</code>, the size of which will be equal to the size of parameter
     *      resources, every entry of which contains the project payments of the corresponding resource in resources.
     */
    public static List<ResourcePayments> getResourcePayments(List<ProjectPayment> payments, List<Resource> resources) {
        List<ResourcePayments> resourcesPayments = new ArrayList<ResourcePayments>();
        for (Resource resource : resources) {
            List<ProjectPayment> resourcePayments = new ArrayList<ProjectPayment>();
            for (ProjectPayment payment : payments) {
                if (payment.getResourceId() == resource.getId()) {
                    resourcePayments.add(payment);
                }
            }

            List<String> paymentTypes = new ArrayList<String>();
            List<String> paymentIds = new ArrayList<String>();
            List<String> submissionIds = new ArrayList<String>();
            List<String> amounts = new ArrayList<String>();
            String manual = (String) resource.getProperty("Manual Payments");
            Boolean automatic = !("true".equalsIgnoreCase(manual));

            for (ProjectPayment payment : resourcePayments) {
                paymentTypes.add(payment.getProjectPaymentType().getProjectPaymentTypeId().toString());
                paymentIds.add(payment.getProjectPaymentId().toString());
                submissionIds.add(payment.getSubmissionId() == null ? null : payment.getSubmissionId().toString());
                amounts.add(payment.getAmount().toString());
            }

            ResourcePayments paymentsOfResource = new ResourcePayments();
            paymentsOfResource.setAutomatic(automatic);
            paymentsOfResource.setResourceId(resource.getId());
            paymentsOfResource.setPaymentTypes(paymentTypes);
            paymentsOfResource.setPaymentIds(paymentIds);
            paymentsOfResource.setSubmissionIds(submissionIds);
            paymentsOfResource.setAmounts(amounts);
            resourcesPayments.add(paymentsOfResource);
        }
        return resourcesPayments;
    }

    /**
     * Populate the mapping of resource id to resource properties to request attribute. Currently only resource roles
     * and resource user ids will be populated.
     *
     * @param request the http request.
     * @param resources all the project resources.
     */
    private static void populateResourcesMap(HttpServletRequest request, Resource[] resources) {
        Map<Long, String> resourceRoles = new HashMap<Long, String>();
        Map<Long, Long> resourceUserIds = new HashMap<Long, Long>();
        for (Resource resource : resources) {
            resourceRoles.put(resource.getId(), resource.getResourceRole().getName());
            resourceUserIds.put(resource.getId(), Long.valueOf((String) resource.getProperty("External Reference ID")));
        }
        request.setAttribute("resourceRoles", resourceRoles);
        request.setAttribute("resourceUserIds", resourceUserIds);
    }

    /**
     * Populate the submitters' submissions to request attribute.
     *
     * @param request the http request.
     * @param submitterResources all the submitter resources.
     * @throws BaseException if any error occurs.
     */
    private static void populateSubmitterSubmissions(HttpServletRequest request, List<Resource> submitterResources)
            throws BaseException {
        Map<Long, List<Long>> resourceContestSubmissions = new HashMap<Long, List<Long>>();
        Map<Long, List<Long>> resourceCheckpointSubmissions = new HashMap<Long, List<Long>>();
        for (Resource resource : submitterResources) {
            // Get all non-deleted submission
            Submission[] submissions = ActionsHelper.getResourceSubmissions(resource.getId(), null, null, false);
            for (Submission sub : submissions) {
                Map<Long, List<Long>> ref;
                if (sub.getSubmissionType().getName().equalsIgnoreCase(Constants.CONTEST_SUBMISSION_TYPE_NAME)) {
                    ref = resourceContestSubmissions;
                } else if (sub.getSubmissionType().getName().equals(Constants.CHECKPOINT_SUBMISSION_TYPE_NAME)) {
                    ref = resourceCheckpointSubmissions;
                } else {
                    continue;
                }
                if (!ref.containsKey(resource.getId())) {
                    ref.put(resource.getId(), new ArrayList<Long>());
                }
                ref.get(resource.getId()).add(sub.getId());
            }
        }
        request.setAttribute("resourceContestSubmissions", resourceContestSubmissions);
        request.setAttribute("resourceCheckpointSubmissions", resourceCheckpointSubmissions);
    }

    /**
     * Populate the common attributes which will be used for editing/saving project payments to http request.
     *
     * @param request the http request.
     * @param resources all the project resources.
     * @param payments all the project payments.
     * @throws BaseException if any error occurs.
     */
    private static void populateCommonAttributes(HttpServletRequest request, Resource[] resources,
                                                 List<ProjectPayment> payments) throws BaseException {
        populateResourcesMap(request, resources);
        populateResourcesByRole(request, resources);

        Map<String, Boolean> paymentsPaid = new HashMap<String, Boolean>();
        for (ProjectPayment payment : payments) {
            paymentsPaid.put(payment.getProjectPaymentId().toString(), payment.getPactsPaymentId() != null);
        }
        request.setAttribute("paymentsPaid", paymentsPaid);
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
}
