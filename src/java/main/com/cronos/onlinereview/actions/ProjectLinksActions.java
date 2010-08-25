/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.link.ProjectLink;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This class contains Struts Actions that are meant to deal with Projects. There are following Actions defined in
 * this class.
 * <ul>
 * <li>Edit Project Link</li>
 * <li>Save Project Link</li>
 * </ul>
 * </p>
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * <p>
 * Change log for version 1.1: updated
 * {@link #saveProjectLinks(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} method to handle the
 * CyclicDependencyError.
 * </p>
 *
 * <p>
 * Version 1.2 (Specification Review Part 1 Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Removed dependency on <code>ContestDependencyAutomation</code> class.</li>
 *   </ol>
 * </p>
 *
 * @author BeBetter, isv
 * @version 1.2
 * @since OR Project Linking Assembly
 */
public class ProjectLinksActions extends DispatchAction {
    /**
     * <p>
     * Constant for "Deleted" status.
     * </p>
     */
    private static final String STATUS_NAME_DELETED = "Deleted";

    /**
     * <p>
     * Constant for link action of "add".
     * </p>
     */
    private static final String LINK_ACTION_ADD = "add";

    /**
     * <p>
     * Constant for link action of "delete".
     * </p>
     */
    private static final String LINK_ACTION_DELETE = "delete";

    /**
     * <p>
     * Edits project links for the given project.
     * </p>
     *
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @return the action forward
     * @throws BaseException when any error happens while processing.
     */
    public ActionForward editProjectLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request),
            request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // obtains the project link manager
        ProjectLinkManager linkManager = ActionsHelper.createProjectLinkManager(request);

        // set up project link types
        request.setAttribute("projectLinkTypes", linkManager.getAllProjectLinkTypes());

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager(request);

        // get all active projects
        Filter filterStatus = new NotFilter(ProjectFilterUtility.buildStatusNameEqualFilter(STATUS_NAME_DELETED));

        //Project[] allProjects = manager.searchProjects(filterStatus);

		Project[] allProjects = manager.getProjectsByCreateDate(90);
        // Sort fetched projects. Currently sorting is done by projects' names only, in ascending order
        Arrays.sort(allProjects, new Comparators.ProjectNameComparer());

        // set up projects except for deleted ones
        request.setAttribute("allProjects", allProjects);

        // Populate the form with project and project link properties
        populateProjectLinkForm(request, (LazyValidatorForm) form, verification.getProject());

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * <p>
     * This method populates the specified LazyValidatorForm with the values taken from the specified Project.
     * </p>
     *
     * @param request the request to be processed
     * @param form the form to be populated with data
     * @param project the project to take the data from
     *
     * @throws BaseException if any error occurs during the population
     */
    private void populateProjectLinkForm(HttpServletRequest request, LazyValidatorForm form, Project project)
        throws BaseException {
        // Populate project id
        form.set("pid", new Long(project.getId()));

        // template row
        form.set("link_dest_id_text", 0, "");
        form.set("link_dest_id", 0, new Long(-1));
        form.set("link_type_id", 0, new Long(-1));
        form.set("link_action", 0, LINK_ACTION_ADD);

        ProjectLinkManager linkManager = ActionsHelper.createProjectLinkManager(request);
        ProjectLink[] links = linkManager.getDestProjectLinks(project.getId());
        for (int i = 0; i < links.length; i++) {
            ProjectLink link = links[i];
            form.set("link_dest_id_text", i + 1, "" + link.getDestProject().getId());
            form.set("link_dest_id", i + 1, new Long(link.getDestProject().getId()));
            form.set("link_type_id", i + 1, new Long(link.getType().getId()));
            form.set("link_action", i + 1, LINK_ACTION_ADD);
        }
    }

    /**
     * <p>
     * Saves project links for the given project.
     * </p>
     *
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @return the action forward
     * @throws BaseException when any error happens while processing in TCS components.
     */
    public ActionForward saveProjectLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request),
            request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        Project project = verification.getProject();

        // save links
        LazyValidatorForm dynaForm = (LazyValidatorForm) form;
        Long[] destIds = (Long[]) dynaForm.get("link_dest_id");
        Long[] typeIds = (Long[]) dynaForm.get("link_type_id");
        String[] actions = (String[]) dynaForm.get("link_action");

        List<Long> destList = new ArrayList<Long>();
        List<Long> typeList = new ArrayList<Long>();
        for (int i = 1; i < destIds.length; i++) {
            if (!LINK_ACTION_DELETE.equals(actions[i])) {
                destList.add(destIds[i]);
                typeList.add(typeIds[i]);
            }
        }

        ProjectLinkManager linkManager = ActionsHelper.createProjectLinkManager(request);

        long[] paramDestProjectIds = new long[destList.size()];
        long[] paramTypeIds = new long[typeList.size()];
        for (int i = 0; i < paramDestProjectIds.length; i++) {
            paramDestProjectIds[i] = destList.get(i);
            paramTypeIds[i] = typeList.get(i);
        }
        linkManager.updateProjectLinks(project.getId(), paramDestProjectIds, paramTypeIds);

        // Return success forward
        return ActionsHelper.cloneForwardAndAppendToPath(mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid="
            + project.getId());
    }

}
