/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.topcoder.management.phase.ContestDependencyAutomation;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseStatusEnum;
import com.topcoder.management.project.link.ProjectLinkType;
import com.topcoder.management.project.link.ProjectLinkCycleException;
import com.topcoder.project.phases.Phase;
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
 * @author BeBetter, isv
 * @version 1.1
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

        // Build the mapping from ID to ProjectLinkType for all existing project link types
        ProjectLinkType[] linkTypes = linkManager.getAllProjectLinkTypes();
        Map<Long, ProjectLinkType> linkTypesMap = new HashMap<Long, ProjectLinkType>();
        for (ProjectLinkType type : linkTypes) {
            linkTypesMap.put(type.getId(), type);
        }

        // Analyze the list of links, find the links to new parent projects and determine the most
        // recent end time for Final Review phase for those new projects. Only for parent projects linked with
        // allow_overlap flag set to false
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(request, false);
        Date maxNewParentFinalReviewEndDate = null;
        for (int i = 0; i < paramTypeIds.length; i++) {
            long linkTypeId = paramTypeIds[i];
            ProjectLinkType linkType = linkTypesMap.get(linkTypeId);
            if (!linkType.isAllowOverlap()) {
                long parentProjectId = paramDestProjectIds[i];
                com.topcoder.project.phases.Project parentProject = phaseManager.getPhases(parentProjectId);
                Phase lastPhase = ActionsHelper.getLastPhase(parentProject.getAllPhases());
                Date lastPhaseEndDate;
                if (lastPhase.getPhaseStatus().getId() == PhaseStatusEnum.SCHEDULED.getId()) {
                    lastPhaseEndDate = lastPhase.getScheduledEndDate();
                } else if (lastPhase.getPhaseStatus().getId() == PhaseStatusEnum.OPEN.getId()) {
                    lastPhaseEndDate = lastPhase.getScheduledEndDate();
                } else {
                    lastPhaseEndDate = lastPhase.getActualEndDate();
                }
                if ((maxNewParentFinalReviewEndDate == null)
                    || (maxNewParentFinalReviewEndDate.compareTo(lastPhaseEndDate) < 0)) {
                    maxNewParentFinalReviewEndDate = lastPhaseEndDate;
                }
            }
        }

        try {
            linkManager.updateProjectLinks(project.getId(), paramDestProjectIds, paramTypeIds);

            // Adjust this project's timelines if necessary in case there were new parent projects linked and current
            // project is not started yet
            if (maxNewParentFinalReviewEndDate != null) {
                Date newStartDate = new Date(maxNewParentFinalReviewEndDate.getTime() + 24 * 60 * 60 * 1000L);

                boolean thereAreAffectedPhases = false;
                com.topcoder.project.phases.Project phasesProject = phaseManager.getPhases(project.getId());
                Phase[] initialPhases = phasesProject.getInitialPhases();
                for (Phase phase : initialPhases) {
                    if (phase.getPhaseStatus().getId() == PhaseStatusEnum.SCHEDULED.getId()) {
                        if (phase.getScheduledStartDate().compareTo(maxNewParentFinalReviewEndDate) <= 0) {
                            thereAreAffectedPhases = true;
                            phase.setScheduledStartDate(newStartDate);
                            phase.setScheduledEndDate(new Date(newStartDate.getTime() + phase.getLength()));
                            phase.getProject().setStartDate(newStartDate);
                        }
                    }
                }

                if (thereAreAffectedPhases) {
                    phasesProject.setStartDate(newStartDate);
                    ActionsHelper.recalculateScheduledDates(phasesProject.getAllPhases());

                    String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
                    ContestDependencyAutomation auto
                        = new ContestDependencyAutomation(phaseManager,
                                                          ActionsHelper.createProjectManager(request),
                                                          linkManager);
                    ActionsHelper.adjustDependentProjects(phasesProject, phaseManager, auto, operator);
                    phaseManager.updatePhases(phasesProject, operator);
                }
            }
        } catch (ProjectLinkCycleException e) {
            LoggingHelper.logException(e);
            request.setAttribute("errorTitle", messages.getMessage("Error.Title.General"));
            request.setAttribute("errorMessage", e.getMessage());
            return mapping.findForward(Constants.USER_ERRROR_FORWARD_NAME);
        }

        // Return success forward
        return ActionsHelper.cloneForwardAndAppendToPath(mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid="
            + project.getId());
    }

}
