/*
 * Copyright (C) 2004 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import static com.cronos.onlinereview.actions.Constants.AGGREGATION_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.AGGREGATION_REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.APPEALS_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.APPEALS_RESPONSE_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.APPROVAL_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.FINAL_FIX_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.FINAL_REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.CHECKPOINT_REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.CHECKPOINT_SCREENING_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.CHECKPOINT_SUBMISSION_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.POST_MORTEM_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.REGISTRATION_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.SCREENING_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.SPECIFICATION_REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.SPECIFICATION_SUBMISSION_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.SUBMISSION_PHASE_NAME;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cronos.onlinereview.phases.PaymentsHelper;
import com.cronos.termsofuse.dao.ProjectTermsOfUseDao;
import com.cronos.termsofuse.dao.UserTermsOfUseDao;
import com.cronos.termsofuse.model.TermsOfUse;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.ProjectPaymentManager;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.LazyValidatorForm;

import com.cronos.onlinereview.dataaccess.DeliverableDataAccess;
import com.cronos.onlinereview.dataaccess.ProjectDataAccess;
import com.cronos.onlinereview.dataaccess.ProjectPhaseDataAccess;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.model.ClientProject;
import com.cronos.onlinereview.model.CockpitProject;
import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.date.workdays.WorkdaysUnitOfTime;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.DeliverableManager;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Prize;
import com.topcoder.management.project.PrizeType;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardSearchBundle;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.CyclicDependencyException;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidatorException;
import com.topcoder.shared.util.DBMS;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.web.common.RowNotFoundException;
import com.topcoder.web.ejb.user.UserPreference;

/**
 * This class contains Struts Actions that are meant to deal with Projects. There are following
 * Actions defined in this class:
 * <ul>
 * <li>New Project</li>
 * <li>Edit Project</li>
 * <li>Save Project</li>
 * <li>List Projects</li>
 * </ul>
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * <p>
 * Version 1.1 (Configurable Contest Terms Release Assembly v1.0) Change notes:
 *   <ol>
 *     <li>Added Project Role User Terms Of Use association generation to project creation.</li>
 *     <li>Added Project Role User Terms Of Use verification when adding/updating project resources.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2 (Appeals Early Completion Release Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added Appeals Completed Early flag manipulation when project is saved.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.3 (Appeals Early Completion Release Assembly 2.0) Change notes:
 *   <ol>
 *     <li>Added sort order parameter to project role terms of use service call.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.4 (Competition Registration Eligibility v1.0) Change notes:
 *   <ol>
 *     <li>Removed old "Public" and "Eligibility" project info code. Public projects are now determined by contest
 *         eligibility service.</li>
 *     <li>Added contest eligibility validation to <code>checkForCorrectProjectId</code> method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.5 (Contest Dependency Automation v1.0) Change notes:
 *   <ol>
 *     <li>
 *       Updated {@link #saveProjectPhases(boolean, HttpServletRequest, LazyValidatorForm, Project, Map, List, boolean)}
 *       method to adjust the start times (if necessary) for projects which depend on current project being updated.
 *     </li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.6 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 *   <ol>
 *     <li>
 *       Updated {@link #saveProject(org.apache.struts.action.ActionMapping,org.apache.struts.action.ActionForm,javax.servlet.http.HttpServletRequest)} method to
 *       validate that category selected for project is not generic one.
 *     </li>
 *     <li>
 *       Updated {@link #validateProjectPhases(HttpServletRequest, Project, Phase[])} method to take into consideration
 *       <code>Approval</code> and <code>Post-Mortem</code> phases.
 *     </li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.7 (Online Review Performance Refactoring 1.0) Change notes:
 *   <ol>
 *     <li>
 *       Updated {@link #listProjects(org.apache.struts.action.ActionMapping,javax.servlet.http.HttpServletRequest)} method to
 *       speed up the projects data retrieval using Query Tool.
 *     </li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.8 (Member Post-Mortem Review Assembly 1.0) Change notes:
 *   <ol>
 *     <li>
 *       Updated {@link #saveResources(javax.servlet.http.HttpServletRequest,org.apache.struts.validator.LazyValidatorForm,com.topcoder.management.project.Project,com.topcoder.project.phases.Phase[],java.util.Map} method to
 *       deny removal, handle/role update for resources which have submissions or review scorecards associated with
 *       them.
 *     </li>
 *     <li>
 *       Updated {@link #populateProjectForm(HttpServletRequest, LazyValidatorForm, Project)} method to
 *       deny removal, handle/role update for resources which have submissions or review scorecards associated with
 *       them.
 *     </li>
 *     <li>Added {@link #REVIEWER_ROLE_NAMES} constant.</li>
 *     <li>Added {@link #findResourcesWithReviewsForProject(ReviewManager, Long)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.9 (Impersonation Login Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #loadProjectEditLookups(HttpServletRequest)} method to filter out resource roles which are
 *     not allowed for selection on <code>Edit Project</code> screen.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.10 (Specification Review Part 1 Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Removed dependency on <code>ContestDependencyAutomation</code> class.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.11 (SVN Automation and Late Deliverables Tracking Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #saveProject(org.apache.struts.action.ActionMapping,org.apache.struts.action.ActionForm,javax.servlet.http.HttpServletRequest)} method to
 *     skip updating the <code>SVN Module</code> property of the project.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.12 (Online Review Payments and Status Automation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #saveProjectPhases(boolean, HttpServletRequest, LazyValidatorForm, Project, Map, List, boolean)} method to
 *     support negative lag time, notes, workdays component is updated to support the functionality.</li>
 *     <li>Updated {@link #saveResources(javax.servlet.http.HttpServletRequest,org.apache.struts.validator.LazyValidatorForm,com.topcoder.management.project.Project,com.topcoder.project.phases.Phase[],java.util.Map} method is
 *     updated to support reset to resource related to project phase properly.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.12 (Checkpoint Support Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added support for <code>Checkpoint</code> phases.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.12.1 (Online Review Replatforming Release 2) Change notes:
 *   <ol>
 *     <li>Change submission.getUplaods.get(0) to submission.getUpload().</li>
 *   </ol>
 * </p>
 * <p>
 * Version 1.13 (Online Review Status Validation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Methods adjusted for new signatures of create managers methods from ActionsHelper.</li>
 *     <li>Updated {@link #saveProjectPhases()} to handle new StatusValidationException and display its error message.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.13.1 (Online Review Phases 1.6.1 Integration) Change notes:
 *   <ol>
 *     <li>Removed Manual Screening support</li>
 *     <li>Removed Submission Number support</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.14 (Online Review - Project Payments Integration Part 1 v1.0) Change notes:
 *  <ol>
 *      <li>Updated {@link #newProject(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *      method to set the last modification timestamp.</li>
 *      <li>Updated {@link #populateProjectForm(HttpServletRequest, LazyValidatorForm, Project)} method to
 *      remove the old payments function and add support for the new project prizes management.</li>
 *      <li>Added {@link #canEditPrize(long)} method to check whether we can edit project prizes.</li>
 *      <li>Updated {@link #saveProject(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} method
 *      to add support for optimistic concurrency control and project prizes management.</li>
 *      <li>Added {@link #validateProjectPrizes(HttpServletRequest, String[], Integer[], String)}
 *      method to validate the project prizes submiited by user.</li>
 *      <li>Added {@link #getProjectPrizesToBeUpdated(HttpServletRequest, LazyValidatorForm, Project, List,
 *      List, List)} and
 *      {@link #getProjectPrizesToBeUpdated(PrizeType, List, List, List, List, String[], Integer[])} method to
 *      collect user submiited project prizes data and determine which prizes should be created, updated or
 *      removed.</li>
 *  </ol>
 * </p>
 *
 * <p>
 * Version 1.15 (Online Review - Project Payments Integration Part 3 v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #populateProjectForm(HttpServletRequest, LazyValidatorForm, Project)} and
 *     {@link #saveResources(HttpServletRequest, LazyValidatorForm, Project, Phase[], Map)} methods to remove
 *     the resource payment property related code.</li>
 *     <li>Updated {@link #saveResources(HttpServletRequest, LazyValidatorForm, Project, Phase[], Map)} method
 *     to validate that there are no paid payments for the resource when deleting a resource, and delete all
 *     project payments when deleting a resource.</li>
 *     <li>Added {@link #setResourcePaidRequestAttribute(Project, HttpServletRequest)} to set the resource paid data
 *     for a given project to the request attribute.</li>
 *     <li>Updated {@link #setEditProjectRequestAttributes(HttpServletRequest, Project, Resource[], ExternalUser[],
 *     Phase[])} method to call {@link #setResourcePaidRequestAttribute(Project, HttpServletRequest)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.16 (Online Review - Iterative Review v1.0) Change notes:
 *   <ol>
 *     <li>Added iterative review scorecards.</li>
 *     <li>Added logic to handle duplicate iterative reviewers.</li>
 *     <li>Fixed the bug when validation failed during creating new project.</li>
 *   </ol>
 * </p>
 *
 * @author George1, real_vg, pulky, isv, FireIce, lmmortal, flexme, duxiaoyang
 * @version 1.16
 * @since 1.0
 */
public class ProjectActions extends DispatchAction {

    /**
     * This constant stores development project type id
     *
     * @since 1.1
     */
    private static final int DEVELOPMENT_PROJECT_TYPE_ID = 2;

    /**
     * Default sort order for project role terms of use generation
     *
     * @since 1.3
     */
    private static final int DEFAULT_TERMS_SORT_ORDER = 1;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM.dd.yyyy hh:mm a", Locale.US);

    /**
     * <p>A <code>Set</code> holding the names for all reviewer roles.</p>
     *
     * @since 1.8
     */
    private static final Set<String> REVIEWER_ROLE_NAMES = new HashSet<String>(Arrays.asList("Reviewer",
        "Accuracy Reviewer", "Failure Reviewer", "Stress Reviewer", "Screener", "Primary Screener", "Aggregator",
        "Final Reviewer", "Approver", "Post-Mortem Reviewer", "Specification Reviewer", "Checkpoint Screener", 
        "Checkpoint Reviewer", "Iterative Reviewer"));

    /**
     * <p>A <code>Set</code> holding the IDs for reviewer roles which do not allow duplicate users to be assigned to.
     * </p>
     *
     * @since 1.8
     */
    private static final Set<Long> NODUPLICATE_REVIEWER_ROLE_IDS = new HashSet<Long>(Arrays.asList(4L, 5L, 6L, 7L));

    /**
     * <p>A long number holding the ID for iterative reviewer role.</p>
     *
     * @since 1.16
     */
    private static final long ITERATIVE_REVIEWER_ROLE_ID = 21L;

    /**
     * <p>A <code>Set</code> holding the IDs for reviewer roles which do not allow more than one user to be assigned to.
     * </p>
     *
     * @since 1.8
     */
    private static final Set<Long> SINGLE_REVIEWER_ROLE_IDS = new HashSet<Long>(Arrays.asList(2L, 8L, 9L, 18L, 19L, 20L));
    
    private final static int GLOBAL_TIMELINE_NOTIFICATION = 29;

    private final static int GLOBAL_FORUM_WATCH = 30;
    
    private UserPreference userPreference;
    
    /**
     * Creates a new instance of the <code>ProjectActions</code> class.
     */
    public ProjectActions() {
        userPreference = EJBLibraryServicesLocator.getUserPreference();
    }

