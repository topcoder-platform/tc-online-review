/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.LazyValidatorForm;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.DeliverableManager;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.resource.NotificationType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
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
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.util.errorhandling.BaseException;

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
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public class ProjectActions extends DispatchAction {

/* TODO: Remove all these logging-related entries
    private final Log logger;

    private static java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
*/

    /**
     * Creates a new instance of the <code>ProjectActions</code> class.
     */
    public ProjectActions() {

//        logger = LogFactory.getLog("ProjectActions");

    }

    /**
     * This method is an implementation of &quot;New Project&quot; Struts Action defined for this
     * assembly, which is supposed to fetch lists of project types and categories from the database
     * and pass it to the JSP page to use it for populating approprate drop down lists.
     *
     * @return &quot;success&quot; forward that forwards to the /jsp/editProject.jsp page (as
     *         defined in struts-config.xml file) in the case of successfull processing,
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
        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check if the user has the permission to perform this action
        if(!AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)) {
            // If he doesn't, redirect the request to login page
            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
        }

        // Place the index of the active tab into the request
        request.setAttribute("projectTabIndex", new Integer(3));

        // Place the flag, indicating that we are creating a new project, into request
        request.setAttribute("newProject", Boolean.TRUE);

        // Load the look up data
        loadProjectEditLookups(request);

        // Populate the default values of some project form fields
        populateProjectFormDefaults((LazyValidatorForm) form);

        // Return the success forward
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    private void populateProjectFormDefaults(LazyValidatorForm lazyForm) {
        // Set the JS id to start generation from
        lazyForm.set("js_current_id", new Long(0));

        // Populate form with some data so that resources row template
        // is rendered properly by the appropriate JSP
        lazyForm.set("resources_role", 0, new Long(-1));
        lazyForm.set("resources_id", 0, new Long(-1));
        lazyForm.set("resources_action", 0, "add");

        // Populate form with some data so that phases row template
        // is rendered properly by the appropriate JSP
        lazyForm.set("phase_id", 0, new Long(-1));
        lazyForm.set("phase_action", 0, "add");
        lazyForm.set("phase_can_open", 0, Boolean.TRUE);
        lazyForm.set("phase_can_close", 0, Boolean.FALSE);
        lazyForm.set("phase_use_duration", 0, Boolean.TRUE);

        // Populate default phase duration
        lazyForm.set("addphase_duration", new Integer(ConfigHelper.getDefaultPhaseDuration()));
    }

    /**
     * This method loads the lookup data needed for rendering the Create Project/New Project pages.
     * The loaded data is stored in the request attributes.
     *
     * @param request the request to load the lookup data into
     * @throws BaseException if any error occurs while loading the lookup data
     */
    private void loadProjectEditLookups(HttpServletRequest request) throws BaseException {
        // Obtain an instance of Project Manager
        ProjectManager projectManager = ActionsHelper.createProjectManager(request);

        // Retrieve project types and categories
        ProjectType[] projectTypes = projectManager.getAllProjectTypes();
        ProjectCategory[] projectCategories = projectManager.getAllProjectCategories();

        // Store the retrieved types and categories in request
        request.setAttribute("projectTypes", projectTypes);
        request.setAttribute("projectCategories", projectCategories);

        // Obtain an instance of Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);
        // Get all types of resource roles
        ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();
        // Place resource roles into the request as attribute
        request.setAttribute("resourceRoles", resourceRoles);

        // Obtain an instance of Phase Manager
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, false);
        // Get all phase types
        PhaseType[] phaseTypes = phaseManager.getAllPhaseTypes();
        // Place them into request as an attribute
        request.setAttribute("phaseTypes", phaseTypes);

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorecardManager = ActionsHelper.createScorecardManager(request);

        // TODO: Check if we need to filter by the project category
        // Retrieve the scorecard lists
        Scorecard[] screeningScorecards = searchActiveScorecards(scorecardManager, "Screening");
        Scorecard[] reviewScorecards = searchActiveScorecards(scorecardManager, "Review");
        Scorecard[] approvalScorecards = searchActiveScorecards(scorecardManager, "Client Review");

        // Store them in the request
        request.setAttribute("screeningScorecards", screeningScorecards);
        request.setAttribute("reviewScorecards", reviewScorecards);
        request.setAttribute("approvalScorecards", approvalScorecards);

        // Load phase template names
        String[] phaseTemplateNames = ActionsHelper.createPhaseTemplate(null).getAllTemplateNames();
        request.setAttribute("phaseTemplateNames", phaseTemplateNames);
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
        form.set("pid", new Long(project.getId()));

        // Populate project name
        populateProjectFormProperty(form, String.class, "project_name", project, "Project Name");

        // Populate project type
        Long projectTypeId = new Long(project.getProjectCategory().getProjectType().getId());
        form.set("project_type", projectTypeId);

        // Populate project category
        Long projectCategoryId = new Long(project.getProjectCategory().getId());
        form.set("project_category", projectCategoryId);

        // Populate project category
        Long projectStatusId = new Long(project.getProjectStatus().getId());
        form.set("status", projectStatusId);

        // Populate project forum id
        populateProjectFormProperty(form, Long.class, "forum_id", project, "Developer Forum ID");

        // Populate project component id
        populateProjectFormProperty(form, Long.class, "component_id", project, "Component ID");

        // Populate project public option
        form.set("public", new Boolean("Yes".equals(project.getProperty("Public"))));
        // Populate project autopilot option
        form.set("autopilot", new Boolean("On".equals(project.getProperty("Autopilot Option"))));
        // Populate project status notification option
        form.set("email_notifications", new Boolean("On".equals(project.getProperty("Status Notification"))));
        // Populate project status notification option
        form.set("timeline_notifications", new Boolean("On".equals(project.getProperty("Timeline Notification"))));
        // Populate project status notification option
        // Note, this property is inverse by its meaning in project and form
        form.set("no_rate_project", new Boolean(!("Yes".equals(project.getProperty("Rated")))));

        // Populate project eligibility
        populateProjectFormProperty(form, String.class, "eligibility", project, "Eligibility");
        // Populate project SVN module
        populateProjectFormProperty(form, String.class, "SVN_module", project, "SVN Module");
        // Populate project notes
        populateProjectFormProperty(form, String.class, "notes", project, "Notes");

        // Populate the default values of some project form fields
        populateProjectFormDefaults(form);

        // Obtain Resource Manager instance
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);

        // Retreive the list of the resources associated with the project
        Resource[] resources =
            resourceManager.searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));

        // Populate form with resources data
        for (int i = 0; i < resources.length; ++i) {
            form.set("resources_id", i + 1, new Long(resources[i].getId()));
            form.set("resources_action", i + 1, "update");

            form.set("resources_role", i + 1, new Long(resources[i].getResourceRole().getId()));
            form.set("resources_phase", i + 1, "loaded_" + resources[i].getPhase());
            form.set("resources_name", i + 1, resources[i].getProperty("Handle"));

            if (resources[i].getProperty("Payment") != null) {
                form.set("resources_payment", i + 1, Boolean.TRUE);
                form.set("resources_payment_amount", i + 1, Double.valueOf((String) resources[i].getProperty("Payment")));
            } else {
                form.set("resources_payment", i + 1, Boolean.FALSE);
            }

            if (resources[i].getProperty("Payment Status") != null) {
                form.set("resources_paid", i + 1, resources[i].getProperty("Payment Status"));
            } else {
                form.set("resources_paid", i + 1, "N/A");
            }
        }

        // Obtain Phase Manager instance
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, false);

        // Retrive project phases
        Phase[] phases = ActionsHelper.getPhasesForProject(phaseManager, project);
        // Sort project phases
        Arrays.sort(phases, new ProjectPhaseComparer());

        Map phaseNumberMap = new HashMap();

        // Populate form with phases data
        for (int i = 0; i < phases.length; ++i) {
            form.set("phase_id", i + 1, new Long(phases[i].getId()));

            form.set("phase_can_open", i + 1,
                    Boolean.valueOf(phases[i].getPhaseStatus().getName().equals(PhaseStatus.SCHEDULED.getName())));
            form.set("phase_can_close", i + 1,
                    Boolean.valueOf(phases[i].getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName())));

            Long phaseTypeId = new Long(phases[i].getPhaseType().getId());
            form.set("phase_type", i + 1, phaseTypeId);
            Integer phaseNumber = (Integer) phaseNumberMap.get(phaseTypeId);
            if (phaseNumber == null) {
                phaseNumber = new Integer(1);
            } else {
                phaseNumber = new Integer(phaseNumber.intValue() + 1);
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
                form.set("phase_start_amount", i + 1, new Integer((int) (dependency.getLagTime() / 3600 / 1000)));
                form.set("phase_start_when", i + 1, dependency.isDependencyStart() ? "starts" : "ends");
                form.set("phase_start_dayshrs", i + 1, "hrs");
            } else {
                form.set("phase_start_by_phase", i + 1, Boolean.FALSE);
            }

            populateDatetimeFormProperties(form, "phase_start_date", "phase_start_time", "phase_start_AMPM", i + 1,
                    phases[i].calcStartDate());

            populateDatetimeFormProperties(form, "phase_end_date", "phase_end_time", "phase_end_AMPM", i + 1,
                    phases[i].calcEndDate());
            // always use duration
            form.set("phase_use_duration", i + 1, Boolean.TRUE);

            // populate the phase duration
            long phaseLength = phases[i].getLength();
            String phaseDuration = "";
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
            if (phases[i].getAttribute("Submission Number") != null) {
                form.set("phase_required_submissions", i + 1,
                        Integer.valueOf((String) phases[i].getAttribute("Submission Number")));
                form.set("phase_manual_screening", i + 1,
                        Boolean.valueOf("Yes".equals(phases[i].getAttribute("Manual Screening"))));
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

    /**
     * TODO: Document it
     *
     * @param form
     * @param dateProperty
     * @param timeProperty
     * @param ampmProperty
     * @param index
     * @param date
     */
    private void populateDatetimeFormProperties(LazyValidatorForm form, String dateProperty, String timeProperty,
            String ampmProperty, int index, Date date) {
        // TODO: Reuse the DateFormat instance
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy hh:mm aa", Locale.US);
        String[] parts = dateFormat.format(date).split("[ ]");
        form.set(dateProperty, index, parts[0]);
        form.set(timeProperty, index, parts[1]);
        form.set(ampmProperty, index, parts[2].toLowerCase());
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
            }
        }
    }

    /**
     * This method is an implementation of &quot;Edit Project&quot; Struts Action defined for this
     * assembly, which is supposed to fetch lists of project types and categories from the database
     * and pass it to the JSP page to use it for populating approprate drop down lists. It is also
     * supposed to retrieve the project to be edited and to populate the form with appropriate
     * values.
     *
     * @return &quot;success&quot; forward that forwards to the /jsp/editProject.jsp page (as
     *         defined in struts-config.xml file) in the case of successfull processing,
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
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Retrieve the id of project to be edited
        long projectId = Long.parseLong(request.getParameter("pid"));

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, projectId);

        // Check if the user has the permission to perform this action
        if(!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME)) {
            // If he doesn't have, forward to login page
            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
        }

        // Place the flag, indicating that we are editing the existing project, into request
        request.setAttribute("newProject", Boolean.FALSE);

        // Load the lookup data
        loadProjectEditLookups(request);

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager(request);

        // Retrieve the list of all project statuses
        ProjectStatus[] projectStatuses = manager.getAllProjectStatuses();
        // Store it in the request
        request.setAttribute("projectStatuses", projectStatuses);

        // Retrieve the project to be edited
        Project project = manager.getProject(projectId);
        // Store the retieved project in the request
        request.setAttribute("project", project);

        // Populate the form with project properties
        populateProjectForm(request, (LazyValidatorForm) form, project);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Write sensible description for method saveProject here
     *
     * @return TODO: Write sensible description of return value for method saveProject
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     */
    public ActionForward saveProject(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        // Cast the form to its actual type
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;

        // Check whether user is creating new project or editing existing one
        boolean newProject = (lazyForm.get("pid") == null);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check if the user has the permission to perform this action
        if (newProject) {
            if(!AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)) {
                // If he doesn't, redirect the request to login page
                return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
            }
        } else {
            if(!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME)) {
                // If he doesn't, redirect the request to login page
                return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
            }
        }

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager(request);

        // Retrieve project types, categories and statuses
        ProjectCategory[] projectCategories = manager.getAllProjectCategories();
        ProjectStatus[] projectStatuses = manager.getAllProjectStatuses();

        // This variable determines whether status of the project has been changed by this save
        // operation. This is usefule to determine whether Explanation is a rquired field or not
        boolean statusHasChanged = false;

        Project project;
        if (newProject) {
            // Find "Active" project status
            ProjectStatus activeStatus = ActionsHelper.findProjectStatusByName(projectStatuses, "Active");
            // Find the project category by the specified id
            ProjectCategory category = ActionsHelper.findProjectCategoryById(projectCategories,
                    ((Long) lazyForm.get("project_category")).longValue());
            // Create Project instance
            project = new Project(category, activeStatus);
            statusHasChanged = true; // Status is always considered to be changed for new projects
        } else {
            // Retrieve Project instance to be edited
            // TODO: Probably check it for null, and issue the error, etc.
            project = manager.getProject(((Long) lazyForm.get("pid")).longValue());
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
            // Populate project root catalog id (always set to Application)
            // TODO: There should be an ability to specify different Root Catalog
            project.setProperty("Root Catalog ID", "9926572");
            // Populate project eligibility
            project.setProperty("Eligibility", lazyForm.get("eligibility"));
            // Populate project public flag
            project.setProperty("Public", Boolean.TRUE.equals(lazyForm.get("public")) ? "Yes" : "No");
        } else {
            ProjectStatus newProjectStatus =
                ActionsHelper.findProjectStatusById(projectStatuses, ((Long) lazyForm.get("status")).longValue());
            String oldStatusName = project.getProjectStatus().getName();
            String newStatusName = (newProjectStatus != null) ? newProjectStatus.getName() : oldStatusName;

            // Determine if status has changed
            statusHasChanged = !oldStatusName.equalsIgnoreCase(newStatusName);
            // If status has changed, update the project
            if (statusHasChanged) {
                // Populate project status
                project.setProjectStatus(newProjectStatus);

                // Set Completion Timestamp once the status is changed to completed, Cancelled - Failed Review or Deleted
                ActionsHelper.setProjectCompletionDate(project, newProjectStatus, (Format) request.getAttribute("date_format"));
            }
        }

        // Populate project forum id
        project.setProperty("Developer Forum ID", lazyForm.get("forum_id"));
        // Populate project component id
        project.setProperty("Component ID", lazyForm.get("component_id"));
        // Populate project SVN module
        project.setProperty("SVN Module", lazyForm.get("SVN_module"));

        // Extract project's properties from the form
        Boolean autopilotOnObj = (Boolean) lazyForm.get("autopilot");
        Boolean sendEmailNotificationsObj = (Boolean) lazyForm.get("email_notifications");
        Boolean sendTLChangeNotificationsObj = (Boolean) lazyForm.get("timeline_notifications");
        Boolean doNotRateProjectObj = (Boolean) lazyForm.get("no_rate_project");

        // Unbox the properties
        boolean autopilotOn = (autopilotOnObj != null) ? autopilotOnObj.booleanValue() : false;
        boolean sendEmailNotifications =
            (sendEmailNotificationsObj != null) ? sendEmailNotificationsObj.booleanValue() : false;
        boolean sendTLChangeNotifications =
            (sendTLChangeNotificationsObj != null) ? sendTLChangeNotificationsObj.booleanValue() : false;
        boolean doNotRateProject = (doNotRateProjectObj != null) ? doNotRateProjectObj.booleanValue() : false;

        // Populate project autopilot option
        project.setProperty("Autopilot Option", (autopilotOn) ? "On" : "Off");
        // Populate project status notifications option
        project.setProperty("Status Notification", (sendEmailNotifications) ? "On" : "Off");
        // Populate project timeline notifications option
        project.setProperty("Timeline Notification", (sendTLChangeNotifications) ? "On" : "Off");
        // Populate project rated option, note that it is inveresed
        project.setProperty("Rated", (doNotRateProject) ? "No" : "Yes");

        // Populate project notes
        project.setProperty("Notes", lazyForm.get("notes"));

        // TODO: Project status change, includes additional explanation to be concatenated

        // Create the map to store the mapping from phase JS ids to phases
        Map phasesJsMap = new HashMap();

        // Create the list to store the phases to be deleted
        List phasesToDelete = new ArrayList();

        // Save the project phases
        // FIXME: the project itself is also saved by the following call. Needs to be refactored
        Phase[] projectPhases =
            saveProjectPhases(newProject, request, lazyForm, project, phasesJsMap, phasesToDelete, statusHasChanged);

        // If needed switch project current phase
        if (!newProject && !ActionsHelper.isErrorsPresent(request)) {
            switchProjectPhase(request, lazyForm, projectPhases, phasesJsMap);
        }

        // FIXME: resources must be saved even if there are validation errors to validate resources
        if (!ActionsHelper.isErrorsPresent(request)) {
            // Save the project resources
            saveResources(newProject, request, lazyForm, project, projectPhases, phasesJsMap);
        }

        if (!ActionsHelper.isErrorsPresent(request)) {
            // Delete the phases to be deleted
            deletePhases(request, project, phasesToDelete);
        }

        // Check if there are any validation errors and return appropriate forward
        if (ActionsHelper.isErrorsPresent(request)) {
            // TODO: Check if the form is really for new project
            request.setAttribute("newProject", Boolean.valueOf(newProject));

            // Load the lookup data
            loadProjectEditLookups(request);
            if (!newProject) {
                // Store project statuses in the request
                request.setAttribute("projectStatuses", projectStatuses);
                // Store the retieved project in the request
                request.setAttribute("project", project);
            }

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
     * @param project
     * @param phasesToDelete
     * @throws BaseException
     */
    private void deletePhases(HttpServletRequest request, Project project, List phasesToDelete) throws BaseException {
        if (phasesToDelete.isEmpty()) {
            return;
        }

        Phase phase = (Phase) phasesToDelete.get(0);
        com.topcoder.project.phases.Project phProject = phase.getProject();

        for (int i = 0; i < phasesToDelete.size(); i++) {
            phase = (Phase) phasesToDelete.get(i);
            phProject.removePhase(phase);
        }

        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, false);

        phaseManager.updatePhases(phProject, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
    }


    /**
     * TODO: Document it
     *
     * @return
     * @param newProject
     * @param request
     * @param lazyForm
     * @param project
     * @param phasesJsMap TODO
     * @param phasesToDelete TODO
     * @throws BaseException
     */
    private Phase[] saveProjectPhases(boolean newProject, HttpServletRequest request, LazyValidatorForm lazyForm,
            Project project, Map phasesJsMap, List phasesToDelete, boolean statusHasChanged)
        throws BaseException {
        // Obtain an instance of Phase Manager
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, false);

        com.topcoder.project.phases.Project phProject;

        if (newProject) {
            // Create new Phases Project
            // TODO: Use real values for date and workdays, not the test ones
            // TODO: Handle the situation of project being edited
            phProject = new com.topcoder.project.phases.Project(new Date(), (new DefaultWorkdaysFactory()).createWorkdaysInstance());
        } else {
            // Retrive the Phases Project with the id equal to the id of specified Project
            phProject = phaseManager.getPhases(project.getId());
        }

        // Get the list of all previously existing phases
        Phase[] oldPhases = phProject.getAllPhases();

        // Get the list of all existing phase types
        PhaseType[] allPhaseTypes = phaseManager.getAllPhaseTypes();

        // Get the array of phase types specified for each phase
        Long[] phaseTypes = (Long[]) lazyForm.get("phase_type");

        // This will be a Map from phases to their indexes in form
        Map phasesToForm = new HashMap();

        // This will be a list of all the phases except the ones to be deleted
        List phaseList = new ArrayList();

        // FIRST PASS
        // 0-index phase is skipped since it is a "dummy" one
        for (int i = 1; i < phaseTypes.length; i++) {
            Phase phase = null;

            // Check what is the action to be performed with the phase
            // and obtain Phase instance in appropriate way
            String phaseAction = (String) lazyForm.get("phase_action", i);

            ////////////////////////////////
            // Add new phase
            ////////////////////////////////
            if ("add".equals(phaseAction)) {
                // Create a Phase using a default length 0 first
                phase = new Phase(phProject, 0);

                // Set phase type
                phase.setPhaseType(ActionsHelper.findPhaseTypeById(allPhaseTypes, phaseTypes[i].longValue()));
                // Set phase status to "Scheduled"
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);

                // Get the start phase
                if (Boolean.FALSE.equals(lazyForm.get("phase_start_by_phase", i))) {
                    // Get phase start date from form
                    Date phaseStartDate = parseDatetimeFormProperties(lazyForm, i, "phase_start_date",
                            "phase_start_time", "phase_start_AMPM");

                    // Set phase fixed start date
                    phase.setFixedStartDate(phaseStartDate);
                }

                phase.setScheduledStartDate(phase.calcStartDate());

                // flag value indicates using end date or using duration
                boolean useDuration = true;
                // length of the phase
                long length;

                useDuration = ((Boolean) lazyForm.get("phase_use_duration", i)).booleanValue();


                // If phase duration was not specified
                if (!useDuration) {
                    // Get phase end date from form
                    Date phaseEndDate = parseDatetimeFormProperties(lazyForm, i, "phase_end_date", "phase_end_time",
                            "phase_end_AMPM");

                    // Calculate phase length
                    length = phaseEndDate.getTime() - phase.getScheduledStartDate().getTime();

                    // Check if the end date of phase goes after the start date
                    if (length < 0) {
                        ActionsHelper.addErrorToRequest(request,
                                new ActionMessage("error.com.cronos.onlinereview.actions.editProject.StartAfterEnd",
                                        phase.getPhaseType().getName()));
                        break;
                    } else {
                        // Set phase duration appropriately
                        phase.setLength(length);
                    }
                } else {

                    String duration = (String) lazyForm.get("phase_duration", i);
                    String[] parts = duration.split(":");

                    // the format should be hh or hh:mm
                    if (parts.length < 1 || parts.length > 2) {
                        ActionsHelper.addErrorToRequest(request, new ActionMessage("error.com.cronos.onlinereview.actions.editProject.InvalidDurationFormat",
                                                                        phase.getPhaseType().getName()));
                        break;
                    }

                    try {

                        if (parts.length == 1) {
                            // use hh format
                            length = Long.parseLong(duration) * 3600 * 1000;
                        } else {
                            // use hh:mm format
                            length = (Long.parseLong(parts[0]) * 60 + Long.parseLong(parts[1])) * 60 * 1000;
                        }

                    } catch (NumberFormatException nfe) {
                        // the hh or mm is not valid integer
                        ActionsHelper.addErrorToRequest(request,
                                new ActionMessage("error.com.cronos.onlinereview.actions.editProject.InvalidDurationFormat",
                                        phase.getPhaseType().getName()));
                        break;
                    }

                }

                phase.setLength(length);
                phase.setScheduledEndDate(phase.calcEndDate());

                // Add it to Phases Project
                phProject.addPhase(phase);
                // Add the phase to the list
                phaseList.add(phase);
            }  else {
                long phaseId = ((Long) lazyForm.get("phase_id", i)).longValue();
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


            /////////////////////////////
            // Update existing phase
            /////////////////////////////
            // If action is "update", update phase duration, put the phase to the map
            if ("update".equals(phaseAction)) {

                // Get the start phase
                if (Boolean.FALSE.equals(lazyForm.get("phase_start_by_phase", i))) {
                    // Get phase start date from form
                    Date phaseStartDate = parseDatetimeFormProperties(lazyForm, i, "phase_start_date",
                            "phase_start_time", "phase_start_AMPM");
                    // Set phase fixed start date
                    phase.setFixedStartDate(phaseStartDate);
                }

                phase.setScheduledStartDate(phase.calcStartDate());

               // flag value indicates using end date or using duration
                boolean useDuration = true;
                // the length of the phase
                long length;

                useDuration = ((Boolean) lazyForm.get("phase_use_duration", i)).booleanValue();


                // If phase duration was not specified
                if (!useDuration) {
                    // Get phase end date from form
                    Date phaseEndDate = parseDatetimeFormProperties(lazyForm, i,
                            "phase_end_date", "phase_end_time", "phase_end_AMPM");

                    // Calculate phase length
                    length = phaseEndDate.getTime() - phase.getScheduledStartDate().getTime();
                    // Check if the end date of phase goes after the start date
                    if (length < 0) {
                        ActionsHelper.addErrorToRequest(request, new ActionMessage(
                                "error.com.cronos.onlinereview.actions.editProject.StartAfterEnd",
                                phase.getPhaseType().getName()));
                        break;
                    } else {
                        // Set phase duration appropriately
                        phase.setLength(length);
                    }
                } else {

                    String duration = (String) lazyForm.get("phase_duration", i);
                    String[] parts = duration.split(":");

                    if (parts.length < 1 || parts.length > 2) {
                        ActionsHelper.addErrorToRequest(request, new ActionMessage("error.com.cronos.onlinereview.actions.editProject.InvalidDurationFormat",
                                                                    phase.getPhaseType().getName()));
                        break;
                    }

                    try {

                    if (parts.length == 1) {
                        // use hh format
                        length = Long.parseLong(duration) * 3600 * 1000;
                    } else {
                        // use hh:mm format
                        length = (Long.parseLong(parts[0]) * 60 + Long.parseLong(parts[1])) * 60 * 1000;
                    }

                    } catch (NumberFormatException nfe) {
                        ActionsHelper.addErrorToRequest(request, new ActionMessage("error.com.cronos.onlinereview.actions.editProject.InvalidDurationFormat",
                                phase.getPhaseType().getName()));
                        break;
                    }

                }

                // Set phase duration
                phase.setLength(length);
                // Add the phase to the list
                phaseList.add(phase);
            }

            // If action is "delete", proceed to the next phase
            if ("delete".equals(phaseAction)) {
                continue;
            }

            // Put the phase to the map from phase JS ids to phases
            phasesJsMap.put(lazyForm.get("phase_js_id", i), phase);
            // Put the phase to the map from phases to the indexes of form inputs
            phasesToForm.put(phase, new Integer(i));
        }

        // Minimal date will be the project start date
        Date minDate = null;

        // SECOND PASS
        for (int i = 1; i < phaseTypes.length; i++) {
            Object phaseObj = phasesJsMap.get(lazyForm.get("phase_js_id", i));

            // If phase is not found in map, it is to be deleted
            if (phaseObj == null) {
                long phaseId = ((Long) lazyForm.get("phase_id", i)).longValue();

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
                phase.setPhaseType(ActionsHelper.findPhaseTypeById(allPhaseTypes, phaseTypes[i].longValue()));
                // Set phase status to "Scheduled"
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            }

            // If phase is not started by other phase end
            if (Boolean.FALSE.equals(lazyForm.get("phase_start_by_phase", i))) {
                // Get phase start date from form
                Date phaseStartDate = parseDatetimeFormProperties(lazyForm, i, "phase_start_date",
                        "phase_start_time", "phase_start_AMPM");
                // Set phase fixed start date
                phase.setFixedStartDate(phaseStartDate);

                // Check if the current date is minimal
                if (minDate == null || phaseStartDate.getTime() < minDate.getTime()) {
                    minDate = phaseStartDate;
                }
            } else {
                // Get the dependency phase
                Phase dependencyPhase = (Phase) phasesJsMap.get(lazyForm.get("phase_start_phase", i));

                if (dependencyPhase != null) {
                    boolean dependencyStart;
                    boolean dependantStart;
                    if ("ends".equals(lazyForm.get("phase_start_when", i))) {
                        dependencyStart = false;
                        dependantStart = true;
                    } else {
                        dependencyStart = true;
                        dependantStart = true;
                    }

                    long unitMutiplier = 1000 * 3600 * ("days".equals(lazyForm.get("phase_start_dayshrs", i)) ? 24 : 1);
                    long lagTime = unitMutiplier * ((Integer) lazyForm.get("phase_start_amount", i)).longValue();

                    // Create phase Dependency
                    Dependency dependency = new Dependency(dependencyPhase, phase,
                            dependencyStart, dependantStart, lagTime);

                    // Add dependency to phase
                    phase.addDependency(dependency);
                }
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
            Integer requiredSubmissions = (Integer) lazyForm.get("phase_required_submissions", i);
            // If the number of required submissions is specified, set it
            if (requiredSubmissions != null) {
                phase.setAttribute("Submission Number", requiredSubmissions.toString());
            }
            // If the number of required reviewers is specified, set it
            Integer requiredReviewer = (Integer) lazyForm.get("phase_required_reviewers", i);
            if (requiredReviewer != null) {
                phase.setAttribute("Reviewer Number", requiredReviewer.toString());
            }

            Boolean manualScreening = (Boolean) lazyForm.get("phase_manual_screening", i);
            // If the manual screening flag is specified, set it
            if (manualScreening != null) {
                phase.setAttribute("Manual Screening", manualScreening.booleanValue() ? "Yes" : "No");
            } else {
                phase.setAttribute("Manual Screening", "No");
            }
            Boolean viewAppealResponses = (Boolean) lazyForm.get("phase_view_appeal_responses", i);
            // If the view appeal response during appeals flag is specified, set it
            if (viewAppealResponses != null) {
                phase.setAttribute("View Response During Appeals", viewAppealResponses.booleanValue() ? "Yes" : "No");
            }
        }

        // Update project start date if needed
        if (minDate != null) {
            phProject.setStartDate(minDate);
        }

        // THIRD PASS
        boolean hasCircularDependencies = false;
        Set processed = new HashSet();
        for (int i = 1; i < phaseTypes.length; i++) {
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

            Set visited = new HashSet();
            Stack stack = new Stack();

            for (;;) {
                processed.add(phase);
                visited.add(phase);
                stack.push(phase);

                Dependency[] dependencies = phase.getAllDependencies();
                // Actually there should be either zero or one dependecy, we'll assume it
                if (dependencies.length == 0) {
                    // If there is no dependency, stop proceessing
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
                phase = (Phase) stack.pop();
                int paramIndex = ((Integer) phasesToForm.get(phase)).intValue();

                // If the phase is scheduled to start before some other phase start/end
                if (Boolean.TRUE.equals(lazyForm.get("phase_start_by_phase", paramIndex)) &&
                        "minus".equals(lazyForm.get("phase_start_plusminus", paramIndex))) {
                    Dependency dependency = phase.getAllDependencies()[0];

                    Date dependencyDate;
                    if ("ends".equals(lazyForm.get("phase_start_when", paramIndex))) {
                        dependencyDate = dependency.getDependency().getScheduledEndDate();
                    } else {
                        dependencyDate = dependency.getDependency().getScheduledStartDate();
                    }
                    phase.setFixedStartDate(new Date(dependencyDate.getTime() - dependency.getLagTime()));

                    phase.clearDependencies();
                }

                try {

                    // Set scheduled start date to calculated start date
                    phase.setScheduledStartDate(phase.calcStartDate());

                    // flag value indicates using end date or using duration
                    boolean useDuration = true;

                    useDuration = ((Boolean) lazyForm.get("phase_use_duration", paramIndex)).booleanValue();


                    // If phase duration was not specified
                    if (!useDuration) {
                        // Get phase end date from form
                        Date phaseEndDate = parseDatetimeFormProperties(lazyForm, paramIndex,
                                "phase_end_date", "phase_end_time", "phase_end_AMPM");

                        // Calculate phase length
                        long length = phaseEndDate.getTime() - phase.getScheduledStartDate().getTime();

                        // Check if the end date of phase goes after the start date
                        if (length < 0) {
                            ActionsHelper.addErrorToRequest(request, new ActionMessage(
                                    "error.com.cronos.onlinereview.actions.editProject.StartAfterEnd",
                                    phase.getPhaseType().getName()));
                            break;
                        } else {
                            // Set phase duration appropriately
                            phase.setLength(length);
                        }
                    }

                    // Set sheduled phase end date to calculated end datehase
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

        if (hasCircularDependencies) {
            // TODO: Return null or so
            return oldPhases;
        }

        // Sort project phases
        Collections.sort(phaseList, new ProjectPhaseComparer());

        Phase[] projectPhases = (Phase[]) phaseList.toArray(new Phase[phaseList.size()]);

        // Validate the project phases
        boolean validationSucceeded = validateProjectPhases(request, project, projectPhases);

        // Get Explanation for edited project.
        // It does not matter what this string contains for new projects
        String explanationText = (!newProject && !statusHasChanged) ? (String) lazyForm.get("explanation") : "***";

        // Validate Explanation, but only if status has not been changed
        if (explanationText == null || explanationText.trim().length() == 0) {
            // Indicate unsuccessful validation
            validationSucceeded = false;
            // Add error that explains the validation error
            ActionsHelper.addErrorToRequest(request, "explanation",
                    "error.com.cronos.onlinereview.actions.editProject.explanation");
        }

        if (!validationSucceeded) {
            // If project validation has failed, return immediately
            return projectPhases;
        }

        // FIXME: Refactor it
        ProjectManager projectManager = ActionsHelper.createProjectManager(request);

        // Set project rating date
        ActionsHelper.setProjectRatingDate(project, projectPhases, (Format) request.getAttribute("date_format"));

        if (newProject) {
            // Create project in persistence level
            projectManager.createProject(project, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

            // Set the id of Phases Project to be equal to the id of appropriate Project
            phProject.setId(project.getId());
        } else {
            projectManager.updateProject(project, (String) lazyForm.get("explanation"),
                    Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        // Save the phases at the persistence level
        phaseManager.updatePhases(phProject, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        // TODO: The following line was added just to be safe. May be unneeded as well as another one
        projectPhases = phProject.getAllPhases();
        // Sort project phases
        Arrays.sort(projectPhases, new ProjectPhaseComparer());

        return projectPhases;
    }

    /**
     *
     * TODO: Document it.
     *
     * @param request
     * @param lazyForm
     * @param projectPhases
     * @param phasesJsMap
     * @throws BaseException
     */
    private void switchProjectPhase(HttpServletRequest request, LazyValidatorForm lazyForm,
            Phase[] projectPhases, Map phasesJsMap) throws BaseException {

        // Get name of action to be performed
        String action = (String) lazyForm.get("action");

        // Get new current phase id
        String phaseJsId = (String) lazyForm.get("action_phase");

        if (phaseJsId != null && phasesJsMap.containsKey(phaseJsId)) {
            // Get the phase to be operated on
            Phase phase = (Phase) phasesJsMap.get(phaseJsId);

            // Get the status of phase
            PhaseStatus phaseStatus = phase.getPhaseStatus();
            // Get the type of the phase
            PhaseType phaseType = phase.getPhaseType();

            // Obtain an instance of Phase Manager
            PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, true);

            if ("close_phase".equals(action)) {
                //logger.log(Level.INFO, "switchProjectPhase: " + phaseType.getName() +
                //        " is in " + phaseStatus.getName() + " state");
                if (phaseStatus.getName().equals(PhaseStatus.OPEN.getName()) && phaseManager.canEnd(phase)) {
                    //logger.log(Level.INFO, "switchProjectPhase: " + phaseType.getName() + " is being closed");
                    // Close the phase
                    phaseManager.end(phase, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                } else {
                    //logger.log(Level.INFO, "switchProjectPhase: " + phaseType.getName() + " cannot be closed");
                    ActionsHelper.addErrorToRequest(request, new ActionMessage(
                            "error.com.cronos.onlinereview.actions.editProject.CannotClosePhase", phaseType.getName()));
                }
            } else if ("open_phase".equals(action)) {
                //logger.log(Level.INFO, "switchProjectPhase: " + phaseType.getName() +
                //        " is in " + phaseStatus.getName() + " state");
                if (phaseStatus.getName().equals(PhaseStatus.SCHEDULED.getName()) && phaseManager.canStart(phase)) {
                    //logger.log(Level.INFO, "switchProjectPhase: " + phaseType.getName() + " is being started");
                    // Open the phase
                    phaseManager.start(phase, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                } else {
                    //logger.log(Level.INFO, "switchProjectPhase: " + phaseType.getName() + " cannot be started");
                    ActionsHelper.addErrorToRequest(request, new ActionMessage(
                            "error.com.cronos.onlinereview.actions.editProject.CannotOpenPhase", phaseType.getName()));
                }
            }
        }
    }

    /**
     * TODO: Document it
     * Note, that this method assumes that phases are already sorted by the start date, etc.
     *
     *
     * @param request
     * @param project
     * @param allPhases
     * @return
     */
    private boolean validateProjectPhases(HttpServletRequest request, Project project, Phase[] projectPhases) {
        boolean arePhasesValid = true;

        // TODO: Refactor this function, make it more concise

        // Check the beginning phase, it should be either Registration or submission
        if (projectPhases.length > 0 &&
                !projectPhases[0].getPhaseType().getName().equals(Constants.REGISTRATION_PHASE_NAME) &&
                !projectPhases[0].getPhaseType().getName().equals(Constants.SUBMISSION_PHASE_NAME)) {
            ActionsHelper.addErrorToRequest(request,
                    "error.com.cronos.onlinereview.actions.editProject.WrongBeginningPhase");
            arePhasesValid = false;
        }

        // Check the phases as a whole
        for (int i = 0; i < projectPhases.length; i++) {
            if (projectPhases[i].getPhaseType().getName().equals(Constants.SUBMISSION_PHASE_NAME)) {
                // Submission should follow registration if it exists
                if (i > 0 && !projectPhases[i - 1].getPhaseType().getName().equals(Constants.REGISTRATION_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.SubmissionMustFollow");
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.REGISTRATION_PHASE_NAME)) {
                // Registration should be followed by submission
                if (i == projectPhases.length - 1 || !projectPhases[i + 1].getPhaseType().getName().equals(Constants.SUBMISSION_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.RegistrationMustBeFollowed");
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.REVIEW_PHASE_NAME)) {
                // Review should follow submission or screening
                if (i == 0 || (!projectPhases[i - 1].getPhaseType().getName().equals(Constants.SUBMISSION_PHASE_NAME) &&
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.SCREENING_PHASE_NAME))) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.ReviewMustFollow");
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.APPEALS_PHASE_NAME)) {
                // Appeals should follow review
                if (i == 0 || !projectPhases[i - 1].getPhaseType().getName().equals(Constants.REVIEW_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.AppealsMustFollow");
                    arePhasesValid = false;
                }
                // Appeals should be followed by the appeals response
                if (i == projectPhases.length - 1 ||
                        !projectPhases[i + 1].getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.AppealsMustBeFollowed");
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                // Appeal response should follow appeals
                if (i == 0 || !projectPhases[i - 1].getPhaseType().getName().equals(Constants.APPEALS_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.AppealsResponseMustFollow");
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.AGGREGATION_PHASE_NAME)) {
                // Aggregation should follow appeals response or review, or aggregation review
                if (i == 0 ||
                        (!projectPhases[i - 1].getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME) &&
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.REVIEW_PHASE_NAME) &&
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.AGGREGATION_REVIEW_PHASE_NAME))) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.AggregationMustFollow");
                    arePhasesValid = false;
                }
                // Aggregation should be followed by the aggregation review
                if (i == projectPhases.length - 1 ||
                        !projectPhases[i + 1].getPhaseType().getName().equals(Constants.AGGREGATION_REVIEW_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.AggregationMustBeFollowed");
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.AGGREGATION_REVIEW_PHASE_NAME)) {
                // Aggregation review should follow aggregation
                if (i == 0 ||
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.AGGREGATION_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.AggregationReviewMustFollow");
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.FINAL_FIX_PHASE_NAME)) {
                // Final fix should follow either appeals response or aggregation review, or final review
                if (i == 0 ||
                        (!projectPhases[i - 1].getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME) &&
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.AGGREGATION_REVIEW_PHASE_NAME) &&
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.FINAL_REVIEW_PHASE_NAME))) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.FinalFixMustFollow");
                    arePhasesValid = false;
                }
                // Final fix should be followed by the final review
                if (i == projectPhases.length - 1 ||
                        !projectPhases[i + 1].getPhaseType().getName().equals(Constants.FINAL_REVIEW_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.FinalFixMustBeFollowed");
                    arePhasesValid = false;
                }
            }  else if (projectPhases[i].getPhaseType().getName().equals(Constants.FINAL_REVIEW_PHASE_NAME)) {
                // Final review should follow final fix
                if (i == 0 ||
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.FINAL_FIX_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request,
                            "error.com.cronos.onlinereview.actions.editProject.FinalReviewMustFollow");
                    arePhasesValid = false;
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
     * @param ampmProperty
     * @return
     */
    private Date parseDatetimeFormProperties(LazyValidatorForm lazyForm, int propertyIndex, String dateProperty,
            String timeProperty, String ampmProperty) {
        // Retrieve the values of form properties
        String dateString = (String) lazyForm.get(dateProperty, propertyIndex);
        String timeString = (String) lazyForm.get(timeProperty, propertyIndex);
        String ampmString = (String) lazyForm.get(ampmProperty, propertyIndex);

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
        calendar.set(Calendar.HOUR, hour != 12 ? hour : 0);
        if (timeParts.length == 1) {
            calendar.set(Calendar.MINUTE, 0);
        } else {
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
        }

        // Set am/pm property
        calendar.set(Calendar.AM_PM, "am".equals(ampmString) ? Calendar.AM : Calendar.PM);

        // Set seconds, milliseconds
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Returned parsed Date
        return calendar.getTime();
    }

    /**
     * TODO: Document it
     *
     * @param newProject
     * @param request
     * @param lazyForm
     * @param project
     * @param projectPhases
     * @param phasesJsMap
     * @throws BaseException
     */
    private void saveResources(boolean newProject, HttpServletRequest request,
            LazyValidatorForm lazyForm, Project project, Phase[] projectPhases, Map phasesJsMap)
        throws BaseException {
        // Obtain the instance of the User Retrieval
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);

        // Obtain the instance of the Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);

        // Get all types of resource roles
        ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();

        // Get all types of notifications
        NotificationType[] types = resourceManager.getAllNotificationTypes();
        long timelineNotificationId = Long.MIN_VALUE;

        // get the id for the timelineNotification
        for (int i = 0; i < types.length; ++i) {
            if (types[i].getName().equals("Timeline Notification")) {
                timelineNotificationId = types[i].getId();
                break;
            }
        }

        // TODO: we assume timelineNotifictionId exists here, need to do the check

        // Get the array of resource names
        String[] resourceNames = (String[]) lazyForm.get("resources_name");

        // HashSet used to identify resource of new user
        Set newUsers = new HashSet();
        Set oldUsers = new HashSet();
        Set newSubmitters = new HashSet();

        // 0-index resource is skipped as it is a "dummy" one
        for (int i = 1; i < resourceNames.length; i++) {

            // TODO: Actually no updates should be done at all in the case validation fails!!!
            if (resourceNames[i] == null || resourceNames[i].trim().length() == 0) {
                ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                        "error.com.cronos.onlinereview.actions.editProject.Resource.Empty");
                continue;
            }

            // Get info about user with the specified handle
            ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);

            // TODO: Actually no updates should be done at all in the case validation fails!!!
            // If there is no user with such handle, indicate an error
            if (user == null) {
                ActionsHelper.addErrorToRequest(request, "resources_name[" + i + "]",
                        "error.com.cronos.onlinereview.actions.editProject.Resource.NotFound");
                continue;
            }

            Resource resource;

            // Check what is the action to be performed with the resource
            // and obtain Resource instance in appropriate way
            String resourceAction = (String) lazyForm.get("resources_action", i);
            if ("add".equals(resourceAction)) {
                // Create new resource
                resource = new Resource();

                newUsers.add(new Long(user.getId()));

                //System.out.println("ADD:" + user.getId());
            }  else {
                Long resourceId = (Long) lazyForm.get("resources_id", i);

                if (resourceId.longValue() != -1) {
                    // Retrieve the resource with the specified id
                    resource = resourceManager.getResource(resourceId.longValue());
                    oldUsers.add(new Long(user.getId()));
                    //System.out.println("REMOVE:" + user.getId());
                } else {
                    // -1 value as id marks the resources that were't persisted in DB yet
                    // and so should be skipped for actions other then "add"
                    oldUsers.add(new Long(user.getId()));
                    //System.out.println("REMOVE:" + user.getId());
                    continue;
                }
            }

            // If action is "delete", delete the resource and proceed to the next one
            if ("delete".equals(resourceAction)) {
                resourceManager.removeResource(resource,
                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                resourceManager.removeNotifications(new long[] {user.getId()}, project.getId(),
                        timelineNotificationId, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                continue;
            }

            // Set resource properties
            resource.setProject(new Long(project.getId()));

            boolean resourceRoleChanged = false;
            ResourceRole role = ActionsHelper.findResourceRoleById(
                    resourceRoles, ((Long) lazyForm.get("resources_role", i)).longValue());
            if (role != null && role != resource.getResourceRole()) {
                resource.setResourceRole(role);
                resourceRoleChanged = true;
            }

            resource.setProperty("Handle", resourceNames[i]);
            if (Boolean.TRUE.equals(lazyForm.get("resources_payment", i))) {
                resource.setProperty("Payment", lazyForm.get("resources_payment_amount", i));
            } else {
                resource.setProperty("Payment", null);
            }
            String paid = (String) lazyForm.get("resources_paid", i);
            if ("Yes".equals(paid) || "No".equals(paid)) {
                resource.setProperty("Payment Status", paid);
            }
            // Set resource phase id, if needed
            Long phaseTypeId = resource.getResourceRole().getPhaseType();
            if (phaseTypeId != null) {
                Phase phase = (Phase) phasesJsMap.get(lazyForm.get("resources_phase", i));
                if (phase != null) {
                    resource.setPhase(new Long(phase.getId()));
                } else {
                    // TODO: Probably issue validation error here
                }
            }

            // Set resource properties copied from external user
            resource.setProperty("External Reference ID", new Long(user.getId()));
            resource.setProperty("Email", user.getEmail());

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
            }
            // TODO: Handle actual registration event, and update the following only then
            // If resource is a submitter, screener or reviewer, store registration date
            if (resourceRole.equals("Submitter") || resourceRole.equals("Screener") ||
                    resourceRole.equals("Reviewer")) {
                resource.setProperty("Registration Date", new Date());
            }

            // Save the resource in the persistence level
            resourceManager.updateResource(resource, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
            if ("add".equals(resourceAction) && resourceRole.equals("Submitter")) {
                newSubmitters.add(new Long(user.getId()));
            }
        }

        for (Iterator itr = oldUsers.iterator(); itr.hasNext();) {
            Object obj = itr.next();
            newUsers.remove(obj);
            newSubmitters.remove(obj);
        }

        // Populate project_result for new submitters
        ActionsHelper.populateProjectResult(project.getId(), newSubmitters);

        // Update all the timeline notifications
        if (project.getProperty("Timeline Notification").equals("On")) {
            long[] userIds = new long[newUsers.size()];
            int i = 0;
            for (Iterator itr = newUsers.iterator(); itr.hasNext();) {
                userIds[i++] = ((Long) itr.next()).longValue();
            }

            resourceManager.addNotifications(userIds, project.getId(),
                    timelineNotificationId, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }
    }

    /**
     * TODO: Document it, and its members.
     *
     */
    static class ProjectPhaseComparer implements Comparator {
        public ProjectPhaseComparer() {

        }

        public int compare(Object o1, Object o2) {
            Phase phase1 = (Phase) o1;
            Phase phase2 = (Phase) o2;
            int compareResult = phase1.calcStartDate().compareTo(phase2.calcStartDate());
            if (compareResult == 0) {
                int ranking1 = getPhaseRanking(phase1);
                int ranking2 = getPhaseRanking(phase2);
                return  ranking1 - ranking2;
            }
            return compareResult;
        }

        private int getPhaseRanking(Phase phase1) {
            String[] phaseOrder = new String[] {"Registration", "Submission", "Screening", "Review",
                    "Appeals", "Appeals Response", "Aggregation", "Aggregation Review",
                    "Final Fixes", "Final Review", "Approval"};
            for (int i = 0; i < phaseOrder.length; i++) {
                if (phaseOrder[i].equals(phase1.getPhaseType().getName())) {
                    return i;
                }
            }

            return phaseOrder.length;
        }
    }


    /**
     * This class implements <code>Comparator</code> interface and is used to sort ProjectTypes
     * in array.  It sorts Project Types by their name, in ascending order.
     */
    static class ProjectTypeComparer implements Comparator {

        /**
         * This method compares its two arguments for order. This method expects that type of
         * objects passed as arguments is <code>ProjectType</code>.
         * <p>
         * This method implements the <code>compare</code> method from the
         * <code>Comparator</code> interface.
         * </p>
         *
         * @return a negative integer, zero, or a positive integer as the first argument is less
         *         than, equal to, or greater than the second respectively.
         * @param o1
         *            the first object to be compared.
         * @param o2
         *            the second object to be compared.
         */
        public int compare(Object o1, Object o2) {
            ProjectType pt1 = (ProjectType)o1;
            ProjectType pt2 = (ProjectType)o2;

            return pt1.getName().compareTo(pt2.getName());
        }
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
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward listProjects(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {

/* TODO: Remove all these logging entries from this method
        Date currentDate = new Date();
*/
        //logger.log(Level.ERROR, "entering listProjects" + dateFormat.format(currentDate));
        //logger.log(Level.ERROR, "gathering user roles" + dateFormat.format(new Date()));
        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);
        //logger.log(Level.ERROR, "got user roles" + dateFormat.format(new Date()));

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
        if (scope.equalsIgnoreCase("inactive") &&
                !AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECTS_INACTIVE_PERM_NAME)) {
            return mapping.findForward("all");
        }

        //currentDate = new Date();
        //logger.log(Level.ERROR, "obtaining projectmanager " + dateFormat.format(currentDate));

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager(request);
        // This variable will specify the index of active tab on the JSP page
        int activeTab;
        Filter projectsFilter = null;
        //currentDate = new Date();
        //logger.log(Level.ERROR, "got projectmanager " + dateFormat.format(currentDate));

        // Determine projects displayed and index of the active tab
        // based on the value of the "scope" parameter
        if (scope.equalsIgnoreCase("my")) {
            activeTab = 1;
        } else if (scope.equalsIgnoreCase("inactive")) {
            projectsFilter = ProjectFilterUtility.buildStatusNameEqualFilter("Inactive");
            activeTab = 4;
        } else {
            // Create filter to select only active projects
            Filter filterStatus = ProjectFilterUtility.buildStatusNameEqualFilter("Active");

            if (!AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
                // Create filters to select only public projects
                Filter filterPublicName = ProjectFilterUtility.buildProjectPropertyNameEqualFilter("Public");
                Filter filterPublicValue = ProjectFilterUtility.buildProjectPropertyValueEqualFilter("Yes");
                // Build final filter
                projectsFilter =
                    new AndFilter(Arrays.asList(new Filter[] {filterPublicName, filterPublicValue, filterStatus}));
            } else {
                // Only Global Managers can see private projects in All Projects list
                projectsFilter = filterStatus;
            }

            // Specify the index of the active tab
            activeTab = 2;
        }

        // Pass the index of the active tab into request
        request.setAttribute("projectTabIndex", new Integer(activeTab));

        // Get all project types defined in the database (e.g. Assembly, Component, etc.)
        ProjectType[] projectTypes = manager.getAllProjectTypes();
        // Sort project types by their names in ascending order
        Arrays.sort(projectTypes, new ProjectTypeComparer());
        // Get all project categories defined in the database (e.g. Design, Security, etc.)
        ProjectCategory[] projectCategories = manager.getAllProjectCategories();

        request.setAttribute("projectTypes", projectTypes);
        request.setAttribute("projectCategories", projectCategories);

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

        //currentDate = new Date();
        //logger.log(Level.ERROR, "fetching listProjects" + dateFormat.format(currentDate));
        // Fetch projects from the database. These projects will require further grouping
        Project[] ungroupedProjects = (projectsFilter != null) ? manager.searchProjects(projectsFilter) :
                manager.getUserProjects(AuthorizationHelper.getLoggedInUserId(request));
        //currentDate = new Date();
        //logger.log(Level.ERROR, "done fetching listProjects" + dateFormat.format(currentDate));
        Resource[] allMyResources = null;
/*****************************************************************************************************************************************/
        if (ungroupedProjects.length != 0 && AuthorizationHelper.isUserLoggedIn(request) && myProjects) {
            Filter filterExtIDname = ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID");
            Filter filterExtIDvalue = ResourceFilterBuilder.createExtensionPropertyValueFilter(
                    String.valueOf(AuthorizationHelper.getLoggedInUserId(request)));

            List projectFilters = new ArrayList();

            for (int i = 0; i < ungroupedProjects.length; ++i) {
                projectFilters.add(new Long(ungroupedProjects[i].getId()));
            }
        //currentDate = new Date();
        //logger.log(Level.ERROR, "got project filters: " +  ungroupedProjects.length +" - " + dateFormat.format(currentDate));

            Filter filterProjects = new InFilter(ResourceFilterBuilder.PROJECT_ID_FIELD_NAME, projectFilters);

            Filter filter = new AndFilter(Arrays.asList(
                    new Filter[] {filterExtIDname, filterExtIDvalue, filterProjects}));

        //currentDate = new Date();
        //logger.log(Level.ERROR, "begin searchResources" + dateFormat.format(currentDate));

            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager(request);
            // Get all "My" resources for the list of projects
            allMyResources = resMgr.searchResources(filter);
        //currentDate = new Date();
        //logger.log(Level.ERROR, "end searchResources" + dateFormat.format(currentDate));

        }
        //currentDate = new Date();
//logger.log(Level.ERROR, "get phase manager" + dateFormat.format(currentDate));
        // Obtain an instance of Phase Manager
        PhaseManager phMgr = ActionsHelper.createPhaseManager(request, false);

        long[] allProjectIds = new long[ungroupedProjects.length];

        for (int i = 0; i < ungroupedProjects.length; ++i) {
            allProjectIds[i] = ungroupedProjects[i].getId();
        }
        //currentDate = new Date();
        //logger.log(Level.ERROR, "getting phases phase manager" + dateFormat.format(currentDate));
        com.topcoder.project.phases.Project[] phProjects = phMgr.getPhases(allProjectIds);
        //currentDate = new Date();
        //logger.log(Level.ERROR, "got phases phase manager" + dateFormat.format(currentDate));
/*****************************************************************************************************************************************/
        //currentDate = new Date();
        //logger.log(Level.ERROR, "get message resources" + dateFormat.format(currentDate));
        // Message Resources to be used for this request
        MessageResources messages = getResources(request);
        //currentDate = new Date();
        //logger.log(Level.ERROR, "got message resources" + dateFormat.format(currentDate));

        for (int i = 0; i < projectCategories.length; ++i) {
            // Count number of projects in this category
            for (int j = 0; j < ungroupedProjects.length; ++j) {
                if (ungroupedProjects[j].getProjectCategory().getId() == projectCategories[i].getId()) {
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

            // No need to collect any Resources or Roles
            // if the list of projects is not just "My" Projects
            Resource[][] myRss = (myProjects) ? new Resource[categoryCounts[i]][] : null;
            String[] rols = (myProjects) ? new String[categoryCounts[i]] : null;

            if (categoryCounts[i] != 0) {
                // Counter of projects currently added to this category
                int counter = 0;
                // Copy ungrouped projects into group of this category
                for (int j = 0; j < ungroupedProjects.length; ++j) {
                    // Skip projects that are not in this category
                    // (they'll be processed later, or have already been processed)
                    if (ungroupedProjects[j].getProjectCategory().getId() != projectCategories[i].getId()) {
                        continue;
                    }

                    // Get a project to store in current group
                    Project project = ungroupedProjects[j];
                    // Get this project's Root Catalog ID
                    String rootCatalogId = (String)project.getProperty("Root Catalog ID");

                    // Fetch Root Catalog icon's filename depending on ID of the Root Catalog
                    rcIcons[counter] = ConfigHelper.getRootCatalogIconNameSm(rootCatalogId);
                    // Fetch Root Catalog name depending depending on ID of the Root Catalog
                    rcNames[counter] = messages.getMessage(ConfigHelper.getRootCatalogAltTextKey(rootCatalogId));

                    Phase[] activePhases = null;

                    // Calculate end date of the project and get all active phases (if any)
                    if (phProjects[j] != null) {
                        preds[counter] = phProjects[j].calcEndDate();
                        activePhases = ActionsHelper.getActivePhases(phProjects[j].getAllPhases());
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

        //currentDate = new Date();
        //logger.log(Level.ERROR, "before ungrouped " + dateFormat.format(currentDate));
        if (ungroupedProjects.length != 0 && myProjects) {
            Deliverable[] allMyDeliverables = getDeliverables(
                    ActionsHelper.createDeliverableManager(request), projects, phases, myResources);

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
        //currentDate = new Date();
        //logger.log(Level.ERROR, "after count " + dateFormat.format(currentDate));

        // Place all collected data into the request as attributes
        request.setAttribute("projects", projects);
        request.setAttribute("rootCatalogIcons", rootCatalogIcons);
        request.setAttribute("rootCatalogNames", rootCatalogNames);
        request.setAttribute("phases", phases);
        request.setAttribute("phaseEndDates", phaseEndDates);
        request.setAttribute("projectEndDates", projectEndDates);
        request.setAttribute("typeCounts", typeCounts);
        request.setAttribute("categoryCounts", categoryCounts);
        request.setAttribute("totalProjectsCount", new Integer(totalProjectsCount));
        request.setAttribute("categoryIconNames", categoryIconNames);

        // If the currently displayed list is a list of "My" Projects, add some more attributes
        if (myProjects) {
            request.setAttribute("isMyProjects", new Boolean(myProjects));
            request.setAttribute("myRoles", myRoles);
            request.setAttribute("myDeliverables", myDeliverables);
        }

        //currentDate = new Date();
        //logger.log(Level.ERROR, "leaving list projects " + dateFormat.format(currentDate));

        // Signal about successfull execution of the Action
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
    private static Deliverable[] getDeliverables(
            DeliverableManager manager, Project[][] projects, Phase[][][] phases, Resource[][][] resources)
        throws DeliverablePersistenceException, SearchBuilderException, DeliverableCheckingException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(manager, "manager");
        ActionsHelper.validateParameterNotNull(projects, "projects");
        ActionsHelper.validateParameterNotNull(phases, "phases");
        ActionsHelper.validateParameterNotNull(resources, "resources");

        List projectIds = new ArrayList();
        List phaseTypeIds = new ArrayList();
        List resourceIds = new ArrayList();

        for (int i = 0; i < projects.length; ++i) {
            for (int j = 0; j < projects[i].length; ++j) {
                projectIds.add(new Long(projects[i][j].getId()));

                // Get an array of active phases for the project
                Phase[] activePhases = phases[i][j];
                // If there are no active phases, no need to select deliverables for this project
                if (activePhases == null) {
                    continue;
                }

                for (int k = 0; k < activePhases.length; ++k) {
                    phaseTypeIds.add(new Long(activePhases[k].getId()));
                }

                // Get an array of "my" resources for the active phases
                Resource[] myResources = resources[i][j];
                // If there are no "my" resources, skip the rest of the loop
                if (resources == null) {
                    continue;
                }

                for (int k = 0; k < myResources.length; ++k) {
                    resourceIds.add(new Long(myResources[k].getId()));
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
        Filter filter = new AndFilter(Arrays.asList(new Filter[] {filterProjects, filterPhases, filterResources}));

        // Get and return an array of my incomplete deliverables for all active phases.
        // These deliverables will require furter grouping
        return manager.searchDeliverables(filter, Boolean.FALSE);
    }

    /**
     * This static method returns a string that lists all the different roles the resources
     * specified by <code>resources</code> array have. The roles will be delimeted by
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

        StringBuffer buffer = new StringBuffer();
        Set rolesSet = new HashSet();

        for (int i = 0; i < resources.length; ++i) {
            // Get the name for a resource in the current iteration
            String resourceRole = resources[i].getResourceRole().getName();

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
     * deliverables will be delimeted by line-breaking tag (<code>&lt;br&#160;/&gt;</code>). If
     * any of the arrays passed to this method is <code>null</code> or emtpy, or no deliverables
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
     */
    private static String getMyDeliverablesForPhases(MessageResources messages,
            Deliverable[] deliverables, Phase[] phases, Resource[] resources, String winnerExtUserId) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(messages, "messages");

        if (deliverables == null || deliverables.length == 0 ||
                phases == null || phases.length == 0 ||
                resources == null || resources.length == 0) {
            return null; // No deliverables
        }

        StringBuffer buffer = new StringBuffer();
        Set deliverablesSet = new HashSet();

        for (int i = 0; i < deliverables.length; ++i) {
            // Get a deliverable for the current iteration
            Deliverable deliverable = deliverables[i];

            // Check if this deliverable is for any of the phases in question
            int j = 0;

            for (;j < phases.length; ++j) {
                if (deliverable.getPhase() == phases[j].getId()) {
                    break;
                }
            }
            // If this deliverable is not for any of the phases, continue the search
            if (j == phases.length) {
                continue;
            }

            for (j = 0;j < resources.length; ++j) {
                if (deliverable.getResource() == resources[j].getId()) {
                    break;
                }
            }
            // If this deliverable is not for any of the resources, continue the search
            if (j == resources.length) {
                continue;
            }

            // Skip deliverables that are not for winning submitter
            if (winnerExtUserId != null) {
                Resource resource = resources[j];

                if (resource.getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME) &&
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
}
