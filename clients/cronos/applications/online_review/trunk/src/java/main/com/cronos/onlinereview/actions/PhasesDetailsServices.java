/*
 * Copyright (C) 2006-2007 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import com.cronos.onlinereview.actions.Comparators.SubmissionComparer;
import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.autoscreening.management.ScreeningTask;
import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.util.errorhandling.BaseException;

/**
 * TODO: Add docs here.
 *
 * @author George1
 * @version 1.0
 */
final class PhasesDetailsServices {

    /**
     * This constructor is declared private to prevent class instantiation.
     */
    private PhasesDetailsServices() {
        // nothing
    }

    public static PhasesDetails getPhasesDetails(HttpServletRequest request, MessageResources messages, Project project,
            Phase[] phases, Resource[] allProjectResources, ExternalUser[] allProjectExternalUsers)
        throws BaseException {
        // Validate parameters first
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(messages, "messages");
        ActionsHelper.validateParameterNotNull(phases, "phases");

        List phaseGroups = new ArrayList();
        Map similarPhaseGroupIndexes = new HashMap();
        PhaseGroup phaseGroup = null;
        int[] phaseGroupIndexes = new int[phases.length];
        int phaseGroupIdx = -1;
        int activeTabIdx = -1;
        Resource[] submitters = null;
        FinalFixesInfo finalFixes = new FinalFixesInfo();

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

                    Integer groupIndexObj = (Integer) similarPhaseGroupIndexes.get(appFuncName);
                    int groupIndex = (groupIndexObj != null) ? groupIndexObj.intValue() : 0;

                    similarPhaseGroupIndexes.put(appFuncName, new Integer(groupIndex + 1));

                    String groupIndexStr = (groupIndex != 0) ? ("&#160;" + (groupIndex + 1)) : "";

                    phaseGroup.setName(messages.getMessage(
                            ConfigHelper.getPhaseGroupNameKey(phaseGroupIdx), groupIndexStr));
                    phaseGroup.setTableName(messages.getMessage(
                            ConfigHelper.getPhaseGroupTableNameKey(phaseGroupIdx), groupIndexStr));
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
                if (activeTabIdx == -1 && phaseStatus.equalsIgnoreCase("Open") && phaseGroupIdx != -1) {
                    activeTabIdx = phaseGroups.size() - 1;
                }
                phaseGroup.setPhaseOpen(true);
            }

            boolean isAfterAppealsResponse = ActionsHelper.isAfterAppealsResponse(phases, phaseIdx);

            submitters = getSubmitters(request, allProjectResources, isAfterAppealsResponse, submitters);
            phaseGroup.setSubmitters(submitters);

            // Determine an index of the current phase group (needed for timeline phases list)
            phaseGroupIndexes[phaseIdx] = phaseGroups.size() - 1;

            if (!phaseGroup.isPhaseOpen()) {
                continue;
            }

