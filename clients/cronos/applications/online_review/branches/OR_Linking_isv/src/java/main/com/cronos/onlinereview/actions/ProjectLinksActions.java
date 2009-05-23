/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * <p>This class contains Struts Actions that are meant to deal with Project Links. There are following Actions defined
 * in this class:</p>
 *
 * <ul>
 *   <li>Edit Project Links</li>
 *   <li>Save Project Links</li>
 * </ul>
 *
 * <p>This class is thread-safe as it does not contain any mutable inner state.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 * @since Online Review Project Linking
 */
public class ProjectLinksActions extends DispatchAction {

    /**
     * <p>Constructs new <code>ProjectLinksActions</code> instance. This implementation does nothing.</p>
     */
    public ProjectLinksActions() {
    }

    /**
     * <p>Handles the request for viewing the page for editing the project links for requested project.</p>
     *
     * @param mapping an <code>ActionMapping</code> used for mapping request to this action.
     * @param form an <code>ActionForm</code> providing the form mapped to this action.
     * @param request an <code>HttpServletRequest </code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws BaseException if an unexpected error occurs.
     */
    public ActionForward editProjectLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that requested project exists and current user has a permission for editing it's details. If not
        // then notify user on error
        CorrectnessCheckResult verification
            = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request,
                                                     Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);
        if (!verification.isSuccessful()) {
            return verification.getForward();
        } else {
            // Load the lookup data and populate the form with project links data
            Project project = verification.getProject();
            request.setAttribute("project", project);
            populateProjectLinksForm(request, (LazyValidatorForm) form, project);
            loadProjectLinksEditLookups(request, project);

            return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
        }
    }

    /**
     * <p>Handles the request for saving the project links for requested project.</p>
     *
     * @param mapping an <code>ActionMapping</code> used for mapping request to this action.
     * @param form an <code>ActionForm</code> providing the form mapped to this action.
     * @param request an <code>HttpServletRequest </code> representing incoming request from the client.
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws BaseException if an unexpected error occurs.
     */
    public ActionForward saveProjectLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, true);
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }
        Project project = verification.getProject();
        long projectId = project.getId();

        // Cast the form to its actual type
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        int targetProjectCount = lazyForm.size("targetProjectId");
        int linkTypeCountCount = lazyForm.size("linkTypeId");
        if (targetProjectCount == linkTypeCountCount) {
            List<ProjectLink> links = new ArrayList<ProjectLink>();
            for (int i = 0; i < targetProjectCount; i++) {
                Long targetProjectId = (Long) lazyForm.get("targetProjectId", i);
                Long linkTypeId = (Long) lazyForm.get("linkTypeId", i);
                ProjectLinkType linkType = new ProjectLinkType(linkTypeId, null);
                ProjectLink link = new ProjectLink(linkType, projectId, null, null, targetProjectId, null, null);
                links.add(link);
            }
            ActionsHelper.setProjectLinks(projectId, links);
        } else {
            ActionsHelper.addErrorToRequest(
                request, "error.com.cronos.onlinereview.actions.saveProjectLinks.targetAndLinkTypeCountMismatch");
        }

        // Check if there are any validation errors and return appropriate forward
        if (ActionsHelper.isErrorsPresent(request)) {
            request.setAttribute("project", project);
            populateProjectLinksForm(request, lazyForm, project);
            loadProjectLinksEditLookups(request, project);
            return mapping.getInputForward();
        } else {
            return ActionsHelper.cloneForwardAndAppendToPath(mapping.findForward(Constants.SUCCESS_FORWARD_NAME),
                                                             "&pid=" + project.getId());
        }
    }

    /**
     * <p>This method loads the lookup data needed for rendering the Edit Project Links pages. Such data includes the
     * lists of available link types and existing projects. The loaded data is stored in the request attributes.</p>
     *
     * @param request the request to load the lookup data into.
     * @param project the project to take the data from.
     * @throws BaseException if any error occurs while loading the lookup data.
     */
    private void loadProjectLinksEditLookups(HttpServletRequest request, Project project) throws BaseException {
        // Get the list of projects already linked to current project and build the filter for excluding those
        // projects from the search. The project itself is also excluded
        Set<Long> excludedProjectIds = new HashSet<Long>();
        excludedProjectIds.add(project.getId());

        List<ProjectLink> projectLinks = (List<ProjectLink>) request.getAttribute("projectLinks");
        if (!projectLinks.isEmpty()) {
            for (ProjectLink link : projectLinks) {
                excludedProjectIds.add(link.getTargetProjectId());
            }
        }

        // Create filter to select only active projects
        Filter projectsFilter = null;
        Filter filterStatus = new NotFilter(ProjectFilterUtility.buildStatusNameEqualFilter("Deleted"));

        if (!AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
            // Create filters to select only public projects
            Filter filterPublicName = ProjectFilterUtility.buildProjectPropertyNameEqualFilter("Public");
            Filter filterPublicValue = ProjectFilterUtility.buildProjectPropertyValueEqualFilter("Yes");
            projectsFilter = new AndFilter(Arrays.asList(filterPublicName, filterPublicValue, filterStatus));
        } else {
            // Only Global Managers can see private projects in All Projects list
            projectsFilter = filterStatus;
        }

        // Retrieve available projects and project link types and bind them to request
        ProjectManager projectManager = ActionsHelper.createProjectManager(request);
        List<ProjectLinkType> projectLinkTypes = ActionsHelper.getProjectLinkTypes();
        Project[] projectsArray = projectManager.searchProjects(projectsFilter);
        List<Project> projects = new ArrayList<Project>();
        for (int i = 0; i < projectsArray.length; i++) {
            if (!excludedProjectIds.contains(projectsArray[i].getId())) {
                projects.add(projectsArray[i]);
            }
        }
        request.setAttribute("projects", projects);
        request.setAttribute("projectLinkTypes", projectLinkTypes);
    }

    /**
     * <p>This method populates the specified LazyValidatorForm with the values taken from the specified Project.</p>
     *
     * @param request the request to be processed.
     * @param form the form to be populated with data.
     * @param project the project to take the data from.
     * @throws BaseException if an unexpected error occurs.
     */
	private void populateProjectLinksForm(HttpServletRequest request, LazyValidatorForm form, Project project)
        throws BaseException {
        long projectId = project.getId();
        form.set("pid", new Long(projectId));
        List<ProjectLink> projectLinks = ActionsHelper.getProjectLinks(projectId, true);
        request.setAttribute("projectLinks", projectLinks);
    }
}
