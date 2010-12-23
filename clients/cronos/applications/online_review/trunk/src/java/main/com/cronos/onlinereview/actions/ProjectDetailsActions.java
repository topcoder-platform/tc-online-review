/*
 * Copyright (C) 2004 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

import com.cronos.onlinereview.autoscreening.management.ResponseSeverity;
import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.autoscreening.management.ScreeningResult;
import com.cronos.onlinereview.autoscreening.management.ScreeningTask;
import com.cronos.onlinereview.dataaccess.ProjectDataAccess;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.model.ClientProject;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.SubmissionType;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.management.resource.Notification;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.DisallowedDirectoryException;
import com.topcoder.servlet.request.FileDoesNotExistException;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.PersistenceException;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;

/**
 * This class contains Struts Actions that are meant to deal with Project's details. There are
 * following Actions defined in this class:
 * <ul>
 * <li>View Project Details</li>
 * <li>Contact Manager</li>
 * <li>Upload Submission</li>
 * <li>Download Submission</li>
 * <li>Upload Final Fix</li>
 * <li>Download Final Fix</li>
 * <li>Upload Test Case</li>
 * <li>Download Test Case</li>
 * <li>Download Document</li>
 * <li>Delete Submission</li>
 * <li>View Auto Screening</li>
 * </ul>
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * <p>
 * Version 1.1 (Configurable Contest Terms Release Assembly v1.0) Change notes:
 *   <ol>
 *     <li>Added flag to allow a submitter to see unregistration link.</li>
 *     <li>Added unregistration action.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2 (Appeals Early Completion Release Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added flags to allow a submitter to see "appeals completed" / "resume appeals" links.</li>
 *     <li>Added "appeals completed" / "resume appeals" actions.</li>
 *     <li>Updated contact PM email notification with improved subject and new fields.</li>
 *     <li>Fixed hardcoded minimum screening score for project details page.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.3 (Online Review Prject Management Console Release Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added flags to allow a user to see "Manage Project" links.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.4 (Online Review End Of Project Analysis Release Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added logic for processing Post-Mortem deliverable.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.5 (Impersonation Login Release Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #getPhaseStatusCodes(Phase[], long)} method to recognize <code>Can't Open</code> phase status.
 *     </li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.6 (Specification Review Part 1 Assembly 1.0) Change notes:
 *   <ol>
 *    <li>Renamed <code>uploadSubmission</code> to <code>uploadContestSubmission</code> and
 *        <code>downloadSubmission</code> to <code>downloadContestSubmission</code> methods.</li>
 *    <li>Added {@link #uploadSpecificationSubmission(ActionMapping, ActionForm, HttpServletRequest,
 *        HttpServletResponse)} method.</li>
 *    <li>Updated {@link #uploadContestSubmission(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *        method to set type for uploaded submission.</li>
 *    <li>Added {@link #downloadSpecificationSubmission(ActionMapping, ActionForm, HttpServletRequest,
 *        HttpServletResponse)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.7 (Online Review Payments and Status Automation Assembly 1.0) Change notes:
 *   <ol>
 *    <li>Update {@link #viewProjectDetails(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} method
 *        to fix the duplication of scorecard templates.</li>
 *    <li>Update {@link #viewProjectDetails(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} method
 *        to show the &quot;Pay Project&quot; button in the Online Review shows when there's at least one resource with
 *        non-zero payment value and payment status other than "Paid".</li>
 *   </ol>
 * </p>
 *
 * @author George1, real_vg, pulky, isv, FireIce
 * @version 1.7
 */
public class ProjectDetailsActions extends DispatchAction {

    /**
     * Creates a new instance of the <code>ProjectDetailsActions</code> class.
     */
    public ProjectDetailsActions() {
    }

    /**
     * This method is an implementation of &quot;View project Details&quot; Struts Action defined
     * for this assembly, which is supposed to gather all possible information about the project and
     * display it to user.
     *
     * <p>
     * Updated for Online Review Update - Add Project Dropdown v1.0
     *      - added isAdmin value to the request.
     *      - if user is admin, then billing project value is set to the request.
     * </p>
     *
     * <p>
     * Updated for Configurable Contest Terms Release Assembly v1.0
     *     - added isAllowedToUnregister value to the request.
     *     - if user is a submitter and registration is open, he can unregister.
     * </p>
     *
     * <p>
     * Appeals Early Completion Release Assembly 1.0
     *     - added isAllowedToCompleteAppeals, isAllowedToResumeAppeals values to the request.
     *     - if user is a submitter and appeals phase is open, he can:
     *         - mark appeals as complete if his "Appeals Completed Early" flag is "No" or doesn't exist.
     *         - resume appeals if his "Appeals Completed Early" flag is "Yes".
     * </p>
     *
     * @return an action forward to the appropriate page. If no error has occured, the forward will
     *         be to viewProjectDetails.jsp page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewProjectDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to view
        Project project = verification.getProject();
        // Get Message Resources used for this request
        MessageResources messages = getResources(request);

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, project, messages);

        final String projectTypeName = project.getProjectCategory().getProjectType().getName();

        long projectId = project.getId();
        long forumId = -1;
        String tempStr;

        tempStr = (String) project.getProperty("Developer Forum ID");
        if (tempStr != null && tempStr.trim().length() != 0) {
            forumId = Long.parseLong(tempStr, 10);
        }

        request.setAttribute("viewContestLink",
                ConfigHelper.getProjectTypeViewContestLink(projectTypeName, projectId));
        request.setAttribute("forumLink", ConfigHelper.getProjectTypeForumLink(projectTypeName, forumId));
        request.setAttribute("projectType", projectTypeName);
        request.setAttribute("projectCategory", project.getProjectCategory().getName());
        request.setAttribute("projectStatus", project.getProjectStatus().getName());
        String paymentStr = project.getProperty("Payments") == null ? "0": project.getProperty("Payments").toString();
        request.setAttribute("projectPayment", Double.valueOf(paymentStr));

        boolean digitalRunFlag = "On".equals(project.getProperty("Digital Run Flag"));

        request.setAttribute("projectDRFlag", digitalRunFlag?"Yes":"No");

        String drpointStr = project.getProperty("DR points") == null ? "0" : project.getProperty("DR points").toString();
        if (digitalRunFlag)
        {
            double drpoint = 0;
            if (drpointStr != null)
            {
                drpoint = Double.parseDouble(drpointStr);
            }
            if (drpoint < 0.5)
            {
                request.setAttribute("projectDRP", Double.valueOf(paymentStr));
            }
            else
            {
                request.setAttribute("projectDRP", Double.valueOf(drpointStr));
            }
        }

        // since Online Review Update - Add Project Dropdown v1.0
        // Retrieve the billing project id from property.
        // And retrieve the list of all client projects, find billing project name by matching on id
        // set billing project name in request.
        // Retrieve Cockpit project also
        if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)
                || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
            long billingProjectId = 0L;
            tempStr = (String) project.getProperty("Billing Project");
            if (tempStr != null && tempStr.trim().length() != 0) {
                billingProjectId = Long.parseLong(tempStr, 10);
            }


            if (billingProjectId > 0) {
                List<ClientProject> clientProjects = ActionsHelper.getClientProjects(request);
                for (ClientProject cp : clientProjects) {
                    if (cp.getId() == billingProjectId) {
                        request.setAttribute("billingProject", cp.getName());
                        break;
                    }
                }
            } else {
                request.setAttribute("billingProject", "");
            }
        }
        boolean isAllowedToViewCockpitProjectName
                = AuthorizationHelper.hasUserPermission(request, Constants.VIEW_COCKPIT_PROJECT_NAME_PERM_NAME);
        request.setAttribute("isAllowedToViewCockpitProjectName", isAllowedToViewCockpitProjectName);
        if (isAllowedToViewCockpitProjectName) {
            ProjectDataAccess projectDataAccess = new ProjectDataAccess();
            request.setAttribute("cockpitProject", projectDataAccess.getCockpitProjectName(projectId));
        }

        // Place a string that represents "my" current role(s) into the request
        MessageResources messageResources = getResources(request);
        ActionsHelper.retrieveAndStoreMyRole(request, messageResources);
        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        // Place an information about the amount of "my" payment into the request
        Map<ResourceRole, Double> myPayments = ActionsHelper.getMyPayments(myResources, request);
        ResourceRole managerResourceRole = ActionsHelper.findResourceRoleByName(
                (ResourceRole[]) myPayments.keySet().toArray(new ResourceRole[myPayments.size()]), "Manager");
        if (managerResourceRole == null) {
            if (AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME) ||
			    AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)) {
                ResourceRole globalManagerRole = new ResourceRole();
                globalManagerRole.setName(messages.getMessage("ResourceRole."
                        + Constants.MANAGER_ROLE_NAME.replaceAll(" ", "")));
                myPayments.put(globalManagerRole, null);
            }
        }
        double totalPayment = 0;
        request.setAttribute("myPayment", myPayments);
        for (Double payment : myPayments.values()) {
            if (payment != null) {
                totalPayment += payment;
            }
        }
        request.setAttribute("totalPayment", totalPayment);
        // Place an information about my payment status into the request
        request.setAttribute("wasPaid", ActionsHelper.getMyPaymentStatuses(myResources));

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Get an array of all resources for the project
        Resource[] allProjectResources = ActionsHelper.getAllResourcesForProject(resMgr, project);
        for (int i = 0; i < allProjectResources.length; i++) {
            ActionsHelper.populateEmailProperty(request, allProjectResources[i]);
        }

        // Obtain an instance of Phase Manager
        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(request, false);
        com.topcoder.project.phases.Project phProj = phaseMgr.getPhases(project.getId());
        Phase[] phases;

        if (phProj != null) {
            // Calculate the date when this project is supposed to end
            phProj.calcEndDate();
            // Get all phases for the current project
            phases = phProj.getAllPhases(new Comparators.ProjectPhaseComparer());
        } else {
            phases = new Phase[0];
        }

        // Obtain an array of all active phases of the project
        Phase[] activePhases = ActionsHelper.getActivePhases(phases);

        // Place all phases of the project into the request
        request.setAttribute("phases", phases);

        long winnerExtUserId = Long.MIN_VALUE;
        String winnerExtRefId = (String) project.getProperty("Winner External Reference ID");

        if (winnerExtRefId != null && winnerExtRefId.trim().length() != 0) {
            winnerExtUserId = Long.parseLong(winnerExtRefId, 10);
        }

        Deliverable[] deliverables = ActionsHelper.getAllDeliverablesForPhases(
                ActionsHelper.createDeliverableManager(request), activePhases, allProjectResources, winnerExtUserId);

        // For approval phase
        Phase approvalPhase = ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME);
        if (approvalPhase != null) {
            ReviewManager reviewManager = ActionsHelper.createReviewManager(request);
            ScorecardType[] allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
            ScorecardType scorecardType = ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Approval");

            for (int i = 0; i < deliverables.length; i++) {
                Deliverable deliverable = deliverables[i];
                if (deliverable.getName().equals(Constants.APPROVAL_DELIVERABLE_NAME)) {
                    Review review = ActionsHelper.findLastApprovalReview(reviewManager, approvalPhase, scorecardType,
                                                                         deliverable.getResource(), false);
                    if ((review == null) || !review.isCommitted()) {
                        Deliverable newDeliverable
                            = new Deliverable(deliverable.getProject(), deliverable.getPhase(),
                                              deliverable.getResource(), deliverable.getSubmission(),
                                              deliverable.isRequired());
                        newDeliverable.setId(deliverable.getId());
                        newDeliverable.setName(deliverable.getName());
                        newDeliverable.setCreationTimestamp(deliverable.getCreationTimestamp());
                        newDeliverable.setCreationUser(deliverable.getCreationUser());
                        newDeliverable.setDescription(deliverable.getDescription());
                        newDeliverable.setModificationTimestamp(deliverable.getModificationTimestamp());
                        newDeliverable.setModificationUser(deliverable.getModificationUser());

                        deliverables[i] = newDeliverable;
                    } else {
                        deliverable.setCompletionDate(review.getModificationTimestamp());
                    }
                }
            }
        }

        Deliverable[] myDeliverables = ActionsHelper.getMyDeliverables(deliverables, myResources);
        Deliverable[] outstandingDeliverables = ActionsHelper.getOutstandingDeliverables(deliverables);

        request.setAttribute("myDeliverables", myDeliverables);
        request.setAttribute("outstandingDeliverables", outstandingDeliverables);

        long currentTime = (new Date()).getTime();

        // These two arrays will contain Deadline near / Late / Completed codes for deliverables
        int[] myDeliverableStatuses = getDeliverableStatusCodes(myDeliverables, activePhases, currentTime);
        int[] outstandingDeliverableStatuses =
            getDeliverableStatusCodes(outstandingDeliverables, activePhases, currentTime);

        Date[] myDeliverableDates = new Date[myDeliverables.length];
        Date[] outstandingDeliverableDates = new Date[outstandingDeliverables.length];

        for (int i = 0; i < myDeliverables.length; ++i) {
            Deliverable deliverable = myDeliverables[i];
            if (deliverable.isComplete()) {
                myDeliverableDates[i] = deliverable.getCompletionDate();
            } else {
                Phase phase = ActionsHelper.getPhaseForDeliverable(activePhases, deliverable);
                myDeliverableDates[i] = phase.getScheduledEndDate();
            }
        }

        for (int i = 0; i < outstandingDeliverables.length; ++i) {
            Deliverable deliverable = outstandingDeliverables[i];
            if (deliverable.isComplete()) {
                outstandingDeliverableDates[i] = deliverable.getCompletionDate();
            } else {
                Phase phase = ActionsHelper.getPhaseForDeliverable(activePhases, deliverable);
                outstandingDeliverableDates[i] = phase.getScheduledEndDate();
            }
        }

        String[] myDeliverableLinks = generateDeliverableLinks(request, myDeliverables, phases);
        String[] outstandingDeliverableUserIds = getDeliverableUserIds(outstandingDeliverables, allProjectResources);
        String[] outstandingDeliverableSubmissionUserIds =
            getDeliverableSubmissionUserIds(request, outstandingDeliverables);

        request.setAttribute("myDeliverableDates", myDeliverableDates);
        request.setAttribute("outstandingDeliverableDates", outstandingDeliverableDates);
        request.setAttribute("myDeliverableStatuses", myDeliverableStatuses);
        request.setAttribute("outstandingDeliverableStatuses", outstandingDeliverableStatuses);
        request.setAttribute("myDeliverableLinks", myDeliverableLinks);
        request.setAttribute("outstandingDeliverableUserIds", outstandingDeliverableUserIds);
        request.setAttribute("outstandingDeliverableSubmissionUserIds", outstandingDeliverableSubmissionUserIds);

        Date[] originalStart = new Date[phases.length];
        Date[] originalEnd = new Date[phases.length];
        long projectStartTime = (phProj != null) ? (phProj.getStartDate().getTime() / (60 * 1000)) : 0;
        // The following two arrays are used to display Gantt chart
        long[] ganttOffsets = new long[phases.length];
        long[] ganttLengths = new long[phases.length];
        // List of scorecard templates used for this project

        Map<String, Scorecard> phaseScorecardTemplates = new LinkedHashMap<String, Scorecard>();
        Map<String, String> phaseScorecardLinks = new LinkedHashMap<String, String>();
        Float minimumScreeningScore = 75f;
        // Iterate over all phases determining dates, durations and assigned scorecards
        for (int i = 0; i < phases.length; ++i) {
            // Get a phase for this iteration
            Phase phase = phases[i];

            Date startDate = phase.getScheduledStartDate();
            Date endDate = phase.getScheduledEndDate();

            // Get times in minutes
            long startTime = startDate.getTime() / (60 * 1000);
            long endTime = endDate.getTime() / (60 * 1000);

            // Determine the dates to display for start/end dates
            originalStart[i] = startDate;
            originalEnd[i] = endDate;

            // Determine offsets and lengths of the bars in Gantt chart, in minutes
            ganttOffsets[i] = startTime - projectStartTime;
            ganttLengths[i] = endTime - startTime;

            String phaseTypeName = phase.getPhaseType().getName();
            // Get a scorecard template associated with this phase if any
            Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(ActionsHelper
                    .createScorecardManager(request), phase);
            // If there is a scorecard template for the phase, store it in the list
            if (scorecardTemplate != null) {
                // override the previous scorecard, here assume the phases are ordered sequentially.
                phaseScorecardTemplates.put(phaseTypeName, scorecardTemplate);
                phaseScorecardLinks.put(phaseTypeName, ConfigHelper.getProjectTypeScorecardLink(projectTypeName, scorecardTemplate.getId()));

                if (phase.getPhaseType().getName().equals(Constants.SCREENING_PHASE_NAME)) {
                    minimumScreeningScore = scorecardTemplate.getMinScore();
                }
            }
        }

        // Collect Open / Closing / Late / Closed codes for phases
        int[] phaseStatuseCodes = getPhaseStatusCodes(phases, currentTime);

        /*
         * Place all gathered information about phases into the request as attributes
         */

