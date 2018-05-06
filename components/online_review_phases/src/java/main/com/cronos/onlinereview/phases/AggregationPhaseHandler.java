/*
 * Copyright (C) 2009-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.phases.logging.LogMessage;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email sending. This class handle the
 * aggregation phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * If the input phase status is Scheduled, then it will check if the phase can be started using the following
 * conditions:
 * <ul>
 * <li>The dependencies are met.</li>
 * </ul>
 * </p>
 * <p>
 * If the input phase status is Open, then it will check if the phase can be stopped using the following
 * conditions:
 * <ul>
 * <li>The dependencies are met.</li>
 * <li>The winning submission have one aggregated review scorecard committed.</li>
 * </ul>
 * </p>
 * <p>
 * The additional logic for executing this phase is:
 * </p>
 * <p>
 * If the input phase status is Scheduled, then it will perform the following additional logic to start the phase:
 * <ul>
 * <li>If Aggregation worksheet is not created, it should be created; otherwise it should be marked uncommitted, as
 * well as the aggregation review comments.</li>
 * </ul>
 * </p>
 * <p>
 * If the input phase status is Open, then it will do nothing.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager,
 * etc).</li>
 * <li>Support for more information in the email generated: for start, puts the aggregators number into the values
 * map.</li>
 * </ul>
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>The return changes from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author tuenm, bose_java, argolite, waits, microsky
 * @version 1.7.10
 */
