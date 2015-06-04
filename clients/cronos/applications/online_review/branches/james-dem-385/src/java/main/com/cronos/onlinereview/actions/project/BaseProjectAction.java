/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.project;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.actions.DynamicModelDrivenAction;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.model.PhasesDetails;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.EJBLibraryServicesLocator;
import com.cronos.onlinereview.util.PhasesDetailsServices;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardSearchBundle;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.web.ejb.user.UserPreference;


/**
 * This is the base class for project actions classes.
 * It provides the basic functions which will be used by all project actions.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseProjectAction extends DynamicModelDrivenAction {
    /**
     * <p>A <code>Set</code> holding the names for all reviewer roles.</p>
     */
    protected static final Set<String> REVIEWER_ROLE_NAMES = new HashSet<String>(Arrays.asList(
                "Reviewer", "Accuracy Reviewer", "Failure Reviewer",
                "Stress Reviewer", "Screener", "Primary Screener",
                "Aggregator", "Final Reviewer", "Approver",
                "Post-Mortem Reviewer", "Specification Reviewer",
                "Checkpoint Screener", "Checkpoint Reviewer", "Iterative Reviewer"));

    /**
     * Represents the UserPreference instance.
    */
    private UserPreference userPreference;

    /**
     * This method will initialize the userPreference instance. It is called from the spring's init-method.
    */
    public void init() {
        userPreference = EJBLibraryServicesLocator.getUserPreference();
    }

    /**
     * Populates the default values for the project form.
     *
     * @param request the current http servlet request
     */
    protected void populateProjectFormDefaults(HttpServletRequest request) {
        // Set the JS id to start generation from
        getModel().set("js_current_id", (long) 0);

        // Populate form with some data so that resources row template
        // is rendered properly by the appropriate JSP
        getModel().set("resources_role", 0, (long) -1);
        getModel().set("resources_id", 0, (long) -1);
        getModel().set("resources_action", 0, "add");

        // Populate form with some data so that phases row template
        // is rendered properly by the appropriate JSP
        getModel().set("phase_id", 0, (long) -1);
        getModel().set("phase_action", 0, "add");
        getModel().set("phase_can_open", 0, Boolean.TRUE);
        getModel().set("phase_can_close", 0, Boolean.FALSE);
        getModel().set("phase_use_duration", 0, Boolean.TRUE);

        // Populate some phase criteria with default values read from the configuration
        if (ConfigHelper.getDefaultRequiredRegistrants() >= 0) {
            getModel().set("phase_required_registrations", 0, ConfigHelper.getDefaultRequiredRegistrants());
        }

        if (ConfigHelper.getDefaultRequiredReviewers() >= 0) {
            getModel().set("phase_required_reviewers", 0, ConfigHelper.getDefaultRequiredReviewers());
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
        getModel().set("addphase_duration", String.valueOf(ConfigHelper.getDefaultPhaseDuration()));
    }

    /**
     * This method loads the lookup data needed for rendering the Create Project/New Project pages.
     * The loaded data is stored in the request attributes.
     *
     * @param request the request to load the lookup data into
     * @throws BaseException if any error occurs while loading the lookup data
     */
    protected void loadProjectEditLookups(HttpServletRequest request) throws BaseException {
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
     * <p>Find the active scorecards by the type name.
     * Note, that the scorecard data(items) is not fully retrieved
     * </p>
     * @param scorecardManager the scorecard manager
     * @param scorecardTypeName the type name
     * @return the active scorecards
     * @throws BaseException if any error
     */
    private Scorecard[] searchActiveScorecards(ScorecardManager scorecardManager, String scorecardTypeName) throws BaseException {
        Filter filter = ScorecardSearchBundle.buildAndFilter(
                ScorecardSearchBundle.buildTypeNameEqualFilter(scorecardTypeName),
                ScorecardSearchBundle.buildStatusNameEqualFilter("Active"));
        return scorecardManager.searchScorecards(filter, false);
    }

    /**
     * Checks whether we can edit project prizes for a project.
     *
     * @param projectId the id of the project.
     * @return an array containing two boolean values, the first value indicates whether we
     *          can edit contest prizes, the second value indicates whether we can edit checkpoint prizes.
     * @throws BaseException if any error occurs.
     */
    protected static boolean[] canEditPrize(long projectId) throws BaseException {
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
     * Populate the date time fields from the properties.
     *
     * @param dateProperty the date property
     * @param timeProperty the time property
     * @param index the index
     * @param date the date
     */
    private void populateDatetimeFormProperties(String dateProperty,
        String timeProperty, int index, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy HH:mm", Locale.US);
        String[] parts = dateFormat.format(date).split("[ ]");
        getModel().set(dateProperty, index, parts[0]);
        getModel().set(timeProperty, index, parts[1]);
    }

    /**
     * <p>Builds the map to be used for looking up the project categories by IDs.</p>
     *
     * @param categories a <code>ProjectCategory</code> array listing existing project categories.
     * @return a <code>Map</code> mapping the category IDs to categories.
     */
    private static Map<Long, ProjectCategory> buildProjectCategoriesLookupMap(
        ProjectCategory[] categories) {
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
     */
    protected static Set<Long> findResourcesWithReviewsForProject(ReviewManager manager, Long projectId)
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
     * <p>Sets the basic form data for <code>Edit Project</code> screen.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param verification a <code>CorrectnessCheckResult</code> providing the verification result.
     * @throws BaseException if an unexpected error occurs.
     */
    protected void setEditProjectFormData(HttpServletRequest request,
        CorrectnessCheckResult verification) throws BaseException {
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

        populateProjectFormDefaults(request);
    }

    /**
     * <p>Sets the basic request attributes for <code>Edit Project</code> page.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param project a <code>Project</code> representing the project to edit.
     * @param resources a <code>Resource</code> array listing existing project resources.
     * @param externalUsers an <code>ExternalUser</code> array listing the users mapped to specified resources.
     * @param phases a <code>Phase</code> array listing existing project phases.
     * @throws BaseException if an unexpected error occurs.
     */
    protected void setEditProjectRequestAttributes(HttpServletRequest request,
        Project project, Resource[] resources, ExternalUser[] externalUsers,
        Phase[] phases) throws BaseException {
        PhasesDetails phasesDetails = PhasesDetailsServices.getPhasesDetails(request,
                this, project, phases, resources, externalUsers);

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
     */
    protected static void setResourcePaidRequestAttribute(Project project, HttpServletRequest request)
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
     * <p>Sets the form data for <code>Edit Project</code> page with data for project phases.</p>
     *
     * @param phases a <code>Phase</code> array listing existing project phases.
     * @param closedPhasesOnly <code>true</code> if only closed phases are to be processed; <code>false</code>
     *        otherwise.
     */
    protected void setEditProjectPhasesData(Phase[] phases, boolean closedPhasesOnly) {
        Map<Long, Integer> phaseNumberMap = new HashMap<Long, Integer>();

        // Populate form with phases data
        for (int i = 0; i < phases.length; ++i) {
            if (closedPhasesOnly && phases[i].getPhaseStatus().getId() != 3) {
                continue;
            }

            getModel().set("phase_id", i + 1, phases[i].getId());

            getModel().set("phase_can_open", i + 1,
                    phases[i].getPhaseStatus().getName().equals(PhaseStatus.SCHEDULED.getName()));
            getModel().set("phase_can_close", i + 1,
                    phases[i].getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName()));

            Long phaseTypeId = phases[i].getPhaseType().getId();
            getModel().set("phase_type", i + 1, phaseTypeId);

            Integer phaseNumber = phaseNumberMap.get(phaseTypeId);

            if (phaseNumber == null) {
                phaseNumber = 1;
            } else {
                phaseNumber = phaseNumber + 1;
            }

            phaseNumberMap.put(phaseTypeId, phaseNumber);
            getModel().set("phase_number", i + 1, phaseNumber);

            getModel().set("phase_name", i + 1, phases[i].getPhaseType().getName());
            getModel().set("phase_action", i + 1, "update");
            getModel().set("phase_js_id", i + 1, "loaded_" + phases[i].getId());

            if (phases[i].getAllDependencies().length > 0) {
                getModel().set("phase_start_by_phase", i + 1, Boolean.TRUE);

                Dependency dependency = phases[i].getAllDependencies()[0];
                getModel().set("phase_start_phase", i + 1, "loaded_" + dependency.getDependency().getId());
                getModel().set("phase_start_when", i + 1, dependency.isDependencyStart() ? "starts" : "ends");

                // lagTime may be negative
                long lagTime = dependency.getLagTime();

                if (lagTime < 0) {
                    // negative lagTime
                    getModel().set("phase_start_plusminus", i + 1, "minus");
                    lagTime *= -1;
                }

                if (lagTime % (24 * 3600 * 1000L) == 0) {
                    getModel().set("phase_start_dayshrs", i + 1, "days");
                    getModel().set("phase_start_amount", i + 1, (int) (lagTime / (24 * 3600 * 1000L)));
                } else if (lagTime % (3600 * 1000L) == 0) {
                    getModel().set("phase_start_dayshrs", i + 1, "hrs");
                    getModel().set("phase_start_amount", i + 1, (int) (lagTime / (3600 * 1000L)));
                } else {
                    getModel().set("phase_start_dayshrs", i + 1, "mins");
                    getModel().set("phase_start_amount", i + 1, (int) (lagTime / (60 * 1000L)));
                }
            }

            if (phases[i].getFixedStartDate() != null) {
                getModel().set("phase_start_by_fixed_time", i + 1, Boolean.TRUE);
                populateDatetimeFormProperties("phase_start_date",
                    "phase_start_time", i + 1, phases[i].getFixedStartDate());
            }

            populateDatetimeFormProperties("phase_end_date", "phase_end_time", i + 1, phases[i].calcEndDate());
            // always use duration
            getModel().set("phase_use_duration", i + 1, Boolean.TRUE);

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

            getModel().set("phase_duration", i + 1, phaseDuration);

            // Populate phase criteria
            if (phases[i].getAttribute("Scorecard ID") != null) {
                getModel().set("phase_scorecard", i + 1, Long.valueOf((String) phases[i].getAttribute("Scorecard ID")));
            }

            if (phases[i].getAttribute("Registration Number") != null) {
                getModel().set("phase_required_registrations", i + 1,
                        Integer.valueOf((String) phases[i].getAttribute("Registration Number")));
            }

            if (phases[i].getAttribute("Reviewer Number") != null) {
                getModel().set("phase_required_reviewers", i + 1,
                        Integer.valueOf((String) phases[i].getAttribute("Reviewer Number")));
            }

            if (phases[i].getAttribute("View Response During Appeals") != null) {
                getModel().set("phase_view_appeal_responses", i + 1,
                        Boolean.valueOf("Yes".equals(phases[i].getAttribute("View Response During Appeals"))));
            }
        }
    }

    /**
     * Getter of userPreference.
     * @return the userPreference
     */
    protected UserPreference getUserPreference() {
        return userPreference;
    }
}
