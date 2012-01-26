/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cronos.onlinereview.actions.AuthorizationHelper;
import com.cronos.onlinereview.dataaccess.ProjectDataAccess;
import com.cronos.onlinereview.functions.Functions;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.model.CockpitProject;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
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
 * <p>
 * Version 1.1 (Online Review Late Deliverables Edit Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated the action logic to support advanced parameters for searching the late deliverables: cockpit project
 *     ID, explanation and response statuses, minimum and maximum deadlines for late deliverables.</li>
 *     <li>Added {@link #editLateDeliverable(org.apache.struts.action.ActionMapping,org.apache.struts.action.ActionForm,javax.servlet.http.HttpServletRequest)}
 *     method.</li>
 *     <li>Added {@link #saveLateDeliverable(org.apache.struts.action.ActionMapping,org.apache.struts.action.ActionForm,javax.servlet.http.HttpServletRequest)} and
 *     accompanying methods.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2 (Online Review Status Validation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Methods adjusted for new signatures of create managers methods from ActionsHelper.</li>
 *   </ol>
 * </p>
 *
 * @author FireIce, isv, rac_
 * @version 1.2
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
     *
     * @since 1.1
     */
    private static final String PARAM_COCKPIT_PROJECT_ID = "tcd_project_id";

    /**
     * <p>A <code>String</code> providing the name of request parameter providing the explanation presence flag to
     * search late deliverables for.</p>
     *
     * @since 1.1
     */
    private static final String PARAM_EXPLANATION_STATUS = "explanation_status";

    /**
     * <p>A <code>String</code> providing the name of request parameter providing the response presence flag to search
     * late deliverables for.</p>
     *
     * @since 1.1
     */
    private static final String PARAM_RESPONSE_STATUS = "response_status";

    /**
     * <p>A <code>String</code> providing the name of request parameter providing the minimum date for deadline to
     * search late deliverables for.</p>
     *
     * @since 1.1
     */
    private static final String PARAM_MIN_DEADLINE = "min_deadline";

    /**
     * <p>A <code>String</code> providing the name of request parameter providing the maximum date for deadline to
     * search late deliverables for.</p>
     *
     * @since 1.1
     */
    private static final String PARAM_MAX_DEADLINE = "max_deadline";

    /**
     * <p>A <code>String</code> array listing the names for advanced search parameters.</p>
     *
     * @since 1.1
     */
    private static final String[] ADVANCED_SEARCH_PARAMS = new String[] {PARAM_COCKPIT_PROJECT_ID,
        PARAM_EXPLANATION_STATUS, PARAM_RESPONSE_STATUS, PARAM_MIN_DEADLINE, PARAM_MAX_DEADLINE, PARAM_LATE_DELIVERABLE_TYPE};

    /**
     * <p>A <code>String</code> array listing the default values for advanced search parameters.</p>
     *
     * @since 1.1
     */
    private static final String[] ADVANCED_SEARCH_PARAM_DEFAULT_VALUES = new String[] {"", "", "", "MM.DD.YYYY",
                                                                                       "MM.DD.YYYY", ""};

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
        LoggingHelper.logAction(request);

        // check user login
        if (!AuthorizationHelper.isUserLoggedIn(request)) {
            // set url for login redirect.
            AuthorizationHelper.setLoginRedirect(request, false);

            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
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
            Filter filter = validateAndBuildFilter(request, form);

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
     * <p>This method is an implementation of &quot;Edit Late Deliverable&quot; Struts Action, which is supposed to
     * show the form for editing the selected late deliverable.</p>
     *
     * @return &quot;success&quot; forward that forwards to the /jsp/editLateDeliverable.jsp page (as defined in
     *         struts-config.xml file) in the case of successfully processing, &quot;notAuthorized&quot; forward in the
     *         case of user not being authorized to perform the action.
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException when any error happens while processing in TCS components
     */
    public ActionForward editLateDeliverable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                             HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        // check user login
        if (!AuthorizationHelper.isUserLoggedIn(request)) {
            AuthorizationHelper.setLoginRedirect(request, false);
            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
        }

        // remove login redirect
        AuthorizationHelper.removeLoginRedirect(request);

        // Get requested late deliverable ID and raise an exception if it is not found
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        Long lateDeliverableId = (Long) lazyForm.get("late_deliverable_id");
        LateDeliverable lateDeliverable;
        if (lateDeliverableId <= 0 || ((lateDeliverable = getLateDeliverable(lateDeliverableId)) == null)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_LATE_DELIVERABLE_PERM_NAME, "Error.UnknownLateDeliverable", null);
        } else {
            // Gather the roles the user has for current request and project
            AuthorizationHelper.gatherUserRoles(request, lateDeliverable.getProjectId());

            // Check if user has a permission to view the late deliverable. The users can view late deliverables
            // either if the they are granted View Late Deliverable permission or if they are the source of the late
            // deliverable
            long lateDeliverableUserId = getLateDeliverableUserId(lateDeliverable);
            long currentUserId = AuthorizationHelper.getLoggedInUserId(request);
            boolean isLateDeliverableOwner = (currentUserId == lateDeliverableUserId);

            boolean canViewLateDeliverable
                = AuthorizationHelper.hasUserPermission(request, Constants.VIEW_LATE_DELIVERABLE_PERM_NAME);

            if (!canViewLateDeliverable && !isLateDeliverableOwner) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.VIEW_LATE_DELIVERABLE_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }

            // Populate form properties with data from late deliverable
            lazyForm.set("forgiven", lateDeliverable.isForgiven());
            lazyForm.set("explanation", lateDeliverable.getExplanation());
            lazyForm.set("response", lateDeliverable.getResponse());
            lazyForm.set("late_deliverable_id", lateDeliverable.getId());

            // Set additional request attributes to be consumed by JSP
            setEditLateDeliverableRequest(request, lateDeliverable, isLateDeliverableOwner);

            return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
        }

    }

    /**
     * <p>This method is an implementation of &quot;Edit Late Deliverable&quot; Struts Action, which is supposed to
     * show the form for editing the selected late deliverable.</p>
     *
     * @return &quot;success&quot; forward that forwards to the /jsp/editLateDeliverable.jsp page (as defined in
     *         struts-config.xml file) in the case of successfully processing, &quot;notAuthorized&quot; forward in the
     *         case of user not being authorized to perform the action.
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws Exception when any error happens while processing in TCS components
     */
    public ActionForward saveLateDeliverable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        LoggingHelper.logAction(request);

        // check user login
        if (!AuthorizationHelper.isUserLoggedIn(request)) {
            AuthorizationHelper.setLoginRedirect(request, false);
            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
        }

        // remove login redirect
        AuthorizationHelper.removeLoginRedirect(request);

        // Get requested late deliverable ID and raise an exception if it is not found
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        Long lateDeliverableId = (Long) lazyForm.get("late_deliverable_id");
        LateDeliverable lateDeliverable;
        if (lateDeliverableId <= 0 || ((lateDeliverable = getLateDeliverable(lateDeliverableId)) == null)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_LATE_DELIVERABLE_PERM_NAME, "Error.UnknownLateDeliverable", null);
        } else {
            // Gather the roles the user has for current request and project
            AuthorizationHelper.gatherUserRoles(request, lateDeliverable.getProjectId());

            // Check if user has a permission to edit the late deliverable. The users can edit late deliverables
            // either if the they are granted Edit Late Deliverable permission or if they are the source of the late
            // deliverable
            long lateDeliverableUserId = getLateDeliverableUserId(lateDeliverable);
            long currentUserId = AuthorizationHelper.getLoggedInUserId(request);
            boolean isLateDeliverableOwner = (currentUserId == lateDeliverableUserId);

            boolean canEditLateDeliverable
                = AuthorizationHelper.hasUserPermission(request, Constants.EDIT_LATE_DELIVERABLE_PERM_NAME);

            if (!canEditLateDeliverable && !isLateDeliverableOwner) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.EDIT_LATE_DELIVERABLE_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }

            // Validate that desired data is submitted based on user's role
            String newResponse = null;
            String newExplanation = null;
            boolean newForgiven = false;
            if (isLateDeliverableOwner) {
                newExplanation = validateText(request, lazyForm, "explanation");
            } else if (canEditLateDeliverable) {
                newForgiven = (Boolean) lazyForm.get("forgiven");
                // Response may be skipped if it has been already set in DB or if there is no explanation set so far
                if ((lateDeliverable.getExplanation() != null) && (lateDeliverable.getResponse() == null)) {
                    newResponse = validateText(request, lazyForm, "response");
                }
            }

            // If there are validation errors then display them; otherwise update the late deliverable
            if (ActionsHelper.isErrorsPresent(request)) {
                setEditLateDeliverableRequest(request, lateDeliverable, isLateDeliverableOwner);
                return mapping.getInputForward();
            } else {
                boolean dataHasBeenChanged = isLateDeliverableOwner
                                             || canEditLateDeliverable
                                                && ((lateDeliverable.isForgiven() != newForgiven)
                                                    || (newResponse != null));

                // Re-read the late deliverable from DB again to get most recent data for late deliverable before
                // updating
                lateDeliverable = getLateDeliverable(lateDeliverableId);
                boolean alreadySet = false;
                boolean lateForExplanation = ActionsHelper.explanationDeadline(lateDeliverable).compareTo(new Date()) < 0;
                if (isLateDeliverableOwner) {
                    if (!lateForExplanation) {
                        if (lateDeliverable.getExplanation() == null) {
                            lateDeliverable.setExplanation(newExplanation);
                            lateDeliverable.setExplanationDate(new Date());
                        } else {
                            alreadySet = true;
                        }
                    }
                } else if (canEditLateDeliverable) {
                    if (lateDeliverable.isForgiven() != newForgiven) {
                        lateDeliverable.setForgiven(newForgiven);
                    }
                    boolean newResponseSubmitted = (lazyForm.get("response") != null
                                                    && ((String) lazyForm.get("response")).length() > 0);
                    if (newResponseSubmitted) {
                        if (lateDeliverable.getResponse() == null) {
                            lateDeliverable.setResponse(newResponse);
                            lateDeliverable.setResponseUser("" + currentUserId);
                            lateDeliverable.setResponseDate(new Date());
                        } else {
                            alreadySet = true;
                        }
                    }
                }

                if (alreadySet) {
                    // Raise an error since explanation/response has been already set for this late deliverable
                    return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                            Constants.EDIT_LATE_DELIVERABLE_PERM_NAME, "Error.LateDeliverableAlreadyUpdated",
                            Boolean.FALSE);
                } else if (isLateDeliverableOwner && lateForExplanation) {
                    // Raise an error since time given for the explanation has ended
                    return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                            Constants.EDIT_LATE_DELIVERABLE_PERM_NAME, "Error.LateDeliverableExplanationLate",
                            Boolean.FALSE);
                } else {

                    // Update the affected properties of the deliverable
                    LateDeliverableManager lateDeliverableManager = ActionsHelper.createLateDeliverableManager();
                    lateDeliverableManager.update(lateDeliverable);

                    // Send notification emails in case any data of late deliverable has been changed
                    if (dataHasBeenChanged) {
                        if (isLateDeliverableOwner) {
                            sendEmailToManagers(lateDeliverable, request);
                        } else if (canEditLateDeliverable) {
                            sendEmailToLateMember(lateDeliverable, request);
                        }
                    }

                    return ActionsHelper.cloneForwardAndAppendToPath(mapping.findForward(Constants.SUCCESS_FORWARD_NAME),
                                                                     "&late_deliverable_id=" + lateDeliverable.getId());
                }
            }
        }
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
        lateDeliverableSearchForm.set(PARAM_PROJECT_ID, "Any");
        lateDeliverableSearchForm.set(PARAM_COCKPIT_PROJECT_ID, "");
        lateDeliverableSearchForm.set(PARAM_EXPLANATION_STATUS, "");
        lateDeliverableSearchForm.set(PARAM_RESPONSE_STATUS, "");
        lateDeliverableSearchForm.set(PARAM_MIN_DEADLINE, "MM.DD.YYYY");
        lateDeliverableSearchForm.set(PARAM_MAX_DEADLINE, "MM.DD.YYYY");
        lateDeliverableSearchForm.set(PARAM_PROJECT_CATEGORIES, new Long[] {0l});
        lateDeliverableSearchForm.set(PARAM_PROJECT_STATUSES, new Long[] {0l});
        lateDeliverableSearchForm.set(PARAM_DELIVERABLE_TYPES, new String[] {"0"});
        lateDeliverableSearchForm.set(PARAM_LATE_DELIVERABLE_TYPE, "");
        lateDeliverableSearchForm.set(PARAM_FORGIVEN, "Any");
        lateDeliverableSearchForm.set(PARAM_HANDLE, "Any");
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
        String cockpitProjectIdStr = (String) lateDeliverableSearchForm.get(PARAM_COCKPIT_PROJECT_ID);
        if (cockpitProjectIdStr != null && cockpitProjectIdStr.trim().length() != 0) {
            try {
                Long cockpitProjectId = Long.parseLong(cockpitProjectIdStr);
                filters.add(LateDeliverableFilterBuilder.createCockpitProjectIdFilter(cockpitProjectId));
            } catch (NumberFormatException e) {
                // simply ignore the invalid parameter
            }
        }

        // Explanation status - optional, can be null or empty.
        String explanationStatusStr = (String) lateDeliverableSearchForm.get(PARAM_EXPLANATION_STATUS);
        if (explanationStatusStr != null && explanationStatusStr.trim().length() != 0) {
            filters.add(LateDeliverableFilterBuilder.createHasExplanationFilter(Boolean.valueOf(explanationStatusStr)));
        }

        // Response status - optional, can be null or empty.
        String responseStatusStr = (String) lateDeliverableSearchForm.get(PARAM_RESPONSE_STATUS);
        if (responseStatusStr != null && responseStatusStr.trim().length() != 0) {
            filters.add(LateDeliverableFilterBuilder.createHasResponseFilter(Boolean.valueOf(responseStatusStr)));
        }

        // Late Deliverable Type ID - optional, can be null or empty.
        String lateDeliverableTypeIdStr = (String) lateDeliverableSearchForm.get(PARAM_LATE_DELIVERABLE_TYPE);
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
        String minDeadlineStr = (String) lateDeliverableSearchForm.get(PARAM_MIN_DEADLINE);
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
        String maxDeadlineStr = (String) lateDeliverableSearchForm.get(PARAM_MAX_DEADLINE);
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
        Map parameterMap = request.getParameterMap();

        return parameterMap.containsKey(PARAM_PROJECT_ID) || parameterMap.containsKey(PARAM_PROJECT_CATEGORIES)
                || parameterMap.containsKey(PARAM_PROJECT_STATUSES)
                || parameterMap.containsKey(PARAM_DELIVERABLE_TYPES) || parameterMap.containsKey(PARAM_HANDLE)
                || parameterMap.containsKey(PARAM_FORGIVEN)
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

    /**
     * <p>Gets the late deliverable matching the specified ID.</p>
     *
     * @param lateDeliverableId a <code>long</code> providing the ID of late deliverable to retrieve.
     * @return a <code>LateDeliverable</code> matching the specified ID or <code>null</code> if requested late
     *         deliverable could not be found.
     * @throws LateDeliverableManagementException if any error occurs.
     */
    private static LateDeliverable getLateDeliverable(long lateDeliverableId)
        throws LateDeliverableManagementException {
        LateDeliverableManager lateDeliverableManager = ActionsHelper.createLateDeliverableManager();
        return lateDeliverableManager.retrieve(lateDeliverableId);
    }

    /**
     * <p>Validates that specified property of specified form is not null and is not empty. If validation fails then
     * binds the error message to request to indicate on validation error.</p>
     *
     * @param request the http request.
     * @param form the action form.
     * @param propertyName a name of form property to validate.
     * @return a value of specified property of the form if it is set or null of the property is not set.
     */
    private static String validateText(HttpServletRequest request, LazyValidatorForm form, String propertyName) {
        String value = (String) form.get(propertyName);
        if ((value == null) || (value.trim().length() == 0)) {
            ActionsHelper.addErrorToRequest(request, propertyName,
                "error.com.cronos.onlinereview.actions.LateDeliverablesActions.emptyText");
            return null;
        } else {
            return value;
        }
    }

    /**
     * <p>Gets the ID for a user associated with the resource assigned to specified late deliverable.</p>
     *
     * @param lateDeliverable a <code>LateDeliverable</code> providing the data for late deliverable.
     * @return a <code>long</code> providing the ID of a user who is associated with the resource set for specified
     *         late deliverable.
     * @throws ResourcePersistenceException if an unexpected error occurs.
     */
    private long getLateDeliverableUserId(LateDeliverable lateDeliverable)
        throws ResourcePersistenceException {
        ResourceManager resourceManager = ActionsHelper.createResourceManager();
        Resource lateDeliverableResource = resourceManager.getResource(lateDeliverable.getResourceId());
        return Long.parseLong(String.valueOf(lateDeliverableResource.getProperty("External Reference ID")));
    }

    /**
     * <p>Sets the request with various attributes for specified late deliverable to be consumed by the JSP.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @param lateDeliverable a <code>LateDeliverable</code> providing the data for late deliverable.
     * @param lateDeliverableOwner <code>true</code> if current user is late member associated with late deliverable;
     *        <code>false</code> otherwise.
     * @throws PersistenceException if an unexpected error occurs.
     */
    private void setEditLateDeliverableRequest(HttpServletRequest request, LateDeliverable lateDeliverable,
                                               boolean lateDeliverableOwner) throws PersistenceException {
        boolean canEditLateDeliverables
            = AuthorizationHelper.hasUserPermission(request, Constants.EDIT_LATE_DELIVERABLE_PERM_NAME);

        request.setAttribute("lateDeliverable", lateDeliverable);
        request.setAttribute("project",
                             ActionsHelper.createProjectManager().getProject(lateDeliverable.getProjectId()));

        boolean explanationEditable = lateDeliverableOwner && lateDeliverable.getExplanation() == null &&
            ActionsHelper.explanationDeadline(lateDeliverable).compareTo(new Date()) > 0;
        request.setAttribute("isExplanationEditable", explanationEditable);

        request.setAttribute("isResponseEditable",
                             canEditLateDeliverables && !lateDeliverableOwner
                             && (lateDeliverable.getResponse() == null)
                             && (lateDeliverable.getExplanation() != null));
        request.setAttribute("isForgivenEditable", canEditLateDeliverables && !lateDeliverableOwner);
        request.setAttribute("isFormSubmittable",
                             (canEditLateDeliverables && !lateDeliverableOwner) || explanationEditable);
    }


    /**
     * This method sets the values of the nodes.
     *
     * @param nodes the nodes in template
     * @param project a <code>Project</code> providing details for current project.
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @throws BaseException if an unexpected error occurs.
     */
    private void setNodes(Node[] nodes, Project project, LateDeliverable lateDeliverable, HttpServletRequest request)
        throws BaseException {
        for (Node node : nodes) {
            if (node instanceof Field) {
                Field field = (Field) node;
                field.setValue(getFieldValue(field.getName(), project, lateDeliverable, request));
            } else if (node instanceof Condition) {
                Condition condition = ((Condition) node);
                condition.setValue(getFieldValue(condition.getName(), project, lateDeliverable, request));

                setNodes(condition.getSubNodes().getNodes(), project, lateDeliverable, request);
            }
        }
    }

    /**
     * <p>Gets field value.</p>
     *
     * @param fieldName the Name of the field in template
     * @param project a <code>Project</code> providing details for current project.
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @return a <code>String</code> providing the value of the template field
     * @throws BaseException if an unexpected error occurs.
     */
    private String getFieldValue(String fieldName, Project project, LateDeliverable lateDeliverable,
	                             HttpServletRequest request) throws BaseException {
        if ("PROJECT_NAME".equals(fieldName)) {
            return "" + project.getProperty("Project Name");
        } else if ("PROJECT_VERSION".equals(fieldName)) {
            return "" + project.getProperty("Project Version");
        } else if ("LATE_DELIVERABLE_LINK".equals(fieldName)) {
            return ConfigHelper.getLateDeliverableBaseURL() + lateDeliverable.getId();
        } else if ("OR_LINK".equals(fieldName)) {
            return ConfigHelper.getProjectDetailsBaseURL() + project.getId();
        } else if ("FORGIVEN".equals(fieldName)) {
            if (lateDeliverable.isForgiven()) {
                return "Yes";
            } else {
                return "No";
            }
        } else if ("EXPLANATION".equals(fieldName)) {
            String explanation = lateDeliverable.getExplanation();
            if (explanation == null) {
                return "N/A";
            } else {
                return Functions.htmlEncode(explanation);
            }
        } else if ("RESPONSE".equals(fieldName)) {
            String response = lateDeliverable.getResponse();
            if (response == null) {
                return "N/A";
            } else {
                return Functions.htmlEncode(response);
            }
        } else if ("DEADLINE".equals(fieldName)) {
            return Functions.displayDate(request, lateDeliverable.getDeadline());
        } else if ("DELIVERABLE_TYPE".equals(fieldName)) {
            return Functions.getDeliverableName(request, lateDeliverable.getDeliverableId());
        } else if ("LATE_DELIVERABLE_TYPE".equals(fieldName)) {
            return lateDeliverable.getType().getName();
        } else if ("LATE_MEMBER_HANDLE".equals(fieldName)) {
            UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
            ExternalUser lateMember = userRetrieval.retrieveUser(getLateDeliverableUserId(lateDeliverable));
            return lateMember.getHandle();
        } else if ("CURRENT_USER_HANDLE".equals(fieldName)) {
            UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
            ExternalUser currentUser = userRetrieval.retrieveUser(AuthorizationHelper.getLoggedInUserId(request));
            return currentUser.getHandle();
        }
        return "";
    }

    /**
     * <p>Sends email to specified recipients on update of specified late deliverable.</p>
     *
     * @param project a <code>Project</code> providing details for project.
     * @param recipients a <code>String</code> list providing the list of email addresses to send email message to.
     * @param ccRecipients a <code>String</code> list providing the list of email addresses to be CCed on the email message.
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param emailTemplateName a <code>String</code> referencing the template for generating the email message body.
     * @param emailSubjectTemplate a <code>String</code> providing the template for subject for the email message.
     * @param fromAddress a <code>String</code> providing the email address to send email from.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @throws Exception if an unexpected error occurs.
     */
    private void sendEmailForUsers(Project project, List<String> recipients, List<String> ccRecipients, LateDeliverable lateDeliverable,
                                   String emailTemplateName, String emailSubjectTemplate,
                                   String fromAddress, HttpServletRequest request) throws Exception {
        if (recipients.size() + ccRecipients.size() == 0) {
            return;
        }

        DocumentGenerator docGenerator = new DocumentGenerator();
        docGenerator.setDefaultTemplateSource(new FileTemplateSource());
        Template template = docGenerator.getTemplate(emailTemplateName);
        TemplateFields root = docGenerator.getFields(template);
        setNodes(root.getNodes(), project, lateDeliverable, request);

        String emailContent = docGenerator.applyTemplate(root);
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject(MessageFormat.format(emailSubjectTemplate, project.getProperty("Project Name"),
                                                project.getProperty("Project Version")));
        message.setBody(emailContent);
        message.setFromAddress(fromAddress);
        for (String recipient : recipients) {
            message.addToAddress(recipient, TCSEmailMessage.TO);
        }
        for (String ccRecipient : ccRecipients) {
            message.addToAddress(ccRecipient, TCSEmailMessage.CC);
        }
        message.setContentType("text/html");
        EmailEngine.send(message);
    }

    /**
     * <p>Sends an email to late member associated with the specified late deliverable to notify on updates of
	 * the late deliverable by one of the managers. The other managers are CCed on the email.</p>
     *
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @throws Exception if an unexpected error occurs.
     */
    private void sendEmailToLateMember(LateDeliverable lateDeliverable, HttpServletRequest request) throws Exception {
        Project project = ActionsHelper.createProjectManager().getProject(lateDeliverable.getProjectId());

        String[] recipientRoleNames = ConfigHelper.getLateDeliverableUpdateByManagerRecipientRoleNames();

        List<Long> managerUserIds = ActionsHelper.getUserIDsByRoleNames(recipientRoleNames, project.getId());

        managerUserIds.remove((long) 22719217); // "Components" dummy user
        managerUserIds.remove((long) 22770213); // "Applications" dummy user
        managerUserIds.remove((long) 22873364); // "LCSUPPORT" dummy user

        // Don't send email to the user who is editing the late deliverable.
        managerUserIds.remove(AuthorizationHelper.getLoggedInUserId(request));

        List<String> managerEmails = ActionsHelper.getEmailsByUserIDs(request, managerUserIds);

        // Get the late member's email.
        List<String> lateMemberEmails = new ArrayList<String>();
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        lateMemberEmails.add( userRetrieval.retrieveUser(getLateDeliverableUserId(lateDeliverable)).getEmail() );

        sendEmailForUsers(project, lateMemberEmails, managerEmails, lateDeliverable,
                          ConfigHelper.getLateDeliverableUpdateByManagerEmailTemplateName(),
                          ConfigHelper.getLateDeliverableUpdateByManagerEmailTemplateSubject(),
                          ConfigHelper.getLateDeliverableUpdateByManagerEmailFromAddress(),
                          request);
    }

    /**
     * <p>Sends an email to managers associated with the specified late deliverable to notify on updates of the late
     * deliverable by the late member.</p>
     *
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @throws Exception if an unexpected error occurs.
     */
    private void sendEmailToManagers(LateDeliverable lateDeliverable, HttpServletRequest request) throws Exception {
        Project project = ActionsHelper.createProjectManager().getProject(lateDeliverable.getProjectId());

        String[] recipientRoleNames = ConfigHelper.getLateDeliverableUpdateByMemberRecipientRoleNames();

        List<Long> managerUserIds = ActionsHelper.getUserIDsByRoleNames(recipientRoleNames, project.getId());

        managerUserIds.remove((long) 22719217); // "Components" dummy user
        managerUserIds.remove((long) 22770213); // "Applications" dummy user
        managerUserIds.remove((long) 22873364); // "LCSUPPORT" dummy user

        List<String> managerEmails = ActionsHelper.getEmailsByUserIDs(request, managerUserIds);
        sendEmailForUsers(project, managerEmails, new ArrayList<String>(), lateDeliverable,
                          ConfigHelper.getLateDeliverableUpdateByMemberEmailTemplateName(),
                          ConfigHelper.getLateDeliverableUpdateByMemberEmailTemplateSubject(),
                          ConfigHelper.getLateDeliverableUpdateByMemberEmailFromAddress(),
                          request);
    }
}
