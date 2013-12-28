/*
 * Copyright (C) 2010-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import static com.cronos.onlinereview.actions.Constants.PROJECT_MANAGEMENT_PERM_NAME;
import static com.cronos.onlinereview.actions.Constants.REGISTRATION_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.SUBMISSION_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.SUCCESS_FORWARD_NAME;
import static com.cronos.onlinereview.actions.Constants.VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cronos.onlinereview.dataaccess.CatalogDataAccess;
import com.cronos.onlinereview.ejblibrary.SpringContextProvider;
import com.cronos.onlinereview.phases.PaymentsHelper;
import com.cronos.termsofuse.dao.ProjectTermsOfUseDao;
import com.cronos.termsofuse.dao.TermsOfUseDao;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.dao.UserTermsOfUseDao;
import com.cronos.termsofuse.model.TermsOfUse;
import com.topcoder.dde.catalog.ComponentVersionInfo;
import com.topcoder.management.payment.ProjectPaymentAdjustment;
import com.topcoder.management.payment.ProjectPaymentAdjustmentManager;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculator;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackDetail;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.errorhandling.BaseRuntimeException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.LazyValidatorForm;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.dde.catalog.Document;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseStatusEnum;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidatorException;
import com.topcoder.util.distribution.DistributionTool;
import com.topcoder.util.distribution.DistributionToolException;
import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>A <code>Struts</code> action to be used for handling requests related to <code>Project Management Console</code>
 * in <code>Online Review</code> application.</p>
 *
 * <p>As of current version such requests may require adding new resources to designated projects, extend
 * <code>Registration</code> or <code>Submission</code> phases for projects.</p>
 *
 * <p>
 * Version 1.0: Online Review Project Management Console assembly v1.0.
 * </p>
 * <p>
 * Version 1.1: Distribution Auto Generation Assembly v1.0 Change notes:
 * <ul>
 *     <li>Added support for managing distribution: create distribution upload distribution.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Version 1.2 (Online Review Status Validation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Methods adjusted for new signatures of create managers methods from ActionsHelper.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.3 (Review Feedback Integration Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #manageReviewFeedback(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} 
 *     method.</li>
 *     <li>Added {@link #initReviewFeedbackIntegration(HttpServletRequest, Project)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.4 (Online Review - Project Payments Integration Part 1 v1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #defaultProjectPaymentCalculator} instance.</li>
 *     <li>Added {@link #getAvailableReviewerRoles(Project)} method to get the available reviewer roles which we
 *     can set the reviewer payments.</li>
 *     <li>Added {@link #setReviewPaymentsRequestAttribute(HttpServletRequest, Project)} method to set the review
 *     payments data to request attribute.</li>
 *     <li>Added {@link #manageReviewPayments(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *     method to manage the review payments.</li>
 *     <li>Updated {@link #initProjectManagementConsole(HttpServletRequest, Project)} method to initialize the
 *     review payments data.</li>
 *     <li>Added {@link #createDefaultProjectPaymentCalculator()} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.5 (Online Review - Project Payments Integration Part 3 v1.0) Change notes:
 *   <ol>
 *       <li>Updated {@link #handleResourceAddition(Project, HttpServletRequest, ActionForm, Map, Map)} to not setting
 *       resource payment related properties.</li>
 *       <li>Updated {@link #manageReviewFeedback(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *       to call {@link PaymentsHelper#processAutomaticPayments(long, String)} to update the resources' payments.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.6 (Module Assembly - Enhanced Review Feedback Integration) Change notes:
 *   <ol>
 *       <li>Added constant {@link #MAX_FEEDBACK_LENGTH}.</li>
 *       <li>Added {@link #editReviewFeedback(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *       method to handle the request to edit the review feedback.</li>
 *       <li>Updated {@link #manageReviewFeedback(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *       method to support editing existing feedback, and adopt for the new review feedback management component.</li>
 *       <li>Updated {@link #initReviewFeedbackIntegration(HttpServletRequest, Project)} to adopt for the new
 *       review feedback management component.</li>
 *       <li>Added helper method {@link #isReviewFeedbackAllowed(Project)} to check whether the current user can
 *       manage the review feedback for the specific project</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.7 (Online Review - Iterative Review v1.0) Change notes:
 *   <ol>
 *       <li>Added iterative reviewer to role list.</li>
 *   </ol>
 * </p>
 *
 * @author isv, romanoTC, rac_, flexme, duxiaoyang
 * @version 1.7
 * @since 1.0
 */
public class ProjectManagementConsoleActions extends DispatchAction {
    
    /**
     * This member variable is a constant array that holds names of different reviewer roles.
     * 
     * @since 1.3
     */
    private static final String[] REVIEW_FEEDBACK_ELIGIBLE_ROLE_NAMES = new String[]{
        Constants.REVIEWER_ROLE_NAME, Constants.ACCURACY_REVIEWER_ROLE_NAME, Constants.FAILURE_REVIEWER_ROLE_NAME,
        Constants.STRESS_REVIEWER_ROLE_NAME, Constants.ITERATIVE_REVIEWER_ROLE_NAME};

    /**
     * <p>A <code>long</code> providing the constant value for single hour duration in milliseconds.</p>
     */
    private static final long HOUR_DURATION_IN_MILLIS = 60 * 60 * 1000L;

    /**
     * <p>A <code>long</code> providing the constant value for single day duration in milliseconds.</p>
     */
    private static final long DAY_DURATION_IN_MILLIS = 24 * 60 * 60 * 1000L;

    /**
     * <p>A <code>long</code> providing the constant value for copilot resource role id </p>
     */
    private static final long COPILOT_RESOURCE_ROLE_ID = 14;

    /**
     * <p>A <code>long</code> providing the constant value for client manager resource role id</p>
     */
    private static final long CLIENT_MANAGER_RESOURCE_ROLE_ID = 15;

        /**
     * <p>A <code>long</code> providing the constant value for observer resource role id</p>
     */
    private static final long OBSERVER_RESOURCE_ROLE_ID = 12;

    /**
     * <p>A <code>long</code> providing the constant value for designer resource role id</p>
     */
    private static final long DESIGNER_RESOURCE_ROLE_ID = 11;

    /**
     * The id of the design distribution document type.
     * @since 1.1
     */
    private static final long DESIGN_DISTRIBUTION_DOC_TYPE_ID = 25;

    /**
     * The id of the development distribution document type.
     * @since 1.1
     */
    private static final long DEVELOPMENT_DISTRIBUTION_DOC_TYPE_ID = 26;

    /**
     * The design distribution document type.
     * @since 1.1
     */
    private static final String DESIGN_DISTRIBUTION_DOC_TYPE = "Design Distribution";

    /**
     * The development distribution document type.
     * @since 1.1
     */
    private static final String DEVELOPMENT_DISTRIBUTION_DOC_TYPE = "Development Distribution";

    /**
     * Design project ID.
     * @since 1.1
     */
    private static final long DESIGN_PROJECT_ID = 1;

    /**
     * Development project ID.
     * @since 1.1
     */
    private static final long DEVELOPMENT_PROJECT_ID = 2;

    /**
     * <p>
     * The directory to upload files that will be used by distribution tool. It is appended to the distribution tool
     * output dir.
     * </p>
     */
    private static final String UPLOADED_ARTIFACTS_DIR = "upload_0";

    /**
     * <p>Valid package names.</p>
     * @since 1.1
     */
    private static final Pattern PACKAGE_PATTERN = Pattern
        .compile("\\s*(([a-zA-Z])[a-zA-Z0-9_]*)(\\.([a-zA-Z])[a-zA-Z0-9_]*)*\\s*");

    /**
     * <p>Valid version numbers.</p>
     * @since 1.1
     */
    private static final Pattern VERSION_PATTERN = Pattern.compile("\\s*([1-9][0-9]*)(\\.[0-9]+){0,3}\\s*");

    /**
     * DistributionTool is thread-safe, so we can keep it as an instance variable.
     * @since 1.1
     */
    private static final DistributionTool DISTRIBUTION_TOOL = new DistributionTool();

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM.dd.yyyy hh:mm a", Locale.US);

    /**
     * The max length of review feedback text.
     *
     * @since 1.6
     */
    private static final int MAX_FEEDBACK_LENGTH = 4096;

    /**
     * The instance of default project payment calculator.
     *
     * @since 1.4
     */
    private static final ProjectPaymentCalculator defaultProjectPaymentCalculator
            = createDefaultProjectPaymentCalculator();

    /**
     * <p>Constructs new <code>ProjectManagementConsoleActions</code> instance. This implementation does nothing.</p>
     */
    public ProjectManagementConsoleActions() {
    }