    /**
     * This method is an implementation of &quot;New Project&quot; Struts Action defined for this
     * assembly, which is supposed to fetch lists of project types and categories from the database
     * and pass it to the JSP page to use it for populating appropriate drop down lists.
     *
     * <p>
     * Updated for Online Review - Project Payments Integration Part 1 v1.0
     *      - Sets the flags indicating whether user can edit contest/checkpoint prizes to request attribute.
     *      - Sets last modification time to request attribute.
     * </p>
     *
     * @return &quot;success&quot; forward that forwards to the /jsp/editProject.jsp page (as
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
     *             when any error happens while processing in TCS components
     */
    public ActionForward newProject(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check if the user has the permission to perform this action
        if (!AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)) {
            // If he doesn't, redirect the request to login page or report about the lack of permissions
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.CREATE_PROJECT_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Place the index of the active tab into the request
        request.setAttribute("projectTabIndex", 3);
        // Place the flag, indicating that we are creating a new project, into request
        request.setAttribute("newProject", Boolean.TRUE);

        LazyValidatorForm formNewProject = (LazyValidatorForm) form;

        // Make 'Send Email Notifications' and
        // 'Receive Timeline Notifications' checkboxes be checked by default
        formNewProject.set("email_notifications", Boolean.TRUE);
        formNewProject.set("timeline_notifications", Boolean.TRUE);

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
        populateProjectFormDefaults(formNewProject, request);
        
        // Populate default last modification timestamp
        formNewProject.set("last_modification_time", 0L);

        // Return the success forward
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Document this method.
     *
     * @param lazyForm
     * @param request
     */
    private void populateProjectFormDefaults(LazyValidatorForm lazyForm, HttpServletRequest request) {
        // Set the JS id to start generation from
        lazyForm.set("js_current_id", (long) 0);

        // Populate form with some data so that resources row template
        // is rendered properly by the appropriate JSP
        lazyForm.set("resources_role", 0, (long) -1);
        lazyForm.set("resources_id", 0, (long) -1);
        lazyForm.set("resources_action", 0, "add");

        // Populate form with some data so that phases row template
        // is rendered properly by the appropriate JSP
        lazyForm.set("phase_id", 0, (long) -1);
        lazyForm.set("phase_action", 0, "add");
        lazyForm.set("phase_can_open", 0, Boolean.TRUE);
        lazyForm.set("phase_can_close", 0, Boolean.FALSE);
        lazyForm.set("phase_use_duration", 0, Boolean.TRUE);

        // Populate some phase criteria with default values read from the configuration
        if (ConfigHelper.getDefaultRequiredRegistrants() >= 0) {
            lazyForm.set("phase_required_registrations", 0, ConfigHelper.getDefaultRequiredRegistrants());
        }
        if (ConfigHelper.getDefaultRequiredReviewers() >= 0) {
            lazyForm.set("phase_required_reviewers", 0, ConfigHelper.getDefaultRequiredReviewers());
        }
        if (ConfigHelper.getDefaultRequiredApprovers() >= 0) {
            request.setAttribute("phase_required_reviewers_approval",
                    ConfigHelper.getDefaultRequiredApprovers());
        }
        if (ConfigHelper.getDefaultRequiredPostMortemReviewers() >= 0) {
            request.setAttribute("phase_required_reviewers_postmortem",
                    ConfigHelper.getDefaultRequiredPostMortemReviewers());
        }

        // Populate default phase duration
        lazyForm.set("addphase_duration", String.valueOf(ConfigHelper.getDefaultPhaseDuration()));
    }

    /**
     * This method loads the lookup data needed for rendering the Create Project/New Project pages.
     * The loaded data is stored in the request attributes.
     *
     * <p>
     * Updated for Online Review Update - Add Project Dropdown v1.0
     *      Added retrieval of billing projects.
     * </p>
     *
     * @param request the request to load the lookup data into
     * @throws BaseException if any error occurs while loading the lookup data
     */
    private void loadProjectEditLookups(HttpServletRequest request) throws BaseException {
        // Obtain an instance of Project Manager
        ProjectManager projectManager = ActionsHelper.createProjectManager();

        // Retrieve project types and categories
        ProjectType[] projectTypes = projectManager.getAllProjectTypes();
        ProjectCategory[] projectCategories = projectManager.getAllProjectCategories();

        // Store the retrieved types and categories in request
        request.setAttribute("projectTypes", projectTypes);
        request.setAttribute("projectCategories", projectCategories);
        request.setAttribute("projectCategoriesMap", buildProjectCategoriesLookupMap(projectCategories));

        // Obtain an instance of Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager();
        // Get all types of resource roles and filter out those which are not allowed for selection
        // Place resource roles into the request as attribute
        ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();
        Set<String> disabledResourceRoles = new HashSet<String>(Arrays.asList(ConfigHelper.getDisabledResourceRoles()));
        List<ResourceRole> allowedResourceRoles = new ArrayList<ResourceRole>();
        for (ResourceRole resourceRole : resourceRoles) {
            if (!disabledResourceRoles.contains(String.valueOf(resourceRole.getId()))) {
                allowedResourceRoles.add(resourceRole);
            }
        }
        request.setAttribute("allowedResourceRoles", allowedResourceRoles);
        request.setAttribute("resourceRoles", resourceRoles);
        request.setAttribute("disabledResourceRoles", disabledResourceRoles);

        // Obtain an instance of Phase Manager
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
        // Get all phase types
        PhaseType[] phaseTypes = phaseManager.getAllPhaseTypes();
        // Place them into request as an attribute
        request.setAttribute("phaseTypes", phaseTypes);
        request.setAttribute("arePhaseDependenciesEditable", true);

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorecardManager = ActionsHelper.createScorecardManager();

        // TODO: Check if we need to filter by the project category
        // Retrieve the scorecard lists
        Scorecard[] screeningScorecards = searchActiveScorecards(scorecardManager, "Screening");
        Scorecard[] reviewScorecards = searchActiveScorecards(scorecardManager, "Review");
        Scorecard[] approvalScorecards = searchActiveScorecards(scorecardManager, "Approval");
        Scorecard[] postMortemScorecards = searchActiveScorecards(scorecardManager, "Post-Mortem");
        Scorecard[] specificationReviewScorecards = searchActiveScorecards(scorecardManager, "Specification Review");
        Scorecard[] checkpointScreeningScorecards = searchActiveScorecards(scorecardManager, "Checkpoint Screening");
        Scorecard[] checkpointReviewScorecards = searchActiveScorecards(scorecardManager, "Checkpoint Review");
        Scorecard[] iterativeReviewScorecards = searchActiveScorecards(scorecardManager, "Iterative Review");

        // Store them in the request
        request.setAttribute("screeningScorecards", screeningScorecards);
        request.setAttribute("reviewScorecards", reviewScorecards);
        request.setAttribute("approvalScorecards", approvalScorecards);
        request.setAttribute("postMortemScorecards", postMortemScorecards);
        request.setAttribute("specificationReviewScorecards", specificationReviewScorecards);
        request.setAttribute("checkpointScreeningScorecards", checkpointScreeningScorecards);
        request.setAttribute("checkpointReviewScorecards", checkpointReviewScorecards);
        request.setAttribute("iterativeReviewScorecards", iterativeReviewScorecards);
        request.setAttribute("defaultScorecards", ActionsHelper.getDefaultScorecards());

        // Load phase template names
        String[] phaseTemplateNames = ActionsHelper.createPhaseTemplate().getAllTemplateNames();
        request.setAttribute("phaseTemplateNames", phaseTemplateNames);

        // since Online Review Update - Add Project Dropdown v1.0
        // Retrieve the list of all client projects and store it in the request
        // this need to be retrieved only for admin user.
        if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)) {
            request.setAttribute("billingProjects", ActionsHelper.getClientProjects(request));
            request.setAttribute("cockpitProjects", ActionsHelper.getCockpitProjects(request));
        }
    }

    /**
     * TODO: Document it
     * Note, that the scorecard data(items) is not fully retrieved
     *
     * @param scorecardManager
     * @param scorecardTypeName
     * @return
     * @throws BaseException
     */
    private Scorecard[] searchActiveScorecards(ScorecardManager scorecardManager, String scorecardTypeName) throws BaseException {
        Filter filter = ScorecardSearchBundle.buildAndFilter(
                ScorecardSearchBundle.buildTypeNameEqualFilter(scorecardTypeName),
                ScorecardSearchBundle.buildStatusNameEqualFilter("Active"));
        return scorecardManager.searchScorecards(filter, false);
    }

    /**
     * This method populates the specified LazyValidatorForm with the values
     * taken from the specified Project.
     *
     * <p>
     * Updated for Online Review Update - Add Project Dropdown v1.0
     *      - Set the 'Billing Project' value to form's billing_project property.
     *      - Set the isAdmin property.
     * </p>
     *
     * <p>
     * Updated for Online Review - Project Payments Integration Part 1 v1.0
     *      - Remove the old project payment information and set the new project prizes data.
     *      - Set the last modification timestamp information to the request attribute.
     * </p>
     *
     * @param request
     *            the request to be processed
     * @param form
     *            the form to be populated with data
     * @param project
     *            the project to take the data from
     * @throws BaseException
     */
    private void populateProjectForm(HttpServletRequest request, LazyValidatorForm form, Project project)
        throws BaseException {
        // TODO: Possibly use string constants instead of hardcoded strings

        // Populate project id
        form.set("pid", project.getId());

        // Populate project name
        populateProjectFormProperty(form, String.class, "project_name", project, "Project Name");

        // Populate project type
        Long projectTypeId = project.getProjectCategory().getProjectType().getId();
        form.set("project_type", projectTypeId);

        // Populate project category
        Long projectCategoryId = project.getProjectCategory().getId();
        form.set("project_category", projectCategoryId);

        // Populate project category
        Long projectStatusId = project.getProjectStatus().getId();
        form.set("status", projectStatusId);

        // Populate project forum id
        populateProjectFormProperty(form, Long.class, "forum_id", project, "Developer Forum ID");

        // Populate project component id
        populateProjectFormProperty(form, Long.class, "component_id", project, "Component ID");
        // Populate project external reference id
        populateProjectFormProperty(form, Long.class, "external_reference_id", project, "External Reference ID");
        // Populate project dr points
        populateProjectFormProperty(form, Double.class, "dr_points", project, "DR points");

        // since Online Review Update - Add Project Dropdown v1.0
        // Populate project billing project
        populateProjectFormProperty(form, Long.class, "billing_project", project, "Billing Project");
        form.set("cockpit_project", (long) project.getTcDirectProjectId());

        // Populate project autopilot option
        form.set("autopilot", "On".equals(project.getProperty("Autopilot Option")));
        // Populate project status notification option
        form.set("email_notifications", "On".equals(project.getProperty("Status Notification")));
        // Populate project timeline notification option
        form.set("timeline_notifications", "On".equals(project.getProperty("Timeline Notification")));
        // Populate project Digital Run option
        form.set("digital_run_flag", "On".equals(project.getProperty("Digital Run Flag")));
        // Populate project's 'do not rate this project' option
        // Note, this property is inverse by its meaning in project and form
        form.set("no_rate_project", !("Yes".equals(project.getProperty("Rated"))));

        // Populate project SVN module
        populateProjectFormProperty(form, String.class, "SVN_module", project, "SVN Module");
        // Populate project notes
        populateProjectFormProperty(form, String.class, "notes", project, "Notes");

        // Obtain Resource Manager instance
        ResourceManager resourceManager = ActionsHelper.createResourceManager();

        // Retrieve the list of the resources associated with the project
        Resource[] resources =
            resourceManager.searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));
        // Get an array of external users for the corresponding resources
        ExternalUser[] externalUsers =
            ActionsHelper.getExternalUsersForResources(ActionsHelper.createUserRetrieval(request), resources);

        // Populate form with resources data
        for (int i = 0; i < resources.length; ++i) {
            form.set("resources_id", i + 1, resources[i].getId());
            form.set("resources_action", i + 1, "update");

            form.set("resources_role", i + 1, resources[i].getResourceRole().getId());
            form.set("resources_phase", i + 1, "loaded_" + resources[i].getPhase());
            form.set("resources_name", i + 1, externalUsers[i].getHandle());
        }

        // Populate project prizes to form
        List<Prize> prizes = project.getPrizes();
        if (prizes != null) {
            PrizeType contestPrize = LookupHelper.getPrizeType(Constants.CONTEST_PRIZE_TYPE_NAME);
            PrizeType checkpointPrize = LookupHelper.getPrizeType(Constants.CHECKPOINT_PRIZE_TYPE_NAME);
            for (Prize prize : prizes) {
                if (prize.getPrizeType().getId() == contestPrize.getId()) {
                    form.set("contest_prizes_amount", prize.getPlace() - 1, String.valueOf(prize.getPrizeAmount()));
                    form.set("contest_prizes_num", prize.getPlace() - 1, prize.getNumberOfSubmissions());
                } else if (prize.getPrizeType().getId() == checkpointPrize.getId()) {
                    form.set("checkpoint_prizes_amount", prize.getPlace() - 1, String.valueOf(prize.getPrizeAmount()));
                    form.set("checkpoint_prizes_num", prize.getPlace() - 1, prize.getNumberOfSubmissions());
                }
            }
        }

        // Obtain Phase Manager instance
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);

        // Retrieve project phases
        Phase[] phases = ActionsHelper.getPhasesForProject(phaseManager, project);
        // Sort project phases
        Arrays.sort(phases, new Comparators.ProjectPhaseComparer());

        setEditProjectPhasesData(form, phases, false);
        setEditProjectRequestAttributes(request, project, resources, externalUsers, phases);

        // Populate last modification timestamp
        form.set("last_modification_time", ActionsHelper.getLastModificationTime(project, phases).getTime());

        // since Online Review Update - Add Project Dropdown v1.0
        boolean isAdmin = AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME);
        request.setAttribute("isAdmin", isAdmin);

       // start BUGR 4039 - Check whether the billing project id is in the user's allowed billing projects list
       List<ClientProject> availableClientProjects = ActionsHelper.getClientProjects(request);
       Long currentClientProjectId = (Long) form.get("billing_project");
       boolean inList = false;

       if(currentClientProjectId == null) {
            // no billing project yet, allow set
            inList = true;
        } else {
            for(ClientProject cp : availableClientProjects) {
                if(cp.getId() == currentClientProjectId) {
                    inList = true;
                    break;
                }
            }
        }

       request.setAttribute("allowBillingEdit", isAdmin && inList);
       // end BUG-4039

       List<CockpitProject> availableCockpitProjects = ActionsHelper.getCockpitProjects(request);
       Long currentCockpitProjectId = (Long) form.get("cockpit_project");
       inList = false;
       if (currentCockpitProjectId == null) {
           inList = true;
       } else {
           for (CockpitProject cockpitProject : availableCockpitProjects) {
               if (cockpitProject.getId() == currentCockpitProjectId) {
                   inList = true;
                   break;
               }
           }
       }
       request.setAttribute("allowCockpitProjectEdit", isAdmin && inList);
    }

    /**
     * Checks whether we can edit project prizes for a project.
     *
     * @param projectId the id of the project.
     * @return an array containing two boolean values, the first value indicates whether we
     *          can edit contest prizes, the second value indicates whether we can edit checkpoint prizes.
     * @throws BaseException if any error occurs.
     * @since  1.14
     */
    private static boolean[] canEditPrize(long projectId) throws BaseException {
        boolean[] ret = {true, true};
        if (projectId <= 0) {
            // new project
            return ret;
        }
        Submission[] contestSubmissions = ActionsHelper.getProjectSubmissions(projectId,
                Constants.CONTEST_SUBMISSION_TYPE_NAME, null, false);
        Submission[] checkpointSubmissions = ActionsHelper.getProjectSubmissions(projectId,
                Constants.CHECKPOINT_SUBMISSION_TYPE_NAME, null, false);
        for (Submission sub : contestSubmissions) {
            if (sub.getPrize() != null) {
                ret[0] = false;
                break;
            }
        }
        for (Submission sub : checkpointSubmissions) {
            if (sub.getPrize() != null) {
                ret[1] = false;
                break;
            }
        }
        return ret;
    }

    /**
     * TODO: Document it
     *
     * @param form
     * @param dateProperty
     * @param timeProperty
     * @param index
     * @param date
     */
    private void populateDatetimeFormProperties(LazyValidatorForm form, String dateProperty, String timeProperty,
            int index, Date date) {
        // TODO: Reuse the DateFormat instance
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy HH:mm", Locale.US);
        String[] parts = dateFormat.format(date).split("[ ]");
        form.set(dateProperty, index, parts[0]);
        form.set(timeProperty, index, parts[1]);
    }

    /**
     * This method populates as single property of the project form by the value taken from the
     * specified Project instance.
     *
     * @param form
     *            the form to populate property of
     * @param type
     *            the type of form property to be populated
     * @param formProperty
     *            the name of form property to be populated
     * @param project
     *            the project to take the value of property of
     * @param projectProperty
     *            the name of project property to take the value of
     */
    private void populateProjectFormProperty(LazyValidatorForm form, Class type, String formProperty,
            Project project, String projectProperty) {

        String value = (String) project.getProperty(projectProperty);
        if (value != null) {
            if (type == String.class) {
                form.set(formProperty, value);
            } else if (type == Boolean.class) {
                form.set(formProperty, Boolean.valueOf(value.compareToIgnoreCase("Yes") == 0));
            } else if (type == Long.class) {
                form.set(formProperty, Long.valueOf(value));
            } else if (type == Integer.class) {
                form.set(formProperty, Integer.valueOf(value));
            } else if (type == Double.class) {
                form.set(formProperty, Double.valueOf(value));
            }
        }
    }

    /**
     * This method is an implementation of &quot;Edit Project&quot; Struts Action defined for this
     * assembly, which is supposed to fetch lists of project types and categories from the database
     * and pass it to the JSP page to use it for populating appropriate drop down lists. It is also
     * supposed to retrieve the project to be edited and to populate the form with appropriate
     * values.
     *
     * @return &quot;success&quot; forward that forwards to the /jsp/editProject.jsp page (as
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
     *             when any error happens while processing in TCS components.
     */
    public ActionForward editProject(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        setEditProjectFormData(request, verification, (LazyValidatorForm) form);

        // Populate the form with project properties
        populateProjectForm(request, (LazyValidatorForm) form, verification.getProject());

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Save Project&quot; Struts Action defined for this
     * assembly, which is supposed to save the project data submitted by the end user to database.
     *
     * <p>
     * Updated for Online Review Update - Add Project Dropdown v1.0:
     *      Added set of 'Billing Project' property.
     * </p>
     *
     * <p>
     * Updated for Configurable Contest Terms Release Assembly v1.0:
     *      Added Project Role User Terms Of Use association generation
     * </p>
     *
     * <p>
     * Updated for Online Review - Project Payments Integration Part 1 v1.0
     *      - Save the project prizes.
     *      - Added the support for concurrency control.
     * </p>
     *
     * @return &quot;success&quot; forward that forwards to view project details page (as
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
     * @throws BaseException if any error occurs.
     */
    public ActionForward saveProject(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Cast the form to its actual type
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;

        // Check whether user is creating new project or editing existing one
        final boolean newProject = (lazyForm.get("pid") == null);
        Project project = null;

        // Check if the user has the permission to perform this action
        verification = null;
        if (newProject) {
            // Gather the roles the user has for current request
            AuthorizationHelper.gatherUserRoles(request);

            if (!AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)) {
                // If he doesn't, redirect the request to login page or report about the lack of permissions
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.CREATE_PROJECT_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
            }

            // At this point, redirect-after-login attribute should be removed (if it exists)
            AuthorizationHelper.removeLoginRedirect(request);
        } else {
            // Verify that certain requirements are met before processing with the Action
            verification = ActionsHelper.checkForCorrectProjectId(
                    mapping, getResources(request), request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, true);
            // If any error has occurred, return action forward contained in the result bean
            if (!verification.isSuccessful()) {
                return verification.getForward();
            }

            project = verification.getProject();

            // Verify that the project hasn't been updated since last modification timestamp
            if ((Long) lazyForm.get("last_modification_time") <
                    ActionsHelper.getLastModificationTime(project,
                            ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false),
                                    project)).getTime()) {
                ActionsHelper.addErrorToRequest(request,
                        "error.com.cronos.onlinereview.actions.editProject.optConcurrency");
            }
        }

        // This variable determines whether status of the project has been changed by this save operation.
        boolean statusHasChanged = false;
        boolean categoryChanged = false;
        if (newProject) {
            // Find "Active" project status
            ProjectStatus activeStatus = LookupHelper.getProjectStatus("Active");
            // Find the project category by the specified id
            ProjectCategory category = LookupHelper.getProjectCategory((Long) lazyForm.get("project_category"));
            if (category.getProjectType().isGeneric()) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.CREATE_PROJECT_PERM_NAME, "Error.GenericProjectType", Boolean.TRUE);
            }
            // Create Project instance
            project = new Project(category, activeStatus);

            project.setProperty("Approval Required", "true"); // All new projects by default need Approval phase.
            project.setProperty("Send Winner Emails", "true");
            project.setProperty("Post-Mortem Required", "true");
            project.setProperty("Reliability Bonus Eligible", "true");
            project.setProperty("Member Payments Eligible", "true");
            project.setProperty("Track Late Deliverables", "true");
            statusHasChanged = true; // Status is always considered to be changed for new projects
        } else {
            long newCategoryId = (Long) lazyForm.get("project_category");
            if (project.getProjectCategory().getId() != newCategoryId) {
                categoryChanged = true;
            }
            // Sets Project category
            ProjectCategory projectCategory = LookupHelper.getProjectCategory(newCategoryId);
            if (projectCategory.getProjectType().isGeneric()) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.CREATE_PROJECT_PERM_NAME, "Error.GenericProjectType", Boolean.TRUE);
            }
            project.setProjectCategory(projectCategory);

        }

        /*
         * Populate the properties of the project
         */

        // Populate project name
        project.setProperty("Project Name", lazyForm.get("project_name"));
        if (newProject) {
            // Populate project version (always set to 1.0)
            // TODO: Fix the version of the project
            project.setProperty("Project Version", "1.0");
            // Populate project root catalog id
            // OrChange - If the project category is Studio set the property to allow multiple submissions.
            if (ActionsHelper.isStudioProject(project)) {
                //TODO retrieve it from the configuration
                log.debug("setting 'Root Catalog ID' to 26887152");
                project.setProperty("Root Catalog ID", "26887152");
                log.debug("Allowing multiple submissions for this project.");
                // As per Checkpoint Support assembly multiple submissions are not allowed for Studio projects for now
                project.setProperty("Allow multiple submissions", false);
            } else {
                project.setProperty("Root Catalog ID", ActionsHelper.getRootCategoryIdByComponentId(lazyForm.get("component_id")));
            }
            // Populate contest indicator flag
            project.setProperty("Contest Indicator", "On");
        } else {
            ProjectStatus newProjectStatus = LookupHelper.getProjectStatus((Long) lazyForm.get("status"));
            String oldStatusName = project.getProjectStatus().getName();
            String newStatusName = (newProjectStatus != null) ? newProjectStatus.getName() : oldStatusName;

            // Determine if status has changed
            statusHasChanged = !oldStatusName.equalsIgnoreCase(newStatusName);
            // If status has changed, update the project

            // Only admins can change status for Studio projects
            if (statusHasChanged && ActionsHelper.isStudioProject(project)) {
                if (!AuthorizationHelper.hasUserRole(request, Constants.ADMIN_ROLE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, "status", "StatusValidation.StudioChangedStatus");
                }
            }

            if (statusHasChanged && !ActionsHelper.isErrorsPresent(request)) {
                // Populate project status
                project.setProjectStatus(newProjectStatus);

                if (oldStatusName.equals("Active") && !newStatusName.equals("Draft")) {
                    // Set Completion Timestamp once the status is changed from Active to Completed, Cancelled - *, or Deleted
                    ActionsHelper.setProjectCompletionDate(project, newProjectStatus, (Format) request.getAttribute("date_format"));
                }
            }
        }

        // Populate project forum id
        project.setProperty("Developer Forum ID", lazyForm.get("forum_id"));
        // Populate project component id
        project.setProperty("Component ID", lazyForm.get("component_id"));
        // Populate project External Reference ID
        project.setProperty("External Reference ID", lazyForm.get("external_reference_id"));

        // Populate project dr points
        Double drPoints = (Double)lazyForm.get("dr_points");
        project.setProperty("DR points", drPoints.equals(0d) ? null : drPoints);

        if (newProject && lazyForm.get("external_reference_id") != null) {
            // Retrieve and populate version
            project.setProperty("Version ID",
                    ActionsHelper.getVersionUsingComponentVersionId(
                            (Long) lazyForm.get("external_reference_id")));
        }

        // Extract project's properties from the form
        Boolean autopilotOnObj = (Boolean) lazyForm.get("autopilot");
        Boolean sendEmailNotificationsObj = (Boolean) lazyForm.get("email_notifications");
        Boolean sendTLChangeNotificationsObj = (Boolean) lazyForm.get("timeline_notifications");
        Boolean digitalRunFlagObj = (Boolean) lazyForm.get("digital_run_flag");
        Boolean doNotRateProjectObj = (Boolean) lazyForm.get("no_rate_project");

        // Unbox the properties
        boolean autopilotOn = (autopilotOnObj != null) && autopilotOnObj;
        boolean sendEmailNotifications =
                (sendEmailNotificationsObj != null) && sendEmailNotificationsObj;
        boolean sendTLChangeNotifications =
                (sendTLChangeNotificationsObj != null) && sendTLChangeNotificationsObj;
        boolean digitalRunFlag = (digitalRunFlagObj != null) && digitalRunFlagObj;
        boolean doNotRateProject = (doNotRateProjectObj != null) && doNotRateProjectObj;

        // Populate project autopilot option
        project.setProperty("Autopilot Option", (autopilotOn) ? "On" : "Off");
        // Populate project status notifications option
        project.setProperty("Status Notification", (sendEmailNotifications) ? "On" : "Off");
        // Populate project timeline notifications option
        project.setProperty("Timeline Notification", (sendTLChangeNotifications) ? "On" : "Off");
        // Populate project Digital Run option
        project.setProperty("Digital Run Flag", (digitalRunFlag) ? "On" : "Off");
        // Populate project rated option, note that it is inverted
        project.setProperty("Rated", (doNotRateProject) ? "No" : "Yes");

        if (lazyForm.get("notes") != null && lazyForm.get("notes").toString().length() > 255) {
            ActionsHelper.addErrorToRequest(request, "notes", "error.com.cronos.onlinereview.actions.editProject.notes.MaxExceeded");
        }
        // Populate project notes
        project.setProperty("Notes", lazyForm.get("notes"));

        // since Online Review Update - Add Project Dropdown v1.0
        // Populate project notes
        if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)
                 || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
                project.setProperty("Billing Project", lazyForm.get("billing_project"));
                String cockpitProjectId = (String) lazyForm.get("cockpit_project");
                if (cockpitProjectId.trim().length() > 0) {
                    project.setTcDirectProjectId(Integer.parseInt(cockpitProjectId));
                }
        }

        // Create the map to store the mapping from phase JS ids to phases
        Map<Object, Phase> phasesJsMap = new HashMap<Object, Phase>();

        // Create the list to store the phases to be deleted
        List<Phase> phasesToDelete = new ArrayList<Phase>();

        List<Prize> createdPrize = new ArrayList<Prize>();
        List<Prize> updatedPrize = new ArrayList<Prize>();
        List<Prize> removedPrize = new ArrayList<Prize>();
        getProjectPrizesToBeUpdated(request, lazyForm, project, createdPrize, updatedPrize, removedPrize);

        Phase[] projectPhases = null;
        if (!ActionsHelper.isErrorsPresent(request)) {
            // Save the project phases
            projectPhases = saveProjectPhases(newProject, request, lazyForm, project, phasesJsMap, phasesToDelete);
        } else {
            // Retrieve and sort project phases
            projectPhases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
            Arrays.sort(projectPhases, new Comparators.ProjectPhaseComparer());
         }

        if (!ActionsHelper.isErrorsPresent(request)) {
            // The project has been saved, so pre-populate last modification timestamp
            lazyForm.set("last_modification_time",
                ActionsHelper.getLastModificationTime(project, projectPhases).getTime());
        }

        // FIXME: resources must be saved even if there are validation errors to validate resources
        if (!ActionsHelper.isErrorsPresent(request)) {
            // Save the project resources
            saveResources(request, lazyForm, project, projectPhases, phasesJsMap);
        }

        if (!ActionsHelper.isErrorsPresent(request)) {
            // Delete the phases to be deleted
            deletePhases(request, phasesToDelete);
        }

        // If needed switch project current phase
        if (!newProject && !ActionsHelper.isErrorsPresent(request)) {
            switchProjectPhase(request, lazyForm, phasesJsMap);
        }

        // Update the project prizes
        if (!ActionsHelper.isErrorsPresent(request)) {
            ProjectManager projectManager = ActionsHelper.createProjectManager();
            String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
            for (Prize prize : createdPrize) {
                prize.setProjectId(project.getId());
                projectManager.createPrize(prize, operator);
            }
            for (Prize prize : updatedPrize) {
                projectManager.updatePrize(prize, operator);
            }
            for (Prize prize : removedPrize) {
                projectManager.removePrize(prize, operator);
            }
            PaymentsHelper.processAutomaticPayments(project.getId(), operator);
        }

        // Check if there are any validation errors and return appropriate forward
        if (ActionsHelper.isErrorsPresent(request)) {
            // TODO: Check if the form is really for new project
            if (verification == null) {
                verification = new CorrectnessCheckResult();
                verification.setProject(project);
            }
            setEditProjectFormData(request, verification, lazyForm);
            setEditProjectPhasesData(lazyForm, projectPhases, true);
            ResourceManager resourceManager = ActionsHelper.createResourceManager();
            Resource[] resources = resourceManager.searchResources(
                ResourceFilterBuilder.createProjectIdFilter(project.getId()));
            ExternalUser[] externalUsers = ActionsHelper.getExternalUsersForResources(
                ActionsHelper.createUserRetrieval(request), resources);
            setEditProjectRequestAttributes(request, project, resources, externalUsers, projectPhases);

            request.setAttribute("newProject", newProject);

            return mapping.getInputForward();
        }

        // Return success forward
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME),"&pid=" + project.getId());
    }

    /**
     * TODO: Document it
     *
     * @param request
     * @param phasesToDelete
     * @throws BaseException
     */
    private void deletePhases(HttpServletRequest request, List<Phase> phasesToDelete)
            throws BaseException {

        if (phasesToDelete.isEmpty()) {
            return;
        }

        com.topcoder.project.phases.Project phProject = phasesToDelete.get(0).getProject();

        for (Phase phase : phasesToDelete) {
            phProject.removePhase(phase);
        }

        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
        phaseManager.updatePhases(phProject, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
    }

    /**
     * Validate the project prizes data.
     *
     * @param request the HttpServletRequest instance.
     * @param amounts an array containing the prizes amount data.
     * @param nums an array containing the number of prizes data.
     * @param keyPrefix the prefix of the form property name.
     * @since 1.14
     */
    private static void validateProjectPrizes(HttpServletRequest request, String[] amounts, Integer[] nums,
                                              String keyPrefix) {
        for (int i = 0; i < amounts.length; i++) {
            if (!ActionsHelper.checkNonNegDoubleWith2Decimal(amounts[i],
                    keyPrefix + "_amount[" + i + "]",
                    "error.com.cronos.onlinereview.actions.editProject.prize.amount.Invalid",
                    "error.com.cronos.onlinereview.actions.editProject.prize.amount.PrecisionInvalid",
                    request, false)) {
            }
            if (nums[i] <= 0 || nums[i] > 10) {
                ActionsHelper.addErrorToRequest(request,
                        keyPrefix + "_num[" + i + "]",
                        "error.com.cronos.onlinereview.actions.editProject.prize.number.Invalid");
            }
        }
    }

    /**
     * This method will check the project prizes submitted by user with the existing project prizes,
     * and determine which prizes should be created, which prizes should be updated and which prizes
     * should be removed.
     *
     * @param prizeType the prize type.
     * @param created the prizes which should be created.
     * @param updated the prizes which should be updated.
     * @param removed the prizes which should be removed.
     * @param oldPrizes the existing project prizes.
     * @param amounts the prizes amount data submitted by user.
     * @param nums the number of prizes data submitted by user.
     * @since 1.14
     */
    private static void getProjectPrizesToBeUpdated(PrizeType prizeType, List<Prize> created, List<Prize> updated,
                                                    List<Prize> removed, List<Prize> oldPrizes,
                                                    String[] amounts, Integer[] nums) {
        for (int i = 0; i < amounts.length; i++) {
            Prize prize;
            double amount = Double.parseDouble(amounts[i]);
            if (i < oldPrizes.size()) {
                // prize exists before
                prize = oldPrizes.get(i);
                if (prize.getNumberOfSubmissions() != nums[i] || prize.getPrizeAmount() != amount) {
                    updated.add(prize);
                }
            } else {
                // add a new prize
                prize = new Prize();
                prize.setPrizeType(prizeType);
                prize.setPlace(i + 1);
                created.add(prize);
            }
            prize.setNumberOfSubmissions(nums[i]);
            prize.setPrizeAmount(amount);
        }
        for (int i = amounts.length; i < oldPrizes.size(); i++) {
            removed.add(oldPrizes.get(i));
        }
    }

    /**
     * This method will check the project prizes submitted by user with the existing project prizes,
     * and determine which prizes should be created, which prizes should be updated and which prizes
     * should be removed.
     *
     * @param request the HttpServletRequest instance.
     * @param lazyForm the form containing the prizes data submitted by user.
     * @param project the project.
     * @param created the prizes which should be created.
     * @param updated the prizes which should be updated
     * @param removed the prizes which should be removed.
     * @throws BaseException if any error occurs.
     * @since  1.14
     */
    private static void getProjectPrizesToBeUpdated(HttpServletRequest request, LazyValidatorForm lazyForm,
                                                    Project project, List<Prize> created, List<Prize> updated,
                                                    List<Prize> removed) throws BaseException {
        boolean[] canEditPrizes = canEditPrize(project.getId());
        String[] contestPrizeAmounts = (String[]) lazyForm.get("contest_prizes_amount");
        Integer[] contestPrizeNums = (Integer[]) lazyForm.get("contest_prizes_num");
        String[] checkpointPrizeAmounts = (String[]) lazyForm.get("checkpoint_prizes_amount");
        Integer[] checkpointPrizeNums = (Integer[]) lazyForm.get("checkpoint_prizes_num");

        validateProjectPrizes(request, contestPrizeAmounts, contestPrizeNums, "contest_prizes");
        validateProjectPrizes(request, checkpointPrizeAmounts, checkpointPrizeNums, "checkpoint_prizes");

        List<Prize> prizes = project.getPrizes();
        PrizeType contestPrizeType  = LookupHelper.getPrizeType(Constants.CONTEST_PRIZE_TYPE_NAME);
        PrizeType checkpointPrizeType = LookupHelper.getPrizeType(Constants.CHECKPOINT_PRIZE_TYPE_NAME);
        if (prizes != null) {
            if (!canEditPrizes[0] && contestPrizeAmounts.length == 0) {
                for (Prize prize : prizes) {
                    if (prize.getPrizeType().getId() == contestPrizeType.getId()) {
                        lazyForm.set("contest_prizes_amount",
                                prize.getPlace() - 1, String.valueOf(prize.getPrizeAmount()));
                        lazyForm.set("contest_prizes_num",
                                prize.getPlace() - 1, prize.getNumberOfSubmissions());
                    }
                }
            }
            if (!canEditPrizes[1] && checkpointPrizeAmounts.length == 0) {
                for (Prize prize : prizes) {
                    if (prize.getPrizeType().getId() == checkpointPrizeType.getId()) {
                        lazyForm.set("checkpoint_prizes_amount",
                                prize.getPlace() - 1, String.valueOf(prize.getPrizeAmount()));
                        lazyForm.set("checkpoint_prizes_num",
                                prize.getPlace() - 1, prize.getNumberOfSubmissions());
                    }
                }
            }
        }

        if (ActionsHelper.isErrorsPresent(request)) {
            return;
        }

        List<Prize> existingContestPrizes = new ArrayList<Prize>();
        List<Prize> existingCheckpointPrizes = new ArrayList<Prize>();

        if (project.getPrizes() != null) {
            for (Prize prize : project.getPrizes()) {
                if (prize.getPrizeType().getId() == contestPrizeType.getId()) {
                    existingContestPrizes.add(prize);
                } else if (prize.getPrizeType().getId() == checkpointPrizeType.getId()) {
                    existingCheckpointPrizes.add(prize);
                }
            }
        }

        Comparator<Prize> comp = new Comparators.PrizePlaceComparator();
        Collections.sort(existingContestPrizes, comp);
        Collections.sort(existingCheckpointPrizes, comp);

        if (canEditPrizes[0]) {
            getProjectPrizesToBeUpdated(contestPrizeType, created, updated, removed,
                    existingContestPrizes, contestPrizeAmounts, contestPrizeNums);
        }
        if (canEditPrizes[1]) {
            getProjectPrizesToBeUpdated(checkpointPrizeType, created, updated, removed,
                    existingCheckpointPrizes, checkpointPrizeAmounts, checkpointPrizeNums);
        }
    }

    /**
     * <p>Updates the list of phases associated with the specified project. Optionally the method accepts the list of
     * project's phases which are to be deleted.</p>
     *
     * <p>This method has the following side-effect: if the end time for <code>Final Review</code> phase for specified
     * project is extended then start times for <code>Registration</code> and <code>Submission</code> phases for
     * projects which depend on this project (directly or indirectly) are also extended by the same amount of time.
     * However nothing happens to depending projects if end time for <code>Final Review</code> phase for specified
     * project is shrinked.</p>
     *
     * @param newProject <code>true</code> if project is new project; <code>false</code> if project is existing project
     *        which is updated.
     * @param request an <code>HttpServletRequest</code> representing current incoming request from the client.
     * @param lazyForm a <code>LazyValidatorForm</code> providing the submitted form mapped to specified request.
     * @param project a <code>Project</code> providing details for project associated with the phases.
     * @param phasesJsMap a <code>Map</code> mapping phase IDs to phases.
     * @param phasesToDelete a <code>List</code> listing the existing phases for specified project which are to be
     *        deleted.
     * @return a <code>Phase</code> array listing the updated phases associated with the specified project.
     * @throws BaseException if an unexpected error occurs.
     */
    private Phase[] saveProjectPhases(boolean newProject, HttpServletRequest request, LazyValidatorForm lazyForm,
            Project project, Map<Object, Phase> phasesJsMap, List<Phase> phasesToDelete)
        throws BaseException {
        // Obtain an instance of Phase Manager
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);

        com.topcoder.project.phases.Project phProject;
        if (newProject) {
            // Create new Phases Project
            // TODO: Use real values for date and workdays, not the test ones
            phProject = new com.topcoder.project.phases.Project(
                    new Date(), (new DefaultWorkdaysFactory()).createWorkdaysInstance());
        } else {
            // Retrieve the Phases Project with the id equal to the id of specified Project
            phProject = phaseManager.getPhases(project.getId());
            // Sometimes the call to the above method returns null. Guard against this situation
            if (phProject == null) {
                // TODO: Same to-do as above
                phProject = new com.topcoder.project.phases.Project(
                        new Date(), (new DefaultWorkdaysFactory()).createWorkdaysInstance());
            }
        }

        // Get the list of all previously existing phases
        Phase[] oldPhases = phProject.getAllPhases();

        // Get the array of phase types specified for each phase
        Long[] phaseTypes = (Long[]) lazyForm.get("phase_type");

        // This will be a Map from phases to their indexes in form
        Map<Phase, Integer> phasesToForm = new HashMap<Phase, Integer>();

        // FIRST PASS
        // 0-index phase is skipped since it is a "dummy" one
        for (int i = 1; i < phaseTypes.length; i++) {
            if (phaseTypes[i] == null) {
                String phaseJsId = (String) lazyForm.get("phase_js_id", i);
                long phaseId = Long.parseLong(phaseJsId.substring("loaded_".length()));
                Phase phase = ActionsHelper.findPhaseById(oldPhases, phaseId);
                phasesJsMap.put(phaseJsId, phase);
                phasesToForm.put(phase, i);
                continue;
            }
            Phase phase;

            // Check what is the action to be performed with the phase
            // and obtain Phase instance in appropriate way
            String phaseAction = (String) lazyForm.get("phase_action", i);
            if ("add".equals(phaseAction)) {
                // Create new phase
                // Phase duration is set to zero here, as it will be updated later anyway
                phase = new Phase(phProject, 0);
                // Add it to Phases Project
                phProject.addPhase(phase);
            }  else {
                long phaseId = (Long) lazyForm.get("phase_id", i);
                if (phaseId != -1) {
                    // Retrieve the phase with the specified id
                    phase = ActionsHelper.findPhaseById(oldPhases, phaseId);

                    // Clear all the pre-existing dependencies
                    phase.clearDependencies();

                    // Clear the previously set fixed start date
                    phase.setFixedStartDate(null);
                } else {
                    // -1 value as id marks the phases that were't persisted in DB yet
                    // and so should be skipped for actions other than "add"
                    continue;
                }
            }

            // If action is "delete", proceed to the next phase
            if ("delete".equals(phaseAction)) {
                continue;
            }

            // flag value indicates using end date or using duration
            boolean useDuration = (Boolean) lazyForm.get("phase_use_duration", i);

            // If phase duration is specified
            if (useDuration) {
                String duration = (String) lazyForm.get("phase_duration", i);
                String[] parts = duration.split(":");

                // the format should be hh or hh:mm
                if (parts.length < 1 || parts.length > 2) {
                    ActionsHelper.addErrorToRequest(request,
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.InvalidDurationFormat",
                                    phase.getPhaseType().getName()));
                    break;
                }

                try {
                    // Calculate phase length taking hh part into account
                    long length = Long.parseLong(parts[0]) * 3600 * 1000;
                    if (parts.length == 2) {
                        // If mm part is present, add it to calculated length
                        length += Long.parseLong(parts[1]) * 60 * 1000;
                    }
                    // Set phase length
                    phase.setLength(length);
                } catch (NumberFormatException nfe) {
                    // the hh or mm is not valid integer
                    ActionsHelper.addErrorToRequest(request,
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.InvalidDurationFormat",
                                    phase.getPhaseType().getName()));
                    break;
                }
            } else {
                // Length is undetermined at current pass
                phase.setLength(0);
            }

            // Put the phase to the map from phase JS ids to phases
            phasesJsMap.put(lazyForm.get("phase_js_id", i), phase);
            // Put the phase to the map from phases to the indexes of form inputs
            phasesToForm.put(phase, i);
        }

        // Minimal date will be the project start date
        Date minDate = null;

        // SECOND PASS
        for (int i = 1; i < phaseTypes.length; i++) {
            if (phaseTypes[i] == null) {
                continue;
            }
            Object phaseObj = phasesJsMap.get(lazyForm.get("phase_js_id", i));
            // If phase is not found in map, it is to be deleted
            if (phaseObj == null) {
                long phaseId = (Long) lazyForm.get("phase_id", i);

                if (phaseId != -1) {
                    // Retrieve the phase with the specified id
                    Phase phase = ActionsHelper.findPhaseById(oldPhases, phaseId);

                    // Signal that phases are to be deleted
                    phasesToDelete.add(phase);
                }

                // Skip further processing
                continue;
            }

            Phase phase = (Phase) phaseObj;

            /*
             * Set phase properties
             */

            String phaseAction = (String) lazyForm.get("phase_action", i);

            if ("add".equals(phaseAction)) {
                // Set phase type
                phase.setPhaseType(LookupHelper.getPhaseType(phaseTypes[i]));
                // Set phase status to "Scheduled"
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            }

            // If phase is not started by other phase end
            Boolean startsByFixedTime = (Boolean) lazyForm.get("phase_start_by_fixed_time", i);
            boolean startTimeSet = false;
            if (startsByFixedTime != null && startsByFixedTime) {
                String datePart = (String) lazyForm.get("phase_start_date", i);
                String timePart = (String) lazyForm.get("phase_start_time", i);
                startTimeSet = !(isEmpty(datePart) && isEmpty(timePart));
                if (startTimeSet) {
                    if (isEmpty(datePart)) {
                        ActionsHelper.addErrorToRequest(request,
                                new ActionMessage("error.com.cronos.onlinereview.actions.editProject.PhaseStartDateNotSet",
                                        phase.getPhaseType().getName()));
                    } else if (isEmpty(timePart)) {
                        ActionsHelper.addErrorToRequest(request,
                                new ActionMessage("error.com.cronos.onlinereview.actions.editProject.PhaseStartTimeNotSet",
                                        phase.getPhaseType().getName()));
                    } else {
                        // Get phase start date from form
                        // Set phase fixed start date
                        Date phaseStartDate = parseDatetimeFormProperties(lazyForm, i, "phase_start_date",
                            "phase_start_time");
                        phase.setFixedStartDate(phaseStartDate);
                        // Check if the current date is minimal
                        if (minDate == null || phaseStartDate.getTime() < minDate.getTime()) {
                            minDate = phaseStartDate;
                        }
                    }
                }
            }

            // Get the dependency phase
            Boolean startsByAnotherPhase = (Boolean) lazyForm.get("phase_start_by_phase", i);
            boolean mainPhaseSet = false;
            if (startsByAnotherPhase != null && startsByAnotherPhase) {
                String phaseJsId = (String) lazyForm.get("phase_start_phase", i);
                if (phaseJsId != null && phaseJsId.trim().length() > 0) {
                    Phase dependencyPhase = phasesJsMap.get(phaseJsId);
                    if (dependencyPhase != null) {
                        mainPhaseSet = true;
                        boolean dependencyStart;
                        boolean dependantStart;
                        if ("ends".equals(lazyForm.get("phase_start_when", i))) {
                            dependencyStart = false;
                            dependantStart = true;
                        } else {
                            dependencyStart = true;
                            dependantStart = true;
                        }
                        Object phaseLag = lazyForm.get("phase_start_dayshrs", i);
                        long unitMutiplier
                            = 1000 * 60 * ("days".equals(phaseLag) ? 24 * 60 : ("hrs".equals(phaseLag) ? 60 : 1));
                        long lagTime = unitMutiplier * ((Integer) lazyForm.get("phase_start_amount", i)).longValue();

                        // negative lag time.
                        if ("minus".equals(lazyForm.get("phase_start_plusminus", i))) {
                            lagTime *= -1;
                        }
                        // Create phase Dependency
                        // Notes, Dependency class does support negative lagTime.
                        Dependency dependency = new Dependency(dependencyPhase, phase,
                                dependencyStart, dependantStart, lagTime);

                        // Add dependency to phase
                        phase.addDependency(dependency);
                    }
                }
            }

            if (!startTimeSet && !mainPhaseSet) {
                ActionsHelper.addErrorToRequest(request,
                        new ActionMessage("error.com.cronos.onlinereview.actions.editProject.PhaseStartBad",
                                phase.getPhaseType().getName()));
            }
            /*
             *  Set phase criteria
             */
            Long scorecardId = (Long) lazyForm.get("phase_scorecard", i);
            // If the scorecard id is specified, set it
            if (scorecardId != null) {
                phase.setAttribute("Scorecard ID", scorecardId.toString());
            }
            Integer requiredRegistrations = (Integer) lazyForm.get("phase_required_registrations", i);
            // If the number of required registrations is specified, set it
            if (requiredRegistrations != null) {
                phase.setAttribute("Registration Number", requiredRegistrations.toString());
            }
            // If the number of required reviewers is specified, set it
            Integer requiredReviewer = (Integer) lazyForm.get("phase_required_reviewers", i);
            if (requiredReviewer != null) {

                if (requiredReviewer < 1) {
                    ActionsHelper.addErrorToRequest(request,
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.InvalidReviewersNumber",
                                    phase.getPhaseType().getName()));
                    break;
                }

                phase.setAttribute("Reviewer Number", requiredReviewer.toString());
            }

            Boolean viewAppealResponses = (Boolean) lazyForm.get("phase_view_appeal_responses", i);
            // If the view appeal response during appeals flag is specified, set it
            if (viewAppealResponses != null) {
                phase.setAttribute("View Response During Appeals", viewAppealResponses ? "Yes" : "No");
            }
        }

        // Update project start date if needed
        if (minDate != null) {
            if (minDate.compareTo(phProject.getStartDate()) < 0) {
                phProject.setStartDate(minDate);
            }
        }

        // THIRD PASS
        boolean hasCircularDependencies = false;
        Set<Phase> processed = new HashSet<Phase>();
        for (int i = 1; i < phaseTypes.length; i++) {
            if (phaseTypes[i] == null) {
                continue;
            }
            Object phaseObj = phasesJsMap.get(lazyForm.get("phase_js_id", i));
            // If phase is not found in map, it was deleted and should not be processed
            if (phaseObj == null) {
                continue;
            }

            Phase phase = (Phase) phaseObj;

            // If phase was already processed, skip it
            if (processed.contains(phase)) {
                continue;
            }

            Set<Phase> visited = new HashSet<Phase>();
            Stack<Phase> stack = new Stack<Phase>();

            for (;;) {
                processed.add(phase);
                visited.add(phase);
                stack.push(phase);

                Dependency[] dependencies = phase.getAllDependencies();
                // Actually there should be either zero or one dependency, we'll assume it
                if (dependencies.length == 0) {
                    // If there is no dependency, stop processing
                    break;
                } else {
                    phase = dependencies[0].getDependency();
                    if (visited.contains(phase)) {
                        // There is circular dependency, report it and stop processing
                        // TODO: Report the particular phases
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.CircularDependency");
                        hasCircularDependencies = true;
                        break;
                    }
                }
            }

            while (!stack.empty()) {
                phase = stack.pop();
                int paramIndex = phasesToForm.get(phase);
                if (phaseTypes[paramIndex] == null) {
                    continue;
                }

                try {
                    // Set scheduled start date to calculated start date
                    phase.setScheduledStartDate(phase.calcStartDate());

                    // flag value indicates using end date or using duration
                    boolean useDuration = (Boolean) lazyForm.get("phase_use_duration", paramIndex);

                    // If phase duration was not specified
                    if (!useDuration) {
                        // Get phase end date from form
                        Date phaseEndDate = parseDatetimeFormProperties(lazyForm, paramIndex,
                                "phase_end_date", "phase_end_time");

                        // Calculate phase length
                        long length = phaseEndDate.getTime() - phase.getScheduledStartDate().getTime();
                        // Check if the end date of phase goes after the start date
                        if (length < 0) {
                            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                                    "error.com.cronos.onlinereview.actions.editProject.StartAfterEnd",
                                    phase.getPhaseType().getName()));
                            break;
                        }

                        // Get the workdays
                        Workdays workdays = phProject.getWorkdays();

                        // Perform binary search to take the workdays into account
                        long minLength = 0;
                        long maxLength = length;

                        Date estimatedEndDate = workdays.add(phase.getScheduledStartDate(),
                                WorkdaysUnitOfTime.MINUTES, (int) (length / 60000));
                        long diff = estimatedEndDate.getTime() - phaseEndDate.getTime();
                        while (Math.abs(diff) > 60000) {
                            if (diff < 0) {
                                // Current length is too small
                                minLength = length;
                            } else {
                                // Current length is too big
                                maxLength = length;
                            }
                            length = (minLength + maxLength) / 2;

                            estimatedEndDate = workdays.add(phase.getScheduledStartDate(),
                                    WorkdaysUnitOfTime.MINUTES, (int) (length / 60000));
                            diff = estimatedEndDate.getTime() - phaseEndDate.getTime();
                        }

                        // Set phase duration appropriately
                        phase.setLength(length);
                    }

                    // Set scheduled phase end date to calculated phase end date
                    phase.setScheduledEndDate(phase.calcEndDate());
                } catch (CyclicDependencyException e) {
                    // There is circular dependency, report it and stop processing
                    // TODO: Report the particular phases
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.CircularDependency");
                    hasCircularDependencies = true;
                    break;
                }
            }

            if (hasCircularDependencies) {
                break;
            }
        }

        if (ActionsHelper.isErrorsPresent(request)) {
            // TODO: Return null or so
            return oldPhases;
        }

        // Get all the project phases
        Phase[] projectPhases = phProject.getAllPhases();
        // Sort project phases
        Arrays.sort(projectPhases, new Comparators.ProjectPhaseComparer());

        // Validate the project phases
        boolean validationSucceeded = validateProjectPhases(request, project, projectPhases);

        if (!validationSucceeded) {
            // If project validation has failed, return immediately
            return oldPhases;
        }

        // FIXME: Refactor it
        ProjectManager projectManager = ActionsHelper.createProjectManager();

        // Set project rating date
        ActionsHelper.setProjectRatingDate(project, projectPhases, (Format) request.getAttribute("date_format"));

        if (newProject) {
            // Create project in persistence level
            projectManager.createProject(project, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

            // Set the id of Phases Project to be equal to the id of appropriate Project
            phProject.setId(project.getId());
        } else {
            try {
                projectManager.updateProject(project, (String) lazyForm.get("explanation"),
                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
            } catch(StatusValidationException statusValidationException) {
                ActionsHelper.addErrorToRequest(request, "status", statusValidationException.getStatusViolationKey());
                return oldPhases;
            }
        }

        // Save the phases at the persistence level
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        phaseManager.updatePhases(phProject, operator);
        // TODO: The following line was added just to be safe. May be unneeded as well as another one
        projectPhases = phProject.getAllPhases();
        // Sort project phases
        Arrays.sort(projectPhases, new Comparators.ProjectPhaseComparer());

        return projectPhases;
    }

    /**
     *
     * TODO: Document it.
     *
     * @param request
     * @param lazyForm
     * @param phasesJsMap
     * @throws BaseException
     */
    private void switchProjectPhase(HttpServletRequest request, LazyValidatorForm lazyForm,
                                    Map<Object, Phase> phasesJsMap) throws BaseException {

        // Get name of action to be performed
        String action = (String) lazyForm.get("action");

        // Get new current phase id
        String phaseJsId = (String) lazyForm.get("action_phase");

        if (phaseJsId != null && phasesJsMap.containsKey(phaseJsId)) {
            // Get the phase to be operated on
            Phase phase = phasesJsMap.get(phaseJsId);

            // Get the status of phase
            PhaseStatus phaseStatus = phase.getPhaseStatus();
            // Get the type of the phase
            PhaseType phaseType = phase.getPhaseType();

            // Obtain an instance of Phase Manager
            PhaseManager phaseManager = ActionsHelper.createPhaseManager(true);

            OperationCheckResult result;
            if ("close_phase".equals(action)) {
                result = phaseManager.canEnd(phase);
                if (phaseStatus.getName().equals(PhaseStatus.OPEN.getName()) && result.isSuccess()) {
                    // Close the phase
                    phaseManager.end(phase, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                } else {
                    ActionsHelper.addErrorToRequest(request, new ActionMessage(
                            "error.com.cronos.onlinereview.actions.editProject.CannotClosePhase",
                            phaseType.getName(), result.getMessage()));
                }
            } else if ("open_phase".equals(action)) {
                result = phaseManager.canStart(phase);
                if (phaseStatus.getName().equals(PhaseStatus.SCHEDULED.getName()) && result.isSuccess()) {
                    // Open the phase
                    phaseManager.start(phase, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                } else {
                    ActionsHelper.addErrorToRequest(request, new ActionMessage(
                            "error.com.cronos.onlinereview.actions.editProject.CannotOpenPhase",
                            phaseType.getName(), result.getMessage()));
                }
            }
        }
    }

    /**
     * TODO: Document it
     * Note, that this method assumes that phases are already sorted by the start date, etc.
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param project
     * @param projectPhases
     * @return
     */
    private boolean validateProjectPhases(HttpServletRequest request, Project project, Phase[] projectPhases) {
        boolean arePhasesValid = true;

        boolean isStudioProject = project.getProjectCategory().getProjectType().getId() == Constants.STUDIO_PROJECT_ID;

        // TODO: Refactor this function, make it more concise
        // IF there is a Post-Mortem phase in project skip the validation as that phase may appear anywhere
        // in project timeline and actual order of the phases is not significant
        boolean postMortemPhaseExists = false;
        for (Phase projectPhase : projectPhases) {
            if (projectPhase.getPhaseType().getName().equals(POST_MORTEM_PHASE_NAME)) {
                return true;
            }
        }


        // Check the beginning phase, it should be either Registration or submission
        if (projectPhases.length > 0 &&
                !projectPhases[0].getPhaseType().getName().equals(SPECIFICATION_SUBMISSION_PHASE_NAME) &&
                !projectPhases[0].getPhaseType().getName().equals(REGISTRATION_PHASE_NAME) &&
                !projectPhases[0].getPhaseType().getName().equals(SUBMISSION_PHASE_NAME) &&
                !projectPhases[0].getPhaseType().getName().equals(POST_MORTEM_PHASE_NAME)) {
            ActionsHelper.addErrorToRequest(request,
                    "error.com.cronos.onlinereview.actions.editProject.WrongBeginningPhase");
            arePhasesValid = false;
        }


        // Check the phases as a whole
        for (int i = 0; i < projectPhases.length; i++) {
            final String previousPhaseName = i > 0 ? projectPhases[i - 1].getPhaseType().getName() : "";
            final String currentPhaseName = projectPhases[i].getPhaseType().getName();
            if (currentPhaseName.equals(SUBMISSION_PHASE_NAME)) {
                // Submission should follow registration or post-mortem if it exists
                if (i > 0 && !(previousPhaseName.equals(REGISTRATION_PHASE_NAME)
                               || previousPhaseName.equals(CHECKPOINT_REVIEW_PHASE_NAME))
                    && !postMortemPhaseExists) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.SubmissionMustFollow");
                    arePhasesValid = false;
                }
            } else {
                final String nextPhaseName
                    = i < (projectPhases.length - 1) ? projectPhases[i + 1].getPhaseType().getName() : "";
                if (currentPhaseName.equals(REGISTRATION_PHASE_NAME)) {
                    // Registration should be followed by submission or post-mortem
                    if (i == projectPhases.length - 1
                        || !(nextPhaseName.equals(SUBMISSION_PHASE_NAME)
                             || nextPhaseName.equals(CHECKPOINT_SUBMISSION_PHASE_NAME))
                           && !postMortemPhaseExists) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.RegistrationMustBeFollowed");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(CHECKPOINT_SUBMISSION_PHASE_NAME)) {
                    // Checkpoint Submission should be followed by Checkpoint Screening or Checkpoint Review
                    if (!nextPhaseName.equals(CHECKPOINT_SCREENING_PHASE_NAME)
                        && !nextPhaseName.equals(CHECKPOINT_REVIEW_PHASE_NAME) ) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.CheckpointSubmissionMustBeFollowed");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(CHECKPOINT_SCREENING_PHASE_NAME)) {
                    // Checkpoint Screening should be followed by Checkpoint Review
                    if (!nextPhaseName.equals(CHECKPOINT_REVIEW_PHASE_NAME)) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.CheckpointScreeningMustBeFollowed");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(SPECIFICATION_SUBMISSION_PHASE_NAME)) {
                    // Specification Submission should be followed by Specification Review
                    if (!nextPhaseName.equals(SPECIFICATION_REVIEW_PHASE_NAME)) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.SpecSubmissionMustBeFollowed");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(SPECIFICATION_REVIEW_PHASE_NAME)) {
                    // Specification Review should be followed either by Specification Submission, Registration
                    // or Submission
                    if (!nextPhaseName.equals(SPECIFICATION_SUBMISSION_PHASE_NAME)
                        && !nextPhaseName.equals(REGISTRATION_PHASE_NAME)
                        && !nextPhaseName.equals(SUBMISSION_PHASE_NAME)) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.SpecReviewMustBeFollowed");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(REVIEW_PHASE_NAME)) {
                    // Review should follow submission or screening or post-mortem
                    if (i == 0 || (!previousPhaseName.equals(SUBMISSION_PHASE_NAME) &&
                            !previousPhaseName.equals(SCREENING_PHASE_NAME)
                            && !postMortemPhaseExists)) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.ReviewMustFollow");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(APPEALS_PHASE_NAME)) {
                    // Appeals should follow review
                    if (i == 0 || !previousPhaseName.equals(REVIEW_PHASE_NAME) &&
                                  !postMortemPhaseExists) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.AppealsMustFollow");
                        arePhasesValid = false;
                    }
                    // Appeals should be followed by the appeals response
                    if (i == projectPhases.length - 1 ||
                            !nextPhaseName.equals(APPEALS_RESPONSE_PHASE_NAME) &&
                            !postMortemPhaseExists) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.AppealsMustBeFollowed");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(APPEALS_RESPONSE_PHASE_NAME)) {
                    // Appeal response should follow appeals
                    if (i == 0 || !previousPhaseName.equals(APPEALS_PHASE_NAME) &&
                                  !postMortemPhaseExists) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.AppealsResponseMustFollow");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(AGGREGATION_PHASE_NAME)) {
                    // Aggregation should follow appeals response or review, or aggregation review or post-mortem
                    if (i == 0 ||
                            (!previousPhaseName.equals(APPEALS_RESPONSE_PHASE_NAME) &&
                            !previousPhaseName.equals(REVIEW_PHASE_NAME) &&
                            !previousPhaseName.equals(AGGREGATION_REVIEW_PHASE_NAME) &&
                            !postMortemPhaseExists)) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.AggregationMustFollow");
                        arePhasesValid = false;
                    }
                    // Aggregation should be followed by the aggregation review
                    if (i == projectPhases.length - 1 ||
                            !nextPhaseName.equals(AGGREGATION_REVIEW_PHASE_NAME) &&
                            !nextPhaseName.equals(FINAL_FIX_PHASE_NAME) &&
                            !postMortemPhaseExists) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.AggregationMustBeFollowed");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(AGGREGATION_REVIEW_PHASE_NAME)) {
                    // Aggregation review should follow aggregation
                    if (i == 0 ||
                            !previousPhaseName.equals(AGGREGATION_PHASE_NAME) &&
                            !postMortemPhaseExists) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.AggregationReviewMustFollow");
                        arePhasesValid = false;
                    }
                } else if (currentPhaseName.equals(FINAL_FIX_PHASE_NAME)) {
                    // Final fix should follow either appeals response or aggregation review, or final review
                    if (i == 0 ||
                            (!previousPhaseName.equals(APPEALS_RESPONSE_PHASE_NAME)
                             && !previousPhaseName.equals(AGGREGATION_REVIEW_PHASE_NAME)
                             && !previousPhaseName.equals(AGGREGATION_PHASE_NAME)
                             && !previousPhaseName.equals(APPROVAL_PHASE_NAME)
                             && !(previousPhaseName.equals(REVIEW_PHASE_NAME) && isStudioProject)
                             && !previousPhaseName.equals(FINAL_REVIEW_PHASE_NAME))
                             && !postMortemPhaseExists
                        ) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.FinalFixMustFollow");
                        arePhasesValid = false;
                    }
                    // Final fix should be followed by the final review
                    if (i == projectPhases.length - 1 ||
                            !nextPhaseName.equals(FINAL_REVIEW_PHASE_NAME) &&
                            !postMortemPhaseExists) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.FinalFixMustBeFollowed");
                        arePhasesValid = false;
                    }
                }  else if (currentPhaseName.equals(FINAL_REVIEW_PHASE_NAME)) {
                    // Final review should follow final fix
                    if (i == 0 ||
                            !previousPhaseName.equals(FINAL_FIX_PHASE_NAME)
                            && !postMortemPhaseExists) {
                        ActionsHelper.addErrorToRequest(request,
                                "error.com.cronos.onlinereview.actions.editProject.FinalReviewMustFollow");
                        arePhasesValid = false;
                    }
                }
            }
        }

        return arePhasesValid;
    }

    /**
     * TODO: Document it
     *
     * @param lazyForm
     * @param dateProperty
     * @param timeProperty
     * @return
     */
    private Date parseDatetimeFormProperties(LazyValidatorForm lazyForm, int propertyIndex, String dateProperty,
            String timeProperty) {
        // Retrieve the values of form properties
        String dateString = (String) lazyForm.get(dateProperty, propertyIndex);
        String timeString = (String) lazyForm.get(timeProperty, propertyIndex);

        // Obtain calendar instance to be used to create Date instance
        Calendar calendar = Calendar.getInstance();

        // Parse date string
        String[] dateParts = dateString.trim().split("[./:-]|([ ])+");
        calendar.set(Calendar.YEAR, Integer.parseInt(dateParts[2]) + (dateParts[2].length() > 2  ? 0 : 2000));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateParts[0]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[1]));

        // Parse time string
        String[] timeParts = timeString.trim().split("[./:-]|([ ])+");
        int hour = Integer.parseInt(timeParts[0]);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        if (timeParts.length == 1) {
            calendar.set(Calendar.MINUTE, 0);
        } else {
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
        }

        // Set seconds, milliseconds
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Returned parsed Date
        return calendar.getTime();
    }

    /**
     * Private helper method to save resources
     *
     * <p>
     *     Updated for Configurable Contest Terms Release Assembly v1.0:
     *     Added Project Role User Terms Of Use verification when adding/updating project resources
     * </p>
     *
     * <p>
     *     Updated for Appeals Early Completion Release Assembly 1.0:
     *     Added Appeals Completed Early flag manipulation when project is saved
     * </p>
     *
     * @param request the HttpServletRequest
     * @param lazyForm the form
     * @param project the project being saved
     * @param projectPhases the project phases being saved
     * @param phasesJsMap the phasesJsMap
     * @throws BaseException if any error occurs
     */
    private void saveResources(HttpServletRequest request, LazyValidatorForm lazyForm,
                               Project project, Phase[] projectPhases, Map<Object, Phase> phasesJsMap) throws BaseException {

        // Obtain the instance of the User Retrieval
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);

        long timelineNotificationId = LookupHelper.getNotificationType("Timeline Notification").getId();

        // Get the array of resource names
        String[] resourceNames = (String[]) lazyForm.get("resources_name");

        // HashSet used to identify resource of new user
        Set<Long> newUsers = new HashSet<Long>();
        Set<Long> newModerators = new HashSet<Long>();
        Set<Long> oldUsers = new HashSet<Long>();
        Set<Long> deletedUsers = new HashSet<Long>();
        Set<Long> newSubmitters = new HashSet<Long>();
        Set<Long> newUsersForumWatch = new HashSet<Long>();

        Set<Long> newUsersForNotification = new HashSet<Long>();
        Set<Long> deletedUsersForNotification = new HashSet<Long>();
        Set<Long> deletedUsersForForumWatch = new HashSet<Long>();

        // 0-index resource is skipped as it is a "dummy" one
        boolean allResourcesValid = true;
        for (int i = 1; i < resourceNames.length; i++) {

            if (resourceNames[i] == null || resourceNames[i].trim().length() == 0) {
                ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                        "error.com.cronos.onlinereview.actions.editProject.Resource.Empty");
                allResourcesValid=false;
                continue;
            }

            // Get info about user with the specified handle
            ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);

            // If there is no user with such handle, indicate an error
            if (user == null) {
                ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                        "error.com.cronos.onlinereview.actions.editProject.Resource.NotFound");
                allResourcesValid=false;
            }
        }

        // validate resources have correct terms of use
        try {
            allResourcesValid = allResourcesValid && validateResourceTermsOfUse(request, lazyForm, project, userRetrieval, resourceNames);
            allResourcesValid = allResourcesValid && validateResourceEligibility(request, lazyForm, project, userRetrieval, resourceNames);
        } catch (RemoteException re) {
            throw new BaseException(re);
        } catch (EJBException e) {
            throw new BaseException(e);
        } catch (ContestEligibilityValidatorException e) {
            throw new BaseException(e);
        }

        // Check for duplicate reviewers and disallowed resource roles
        Set<String> disabledResourceRoles = new HashSet<String>(Arrays.asList(ConfigHelper.getDisabledResourceRoles()));
        Set<String> reviewerHandles = new HashSet<String>();
        Set<String> iterativeReviewerHandles = new HashSet<String>();
        Map<String, String> primaryReviewerRoles = new HashMap<String, String>();

        // Obtain the instance of the Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager();

        for (int i = 1; i < resourceNames.length; i++) {
            String resourceAction = (String) lazyForm.get("resources_action", i);
            if (!"delete".equalsIgnoreCase(resourceAction)) {
                String handle = resourceNames[i];
                long resourceRoleId = (Long) lazyForm.get("resources_role", i);
                String resourcePhaseId = String.valueOf( lazyForm.get("resources_phase", i) );
                String resourceKey = String.valueOf(resourceRoleId) + resourcePhaseId;

                if (disabledResourceRoles.contains(String.valueOf(resourceRoleId))) {
                    boolean attemptingToAssignDisabledRole = false;
                    if ("add".equalsIgnoreCase(resourceAction)) {
                        attemptingToAssignDisabledRole = true;
                    } else if ("update".equalsIgnoreCase(resourceAction)) {
                        Long resourceId = (Long) lazyForm.get("resources_id", i);
                        Resource oldResource = resourceManager.getResource(resourceId);
                        if (oldResource.getResourceRole().getId() != resourceRoleId) {
                            attemptingToAssignDisabledRole = true;
                        }
                    }
                    if (attemptingToAssignDisabledRole) {
                        ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                        "error.com.cronos.onlinereview.actions."
                                                        + "editProject.Resource.RoleDisabled");
                        allResourcesValid = false;
                    }
                }
                if (NODUPLICATE_REVIEWER_ROLE_IDS.contains(resourceRoleId)) {
                    if (reviewerHandles.contains(handle)) {
                        ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                        "error.com.cronos.onlinereview.actions."
                                                        + "editProject.Resource.DuplicateReviewerRole");
                        allResourcesValid = false;
                    } else {
                        reviewerHandles.add(handle);
                    }
                } else if (SINGLE_REVIEWER_ROLE_IDS.contains(resourceRoleId)) {
                    if (primaryReviewerRoles.containsKey(resourceKey)) {
                        if ((resourceRoleId != 9) && (resourceRoleId != 18) && (resourceRoleId != 8)
                            || !primaryReviewerRoles.get(resourceKey).equals(handle)) {
                            ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                            "error.com.cronos.onlinereview.actions."
                                                            + "editProject.Resource.MoreThanOneReviewer");
                            allResourcesValid = false;
                        }
                    } else {
                        primaryReviewerRoles.put(resourceKey, handle);
                    }
                } else if (ITERATIVE_REVIEWER_ROLE_ID == resourceRoleId) {
                    if (iterativeReviewerHandles.contains(handle)) {
                        ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                "error.com.cronos.onlinereview.actions."
                                        + "editProject.Resource.DuplicateIterativeReviewerRole");
                        allResourcesValid = false;
                    } else {
                        iterativeReviewerHandles.add(handle);
                    }
                }
                // Check if the phase related to resource role exists
                ResourceRole role = LookupHelper.getResourceRole(resourceRoleId);
                if (role != null) {
                    Long relatedPhaseTypeId = role.getPhaseType();
                    if (relatedPhaseTypeId != null) {

                        boolean found = false;
                        for(Phase phase : projectPhases){
                            if(phase.getPhaseType().getId() == (relatedPhaseTypeId))
                                found = true;
                        }

                        if (!found) {
                            PhaseType phaseType = LookupHelper.getPhaseType(relatedPhaseTypeId);
                            ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                            new ActionMessage( "error.com.cronos.onlinereview.actions."
                                                                               + "editProject.Resource.PhaseNotFound",
                                                                               phaseType.getName()));
                            allResourcesValid = false;
                        }
                    }
                }
            }
        }

        setResourcePaidRequestAttribute(project, request);
        Map<Long, Boolean> resourcePaid = (Map<Long, Boolean>) request.getAttribute("resourcePaid");
        List<ProjectPayment> allPayments = (List<ProjectPayment>) request.getAttribute("allPayments");
        ProjectPaymentManager projectPaymentManager = ActionsHelper.createProjectPaymentManager();

        // Validate that no submitters who have submitted for project were changed (either by role or handle) or deleted
        // 0-index resource is skipped as it is a "dummy" one
        ReviewManager reviewManager = ActionsHelper.createReviewManager();
        Set<Long> reviewResourceIds = findResourcesWithReviewsForProject(reviewManager, project.getId());
        for (int i = 1; i < resourceNames.length; i++) {
            String resourceAction = (String) lazyForm.get("resources_action", i);
            if (!"add".equals(resourceAction)) {
                Long resourceId = (Long) lazyForm.get("resources_id", i);
                if (resourceId != -1) {
                    Resource oldResource = resourceManager.getResource(resourceId);
                    String oldResourceRoleName = oldResource.getResourceRole().getName();
                    boolean resourceHasSubmissions = false;
                    boolean resourceHasReviews = false;

                    if ("Submitter".equals(oldResourceRoleName)) {
                        Long[] submissionIds = oldResource.getSubmissions();
                        if ((submissionIds != null) && (submissionIds.length > 0)) {
                            resourceHasSubmissions = true;
                        }
                    } else if (REVIEWER_ROLE_NAMES.contains(oldResourceRoleName)) {
                        resourceHasReviews = (reviewResourceIds != null && reviewResourceIds.contains(resourceId));
                    }
                    boolean paid = resourcePaid.get(resourceId) != null && resourcePaid.get(resourceId);
                    boolean resourceUpdateProhibited = resourceHasReviews || resourceHasSubmissions || paid;

                    if (resourceUpdateProhibited) {
                        if ("delete".equalsIgnoreCase(resourceAction)) {
                            if (resourceHasSubmissions) {
                                ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                                "error.com.cronos.onlinereview.actions."
                                                                + "editProject.Resource.TrueSubmitterDeleted");
                            } else if (resourceHasReviews) {
                                ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                                "error.com.cronos.onlinereview.actions."
                                                                + "editProject.Resource.TrueReviewerDeleted");
                            } else {
                                ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                        "error.com.cronos.onlinereview.actions."
                                                + "editProject.Resource.PaidDeleted");
                            }
                            allResourcesValid = false;
                        } else {
                            String oldHandle = (String) oldResource.getProperty("Handle");
                            String newHandle = resourceNames[i];
                            if (!oldHandle.equals(newHandle)) {
                                if (resourceHasSubmissions) {
                                    ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                                    "error.com.cronos.onlinereview.actions."
                                                                    + "editProject.Resource.TrueSubmitterHandleChanged");
                                } else if (resourceHasReviews) {
                                    ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                                    "error.com.cronos.onlinereview.actions."
                                                                    + "editProject.Resource.TrueReviewerHandleChanged");
                                } else {
                                    ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                            "error.com.cronos.onlinereview.actions."
                                                    + "editProject.Resource.PaidHandleChanged");
                                }
                                allResourcesValid = false;
                            }
                            long newResourceRoleId = (Long) lazyForm.get("resources_role", i);
                            if (oldResource.getResourceRole().getId() != newResourceRoleId) {
                                if (resourceHasSubmissions) {
                                    ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                                    "error.com.cronos.onlinereview.actions."
                                                                    + "editProject.Resource.TrueSubmitterRoleChanged");
                                } else if (resourceHasReviews) {
                                    ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                                    "error.com.cronos.onlinereview.actions."
                                                                    + "editProject.Resource.TrueReviewerRoleChanged");
                                } else {
                                    ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                            "error.com.cronos.onlinereview.actions."
                                                    + "editProject.Resource.PaidRoleChanged");
                                }
                                allResourcesValid = false;
                            }
                        }
                    }
                }
            }
        }


        // No resources are updated if at least one of them is incorrect.
        if (!allResourcesValid)
            return;

        // 0-index resource is skipped as it is a "dummy" one
        for (int i = 1; i < resourceNames.length; i++) {

            // Get info about user with the specified handle
            ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);

            Resource resource;

            // Check what is the action to be performed with the resource
            // and obtain Resource instance in appropriate way
            String resourceAction = (String) lazyForm.get("resources_action", i);
            if ("add".equals(resourceAction)) {
                // Create new resource
                resource = new Resource();

                // FIXME: Format this date properly.
                resource.setProperty("Registration Date", DATE_FORMAT.format(new Date()));

                newUsers.add(user.getId());

                ResourceRole role = LookupHelper.getResourceRole((Long) lazyForm.get("resources_role", i));
                if (!role.getName().equals("Observer") || Boolean.parseBoolean(retrieveUserPreference(user.getId(), GLOBAL_TIMELINE_NOTIFICATION))) {
                    newUsersForNotification.add(user.getId());
                }

                //System.out.println("ADD:" + user.getId());
            }  else {
                Long resourceId = (Long) lazyForm.get("resources_id", i);

                if (resourceId != -1) {
                    // Retrieve the resource with the specified id
                    resource = resourceManager.getResource(resourceId);
                    oldUsers.add(user.getId());
                    //System.out.println("REMOVE:" + user.getId());
                } else {
                    // -1 value as id marks the resources that were't persisted in DB yet
                    // and so should be skipped for actions other then "add"
                    oldUsers.add(user.getId());
                    //System.out.println("REMOVE:" + user.getId());
                    continue;
                }
            }

            // If action is "delete", delete the resource and proceed to the next one
            if ("delete".equals(resourceAction)) {
                deletedUsers.add(user.getId());

                // delete project payments
                for (ProjectPayment payment : allPayments) {
                    if (resource.getId() == payment.getResourceId()) {
                        projectPaymentManager.delete(payment.getProjectPaymentId());
                    }
                }
                // delete project_result
                ActionsHelper.deleteProjectResult(project, user.getId(),
                        (Long) lazyForm.get("resources_role", i));
                resourceManager.removeResource(resource,
                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                resourceManager.removeNotifications(new long[] {user.getId()}, project.getId(),
                        timelineNotificationId, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                continue;
            }

            // Set resource properties
            resource.setProject(project.getId());

            boolean resourceRoleChanged = false;
            ResourceRole role = LookupHelper.getResourceRole((Long) lazyForm.get("resources_role", i));
            if (role != null && resource.getResourceRole() != null &&
                role.getId() != resource.getResourceRole().getId()) {
                // delete project_result if old role is submitter
                // populate project_result if new role is submitter and project is component
                ActionsHelper.changeResourceRole(project, user.getId(), resource.getResourceRole().getId(),
                    role.getId());

                resourceRoleChanged = true;

                if (role.getName().equals("Observer")) {
                    // change to observer
                    if (!Boolean.parseBoolean(retrieveUserPreference(user.getId(), GLOBAL_TIMELINE_NOTIFICATION))) {
                        deletedUsersForNotification.add(user.getId());
                    }

                    if (!Boolean.parseBoolean(retrieveUserPreference(user.getId(), GLOBAL_FORUM_WATCH))) {
                        deletedUsersForForumWatch.add(user.getId());
                    }
                }
                if (resource.getResourceRole().getName().equals("Observer")) {
                    // change from observer to other role
                    // add forum watch & notification anyway
                    newUsersForumWatch.add(user.getId());
                    newUsersForNotification.add(user.getId());
                }
            }
            resource.setResourceRole(role);

            resource.setProperty("Handle", resourceNames[i]);

            // Set resource phase id, if needed
            Long phaseTypeId = resource.getResourceRole().getPhaseType();
            if (phaseTypeId != null) {
                Phase phase = phasesJsMap.get(lazyForm.get("resources_phase", i));
                if (phase != null) {
                    resource.setPhase(phase.getId());
                } else {
                    // TODO: Probably issue validation error here
                }
            } else {
                // the resource role is not associated with any phase
                // reset the phase, if the new resource role does not tied to any phase
                resource.setPhase(null);
            }

            // Set resource properties copied from external user
            resource.setProperty("External Reference ID", user.getId());
            // not store in resource info resource.setProperty("Email", user.getEmail());

            String resourceRole = resource.getResourceRole().getName();
            // If resource is a submitter, we need to store appropriate rating and reliability
            // Note, that it is done only in the case resource is added or resource role is changed
            if (resourceRole.equals("Submitter") && (resourceRoleChanged || resourceAction.equals("add"))) {
                if (project.getProjectCategory().getName().equals("Design")) {
                    resource.setProperty("Rating", user.getDesignRating());
                    resource.setProperty("Reliability", user.getDesignReliability());
                } else if (project.getProjectCategory().getName().equals("Development")) {
                    resource.setProperty("Rating", user.getDevRating());
                    resource.setProperty("Reliability", user.getDevReliability());
                }

                // add "Appeals Completed Early" flag.
                resource.setProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY, Constants.NO_VALUE);
            }

            if ("add".equals(resourceAction)) {

                if (resourceRole.equals("Manager") || resourceRole.equals("Observer")
                         || resourceRole.equals("Designer")  || resourceRole.equals("Client Manager")  || resourceRole.equals("Copilot"))
                {
                    // no need for Applications/Components/LCSUPPORT
                    if (!resource.getProperty("Handle").equals("Applications") &&
                        !resource.getProperty("Handle").equals("Components") &&
                        !resource.getProperty("Handle").equals("LCSUPPORT"))
                    {
                        if (!resourceRole.equals("Observer")
                                || Boolean.parseBoolean(retrieveUserPreference(
                                        user.getId(), GLOBAL_FORUM_WATCH))) {
                            newUsersForumWatch.add(user.getId());
                        }
                    }

                }
            }

            // client manager and copilot have moderator role
            if (resourceRole.equals("Client Manager")  || resourceRole.equals("Copilot")
                    || resourceRole.equals("Observer") || resourceRole.equals("Designer"))
            {
                newUsers.remove(user.getId());
                newModerators.add(user.getId());

            }

            // make sure "Appeals Completed Early" flag is not set if the role is not submitter.
            if (resourceRoleChanged && !resourceRole.equals(Constants.SUBMITTER_ROLE_NAME)) {
                resource.setProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY, null);
            }

            // Save the resource in the persistence level
            resourceManager.updateResource(resource, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

            if ("add".equals(resourceAction) && resourceRole.equals("Submitter")) {
                newSubmitters.add(user.getId());
            }
        }

        // check the list of users to delete and remove those still have other roles
        Resource[] allProjectResources = ActionsHelper.getAllResourcesForProject(project);
        Set<Long> usersToKeep = new HashSet<Long>();
        for (Long id : deletedUsers) {
            for (Resource projectResource : allProjectResources) {
                long userId = Long.parseLong(((String) projectResource.getProperty("External Reference ID")).trim());
                if (userId == id) {
                    // still have other roles
                    usersToKeep.add(userId);
                    break;
                }
            }
        }
        deletedUsers.removeAll(usersToKeep);


        for (Long id : oldUsers) {
            newUsers.remove(id);
        }

        // Populate project_result and component_inquiry for new submitters
        ActionsHelper.populateProjectResult(project, newSubmitters);

        // delete timeline notifications
        long[] idsToDeletedForNotification = new long[deletedUsersForNotification.size()];
        int k = 0;
        for (long id : deletedUsersForNotification) {
            idsToDeletedForNotification[k++] = id;
        }
        resourceManager.removeNotifications(idsToDeletedForNotification, project.getId(),
                timelineNotificationId, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        // Update all the timeline notifications
        if (project.getProperty("Timeline Notification").equals("On") && !newUsersForNotification.isEmpty()) {
            // Remove duplicated user ids
            long[] existUserIds = resourceManager.getNotifications(project.getId(), timelineNotificationId);
            Set<Long> finalUsers = new HashSet<Long>(newUsersForNotification);

            for (long existUserId : existUserIds) {
                finalUsers.remove(existUserId);
            }

            finalUsers.remove((long)22770213); // Applications user
            finalUsers.remove((long)22719217); // Components user
            finalUsers.remove((long)22873364); // LCSUPPORT user

            long[] userIds = new long[finalUsers.size()];
            int i = 0;
            for (Long id : finalUsers) {
                userIds[i++] = id;
            }

            resourceManager.addNotifications(userIds, project.getId(),
                    timelineNotificationId, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        // Add forum permissions for all new users and remove permissions for removed resources.
        ActionsHelper.removeForumPermissions(project, deletedUsers);
        ActionsHelper.addForumPermissions(project, newUsers, false);
        ActionsHelper.addForumPermissions(project, newModerators, true);

        long forumId = 0;
        if (project.getProperty("Developer Forum ID") != null && (Long) project.getProperty("Developer Forum ID") != 0) {
            forumId = ((Long) project.getProperty("Developer Forum ID"));
        }

        ActionsHelper.removeForumWatch(project, deletedUsers, forumId);
        ActionsHelper.removeForumWatch(project, deletedUsersForForumWatch, forumId);
        ActionsHelper.addForumWatch(project, newUsersForumWatch, forumId);
    }

    /**
     * Helper method to validate if resources in the request have the required terms of use
     *
     * @param request the current <code>HttpServletRequest</code>.
     * @param lazyForm the edition <code>LazyValidatorForm</code>.
     * @param project the edited <code>Project</code>.
     * @param userRetrieval a <code>UserRetrieval</code> instance to obtain the user id.
     * @param resourceNames a <code>String[]</code> containing edited resource names.
     *
     * @throws RemoteException if any errors occur during EJB remote invocation
     * @throws EJBException if any other errors occur while invoking EJB services
     * @throws BaseException if any other errors occur while retrieving user
     *
     * @return true if all resources are valid
     * @since 1.1
     */
    private boolean validateResourceTermsOfUse(HttpServletRequest request, LazyValidatorForm lazyForm,
            Project project, UserRetrieval userRetrieval, String[] resourceNames)
            throws RemoteException, EJBException, BaseException {

                boolean allResourcesValid = true;

                // get remote services
                // check if the user agreed to all terms of use
                ProjectTermsOfUseDao projectTermsOfUseDao = ActionsHelper.getProjectTermsOfUseDao();
                UserTermsOfUseDao userTermsOfUseDao = ActionsHelper.getUserTermsOfUseDao();

                // validate that new resources have agreed to the necessary terms of use
                // 0-index resource is skipped as it is a "dummy" one
                for (int i = 1; i < resourceNames.length; i++) {
                    if (resourceNames[i] != null && resourceNames[i].trim().length() > 0) {
                        ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);
                        String resourceAction = (String) lazyForm.get("resources_action", i);
                        // check for additions or modifications
                        if (!"delete".equals(resourceAction)) {
                            long roleId = (Long) lazyForm.get("resources_role", i);
                            long userId = user.getId();

                            Map<Integer, List<TermsOfUse>> necessaryTerms =
                                projectTermsOfUseDao.getTermsOfUse((int) project.getId(), (int) roleId, null);

                            if (necessaryTerms != null && !necessaryTerms.isEmpty()) {
                                boolean hasGroupWithAllTermsAccepted = false;
                                StringBuilder b = new StringBuilder();
                                for (Integer groupId : necessaryTerms.keySet()) {
                                    b.append("Group " + groupId + ":<br/>");
                                    boolean hasNonAcceptedTermsForGroup = false;
                                    List<TermsOfUse> groupTermsOfUse = necessaryTerms.get(groupId);
                                    for (TermsOfUse terms : groupTermsOfUse) {
                                        long termsId = terms.getTermsOfUseId();
                                        // check if the user has this terms
                                        if (!userTermsOfUseDao.hasTermsOfUse(userId, termsId)) {
                                            hasNonAcceptedTermsForGroup = true;
                                            b.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(terms.getTitle()).append("<br/>");
                                        } else {
                                            b.append("&nbsp;&nbsp;<img src='/i/checkmark.jpg'/>").append(terms.getTitle()).append("<br/>");
                                        }
                                    }
                                    if (!hasNonAcceptedTermsForGroup) {
                                        hasGroupWithAllTermsAccepted = true;
                                        break; // User has at least one terms group with all terms from that group accepted
                                        // so there is no need to check other terms of use groups
                                    }
                                }
                                if (!hasGroupWithAllTermsAccepted) {
                                    ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                                    new ActionMessage(
                                                                        "error.com.cronos.onlinereview.actions.editProject.Resource.MissingGroupTerms",
                                                                        b.toString()));
                                    allResourcesValid = false;

                                }
                            }
                        }
                    }
                }

                return allResourcesValid;
    }

    /**
     * Helper method to validate if resources in the request are eligible for the project
     *
     * @param request the current <code>HttpServletRequest</code>.
     * @param lazyForm the edition <code>LazyValidatorForm</code>.
     * @param project the edited <code>Project</code>.
     * @param userRetrieval a <code>UserRetrieval</code> instance to obtain the user id.
     * @param resourceNames a <code>String[]</code> containing edited resource names.
     *
     * @throws EJBException if any other errors occur while invoking EJB services
     * @throws BaseException if any other errors occur while retrieving user
     *
     * @return true if all resources are valid
     * @since 1.1
     */
    private boolean validateResourceEligibility(HttpServletRequest request, LazyValidatorForm lazyForm,
            Project project, UserRetrieval userRetrieval, String[] resourceNames)
            throws EJBException, BaseException, ContestEligibilityValidatorException {

        boolean allResourcesValid = true;


        // validate that new resources have agreed to the necessary terms of use
        // 0-index resource is skipped as it is a "dummy" one
        for (int i = 1; i < resourceNames.length; i++) {
            if (resourceNames[i] != null && resourceNames[i].trim().length() > 0) {
                ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);
                String resourceAction = (String) lazyForm.get("resources_action", i);
                // check for additions or modifications
                if (!"delete".equals(resourceAction)) {
                    long userId = user.getId();

                    // dont check Applications or Components
                    if (resourceNames[i].equals("Applications") ||
                        resourceNames[i].equals("Components") ||
                        resourceNames[i].equals("LCSUPPORT"))
                    {
                        continue;
                    }

                    // dont check project creator
                    if (project.getCreationUser().equals(Long.toString(userId)))
                    {
                        continue;
                    }

                    if (!EJBLibraryServicesLocator.getContestEligibilityService().isEligible(userId, project.getId(),
                                                                                             false))
                    {
                        ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                        new ActionMessage("error.com.cronos.onlinereview.actions.editProject.Resource.NotEligible"));

                        allResourcesValid=false;
                    }
                }
            }
        }

        return allResourcesValid;
    }

    /**
     * This method is an implementation of &quot;List Projects&quot; Struts Action defined for this
     * assembly, which is supposed to fetch list of projects from the database and pass it to the
     * JSP page for subsequent presentation to the end user.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/listProjects.jsp page (as
     *         defined in struts-config.xml file).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the HTTP request.
     * @param response
     *            the HTTP response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward listProjects(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException {

        // Remove redirect-after-login attribute (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(true, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Retrieve the value of "scope" parameter
        String scope = request.getParameter("scope");
        // Verify that "scope" parameter is specified and is not empty
        if (scope == null || scope.trim().length() == 0) {
            // Set default value for "scope" parameter, if previous condition has not been met
            scope = "my";
        }

        // If the user is trying to access pages he doesn't have permission to view,
        // redirect him to scope-all page, where public projects are listed
        if (scope.equalsIgnoreCase("my") && !AuthorizationHelper.isUserLoggedIn(request)) {
            return mapping.findForward("all");
        }

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager();
        // This variable will specify the index of active tab on the JSP page
        int activeTab;

        // Determine projects displayed and index of the active tab
        // based on the value of the "scope" parameter
        if (scope.equalsIgnoreCase("my")) {
            activeTab = 1;
        } else if (scope.equalsIgnoreCase("draft")) {
            activeTab = 4;
        } else {
            activeTab = 2;
        }

        // Pass the index of the active tab into request
        request.setAttribute("projectTabIndex", activeTab);

        // Get all project types defined in the database (e.g. Assembly, Component, etc.)
        ProjectType[] projectTypes = manager.getAllProjectTypes();

        // Sort project types by their names in ascending order
        Arrays.sort(projectTypes, new Comparators.ProjectTypeComparer());

        // Get all project categories defined in the database (e.g. Design, Security, etc.)
        ProjectCategory[] projectCategories = manager.getAllProjectCategories();

        request.setAttribute("projectTypes", projectTypes);
        request.setAttribute("projectCategories", projectCategories);

        ProjectPropertyType[] projectInfoTypes = manager.getAllProjectPropertyTypes();

        int[] typeCounts = new int[projectTypes.length];
        int[] categoryCounts = new int[projectCategories.length];
        String[] categoryIconNames = new String[projectCategories.length];

        // This is to signify whether "My" Projects list is displayed, or any other
        // type of Projects List. Some columns are present only in "My" Projects List
        boolean myProjects = scope.equalsIgnoreCase("my");

        Project[][] projects = new Project[projectCategories.length][];
        String[][] rootCatalogIcons = new String[projectCategories.length][];
        String[][] rootCatalogNames = new String[projectCategories.length][];
        Phase[][][] phases = new Phase[projectCategories.length][][];
        Date[][] phaseEndDates = new Date[projectCategories.length][];
        Date[][] projectEndDates = new Date[projectCategories.length][];

        // The following will only be non-null for the list of "My" Projects
        Resource[][][] myResources = (myProjects) ? new Resource[projectCategories.length][][] : null;
        String[][] myRoles = (myProjects) ? new String[projectCategories.length][] : null;
        String[][] myDeliverables = (myProjects) ? new String[projectCategories.length][] : null;

        // Fetch projects from the database. These projects will require further grouping
        ProjectStatus draftStatus = LookupHelper.getProjectStatus("Draft");
        ProjectStatus activeStatus = LookupHelper.getProjectStatus("Active");
        long userId = AuthorizationHelper.getLoggedInUserId(request);
        Project[] ungroupedProjects;
        ProjectDataAccess projectDataAccess = new ProjectDataAccess();
        ProjectStatus[] projectStatuses = manager.getAllProjectStatuses();
        if (activeTab != 1) {
            if (activeTab == 4) {
                ungroupedProjects = projectDataAccess.searchDraftProjects(projectStatuses, projectCategories,
                                                                          projectInfoTypes);
            } else {
                ungroupedProjects = projectDataAccess.searchActiveProjects(projectStatuses, projectCategories,
                                                                           projectInfoTypes);
            }
        } else {
            // user projects
            ungroupedProjects = projectDataAccess.searchUserActiveProjects(userId, projectStatuses, projectCategories,
                                                                           projectInfoTypes);
        }

        // Sort fetched projects. Currently sorting is done by projects' names only, in ascending order
        Arrays.sort(ungroupedProjects, new Comparators.ProjectNameComparer());

        List<Long> projectFilters = new ArrayList<Long>();
        for (Project ungroupedProject : ungroupedProjects) {
            projectFilters.add(ungroupedProject.getId());
        }

        Resource[] allMyResources = null;
        if (ungroupedProjects.length != 0 && AuthorizationHelper.isUserLoggedIn(request)) {
            if (activeTab == 1) {  // My projects
                allMyResources = ActionsHelper.searchUserResources(userId, activeStatus);
            } else if (activeTab == 2) { // Active projects
                allMyResources = ActionsHelper.searchUserResources(userId, activeStatus);
            } else if (activeTab == 4) { // Draft projects
                allMyResources = ActionsHelper.searchUserResources(userId, draftStatus);
            }
        }

        // new eligibility constraints
        // if the user is not a global manager and is seeing all projects eligibility checks need to be performed
        if (!AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME) &&
                (scope.equalsIgnoreCase("all") || scope.equalsIgnoreCase("draft")) && projectFilters.size() > 0) {

            // remove those projects that the user can't see
            ungroupedProjects = filterUsingEligibilityConstraints(
                    ungroupedProjects, projectFilters, allMyResources);
        }

        // Obtain an instance of Phase Manager
        PhaseManager phMgr = ActionsHelper.createPhaseManager(false);

        PhaseStatus[] phaseStatuses = phMgr.getAllPhaseStatuses();

        PhaseType[] phaseTypes = phMgr.getAllPhaseTypes();

        ProjectPhaseDataAccess phasesDataAccess = new ProjectPhaseDataAccess();
        Map<Long, com.topcoder.project.phases.Project> phProjects;
        if (activeTab != 1) {
            if (activeTab == 4) {
                phProjects = phasesDataAccess.searchDraftProjectPhases(phaseStatuses, phaseTypes);
            } else {
                phProjects = phasesDataAccess.searchActiveProjectPhases(phaseStatuses, phaseTypes);
            }
        } else {
            // user projects
            phProjects = phasesDataAccess.searchUserProjectPhases(userId, phaseStatuses, phaseTypes);
        }

        // Message Resources to be used for this request
        MessageResources messages = getResources(request);

        for (int i = 0; i < projectCategories.length; ++i) {
            // Count number of projects in this category
            for (Project ungroupedProject : ungroupedProjects) {
                if (ungroupedProject.getProjectCategory().getId() == projectCategories[i].getId()) {
                    ++categoryCounts[i];
                }
            }

            /*
             * Now, as the exact count of projects in this category is known,
             * it is possible to initialize arrays
             */
            Project[] projs = new Project[categoryCounts[i]]; // This Category's Projects
            String[] rcIcons = new String[categoryCounts[i]]; // Root Catalog Icons
            String[] rcNames = new String[categoryCounts[i]]; // Root Catalog Names (shown in tooltip)
            Phase[][] phass = new Phase[categoryCounts[i]][]; // Projects' active Phases
            Date[] pheds = new Date[categoryCounts[i]]; // End date of every first active phase
            Date[] preds = new Date[categoryCounts[i]]; // Projects' end dates

            // No need to collect any Resources or Roles if
            // the list of projects is not just "My" Projects
            Resource[][] myRss = (myProjects) ? new Resource[categoryCounts[i]][] : null;
            String[] rols = (myProjects) ? new String[categoryCounts[i]] : null;

            if (categoryCounts[i] != 0) {
                // Counter of projects currently added to this category
                int counter = 0;
                // Copy ungrouped projects into group of this category
                for (Project ungroupedProject : ungroupedProjects) {
                    // Skip projects that are not in this category
                    // (they'll be processed later, or have already been processed)
                    if (ungroupedProject.getProjectCategory().getId() != projectCategories[i].getId()) {
                        continue;
                    }

                    // Get a project to store in current group
                    Project project = ungroupedProject;
                    // Get this project's Root Catalog ID
                    String rootCatalogId = (String) project.getProperty("Root Catalog ID");

                    // Fetch Root Catalog icon's filename depending on ID of the Root Catalog
                    rcIcons[counter] = ConfigHelper.getRootCatalogIconNameSm(rootCatalogId);
                    // Fetch Root Catalog name depending depending on ID of the Root Catalog
                    rcNames[counter] = messages.getMessage(ConfigHelper.getRootCatalogAltTextKey(rootCatalogId));

                    Phase[] activePhases = null;

                    // Calculate end date of the project and get all active phases (if any)
                    if (phProjects.containsKey(project.getId())) {
                        com.topcoder.project.phases.Project phProject = phProjects.get(project.getId());
                        preds[counter] = phProject.calcEndDate();
                        activePhases = ActionsHelper.getActivePhases(phProject.getAllPhases());
                        pheds[counter] = null;
                    }

                    // Get currently open phase end calculate its end date
                    if (activePhases != null && activePhases.length != 0) {
                        phass[counter] = activePhases;
                        pheds[counter] = activePhases[0].getScheduledEndDate();
                    }

                    // Retrieve information about my roles, and my current unfinished deliverables
                    if (myProjects) {
                        Resource[] myResources2 = ActionsHelper.getResourcesForProject(allMyResources, project);
                        myRss[counter] = myResources2;
                        rols[counter] = getRolesFromResources(messages, myResources2);
                    }

                    // Store project in a group and increment counter
                    projs[counter] = project;
                    ++counter;
                }
            }

            // Save collected data in main arrays
            projects[i] = projs;
            rootCatalogIcons[i] = rcIcons;
            rootCatalogNames[i] = rcNames;
            phases[i] = phass;
            phaseEndDates[i] = pheds;
            projectEndDates[i] = preds;

            // Resources and roles must not always be saved
            if (myProjects) {
                myResources[i] = myRss;
                myRoles[i] = rols;
            }

            // Fetch Project Category icon's filename depending on the name of the current category
            categoryIconNames[i] = ConfigHelper.getProjectCategoryIconNameSm(projectCategories[i].getName());
        }

        if (ungroupedProjects.length != 0 && myProjects) {
            Deliverable[] allMyDeliverables = getDeliverables(
                    ActionsHelper.createDeliverableManager(), projects, phases, myResources);

            // Group the deliverables per projects in list
            for (int i = 0; i < projects.length; ++i) {
                String[] deliverables = new String[projects[i].length];
                for (int j = 0; j < projects[i].length; ++j) {
                    String winnerIdStr = (String) projects[i][j].getProperty("Winner External Reference ID");
                    if (winnerIdStr != null && winnerIdStr.trim().length() == 0) {
                        winnerIdStr = null;
                    }

                    deliverables[j] = getMyDeliverablesForPhases(
                            messages, allMyDeliverables, phases[i][j], myResources[i][j], winnerIdStr);
                }
                myDeliverables[i] = deliverables;
            }
        }

        int totalProjectsCount = 0;

        // Count projects in every type group now
        for (int i = 0; i < projectTypes.length; ++i) {
            for (int j = 0; j < projectCategories.length; ++j) {
                if (projectCategories[j].getProjectType().getId() == projectTypes[i].getId()) {
                    typeCounts[i] += categoryCounts[j];
                }
            }
            totalProjectsCount += typeCounts[i];
        }

        // Place all collected data into the request as attributes
        request.setAttribute("projects", projects);
        request.setAttribute("rootCatalogIcons", rootCatalogIcons);
        request.setAttribute("rootCatalogNames", rootCatalogNames);
        request.setAttribute("phases", phases);
        request.setAttribute("phaseEndDates", phaseEndDates);
        request.setAttribute("projectEndDates", projectEndDates);
        request.setAttribute("typeCounts", typeCounts);
        request.setAttribute("categoryCounts", categoryCounts);
        request.setAttribute("totalProjectsCount", totalProjectsCount);
        request.setAttribute("categoryIconNames", categoryIconNames);

        // If the currently displayed list is a list of "My" Projects, add some more attributes
        if (myProjects) {
            request.setAttribute("isMyProjects", myProjects);
            request.setAttribute("myRoles", myRoles);
            request.setAttribute("myDeliverables", myDeliverables);
        }

        // Signal about successful execution of the Action
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method will return an array of <code>Project</code> with those projects the user can see taking into
     * consideration eligibility constraints.
     *
     * The user can see all those "public" (no eligibility constraints) projects plus those non-public projects where
     * he is assigned as a resource.
     *
     * @param ungroupedProjects all project to be displayed
     * @param projectFilters all project ids to be displayed
     * @param allMyResources all resources the user has for the projects to be displayed
     *
     * @return a <code>Project[]</code> with those projects that the user can see.
     *
     * @throws BaseException if any error occurs during eligibility services call
     *
     * @since 1.4
     */
    private Project[] filterUsingEligibilityConstraints(Project[] ungroupedProjects, List<Long> projectFilters,
            Resource[] allMyResources) throws BaseException {
        // check which projects have eligibility constraints
        Set<Long> projectsWithEligibilityConstraints;
        try {
            projectsWithEligibilityConstraints =
                EJBLibraryServicesLocator.getContestEligibilityService().haveEligibility(
                    projectFilters.toArray(new Long[projectFilters.size()]), false);
        } catch (Exception e) {
            log.error("It was not possible to retrieve eligibility constraints: "+e);
            throw new BaseException("It was not possible to retrieve eligibility constraints", e);
        }

        // create a set of projects where the user is a resource
        Set<Long> resourceProjects = new HashSet<Long>();
        if (allMyResources != null) {
            for (Resource r: allMyResources) {
                resourceProjects.add(r.getProject());
            }
        }

        // user can see those projects with eligibility constraints where he is a resource, so remove these
        // from the projectsWithEligibilityConstraints set
        projectsWithEligibilityConstraints.removeAll(resourceProjects);

        // finally remove those projects left in projectsWithEligibilityConstraints from ungroupedProjects
        List<Project> visibleProjects = new ArrayList<Project>();
        for (Project p : ungroupedProjects) {
            if (!projectsWithEligibilityConstraints.contains(p.getId())) {
                visibleProjects.add(p);
            }
        }

        ungroupedProjects = visibleProjects.toArray(new Project[visibleProjects.size()]);
        return ungroupedProjects;
    }

    /**
     * This static method performs a search for all outstanding deliverables. The list of these
     * deliverables is returned as is, i.e. as one-dimensional array, and will require further
     * grouping.
     *
     * @return an array of outstanding (incomplete) deliverables.
     * @param manager
     *            an instance of <code>DeliverableManager</code> class that will be used to
     *            perform a search for deliverables.
     * @param projects
     *            an array of the projects to search the deliverables for.
     * @param phases
     *            an array of active phases for the projects specified by <code>projects</code>
     *            parameter. The deliverables found will only be related to these phases.
     * @param resources
     *            an array of resources to search the deliverables for. Each of the deliverables
     *            found will have to be complited by one of the resources from this array.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws DeliverablePersistenceException
     *             if there is an error reading the persistence store.
     * @throws SearchBuilderException
     *             if there is an error executing the filter.
     * @throws DeliverableCheckingException
     *             if there is an error determining whether some Deliverable has been completed or
     *             not.
     */
    private static Deliverable[] getDeliverables(DeliverableManager manager, Project[][] projects, Phase[][][] phases,
            Resource[][][] resources)
            throws DeliverablePersistenceException, SearchBuilderException, DeliverableCheckingException {
        DeliverableDataAccess deliverableDataAccess = new DeliverableDataAccess();
        Map<Long, Map<Long, Long>> deliverableTypes = deliverableDataAccess.getDeliverablesList();

        // Validate parameters
        ActionsHelper.validateParameterNotNull(manager, "manager");
        ActionsHelper.validateParameterNotNull(projects, "projects");
        ActionsHelper.validateParameterNotNull(phases, "phases");
        ActionsHelper.validateParameterNotNull(resources, "resources");

        List<Long> projectIds = new ArrayList<Long>();
        List<Long> phaseTypeIds = new ArrayList<Long>();
        List<Long> resourceIds = new ArrayList<Long>();

        for (int i = 0; i < projects.length; ++i) {
            for (int j = 0; j < projects[i].length; ++j) {
                projectIds.add(projects[i][j].getId());

                // Get an array of active phases for the project
                Phase[] activePhases = phases[i][j];
                // If there are no active phases, no need to select deliverables for this project
                if (activePhases == null) {
                    continue;
                }

                for (Phase activePhase : activePhases) {
                    phaseTypeIds.add(activePhase.getId());
                }

                // Get an array of "my" resources for the active phases
                Resource[] myResources = resources[i][j];
                // If there are no "my" resources, skip the rest of the loop
                if (myResources == null) {
                    continue;
                }

                // Filter out those resources which do not correspond to active phases. If resource has phase set
                // explicitly (but is not one of the reviewer roles) then check if it's phase is in list of active
                // phases; otherwise check if it's role has a deliverable for one of the active phases
                for (Resource myResource : myResources) {
                    boolean toAdd = false;
                    long resourceRoleId = myResource.getResourceRole().getId();
                    boolean isReviewer = (resourceRoleId == 4) || (resourceRoleId == 5) || (resourceRoleId == 6)
                            || (resourceRoleId == 7);
                    for (int m = 0; !toAdd && (m < activePhases.length); m++) {
                        Phase activePhase = activePhases[m];
                        if (myResource.getPhase() != null && !isReviewer) {
                            toAdd = (activePhase.getId() == myResource.getPhase());
                        } else {
                            Map<Long, Long> roleDeliverables
                                    = deliverableTypes.get(myResource.getResourceRole().getId());
                            if (roleDeliverables != null) {
                                if (roleDeliverables.containsKey(activePhase.getPhaseType().getId())) {
                                    toAdd = true;
                                }
                            }
                        }
                    }

                    if (toAdd) {
                        resourceIds.add(myResource.getId());
                    }
                }
            }
        }

        // If any of the sets is empty, there cannot be any deliverables
        if (projectIds.isEmpty() || phaseTypeIds.isEmpty() || resourceIds.isEmpty()) {
            return new Deliverable[0]; // No deliverables
        }

        // Build filters to select deliverables
        Filter filterProjects = new InFilter("project_id", projectIds);
        Filter filterPhases = new InFilter("phase_id", phaseTypeIds);
        Filter filterResources = new InFilter("resource_id", resourceIds);
        // Build final combined filter
        Filter filter = new AndFilter(Arrays.asList(filterProjects, filterPhases, filterResources));

        // Get and return an array of my incomplete deliverables for all active phases.
        // These deliverables will require further grouping
        return manager.searchDeliverables(filter, Boolean.FALSE);
    }

    /**
     * This static method returns a string that lists all the different roles the resources
     * specified by <code>resources</code> array have. The roles will be delimited by
     * line-breaking tag (<code>&lt;br&#160;/&gt;</code>). If there are no resources in
     * <code>resources</code> array or no roles have been found, this method returns a string that
     * denotes Public role (usually this string just says &quot;Public&quot;).
     *
     * @return a human-readable list of resource roles.
     * @param messages
     *            an instance of <code>MessageResources</code> class used to retrieve textual
     *            representation of resource roles in different locales.
     * @param resources
     *            an array of the roles to determine the names of their resource roles.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    private static String getRolesFromResources(MessageResources messages, Resource[] resources) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(messages, "messages");
        ActionsHelper.validateParameterNotNull(resources, "resources");

        if (resources == null || resources.length == 0) {
            return messages.getMessage("ResourceRole.Public");
        }

        StringBuilder buffer = new StringBuilder();
        Set<String> rolesSet = new HashSet<String>();

        for (Resource resource : resources) {
            // Get the name for a resource in the current iteration
            String resourceRole = resource.getResourceRole().getName();

            if (rolesSet.contains(resourceRole)) {
                continue;
            }

            if (buffer.length() != 0) {
                buffer.append("<br />");
            }
            buffer.append(messages.getMessage("ResourceRole." + resourceRole.replaceAll(" ", "")));
            rolesSet.add(resourceRole);
        }

        return (buffer.length() != 0) ? buffer.toString() : messages.getMessage("ResourceRole.Public");
    }

    /**
     * This static method returns a string that lists all the different outstanding (i.e.
     * incomplete) deliverables the resources specified by <code>resources</code> array have. The
     * deliverables will be delimited by line-breaking tag (<code>&lt;br&#160;/&gt;</code>). If
     * any of the arrays passed to this method is <code>null</code> or empty, or no deliverables
     * have been found, this method returns empty string.
     *
     * @return a human-readable list of deliverables.
     * @param messages
     *            an instance of <code>MessageResources</code> class used to retrieve textual
     *            representation of deliverables' names in different locales.
     * @param deliverables
     *            an array of deliverables to fetch outstanding deliverables (and their names) from.
     * @param phases
     *            an array of phases to look up the deliverables for.
     * @param resources
     *            an array of resources to look up the deliverables for.
     * @param winnerExtUserId
     *            an External User ID of the winning user for the project, if any. If there is no
     *            winner for the project, this parameter must be <code>null</code>.
     * @throws IllegalArgumentException
     *             if parameter <code>messages</code> is <code>null</code>.
     * @throws BaseException if an unexpected error occurs.
     */
    private static String getMyDeliverablesForPhases(MessageResources messages,
        Deliverable[] deliverables, Phase[] phases, Resource[] resources, String winnerExtUserId)
        throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(messages, "messages");

        if (deliverables == null || deliverables.length == 0 ||
                phases == null || phases.length == 0 ||
                resources == null || resources.length == 0) {
            return null; // No deliverables
        }

        StringBuilder buffer = new StringBuilder();
        Set<String> deliverablesSet = new HashSet<String>();

        for (Deliverable deliverable : deliverables) {
            // Get a deliverable for the current iteration
            // Check if this deliverable is for any of the phases in question
            int j = 0;

            for (; j < phases.length; ++j) {
                if (deliverable.getPhase() == phases[j].getId()) {
                    break;
                }
            }
            // If this deliverable is not for any of the phases, continue the search
            if (j == phases.length) {
                continue;
            }

            for (j = 0; j < resources.length; ++j) {
                if (deliverable.getResource() == resources[j].getId()) {
                    break;
                }
            }
            // If this deliverable is not for any of the resources, continue the search
            if (j == resources.length) {
                continue;
            }

            // Get a resource this deliverable is for
            final Resource forResource = resources[j];

            // Some additional special checking is need for Specification Submission type of deliverables
            if (Constants.SPECIFICATION_SUBMISSION_DELIVERABLE_NAME.equals(deliverable.getName())) {
                Submission[] submissions= ActionsHelper.getProjectSubmissions(
                    phases[0].getProject().getId(), Constants.SPECIFICATION_SUBMISSION_TYPE_NAME,
                    Constants.ACTIVE_SUBMISSION_STATUS_NAME, false);
                if (submissions != null && submissions.length > 0 &&
                        submissions[0].getUpload().getOwner() != deliverable.getResource()) {
                    continue;
                }
            }

            // Skip deliverables that are not for winning submitter
            if (winnerExtUserId != null) {
                if (forResource.getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME) &&
                        !winnerExtUserId.equals(resources[j].getProperty("External Reference ID"))) {
                    continue;
                }
            }

            // Get the name of the deliverable
            String deliverableName = deliverable.getName();
            // Do not add the same deliverable twice
            if (deliverablesSet.contains(deliverableName)) {
                continue;
            }

            // If this is not the first deliverable, add line-breaking tag
            if (buffer.length() != 0) {
                buffer.append("<br />");
            }
            buffer.append(messages.getMessage("Deliverable." + deliverableName.replaceAll(" ", "")));
            deliverablesSet.add(deliverableName);
        }

        return (buffer.length() != 0) ? buffer.toString() : null;
    }

    /**
     * <p>Checks if specified resource is assigned <code>Submitter</code> role or not.</p>
     *
     * @param resource a <code>Resource</code> to be verified.
     * @return <code>true</code> if specified resource is assigned <code>Submitter</code> role; <code>false</code>
     *         otherwise.
     * @since BUGR-2807
     */
    private static boolean isSubmitter(Resource resource) {
        ResourceRole role = resource.getResourceRole();
        return (role != null) && (role.getId() == 1);
    }

    /**
     * <p>Builds the map to be used for looking up the project categories by IDs.</p>
     *
     * @param categories a <code>ProjectCategory</code> array listing existing project categories.
     * @return a <code>Map</code> mapping the category IDs to categories.
     * @since 1.6
     */
    private static Map<Long, ProjectCategory> buildProjectCategoriesLookupMap(ProjectCategory[] categories) {
        Map<Long, ProjectCategory> map = new HashMap<Long, ProjectCategory>(categories.length);
        for (ProjectCategory category : categories) {
            map.put(category.getId(), category);
        }
        return map;
    }

    /**
     * <p>Gets the set of project resources which have the review scorecards associated with them.</p>
     *
     * @param manager an instance of <code>ReviewManager</code> class that retrieves a review from the database.
     * @param projectId an ID of the project which the review was made for.
     * @return a <code>Set</code> providing the Ids of project resources which have review scorecards associated with
     *         them.
     * @throws ReviewManagementException if any error occurs during review search or retrieval.
     * @since 1.8
     */
    private static Set<Long> findResourcesWithReviewsForProject(ReviewManager manager, Long projectId)
        throws ReviewManagementException {
        Filter filterProject = new EqualToFilter("project", projectId);
        Set<Long> result = new HashSet<Long>();
        Review[] reviews = manager.searchReviews(filterProject, false);
        for (Review review : reviews) {
            result.add(review.getAuthor());
        }
        return result;
    }

    /**
     * <p>Checks if specified string is empty.</p>
     *
     * @param value <code>true</code> if specified value is <code>null</code> or empty; <code>false</code> otherwise.
     */
    private static boolean isEmpty(String value) {
        return (value == null) || (value.trim().length() == 0);
    }

    /**
     * <p>Sets the basic form data for <code>Edit Project</code> sceen.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param verification a <code>CorrectnessCheckResult</code> providing the verificaiton result.
     * @param form a <code>LazyValidatorForm</code> providing the current action form.
     * @throws BaseException if an unexpected error occurs.
     */
    private void setEditProjectFormData(HttpServletRequest request, CorrectnessCheckResult verification,
                                        LazyValidatorForm form) throws BaseException {

        // Load the lookup data
        loadProjectEditLookups(request);
        String phaseDependenciesEditable = null;
        if (verification != null) {
            phaseDependenciesEditable = (String) verification.getProject().getProperty("Phase Dependencies Editable");
        }
        request.setAttribute("arePhaseDependenciesEditable", "true".equalsIgnoreCase(phaseDependenciesEditable));
        // Place the flag, indicating that we are editing the existing project, into request
        request.setAttribute("newProject", Boolean.FALSE);

        ProjectManager manager = ActionsHelper.createProjectManager();
        ProjectStatus[] projectStatuses = manager.getAllProjectStatuses();
        request.setAttribute("projectStatuses", projectStatuses);
        request.setAttribute("project", verification.getProject());

        populateProjectFormDefaults(form, request);
    }

    /**
     * <p>Sets the basic request attributes for <code>Edit Project</code> sceen.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param project a <code>Project</code> representing the project to edit.
     * @param resources a <code>Resource</code> array listing existing project resources.
     * @param externalUsers an <code>ExternalUser</code> array listing the users mapped to specified resources.
     * @param phases a <code>Phase</code> array listing existing project phases.
     * @throws BaseException if an unexpected error occurs.
     */
    private void setEditProjectRequestAttributes(HttpServletRequest request, Project project, Resource[] resources,
                                                 ExternalUser[] externalUsers, Phase[] phases) throws BaseException {
        PhasesDetails phasesDetails = PhasesDetailsServices.getPhasesDetails(
                request, getResources(request), project, phases, resources, externalUsers);

        request.setAttribute("phaseGroupIndexes", phasesDetails.getPhaseGroupIndexes());
        request.setAttribute("phaseGroups", phasesDetails.getPhaseGroups());
        request.setAttribute("activeTabIdx", phasesDetails.getActiveTabIndex());

        request.setAttribute("isManager",
                AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES));
        request.setAttribute("isAllowedToPerformScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_SCREENING_PERM_NAME) &&
                        ActionsHelper.getPhase(phases, true, Constants.SCREENING_PHASE_NAME) != null);
        request.setAttribute("isAllowedToPerformCheckpointScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_CHECKPOINT_SCREENING_PERM_NAME) &&
                        ActionsHelper.getPhase(phases, true, Constants.CHECKPOINT_SCREENING_PHASE_NAME) != null);
        request.setAttribute("isAllowedToPerformCheckpointReview",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_CHECKPOINT_REVIEW_PERM_NAME) &&
                        ActionsHelper.getPhase(phases, true, Constants.CHECKPOINT_REVIEW_PHASE_NAME) != null);
        request.setAttribute("isAllowedToViewScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENING_PERM_NAME));
        request.setAttribute("isAllowedToViewCheckpointScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_CHECKPOINT_SCREENING_PERM_NAME));
        request.setAttribute("isAllowedToViewCheckpointReview",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_CHECKPOINT_REVIEW_PERM_NAME));
        request.setAttribute("isAllowedToUploadTC",
                AuthorizationHelper.hasUserPermission(request, Constants.UPLOAD_TEST_CASES_PERM_NAME));
        request.setAttribute("isAllowedToPerformAggregation",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME));
        request.setAttribute("isAllowedToPerformAggregationReview",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME) &&
                        !AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME));
        request.setAttribute("isAllowedToUploadFF",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_FIX_PERM_NAME));
        request.setAttribute("isAllowedToPerformFinalReview",
                ActionsHelper.getPhase(phases, true, Constants.FINAL_REVIEW_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME));
        request.setAttribute("isAllowedToPerformApproval",
                ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPROVAL_PERM_NAME));
        request.setAttribute("isAllowedToPerformPortMortemReview",
                ActionsHelper.getPhase(phases, true, Constants.POST_MORTEM_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_POST_MORTEM_REVIEW_PERM_NAME));

        collectTrueSubmittersAndReviewers(project, resources, ActionsHelper.createReviewManager(), request);

        boolean[] canEditPrizes = canEditPrize(project.getId());

        request.setAttribute("canEditContestPrize", canEditPrizes[0]);
        request.setAttribute("canEditCheckpointPrize", canEditPrizes[1]);

        setResourcePaidRequestAttribute(project, request);
    }

    /**
     * Populate the resource paid data of the given project to request attribute.
     *
     * @param project the given project.
     * @param request the HTTP request.
     * @throws BaseException if any error occurs.
     * @since 1.15
     */
    private static void setResourcePaidRequestAttribute(Project project, HttpServletRequest request)
            throws BaseException {
        if (request.getAttribute("resourcePaid") != null) {
            return;
        }
        Map<Long, Boolean> resourcePaid = new HashMap<Long, Boolean>();
        if (project.getId() > 0) {
            List<ProjectPayment> allPayments = ActionsHelper.createProjectPaymentManager().search(
                    ProjectPaymentFilterBuilder.createProjectIdFilter(project.getId()));
            request.setAttribute("allPayments", allPayments);
            for (ProjectPayment payment : allPayments) {
                if (payment.getPactsPaymentId() != null) {
                    resourcePaid.put(payment.getResourceId(), Boolean.TRUE);
                }
            }
        }
        request.setAttribute("resourcePaid", resourcePaid);
    }

    /**
     * <p>Collects the lists of resources for specified project which are either submitters with submissions or
     * reviewers with reviews and binds them to request.</p>
     *
     * @param project a <code>Project</code> representing the project to edit.
     * @param resources a <code>Resource</code> array listing existing project resources.
     * @param reviewManager a <code>ReviewManager</code> to be used for accessing reviews.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @throws ReviewManagementException if an unexpected error occurs.
     */
    private void collectTrueSubmittersAndReviewers(Project project, Resource[] resources, ReviewManager reviewManager,
                                                   HttpServletRequest request) throws ReviewManagementException {
        Map<Integer, Boolean> trueSubmitters = new HashMap<Integer, Boolean>();
        Map<Integer, Boolean> trueReviewers = new HashMap<Integer, Boolean>();
        request.setAttribute("trueSubmitters", trueSubmitters);
        request.setAttribute("trueReviewers", trueReviewers);
        Set<Long> reviewersWithScorecards = findResourcesWithReviewsForProject(reviewManager, project.getId());
        for (int i = 0; i < resources.length; ++i) {
            collectSingleTrueSubmitterOrReviewer(resources[i], reviewersWithScorecards.contains(resources[i].getId()),
                                                 i, trueSubmitters, trueReviewers);
        }
    }

    /**
     * <p>Checks if specified resource is either submitter with submissions or reviewer with reviews and adds it to
     * respective map in either case.</p>
     *
     * @param resource a <code>Resource</code> to be verified.
     * @param hasReviewScorecardFilled <code>true</code> if resource has review filled; <code>false</code> otherwise.
     * @param index an <code>int</code> providing the index in the form for specified resource.
     * @param trueSubmitters a <code>Map</code> collecting the submitters.
     * @param trueReviewers a <code>Map</code> collecting the reviewers.
     */
    private void collectSingleTrueSubmitterOrReviewer(Resource resource, boolean hasReviewScorecardFilled, int index,
                                                      Map<Integer, Boolean> trueSubmitters,
                                                      Map<Integer, Boolean> trueReviewers) {
        String resourceRoleName = resource.getResourceRole().getName();
        if ("Submitter".equalsIgnoreCase(resourceRoleName)
            || "Specification Submitter".equalsIgnoreCase(resourceRoleName)) {
            Long[] submissionIds = resource.getSubmissions();
            if ((submissionIds != null) && (submissionIds.length > 0)) {
                trueSubmitters.put(index + 1, Boolean.TRUE);
            }
        } else if (REVIEWER_ROLE_NAMES.contains(resourceRoleName)) {
            if (hasReviewScorecardFilled) {
                trueReviewers.put(index + 1, Boolean.TRUE);
            }
        }
    }

    /**
     * <p>Sets the form data for <code>Edit Project</code> sceen with data for project phases.</p>
     *
     * @param form a <code>LazyValidatorForm</code> providing the current action form.
     * @param phases a <code>Phase</code> array listing existing project phases.
     * @param closedPhasesOnly <code>true</code> if only closed phases are to be processed; <code>false</code>
     *        otherwise.
     */
    private void setEditProjectPhasesData(LazyValidatorForm form, Phase[] phases, boolean closedPhasesOnly) {
        Map<Long, Integer> phaseNumberMap = new HashMap<Long, Integer>();

        // Populate form with phases data
        for (int i = 0; i < phases.length; ++i) {
            if (closedPhasesOnly && phases[i].getPhaseStatus().getId() != 3) {
                continue;
            }
            form.set("phase_id", i + 1, phases[i].getId());

            form.set("phase_can_open", i + 1,
                    Boolean.valueOf(phases[i].getPhaseStatus().getName().equals(PhaseStatus.SCHEDULED.getName())));
            form.set("phase_can_close", i + 1,
                    Boolean.valueOf(phases[i].getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName())));

            Long phaseTypeId = phases[i].getPhaseType().getId();
            form.set("phase_type", i + 1, phaseTypeId);
            Integer phaseNumber = phaseNumberMap.get(phaseTypeId);
            if (phaseNumber == null) {
                phaseNumber = 1;
            } else {
                phaseNumber = phaseNumber + 1;
            }
            phaseNumberMap.put(phaseTypeId, phaseNumber);
            form.set("phase_number", i + 1, phaseNumber);

            form.set("phase_name", i + 1, phases[i].getPhaseType().getName());
            form.set("phase_action", i + 1, "update");
            form.set("phase_js_id", i + 1, "loaded_" + phases[i].getId());
            if (phases[i].getAllDependencies().length > 0) {
                form.set("phase_start_by_phase", i + 1, Boolean.TRUE);
                // TODO: Probably will need to rewrite all those dependency stuff
                // TODO: It is very incomplete actually
                Dependency dependency = phases[i].getAllDependencies()[0];
                form.set("phase_start_phase", i + 1, "loaded_" + dependency.getDependency().getId());
                form.set("phase_start_when", i + 1, dependency.isDependencyStart() ? "starts" : "ends");
                // lagTime may be negative
                long lagTime = dependency.getLagTime();
                if (lagTime < 0) {
                    // negative lagTime
                    form.set("phase_start_plusminus", i + 1, "minus");
                    lagTime *= -1;
                }
                if (lagTime % (24 * 3600 * 1000L) == 0) {
                    form.set("phase_start_dayshrs", i + 1, "days");
                    form.set("phase_start_amount", i + 1, (int) (lagTime / (24 * 3600 * 1000L)));
                } else if (lagTime % (3600 * 1000L) == 0) {
                    form.set("phase_start_dayshrs", i + 1, "hrs");
                    form.set("phase_start_amount", i + 1, (int) (lagTime / (3600 * 1000L)));
                } else {
                    form.set("phase_start_dayshrs", i + 1, "mins");
                    form.set("phase_start_amount", i + 1, (int) (lagTime / (60 * 1000L)));
                }
            }
            if (phases[i].getFixedStartDate() != null) {
                form.set("phase_start_by_fixed_time", i + 1, Boolean.TRUE);
                populateDatetimeFormProperties(form, "phase_start_date", "phase_start_time", i + 1,
                        phases[i].getFixedStartDate());
            }

            populateDatetimeFormProperties(form, "phase_end_date", "phase_end_time", i + 1,
                    phases[i].calcEndDate());
            // always use duration
            form.set("phase_use_duration", i + 1, Boolean.TRUE);

            // populate the phase duration
            long phaseLength = phases[i].getLength();
            String phaseDuration;
            if (phaseLength % (3600*1000) == 0) {
                phaseDuration = "" + phaseLength / (3600 * 1000);
            } else {
                long hour = phaseLength / 3600 / 1000;
                long min = (phaseLength % (3600 * 1000)) / 1000 / 60;
                phaseDuration = hour + ":" + (min >= 10 ? "" + min : "0" + min);
            }

            form.set("phase_duration", i + 1, phaseDuration);

            // Populate phase criteria
            if (phases[i].getAttribute("Scorecard ID") != null) {
                form.set("phase_scorecard", i + 1, Long.valueOf((String) phases[i].getAttribute("Scorecard ID")));
            }
            if (phases[i].getAttribute("Registration Number") != null) {
                form.set("phase_required_registrations", i + 1,
                        Integer.valueOf((String) phases[i].getAttribute("Registration Number")));
            }
            if (phases[i].getAttribute("Reviewer Number") != null) {
                form.set("phase_required_reviewers", i + 1,
                        Integer.valueOf((String) phases[i].getAttribute("Reviewer Number")));
            }
            if (phases[i].getAttribute("View Response During Appeals") != null) {
                form.set("phase_view_appeal_responses", i + 1,
                        Boolean.valueOf("Yes".equals(phases[i].getAttribute("View Response During Appeals"))));
            }
        }
    }
    
    private String retrieveUserPreference(long userId, int preferenceId) throws BaseException {
        String value;
        
        try {
            value = userPreference.getValue(userId, preferenceId, DBMS.COMMON_OLTP_DATASOURCE_NAME);
            
            //System.out.println("----------------------------------");
            //System.out.println("Find preference " + preferenceId + " of user " + userId + " : " + value);
            //System.out.println("----------------------------------");
        } catch (RowNotFoundException e) {
            value = "false";
            //System.out.println("----------------------------------");
            //System.out.println("Can't find preference " + preferenceId + " of user " + userId + ", use 'false' instead.");
            //System.out.println("----------------------------------");
        } catch (RemoteException e) {
            throw new BaseException("Fail to retrieve user preference data", e);
        }
        
        return value;
    }
}
