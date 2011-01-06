/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.management.phase.PhasePersistence;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.errorhandling.BaseException;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * <p>
 * This class contains Struts Actions that are meant to deal with late deliverables.
 * </p>
 * <p>
 * There are following Actions defined in this class:
 * <ul>
 * <li>Search Late Deliverables.</li>
 * </ul>
 * </p>
 * <p>
 * <strong>Thread Safety</strong>: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author FireIce
 * @version 1.0
 * @since Online Review Late Deliverables Search Assembly 1.0
 */
public class LateDeliverablesActions extends DispatchAction {

    /**
     * Represents the forgiven parameter name.
     */
    private static final String PARAM_FORGIVEN = "forgiven";

    /**
     * Represents the handle parameter name.
     */
    private static final String PARAM_HANDLE = "handle";

    /**
     * Represents the deliverable types parameter name.
     */
    private static final String PARAM_DELIVERABLE_TYPES = "deliverable_types";

    /**
     * Represents the project statuses parameter name.
     */
    private static final String PARAM_PROJECT_STATUSES = "project_statuses";

    /**
     * Represents the project categories parameter name.
     */
    private static final String PARAM_PROJECT_CATEGORIES = "project_categories";

    /**
     * Represents the project id parameter name.
     */
    private static final String PARAM_PROJECT_ID = "project_id";

    /**
     * Creates a new instance of the <code>LateDeliverablesActions</code> class.
     */
    public LateDeliverablesActions() {
    }

    /**
     * This method is an implementation of &quot;Late Deliverables Search&quot; Struts Action, which is supposed to
     * search late deliverables based on the defined criteria from the database and pass it to the JSP page to render.
     *
     * @return &quot;success&quot; forward that forwards to the /jsp/viewLateDeliverables.jsp page (as defined in
     *         struts-config.xml file) in the case of successfully processing, &quot;notAuthorized&quot; forward in the
     *         case of user not being authorized to perform the action.
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
    public ActionForward viewLateDeliverables(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws BaseException {
        // check user login
        if (!AuthorizationHelper.isUserLoggedIn(request)) {
            // set url for login redirect.
            AuthorizationHelper.setLoginRedirect(request, false);

            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
        }

        // remove login redirect
        AuthorizationHelper.removeLoginRedirect(request);

        // Place the index of the active tab into the request
        request.setAttribute("projectTabIndex", new Integer(5));

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // load lookups and save to request
        loadLookups(request);

        // checks whether there is any search parameter
        if (hasAnySearchParameter(request)) {
            // validate the request parameters and build the corresponding filter, possibly be null.
            Filter filter = validateAndBuildFilter(request, form);

            if (!ActionsHelper.isErrorsPresent(request)) {
                // search the late deliverables.
                List<LateDeliverable> lateDeliverables = searchLateDeliverables(request, filter);

                // set flag to show the search result section.
                request.setAttribute("showSearchResultsSection", Boolean.TRUE);

                Map<Long, List<LateDeliverable>> groupedLateDeliverables = new HashMap<Long, List<LateDeliverable>>();
                Map<Long, Project> projectMap = new HashMap<Long, Project>();
                if (!lateDeliverables.isEmpty()) {
                    // group the late deliverables by project.
                    groupedLateDeliverables = groupLateDeliverablesByProject(request, lateDeliverables);

                    // retrieve the projects for the project ids.
                    projectMap = getProjects(request, groupedLateDeliverables.keySet());

                    request.setAttribute("projectMap", projectMap);
                    request.setAttribute("groupedLateDeliverables", groupedLateDeliverables);
                }

                // Return the success forward
                return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
            }
        } else {
            // first page load without doing search.
            // populate default values for each form element.
            populateDefaultFormFields(form);
        }

        return mapping.getInputForward();
    }

    /**
     * Populates the default values for form fields.
     *
     * @param form
     *            the action form
     */
    private static void populateDefaultFormFields(ActionForm form) {
        LazyValidatorForm lateDeliverableSearchForm = (LazyValidatorForm) form;

        // select Any opinion for project categories.
        lateDeliverableSearchForm.set(PARAM_PROJECT_ID, "");
        lateDeliverableSearchForm.set(PARAM_PROJECT_CATEGORIES, new Long[] {0l});
        lateDeliverableSearchForm.set(PARAM_PROJECT_STATUSES, new Long[] {0l});
        lateDeliverableSearchForm.set(PARAM_DELIVERABLE_TYPES, new String[] {"0"});
        lateDeliverableSearchForm.set(PARAM_FORGIVEN, "Any");
        lateDeliverableSearchForm.set(PARAM_HANDLE, "");
    }


