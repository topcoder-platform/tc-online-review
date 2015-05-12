/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.latedeliverables;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.dataaccess.ProjectDataAccess;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.model.CockpitProject;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.Comparators;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for viewing the late deliverables.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ViewLateDeliverablesAction extends BaseLateDeliverableAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -2916007449206407845L;

    /**
     * Represents the justified parameter name.
     */
    private static final String PARAM_JUSTIFIED = "justified";

    /**
     * Represents the handle parameter name.
     */
    private static final String PARAM_HANDLE = "handle";

    /**
     * Represents the deliverable types parameter name.
     */
    private static final String PARAM_DELIVERABLE_TYPES = "deliverable_types";

    /**
     * Represents the late deliverable type parameter name.
     */
    private static final String PARAM_LATE_DELIVERABLE_TYPE = "late_deliverable_type";

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
     * <p>A <code>String</code> providing the name of request parameter providing the ID of TC Direct Project to search
     * late deliverables for.</p>
     */
    private static final String PARAM_COCKPIT_PROJECT_ID = "tcd_project_id";

    /**
     * <p>A <code>String</code> providing the name of request parameter providing the explanation presence flag to
     * search late deliverables for.</p>
     */
    private static final String PARAM_EXPLANATION_STATUS = "explanation_status";

    /**
     * <p>A <code>String</code> providing the name of request parameter providing the response presence flag to search
     * late deliverables for.</p>
     */
    private static final String PARAM_RESPONSE_STATUS = "response_status";

    /**
     * <p>A <code>String</code> providing the name of request parameter providing the minimum date for deadline to
     * search late deliverables for.</p>
     */
    private static final String PARAM_MIN_DEADLINE = "min_deadline";

    /**
     * <p>A <code>String</code> providing the name of request parameter providing the maximum date for deadline to
     * search late deliverables for.</p>
     */
    private static final String PARAM_MAX_DEADLINE = "max_deadline";

    /**
     * <p>A <code>String</code> array listing the names for advanced search parameters.</p>
     */
    private static final String[] ADVANCED_SEARCH_PARAMS = new String[] {PARAM_COCKPIT_PROJECT_ID,
        PARAM_EXPLANATION_STATUS, PARAM_RESPONSE_STATUS, PARAM_MIN_DEADLINE, PARAM_MAX_DEADLINE, PARAM_LATE_DELIVERABLE_TYPE};

    /**
     * <p>A <code>String</code> array listing the default values for advanced search parameters.</p>
     */
    private static final String[] ADVANCED_SEARCH_PARAM_DEFAULT_VALUES = new String[] {"", "", "", "MM.DD.YYYY",
                                                                                       "MM.DD.YYYY", ""};

    /**
     * Creates a new instance of the <code>ViewLateDeliverablesAction</code> class.
     */
    public ViewLateDeliverablesAction() {
    }

    /**
     * This method is an implementation of &quot;Late Deliverables Search&quot; Struts Action, which is supposed to
     * search late deliverables based on the defined criteria from the database and pass it to the JSP page to render.
     *
     * @return &quot;success&quot; string result that forwards to the /jsp/viewLateDeliverables.jsp page (as defined in
     *         struts-config.xml file) in the case of successfully processing, &quot;notAuthorized&quot; forward in the
     *         case of user not being authorized to perform the action.
     * @throws BaseException
     *             when any error happens while processing in TCS components
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // check user login
        if (!AuthorizationHelper.isUserLoggedIn(request)) {
            // set url for login redirect.
            AuthorizationHelper.setLoginRedirect(request, false);

            return Constants.NOT_AUTHORIZED_FORWARD_NAME;
        }

        // remove login redirect
        AuthorizationHelper.removeLoginRedirect(request);

        // Place the index of the active tab into the request
        request.setAttribute("projectTabIndex", 5);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // load lookups and save to request
        loadLookups(request);

        // checks whether there is any search parameter
        request.setAttribute("hasAnyAdvancedSearchParameter", hasAnyAdvancedSearchParameter(request));
        if (hasAnySearchParameter(request)) {
            // validate the request parameters and build the corresponding filter, possibly be null.
            Filter filter = validateAndBuildFilter(request, getModel());

            if (!ActionsHelper.isErrorsPresent(request)) {
                // search the late deliverables.
                List<LateDeliverable> lateDeliverables = searchLateDeliverables(request, filter);

                // set flag to show the search result section.
                request.setAttribute("showSearchResultsSection", Boolean.TRUE);

                Map<Long, List<LateDeliverable>> groupedLateDeliverables;
                Map<Long, Project> projectMap;
                if (!lateDeliverables.isEmpty()) {
                    // group the late deliverables by project.
                    groupedLateDeliverables = groupLateDeliverablesByProject(lateDeliverables);

                    // retrieve the projects for the project ids.
                    projectMap = getProjects(groupedLateDeliverables.keySet());

                    request.setAttribute("projectMap", projectMap);
                    request.setAttribute("groupedLateDeliverables", groupedLateDeliverables);
                }

                // Return the success forward
                return Constants.SUCCESS_FORWARD_NAME;
            }
        } else {
            // first page load without doing search.
            // populate default values for each form element.
            populateDefaultFormFields(getModel());
        }

        return INPUT;
    }

    /**
     * Populates the default values for form fields.
     *
     * @param model
     *            the action form
     */
    private static void populateDefaultFormFields(DynamicModel model) {

        // select Any opinion for project categories.
        model.set(PARAM_PROJECT_ID, "Any");
        model.set(PARAM_COCKPIT_PROJECT_ID, "");
        model.set(PARAM_EXPLANATION_STATUS, "");
        model.set(PARAM_RESPONSE_STATUS, "");
        model.set(PARAM_MIN_DEADLINE, "MM.DD.YYYY");
        model.set(PARAM_MAX_DEADLINE, "MM.DD.YYYY");
        model.set(PARAM_PROJECT_CATEGORIES, new Long[] {0l});
        model.set(PARAM_PROJECT_STATUSES, new Long[] {0l});
        model.set(PARAM_DELIVERABLE_TYPES, new String[] {"0"});
        model.set(PARAM_LATE_DELIVERABLE_TYPE, "");
        model.set(PARAM_JUSTIFIED, "Any");
        model.set(PARAM_HANDLE, "Any");
    }


    /**
     * Validates the form values and build the filter for searching late deliverables.
     *
     * @param request
     *            the http request
     * @param model
     *            the action form
     * @return the built filter, possibly null, if no special filtering needed.
     * @throws BaseException
     *             if any error occurs.
     */
    private static Filter validateAndBuildFilter(HttpServletRequest request, DynamicModel model) throws BaseException {
        List<Filter> filters = new ArrayList<Filter>();

        // optional, can be null or empty.
        String projectIdStr = (String) model.get(PARAM_PROJECT_ID);

        if (projectIdStr != null && projectIdStr.trim().length() != 0 && !projectIdStr.equals("Any")) {
            try {
                Long projectId = Long.parseLong(projectIdStr);

                // check project exist
                // Obtain an instance of Project Manager
                ProjectManager projectManager = ActionsHelper.createProjectManager();

                if (projectManager.getProject(projectId) == null) {
                    ActionsHelper.addErrorToRequest(request, PARAM_PROJECT_ID,
                            "error.com.cronos.onlinereview.actions.LateDeliverablesActions.projectId.notExist");
                }

                filters.add(LateDeliverableFilterBuilder.createProjectIdFilter(projectId));
            } catch (NumberFormatException e) {
                // simply ignore the invalid parameter
            }
        }

        // Cockpit project ID - optional, can be null or empty.
        String cockpitProjectIdStr = (String) model.get(PARAM_COCKPIT_PROJECT_ID);
        if (cockpitProjectIdStr != null && cockpitProjectIdStr.trim().length() != 0) {
            try {
                Long cockpitProjectId = Long.parseLong(cockpitProjectIdStr);
                filters.add(LateDeliverableFilterBuilder.createCockpitProjectIdFilter(cockpitProjectId));
            } catch (NumberFormatException e) {
                // simply ignore the invalid parameter
            }
        }

        // Explanation status - optional, can be null or empty.
        String explanationStatusStr = (String) model.get(PARAM_EXPLANATION_STATUS);
        if (explanationStatusStr != null && explanationStatusStr.trim().length() != 0) {
            filters.add(LateDeliverableFilterBuilder.createHasExplanationFilter(Boolean.valueOf(explanationStatusStr)));
        }

        // Response status - optional, can be null or empty.
        String responseStatusStr = (String) model.get(PARAM_RESPONSE_STATUS);
        if (responseStatusStr != null && responseStatusStr.trim().length() != 0) {
            filters.add(LateDeliverableFilterBuilder.createHasResponseFilter(Boolean.valueOf(responseStatusStr)));
        }

        // Late Deliverable Type ID - optional, can be null or empty.
        String lateDeliverableTypeIdStr = (String) model.get(PARAM_LATE_DELIVERABLE_TYPE);
        if (lateDeliverableTypeIdStr != null && lateDeliverableTypeIdStr.trim().length() != 0) {
            try {
                Long lateDeliverableTypeId = Long.parseLong(lateDeliverableTypeIdStr);
                filters.add(LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(lateDeliverableTypeId));
            } catch (NumberFormatException e) {
                // simply ignore the invalid parameter
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

        // Min deadline - optional, can be null or empty.
        Date minDeadline = null;
        String minDeadlineStr = (String) model.get(PARAM_MIN_DEADLINE);
        if (minDeadlineStr != null && minDeadlineStr.trim().length() != 0 && !minDeadlineStr.equals("MM.DD.YYYY")) {
            try {
                minDeadline = dateFormat.parse(minDeadlineStr);
                filters.add(LateDeliverableFilterBuilder.createMinimumDeadlineFilter(
                    new Timestamp(minDeadline.getTime())));
            } catch (ParseException e) {
                ActionsHelper.addErrorToRequest(request, PARAM_MIN_DEADLINE,
                        "error.com.cronos.onlinereview.actions.LateDeliverablesActions.invalidDate");
            }
        }

        // Max deadline - optional, can be null or empty.
        Date maxDeadline = null;
        String maxDeadlineStr = (String) model.get(PARAM_MAX_DEADLINE);
        if (maxDeadlineStr != null && maxDeadlineStr.trim().length() != 0 && !maxDeadlineStr.equals("MM.DD.YYYY")) {
            try {
                maxDeadline = dateFormat.parse(maxDeadlineStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(maxDeadline);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                filters.add(LateDeliverableFilterBuilder.createMaximumDeadlineFilter(
                    new Timestamp(cal.getTime().getTime())));
            } catch (ParseException e) {
                ActionsHelper.addErrorToRequest(request, PARAM_MAX_DEADLINE,
                        "error.com.cronos.onlinereview.actions.LateDeliverablesActions.invalidDate");
            }
        }

        // Check if min/max deadline range is valid
        if ((minDeadline != null) && (maxDeadline != null)) {
            if (minDeadline.compareTo(maxDeadline) > 0) {
                ActionsHelper.addErrorToRequest(request, PARAM_MAX_DEADLINE,
                        "error.com.cronos.onlinereview.actions.LateDeliverablesActions.invalidDatesRange");
            }
        }


        // selected project categories
        Long[] projectCategories = (Long[]) model.get(PARAM_PROJECT_CATEGORIES);

        if (projectCategories == null || projectCategories.length == 0) {
            // this will populate the form with Any option selected.
            model.set(PARAM_PROJECT_CATEGORIES, new Long[] {0l});
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
        Long[] projectStatuses = (Long[]) model.get(PARAM_PROJECT_STATUSES);

        if (projectStatuses == null || projectStatuses.length == 0) {
            // this will populate the form with Any option selected.
            model.set(PARAM_PROJECT_STATUSES, new Long[] {0l});
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

        String[] deliverableTypes = (String[]) model.get(PARAM_DELIVERABLE_TYPES);

        if (deliverableTypes == null || deliverableTypes.length == 0) {
            // this will populate the form with Any option selected.
            model.set(PARAM_DELIVERABLE_TYPES, new String[] {"0"});
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

        String justifiedStr = (String) model.get(PARAM_JUSTIFIED);
        if (justifiedStr != null && justifiedStr.trim().length() != 0) {
            if ("Justified".equalsIgnoreCase(justifiedStr)) {
                filters.add(LateDeliverableFilterBuilder.createForgivenFilter(true));
            } else if ("Not justified".equalsIgnoreCase(justifiedStr)) {
                filters.add(LateDeliverableFilterBuilder.createForgivenFilter(false));
            }
        }

        // optional, can be null or empty
        String handle = (String) model.get(PARAM_HANDLE);

        if (handle != null && handle.trim().length() != 0 && !handle.equals("Any")) {
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
     * @param lateDeliverables
     *            the late deliverables to group
     * @return the grouped late deliverables mapped to the specific project.
     */
    private static Map<Long, List<LateDeliverable>> groupLateDeliverablesByProject(
            List<LateDeliverable> lateDeliverables) {
        Map<Long, List<LateDeliverable>> groupedLateDeliverables = new HashMap<Long, List<LateDeliverable>>();

        // we should group the late deliverables by project.
        for (LateDeliverable lateDeliverable : lateDeliverables) {
            // get the project id.
            long projectId = lateDeliverable.getProjectId();

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
     * @param projectIdSet
     *            the project id set.
     * @return the map of Projects.
     * @throws BaseException
     *             if any error occurs.
     */
    private static Map<Long, Project> getProjects(Set<Long> projectIdSet)
        throws BaseException {
        long[] projectIds = new long[projectIdSet.size()];
        int i = 0;
        for (Long projectId : projectIdSet) {
            projectIds[i++] = projectId;
        }

        // Obtain the project manager
        ProjectManager projectManager = ActionsHelper.createProjectManager();

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
        ProjectManager projectManager = ActionsHelper.createProjectManager();

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
        request.setAttribute("projectCategories", projectCategoryList.toArray(new ProjectCategory[projectCategoryList.size()]));

        // Retrieve project statuses
        ProjectStatus[] projectStatuses = projectManager.getAllProjectStatuses();

        // Store the retrieved project statues in request
        request.setAttribute("projectStatuses", projectStatuses);

        // Retrieve deliverable types and store in request
        request.setAttribute("deliverableTypes", ConfigHelper.getDeliverableTypes());

        // Retrieve available Cockpit projects and store in request
        ProjectDataAccess projectDataAccess = new ProjectDataAccess();
        List<CockpitProject> cockpitProjects;
        if (AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
            cockpitProjects = projectDataAccess.getAllCockpitProjects();
        } else {
            long currentUserId = AuthorizationHelper.getLoggedInUserId(request);
            cockpitProjects = projectDataAccess.getCockpitProjectsForUser(currentUserId);
        }
        request.setAttribute("cockpitProjects", cockpitProjects);

        LateDeliverableManager lateDeliverableManager = ActionsHelper.createLateDeliverableManager();        
        List<LateDeliverableType> lateDeliverableTypes = lateDeliverableManager.getLateDeliverableTypes();
        request.setAttribute("lateDeliverableTypes", lateDeliverableTypes.toArray(new LateDeliverableType[lateDeliverableTypes.size()]));
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
        LateDeliverableManager lateDeliverableManager = ActionsHelper.createLateDeliverableManager();

        List<LateDeliverable> lateDeliverables;
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
        Map<?,?> parameterMap = request.getParameterMap();

        return parameterMap.containsKey(PARAM_PROJECT_ID) || parameterMap.containsKey(PARAM_PROJECT_CATEGORIES)
                || parameterMap.containsKey(PARAM_PROJECT_STATUSES)
                || parameterMap.containsKey(PARAM_DELIVERABLE_TYPES) || parameterMap.containsKey(PARAM_HANDLE)
                || parameterMap.containsKey(PARAM_JUSTIFIED)
                || parameterMap.containsKey(PARAM_COCKPIT_PROJECT_ID)
                || parameterMap.containsKey(PARAM_EXPLANATION_STATUS)
                || parameterMap.containsKey(PARAM_RESPONSE_STATUS)
                || parameterMap.containsKey(PARAM_MIN_DEADLINE)
                || parameterMap.containsKey(PARAM_MAX_DEADLINE);
    }

    /**
     * Checks whether there is any advanced search parameter with non default value in request.
     *
     * @param request the http request
     * @return whether there is any search parameter in request
     */
    private static boolean hasAnyAdvancedSearchParameter(HttpServletRequest request) {
        for (int i = 0; i < ADVANCED_SEARCH_PARAMS.length; i++) {
            String advancedSearchParam = ADVANCED_SEARCH_PARAMS[i];
            String paramValue = request.getParameter(advancedSearchParam);
            if ((paramValue != null) && !ADVANCED_SEARCH_PARAM_DEFAULT_VALUES[i].equals(paramValue)) {
                return true;
            }
        }
        return false;
    }
}

