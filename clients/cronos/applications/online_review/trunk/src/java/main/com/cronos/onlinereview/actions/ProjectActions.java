/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.LazyValidatorForm;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.util.errorhandling.BaseException;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardSearchBundle;
import com.topcoder.management.scorecard.data.Scorecard;

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
 * @author TCSAssemblyTeam
 * @version 1.0
 */
public class ProjectActions extends DispatchAction {
    
    /**
     * Creates a new instance of the <code>ProjectActions</code> class.
     */
    public ProjectActions() {
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

        LazyValidatorForm lazyForm = (LazyValidatorForm) form;

        // Set the JS id to start generation from
        lazyForm.set("js_current_id", new Long(0));
        
        // Populate form with some data so that resources row template
        // is rendered properly by the appropriate JSP
        lazyForm.set("resources_role", 0, new Long(-1));
        lazyForm.set("resources_id", 0, new Long(-1));
        lazyForm.set("resources_action", 0, "add");

        // Populate form with some data so that resources row template
        // is rendered properly by the appropriate JSP
        lazyForm.set("phase_id", 0, new Long(-1));
        lazyForm.set("phase_action", 0, "add");

        // Return the success forward
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request);
        // Get all phase types
        PhaseType[] phaseTypes = phaseManager.getAllPhaseTypes();
        // Place them into request as an attribute
        request.setAttribute("phaseTypes", phaseTypes);

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorecardManager = ActionsHelper.createScorecardManager(request);

