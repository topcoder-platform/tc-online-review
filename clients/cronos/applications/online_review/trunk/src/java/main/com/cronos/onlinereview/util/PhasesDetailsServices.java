/*
 * Copyright (C) 2006 - 2014 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.opensymphony.xwork2.TextProvider;
import com.topcoder.management.deliverable.SubmissionType;
import com.topcoder.management.resource.ResourceManager;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.model.PhaseGroup;
import com.cronos.onlinereview.model.PhasesDetails;
import com.cronos.onlinereview.util.Comparators.SubmissionComparer;
import com.cronos.onlinereview.util.Comparators.CheckpointSubmissionComparator;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>An utility class for getting the details for particular project phase.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public final class PhasesDetailsServices {

    /**
     * This constructor is declared private to prevent class instantiation.
     */
    private PhasesDetailsServices() {
        // nothing
    }

    /**
     * Gets the phases details.
     *
     * @param request the request
     * @param textProvider the text provider
     * @param project the project
     * @param phases the phases
     * @param allProjectResources the all project resources
     * @param allProjectExternalUsers the all project external users
     * @return the phases details
     * @throws BaseException the base exception
     */
    public static PhasesDetails getPhasesDetails(HttpServletRequest request, TextProvider textProvider, Project project,
            Phase[] phases, Resource[] allProjectResources, ExternalUser[] allProjectExternalUsers)
        throws BaseException {

        // Validate parameters first
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(textProvider, "textProvider");
        ActionsHelper.validateParameterNotNull(phases, "phases");

        // Move Post-Mortem phase to appropriate place in the list in order to prevent splitting phase group tabs into
        // separate same-named tabs
        Phase[] originalPhases = phases;
        Phase[] phasesCopy = new Phase[phases.length];
        System.arraycopy(phases, 0, phasesCopy, 0, phases.length);
        phases = phasesCopy;

        // Determine the index of Post-Mortem phase in array and if it is present get the phase which Post-Mortem
        // phase depends on
        Phase postMortemPhasePredecessor = null;
        Phase postMortemPhase = null;
        for (int i = 0; i < phases.length; i++) {
            Phase phase = phases[i];
            if (phase.getPhaseType().getName().equals(Constants.POST_MORTEM_PHASE_NAME)) {
                postMortemPhase = phase;
                System.arraycopy(phases, i + 1, phases, i, phases.length - i - 1);
            } else {
                if (phase.getPhaseStatus().getId() == 3) { // Closed
                    postMortemPhasePredecessor = phase;
                }
            }
        }

        // If Post-Mortem phase exists and depends on some other phase then
        if (postMortemPhase != null) {
            if (postMortemPhasePredecessor != null) {
                for (int i = 0; i < phases.length; i++) {
                    Phase phase = phases[i];
                    if (phase.getId() == postMortemPhasePredecessor.getId()) {
                        int phaseGroupIndex = ConfigHelper.findPhaseGroupForPhaseName(
                            postMortemPhasePredecessor.getPhaseType().getName());
                        for (int j = i + 1; j < phases.length; j++) {
                            Phase nextPhase = phases[j];
                            int phaseGroupIndexNext
                                = ConfigHelper.findPhaseGroupForPhaseName(nextPhase.getPhaseType().getName());
                            if (phaseGroupIndexNext != phaseGroupIndex) {
                                System.arraycopy(phases, j, phases, j + 1, phases.length - j - 1);
                                phases[j] = postMortemPhase;
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        List<PhaseGroup> phaseGroups = new ArrayList<PhaseGroup>();
        Map<String, Integer> similarPhaseGroupIndexes = new HashMap<String, Integer>();
        PhaseGroup phaseGroup = null;
        int[] phaseGroupIndexes = new int[phases.length];
        int phaseGroupIdx = -1;
        int activeTabIdx = -1;
        Resource[] submitters = null;

        Submission[] mostRecentContestSubmissions = ActionsHelper.getProjectSubmissions(
                project.getId(), Constants.CONTEST_SUBMISSION_TYPE_NAME, null, false);

        for (int phaseIdx = 0; phaseIdx < phases.length; ++phaseIdx) {
            // Get a phase for the current iteration
            Phase phase = phases[phaseIdx];
            String phaseName = phase.getPhaseType().getName();
            // Take next phase (if the current one is not the last)
            Phase nextPhase = (phaseIdx + 1 != phases.length) ? phases[phaseIdx + 1] : null;

            if (phaseGroup == null || !ConfigHelper.isPhaseGroupContainsPhase(phaseGroupIdx, phaseName) ||
                    phaseGroup.isPhaseInThisGroup(phase)) {
                // Get an index of this potential phase group in the configuration
                phaseGroupIdx = ConfigHelper.findPhaseGroupForPhaseName(phaseName);

                String appFuncName = ConfigHelper.getPhaseGroupAppFunction(phaseGroupIdx);

                if (!appFuncName.equals(Constants.VIEW_REGISTRANTS_APP_FUNC) ||
                        AuthorizationHelper.hasUserPermission(request, Constants.VIEW_REGISTRATIONS_PERM_NAME)) {
                    phaseGroup = new PhaseGroup();
                    phaseGroups.add(phaseGroup);

                    Integer groupIndexObj = similarPhaseGroupIndexes.get(appFuncName);
                    int groupIndex = (groupIndexObj != null) ? groupIndexObj : 0;

                    similarPhaseGroupIndexes.put(appFuncName, groupIndex + 1);

                    String groupIndexStr = (groupIndex != 0) ? ("&#160;" + (groupIndex + 1)) : "";

                    if (appFuncName.equals(Constants.VIEW_REVIEWS_APP_FUNC) && ActionsHelper.isStudioProject(project)) {
                        phaseGroup.setName(textProvider.getText("ProjectPhaseGroup.Review", new String[]{groupIndexStr}));
                    } else {
                        phaseGroup.setName(textProvider.getText(
                                ConfigHelper.getPhaseGroupNameKey(phaseGroupIdx), new String[]{groupIndexStr}));
                    }
                    phaseGroup.setTableName(textProvider.getText(
                            ConfigHelper.getPhaseGroupTableNameKey(phaseGroupIdx), new String[]{groupIndexStr}));
                    phaseGroup.setGroupIndex(groupIndexStr);
                    phaseGroup.setAppFunc(appFuncName);
                } else {
                    phaseGroup = null;
                }
            }

            if (phaseGroup == null) {
                phaseGroupIndexes[phaseIdx] = -1;
                continue;
            }

            phaseGroup.addPhase(phase);

            String phaseStatus = phase.getPhaseStatus().getName();

            if (phaseStatus.equalsIgnoreCase("Closed") || phaseStatus.equalsIgnoreCase("Open")) {
                if (phaseStatus.equalsIgnoreCase("Open") && phaseGroupIdx != -1) {
                    // If there are multiple open phases, only the last one's tab will be "active", i.e. open by default
                    activeTabIdx = phaseGroups.size() - 1;
                }
                phaseGroup.setPhaseOpen(true);
            }

            boolean isAfterAppealsResponse = ActionsHelper.isAfterAppealsResponse(phases, phaseIdx);
            boolean isStudioScreening = false;
            Phase[] activePhases = ActionsHelper.getActivePhases(phases);
            if (ActionsHelper.isStudioProject(project)) {
                for (Phase p : activePhases) {
                    if (p.getPhaseType().getName().equals(Constants.SCREENING_PHASE_NAME) || p.getPhaseType().getName().equals(Constants.CHECKPOINT_SCREENING_PHASE_NAME)) {
                        isStudioScreening = true;
                    }
                }
            }

            submitters = getSubmitters(request, allProjectResources, isAfterAppealsResponse, isStudioScreening, submitters);
            phaseGroup.setSubmitters(submitters);

            // Determine an index of the current phase group (needed for timeline phases list)
            for (int i = 0; i < originalPhases.length; i++) {
                Phase originalPhase = originalPhases[i];
                if (originalPhase.getId() == phase.getId()) {
                    phaseGroupIndexes[i] = phaseGroups.size() - 1;
                }
            }

            if (!phaseGroup.isPhaseOpen()) {
                continue;
            }

            if (phaseGroup.getAppFunc().equals(Constants.VIEW_REGISTRANTS_APP_FUNC)) {
                serviceRegistrantsAppFunc(request, phaseGroup, submitters, allProjectExternalUsers);
            } else if (phaseGroup.getAppFunc().equals(Constants.VIEW_SUBMISSIONS_APP_FUNC)) {
                serviceSubmissionsAppFunc(request, phaseGroup, project, phases, phaseIdx,
                        allProjectResources, submitters, mostRecentContestSubmissions, isAfterAppealsResponse);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.VIEW_REVIEWS_APP_FUNC)) {
                serviceReviewsAppFunc(request, phaseGroup, project, phase, nextPhase,
                        allProjectResources, submitters, mostRecentContestSubmissions, isAfterAppealsResponse);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.AGGREGATION_APP_FUNC)) {
                serviceAggregationAppFunc(request, phaseGroup, project, phase,
                        allProjectResources, mostRecentContestSubmissions, isAfterAppealsResponse);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.FINAL_FIX_APP_FUNC)) {
                serviceFinalFixAppFunc(request, phaseGroup, project, phase,
                        allProjectResources, mostRecentContestSubmissions, isAfterAppealsResponse);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.APPROVAL_APP_FUNC)) {
                serviceApprovalAppFunc(request, phaseGroup, project, phase,
                        allProjectResources, isAfterAppealsResponse, mostRecentContestSubmissions);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.POST_MORTEM_APP_FUNC)) {
                servicePostMortemAppFunc(phaseGroup, project, phase, allProjectResources);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.SPEC_REVIEW_APP_FUNC)) {
                serviceSpecReviewAppFunc(phaseGroup, project, phase, allProjectResources);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.CHECKPOINT_APP_FUNC)) {
                serviceCheckpointAppFunc(request, phaseGroup, project, phase, allProjectResources, phases, phaseIdx,
                                        submitters);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.ITERATIVEREVIEW_APP_FUNC)) {
                serviceIterativeReviewsAppFunc(request, phaseGroup, project, phase, allProjectResources, submitters,
                        mostRecentContestSubmissions);
            }
        }

        PhasesDetails details = new PhasesDetails();

        details.setPhaseGroup(phaseGroups.toArray(new PhaseGroup[phaseGroups.size()]));
        details.setPhaseGroupIndexes(phaseGroupIndexes);
        details.setActiveTabIndex(activeTabIdx);

        return details;
    }

    /**
     * <p>Processes the current phase from <code>Checkpoint</code> group of phases.</p>
     *
     * @param request an <code>HttpServletRequest</code> referencing the incoming request.
     * @param phaseGroup a <code>PhaseGroup</code> providing the collected data for groups of phases.
     * @param project a <code>Project</code> providing details for current project.
     * @param phase a <code>Phase</code> providing details for current phase.
     * @param allProjectResources a <code>Resource</code> listing all project resources.
     * @param phases a <code>Phase</code> array listing all project phases.
     * @param phaseIdx an <code>int</code> specifying the index of current phase.
     * @param submitters a <code>Resource</code> array listing the submitters for project.
     * @throws BaseException if an unexpected error occurs.
     */
    private static void serviceCheckpointAppFunc(HttpServletRequest request, PhaseGroup phaseGroup, Project project,
                                                Phase phase, Resource[] allProjectResources, Phase[] phases,
                                                int phaseIdx, Resource[] submitters) throws BaseException {
        Phase checkpointReviewPhase = ActionsHelper.getPhase(phases, false, Constants.CHECKPOINT_REVIEW_PHASE_NAME);
        if (checkpointReviewPhase != null) {
            phaseGroup.setCheckpointReviewFinished(checkpointReviewPhase.getPhaseStatus().getId() == 3);
        }

        Phase reviewPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.APPEALS_RESPONSE_PHASE_NAME);
        if (reviewPhase == null) {
            reviewPhase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);
        }
        boolean isReviewFinished = (reviewPhase != null) && (reviewPhase.getPhaseStatus().getId() == 3);

        String phaseName = phase.getPhaseType().getName();

        boolean mayViewMostRecentAfterReview
            = AuthorizationHelper.hasUserPermission(request,
                                                    Constants.VIEW_RECENT_CHECKPOINT_SUBMISSIONS_AFTER_REVIEW_PERM_NAME);

        // Checkpoint Submission phase
        if (phaseName.equalsIgnoreCase(Constants.CHECKPOINT_SUBMISSION_PHASE_NAME)) {
            Submission[] submissions = null;

            if (mayViewMostRecentAfterReview && isReviewFinished
                || AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_CHECKPOINT_SUBMISSIONS_PERM_NAME)
                || (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_CHECKPOINT_SUBMISSIONS_PERM_NAME)
                    && !AuthorizationHelper.hasUserRole(request, Constants.CHECKPOINT_REVIEWER_ROLE_NAME)
                    && !AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES))
                || (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_CHECKPOINT_SUBMISSIONS_PERM_NAME)
                    && AuthorizationHelper.hasUserRole(request, Constants.CHECKPOINT_REVIEWER_ROLE_NAME)
                    && ActionsHelper.isInOrAfterPhase(phases, phaseIdx, Constants.CHECKPOINT_REVIEW_PHASE_NAME))
                || (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_CHECKPOINT_SUBMISSIONS_PERM_NAME)
                    && AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES)
                    && ActionsHelper.isInOrAfterPhase(phases, phaseIdx, Constants.REVIEW_PHASE_NAME))
                || (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENER_CHECKPOINT_SUBMISSION_PERM_NAME)
                    && ActionsHelper.isInOrAfterPhase(phases, phaseIdx, Constants.CHECKPOINT_SCREENING_PHASE_NAME))) {
                submissions = ActionsHelper.getProjectSubmissions(project.getId(),
                    Constants.CHECKPOINT_SUBMISSION_TYPE_NAME, null, false);
            }

            if (submissions == null
                && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_CHECKPOINT_SUBMISSIONS_PERM_NAME)) {
                // Get "my" (submitter's) resource
                Resource myResource = null;
                Resource[] myResources = ActionsHelper.getMyResourcesForPhase(request, null);
                for (Resource resource : myResources) {
                    if (resource.getResourceRole().getName().equals("Submitter")) {
                        myResource = resource;
                        break;
                    }
                }
                if (myResource == null) {
                    throw new BaseException("Unable to find the Submitter resource " +
                            "associated with the current user for project " + project.getId());
                }

                submissions = ActionsHelper.getResourceSubmissions(myResource.getId(),
                        Constants.CHECKPOINT_SUBMISSION_TYPE_NAME, null, false);
            }

            if (submissions == null) {
                submissions = new Submission[0];
            }
            // Use comparator to sort submissions either by placement
            // or by the time when they were uploaded
            CheckpointSubmissionComparator comparator = new CheckpointSubmissionComparator();

            comparator.assignSubmitters(submitters);
            Arrays.sort(submissions, comparator);

            phaseGroup.setPastCheckpointSubmissions(
                getPreviousUploadsForSubmissions(request, project, submissions,
                                                 Constants.VIEW_ALL_CHECKPOINT_SUBMISSIONS_PERM_NAME));
            phaseGroup.setCheckpointSubmissions(submissions);
        }

        // Checkpoint Screening phase
        if (phaseName.equalsIgnoreCase(Constants.CHECKPOINT_SCREENING_PHASE_NAME)
            && phaseGroup.getCheckpointSubmissions() != null) {

            Resource[] screeners = ActionsHelper.getResourcesForPhase(allProjectResources, phases[phaseIdx]);
            if (screeners != null && screeners.length > 0) {
                phaseGroup.setCheckpointScreener(screeners[0]);
            }

            phaseGroup.setCheckpointScreeningPhaseStatus(phase.getPhaseStatus().getId());
            phaseGroup.setCheckpointScreeningReviews(ActionsHelper.searchReviews(phase.getId(), null, false));
        }

        // Checkpoint Review phase
        if (phaseName.equalsIgnoreCase(Constants.CHECKPOINT_REVIEW_PHASE_NAME)
            && phaseGroup.getCheckpointSubmissions() != null) {

            Resource[] reviewers = ActionsHelper.getResourcesForPhase(allProjectResources, phase);
            if (reviewers != null && reviewers.length > 0) {
                phaseGroup.setCheckpointReviewer(reviewers[0]);
            }

            // Obtain an instance of Review Manager
            phaseGroup.setCheckpointReviews(ActionsHelper.searchReviews(phase.getId(), null, false));
        }
    }

    /**
     * Service registrants app func.
     *
     * @param request the request
     * @param phaseGroup the phase group
     * @param submitters the submitters
     * @param allProjectExternalUsers the all project external users
     * @throws RetrievalException the retrieval exception
     * @throws ConfigException the config exception
     */
    private static void serviceRegistrantsAppFunc(HttpServletRequest request, PhaseGroup phaseGroup,
            Resource[] submitters, ExternalUser[] allProjectExternalUsers) throws RetrievalException, ConfigException {
        if (submitters == null || submitters.length == 0) {
            return;
        }

        // Get corresponding external users for the array of submitters
        ExternalUser[] extUsers = (allProjectExternalUsers != null) ? allProjectExternalUsers :
            ActionsHelper.getExternalUsersForResources(ActionsHelper.createUserRetrieval(request), submitters);
        String[] userEmails = new String[submitters.length];

        for (int j = 0; j < submitters.length; ++j) {
            // Get external ID for the current submitter's resource
            long extUserId = Long.parseLong((String) submitters[j].getProperty("External Reference ID"), 10);
            for (ExternalUser extUser : extUsers) {
                if (extUserId == extUser.getId()) {
                    userEmails[j] = extUser.getEmail();
                    break;
                }
            }
        }

        phaseGroup.setRegistrantsEmails(userEmails);
    }

    /**
     * <p>Gets the previous uploads for specified submissions.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @param project a <code>Project</code> providing details for project.
     * @param submissions a <code>Submission</code> array listing the current submissions to get previous uploads for.
     * @param viewAllSubmissionsPermission a <code>String</code> providing the permission for viewing all submissions of
     *        desired type.
     * @return an <code>Upload</code> array listing the uploads for previous submissions for specified submissions.
     * @throws BaseException if an unexpected error occurs.
     */
    private static Upload[][] getPreviousUploadsForSubmissions(HttpServletRequest request, Project project,
                                                               Submission[] submissions,
                                                               String viewAllSubmissionsPermission)
        throws BaseException {
        Upload[][] pastSubmissions = null;
        if (submissions.length > 0 &&
            AuthorizationHelper.hasUserPermission(request, viewAllSubmissionsPermission)) {

            // Find all deleted submissions for specified project
            Submission[] allDeletedSubmissions = ActionsHelper.getProjectSubmissions(
                    project.getId(), null, "Deleted", true);

            pastSubmissions = new Upload[submissions.length][];

            for (int j = 0; j < pastSubmissions.length; ++j) {
                List<Upload> temp = new ArrayList<Upload>();
                long currentUploadOwnerId = submissions[j].getUpload().getOwner();

                for (Submission deletedSubmission : allDeletedSubmissions) {
                    Upload deletedSubmissionUpload = deletedSubmission.getUpload();
                    if (deletedSubmission.getSubmissionType().getId() == submissions[j].getSubmissionType().getId() &&
                        deletedSubmissionUpload.getOwner() == currentUploadOwnerId) {
                        temp.add(deletedSubmissionUpload);
                    }
                }

                if (!temp.isEmpty()) {
                    pastSubmissions[j] = temp.toArray(new Upload[temp.size()]);
                }
            }
        }
        return pastSubmissions;
    }

    /**
     * Service submissions app func.
     *
     * @param request the request
     * @param phaseGroup the phase group
     * @param project the project
     * @param phases the phases
     * @param phaseIdx the phase idx
     * @param allProjectResources the all project resources
     * @param submitters the submitters
     * @param mostRecentContestSubmissions the most recent contest submissions
     * @param isAfterAppealsResponse the is after appeals response
     * @throws BaseException the base exception
     */
    private static void serviceSubmissionsAppFunc(HttpServletRequest request,
            PhaseGroup phaseGroup, Project project, Phase[] phases, int phaseIdx,
            Resource[] allProjectResources, Resource[] submitters, Submission[] mostRecentContestSubmissions,
            boolean isAfterAppealsResponse)
        throws BaseException {
        String phaseName = phases[phaseIdx].getPhaseType().getName();

        if (phaseName.equalsIgnoreCase(Constants.SUBMISSION_PHASE_NAME)) {

            Submission[] submissions = null;

            if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
                submissions = mostRecentContestSubmissions;
            }

            boolean mayViewMostRecentAfterAppealsResponse =
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_AAR_PERM_NAME);

            if (submissions == null && ((mayViewMostRecentAfterAppealsResponse && isAfterAppealsResponse))) {
                submissions = mostRecentContestSubmissions;
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME) &&
                    !AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES)) {
                submissions = mostRecentContestSubmissions;
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME) &&
                    AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES) &&
                    ActionsHelper.isInOrAfterPhase(phases, phaseIdx, Constants.REVIEW_PHASE_NAME)) {
                submissions = mostRecentContestSubmissions;
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENER_SUBM_PERM_NAME) &&
                    ActionsHelper.isInOrAfterPhase(phases, phaseIdx, Constants.SCREENING_PHASE_NAME)) {
                submissions = mostRecentContestSubmissions;
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {
                // Get "my" (submitter's) resource
                Resource myResource = null;
                Resource[] myResources = ActionsHelper.getMyResourcesForPhase(request, null);
                for (Resource resource : myResources) {
                    if (resource.getResourceRole().getName().equals("Submitter")) {
                        myResource = resource;
                        break;
                    }
                }

                if (myResource == null) {
                    throw new BaseException("Unable to find the Submitter resource " +
                            "associated with the current user for project " + project.getId());
                }

                submissions = ActionsHelper.getResourceSubmissions(myResource.getId(),
                        Constants.CONTEST_SUBMISSION_TYPE_NAME, null, false);
            }
            if (submissions == null
                    && AuthorizationHelper.hasUserPermission(request,
                            Constants.VIEW_CURRENT_ITERATIVE_REVIEW_SUBMISSION)) {
                submissions = mostRecentContestSubmissions;
                request.setAttribute("downloadCurrentIterativeReview", true);
            }

            if (submissions == null) {
                submissions = new Submission[0];
            }
            // Use comparator to sort submissions either by placement
            // or by the time when they were uploaded
            SubmissionComparer comparator = new SubmissionComparer();

            comparator.assignSubmitters(submitters);
            Arrays.sort(submissions, comparator);

            phaseGroup.setPastSubmissions(getPreviousUploadsForSubmissions(request, project, submissions,
                                                                           Constants.VIEW_ALL_SUBM_PERM_NAME));

            phaseGroup.setSubmissions(submissions);
        }

        if (phaseName.equalsIgnoreCase(Constants.SCREENING_PHASE_NAME) &&
                phaseGroup.getSubmissions() != null) {
            Submission[] submissions = phaseGroup.getSubmissions();

            if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENER_SUBM_PERM_NAME) &&
                    !AuthorizationHelper.hasUserRole(request, Constants.PRIMARY_SCREENER_ROLE_NAME)) {
                Resource[] myResources = ActionsHelper.getMyResourcesForPhase(request, phases[phaseIdx]);
                List<Submission> tempSubs = new ArrayList<Submission>();

                for (Submission submission : submissions) {
                    for (Resource resource : myResources) {
                        if (resource.containsSubmission(submission.getId())) {
                            tempSubs.add(submission);
                        }
                    }
                }
                submissions = tempSubs.toArray(new Submission[tempSubs.size()]);
                phaseGroup.setSubmissions(submissions);
            }

            Resource[] screeners = ActionsHelper.getResourcesForPhase(allProjectResources, phases[phaseIdx]);

            phaseGroup.setReviewers(screeners);
            phaseGroup.setScreeningPhaseStatus(phases[phaseIdx].getPhaseStatus().getId());

            // No need to fetch screening results if there are no submissions
            if (submissions.length == 0) {
                return;
            }

            phaseGroup.setScreenings(ActionsHelper.searchReviews(phases[phaseIdx].getId(), null, false));
        }
    }

    /**
     * Service reviews app func.
     *
     * @param request the request
     * @param phaseGroup the phase group
     * @param project the project
     * @param phase the phase
     * @param nextPhase the next phase
     * @param allProjectResources the all project resources
     * @param submitters the submitters
     * @param mostRecentContestSubmissions the most recent contest submissions
     * @param isAfterAppealsResponse the is after appeals response
     * @throws BaseException the base exception
     */
    private static void serviceReviewsAppFunc(HttpServletRequest request,
            PhaseGroup phaseGroup, Project project, Phase phase, Phase nextPhase,
            Resource[] allProjectResources, Resource[] submitters, Submission[] mostRecentContestSubmissions,
            boolean isAfterAppealsResponse)
        throws BaseException {
        String phaseName = phase.getPhaseType().getName();

        if (phaseName.equalsIgnoreCase(Constants.REVIEW_PHASE_NAME)) {
            // If the project is not in the after appeals response state, allow uploading of testcases
            phaseGroup.setUploadingTestcasesAllowed(!isAfterAppealsResponse);

            Submission[] submissions = null;

            if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
                submissions = mostRecentContestSubmissions;
            }

            boolean mayViewMostRecentAfterAppealsResponse =
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_AAR_PERM_NAME);

            if (submissions == null &&
                    ((mayViewMostRecentAfterAppealsResponse && isAfterAppealsResponse) ||
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME))) {
                submissions = mostRecentContestSubmissions;
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {
                // Get "my" (submitter's) resource
                Resource myResource = null;
                Resource[] myResources = ActionsHelper.getMyResourcesForPhase(request, null);
                for (Resource resource : myResources) {
                    if (resource.getResourceRole().getName().equals("Submitter")) {
                        myResource = resource;
                        break;
                    }
                }
                
                if (myResource == null) {
                    throw new BaseException("Unable to find the Submitter resource " +
                            "associated with the current user for project " + project.getId());
                }

                submissions = ActionsHelper.getResourceSubmissions(myResource.getId(),
                        Constants.CONTEST_SUBMISSION_TYPE_NAME, null, false);
            }
            // No submissions -- nothing to review, but the list of submissions must not be null in this case
            if (submissions == null) {
                submissions = new Submission[0];
            }

            // Use comparator to sort submissions either by placement or by the time when they were uploaded
            SubmissionComparer comparator = new SubmissionComparer();

            comparator.assignSubmitters(submitters);
            Arrays.sort(submissions, comparator);

            phaseGroup.setSubmissions(submissions);

            // Some resource roles can always see links to reviews (if there are any).
            // There is no corresponding permission, so the list of roles is hard-coded
            boolean allowedToSeeReviewLink =
                AuthorizationHelper.hasUserRole(request, new String[] {
                    Constants.MANAGER_ROLE_NAME, Constants.GLOBAL_MANAGER_ROLE_NAME,
                    Constants.COCKPIT_PROJECT_USER_ROLE_NAME,
                    Constants.REVIEWER_ROLE_NAME, Constants.ACCURACY_REVIEWER_ROLE_NAME,
                    Constants.FAILURE_REVIEWER_ROLE_NAME, Constants.STRESS_REVIEWER_ROLE_NAME,
                    Constants.CLIENT_MANAGER_ROLE_NAME, Constants.COPILOT_ROLE_NAME,
                    Constants.OBSERVER_ROLE_NAME});
            // Determine if the Review phase is closed
            boolean isReviewClosed =
                phase.getPhaseStatus().getName().equalsIgnoreCase(Constants.CLOSED_PH_STATUS_NAME);
            // Determine if the Appeals phase is open
            boolean isAppealsOpen = (nextPhase != null &&
                    nextPhase.getPhaseType().getName().equalsIgnoreCase(Constants.APPEALS_PHASE_NAME) &&
                    nextPhase.getPhaseStatus().getName().equalsIgnoreCase(Constants.OPEN_PH_STATUS_NAME));

            if (!allowedToSeeReviewLink) {
                // Determine if the user is allowed to place appeals and Appeals phase is open
                if (isReviewClosed || (isAppealsOpen &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_PERM_NAME))) {
                    allowedToSeeReviewLink = true;
                }
            }

            phaseGroup.setDisplayReviewLinks(allowedToSeeReviewLink);

            Resource[] reviewers = null;

            if (!isAfterAppealsResponse &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_REVIEWER_REVIEWS_PERM_NAME) &&
                    AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES)) {
                // Get "my" (reviewer's) resource
                reviewers = ActionsHelper.getMyResourcesForPhase(request, phase);
            }

            if (reviewers == null) {
                reviewers = ActionsHelper.getResourcesForPhase(allProjectResources, phase);
            }

            // Put collected reviewers into the phase group
            phaseGroup.setReviewers(reviewers);
            // A safety check: create an empty array in case reviewers is null
            if (reviewers == null) {
                reviewers = new Resource[0];
            }

            List<Long> submissionIds = new ArrayList<Long>();
            for (Submission submission : submissions) {
                submissionIds.add(submission.getId());
            }

            List<Long> reviewerIds = new ArrayList<Long>();
            for (Resource reviewer : reviewers) {
                reviewerIds.add(reviewer.getId());
            }

            Review[] ungroupedReviews = null;

            if (!(submissionIds.isEmpty() || reviewerIds.isEmpty())) {
                Filter filterSubmissions = new InFilter("submission", submissionIds);
                Filter filterReviewers = new InFilter("reviewer", reviewerIds);
                Filter filterScorecard = new EqualToFilter("scorecardType", LookupHelper.getScorecardType("Review").getId());

                List<Filter> reviewFilters = new ArrayList<Filter>();
                reviewFilters.add(filterReviewers);
                reviewFilters.add(filterScorecard);
                reviewFilters.add(filterSubmissions);

                // Create final filter
                Filter filterForReviews = new AndFilter(reviewFilters);

                // Obtain an instance of Review Manager
                ReviewManager revMgr = ActionsHelper.createReviewManager();
                // Get the reviews from every individual reviewer
                ungroupedReviews = revMgr.searchReviews(filterForReviews, false);
            }
            if (ungroupedReviews == null) {
                ungroupedReviews = new Review[0];
            }

            boolean canDownloadTestCases =
                (isReviewClosed &&
                        AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME)) ||
                (!isReviewClosed &&
                        AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_TC_DUR_REVIEW_PERM_NAME)) ||
                (!isReviewClosed && isAppealsOpen &&
                        AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_PERM_NAME) &&
                        AuthorizationHelper.hasUserPermission(request, Constants.DOWNLOAD_TEST_CASES_PERM_NAME));

            if (!reviewerIds.isEmpty() && canDownloadTestCases) {
                Filter filterResource = new InFilter("resource_id", reviewerIds);
                Filter filterStatus = UploadFilterBuilder.createUploadStatusIdFilter(
                        LookupHelper.getUploadStatus("Active").getId());
                Filter filterType = UploadFilterBuilder.createUploadTypeIdFilter(
                        LookupHelper.getUploadType("Test Case").getId());

                Filter filterForUploads = new AndFilter(Arrays.asList(filterResource, filterStatus, filterType));
                Upload[] testCases = ActionsHelper.createUploadManager().searchUploads(filterForUploads);

                phaseGroup.setTestCases(testCases);
            }

            Review[][] reviews = new Review[submissions.length][];
            Date[] reviewDates = new Date[submissions.length];

            for (int j = 0; j < submissions.length; ++j) {
                Date latestDate = null;
                Review[] innerReviews = new Review[reviewers.length];
                Arrays.fill(innerReviews, null);

                for (int k = 0; k < reviewers.length; ++k) {
                    for (Review ungrouped : ungroupedReviews) {
                        if (ungrouped.getAuthor() == reviewers[k].getId() &&
                                ungrouped.getSubmission() == submissions[j].getId()) {
                            innerReviews[k] = ungrouped;
                            if (!ungrouped.isCommitted()) {
                                continue;
                            }
                            if (latestDate == null || latestDate.before(ungrouped.getModificationTimestamp())) {
                                latestDate = ungrouped.getModificationTimestamp();
                            }
                        }
                    }
                }

                reviews[j] = innerReviews;
                reviewDates[j] = latestDate;
            }
            phaseGroup.setReviews(reviews);
            phaseGroup.setReviewDates(reviewDates);
        }

        if (phaseName.equalsIgnoreCase(Constants.APPEALS_PHASE_NAME) && phaseGroup.getReviews() != null) {
            // set the Appeals phase status to indicate
            // if the appeals information should be available
            if (phase.getPhaseStatus().getName().equalsIgnoreCase("Scheduled")) {
                return;
            }

            phaseGroup.setAppealsPhaseOpened(true);

            Review[][] reviews = phaseGroup.getReviews();
            int[][] totalAppeals = new int[reviews.length][];
            int[][] unresolvedAppeals = new int[reviews.length][];

            countAppeals(reviews, totalAppeals, unresolvedAppeals);

            phaseGroup.setTotalAppealsCounts(totalAppeals);
            phaseGroup.setUnresolvedAppealsCounts(unresolvedAppeals);
        }
    }

    /**
     * Service aggregation app func.
     *
     * @param request the request
     * @param phaseGroup the phase group
     * @param project the project
     * @param phase the phase
     * @param allProjectResources the all project resources
     * @param mostRecentContestSubmissions the most recent contest submissions
     * @param isAfterAppealsResponse the is after appeals response
     * @throws BaseException the base exception
     */
    private static void serviceAggregationAppFunc(HttpServletRequest request, PhaseGroup phaseGroup,
        Project project, Phase phase, Resource[] allProjectResources, Submission[] mostRecentContestSubmissions,
        boolean isAfterAppealsResponse)
        throws BaseException {
        retrieveSubmissions(request, phaseGroup, project, mostRecentContestSubmissions, isAfterAppealsResponse);
        String phaseName = phase.getPhaseType().getName();

        if (phaseName.equalsIgnoreCase(Constants.AGGREGATION_PHASE_NAME) &&
                phaseGroup.getSubmitters() != null) {
            Resource winner = ActionsHelper.getWinner(phase.getProject().getId());
            phaseGroup.setWinner(winner);

            Resource[] aggregator = ActionsHelper.getResourcesForPhase(allProjectResources, phase);

            if (aggregator == null || aggregator.length == 0) {
                return;
            }

            Review[] reviews = ActionsHelper.searchReviews(phase.getId(), aggregator[0].getId(), false);
            if (reviews.length != 0) {
                phaseGroup.setAggregation(reviews[0]);
            }
        }
    }

    /**
     * Service final fix app func.
     *
     * @param request the request
     * @param phaseGroup the phase group
     * @param project the project
     * @param phase the phase
     * @param allProjectResources the all project resources
     * @param mostRecentContestSubmissions the most recent contest submissions
     * @param isAfterAppealsResponse the is after appeals response
     * @throws BaseException the base exception
     */
    private static void serviceFinalFixAppFunc(HttpServletRequest request, PhaseGroup phaseGroup, Project project,
        Phase phase, Resource[] allProjectResources, Submission[] mostRecentContestSubmissions,
        boolean isAfterAppealsResponse) throws BaseException {
        retrieveSubmissions(request, phaseGroup, project, mostRecentContestSubmissions, isAfterAppealsResponse);
        String phaseName = phase.getPhaseType().getName();

        if (phaseGroup.getSubmitters() != null) {
            Resource winner = phaseGroup.getWinner();
            if (winner == null) {
                winner = ActionsHelper.getWinner(phase.getProject().getId());
                phaseGroup.setWinner(winner);
            }
            if (winner == null) {
                return;
            }
        }

        if (phaseName.equalsIgnoreCase(Constants.FINAL_FIX_PHASE_NAME)) {
            Upload[] uploads = ActionsHelper.getPhaseUploads(phase.getId(), "Final Fix");
            
            if (uploads.length > 0) { 
                phaseGroup.setFinalFix(uploads[0]);
            }
        }

        if (phaseName.equalsIgnoreCase(Constants.FINAL_REVIEW_PHASE_NAME) && phaseGroup.getSubmitters() != null) {
            Resource[] reviewers = ActionsHelper.getResourcesForPhase(allProjectResources, phase);

            if (reviewers == null || reviewers.length == 0) {
                return;
            }

            Review[] reviews = ActionsHelper.searchReviews(phase.getId(), reviewers[0].getId(), false);
            if (reviews.length != 0) {
                phaseGroup.setFinalReview(reviews[0]);
            }
        }
    }

    /**
     * <p>Sets the specified phase group with details for <code>Specification Submission/Review</code> phases.</p>
     *
     * @param phaseGroup a <code>PhaseGroup</code> providing the details for group of phase the current phase belongs
     *        to.
     * @param project a <code>Project</code> providing the details for current project.
     * @param phase a <code>Phase</code> providing the details for <code>Post-Mortem</code> phase.
     * @param allProjectResources a <code>Resource</code> array listing all existing resources for specified project.
     * @throws BaseException if an unexpected error occurs.
     */
    private static void serviceSpecReviewAppFunc(PhaseGroup phaseGroup, Project project,
                                                 Phase phase, Resource[] allProjectResources) throws BaseException {
        String phaseName = phase.getPhaseType().getName();

        if (phaseName.equalsIgnoreCase(Constants.SPECIFICATION_SUBMISSION_PHASE_NAME)) {
            SubmissionType specSubmissionType = LookupHelper.getSubmissionType(Constants.SPECIFICATION_SUBMISSION_TYPE_NAME);

            Filter submissionTypeFilter = SubmissionFilterBuilder.createSubmissionTypeIdFilter(specSubmissionType.getId());
            Filter phaseFilter = SubmissionFilterBuilder.createProjectPhaseIdFilter(phase.getId());

            UploadManager upMgr = ActionsHelper.createUploadManager();
            Submission[] submissions = upMgr.searchSubmissions(new AndFilter(phaseFilter, submissionTypeFilter));
            if (submissions.length > 0) {
                phaseGroup.setSpecificationSubmission(submissions[0]);
                ResourceManager resourceManager = ActionsHelper.createResourceManager();
                phaseGroup.setSpecificationSubmitter(resourceManager.getResource(submissions[0].getUpload().getOwner()));
            }
        }

        if (phaseName.equalsIgnoreCase(Constants.SPECIFICATION_REVIEW_PHASE_NAME)) {
            Resource[] reviewers = ActionsHelper.getResourcesForPhase(allProjectResources, phase);
            if ((reviewers == null) || (reviewers.length == 0)) {
                return;
            }
            Review[] reviews = ActionsHelper.searchReviews(phase.getId(), reviewers[0].getId(), false);
            if (reviews.length != 0) {
                phaseGroup.setSpecificationReview(reviews[0]);
            }
        }
    }

    /**
     * <p>Sets the specified phase group with details for <code>Approval</code> phase.</p>
     *
     * @param request an <code>HttpServletRequest</code> providing incoming request from the client.
     * @param phaseGroup a <code>PhaseGroup</code> providing the details for group of phase the current phase belongs
     *        to.
     * @param project a <code>Project</code> providing the details for current project.
     * @param thisPhase a <code>Phase</code> providing the details for <code>Post-Mortem</code> phase.
     * @param allProjectResources a <code>Resource</code> array listing all existing resources for specified project.
     * @param isAfterAppealsResponse <code>true</code> if current phase is after appeals response; <code>false</code>
     *        otherwise.
     * @throws BaseException if an unexpected error occurs.
     */
    private static void serviceApprovalAppFunc(HttpServletRequest request, PhaseGroup phaseGroup,
        Project project, Phase thisPhase, Resource[] allProjectResources, boolean isAfterAppealsResponse,
        Submission[] mostRecentContestSubmissions) throws BaseException {
        if (phaseGroup.getSubmitters() == null) {
            return;
        }
        retrieveSubmissions(request, phaseGroup, project, mostRecentContestSubmissions, isAfterAppealsResponse);
        Resource winner = ActionsHelper.getWinner(project.getId());
        phaseGroup.setWinner(winner);

        Resource[] approvers = ActionsHelper.getResourcesForPhase(allProjectResources, thisPhase);
        if (approvers == null || approvers.length == 0) {
            return;
        }
        phaseGroup.setApprovalReviewers(approvers);

        phaseGroup.setApproval(ActionsHelper.searchReviews(thisPhase.getId(), null, false));
        phaseGroup.setApprovalPhaseStatus(thisPhase.getPhaseStatus().getId());
        phaseGroup.setFinalFix(ActionsHelper.getFinalFixForApprovalPhase(thisPhase));
    }

    /**
     * <p>Sets the specified phase group with details for <code>Post-Mortem</code> phase.</p>
     *
     * @param phaseGroup a <code>PhaseGroup</code> providing the details for group of phase the current phase belongs
     *        to.
     * @param project a <code>Project</code> providing the details for current project.
     * @param thisPhase a <code>Phase</code> providing the details for <code>Post-Mortem</code> phase.
     * @param allProjectResources a <code>Resource</code> array listing all existing resources for specified project.
     * @throws BaseException if an unexpected error occurs.
     */
    private static void servicePostMortemAppFunc(PhaseGroup phaseGroup,
                                                 Project project, Phase thisPhase, Resource[] allProjectResources
    ) throws BaseException {
        // Get the list of Post-Mortem reviewers assigned to project
        Resource[] postMortemReviewers = ActionsHelper.getResourcesForPhase(allProjectResources, thisPhase);
        if (postMortemReviewers == null || postMortemReviewers.length == 0) {
            return;
        }
        phaseGroup.setPostMortemReviewers(postMortemReviewers);

        phaseGroup.setPostMortemReviews(ActionsHelper.searchReviews(thisPhase.getId(), null, false));
        phaseGroup.setPostMortemPhaseStatus(thisPhase.getPhaseStatus().getId());
    }

    /**
     * <p>Sets the specified phase group with details for <code>Iterative Review</code> phase.</p>
     *
     * @param request an <code>HttpServletRequest</code> providing incoming request from the client.
     * @param phaseGroup a <code>PhaseGroup</code> providing the details for group of phase the current phase belongs
     *        to.
     * @param project a <code>Project</code> providing the details for current project.
     * @param phase a <code>Phase</code> providing the details for <code>Iterative Review</code> phase.
     * @param allProjectResources a <code>Resource</code> array listing all existing resources for specified project.
     * @param submitters a <code>Resource</code> array listing all submitters for specified project.
     * @param mostRecentContestSubmissions a <code>Submission</code> array listing all active contest submissions.
     * @throws BaseException if an unexpected error occurs.
     */
    private static void serviceIterativeReviewsAppFunc(HttpServletRequest request, PhaseGroup phaseGroup,
            Project project, Phase phase, Resource[] allProjectResources, Resource[] submitters,
            Submission[] mostRecentContestSubmissions) throws BaseException {
        String phaseName = phase.getPhaseType().getName();

        Submission associatedSubmission = null;

        Review[] ungroupedReviews = null;
        Filter filterPhase = new EqualToFilter("projectPhase", phase.getId());
        Filter filterScorecard = new EqualToFilter("scorecardType", LookupHelper.getScorecardType("Iterative Review")
                .getId());
        List<Filter> reviewFilters = new ArrayList<Filter>();
        reviewFilters.add(filterScorecard);
        reviewFilters.add(filterPhase);
        // Create final filter
        Filter filterForReviews = new AndFilter(reviewFilters);
        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager();
        // Get the reviews from every individual reviewer
        ungroupedReviews = revMgr.searchReviews(filterForReviews, false);

        if (mostRecentContestSubmissions != null) {
            if (ungroupedReviews != null && ungroupedReviews.length > 0) {
                long submissionId = ungroupedReviews[0].getSubmission();
                for (Submission submission : mostRecentContestSubmissions) {
                    if (submissionId == submission.getId()) {
                        associatedSubmission = submission;
                    }
                }
            } else {
                Date earliestDate = null;
                for (Submission submission : mostRecentContestSubmissions) {
                    if (submission.getSubmissionStatus().getName().equalsIgnoreCase("Active")
                            && (earliestDate == null || earliestDate.compareTo(submission.getCreationTimestamp()) > 0)) {
                        earliestDate = submission.getCreationTimestamp();
                        associatedSubmission = submission;
                    }
                }
            }
        }

        if (phaseName.equalsIgnoreCase(Constants.ITERATIVE_REVIEW_PHASE_NAME)) {
            if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
                phaseGroup.setIterativeReviewSubmission(associatedSubmission);
            }

            if (AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_ITERATIVE_REVIEW_PERM_NAME)) {
                phaseGroup.setIterativeReviewSubmission(associatedSubmission);
            }

            if (phaseGroup.getIterativeReviewSubmission() == null
                    && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {
                // Get "my" (submitter's) resource
                Resource myResource = null;
                Resource[] myResources = ActionsHelper.getMyResourcesForPhase(request, null);
                for (Resource resource : myResources) {
                    if (resource.getResourceRole().getName().equals("Submitter")) {
                        myResource = resource;
                        break;
                    }
                }
                
                if (myResource == null) {
                    throw new BaseException("Unable to find the Submitter resource " +
                            "associated with the current user for project " + project.getId());
                }

                Submission[] mySubmissions = ActionsHelper.getResourceSubmissions(myResource.getId(),
                        Constants.CONTEST_SUBMISSION_TYPE_NAME, null, false);
                if (mySubmissions != null) {
                    for (Submission submission : mySubmissions) {
                        if (submission.getId() == associatedSubmission.getId()) {
                            phaseGroup.setIterativeReviewSubmission(associatedSubmission);
                        }
                    }
                }
            }

            for (Resource resource : allProjectResources) {
                if (resource.getId() == associatedSubmission.getUpload().getOwner()) {
                    phaseGroup.setIterativeReviewSubmitter(resource);
                    break;
                }
            }

            // Some resource roles can always see links to reviews (if there are any).
            // There is no corresponding permission, so the list of roles is hard-coded
            boolean allowedToSeeReviewLink = AuthorizationHelper.hasUserRole(request, new String[] {
                    Constants.MANAGER_ROLE_NAME, Constants.GLOBAL_MANAGER_ROLE_NAME,
                    Constants.COCKPIT_PROJECT_USER_ROLE_NAME, Constants.ITERATIVE_REVIEWER_ROLE_NAME,
                    Constants.CLIENT_MANAGER_ROLE_NAME, Constants.COPILOT_ROLE_NAME, Constants.OBSERVER_ROLE_NAME });
            // Determine if the Iterative Review phase is closed
            boolean isReviewClosed = phase.getPhaseStatus().getName().equalsIgnoreCase(Constants.CLOSED_PH_STATUS_NAME);

            if (isReviewClosed) {
                allowedToSeeReviewLink = true;
            }

            phaseGroup.setDisplayReviewLinks(allowedToSeeReviewLink);

            Resource[] reviewers = null;

            if (phase.getPhaseStatus().getName().equalsIgnoreCase("Open")
                    && AuthorizationHelper.hasUserPermission(request,
                            Constants.VIEW_ITERATIVE_REVIEWER_REVIEWS_PERM_NAME)
                    && AuthorizationHelper.hasUserRole(request, Constants.ITERATIVE_REVIEWER_ROLE_NAME)) {
                // Get "my" (reviewer's) resource
                reviewers = new Resource[1];
                reviewers[0] = ActionsHelper.getMyResourceForRole(request, Constants.ITERATIVE_REVIEWER_ROLE_NAME);
            }

            if (reviewers == null) {
                List<Resource> resources = new ArrayList<Resource>();
                for (Resource resource : allProjectResources) {
                    if (resource.getResourceRole().getName().equals(Constants.ITERATIVE_REVIEWER_ROLE_NAME)) {
                        resources.add(resource);
                    }
                }
                reviewers = resources.toArray(new Resource[resources.size()]);
            }

            // Put collected reviewers into the phase group
            phaseGroup.setIterativeReviewers(reviewers);
            // A safety check: create an empty array in case reviewers is null
            if (reviewers == null) {
                phaseGroup.setIterativeReviewers(new Resource[0]);
            }

            List<Long> reviewerIds = new ArrayList<Long>();
            for (Resource reviewer : reviewers) {
                reviewerIds.add(reviewer.getId());
            }

            if (ungroupedReviews == null) {
                ungroupedReviews = new Review[0];
            }

            Review[] reviews = new Review[reviewerIds.size()];
            Date[] reviewDates = new Date[1];

            Date latestDate = null;
            for (int k = 0; k < reviewerIds.size(); ++k) {
                for (Review ungrouped : ungroupedReviews) {
                    if (ungrouped.getAuthor() == reviewers[k].getId()) {
                        reviews[k] = ungrouped;
                        if (!ungrouped.isCommitted()) {
                            continue;
                        }
                        if (latestDate == null || latestDate.before(ungrouped.getModificationTimestamp())) {
                            latestDate = ungrouped.getModificationTimestamp();
                        }
                    }
                }
            }

            reviewDates[0] = latestDate;
            phaseGroup.setIterativeReviewReviews(reviews);
            phaseGroup.setReviewDates(reviewDates);
            phaseGroup.setIterativeReviewPhase(phase);
        }
    }

    /**
     * Retrieve submissions.
     *
     * @param request the request
     * @param phaseGroup the phase group
     * @param project the project
     * @param mostRecentContestSubmissions the most recent contest submissions
     * @param isAfterAppealsResponse the is after appeals response
     * @throws BaseException the base exception
     */
    private static void retrieveSubmissions(HttpServletRequest request, PhaseGroup phaseGroup,
        Project project, Submission[] mostRecentContestSubmissions, boolean isAfterAppealsResponse) throws BaseException {
        if (phaseGroup.getSubmitters() == null || phaseGroup.getSubmissions() != null) {
            return;
        }

        Submission[] submissions = null;

        boolean mayViewMostRecentAfterAppealsResponse =
            AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_AAR_PERM_NAME);

        if ((mayViewMostRecentAfterAppealsResponse && isAfterAppealsResponse) ||
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME) ||
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME) ||
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_WINNING_SUBM_PERM_NAME)) {
            submissions = mostRecentContestSubmissions;
        }
        if (submissions == null &&
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {

            // Get "my" (submitter's) resource
            Resource myResource = null;
            Resource[] myResources = ActionsHelper.getMyResourcesForPhase(request, null);
            for (Resource resource : myResources) {
                if (resource.getResourceRole().getName().equals("Submitter")) {
                    myResource = resource;
                    break;
                }
            }

            if (myResource == null) {
                throw new BaseException("Unable to find the Submitter resource " +
                        "associated with the current user for project " + project.getId());
            }

            submissions = ActionsHelper.getResourceSubmissions(myResource.getId(),
                Constants.CONTEST_SUBMISSION_TYPE_NAME, Constants.ACTIVE_SUBMISSION_STATUS_NAME, false);
        }
        if (submissions != null) {
            phaseGroup.setSubmissions(submissions);
        }
    }

    /**
     * Gets the submitters.
     *
     * @param request the request
     * @param allProjectResources the all project resources
     * @param isAfterAppealsResponse the is after appeals response
     * @param isStudioScreening the is studio screening
     * @param prevSubmitters the prev submitters
     * @return the submitters
     */
    private static Resource[] getSubmitters(HttpServletRequest request,
            Resource[] allProjectResources, boolean isAfterAppealsResponse, boolean isStudioScreening, Resource[] prevSubmitters) {
        final boolean canSeeSubmitters = (isAfterAppealsResponse 
                || AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME) 
                || (isStudioScreening && (AuthorizationHelper.hasUserRole(request, Constants.SCREENER_ROLE_NAME) || 
                AuthorizationHelper.hasUserRole(request, Constants.CHECKPOINT_SCREENER_ROLE_NAME) )));

        if (!canSeeSubmitters) {
            return null;
        }

        return (prevSubmitters != null) ? prevSubmitters : ActionsHelper.getAllSubmitters(allProjectResources);
    }

    /**
     * This static method counts the number of total and unresolved appeals for every review in the
     * provided array of reviews. The other two arrays (specified by parameters
     * <code>totalAppeals</code> and <code>unresolvedAppeals</code>) must be of the same length
     * as the array specified by <code>reviews<code> parameter.
     *
     * @param reviews
     *            a two-dimensional array of reviews to count appeals counts in.
     * @param totalAppeals
     *            specifies an array that on output will receive the amount of total appeals per
     *            every review in <code>reviews</code> input array.
     * @param unresolvedAppeals
     *            specifies an array that on output will receive the amount of unresolved appeals
     *            per every review in <code>reviews</code> input array.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if the number of items in
     *             <code>totalAppeals</code> or <code>unresolvedAppeals</code> arrays does not match
     *             the number of items in the <code>reviews</code> array.
     */
    private static void countAppeals(Review[][] reviews, int[][] totalAppeals, int[][] unresolvedAppeals) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(reviews, "reviews");
        ActionsHelper.validateParameterNotNull(totalAppeals, "totalAppeals");
        ActionsHelper.validateParameterNotNull(unresolvedAppeals, "unresolvedAppeals");

        // Validate array lengths
        if (reviews.length != totalAppeals.length) {
            throw new IllegalArgumentException("The number of items in 'reviews' array (" + reviews.length + ")" +
                    " does not match the number of items in 'totalAppeals' array (" + totalAppeals.length + ").");
        }
        if (reviews.length != unresolvedAppeals.length) {
            throw new IllegalArgumentException("The number of items in 'reviews' array (" + reviews.length + ")" +
                    " does not match the number of items in 'unresolvedAppeals' array (" + unresolvedAppeals.length +
                    ").");
        }

        for (int i = 0; i < reviews.length; ++i) {
            Review[] innerReviews = reviews[i];
            int[] innerTotalAppeals = new int[innerReviews.length];
            int[] innerUnresolvedAppeals = new int[innerReviews.length];

            for (int j = 0; j < innerReviews.length; ++j) {
                Review review = innerReviews[j];

                if (review == null) {
                    continue;
                }

                for (int itemIdx = 0; itemIdx < review.getNumberOfItems(); ++itemIdx) {
                    Item item = review.getItem(itemIdx);
                    boolean appealFound = false;
                    boolean appealResolved = false;

                    for (int commentIdx = 0;
                            commentIdx < item.getNumberOfComments() && !(appealFound && appealResolved);
                            ++commentIdx) {
                        String commentType = item.getComment(commentIdx).getCommentType().getName();

                        if (!appealFound && commentType.equalsIgnoreCase("Appeal")) {
                            appealFound = true;
                            continue;
                        }
                        if (!appealResolved && commentType.equalsIgnoreCase("Appeal Response")) {
                            appealResolved = true;
                        }
                    }

                    if (appealFound) {
                        ++innerTotalAppeals[j];
                        if (!appealResolved) {
                            ++innerUnresolvedAppeals[j];
                        }
                    }
                }
            }

            totalAppeals[i] = innerTotalAppeals;
            unresolvedAppeals[i] = innerUnresolvedAppeals;
        }
    }
}