public class AggregationPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.AggregationPhaseHandler";

    /**
     * array of comment types to be copied from individual review scorecards to
     * new aggregation worksheet.
     */
    private static final String[] COMMENT_TYPES_TO_COPY = new String[] {
        Constants.COMMENT_TYPE_COMMENT, Constants.COMMENT_TYPE_REQUIRED,
        Constants.COMMENT_TYPE_RECOMMENDED, Constants.COMMENT_TYPE_APPEAL,
        Constants.COMMENT_TYPE_APPEAL_RESPONSE, Constants.COMMENT_TYPE_AGGREGATION_COMMENT,
        Constants.COMMENT_TYPE_SUBMITTER_COMMENT, Constants.COMMENT_TYPE_MANAGER_COMMENT };

    /**
     * The log instance used by this handler.
     */
    private static final Log log = LogManager
                    .getLog(AggregationPhaseHandler.class.getName());

    /**
     * Create a new instance of AggregationPhaseHandler using the default
     * namespace for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public AggregationPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AggregationPhaseHandler using the given
     * namespace for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public AggregationPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
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
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met.</li>
     * <li>The winning submission have one aggregated review scorecard committed.</li>
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
     * @throws PhaseNotSupportedException if the input phase type is not
     *             &quot;Aggregation&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_AGGREGATION);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        OperationCheckResult result;
        if (toStart) {
            // return true if all dependencies have stopped and start time has
            // been reached.
            result = PhasesHelper.checkPhaseCanStart(phase);
            if (!result.isSuccess()) {
                return result;
            }

            Resource winner = PhasesHelper.getWinningSubmitter(getManagerHelper(), phase.getProject().getId());

            Resource[] aggregator = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
                new String[] {Constants.ROLE_AGGREGATOR }, phase.getId());

            if (winner == null) {
                return new OperationCheckResult("There is no winner for the project");
            }
            if (aggregator.length != 1) {
                return new OperationCheckResult("There is no aggregator for the project");
            }
            // return true if there is a winner and an aggregator
            return OperationCheckResult.SUCCESS;
        } else {
            // return true if all dependencies have stopped and aggregation worksheet exists.
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            }
            if (isAggregationWorksheetPresent(phase)) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("Aggregation review scorecard is not yet committed");
            }
        }
    }

    /**
     * <p>
     * Provides additional logic to execute a phase. This method will be called by start() and end() methods of
     * PhaseManager implementations in Phase Management component. This method can send email to a group of users
     * associated with timeline notification for the project.The email can be send on start phase or end phase base
     * on configuration settings.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will perform the following additional logic to start the
     * phase:
     * <ul>
     * <li>If Aggregation worksheet is not created, it should be created; otherwise it should be marked
     * uncommitted, as well as the aggregation review comments.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Open, then it will do nothing.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * In version 1.2, put the aggregator number into the values map.
     * </p>
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not
     *             "Aggregation" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *             string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_AGGREGATION);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        Map<String, Object> values = new HashMap<String, Object>();
        if (toStart) {
            createAggregationWorksheet(phase, operator);
        }
        sendPhaseEmails(phase, values);
    }

    /**
     * This method is called from perform method when phase is starting.
     * The method crates the Aggregation worksheet.
     * @param phase the phase.
     * @param operator operator name.
     * @throws PhaseHandlingException if an error occurs when retrieving/saving data.
     */
    private void createAggregationWorksheet(Phase phase, String operator)
        throws PhaseHandlingException {
        // Check if there is already a previous Aggregation phase.
        // In the past the system supported multiple Aggregation phase cycles but this is not longer supported.
        Phase previousAggregationPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_AGGREGATION, false, false);
        if (previousAggregationPhase != null) {
            throw new PhaseHandlingException("Multiple Aggregation phases are no longer supported");
        }

        try {
            // Search for id of the Aggregator
            Resource[] aggregators = PhasesHelper.searchResourcesForRoleNames(
                getManagerHelper(), new String[] {Constants.ROLE_AGGREGATOR }, phase.getId());
            if (aggregators.length != 1) {
                throw new PhaseHandlingException(
                    "Not exactly one Aggregator found for phase: " + phase.getId());
            }
            // create the worksheet
            Review aggWorksheet = new Review();
            aggWorksheet.setAuthor(aggregators[0].getId());
            aggWorksheet.setProjectPhase(phase.getId());

            // copy the comments from review scorecards
            Phase reviewPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_REVIEW, false, true);

            // find winning submitter.
            Resource winningSubmitter = PhasesHelper.getWinningSubmitter(getManagerHelper(), phase.getProject().getId());
            if (winningSubmitter == null) {
                throw new PhaseHandlingException("No winner for project with id : "
                    + phase.getProject().getId());
            }

            UploadManager uploadManager = getManagerHelper().getUploadManager();

            // find the winning submission
            Filter filter = SubmissionFilterBuilder.createResourceIdFilter(winningSubmitter.getId());

            long submissionTypeId = LookupHelper.getSubmissionType(uploadManager,
                Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION).getId();
            Filter typeFilter = SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionTypeId);

            Submission[] submissions = uploadManager.searchSubmissions(new AndFilter(filter, typeFilter));

            // OrChange - Modified to handle multiple submissions for a single resource
            Long winningSubmissionId = null;
            if (submissions == null || submissions.length == 0) {
                log.log(Level.ERROR, "No winner for project with id" + phase.getProject().getId());
                throw new PhaseHandlingException(
                                "No winning submission for project[Winner id search"
                                                + " returned empty result for submission] with id : "
                                                + phase.getProject().getId());
            } else if (submissions.length >= 1) {
                // loop through the submissions and find out the one with the placement as first
                for (Submission submission : submissions) {
                    if (submission.getPlacement() != null && submission.getPlacement() == 1) {
                        winningSubmissionId = submission.getId();
                        break;
                    }
                }
            } else {
                winningSubmissionId = submissions[0].getId();
            }
            // No winner id set at this point means, none of the submissions have placement as 1
            if (winningSubmissionId == null) {
                throw new PhaseHandlingException(
                                "No winning submission for project[Submissions from the"
                                                + " winner id does not have placement 1] with id : "
                                                + phase.getProject().getId());
            }

            // Search all review scorecard from review phase for the winning  submitter
            Review[] reviews = PhasesHelper.searchReviewsForPhase(
                            getManagerHelper(), reviewPhase.getId(),
                            winningSubmissionId);

            for (Review review : reviews) {
                aggWorksheet.setScorecard(review.getScorecard());
                aggWorksheet.setSubmission(review.getSubmission());
                PhasesHelper.copyComments(review, aggWorksheet, COMMENT_TYPES_TO_COPY, null);
                PhasesHelper.copyReviewItems(review, aggWorksheet, COMMENT_TYPES_TO_COPY);
            }

            // Adding empty comments
            CommentType submitterCommentType = LookupHelper.getCommentType(
                getManagerHelper().getReviewManager(), Constants.COMMENT_TYPE_SUBMITTER_COMMENT);
            Comment comment = new Comment();
            comment.setAuthor(winningSubmitter.getId());
            comment.setComment("");
            comment.setCommentType(submitterCommentType);
            aggWorksheet.addComment(comment);

            getManagerHelper().getReviewManager().createReview(aggWorksheet, operator);

            log.log(Level.DEBUG, new LogMessage(phase.getId(), operator, "create aggregate worksheet."));
        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Problem when persisting review", e);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Problem when retrieving winning submission.", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem when retrieving winning submission.", e);
        }
    }

    /**
     * returns true if aggregation worksheets exists.
     * @param phase phase to check.
     * @return whether aggregation worksheets exists.
     * @throws PhaseHandlingException if an error occurs when retrieving/saving
     *             data.
     */
    private boolean isAggregationWorksheetPresent(Phase phase) throws PhaseHandlingException {
        Review review = PhasesHelper.getWorksheet(getManagerHelper(), phase.getId());
        return (review != null && review.isCommitted());
    }
}