    /**
     * Validates the form values and build the filter for searching late deliverables.
     *
     * @param request
     *            the http request
     * @param form
     *            the action form
     * @return the built filter, possibly null, if no special filtering needed.
     * @throws BaseException
     *             if any error occurs.
     */
    private static Filter validateAndBuildFilter(HttpServletRequest request, ActionForm form) throws BaseException {
        LazyValidatorForm lateDeliverableSearchForm = (LazyValidatorForm) form;
        List<Filter> filters = new ArrayList<Filter>();

        // optional, can be null or empty.
        String projectIdStr = (String) lateDeliverableSearchForm.get(PARAM_PROJECT_ID);

        if (projectIdStr != null && projectIdStr.trim().length() != 0) {
            try {
                Long projectId = Long.parseLong(projectIdStr);

                // check project exist
                // Obtain an instance of Project Manager
                ProjectManager projectManager = ActionsHelper.createProjectManager(request);

                if (projectManager.getProject(projectId) == null) {
                    ActionsHelper.addErrorToRequest(request, PARAM_PROJECT_ID,
                            "error.com.cronos.onlinereview.actions.LateDeliverablesActions.projectId.notExist");
                }

                filters.add(LateDeliverableFilterBuilder.createProjectIdFilter(projectId));
            } catch (NumberFormatException e) {
                // simply ignore the invalid parameter
            }
        }

        // selected project categories
        Long[] projectCategories = (Long[]) lateDeliverableSearchForm.get(PARAM_PROJECT_CATEGORIES);

        if (projectCategories == null || projectCategories.length == 0) {
            // this will populate the form with Any option selected.
            lateDeliverableSearchForm.set(PARAM_PROJECT_CATEGORIES, new Long[] {0l});
        } else {
            // build filter for project categories
            // check if Any option selected
            Filter projectCategoriesFilter = null;
            for (Long projectCategory : projectCategories) {
                if (projectCategory == 0) {
                    projectCategoriesFilter = null;

                    break;
                }

                Filter filter = LateDeliverableFilterBuilder.createProjectCategoryIdFilter(projectCategory);
                if (projectCategoriesFilter == null) {
                    projectCategoriesFilter = filter;
                } else {
                    projectCategoriesFilter = new OrFilter(projectCategoriesFilter, filter);
                }
            }

            if (projectCategoriesFilter != null) {
                filters.add(projectCategoriesFilter);
            }

        }

        // selected project statuses
        Long[] projectStatuses = (Long[]) lateDeliverableSearchForm.get(PARAM_PROJECT_STATUSES);

        if (projectStatuses == null || projectStatuses.length == 0) {
            // this will populate the form with Any option selected.
            lateDeliverableSearchForm.set(PARAM_PROJECT_STATUSES, new Long[] {0l});
        } else {
            // build filter for project statuses
            // check if Any option selected
            Filter projectStatusesFilter = null;
            for (Long projectStatus : projectStatuses) {
                if (projectStatus == 0) {
                    projectStatusesFilter = null;

                    break;
                }

                Filter filter = LateDeliverableFilterBuilder.createProjectStatusIdFilter(projectStatus);
                if (projectStatusesFilter == null) {
                    projectStatusesFilter = filter;
                } else {
                    projectStatusesFilter = new OrFilter(projectStatusesFilter, filter);
                }
            }

            if (projectStatusesFilter != null) {
                filters.add(projectStatusesFilter);
            }
        }

        String[] deliverableTypes = (String[]) lateDeliverableSearchForm.get(PARAM_DELIVERABLE_TYPES);

        if (deliverableTypes == null || deliverableTypes.length == 0) {
            // this will populate the form with Any option selected.
            lateDeliverableSearchForm.set(PARAM_DELIVERABLE_TYPES, new String[] {"0"});
        } else {
            Set<Long> deliverableIds = new HashSet<Long>();

            for (String deliverableType : deliverableTypes) {
                try {
                    if (deliverableType.indexOf(',') != -1) {
                        // multiple deliverable ids.
                        String[] deliverableIdStrs = deliverableType.split(",");

                        for (String deliverableIdStr : deliverableIdStrs) {
                            deliverableIds.add(Long.parseLong(deliverableIdStr));
                        }
                    } else {
                        deliverableIds.add(Long.parseLong(deliverableType));
                    }
                } catch (NumberFormatException e) {
                    // simply ignore the current parameter
                }
            }

            // build filter for deliverable types.
            // check if Any option selected
            Filter deliverableTypesFilter = null;
            for (Long deliverableId : deliverableIds) {
                if (deliverableId == 0) {
                    deliverableTypesFilter = null;

                    break;
                }

                Filter filter = LateDeliverableFilterBuilder.createDeliverableIdFilter(deliverableId);
                if (deliverableTypesFilter == null) {
                    deliverableTypesFilter = filter;
                } else {
                    deliverableTypesFilter = new OrFilter(deliverableTypesFilter, filter);
                }
            }

            if (deliverableTypesFilter != null) {
                filters.add(deliverableTypesFilter);
            }

        }

        String forgivenStr = (String) lateDeliverableSearchForm.get(PARAM_FORGIVEN);
        if (forgivenStr != null && forgivenStr.trim().length() != 0) {
            if ("Forgiven".equalsIgnoreCase(forgivenStr)) {
                filters.add(LateDeliverableFilterBuilder.createForgivenFilter(true));
            } else if ("Not forgiven".equalsIgnoreCase(forgivenStr)) {
                filters.add(LateDeliverableFilterBuilder.createForgivenFilter(false));
            }
        }

        // optional, can be null or empty
        String handle = (String) lateDeliverableSearchForm.get(PARAM_HANDLE);

        if (handle != null && handle.trim().length() != 0) {
            // check handle existence
            // Obtain the instance of the User Retrieval
            UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);

            // Get info about user with the specified handle
            ExternalUser user = userRetrieval.retrieveUser(handle);

            // If there is no user with such handle, indicate an error
            if (user == null) {
                ActionsHelper.addErrorToRequest(request, PARAM_HANDLE,
                        "error.com.cronos.onlinereview.actions.LateDeliverablesActions.handle.notExist");
            }

            filters.add(LateDeliverableFilterBuilder.createUserHandleFilter(handle));
        }