            if (phaseGroup.getAppFunc().equals(Constants.VIEW_REGISTRANTS_APP_FUNC)) {
                serviceRegistrantsAppFunc(request, phaseGroup, submitters, allProjectExternalUsers);
            } else if (phaseGroup.getAppFunc().equals(Constants.VIEW_SUBMISSIONS_APP_FUNC)) {
                serviceSubmissionsAppFunc(request, phaseGroup, project, phases, phaseIdx,
                        allProjectResources, submitters, isAfterAppealsResponse);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.VIEW_REVIEWS_APP_FUNC)) {
                serviceReviewsAppFunc(request, phaseGroup, project, phase, nextPhase,
                        allProjectResources, submitters, isAfterAppealsResponse);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.AGGREGATION_APP_FUNC)) {
                serviceAggregationAppFunc(request, phaseGroup, project, phase,
                        allProjectResources, isAfterAppealsResponse);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.FINAL_FIX_APP_FUNC)) {
                serviceFinalFixAppFunc(request, phaseGroup, project, phase,
                        allProjectResources, finalFixes, isAfterAppealsResponse);
            } else if (phaseGroup.getAppFunc().equalsIgnoreCase(Constants.APPROVAL_APP_FUNC)) {
                serviceApprovalAppFunc(request, phaseGroup, project, phase,
                        allProjectResources, isAfterAppealsResponse);
            }
        }

        PhasesDetails details = new PhasesDetails();

        details.setPhaseGroup((PhaseGroup[]) phaseGroups.toArray(new PhaseGroup[phaseGroups.size()]));
        details.setPhaseGroupIndexes(phaseGroupIndexes);
        details.setActiveTabIndex(activeTabIdx);

        return details;
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @param request
     * @param phaseGroup
     * @param submitters
     * @param allProjectExternalUsers
     * @throws RetrievalException
     * @throws ConfigException
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
            for (int k = 0; k < extUsers.length; ++k) {
                if (extUserId == extUsers[k].getId()) {
                    userEmails[j] = extUsers[k].getEmail();
                    break;
                }
            }
        }

        phaseGroup.setRegistantsEmails(userEmails);
    }

    private static void getPreviousUploadsForSubmissions(HttpServletRequest request, Project project, PhaseGroup phaseGroup, Submission[] submissions) throws BaseException {
	if (submissions.length > 0 && AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        // Get all upload types
        UploadType[] allUploadTypes = upMgr.getAllUploadTypes();
        // Get all upload statuses
        UploadStatus[] allUploadStatuses = upMgr.getAllUploadStatuses();

        Filter filterProject = UploadFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterUploadType = UploadFilterBuilder.createUploadTypeIdFilter(
                ActionsHelper.findUploadTypeByName(allUploadTypes, "Submission").getId());
        Filter filterUploadStatus = UploadFilterBuilder.createUploadStatusIdFilter(
                ActionsHelper.findUploadStatusByName(allUploadStatuses, "Deleted").getId());

        Filter filter =
            new AndFilter(Arrays.asList(new Filter[] {filterProject, filterUploadType, filterUploadStatus}));
        Upload[] ungroupedUploads = upMgr.searchUploads(filter);
        Upload[][] pastSubmissions = new Upload[submissions.length][];

        for (int j = 0; j < pastSubmissions.length; ++j) {
            List temp = new ArrayList();
            long currentUploadOwnerId = submissions[j].getUpload().getOwner();

            for (int k = 0; k < ungroupedUploads.length; k++) {
                if (currentUploadOwnerId == ungroupedUploads[k].getOwner()) {
                    temp.add(ungroupedUploads[k]);
                }
            }

            if (!temp.isEmpty()) {
                pastSubmissions[j] = (Upload[]) temp.toArray(new Upload[temp.size()]);
            }
        }

        if (pastSubmissions.length != 0) {
            phaseGroup.setPastSubmissions(pastSubmissions);
        }
    }
	}
    
    /**
     * TODO: Write documentation for this method.
     *
     * @param request
     * @param phaseGroup
     * @param project
     * @param phases
     * @param phaseIdx
     * @param allProjectResources
     * @param submitters
     * @param isAfterAppealsResponse
     * @throws BaseException
     */
    private static void serviceSubmissionsAppFunc(HttpServletRequest request,
            PhaseGroup phaseGroup, Project project, Phase[] phases, int phaseIdx,
            Resource[] allProjectResources, Resource[] submitters, boolean isAfterAppealsResponse)
        throws BaseException {
        String phaseName = phases[phaseIdx].getPhaseType().getName();

        if (phaseName.equalsIgnoreCase(Constants.SUBMISSION_PHASE_NAME)) {
            Submission[] submissions = null;
            
            if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
            	submissions = ActionsHelper.getMostRecentSubmissions(ActionsHelper.createUploadManager(request), project);
            }

            boolean mayViewMostRecentAfterAppealsResponse =
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_AAR_PERM_NAME);

            if (submissions == null &&
                    ((mayViewMostRecentAfterAppealsResponse && isAfterAppealsResponse))) {
                submissions =
                    ActionsHelper.getMostRecentSubmissions(ActionsHelper.createUploadManager(request), project);
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME) &&
                    !AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES)) {
                submissions =
                    ActionsHelper.getMostRecentSubmissions(ActionsHelper.createUploadManager(request), project);
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME) &&
                    AuthorizationHelper.hasUserRole(request, Constants.REVIEWER_ROLE_NAMES) &&
                    ActionsHelper.isInOrAfterPhase(phases, phaseIdx, Constants.REVIEW_PHASE_NAME)) {
                submissions =
                    ActionsHelper.getMostRecentSubmissions(ActionsHelper.createUploadManager(request), project);
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENER_SUBM_PERM_NAME) &&
                    ActionsHelper.isInOrAfterPhase(phases, phaseIdx, Constants.SCREENING_PHASE_NAME)) {
                submissions =
                    ActionsHelper.getMostRecentSubmissions(ActionsHelper.createUploadManager(request), project);
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {
                // Obtain an instance of Upload Manager
                UploadManager upMgr = ActionsHelper.createUploadManager(request);
                SubmissionStatus[] allSubmissionStatuses = upMgr.getAllSubmissionStatuses();

                // Get "my" (submitter's) resource
                Resource myResource = ActionsHelper.getMyResourceForPhase(request, null);

                Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
                Filter filterStatus = ActionsHelper.createSubmissionStatusFilter(allSubmissionStatuses);
                Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(myResource.getId());

                Filter filter =
                    new AndFilter(Arrays.asList(new Filter[] {filterProject, filterStatus, filterResource}));

                submissions = upMgr.searchSubmissions(filter);
            }

            if (submissions == null) {
                    submissions = new Submission[0];
            }
            // Use comparator to sort submissions either by placement
            // or by the time when they were uploaded
            SubmissionComparer comparator = new SubmissionComparer();

            comparator.assignSubmitters(submitters);
            Arrays.sort(submissions, comparator);

            getPreviousUploadsForSubmissions(request, project, phaseGroup, submissions);
            
            phaseGroup.setSubmissions(submissions);

            if (submissions.length != 0) {
                long[] uploadIds = new long[submissions.length];

                for (int j = 0; j < submissions.length; ++j) {
                    uploadIds[j] = submissions[j].getUpload().getId();
                }

                ScreeningManager scrMgr = ActionsHelper.createScreeningManager(request);
                ScreeningTask[] tasks = scrMgr.getScreeningTasks(uploadIds, true);

                phaseGroup.setScreeningTasks(tasks);
            }
        }

        if (phaseName.equalsIgnoreCase(Constants.SCREENING_PHASE_NAME) &&
                phaseGroup.getSubmissions() != null) {
            Submission[] submissions = phaseGroup.getSubmissions();

            if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SCREENER_SUBM_PERM_NAME) &&
                    !AuthorizationHelper.hasUserRole(request, Constants.PRIMARY_SCREENER_ROLE_NAME)) {
                Resource[] my = ActionsHelper.getMyResourcesForPhase(request, phases[phaseIdx]);
                ScreeningTask[] allTasks = phaseGroup.getScreeningTasks();
                List tempSubs = new ArrayList();
                List tasks = new ArrayList();

                for (int j = 0; j < submissions.length; ++j) {
                    for (int k = 0; k < my.length; ++k) {
                        if (my[k].containsSubmission(submissions[j].getId())) {
                            tempSubs.add(submissions[j]);
                            tasks.add(allTasks[j]);
                        }
                    }
                }

                submissions = (Submission[]) tempSubs.toArray(new Submission[tempSubs.size()]);
                phaseGroup.setSubmissions(submissions);
                phaseGroup.setScreeningTasks((ScreeningTask[]) tasks.toArray(new ScreeningTask[tasks.size()]));
            }

            Resource[] screeners = ActionsHelper.getResourcesForPhase(allProjectResources, phases[phaseIdx]);

            phaseGroup.setReviewers(screeners);

            // No need to fetch auto screening results if there are no submissions
            if (submissions.length == 0) {
                return;
            }

            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
            ScorecardType[] allScorecardTypes = scrMgr.getAllScorecardTypes();

            List submissionIds = new ArrayList();

            for (int j = 0; j < submissions.length; ++j) {
                submissionIds.add(new Long(submissions[j].getId()));
            }

            Filter filterSubmissions = new InFilter("submission", submissionIds);
            Filter filterScorecard = new EqualToFilter("scorecardType",
                    new Long(ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Screening").getId()));

            Filter filter = new AndFilter(filterSubmissions, filterScorecard);

            // Obtain an instance of Review Manager
            ReviewManager revMgr = ActionsHelper.createReviewManager(request);
            Review[] reviews = revMgr.searchReviews(filter, false);

            phaseGroup.setScreenings(reviews);
        }
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @param request
     * @param phaseGroup
     * @param project
     * @param phase
     * @param nextPhase
     * @param allProjectResources
     * @param submitters
     * @param isAfterAppealsResponse
     * @throws BaseException
     */
    private static void serviceReviewsAppFunc(HttpServletRequest request,
            PhaseGroup phaseGroup, Project project, Phase phase, Phase nextPhase,
            Resource[] allProjectResources, Resource[] submitters, boolean isAfterAppealsResponse)
        throws BaseException {
        String phaseName = phase.getPhaseType().getName();

        if (phaseName.equalsIgnoreCase(Constants.REVIEW_PHASE_NAME)) {
            // If the project is not in the after appeals response state, allow uploading of testcases
            phaseGroup.setUploadingTestcasesAllowed(!isAfterAppealsResponse);

            Submission[] submissions = null;

            if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME)) {
                submissions =
                    ActionsHelper.getMostRecentSubmissions(ActionsHelper.createUploadManager(request), project);
            }

            boolean mayViewMostRecentAfterAppealsResponse =
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_AAR_PERM_NAME);

            if (submissions == null &&
                    ((mayViewMostRecentAfterAppealsResponse && isAfterAppealsResponse) ||
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_RECENT_SUBM_PERM_NAME))) {
                submissions =
                    ActionsHelper.getMostRecentSubmissions(ActionsHelper.createUploadManager(request), project);
            }
            if (submissions == null &&
                    AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {
                // Obtain an instance of Upload Manager
                UploadManager upMgr = ActionsHelper.createUploadManager(request);
                SubmissionStatus[] allSubmissionStatuses = upMgr.getAllSubmissionStatuses();

                // Get "my" (submitter's) resource
                Resource myResource = ActionsHelper.getMyResourceForPhase(request, null);

                Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
                Filter filterStatus = ActionsHelper.createSubmissionStatusFilter(allSubmissionStatuses);
                Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(myResource.getId());

                Filter filter =
                    new AndFilter(Arrays.asList(new Filter[] {filterProject, filterStatus, filterResource}));

                submissions = upMgr.searchSubmissions(filter);
            }
            // No submissions -- nothing to review,
            // but the list of submissions must not be null in this case
            if (submissions == null) {
                submissions = new Submission[0];
            }

            // Use comparator to sort submissions either by placement
            // or by the time when they were uploaded
            SubmissionComparer comparator = new SubmissionComparer();

            comparator.assignSubmitters(submitters);
            Arrays.sort(submissions, comparator);

            phaseGroup.setSubmissions(submissions);

            // Some resource roles can always see links to reviews (if there are any).
            // There is no corresponding permission, so the list of roles is hard-coded
            boolean allowedToSeeReviewLink =
                AuthorizationHelper.hasUserRole(request, new String[] {
                    Constants.MANAGER_ROLE_NAME, Constants.GLOBAL_MANAGER_ROLE_NAME,
                    Constants.REVIEWER_ROLE_NAME, Constants.ACCURACY_REVIEWER_ROLE_NAME,
                    Constants.FAILURE_REVIEWER_ROLE_NAME, Constants.STRESS_REVIEWER_ROLE_NAME,
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

            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
            ScorecardType[] allScorecardTypes = scrMgr.getAllScorecardTypes();

            List submissionIds = new ArrayList();

            for (int j = 0; j < submissions.length; ++j) {
                submissionIds.add(new Long(submissions[j].getId()));
            }

            List reviewerIds = new ArrayList();

            for (int j = 0; j < reviewers.length; ++j) {
                reviewerIds.add(new Long(reviewers[j].getId()));
            }

            Review[] ungroupedReviews = null;

            if (!(submissionIds.isEmpty() || reviewerIds.isEmpty())) {
                Filter filterSubmissions = new InFilter("submission", submissionIds);
                Filter filterReviewers = new InFilter("reviewer", reviewerIds);
                Filter filterScorecard = new EqualToFilter("scorecardType",
                        new Long(ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Review").getId()));

                List reviewFilters = new ArrayList();
                reviewFilters.add(filterReviewers);
                reviewFilters.add(filterScorecard);
                reviewFilters.add(filterSubmissions);

                // Create final filter
                Filter filterForReviews = new AndFilter(reviewFilters);

                boolean needFullReviews = false;

                // If next phase is Appeals and that phase is not scheduled, ...
                if (nextPhase != null &&
                        nextPhase.getPhaseType().getName().equalsIgnoreCase(Constants.APPEALS_PHASE_NAME)) {
                    // ... indicate that full review (with all comments) must be retrieved
                    if (!nextPhase.getPhaseStatus().getName().equalsIgnoreCase("Scheduled")) {
                        needFullReviews = true;
                    }
                }

                // Obtain an instance of Review Manager
                ReviewManager revMgr = ActionsHelper.createReviewManager(request);
                // Get the reviews from every individual reviewer
                ungroupedReviews = revMgr.searchReviews(filterForReviews, needFullReviews);
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
                // Obtain an instance of Upload Manager
                UploadManager upMgr = ActionsHelper.createUploadManager(request);
                UploadStatus[] allUploadStatuses = upMgr.getAllUploadStatuses();
                UploadType[] allUploadTypes = upMgr.getAllUploadTypes();

                Filter filterResource = new InFilter("resource_id", reviewerIds);
                Filter filterStatus = UploadFilterBuilder.createUploadStatusIdFilter(
                        ActionsHelper.findUploadStatusByName(allUploadStatuses, "Active").getId());
                Filter filterType = UploadFilterBuilder.createUploadTypeIdFilter(
                        ActionsHelper.findUploadTypeByName(allUploadTypes, "Test Case").getId());

                Filter filterForUploads =
                    new AndFilter(Arrays.asList(new Filter[] {filterResource, filterStatus, filterType}));

                Upload[] testCases = upMgr.searchUploads(filterForUploads);

                phaseGroup.setTestCases(testCases);
            }

            Review[][] reviews = new Review[submissions.length][];
            Date[] reviewDates = new Date[submissions.length];

            for (int j = 0; j < submissions.length; ++j) {
                Date latestDate = null;
                Review[] innerReviews = new Review[reviewers.length];
                Arrays.fill(innerReviews, null);

                for (int k = 0; k < reviewers.length; ++k) {
                    for (int l = 0; l < ungroupedReviews.length; ++l) {
                        Review ungrouped = ungroupedReviews[l];
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
     * TODO: Write documentation for this method.
     *
     * @param request
     * @param phaseGroup
     * @param project
     * @param phase
     * @param allProjectResources
     * @param isAfterAppealsResponse
     * @throws BaseException
     */
    private static void serviceAggregationAppFunc(HttpServletRequest request, PhaseGroup phaseGroup,
            Project project, Phase phase, Resource[] allProjectResources, boolean isAfterAppealsResponse)
        throws BaseException {
        retrieveSubmissions(request, phaseGroup, project, isAfterAppealsResponse);

        String phaseName = phase.getPhaseType().getName();

        if (phaseName.equalsIgnoreCase(Constants.AGGREGATION_PHASE_NAME) &&
                phaseGroup.getSubmitters() != null) {
            Resource winner = ActionsHelper.getWinner(request, phase.getProject().getId());
            phaseGroup.setWinner(winner);

            Resource[] aggregator = ActionsHelper.getResourcesForPhase(allProjectResources, phase);

            if (aggregator == null || aggregator.length == 0) {
                return;
            }

            Filter filterResource = new EqualToFilter("reviewer", new Long(aggregator[0].getId()));
            Filter filterProject = new EqualToFilter("project", new Long(project.getId()));

            Filter filter = new AndFilter(filterResource, filterProject);

            // Obtain an instance of Review Manager
            ReviewManager revMgr = ActionsHelper.createReviewManager(request);
            Review[] reviews = revMgr.searchReviews(filter, true);

            if (reviews.length != 0) {
                phaseGroup.setAggregation(reviews[0]);
            }
        }

        if (phaseName.equalsIgnoreCase(Constants.AGGREGATION_REVIEW_PHASE_NAME) &&
                phaseGroup.getAggregation() != null && phaseGroup.getAggregation().isCommitted()) {
            Review aggregation = phaseGroup.getAggregation();

            boolean reviewCommitted = true;

            for (int j = 0; j < aggregation.getNumberOfComments(); ++j) {
                    // Get a comment for the current iteration
                    Comment comment = aggregation.getComment(j);

                if (ActionsHelper.isAggregationReviewComment(comment)) {
                        String extraInfo = (String) comment.getExtraInfo();
                        if (!("Approved".equalsIgnoreCase(extraInfo) ||
                                "Rejected".equalsIgnoreCase(extraInfo))) {
                            reviewCommitted = false;
                            break;
                        }
                    }
                }

            final String phaseStatus = phase.getPhaseStatus().getName();

            phaseGroup.setDisplayAggregationReviewLink(!phaseStatus.equalsIgnoreCase(Constants.SCHEDULED_PH_STATUS_NAME));
            phaseGroup.setAggregationReviewCommitted(reviewCommitted);
        }
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @param request
     * @param phaseGroup
     * @param project
     * @param phase
     * @param allProjectResources
     * @param finalFixes
     * @param isAfterAppealsResponse
     * @throws BaseException
     */
    private static void serviceFinalFixAppFunc(HttpServletRequest request, PhaseGroup phaseGroup, Project project,
            Phase phase, Resource[] allProjectResources, FinalFixesInfo finalFixes, boolean isAfterAppealsResponse)
        throws BaseException {
        retrieveSubmissions(request, phaseGroup, project, isAfterAppealsResponse);

        String phaseName = phase.getPhaseType().getName();

        if (phaseGroup.getSubmitters() != null) {
            Resource winner = phaseGroup.getWinner();
            if (winner == null) {
                winner = ActionsHelper.getWinner(request, phase.getProject().getId());
                phaseGroup.setWinner(winner);
            }
            if (winner == null) {
                return;
            }

            if (finalFixes.finalFixes == null) {
                // Obtain an instance of Upload Manager
                UploadManager upMgr = ActionsHelper.createUploadManager(request);
//                UploadStatus[] allUploadStatuses = upMgr.getAllUploadStatuses();
                UploadType[] allUploadTypes = upMgr.getAllUploadTypes();

//                Filter filterStatus = UploadFilterBuilder.createUploadStatusIdFilter(
//                        ActionsHelper.findUploadStatusByName(allUploadStatuses, "Active").getId());
                Filter filterType = UploadFilterBuilder.createUploadTypeIdFilter(
                        ActionsHelper.findUploadTypeByName(allUploadTypes, "Final Fix").getId());
                Filter filterResource = UploadFilterBuilder.createResourceIdFilter(winner.getId());

                Filter filter = new AndFilter(Arrays.asList(
                        new Filter[] {/*filterStatus, */filterType, filterResource}));
                finalFixes.finalFixes = upMgr.searchUploads(filter);

                Arrays.sort(finalFixes.finalFixes, new Comparators.UploadComparer());

                finalFixes.finalFixIdx = 0;
            }
        }

        if (phaseName.equalsIgnoreCase(Constants.FINAL_FIX_PHASE_NAME) && finalFixes.finalFixes != null) {
            if (finalFixes.finalFixIdx < finalFixes.finalFixes.length) {
                phaseGroup.setFinalFix(finalFixes.finalFixes[finalFixes.finalFixIdx++]);
            }
        }

        if (phaseName.equalsIgnoreCase(Constants.FINAL_REVIEW_PHASE_NAME) &&
                phaseGroup.getSubmitters() != null) {
            Resource[] reviewer = ActionsHelper.getResourcesForPhase(allProjectResources, phase);

            if (reviewer == null || reviewer.length == 0) {
                return;
            }

            Filter filterResource = new EqualToFilter("reviewer", new Long(reviewer[0].getId()));
            Filter filterProject = new EqualToFilter("project", new Long(project.getId()));

            Filter filter = new AndFilter(filterResource, filterProject);

            // Obtain an instance of Review Manager
            ReviewManager revMgr = ActionsHelper.createReviewManager(request);
            Review[] reviews = revMgr.searchReviews(filter, true);

            if (reviews.length != 0) {
                phaseGroup.setFinalReview(reviews[0]);
            }
        }
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @param request
     * @param phaseGroup
     * @param project
     * @param thisPhase
     * @param allProjectResources
     * @param isAfterAppealsResponse
     * @throws BaseException
     */
    private static void serviceApprovalAppFunc(HttpServletRequest request, PhaseGroup phaseGroup,
            Project project, Phase thisPhase, Resource[] allProjectResources, boolean isAfterAppealsResponse)
        throws BaseException {
        if (phaseGroup.getSubmitters() == null) {
            return;
        }
        retrieveSubmissions(request, phaseGroup, project, isAfterAppealsResponse);

        Resource winner = ActionsHelper.getWinner(request, project.getId());
        phaseGroup.setWinner(winner);

        Resource[] approver = ActionsHelper.getResourcesForPhase(allProjectResources, thisPhase);

        if (approver == null || approver.length == 0) {
            return;
        }

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        ScorecardType[] allScorecardTypes = scrMgr.getAllScorecardTypes();

        Filter filterResource = new EqualToFilter("reviewer", new Long(approver[0].getId()));
        Filter filterProject = new EqualToFilter("project", new Long(project.getId()));
        Filter filterScorecard = new EqualToFilter("scorecardType",
                new Long(ActionsHelper.findScorecardTypeByName(allScorecardTypes, "Client Review").getId()));

        Filter filter = new AndFilter(Arrays.asList(
                new Filter[] {filterResource, filterProject, filterScorecard}));

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        Review[] reviews = revMgr.searchReviews(filter, true);

        if (reviews.length != 0) {
            phaseGroup.setApproval(reviews[0]);
        }
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @param request
     * @param phaseGroup
     * @param project
     * @param isAfterAppealsResponse
     * @throws BaseException
     */
    private static void retrieveSubmissions(HttpServletRequest request,
            PhaseGroup phaseGroup, Project project, boolean isAfterAppealsResponse)
        throws BaseException {
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
            submissions =
                ActionsHelper.getMostRecentSubmissions(ActionsHelper.createUploadManager(request), project);
        }
        if (submissions == null &&
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_SUBM_PERM_NAME)) {
            // Obtain an instance of Upload Manager
            UploadManager upMgr = ActionsHelper.createUploadManager(request);
            SubmissionStatus[] allSubmissionStatuses = upMgr.getAllSubmissionStatuses();

            // Get "my" (submitter's) resource
            Resource myResource = ActionsHelper.getMyResourceForPhase(request, null);

            Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
            Filter filterStatus = SubmissionFilterBuilder.createSubmissionStatusIdFilter(
                    ActionsHelper.findSubmissionStatusByName(allSubmissionStatuses, "Active").getId());
            Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(myResource.getId());

            Filter filter =
                new AndFilter(Arrays.asList(new Filter[] {filterProject, filterStatus, filterResource}));

            submissions = upMgr.searchSubmissions(filter);
        }
        if (submissions != null) {
            phaseGroup.setSubmissions(submissions);
        }
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @param request
     * @param allProjectResources
     * @param isAfterAppealsResponse
     * @param prevSubmitters
     * @return
     */
    private static Resource[] getSubmitters(HttpServletRequest request,
            Resource[] allProjectResources, boolean isAfterAppealsResponse, Resource[] prevSubmitters) {
        final boolean canSeeSubmitters = (isAfterAppealsResponse ||
                AuthorizationHelper.hasUserPermission(request, Constants.VIEW_ALL_SUBM_PERM_NAME));

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
                            continue;
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

    /**
     * TODO: Document this helper class.
     *
     * @author George1
     * @version 1.0
     */
    static class FinalFixesInfo {

        public Upload[] finalFixes = null;

        public int finalFixIdx = -1;

        public FinalFixesInfo() {
            // empty constructor
        }
    }
}
