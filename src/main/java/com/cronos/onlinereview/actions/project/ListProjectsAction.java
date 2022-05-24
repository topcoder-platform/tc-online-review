/*
 * Copyright (C) 2013-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.project;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.dataaccess.DeliverableDataAccess;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.LoggingHelper;
import com.opensymphony.xwork2.TextProvider;
import com.topcoder.onlinereview.component.deliverable.Deliverable;
import com.topcoder.onlinereview.component.deliverable.DeliverableCheckingException;
import com.topcoder.onlinereview.component.deliverable.DeliverableManager;
import com.topcoder.onlinereview.component.deliverable.DeliverablePersistenceException;
import com.topcoder.onlinereview.component.deliverable.Submission;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.project.management.ProjectStatus;
import com.topcoder.onlinereview.component.project.management.UserProjectCategory;
import com.topcoder.onlinereview.component.project.management.UserProjectType;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseManager;
import com.topcoder.onlinereview.component.resource.Resource;
import com.topcoder.onlinereview.component.search.SearchBuilderException;
import com.topcoder.onlinereview.component.search.filter.AndFilter;
import com.topcoder.onlinereview.component.search.filter.Filter;
import com.topcoder.onlinereview.component.search.filter.InFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is the struts action class which is used for listing all projects.
 * <p>
 * Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ListProjectsAction extends BaseProjectAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 8036294134759200834L;

    /**
     * Represents the amount of projects to be showed per page
     */
    private int projectPerpage = 100;

    @Autowired
    private DeliverableDataAccess deliverableDataAccess;

    /**
     * Default constructor.
     */
    public ListProjectsAction() {
    }

    /**
     * This method is an implementation of &quot;List Projects&quot; Struts Action defined for this
     * assembly, which is supposed to fetch list of projects from the database and pass it to the
     * JSP page for subsequent presentation to the end user.
     *
     * @return &quot;success&quot; result, which forwards to the /jsp/listProjects.jsp page (as
     *         defined in struts.xml file).
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        // Remove redirect-after-login attribute (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        LoggingHelper.logAction(request);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Retrieve the value of "scope" parameter
        String scope = request.getParameter("scope");

        // Verify that "scope" parameter is specified and is not empty
        if (scope == null || scope.trim().length() == 0) {
            // Set default value for "scope" parameter, if previous condition has not been met
            scope = "my";
        }
        scope = scope.toLowerCase();

        boolean isUserLoggedIn = AuthorizationHelper.isUserLoggedIn(request);
        // If the user is trying to access pages he doesn't have permission to view,
        // redirect him to scope-all page, where public projects are listed
        if (scope.equals("my") && !isUserLoggedIn) {
            return "all";
        }

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager();
        // This variable will specify the index of active tab on the JSP page
        int activeTab;

        // Determine projects displayed and index of the active tab
        // based on the value of the "scope" parameter
        if (scope.equals("my")) {
            activeTab = 1;
        } else if (scope.equals("draft")) {
            activeTab = 4;
        } else {
            activeTab = 2;
            scope = "all";
        }
        request.setAttribute("scope", scope);
        // Pass the index of the active tab into request
        request.setAttribute("projectTabIndex", activeTab);

        long userId = AuthorizationHelper.getLoggedInUserId(request);
        boolean hasManagerRole = false;
        if (isUserLoggedIn) {
            hasManagerRole = AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME);
        }
        ProjectStatus activeStatus = new ProjectStatus(1, "Active");
        ProjectStatus draftStatus = new ProjectStatus(2, "Draft");
        List<UserProjectType> userProjectTypes;
        if (activeTab == 1) {
            userProjectTypes = manager.countUserProjects(userId, activeStatus, true, hasManagerRole);
        } else if (activeTab == 2) {
            if (isUserLoggedIn) {
                userProjectTypes = manager.countUserProjects(userId, activeStatus, false, hasManagerRole);
            } else {
                userProjectTypes = manager.countUserProjects(null, activeStatus, false, hasManagerRole);
            }
        } else {
            if (isUserLoggedIn) {
                userProjectTypes = manager.countUserProjects(userId, draftStatus, false, hasManagerRole);
            } else {
                userProjectTypes = manager.countUserProjects(null, draftStatus, false, hasManagerRole);
            }
        }

        request.setAttribute("userProjectTypes", userProjectTypes);

        String selectedCategoryParam = request.getParameter("category");
        Long selectedCategoryId = null;
        String categoryName = "";
        int totalProjectCount = 0;
        try {
            selectedCategoryId = Long.parseLong(selectedCategoryParam);
        } catch (Exception e) {
        }
        if (selectedCategoryId != null) {
            for (UserProjectType projectType : userProjectTypes) {
                if (totalProjectCount > 0)
                    break;
                for (UserProjectCategory projectCategory : projectType.getCategories()) {
                    if (selectedCategoryId.equals(projectCategory.getId())) {
                        totalProjectCount = projectCategory.getCount();
                        categoryName = projectCategory.getName();
                        break;
                    }
                }
            }
        }
        if (totalProjectCount == 0 && !userProjectTypes.isEmpty()) {
            UserProjectCategory defaultCategory = userProjectTypes.get(0).getCategories().get(0);
            selectedCategoryId = defaultCategory.getId();
            totalProjectCount = defaultCategory.getCount();
            categoryName = defaultCategory.getName();
        }
        request.setAttribute("totalProjectCount", totalProjectCount);

        if (totalProjectCount == 0) {
            request.setAttribute("projectCount", 0);
            return SUCCESS;
        }
        request.setAttribute("selectedCategoryId", selectedCategoryId);
        // pagination parameter
        String pageParameter = request.getParameter("page");
        Integer currentPage = null;
        try {
            currentPage = Integer.parseInt(pageParameter);
        } catch (Exception e) {
        }
        if (currentPage == null || currentPage < 1 || (currentPage - 1) * projectPerpage > totalProjectCount) {
            currentPage = 1;
        }
        int lastPage = (int) Math.ceil(totalProjectCount / (projectPerpage * 1.0));
        int showingProjectsFrom = (currentPage - 1) * projectPerpage + 1;
        int showingProjectsTo = Math.min(currentPage * projectPerpage, totalProjectCount);
        int paginationStart = Math.max(currentPage - 2, 1);
        int paginationEnd = Math.min(currentPage < 3 ? 5 : currentPage + 2, lastPage);
        request.setAttribute("showingProjectsFrom", showingProjectsFrom);
        request.setAttribute("showingProjectsTo", showingProjectsTo);
        request.setAttribute("page", currentPage);
        request.setAttribute("perPage", projectPerpage);
        request.setAttribute("lastPage", lastPage);
        request.setAttribute("paginationStart", paginationStart);
        request.setAttribute("paginationEnd", paginationEnd);

        Project[] projects;

        if (activeTab == 1) {
            // user projects
            projects = manager.getAllProjects(userId, activeStatus, currentPage, projectPerpage, selectedCategoryId,
                    true, hasManagerRole);
        } else if (activeTab == 2) {
            if (isUserLoggedIn) {
                projects = manager.getAllProjects(userId, activeStatus, currentPage, projectPerpage, selectedCategoryId,
                        false, hasManagerRole);
            } else {
                projects = manager.getAllProjects(null, activeStatus, currentPage, projectPerpage, selectedCategoryId,
                        false, hasManagerRole);
            }
        } else {
            if (isUserLoggedIn) {
                projects = manager.getAllProjects(userId, draftStatus, currentPage, projectPerpage, selectedCategoryId,
                        false, hasManagerRole);
            } else {
                projects = manager.getAllProjects(null, draftStatus, currentPage, projectPerpage, selectedCategoryId,
                        false, hasManagerRole);
            }
        }
        request.setAttribute("projectCount", projects.length);

        String categoryIconName = ConfigHelper.getProjectCategoryIconNameSm(categoryName);

        // This is to signify whether "My" Projects list is displayed, or any other
        // type of Projects List. Some columns are present only in "My" Projects List
        boolean myProjects = scope.equals("my");

        String[] rootCatalogIcons = new String[projects.length];
        String[] rootCatalogNames = new String[projects.length];
        Phase[][] phases = new Phase[projects.length][];
        Date[] phaseEndDates = new Date[projects.length];
        Date[] projectEndDates = new Date[projects.length];

        // The following will only be non-null for the list of "My" Projects
        Resource[][] myResources = (myProjects) ? new Resource[projects.length][] : null;
        String[] myRoles = (myProjects) ? new String[projects.length] : null;
        String[] myDeliverables = (myProjects) ? new String[projects.length] : null;

        Long[] projectIds = Arrays.stream(projects).map(x -> x.getId()).collect(Collectors.toList())
                .toArray(new Long[projects.length]);
        Resource[] allMyResources = null;
        if (projects.length != 0 && isUserLoggedIn) {
            if (activeTab == 1) { // My projects
                allMyResources = ActionsHelper.createResourceManager().getResourcesByProjects(projectIds, userId);
            }
        }

        // Obtain an instance of Phase Manager
        PhaseManager phMgr = ActionsHelper.createPhaseManager(false);
        com.topcoder.onlinereview.component.project.phase.Project[] phaseProjects = phMgr.getPhases(projectIds);
        Map<Long, com.topcoder.onlinereview.component.project.phase.Project> phProjects = new HashMap<>(
                phaseProjects.length);
        for (com.topcoder.onlinereview.component.project.phase.Project phPr : phaseProjects) {
            phProjects.put(phPr.getId(), phPr);
        }

        int counter = 0;
        // Copy ungrouped projects into group of this category
        for (Project project : projects) {
            // Get this project's Root Catalog ID
            String rootCatalogId = (String) project.getProperty("Root Catalog ID");

            // Fetch Root Catalog icon's filename depending on ID of the Root Catalog
            rootCatalogIcons[counter] = ConfigHelper.getRootCatalogIconNameSm(rootCatalogId);
            // Fetch Root Catalog name depending depending on ID of the Root Catalog
            rootCatalogNames[counter] = getText(ConfigHelper.getRootCatalogAltTextKey(rootCatalogId));

            Phase[] activePhases = null;

            // Calculate end date of the project and get all active phases (if any)
            if (phProjects.containsKey(project.getId())) {
                com.topcoder.onlinereview.component.project.phase.Project phProject = phProjects.get(project.getId());
                projectEndDates[counter] = phProject.calcEndDate();
                activePhases = ActionsHelper.getActivePhases(phProject.getAllPhases());
                phaseEndDates[counter] = null;
            }

            // Get currently open phase end calculate its end date
            if (activePhases != null && activePhases.length != 0) {
                phases[counter] = activePhases;
                phaseEndDates[counter] = activePhases[0].getScheduledEndDate();
            }

            // Retrieve information about my roles, and my current unfinished deliverables
            if (myProjects) {
                Resource[] myResources2 = ActionsHelper.getResourcesForProject(allMyResources, project);
                myResources[counter] = myResources2;
                myRoles[counter] = getRolesFromResources(this, myResources2);
            }
            ++counter;
        }

        if (projects.length != 0 && myProjects)

        {
            Deliverable[] allMyDeliverables = getDeliverables(
                    ActionsHelper.createDeliverableManager(), projects, phases, myResources);

            // Group the deliverables per projects in list
            for (int i = 0; i < projects.length; ++i) {

                Long winnerId;
                try {
                    winnerId = Long.parseLong((String) projects[i].getProperty("Winner External Reference ID"));
                } catch (NumberFormatException nfe) {
                    winnerId = null;
                }

                myDeliverables[i] = getMyDeliverablesForPhases(
                        this, allMyDeliverables, phases[i], myResources[i], winnerId);

            }
        }

        // Place all collected data into the request as attributes
        request.setAttribute("projects", projects);
        request.setAttribute("rootCatalogIcons", rootCatalogIcons);
        request.setAttribute("rootCatalogNames", rootCatalogNames);
        request.setAttribute("phases", phases);
        request.setAttribute("phaseEndDates", phaseEndDates);
        request.setAttribute("projectEndDates", projectEndDates);
        request.setAttribute("categoryIconName", categoryIconName);

        // If the currently displayed list is a list of "My" Projects, add some more
        // attributes
        if (myProjects) {
            request.setAttribute("isMyProjects", myProjects);
            request.setAttribute("myRoles", myRoles);
            request.setAttribute("myDeliverables", myDeliverables);
        }

        // Signal about successful execution of the Action
        return SUCCESS;
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
     *            found will have to be completed by one of the resources from this array.
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
    private Deliverable[] getDeliverables(DeliverableManager manager, Project[] projects, Phase[][] phases,
                                                 Resource[][] resources)
            throws DeliverablePersistenceException, SearchBuilderException, DeliverableCheckingException {
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

            projectIds.add(projects[i].getId());

            // Get an array of active phases for the project
            Phase[] activePhases = phases[i];

                // If there are no active phases, no need to select deliverables for this project
                if (activePhases == null) {
                    continue;
                }

            for (Phase activePhase : activePhases) {
                phaseTypeIds.add(activePhase.getId());
            }

            // Get an array of "my" resources for the active phases
            Resource[] myResources = resources[i];

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
     * @param testProvider the text provider to be used
     * @param resources
     *            an array of the roles to determine the names of their resource roles.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    private static String getRolesFromResources(TextProvider testProvider, Resource[] resources) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(testProvider, "testProvider");
        ActionsHelper.validateParameterNotNull(resources, "resources");

        if (resources == null || resources.length == 0) {
            return testProvider.getText("ResourceRole.Public");
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
            buffer.append(testProvider.getText("ResourceRole." + resourceRole.replaceAll(" ", "")));
            rolesSet.add(resourceRole);
        }

        return (buffer.length() != 0) ? buffer.toString() : testProvider.getText("ResourceRole.Public");
    }

    /**
     * This static method returns a string that lists all the different outstanding (i.e.
     * incomplete) deliverables the resources specified by <code>resources</code> array have. The
     * deliverables will be delimited by line-breaking tag (<code>&lt;br&#160;/&gt;</code>). If
     * any of the arrays passed to this method is <code>null</code> or empty, or no deliverables
     * have been found, this method returns empty string.
     *
     * @return a human-readable list of deliverables.
     * @param textProvider the text provider to be used
     * @param deliverables
     *            an array of deliverables to fetch outstanding deliverables (and their names) from.
     * @param phases
     *            an array of phases to look up the deliverables for.
     * @param resources
     *            an array of resources to look up the deliverables for.
     * @param winnerUserId
     *            User ID of the winning user for the project, if any. If there is no
     *            winner for the project, this parameter must be <code>null</code>.
     * @throws IllegalArgumentException
     *             if parameter <code>messages</code> is <code>null</code>.
     * @throws BaseException if an unexpected error occurs.
     */
    private static String getMyDeliverablesForPhases(TextProvider textProvider,
        Deliverable[] deliverables, Phase[] phases, Resource[] resources, Long winnerUserId)
        throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(textProvider, "textProvider");

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
            if (winnerUserId != null) {
                if (forResource.getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME) &&
                        !winnerUserId.equals(resources[j].getUserId())) {
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
            buffer.append(textProvider.getText("Deliverable." + deliverableName.replaceAll(" ", "")));
            deliverablesSet.add(deliverableName);
        }

        return (buffer.length() != 0) ? buffer.toString() : null;
    }
}