        // TODO: Check if we need to filter by the project category
        // Retrieve the scorecard lists
        Scorecard[] screeningScorecards = scorecardManager.searchScorecards(
                ScorecardSearchBundle.buildTypeNameEqualFilter("Screening"), true);
        Scorecard[] reviewScorecards = scorecardManager.searchScorecards(
                ScorecardSearchBundle.buildTypeNameEqualFilter("Review"), true);
        Scorecard[] approvalScorecards = scorecardManager.searchScorecards(
                ScorecardSearchBundle.buildTypeNameEqualFilter("Client Review"), true);
        // Store them in the request
        request.setAttribute("screeningScorecards", screeningScorecards);
        request.setAttribute("reviewScorecards", reviewScorecards);
        request.setAttribute("approvalScorecards", approvalScorecards);
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
    private void populateProjectForm(HttpServletRequest request, LazyValidatorForm form, Project project) throws BaseException {
        // TODO: Possibly use string constants instead of hardcoded strings

        // Set the JS id to start generation from
        form.set("js_current_id", new Long(0));
        
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

        // Populate project eligibility
        populateProjectFormProperty(form, String.class, "eligibility", project, "Eligibility");

        // Populate project public option
        populateProjectFormProperty(form, Boolean.class, "public", project, "Public");

        // Populate project autopilot option
        populateProjectFormProperty(form, Boolean.class, "autopilot", project, "Autopilot");

        // Populate project status notification option
        populateProjectFormProperty(form, Boolean.class, "email_notifications", project, "Status Notification");

        // Populate project status notification option
        // Note, this property is inverse by its meaning in project and form
        if ("Yes".equals(project.getProperty("Rated"))) {
            form.set("no_rate_project", Boolean.FALSE);
        } else {
            form.set("no_rate_project", Boolean.TRUE);
        }

        // Populate project status notification option
        populateProjectFormProperty(form, Boolean.class, "timeline_notifications", project, "Timeline Notification");

        // Populate project forum name
        populateProjectFormProperty(form, Long.class, "forum_id", project, "Developer Forum ID");

        // Populate project SVN module
        populateProjectFormProperty(form, String.class, "SVN_module", project, "SVN Module");

        // TODO: Check whether edit or add notes?
        // Populate project notes
        populateProjectFormProperty(form, String.class, "notes", project, "Notes");

        // Populate form with some data so that resources row template
        // is rendered properly by the appropriate JSP
        form.set("resources_role", 0, new Long(-1));
        form.set("resources_id", 0, new Long(-1));
        form.set("resources_action", 0, "add");

        // Populate form with some data so that resources row template
        // is rendered properly by the appropriate JSP
        form.set("phase_id", 0, new Long(-1));
        form.set("phase_action", 0, "add");

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

            form.set("resources_name", i + 1, resources[i].getProperty("Handle"));
            if (resources[i].getProperty("Payment") != null) {
                form.set("resources_payment", i + 1, Boolean.TRUE);
                form.set("resources_payment_amount", i + 1, Long.valueOf((String) resources[i].getProperty("Payment")));
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
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request);

        // Retrive project phases
        Phase[] phases = ActionsHelper.getPhasesForProject(phaseManager, project);
        // Sort project phases
        Arrays.sort(phases, new ProjectPhaseComparer());
        

        // Populate form with phases data
        for (int i = 0; i < phases.length; ++i) {
            form.set("phase_id", i + 1, new Long(phases[i].getId()));
            form.set("phase_type", i + 1, new Long(phases[i].getPhaseType().getId()));
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

            form.set("phase_duration", i + 1, new Integer((int) (phases[i].getLength() / 3600 / 1000)));

            // Populate phase criteria
            if (phases[i].getAttribute("Scorecard ID") != null) {
                form.set("phase_scorecard_id", i + 1, Long.valueOf((String) phases[i].getAttribute("Scorecard ID")));
            }
            if (phases[i].getAttribute("Registration Number") != null) {
                form.set("phase_required_registrations", i + 1,
                        Integer.valueOf((String) phases[i].getAttribute("Registration Number")));
            }
            if (phases[i].getAttribute("Submission Number") != null) {
                form.set("phase_required_submissions", i + 1,
                        Integer.valueOf((String) phases[i].getAttribute("Submission Number")));
                form.set("manual_screening", i + 1,
                        Boolean.valueOf("Yes".equals(phases[i].getAttribute("Manual Screening"))));
            }
            if (phases[i].getAttribute("View Response During Appeals") != null) {
                form.set("phase_view_appeal_responses", i + 1,
                        Boolean.valueOf("Yes".equals(phases[i].getAttribute("View Response During Appeals"))));
            }
        }

        // Get current project phase
        Phase currentPhase = getCurrentProjectPhase(phases);
        if (currentPhase != null) {
            // Populate current phase
            form.set("current_phase", "loaded_" + currentPhase.getId());
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
        DateFormat dateFormat = new SimpleDateFormat(("MM.dd.yy hh:mm aa"));
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
        // TODO: Complete this method
        // It is actually very-very incomplete just partially demonstrates the functionality
        // We assume that the project is always being created; edit is not supported yet

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
//        ProjectType[] projectTypes = manager.getAllProjectTypes(); // This line is causing warning
        ProjectCategory[] projectCategories = manager.getAllProjectCategories();
        ProjectStatus[] projectStatuses = manager.getAllProjectStatuses();

        Project project;
        if (newProject) {
            // Find "Active" project status
            ProjectStatus activeStatus = ActionsHelper.findProjectStatusByName(projectStatuses, "Active");
            // Find the project category by the specified id
            ProjectCategory category = ActionsHelper.findProjectCategoryById(projectCategories,
                    ((Long) lazyForm.get("project_category")).longValue());
            // Create Project instance
            project = new Project(category, activeStatus);
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
            project.setProperty("Project Version", "1.0");
            // Populate project root catalog id (always set to Application)
            // TODO: There should be an ability to specify different Root Catalog
            project.setProperty("Root Catalog ID", "9926572");
            // Populate project eligibility
            project.setProperty("Eligibility", lazyForm.get("eligibility"));
            // Populate project public flag
            project.setProperty("Public", Boolean.TRUE.equals(lazyForm.get("public")) ? "Yes" : "No");
        } else {
            // Populate project status
            project.setProjectStatus(
                    ActionsHelper.findProjectStatusById(projectStatuses, ((Long) lazyForm.get("status")).longValue()));
        }
        // Populate project forum id
        project.setProperty("Developer Forum ID", lazyForm.get("forum_id"));
        // Populate project SVN module
        project.setProperty("SVN Module", lazyForm.get("SVN_module"));
        // Populate project autopilot option
        project.setProperty("Autopilot Option", Boolean.TRUE.equals(lazyForm.get("autopilot")) ? "On" : "Off");
        // Populate project status notifications option
        project.setProperty("Status Notification", Boolean.TRUE.equals(lazyForm.get("email_notifications")) ? "On" : "Off");
        // Populate project timeline notifications option
        project.setProperty("Timeline Notification", Boolean.TRUE.equals(lazyForm.get("timeline_notifications")) ? "On" : "Off");
        // Populate project rated option, note that it is inveresed
        project.setProperty("Rated", Boolean.TRUE.equals(lazyForm.get("no_rate_project")) ? "Off" : "On");

        // Populate project notes
        project.setProperty("Notes", lazyForm.get("notes"));

        // TODO: Project status change, includes additional explanation to be concatenated

        
        // Save the project phases
        // FIXME: the project it slef is also saved by the following call. Needs to be refactored
        Phase[] projectPhases = saveProjectPhases(newProject, request, lazyForm, project);
        
        // Check if there are any validation errors and return appropriate forward
        if (request.getAttribute(Globals.ERROR_KEY) != null) {
            // TODO : Check if the form is really for new project
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
        
        // Save the project resources
        saveResources(newProject, request, lazyForm, project, projectPhases);
        
        // Return success forward
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);        
    }

    /**
     * TODO: Document it
     *
     * @param projectPhases
     * @return
     */
    private Phase getCurrentProjectPhase(Phase[] projectPhases) {
        for (int i = 0; i < projectPhases.length; i++) {
            if (projectPhases[i].getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName())) {
                return projectPhases[i];
            }
        }
        return null;
    }

