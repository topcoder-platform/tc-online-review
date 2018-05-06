/*
 * Copyright (C) 2009-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;

import com.topcoder.project.phases.Phase;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection, email sending. This class handles
 * both the Screening and Checkpoint Screening phases. If the input is of other phase types, PhaseNotSupportedException
 * will be thrown.
 * </p>
 * <p>
 * The (checkpoint) screening phase can start as soon as the following conditions met:
 * <ul>
 * <li>The dependencies are met.</li>
 * <li>Phase start date/time is reached (if specified).</li>
 * <li>There are active checkpoint or contest submissions.</li>
 * </ul>
 * and can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>All active (checkpoint) submissions have one screening scorecard committed.</li>
 * </ul>
 * </p>
 * <p>
 * The additional logic for executing this phase is: When screening is stopping,
 * <ul>
 * <li>All submissions with failed screening scorecard scores should be set to the status
 * "Failed Screening" or "Failed Checkpoint Screening".</li>
 * <li>Screening score for the all submissions will be calculated and saved to the submission.</li>
 * </ul>
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * @author tuenm, bose_java, argolite, waits, saarixx, microsky, lmmortal, VolodymyrK
 * @version 1.8.3
 */
abstract class BaseScreeningPhaseHandler extends AbstractPhaseHandler {

    /**
     * Indicates whether the handler is for the "Checkpoint Screening" phase or for the "Screening" phase.
     */
    final private boolean isCheckpoint;

    /**
     * Stores the phase type name.
     */
    final private String phaseTypeName;

    /**
     * Stores the submission type name.
     */
    final private String submissionTypeName;

    /**
     * Stores the screener role name.
     */
    final private String screenerRoleName;

