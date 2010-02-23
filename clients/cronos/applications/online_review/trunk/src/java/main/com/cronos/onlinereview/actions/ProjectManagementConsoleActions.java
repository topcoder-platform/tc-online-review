/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
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
import com.topcoder.shared.util.DBMS;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.web.ejb.project.ProjectRoleTermsOfUse;
import com.topcoder.web.ejb.project.ProjectRoleTermsOfUseLocator;
import com.topcoder.web.ejb.termsofuse.TermsOfUse;
import com.topcoder.web.ejb.termsofuse.TermsOfUseEntity;
import com.topcoder.web.ejb.termsofuse.TermsOfUseLocator;
import com.topcoder.web.ejb.user.UserTermsOfUse;
import com.topcoder.web.ejb.user.UserTermsOfUseLocator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cronos.onlinereview.actions.Constants.VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME;
import static com.cronos.onlinereview.actions.Constants.PROJECT_MANAGEMENT_PERM_NAME;
import static com.cronos.onlinereview.actions.Constants.REGISTRATION_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.SUBMISSION_PHASE_NAME;
import static com.cronos.onlinereview.actions.Constants.SUCCESS_FORWARD_NAME;

/**
 * <p>A <code>Struts</code> action to be used for handling requests related to <code>Project Management Console</code>
 * in <code>Online Review</code> application.</p>
 *
 * <p>As of current version such requests may require adding new resources to designated projects, extend
 * <code>Registration</code> or <code>Submission</code> phases for projects.</p>
 *
 * @author isv
 * @version 1.0 (Online Review Project Management Console assembly v1.0)
 */
public class ProjectManagementConsoleActions extends DispatchAction {

    /**
     * <p>A <code>long</code> providing the constant value for single hour duration in milliseconds.</p>
     */
    private static final long HOUR_DURATION_IN_MILLIS = 60 * 60 * 1000L;

