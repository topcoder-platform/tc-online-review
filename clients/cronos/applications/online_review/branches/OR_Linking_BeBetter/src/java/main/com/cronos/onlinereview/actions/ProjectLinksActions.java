/*
 * Copyright (C) 2006-2007 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.Arrays;

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
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.search.builder.filter.Filter;
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
 *
 * @author TCSDEVELOPER
 * @version 1.0
 * @since OR Project Linking Assembly
 */
public class ProjectLinksActions extends DispatchAction {

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
     * @throws BaseException when any error happens while processing in TCS components.
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
        ProjectLinkManager linkManager = new ProjectLinkManager();

        // set up project link types
        request.setAttribute("projectLinkTypes", linkManager.getAllProjectLinkTypes());

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager(request);

        // get all active projects
        Filter filterStatus = ProjectFilterUtility.buildStatusNameEqualFilter("Active");
        Project[] activeProjects = manager.searchProjects(filterStatus);
        // Sort fetched projects. Currently sorting is done by projects' names only, in ascending order
        Arrays.sort(activeProjects, new Comparators.ProjectNameComparer());

        // set up active projects
        request.setAttribute("activeProjects", linkManager.getAllProjectLinkTypes());

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
     */
    private void populateProjectLinkForm(HttpServletRequest request, LazyValidatorForm form, Project project) {
        // Populate project id
        form.set("pid", new Long(project.getId()));

        form.set("new_link_dest_id_text",0, "");
        form.set("new_link_dest_id",0, new Long(-1));
        form.set("new_link_type_id",0, new Long(-1));
        form.set("new_link_action",0, "add");
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

        // Return success forward
        return ActionsHelper.cloneForwardAndAppendToPath(mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid="
            + project.getId());
    }

}
