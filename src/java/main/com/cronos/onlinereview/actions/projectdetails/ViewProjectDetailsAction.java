/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.dataaccess.ProjectDataAccess;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.model.ClientProject;
import com.cronos.onlinereview.model.CockpitProject;
import com.cronos.onlinereview.model.PhasesDetails;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.Comparators;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.cronos.onlinereview.util.PhasesDetailsServices;
import com.opensymphony.xwork2.TextProvider;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Prize;
import com.topcoder.management.project.PrizeType;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.management.resource.Notification;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.shared.util.ApplicationServer;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for rendering the project details page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ViewProjectDetailsAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -3704837880945145746L;

    /**
     * Creates a new instance of the <code>ViewProjectDetailsAction</code> class.
     */
    public ViewProjectDetailsAction() {
    }

    /**
     * This method is an implementation of &quot;View project Details&quot; Struts Action defined
     * for this assembly, which is supposed to gather all possible information about the project and
     * display it to user.
     *
     * @return a string forward to the appropriate page. If no error has occurred, the forward will
     *         be to viewProjectDetails.jsp page.
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                this, request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Retrieve a review to view
        Project project = verification.getProject();

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, project, this);

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

        // check if this project uses Thurgood
        if (!isEmptyOrNull(project.getProperty("Thurgood Platform")) && !isEmptyOrNull(project.getProperty("Thurgood Language")) ) {
            request.setAttribute("isThurgood", true);
            request.setAttribute("thurgoodBaseUIURL", ConfigHelper.getThurgoodJobBaseUIURL());
        }

        boolean digitalRunFlag = "On".equals(project.getProperty("Digital Run Flag"));

        request.setAttribute("projectDRFlag", digitalRunFlag?"Yes":"No");

        String drpointStr = project.getProperty("DR points") == null ? "0" : project.getProperty("DR points").toString();
        if (digitalRunFlag) {
            double drpoint = 0;
            if (drpointStr != null) {
                drpoint = Double.parseDouble(drpointStr);
            }
            request.setAttribute("projectDRP", drpoint);
        }

        ProjectDataAccess projectDataAccess = new ProjectDataAccess();

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
                ClientProject cp = projectDataAccess.getClientProject(billingProjectId);
                request.setAttribute("billingProject", cp != null ? cp.getName() : "");
            } else {
                request.setAttribute("billingProject", "");
            }
        }
        boolean isAllowedToViewCockpitProjectName
                = AuthorizationHelper.hasUserPermission(request, Constants.VIEW_COCKPIT_PROJECT_NAME_PERM_NAME);
        request.setAttribute("isAllowedToViewCockpitProjectName", isAllowedToViewCockpitProjectName);
        if (isAllowedToViewCockpitProjectName) {
            CockpitProject cp = projectDataAccess.getCockpitProject(project.getTcDirectProjectId());
            request.setAttribute("cockpitProject", cp != null ? cp.getName() : "");
            request.setAttribute("cockpitProjectLink", ConfigHelper.getDirectProjectBaseURL() + project.getTcDirectProjectId());
        }

        // Place a string that represents "my" current role(s) into the request
        ActionsHelper.retrieveAndStoreMyRole(request, this);
        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        List<ProjectPayment> allPayments = ActionsHelper.createProjectPaymentManager().search(
                ProjectPaymentFilterBuilder.createProjectIdFilter(projectId));
        // Place an information about the amount of "my" payment into the request
        Map<ResourceRole, Double> myPayments = new LinkedHashMap<ResourceRole, Double>();
        Map<ResourceRole, Boolean> myPaymentsPaid = new LinkedHashMap<ResourceRole, Boolean>();
        ActionsHelper.getMyPayments(myResources, allPayments, myPayments, myPaymentsPaid);

        double totalPayment = 0;
        request.setAttribute("myPayment", myPayments);
        for (Double payment : myPayments.values()) {
            if (payment != null) {
                totalPayment += payment;
            }
        }
        request.setAttribute("totalPayment", totalPayment);
        // Place an information about my payment status into the request
        request.setAttribute("wasPaid", myPaymentsPaid);

        // calculate resources' payments amount
        Map<Long, Double> resourcePaymentsAmount = new HashMap<Long, Double>();
        for (ProjectPayment payment : allPayments) {
            Double oldPayment = resourcePaymentsAmount.get(payment.getResourceId());
            if (oldPayment == null) {
                oldPayment = 0.0;
            }
            resourcePaymentsAmount.put(payment.getResourceId(), oldPayment + payment.getAmount().doubleValue());
        }
        request.setAttribute("resourcePaymentsAmount", resourcePaymentsAmount);

        // Retrieve late records for the current user.
        LateDeliverableManager lateDeliverableManager = ActionsHelper.createLateDeliverableManager();
        if (myResources.length > 0) {
            List<Filter> filters = new ArrayList<Filter>();

            filters.add(LateDeliverableFilterBuilder.createProjectIdFilter(projectId));
            filters.add(LateDeliverableFilterBuilder.createForgivenFilter(false));
            filters.add(LateDeliverableFilterBuilder.createUserHandleFilter((String) myResources[0].getProperty("Handle")));

            List<LateDeliverable> lateDeliverables = lateDeliverableManager.searchAllLateDeliverables(new AndFilter(filters));
            long delay = 0, rejectedFinalFixes = 0;
            boolean potentialPenalty = false;
            for(LateDeliverable lateDeliverable : lateDeliverables) {
                if (lateDeliverable.getType().getId() == Constants.MISSED_DEADLINE_ID) {
                    delay += lateDeliverable.getDelay() != null ? lateDeliverable.getDelay() : 0;
                } else if (lateDeliverable.getType().getId() == Constants.REJECTED_FINAL_FIX_ID) {
                    rejectedFinalFixes++;
                }

                // Check if the late deliverable can still be set as "Justified".
                if ((lateDeliverable.getExplanation()!=null && lateDeliverable.getResponse()==null) ||
                    ActionsHelper.explanationDeadline(lateDeliverable).compareTo(new Date()) > 0) {
                    potentialPenalty = true;
                }
            }
            request.setAttribute("myDelay", delay);
            request.setAttribute("potentialPenalty", potentialPenalty);

            long paymentPenaltyPercentage = (delay>0 ? 5 : 0) + (delay/3600) + rejectedFinalFixes * 5;
            if (paymentPenaltyPercentage > 50) {
                paymentPenaltyPercentage = 50;
            }
            request.setAttribute("paymentPenaltyPercentage", paymentPenaltyPercentage);
        }

        // Get an array of all resources for the project
        Resource[] allProjectResources = ActionsHelper.getAllResourcesForProject(project);
        ActionsHelper.populateEmailProperty(request, allProjectResources);

        // Obtain an instance of Phase Manager
        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(true);
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

        Deliverable[] deliverables = ActionsHelper.getAllDeliverablesForPhases(activePhases, allProjectResources);

        Deliverable[] myDeliverables = ActionsHelper.getMyDeliverables(deliverables, myResources);
        Deliverable[] outstandingDeliverables = ActionsHelper.getOutstandingDeliverables(deliverables);

        request.setAttribute("myDeliverables", myDeliverables);
        request.setAttribute("outstandingDeliverables", outstandingDeliverables);

        request.setAttribute("unrespondedLateDeliverables", false);
        if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_LATE_DELIVERABLE_PERM_NAME)) {
            List<Filter> filters = new ArrayList<Filter>();

            filters.add(LateDeliverableFilterBuilder.createProjectIdFilter(projectId));
            filters.add(LateDeliverableFilterBuilder.createHasExplanationFilter(true));
            filters.add(LateDeliverableFilterBuilder.createHasResponseFilter(false));
            filters.add(LateDeliverableFilterBuilder.createForgivenFilter(false));

            List<LateDeliverable> lateDeliverables = lateDeliverableManager.searchAllLateDeliverables(new AndFilter(filters));
            if (lateDeliverables.size() > 0) {
                request.setAttribute("unrespondedLateDeliverables", true);

                request.setAttribute("unrespondedLateDeliverablesLink", "ViewLateDeliverables?project_id=" +
                                     projectId + "&forgiven=Not+forgiven&explanation_status=true&response_status=false");
            }
        }

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
        String[] outstandingDeliverableSubmissionUserIds = getDeliverableSubmissionUserIds(outstandingDeliverables);

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
        long projectEndTime = projectStartTime; // The project end date will be set as the max phase's end date.

        // The following two arrays are used to display Gantt chart
        long[] ganttOffsets = new long[phases.length];
        long[] ganttLengths = new long[phases.length];

        // List of scorecard templates used for this project
        Map<String, Scorecard> phaseScorecardTemplates = new LinkedHashMap<String, Scorecard>();
        Map<String, String> phaseScorecardLinks = new LinkedHashMap<String, String>();
        // Iterate over all phases determining dates, durations and assigned scorecards
        for (int i = 0; i < phases.length; ++i) {
            // Get a phase for this iteration
            Phase phase = phases[i];

            Date startDate = phase.getScheduledStartDate();
            Date endDate = phase.getScheduledEndDate();

            // Get times in minutes
            long startTime = startDate.getTime() / (60 * 1000);
            long endTime = endDate.getTime() / (60 * 1000);

            if (endTime > projectEndTime) {
                projectEndTime = endTime;
            }

            // Determine the dates to display for start/end dates
            originalStart[i] = startDate;
            originalEnd[i] = endDate;

            // Determine offsets and lengths of the bars in Gantt chart, in minutes
            ganttOffsets[i] = startTime - projectStartTime;
            ganttLengths[i] = endTime - startTime;

            String phaseTypeName = phase.getPhaseType().getName();
            // Get a scorecard template associated with this phase if any
            Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(phase, true);
            // If there is a scorecard template for the phase, store it in the list
            if (scorecardTemplate != null) {
                // override the previous scorecard, here assume the phases are ordered sequentially.
                phaseScorecardTemplates.put(phaseTypeName, scorecardTemplate);
                phaseScorecardLinks.put(phaseTypeName, "ViewScorecard?scid=" + scorecardTemplate.getId());
            }
        }

        long currentTimeInMinutes = (new Date()).getTime() / (60 * 1000);
        if (currentTimeInMinutes >= projectStartTime && currentTimeInMinutes <= projectEndTime) {
            request.setAttribute("ganttCurrentTime", currentTimeInMinutes - projectStartTime);
        }

        // Collect Open / Closing / Late / Closed codes for phases
        int[] phaseStatuseCodes = getPhaseStatusCodes(phases, currentTime);
        String[] cannotOpenHints = getPhaseCannotOpenHints(phaseMgr, phases, phaseStatuseCodes, this);

        /*
         * Place all gathered information about phases into the request as attributes
         */

        // Place phases' start/end dates

        request.setAttribute("originalStart", originalStart);
        request.setAttribute("originalEnd", originalEnd);
        request.setAttribute("phaseStatuseCodes", phaseStatuseCodes);
        request.setAttribute("cannotOpenHints", cannotOpenHints);
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

        // Project Prizes
        List<Prize> contestPrizes = new ArrayList<Prize>();
        List<Prize> checkpointPrizes = new ArrayList<Prize>();
        if (project.getPrizes() != null) {
            PrizeType contestPrizeType = LookupHelper.getPrizeType(Constants.CONTEST_PRIZE_TYPE_NAME);
            PrizeType checkpointPrizeType = LookupHelper.getPrizeType(Constants.CHECKPOINT_PRIZE_TYPE_NAME);
            for (Prize prize : project.getPrizes()) {
                if (prize.getPrizeType().getId() == contestPrizeType.getId()) {
                    contestPrizes.add(prize);
                } else if (prize.getPrizeType().getId() == checkpointPrizeType.getId()) {
                    checkpointPrizes.add(prize);
                }
            }
        }
        Comparator<Prize> comp = new Comparators.PrizePlaceComparator();
        Collections.sort(contestPrizes, comp);
        Collections.sort(checkpointPrizes, comp);
        request.setAttribute("contestPrizes", contestPrizes);
        request.setAttribute("checkpointPrizes", checkpointPrizes);

        PhasesDetails phasesDetails = PhasesDetailsServices.getPhasesDetails(
                request, this, project, phases, allProjectResources, allProjectExtUsers);

        request.setAttribute("phaseGroupIndexes", phasesDetails.getPhaseGroupIndexes());
        request.setAttribute("phaseGroups", phasesDetails.getPhaseGroups());
        request.setAttribute("activeTabIdx", phasesDetails.getActiveTabIndex());

        boolean sendTLNotifications = false;

        if (AuthorizationHelper.isUserLoggedIn(request)) {
            Filter filterTNproject = NotificationFilterBuilder.createProjectIdFilter(project.getId());
            Filter filterTNuser = NotificationFilterBuilder.createExternalRefIdFilter(
                    AuthorizationHelper.getLoggedInUserId(request));
            Filter filterTNtype = NotificationFilterBuilder.createNotificationTypeIdFilter(1);
            Filter filterTN = new AndFilter(filterTNproject, new AndFilter(filterTNuser, filterTNtype));

            Notification[] notifications = ActionsHelper.createResourceManager().searchNotifications(filterTN);
            sendTLNotifications = (notifications.length != 0);
        }

        request.setAttribute("sendTLNotifications", (sendTLNotifications) ? "On" : "Off");

        // Check resource roles
        request.setAttribute("isManager",
                AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES));
        request.setAttribute("isSubmitter",
                AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME));

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
                AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME) && registrationOpen
            && !alreadySubmitted);

        // get appeals completed early property value
        boolean appealsCompletedFlag = false;
        if (submitter != null) {
            String value = (String) submitter.getProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY);
            if (value != null && value.equals(Constants.YES_VALUE)) {
                appealsCompletedFlag = true;
            }
        }

        long winnerExtUserId = Long.MIN_VALUE;
        String winnerExtRefId = (String) project.getProperty("Winner External Reference ID");

        if (winnerExtRefId != null && winnerExtRefId.trim().length() != 0) {
            winnerExtUserId = Long.parseLong(winnerExtRefId, 10);
        }

        // check if the user can mark appeals as completed
        request.setAttribute("isAllowedToCompleteAppeals",
                AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME) &&
            appealsOpen && !appealsCompletedFlag);

        // check if the user can resume appeals
        request.setAttribute("isAllowedToResumeAppeals",
                AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME) &&
            appealsOpen && appealsCompletedFlag);

        // Check permissions
        request.setAttribute("isAllowedToManageProjects",
                AuthorizationHelper.hasUserPermission(request,
                        Constants.VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME));
        request.setAttribute("isAllowedToEditProjects",
                AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME));
        request.setAttribute("isAllowedToContactPM",
                AuthorizationHelper.hasUserPermission(request, Constants.CONTACT_PM_PERM_NAME));
        request.setAttribute("isAllowedToSetTL",
                AuthorizationHelper.hasUserPermission(request, Constants.SET_TL_NOTIFY_PERM_NAME));
        request.setAttribute("isAllowedToViewSVNLink",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SVN_LINK_PERM_NAME));
        request.setAttribute("isAllowedToViewAutopilotStatus",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_AUTOPILOT_STATUS_PERM_NAME));
        request.setAttribute("isAllowedToViewPayment",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_PAY_INFO_PERM_NAME));
        request.setAttribute("isAllowedToViewAllPayment",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_PAYMENT_INFO_PERM_NAME));
        request.setAttribute("isAllowedToViewResources",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECT_RESOURCES_PERM_NAME));
        request.setAttribute("isAllowedToPerformScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_SCREENING_PERM_NAME) &&
                        ActionsHelper.getPhase(phases, true, Constants.SCREENING_PHASE_NAME) != null);
        request.setAttribute("isAllowedToViewPayments",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PAYMENTS_PERM_NAME));

        Phase reviewPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.REVIEW_PHASE_NAME);
        request.setAttribute("isAllowedToAdvanceSubmissionWithFailedScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME) &&
                        project.getProjectStatus().getName().equals("Active") &&
                        reviewPhase != null && !ActionsHelper.isPhaseClosed(reviewPhase.getPhaseStatus()));

        request.setAttribute("isAllowedToPerformCheckpointScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_CHECKPOINT_SCREENING_PERM_NAME) &&
                        ActionsHelper.getPhase(phases, true, Constants.CHECKPOINT_SCREENING_PHASE_NAME) != null);
        request.setAttribute("isAllowedToPerformCheckpointReview",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_CHECKPOINT_REVIEW_PERM_NAME) &&
                        ActionsHelper.getPhase(phases, true, Constants.CHECKPOINT_REVIEW_PHASE_NAME) != null);
        request.setAttribute("isAllowedToViewScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENING_PERM_NAME));
        request.setAttribute("isAllowedToViewCheckpointScreening",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_CHECKPOINT_SCREENING_PERM_NAME));
        request.setAttribute("isAllowedToViewCheckpointReview",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_CHECKPOINT_REVIEW_PERM_NAME));
        request.setAttribute("isAllowedToEditHisReviews",
                AuthorizationHelper.hasUserPermission(
                        request, Constants.VIEW_REVIEWER_REVIEWS_PERM_NAME) &&
                        (ActionsHelper.getPhase(phases, true, Constants.REVIEW_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.POST_MORTEM_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.APPEALS_PHASE_NAME) != null ||
                                ActionsHelper.getPhase(phases, true, Constants.APPEALS_RESPONSE_PHASE_NAME) != null));
        request.setAttribute("isAllowedToEditHisIterativeReview",
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ITERATIVE_REVIEWER_REVIEWS_PERM_NAME)
                        && ActionsHelper.getPhase(phases, true, Constants.ITERATIVE_REVIEW_PHASE_NAME) != null);
        request.setAttribute("isAllowedToUploadTC",
                AuthorizationHelper.hasUserPermission(request, Constants.UPLOAD_TEST_CASES_PERM_NAME));
        request.setAttribute("isAllowedToPerformAggregation",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME));
        request.setAttribute("isAllowedToUploadFF",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_FIX_PERM_NAME) &&
                        AuthorizationHelper.getLoggedInUserId(request) == winnerExtUserId);
        request.setAttribute("isAllowedToUploadSpec",
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME));
        request.setAttribute("isAllowedToPerformFinalReview",
                ActionsHelper.getPhase(phases, true, Constants.FINAL_REVIEW_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME));
        request.setAttribute("isAllowedToPerformSpecReview",
                ActionsHelper.getPhase(phases, true, Constants.SPECIFICATION_REVIEW_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_SPECIFICATION_REVIEW_PERM_NAME));
        request.setAttribute("isAllowedToPerformApproval",
                ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPROVAL_PERM_NAME));
        request.setAttribute("isAllowedToPerformPortMortemReview",
                ActionsHelper.getPhase(phases, true, Constants.POST_MORTEM_PHASE_NAME) != null &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_POST_MORTEM_REVIEW_PERM_NAME));

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
            for (Resource resource : myResources) {
                // Examine only Submitters, skip all other ones
                if (!resource.getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                    continue;
                }
                // This resource is a submitter;
                // compare its external user ID to the official project's winner's one
                if (resource.getProperty("External Reference ID").equals(winnerExtId)) {
                    allowedToReviewAggregation = true;
                    break;
                }
            }
        }
        request.setAttribute("isAllowedToPerformAggregationReview", allowedToReviewAggregation);

        // since Online Review Update - Add Project Dropdown v1.0
        request.setAttribute("isAdmin",
                AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                        || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)
                        || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME));

        //OR Project Linking Assembly
        ProjectLinkManager linkManager = ActionsHelper.createProjectLinkManager();
        request.setAttribute("destProjectLinks", linkManager.getDestProjectLinks(project.getId()));
        request.setAttribute("srcProjectLinks", linkManager.getSourceProjectLinks(project.getId()));

        return Constants.SUCCESS_FORWARD_NAME;
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
                    statusCodes[i] = Constants.CANNOT_OPEN_PHASE_STATUS_CODE; // Can't open, phase start time is reached and dependencies are met
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
     * This method returns the hints for the phases which can't open.
     *
     * @param phaseMgr the <code>PhaseManager</code> instance.
     * @param phases the array of <code>Phase</code> to get the hints.
     * @param phaseStatusCodes the status codes of the provided phases.
     * @param textProvider the <code>TextProvider</code> instance used to retrieve message.
     * @return the hints for the phases which can't be open.
     * @throws PhaseManagementException if error occurs
     */
    private static String[] getPhaseCannotOpenHints(PhaseManager phaseMgr, Phase[] phases, int[] phaseStatusCodes, TextProvider textProvider)
        throws PhaseManagementException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(phases, "phases");
        ActionsHelper.validateParameterNotNull(phaseMgr, "phaseMgr");
        ActionsHelper.validateParameterNotNull(phaseStatusCodes, "phaseStatusCodes");

        String[] hints = new String[phases.length];
        for (int i = 0; i < phases.length; i++) {
            if (phaseStatusCodes[i] == Constants.CANNOT_OPEN_PHASE_STATUS_CODE) {
                // Can't open
                OperationCheckResult result = phaseMgr.canStart(phases[i]);
                if (result.isSuccess()) {
                    hints[i] = textProvider.getText("viewProjectDetails.CannotOpenHint.AllMet");
                } else {
                    hints[i] = result.getMessage();
                }
            }
        }
        return hints;
    }

    /**
     * This static method will get the deliverable status codes.
     *
     * @return the deliverable status codes array
     * @param deliverables the deliverables to work with
     * @param activePhases the active phases
     * @param currentTime the current time
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

        boolean isStudio = "Studio".equalsIgnoreCase((String) request.getAttribute("projectType"));

        for (int i = 0; i < deliverables.length; ++i) {
            // Get a Deliverable for the current iteration
            Deliverable deliverable = deliverables[i];
            String delivName = deliverable.getName();
            if (delivName.equalsIgnoreCase(Constants.SUBMISSION_DELIVERABLE_NAME)) {
                if (!isStudio) {
                    links[i] = "UploadContestSubmission?pid=" + deliverable.getProject();
                }
                else{
                    links[i] = "http://" + ApplicationServer.STUDIO_SERVER_NAME + "/?module=ViewContestDetails&ct=" + deliverable.getProject();
                }
            } else if (delivName.equalsIgnoreCase(Constants.CHECKPOINT_SUBMISSION_DELIVERABLE_NAME)) {
                if (!isStudio) {
                    links[i] = "UploadCheckpointSubmission?pid=" + deliverable.getProject();
                }
                else{
                    links[i] = "http://" + ApplicationServer.STUDIO_SERVER_NAME + "/?module=ViewContestDetails&ct=" + deliverable.getProject();
                }
            } else if (delivName.equalsIgnoreCase(Constants.SPECIFICATION_SUBMISSION_DELIVERABLE_NAME)) {
                if (!deliverable.isComplete()) {
                    links[i] = "UploadSpecificationSubmission?pid=" + deliverable.getProject();
                }
            } else if (delivName.equalsIgnoreCase(Constants.SPECIFICATION_REVIEW_DELIVERABLE_NAME)) {
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Specification Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateSpecificationReview?sid=" + deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditSpecificationReview?rid=" + review.getId();
                } else {
                    links[i] = "ViewSpecificationReview?rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.SCREENING_DELIVERABLE_NAME) ||
                    delivName.equalsIgnoreCase(Constants.PRIMARY_SCREENING_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Screening"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateScreening?sid=" + deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditScreening?rid=" + review.getId();
                } else {
                    links[i] = "ViewScreening?rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.CHECKPOINT_SCREENING_DELIVERABLE_NAME)) {
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Checkpoint Screening"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateCheckpointScreening?sid=" + deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditCheckpointScreening?rid=" + review.getId();
                } else {
                    links[i] = "ViewCheckpointScreening?rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.REVIEW_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateReview?sid=" + deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditReview?rid=" + review.getId();
                } else {
                    links[i] = "ViewReview?rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.ITERATIVE_REVIEW_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Iterative Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateIterativeReview?sid=" + deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditIterativeReview?rid=" + review.getId();
                } else {
                    links[i] = "ViewIterativeReview?rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.CHECKPOINT_REVIEW_DELIVERABLE_NAME)) {
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Checkpoint Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateCheckpointReview?sid=" + deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditCheckpointReview?rid=" + review.getId();
                } else {
                    links[i] = "ViewCheckpointReview?rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.ACC_TEST_CASES_DELIVERABLE_NAME) ||
                    delivName.equalsIgnoreCase(Constants.FAIL_TEST_CASES_DELIVERABLE_NAME) ||
                    delivName.equalsIgnoreCase(Constants.STRS_TEST_CASES_DELIVERABLE_NAME)) {
                links[i] = "UploadTestCase?pid=" + deliverable.getProject();
            } else if (delivName.equalsIgnoreCase(Constants.APPEAL_RESP_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                // If review for the submission is absent, something is wrong
                // and no link for this deliverable can be generated
                if (review != null) {
                    links[i] = "ViewReview?rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.AGGREGATION_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field,
                // as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review != null) {
                    if (!review.isCommitted()) {
                        links[i] = "EditAggregation?rid=" + review.getId();
                    } else {
                        links[i] = "ViewAggregation?rid=" + review.getId();
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
                Resource[] aggregator = ActionsHelper.getAllResourcesForPhase(phase);

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Review"),
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
                    links[i] = "EditAggregationReview?rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.FINAL_FIX_DELIVERABLE_NAME)) {
                if(deliverable.isComplete()) {
                    //get final fix uploads for current project phase
                    Upload[] uploads = ActionsHelper.getPhaseUploads(deliverable.getPhase(), "Final Fix");
                    links[i] = "DownloadFinalFix?uid=" + uploads[0].getId();
                } else {
                    links[i] = "UploadFinalFix?pid=" + deliverable.getProject();
                }
            } else if (delivName.equalsIgnoreCase(Constants.FINAL_REVIEW_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field, as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        null, deliverable.getSubmission(), deliverable.getResource(), false);

                if (review != null) {
                    if (!review.isCommitted()) {
                        links[i] = "EditFinalReview?rid=" + review.getId();
                    } else {
                        links[i] = "ViewFinalReview?rid=" + review.getId();
                    }
                }
            } else if (delivName.equalsIgnoreCase(Constants.APPROVAL_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field, as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review[] reviews = ActionsHelper.searchReviews(deliverable.getPhase(), deliverable.getResource(), false);

                if (reviews.length == 0) {
                    links[i] = "CreateApproval?sid=" + deliverable.getSubmission();
                } else if (!reviews[0].isCommitted()) {
                    links[i] = "EditApproval?rid=" + reviews[0].getId();
                } else {
                    links[i] = "ViewApproval?rid=" + reviews[0].getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.POST_MORTEM_DELIVERABLE_NAME)) {
                Review[] reviews = ActionsHelper.searchReviews(deliverable.getPhase(), deliverable.getResource(), false);

                if (reviews.length == 0) {
                    links[i] = "CreatePostMortem?pid=" + deliverable.getProject();
                    //  request.setAttribute("postMortemSubmissionId", deliverable.getSubmission().longValue());
                } else if (!reviews[0].isCommitted()) {
                    links[i] = "EditPostMortem?rid=" + reviews[0].getId();
                } else {
                    links[i] = "ViewPostMortem?rid=" + reviews[0].getId();
                }
            }
        }

        return links;
    }

    /**
     * Get the user ids from a deliverables array.
     *
     * @return the user ids found
     * @param deliverables the deliverables array
     * @param resources the resources
     * @throws BaseException if any error
     */
    private static String[] getDeliverableUserIds(Deliverable[] deliverables, Resource[] resources) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(deliverables, "deliverables");
        ActionsHelper.validateParameterNotNull(resources, "resources");

        String[] ids = new String[deliverables.length];

        for (int i = 0; i < deliverables.length; ++i) {
            final long deliverableResourceId = deliverables[i].getResource();
            for (Resource resource : resources) {
                if (resource.getId() == deliverableResourceId) {
                    ids[i] = (String) resource.getProperty("External Reference ID");
                    break;
                }
            }
        }

        return ids;
    }

    /**
     * Get the submission user ids from a deliverables array.
     *
     * @return the submission user ids
     * @param deliverables the deliverables array
     * @throws BaseException if any error
     */
    private static String[] getDeliverableSubmissionUserIds(Deliverable[] deliverables)
            throws BaseException {

        List<Long> submissionIds = new ArrayList<Long>();

        for (Deliverable deliverable : deliverables) {
            if (deliverable.getSubmission() != null) {
                submissionIds.add(deliverable.getSubmission());
            }
        }

        if (submissionIds.isEmpty()) {
            return new String[0];
        }

        Filter filterSubmissions = new InFilter("submission_id", submissionIds);

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();
        Submission[] submissions = upMgr.searchSubmissions(filterSubmissions);

        List<Long> resourceIds = new ArrayList<Long>();

        for (Submission submission : submissions) {
            resourceIds.add(submission.getUpload().getOwner());
        }

        Filter filterResources = new InFilter("resource.resource_id", resourceIds);

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager();
        Resource[] resources = resMgr.searchResources(filterResources);

        String[] ids = new String[deliverables.length];

        for (int i = 0; i < deliverables.length; ++i) {
            if (deliverables[i].getSubmission() == null) {
                continue;
            }
            long deliverableId = deliverables[i].getSubmission();
            for (Submission submission : submissions) {
                if (submission.getId() != deliverableId) {
                    continue;
                }
                long submissionOwnerId = submission.getUpload().getOwner();
                for (Resource resource : resources) {
                    if (resource.getId() == submissionOwnerId) {
                        ids[i] = (String) resource.getProperty("External Reference ID");
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
     *            a scorecard template type that found review should have (null for any)
     * @param submissionId
     *            an ID of the submission which the review was made for.
     * @param resourceId
     *            an ID of the resource who made (created) the review.
     * @param complete
     *            specifies whether retrieved review should have all infomration (like all items and
     *            their comments).
     * @throws IllegalArgumentException
     *             if <code>submissionId</code> parameter is
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

        ActionsHelper.validateParameterNotNull(submissionId, "submissionId");
        ActionsHelper.validateParameterPositive(submissionId, "submissionId");
        ActionsHelper.validateParameterPositive(resourceId, "resourceId");

        Filter filterSubmission = new EqualToFilter("submission", submissionId);
        Filter filterReviewer = new EqualToFilter("reviewer", resourceId);

        Filter filter;
        if (scorecardType != null) {
            Filter filterScorecard = new EqualToFilter("scorecardType", scorecardType.getId());
            filter = new AndFilter(Arrays.asList(filterSubmission, filterScorecard, filterReviewer));
        } else {
            filter = new AndFilter(Arrays.asList(filterSubmission, filterReviewer));
        }

        // Get a review(s) that pass filter
        Review[] reviews = manager.searchReviews(filter, complete);
        // Return the first found review if any, or null
        return (reviews.length != 0) ? reviews[0] : null;
    }
}

