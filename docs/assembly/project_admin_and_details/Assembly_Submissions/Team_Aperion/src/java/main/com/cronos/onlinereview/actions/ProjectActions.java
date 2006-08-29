/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.Arrays;
import java.util.Comparator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;

import com.topcoder.management.project.ConfigurationException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;

/**
 * TODO: Write sensible description for the ProjectActions class here
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
     * This method is an implementation of "New Project" Struts Action defined for this assembly,
     * which is supposed to fetch lists of project types and categories from the database and 
     * pass it to the JSP page to use it for populating approprate drop down lists.
     *
     * @return "success" forward that forwards to the /jsp/editProject.jsp page (as defined in
     *         struts-config.xml file) in the case of successfull processing,
     *         "notAuthorized" forward in the case of user not being authorized to perform the action.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException when any error happens while processing in TCS components
     */
    public ActionForward newProject(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check if the user has the permission to perform this action
        if(!AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)) {
            // If he doesn't have, forward to login page
            return mapping.findForward("notAuthorized");
        }
        
        // Pass the index of the active tab into request
        request.setAttribute("projectTabIndex", new Integer(3));
        
        // TODO: Complete this method
        // It probably should also retrieve the lists of available scorecards, etc.

        // Create ProjectManager instance
        ProjectManager manager = new ProjectManagerImpl();
        
        // Retrieve project types and categories
        ProjectType[] projectTypes = manager.getAllProjectTypes();
        ProjectCategory[] projectCategories = manager.getAllProjectCategories();
        
        // Store the retrieved types and categories in request
        request.setAttribute("projectTypes", projectTypes);
        request.setAttribute("projectCategories", projectCategories);

        // Put the flag to request, that specifies that we are creating new project
        request.setAttribute("newProject", Boolean.TRUE);

        return mapping.findForward("success");
    }

    /**
     * This method populates the specified DynaActionForm with the values
     * taken from specified Project.
     *
     * @param project the project to take the data from
     * @param form the form to be populated with data
     */
    private void populateProjectForm(Project project, DynaActionForm form) {
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

        // TODO : Populate resources and phases

        // Populate project forum name
        populateProjectFormProperty(form, String.class, "forum_name", project, "Forum Name");

        // Populate project SVN module
        populateProjectFormProperty(form, String.class, "SVN_module", project, "SVN Module");
    }


    /**
     * This method populates as single property of the project form 
     * by the value taken from the specified Project instance.
     *
     * @param form the form to populate property of
     * @param type the type of form property to be populated
     * @param formProperty the name of form property to be populated
     * @param project the project to take the value of property of
     * @param projectProperty the name of project property to take the value of
     */
    private void populateProjectFormProperty(DynaActionForm form, Class type, String formProperty,
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
     * This method is an implementation of "Edit Project" Struts Action defined for this assembly,
     * which is supposed to fetch lists of project types and categories from the database and 
     * pass it to the JSP page to use it for populating approprate drop down lists.
     * It is also supposed to retrieve the project to be deited and to populate the
     * form with appropriate values.
     *
     * @return "success" forward that forwards to the /jsp/editProject.jsp page (as defined in
     *         struts-config.xml file) in the case of successfull processing,
     *         "notAuthorized" forward in the case of user not being authorized to perform the action.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException when any error happens while processing in TCS components
     */
    public ActionForward editProject(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        
        // TODO: Complete this method

        // Retrieve the id of project to be edited
        long projectId = Long.parseLong(request.getParameter("pid"));
        
        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, projectId);
        
        // Check if the user has the permission to perform this action
        if(!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME)) {
            // If he doesn't have, forward to login page
            return mapping.findForward("notAuthorized");
        }
        
        // Create ProjectManager instance
        ProjectManager manager = new ProjectManagerImpl();

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

        // Put the flag to request, that specifies that we aren't creating new project
        request.setAttribute("newProject", Boolean.FALSE);

        // Populate the form with project properties
        populateProjectForm(project, (DynaActionForm) form);

        return mapping.findForward("success");
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
        // We assume that the project is laways created, edit is not supported yet
        
        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);
        
        // Check if the user has the permission to perform this action
        if(!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME)) {
            // If he doesn't have, forward to login page
            return mapping.findForward("notAuthorized");
        }
        
        // Obtain an instance of Project Manager
        ProjectManager manager = new ProjectManagerImpl();
        
        // Retrieve project types, categories and statuses
        ProjectType[] projectTypes = manager.getAllProjectTypes();
        ProjectCategory[] projectCategories = manager.getAllProjectCategories();
        ProjectStatus[] projectStatuses = manager.getAllProjectStatuses();
        
        // Create a Project instance
        // TODO: The status should be "Active", not the first met
        // TODO: The category actually should also be different
        Project project = new Project(projectCategories[0], projectStatuses[0]);

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        
        // Populate some of the properties of the project
        
        // Populate project name
        project.setProperty("Project Name", dynaActionForm.get("project_name"));
        
        // Populate project eligibility
        project.setProperty("Eligibility", dynaActionForm.get("eligibility"));
        // Populate project public flag
        project.setProperty("Public", dynaActionForm.get("public"));
        // Populate project forum name
        project.setProperty("Forum Name", dynaActionForm.get("forum_name"));
        // Populate project SVN module
        project.setProperty("SVN Module", dynaActionForm.get("SVN_module"));
        
        // Create project in persistence level
        // TODO: Use real user name here
        manager.createProject(project, "admin");
        
        
        return mapping.findForward("success");        
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort ProjectTypes
     * in array.  It sorts Project Types by their name, in ascending order.
     */
    class ProjectTypeComparer implements Comparator {

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
     * This method is an implementation of "List Projects" Struts Action defined for this assembly,
     * which is supposed to fetch list of projects from the database and pass it to the JSP page for
     * subsequent presentation to the end user.
     *
     * @return "success" forward that forwards to the /jsp/listProjects.jsp page (as defined in
     *         struts-config.xml file).
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
        ProjectManager manager = new ProjectManagerImpl();
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
