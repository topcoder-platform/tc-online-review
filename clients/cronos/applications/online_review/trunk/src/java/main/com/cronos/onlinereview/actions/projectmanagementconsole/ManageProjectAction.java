/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectmanagementconsole;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.EJBLibraryServicesLocator;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.cronos.termsofuse.dao.ProjectTermsOfUseDao;
import com.cronos.termsofuse.dao.TermsOfUseDao;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.dao.UserTermsOfUseDao;
import com.cronos.termsofuse.model.TermsOfUse;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseStatusEnum;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidatorException;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to view the project management console.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ManageProjectAction extends BaseProjectManagementConsoleAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -5470348822370215570L;

    /**
     * Creates a new instance of the <code>ManageProjectAction</code> class.
     */
    public ManageProjectAction() {
    }

    /**
     * <p>Processes the incoming request which is a request for viewing the <code>Project Management Console</code> view
     * for requested project.</p>
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

        request.setAttribute("activeTabIdx", 1);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        verification
            = ActionsHelper.checkForCorrectProjectId(this, request, Constants.PROJECT_MANAGEMENT_PERM_NAME, false);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        } else {
            // Validate the forms
            final Project project = verification.getProject();
            final Phase[] phases = getProjectPhases(project);

            // Validate that Registration phase indeed exists and can be extended based on current state of the project
            // and that valid positive amount of days to extend is provided
            final Phase registrationPhase = getRegistrationPhaseForExtension(phases);
            int registrationExtensionDays = validatePhaseExtensionRequest(request, getModel(), registrationPhase,
                "registration_phase_extension", Constants.REGISTRATION_PHASE_NAME,
                ConfigHelper.getRegistrationPhaseMaxExtensionDays());

            // Validate that Submission phase indeed exists and can be extended based on current state of the project
            // and that valid positive amount of days to extend is provided
            final Phase submissionPhase = getSubmissionPhaseForExtension(phases);
            int submissionExtensionDays = validatePhaseExtensionRequest(request, getModel(), submissionPhase,
                "submission_phase_extension", Constants.SUBMISSION_PHASE_NAME,
                ConfigHelper.getRegistrationPhaseMaxExtensionDays());

            // Verify that new registration phase end time (if phase is going to be extended) will not exceed the
            // end time for submission phase (despite whether it is also going to be extended or not)
            if ((registrationPhase != null) && (registrationExtensionDays > 0)) {
                Phase submissionPhaseCurrent = ActionsHelper.findPhaseByTypeName(phases, Constants.SUBMISSION_PHASE_NAME);
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
                        "error.com.cronos.onlinereview.actions.manageProject."
                                          + Constants.REGISTRATION_PHASE_NAME + ".AfterSubmission");
                }
            }

            // Validate the Add Resources form
            Object[] caches = validateAddResourcesRequest(request, getModel());

            // Check if there were any validation errors identified and return appropriate forward
            if (ActionsHelper.isErrorsPresent(request)) {
                initProjectManagementConsole(request, project);
                return INPUT;
            } else {
                handleRegistrationPhaseExtension(registrationPhase, registrationExtensionDays, request);
                handleSubmissionPhaseExtension(submissionPhase, submissionExtensionDays, request);
                handleResourceAddition(project, request, (Map<Long, ResourceRole>) caches[0],
                                       (Map<String, ExternalUser>) caches[1]);
                if (ActionsHelper.isErrorsPresent(request)) {
                    initProjectManagementConsole(request, project);
                    return INPUT;
                } else {
                    setPid(project.getId());
                    return SUCCESS;
                }
            }
        }
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
     * @param model model providing parameters mapped to this request.
     * @param phase a <code>Phase</code> providing details for phase requested for extension.
     * @param extensionDaysRequestParamName a <code>String</code> providing the name of form parameter which provides
     *        the number of days to extend specified phase for as set by current user.
     * @param phaseTypeName a <code>String</code> providing the name of phase type.
     * @param limit an <code>Integer</code> providing the maximum allowed number of days to extend phase for.
     *        <code>null</code> value indicates that there is no such limit set.
     * @return an <code>int</code> providing the number of days to extend the specified phase for or 0 if there were
     *         validation errors encountered or if there were no request for extending the specified phase at all.
     */
    private int validatePhaseExtensionRequest(HttpServletRequest request, DynamicModel model, Phase phase,
                                                     String extensionDaysRequestParamName, String phaseTypeName,
                                                     Integer limit) {
        int extensionDays = 0;
        String extensionDaysString = (String) model.get(extensionDaysRequestParamName);
        if ((extensionDaysString != null) && (extensionDaysString.trim().length() > 0)) {
            // If phase extension was requested then verify that phase exists and can be extended and that
            // valid number of days for extension was specified
            if (phase == null) {
                ActionsHelper.addErrorToRequest(request, extensionDaysRequestParamName,
                    "error.com.cronos.onlinereview.actions.manageProject." + phaseTypeName
                                      + ".NotExtendable",
                                      ConfigHelper.getMinimumHoursBeforeSubmissionDeadlineForExtension());
            } else {
                try {
                    extensionDays = Integer.parseInt(extensionDaysString);
                    if ((extensionDays < 1) || ((limit != null) && (extensionDays > limit))) {
                        ActionsHelper.addErrorToRequest(request, extensionDaysRequestParamName,
                                "error.com.cronos.onlinereview.actions."
                                        + "manageProject." + phaseTypeName
                                        + ".LimitExceeded",
                                        1, limit == null ? "" : limit);
                    }
                } catch (NumberFormatException e) {
                    ActionsHelper.addErrorToRequest(request, extensionDaysRequestParamName,
                            "error.com.cronos.onlinereview.actions.manageProject." + phaseTypeName
                                    + ".NotNumeric");
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
     * @throws BaseException if an unexpected error occurs.
     */
    private void handleRegistrationPhaseExtension(Phase registrationPhase, int extensionDays,
                                                  HttpServletRequest request) throws BaseException {
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
            getModel().set("registration_phase_extension", "");
        }
    }

    /**
     * <p>Handles the specified request which may require the <code>Submission</code> phase to be extended by number
     * of days specified by current user.</p>
     *
     * @param submissionPhase a <code>Phase</code> representing <code>Submission</code> phase to be extended.
     * @param extensionDays an <code>int</code> providing the number of days to extend phase for.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @throws BaseException if an unexpected error occurs.
     */
    private void handleSubmissionPhaseExtension(Phase submissionPhase, int extensionDays,
                                                HttpServletRequest request) throws BaseException {
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
            getModel().set("submission_phase_extension", "");
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
     * @param model a model providing the form parameters mapped to specified request.
     * @return an <code>Object</code> array of two elements. The first element provides mapping from role IDs to
     *         <code>ResourceRole</code> objects. The second element provides the mapping from user handles to
     *         <code>ExternalUser</code> objects.
     * @throws ConfigException if a configuration error is encountered while initializing user project data store.
     * @throws BaseException if an unexpected error occurs
     */
    private Object[] validateAddResourcesRequest(HttpServletRequest request, DynamicModel model)
        throws BaseException {

        Long[] resourceRoleIds = (Long[]) model.get("resource_role_id");
        String[] resourceHandles = (String[]) model.get("resource_handles");

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
                    "error.com.cronos.onlinereview.actions.manageProject.ResourceRole.InvalidId",
                                      requestedRoleId);
            } else {
                final String permission = "Can Add Resource Role " + existingRole.getName();
                if (!AuthorizationHelper.hasUserPermission(request, permission)) {
                    ActionsHelper.addErrorToRequest(request, "resource_role_id[" + i + "]",
                        "error.com.cronos.onlinereview.actions.manageProject.ResourceRole.Denied",
                                          existingRole.getName());
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
                                            "error.com.cronos.onlinereview.actions.manageProject."
                                                              + "Resource.Unknown", handle);
                                    }
                                } catch (RetrievalException e) {
                                    e.printStackTrace();
                                    ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                                        "error.com.cronos.onlinereview.actions.manageProject."
                                                          + "Resource.Unknown", handle);
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
     * @param roleMapping a <code>Map</code> mapping resource role IDs to role details.
     * @param users a <code>Map</code> mapping user handlers to user account details.
     * @throws BaseException if an unexpected error occurs.
     * @throws RemoteException if an unexpected error occurs.
     */
    private void handleResourceAddition(Project project, HttpServletRequest request,
                                        Map<Long, ResourceRole> roleMapping, Map<String, ExternalUser> users)
        throws BaseException, RemoteException {

        Long[] resourceRoleIds = (Long[]) getModel().get("resource_role_id");
        String[] resourceHandles = (String[]) getModel().get("resource_handles");

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
                                        "error.com.cronos.onlinereview.actions."
                                                + "editProject.Resource.MissingTermsByUser",
                                                handle, terms.getTitle());
                            }
                        } else if (!validateResourceEligibility(project.getId(), userId)) {
                                ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                                        "error.com.cronos.onlinereview.actions."
                                                + "editProject.Resource.NotEligibleByUser", handle);
                        } else {
                            // The resource can be added - all necessary terms of use are accepted
                            Resource resource = new Resource();
                            resource.setProject(project.getId());
                            resource.setResourceRole(roleMapping.get(resourceRoleId));
                            resource.setUserId(userId);
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
            getModel().set("resource_handles", i, "");
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
                    "Error.manageProject.DuplicateHandlesPerRole", role.getName(), duplicates);
            }
            if (!existing.isEmpty()) {
                ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                    "Error.manageProject.ExistingHandlesPerRole", role.getName(), existing);
            }
            Set<String> badRoleHandles = badHandles.get(resourceRoleId);
            for (String badHandle : badRoleHandles) {
                addBadResourceHandle(i, badHandle);
            }
        }
    }

    /**
     * <p>Adds specified bad handle to list of bad handles for resource role at specified index in specified form.</p>
     *
     * @param index an <code>int</code> providing the index of list of bad handles in the form.
     * @param badHandle a <code>String</code> providing the bad handle.
     */
    private void addBadResourceHandle(int index, String badHandle) {
        String badHandles = (String) getModel().get("resource_handles", index);
        if (badHandles.length() > 0) {
            badHandles += ", ";
        }
        badHandles += badHandle;
        getModel().set("resource_handles", index, badHandles);
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
        Phase submissionPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.SUBMISSION_PHASE_NAME);
        if (submissionPhaseAllowsExtension(submissionPhase)) {
            return submissionPhase;
        }
        return null;
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
     * @throws TermsOfUsePersistenceException if any term of use persistence error
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
}
