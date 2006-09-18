/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

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
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.Notification;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.RemoteFileUpload;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;

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
 * @author TCSAssemblyTeam
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
     */
    public ActionForward viewProjectDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
            checkForCorrectProjectId(mapping, request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to view
        Project project = verification.getProject();
        // Get Message Resources used for this request
        MessageResources messages = getResources(request);

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, project, messages);

        // Place a string that represents "my" current role(s) into the request
        ActionsHelper.retrieveAndStoreMyRole(request, getResources(request));
        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        // Place an information about the amount of "my" payment into the request
        request.setAttribute("myPayment", ActionsHelper.determineMyPayment(myResources));
        // Place an information about my payment status into the request
        request.setAttribute("wasPaid", ActionsHelper.determineMyPaymentPaid(myResources));

        // Obtain an instance of Phase Manager
        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(request);

        // Calculate the date when this project is supposed to end
        com.topcoder.project.phases.Project phProj = phaseMgr.getPhases(project.getId());
        phProj.calcEndDate();
        // Get all phases for the current project
        Phase[] phases = phProj.getAllPhases();

        // Place all phases of the project into the request
        request.setAttribute("phases", phases);

        Deliverable[] deliverables = ActionsHelper.getAllDeliverablesForActivePhases(
                request, ActionsHelper.createDeliverableManager(request));
        Deliverable[] myDeliverables = ActionsHelper.getMyDeliverables(deliverables, myResources);
        Deliverable[] outstandingDeliverables = ActionsHelper.getOutstandingDeliverables(deliverables);

        request.setAttribute("myDeliverables", myDeliverables);
        request.setAttribute("outstandingDeliverables", outstandingDeliverables);

        Format format = new SimpleDateFormat("MM.dd.yyyy hh:mm a");
        String[] myDeliverableDates = new String[myDeliverables.length];
        String[] outstandingDeliverableDates = new String[outstandingDeliverables.length];
        int[] myDeliverableStatuses = new int[myDeliverables.length];
        int[] outstandingDeliverableStatuses = new int[outstandingDeliverables.length];

        // Obtain an array of all active phases of the project
        Phase[] activePhases = ActionsHelper.getActivePhases(phases);
        long currentTime = (new Date()).getTime();

        for (int i = 0; i < myDeliverables.length; ++i) {
            Deliverable deliverable = myDeliverables[i];
            if (deliverable.isComplete()) {
                myDeliverableDates[i] = format.format(deliverable.getCompletionDate());
                myDeliverableStatuses[i] = 0;
            } else {
                Phase phase = ActionsHelper.getPhaseForDeliverable(activePhases, deliverable);
                myDeliverableDates[i] = format.format(phase.calcEndDate());

                long deliverableTime = phase.calcEndDate().getTime();
                if (currentTime > deliverableTime) {
                    myDeliverableStatuses[i] = 2; // Late
                } else if (currentTime + (2 * 60 * 60 * 1000) > deliverableTime) {
                    myDeliverableStatuses[i] = 1; // Deadline near
                } else {
                    myDeliverableStatuses[i] = 0;
                }
            }
        }
        for (int i = 0; i < outstandingDeliverables.length; ++i) {
            Deliverable deliverable = outstandingDeliverables[i];
            if (deliverable.isComplete()) {
                outstandingDeliverableDates[i] = format.format(deliverable.getCompletionDate());
                outstandingDeliverableStatuses[i] = 0;
            } else {
                Phase phase = ActionsHelper.getPhaseForDeliverable(activePhases, deliverable);
                outstandingDeliverableDates[i] = format.format(phase.calcEndDate());

                long deliverableTime = phase.calcEndDate().getTime();
                if (currentTime > deliverableTime) {
                    outstandingDeliverableStatuses[i] = 2; // Late
                } else if (currentTime + (2 * 60 * 60 * 1000) > deliverableTime) {
                    outstandingDeliverableStatuses[i] = 1; // Deadline near
                } else {
                    outstandingDeliverableStatuses[i] = 0;
                }
            }
        }

        request.setAttribute("myDeliverableDates", myDeliverableDates);
        request.setAttribute("outstandingDeliverableDates", outstandingDeliverableDates);
        request.setAttribute("myDeliverableStatuses", myDeliverableStatuses);
        request.setAttribute("outstandingDeliverableStatuses", outstandingDeliverableStatuses);

        String[] displayedStart = new String[phases.length];
        String[] displayedEnd = new String[phases.length];
        String[] originalStart = new String[phases.length];
        String[] originalEnd = new String[phases.length];
        String strOrigStartTime = messages.getMessage("viewProjectDetails.Timeline.OriginalStartTime") + " ";
        String strOrigEndTime = messages.getMessage("viewProjectDetails.Timeline.OriginalEndTime") + " ";
        long projectStartTime = phProj.getStartDate().getTime();
        // The following two arrays are used to display Gantt chart
        long[] ganttOffsets = new long[phases.length];
        long[] ganttLengths = new long[phases.length];
        // List of scorecard templates used for this project
        List scorecardTemplates = new ArrayList();

        // Iterate over all phases determining dates, durations and assigned scorecards
        for (int i = 0; i < phases.length; ++i) {
            // Get a phase for this iteration
            Phase phase = phases[i];

            Date startDate = phase.calcStartDate();
            Date endDate = phase.calcEndDate();

            // Determine the strings to display for start/end dates
            displayedStart[i] = format.format(startDate);
            originalStart[i] = strOrigStartTime + format.format(phase.getScheduledStartDate());
            displayedEnd[i] = format.format(endDate);
            originalEnd[i] = strOrigEndTime + format.format(phase.getScheduledEndDate());

            // Determine offsets and lengths of the bars in Gantt chart, in hours
            ganttOffsets[i] = (startDate.getTime() - projectStartTime) / (60 * 60 * 1000);
            ganttLengths[i] = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000);

            // Get a scorecard template associated with this phase if any
            Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                    ActionsHelper.createScorecardManager(request), phase);
            // If there is a scorecard template for the phase, store it in the list
            if (scorecardTemplate != null) {
                scorecardTemplates.add(scorecardTemplate);
            }
        }

        /*
         * Place all gathered information about phases into the request as attributes
         */

        // Place phases' start/end dates
        request.setAttribute("displayedStart", displayedStart);
        request.setAttribute("displayedEnd", displayedEnd);
        request.setAttribute("originalStart", originalStart);
        request.setAttribute("originalEnd", originalEnd);
        // Place phases durations for Gantt chart
        request.setAttribute("ganttOffsets", ganttOffsets);
        request.setAttribute("ganttLengths", ganttLengths);
        // Determine the amount of pixels to display in Gantt chart for every hour
        request.setAttribute("pixelsPerHour", ConfigHelper.getPixelsPerHour());
        // Place information about used scorecard templates
        request.setAttribute("scorecardTemplates", scorecardTemplates);

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        Resource[] allProjectResources = null;
        ExternalUser[] allProjectExtUsers = null;

        // Determine if the user has permission to view a list of resources for the project
        if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECT_RESOURCES_PERM_NAME) ||
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_REGISTRATIONS_PERM_NAME)) {
            // Build a filter to fetch all resources for the current project
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(project.getId());
            // Get an array of resources for the project
            allProjectResources = resMgr.searchResources(filterProject);

            // Prepare an array to store External User IDs
            long[] extUserIds = new long[allProjectResources.length];
            // Fill the array with user IDs retrieved from resource properties
            for (int i = 0; i < allProjectResources.length; ++i) {
                String userID = (String) allProjectResources[i].getProperty("External Reference ID");
                extUserIds[i] = Long.valueOf(userID).longValue();
            }

            ExternalUser[] extUsers = null;
            // If there are no resource for this project defined,
            // there will be no external users
            if (extUserIds.length != 0) {
                // Obtain an instance of User Retrieval
                UserRetrieval usrMgr = ActionsHelper.createUserRetrieval(request);
                // Retrieve external users for the list of resources using batch retrieval
                extUsers = usrMgr.retrieveUsers(extUserIds);
            } else {
                extUsers = new ExternalUser[0];
            }

            // This is final array for External User objects. It is needed because the previous
            // operation may return shorter array than there are resources for the project
            // (sometimes several resources can be associated with one external user)
            allProjectExtUsers = new ExternalUser[allProjectResources.length];

            for (int i = 0; i < extUserIds.length; ++i) {
                for (int j = 0; j < extUsers.length; ++j) {
                    if (extUsers[j].getId() == extUserIds[i]) {
                        allProjectExtUsers[i] = extUsers[j];
                        break;
                    }
                }
            }
        }

        if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECT_RESOURCES_PERM_NAME)) {
            // Place resources and external users into the request
            request.setAttribute("resources", allProjectResources);
            request.setAttribute("users", allProjectExtUsers);
        }

        int[] phaseGroupIndexes = new int[phases.length];
        List phaseGroups = new ArrayList();
        int phaseGroupIdx = -1;
        PhaseGroup phaseGroup = null;

        for (int i = 0; i < phases.length; ++i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            String phaseName = phase.getPhaseType().getName();

            if (phaseGroupIdx == -1 ||
                    !ConfigHelper.isPhaseGroupContainsPhase(phaseGroupIdx, phaseName)) {
                phaseGroupIdx = ConfigHelper.findPhaseGroupForPhaseName(phaseName);

                String appFuncName = ConfigHelper.getPhaseGroupAppFunction(phaseGroupIdx);

                if (!appFuncName.equals(Constants.VIEW_REGISTRANTS_APP_FUNC) ||
                        AuthorizationHelper.hasUserPermission(request, Constants.VIEW_REGISTRATIONS_PERM_NAME)) {
                    phaseGroup = new PhaseGroup();
                    phaseGroups.add(phaseGroup);

                    phaseGroup.setName(messages.getMessage(ConfigHelper.getPhaseGroupNameKey(phaseGroupIdx)));
                    phaseGroup.setAppFunc(appFuncName);
                } else {
                    phaseGroup = null;
                }
            }

            if (phaseGroup == null) {
                phaseGroupIndexes[i] = -1;
                continue;
            }

            if (phaseGroup.getAppFunc().equals(Constants.VIEW_REGISTRANTS_APP_FUNC)) {
                List registrants = new ArrayList();

                for (int j = 0; j < allProjectResources.length; ++j) {
                    Resource resource = allProjectResources[j];

                    if (resource.getResourceRole().getName().equalsIgnoreCase("Submitter")) {
                        registrants.add(resource);
                    }
                }

                phaseGroup.setAdditionalInfo(registrants);
            }

            phaseGroupIndexes[i] = phaseGroups.size() - 1;
        }

        request.setAttribute("phaseGroupIndexes", phaseGroupIndexes);
        request.setAttribute("phaseGroups", phaseGroups);

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

        // Check permissions
        request.setAttribute("isAllowedToViewSVNLink",
                new Boolean(AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SVN_LINK_PERM_NAME)));
        request.setAttribute("isAllowedToViewPayment",
                new Boolean(AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_PAY_INFO_PERM_NAME)));

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
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward contactManager(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = checkForCorrectProjectId(mapping, request, Constants.CONTACT_PM_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        }

        return mapping.findForward((postBack) ? Constants.SUCCESS_FORWARD_NAME : Constants.DISPLAY_PAGE_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
            checkForCorrectProjectId(mapping, request, Constants.PERFORM_SUBM_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        if (postBack != true) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        // Retrieve current project
        Project project = verification.getProject();

        DynaValidatorForm uploadSubmissionForm = (DynaValidatorForm) form;
        FormFile file = (FormFile) uploadSubmissionForm.get("file");

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        FileUpload fileUpload = new RemoteFileUpload("com.topcoder.servlet.request.RemoteFileUpload");

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForPhase(request, null);

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(resource.getId());

        Filter filterForSubmission = new AndFilter(filterProject, filterResource);

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        Submission[] submissions = upMgr.searchSubmissions(filterForSubmission);
        Submission submission = (submissions.length != 0) ? submissions[0] : null;
        Upload upload = (submission != null) ? submission.getUpload() : null;
        Upload deletedUpload = null;

        UploadStatus[] uploadStatuses = upMgr.getAllUploadStatuses();

        if (upload == null) {
            upload = new Upload();

            UploadType[] uploadTypes = upMgr.getAllUploadTypes();

            upload.setProject(project.getId());
            upload.setOwner(resource.getId());
            upload.setUploadStatus(ActionsHelper.findUploadStatusByName(uploadStatuses, "Active"));
            upload.setUploadType(ActionsHelper.findUploadTypeByName(uploadTypes, "Submission"));
            upload.setParameter(uploadedFile.getFileId());

            SubmissionStatus[] submissionStatuses = upMgr.getAllSubmissionStatuses();

            submission = new Submission();
            submission.setUpload(upload);
            submission.setSubmissionStatus(ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active"));
        } else {
            deletedUpload = upload;

            upload = new Upload();
            upload.setProject(deletedUpload.getProject());
            upload.setOwner(deletedUpload.getOwner());
            upload.setUploadStatus(deletedUpload.getUploadStatus());
            upload.setUploadType(deletedUpload.getUploadType());
            upload.setParameter(uploadedFile.getFileId());

            submission.setUpload(upload);

            deletedUpload.setUploadStatus(ActionsHelper.findUploadStatusByName(uploadStatuses, "Deleted"));
        }

        if (deletedUpload != null) {
            upMgr.updateUpload(deletedUpload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }
        upMgr.createUpload(upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        if (submissions.length == 0) {
            upMgr.createSubmission(submission, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        } else {
            upMgr.updateSubmission(submission, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

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
     */
    public ActionForward downloadSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException, IOException {
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
                    mapping, getResources(request), request, "ViewSubmission", "Error.NotASubmission");
        }

        // Verify the status of upload and check whether the user has permission to download old uploads
        if (upload.getUploadStatus().getName().equalsIgnoreCase("Deleted") &&
                !AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, "ViewSubmission", "Error.UploadDeleted");
        }

        boolean noRights = true;

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

        if (noRights && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME)) {
            noRights = false;
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENER_SUBM_PERM_NAME)) {
            // TODO: Check if screener can download this submission
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_WINNING_SUBM_PERM_NAME)) {
            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager(request);
            Resource submitter = resMgr.getResource(upload.getOwner());

            if ("1".equals(submitter.getProperty("Placement"))) {
                noRights = false;
            }
        }

        if (noRights && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_AAR_PERM_NAME)) {
            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager(request);
            Resource submitter = resMgr.getResource(upload.getOwner());
            String placement = (String) submitter.getProperty("Placement");

            if (placement != null && placement.trim().length() != 0) {
                noRights = false;
            }
        }

        if (noRights) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, "ViewSubmission", "Error.NoPermission");
        }

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(upload.getProject());
        Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(upload.getOwner());
        Filter filterUpload = SubmissionFilterBuilder.createUploadIdFilter(upload.getId());

        Filter filter = new AndFilter(Arrays.asList(new Filter[] {filterProject, filterResource, filterUpload}));

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        Submission[] submissions = upMgr.searchSubmissions(filter);
        Submission submission = (submissions.length != 0) ? submissions[0] : null;

        FileUpload fileUpload = new RemoteFileUpload("com.topcoder.servlet.request.RemoteFileUpload");
        UploadedFile uploadedFile = fileUpload.getUploadedFile(upload.getParameter());

        InputStream in = uploadedFile.getInputStream();

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) uploadedFile.getSize());
        response.setHeader("Content-Location",
                "submission-" + ((submission != null) ? Long.toString(submission.getId()) : "") + ".zip");

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
     * TODO: Write sensible description for method uploadFinalFix here
     *
     * @return TODO: Write sensible description of return value for method uploadFinalFix
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward uploadFinalFix(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method uploadFinalFix here
        return null;
    }

    /**
     * TODO: Write sensible description for method downloadFinalFix here
     *
     * @return TODO: Write sensible description of return value for method downloadFinalFix
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward downloadFinalFix(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method downloadFinalFix here
        return null;
    }

    /**
     * TODO: Write sensible description for method uploadTestCase here
     *
     * @return TODO: Write sensible description of return value for method uploadTestCase
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward uploadTestCase(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method uploadTestCase here
        return null;
    }

    /**
     * TODO: Write sensible description for method downloadTestCase here
     *
     * @return TODO: Write sensible description of return value for method downloadTestCase
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward downloadTestCase(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method downloadTestCase here
        return null;
    }

    /**
     * TODO: Write sensible description for method deleteSubmission here
     *
     * @return TODO: Write sensible description of return value for method deleteSubmission
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward deleteSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method deleteSubmission here
        return null;
    }

    /**
     * TODO: Write sensible description for method downloadDocument here
     *
     * @return TODO: Write sensible description of return value for method downloadDocument
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward downloadDocument(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method downloadDocument here
        return null;
    }

    /**
     * TODO: Write sensible description for method viewAutoScreening here
     *
     * @return TODO: Write sensible description of return value for method viewAutoScreening
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward viewAutoScreening(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method viewAutoScreening here
        return null;
    }

    /**
     * This method verifies the request for ceratin conditions to be met. This includes verifying if
     * the user has specified an ID of the project he wants to perform an operation on, if the ID of
     * the project specified by user denotes existing project, and whether the user has rights to
     * perform the operation specified by <code>permission</code> parameter.
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
     * @throws BaseException
     *             if any error occurs.
     */
    private CorrectnessCheckResult checkForCorrectProjectId(ActionMapping mapping,
            HttpServletRequest request, String permission)
        throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Project ID was specified and denotes correct project
        String pidParam = request.getParameter("pid");
        if (pidParam == null || pidParam.trim().length() == 0) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ProjectIdNotSpecified"));
            // Return the result of the check
            return result;
        }

        long pid;

        try {
            // Try to convert specified pid parameter to its integer representation
            pid = Long.parseLong(pidParam, 10);
        } catch (NumberFormatException nfe) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ProjectNotFound"));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Project Manager
        ProjectManager projMgr = ActionsHelper.createProjectManager(request);
        // Get Project by its id
        Project project = projMgr.getProject(pid);
        // Verify that project with given ID exists
        if (project == null) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ProjectNotFound"));
            // Return the result of the check
            return result;
        }

        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, pid);

        // If permission parameter was not null or empty string ...
        if (permission != null) {
            // ... verify that this permission is granted for currently logged in user
            if (!AuthorizationHelper.hasUserPermission(request, permission)) {
                result.setForward(ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, permission, "Error.NoPermission"));
                // Return the result of the check
                return result;
            }
        }

        return result;
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
                    mapping, getResources(request), request, errorMessageKey, "Error.UploadIdNotSpecified"));
            // Return the result of the check
            return result;
        }

        long uid;

        try {
            // Try to convert specified uid parameter to its integer representation
            uid = Long.parseLong(uidParam, 10);
        } catch (NumberFormatException nfe) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, errorMessageKey, "Error.UploadNotFound"));
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
                    mapping, getResources(request), request, errorMessageKey, "Error.UploadNotFound"));
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
}
