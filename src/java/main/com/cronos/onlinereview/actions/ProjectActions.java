/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.LazyValidatorForm;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.search.builder.filter.Filter;
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

        // TODO: Complete this method
        // It probably should also retrieve the lists of available scorecards, etc.

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
        
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method populates the specified LazyValidatorForm with the values 
     * taken from the specified Project.
     *
     * @param project
     *            the project to take the data from
     * @param form
     *            the form to be populated with data
     */
    private void populateProjectForm(Project project, LazyValidatorForm form) {
        // TODO: Possibly use string constants instead of hardcoded strings

        // Populate project name
        populateProjectFormProperty(form, String.class, "project_name", project, "Project Name");

        // Populate project type
        Long projectTypeId = new Long(project.getProjectCategory().getProjectType().getId());
        if (projectTypeId != null) {
            form.set("project_type", projectTypeId);
        }

        // Populate project category
        Long projectCategoryId = new Long(project.getProjectCategory().getId());
        if (projectCategoryId != null) {
            form.set("project_category", projectCategoryId);
        }

        // Populate project eligibility
        populateProjectFormProperty(form, String.class, "eligibility", project, "Eligibility");

        // Populate project public option
        populateProjectFormProperty(form, Boolean.class, "public", project, "Public");

        // Populate project autopilot option
        populateProjectFormProperty(form, Boolean.class, "autopilot", project, "Autopilot");

        // Populate project status notification option
        populateProjectFormProperty(form, Boolean.class, "email_notifications", project, "Status Notification");

        // Populate project status notification option
        // FIXME: This property is inverse in project and form (currently)
        populateProjectFormProperty(form, Boolean.class, "no_rate_project", project, "Rated");

        // Populate project status notification option
        populateProjectFormProperty(form, Boolean.class, "timeline_notifications", project, "Timeline Notification");

        // TODO: Populate resources and phases

        // Populate project forum name
        populateProjectFormProperty(form, String.class, "forum_name", project, "Forum Name");

        // Populate project SVN module
        populateProjectFormProperty(form, String.class, "SVN_module", project, "SVN Module");
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

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager(request);

        // Retrieve project types and categories
        ProjectType[] projectTypes = manager.getAllProjectTypes();
        ProjectCategory[] projectCategories = manager.getAllProjectCategories();

        // Store the retrieved types and categories in request
        request.setAttribute("projectTypes", projectTypes);
        request.setAttribute("projectCategories", projectCategories);

        // Retrieve the project to be edited
        Project project = manager.getProject(projectId);
        // Store the retieved project in the request
        request.setAttribute("project", project);

        // Populate the form with project properties
        populateProjectForm(project, (LazyValidatorForm) form);

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

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check if the user has the permission to perform this action
        if(!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME)) {
            // If he doesn't, redirect the request to login page
            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
        }

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager(request);

        // Retrieve project types, categories and statuses
        ProjectType[] projectTypes = manager.getAllProjectTypes();
        ProjectCategory[] projectCategories = manager.getAllProjectCategories();
        ProjectStatus[] projectStatuses = manager.getAllProjectStatuses();

        // Cast the form to its actual type
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        
        // Find "Active" project status
        ProjectStatus activeStatus = ActionsHelper.findProjectStatusByName(projectStatuses, "Active");
        // Find the project category by the specified id
        ProjectCategory category = ActionsHelper.findProjectCategoryById(projectCategories, (Long) lazyForm.get("project_category"));
        // Create Project instance
        Project project = new Project(category, activeStatus);
        // TODO: What to do with project type???
        /*
         * Populate the properties of the project
         */
        // Populate project name
        project.setProperty("Project Name", lazyForm.get("project_name"));
        // Populate project version (always set to 1.0)
        project.setProperty("Project Version", "1.0");
        // Populate project root catalog id (always set to Application)
        // TODO: There should be an ability to specify different Root Catalog
        project.setProperty("Root Catalog ID", "9926572");
        // Populate project eligibility
        project.setProperty("Eligibility", lazyForm.get("eligibility"));
        // Populate project public flag
        project.setProperty("Public", lazyForm.get("public"));
        // Populate project forum name
        // project.setProperty("Forum Name", dynaActionForm.get("forum_name"));
        // FIXME: There is no Forum Name, but there is a Developer Forum ID
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
        
        
        // Create project in persistence level
        manager.createProject(project, AuthorizationHelper.getLoggedInUserId(request) + "");
        
        // Save the project phases
        Phase[] savedPhases = saveProjectPhases(request, lazyForm, project);
        
        // Save the project resources
        saveResources(request, lazyForm, project);
        
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Document it
     * 
     * @param request
     * @param lazyForm
     * @param project
     * @return
     * @throws BaseException 
     */
    private Phase[] saveProjectPhases(HttpServletRequest request, LazyValidatorForm lazyForm, Project project)
            throws BaseException {
        // TODO Auto-generated method stub

        // Obtain the instance of Phase Manager
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request);
        
        // Create new Phases Project
        // TODO: Use real values for date and workdays, not the test ones
        // TODO: Handle the situation of project being edited
        com.topcoder.project.phases.Project phProject = 
                new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays());
       
        // Get the list of all existing phases 
        Phase[] phases = phProject.getAllPhases();
        
        // Get the list of all exisitng phase types
        PhaseType[] allPhaseTypes = phaseManager.getAllPhaseTypes();
        
        // Get the array of phase types specified for each phase
        Long[] phaseTypes = (Long[]) lazyForm.get("phase_type");
        // 0-index phase is skipped as it is a "dummy" one
        for (int i = 0; i < phaseTypes.length; i++) {
            Phase phase = null;
            
            // Check what is the action to be performed with the phase
            // and obtain Phase instance in appropriate way
            String phaseAction = (String) lazyForm.get("phase_action", i);
            if ("add".equals(phaseAction)) {
                // Create new phase
                // TODO: Check if the phase duration is specified as 
                // just number of hours or as "hrs:min"
                phase = new Phase(phProject, ((Long) lazyForm.get("phase_duration", i)).longValue());
                // Add it to Phases Project
                phProject.addPhase(phase);
            }  else {
                Long phaseId = (Long) lazyForm.get("phase_id", i);
                if (phaseId.longValue() != -1) {
                    // Retrieve the phase with the specified id
                    phase = ActionsHelper.findPhaseById(phases, phaseId);
                } else {
                    // -1 value as id marks the phases that were't persisted in DB yet
                    // and so should be skipped for actions other then "add"
                    continue;
                }
            }
            
            // If action is "delete", delete the phase and proceed to the next one
            if ("delete".equals(phaseAction)) {
                phProject.removePhase(phase);
                continue;
            }
            
            /* 
             * Set phase properties
             */
            
            // Set phase type
            phase.setPhaseType(ActionsHelper.findPhaseTypeById(allPhaseTypes, phaseTypes[i]));
            
            try {
                // If phase is not started by other phase end
                if (Boolean.FALSE.equals(lazyForm.get("phase_start_by_phase"))) {
                    // Get phase start date from form
                    Date phaseStartDate = parseDatetimeFormProperties(lazyForm, i, "phase_start_date",
                            "phase_start_time", "phase_start_AMPM");
                    // Set sheduled phase start date
                    phase.setScheduledStartDate(phaseStartDate);
                } else {
                    // Create phase Dependency
                    // TODO: Complete it
                }
                
                // Get phase end date from form
                Date phaseEndDate = parseDatetimeFormProperties(lazyForm, i, "phase_end_date", "phase_end_time",
                        "phase_end_AMPM");
                // Set sheduled phase end date
                phase.setScheduledEndDate(phaseEndDate);
            } catch (ParseException e) {
                // TODO: handle exception
                // Actually will be an unreal situation when form validation is
                // configured properly
            }
            
        }
        
        // Save the phases at the persistence level
        phaseManager.updatePhases(phProject, AuthorizationHelper.getLoggedInUserId(request) + "");

        // TODO : Fix it
        return null;
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
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy hh.mm a");
        return dateFormat.parse(fullDate);
    }

    /**
     * TODO: Document it
     * 
     * @param request
     * @param lazyForm
     * @param project
     * @throws BaseException
     */
    private void saveResources(HttpServletRequest request, LazyValidatorForm lazyForm, Project project)
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
            }
            String paid = (String) lazyForm.get("resources_paid", i);
            if ("Yes".equals(paid) || "No".equals(paid)) {
                resource.setProperty("Payment Status", paid);
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
        // redirect him scope-all page, where public projects are listed
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
        // This variable will specify the indeex of active tab on the JSP page
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

        // Fetch projects from the database.  These projects will require further grouping
        Project[] ungroupedProjects = (projectsFilter != null) ? manager.searchProjects(projectsFilter) :
                manager.getUserProjects(AuthorizationHelper.getLoggedInUserId(request));

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

                    // Store project in a group and increment counter
                    projs[counter] = project;
                    ++counter;
                }
            }

            projects[i] = projs;
            rootCatalogIcons[i] = rcIcons;
            rootCatalogNames[i] = rcNames;
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
        request.setAttribute("typeCounts", typeCounts);
        request.setAttribute("categoryCounts", categoryCounts);
        request.setAttribute("totalProjectsCount", new Integer(totalProjectsCount));
        request.setAttribute("categoryIconNames", categoryIconNames);

        // Signal about successfull execution of the Action
        return mapping.findForward("success");
    }
}