        // Place phases' start/end dates

        request.setAttribute("originalStart", originalStart);
        request.setAttribute("originalEnd", originalEnd);
        request.setAttribute("phaseStatuseCodes", phaseStatuseCodes);
        // Place phases durations for Gantt chart
        request.setAttribute("ganttOffsets", ganttOffsets);
        request.setAttribute("ganttLengths", ganttLengths);
        // Place information about used scorecard templates
        request.setAttribute("scorecardTemplates", new ArrayList<Scorecard>(phaseScorecardTemplates.values()));
        request.setAttribute("scorecardLinks", new ArrayList<String>(phaseScorecardLinks.values()));

        ExternalUser[] allProjectExtUsers = null;

        // Determine if the user has permission to view a list of resources for the project
        if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECT_RESOURCES_PERM_NAME)) {
            // Get an array of external users for the corresponding resources
            allProjectExtUsers = ActionsHelper.getExternalUsersForResources(
                    ActionsHelper.createUserRetrieval(request), allProjectResources);

            // Place resources and external users into the request
            request.setAttribute("resources", allProjectResources);
            request.setAttribute("users", allProjectExtUsers);
        }
        PhasesDetails phasesDetails = PhasesDetailsServices.getPhasesDetails(
                request, messages, project, phases, allProjectResources, allProjectExtUsers);

        request.setAttribute("phaseGroupIndexes", phasesDetails.getPhaseGroupIndexes());
        request.setAttribute("phaseGroups", phasesDetails.getPhaseGroups());
        request.setAttribute("activeTabIdx", phasesDetails.getActiveTabIndex());
        request.setAttribute("passingMinimum", minimumScreeningScore);

        boolean sendTLNotifications = false;

        if (AuthorizationHelper.isUserLoggedIn(request)) {
            Filter filterTNproject = NotificationFilterBuilder.createProjectIdFilter(project.getId());
            Filter filterTNuser = NotificationFilterBuilder.createExternalRefIdFilter(
                    AuthorizationHelper.getLoggedInUserId(request));
            Filter filterTNtype = NotificationFilterBuilder.createNotificationTypeIdFilter(1);
            Filter filterTN = new AndFilter(filterTNproject, new AndFilter(filterTNuser, filterTNtype));

            Notification[] notifications = resMgr.searchNotifications(filterTN);
            sendTLNotifications = (notifications.length != 0);
        }

        request.setAttribute("sendTLNotifications", (sendTLNotifications) ? "On" : "Off");

        // Check resource roles
        request.setAttribute("isManager",
                Boolean.valueOf(AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)));
        request.setAttribute("isSubmitter",
                Boolean.valueOf(AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)));

        // check if registration phase is open
        boolean registrationOpen = false;
        for (int i = 0; i < activePhases.length && !registrationOpen; i++) {
            if (activePhases[i].getPhaseType().getName().equalsIgnoreCase(Constants.REGISTRATION_PHASE_NAME)) {
                registrationOpen = true;
            }
        }
        // check if appeals phase is open
        boolean appealsOpen = false;
        for (int i = 0; i < activePhases.length && !appealsOpen; i++) {
            if (activePhases[i].getPhaseType().getName().equalsIgnoreCase(Constants.APPEALS_PHASE_NAME)) {
                appealsOpen = true;
            }
        }

        // check if the user already submitted
        Resource submitter = ActionsHelper.getMyResourceForRole(request, "Submitter");
        boolean alreadySubmitted = submitter != null && submitter.getSubmissions() != null && submitter.getSubmissions().length > 0;

        request.setAttribute("isAllowedToUnregister",
            Boolean.valueOf(AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) && registrationOpen
            && !alreadySubmitted);

        // get appeals completed early property value
        boolean appealsCompletedFlag = false;
        if (submitter != null) {
            String value = (String) submitter.getProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY);
            if (value != null && value.equals(Constants.YES_VALUE)) {
                appealsCompletedFlag = true;
            }
        }

        // check if the user can mark appeals as completed
        request.setAttribute("isAllowedToCompleteAppeals",
            Boolean.valueOf(AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) &&
            appealsOpen && !appealsCompletedFlag);

        // check if the user can resume appeals
        request.setAttribute("isAllowedToResumeAppeals",
            Boolean.valueOf(AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) &&
            appealsOpen && appealsCompletedFlag);

        // Check permissions
        request.setAttribute("isAllowedToManageProjects",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request,
                                Constants.VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME)));
        request.setAttribute("isAllowedToEditProjects",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME)));
        request.setAttribute("isAllowedToContactPM",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.CONTACT_PM_PERM_NAME)));
        request.setAttribute("isAllowedToSetTL",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.SET_TL_NOTIFY_PERM_NAME)));
        request.setAttribute("isAllowedToViewSVNLink",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SVN_LINK_PERM_NAME)));
        request.setAttribute("isAllowedToViewAutopilotStatus",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.VIEW_AUTOPILOT_STATUS_PERM_NAME)));
        request.setAttribute("isAllowedToViewPayment",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_PAY_INFO_PERM_NAME)));
        request.setAttribute("isAllowedToViewAllPayment",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_PAYMENT_INFO_PERM_NAME)));
        request.setAttribute("isAllowedToViewResources",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECT_RESOURCES_PERM_NAME)));
        request.setAttribute("isAllowedToPerformScreening",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_SCREENING_PERM_NAME) &&
                        ActionsHelper.getPhase(phases, true, Constants.SCREENING_PHASE_NAME) != null));
        request.setAttribute("isAllowedToViewScreening",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENING_PERM_NAME)));
        request.setAttribute("isAllowedToEditHisReviews",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(
                        request, Constants.VIEW_REVIEWER_REVIEWS_PERM_NAME) &&
                        (ActionsHelper.getPhase(phases, true, Constants.REVIEW_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.POST_MORTEM_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.APPEALS_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.APPEALS_RESPONSE_PHASE_NAME) != null)));
        request.setAttribute("isAllowedToUploadTC",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.UPLOAD_TEST_CASES_PERM_NAME)));
        request.setAttribute("isAllowedToPerformAggregation",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME)));
        request.setAttribute("isAllowedToUploadFF",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_FIX_PERM_NAME) &&
                        AuthorizationHelper.getLoggedInUserId(request) == winnerExtUserId));
        request.setAttribute("isAllowedToUploadSpec",
                Boolean.valueOf(
                    AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME)));
        request.setAttribute("isAllowedToPerformFinalReview",
                Boolean.valueOf(ActionsHelper.getPhase(phases, true, Constants.FINAL_REVIEW_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME)));
        request.setAttribute("isAllowedToPerformSpecReview",
                Boolean.valueOf(ActionsHelper.getPhase(phases, true, Constants.SPECIFICATION_REVIEW_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_SPECIFICATION_REVIEW_PERM_NAME)));
        request.setAttribute("isAllowedToPerformApproval",
                Boolean.valueOf(ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPROVAL_PERM_NAME)));
        request.setAttribute("isAllowedToPerformPortMortemReview",
                Boolean.valueOf(ActionsHelper.getPhase(phases, true, Constants.POST_MORTEM_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_POST_MORTEM_REVIEW_PERM_NAME)));
        request.setAttribute("isAllowedToPay", Boolean.valueOf(isAllowedToPay(request, allProjectResources)));

        // Checking whether some user is allowed to submit his approval or comments for the
        // Aggregation worksheet needs more robust verification since this check includes a test
        // against whether a user is a submitter, and if it is, whether he is also a winner
        boolean allowedToReviewAggregation = false;

        if (AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME) &&
                !AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME)) {
            allowedToReviewAggregation = true;
        }

        if (allowedToReviewAggregation && AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) {
            final String winnerExtId = (String) project.getProperty("Winner External Reference ID");

            // Set 'allowed' status to false temporarily.
            // If current user is a winning submitter, this variable will be reset back to true
            allowedToReviewAggregation = false;

            // Iterate over all 'my' resources looking for 'Submitter' ones and comparing them to the
            // value of the winner for the current project (if there is any winner already)
            for (int i = 0; i < myResources.length; ++i) {
                // Get a resource for the current iteration
                final Resource resource = myResources[i];
                // Examine only Submitters, skip all other ones
                if (!resource.getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                    continue;
                }
                // This resource is a submitter;
                // compare its external user ID to the official project's winner's one
                if (myResources[i].getProperty("External Reference ID").equals(winnerExtId)) {
                        allowedToReviewAggregation = true;
                    break;
                }
            }
        }
        request.setAttribute("isAllowedToPerformAggregationReview", Boolean.valueOf(allowedToReviewAggregation));

        // since Online Review Update - Add Project Dropdown v1.0
        request.setAttribute("isAdmin",
                Boolean.valueOf(AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                        || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)
                        || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)));

        //OR Project Linking Assembly
        ProjectLinkManager linkManager = ActionsHelper.createProjectLinkManager(request);
        request.setAttribute("destProjectLinks", linkManager.getDestProjectLinks(project.getId()));
        request.setAttribute("srcProjectLinks", linkManager.getSourceProjectLinks(project.getId()));

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Contact Manager&quot; Struts Action defined for
     * this assembly, which is supposed to send a message entered by user to the manager of some
     * project. This action gets executed twice &#x96; once to display the page with the form, and
     * once to process the message entered by user on that form.
     *
     * @return an action forward to the appropriate page. If no error has occured and this action
     *         was called the first time, the forward will be to contactManager.jsp page, which
     *         displays the form where user can enter his message. If this action was called during
     *         the post back (the second time), then the request should contain the message to send
     *         entered by user. In this case, this method verifies if everything is correct, sends
     *         the message to manager and returns a forward to the View Project Details page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws ConfigManagerException
     *             if any error occurs while loading the document generator's configuration.
     * @throws BaseException
     *             if any other error occurs.
     */
    public ActionForward contactManager(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException, ConfigManagerException {
        LoggingHelper.logAction(request);

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.CONTACT_PM_PERM_NAME, postBack);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Obtain an instance of Document Generator
        DocumentGenerator docGenerator = DocumentGenerator.getInstance();

        // Get the template of email
        Template docTemplate = docGenerator.getTemplate(
                ConfigHelper.getContactManagerEmailSrcType(), ConfigHelper.getContactManagerEmailTemplate());

        // Get the ID of the sender
        long senderId = AuthorizationHelper.getLoggedInUserId(request);
        // Obtain an instance of User Retrieval
        UserRetrieval userMgr = ActionsHelper.createUserRetrieval(request);
        // Retrieve information about an external user by its ID
        ExternalUser sender = userMgr.retrieveUser(senderId);

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Get all Resource Roles
        ResourceRole[] allResourceRoles = resMgr.getAllResourceRoles();

        // Get current project from the verification result bean
        Project project = verification.getProject();

        // Build filters
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(project.getId());

        Filter filterRole = new OrFilter(
                new OrFilter(
                        ResourceFilterBuilder.createResourceRoleIdFilter(
                            ActionsHelper.findResourceRoleByName(allResourceRoles, "Manager").getId()),
                        ResourceFilterBuilder.createResourceRoleIdFilter(
                            ActionsHelper.findResourceRoleByName(allResourceRoles, "Client Manager").getId())),
                ResourceFilterBuilder.createResourceRoleIdFilter(
                        ActionsHelper.findResourceRoleByName(allResourceRoles, "Copilot").getId()));

        // Build final filter
        Filter filter = new AndFilter(filterProject, filterRole);
        // Search for the managers of this project
        Resource[] managers = resMgr.searchResources(filter);

        Set<String> existingManagers = new HashSet<String>();

        // Collect unique external user IDs first,
        // as there may exist multiple manager resources for the same user
        for (int i = 0; i < managers.length; ++i) {
            String extUserId = ((String) managers[i].getProperty("External Reference ID")).trim();
            if (!existingManagers.contains(extUserId)) {
                existingManagers.add(extUserId);
            }
        }

        long[] extUsrManagerIds = new long[existingManagers.size() + 1];
        int managerIdx = 0;

        // This inefficient operation, but going over all resources' properties is even more inefficient
        for (String mgr : existingManagers) {
            extUsrManagerIds[managerIdx++] = Long.parseLong(mgr);
        }

        //send a copy to the sender
        extUsrManagerIds[managerIdx++] = senderId;

        // Retrieve all external resources for managers in a single batch operation
        ExternalUser[] extUsrManagers = userMgr.retrieveUsers(extUsrManagerIds);

        // Get the category of the question
        String questionType = request.getParameter("cat");
        // Get question's text
        String text = "<![CDATA[" + request.getParameter("msg") +"]]>";

        TemplateFields fields = docGenerator.getFields(docTemplate);
        Node[] nodes = fields.getNodes();

        // Construct the body of the email
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof Field) {
                Field field = (Field) nodes[i];

                if ("USER_FIRST_NAME".equals(field.getName())) {
                    field.setValue(sender.getFirstName());
                } else if ("USER_LAST_NAME".equals(field.getName())) {
                    field.setValue(sender.getLastName());
                } else if ("USER_HANDLE".equals(field.getName())) {
                    field.setValue(sender.getHandle());
                } else if ("PROJECT_NAME".equals(field.getName())) {
                    field.setValue("<![CDATA[" + project.getProjectCategory().getDescription() + " - " +
                            project.getProperty("Project Name") + "]]>");
                } else if ("PROJECT_VERSION".equals(field.getName())) {
                    field.setValue("" + project.getProperty("Project Version"));
                } else if ("QUESTION_TYPE".equals(field.getName())) {
                    field.setValue(questionType);
                } else if ("TEXT".equals(field.getName())) {
                    field.setValue(text);
                } else if ("OR_LINK".equals(field.getName())) {
                    field.setValue("<![CDATA[" + ConfigHelper.getProjectDetailsBaseURL() + project.getId() + "]]>");
                } else if ("LIST_OF_ROLES".equals(field.getName())) {
                    StringBuilder roleList = new StringBuilder();
                    Resource[] myResources = (Resource[]) request.getAttribute("myResources");

                    for (Resource resource : myResources) {
                        if (roleList.length() != 0) {
                            roleList.append(", ");
                        }
                        roleList.append(resource.getResourceRole().getName());
                    }
                    field.setValue(roleList.toString());
                }
            }
        }

        // Compose a message to send
        TCSEmailMessage message = new TCSEmailMessage();

        // Add 'To' addresses to message
        for (int i = 0; i < extUsrManagers.length - 1; ++i) {
            message.addToAddress(extUsrManagers[i].getEmail(), TCSEmailMessage.TO);
        }
        //The last one is the sender that is CC'd in the mail
        message.addToAddress(extUsrManagers[extUsrManagers.length - 1].getEmail(), TCSEmailMessage.CC);
        // Add 'From' address
        message.setFromAddress(sender.getEmail());
        // Set message's subject
        message.setSubject((String) project.getProperty("Project Name") + " - " + sender.getHandle());
        // Insert a body into the message
        message.setBody(docGenerator.applyTemplate(fields));

        // Send an email
        EmailEngine.send(message);

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
    }

    /**
     * This method is an implementation of &quot;Upload Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.
     *
     * @return an action forward to the appropriate page. If no error has occured and this action
     *         was called the first time, the forward will be to uploadSubmission.jsp page, which
     *         displays the form where user can specify the file he/she wants to upload. If this
     *         action was called during the post back (the second time), then the request should
     *         contain the file uploaded by user. In this case, this method verifies if everything
     *         is correct, stores the file on file server and returns a forward to the View Project
     *         Details page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward uploadContestSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);

        // Determine if this request is a post back
        final boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.PERFORM_SUBM_PERM_NAME, postBack);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve current project
        Project project = verification.getProject();
        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);

        if (ActionsHelper.getPhase(phases, true, Constants.SUBMISSION_PHASE_NAME) == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SUBM_PERM_NAME, "Error.IncorrectPhase", null);
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        DynaValidatorForm uploadSubmissionForm = (DynaValidatorForm) form;
        FormFile file = (FormFile) uploadSubmissionForm.get("file");

        // Disallow uploading of empty files
        if (file.getFileSize() == 0) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SUBM_PERM_NAME, "Error.EmptyFileUploaded", null);
        }

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        // Obtain an instance of File Upload Manager
        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForRole(request, "Submitter");

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        SubmissionStatus[] submissionStatuses = upMgr.getAllSubmissionStatuses();
        SubmissionType[] submissionTypes = upMgr.getAllSubmissionTypes();

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(resource.getId());
        Filter filterStatus = SubmissionFilterBuilder.createSubmissionStatusIdFilter(
                ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active").getId());

        Filter filter = new AndFilter(Arrays.asList(new Filter[] {filterProject, filterResource, filterStatus}));

        Submission[] oldSubmissions = upMgr.searchSubmissions(filter);

        // Modification for the new requirement - uploading new submission/upload every time.

        // Begins - OrChange - Modified to create upload/submission pair always
        // Always create a new submission/ upload
        Submission submission = new Submission();
        Upload upload = new Upload();

        UploadStatus[] uploadStatuses = upMgr.getAllUploadStatuses();
        UploadType[] uploadTypes = upMgr.getAllUploadTypes();

        upload.setProject(project.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(ActionsHelper.findUploadStatusByName(uploadStatuses, "Active"));
        upload.setUploadType(ActionsHelper.findUploadTypeByName(uploadTypes, "Submission"));
        upload.setParameter(uploadedFile.getFileId());

        submission.setUpload(upload);
        submission.setSubmissionStatus(ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active"));
        submission.setSubmissionType(ActionsHelper.findSubmissionTypeByName(submissionTypes, "Contest Submission"));

        // Get the name (id) of the user performing the operations
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));

        // If the project DOESN'T allow multiple submissions hence its property "Allow
        // multiple submissions" will be false
        Boolean allowOldSubmissions = Boolean.parseBoolean((String) project.getProperty("Allow multiple submissions"));

        upMgr.createUpload(upload, operator);
        upMgr.createSubmission(submission, operator);
        resource.addSubmission(submission.getId());
        ActionsHelper.createResourceManager(request).updateResource(resource, operator);
        log.debug("Allow Multiple Submissions : " + allowOldSubmissions);
        // Now depending on whether the project allows multiple submissions or not mark the old submission
        // and the upload as deleted.
        if (oldSubmissions.length != 0 && !allowOldSubmissions) {
            SubmissionStatus deleteSubmissionStatus = ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Deleted");
            UploadStatus deleteUploadStatus = ActionsHelper.findUploadStatusByName(uploadStatuses, "Deleted");
            for (Submission oldSubmission : oldSubmissions) {
                oldSubmission.getUpload().setUploadStatus(deleteUploadStatus);
                oldSubmission.setSubmissionStatus(deleteSubmissionStatus);
                upMgr.updateUpload(oldSubmission.getUpload(), operator);
                upMgr.updateSubmission(oldSubmission, operator);
            }
        }

        // Obtain an instance of Screening Manager
        ScreeningManager scrMgr = ActionsHelper.createScreeningManager(request);
        scrMgr.initiateScreening(upload.getId(), operator);

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
    }

    /**
     * <p>This method is an implementation of &quot;Upload Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.</p>
     *
     * @return an action forward to the appropriate page. If no error has occured and this action
     *         was called the first time, the forward will be to uploadSubmission.jsp page, which
     *         displays the form where user can specify the file he/she wants to upload. If this
     *         action was called during the post back (the second time), then the request should
     *         contain the file uploaded by user. In this case, this method verifies if everything
     *         is correct, stores the file on file server and returns a forward to the View Project
     *         Details page.
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.6
     */
    public ActionForward uploadSpecificationSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        // Determine if this request is a post back
        final boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
            mapping, getResources(request), request, Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME,
            postBack);

        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve current project
        Project project = verification.getProject();
        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);

        Phase specificationPhase = ActionsHelper.getPhase(phases, true, Constants.SPECIFICATION_SUBMISSION_PHASE_NAME);
        if (specificationPhase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME, "Error.IncorrectPhase", null);
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Check if specification is already submitted
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        Submission oldSubmission = ActionsHelper.getActiveSpecificationSubmission(project.getId(), upMgr);
        if (oldSubmission != null) {
            // Disallow submitting more than one Specification Submission for project
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME, "Error.SpecificationAlreadyUploaded", null);
        }

        // Analyze the nature of the submitted specification - uploaded file vs plain text and construct appropriate
        // request parser based on that
        DynaValidatorForm uploadSubmissionForm = (DynaValidatorForm) form;
        String specFormatType = (String) uploadSubmissionForm.get("specificationType");
        RequestParser parser;
        if ("file".equalsIgnoreCase(specFormatType)) {
            FormFile file = (FormFile) uploadSubmissionForm.get("file");
            // Disallow uploading of empty files
            if (file.getFileSize() == 0) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME, "Error.EmptyFileUploaded", null);
            }
            StrutsRequestParser strutsParser = new StrutsRequestParser();
            strutsParser.AddFile(file);
            parser = strutsParser;
        } else {
            String specificationText = (String) uploadSubmissionForm.get("specificationText");
            if ((specificationText == null) || (specificationText.trim().length() == 0)) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME, "Error.EmptySpecification", null);
            }
            parser = new TextContentRequestParser(specificationText);
        }

        // Parse request and save specification as uploaded file
        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        Resource resource = ActionsHelper.getMyResourceForRole(request, "Specification Submitter");
        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Always create a new submission/ upload
        Upload upload = new Upload();

        UploadStatus[] uploadStatuses = upMgr.getAllUploadStatuses();
        UploadType[] uploadTypes = upMgr.getAllUploadTypes();

        upload.setProject(project.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(ActionsHelper.findUploadStatusByName(uploadStatuses, "Active"));
        upload.setUploadType(ActionsHelper.findUploadTypeByName(uploadTypes, "Submission"));
        upload.setParameter(uploadedFile.getFileId());

        Submission submission = new Submission();
        submission.setUpload(upload);

        SubmissionStatus[] submissionStatuses = upMgr.getAllSubmissionStatuses();
        SubmissionType[] submissionTypes = upMgr.getAllSubmissionTypes();
        SubmissionType specSubmissionType
            = ActionsHelper.findSubmissionTypeByName(submissionTypes, "Specification Submission");
        SubmissionStatus submissionActiveStatus
            = ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active");
        submission.setSubmissionStatus(submissionActiveStatus);
        submission.setSubmissionType(specSubmissionType);

        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        upMgr.createUpload(upload, operator);
        upMgr.createSubmission(submission, operator);
        resource.addSubmission(submission.getId());
        resourceManager.updateResource(resource, operator);

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
    }

    /**
     * This method is an implementation of &quot;Download Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a submission from the server.
     *
     * @return a <code>null</code> code if everything went fine, or an action forward to
     *         /jsp/userError.jsp page which will display the information about the cause of error.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     * @throws IOException
     *             if some error occurs during disk input/output operation.
     */
    public ActionForward downloadContestSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException, IOException {
        LoggingHelper.logAction(request);
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
            checkForCorrectUploadId(mapping, request, "ViewSubmission");
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();

        // Verify that upload is a submission
        if (!upload.getUploadType().getName().equalsIgnoreCase("Submission")) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, "ViewSubmission", "Error.NotASubmission", null);
        }

        // Verify the status of upload and check whether the user has permission to download old uploads
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted") &&
                !AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, "ViewSubmission", "Error.UploadDeleted", null);
        }

        // Get all phases for the current project (needed to do permission checks)
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request, false), verification.getProject());

        boolean noRights = true;

        if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
            noRights = false;
        }

        if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {
            long owningResourceId = upload.getOwner();
            Resource[] myResources = (Resource[]) request.getAttribute("myResources");
            for (int i = 0; i < myResources.length; ++i) {
                if (myResources[i].getId() == owningResourceId) {
                    noRights = false;
                    break;
                }
            }
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENER_SUBM_PERM_NAME)) {
            // Determine whether Screening phase has already been opened (does not have Scheduled status)
            final boolean isScreeningOpen = ActionsHelper.isInOrAfterPhase(phases, 0, Constants.SCREENING_PHASE_NAME);
            // If screener tries to download submission before Screening phase opens,
            // notify him about this wrong-doing and do not let perform the action
            if (AuthorizationHelper.hasUserRole(request, Constants.SCREENER_ROLE_NAMES) && !isScreeningOpen) {
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, "ViewSubmission", "Error.IncorrectPhase", null);
            }
            noRights = false; // TODO: Check if screener can download this submission
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME)) {
            // Determine whether Review phase has already been opened (does not have Scheduled status)
            final boolean isReviewOpen = ActionsHelper.isInOrAfterPhase(phases, 0, Constants.REVIEW_PHASE_NAME);
            // If reviewer tries to download submission before Review phase opens,
            // notify him about this wrong-doing and do not let perform the action
            if (AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES) && !isReviewOpen) {
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, "ViewSubmission", "Error.IncorrectPhase", null);
            }
            noRights = false;
        }

        // the download validation for custom components is different
        String rootCatalogId = (String)((verification.getProject()).getProperty("Root Catalog ID"));
        boolean custom = ConfigHelper.isCustomRootCatalog(rootCatalogId);

        boolean mayDownload = (custom ?
            AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_CUSTOM_SUBM_PERM_NAME) :
            AuthorizationHelper.hasUserPermission(request, Constants.VIEW_WINNING_SUBM_PERM_NAME));

        if (noRights && mayDownload) {
            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager(request);
            Resource submitter = resMgr.getResource(upload.getOwner());
            UploadManager upMgr = ActionsHelper.createUploadManager(request);
            Long[] subIds = submitter.getSubmissions();
            Submission submission = null;
            for (int i = 0; i < subIds.length; i++) {
                submission = upMgr.getSubmission(subIds[i]);
                if(submission.getUpload().getId() == upload.getId()) {
                    break;
                }
            }

            // OrChange - Placement is retrieved from submission instead of resource
            if (submission.getPlacement() != null && submission.getPlacement() == 1) {
                noRights = false;
            }
        }

        mayDownload = (custom ?
                AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_CUSTOM_SUBM_PERM_NAME) :
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_AAR_PERM_NAME));

        if (noRights && mayDownload) {
            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager(request);
            Resource submitter = resMgr.getResource(upload.getOwner());
            UploadManager upMgr = ActionsHelper.createUploadManager(request);
            Long[] subIds = submitter.getSubmissions();
            Submission submission = null;
            for (int i = 0; i < subIds.length; i++) {
                submission = upMgr.getSubmission(subIds[i]);
                if(submission.getUpload().getId() == upload.getId()) {
                    break;
                }
            }
//          OrChange - Placement is retrieved from submission instead of resource
            if (submission.getPlacement() != null && submission.getPlacement() > 0) {
                noRights = false;
            }
        }

        if (noRights) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    "ViewSubmission", "Error.NoPermission", Boolean.TRUE);
        }

        processSubmissionDownload(upload, request, response);

        return null;
    }

    /**
     * <p>This method is an implementation of &quot;Download Specification Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a submission from the server.</p>
     *
     * @return a <code>null</code> code if everything went fine, or an action forward to /jsp/userError.jsp page which
     *         will display the information about the cause of error.
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @throws IOException if some error occurs during disk input/output operation.
     * @since 1.6
     */
    public ActionForward downloadSpecificationSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = checkForCorrectUploadId(mapping, request, "ViewSubmission");
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();
        // Verify that upload is a submission
        if (!upload.getUploadType().getName().equalsIgnoreCase("Submission")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request, "ViewSubmission",
                "Error.NotASubmission", null);
        }

        // Verify the status of upload and check whether the user has permission to download old uploads
        boolean hasViewAllSpecSubmissionsPermission
            = AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SPECIFICATION_SUBMISSIONS_PERM_NAME);
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted") && !hasViewAllSpecSubmissionsPermission) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, "ViewSubmission", "Error.UploadDeleted", null);
        }

        boolean noRights = true;

        if (hasViewAllSpecSubmissionsPermission) {
            noRights = false;
        }

        if (noRights
            && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SPECIFICATION_SUBMISSIONS_PERM_NAME)) {
            long owningResourceId = upload.getOwner();
            Resource[] myResources = (Resource[]) request.getAttribute("myResources");
            for (int i = 0; i < myResources.length; ++i) {
                if (myResources[i].getId() == owningResourceId) {
                    noRights = false;
                    break;
                }
            }
        }

        if (noRights && AuthorizationHelper.hasUserPermission(
                            request, Constants.VIEW_RECENT_SPECIFICATION_SUBMISSIONS_PERM_NAME)) {
            Phase[] phases = ActionsHelper.getPhasesForProject(
                    ActionsHelper.createPhaseManager(request, false), verification.getProject());
            final boolean isReviewOpen = ActionsHelper.isInOrAfterPhase(phases, 0,
                Constants.SPECIFICATION_REVIEW_PHASE_NAME);
            if (AuthorizationHelper.hasUserRole(request, Constants.SPECIFICATION_REVIEWER_ROLE_NAME) && !isReviewOpen) {
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, "ViewSubmission", "Error.IncorrectPhase", null);
            }
            noRights = false;
        }

        if (noRights) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request, "ViewSubmission",
                "Error.NoPermission", Boolean.TRUE);
        }

        processSubmissionDownload(upload, request, response);

        return null;
    }

    /**
     * This method is an implementation of &quot;Upload Final Fix&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.
     *
     * @return an action forward to the appropriate page. If no error has occured and this action
     *         was called the first time, the forward will be to uploadFinalFix.jsp page, which
     *         displays the form where user can specify the file he/she wants to upload. If this
     *         action was called during the post back (the second time), then the request should
     *         contain the file uploaded by user. In this case, this method verifies if everything
     *         is correct, stores the file on file server and returns a forward to the View Project
     *         Details page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward uploadFinalFix(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.PERFORM_FINAL_FIX_PERM_NAME, postBack);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        if (postBack != true) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Retrieve current project
        Project project = verification.getProject();

        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);
        // Retrieve the current phase for the project
        Phase currentPhase = ActionsHelper.getPhase(phases, true, Constants.FINAL_FIX_PHASE_NAME);
        // Check that active phase is Final Fix
        if (currentPhase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.PERFORM_FINAL_FIX_PERM_NAME, "Error.IncorrectPhase", null);
        }

        int finalFixCount = 0;

        for (int i = 0; i < phases.length; ++i) {
            Phase phase = phases[i];
            if (phase.getPhaseType().getName().equalsIgnoreCase(Constants.FINAL_FIX_PHASE_NAME)) {
                ++finalFixCount;
            }
            if (phase == currentPhase) {
                break;
            }
        }

        DynaValidatorForm uploadSubmissionForm = (DynaValidatorForm) form;
        FormFile file = (FormFile) uploadSubmissionForm.get("file");

        // Disallow uploading of empty files
        if (file.getFileSize() == 0) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_FIX_PERM_NAME, "Error.EmptyFileUploaded", null);
        }

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        UploadStatus[] allUploadStatuses = upMgr.getAllUploadStatuses();
        UploadType[] allUploadTypes = upMgr.getAllUploadTypes();

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForRole(request, "Submitter");

        Filter projectFilter = UploadFilterBuilder.createProjectIdFilter(project.getId());
        Filter resourceFilter = UploadFilterBuilder.createResourceIdFilter(resource.getId());
        Filter typeFilter = UploadFilterBuilder.createUploadTypeIdFilter(
                ActionsHelper.findUploadTypeByName(allUploadTypes, "Final Fix").getId());
        Filter combinedFilter = new AndFilter(
                Arrays.asList(new Filter[] { projectFilter, resourceFilter, typeFilter }));

        Upload[] uploads = upMgr.searchUploads(combinedFilter);

        if (uploads.length >= finalFixCount) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.PERFORM_FINAL_FIX_PERM_NAME, "Error.OnlyOneFinalFix", null);
        }

        Arrays.sort(uploads, new Comparators.UploadComparer());
        Upload oldUpload = (uploads.length != 0) ? uploads[uploads.length - 1] : null;

        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(ActionsHelper.findUploadStatusByName(allUploadStatuses, "Active"));
        upload.setUploadType(ActionsHelper.findUploadTypeByName(allUploadTypes, "Final Fix"));
        upload.setParameter(uploadedFile.getFileId());

        if (oldUpload != null) {
            oldUpload.setUploadStatus(ActionsHelper.findUploadStatusByName(allUploadStatuses, "Deleted"));
            upMgr.updateUpload(oldUpload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        upMgr.createUpload(upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
    }

    /**
     * This method is an implementation of &quot;Download Final Fix&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a final fixes from the server.
     *
     * @return a <code>null</code> code if everything went fine, or an action forward to
     *         /jsp/userError.jsp page which will display the information about the cause of error.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     * @throws IOException
     *             if some error occurs during disk input/output operation.
     */
    public ActionForward downloadFinalFix(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException, IOException {
        LoggingHelper.logAction(request);
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
            checkForCorrectUploadId(mapping, request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Check that user has permissions to download Final Fixes
        if (!AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();

        // Verify that upload is a Final Fix
        if (!upload.getUploadType().getName().equalsIgnoreCase("Final Fix")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME, "Error.NotAFinalFix", null);
        }
/* TODO: Remove this commented block when everything works ok
        // Verify the status of upload
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME, "Error.UploadDeleted");
        }
*/

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());
        outputDownloadedFile(uploadedFile, "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"",
                             request, response);

        return null;
    }

    /**
     * This method is an implementation of &quot;Upload Test Case&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his test cases to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.
     *
     * @return an action forward to the appropriate page. If no error has occured and this action
     *         was called the first time, the forward will be to uploadTestCase.jsp page, which
     *         displays the form where user can specify the file he/she wants to upload. If this
     *         action was called during the post back (the second time), then the request should
     *         contain the file uploaded by user. In this case, this method verifies if everything
     *         is correct, stores the file on file server and returns a forward to the View Project
     *         Details page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward uploadTestCase(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.UPLOAD_TEST_CASES_PERM_NAME, postBack);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        if (postBack != true) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Retrieve current project
        Project project = verification.getProject();

        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);
        // Retrieve the current phase for the project
        Phase currentPhase = ActionsHelper.getPhase(phases, true, Constants.REVIEW_PHASE_NAME);

        if (currentPhase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.UPLOAD_TEST_CASES_PERM_NAME, "Error.IncorrectPhase", null);
        }

        DynaValidatorForm uploadSubmissionForm = (DynaValidatorForm) form;
        FormFile file = (FormFile) uploadSubmissionForm.get("file");

        // Disallow uploading of empty files
        if (file.getFileSize() == 0) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.UPLOAD_TEST_CASES_PERM_NAME, "Error.EmptyFileUploaded", null);
        }

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        UploadStatus[] allUploadStatuses = upMgr.getAllUploadStatuses();
        UploadType[] allUploadTypes = upMgr.getAllUploadTypes();

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForPhase(
                request, ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME));

        Filter filterProject = UploadFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterResource = UploadFilterBuilder.createResourceIdFilter(resource.getId());
        Filter filterStatus = UploadFilterBuilder.createUploadStatusIdFilter(
                ActionsHelper.findUploadStatusByName(allUploadStatuses, "Active").getId());
        Filter filterType = UploadFilterBuilder.createUploadTypeIdFilter(
                ActionsHelper.findUploadTypeByName(allUploadTypes, "Test Case").getId());

        Filter filter = new AndFilter(
                Arrays.asList(new Filter[] {filterProject, filterResource, filterStatus, filterType}));

        Upload[] uploads = upMgr.searchUploads(filter);
        Upload oldUpload = (uploads.length != 0) ? uploads[0] : null;

        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(ActionsHelper.findUploadStatusByName(allUploadStatuses, "Active"));
        upload.setUploadType(ActionsHelper.findUploadTypeByName(allUploadTypes, "Test Case"));
        upload.setParameter(uploadedFile.getFileId());

        if (oldUpload != null) {
            oldUpload.setUploadStatus(ActionsHelper.findUploadStatusByName(allUploadStatuses, "Deleted"));
            upMgr.updateUpload(oldUpload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        upMgr.createUpload(upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        if (oldUpload != null) {
            // TODO: Send email notifications here
        }

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
    }

    /**
     * This method is an implementation of &quot;Download Test Case&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a final fixes from the server.
     *
     * @return a <code>null</code> code if everything went fine, or an action forward to
     *         /jsp/userError.jsp page which will display the information about the cause of error.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     * @throws IOException
     *             if some error occurs during disk input/output operation.
     */
    public ActionForward downloadTestCase(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException, IOException {
        LoggingHelper.logAction(request);
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
            checkForCorrectUploadId(mapping, request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request, false), verification.getProject());

        boolean isReviewClosed = false;
        boolean isAppealsOpen = false;

        for (int i = 0; i < phases.length; ++i) {
            // Get phase's type name for the current iteration
            String phaseName = phases[i].getPhaseType().getName();
            // Skip the phase if it is not a Review phase
            if (!phaseName.equalsIgnoreCase(Constants.REVIEW_PHASE_NAME)) {
                continue;
            }
            // Check if this phase is closed
            if (phases[i].getPhaseStatus().getName().equalsIgnoreCase(Constants.CLOSED_PH_STATUS_NAME)) {
                isReviewClosed = true;
                // Review phase is closed, not need to check anything else
                break;
            }
            // Check that there are more phases, and exit from cycle if current phase is the last
            if (i + 1 == phases.length) {
                break;
            }
            // Get next phase
            Phase nextPhase = phases[i + 1];
            // Check that next phase is Appeals and it is open
            if (nextPhase.getPhaseType().getName().equalsIgnoreCase(Constants.APPEALS_PHASE_NAME) &&
                    nextPhase.getPhaseStatus().getName().equalsIgnoreCase(Constants.OPEN_PH_STATUS_NAME)) {
                isAppealsOpen = true;
            }
            // No need to proceed with the cycle anymore
            break;
        }

        final boolean canDownload =
            AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME);
        final boolean canDownloadDuringReview =
            AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_TC_DUR_REVIEW_PERM_NAME);
        final boolean canPlaceAppeals =
            AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_PERM_NAME);

        // If Review phase is not closed yet, there is a need to check whether the user that is
        // attempting to download test cases is a Submitter and an Appeals phase is open
        if (canDownload && canPlaceAppeals && !isReviewClosed && !canDownloadDuringReview && !isAppealsOpen) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.IncorrectPhase", null);
        }
        // Verify that user can download test cases during Review
        if (canDownload && !isReviewClosed && !canDownloadDuringReview) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.IncorrectPhase", null);
        }
        // Check that the user is allowed to download test cases in general
        if (!canDownload) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();

        // Verify that upload is Test Cases
        if (!upload.getUploadType().getName().equalsIgnoreCase("Test Case")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.NotTestCases", null);
        }
        // Verify the status of upload
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.UploadDeleted", null);
        }

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());
        outputDownloadedFile(uploadedFile, "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"",
                             request, response);

        return null;
    }

    /**
     * This method is an implementation of &quot;Delete Submission&quot; Struts Action defined for
     * this assembly, which is supposed to delete (mark as deleted) submission for particular upload
     * (denoted by <code>uid</code> parameter). This action gets executed twice &#x96; once to
     * display the page with the confirmation, and once to process the confiremed delete request to
     * actually delete the submission.
     *
     * @return an action forward to the appropriate page. If no error has occured and this action
     *         was called the first time, the forward will be to /jsp/confirmDeleteSubmission.jsp
     *         page, which displays the confirmation dialog where user can confirm his intention to
     *         remove the submission. If this action was called during the post back (the second
     *         time), then this method verifies if everything is correct, and marks submission and
     *         its current active upload as deleted. After this it returns a forward to the View
     *         Project Details page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward deleteSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
            checkForCorrectUploadId(mapping, request, Constants.REMOVE_SUBM_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve the upload user tries to delete
        Upload upload = verification.getUpload();

        // Check that user has permissions to delete submission
        if (!AuthorizationHelper.hasUserPermission(request, Constants.REMOVE_SUBM_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.REMOVE_SUBM_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Verify that the user is attempting to delete submission
        if (!upload.getUploadType().getName().equalsIgnoreCase("Submission")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.REMOVE_SUBM_PERM_NAME, "Error.NotASubmission2", null);
        }

        Filter filter = SubmissionFilterBuilder.createUploadIdFilter(upload.getId());
        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        Submission[] submissions = upMgr.searchSubmissions(filter);
        Submission submission = (submissions.length != 0) ? submissions[0] : null;

        if (submission == null || submission.getSubmissionStatus().getName().equalsIgnoreCase("Deleted")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.REMOVE_SUBM_PERM_NAME, "Error.SubmissionDeleted", null);
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("delete") != null);

        if (postBack != true) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            // Place upload ID into the request as attribute
            request.setAttribute("uid", new Long(upload.getId()));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        UploadStatus[] allUploadStatuses = upMgr.getAllUploadStatuses();
        SubmissionStatus[] allSubmissionStatuses = upMgr.getAllSubmissionStatuses();

        upload.setUploadStatus(ActionsHelper.findUploadStatusByName(allUploadStatuses, "Deleted"));
        submission.setSubmissionStatus(ActionsHelper.findSubmissionStatusByName(allSubmissionStatuses, "Deleted"));

        upMgr.updateUpload(upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        upMgr.updateSubmission(submission, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        // recaculate screening reviewer payments
        ActionsHelper.recaculateScreeningReviewerPayments(upload.getProject());

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * This method is an implementation of &quot;Unregister&quot; Struts Action defined for
     * this assembly, which is supposed to unregister the logged in submitter from a project
     * (denoted by <code>pid</code> parameter). This action gets executed twice &#x96; once to
     * display the page with the confirmation, and once to process the confirmed request to
     * actually perform the unregistration.
     *
     * @return an action forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmUnregistration.jsp
     *         If this action was called during the post back (the second time), then this method
     *         verifies if everything is correct, and proceeds with the unregisration.
     *         After this it returns a forward to the View Project Details page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     * @since 1.1
     */
    public ActionForward unregister(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Check the user has submitter role and registration phase is open
        boolean isSubmitter = Boolean.valueOf(AuthorizationHelper.hasUserRole(request,
                Constants.SUBMITTER_ROLE_NAME));

        if (!isSubmitter) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, "Unregistration", "Error.NoPermission", Boolean.TRUE);
        }

        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(request, false);
        com.topcoder.project.phases.Project phProj = phaseMgr.getPhases(verification.getProject().getId());
        Phase[] phases = phProj.getAllPhases(new Comparators.ProjectPhaseComparer());

        // Obtain an array of all active phases of the project
        Phase[] activePhases = ActionsHelper.getActivePhases(phases);

        // check if registration phase is open
        boolean registrationOpen = false;
        for (int i = 0; i < activePhases.length && !registrationOpen; i++) {
            if (activePhases[i].getPhaseType().getName().equalsIgnoreCase(Constants.REGISTRATION_PHASE_NAME)) {
                registrationOpen = true;
            }
        }

        if (!registrationOpen) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, "Unregistration", "Error.RegistrationClosed", Boolean.TRUE);
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("unregister") != null);

        if (postBack != true) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(),
                    getResources(request));

            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Obtain the instance of the Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);
        Resource[] allProjectResources = ActionsHelper.getAllResourcesForProject(resourceManager,
                verification.getProject());

        boolean found = false;
        boolean hasOtherRoles = false;
        for (int i = 0; i < allProjectResources.length && (!found || !hasOtherRoles); i++) {
            long userId = Long.parseLong(((String) allProjectResources[i].getProperty("External Reference ID")).trim());

            if (userId == AuthorizationHelper.getLoggedInUserId(request)) {
                if (allProjectResources[i].getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                    ActionsHelper.deleteProjectResult(verification.getProject(), userId, allProjectResources[i].getResourceRole().getId());
                    resourceManager.removeResource(allProjectResources[i],
                            Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

                    found = true;
                } else {
                    hasOtherRoles = true;
                }
            }
        }

        String tempStr = (String) verification.getProject().getProperty("Developer Forum ID");
        long forumId = 0;
        if (tempStr != null && tempStr.trim().length() != 0) {
            forumId = Long.parseLong(tempStr, 10);
        }

        // Only remove forum permissions if the user has no roles left.
        if (!hasOtherRoles) {
            ActionsHelper.removeForumPermissions(verification.getProject(), AuthorizationHelper.getLoggedInUserId(request));
            ActionsHelper.removeForumWatch(verification.getProject(), AuthorizationHelper.getLoggedInUserId(request), forumId);
        }

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * This method is an implementation of &quot;EarlyAppeals&quot; Struts Action defined for
     * this assembly, which allows the logged in submitter from a project (denoted by <code>pid</code>
     * parameter) to mark his appeals completed or to resume appealing. This action gets executed twice &#x96;
     * once to display the page with the confirmation, and once to process the confirmed request to
     * actually perform the action.
     *
     * @return an action forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmEarlyAppeals.jsp
     *         If this action was called during the post back (the second time), then this method
     *         verifies if everything is correct, and proceeds with the unregistration.
     *         After this it returns a forward to the View Project Details page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     * @since 1.2
     */
    public ActionForward earlyAppeals(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME, false);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Check the user has submitter role and appeals phase is open
        boolean isSubmitter = Boolean.valueOf(AuthorizationHelper.hasUserRole(request,
                Constants.SUBMITTER_ROLE_NAME));

        if (!isSubmitter) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, "Early Appeals", "Error.NoPermission", Boolean.TRUE);
        }

        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(request, false);
        com.topcoder.project.phases.Project phProj = phaseMgr.getPhases(verification.getProject().getId());
        Phase[] phases = phProj.getAllPhases(new Comparators.ProjectPhaseComparer());

        // Obtain an array of all active phases of the project
        Phase[] activePhases = ActionsHelper.getActivePhases(phases);

        // check if appeals phase is open
        boolean appealsOpen = false;
        for (int i = 0; i < activePhases.length && !appealsOpen; i++) {
            if (activePhases[i].getPhaseType().getName().equalsIgnoreCase(Constants.APPEALS_PHASE_NAME)) {
                appealsOpen = true;
            }
        }

        if (!appealsOpen) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, "Early Appeals", "Error.AppealsClosed", Boolean.TRUE);
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        Resource submitter = ActionsHelper.getMyResourceForRole(request, "Submitter");

        // get appeals completed early property value
        boolean appealsCompletedFlag = false;
        if (submitter != null) {
            String value = (String) submitter.getProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY);
            if (value != null && value.equals(Constants.YES_VALUE)) {
                appealsCompletedFlag = true;
            }
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("perform") != null);
        if (postBack != true) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(),
                    getResources(request));

            request.setAttribute("complete", !appealsCompletedFlag);

            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Get the name (id) of the user performing the operations
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));

        // Obtain the instance of the Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);

        // set appeasl completed early property
        submitter.setProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY,
                appealsCompletedFlag ? Constants.NO_VALUE : Constants.YES_VALUE);

        // update resource
        resourceManager.updateResource(submitter, operator);

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * This method is an implementation of &quot;Download Document&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a review's document from the
     * server.
     *
     * @return a <code>null</code> code if everything went fine, or an action forward to
     *         /jsp/userError.jsp page which will display the information about the cause of error.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     * @throws IOException
     *             if some error occurs during disk input/output operation.
     */
    public ActionForward downloadDocument(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException, IOException {
        LoggingHelper.logAction(request);
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
            checkForCorrectUploadId(mapping, request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Check that user has permissions to download a Document
        if (!AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();

        // Verify that upload is a Review Document
        if (!upload.getUploadType().getName().equalsIgnoreCase("Review Document")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME, "Error.NotADocument", null);
        }
        // Verify the status of upload
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME, "Error.UploadDeleted", null);
        }

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());
        outputDownloadedFile(uploadedFile, "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"",
                             request, response);

        return null;
    }

    /**
     * This method is an implementation of &quot;View Auto Screening&quot; Struts Action defined for
     * this assembly, which is supposed to show the results of auto screening to user.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewAutoScreening.jsp page
     *         (as defined in struts-config.xml file), or &quot;userError&quot; forward, which
     *         forwards to the /jsp/userError.jsp page, which displays information about an error
     *         that is usually caused by incorrect user input (such as absent upload id, or the lack
     *         of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewAutoScreening(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
            checkForCorrectUploadId(mapping, request, "ViewAutoScreening");
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // TODO: Refactor permission check

        // Get an upload to display autoscreening results of
        Upload upload = verification.getUpload();

        // Verify that upload is a submission
        if (!upload.getUploadType().getName().equalsIgnoreCase("Submission")) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, "ViewAutoScreening", "Error.NotASubmission", null);
        }

        // Verify the status of upload and check whether the user has permission to download old uploads
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted") &&
                !AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, "ViewAutoScreening", "Error.UploadDeleted", null);
        }

        boolean noRights = true;

        if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
            noRights = false;
        }

        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");

        if (noRights && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {
            long owningResourceId = upload.getOwner();
            for (int i = 0; i < myResources.length; ++i) {
                if (myResources[i].getId() == owningResourceId) {
                    noRights = false;
                    break;
                }
            }
        }

        if (noRights) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, "ViewAutoScreening", "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), messages);

        // Place a string that represents "my" current role(s) into the request
        ActionsHelper.retrieveAndStoreMyRole(request, getResources(request));

        // Retrieve the submitter id and place it into request
        ActionsHelper.retrieveAndStoreSubmitterInfo(request, upload);

        // Put review type into the request
        request.setAttribute("reviewType", "AutoScreening");
        // Specify that Expand All / Collapse All links are not needed
        request.setAttribute("noExpandCollapse", Boolean.TRUE);

        // Obtain Screening Manager instance
        ScreeningManager screeningManager = ActionsHelper.createScreeningManager(request);

        // Retrieve the automated screening results
        ScreeningTask screeningTask = screeningManager.getScreeningDetails(upload.getId());
        ScreeningResult[] screeningResults = screeningTask.getAllScreeningResults();

        // Group the results according to severity statuses and screening responses
        // Finally the results are grouped into the map where
        // the keys are the ids of severity statuses and the values are another maps,
        // in which keys are ids of screening responses and the values are screening results.
        Map<Long, Map<Long, List<ScreeningResult>>> screeningResultsMap =
                new TreeMap<Long, Map<Long, List<ScreeningResult>>>();
        for (int i = 0; i < screeningResults.length; i++) {
            ResponseSeverity responseSeverity = screeningResults[i].getScreeningResponse().getResponseSeverity();
            // ignore response with "Success" severity
            if (Constants.SUCCESS_SCREENING_SEVERITY_NAME.equalsIgnoreCase(responseSeverity.getName())) {
                continue;
            }
            Long responseSeverityId = new Long(responseSeverity.getId());
            Long screeningResponseId = new Long(screeningResults[i].getScreeningResponse().getId());
            Map<Long, List<ScreeningResult>> innerMap;
            if (screeningResultsMap.containsKey(responseSeverityId)) {
                innerMap = screeningResultsMap.get(responseSeverityId);
            } else {
                innerMap = new TreeMap<Long, List<ScreeningResult>>();
                screeningResultsMap.put(responseSeverityId, innerMap);
            }
            if (innerMap.containsKey(screeningResponseId)) {
                innerMap.get(screeningResponseId).add(screeningResults[i]);
            } else {
                List<ScreeningResult> list = new ArrayList<ScreeningResult>();
                innerMap.put(screeningResponseId, list);
                list.add(screeningResults[i]);
            }
        }
        // Store grouped results in the request
        request.setAttribute("screeningResultsMap", screeningResultsMap);

        // Return success forward
        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method verifies the request for certain conditions to be met. This includes verifying if
     * the user has specified an ID of the upload he wants to perform an operation on (most often
     * &#x96; to download), and whether the ID of the upload specified by user denotes existing
     * upload.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which specifies whether the
     *         check was successful and, in the case it was, contains additional information
     *         retrieved during the check operation, which might be of some use for the calling
     *         method.
     * @param mapping
     *            action mapping.
     * @param request
     *            the http request.
     * @param errorMessageKey
     *            permission to check against, or <code>null</code> if no check is requeired.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if
     *             <code>errorMessageKey</code> parameter is an empty string.
     * @throws BaseException
     *             if any error occurs.
     */
    private CorrectnessCheckResult checkForCorrectUploadId(ActionMapping mapping,
            HttpServletRequest request, String errorMessageKey)
        throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(mapping, "mapping");
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterStringNotEmpty(errorMessageKey, "errorMessageKey");

        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        // Verify that Upload ID was specified and denotes correct upload
        String uidParam = request.getParameter("uid");
        if (uidParam == null || uidParam.trim().length() == 0) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, errorMessageKey, "Error.UploadIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long uid;

        try {
            // Try to convert specified uid parameter to its integer representation
            uid = Long.parseLong(uidParam, 10);
        } catch (NumberFormatException nfe) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, errorMessageKey, "Error.UploadNotFound", null));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        // Get Upload by its ID
        Upload upload = upMgr.getUpload(uid);
        // Verify that upload with given ID exists
        if (upload == null) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, errorMessageKey, "Error.UploadNotFound", null));
            // Return the result of the check
            return result;
        }

        // Store Upload object in the result bean
        result.setUpload(upload);

        // Obtain an instance of Project Manager
        ProjectManager projMgr = ActionsHelper.createProjectManager(request);
        // Get a Project for this upload
        Project project = projMgr.getProject(upload.getProject());

        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, project.getId());

        return result;
    }

    /**
     * <p>Analyzes the current statuses of the specified phases and returns integer codes encoding the current phase
     * statuses. Each </p>
     *
     * @param phases a <code>Phase</code> array listing the phase to get status codes for.
     * @param currentTime a <code>long</code> specifying the current time (in milliseconds since 01/01/1970).
     * @return an <code>int</code> array encoding the statuses for specified phases.
     * @throws IllegalArgumentException if <code>phases</code> parameter is <code>null</code>.
     */
    private static int[] getPhaseStatusCodes(Phase[] phases, long currentTime) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(phases, "phases");

        final long deadlineNear = ConfigHelper.getDeadlineNearDuration() * 60 * 60 * 1000;

        int[] statusCodes = new int[phases.length];
        for (int i = 0; i < phases.length; ++i) {
            // Get a Phase for the current iteration
            Phase phase = phases[i];
            String phaseStatus = phase.getPhaseStatus().getName();
            if (phaseStatus.equalsIgnoreCase("Scheduled")) {
                long phaseTime = phase.getScheduledStartDate().getTime();
                if ((currentTime >= phaseTime) && (ActionsHelper.arePhaseDependenciesMet(phase, true))) {
                    statusCodes[i] = 5; // Can't open, phase start time is reached and dependencies are met
                } else {
                    statusCodes[i] = 0; // Scheduled, not yet open, nothing will be displayed
                }
            } else if (phaseStatus.equalsIgnoreCase("Closed")) {
                statusCodes[i] = 1; // Closed
            } else if (phaseStatus.equalsIgnoreCase("Open")) {
                long phaseTime = phase.getScheduledEndDate().getTime();

                if (currentTime > phaseTime) {
                    statusCodes[i] = 4; // Late
                } else if (currentTime + deadlineNear > phaseTime) {
                    statusCodes[i] = 3; // Closing
                } else {
                    statusCodes[i] = 2; // Open
                }
            }
        }
        return statusCodes;
    }

    /**
     * This static method
     *
     * @return
     * @param deliverables
     * @param activePhases
     * @param currentTime
     */
    private static int[] getDeliverableStatusCodes(Deliverable[] deliverables,
            Phase[] activePhases, long currentTime) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(deliverables, "deliverables");
        ActionsHelper.validateParameterNotNull(activePhases, "activePhases");

        int[] statusCodes = new int[deliverables.length];
        for (int i = 0; i < deliverables.length; ++i) {
            // Get a Deliverable for the current iteration
            Deliverable deliverable = deliverables[i];
            if (deliverable.isComplete()) {
                statusCodes[i] = 0;
            } else {
                Phase phase = ActionsHelper.getPhaseForDeliverable(activePhases, deliverable);

                long deliverableTime = phase.getScheduledEndDate().getTime();
                if (currentTime > deliverableTime) {
                    statusCodes[i] = 2; // Late
                } else if (currentTime + (2 * 60 * 60 * 1000) > deliverableTime) {
                    statusCodes[i] = 1; // Deadline near
                } else {
                    statusCodes[i] = 0;
                }
            }
        }
        return statusCodes;
    }

    /**
     * This static method analyzes the array of deliverables and generates links for them.
     *
     * @return an array of links. If some deliverable does not have its appropriate link, then that
     *         entry in the returned array will be empty (containing <code>null</code> value).
     * @param request
     *            an <code>HttpServletRequest</code> object.
     * @param deliverables
     *            an array of deliverables to generate the links for.
     * @param phases
     *            an array of all phases for the current project.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     */
    private static String[] generateDeliverableLinks(HttpServletRequest request,
            Deliverable[] deliverables, Phase[] phases)
        throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(deliverables, "deliverables");
        ActionsHelper.validateParameterNotNull(phases, "phases");

        String[] links = new String[deliverables.length];

        ScorecardType[] allScorecardTypes = null;

        for (int i = 0; i < deliverables.length; ++i) {
            // Get a Deliverable for the current iteration
            Deliverable deliverable = deliverables[i];
            String delivName = deliverable.getName();
            if (delivName.equalsIgnoreCase(Constants.SUBMISSION_DELIVERABLE_NAME)) {
                links[i] = "UploadContestSubmission.do?method=uploadContestSubmission&pid=" + deliverable.getProject();
            } else if (delivName.equalsIgnoreCase(Constants.SPECIFICATION_SUBMISSION_DELIVERABLE_NAME)) {
                if (!deliverable.isComplete()) {
                    links[i] = "UploadSpecificationSubmission.do?method=uploadSpecificationSubmission&pid="
                               + deliverable.getProject();
                }
            } else if (delivName.equalsIgnoreCase(Constants.SPECIFICATION_REVIEW_DELIVERABLE_NAME)) {
                if (deliverable.getSubmission() == null) {
                    continue;
                } else if (allScorecardTypes == null) {
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(request),
                        ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Specification Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateSpecificationReview.do?method=createSpecificationReview&sid="
                               + deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditSpecificationReview.do?method=editSpecificationReview&rid=" + review.getId();
                } else {
                    links[i] = "ViewSpecificationReview.do?method=viewSpecificationReview&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.SCREENING_DELIVERABLE_NAME) ||
                    delivName.equalsIgnoreCase(Constants.PRIMARY_SCREENING_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                if (allScorecardTypes == null) {
                    // Get all scorecard types
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(request),
                        ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Screening"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateScreening.do?method=createScreening&sid=" +
                            deliverable.getSubmission().longValue();
                } else if (!review.isCommitted()) {
                    links[i] = "EditScreening.do?method=editScreening&rid=" + review.getId();
                } else {
                    links[i] = "ViewScreening.do?method=viewScreening&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.REVIEW_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                if (allScorecardTypes == null) {
                    // Get all scorecard types
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(request),
                        ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateReview.do?method=createReview&sid=" +
                            deliverable.getSubmission().longValue();
                } else if (!review.isCommitted()) {
                    links[i] = "EditReview.do?method=editReview&rid=" + review.getId();
                } else {
                    links[i] = "ViewReview.do?method=viewReview&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.ACC_TEST_CASES_DELIVERABLE_NAME) ||
                    delivName.equalsIgnoreCase(Constants.FAIL_TEST_CASES_DELIVERABLE_NAME) ||
                    delivName.equalsIgnoreCase(Constants.STRS_TEST_CASES_DELIVERABLE_NAME)) {
                links[i] = "UploadTestCase.do?method=uploadTestCase&pid=" + deliverable.getProject();
            } else if (delivName.equalsIgnoreCase(Constants.APPEAL_RESP_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                if (allScorecardTypes == null) {
                    // Get all scorecard types
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(request),
                        ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                // If review for the submission is absent, something is wrong
                // and no link for this deliverable can be generated
                if (review != null) {
                    links[i] = "ViewReview.do?method=viewReview&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.AGGREGATION_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                if (allScorecardTypes == null) {
                    // Get all scorecard types
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(request),
                        ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review != null) {
                    if (!review.isCommitted()) {
                        links[i] = "EditAggregation.do?method=editAggregation&rid=" + review.getId();
                    } else {
                        links[i] = "ViewAggregation.do?method=viewAggregation&rid=" + review.getId();
                    }
                }
            } else if (delivName.equalsIgnoreCase(Constants.AGGREGATION_REV_DELIVERABLE_NAME) ||
                    delivName.equalsIgnoreCase(Constants.SCORECARD_COMM_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Phase phase = ActionsHelper.getPhase(phases, false, Constants.AGGREGATION_PHASE_NAME);
                Resource[] aggregator =
                    ActionsHelper.getAllResourcesForPhase(ActionsHelper.createResourceManager(request), phase);

                if (allScorecardTypes == null) {
                    // Get all scorecard types
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(request),
                        ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Review"),
                        deliverable.getSubmission(), aggregator[0].getId(), true);

                if (review == null) {
                    continue;
                }

                Comment myComment = null;

                for (int j = 0; j < review.getNumberOfComments(); ++j) {
                    if (review.getComment(j).getAuthor() == deliverable.getResource()) {
                        myComment = review.getComment(j);
                        break;
                    }
                }

                if (myComment == null ||
                        !("Approved".equalsIgnoreCase((String) myComment.getExtraInfo()) ||
                        "Rejected".equalsIgnoreCase((String) myComment.getExtraInfo()))) {
                    links[i] = "EditAggregationReview.do?method=editAggregationReview&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.FINAL_FIX_DELIVERABLE_NAME)) {
                links[i] = "UploadFinalFix.do?method=uploadFinalFix&pid=" + deliverable.getProject();
            } else if (delivName.equalsIgnoreCase(Constants.FINAL_REVIEW_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                if (allScorecardTypes == null) {
                    // Get all scorecard types
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(request),
                        ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review != null) {
                    if (!review.isCommitted()) {
                        links[i] = "EditFinalReview.do?method=editFinalReview&rid=" + review.getId();
                    } else {
                        links[i] = "ViewFinalReview.do?method=viewFinalReview&rid=" + review.getId();
                    }
                }
            } else if (delivName.equalsIgnoreCase(Constants.APPROVAL_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                if (allScorecardTypes == null) {
                    // Get all scorecard types
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                Phase[] activePhases = ActionsHelper.getActivePhases(phases);
                Phase phase = ActionsHelper.getPhaseForDeliverable(activePhases, deliverable);
                Review review = ActionsHelper.findLastApprovalReview(ActionsHelper.createReviewManager(request), phase,
                    ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Approval"), deliverable.getResource(),
                    false);

                if (review == null) {
                    links[i] = "CreateApproval.do?method=createApproval&sid=" +
                            deliverable.getSubmission().longValue();
                } else if (!review.isCommitted()) {
                    links[i] = "EditApproval.do?method=editApproval&rid=" + review.getId();
                } else {
                    links[i] = "ViewApproval.do?method=viewApproval&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.POST_MORTEM_DELIVERABLE_NAME)) {
                if (allScorecardTypes == null) {
                    // Get all scorecard types
                    allScorecardTypes = ActionsHelper.createScorecardManager(request).getAllScorecardTypes();
                }

                ScorecardType scorecardType = ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Post-Mortem");
                Review review = findReviewForProject(ActionsHelper.createReviewManager(request), scorecardType,
                                                     deliverable.getProject(), deliverable.getResource(), false);
                if (review == null) {
                    links[i] = "CreatePostMortem.do?method=createPostMortem&pid=" + deliverable.getProject();
//                    request.setAttribute("postMortemSubmissionId", deliverable.getSubmission().longValue());
                } else if (!review.isCommitted()) {
                    links[i] = "EditPostMortem.do?method=editPostMortem&rid=" + review.getId();
                } else {
                    links[i] = "ViewPostMortem.do?method=viewPostMortem&rid=" + review.getId();
                }
            }
        }

        return links;
    }

    /**
     * TODO: Doccument this method.
     *
     * @return
     * @param deliverables
     * @param resources
     * @throws BaseException
     */
    private static String[] getDeliverableUserIds(Deliverable[] deliverables, Resource[] resources) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(deliverables, "deliverables");
        ActionsHelper.validateParameterNotNull(resources, "resources");

        String[] ids = new String[deliverables.length];

        for (int i = 0; i < deliverables.length; ++i) {
            final long deliverableResourceId = deliverables[i].getResource();
            for (int j = 0; j < resources.length; ++j) {
                if (resources[j].getId() == deliverableResourceId) {
                    ids[i] = (String) resources[j].getProperty("External Reference ID");
                    break;
                }
            }
        }

        return ids;
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @return
     * @param request
     * @param deliverables
     * @throws BaseException
     */
    private static String[] getDeliverableSubmissionUserIds(HttpServletRequest request, Deliverable[] deliverables)
            throws BaseException {

        List<Long> submissionIds = new ArrayList<Long>();

        for (int i = 0; i < deliverables.length; ++i) {
            if (deliverables[i].getSubmission() != null) {
                submissionIds.add(deliverables[i].getSubmission());
            }
        }

        if (submissionIds.isEmpty()) {
            return new String[0];
        }

        Filter filterSubmissions = new InFilter("submission_id", submissionIds);

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        Submission[] submissions = upMgr.searchSubmissions(filterSubmissions);

        List<Long> resourceIds = new ArrayList<Long>();

        for (int i = 0; i < submissions.length; ++i) {
            resourceIds.add(submissions[i].getUpload().getOwner());
        }

        Filter filterResources = new InFilter("resource.resource_id", resourceIds);

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        Resource[] resources = resMgr.searchResources(filterResources);

        String[] ids = new String[deliverables.length];

        for (int i = 0; i < deliverables.length; ++i) {
            if (deliverables[i].getSubmission() == null) {
                continue;
            }
            long deliverableId = deliverables[i].getSubmission().longValue();
            for (int j = 0; j < submissions.length; ++j) {
                if (submissions[j].getId() != deliverableId) {
                    continue;
                }
                long submissionOwnerId = submissions[j].getUpload().getOwner();
                for (int k = 0; k < resources.length; ++k) {
                    if (resources[k].getId() == submissionOwnerId) {
                        ids[i] = (String) resources[k].getProperty("External Reference ID");
                        break;
                    }
                }
                break;
            }
        }

        return ids;
    }

    /**
     * This static method finds and returns a review of specified scorecard template type for
     * specified submission ID and made by specified resource.
     *
     * @return found review or <code>null</code> if no review has been found.
     * @param manager
     *            an instance of <code>ReviewManager</code> class that retrieves a review from the
     *            database.
     * @param scorecardType
     *            a scorecard template type that found review should have.
     * @param submissionId
     *            an ID of the submission which the review was made for.
     * @param resourceId
     *            an ID of the resource who made (created) the review.
     * @param complete
     *            specifies whether retrieved review should have all infomration (like all items and
     *            their comments).
     * @throws IllegalArgumentException
     *             if <code>scorecardType</code> or <code>submissionId</code> parameters are
     *             <code>null</code>, or if <code>submissionId</code> or
     *             <code>resourceId</code> parameters contain negative value or zero.
     * @throws ReviewManagementException
     *             if any error occurs during review search or retrieval.
     */
    private static Review findReviewForSubmission(ReviewManager manager,
            ScorecardType scorecardType, Long submissionId, long resourceId, boolean complete)
        throws ReviewManagementException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(manager, "manager");
        ActionsHelper.validateParameterNotNull(scorecardType, "scorecardType");
        ActionsHelper.validateParameterNotNull(submissionId, "submissionId");
        ActionsHelper.validateParameterPositive(submissionId.longValue(), "submissionId");
        ActionsHelper.validateParameterPositive(resourceId, "resourceId");

        Filter filterSubmission = new EqualToFilter("submission", submissionId);
        Filter filterScorecard = new EqualToFilter("scorecardType", new Long(scorecardType.getId()));
        Filter filterReviewer = new EqualToFilter("reviewer", new Long (resourceId));

        Filter filter = new AndFilter(Arrays.asList(
                new Filter[] {filterSubmission, filterScorecard, filterReviewer}));

        // Get a review(s) that pass filter
        Review[] reviews = manager.searchReviews(filter, complete);
        // Return the first found review if any, or null
        return (reviews.length != 0) ? reviews[0] : null;
    }

    /**
     * This static method finds and returns a review of specified scorecard template type for
     * specified submission ID and made by specified resource.
     *
     * @return found review or <code>null</code> if no review has been found.
     * @param manager
     *            an instance of <code>ReviewManager</code> class that retrieves a review from the
     *            database.
     * @param scorecardType
     *            a scorecard template type that found review should have.
     * @param projectId
     *            an ID of the project which the review was made for.
     * @param resourceId
     *            an ID of the resource who made (created) the review.
     * @param complete
     *            specifies whether retrieved review should have all infomration (like all items and
     *            their comments).
     * @throws IllegalArgumentException
     *             if <code>scorecardType</code> or <code>submissionId</code> parameters are
     *             <code>null</code>, or if <code>submissionId</code> or
     *             <code>resourceId</code> parameters contain negative value or zero.
     * @throws ReviewManagementException
     *             if any error occurs during review search or retrieval.
     */
    private static Review findReviewForProject(ReviewManager manager,
            ScorecardType scorecardType, Long projectId, long resourceId, boolean complete)
        throws ReviewManagementException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(manager, "manager");
        ActionsHelper.validateParameterNotNull(scorecardType, "scorecardType");
        ActionsHelper.validateParameterNotNull(projectId, "projectId");
        ActionsHelper.validateParameterPositive(projectId.longValue(), "projectId");
        ActionsHelper.validateParameterPositive(resourceId, "resourceId");

        Filter filterProject = new EqualToFilter("project", projectId);
        Filter filterScorecard = new EqualToFilter("scorecardType", new Long(scorecardType.getId()));
        Filter filterReviewer = new EqualToFilter("reviewer", new Long (resourceId));

        Filter filter = new AndFilter(Arrays.asList(filterProject, filterScorecard, filterReviewer));

        // Get a review(s) that pass filter
        Review[] reviews = manager.searchReviews(filter, complete);

        // Return the first found review if any, or null
        return (reviews.length != 0) ? reviews[0] : null;
    }

    /**
     * <p>Sends the content of specified file for downloading by client.</p>
     *
     * @param upload an <code>Upload</code> providing the details for the filr to be downloaded by client.
     * @param request an <code>HttpServletRequest</code> representing the incoming request.
     * @param response an <code>HttpServletResponse</code> representing the outgoing response.
     * @throws UploadPersistenceException if an unexpected error occurs.
     * @throws SearchBuilderException if an unexpected error occurs.
     * @throws DisallowedDirectoryException if an unexpected error occurs.
     * @throws ConfigurationException if an unexpected error occurs.
     * @throws PersistenceException if an unexpected error occurs.
     * @throws FileDoesNotExistException if an unexpected error occurs.
     * @throws IOException if an unexpected error occurs.
     * @since 1.6
     */
    private void processSubmissionDownload(Upload upload, HttpServletRequest request, HttpServletResponse response)
        throws UploadPersistenceException, SearchBuilderException, DisallowedDirectoryException,
               ConfigurationException, PersistenceException, FileDoesNotExistException, IOException {

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(upload.getProject());
        Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(upload.getOwner());
        Filter filterUpload = SubmissionFilterBuilder.createUploadIdFilter(upload.getId());

        Filter filter = new AndFilter(Arrays.asList(filterProject, filterResource, filterUpload));

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());

        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        Submission[] submissions = upMgr.searchSubmissions(filter);
        Submission submission = (submissions.length != 0) ? submissions[0] : null;

        String contentDisposition;
        if (submission != null) {
            contentDisposition = "attachment; filename=\"submission-" + submission.getId() + "-"
                                 + uploadedFile.getRemoteFileName() + "\"";
        } else {
            contentDisposition = "attachment; filename=\"upload-" + upload.getId() + "-"
                                 + uploadedFile.getRemoteFileName() + "\"";
        }

        outputDownloadedFile(uploadedFile, contentDisposition, request, response);
    }

    /**
     *
     * <p>Outputs the content of specified file to specified response for downloading by client.</p>
     *
     * @param uploadedFile an <code>UploadedFile</code> providing the details for the filr to be downloaded by client.
     * @param contentDisposition a <code>String</code> providing the value for <code>Content-Disposition</code> header.
     * @param request an <code>HttpServletRequest</code> representing the incoming request.
     * @param response an <code>HttpServletResponse</code> representing the outgoing response.
     * @throws DisallowedDirectoryException if an unexpected error occurs.
     * @throws ConfigurationException if an unexpected error occurs.
     * @throws PersistenceException if an unexpected error occurs.
     * @throws FileDoesNotExistException if an unexpected error occurs.
     * @throws IOException if an unexpected error occurs.
     * @since 1.6
     */
    private void outputDownloadedFile(UploadedFile uploadedFile, String contentDisposition, HttpServletRequest request,
                                      HttpServletResponse response)
        throws DisallowedDirectoryException, ConfigurationException, PersistenceException, FileDoesNotExistException,
               IOException {

        InputStream in = uploadedFile.getInputStream();

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) uploadedFile.getSize());
        response.setHeader("Content-Disposition", contentDisposition);

        response.flushBuffer();

        OutputStream out = null;

        try {
            out = response.getOutputStream();
            byte[] buffer = new byte[65536];

            for (;;) {
                int numOfBytesRead = in.read(buffer);
                if (numOfBytesRead == -1) {
                    break;
                }
                out.write(buffer, 0, numOfBytesRead);
            }
        } finally {
            in.close();
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Checks whether to show the 'Pay Project' button in OR. The following criteria is used.
     * <ul>
     * <li>The user has permissions to pay the project</li>
     * <li>The project has at least one resource with non-zero Payment amount value and payment status other than
     * 'Paid'.</li>
     * </ul>
     *
     * @param request
     *            the http servlet request
     * @param allProjectResources
     *            the array of all resources of this project.
     * @return true to show the 'Pay Project' button in OR, otherwise false.
     * @since Online Review Payments and Status Automation Assembly 1.0
     */
    private static boolean isAllowedToPay(HttpServletRequest request, Resource[] allProjectResources) {
        boolean hasUserPermission = AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PAYMENT_PERM_NAME);

        if (!hasUserPermission) {
            return false;
        }

        for (Resource resource : allProjectResources) {

            String paymentStr = (String) resource.getProperty("Payment");

            try {
            if (paymentStr != null && paymentStr.trim().length() != 0 && Double.parseDouble(paymentStr) > 0
                    && !"Yes".equals(resource.getProperty("Payment Status"))) {
                return true;
            }

            } catch (NumberFormatException e) {
                // the payment string is not double format, we simply ignore it.
                continue;
            }

        }

        return false;
    }
}

