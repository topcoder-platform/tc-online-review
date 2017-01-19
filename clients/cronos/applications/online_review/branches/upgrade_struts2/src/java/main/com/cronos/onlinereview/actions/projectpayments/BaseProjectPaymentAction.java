/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectpayments;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.actions.BaseServletAwareAction;
import com.cronos.onlinereview.model.ProjectPaymentsForm;
import com.cronos.onlinereview.model.ProjectPaymentsForm.ResourcePayments;
import com.cronos.onlinereview.util.ActionsHelper;
import com.opensymphony.xwork2.ModelDriven;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This is the base class for project payment actions classes.
 * It provides the basic functions which will be used by all project payments actions.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseProjectPaymentAction extends BaseServletAwareAction implements ModelDriven<ProjectPaymentsForm> {
    /**
     * The copilot resource roles.
     */
    protected static final Set<String> COPILOT_ROLES = new HashSet<String>(Arrays.asList(Constants.COPILOT_ROLE_NAME));
    /**
     * The allowed project payment type ids of submitter.
     */
    protected static final List<Long> SUBMITTER_PROJECT_PAYMENT_TYPES = Arrays.asList(
            Constants.CONTEST_PAYMENT_TYPE_ID,
            Constants.CONTEST_CHECKPOINT_PAYMENT_TYPE_ID);
    /**
     * The allowed project payment type ids of reviewer.
     */
    protected static final List<Long> REVIEWER_PROJECT_PAYMENT_TYPES = Arrays.asList(Constants.REVIEW_PAYMENT_TYPE_ID);
    /**
     * The allowed project payment type ids of copilot.
     */
    protected static final List<Long> COPILOT_PROJECT_PAYMENT_TYPES = Arrays.asList(Constants.COPILOT_PAYMENT_TYPE_ID);
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
     * Represents the project payments form.
    */
    private ProjectPaymentsForm model;

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
    protected static void populatePaymentsForm(HttpServletRequest request, Project project,
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
    private static List<ResourcePayments> getResourcePayments(List<ProjectPayment> payments, List<Resource> resources) {
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
    protected static void populateResourcesMap(HttpServletRequest request, Resource[] resources) {
        Map<Long, String> resourceRoles = new HashMap<Long, String>();
        Map<Long, Long> resourceUserIds = new HashMap<Long, Long>();
        for (Resource resource : resources) {
            resourceRoles.put(resource.getId(), resource.getResourceRole().getName());
            resourceUserIds.put(resource.getId(), resource.getUserId());
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
    protected static void populateCommonAttributes(HttpServletRequest request, Resource[] resources,
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
     * Getter of model.
     * @return the model
     */
    public ProjectPaymentsForm getModel() {
        return model;
    }

    /**
     * Setter of model.
     * @param model the model to set
     */
    public void setModel(ProjectPaymentsForm model) {
        this.model = model;
    }
}

