/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectlinks;

import java.util.Arrays;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.actions.DynamicModelDrivenAction;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.Comparators;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.link.ProjectLink;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for displaying editing project links page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EditProjectLinksAction extends DynamicModelDrivenAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 8941734437689160851L;

    /**
     * <p>
     * Constant for link action of "add".
     * </p>
     */
    private static final String LINK_ACTION_ADD ="add";

    /**
     * Creates a new instance of the <code>EditProjectLinksAction</code> class.
     */
    public EditProjectLinksAction() {
    }

    /**
     * <p>
     * Edits project links for the given project.
     * </p>
     *
     * @return the action result string
     * @throws BaseException when any error happens while processing.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(this,
            request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        Project project = verification.getProject();

        // obtains the project link manager
        ProjectLinkManager linkManager = ActionsHelper.createProjectLinkManager();

        // set up project link types
        request.setAttribute("projectLinkTypes", linkManager.getAllProjectLinkTypes());

        // Obtain an instance of Project Manager
        ProjectManager manager = ActionsHelper.createProjectManager();

        Project[] allProjects = null;
        if (project.getTcDirectProjectId() > 0) {
            Filter filter = ProjectFilterUtility.buildTCDirectProjectIDEqualFilter(project.getTcDirectProjectId());
            allProjects = manager.searchProjects(filter);

            // Sort fetched projects. Currently sorting is done by projects' names only, in ascending order
            Arrays.sort(allProjects, new Comparators.ProjectNameComparer());
        } else {
            allProjects = new Project[0];
        }

        // set up projects except for deleted ones
        request.setAttribute("allProjects", allProjects);

        // Populate the form with project and project link properties
        populateProjectLinkForm(getModel(), verification.getProject());

        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * <p>
     * This method populates the specified LazyValidatorForm with the values taken from the specified Project.
     * </p>
     *
     * @param model the model to be populated with data
     * @param project the project to take the data from
     *
     * @throws BaseException if any error occurs during the population
     */
    private void populateProjectLinkForm(DynamicModel model, Project project)
        throws BaseException {
        // Populate project id
        model.set("pid", project.getId());

        // template row
        model.set("link_dest_id_text", 0, "");
        model.set("link_dest_id", 0, (long) -1);
        model.set("link_type_id", 0, (long) -1);
        model.set("link_action", 0, LINK_ACTION_ADD);

        ProjectLinkManager linkManager = ActionsHelper.createProjectLinkManager();
        ProjectLink[] links = linkManager.getDestProjectLinks(project.getId());
        for (int i = 0; i < links.length; i++) {
            ProjectLink link = links[i];
            model.set("link_dest_id_text", i + 1, "" + link.getDestProject().getId());
            model.set("link_dest_id", i + 1, link.getDestProject().getId());
            model.set("link_type_id", i + 1, link.getType().getId());
            model.set("link_action", i + 1, LINK_ACTION_ADD);
        }
    }
}

