/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.project;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.dataaccess.DeliverableDataAccess;
import com.cronos.onlinereview.dataaccess.ProjectDataAccess;
import com.cronos.onlinereview.dataaccess.ProjectPhaseDataAccess;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.Comparators;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.EJBLibraryServicesLocator;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;

import com.opensymphony.xwork2.TextProvider;

import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.DeliverableManager;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.resource.Resource;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;

import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;

import com.topcoder.util.errorhandling.BaseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
     * Represents the key of the action error.
     */
    private static final String ACTION_ERROR_LIST_PROJECTS =
            "exception.com.cronos.onlinereview.actions.project.ListProjectsAction";

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 8036294134759200834L;

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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(true, request, this);

        if (!verification.isSuccessful()) {
            return verification.getResult();
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
            return "all";
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

                    // Get this project's Root Catalog ID
                    String rootCatalogId = (String) ungroupedProject.getProperty("Root Catalog ID");

                    // Fetch Root Catalog icon's filename depending on ID of the Root Catalog
                    rcIcons[counter] = ConfigHelper.getRootCatalogIconNameSm(rootCatalogId);
                    // Fetch Root Catalog name depending depending on ID of the Root Catalog
                    rcNames[counter] = getText(ConfigHelper.getRootCatalogAltTextKey(rootCatalogId));

                    Phase[] activePhases = null;

                    // Calculate end date of the project and get all active phases (if any)
                    if (phProjects.containsKey(ungroupedProject.getId())) {
                        com.topcoder.project.phases.Project phProject = phProjects.get(ungroupedProject.getId());
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
                        Resource[] myResources2 = ActionsHelper.getResourcesForProject(allMyResources, ungroupedProject);
                        myRss[counter] = myResources2;
                        rols[counter] = getRolesFromResources(this, myResources2);
                    }

                    // Store project in a group and increment counter
                    projs[counter] = ungroupedProject;
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
                            this, allMyDeliverables, phases[i][j], myResources[i][j], winnerIdStr);
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
        return SUCCESS;
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
            addActionError(getText(ACTION_ERROR_LIST_PROJECTS));
            throw new BaseException("It was not possible to retrieve eligibility constraints", e);
        }

        // create a set of projects where the user is a resource
        Set<Long> resourceProjects = new HashSet<Long>();

        if (allMyResources != null) {
            for (Resource r : allMyResources) {
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
     * @param winnerExtUserId
     *            an External User ID of the winning user for the project, if any. If there is no
     *            winner for the project, this parameter must be <code>null</code>.
     * @throws IllegalArgumentException
     *             if parameter <code>messages</code> is <code>null</code>.
     * @throws BaseException if an unexpected error occurs.
     */
    private static String getMyDeliverablesForPhases(TextProvider textProvider,
        Deliverable[] deliverables, Phase[] phases, Resource[] resources, String winnerExtUserId)
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
            buffer.append(textProvider.getText("Deliverable." + deliverableName.replaceAll(" ", "")));
            deliverablesSet.add(deliverableName);
        }

        return (buffer.length() != 0) ? buffer.toString() : null;
    }
}