    /**
     * <p>Processes the incoming request which is a request for viewing the <code>Project Management Console</code> view
     * for requested project.</p>
     *
     * <p>Verifies that current user is granted access to this functionality and is granted a permission to access the
     * requested project details.</p>
     *
     * @param mapping an <code>ActionMapping</code> used for mapping the specified request to this action.
     * @param form an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws BaseException if an unexpected error occurs.
     */
    public ActionForward viewConsole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. Also check that current user is granted a
        // permission to access the details for requested project
        verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                                                     VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME, false);

        if (!verification.isSuccessful()) {
            // If not then redirect the request to log-in page or report about the lack of permissions.
            return verification.getForward();
        } else {
            // User is granted appropriate permissions - set the list of available roles for resources and flags
            // affecting the Extend Registration/Submission Phase functionality
            Project project = verification.getProject();
            initProjectManagementConsole(request, project);

            return mapping.findForward(SUCCESS_FORWARD_NAME);
        }
    }

    /**
     * <p>Processes the incoming request which is a request for viewing the <code>Edit Review Feedback</code> view
     * for requested project.</p>
     *
     * <p>Verifies that current user is granted access to this functionality and is granted a permission to access the
     * requested project details, also the user doesn't exist in the existing feedback details.</p>
     *
     * @param mapping an <code>ActionMapping</code> used for mapping the specified request to this action.
     * @param form an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws BaseException if an unexpected error occurs.
     * @since 1.6
     */
    public ActionForward editReviewFeedback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                            HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping,
                request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        request.setAttribute("activeTabIdx", 3);
        request.setAttribute("toEdit", Boolean.TRUE);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. Also check that current user is granted a
        // permission to access the details for requested project
        verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME, false);

        if (!verification.isSuccessful()) {
            // If not then redirect the request to log-in page or report about the lack of permissions.
            return verification.getForward();
        } else {
            final Project project = verification.getProject();
            if (!isReviewFeedbackAllowed(project)) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        PROJECT_MANAGEMENT_PERM_NAME,
                        "Error.ReviewFeedbackNotAllowed", Boolean.TRUE);
            }

            ReviewFeedbackManager reviewFeedbackManager = ActionsHelper.createReviewFeedbackManager();
            List<ReviewFeedback> existingReviewFeedbacks = reviewFeedbackManager.getForProject(project.getId());
            if (existingReviewFeedbacks.size() != 1) {
                throw new BaseException("These should be exactly one existing review feedback");
            }
            ReviewFeedback feedback = existingReviewFeedbacks.get(0);
            // if current user is in the existing feedback details, he/she can't edit the review feedback
            for (ReviewFeedbackDetail detail : feedback.getDetails()) {
                if (detail.getReviewerUserId() == AuthorizationHelper.getLoggedInUserId(request)) {
                    return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                            PROJECT_MANAGEMENT_PERM_NAME,
                            "Error.EditReviewFeedbackNotAllowed", Boolean.TRUE);
                }
            }

            initProjectManagementConsole(request, project);

            // Populate the form properties
            LazyValidatorForm lazyForm = (LazyValidatorForm) form;
            if (feedback.getComment() != null) {
                lazyForm.set("unavailable", Boolean.TRUE);
            }
            lazyForm.set("explanation", feedback.getComment());
            Map<String, Resource> reviewerResourcesMap =
                    (Map<String, Resource>) request.getAttribute("reviewerResourcesMap");
            Map<String, ReviewFeedbackDetail> reviewerFeedbackDetail = new HashMap<String, ReviewFeedbackDetail>();
            for (ReviewFeedbackDetail detail : feedback.getDetails()) {
                reviewerFeedbackDetail.put(String.valueOf(detail.getReviewerUserId()), detail);
            }
            int idx = -1;
            for (Map.Entry<String, Resource> entry : reviewerResourcesMap.entrySet()) {
                idx++;
                lazyForm.set("reviewerUserId", idx, Long.parseLong(entry.getKey()));
                ReviewFeedbackDetail detail = reviewerFeedbackDetail.get(entry.getKey());
                if (detail == null) {
                    continue;
                }
                lazyForm.set("reviewerScore", idx, detail.getScore());
                lazyForm.set("reviewerFeedback", idx, detail.getFeedbackText());
            }

            return mapping.findForward(SUCCESS_FORWARD_NAME);
        }
    }

    /**
     * Gets the available reviewer roles which we can set the review payments for a project.
     *
     * @param project the project.
     * @return the list of role ids which we can set the review payments for the project.
     * @throws BaseException if any error occurs.
     * @since 1.4
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
            if ( project.getProjectCategory().getName().equals("Development")) {
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
     * Sets the review payments data of a project into request attribute.
     *
     * @param request the HttpServletRequest instance.
     * @param project the project.
     * @throws BaseException if any error occurs.
     * @since 1.4
     */
    private static void setReviewPaymentsRequestAttribute(HttpServletRequest request, Project project) throws BaseException {
        List<Long> resourceRoleIds = getAvailableReviewerRoles(project);
        ResourceRole[] allRoles = ActionsHelper.createResourceManager().getAllResourceRoles();

        String[] resourceRoles = new String[resourceRoleIds.size()];
        for (int i = 0; i < resourceRoleIds.size(); i++) {
            for (ResourceRole role : allRoles) {
                if (role.getId() == resourceRoleIds.get(i)) {
                    resourceRoles[i] = role.getName();
                    break;
                }
            }
        }

        Map<Long, BigDecimal> defaultPayments = defaultProjectPaymentCalculator.getDefaultPayments(project.getId(), resourceRoleIds);
        List<ProjectPaymentAdjustment> paymentAdjusts = ActionsHelper.createProjectPaymentAdjustmentManager().retrieveByProjectId(project.getId());
        Map<Long, Double> fixedAmount = new HashMap<Long, Double>();
        Map<Long, Integer> percentAmount = new HashMap<Long, Integer>();
        for (ProjectPaymentAdjustment paymentAdjust : paymentAdjusts) {
            if (paymentAdjust.getFixedAmount() != null) {
                fixedAmount.put(paymentAdjust.getResourceRoleId(), paymentAdjust.getFixedAmount().doubleValue());
            } else if (paymentAdjust.getMultiplier() != null) {
                percentAmount.put(paymentAdjust.getResourceRoleId(), (int) (paymentAdjust.getMultiplier().doubleValue() * 100.0));
            }
        }

        List<String> radios = new ArrayList<String>();
        List<String> fixed = new ArrayList<String>();
        List<String> percentage = new ArrayList<String>();
        for (int i = 0; i < resourceRoleIds.size(); i++) {
            long roleId = resourceRoleIds.get(i);
            boolean chooseDefault = true;
            if (fixedAmount.containsKey(roleId)) {
                radios.add("fixed");
                fixed.add(fixedAmount.get(roleId).toString());
                chooseDefault = false;
            } else {
                fixed.add(null);
            }
            if (percentAmount.containsKey(roleId)) {
                radios.add("percentage");
                percentage.add(percentAmount.get(roleId).toString());
                chooseDefault = false;
            } else {
                percentage.add("100");
            }
            if (chooseDefault) {
                radios.add("default");
            }
        }

        request.setAttribute("defaultPayments", defaultPayments);
        request.setAttribute("resourceRoleIds", resourceRoleIds);
        request.setAttribute("resourceRoleNames", resourceRoles);
        request.setAttribute("reviewPaymentsRadio", radios);
        request.setAttribute("reviewPaymentsFixed", fixed);
        request.setAttribute("reviewPaymentsPercentage", percentage);
    }

    /**
     * <p>
     * Creates a project design distribution file, upload it to the server or return it to the user (or both).
     * </p>
     *
     * @param mapping an <code>ActionMapping</code> used for mapping the specified request to this action.
     * @param form an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws Exception if an unexpected error occurs.
     * @since 1.1
     */
    public ActionForward manageDistribution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // set the active tab index to able to re-render the page with the correct selected tab in
        // case an error occurs
        request.setAttribute("activeTabIdx", 2);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        verification
            = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                                                     PROJECT_MANAGEMENT_PERM_NAME, false);

        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get the current project
        Project project = verification.getProject();

        LazyValidatorForm lazyForm = (LazyValidatorForm) form;

        // Validate form data
        validateCreateDistributionForm(project, lazyForm, request);

        // Check if there were any validation errors identified and return appropriate forward
        if (ActionsHelper.isErrorsPresent(request)) {
            initProjectManagementConsole(request, project);
            return mapping.getInputForward();
        }

        // Creates the distribution file using the distribution tool component
        File outputDirFile = createDistributionFile(project, lazyForm, request);

        try {
            File distributionFile = null;

            if (outputDirFile != null) {
                // Returns the distribution file from the output dir
                distributionFile = getDistributionFile(outputDirFile);
            }

            if (distributionFile == null) {
                ActionsHelper.addErrorToRequest(request, new ActionMessage(
                    "error.com.cronos.onlinereview.actions.manageProject."
                        + "Distributions.DistTool.CannotFind"));

                initProjectManagementConsole(request, project);
                return mapping.getInputForward();
            }

            // Check what to do with the file
            boolean uploadToServer = getBooleanFromForm(lazyForm, "upload_to_server");
            boolean returnDistribution = getBooleanFromForm(lazyForm, "return_distribution");

            if (uploadToServer) {
                // A temporary final variable to be used by an anonymous class.
                final File tempFile = distributionFile;

                DistributionFileDescriptor descriptor = new DistributionFileDescriptor() {
                    public String getFileName() {
                        return tempFile.getName();
                    }

                    public InputStream getInputStream() throws IOException {
                        return new FileInputStream(tempFile);
                    }
                };

                // Saves the distribution file into the catalog directory (it will always be a design distribution)
                if (!saveDistributionFileToCatalog(project, descriptor, request, true)) {
                    initProjectManagementConsole(request, project);
                    return mapping.getInputForward();
                }
            }

            if (returnDistribution) {

                // Return the distribution file - write it to the response object
                writeDistributionFileToResponse(response, distributionFile);
                return null;

            } else {

                request.getSession().setAttribute("success_upload",
                    getResources(request).getMessage("manageProject.Distributions.Successful_upload"));

                return ActionsHelper.cloneForwardAndAppendToPath(mapping
                    .findForward(SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
            }
        } finally {
            if (outputDirFile != null) {
                // cleans output directory file
                cleanupDirectory(outputDirFile);
            }
        }
    }

    /**
     * <p>
     * Uploads a project distribution file (design or development).
     * </p>
     *
     * @param mapping an <code>ActionMapping</code> used for mapping the specified request to this action.
     * @param form an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws Exception if an unexpected error occurs.
     * @since 1.1
     */
    public ActionForward uploadDistribution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // set the active tab index to able to re-render the page with the correct selected tab in
        // case an error occurs
        request.setAttribute("activeTabIdx", 2);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        verification
            = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                                                     PROJECT_MANAGEMENT_PERM_NAME, false);

        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        Project project = verification.getProject();
        long projectCategoryId = project.getProjectCategory().getId();

        // Validate project type - must be a Design or Development
        if ((projectCategoryId != DESIGN_PROJECT_ID) && (projectCategoryId != DEVELOPMENT_PROJECT_ID)) {
            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Upload.ProjectCategory"));

            initProjectManagementConsole(request, project);
            return mapping.getInputForward();
        }

        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        final FormFile distributionFormFile = (FormFile) lazyForm.get("distribution_file");

        if (distributionFormFile == null || distributionFormFile.getFileSize() == 0) {
            ActionsHelper.addErrorToRequest(request, "distribution_file", new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Distribution.Empty"));

            initProjectManagementConsole(request, project);
            return mapping.getInputForward();
        }

        DistributionFileDescriptor descriptor = new DistributionFileDescriptor() {
            public String getFileName() {
                return distributionFormFile.getFileName();
            }

            public InputStream getInputStream() throws IOException {
                return distributionFormFile.getInputStream();
            }
        };

        boolean isDesign = (projectCategoryId == DESIGN_PROJECT_ID);

        // Saves the distribution file into the catalog directory
        if (!saveDistributionFileToCatalog(project, descriptor, request, isDesign)) {

            initProjectManagementConsole(request, project);
            return mapping.getInputForward();
        }

        request.getSession().setAttribute("success_upload",
            getResources(request).getMessage("manageProject.Distributions.Successful_upload"));

        return ActionsHelper.cloneForwardAndAppendToPath(mapping
            .findForward(SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
    }

    /**
     * <p>
     * Saves (or updates) the distribution file to the component catalog.
     * </p>
     *
     * @param project the current project used to get component information.
     * @param descriptor an object defining the distribution file (may be the upload stream or a file stream).
     * @param request object used to add error information in case something goes wrong.
     * @param isDesign indicates if the distribution is for design or development.
     * @return <code>true</code> if the file was save properly, <code>false</code> otherwise
     * @throws Exception if any error occurs while uploading the file.
     * @since 1.1
     */
    private boolean saveDistributionFileToCatalog(Project project, DistributionFileDescriptor descriptor,
        HttpServletRequest request, boolean isDesign) throws Exception {

        long componentId = getProjectLongValue(project, "Component ID");
        long versionId = getProjectLongValue(project, "Version ID");

        if (componentId == 0 || versionId == 0) {
            if (componentId == 0) {
                ActionsHelper.addErrorToRequest(request, new ActionMessage(
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.Component.Invalid"));
            }

            if (versionId == 0) {
                ActionsHelper.addErrorToRequest(request, new ActionMessage(
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.ComponentVersion.Invalid"));
            }

            return false;
        }

        CatalogDataAccess catalogDataAccess = SpringContextProvider.getCatalogDataAccess();
        ComponentVersionInfo componentVersion = catalogDataAccess.getComponentVersionInfo(componentId, versionId);

        String rootDir = ConfigHelper.getCatalogOutputDir() + File.separator;
        String dir = "" + componentId + File.separator + componentVersion.getVersionId() + File.separator;

        File dirFile = new File(rootDir + dir);

        // Create the directories if they do not already exist.
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Catalog.OutputDir"));
            return false;
        }

        String url = dir + descriptor.getFileName();

        // Copy distribution file to catalog folder
        copyStream(descriptor.getInputStream(), new FileOutputStream(rootDir + url));

        long documentType;
        String documentName;

        // project is either Design or Development
        if (isDesign) {
            documentType = DESIGN_DISTRIBUTION_DOC_TYPE_ID;
            documentName = DESIGN_DISTRIBUTION_DOC_TYPE;

        } else {
            documentType = DEVELOPMENT_DISTRIBUTION_DOC_TYPE_ID;
            documentName = DEVELOPMENT_DISTRIBUTION_DOC_TYPE;
        }

        Document document
            = getDocumentOfType(catalogDataAccess.getDocuments(componentVersion.getVersionId()), documentType);

        if (document == null) {
            // Add document to component
            document = new Document(documentName, url, documentType);
            catalogDataAccess.addDocument(componentVersion.getVersionId(), document);
        } else {
            // Update document
            document.setURL(url);
            catalogDataAccess.updateDocument(componentVersion.getVersionId(), document);
        }

        return true;
    }

    /**
     * Return the document with the associated document type;
     *
     * @param documents the list of component documents.
     * @param documentType document type.
     * @return the document with the given document type, or null if not found.
     * @since 1.1
     */
    private Document getDocumentOfType(Collection<?> documents, long documentType) {
        for (Object documentObject : documents) {
            Document document = (Document) documentObject;
            if (document.getType() == documentType) {
                // Assume a single document of this type will exist
                return document;
            }
        }
        // Not found
        return null;
    }

    /**
     * Return project property long value.
     *
     * @param project the project object
     * @param name the property name
     * @return the long value, 0 if it does not exist
     * @since 1.1
     */
    private static long getProjectLongValue(Project project, String name) {
        Object obj = project.getProperty(name);
        if (obj == null) {
            return 0;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * <p>
     * Writes the distribution file that was recently created to the HTTP response object.
     * </p>
     *
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @param distributionFile the distribution file.
     * @throws IOException if any error occurs while reading from the distribution file or writing to the response.
     * @since 1.1
     */
    private void writeDistributionFileToResponse(HttpServletResponse response, File distributionFile)
        throws IOException {

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) distributionFile.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + distributionFile.getName() + "\"");

        response.flushBuffer();

        copyStream(new FileInputStream(distributionFile), response.getOutputStream());
    }

    /**
     * <p>
     * Creates the distribution file using the distribution tool component.
     * </p>
     *
     * @param project the current project.
     * @param lazyForm provides the form parameters mapped to specified request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @return the distribution file (or null if unable to create the output file or find the script to use).
     * @throws IOException if any error occurs while copying uploaded files.
     * @since 1.1
     */
    private File createDistributionFile(Project project, LazyValidatorForm lazyForm, HttpServletRequest request)
        throws IOException {

        String outputDir = getOutputDir(project);

        if (outputDir == null) {
            // Problem creating the output dir
            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.OutputDir"));
            return null;
        }

        File outputDirFile = new File(outputDir);

        // Create Java design distribution
        Map<String, String> parameters = new HashMap<String, String>();

        // At this point, Project Name and Version are guaranteed to be not null
        String projectName = (String) project.getProperty("Project Name");
        String version = (String) project.getProperty("Project Version");

        parameters.put(DistributionTool.VERSION_PARAM_NAME, version.trim());
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, projectName.trim());

        String packageName = (String) lazyForm.get("distribution_package_name");
        if (!isEmpty(packageName)) {
            parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, packageName.trim());
        }

        parameters.put("output_dir", outputDir);

        // Requirements Specification (validate already to be not null);
        FormFile rsFormFile = (FormFile) lazyForm.get("distribution_rs");
        File rsFile = createTempFile(outputDir, rsFormFile);

        parameters.put("rs", rsFile.getAbsolutePath());

        // Add additional documents
        int j = 1;
        for (int i = 1; i <= 3; ++i) {
            FormFile additionalFormFile = (FormFile) lazyForm.get("distribution_additional" + i);

            // Only use additional file if it is uploaded and well set
            if ((additionalFormFile != null) && (additionalFormFile.getFileSize() > 0)
                && !isEmpty(additionalFormFile.getFileName())) {

                File additionalFile = createTempFile(outputDir, additionalFormFile);

                parameters.put("additional_doc" + j, additionalFile.getAbsolutePath());
                ++j;
            }
        }

        // Determines the script that will be used
        String rootCatalogID = (String) project.getProperty("Root Catalog ID");
        try {
            DISTRIBUTION_TOOL.createDistribution(ConfigHelper.getDistributionScript(rootCatalogID), parameters);
        } catch (DistributionToolException ex) {
            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.DistTool.Failure", ex.getMessage()));

            return null;
        }

        return outputDirFile;
    }

    /**
     * <p>
     * Returns the generate distribution file. Looks for the file in the output directory. A safe process will leave a
     * single file in there. If more than one is found, return the file with the latest modified date (since the file
     * will be the last one to be generated).
     * </p>
     *
     * @param outputDirFile the output distribution directory.
     * @return the distribution file.
     * @since 1.1
     */
    private File getDistributionFile(File outputDirFile) {
        File distFile = null;
        long lastModified = Long.MIN_VALUE;

        // Should be a single file only, but there might a problem deleting files, so, to be safe,
        // check for the latest file - which should be the distribution file
        File[] files = outputDirFile.listFiles();
        for (File file : files) {
            if (file.isFile() && (file.lastModified() > lastModified)) {
                distFile = file;
                lastModified = file.lastModified();
            }
        }

        return distFile;
    }

    /**
     * <p>
     * Deletes a directory. Traverse deleting all files and sub directories.
     * </p>
     *
     * @param dirFile the directory to clear and delete.
     * @since 1.1
     */
    private void cleanupDirectory(File dirFile) {

        for (File file : dirFile.listFiles()) {
            if (file.isDirectory()) {
                cleanupDirectory(file);
            }

            if (file.isFile()) {
                file.delete();
            }
        }

        dirFile.delete();
    }

    /**
     * <p>
     * Saves the uploaded file to disk and returns its absolute path name.
     * </p>
     *
     * @param outputdir the base directory to save the file.
     * @param formFile the uploaded file.
     * @return the absolute path of the temporary file.
     * @throws IOException if any error occurs while uploading a file.
     * @since 1.1
     */
    private File createTempFile(String outputdir, FormFile formFile)
        throws IOException {

        String dir = outputdir + File.separator + UPLOADED_ARTIFACTS_DIR;
        File output = new File(dir + File.separator + formFile.getFileName());
        FileOutputStream out = new FileOutputStream(output);
        InputStream in = formFile.getInputStream();

        copyStream(in, out);

        return output;
    }

    /**
     * <p>
     * Copies data from an InputStream to an OutputStream.
     * </p>
     *
     * @param in the input stream.
     * @param out the output stream.
     * @throws IOException if any exception occurs.
     * @since 1.1
     */
    private void copyStream(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[65536];

            for (;;) {
                int numOfBytesRead = in.read(buffer);
                if (numOfBytesRead == -1) {
                    break;
                }
                out.write(buffer, 0, numOfBytesRead);
            }
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                // ignore
            }

            try {
                out.close();
            } catch (IOException ex) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * Creates the output directory based on the project ID. This directory will be used to generated the distribution
     * file.
     * </p>
     *
     * @param project the current project.
     * @return the output directory based on the project ID (null if unable to create the directories).
     * @since 1.1
     */
    private String getOutputDir(Project project) {

        String baseDir = ConfigHelper.getDistributionToolOutputDir() + File.separator;
        baseDir = baseDir + project.getId() + "_" + System.currentTimeMillis();

        File dir = new File(baseDir + File.separator + UPLOADED_ARTIFACTS_DIR);

        // Temp directory should not exist, otherwise other files will be considered (this can happen
        // in extremely odd cases of two people submitting files at the same millisecond
        if (dir.exists()) {
            return null;
        }

        if (!dir.mkdirs()) {
            return null;
        }

        return baseDir;
    }

    /**
     * <p>
     * Validates the specified request which is expected to be a create design distribution request.
     * </p>
     * <p>
     * Verifies that the package name and the RS are provided. RS should be PDF/DOC/RTF.
     * </p>
     *
     * @param project the current project.
     * @param lazyForm an <code>LazyValidatorForm</code> providing parameters mapped to this request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @since 1.1
     */
    private void validateCreateDistributionForm(Project project, LazyValidatorForm lazyForm, HttpServletRequest request) {

        // Validate project type - must be a Design or Development
        long projectCategoryId = project.getProjectCategory().getId() ;
        if ((projectCategoryId != DESIGN_PROJECT_ID) && (projectCategoryId != DEVELOPMENT_PROJECT_ID)) {
            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.ProjectCategory"));
        }

        String projectName = (String) project.getProperty("Project Name");

        // validate project name
        if (isEmpty(projectName)) {
            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.ProjectName.Invalid"));
        }

        String version = (String) project.getProperty("Project Version");

        // validate project version
        if (isEmpty(version) || !VERSION_PATTERN.matcher(version).matches()) {
            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Version.Invalid",
                    new Object[] {version}));
        }

        // Determines the script that will be used (if other is used, package is not mandatory)
        String rootCatalogID = (String) project.getProperty("Root Catalog ID");

        // validate root catalog id
        if (isEmpty(rootCatalogID)) {
            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.RootCatalog.Invalid"));

        } else {
            String defaultScript = ConfigHelper.getDefaultDistributionScript();
            String distributionScript = ConfigHelper.getDistributionScript(rootCatalogID);

            // validate root catalog id is defined in configuration file
            if (distributionScript == null) {

                ActionsHelper.addErrorToRequest(request, new ActionMessage(
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.RootCatalog.NotDefined",
                        new Object[] {rootCatalogID}));

            } else if (!defaultScript.equals(distributionScript)) {
                // Assume default script ('other') does not need package name
                String packageName = (String) lazyForm.get("distribution_package_name");

                // validate package is required
                if (isEmpty(packageName)) {
                    ActionsHelper.addErrorToRequest(request, "distribution_package_name", new ActionMessage(
                        "error.com.cronos.onlinereview.actions.manageProject.Distributions.PackageName.Empty"));

                } else if (!PACKAGE_PATTERN.matcher(packageName).matches()) {
                    // Validate it is a valid package
                    ActionsHelper.addErrorToRequest(request, "distribution_package_name", new ActionMessage(
                        "error.com.cronos.onlinereview.actions.manageProject.Distributions.PackageName.Invalid"));
                }
            }
        }

        Set<String> uploadedFiles = new HashSet<String>();
        FormFile distributionRSFile = (FormFile) lazyForm.get("distribution_rs");

        // Validate the RS form file
        if (distributionRSFile == null || isEmpty(distributionRSFile.getFileName())) {
            ActionsHelper.addErrorToRequest(request, "distribution_rs", new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.RS.Missing"));

        } else if (distributionRSFile.getFileSize() == 0) {
            ActionsHelper.addErrorToRequest(request, "distribution_rs", new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.RS.Empty"));

        } else {
            // Validate RS file name
            String lcFileName = distributionRSFile.getFileName().toLowerCase();
            if (!(lcFileName.endsWith("rtf") || lcFileName.endsWith("doc") || lcFileName.endsWith("pdf"))) {
                ActionsHelper.addErrorToRequest(request, "distribution_rs", new ActionMessage(
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.RS.Invalid"));
            }

            uploadedFiles.add(lcFileName);
        }

        // Validate additional files - check if more than one file has the same name
        for (int i = 1; i <= 3; ++i) {
            FormFile additionalFormFile = (FormFile) lazyForm.get("distribution_additional" + i);

            // Only use additional file if it is uploaded and well set
            if ((additionalFormFile != null) && (additionalFormFile.getFileSize() > 0)
                && !isEmpty(additionalFormFile.getFileName())) {

                String lcFileName = additionalFormFile.getFileName().toLowerCase();
                if (uploadedFiles.contains(lcFileName)) {
                    ActionsHelper.addErrorToRequest(request, "distribution_additional" + i, new ActionMessage(
                        "error.com.cronos.onlinereview.actions.manageProject.Distributions.Files.SameName"));
                }

                uploadedFiles.add(lcFileName);
            }
        }


        boolean uploadToServer = getBooleanFromForm(lazyForm, "upload_to_server");
        boolean returnDistribution = getBooleanFromForm(lazyForm, "return_distribution");

        // Must select at least one of these options
        if (!uploadToServer && !returnDistribution) {
            // Add the error message to both checkboxes if design distribution

            if (projectCategoryId == DESIGN_PROJECT_ID) {
                ActionsHelper.addErrorToRequest(request, "upload_to_server", new ActionMessage(
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.Upload.Unchecked"));
            }

            ActionsHelper.addErrorToRequest(request, "return_distribution", new ActionMessage(
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Upload.Unchecked"));
        }
    }

    /**
     * <p>
     * Checks if a string value is null/empty.
     * </p>
     *
     * @param value the string being validate.
     * @return <code>true</code> if a string value is null/empty.
     * @since 1.1
     */
    private static boolean isEmpty(String value) {
        return (value == null) || (value.trim().length() == 0);
    }

    /**
     * <p>Processes the incoming request which is a request for saving review payments for requested project.</p>
     *
     * <p>Verifies that current user is granted access to this functionality and is granted a permission to access the
     * requested project details.</p>
     *
     * @param mapping an <code>ActionMapping</code> used for mapping the specified request to this action.
     * @param form an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws Exception if an unexpected error occurs.
     * @since 1.4
     */
    public ActionForward manageReviewPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        request.setAttribute("activeTabIdx", 4);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        verification
                = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                PROJECT_MANAGEMENT_PERM_NAME, false);
        if (!verification.isSuccessful()) {
            return verification.getForward();
        } else {
            // Validate the forms
            final Project project = verification.getProject();
            List<Long> availableRoleIds = getAvailableReviewerRoles(project);
            LazyValidatorForm lazyForm = (LazyValidatorForm) form;
            Long[] roleIds = (Long[]) lazyForm.get("resources_roles_id");
            String[] paymentRadios = (String[]) lazyForm.get("resource_payments_radio");
            String[] fixed = (String[]) lazyForm.get("resource_payments_fixed_amount");
            String[] percentage = (String[]) lazyForm.get("resource_payments_percent_amount");

            for (int i = 0; i < roleIds.length; i++) {
                if (!availableRoleIds.contains(roleIds[i])) {
                    ActionsHelper.addErrorToRequest(request,
                        "resources_roles_id[" + i + "]",
                        new ActionMessage(
                            "error.com.cronos.onlinereview.actions.manageProject.ReviewPayments.ResourceRoleProhibited",
                            roleIds[i]));
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
                return mapping.getInputForward();
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
                return ActionsHelper.cloneForwardAndAppendToPath(
                        mapping.findForward(SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
            }
        }
    }
    /**
     * <p>Processes the incoming request which is a request for viewing the <code>Project Management Console</code> view
     * for requested project.</p>
     *
     * <p>Verifies that current user is granted access to this functionality and is granted a permission to access the
     * requested project details.</p>
     *
     * @param mapping an <code>ActionMapping</code> used for mapping the specified request to this action.
     * @param form an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws Exception if an unexpected error occurs.
     */
    @SuppressWarnings("unchecked")
    public ActionForward manageProject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        request.setAttribute("activeTabIdx", 1);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        verification
            = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                                                     PROJECT_MANAGEMENT_PERM_NAME, false);
        if (!verification.isSuccessful()) {
            return verification.getForward();
        } else {
            // Validate the forms
            final Project project = verification.getProject();
            final Phase[] phases = getProjectPhases(project);

            // Validate that Registration phase indeed exists and can be extended based on current state of the project
            // and that valid positive amount of days to extend is provided
            final Phase registrationPhase = getRegistrationPhaseForExtension(phases);
            int registrationExtensionDays = validatePhaseExtensionRequest(request, form, registrationPhase,
                "registration_phase_extension", REGISTRATION_PHASE_NAME,
                ConfigHelper.getRegistrationPhaseMaxExtensionDays());

            // Validate that Submission phase indeed exists and can be extended based on current state of the project
            // and that valid positive amount of days to extend is provided
            final Phase submissionPhase = getSubmissionPhaseForExtension(phases);
            int submissionExtensionDays = validatePhaseExtensionRequest(request, form, submissionPhase,
                "submission_phase_extension", SUBMISSION_PHASE_NAME,
                ConfigHelper.getRegistrationPhaseMaxExtensionDays());

            // Verify that new registration phase end time (if phase is going to be extended) will not exceed the
            // end time for submission phase (despite whether it is also going to be extended or not)
            if ((registrationPhase != null) && (registrationExtensionDays > 0)) {
                Phase submissionPhaseCurrent = ActionsHelper.findPhaseByTypeName(phases, SUBMISSION_PHASE_NAME);
                Date expectedSubmissionPhaseEndDate;
                if ((submissionPhase != null) && (submissionExtensionDays > 0)) {
                    expectedSubmissionPhaseEndDate = new Date(submissionPhase.getScheduledEndDate().getTime()
                                                              + submissionExtensionDays * DAY_DURATION_IN_MILLIS);
                } else {
                    expectedSubmissionPhaseEndDate = submissionPhaseCurrent.getScheduledEndDate();
                }
                Date expectedRegistrationEndDate = new Date(registrationPhase.getScheduledEndDate().getTime()
                                                            + registrationExtensionDays * DAY_DURATION_IN_MILLIS);
                if (expectedRegistrationEndDate.after(expectedSubmissionPhaseEndDate)) {
                    ActionsHelper.addErrorToRequest(request, "registration_phase_extension",
                        new ActionMessage("error.com.cronos.onlinereview.actions.manageProject."
                                          + REGISTRATION_PHASE_NAME + ".AfterSubmission"));
                }
            }

            // Validate the Add Resources form
            Object[] caches = validateAddResourcesRequest(request, form);

            // Check if there were any validation errors identified and return appropriate forward
            if (ActionsHelper.isErrorsPresent(request)) {
                initProjectManagementConsole(request, project);
                return mapping.getInputForward();
            } else {
                handleRegistrationPhaseExtension(registrationPhase, registrationExtensionDays, request, form);
                handleSubmissionPhaseExtension(submissionPhase, submissionExtensionDays, request, form);
                handleResourceAddition(project, request, form, (Map<Long, ResourceRole>) caches[0],
                                       (Map<String, ExternalUser>) caches[1]);
                if (ActionsHelper.isErrorsPresent(request)) {
                    initProjectManagementConsole(request, project);
                    return mapping.getInputForward();
                } else {
                    return ActionsHelper.cloneForwardAndAppendToPath(
                        mapping.findForward(SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
                }
            }
        }
    }



    /**
     * <p>
     * Read a boolean value from the form.
     * </p>
     *
     * @param lazyForm the form to read the value from.
     * @param attribute the name of the attribute of the form (which must be of type Boolean).
     * @return <code>true</code> if the form value is Boolean.TRUE, <code>false</code> if it is Boolean.FALSE or null.
     * @since 1.1
     */
    private static boolean getBooleanFromForm(LazyValidatorForm lazyForm, String attribute) {
        Boolean value = (Boolean) lazyForm.get(attribute);
        return (value != null) && value;
    }

    /**
     * <p>Initializes the <code>Project Management Console</code> view. Binds necessary objects to current request. Such
     * objects include: list of available roles for resources which can be added by current user to specified project;
     * registration/submission phase durations and object presentations; flags indicating whether
     * registration/submission phase can be extended or not.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param project a <code>Project</code> providing the details for current project being managed by current user.
     * @throws BaseException if an unexpected error occurs.
     */
    private void initProjectManagementConsole(HttpServletRequest request, Project project) throws BaseException {
        Phase[] phases = getProjectPhases(project);
        setAvailableResourceRoles(request);
        setRegistrationPhaseExtensionParameters(request, phases);
        setSubmissionPhaseExtensionParameters(request, phases);

        // Identifies if package name is needed
        String rootCatalogID = (String) project.getProperty("Root Catalog ID");
        if (!isEmpty(rootCatalogID)) {
            String distributionScript = ConfigHelper.getDistributionScript(rootCatalogID);
            if (!isEmpty(distributionScript)) {
                request.setAttribute("needsPackageName", !ConfigHelper.getDefaultDistributionScript().equals(
                    distributionScript));
            }
        }
        
        // Initialize the Review Feedback area
        initReviewFeedbackIntegration(request, project);

        setReviewPaymentsRequestAttribute(request, project);
    }

    /**
     * <p>Gets the phases for the specified project.</p>
     *
     * @param project a <code>Project</code> providing the details for current project being managed by current user.
     * @return a <code>Phase</code> array listing the phases for specified project.
     * @throws BaseException if an unexpected error occurs.
     */
    private Phase[] getProjectPhases(Project project) throws BaseException {
        // Get details for requested project
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
        com.topcoder.project.phases.Project phasesProject = phaseManager.getPhases(project.getId());
        return phasesProject.getAllPhases();
    }

    /**
     * <p>Validates the specified request which is expected to be a possible request for extending the specified phase.
     * </p>
     *
     * <p>Verifies that if phase extension was requested then requested phase should exist and should be allowed to be
     * extended and that valid number of days for extension is provided. If either validation fails then respective
     * validation error is bound to request.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param form an <code>ActionForm</code> providing parameters mapped to this request.
     * @param phase a <code>Phase</code> providing details for phase requested for extension.
     * @param extensionDaysRequestParamName a <code>String</code> providing the name of form parameter which provides
     *        the number of days to extend specified phase for as set by current user.
     * @param phaseTypeName a <code>String</code> providing the name of phase type.
     * @param limit an <code>Integer</code> providing the maximum allowed number of days to extend phase for.
     *        <code>null</code> value indicates that there is no such limit set.
     * @return an <code>int</code> providing the number of days to extend the specified phase for or 0 if there were
     *         validation errors encountered or if there were no request for extending the specified phase at all.
     */
    private int validatePhaseExtensionRequest(HttpServletRequest request, ActionForm form, Phase phase,
                                                     String extensionDaysRequestParamName, String phaseTypeName,
                                                     Integer limit) {
        int extensionDays = 0;
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        String extensionDaysString = (String) lazyForm.get(extensionDaysRequestParamName);
        if ((extensionDaysString != null) && (extensionDaysString.trim().length() > 0)) {
            // If phase extension was requested then verify that phase exists and can be extended and that
            // valid number of days for extension was specified
            if (phase == null) {
                ActionsHelper.addErrorToRequest(request, extensionDaysRequestParamName,
                    new ActionMessage("error.com.cronos.onlinereview.actions.manageProject." + phaseTypeName
                                      + ".NotExtendable",
                                      ConfigHelper.getMinimumHoursBeforeSubmissionDeadlineForExtension()));
            } else {
                try {
                    extensionDays = Integer.parseInt(extensionDaysString);
                    if ((extensionDays < 1) || ((limit != null) && (extensionDays > limit))) {
                        ActionsHelper.addErrorToRequest(request, extensionDaysRequestParamName,
                                new ActionMessage("error.com.cronos.onlinereview.actions."
                                        + "manageProject." + phaseTypeName
                                        + ".LimitExceeded",
                                        1, limit == null ? "" : limit));
                    }
                } catch (NumberFormatException e) {
                    ActionsHelper.addErrorToRequest(request, extensionDaysRequestParamName,
                            new ActionMessage("error.com.cronos.onlinereview.actions.manageProject." + phaseTypeName
                                    + ".NotNumeric"));
                }
            }
        }
        return extensionDays;
    }

    /**
     * <p>Handles the specified request which may require the <code>Registration</code> phase to be extended by number
     * of days specified by current user.</p>
     *
     * @param registrationPhase a <code>Phase</code> representing <code>Registration</code> phase to be extended.
     * @param extensionDays an <code>int</code> providing the number of days to extend phase for.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param form an <code>ActionForm</code> representing the form mapped to this request.
     * @throws BaseException if an unexpected error occurs.
     */
    private void handleRegistrationPhaseExtension(Phase registrationPhase, int extensionDays,
                                                  HttpServletRequest request, ActionForm form) throws BaseException {
        // Adjust registration phase end-time and status only if extension was indeed requested by user
        if (extensionDays > 0) {
            if (isClosed(registrationPhase)) {
                // Re-open closed Registration phase if necessary
                PhaseStatus openPhaseStatus = LookupHelper.getPhaseStatus(PhaseStatusEnum.OPEN.getId());
                registrationPhase.setPhaseStatus(openPhaseStatus);
                registrationPhase.setActualEndDate(null);
            }
            long durationExtension = extensionDays * DAY_DURATION_IN_MILLIS;
            Date currentScheduledEndDate = registrationPhase.getScheduledEndDate();
            Date newScheduledEndDate = new Date(currentScheduledEndDate.getTime() + durationExtension);
            registrationPhase.setScheduledEndDate(newScheduledEndDate);
            registrationPhase.setLength(registrationPhase.getLength() + durationExtension);

            com.topcoder.project.phases.Project phasesProject = registrationPhase.getProject();
            recalculateScheduledDates(phasesProject.getAllPhases());
            PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
            phaseManager.updatePhases(phasesProject, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

            // Clean-up successfully processed form input
            LazyValidatorForm lazyForm = (LazyValidatorForm) form;
            lazyForm.set("registration_phase_extension", "");
        }
    }

    /**
     * <p>Handles the specified request which may require the <code>Submission</code> phase to be extended by number
     * of days specified by current user.</p>
     *
     * @param submissionPhase a <code>Phase</code> representing <code>Submission</code> phase to be extended.
     * @param extensionDays an <code>int</code> providing the number of days to extend phase for.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param form an <code>ActionForm</code> representing the form mapped to this request.
     * @throws BaseException if an unexpected error occurs.
     */
    private void handleSubmissionPhaseExtension(Phase submissionPhase, int extensionDays,
                                                HttpServletRequest request, ActionForm form) throws BaseException {
        // Adjust submission phase end-time only if Submission phase extension was indeed requested by user
        if (extensionDays > 0) {
            PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
            long durationExtension = extensionDays * DAY_DURATION_IN_MILLIS;
            Date currentScheduledEndDate = submissionPhase.getScheduledEndDate();
            Date newScheduledEndDate = new Date(currentScheduledEndDate.getTime() + durationExtension);
            submissionPhase.setScheduledEndDate(newScheduledEndDate);
            submissionPhase.setLength(submissionPhase.getLength() + durationExtension);
            recalculateScheduledDates(submissionPhase.getProject().getAllPhases());
            phaseManager.updatePhases(submissionPhase.getProject(),
                                      Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

            // Clean-up successfully processed form input
            LazyValidatorForm lazyForm = (LazyValidatorForm) form;
            lazyForm.set("submission_phase_extension", "");
        }
    }

    /**
     * <p>Validates the specified request which is expected to be a possible request for assigning new resources to
     * project being edited by current user.</p>
     *
     * <p>Verifies that current user indeed can add resources of requested roles and that all requested roles exist and
     * that all requested users exist also. If any verification fails then respective error is bound to request.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param form an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @return an <code>Object</code> array of two elements. The first element provides mapping from role IDs to
     *         <code>ResourceRole</code> objects. The second element provides the mapping from user handles to
     *         <code>ExternalUser</code> objects.
     * @throws ConfigException if a configuration error is encountered while initializing user project data store.
     * @throws BaseException if an unexpected error occurs
     */
    private Object[] validateAddResourcesRequest(HttpServletRequest request, ActionForm form)
            throws BaseException {

        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        Long[] resourceRoleIds = (Long[]) lazyForm.get("resource_role_id");
        String[] resourceHandles = (String[]) lazyForm.get("resource_handles");

        // Validate that current user is indeed granted permission to add resources of requested roles and that
        // requested roles exist and collect mapping from role IDs to ResourceRole objects to be used further if
        // validation succeeds
        Map<Long, ResourceRole> roleMapping = new HashMap<Long, ResourceRole>();
        Map<String, ExternalUser> users = new HashMap<String, ExternalUser>();
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        for (int i = 0; i < resourceRoleIds.length; i++) {
            Long requestedRoleId = resourceRoleIds[i];
            ResourceRole existingRole = LookupHelper.getResourceRole(requestedRoleId);
            if (existingRole == null) {
                ActionsHelper.addErrorToRequest(request, "resource_role_id[" + i + "]",
                    new ActionMessage("error.com.cronos.onlinereview.actions.manageProject.ResourceRole.InvalidId",
                                      requestedRoleId));
            } else {
                final String permission = "Can Add Resource Role " + existingRole.getName();
                if (!AuthorizationHelper.hasUserPermission(request, permission)) {
                    ActionsHelper.addErrorToRequest(request, "resource_role_id[" + i + "]",
                        new ActionMessage("error.com.cronos.onlinereview.actions.manageProject.ResourceRole.Denied",
                                          existingRole.getName()));
                } else {
                    roleMapping.put(existingRole.getId(), existingRole);

                    // Verify users
                    String handlesParam = resourceHandles[i];
                    if ((handlesParam != null) && (handlesParam.trim().length() > 0)) {
                        String[] handles = handlesParam.split(",");
                        for (String handle : handles) {
                            handle = handle.trim();
                            if (!users.containsKey(handle)) {
                                try {
                                    ExternalUser user = userRetrieval.retrieveUser(handle);
                                    if (user != null) {
                                        users.put(handle, user);
                                    } else {
                                        ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                                            new ActionMessage("error.com.cronos.onlinereview.actions.manageProject."
                                                              + "Resource.Unknown", handle));
                                    }
                                } catch (RetrievalException e) {
                                    e.printStackTrace();
                                    ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                                        new ActionMessage("error.com.cronos.onlinereview.actions.manageProject."
                                                          + "Resource.Unknown", handle));
                                }
                            }
                        }
                    }
                }
            }
        }

        return new Object[] {roleMapping, users};
    }

    /**
     * <p>Handles the specified request which may require the new resources to be added to specified project by current
     * user.</p>
     *
     * @param project a <code>Project</code> providing the details for current project to add new resources to.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param form an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @param roleMapping a <code>Map</code> mapping resource role IDs to role details.
     * @param users a <code>Map</code> mapping user handlers to user account details.
     * @throws BaseException if an unexpected error occurs.
     * @throws RemoteException if an unexpected error occurs.
     */
    private void handleResourceAddition(Project project, HttpServletRequest request, ActionForm form,
                                        Map<Long, ResourceRole> roleMapping, Map<String, ExternalUser> users)
            throws BaseException, RemoteException {

        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        Long[] resourceRoleIds = (Long[]) lazyForm.get("resource_role_id");
        String[] resourceHandles = (String[]) lazyForm.get("resource_handles");

        ResourceManager resourceManager = ActionsHelper.createResourceManager();

        // Collect the lists of resources per roles already registered to project
        Resource[] resources =
            resourceManager.searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));
        Map<Long, Set<String>> existingResources = new HashMap<Long, Set<String>>();
        for (Resource resource : resources) {
            long roleId = resource.getResourceRole().getId();
            if (!existingResources.containsKey(roleId)) {
                existingResources.put(roleId, new HashSet<String>());
            }
            Set<String> roleResources = existingResources.get(roleId);
            roleResources.add((String) resource.getProperty("Handle"));
        }

        // Now add resources for selected roles only if there were no validation errors
        Set<Long> newUsersForumWatch = new HashSet<Long>();
        Set<Long> newUsersForumRoles = new HashSet<Long>();
        Set<Long> newModeratorsForumRoles = new HashSet<Long>();

        Map<Long, Set<String>> processedHandles = new HashMap<Long, Set<String>>();
        Map<Long, Set<String>> existingResourceHandles = new HashMap<Long, Set<String>>();
        Map<Long, Set<String>> duplicateHandles = new HashMap<Long, Set<String>>();
        Map<Long, Set<String>> usersWithPendingTerms = new HashMap<Long, Set<String>>();
        Map<Long, Set<String>> badHandles = new HashMap<Long, Set<String>>();

        for (Long resourceRoleId : resourceRoleIds) {
            duplicateHandles.put(resourceRoleId, new HashSet<String>());
            processedHandles.put(resourceRoleId, new HashSet<String>());
            existingResourceHandles.put(resourceRoleId, new HashSet<String>());
            usersWithPendingTerms.put(resourceRoleId, new HashSet<String>());
            badHandles.put(resourceRoleId, new HashSet<String>());
        }

        for (int i = 0; i < resourceRoleIds.length; i++) {
            Long resourceRoleId = resourceRoleIds[i];

            String handlesParam = resourceHandles[i];
            if ((handlesParam != null) && (handlesParam.trim().length() > 0)) {
                String[] handles = handlesParam.split(",");
                for (String handle : handles) {
                    handle = handle.trim();

                    // Prior to adding resource first check if handle is already assigned the specified role or
                    // if handle is duplicate
                    Set<String> currentRoleResources = existingResources.get(resourceRoleId);
                    if ((currentRoleResources != null) && (currentRoleResources.contains(handle))) {
                        existingResourceHandles.get(resourceRoleId).add(handle);
                    } else if (processedHandles.get(resourceRoleId).contains(handle)) {
                        duplicateHandles.get(resourceRoleId).add(handle);
                    } else {
                        // The resource can be added - there will be no duplicate
                        processedHandles.get(resourceRoleId).add(handle);

                        // Verify if resource has accepted all necessary terms of use for project
                        Long userId = users.get(handle).getId();
                        List<TermsOfUse> pendingTerms
                            = validateResourceTermsOfUse(project.getId(), userId, resourceRoleId);
                        if (!pendingTerms.isEmpty()) {
                            for (TermsOfUse terms : pendingTerms) {
                                usersWithPendingTerms.get(resourceRoleId).add(handle);
                                ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                                        new ActionMessage("error.com.cronos.onlinereview.actions."
                                                + "editProject.Resource.MissingTermsByUser",
                                                handle, terms.getTitle()));
                            }
                        } else if (!validateResourceEligibility(project.getId(), userId)) {
                                ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                                        new ActionMessage("error.com.cronos.onlinereview.actions."
                                                + "editProject.Resource.NotEligibleByUser", handle));
                        } else {
                            // The resource can be added - all necessary terms of use are accepted
                            Resource resource = new Resource();
                            resource.setProject(project.getId());
                            resource.setResourceRole(roleMapping.get(resourceRoleId));
                            resource.setProperty("Handle", handle);
                            resource.setProperty("External Reference ID", userId);
                            resource.setProperty("Registration Date", DATE_FORMAT.format(new Date()));

                            newUsersForumWatch.add(userId);

                            // client manager and copilot have moderator role
                            if (resourceRoleId == COPILOT_RESOURCE_ROLE_ID  || resourceRoleId == CLIENT_MANAGER_RESOURCE_ROLE_ID
                                  || resourceRoleId == OBSERVER_RESOURCE_ROLE_ID || resourceRoleId == DESIGNER_RESOURCE_ROLE_ID)
                            {
                                newModeratorsForumRoles.add(userId);
                            }
                            else
                            {
                                newUsersForumRoles.add(userId);
                            }

                            resourceManager.updateResource(resource,
                                    Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                        }
                    }
                }
            }
        }

        // Add all assigned resources as watchers for forum associated with project and grant then a permission
        // to access the forum
        ActionsHelper.addForumPermissions(project, newUsersForumRoles, false);
        ActionsHelper.addForumPermissions(project, newModeratorsForumRoles, true);
        String forumId = (String) project.getProperty("Developer Forum ID");
        if (forumId == null) {
            forumId = "0";
        }
        ActionsHelper.addForumWatch(project, newUsersForumWatch, Long.parseLong(forumId));

        // If there were any duplicates in resources then bind appropriate messages to request
        // to be shown to user
        for (int i = 0; i < resourceRoleIds.length; i++) {
            Long resourceRoleId = resourceRoleIds[i];
            lazyForm.set("resource_handles", i, "");
            Set<String> badRoleHandles = badHandles.get(resourceRoleId);
            badRoleHandles.addAll(existingResourceHandles.get(resourceRoleId));
            badRoleHandles.addAll(duplicateHandles.get(resourceRoleId));
            badRoleHandles.addAll(usersWithPendingTerms.get(resourceRoleId));
        }

        for (int i = 0; i < resourceRoleIds.length; i++) {
            Long resourceRoleId = resourceRoleIds[i];
            ResourceRole role = LookupHelper.getResourceRole(resourceRoleId);
            Set<String> duplicates = duplicateHandles.get(resourceRoleId);
            Set<String> existing = existingResourceHandles.get(resourceRoleId);
            if (!duplicates.isEmpty()) {
                ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                    new ActionMessage("Error.manageProject.DuplicateHandlesPerRole", role.getName(), duplicates));
            }
            if (!existing.isEmpty()) {
                ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                    new ActionMessage("Error.manageProject.ExistingHandlesPerRole", role.getName(), existing));
            }
            Set<String> badRoleHandles = badHandles.get(resourceRoleId);
            for (String badHandle : badRoleHandles) {
                addBadResourceHandle(i, badHandle, lazyForm);
            }
        }
    }

    /**
     * <p>Adds specified bad handle to list of bad handles for resource role at specified index in specified form.</p>
     *
     * @param index an <code>int</code> providing the index of list of bad handles in the form.
     * @param badHandle a <code>String</code> providing the bad handle.
     * @param lazyForm a <code>LazyValidatorForm</code> mapped to request.
     */
    private void addBadResourceHandle(int index, String badHandle, LazyValidatorForm lazyForm) {
        String badHandles = (String) lazyForm.get("resource_handles", index);
        if (badHandles.length() > 0) {
            badHandles += ", ";
        }
        badHandles += badHandle;
        lazyForm.set("resource_handles", index, badHandles);
    }

     /**
     * <p>Binds to specified request the list of roles available for resources which current user can add to project.
     * Analyzes the assigned roles for current user and binds appropriate list of available resource roles to specified
     * request.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client to bind the list
     *        of available roles to.
     * @throws ResourcePersistenceException if an unexpected error occurs while instantiating resource manager.
     */
    private void setAvailableResourceRoles(HttpServletRequest request) throws ResourcePersistenceException {
        ResourceManager resourceManager = ActionsHelper.createResourceManager();
        ResourceRole[] existingResourceRoles = resourceManager.getAllResourceRoles();
        List<ResourceRole> availableRoles = new ArrayList<ResourceRole>();
        for (ResourceRole role : existingResourceRoles) {
            final String permission = "Can Add Resource Role " + role.getName();
            if (AuthorizationHelper.hasUserPermission(request, permission)) {
                availableRoles.add(role);
            }
        }
        request.setAttribute("availableRoles", availableRoles);
    }

    /**
     * <p>Checks if specified phase is closed by analyzing current phase status.</p>
     *
     * @param phase a <code>Phase</code> representing the phase to be verified.
     * @return <code>true</code> if specified phase is closed; <code>false</code> otherwise.
     */
    private boolean isClosed(Phase phase) {
        long phaseStatusId = phase.getPhaseStatus().getId();
        return phaseStatusId == PhaseStatusEnum.CLOSED.getId();
    }

    /**
     * <p>Gets the number of hours left before the specified time is reached.</p>
     *
     * @param time a <code>Date</code> providing the date and time to calculate time left to.
     * @return a <code>long</code> providing the number of hours left from current time before the specified date and
     *         time is reached.
     */
    private long getHoursLeft(Date time) {
        Date now = new Date();
        long diff = time.getTime() - now.getTime();
        return diff / HOUR_DURATION_IN_MILLIS;
    }

    /**
     * <p>Gets the <code>Registration</code> phase for specified project if such a phase exists and if it can be
     * extended based on current project's state.</p>
     *
     * @param phases a <code>Phase</code> array listing the current project phases.
     * @return a <code>Phase</code> providing the <code>Registration</code> phase for current project which can be
     *         extended or <code>null</code> if there is no <code>Registration</code> phase for project or such a phase
     *         can not be extended since there is less than 48 hours left before <code>Submission</code> phase deadline
     *         or <code>Submission</code> phase is already closed.
     */
    private Phase getRegistrationPhaseForExtension(Phase[] phases) {
        Phase registrationPhase = ActionsHelper.findPhaseByTypeName(phases, REGISTRATION_PHASE_NAME);
        if ((registrationPhase != null)) {
            Phase submissionPhase = ActionsHelper.findPhaseByTypeName(phases, SUBMISSION_PHASE_NAME);
            if (submissionPhaseAllowsExtension(submissionPhase)) {
                return registrationPhase;
            }
        }
        return null;
    }

    /**
     * <p>Gets the <code>Submission</code> phase for specified project if such a phase exists and if it can be
     * extended based on current project's state.</p>
     *
     * @param phases a <code>Phase</code> array listing the current project phases.
     * @return a <code>Phase</code> providing the <code>Submission</code> phase for current project which can be
     *         extended or <code>null</code> if there is no <code>Submission</code> phase for project or such a phase
     *         can not be extended since there is less than 48 hours left before <code>Submission</code> phase deadline
     *         or <code>Submission</code> phase is already closed.
     */
    private Phase getSubmissionPhaseForExtension(Phase[] phases) {
        Phase submissionPhase = ActionsHelper.findPhaseByTypeName(phases, SUBMISSION_PHASE_NAME);
        if (submissionPhaseAllowsExtension(submissionPhase)) {
            return submissionPhase;
        }
        return null;
    }

    /**
     * <p>Binds the attributes to specified request which affect the <code>Extend Registration Phase</code>
     * functionality like indicating whether the <code>Registration</code> phase extension is allowed or not.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client to bind the flags
     *        related to phase extension to.
     * @param phases a <code>Phase</code> array listing the current project phases.
     */
    private void setRegistrationPhaseExtensionParameters(HttpServletRequest request, Phase[] phases) {
        Phase registrationPhase = getRegistrationPhaseForExtension(phases);
        if ((registrationPhase != null)) {
            // Registration phase can be extended
            request.setAttribute("allowRegistrationPhaseExtension", Boolean.TRUE);
        } else {
            // Registration phase can NOT be extended
            registrationPhase = ActionsHelper.findPhaseByTypeName(phases, REGISTRATION_PHASE_NAME);
            request.setAttribute("allowRegistrationPhaseExtension", Boolean.FALSE);
        }
        if (registrationPhase != null) {
            request.setAttribute("registrationPhaseClosed", isClosed(registrationPhase));
            request.setAttribute("registrationPhase", registrationPhase);
            request.setAttribute("registrationPhaseDuration", registrationPhase.getLength() / HOUR_DURATION_IN_MILLIS);
        }
    }

    /**
     * <p>Binds the attributes to specified request which affect the <code>Extend Submission Phase</code> functionality
     * like indicating whether the <code>Submission</code> phase extension is allowed or not.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client to bind the flags
     *        related to phase extension to.
     * @param phases a <code>Phase</code> array listing the current project phases.
     */
    private void setSubmissionPhaseExtensionParameters(HttpServletRequest request, Phase[] phases) {
        Phase submissionPhase = ActionsHelper.findPhaseByTypeName(phases, SUBMISSION_PHASE_NAME);
        if (submissionPhase != null) {
            if (submissionPhaseAllowsExtension(submissionPhase)) {
                request.setAttribute("allowSubmissionPhaseExtension", Boolean.TRUE);
            }
            request.setAttribute("submissionPhase", submissionPhase);
            request.setAttribute("submissionPhaseDuration", submissionPhase.getLength() / HOUR_DURATION_IN_MILLIS);
            return;
        }
        request.setAttribute("allowSubmissionPhaseExtension", Boolean.FALSE);
    }

    /**
     * <p>Checks if specified <code>Submission</code> phase allows phase extension or not. The method returns
     * <code>true</code> if specified phase is not <code>null</code> and is not closed and there are at least 48 hours
     * left before submission phase deadline.</p>
     *
     * @param submissionPhase a <code>Phase</code> providing details for <code>Submission</code> phase.
     * @return <code>true</code> if specified phase allows phase extension; <code>false</code> otherwise.
     * @throws IllegalArgumentException if specified <code>submissionPhase</code> is not of <code>Submission</code>
     *         phase type.
     */
    private boolean submissionPhaseAllowsExtension(Phase submissionPhase) {
        if (submissionPhase != null) {
            String phaseTypeName = submissionPhase.getPhaseType().getName();
            if (!SUBMISSION_PHASE_NAME.equals(phaseTypeName)) {
                throw new IllegalArgumentException("Wrong phase. Expected Submission phase but provide phase is: "
                                                   + phaseTypeName);
            }
            Date submissionScheduledEndDate = submissionPhase.getScheduledEndDate();
            Integer minimumHoursBeforeSubmissionDeadlineForExtension
                = ConfigHelper.getMinimumHoursBeforeSubmissionDeadlineForExtension();
            if ((!isClosed(submissionPhase))
                && (getHoursLeft(submissionScheduledEndDate) >= minimumHoursBeforeSubmissionDeadlineForExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Recalculates scheduled start date and end date for all phases when a phase is extended.</p>
     *
     * @param allPhases all the phases for the project.
     */
    private void recalculateScheduledDates(Phase[] allPhases) {
        for (Phase phase : allPhases) {
            phase.setScheduledStartDate(phase.calcStartDate());
            phase.setScheduledEndDate(phase.calcEndDate());
        }
    }

    /**
     * <p>Validates that specified user which is going to be assigned specified role for specified project has accepted
     * all terms of use set for specified role in context of the specified project.</p>
     *
     * @param projectId a <code>long</code> providing the project ID.
     * @param userId a <code>long</code> providing the user ID.
     * @param roleId a <code>long</code> providing the role ID.
     * @return a <code>List</code> of terms of use which are not yet accepted by the specified user or empty list if all
     *         necessary terms of use are accepted.
     * @throws RemoteException if any errors occur during EJB remote invocation.
     */
    private List<TermsOfUse> validateResourceTermsOfUse(long projectId, long userId, long roleId)
        throws RemoteException, TermsOfUsePersistenceException {

        List<TermsOfUse> unAcceptedTerms = new ArrayList<TermsOfUse>();

        // get remote services
        ProjectTermsOfUseDao projectRoleTermsOfUse = ActionsHelper.getProjectTermsOfUseDao();
        UserTermsOfUseDao userTermsOfUse = ActionsHelper.getUserTermsOfUseDao();
        TermsOfUseDao termsOfUse = ActionsHelper.getTermsOfUseDao();

        Map<Integer, List<TermsOfUse>> necessaryTerms = projectRoleTermsOfUse.getTermsOfUse(
            (int) projectId, (int) roleId, null);

        for (List<TermsOfUse> t : necessaryTerms.values()) {
            for (TermsOfUse necessaryTerm : t) {
                long termsId = necessaryTerm.getTermsOfUseId();
                if (!userTermsOfUse.hasTermsOfUse(userId, termsId)) {
                    unAcceptedTerms.add(necessaryTerm);
                }
            }
        }
        return unAcceptedTerms;
    }

    /**
     * <p>Validates that specified user which is going to be assigned specified role for specified project has accepted
     * all terms of use set for specified role in context of the specified project.</p>
     *
     * @param projectId a <code>long</code> providing the project ID.
     * @param userId a <code>long</code> providing the user ID.
     * @return a <code>List</code> of terms of use which are not yet accepted by the specified user or empty list if all
     *         necessary terms of use are accepted.
     * @throws BaseException if an unexpected error occurs.
     */
    private boolean validateResourceEligibility(long projectId, long userId) throws BaseException {

        try {
            return EJBLibraryServicesLocator.getContestEligibilityService().isEligible(userId, projectId, false);
        } catch (ContestEligibilityValidatorException e) {
            throw new BaseException(e);
        }
    }

    /**
     * <p>Processes the incoming request for saving the review performance feedbacks for requested project.</p>
     *
     * @param mapping  an <code>ActionMapping</code> used for mapping the specified request to this action.
     * @param form     an <code>ActionForm</code> providing the form parameters mapped to specified request.
     * @param request  an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws Exception if an unexpected error occurs.
     * @since 1.3
     */
    @SuppressWarnings("unchecked")
    public ActionForward manageReviewFeedback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping,
                request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        request.setAttribute("activeTabIdx", 3);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                                                     PROJECT_MANAGEMENT_PERM_NAME, false);
        if (!verification.isSuccessful()) {
            return verification.getForward();
        } else {
            // Validate the forms
            final Project project = verification.getProject();
            final boolean reviewFeedbackAllowed = isReviewFeedbackAllowed(project);

            long currentUserId = AuthorizationHelper.getLoggedInUserId(request);
            String currentUserIdString = Long.toString(currentUserId);

            if (reviewFeedbackAllowed) {
                ReviewFeedbackManager reviewFeedbackManager = ActionsHelper.createReviewFeedbackManager();
                List<ReviewFeedback> existingReviewFeedbacks = reviewFeedbackManager.getForProject(project.getId());
                if (existingReviewFeedbacks.size() > 1) {
                    throw new BaseException("These should be at most 1 review feedback");
                }

                boolean updated = existingReviewFeedbacks.size() == 1;
                request.setAttribute("toEdit", updated);

                if (updated) {
                    // if current user is in the existing feedback details, he/she can't edit the review feedback
                    for (ReviewFeedbackDetail detail : existingReviewFeedbacks.get(0).getDetails()) {
                        if (detail.getReviewerUserId() == AuthorizationHelper.getLoggedInUserId(request)) {
                            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                                    PROJECT_MANAGEMENT_PERM_NAME,
                                    "Error.EditReviewFeedbackNotAllowed", Boolean.TRUE);
                        }
                    }
                }

                // Get the list of reviewers eligible for feedback and convert it to set of respective user IDs
                List<Resource> reviewerResources = getFeedbackEligibleReviewers(project.getId(), request);
                Set<Long> eligibleReviewerUserIds = new HashSet<Long>();
                for (Resource reviewer : reviewerResources) {
                    String reviewerUserId = (String) reviewer.getProperty("External Reference ID");
                    eligibleReviewerUserIds.add(Long.parseLong(reviewerUserId));
                }

                // Validate the input
                LazyValidatorForm lazyForm = (LazyValidatorForm) form;
                java.lang.Long[] reviewerUserIds = (java.lang.Long[]) lazyForm.get("reviewerUserId");
                java.lang.Integer[] reviewerScores = (java.lang.Integer[]) lazyForm.get("reviewerScore");
                java.lang.String[] reviewerFeedbacks = (java.lang.String[]) lazyForm.get("reviewerFeedback");
                Boolean unavailable = (Boolean) lazyForm.get("unavailable");
                unavailable = unavailable == null ? Boolean.FALSE : unavailable;
                String explanation = (String) lazyForm.get("explanation");

                if (!unavailable) {
                    for (int i = 0; i < reviewerUserIds.length; i++) {
                        if (reviewerScores.length <= i || reviewerScores[i] == null) {
                            ActionsHelper.addErrorToRequest(request, "reviewerScore[" + i + "]", new ActionMessage(
                                "error.com.cronos.onlinereview.actions.manageProject.ReviewPerformance.Score.Empty"));
                        } else if (reviewerScores[i] < 0 || reviewerScores[i] > 2) {
                            ActionsHelper.addErrorToRequest(request, "reviewerScore[" + i + "]", new ActionMessage(
                                "error.com.cronos.onlinereview.actions.manageProject.ReviewPerformance.Score.Invalid"));
                        }
                        if (reviewerFeedbacks.length <= i || reviewerFeedbacks[i] == null
                            || reviewerFeedbacks[i].trim().length() == 0) {
                            ActionsHelper.addErrorToRequest(request, "reviewerFeedback[" + i + "]", new ActionMessage(
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.Feedback.Empty"));
                        } else if (reviewerFeedbacks[i].length() > MAX_FEEDBACK_LENGTH) {
                            ActionsHelper.addErrorToRequest(request, "reviewerFeedback[" + i + "]", new ActionMessage(
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.Feedback.MaxExceeded"));
                        }
                        if (currentUserId == reviewerUserIds[i]) {
                            ActionsHelper.addErrorToRequest(request, "reviewerFeedback[" + i + "]", new ActionMessage(
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.SelfFeedbackProhibited"));
                        } else if (!eligibleReviewerUserIds.contains(reviewerUserIds[i])) {
                            ActionsHelper.addErrorToRequest(request, "reviewerFeedback[" + i + "]", new ActionMessage(
                                "error.com.cronos.onlinereview.actions.manageProject.ReviewPerformance.WrongReviewer"));
                        }
                        eligibleReviewerUserIds.remove(reviewerUserIds[i]);
                    }
                    if (eligibleReviewerUserIds.size() > 0) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.MissingReviewer");
                    }
                } else {
                    if (explanation == null || explanation.trim().length() == 0) {
                        ActionsHelper.addErrorToRequest(request, "explanation", new ActionMessage(
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.Explanation.Empty"));
                    } else if (explanation.length() > MAX_FEEDBACK_LENGTH) {
                        ActionsHelper.addErrorToRequest(request, "explanation", new ActionMessage(
                                "error.com.cronos.onlinereview.actions.manageProject." +
                                        "ReviewPerformance.Explanation.MaxExceeded"));
                    }
                }
                if (!ActionsHelper.isErrorsPresent(request)) {
                    ReviewFeedback feedback;
                    if (updated) {
                        feedback = existingReviewFeedbacks.get(0);
                    } else {
                        feedback = new ReviewFeedback();
                        feedback.setProjectId(project.getId());
                    }
                    List<ReviewFeedbackDetail> feedbackDetails = new ArrayList<ReviewFeedbackDetail>();
                    if (unavailable) {
                        feedback.setComment(explanation);
                    } else {
                        feedback.setComment(null);
                        // Save the feedback details
                        for (int i = 0; i < reviewerUserIds.length; i++) {
                            ReviewFeedbackDetail feedbackDetail = new ReviewFeedbackDetail();
                            feedbackDetail.setFeedbackText(reviewerFeedbacks[i]);
                            feedbackDetail.setReviewerUserId(reviewerUserIds[i]);
                            feedbackDetail.setScore(reviewerScores[i]);
                            feedbackDetails.add(feedbackDetail);
                        }
                    }
                    feedback.setDetails(feedbackDetails);
                    if (updated) {
                        reviewFeedbackManager.update(feedback, currentUserIdString);
                    } else {
                        reviewFeedbackManager.create(feedback, currentUserIdString);
                    }
                }
            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                                                        PROJECT_MANAGEMENT_PERM_NAME,
                                                        "Error.ReviewFeedbackNotAllowed", Boolean.TRUE);
            }

            if (ActionsHelper.isErrorsPresent(request)) {
                initProjectManagementConsole(request, project);
                return mapping.getInputForward();
            } else {
                return ActionsHelper.cloneForwardAndAppendToPath(
                    mapping.findForward(SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
            }
        }
    }

    /**
     * <p>Retrieves data for displaying by Review Performance tab when Project Management Console page is displayed to 
     * user.</p>
     * 
     * @param request an <code>HttpServletRequest</code> representing incoming request from client. 
     * @param project a <code>Project</code> providing the data for the project being managed.
     * @throws BaseException if an unexpected error occurs.
     * @since 1.3
     */
    private void initReviewFeedbackIntegration(HttpServletRequest request, Project project) throws BaseException {
        
        // Retrieve existing review feedbacks for project
        ReviewFeedbackManager reviewFeedbackManager = ActionsHelper.createReviewFeedbackManager();
        List<ReviewFeedback> reviewFeedbacks = reviewFeedbackManager.getForProject(project.getId());

        if (reviewFeedbacks.size() > 1) {
            throw new BaseException("These should be at most 1 review feedback");
        }

        final boolean reviewFeedbackAllowed = isReviewFeedbackAllowed(project);
        
        // Get the resources for the reviewers (if necessary)
        Boolean toEdit = (Boolean) request.getAttribute("toEdit");
        toEdit = toEdit != null && toEdit;
        if (reviewFeedbackAllowed && (reviewFeedbacks.size() == 0 || toEdit)) {
            List<Resource> reviewerResources = getFeedbackEligibleReviewers(project.getId(), request);
            Map<String, Resource> reviewerResourcesMap = new TreeMap<String, Resource>();
            for (Resource reviewer : reviewerResources) {
                String reviewerUserId = (String) reviewer.getProperty("External Reference ID");
                reviewerResourcesMap.put(reviewerUserId, reviewer);
            }

            request.setAttribute("reviewerResourcesMap", reviewerResourcesMap);
        }

        // Bind data to request
        request.setAttribute("feedback", reviewFeedbacks.size() == 0 ? null : reviewFeedbacks.get(0));
        request.setAttribute("reviewFeedbackAllowed", reviewFeedbackAllowed);
    }

    /**
     * <p>Gets the list of reviewers eligible for review feedback for specified project. Such reviewer resources will 
     * have their reviews committed and resource matching the current user will be excluded from the resulting list.</p>
     * 
     * @param projectId a <code>long</code> providing the ID of a project.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client. 
     * @return a <code>List</code> of reviewers for specified project which the feedback can be provided for. 
     * @throws LookupException if an unexpected error occurs.
     * @throws ResourcePersistenceException if an unexpected error occurs.
     * @throws SearchBuilderException if an unexpected error occurs.
     * @since 1.3
     */
    private List<Resource> getFeedbackEligibleReviewers(long projectId, HttpServletRequest request)
        throws LookupException, ResourcePersistenceException, SearchBuilderException, ReviewManagementException {
        // Build filter for reviewer role names
        List<Filter> reviewerRoleFilters = new ArrayList<Filter>();
        for (String reviewerRoleName : REVIEW_FEEDBACK_ELIGIBLE_ROLE_NAMES) {
            Filter resourceRoleIdFilter = ResourceFilterBuilder
                .createResourceRoleIdFilter(LookupHelper.getResourceRole(reviewerRoleName).getId());
            reviewerRoleFilters.add(resourceRoleIdFilter);
        }
        OrFilter reviewerRolesFilter = new OrFilter(reviewerRoleFilters);

        // Build filter for searching reviewers for project
        AndFilter filter = new AndFilter(reviewerRolesFilter, ResourceFilterBuilder.createProjectIdFilter(projectId));

        // Search reviewers for project
        ResourceManager resourceManager = ActionsHelper.createResourceManager();
        List<Resource> reviewerResources 
            = new ArrayList<Resource>(Arrays.asList(resourceManager.searchResources(filter)));

        // Build list of reviewer resource IDs
        List<Long> reviewerResourceIds = new ArrayList<Long>();
        for (Resource reviewerResource : reviewerResources) {
            reviewerResourceIds.add(reviewerResource.getId());
        }

        // Get reviews for project
        ReviewManager reviewManager = ActionsHelper.createReviewManager();
        Filter filterProject = new EqualToFilter("project", projectId);
        InFilter reviewerResourcesFilter = new InFilter("reviewer", reviewerResourceIds);
        Filter committedReviewFilter = new EqualToFilter("committed", 1);
        AndFilter reviewFilter = new AndFilter(Arrays.asList(filterProject, reviewerResourcesFilter,
                                                             committedReviewFilter));
        Review[] reviews = reviewManager.searchReviews(reviewFilter, false);

        // Collect the IDs of committed review author resources
        Set<Long> committedReviewAuthors = new HashSet<Long>();
        for (Review review : reviews) {
            if (review.isCommitted()) {
                committedReviewAuthors.add(review.getAuthor());
            }
        }
        
        // Filter out those reviewer resources who either do not have committed reviews or correspond to current user
        String currentUserId = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        Iterator<Resource> reviewersIterator = reviewerResources.iterator();
        while (reviewersIterator.hasNext()) {
            Resource reviewer = reviewersIterator.next();
            if (committedReviewAuthors.contains(reviewer.getId())) {
                String reviewerUserId = (String) reviewer.getProperty("External Reference ID");
                if (currentUserId.equalsIgnoreCase(reviewerUserId)) {
                    reviewersIterator.remove();
                }
            } else {
                reviewersIterator.remove();
            }
        }

        return reviewerResources;
    }

    /**
     * Checks whether the current user can manage the review feedback for the specific project.
     *
     * @param project the specific project.
     * @return true if the current user can manage the review feedback for the specific project, false otherwise.
     * @throws BaseException if any error occurs.
     * @since 1.6
     */
    private boolean isReviewFeedbackAllowed(Project project) throws BaseException {
        final Phase[] phases = getProjectPhases(project);

        // Check if project allows review feedback creation
        String reviewFeedbackFlag = (String) project.getProperty("Review Feedback Flag");
        boolean reviewFeedbackFlagSet = "true".equalsIgnoreCase(reviewFeedbackFlag);

        // Check if Appeals Response phase or Review phase (if Appeals Response phase is missing) is closed
        Phase phase = ActionsHelper.findPhaseByTypeName(phases, Constants.APPEALS_RESPONSE_PHASE_NAME);
        if (phase == null) {
            phase = ActionsHelper.findPhaseByTypeName(phases, Constants.REVIEW_PHASE_NAME);
        }
        final boolean feedbackStopperPhaseIsClosed
                = phase != null && ActionsHelper.isPhaseClosed(phase.getPhaseStatus());
        return reviewFeedbackFlagSet && feedbackStopperPhaseIsClosed;
    }

    /**
     * This static method helps to create the default project payment calculator.
     *
     * @return the instance of default project payment calculator.
     * @throws com.topcoder.util.errorhandling.BaseRuntimeException if any error occurs
     * @since  1.4
     */
    private static ProjectPaymentCalculator createDefaultProjectPaymentCalculator() throws BaseRuntimeException {
        String className = null;
        try {
            ConfigManager cfgMgr = ConfigManager.getInstance();
            Property config = cfgMgr.getPropertyObject("com.cronos.OnlineReview", "DefaultProjectPaymentConfig");
            className = config.getValue("CalculatorClass");
            Class clazz = Class.forName(className);
            return (ProjectPaymentCalculator) clazz.newInstance();
        } catch (Exception e) {
            throw new BaseRuntimeException("Failed to instantiate the project payment calculator of type: "
                    + className, e);
        }
    }

    /**
     * This interface provides an abstraction to the origin of the distribution file. It may be
     * read directly from the file system, from memory or from an uploaded file.
     *
     * @since 1.1
     */
    private static interface DistributionFileDescriptor {
        /**
         * <p>
         * Returns the file name.
         * </p>
         *
         * @return the file name.
         */
        public abstract String getFileName();

        /**
         * <p>
         * Returns an input stream that will be used read the file contents.
         * </p>
         *
         * @return the input stream that will be used read the file contents.
         * @throws IOException if any error occurs while creating the input stream.
         */
        public abstract InputStream getInputStream() throws IOException;
    }
}