    /**
     * TODO: Document it
     * @param newProject
     *
     * @param request
     * @param lazyForm
     * @param project
     * @return
     * @throws BaseException
     */
    private Phase[] saveProjectPhases(boolean newProject, HttpServletRequest request, LazyValidatorForm lazyForm, Project project)
            throws BaseException {
        // TODO Auto-generated method stub

        // Obtain the instance of Phase Manager
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request);

        com.topcoder.project.phases.Project phProject;
        if (newProject) {
            // Create new Phases Project
            // TODO: Use real values for date and workdays, not the test ones
            // TODO: Handle the situation of project being edited
            phProject = new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays());
        } else {
            // Retrive the Phases Project with the id equal to the id of specified Project
            phProject = phaseManager.getPhases(project.getId());
        }

        // Get the list of all previously existing phases
        Phase[] oldPhases = phProject.getAllPhases();

        // Get the list of all existing phase types
        PhaseType[] allPhaseTypes = phaseManager.getAllPhaseTypes();

        // Create the map to store the mapping from phase JS ids to phases
        Map phasesJsMap = new HashMap();

        // Get the array of phase types specified for each phase
        Long[] phaseTypes = (Long[]) lazyForm.get("phase_type");
        // 0-index phase is skipped as it is a "dummy" one
        for (int i = 1; i < phaseTypes.length; i++) {
            Phase phase = null;

            // Check what is the action to be performed with the phase
            // and obtain Phase instance in appropriate way
            String phaseAction = (String) lazyForm.get("phase_action", i);
            if ("add".equals(phaseAction)) {
                // Create new phase
                // TODO: Check if the phase duration is specified as
                // just number of hours or as "hrs:min", also check the untis of measure
                phase = new Phase(phProject, ((Integer) lazyForm.get("phase_duration", i)).longValue() * 3600 * 1000);
                // Add it to Phases Project
                phProject.addPhase(phase);
                // Put it to the map
                phasesJsMap.put(lazyForm.get("phase_js_id", i), phase);

            }  else {
                long phaseId = ((Long) lazyForm.get("phase_id", i)).longValue();
                if (phaseId != -1) {
                    // Retrieve the phase with the specified id
                    phase = ActionsHelper.findPhaseById(oldPhases, phaseId);
                } else {
                    // -1 value as id marks the phases that were't persisted in DB yet
                    // and so should be skipped for actions other then "add"
                    continue;
                }
            }

            // If action is "update", update phase duration, put the phase to the map
            if ("update".equals(phaseAction)) {
                phase.setLength(((Integer) lazyForm.get("phase_duration", i)).longValue() * 3600 * 1000);

                // Put the phase to the map
                phasesJsMap.put(lazyForm.get("phase_js_id", i), phase);
            }

            // If action is "delete", delete the phase and proceed to the next one
            if ("delete".equals(phaseAction)) {
                phProject.removePhase(phase);
                continue;
            }

            /*
             * Set phase properties
             */

            if ("add".equals(phaseAction)) {
                // Set phase type
                phase.setPhaseType(ActionsHelper.findPhaseTypeById(allPhaseTypes, phaseTypes[i].longValue()));
                // Set phase status to "Scheduled"
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            }

            try {
                // If phase is not started by other phase end
                if (Boolean.FALSE.equals(lazyForm.get("phase_start_by_phase", i))) {
                    // Get phase start date from form
                    Date phaseStartDate = parseDatetimeFormProperties(lazyForm, i, "phase_start_date",
                            "phase_start_time", "phase_start_AMPM");
                    // TODO: Determine which of the dates should actually be set
                    // Set sheduled phase start date
                    phase.setScheduledStartDate(phaseStartDate);
                    // Set sheduled phase start date
                    phase.setFixedStartDate(phaseStartDate);
                } else {
                    // TODO: These parameters should probably be populated in some other way
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

                    // TODO: minus should probably be handled by swapping the dependency and dependant phases
                    if ("minus".equals(lazyForm.get("phase_start_plusminus", i))) {
                        unitMutiplier = -unitMutiplier;
                    }
                    long lagTime = unitMutiplier * ((Integer) lazyForm.get("phase_start_amount", i)).longValue();

                    // Create phase Dependency
                    Dependency dependency = new Dependency((Phase) phasesJsMap.get(lazyForm.get("phase_start_phase", i)),
                            phase, dependencyStart, dependantStart, lagTime);

                    if ("update".equals(phaseAction)) {
                        // Clear all the pre-existing dependencies
                        phase.clearDependencies();
                    }

                    // Add dependency to phase
                    phase.addDependency(dependency);

                    // TODO: Check how to deal with it
                    // Set scheduled start date to calculate start date
                    phase.setScheduledStartDate(phase.calcStartDate());
                }

                /*// Get phase end date from form
                Date phaseEndDate = parseDatetimeFormProperties(lazyForm, i, "phase_end_date", "phase_end_time",
                        "phase_end_AMPM");
                */
                // TODO: Set duration based on specified phase end date if needed
                // Set sheduled phase end date
                phase.setScheduledEndDate(new Date(phase.getScheduledStartDate().getTime() + phase.getLength())); //(phaseEndDate);
            } catch (ParseException e) {
                e.printStackTrace();
                // TODO: handle exception
                // Actually will be an unreal situation when form validation is
                // configured properly
            }


            // Set phase criteria
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
            Boolean manualScreening = (Boolean) lazyForm.get("phase_manual_screening", i);
            // If the manual screening flag is specified, set it
            if (manualScreening != null) {
                phase.setAttribute("Manual Screening", manualScreening.booleanValue() ? "Yes" : "No");
            }
            Boolean viewAppealResponses = (Boolean) lazyForm.get("phase_view_appeal_responses", i);
            // If the view appeal response during appeals flag is specified, set it
            if (viewAppealResponses != null) {
                phase.setAttribute("View Response During Appeals", viewAppealResponses.booleanValue() ? "Yes" : "No");
            }
        }

        Phase[] projectPhases = phProject.getAllPhases();        
        // Sort project phases
        Arrays.sort(projectPhases, new ProjectPhaseComparer());
        
        // Validate the project phases
        if (!validateProjectPhases(request, project, projectPhases)) {
            // If project phases are invalid, return immediately
            return projectPhases;
        }        


        // FIXME: Refactor it
        ProjectManager projectManager = ActionsHelper.createProjectManager(request);
        if (newProject) {
            // Create project in persistence level
            projectManager.createProject(project, AuthorizationHelper.getLoggedInUserId(request) + "");
        
            // Set the id of Phases Project to be equal to the id of appropriate Project
            phProject.setId(project.getId());
        } else {
            projectManager.updateProject(project, (String) lazyForm.get("explanation"), AuthorizationHelper.getLoggedInUserId(request) + "");
        }
        
        
        // Save the phases at the persistence level
        phaseManager.updatePhases(phProject, AuthorizationHelper.getLoggedInUserId(request) + "");
        // TODO : The following line was added just to be safe. May be unneeded as well as another one.
        projectPhases = phProject.getAllPhases();
        
        // If needed switch project current phase
        if (!newProject) {
            // Get current project phase
            Phase currentPhase = getCurrentProjectPhase(projectPhases);
            // Get new current phase id
            String newCurPhaseId = (String) lazyForm.get("current_phase");            
            // Get new current phase
            Phase newCurrentPhase = (Phase) phasesJsMap.get(newCurPhaseId);
            if (newCurrentPhase != null) {
                int i = 0;
                if (currentPhase != null) {
                    for (; i < projectPhases.length; i++) {
                        if (projectPhases[i] == currentPhase) {
                            break;
                        }
                    }
                }
                for (; i < projectPhases.length; i++) {
                    if (projectPhases[i] != newCurrentPhase) {
                        if (projectPhases[i].getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName())) {
                            /*if (phaseManager.canEnd(projectPhases[i])) {
                              */  phaseManager.end(projectPhases[i],
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                            /*} else {
                                // TODO: issue an error
                            }*/
                        } else if (projectPhases[i].getPhaseStatus().getName().equals(PhaseStatus.SCHEDULED.getName())) {
                            /*if (phaseManager.canStart(projectPhases[i])) {
                              */  phaseManager.start(projectPhases[i],
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                            /*}
                            if (phaseManager.canEnd(projectPhases[i])) {
                              */  phaseManager.end(projectPhases[i],
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                            //}
                        }
                    } else {
                        if (projectPhases[i].getPhaseStatus().getName().equals(PhaseStatus.SCHEDULED.getName())) {
                            /*if (phaseManager.canStart(projectPhases[i])) {
                              */  phaseManager.start(projectPhases[i],
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                            //}
                        }
                        break;
                    }
                }
            }
        }

        return projectPhases;
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
            ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                    new ActionMessage("error.com.cronos.onlinereview.actions.editProject.WrongBeginningPhase"));
            arePhasesValid = false;
        }
        
        // Check the phases as a whole
        for (int i = 0; i < projectPhases.length; i++) {
            if (projectPhases[i].getPhaseType().getName().equals(Constants.SUBMISSION_PHASE_NAME)) {
                // Submission should follow registration if it exists
                if (i > 0 && !projectPhases[i - 1].getPhaseType().getName().equals(Constants.REGISTRATION_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.SubmissionMustFollow"));
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.REGISTRATION_PHASE_NAME)) {
                // Registration should be followed by submission
                if (i == projectPhases.length - 1 || !projectPhases[i + 1].getPhaseType().getName().equals(Constants.SUBMISSION_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.RegistrationMustBeFollowed"));
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.REVIEW_PHASE_NAME)) {
                // Review should follow submission or screening
                if (i == 0 || (!projectPhases[i - 1].getPhaseType().getName().equals(Constants.SUBMISSION_PHASE_NAME) &&
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.SCREENING_PHASE_NAME))) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.ReviewMustFollow"));
                    arePhasesValid = false;
                }
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.APPEALS_PHASE_NAME)) {
                // Appeals should follow review
                if (i == 0 || !projectPhases[i - 1].getPhaseType().getName().equals(Constants.REVIEW_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.AppealsMustFollow"));
                    arePhasesValid = false;
                }
                // Appeals should be followed by the appeals response
                if (i == projectPhases.length - 1 || 
                        !projectPhases[i + 1].getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.AppealsMustBeFollowed"));
                    arePhasesValid = false;
                }                
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                // Appeal response should follow appeals
                if (i == 0 || !projectPhases[i - 1].getPhaseType().getName().equals(Constants.APPEALS_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.AppealsResponseMustFollow"));
                    arePhasesValid = false;
                } 
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.AGGREGATION_PHASE_NAME)) {
                // Aggregation should follow appeals response or review
                if (i == 0 || 
                        (!projectPhases[i - 1].getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME) && 
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.REVIEW_PHASE_NAME))) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.AggregationMustFollow"));
                    arePhasesValid = false;
                } 
                // Aggregation should be followed by the aggregation review
                if (i == projectPhases.length - 1 || 
                        !projectPhases[i + 1].getPhaseType().getName().equals(Constants.AGGREGATION_REVIEW_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.AggregationMustBeFollowed"));
                    arePhasesValid = false;
                }                                
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.AGGREGATION_REVIEW_PHASE_NAME)) {
                // Aggregation review should follow aggregation
                if (i == 0 || 
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.AGGREGATION_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.AggregationReviewMustFollow"));
                    arePhasesValid = false;
                }                 
            } else if (projectPhases[i].getPhaseType().getName().equals(Constants.FINAL_FIX_PHASE_NAME)) {
                // Final fix should follow either appeals response or aggregation review
                if (i == 0 || 
                        (!projectPhases[i - 1].getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME) && 
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.AGGREGATION_REVIEW_PHASE_NAME))) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.FinalFixMustFollow"));
                    arePhasesValid = false;
                } 
                // Final fix should be followed by the final review
                if (i == projectPhases.length - 1 || 
                        !projectPhases[i + 1].getPhaseType().getName().equals(Constants.FINAL_REVIEW_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.FinalFixMustBeFollowed"));
                    arePhasesValid = false;
                }                                
            }  else if (projectPhases[i].getPhaseType().getName().equals(Constants.FINAL_REVIEW_PHASE_NAME)) {
                // Final review should follow final fix
                if (i == 0 || 
                        !projectPhases[i - 1].getPhaseType().getName().equals(Constants.FINAL_FIX_PHASE_NAME)) {
                    ActionsHelper.addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, 
                            new ActionMessage("error.com.cronos.onlinereview.actions.editProject.FinalReviewMustFollow"));
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
     * @throws ParseException
     */
    private Date parseDatetimeFormProperties(LazyValidatorForm lazyForm, int propertyIndex, String dateProperty,
            String timeProperty, String ampmProperty) throws ParseException {
        // Retrieve the values of form properties
        String dateString = (String) lazyForm.get(dateProperty, propertyIndex);
        String timeString = (String) lazyForm.get(timeProperty, propertyIndex);
        String ampmString = (String) lazyForm.get(ampmProperty, propertyIndex);
        // Construct the full date/time string
        String fullDate = dateString + " " + timeString + " " + ampmString;
        // Parse the date
        // TODO: Reuse the DateFormat instance instead of creating new ones
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy hh:mm aa");
        return dateFormat.parse(fullDate);
    }

    /**
     * TODO: Document it
     * @param newProject
     *
     * @param request
     * @param lazyForm
     * @param project
     * @throws BaseException
     */
    private void saveResources(boolean newProject, HttpServletRequest request, LazyValidatorForm lazyForm, Project project, Phase[] projectPhases)
            throws BaseException {

        // Obtain the instance of the User Retrieval
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);

        // Obtain the instance of the Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);

        // Get all types of resource roles
        ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();

        // Get the array of resource names
        String[] resourceNames = (String[]) lazyForm.get("resources_name");
        // 0-index resource is skipped as it is a "dummy" one
        for (int i = 1; i < resourceNames.length; i++) {
            Resource resource;

            // Check what is the action to be performed with the resource
            // and obtain Resource instance in appropriate way
            String resourceAction = (String) lazyForm.get("resources_action", i);
            if ("add".equals(resourceAction)) {
                // Create new resource
                resource = new Resource();
            }  else {
                Long resourceId = (Long) lazyForm.get("resources_id", i);
                if (resourceId.longValue() != -1) {
                    // Retrieve the resource with the specified id
                    resource = resourceManager.getResource(resourceId.longValue());
                } else {
                    // -1 value as id marks the resources that were't persisted in DB yet
                    // and so should be skipped for actions other then "add"
                    continue;
                }
            }

            // If action is "delete", delete the resource and proceed to the next one
            if ("delete".equals(resourceAction)) {
                resourceManager.removeResource(resource,  AuthorizationHelper.getLoggedInUserId(request) + "");
                continue;
            }

            // Set resource properties
            resource.setProject(new Long(project.getId()));
            for (int j = 0; j < resourceRoles.length; j++) {
                if (resourceRoles[j].getId() == ((Long) lazyForm.get("resources_role", i)).longValue()) {
                    resource.setResourceRole(resourceRoles[j]);
                    break;
                }
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
                // TODO: Need to support several same typed phases
                Phase phase = ActionsHelper.findPhaseByPhaseTypeId(projectPhases, phaseTypeId.longValue());
                resource.setPhase(new Long(phase.getId()));
            }

            // Get info about user with the specified handle
            // TODO: Check if user exists
            ExternalUser user = userRetrieval.retrieveUser(resourceNames[i]);

            // Set resource properties copied from external user
            resource.setProperty("External Reference ID", new Long(user.getId()));
            resource.setProperty("Email", user.getEmail());

            String resourceRole = resource.getResourceRole().getName();
            // If resource is a submitter, we need to store appropriate rating and reliability
            if (resourceRole.equals("Submitter")) {
                if (project.getProjectCategory().getName().equals("Design")) {
                    resource.setProperty("Rating", user.getDesignRating());
                    resource.setProperty("Reliability", user.getDesignReliability());
                } else if (project.getProjectCategory().getName().equals("Development")) {
                    resource.setProperty("Rating", user.getDevRating());
                    resource.setProperty("Reliability", user.getDevReliability());
                }
            }
            // If resource is a submitter, screener or reviewer, store registration date
            if (resourceRole.equals("Submitter") || resourceRole.equals("Screener") ||
                    resourceRole.equals("Reviewer")) {
                resource.setProperty("Registration Date", new Date());
            }

            // Save the resource in the persistence level
            resourceManager.updateResource(resource, AuthorizationHelper.getLoggedInUserId(request) + "");
        }
    }
    
    /**
     * TODO: Document it, and members.
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
     */
    public ActionForward listProjects(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Retrieve the value of "scope" parameter
        String scope = request.getParameter("scope");
        // Verify that "scope" parameter is specified and is not empty
        if (scope == null || scope.trim().length() == 0) {
            // Set default value for "scope" parameter, if previous condition has not been met
            scope = "all";
        }

        // If the user is trying to access pages he doesn't have permission to view,
        // redirect him to scope-all page, where public projects are listed
        if (scope.equalsIgnoreCase("my") &&
                !AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_PROJECTS_PERM_NAME)) {
            return mapping.findForward("all");
        }
        if (scope.equalsIgnoreCase("inactive") &&
                !AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECTS_INACTIVE_PERM_NAME)) {
            return mapping.findForward("all");
        }

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager(request);
        // This variable will specify the index of active tab on the JSP page
        int activeTab;
        Filter projectsFilter = null;

        // Determine projects displayed and index of the active tab
        // based on the value of the "scope" parameter
        if (scope.equalsIgnoreCase("my")) {
            activeTab = 1;
        } else if (scope.equalsIgnoreCase("inactive")) {
            projectsFilter = ProjectFilterUtility.buildStatusNameEqualFilter("Inactive");
            activeTab = 4;
        } else {
            projectsFilter = ProjectFilterUtility.buildStatusNameEqualFilter("Active");
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

        Project[][] projects = new Project[projectCategories.length][];
        String[][] rootCatalogIcons = new String[projectCategories.length][];
        String[][] rootCatalogNames = new String[projectCategories.length][];
        String[][] myRoles = new String[projectCategories.length][];
        Phase[][] phases = new Phase[projectCategories.length][];
        Date[][] phaseEndDates = new Date[projectCategories.length][];
        Date[][] projectEndDates = new Date[projectCategories.length][];

        // Fetch projects from the database.  These projects will require further grouping
        Project[] ungroupedProjects = (projectsFilter != null) ? manager.searchProjects(projectsFilter) :
                manager.getUserProjects(AuthorizationHelper.getLoggedInUserId(request));

        Resource[] allMyResources = null;

        if (ungroupedProjects.length != 0 && AuthorizationHelper.isUserLoggedIn(request)) {
            Filter filterExtIDname = ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID");
            Filter filterExtIDvalue = ResourceFilterBuilder.createExtensionPropertyValueFilter(
                    String.valueOf(AuthorizationHelper.getLoggedInUserId(request)));

            List projectFilters = new ArrayList();

            for (int i = 0; i < ungroupedProjects.length; ++i) {
                projectFilters.add(new Long(ungroupedProjects[i].getId()));
            }

            Filter filterProjects = new InFilter(ResourceFilterBuilder.PROJECT_ID_FIELD_NAME, projectFilters);

            Filter filter = new AndFilter(Arrays.asList(
                    new Filter[] {filterExtIDname, filterExtIDvalue, filterProjects}));
            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager(request);
            allMyResources = resMgr.searchResources(filter);
        }

        // Obtain an instance of Phase Manager
        PhaseManager phMgr = ActionsHelper.createPhaseManager(request);

        long[] allProjectIds = new long[ungroupedProjects.length];

        for (int i = 0; i < ungroupedProjects.length; ++i) {
            allProjectIds[i] = ungroupedProjects[i].getId();
        }

        com.topcoder.project.phases.Project[] phProjects = phMgr.getPhases(allProjectIds);

        // Message Resources to be used for this request
        MessageResources messages = getResources(request);

        for (int i = 0; i < projectCategories.length; ++i) {
            // Count projects of this category
            for (int j = 0; j < ungroupedProjects.length; ++j) {
                if (ungroupedProjects[j].getProjectCategory().getId() == projectCategories[i].getId()) {
                    ++categoryCounts[i];
                }
            }

            // Now, as the exact count of projects in this category is known,
            // it is possible to initialize arrays
            Project[] projs = new Project[categoryCounts[i]];
            String[] rcIcons = new String[categoryCounts[i]];
            String[] rcNames = new String[categoryCounts[i]];
            String[] rols = new String[categoryCounts[i]];
            Phase[] phass = new Phase[categoryCounts[i]];
            Date[] pheds = new Date[categoryCounts[i]];
            Date[] preds = new Date[categoryCounts[i]];

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

                    if (allMyResources != null) {
                        for (int k = 0; k < allMyResources.length; ++k) {
                            // Get a Resource for the current iteration
                            Resource resource = allMyResources[k];
                            if (resource.getProject() == null || resource.getProject().longValue() != project.getId()) {
                                continue;
                            }
                            rols[counter] = messages.getMessage("ResourceRole." +
                                    resource.getResourceRole().getName().replaceAll(" ", ""));
                        }
                    }

                    if (rols[counter] == null || rols[counter].length() == 0) {
                        rols[counter] = messages.getMessage("ResourceRole.Public");
                    }

                    if (phProjects[j] != null) {
                        preds[counter] = phProjects[j].calcEndDate();
                        Phase activePhase = ActionsHelper.getPhase(phProjects[j].getAllPhases(), true, null);
                        phass[counter] = activePhase;
                        pheds[counter] = (activePhase != null) ? activePhase.calcEndDate() : null;
                    }

                    // Store project in a group and increment counter
                    projs[counter] = project;
                    ++counter;
                }
            }

            projects[i] = projs;
            rootCatalogIcons[i] = rcIcons;
            rootCatalogNames[i] = rcNames;
            myRoles[i] = rols;
            phases[i] = phass;
            phaseEndDates[i] = pheds;
            projectEndDates[i] = preds;
            // Fetch Project Category icon's filename depending on the name of the current category
            categoryIconNames[i] = ConfigHelper.getProjectCategoryIconNameSm(projectCategories[i].getName());
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

        request.setAttribute("projects", projects);
        request.setAttribute("rootCatalogIcons", rootCatalogIcons);
        request.setAttribute("rootCatalogNames", rootCatalogNames);
        request.setAttribute("myRoles", myRoles);
        request.setAttribute("phases", phases);
        request.setAttribute("phaseEndDates", phaseEndDates);
        request.setAttribute("projectEndDates", projectEndDates);
        request.setAttribute("typeCounts", typeCounts);
        request.setAttribute("categoryCounts", categoryCounts);
        request.setAttribute("totalProjectsCount", new Integer(totalProjectsCount));
        request.setAttribute("categoryIconNames", categoryIconNames);

        // Signal about successfull execution of the Action
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }
}
