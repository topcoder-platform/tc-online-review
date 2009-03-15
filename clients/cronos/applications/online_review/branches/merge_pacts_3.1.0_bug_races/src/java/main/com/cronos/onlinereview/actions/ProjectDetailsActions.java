/*
 * Copyright (C) 2006-2007 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
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
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
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
 * @author George1
 * @author real_vg
 * @version 1.0
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

        long componentId = -1;
        long versionId = -1;
        long forumId = -1;
        String tempStr;

        tempStr = (String) project.getProperty("Component ID");
        if (tempStr != null && tempStr.trim().length() != 0) {
            componentId = Long.parseLong(tempStr, 10);
        }

        tempStr = (String) project.getProperty("Version ID");
        if (tempStr != null && tempStr.trim().length() != 0) {
            versionId = Long.parseLong(tempStr, 10);
        }

        tempStr = (String) project.getProperty("Developer Forum ID");
        if (tempStr != null && tempStr.trim().length() != 0) {
            forumId = Long.parseLong(tempStr, 10);
        }

        request.setAttribute("descriptionLink",
                ConfigHelper.getProjectTypeDescriptionLink(projectTypeName, componentId, versionId));
        request.setAttribute("forumLink", ConfigHelper.getProjectTypeForumLink(projectTypeName, forumId));
		request.setAttribute("projectType", projectTypeName);
		request.setAttribute("projectCategory", project.getProjectCategory().getName());
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
		
		

        // Place a string that represents "my" current role(s) into the request
        ActionsHelper.retrieveAndStoreMyRole(request, getResources(request));
        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        // Place an information about the amount of "my" payment into the request
        request.setAttribute("myPayment", ActionsHelper.determineMyPayment(myResources));
        // Place an information about my payment status into the request
        request.setAttribute("wasPaid", ActionsHelper.determineMyPaymentPaid(myResources));

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
        List scorecardTemplates = new ArrayList();

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

            // Get a scorecard template associated with this phase if any
            Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                    ActionsHelper.createScorecardManager(request), phase);
            // If there is a scorecard template for the phase, store it in the list
            if (scorecardTemplate != null) {
                scorecardTemplates.add(scorecardTemplate);
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
        request.setAttribute("scorecardTemplates", scorecardTemplates);

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
        request.setAttribute("passingMinimum", new Float(75.0)); // TODO: Take this value from scorecard template

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
        // Check permissions
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
                                ActionsHelper.getPhase(phases, true, Constants.APPEALS_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.APPEALS_RESPONSE_PHASE_NAME) != null)));
        request.setAttribute("isAllowedToUploadTC",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.UPLOAD_TEST_CASES_PERM_NAME)));
        request.setAttribute("isAllowedToPerformAggregation",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME)));
        request.setAttribute("isAllowedToUploadFF",
                Boolean.valueOf(AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_FIX_PERM_NAME) &&
                        AuthorizationHelper.getLoggedInUserId(request) == winnerExtUserId));
        request.setAttribute("isAllowedToPerformFinalReview",
                Boolean.valueOf(ActionsHelper.getPhase(phases, true, Constants.FINAL_REVIEW_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME)));
        request.setAttribute("isAllowedToPerformApproval",
                Boolean.valueOf(ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPROVAL_PERM_NAME)));

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
        
        Filter filterRole = ResourceFilterBuilder.createResourceRoleIdFilter(
        		ActionsHelper.findResourceRoleByName(allResourceRoles, "Manager").getId());
        
        // Build final filter
        Filter filter = new AndFilter(filterProject, filterRole);
        // Search for the managers of this project
        Resource[] managers = resMgr.searchResources(filter);

        Set existingManagers = new HashSet();

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
        for (Iterator iter = existingManagers.iterator(); iter.hasNext(); ) {
            extUsrManagerIds[managerIdx++] = Long.parseLong((String) iter.next());
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
        message.setSubject(ConfigHelper.getContactManagerEmailSubject());
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
    public ActionForward uploadSubmission(ActionMapping mapping, ActionForm form,
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
    public ActionForward downloadSubmission(ActionMapping mapping, ActionForm form,
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
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(upload.getProject());
        Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(upload.getOwner());
        Filter filterUpload = SubmissionFilterBuilder.createUploadIdFilter(upload.getId());

        Filter filter = new AndFilter(Arrays.asList(new Filter[] {filterProject, filterResource, filterUpload}));

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        Submission[] submissions = upMgr.searchSubmissions(filter);
        Submission submission = (submissions.length != 0) ? submissions[0] : null;

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());

        InputStream in = uploadedFile.getInputStream();

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) uploadedFile.getSize());
        if (submission != null) {
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"submission-" + submission.getId() +
                    "-" + uploadedFile.getRemoteFileName() + "\"");
        } else {
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"upload-" + upload.getId() +
                    "-" + uploadedFile.getRemoteFileName() + "\"");
        }

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

        InputStream in = uploadedFile.getInputStream();

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) uploadedFile.getSize());
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"");

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

        InputStream in = uploadedFile.getInputStream();

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) uploadedFile.getSize());
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"");

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

        InputStream in = uploadedFile.getInputStream();

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) uploadedFile.getSize());
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"");

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
        Map screeningResultsMap = new TreeMap();
        for (int i = 0; i < screeningResults.length; i++) {
            ResponseSeverity responseSeverity = screeningResults[i].getScreeningResponse().getResponseSeverity();
            // ignore response with "Success" severity
            if (Constants.SUCCESS_SCREENING_SEVERITY_NAME.equalsIgnoreCase(responseSeverity.getName())) {
            	continue;
            }
            Long responseSeverityId = new Long(responseSeverity.getId());
            Long screeningResponseId = new Long(screeningResults[i].getScreeningResponse().getId());
            Map innerMap;
            if (screeningResultsMap.containsKey(responseSeverityId)) {
                innerMap = (Map) screeningResultsMap.get(responseSeverityId);
            } else {
                innerMap = new TreeMap();
                screeningResultsMap.put(responseSeverityId, innerMap);
            }
            if (innerMap.containsKey(screeningResponseId)) {
                ((List) innerMap.get(screeningResponseId)).add(screeningResults[i]);
            } else {
                List list = new ArrayList();
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
     * This method verifies the request for ceratin conditions to be met. This includes verifying if
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
     * @param permission
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
     * TODO: Write docummentation for this method.
     *
     * @return
     * @param phases
     * @param currentTime
     * @throws IllegalArgumentException
     *             if <code>phases</code> parameter is <code>null</code>.
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
                statusCodes[i] = 0; // Scheduled, not yet open, nothing will be displayed
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
                links[i] = "UploadSubmission.do?method=uploadSubmission&pid=" + deliverable.getProject();
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

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(request),
                        ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Client Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateApproval.do?method=createApproval&sid=" +
                            deliverable.getSubmission().longValue();
                } else if (!review.isCommitted()) {
                    links[i] = "EditApproval.do?method=editApproval&rid=" + review.getId();
                } else {
                    links[i] = "ViewApproval.do?method=viewApproval&rid=" + review.getId();
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
        List submissionIds = new ArrayList();

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

        List resourceIds = new ArrayList();

        for (int i = 0; i < submissions.length; ++i) {
            resourceIds.add(new Long(submissions[i].getUpload().getOwner()));
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
}