    /**
     * <p>A <code>long</code> providing the constant value for single day duration in milliseconds.</p>
     */
    private static final long DAY_DURATION_IN_MILLIS = 24 * 60 * 60 * 1000L;

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

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. Also check that current user is granted a
        // permission to access the details for requested project
        CorrectnessCheckResult verification
            = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
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

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        CorrectnessCheckResult verification
            = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                                                     PROJECT_MANAGEMENT_PERM_NAME, false);
        if (!verification.isSuccessful()) {
            return verification.getForward();
        } else {
            // Validate the forms
            final Project project = verification.getProject();
            final Phase[] phases = getProjectPhases(request, project);

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
        Phase[] phases = getProjectPhases(request, project);
        setAvailableResourceRoles(request);
        setRegistrationPhaseExtensionParameters(request, phases);
        setSubmissionPhaseExtensionParameters(request, phases);
    }

    /**
     * <p>Gets the phases for the specified project.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param project a <code>Project</code> providing the details for current project being managed by current user.
     * @return a <code>Phase</code> array listing the phases for specified project.
     * @throws BaseException if an unexpected error occurs.
     */
    private Phase[] getProjectPhases(HttpServletRequest request, Project project) throws BaseException {
        // Get details for requested project
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, false);
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
            PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, false);
            if (isClosed(registrationPhase)) {
                // Re-open closed Registration phase if necessary
                PhaseStatus[] statuses = phaseManager.getAllPhaseStatuses();
                PhaseStatus openPhaseStatus = ActionsHelper.findPhaseStatusById(statuses, PhaseStatusEnum.OPEN.getId());
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
            PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, false);
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
     * @throws ResourcePersistenceException if an unexpected error occurs while accessing user project data store.
     * @throws ConfigException if a configuration error is encountered while initializing user project data store.
     */
    private Object[] validateAddResourcesRequest(HttpServletRequest request, ActionForm form)
            throws ResourcePersistenceException, ConfigException {

        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        Long[] resourceRoleIds = (Long[]) lazyForm.get("resource_role_id");
        String[] resourceHandles = (String[]) lazyForm.get("resource_handles");

        // Validate that current user is indeed granted permission to add resources of requested roles and that
        // requested roles exist and collect mapping from role IDs to ResourceRole objects to be used further if
        // validation succeeds
        Map<Long, ResourceRole> roleMapping = new HashMap<Long, ResourceRole>();
        Map<String, ExternalUser> users = new HashMap<String, ExternalUser>();
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        ResourceRole[] existingResourceRoles = resourceManager.getAllResourceRoles();
        for (int i = 0; i < resourceRoleIds.length; i++) {
            Long requestedRoleId = resourceRoleIds[i];
            ResourceRole existingRole = ActionsHelper.findResourceRoleById(existingResourceRoles, requestedRoleId);
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
     * @throws NamingException if an unexpected error occurs.
     * @throws RemoteException if an unexpected error occurs.
     * @throws CreateException if an unexpected error occurs.
     */
    private void handleResourceAddition(Project project, HttpServletRequest request, ActionForm form,
                                        Map<Long, ResourceRole> roleMapping, Map<String, ExternalUser> users)
            throws BaseException, NamingException, CreateException, RemoteException {
        
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        Long[] resourceRoleIds = (Long[]) lazyForm.get("resource_role_id");
        String[] resourceHandles = (String[]) lazyForm.get("resource_handles");

        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);

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

        Map<Long, Set<String>> processedHandles = new HashMap<Long, Set<String>>();
        Map<Long, Set<String>> existingResourceHandles = new HashMap<Long, Set<String>>();
        Map<Long, Set<String>> duplicateHandles = new HashMap<Long, Set<String>>();
        Map<Long, Set<String>> usersWithPendingTerms = new HashMap<Long, Set<String>>();
        Map<Long, Set<String>> badHandles = new HashMap<Long, Set<String>>();

        for (int i = 0; i < resourceRoleIds.length; i++) {
            Long resourceRoleId = resourceRoleIds[i];
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
                        List<TermsOfUseEntity> pendingTerms
                            = validateResourceTermsOfUse(project.getId(), userId, resourceRoleId);
                        if (!pendingTerms.isEmpty()) {
                            for (TermsOfUseEntity terms : pendingTerms) {
                                usersWithPendingTerms.get(resourceRoleId).add(handle);
                                ActionsHelper.addErrorToRequest(request, "resource_handles[" + i + "]",
                                        new ActionMessage("error.com.cronos.onlinereview.actions."
                                                + "editProject.Resource.MissingTermsByUser",
                                                handle, terms.getTitle()));
                            }
                        } else {
                            // The resource can be added - all necessary terms of use are accepted
                            Resource resource = new Resource();
                            resource.setProject(project.getId());
                            resource.setProperty("Payment", null);
                            resource.setProperty("Payment Status", "N/A");
                            resource.setResourceRole(roleMapping.get(resourceRoleId));
                            resource.setProperty("Handle", handle);
                            resource.setProperty("External Reference ID", userId);

                            newUsersForumWatch.add(userId);

                            resourceManager.updateResource(resource,
                                    Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                        }
                    }
                }
            }
        }

        // Add all assigned resources as watchers for forum associated with project
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

        ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();
        for (int i = 0; i < resourceRoleIds.length; i++) {
            Long resourceRoleId = resourceRoleIds[i];
            ResourceRole role = ActionsHelper.findResourceRoleById(resourceRoles, resourceRoleId);
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
     * <p>Gets the list of roles available for resources which current user can add to project.</p>
     *
     * @param allowedRoleNames a <code>String</code> array listing the names for roles for resources which can be added
     *        by current user to project.
     * @param existingResourceRoles a <code>ResourceRole</code> array listing all existing resource roles.
     * @return a <code>ResourceRole</code> array listing the roles available for resources which current user can add to
     *         project.
     */
    private ResourceRole[] getAvailableRoles(String[] allowedRoleNames, ResourceRole[] existingResourceRoles) {
        List<ResourceRole> availableRoles = new ArrayList<ResourceRole>();
        for (String allowedRoleName : allowedRoleNames) {
            ResourceRole role = ActionsHelper.findResourceRoleByName(existingResourceRoles, allowedRoleName);
            if (role != null) {
                availableRoles.add(role);
            }
        }
        return availableRoles.toArray(new ResourceRole[availableRoles.size()]);
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
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);
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
     * @throws BaseException if an unexpected error occurs.
     */
    private void setRegistrationPhaseExtensionParameters(HttpServletRequest request, Phase[] phases)
        throws BaseException {
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
     * @throws BaseException if an unexpected error occurs.
     */
    private void setSubmissionPhaseExtensionParameters(HttpServletRequest request, Phase[] phases)
        throws BaseException {
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
     * @throws NamingException if any errors occur during EJB lookup.
     * @throws RemoteException if any errors occur during EJB remote invocation.
     * @throws CreateException if any errors occur during EJB creation.
     */
    private List<TermsOfUseEntity> validateResourceTermsOfUse(long projectId, long userId, long roleId)
        throws CreateException, NamingException, RemoteException {
        
        List<TermsOfUseEntity> unAcceptedTerms = new ArrayList<TermsOfUseEntity>();

        // get remote services
        ProjectRoleTermsOfUse projectRoleTermsOfUse = ProjectRoleTermsOfUseLocator.getService();
        UserTermsOfUse userTermsOfUse = UserTermsOfUseLocator.getService();
        TermsOfUse termsOfUse = TermsOfUseLocator.getService();

        List<Long>[] necessaryTerms = projectRoleTermsOfUse.getTermsOfUse(
            (int) projectId, new int[]{new Long(roleId).intValue()}, DBMS.COMMON_OLTP_DATASOURCE_NAME);

        for (int j = 0; j < necessaryTerms.length; j++) {
            if (necessaryTerms[j] != null) {
                for (Long termsId : necessaryTerms[j]) {
                    if (!userTermsOfUse.hasTermsOfUse(userId, termsId, DBMS.COMMON_OLTP_DATASOURCE_NAME)) {
                        TermsOfUseEntity terms = termsOfUse.getEntity(termsId, DBMS.COMMON_OLTP_DATASOURCE_NAME);
                        unAcceptedTerms.add(terms);
                    }
                }
            }
        }

        return unAcceptedTerms;
    }
}
