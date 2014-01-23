/*
 * Copyright (C) 2004 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import javax.servlet.http.HttpServletResponse;

import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import com.topcoder.management.project.Prize;
import com.topcoder.management.project.PrizeType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

import com.cronos.onlinereview.dataaccess.ProjectDataAccess;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.functions.Functions;
import com.cronos.onlinereview.model.ClientProject;
import com.cronos.onlinereview.model.CockpitProject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.SubmissionType;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
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
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.DisallowedDirectoryException;
import com.topcoder.servlet.request.FileDoesNotExistException;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.PersistenceException;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.shared.util.ApplicationServer;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;

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
 *    <li>Added {@link #uploadSpecificationSubmission(org.apache.struts.action.ActionMapping,org.apache.struts.action.ActionForm,javax.servlet.http.HttpServletRequest)} method.</li>
 *    <li>Updated {@link #uploadContestSubmission(org.apache.struts.action.ActionMapping,org.apache.struts.action.ActionForm,javax.servlet.http.HttpServletRequest)}
 *        method to set type for uploaded submission.</li>
 *    <li>Added {@link #downloadSpecificationSubmission(org.apache.struts.action.ActionMapping,javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.6.1 (Checkpoint Support Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added logic fo downloading, uploading Checkpoint submissions.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.6.2 (Online Review Replatforming Release 2) Change notes:
 *   <ol>
 *     <li>Change submission.getUplaods.get(0) to submission.getUpload().</li>
 *     <li>Update {@link #generateDeliverableLinks(HttpServletRequest, Deliverable[], Phase[])} so that we don't generate
 *     the submission link for studio contest.</li>
 *     <li>Update {@link #handleUploadSubmission(ActionMapping, ActionForm, HttpServletRequest, String, String, String)} method
 *     to disable uploading for studio contest.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.7 (Online Review Payments and Status Automation Assembly 1.0) Change notes:
 *   <ol>
 *    <li>Update {@link #viewProjectDetails(org.apache.struts.action.ActionMapping,javax.servlet.http.HttpServletRequest)} method
 *        to fix the duplication of scorecard templates.</li>
 *    <li>Update {@link #viewProjectDetails(org.apache.struts.action.ActionMapping,javax.servlet.http.HttpServletRequest)} method
 *        to show the &quot;Pay Project&quot; button in the Online Review shows when there's at least one resource with
 *        non-zero payment value and payment status other than "Paid".</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.8 (Online Review Status Validation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Methods adjusted for new signatures of create managers methods from ActionsHelper</li>
 *     <li>Updated {@link #generateDeliverableLinks(HttpServletRequest request,
            Deliverable[] deliverables, Phase[] phases)}
 *         to generate link to uploaded final fix, instead of link to upload page again</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.9 (Online Review Miscellaneous Improvements) Change notes:
 *   <ol>
 *     <li>Added {@link #getPhaseCannotOpenHints(PhaseManager, Phase[], int[], MessageResources)} to get the hints for the phases
 *     which can't be open.</li>
 *     <li>Added struts action {@link #advanceFailedScreeningSubmission(org.apache.struts.action.ActionMapping,javax.servlet.http.HttpServletRequest)}
 *     to advance the submission that failed screening.</li>
 *     <li>Updated {@link #viewProjectDetails(org.apache.struts.action.ActionMapping,javax.servlet.http.HttpServletRequest)} method to get the hints for
 *     the phases which can't be open.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.10 (Online Review - Project Payments Integration Part 1 v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #viewProjectDetails(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *     method to remove the old payments and add support to the new project prizes.</li>
 *     <li>Updated {@link #advanceFailedScreeningSubmission(ActionMapping, ActionForm, HttpServletRequest,
 *     HttpServletResponse)} method to add support to advance failed checkpoint screening submission.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.11 (Online Review - Project Payments Integration Part 2 v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #viewProjectDetails(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *     method to set isAllowedToViewPayments to request attribute.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.12 (Online Review - Project Payments Integration Part 3 v1.0) Change notes:
 *   <ol>
 *       <li>Removed <code>isAllowedToPay</code> method.</li>
 *       <li>Updated {@link #viewProjectDetails(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *       method to set the resource payments data based on the project payments instead of
 *       resource payment property.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.13 (Online Review - Iterative Review v1.0) Change notes:
 *   <ol>
 *       <li>Added logic to handle iterative review deliverables.</li>
 *       <li>Modified handleDownloadSubmission method to handle download submission with iterative review phase.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.14 (Online Review - Thurgood Integration v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #handleUploadSubmission(ActionMapping, ActionForm, HttpServletRequest, String, String, String)}
 *     to support Thurgood job.</li>
 *   </ol>
 * </p>
 *
 * @author George1, real_vg, pulky, isv, FireIce, rac_, flexme, duxiaoyang, gjw99
 * @version 1.14
 * @since 1.0
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
     * <p>
     * Online Review - Project Payments Integration Part 1 v1.0
     *      - Remove the old prize information and populate the new project prizes to the request attributes.
     * </p>
     *
     * <p>
     * Online Review - Thurgood Integration v1.0
     *      - Add Thurgood logic to the request attributes.
     * </p>
     *
     * @return an action forward to the appropriate page. If no error has occurred, the forward will
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
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
        
        // check if this project uses Thurgood
        if (!isEmptyOrNull(project.getProperty("Thurgood Platform")) && !isEmptyOrNull(project.getProperty("Thurgood Language")) ) {
            request.setAttribute("isThurgood", true);
            request.setAttribute("thurgoodBaseUIURL", ConfigHelper.getThurgoodJobBaseUIURL());
        }

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
        MessageResources messageResources = getResources(request);
        ActionsHelper.retrieveAndStoreMyRole(request, messageResources);
        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        List<ProjectPayment> allPayments = ActionsHelper.createProjectPaymentManager().search(
                ProjectPaymentFilterBuilder.createProjectIdFilter(projectId));
        // Place an information about the amount of "my" payment into the request
        Map<ResourceRole, Double> myPayments = new HashMap<ResourceRole, Double>();
        Map<ResourceRole, Boolean> myPaymentsPaid = new HashMap<ResourceRole, Boolean>();
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

                request.setAttribute("unrespondedLateDeliverablesLink", "ViewLateDeliverables.do?method=viewLateDeliverables&project_id=" +
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
                phaseScorecardLinks.put(phaseTypeName, ConfigHelper.getProjectTypeScorecardLink(projectTypeName, scorecardTemplate.getId()));
            }
        }

        long currentTimeInMinutes = (new Date()).getTime() / (60 * 1000);
        if (currentTimeInMinutes >= projectStartTime && currentTimeInMinutes <= projectEndTime) {
            request.setAttribute("ganttCurrentTime", currentTimeInMinutes - projectStartTime);
        }

        // Collect Open / Closing / Late / Closed codes for phases
        int[] phaseStatuseCodes = getPhaseStatusCodes(phases, currentTime);
        String[] cannotOpenHints = getPhaseCannotOpenHints(phaseMgr, phases, phaseStatuseCodes, messages);

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
                request, messages, project, phases, allProjectResources, allProjectExtUsers);

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

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Contact Manager&quot; Struts Action defined for
     * this assembly, which is supposed to send a message entered by user to the manager of some
     * project. This action gets executed twice &#x96; once to display the page with the form, and
     * once to process the message entered by user on that form.
     *
     * @return an action forward to the appropriate page. If no error has occurred and this action
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.CONTACT_PM_PERM_NAME, postBack);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }
        // Get the ID of the sender
        long senderId = AuthorizationHelper.getLoggedInUserId(request);

        // Obtain an instance of User Retrieval
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        // Retrieve information about an external user by its ID
        ExternalUser sender = userRetrieval.retrieveUser(senderId);

        // Get current project from the verification result bean
        Project project = verification.getProject();

        // Obtain an instance of Document Generator
        DocumentGenerator docGenerator = new DocumentGenerator();
        docGenerator.setDefaultTemplateSource(new FileTemplateSource());

        // Get the template of email
        Template docTemplate = docGenerator.getTemplate(ConfigHelper.getContactManagerEmailTemplate());

        TemplateFields fields = docGenerator.getFields(docTemplate);
        Node[] nodes = fields.getNodes();

        // Construct the body of the email
        for (Node node : nodes) {
            if (node instanceof Field) {
                Field field = (Field) node;

                if ("USER_FIRST_NAME".equals(field.getName())) {
                    field.setValue(sender.getFirstName());
                } else if ("USER_LAST_NAME".equals(field.getName())) {
                    field.setValue(sender.getLastName());
                } else if ("USER_HANDLE".equals(field.getName())) {
                    field.setValue(sender.getHandle());
                } else if ("PROJECT_NAME".equals(field.getName())) {
                    field.setValue("" + project.getProperty("Project Name"));
                } else if ("PROJECT_VERSION".equals(field.getName())) {
                    field.setValue("" + project.getProperty("Project Version"));
                } else if ("QUESTION_TYPE".equals(field.getName())) {
                    field.setValue(request.getParameter("cat"));
                } else if ("TEXT".equals(field.getName())) {
                    field.setValue(Functions.htmlEncode(request.getParameter("msg")));
                } else if ("OR_LINK".equals(field.getName())) {
                    field.setValue(ConfigHelper.getProjectDetailsBaseURL() + project.getId());
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

        // Add 'TO' addresses to message
        List<Long> managerUsrIds = ActionsHelper.getUserIDsByRoleNames(new String[]{"Manager", "Copilot"}, project.getId());
        List<String> managerEmails = ActionsHelper.getEmailsByUserIDs(request, managerUsrIds);
        for (String managerEmail : managerEmails) {
            message.addToAddress(managerEmail, TCSEmailMessage.TO);
        }

        // Add 'BCC' addresses to message (Client Managers wish to keep their email addresses private)
        List<Long> clientManagerUsrIds = ActionsHelper.getUserIDsByRoleNames(new String[]{"Client Manager"}, project.getId());
        List<String> clientManagerEmails = ActionsHelper.getEmailsByUserIDs(request, clientManagerUsrIds);
        for (String clientManagerEmail : clientManagerEmails) {
            // Don't duplicate addressee.
            if (!managerEmails.contains(clientManagerEmail)) {
                message.addToAddress(clientManagerEmail, TCSEmailMessage.BCC);
            }
        }

        //The sender is CC'd in the mail
        message.addToAddress(sender.getEmail(), TCSEmailMessage.CC);

        // Add 'From' address
        message.setFromAddress(sender.getEmail());
        // Set message's subject
        message.setSubject(project.getProperty("Project Name") + " - " + sender.getHandle());
        // Insert a body into the message
        message.setBody(docGenerator.applyTemplate(fields));

        message.setContentType("text/html");

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
     * @return an action forward to the appropriate page. If no error has occurred and this action
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
        return handleUploadSubmission(mapping, form, request, Constants.CONTEST_SUBMISSION_TYPE_NAME,
                                      Constants.PERFORM_SUBM_PERM_NAME, Constants.SUBMISSION_PHASE_NAME);
    }

    /**
     * <p>This method is an implementation of &quot;Upload Checkpoint Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his checkpoint submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.</p>
     *
     * @param mapping an <code>ActionMapping</code> used to map the request to this action.
     * @param form an <code>ActionForm</code> providing the form submitted by user.
     * @param request an <code>HttpServletRequest</code> providing the details for incoming request.
     * @param response an <code>HttpServletResponse</code> providing the details for outgoing response.
     * @return an <code>ActionForward</code> to the appropriate page. If no error has occurred and this action was called
     *         the first time, the forward will be to uploadCheckpointSubmission.jsp page, which displays the form where
     *         user can specify the file he/she wants to upload. If this action was called during the post back (the
     *         second time), then the request should contain the file uploaded by user. In this case, this method
     *         verifies if everything is correct, stores the file on file server and returns a forward to the View
     *         Project Details page.
     * @throws BaseException if an unexpected error occurs.
     * @since 1.6.1
     */
    public ActionForward uploadCheckpointSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        return handleUploadSubmission(mapping, form, request, Constants.CHECKPOINT_SUBMISSION_TYPE_NAME,
                                      Constants.PERFORM_CHECKPOINT_SUBMISSION_PERM_NAME,
                                      Constants.CHECKPOINT_SUBMISSION_PHASE_NAME);
    }

    /**
     * <p>This method is an implementation of &quot;Upload Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.</p>
     *
     * @return an action forward to the appropriate page. If no error has occurred and this action
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Determine if this request is a post back
        final boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
            mapping, getResources(request), request, Constants.PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME,
            postBack);

        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve current project
        Project project = verification.getProject();
        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);

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
        Submission[] oldSubmissions = ActionsHelper.getProjectSubmissions(
                project.getId(), Constants.SPECIFICATION_SUBMISSION_TYPE_NAME,
                Constants.ACTIVE_SUBMISSION_STATUS_NAME, false);

        if (oldSubmissions != null && oldSubmissions.length > 0) {
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

        upload.setProject(project.getId());
        upload.setProjectPhase(specificationPhase.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
        upload.setUploadType(LookupHelper.getUploadType("Submission"));
        upload.setParameter(uploadedFile.getFileId());

        Submission submission = new Submission();
        submission.setUpload(upload);

        SubmissionType specSubmissionType = LookupHelper.getSubmissionType(Constants.SPECIFICATION_SUBMISSION_TYPE_NAME);
        SubmissionStatus submissionActiveStatus = LookupHelper.getSubmissionStatus("Active");
        submission.setSubmissionStatus(submissionActiveStatus);
        submission.setSubmissionType(specSubmissionType);

        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        UploadManager upMgr = ActionsHelper.createUploadManager();
        upMgr.createUpload(upload, operator);
        upMgr.createSubmission(submission, operator);
        resource.addSubmission(submission.getId());
        ActionsHelper.createResourceManager().updateResource(resource, operator);

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
        return handleDownloadSubmission(mapping, request, response, "ViewSubmission",
                                        Constants.VIEW_ALL_SUBM_PERM_NAME, Constants.VIEW_MY_SUBM_PERM_NAME,
                                        Constants.VIEW_SCREENER_SUBM_PERM_NAME, Constants.VIEW_RECENT_SUBM_PERM_NAME,
                                        Constants.DOWNLOAD_CUSTOM_SUBM_PERM_NAME,
                                        Constants.VIEW_WINNING_SUBM_PERM_NAME,
                                        Constants.SCREENING_PHASE_NAME, Constants.REVIEW_PHASE_NAME,
                                        Constants.SCREENER_ROLE_NAMES, Constants.REVIEWER_ROLE_NAMES, 1);
    }

    /**
     * This method is an implementation of &quot;Download Checkpoint Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a checkpoint submission from the server.
     *
     * @return a <code>null</code> code if everything went fine, or an action forward to
     *         /jsp/userError.jsp page which will display the information about the cause of error.
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @throws IOException if some error occurs during disk input/output operation.
     * @since 1.6.1
     */
    public ActionForward downloadCheckpointSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException, IOException {
        return handleDownloadSubmission(mapping, request, response, "ViewCheckpointSubmission",
                                        Constants.VIEW_ALL_CHECKPOINT_SUBMISSIONS_PERM_NAME,
                                        Constants.VIEW_MY_CHECKPOINT_SUBMISSIONS_PERM_NAME,
                                        Constants.VIEW_SCREENER_CHECKPOINT_SUBMISSION_PERM_NAME,
                                        Constants.VIEW_RECENT_CHECKPOINT_SUBMISSIONS_PERM_NAME,
                                        Constants.DOWNLOAD_CUSTOM_CHECKPOINT_SUBMISSION_PERM_NAME,
                                        Constants.VIEW_WINNING_CHECKPOINT_SUBMISSION_PERM_NAME,
                                        Constants.CHECKPOINT_SCREENING_PHASE_NAME,
                                        Constants.CHECKPOINT_REVIEW_PHASE_NAME,
                                        new String[] {Constants.CHECKPOINT_SCREENER_ROLE_NAME},
                                        new String[] {Constants.CHECKPOINT_REVIEWER_ROLE_NAME}, 3);
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(mapping, request, "ViewSubmission");
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();
        // Verify that upload is a submission
        if (!upload.getUploadType().getName().equalsIgnoreCase("Submission")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request, "ViewSubmission",
                "Error.NotASubmission", null);
        }

        // Verify the status of upload and check whether the user has permission to download old uploads
        boolean hasViewAllSpecSubmissionsPermission
            = AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SPECIFICATION_SUBMISSIONS_PERM_NAME);
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted") && !hasViewAllSpecSubmissionsPermission) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
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
            for (Resource myResource : myResources) {
                if (myResource.getId() == owningResourceId) {
                    noRights = false;
                    break;
                }
            }
        }

        if (noRights && AuthorizationHelper.hasUserPermission(
                            request, Constants.VIEW_RECENT_SPECIFICATION_SUBMISSIONS_PERM_NAME)) {
            Phase[] phases = ActionsHelper.getPhasesForProject(
                    ActionsHelper.createPhaseManager(false), verification.getProject());
            final boolean isReviewOpen = ActionsHelper.isInOrAfterPhase(phases, 0,
                Constants.SPECIFICATION_REVIEW_PHASE_NAME);
            if (AuthorizationHelper.hasUserRole(request, Constants.SPECIFICATION_REVIEWER_ROLE_NAME) && !isReviewOpen) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, "ViewSubmission", "Error.IncorrectPhase", null);
            }
            noRights = false;
        }

        ActionsHelper.logDownloadAttempt(request, upload, !noRights);

        if (noRights) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request, "ViewSubmission",
                "Error.NoPermission", Boolean.FALSE);
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
     * @return an action forward to the appropriate page. If no error has occurred and this action
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.PERFORM_FINAL_FIX_PERM_NAME, postBack);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Retrieve current project
        Project project = verification.getProject();

        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        // Retrieve the current phase for the project
        Phase currentPhase = ActionsHelper.getPhase(phases, true, Constants.FINAL_FIX_PHASE_NAME);
        // Check that active phase is Final Fix
        if (currentPhase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.PERFORM_FINAL_FIX_PERM_NAME, "Error.IncorrectPhase", null);
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

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForRole(request, "Submitter");

        Upload[] uploads = ActionsHelper.getPhaseUploads(currentPhase.getId(), "Final Fix");
        if (uploads.length > 0) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.PERFORM_FINAL_FIX_PERM_NAME, "Error.OnlyOneFinalFix", null);
        }

        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setProjectPhase(currentPhase.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
        upload.setUploadType(LookupHelper.getUploadType("Final Fix"));
        upload.setParameter(uploadedFile.getFileId());

        ActionsHelper.createUploadManager().createUpload(
                upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(mapping, request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        boolean hasPermission = false, hasSubmitterRole = false;
        String[] roles = ConfigHelper.getRolesForPermission(Constants.DOWNLOAD_FINAL_FIX_PERM_NAME);
        for (String role : roles) {
            if (!AuthorizationHelper.hasUserRole(request, role)) {
                continue;
            }

            // For the Submitters we only allow to download final fixes if the user has at least passed screening.
            if (role.equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                hasSubmitterRole = true;

                // Get all submissions for this user.
                Resource resource = ActionsHelper.getMyResourceForRole(request, Constants.SUBMITTER_ROLE_NAME);
                UploadManager upMgr = ActionsHelper.createUploadManager();
                Long[] subIds = resource.getSubmissions();

                // Check that the user has a submission that passed screening.
                // We don't need to check the current phase because if there is a final fix submitted
                // it is already past the Apepals Response anyway.
                for (Long id : subIds) {
                    Submission submission = upMgr.getSubmission(id);
                    if (submission != null && submission.getSubmissionType().getName().equals(Constants.CONTEST_SUBMISSION_TYPE_NAME) &&
                            !submission.getSubmissionStatus().getName().equals(Constants.FAILED_SCREENING_SUBMISSION_STATUS_NAME)) {
                        hasPermission = true;
                        break;
                    }
                }
            } else {
                hasPermission = true;
                break;
            }
        }

        // Get the upload the user wants to download
        Upload upload = verification.getUpload();

        if (!hasPermission) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            if (hasSubmitterRole) {
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, "ViewSubmission", "Error.NoScreeningPassed", null);

            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request),
                        request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Verify that upload is a Final Fix
        if (!upload.getUploadType().getName().equalsIgnoreCase("Final Fix")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_FINAL_FIX_PERM_NAME, "Error.NotAFinalFix", null);
        }
        ActionsHelper.logDownloadAttempt(request, upload, true);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());
        outputDownloadedFile(uploadedFile, "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"", response);

        return null;
    }

    /**
     * This method is an implementation of &quot;Upload Test Case&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his test cases to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.
     *
     * @return an action forward to the appropriate page. If no error has occurred and this action
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.UPLOAD_TEST_CASES_PERM_NAME, postBack);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Retrieve current project
        Project project = verification.getProject();

        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);

        // Retrieve the current phase for the project, it should be either Review or Appeals Response
        Phase reviewPhase = ActionsHelper.getPhase(phases, true, Constants.REVIEW_PHASE_NAME);
        Phase appealsResponsePhase = ActionsHelper.getPhase(phases, true, Constants.APPEALS_RESPONSE_PHASE_NAME);

        if (reviewPhase == null && appealsResponsePhase == null) {
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

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForPhase(
                request, ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME));

        Filter filterProject = UploadFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterResource = UploadFilterBuilder.createResourceIdFilter(resource.getId());
        Filter filterStatus = UploadFilterBuilder.createUploadStatusIdFilter(
                LookupHelper.getUploadStatus("Active").getId());
        Filter filterType = UploadFilterBuilder.createUploadTypeIdFilter(
                LookupHelper.getUploadType("Test Case").getId());

        Filter filter = new AndFilter(
                Arrays.asList(filterProject, filterResource, filterStatus, filterType));

        UploadManager upMgr = ActionsHelper.createUploadManager();
        Upload[] oldUploads = upMgr.searchUploads(filter);

        // Set the status for old uploads to Deleted
        for (Upload oldUpload : oldUploads) {
            oldUpload.setUploadStatus(LookupHelper.getUploadStatus("Deleted"));
            upMgr.updateUpload(oldUpload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setProjectPhase(reviewPhase != null ? reviewPhase.getId() : appealsResponsePhase.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
        upload.setUploadType(LookupHelper.getUploadType("Test Case"));
        upload.setParameter(uploadedFile.getFileId());

        upMgr.createUpload(upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(mapping, request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(false), verification.getProject());

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

        // Get the upload the user wants to download
        Upload upload = verification.getUpload();

        // If Review phase is not closed yet, there is a need to check whether the user that is
        // attempting to download test cases is a Submitter and an Appeals phase is open
        if (canDownload && canPlaceAppeals && !isReviewClosed && !canDownloadDuringReview && !isAppealsOpen) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.IncorrectPhase", null);
        }
        // Verify that user can download test cases during Review
        if (canDownload && !isReviewClosed && !canDownloadDuringReview) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.IncorrectPhase", null);
        }
        // Check that the user is allowed to download test cases in general
        if (!canDownload) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Verify that upload is Test Cases
        if (!upload.getUploadType().getName().equalsIgnoreCase("Test Case")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.NotTestCases", null);
        }
        // Verify the status of upload
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME, "Error.UploadDeleted", null);
        }

        ActionsHelper.logDownloadAttempt(request, upload, true);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());
        outputDownloadedFile(uploadedFile, "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"", response);

        return null;
    }

    /**
     * This method is an implementation of &quot;Advance Submission That Failed Screening&quot; Struts Action defined for
     * this assembly, which is supposed to advance submission that failed screening for particular upload
     * (denoted by <code>uid</code> parameter). This action gets executed twice &#x96; once to
     * display the page with the confirmation, and once to process the confirmed advance request to
     * actually advance the submission to pass screening.
     *
     * <p>
     *  Online Review - Project Payments Integration Part 1 v1.0
     *      - Add support to process checkpoint submission.
     * </p>
     *
     * @return an action forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmAdvanceFailedScreeningSubmission.jsp
     *         or /jsp/confirmAdvanceFailedCheckpointScreeningSubmission.jsp page, which displays the confirmation dialog
     *         where user can confirm his intention to advance the submission. If this action was called during the post
     *         back (the second time), then this method verifies if everything is correct, and process the advance logic.
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
     * @since 1.9
     */
    public ActionForward advanceFailedScreeningSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(mapping, request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve the upload user tries to advance
        Upload upload = verification.getUpload();
        Project project = verification.getProject();

        // Check that user has permissions to delete submission.
        boolean hasPermission = AuthorizationHelper.hasUserPermission(request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME);
        // For Studio projects only Admins are authorized to advance submissions.
        if (ActionsHelper.isStudioProject(project)) {
            hasPermission = hasPermission && AuthorizationHelper.hasUserRole(request, Constants.ADMIN_ROLE_NAME);
        }

        if (!hasPermission) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Verify that the user is attempting to advance submission
        if (!upload.getUploadType().getName().equalsIgnoreCase(Constants.SUBMISSION_UPLOAD_TYPE_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME, "Error.NotASubmission3", null);
        }

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();
        Submission[] submissions = upMgr.searchSubmissions(SubmissionFilterBuilder.createUploadIdFilter(upload.getId()));
        Submission submission = (submissions.length != 0) ? submissions[0] : null;

        boolean isContestSubmission = submission.getSubmissionType().getName().equalsIgnoreCase(
                                        Constants.CONTEST_SUBMISSION_TYPE_NAME);

        // Check the submission status is Failed Screening
        String status = submission.getSubmissionStatus().getName();
        if (submission == null ||
            (!status.equalsIgnoreCase(Constants.FAILED_SCREENING_SUBMISSION_STATUS_NAME)
                && !status.equalsIgnoreCase(Constants.FAILED_CHECKPOINT_SCREENING_SUBMISSION_STATUS_NAME))) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME,
                    isContestSubmission ?
                            "Error.SubmissionNotFailedScreening" :"Error.SubmissionNotFailedCheckpointScreening",
                    null);
        }

        // Check the project status is Active
        if (!project.getProjectStatus().getName().equalsIgnoreCase(Constants.ACTIVE_PROJECT_STATUS_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME, "Error.ProjectNotActive", null);
        }

        // Check the Review phase is not closed
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        Phase reviewPhase = ActionsHelper.findPhaseByTypeName(phases,
                isContestSubmission ? Constants.REVIEW_PHASE_NAME : Constants.CHECKPOINT_REVIEW_PHASE_NAME);
        if (reviewPhase == null || ActionsHelper.isPhaseClosed(reviewPhase.getPhaseStatus())) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME,
                    isContestSubmission ? "Error.ReviewClosed" : "Error.CheckpointReviewClosed",
                    null);
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("advance") != null);

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            // Place upload ID into the request as attribute
            request.setAttribute("uid", upload.getId());
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        ProjectManager projectMgr = ActionsHelper.createProjectManager();
        String oldAutoPilotOption = (String) project.getProperty("Autopilot Option");
        if (!"Off".equalsIgnoreCase(oldAutoPilotOption)) {
            // Set AutoPilot status to Off
            project.setProperty("Autopilot Option", "Off");
            projectMgr.updateProject(project, "Turing AP off before advancing failed screening submission", operator);
        }

        if (isContestSubmission) {
            Phase postMortemPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.POST_MORTEM_PHASE_NAME);
            if (postMortemPhase != null) {
                ActionsHelper.deletePostMortem(project, postMortemPhase, operator);
            }
        }
        // Set submission status to Active
        SubmissionStatus submissionActiveStatus = LookupHelper.getSubmissionStatus(Constants.ACTIVE_SUBMISSION_STATUS_NAME);
        submission.setSubmissionStatus(submissionActiveStatus);
        upMgr.updateSubmission(submission, operator);

        if (isContestSubmission) {
            // Update the project_result table
            ResourceManager resMgr = ActionsHelper.createResourceManager();
            Resource uploadOwner = resMgr.getResource(upload.getOwner());
            ActionsHelper.updateProjectResultForAdvanceScreening(project.getId(),
                    Long.parseLong((String) uploadOwner.getProperty("External Reference ID")));
        }

        if (!"Off".equalsIgnoreCase(oldAutoPilotOption)) {
            // Restore the AutoPilot status
            project.setProperty("Autopilot Option", oldAutoPilotOption);
            projectMgr.updateProject(project, "Restoring the AP status after advancing failed screening submission", operator);
        }

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * This method is an implementation of &quot;Delete Submission&quot; Struts Action defined for
     * this assembly, which is supposed to delete (mark as deleted) submission for particular upload
     * (denoted by <code>uid</code> parameter). This action gets executed twice &#x96; once to
     * display the page with the confirmation, and once to process the confirmed delete request to
     * actually delete the submission.
     *
     * @return an action forward to the appropriate page. If no error has occurred and this action
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
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);
        // TODO: Delete this method and the whole action.
        throw new BaseException("Not supported anymore");
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
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Check the user has submitter role and registration phase is open
        boolean isSubmitter = AuthorizationHelper.hasUserRole(request,
                Constants.SUBMITTER_ROLE_NAME);

        if (!isSubmitter) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, "Unregistration", "Error.NoPermission", Boolean.FALSE);
        }

        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(false);
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
                    request, "Unregistration", "Error.RegistrationClosed", null);
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("unregister") != null);

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(),
                    getResources(request));

            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        Resource[] allProjectResources = ActionsHelper.getAllResourcesForProject(verification.getProject());

        boolean found = false;
        boolean hasOtherRoles = false;
        for (int i = 0; i < allProjectResources.length && (!found || !hasOtherRoles); i++) {
            long userId = Long.parseLong(((String) allProjectResources[i].getProperty("External Reference ID")).trim());

            if (userId == AuthorizationHelper.getLoggedInUserId(request)) {
                if (allProjectResources[i].getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                    ActionsHelper.deleteProjectResult(verification.getProject(), userId, allProjectResources[i].getResourceRole().getId());
                    ActionsHelper.createResourceManager().removeResource(allProjectResources[i],
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
        HttpServletRequest request, HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Check the user has submitter role and appeals phase is open
        boolean isSubmitter = AuthorizationHelper.hasUserRole(request,
                Constants.SUBMITTER_ROLE_NAME);

        if (!isSubmitter) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, "Early Appeals", "Error.NoPermission", Boolean.FALSE);
        }

        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(false);
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
                    request, "Early Appeals", "Error.AppealsClosed", null);
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        Resource submitter = ActionsHelper.getMyResourceForRole(request, "Submitter");
        if (submitter == null) {
            throw new BaseException("Unable to find the Submitter resource " +
                    "associated with the current user for project " + verification.getProject().getId());
        }

        // get appeals completed early property value
        boolean appealsCompletedFlag = false;
        String value = (String) submitter.getProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY);
        if (value != null && value.equals(Constants.YES_VALUE)) {
            appealsCompletedFlag = true;
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("perform") != null);
        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(),
                    getResources(request));

            request.setAttribute("complete", !appealsCompletedFlag);

            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Get the name (id) of the user performing the operations
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));

        // Obtain the instance of the Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager();

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
        HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(mapping, request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();

        // Check that user has permissions to download a Document
        if (!AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME)) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Verify that upload is a Review Document
        if (!upload.getUploadType().getName().equalsIgnoreCase("Review Document")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME, "Error.NotADocument", null);
        }
        // Verify the status of upload
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.DOWNLOAD_DOCUMENT_PERM_NAME, "Error.UploadDeleted", null);
        }

        ActionsHelper.logDownloadAttempt(request, upload, true);

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());
        outputDownloadedFile(uploadedFile, "attachment; filename=\"" + uploadedFile.getRemoteFileName() + "\"", response);

        return null;
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
     *            permission to check against, or <code>null</code> if no check is required.
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
        UploadManager upMgr = ActionsHelper.createUploadManager();
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
        ProjectManager projMgr = ActionsHelper.createProjectManager();
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
     * @param messages the <code>MessageResources</code> instance used to retrieve message.
     * @return the hints for the phases which can't be open.
     * @throws PhaseManagementException if error occurs
     * @since 1.9
     */
    private static String[] getPhaseCannotOpenHints(PhaseManager phaseMgr, Phase[] phases, int[] phaseStatusCodes, MessageResources messages)
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
                    hints[i] = messages.getMessage("viewProjectDetails.CannotOpenHint.AllMet");
                } else {
                    hints[i] = result.getMessage();
                }
            }
        }
        return hints;
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

        boolean isStudio = "Studio".equalsIgnoreCase((String) request.getAttribute("projectType"));

        for (int i = 0; i < deliverables.length; ++i) {
            // Get a Deliverable for the current iteration
            Deliverable deliverable = deliverables[i];
            String delivName = deliverable.getName();
            if (delivName.equalsIgnoreCase(Constants.SUBMISSION_DELIVERABLE_NAME)) {
                if (!isStudio) {
                    links[i] = "UploadContestSubmission.do?method=uploadContestSubmission&pid=" + deliverable.getProject();
                }
                else{
                    links[i] = "http://" + ApplicationServer.STUDIO_SERVER_NAME + "/?module=ViewContestDetails&ct=" + deliverable.getProject();
                }
            } else if (delivName.equalsIgnoreCase(Constants.CHECKPOINT_SUBMISSION_DELIVERABLE_NAME)) {
                if (!isStudio) {
                    links[i] = "UploadCheckpointSubmission.do?method=uploadCheckpointSubmission&pid=" + deliverable.getProject();
                }
                else{
                    links[i] = "http://" + ApplicationServer.STUDIO_SERVER_NAME + "/?module=ViewContestDetails&ct=" + deliverable.getProject();
                }
            } else if (delivName.equalsIgnoreCase(Constants.SPECIFICATION_SUBMISSION_DELIVERABLE_NAME)) {
                if (!deliverable.isComplete()) {
                    links[i] = "UploadSpecificationSubmission.do?method=uploadSpecificationSubmission&pid="
                               + deliverable.getProject();
                }
            } else if (delivName.equalsIgnoreCase(Constants.SPECIFICATION_REVIEW_DELIVERABLE_NAME)) {
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Specification Review"),
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

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Screening"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateScreening.do?method=createScreening&sid=" +
                            deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditScreening.do?method=editScreening&rid=" + review.getId();
                } else {
                    links[i] = "ViewScreening.do?method=viewScreening&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.CHECKPOINT_SCREENING_DELIVERABLE_NAME)) {
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Checkpoint Screening"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateCheckpointScreening.do?method=createCheckpointScreening&sid=" +
                            deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditCheckpointScreening.do?method=editCheckpointScreening&rid=" + review.getId();
                } else {
                    links[i] = "ViewCheckpointScreening.do?method=viewCheckpointScreening&rid=" + review.getId();
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
                    links[i] = "CreateReview.do?method=createReview&sid=" +
                            deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditReview.do?method=editReview&rid=" + review.getId();
                } else {
                    links[i] = "ViewReview.do?method=viewReview&rid=" + review.getId();
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
                    links[i] = "CreateIterativeReview.do?method=createIterativeReview&sid=" +
                            deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditIterativeReview.do?method=editIterativeReview&rid=" + review.getId();
                } else {
                    links[i] = "ViewIterativeReview.do?method=viewIterativeReview&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.CHECKPOINT_REVIEW_DELIVERABLE_NAME)) {
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Checkpoint Review"),
                        deliverable.getSubmission(), deliverable.getResource(), false);

                if (review == null) {
                    links[i] = "CreateCheckpointReview.do?method=createCheckpointReview&sid=" +
                            deliverable.getSubmission();
                } else if (!review.isCommitted()) {
                    links[i] = "EditCheckpointReview.do?method=editCheckpointReview&rid=" + review.getId();
                } else {
                    links[i] = "ViewCheckpointReview.do?method=viewCheckpointReview&rid=" + review.getId();
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

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Review"),
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

                Review review = findReviewForSubmission(ActionsHelper.createReviewManager(),
                        LookupHelper.getScorecardType("Review"),
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
                    links[i] = "EditAggregationReview.do?method=editAggregationReview&rid=" + review.getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.FINAL_FIX_DELIVERABLE_NAME)) {
                if(deliverable.isComplete()) {
                    //get final fix uploads for current project phase
                    Upload[] uploads = ActionsHelper.getPhaseUploads(deliverable.getPhase(), "Final Fix");
                    links[i] = "DownloadFinalFix.do?method=downloadFinalFix&uid=" + uploads[0].getId();
                } else {
                    links[i] = "UploadFinalFix.do?method=uploadFinalFix&pid=" + deliverable.getProject();
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
                        links[i] = "EditFinalReview.do?method=editFinalReview&rid=" + review.getId();
                    } else {
                        links[i] = "ViewFinalReview.do?method=viewFinalReview&rid=" + review.getId();
                    }
                }
            } else if (delivName.equalsIgnoreCase(Constants.APPROVAL_DELIVERABLE_NAME)) {
                // Skip deliverables with empty Submission ID field, as no links can be generated for such deliverables
                if (deliverable.getSubmission() == null) {
                    continue;
                }

                Review[] reviews = ActionsHelper.searchReviews(deliverable.getPhase(), deliverable.getResource(), false);

                if (reviews.length == 0) {
                    links[i] = "CreateApproval.do?method=createApproval&sid=" +
                            deliverable.getSubmission();
                } else if (!reviews[0].isCommitted()) {
                    links[i] = "EditApproval.do?method=editApproval&rid=" + reviews[0].getId();
                } else {
                    links[i] = "ViewApproval.do?method=viewApproval&rid=" + reviews[0].getId();
                }
            } else if (delivName.equalsIgnoreCase(Constants.POST_MORTEM_DELIVERABLE_NAME)) {
                Review[] reviews = ActionsHelper.searchReviews(deliverable.getPhase(), deliverable.getResource(), false);

                if (reviews.length == 0) {
                    links[i] = "CreatePostMortem.do?method=createPostMortem&pid=" + deliverable.getProject();
//                    request.setAttribute("postMortemSubmissionId", deliverable.getSubmission().longValue());
                } else if (!reviews[0].isCommitted()) {
                    links[i] = "EditPostMortem.do?method=editPostMortem&rid=" + reviews[0].getId();
                } else {
                    links[i] = "ViewPostMortem.do?method=viewPostMortem&rid=" + reviews[0].getId();
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
     * TODO: Write documentation for this method.
     *
     * @return
     * @param deliverables
     * @throws BaseException
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

        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());

        UploadManager upMgr = ActionsHelper.createUploadManager();
        Submission[] submissions = upMgr.searchSubmissions(SubmissionFilterBuilder.createUploadIdFilter(upload.getId()));
        Submission submission = (submissions.length != 0) ? submissions[0] : null;

        String contentDisposition;
        if (submission != null) {
            contentDisposition = "attachment; filename=\"submission-" + submission.getId() + "-"
                                 + uploadedFile.getRemoteFileName() + "\"";
        } else {
            contentDisposition = "attachment; filename=\"upload-" + upload.getId() + "-"
                                 + uploadedFile.getRemoteFileName() + "\"";
        }

        outputDownloadedFile(uploadedFile, contentDisposition, response);
    }

    /**
     *
     * <p>Outputs the content of specified file to specified response for downloading by client.</p>
     *
     * @param uploadedFile an <code>UploadedFile</code> providing the details for the filr to be downloaded by client.
     * @param contentDisposition a <code>String</code> providing the value for <code>Content-Disposition</code> header.
     * @param response an <code>HttpServletResponse</code> representing the outgoing response.
     * @throws PersistenceException if an unexpected error occurs.
     * @throws FileDoesNotExistException if an unexpected error occurs.
     * @throws IOException if an unexpected error occurs.
     * @since 1.6
     */
    private void outputDownloadedFile(UploadedFile uploadedFile, String contentDisposition,
                                      HttpServletResponse response)
        throws PersistenceException, FileDoesNotExistException, IOException {

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
     * <p>Handles the request for downloading the submission of desired type.</p>
     *
     *
     * @param mapping an <code>ActionMapping</code> used to map the request to this action.
     * @param request an <code>HttpServletRequest</code> providing the details for incoming request.
     * @param response an <code>HttpServletResponse</code> providing the details for outgoing response.
     * @param errorMessageKey a <code>String</code> providing the key in message bundle for the error to be displayed
     *        to user.
     * @param viewAllSubmissionsPermName a <code>String</code> providing the name for permission for viewing all
     *        submissions.
     * @param viewMySubmissionsPermissionName a <code>String</code> providing the name for permission for viewing own
     *        submissions
     * @param viewSubmissionByScreenerPermissionName a <code>String</code> providing the name for permission for viewing
     *        submissions by screener.
     * @param viewMostRecentSubmissionsPermissionName a <code>String</code> providing the name for permission for
     *        viewing most recent submissions.
     * @param downloadCustomSubmissionPermissionName a <code>String</code> providing the name for permission for
     *        downloading submission for custom catalog.
     * @param viewWinningSubmissionPermissionName a <code>String</code> providing the name for permission for viewing
     *        winning submissions.
     * @param screeningPhaseName a <code>String</code> providing the name for screening phase type.
     * @param reviewPhaseName a <code>String</code> providing the name for review phase type.
     * @param screenerRoleNames a <code>String</code> array listing the names for screener roles.
     * @param reviewerRoleNames a <code>String</code> array listing the names for reviewer roles.
     * @param submissionType a <code>long</code> referencing the type of the submission being downloaded.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user in case of errors or
     *         <code>null</code> if submission is downloaded successfully.
     * @throws BaseException if an unexpected error occurs.
     * @throws IOException if an I/O error occurs.
     * @since 1.6.1
     */
    private ActionForward handleDownloadSubmission(ActionMapping mapping, HttpServletRequest request,
                                                   HttpServletResponse response, String errorMessageKey,
                                                   String viewAllSubmissionsPermName,
                                                   String viewMySubmissionsPermissionName,
                                                   String viewSubmissionByScreenerPermissionName,
                                                   String viewMostRecentSubmissionsPermissionName,
                                                   String downloadCustomSubmissionPermissionName,
                                                   String viewWinningSubmissionPermissionName,
                                                   String screeningPhaseName,
                                                   String reviewPhaseName,
                                                   String[] screenerRoleNames,
                                                   String[] reviewerRoleNames,
                                                   long submissionType)
        throws BaseException, IOException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = checkForCorrectUploadId(mapping, request, errorMessageKey);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get an upload the user wants to download
        Upload upload = verification.getUpload();

        // Verify that upload is a submission
        if (!upload.getUploadType().getName().equalsIgnoreCase("Submission")) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, errorMessageKey, "Error.NotASubmission", null);
        }

        // Verify the status of upload and check whether the user has permission to download old uploads
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted")
            && !AuthorizationHelper.hasUserPermission(request, viewAllSubmissionsPermName)) {
            ActionsHelper.logDownloadAttempt(request, upload, false);
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, errorMessageKey, "Error.UploadDeleted", null);
        }

        // Get all phases for the current project (needed to do permission checks)
        Project project = verification.getProject(); 
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(false), verification.getProject());

        boolean noRights = true;

        // Check if it is the Thurgood server trying to get the submission.
        // If it's Thurgood, authorize the request.
        boolean useThurgood = !isEmptyOrNull(project.getProperty("Thurgood Platform")) &&
                !isEmptyOrNull(project.getProperty("Thurgood Language"));
        if (useThurgood && !AuthorizationHelper.isUserLoggedIn(request)) {
            if (ConfigHelper.getThurgoodUsername().equals(request.getParameter("username")) &&
                ConfigHelper.getThurgoodPassword().equals(request.getParameter("password"))) {
                noRights = false;
            }
        }

        if (AuthorizationHelper.hasUserPermission(request, viewAllSubmissionsPermName)) {
            noRights = false;
        }

        if (AuthorizationHelper.hasUserPermission(request, viewMySubmissionsPermissionName)) {
            long owningResourceId = upload.getOwner();
            Resource[] myResources = (Resource[]) request.getAttribute("myResources");
            for (Resource myResource : myResources) {
                if (myResource.getId() == owningResourceId) {
                    noRights = false;
                    break;
                }
            }
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, viewSubmissionByScreenerPermissionName)) {
            // Determine whether Screening phase has already been opened (does not have Scheduled status)
            final boolean isScreeningOpen = ActionsHelper.isInOrAfterPhase(phases, 0, screeningPhaseName);
            // If screener tries to download submission before Screening phase opens,
            // notify him about this wrong-doing and do not let perform the action
            if (AuthorizationHelper.hasUserRole(request, screenerRoleNames) && !isScreeningOpen) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, errorMessageKey, "Error.IncorrectPhase", null);
            }
            noRights = false; // TODO: Check if screener can download this submission
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, viewMostRecentSubmissionsPermissionName)) {
            // If reviewer tries to download submission before Review phase opens,
            // notify him about this wrong-doing and do not let perform the action
            if (AuthorizationHelper.hasUserRole(request, reviewerRoleNames)
                    && !ActionsHelper.isInOrAfterPhase(phases, 0, reviewPhaseName)) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, errorMessageKey, "Error.IncorrectPhase", null);
            }

            // Regular reviewers can view checkpoint submissions only after the Review phase has started
            if (submissionType == 3
                    && !AuthorizationHelper.hasUserRole(request, Constants.CHECKPOINT_REVIEWER_ROLE_NAME)
                    && AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES)
                    && !ActionsHelper.isInOrAfterPhase(phases, 0, Constants.REVIEW_PHASE_NAME)) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, errorMessageKey, "Error.IncorrectPhase", null);
            }
            noRights = false;
        }

        // For the Submitters we only allow to download others' submissions if the user has at least passed screening.
        if (noRights && AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) {
            // Get all submissions for this user.
            Resource resource = ActionsHelper.getMyResourceForRole(request, Constants.SUBMITTER_ROLE_NAME);
            UploadManager upMgr = ActionsHelper.createUploadManager();
            Long[] subIds = resource.getSubmissions();

            // Check that the user has a submission that passed screening.
            // We don't need to check the current phase because if it is still prior to the Appeals Response
            // the user won't be able to download others' submissions anyway.
            boolean passedScreening = false;
            for (Long id : subIds) {
                Submission submission = upMgr.getSubmission(id);
                if (submission != null && submission.getSubmissionType().getName().equals(Constants.CONTEST_SUBMISSION_TYPE_NAME) &&
                    !submission.getSubmissionStatus().getName().equals(Constants.FAILED_SCREENING_SUBMISSION_STATUS_NAME)) {
                    passedScreening = true;
                    break;
                }
            }

            if (!passedScreening) {
                ActionsHelper.logDownloadAttempt(request, upload, false);
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, "ViewSubmission", "Error.NoScreeningPassed", null);
            }
            
            // Submitters can download others' contest submissions and checkpoint submissions only
            // after the Appeals Response phase (or Review phase if Appeals Response phase is not present)
            // is closed. This also takes care of contests with Iterative Review phase, for which submitters
            // can't download others' submissions at all.
            if (submissionType == 1 || submissionType == 3) {
                Phase reviewPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.APPEALS_RESPONSE_PHASE_NAME);
                if (reviewPhase == null) {
                    reviewPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.REVIEW_PHASE_NAME);
                }
                boolean isReviewFinished = (reviewPhase != null) && (reviewPhase.getPhaseStatus().getId() == 3);
                if (isReviewFinished) {
                    noRights = false;
                }
            }
        }

        if (noRights && submissionType == 1
                && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_CURRENT_ITERATIVE_REVIEW_SUBMISSION)) {
            Submission[] submissions = ActionsHelper.getProjectSubmissions(upload.getProject(),
                    Constants.CONTEST_SUBMISSION_TYPE_NAME, null, false);
            Submission earliestSubmission = null;
            Resource resource = ActionsHelper.getMyResourceForRole(request, Constants.ITERATIVE_REVIEWER_ROLE_NAME);
            List<Filter> filters = new ArrayList<Filter>();
            Filter filterScorecard = new EqualToFilter("scorecardType", LookupHelper.getScorecardType("Iterative Review").getId());
            Filter filterReviewer = new EqualToFilter("reviewer", resource.getId());
            filters.add(filterScorecard);
            filters.add(filterReviewer);
            Filter filterForReviews = new AndFilter(filters);
            ReviewManager revMgr = ActionsHelper.createReviewManager();
            Review[] reviews = revMgr.searchReviews(filterForReviews, false);
            for (Submission submission : submissions) {
                // Find the earliest active submission, i.e. the next one in the queue.
                if (submission.getSubmissionStatus().getName().equals("Active") && (earliestSubmission == null
                        || earliestSubmission.getCreationTimestamp().compareTo(submission.getCreationTimestamp()) > 0)) {
                    earliestSubmission = submission;
                }
                // If this reviewer already has a review scorecard submitted for this submission, that reviewer can
                // download the submission.
                if (submission.getUpload().getId() == upload.getId()) {
                    for (Review review : reviews) {
                        if (review.getSubmission() == submission.getId()) {
                            noRights = false;
                        }
                    }
                }
            }
            
            // If it's the "current" submission (i.e. the next one in the queue), the reviewer can download the submission.
            if (earliestSubmission != null && earliestSubmission.getUpload().getId() == upload.getId()) {
                noRights = false;
            }
        }

        ActionsHelper.logDownloadAttempt(request, upload, !noRights);
        
        if (noRights) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    errorMessageKey, "Error.NoPermission", Boolean.FALSE);
        }

        processSubmissionDownload(upload, request, response);
        
        return null;
    }

    /**
     * <p>Handles the request for uploading a submission to project.</p>
     * <p>
     * Change in 1.14: The method is updated to support thurgoodJobId property of Submission.
     * </p>
     *
     * @param mapping an <code>ActionMapping</code> used to map the request to this action.
     * @param form an <code>ActionForm</code> providing the form submitted by user.
     * @param request an <code>HttpServletRequest</code> providing the details for incoming request.
     * @param submissionTypeName a <code>String</code> providing the name of submission type.
     * @param submitPermissionName a <code>String</code> providing the permission to be used for authorizing users to
     *        perform submission upload.
     * @param phaseName a <code>String</code> providing the name of the type of project phase which submission maps to.
     * @return an <code>ActionForward</code> referencing the next view to be displayed to user.
     * @throws BaseException if an unexpected error occurs.
     * @since 1.6.1
     */
    private ActionForward handleUploadSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                 String submissionTypeName, String submitPermissionName,
                                                 String phaseName)
        throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Determine if this request is a post back
        final boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, submitPermissionName, postBack);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve current project
        Project project = verification.getProject();
        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);

        Phase currentPhase = ActionsHelper.getPhase(phases, true, phaseName);
        if (currentPhase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    submitPermissionName, "Error.IncorrectPhase", null);
        }

        // We don't allow user to upload contest submissions/checkpoint submissions for studio contest
        if ("Studio".equalsIgnoreCase(project.getProjectCategory().getProjectType().getName())
                && (submissionTypeName.equals(Constants.CONTEST_SUBMISSION_TYPE_NAME) ||
                    submissionTypeName.equals(Constants.CHECKPOINT_SUBMISSION_TYPE_NAME))) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    submitPermissionName, "Error.UploadForStudio", null);
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
                                                    submitPermissionName, "Error.EmptyFileUploaded", null);
        }

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        // Obtain an instance of File Upload Manager
        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForRole(request, "Submitter");

        Submission submission = new Submission();
        Upload upload = new Upload();

        upload.setProject(project.getId());
        upload.setProjectPhase(currentPhase.getId());
        upload.setOwner(resource.getId());
        upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
        upload.setUploadType(LookupHelper.getUploadType("Submission"));
        upload.setParameter(uploadedFile.getFileId());

        submission.setUpload(upload);
        submission.setSubmissionStatus(LookupHelper.getSubmissionStatus("Active"));
        submission.setSubmissionType(LookupHelper.getSubmissionType(submissionTypeName));

        // Get the name (id) of the user performing the operations
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));

        // If the project DOESN'T allow multiple submissions hence its property "Allow
        // multiple submissions" will be false
        Boolean allowOldSubmissions = Boolean.parseBoolean((String) project.getProperty("Allow multiple submissions"));
        log.debug("Allow Multiple Submissions : " + allowOldSubmissions);

        UploadManager upMgr = ActionsHelper.createUploadManager();
        upMgr.createUpload(upload, operator);

        // set the Thurgood job id if the contest is from CloudSpokes
        String thurgoodPlatform = (String) project.getProperty("Thurgood Platform");
        String thurgoodLanguage = (String) project.getProperty("Thurgood Language");
        // Thurgood will be used only for Contest Submissions
        if (!isEmptyOrNull(thurgoodPlatform) && !isEmptyOrNull(thurgoodLanguage)
                && submissionTypeName.equals(Constants.CONTEST_SUBMISSION_TYPE_NAME)) {

            Map<String, String> parameters =
                buildCreateThurgoodParameters(project, thurgoodPlatform, thurgoodLanguage, upload.getId(), request);
            if (parameters != null) {
                String thurgoodJobId = submitThurgoodJob(createThurgoodJob(parameters));
                submission.setThurgoodJobId(thurgoodJobId);
            }
        }

        upMgr.createSubmission(submission, operator);
        resource.addSubmission(submission.getId());
        ActionsHelper.createResourceManager().updateResource(resource, operator);

        Submission[] activeSubmissions = ActionsHelper.getResourceSubmissions(resource.getId(), submissionTypeName,
                Constants.ACTIVE_SUBMISSION_STATUS_NAME, false);

        // Now depending on whether the project allows multiple submissions or not mark the old submission
        // and the upload as deleted.
        if (activeSubmissions.length > 1 && !allowOldSubmissions) {
            SubmissionStatus deleteSubmissionStatus = LookupHelper.getSubmissionStatus("Deleted");
            UploadStatus deleteUploadStatus = LookupHelper.getUploadStatus("Deleted");
            for (Submission activeSubmission : activeSubmissions) {
                if (activeSubmission.getId() != submission.getId()) {
                    activeSubmission.getUpload().setUploadStatus(deleteUploadStatus);
                    activeSubmission.setSubmissionStatus(deleteSubmissionStatus);
                    upMgr.updateUpload(activeSubmission.getUpload(), operator);
                    upMgr.updateSubmission(activeSubmission, operator);
                }
            }
        }

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
    }

    /**
     * Build the parameters for creating Thurgood job.
     * @param project the current project
     * @param thurgoodPlatform the Thurgood platform
     * @param thurgoodLanguage the Thurgood language
     * @param uploadId the upload id
     * @param request the http servlet request
     * @return the parameters map
     * @throws BaseException if any error
     * @since 1.14
     */
    private Map<String, String> buildCreateThurgoodParameters(Project project, String thurgoodPlatform,
        String thurgoodLanguage, long uploadId, HttpServletRequest request) throws BaseException {

        // Get the ID of the sender
        long senderId = AuthorizationHelper.getLoggedInUserId(request);

        // Obtain an instance of User Retrieval
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        // Retrieve information about an external user by its ID
        ExternalUser user = userRetrieval.retrieveUser(senderId);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", user.getEmail());
        parameters.put("language", thurgoodLanguage);
        parameters.put("userId", user.getHandle());
        parameters.put("notification", "email");
        parameters.put("codeUrl", ConfigHelper.getThurgoodCodeURL() + uploadId);
        parameters.put("platform", thurgoodPlatform);
        return parameters;
    }

    /**
     * Check if the given object is a null or empty string.
     * As it is called, it is guaranteed that the value is of string type.
     * @param value the value to be checked.
     * @return true if the string object is null or empty.
     * @since 1.14
     */
    private boolean isEmptyOrNull(Object value) {
        return value == null || ((String) value).trim().length() == 0;
    }

    /**
     * Submit the Thurgood job.
     * @param id the Thurgood job id.
     * @return the submitted job id, null if submitting fails
     * @since 1.14
     */
    private static String submitThurgoodJob(String id) {
        String authHeader = "Token token=\"%s\"";
        if (id != null) {
            HttpURLConnection con = null;
            BufferedReader in = null;
            try {
                URL obj = new URL(ConfigHelper.getThurgoodApiURL() + "/" + id + "/submit");
                con = (HttpURLConnection) obj.openConnection();
                // set connection timeout
                con.setConnectTimeout(ConfigHelper.getThurgoodTimeout());

                // optional default is GET
                con.setRequestMethod("PUT");

                //add request header
                con.setRequestProperty("Authorization", String.format(authHeader, ConfigHelper.getThurgoodApiKey()));
                int responseCode = con.getResponseCode();

                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    LoggingHelper.logError("Server backend error happened with response " + response);
                    return null;
                }
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode root = (ObjectNode) mapper.readTree(response.toString());
                if (root.get("success") == null || !"true".equalsIgnoreCase(root.get("success").asText())) {
                    LoggingHelper.logError("Submitting Thurgood job failed with response " + response);
                    return null;
                }
                return id;
            } catch (MalformedURLException e) {
                LoggingHelper.logException("The requested url is malformed", e);
            } catch (IOException e) {
                LoggingHelper.logException("IO error while submitting the Thurgood job", e);
            } catch (Exception e) {
                LoggingHelper.logException("Error occurs while submitting the Thurgood job", e);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                closeResource(in);
            }
        }
        return null;
    }

    /**
     * Create the Thurgood Job.
     * @param parameters the submitted parameters
     * @return the created job id, null if creation fails
     * @since 1.14
     */
    private static String createThurgoodJob(Map<String, String> parameters) {
        String authHeader = "Token token=\"%s\"";
        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        try {
            URL obj = new URL(ConfigHelper.getThurgoodApiURL());
            con = (HttpURLConnection) obj.openConnection();
            // set connection timeout
            con.setConnectTimeout(ConfigHelper.getThurgoodTimeout());

            //set header
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", String.format(authHeader, ConfigHelper.getThurgoodApiKey()));

            StringBuilder urlParameters = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                urlParameters.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }

            // Send post request
            con.setDoOutput(true);
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters.toString());
            wr.flush();

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                LoggingHelper.logError("Server backend error happened with response " + response);
                return null;
            }

            //get the job id.
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = (ObjectNode) mapper.readTree(response.toString());
            if (root.get("success") == null || !"true".equalsIgnoreCase(root.get("success").asText())
                    || root.get("data") == null) {
                LoggingHelper.logError("Creating Thurgood job failed with response " + response);
                return null;
            }
            JsonNode idNode = root.get("data").get("_id");
            if (idNode == null || idNode.asText().trim().length() == 0) {
                LoggingHelper.logError("No Created Job ID returned with response " + response);
                return null;
            }
            return idNode.asText();
        } catch (MalformedURLException e) {
            LoggingHelper.logException("The requested url is malformed", e);
        } catch (IOException e) {
            LoggingHelper.logException("IO error while creating the Thurgood job", e);
        } catch (Exception e) {
            LoggingHelper.logException("Error occurs while creating the Thurgood job", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
            closeResource(wr);
            closeResource(in);
        }
        return null;
    }

    /**
     * Close the resource if it is not null.
     * @param resource the resource to be closed.
     * @since 1.14
     */
    private static void closeResource(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                LoggingHelper.logException("IO error while closing the resource", e);
            }
        }
    }
}