        // filters is possible empty, which means we should retrieve all late deliverables.
        Filter lateDeliverablesFilter = null;
        if (!filters.isEmpty()) {
            lateDeliverablesFilter = new AndFilter(filters);
        }

        return lateDeliverablesFilter;
    }

    /**
     * Groups the late deliverables by project.
     *
     * @param request
     *            the http request
     * @param lateDeliverables
     *            the late deliverables to group
     * @return the grouped late deliverables mapped to the specific project.
     * @throws BaseException
     *             if any error occurs.
     */
    private static Map<Long, List<LateDeliverable>> groupLateDeliverablesByProject(HttpServletRequest request,
            List<LateDeliverable> lateDeliverables) throws BaseException {
        // Obtain the phase persistence
        PhasePersistence phasePersistence = ActionsHelper.createPhasePersistence(request);

        Map<Long, List<LateDeliverable>> groupedLateDeliverables = new HashMap<Long, List<LateDeliverable>>();

        // we should group the late deliverables by project.
        for (LateDeliverable lateDeliverable : lateDeliverables) {
            // retrieve the Phase
            Phase phase = phasePersistence.getPhase(lateDeliverable.getProjectPhaseId());

            // get the project id.
            long projectId = phase.getProject().getId();

            List<LateDeliverable> value = groupedLateDeliverables.get(projectId);

            if (value == null) {
                value = new ArrayList<LateDeliverable>();
                groupedLateDeliverables.put(projectId, value);
            }

            value.add(lateDeliverable);
        }

        // order the late deliverables.
        for (List<LateDeliverable> lateDeliverablesList : groupedLateDeliverables.values()) {
            Collections.sort(lateDeliverablesList, new Comparators.LateDeliverableComparer());
        }

        return groupedLateDeliverables;
    }

    /**
     * Retrieves the projects for the given project id set.
     *
     * @param request
     *            the http request
     * @param projectIdSet
     *            the project id set.
     * @return the map of Projects.
     * @throws BaseException
     *             if any error occurs.
     */
    private static Map<Long, Project> getProjects(HttpServletRequest request, Set<Long> projectIdSet)
        throws BaseException {
        long[] projectIds = new long[projectIdSet.size()];
        int i = 0;
        for (Long projectId : projectIdSet) {
            projectIds[i++] = projectId;
        }

        // Obtain the project manager
        ProjectManager projectManager = ActionsHelper.createProjectManager(request);

        Project[] projects = projectManager.getProjects(projectIds);

        Map<Long, Project> projectMap = new HashMap<Long, Project>();

        for (Project project : projects) {
            projectMap.put(project.getId(), project);
        }
        return projectMap;
    }

    /**
     * This method loads the lookup data needed for rendering the View Late Deliverables pages. The loaded data is
     * stored in the request attributes.
     *
     * @param request
     *            the request to load the lookup data into
     * @throws BaseException
     *             if any error occurs while loading the lookup data
     */
    private static void loadLookups(HttpServletRequest request) throws BaseException {
        // Obtain an instance of Project Manager
        ProjectManager projectManager = ActionsHelper.createProjectManager(request);

        // Retrieve project categories
        ProjectCategory[] projectCategories = projectManager.getAllProjectCategories();

        List<ProjectCategory> projectCategoryList = new ArrayList<ProjectCategory>();

        // filter out the generic project categories.
        for (ProjectCategory projectCategory : projectCategories) {
            if (!projectCategory.getProjectType().isGeneric()) {
                projectCategoryList.add(projectCategory);
            }
        }
        // Store the retrieved categories in request
        request.setAttribute("projectCategories", projectCategoryList.toArray(new ProjectCategory[0]));

        // Retrieve project statuses
        ProjectStatus[] projectStatuses = projectManager.getAllProjectStatuses();

        // Store the retrieved project statues in request
        request.setAttribute("projectStatuses", projectStatuses);

        // Retrieve deliverable types and store in request
        request.setAttribute("deliverableTypes", ConfigHelper.getDeliverableTypes());
    }

    /**
     * Searches the late deliverables based on the given filter and the role of current user. If the user has global
     * manager role, all late deliverables based on the given filter should be returned. or only restricted late
     * deliverables related to the current user and given filter should be returned.
     *
     * @param request
     *            the http request
     * @param filter
     *            the filter, can be null.
     * @return the list of matched late deliverables.
     * @throws LateDeliverableManagementException
     *             if any error occurs.
     */
    private static List<LateDeliverable> searchLateDeliverables(HttpServletRequest request, Filter filter)
        throws LateDeliverableManagementException {
        LateDeliverableManager lateDeliverableManager = ActionsHelper.createLateDeliverableManager(request);

        List<LateDeliverable> lateDeliverables = null;
        if (AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
            lateDeliverables = lateDeliverableManager.searchAllLateDeliverables(filter);
        } else {
            lateDeliverables = lateDeliverableManager.searchRestrictedLateDeliverables(filter, AuthorizationHelper
                    .getLoggedInUserId(request));
        }

        return lateDeliverables;
    }

    /**
     * Checks whether there is any search parameter in request, which means we should do search late deliverables
     * operation.
     *
     * @param request
     *            the http request
     * @return whether there is any search parameter in request
     */
    private static boolean hasAnySearchParameter(HttpServletRequest request) {
        Map parameterMap = request.getParameterMap();

        return parameterMap.containsKey(PARAM_PROJECT_ID) || parameterMap.containsKey(PARAM_PROJECT_CATEGORIES)
                || parameterMap.containsKey(PARAM_PROJECT_STATUSES)
                || parameterMap.containsKey(PARAM_DELIVERABLE_TYPES) || parameterMap.containsKey(PARAM_HANDLE)
                || parameterMap.containsKey(PARAM_FORGIVEN);
    }
}