    /**
     * Create a new instance of BaseScreeningPhaseHandler using the given namespace
     * for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @param isCheckpoint True if the phase type is "Checkpoint Screening" and false for "Screening" phase type
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    protected BaseScreeningPhaseHandler(String namespace, boolean isCheckpoint) throws ConfigurationException {
        super(namespace);
        this.isCheckpoint = isCheckpoint;
        
        if (isCheckpoint) {
            phaseTypeName = Constants.PHASE_CHECKPOINT_SCREENING;
            submissionTypeName = Constants.SUBMISSION_TYPE_CHECKPOINT_SUBMISSION;
            screenerRoleName = Constants.ROLE_CHECKPOINT_SCREENER;
        } else {
            phaseTypeName = Constants.PHASE_SCREENING;
            submissionTypeName = Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION;
            screenerRoleName = Constants.ROLE_PRIMARY_SCREENER;
        }
    }

    /**
     * Check if the input phase can be executed or not. This method will check
     * the phase status to see what will be executed. This method will be called
     * by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met.</li>
     * <li>Phase start date/time is reached (if specified).</li>
     * <li>There active checkpoint or contest submissions.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met</li>
     * <li>All active (checkpoint) submissions have one screening scorecard committed.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * </ul>
     * </p>
     * @param phase The input phase to check.
     * @return the validation result indicating whether the associated operation can be performed, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseNotSupportedException if the input phase type is not "Screening" for non-checkpoint phase
     *             and "Checkpoint Screening" for checkpoint phase.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, phaseTypeName);

        // Will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        OperationCheckResult result;
        if (toStart) {
            // return true if all dependencies have stopped and start time has been reached.
            result = PhasesHelper.checkPhaseCanStart(phase);
            if (!result.isSuccess()) {
                return result;
            }

            // Search all active submissions of the respective type
            Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                    phase.getProject().getId(), submissionTypeName);
            if (subs.length > 0) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("There are no submissions for the project");
            }
        } else {
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            }

            // For Copilot Posting contests the Screening phase closes immediately if no screener is assigned
            if (!isCheckpoint) {
                try {
                    com.topcoder.management.project.Project project =
                            getManagerHelper().getProjectManager().getProject(phase.getProject().getId());
                    if (project.getProjectCategory().getName().equals(Constants.PROJECT_CATEGORY_COPILOT_POSTING)) {
                        Resource[] screeners = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
                                new String[] {screenerRoleName}, phase.getId());
                        if (screeners.length == 0) {
                            return OperationCheckResult.SUCCESS;
                        }
                    }
                } catch (PersistenceException e) {
                    throw new PhaseHandlingException("There was an error with project persistence", e);
                }
            }

            return areScorecardsCommitted(phase);
        }
    }

    /**
     * Provides additional logic to execute a phase. This method will be called
     * by start() and end() methods of PhaseManager implementations in Phase
     * Management component. This method can send email to a group of users
     * associated with timeline notification for the project. The email can be
     * send on start phase or end phase base on configuration settings.
     * <p>
     * If the input phase status is Scheduled, then it will do nothing.
     * </p>
     * <p>
     * If the input phase status is Open, then it will perform the following additional logic to stop the phase:
     * <ul>
     * <li>all submissions with failed screening scorecard scores should be set to the status "Failed Screening" or
     * "Failed Checkpoint Screening".</li>
     * <li>Screening score for the all submissions will be calculated and saved to the submission.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.2 update: if it is start, get the submission's info. if it is stop, get the screening result info.
     * </p>
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not "Screening" for non-checkpoint phase
     *             and "Checkpoint Screening" for checkpoint phase.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *             string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, phaseTypeName);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        Map<String, Object> values = new HashMap<String, Object>();

        com.topcoder.management.project.Project project;
        try {
            project = getManagerHelper().getProjectManager().getProject(phase.getProject().getId());
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("There was an error with project persistence", e);
        }

        if (toStart) {
            if (PhasesHelper.isStudio(project)) {
                PhasesHelper.autoScreenStudioSubmissions(project, getManagerHelper(),
                    submissionTypeName, operator);
            }

            putPhaseStartInfos(phase, values);
        } else {
            // Search all submissions for current project
            Submission[] submissions = PhasesHelper.getActiveProjectSubmissions(
                    getManagerHelper().getUploadManager(), phase.getProject().getId(), submissionTypeName);

            // For Copilot Posting contests the Screening phase closes immediately if no screener is assigned
            boolean autoComplete = false;
            if (!isCheckpoint) {
                if (project.getProjectCategory().getName().equals(Constants.PROJECT_CATEGORY_COPILOT_POSTING)) {
                    Resource[] screeners = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
                            new String[] {screenerRoleName}, phase.getId());
                    autoComplete = (screeners.length == 0);
                }
            }

            // flag to indicate whether all submissions fail screening
            boolean noScreeningPass = true;

            if (!autoComplete) {
                Review[] screenReviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(), phase.getId(), null);
                if (submissions.length != screenReviews.length) {
                    throw new PhaseHandlingException(
                            "Submission count does not match screening count for project:" + phase.getProject().getId());
                }

                // get minimum score
                float minScore = PhasesHelper.getScorecardMinimumScore(
                        getManagerHelper().getScorecardManager(), screenReviews[0]);

                for (Submission submission : submissions) {
                    if (!processAndUpdateSubmission(submission, screenReviews, minScore, operator)) {
                        // if there is one passed screening, set noScreeningPass to false
                        noScreeningPass = false;
                    }
                }
            } else {
                noScreeningPass = false;
            }

            // put the submission screening result
            values.put("SUBMITTER", PhasesHelper.constructSubmitterValues(submissions,
                getManagerHelper().getResourceManager(), true));
            values.put("NO_SCREENING_PASS", noScreeningPass ? 1 : 0);

            // For non-checkpoint screening if there no submission passed screening, insert post-mortem phase
            if (!isCheckpoint && noScreeningPass) {
                PhasesHelper.insertPostMortemPhase(phase.getProject(), phase, getManagerHelper(), operator);
            }
        }

        // send mail for start/stop phase
        sendPhaseEmails(phase, values);
    }

    /**
     * <p>
     * Puts the screening start phase info for email generation.
     * </p>
     * @param phase the current Phase
     * @param values the value map to hold the info
     * @throws PhaseHandlingException if any error occurs
     */
    private void putPhaseStartInfos(Phase phase, Map<String, Object> values) throws PhaseHandlingException {
        // get the screeners
        Resource[] screeners = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
            new String[] {screenerRoleName}, phase.getId());
        values.put(isCheckpoint ? "NEED_CHECKPOINT_SCREENER" : "NEED_PRIMARY_SCREENER", screeners.length == 0 ? 1 : 0);
        // get submissions
        Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                phase.getProject().getId(), submissionTypeName);
        // put the submitter info into the map
        values.put("SUBMITTER",
            PhasesHelper.constructSubmitterValues(subs, getManagerHelper().getResourceManager(), false));
    }

    /**
     * This method is called from perform method when phase is stopping. It does
     * the following :
     * <ul>
     * <li>All submissions with failed screening scorecard scores are set to the status "Failed Screening" or
     * "Failed Checkpoint Screening".</li>
     * <li>Screening score for the all submissions will be saved to the submission.</li>
     * </ul>
     * <p>
     * Change notes: Change return value from void to boolean to indicate whether the checked submission failed
     * screen. True if failed, false otherwise.
     * </p>
     * @param submission the submission to process and update.
     * @param screenReviews screening reviews.
     * @param minScore minimum scorecard score.
     * @param operator operator name.
     * @return true if the submission failed screen, false otherwise.
     * @throws PhaseHandlingException if there was a problem retrieving/updating data.
     */
    private boolean processAndUpdateSubmission(Submission submission,
                    Review[] screenReviews, float minScore, String operator) throws PhaseHandlingException {

        // boolean flag to indicate whether the submission failed screen
        boolean failedScreening = false;

        // Find the matching screening review
        Review review = null;
        long subId = submission.getId();

        for (Review screenReview : screenReviews) {
            if (subId == screenReview.getSubmission()) {
                review = screenReview;
                break;
            }
        }

        if (review == null) {
            throw new PhaseHandlingException("Unable to find screening review for the submission " + subId);
        }

        Double screeningScore = review.getScore();
        UploadManager uploadManager = getManagerHelper().getUploadManager();

        try {
            // Set the screening score to the submission instead of the resource
            submission.setScreeningScore(Double.valueOf(String.valueOf(screeningScore)));

            // If screeningScore < screening minimum score
            // set submission status to "Failed Screening" or "Failed Checkpoint Screening"
            if (screeningScore < minScore) {
                SubmissionStatus subStatus = LookupHelper.getSubmissionStatus(uploadManager,
                    isCheckpoint ? Constants.SUBMISSION_STATUS_FAILED_CHECKPOINT_SCREENING : Constants.SUBMISSION_STATUS_FAILED_SCREENING);

                submission.setSubmissionStatus(subStatus);
                failedScreening = true;
            }
            uploadManager.updateSubmission(submission, operator);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("There was a upload persistence error", e);
        }

        return failedScreening;
    }

    /**
     * Returns true if all the submissions for the given project have one scorecard committed, false otherwise.
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * </ul>
     * </p>
     * @param phase the phase to check.
     * @return the validation result indicating whether the primary scorecards are committed, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseHandlingException if there was an error retrieving data.
     */
    private OperationCheckResult areScorecardsCommitted(Phase phase) throws PhaseHandlingException {
        // get all screening scorecards for the phase
        Review[] screenReviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(), phase.getId(), null);

        // get the submissions for the project
        Submission[] submissions = PhasesHelper.getActiveProjectSubmissions(
                getManagerHelper().getUploadManager(), phase.getProject().getId(), submissionTypeName);

        // If the number of reviews doesn't match the number of submissions it means that
        // not all screening scorecards have been committed yet.
        if (screenReviews.length != submissions.length) {
            return new OperationCheckResult("Not all screening scorecards are committed");
        }

        for (Submission submission : submissions) {
            long subId = submission.getId();

            // check if each submission has a review
            boolean foundReview = false;

            for (Review screenReview : screenReviews) {
                if (subId == screenReview.getSubmission()) {
                    foundReview = true;

                    // check if each review is committed
                    if (!screenReview.isCommitted()) {
                        return new OperationCheckResult("Screening for " + subId + " has not been committed yet.");
                    }

                    break;
                }
            }

            // if a review was not found for a particular submission.
            if (!foundReview) {
                return new OperationCheckResult("Screening for " + subId + " has not been committed yet.");
            }
        }

        return OperationCheckResult.SUCCESS;
    }
}
