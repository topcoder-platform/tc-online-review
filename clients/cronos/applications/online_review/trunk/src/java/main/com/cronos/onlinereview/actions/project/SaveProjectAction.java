/*
 * Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.project;

import static com.cronos.onlinereview.Constants.AGGREGATION_PHASE_NAME;
import static com.cronos.onlinereview.Constants.AGGREGATION_REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.Constants.APPEALS_PHASE_NAME;
import static com.cronos.onlinereview.Constants.APPEALS_RESPONSE_PHASE_NAME;
import static com.cronos.onlinereview.Constants.APPROVAL_PHASE_NAME;
import static com.cronos.onlinereview.Constants.CHECKPOINT_REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.Constants.CHECKPOINT_SCREENING_PHASE_NAME;
import static com.cronos.onlinereview.Constants.CHECKPOINT_SUBMISSION_PHASE_NAME;
import static com.cronos.onlinereview.Constants.FINAL_FIX_PHASE_NAME;
import static com.cronos.onlinereview.Constants.FINAL_REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.Constants.POST_MORTEM_PHASE_NAME;
import static com.cronos.onlinereview.Constants.REGISTRATION_PHASE_NAME;
import static com.cronos.onlinereview.Constants.REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.Constants.SCREENING_PHASE_NAME;
import static com.cronos.onlinereview.Constants.SPECIFICATION_REVIEW_PHASE_NAME;
import static com.cronos.onlinereview.Constants.SPECIFICATION_SUBMISSION_PHASE_NAME;
import static com.cronos.onlinereview.Constants.SUBMISSION_PHASE_NAME;

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

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.phases.PaymentsHelper;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.Comparators;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.EJBLibraryServicesLocator;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.cronos.onlinereview.util.StatusValidationException;
import com.cronos.termsofuse.dao.ProjectTermsOfUseDao;
import com.cronos.termsofuse.dao.UserTermsOfUseDao;
import com.cronos.termsofuse.model.TermsOfUse;
import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.date.workdays.WorkdaysUnitOfTime;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.ProjectPaymentManager;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Prize;
import com.topcoder.management.project.PrizeType;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.project.phases.CyclicDependencyException;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidatorException;
import com.topcoder.shared.util.DBMS;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.web.common.RowNotFoundException;

/**
 * This class is the struts action class which is used for saving the project, including both creating
 *  and updating actions.
 * <p>
 * Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveProjectAction extends BaseProjectAction {

    /**
     * Represents the serial version UID.
     */
    private static final long serialVersionUID = 8036294134759299834L;

    /**
     * Represents the int value of global timeline notification.
     */
    private static final int GLOBAL_TIMELINE_NOTIFICATION = 29;

    /**
     * Represents the int value of global forum watch.
     */
    private static final int GLOBAL_FORUM_WATCH = 30;

    /**
     * Represents the date format which is used for project saving.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM.dd.yyyy hh:mm a", Locale.US);

    /**
     * <p>A <code>Set</code> holding the IDs for reviewer roles which do not allow duplicate users to be assigned to.
     * </p>
     */
    private static final Set<Long> NODUPLICATE_REVIEWER_ROLE_IDS = new HashSet<Long>(Arrays.asList(4L, 5L, 6L, 7L));

    /**
     * <p>A <code>Set</code> holding the IDs for reviewer roles which do not allow more than one user to be assigned to.
     * </p>
     */
    private static final Set<Long> SINGLE_REVIEWER_ROLE_IDS =
        new HashSet<Long>(Arrays.asList(2L, 8L, 9L, 18L, 19L, 20L));

    /**
     * <p>A long number holding the ID for iterative reviewer role.</p>
     */
    private static final long ITERATIVE_REVIEWER_ROLE_ID = 21L;

    /**
     * <p>A long number holding the ID for submitter role.</p>
     */
    private static final long SUBMITTER_ROLE_ID = 1L;

    /**
     * Represents the project id which is used for viewing project details.
     */
    private long pid;


    /**
     * This method is an implementation of &quot;Save Project&quot; Struts Action defined for this
     * assembly, which is supposed to save the project data submitted by the end user to database.
     *
     * @return &quot;success&quot; result that forwards to view project details page (as
     *         defined in struts.xml file) in the case of successful processing,
     *         &quot;notAuthorized&quot; result in the case of user not being authorized to perform
     *         the action.
     * @throws BaseException if any error occurs.
     */
    @SuppressWarnings("unchecked")
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Check whether user is creating new project or editing existing one
        final boolean newProject = (getModel().get("pid") == null);
        Project project = null;

        // Check if the user has the permission to perform this action
        verification = null;
        if (newProject) {
            // Gather the roles the user has for current request
            AuthorizationHelper.gatherUserRoles(request);

            if (!AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)) {
                // If he doesn't, redirect the request to login page or report about the lack of permissions
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.CREATE_PROJECT_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
            }

            // At this point, redirect-after-login attribute should be removed (if it exists)
            AuthorizationHelper.removeLoginRedirect(request);
        } else {
            // Verify that certain requirements are met before processing with the Action
            verification = ActionsHelper.checkForCorrectProjectId(
                    this, request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, true);
            // If any error has occurred, return action forward contained in the result bean
            if (!verification.isSuccessful()) {
                return verification.getResult();
            }

            project = verification.getProject();

            // Verify that the project hasn't been updated since last modification timestamp
            if ((Long) getModel().get("last_modification_time") <
                    ActionsHelper.getLastModificationTime(project,
                            ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false),
                                    project)).getTime()) {
                ActionsHelper.addErrorToRequest(request,
                        "error.com.cronos.onlinereview.actions.editProject.optConcurrency");
            }
        }

        // This variable determines whether status of the project has been changed by this save operation.
        boolean statusHasChanged = false;
        if (newProject) {
            // Find "Active" project status
            ProjectStatus activeStatus = LookupHelper.getProjectStatus("Active");
            // Find the project category by the specified id
            ProjectCategory category = LookupHelper.getProjectCategory((Long) getModel().get("project_category"));
            if (category.getProjectType().isGeneric()) {
                return ActionsHelper.produceErrorReport(this, request,
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
            long newCategoryId = (Long) getModel().get("project_category");
            // Sets Project category
            ProjectCategory projectCategory = LookupHelper.getProjectCategory(newCategoryId);
            if (projectCategory.getProjectType().isGeneric()) {
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.CREATE_PROJECT_PERM_NAME, "Error.GenericProjectType", Boolean.TRUE);
            }
            project.setProjectCategory(projectCategory);

        }

        // Populate project name
        project.setProperty("Project Name", getModel().get("project_name"));
        if (newProject) {
            // Populate project version (always set to 1.0)
            project.setProperty("Project Version", "1.0");
            // Populate project root catalog id
            // OrChange - If the project category is Studio set the property to allow multiple submissions.
            if (ActionsHelper.isStudioProject(project)) {
                //TODO retrieve it from the configuration
                project.setProperty("Root Catalog ID", "26887152");
                // As per Checkpoint Support assembly multiple submissions are not allowed for Studio projects for now
                project.setProperty("Allow multiple submissions", false);
            } else {
                project.setProperty("Root Catalog ID", ActionsHelper.getRootCategoryIdByComponentId(getModel().get("component_id")));
            }
            // Populate contest indicator flag
            project.setProperty("Contest Indicator", "On");
        } else {
            ProjectStatus newProjectStatus = LookupHelper.getProjectStatus((Long) getModel().get("status"));
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
        project.setProperty("Developer Forum ID", getModel().get("forum_id"));
        // Populate project component id
        Long componentId = (Long) getModel().get("component_id");
        project.setProperty("Component ID", componentId.equals(0l) ? null : componentId);
        // Populate project External Reference ID
        Long refId = (Long) getModel().get("external_reference_id");
        project.setProperty("External Reference ID", refId.equals(0l) ? null : refId);

        // Populate project dr points
        Double drPoints = (Double) getModel().get("dr_points");
        project.setProperty("DR points", drPoints.equals(0d) ? null : drPoints);

        if (newProject && getModel().get("external_reference_id") != null) {
            // Retrieve and populate version
            project.setProperty("Version ID",
                    ActionsHelper.getVersionUsingComponentVersionId(
                            (Long) getModel().get("external_reference_id")));
        }

        // Extract project's properties from the form
        Boolean autopilotOnObj = (Boolean) getModel().get("autopilot");
        Boolean sendEmailNotificationsObj = (Boolean) getModel().get("email_notifications");
        Boolean sendTLChangeNotificationsObj = (Boolean) getModel().get("timeline_notifications");
        Boolean digitalRunFlagObj = (Boolean) getModel().get("digital_run_flag");
        Boolean doNotRateProjectObj = (Boolean) getModel().get("no_rate_project");

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

        if (getModel().get("notes") != null && getModel().get("notes").toString().length() > 255) {
            ActionsHelper.addErrorToRequest(request, "notes", "error.com.cronos.onlinereview.actions.editProject.notes.MaxExceeded");
        }
        // Populate project notes
        project.setProperty("Notes", getModel().get("notes"));

        // since Online Review Update - Add Project Dropdown v1.0
        // Populate project notes
        if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)
                 || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
                project.setProperty("Billing Project", getModel().get("billing_project"));
                String cockpitProjectId = (String) getModel().get("cockpit_project");
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
        getProjectPrizesToBeUpdated(request, project, createdPrize, updatedPrize, removedPrize);

        Phase[] projectPhases;
        if (!ActionsHelper.isErrorsPresent(request)) {
            // Save the project phases
            projectPhases = saveProjectPhases(newProject, request, project, phasesJsMap, phasesToDelete);
        } else {
            // Retrieve and sort project phases
            projectPhases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
            Arrays.sort(projectPhases, new Comparators.ProjectPhaseComparer());
        }

        if (!ActionsHelper.isErrorsPresent(request)) {
            // The project has been saved, so pre-populate last modification timestamp
            getModel().set("last_modification_time",
                ActionsHelper.getLastModificationTime(project, projectPhases).getTime());
        }

        // resources must be saved even if there are validation errors to validate resources
        if (!ActionsHelper.isErrorsPresent(request)) {
            // Save the project resources
            saveResources(request, project, projectPhases, phasesJsMap);
        }

        if (!ActionsHelper.isErrorsPresent(request)) {
            // Delete the phases to be deleted
            deletePhases(request, phasesToDelete);
        }

        // If needed switch project current phase
        if (!newProject && !ActionsHelper.isErrorsPresent(request)) {
            switchProjectPhase(request, phasesJsMap);
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
            // Check if the form is really for new project
            if (verification == null) {
                verification = new CorrectnessCheckResult();
                verification.setProject(project);
            }
            setEditProjectFormData(request, verification);
            setEditProjectPhasesData(projectPhases, true);
            if (project.getId() > 0) {
                ResourceManager resourceManager = ActionsHelper.createResourceManager();
                Resource[] resources = resourceManager.searchResources(
                        ResourceFilterBuilder.createProjectIdFilter(project.getId()));
                ExternalUser[] externalUsers = ActionsHelper.getExternalUsersForResources(
                        ActionsHelper.createUserRetrieval(request), resources);
                    setEditProjectRequestAttributes(request, project, resources, externalUsers, projectPhases);
            }

            request.setAttribute("newProject", newProject);

            return INPUT;
        }

        this.pid = project.getId();
        // Return success forward
        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * Delete the given phases.
     *
     * @param request the http request
     * @param phasesToDelete the phases to be deleted
     * @throws BaseException if any error
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
     * @param project the project.
     * @param created the prizes which should be created.
     * @param updated the prizes which should be updated
     * @param removed the prizes which should be removed.
     * @throws BaseException if any error occurs.
     */
    private void getProjectPrizesToBeUpdated(HttpServletRequest request,
                                                    Project project, List<Prize> created, List<Prize> updated,
                                                    List<Prize> removed) throws BaseException {
        boolean[] canEditPrizes = canEditPrize(project.getId());
        String[] contestPrizeAmounts = (String[]) getModel().get("contest_prizes_amount");
        Integer[] contestPrizeNums = (Integer[]) getModel().get("contest_prizes_num");
        String[] checkpointPrizeAmounts = (String[]) getModel().get("checkpoint_prizes_amount");
        Integer[] checkpointPrizeNums = (Integer[]) getModel().get("checkpoint_prizes_num");

        validateProjectPrizes(request, contestPrizeAmounts, contestPrizeNums, "contest_prizes");
        validateProjectPrizes(request, checkpointPrizeAmounts, checkpointPrizeNums, "checkpoint_prizes");

        List<Prize> prizes = project.getPrizes();
        PrizeType contestPrizeType  = LookupHelper.getPrizeType(Constants.CONTEST_PRIZE_TYPE_NAME);
        PrizeType checkpointPrizeType = LookupHelper.getPrizeType(Constants.CHECKPOINT_PRIZE_TYPE_NAME);
        if (prizes != null) {
            if (!canEditPrizes[0] && contestPrizeAmounts.length == 0) {
                for (Prize prize : prizes) {
                    if (prize.getPrizeType().getId() == contestPrizeType.getId()) {
                        getModel().set("contest_prizes_amount",
                                prize.getPlace() - 1, String.valueOf(prize.getPrizeAmount()));
                        getModel().set("contest_prizes_num",
                                prize.getPlace() - 1, prize.getNumberOfSubmissions());
                    }
                }
            }
            if (!canEditPrizes[1] && checkpointPrizeAmounts.length == 0) {
                for (Prize prize : prizes) {
                    if (prize.getPrizeType().getId() == checkpointPrizeType.getId()) {
                        getModel().set("checkpoint_prizes_amount",
                                prize.getPlace() - 1, String.valueOf(prize.getPrizeAmount()));
                        getModel().set("checkpoint_prizes_num",
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
     * @param project a <code>Project</code> providing details for project associated with the phases.
     * @param phasesJsMap a <code>Map</code> mapping phase IDs to phases.
     * @param phasesToDelete a <code>List</code> listing the existing phases for specified project which are to be
     *        deleted.
     * @return a <code>Phase</code> array listing the updated phases associated with the specified project.
     * @throws BaseException if an unexpected error occurs.
     */
    private Phase[] saveProjectPhases(boolean newProject, HttpServletRequest request,
            Project project, Map<Object, Phase> phasesJsMap, List<Phase> phasesToDelete)
        throws BaseException {
        // Obtain an instance of Phase Manager
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);

        com.topcoder.project.phases.Project phProject;
        if (newProject) {
            // Create new Phases Project
            phProject = new com.topcoder.project.phases.Project(
                    new Date(), (new DefaultWorkdaysFactory()).createWorkdaysInstance());
        } else {
            // Retrieve the Phases Project with the id equal to the id of specified Project
            phProject = phaseManager.getPhases(project.getId());
            // Sometimes the call to the above method returns null. Guard against this situation
            if (phProject == null) {
                phProject = new com.topcoder.project.phases.Project(
                        new Date(), (new DefaultWorkdaysFactory()).createWorkdaysInstance());
            }
        }

        // Get the list of all previously existing phases
        Phase[] oldPhases = phProject.getAllPhases();

        // Get the array of phase types specified for each phase
        Long[] phaseTypes = (Long[]) getModel().get("phase_type");

        // This will be a Map from phases to their indexes in form
        Map<Phase, Integer> phasesToForm = new HashMap<Phase, Integer>();

        // FIRST PASS
        // 0-index phase is skipped since it is a "dummy" one
        for (int i = 1; i < phaseTypes.length; i++) {
            if (phaseTypes[i] == null) {
                String phaseJsId = (String) getModel().get("phase_js_id", i);
                long phaseId = Long.parseLong(phaseJsId.substring("loaded_".length()));
                Phase phase = ActionsHelper.findPhaseById(oldPhases, phaseId);
                phasesJsMap.put(phaseJsId, phase);
                phasesToForm.put(phase, i);
                continue;
            }
            Phase phase;

            // Check what is the action to be performed with the phase
            // and obtain Phase instance in appropriate way
            String phaseAction = (String) getModel().get("phase_action", i);
            if ("add".equals(phaseAction)) {
                // Create new phase
                // Phase duration is set to zero here, as it will be updated later anyway
                phase = new Phase(phProject, 0);
                // Add it to Phases Project
                phProject.addPhase(phase);
            }  else {
                long phaseId = (Long) getModel().get("phase_id", i);
                if (phaseId != -1) {
                    // Retrieve the phase with the specified id
                    phase = ActionsHelper.findPhaseById(oldPhases, phaseId);

                    // Clear all the pre-existing dependencies
                    phase.clearDependencies();

                    // Clear the previously set fixed start date
                    phase.setFixedStartDate(null);
                } else {
                    // -1 value as id marks the phases that weren't persisted in DB yet
                    // and so should be skipped for actions other than "add"
                    continue;
                }
            }

            // If action is "delete", proceed to the next phase
            if ("delete".equals(phaseAction)) {
                continue;
            }

            // flag value indicates using end date or using duration
            boolean useDuration = (Boolean) getModel().get("phase_use_duration", i);

            // If phase duration is specified
            if (useDuration) {
                String duration = (String) getModel().get("phase_duration", i);
                String[] parts = duration.split(":");

                // the format should be hh or hh:mm
                if (parts.length < 1 || parts.length > 2) {
                    ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                            "error.com.cronos.onlinereview.actions.editProject.InvalidDurationFormat",
                                    phase.getPhaseType().getName());
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
                    ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                            "error.com.cronos.onlinereview.actions.editProject.InvalidDurationFormat",
                                    phase.getPhaseType().getName());
                    break;
                }
            } else {
                // Length is undetermined at current pass
                phase.setLength(0);
            }

            // Put the phase to the map from phase JS ids to phases
            phasesJsMap.put(getModel().get("phase_js_id", i), phase);
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
            Object phaseObj = phasesJsMap.get(getModel().get("phase_js_id", i));
            // If phase is not found in map, it is to be deleted
            if (phaseObj == null) {
                long phaseId = (Long) getModel().get("phase_id", i);

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

            String phaseAction = (String) getModel().get("phase_action", i);

            if ("add".equals(phaseAction)) {
                // Set phase type
                phase.setPhaseType(LookupHelper.getPhaseType(phaseTypes[i]));
                // Set phase status to "Scheduled"
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            }

            // If phase is not started by other phase end
            Boolean startsByFixedTime = (Boolean) getModel().get("phase_start_by_fixed_time", i);
            boolean startTimeSet = false;
            if (startsByFixedTime != null && startsByFixedTime) {
                String datePart = (String) getModel().get("phase_start_date", i);
                String timePart = (String) getModel().get("phase_start_time", i);
                startTimeSet = !(isEmpty(datePart) && isEmpty(timePart));
                if (startTimeSet) {
                    if (isEmpty(datePart)) {
                        ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                                "error.com.cronos.onlinereview.actions.editProject.PhaseStartDateNotSet",
                                        phase.getPhaseType().getName());
                    } else if (isEmpty(timePart)) {
                        ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                                "error.com.cronos.onlinereview.actions.editProject.PhaseStartTimeNotSet",
                                        phase.getPhaseType().getName());
                    } else {
                        // Get phase start date from form
                        // Set phase fixed start date
                        Date phaseStartDate = parseDatetimeFormProperties(i, "phase_start_date", "phase_start_time");
                        phase.setFixedStartDate(phaseStartDate);
                        // Check if the current date is minimal
                        if (minDate == null || phaseStartDate.getTime() < minDate.getTime()) {
                            minDate = phaseStartDate;
                        }
                    }
                }
            }

            // Get the dependency phase
            Boolean startsByAnotherPhase = (Boolean) getModel().get("phase_start_by_phase", i);
            boolean mainPhaseSet = false;
            if (startsByAnotherPhase != null && startsByAnotherPhase) {
                String phaseJsId = (String) getModel().get("phase_start_phase", i);
                if (phaseJsId != null && phaseJsId.trim().length() > 0) {
                    Phase dependencyPhase = phasesJsMap.get(phaseJsId);
                    if (dependencyPhase != null) {
                        mainPhaseSet = true;
                        boolean dependencyStart;
                        boolean dependantStart;
                        if ("ends".equals(getModel().get("phase_start_when", i))) {
                            dependencyStart = false;
                            dependantStart = true;
                        } else {
                            dependencyStart = true;
                            dependantStart = true;
                        }
                        Object phaseLag = getModel().get("phase_start_dayshrs", i);
                        long unitMultiplier
                            = 1000 * 60 * ("days".equals(phaseLag) ? 24 * 60 : ("hrs".equals(phaseLag) ? 60 : 1));
                        long lagTime = unitMultiplier * ((Integer) getModel().get("phase_start_amount", i)).longValue();

                        // negative lag time.
                        if ("minus".equals(getModel().get("phase_start_plusminus", i))) {
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
                ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                        "error.com.cronos.onlinereview.actions.editProject.PhaseStartBad",
                                phase.getPhaseType().getName());
            }
            /*
             *  Set phase criteria
             */
            Long scorecardId = (Long) getModel().get("phase_scorecard", i);
            // If the scorecard id is specified, set it
            if (scorecardId != null) {
                phase.setAttribute("Scorecard ID", scorecardId.toString());
            }
            Integer requiredRegistrations = (Integer) getModel().get("phase_required_registrations", i);
            // If the number of required registrations is specified, set it
            if (requiredRegistrations != null) {
                phase.setAttribute("Registration Number", requiredRegistrations.toString());
            }
            // If the number of required reviewers is specified, set it
            Integer requiredReviewer = (Integer) getModel().get("phase_required_reviewers", i);
            if (requiredReviewer != null) {

                if (requiredReviewer < 1) {
                    ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                            "error.com.cronos.onlinereview.actions.editProject.InvalidReviewersNumber",
                                    phase.getPhaseType().getName());
                    break;
                }

                phase.setAttribute("Reviewer Number", requiredReviewer.toString());
            }

            Boolean viewAppealResponses = (Boolean) getModel().get("phase_view_appeal_responses", i);
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
            Object phaseObj = phasesJsMap.get(getModel().get("phase_js_id", i));
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
                    boolean useDuration = (Boolean) getModel().get("phase_use_duration", paramIndex);

                    // If phase duration was not specified
                    if (!useDuration) {
                        // Get phase end date from form
                        Date phaseEndDate = parseDatetimeFormProperties(paramIndex,
                                "phase_end_date", "phase_end_time");

                        // Calculate phase length
                        long length = phaseEndDate.getTime() - phase.getScheduledStartDate().getTime();
                        // Check if the end date of phase goes after the start date
                        if (length < 0) {
                            ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                                    "error.com.cronos.onlinereview.actions.editProject.StartAfterEnd",
                                    phase.getPhaseType().getName());
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
                projectManager.updateProject(project, (String) getModel().get("explanation"),
                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
            } catch (StatusValidationException statusValidationException) {
                ActionsHelper.addErrorToRequest(request, "status", statusValidationException.getStatusViolationKey());
                return oldPhases;
            }
        }

        // Save the phases at the persistence level
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        phaseManager.updatePhases(phProject, operator);
        projectPhases = phProject.getAllPhases();
        // Sort project phases
        Arrays.sort(projectPhases, new Comparators.ProjectPhaseComparer());

        return projectPhases;
    }

    /**
     *
     * Switch to the project phase.
     *
     * @param request the http servlet request
     * @param phasesJsMap the phases js map
     * @throws BaseException if any error.
     */
    private void switchProjectPhase(HttpServletRequest request,
                                    Map<Object, Phase> phasesJsMap) throws BaseException {

        // Get name of action to be performed
        String action = (String) getModel().get("action");

        // Get new current phase id
        String phaseJsId = (String) getModel().get("action_phase");

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
                    ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                            "error.com.cronos.onlinereview.actions.editProject.CannotClosePhase",
                            phaseType.getName(), result.getMessage());
                }
            } else if ("open_phase".equals(action)) {
                result = phaseManager.canStart(phase);
                if (phaseStatus.getName().equals(PhaseStatus.SCHEDULED.getName()) && result.isSuccess()) {
                    // Open the phase
                    phaseManager.start(phase, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                } else {
                    ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                            "error.com.cronos.onlinereview.actions.editProject.CannotOpenPhase",
                            phaseType.getName(), result.getMessage());
                }
            }
        }
    }

    /**
     * Validate the project phases.
     * Note, that this method assumes that phases are already sorted by the start date, etc.
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param project the project of the phases
     * @param projectPhases the phases to be validated
     * @return true if all the phases are valid, false otherwise
     */
    private boolean validateProjectPhases(HttpServletRequest request, Project project, Phase[] projectPhases) {
        boolean arePhasesValid = true;

        boolean isStudioProject = project.getProjectCategory().getProjectType().getId() == Constants.STUDIO_PROJECT_ID;

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
                        && !nextPhaseName.equals(CHECKPOINT_REVIEW_PHASE_NAME)) {
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
     * Get the date from the given properties.
     *
     * @param propertyIndex the property index
     * @param dateProperty the date property
     * @param timeProperty the time property
     * @return the date parsed
     */
    private Date parseDatetimeFormProperties(int propertyIndex, String dateProperty,
            String timeProperty) {
        // Retrieve the values of form properties
        String dateString = (String) getModel().get(dateProperty, propertyIndex);
        String timeString = (String) getModel().get(timeProperty, propertyIndex);

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
     * Private helper method to save resources.
     *
     * @param request the HttpServletRequest
     * @param project the project being saved
     * @param projectPhases the project phases being saved
     * @param phasesJsMap the phasesJsMap
     * @throws BaseException if any error occurs
     */
    private void saveResources(HttpServletRequest request,
                               Project project, Phase[] projectPhases, Map<Object, Phase> phasesJsMap) throws BaseException {

        // Obtain the instance of the User Retrieval
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);

        long timelineNotificationId = LookupHelper.getNotificationType("Timeline Notification").getId();

        // Get the array of resource names
        String[] resourceNames = (String[]) getModel().get("resources_name");

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
                allResourcesValid = false;
                continue;
            }

            // Get info about user with the specified handle
            ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);

            // If there is no user with such handle, indicate an error
            if (user == null) {
                ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                        "error.com.cronos.onlinereview.actions.editProject.Resource.NotFound");
                allResourcesValid = false;
            }
        }

        // validate resources have correct terms of use
        try {
            allResourcesValid = allResourcesValid && validateResourceTermsOfUse(request, project, userRetrieval, resourceNames);
            allResourcesValid = allResourcesValid && validateResourceEligibility(request, project, userRetrieval, resourceNames);
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
        Set<String> submitterHandles = new HashSet<String>();

        // Obtain the instance of the Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager();

        for (int i = 1; i < resourceNames.length; i++) {
            String resourceAction = (String) getModel().get("resources_action", i);
            if (!"delete".equalsIgnoreCase(resourceAction)) {
                String handle = resourceNames[i];
                long resourceRoleId = (Long) getModel().get("resources_role", i);
                String resourcePhaseId = String.valueOf(getModel().get("resources_phase", i));
                String resourceKey = String.valueOf(resourceRoleId) + resourcePhaseId;

                if (disabledResourceRoles.contains(String.valueOf(resourceRoleId))) {
                    boolean attemptingToAssignDisabledRole = false;
                    if ("add".equalsIgnoreCase(resourceAction)) {
                        attemptingToAssignDisabledRole = true;
                    } else if ("update".equalsIgnoreCase(resourceAction)) {
                        Long resourceId = (Long) getModel().get("resources_id", i);
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
                } else if (SUBMITTER_ROLE_ID == resourceRoleId) {
                    if (submitterHandles.contains(handle)) {
                        ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                "error.com.cronos.onlinereview.actions."
                                        + "editProject.Resource.DuplicateSubmitterRole");
                        allResourcesValid = false;
                    } else {
                        submitterHandles.add(handle);
                    }
                }
                // Check if the phase related to resource role exists
                ResourceRole role = LookupHelper.getResourceRole(resourceRoleId);
                if (role != null) {
                    Long relatedPhaseTypeId = role.getPhaseType();
                    if (relatedPhaseTypeId != null) {

                        boolean found = false;
                        for (Phase phase : projectPhases) {
                            if (phase.getPhaseType().getId() == (relatedPhaseTypeId)) {
                                found = true;
                            }
                        }

                        if (!found) {
                            PhaseType phaseType = LookupHelper.getPhaseType(relatedPhaseTypeId);
                            ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                                             "error.com.cronos.onlinereview.actions."
                                                              + "editProject.Resource.PhaseNotFound", phaseType.getName());
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
            String resourceAction = (String) getModel().get("resources_action", i);
            if (!"add".equals(resourceAction)) {
                Long resourceId = (Long) getModel().get("resources_id", i);
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
                                        "error.com.cronos.onlinereview.actions." + "editProject.Resource.PaidDeleted");
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
                            long newResourceRoleId = (Long) getModel().get("resources_role", i);
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
        if (!allResourcesValid) {
            return;
        }

        // 0-index resource is skipped as it is a "dummy" one
        for (int i = 1; i < resourceNames.length; i++) {

            // Get info about user with the specified handle
            ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);

            Resource resource;

            // Check what is the action to be performed with the resource
            // and obtain Resource instance in appropriate way
            String resourceAction = (String) getModel().get("resources_action", i);
            if ("add".equals(resourceAction)) {
                // Create new resource
                resource = new Resource();

                resource.setProperty("Registration Date", DATE_FORMAT.format(new Date()));

                newUsers.add(user.getId());

                ResourceRole role = LookupHelper.getResourceRole((Long) getModel().get("resources_role", i));
                if (!role.getName().equals("Observer") || Boolean.parseBoolean(retrieveUserPreference(user.getId(), GLOBAL_TIMELINE_NOTIFICATION))) {
                    newUsersForNotification.add(user.getId());
                }

                //System.out.println("ADD:" + user.getId());
            }  else {
                Long resourceId = (Long) getModel().get("resources_id", i);

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
                        (Long) getModel().get("resources_role", i));
                resourceManager.removeResource(resource,
                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                resourceManager.removeNotifications(new long[] {user.getId()}, project.getId(),
                        timelineNotificationId, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                continue;
            }

            // Set resource properties
            resource.setProject(project.getId());

            boolean resourceRoleChanged = false;
            ResourceRole role = LookupHelper.getResourceRole((Long) getModel().get("resources_role", i));
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
            resource.setUserId(user.getId());

            resource.setProperty("Handle", resourceNames[i]);

            // Set resource phase id, if needed
            Long phaseTypeId = resource.getResourceRole().getPhaseType();
            if (phaseTypeId != null) {
                Phase phase = phasesJsMap.get(getModel().get("resources_phase", i));
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

            finalUsers.remove((long) 22770213); // Applications user
            finalUsers.remove((long) 22719217); // Components user
            finalUsers.remove((long) 22873364); // LCSUPPORT user

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
     * Helper method to validate if resources in the request have the required terms of use.
     *
     * @param request the current <code>HttpServletRequest</code>.
     * @param project the edited <code>Project</code>.
     * @param userRetrieval a <code>UserRetrieval</code> instance to obtain the user id.
     * @param resourceNames a <code>String[]</code> containing edited resource names.
     *
     * @throws RemoteException if any errors occur during EJB remote invocation
     * @throws EJBException if any other errors occur while invoking EJB services
     * @throws BaseException if any other errors occur while retrieving user
     *
     * @return true if all resources are valid
     */
    private boolean validateResourceTermsOfUse(HttpServletRequest request,
            Project project, UserRetrieval userRetrieval, String[] resourceNames)
            throws EJBException, BaseException {

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
                String resourceAction = (String) getModel().get("resources_action", i);
                // check for additions or modifications
                if (!"delete".equals(resourceAction)) {
                    long roleId = (Long) getModel().get("resources_role", i);
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
                                    "error.com.cronos.onlinereview.actions.editProject.Resource.MissingGroupTerms",
                                    b.toString());
                            allResourcesValid = false;

                        }
                    }
                }
            }
        }

        return allResourcesValid;
    }

    /**
     * Helper method to validate if resources in the request are eligible for the project.
     *
     * @param request the current <code>HttpServletRequest</code>.
     * @param project the edited <code>Project</code>.
     * @param userRetrieval a <code>UserRetrieval</code> instance to obtain the user id.
     * @param resourceNames a <code>String[]</code> containing edited resource names.
     *
     * @throws EJBException if any other errors occur while invoking EJB services
     * @throws BaseException if any other errors occur while retrieving user
     * @throws ContestEligibilityValidatorException if any validator error
     *
     * @return true if all resources are valid
     */
    private boolean validateResourceEligibility(HttpServletRequest request,
            Project project, UserRetrieval userRetrieval, String[] resourceNames)
            throws EJBException, BaseException, ContestEligibilityValidatorException {

        boolean allResourcesValid = true;


        // validate that new resources have agreed to the necessary terms of use
        // 0-index resource is skipped as it is a "dummy" one
        for (int i = 1; i < resourceNames.length; i++) {
            if (resourceNames[i] != null && resourceNames[i].trim().length() > 0) {
                ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);
                String resourceAction = (String) getModel().get("resources_action", i);
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

                    // don't check project creator
                    if (project.getCreationUser().equals(Long.toString(userId)))
                    {
                        continue;
                    }

                    if (!EJBLibraryServicesLocator.getContestEligibilityService().isEligible(userId, project.getId(),
                                                                                             false))
                    {
                        ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                                        "error.com.cronos.onlinereview.actions.editProject.Resource.NotEligible");

                        allResourcesValid = false;
                    }
                }
            }
        }

        return allResourcesValid;
    }

    /**
     * <p>Checks if specified string is empty.</p>
     *
     * @param value <code>true</code> if specified value is <code>null</code> or empty; <code>false</code> otherwise.
     * @return true if the string is empty or null
     */
    private static boolean isEmpty(Object value) {
        return (value == null) || (((String) value).trim().length() == 0);
    }

    /**
     * Retrieve the user preference.
     * @param userId the user id.
     * @param preferenceId the preference id
     * @return the use preference value
     * @throws BaseException if any error
     */
    private String retrieveUserPreference(long userId, int preferenceId) throws BaseException {
        String value;

        try {
            value = getUserPreference().getValue(userId, preferenceId, DBMS.COMMON_OLTP_DATASOURCE_NAME);

        } catch (RowNotFoundException e) {
            value = "false";
        } catch (RemoteException e) {
            throw new BaseException("Fail to retrieve user preference data", e);
        }
        return value;
    }

    /**
     * Getter of pid.
     * @return the pid
     */
    public long getPid() {
        return pid;
    }

    /**
     * Setter of project id.
     * @param pid the project id to set
     */
    public void setPid(long pid) {
        this.pid = pid;
    }
}

