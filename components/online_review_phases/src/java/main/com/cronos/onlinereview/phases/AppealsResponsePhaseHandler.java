/*
 * Copyright (C) 2009-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email sending. This class handle the
 * appeals response phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The appeals response phase can start as soon as the dependencies are met and can stop when the following
 * conditions met: The dependencies are met and all appeals are resolved.
 * </p>
 * <p>
 * The additional logic for executing this phase is:<br>
 * When Appeals Response is stopping:
 * <ul>
 * <li>All submissions with failed review scores will be set to the status &quot;Failed Review&quot;.</li>
 * <li>Overall score for the passing submissions will be calculated and saved to the submitters&#146; resource
 * properties together with their placements. The property names are &quot;Final Score&quot; and
 * &quot;Placement&quot;.</li>
 * <li>The winner and runner-up will be populated in the project properties. The property names are &quot;Winner
 * External Reference ID&quot; and &quot;Runner-up External Reference ID&quot;.</li>
 * <li>Submissions that do not win will be set to the status &quot;Completed Without Win&quot;.</li>
 * </ul>
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * version 1.1 change notes: Modify the method <code>perform</code> to add a post-mortem phase if there is no
 * submission passes review after appeal response.
 * </p>
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager,
 * etc).</li>
 * <li>Support for more information in the email generated: for start/stop, puts the submissions info/scores into
 * the values map for email generation.</li>
 * </ul>
 * </p>
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #perform(Phase, String)} method to use updated
 * PhasesHelper#insertPostMortemPhase(com.topcoder.project.phases.Project, Phase, ManagerHelper, String) method for
 * creating <code>Post-Mortem</code> phase.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 Change notes:
 * <ol>
 * <li>Updated {@link #perform(Phase, String)} method to calculate the number of aggregators for project and bind
 * it to map used for filling email template.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.6 Change notes:
 * <ol>
 * <li>Refactor breakTies and getSubmissionById methods to PhaseHelper to reduce code redundancy.</li>
 * <li>Change to use getUploads().get(0) to retrieve the only upload for software competitions.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.7 (Online Review Replatforming Release 2 ) Change notes:
 * <ol>
 * <li>Change submission.getUploads() to submission.getUpload().</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.6.1 (Component development) changes note:
 * <ul>
 * <li>canPerform() method was updated to return not only true/false value, but additionally an explanation message
 * in case if operation cannot be performed.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Version 1.7.1 (BUGR-4779) Change notes:
 * <ol>
 * <li>Updated {@link #updateSubmissions(Phase, String, Map)} method to populate contest prizes for submissions.</li>
 * </ol>
 * </p>
 *
 * @author tuenm, bose_java, argolite, waits, isv, microsky, flexme
 * @version 1.7.10
 */
public class AppealsResponsePhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.AppealsResponsePhaseHandler";

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the default
     * namespace for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public AppealsResponsePhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the given
     * namespace for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public AppealsResponsePhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * <p>
     * Check if the input phase can be executed or not. This method will check the phase status to see what will be
     * executed. This method will be called by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions: The dependencies are met
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions: The dependencies are met and All appeals are resolved.
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
     *             &quot;Appeals Response&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_APPEALS_RESPONSE);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            // return true if all dependencies have stopped and start time has been reached.
            return PhasesHelper.checkPhaseCanStart(phase);
        } else {
            OperationCheckResult result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            }
            if (!allAppealsResolved(phase)) {
                return new OperationCheckResult("Not all appeals are resolved");
            }
            return OperationCheckResult.SUCCESS;
        }
    }

    /**
     * <p>
     * Provides additional logic to execute a phase. This method will be called by start() and end() methods of
     * PhaseManager implementations in Phase Management component. This method can send email to a group of users
     * associated with timeline notification for the project. The email can be send on start phase or end phase
     * base on configuration settings.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will do nothing.
     * </p>
     * <p>
     * If the input phase status is Open, then it will perform the following additional logic to stop the phase:
     * </p>
     * <ul>
     * <li>All submissions with failed review scores will be set to the status &quot;Failed Review&quot;.</li>
     * <li>Overall score for the passing submissions will be calculated and saved to the submitters&#146; resource
     * properties together with their placements. The property names are &quot;Final Score&quot; and
     * &quot;Placement&quot;.</li>
     * <li>The winner and runner-up will be populated in the project properties. The property names are
     * &quot;Winner External Reference ID&quot; and &quot;Runner-up External Reference ID&quot;.</li>
     * <li>Submissions that do not win will be set to the status &quot;Completed Without Win&quot;.</li>
     * </ul>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.1. change notes: Add a post-mortem phase after this phase if there is no submission passes review
     * after appeal response.
     * </p>
     * <p>
     * version 1.2. change notes: put the scores of the submissions/submitter handle into the values map. for each
     * submission, there will be a sub-map, all in a list with key 'SUBMITTER'.
     * </p>
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not
     *             &quot;Appeals Response&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *             string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_APPEALS_RESPONSE);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        Map<String, Object> values = new HashMap<String, Object>();

        if (toStart) {
            // for start phase, puts the submission info/initial score
            values.put("SUBMITTER", PhasesHelper.getSubmitterValueArray(
                getManagerHelper(), phase.getProject().getId(), false));
        } else {
            // it is going to calculate the final score for every submission
            // and put the scores into the values after calculation
            updateSubmissions(phase, operator, values);
            
            Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                    phase.getProject().getId(), Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);

            boolean allSubmissionsFailed = false;
            if (subs == null || subs.length == 0) {
                // if there is no active submissions after appeal response, insert the post-mortem phase
                PhasesHelper.insertPostMortemPhase(phase.getProject(), phase, getManagerHelper(), operator);
                allSubmissionsFailed = true;
            }

            Phase aggregationPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_AGGREGATION, true, false);
            if (aggregationPhase != null) {
                Resource[] aggregators = getAggregators(aggregationPhase);
                values.put("NEED_AGGREGATOR", aggregators.length == 0 ? 1 : 0);
            } else {
                values.put("NEED_AGGREGATOR", 0);
            }
            values.put("NO_REVIEW_PASS", allSubmissionsFailed ? 1 : 0);
        }

        sendPhaseEmails(phase, values);
    }

    /**
     * <p>
     * Gets the list of resources assigned <code>Aggregator</code> role.
     * </p>
     * @param aggregationPhase a <code>Phase</code> providing the details for <code>Aggregation</code> phase.
     * @return a <code>Resource</code> array listing the resources granted <code>Aggregator</code> role.
     * @throws PhaseHandlingException if an unexpected error occurs while accessing the data store.
     * @since 1.4
     */
    private Resource[] getAggregators(Phase aggregationPhase) throws PhaseHandlingException {
        Resource[] aggregators;
        aggregators = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
            new String[] {Constants.ROLE_AGGREGATOR }, aggregationPhase.getId());
        return aggregators;
    }

    /**
     * This method is called from perform method when phase is stopping. It does
     * the following:
     * <ul>
     * <li>all submissions with failed review scores are set to the status Failed Review.</li>
     * <li>Overall score for the passing submissions are calculated and saved.</li>
     * <li>The winner and runner-up are populated in the project properties.</li>
     * <li>Submissions that do not win are set to the status Completed Without Win.</li>
     * </ul>
     * <p>
     * version 1.1. change notes: Change return type from void to boolean, returning true indicates there is at
     * least one submission passes review after appeal response, false otherwise.
     * </p>
     * <p>
     * version 1.2. change notes: put the scores of the submissions/submitter handle into the values map. for each
     * submission, there will be a sub-map, all in a list with key 'SUBMITTER'.
     * </p>
     * @param phase the phase instance.
     * @param operator operator name.
     * @param values the values map
     * @throws PhaseHandlingException if there was an error updating data.
     */
    private void updateSubmissions(Phase phase, String operator, Map<String, Object> values)
        throws PhaseHandlingException {

        try {
            // locate previous review phase
            Phase reviewPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_REVIEW, false, true);

            Submission[] subs = PhasesHelper.updateSubmissionsResults(getManagerHelper(), reviewPhase,
                operator, false, true);

            // Order submissions by placement for the notification messages
            Arrays.sort(subs, new Comparator<Submission>() { 
                public int compare(Submission sub1, Submission sub2) {
                   return (int) (sub1.getPlacement() - sub2.getPlacement());
                } 
            });

            // For each submission, get the submitter and its scores
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            DecimalFormat df = new DecimalFormat("#.##");
            for (Submission submission : subs) {
                Map<String, Object> infos = new HashMap<String, Object>();
                Resource submitter = getManagerHelper().getResourceManager().getResource(submission.getUpload().getOwner());
                infos.put("SUBMITTER_HANDLE", PhasesHelper.notNullValue(submitter.getProperty(PhasesHelper.HANDLE)));
                infos.put("SUBMITTER_PRE_APPEALS_SCORE", df.format(submission.getInitialScore()));
                infos.put("SUBMITTER_POST_APPEALS_SCORE", df.format(submission.getFinalScore()));
                infos.put("SUBMITTER_RESULT", submission.getPlacement());
                result.add(infos);
            }
            values.put("SUBMITTER", result);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem with resource persistence", e);
        }
    }

    /**
     * This method returns true if all the appeals have been responded to.
     * @param phase phase instance.
     * @return true if all appeals are resolved, false otherwise.
     * @throws PhaseHandlingException if an error occurs when retrieving data.
     */
    private boolean allAppealsResolved(Phase phase) throws PhaseHandlingException {
        // Find appeals : Go back to the nearest Review phase
        Phase reviewPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_REVIEW, false, false);
        if (reviewPhase == null) {
            return false;
        }
        long reviewPhaseId = reviewPhase.getId();

        // Get all reviews
        Review[] reviews = PhasesHelper.searchReviewsForPhase(
            getManagerHelper(), reviewPhaseId, null);

        // for each review
        for (Review review : reviews) {
            int appealCount = 0;
            int responseCount = 0;

            Comment[] comments = review.getAllComments();

            for (Comment comment : comments) {
                String commentType = comment.getCommentType().getName();

                if (Constants.COMMENT_TYPE_APPEAL.equals(commentType)) {
                    appealCount++;
                } else if (Constants.COMMENT_TYPE_APPEAL_RESPONSE.equals(commentType)) {
                    responseCount++;
                }
            }

            Item[] items = review.getAllItems();

            for (Item item : items) {
                comments = item.getAllComments();

                for (Comment comment : comments) {
                    String commentType = comment.getCommentType().getName();

                    if (Constants.COMMENT_TYPE_APPEAL.equals(commentType)) {
                        appealCount++;
                    } else if (Constants.COMMENT_TYPE_APPEAL_RESPONSE.equals(commentType)) {
                        responseCount++;
                    }
                }
            }

            // if appeals count does not match response count, return false.
            if (appealCount != responseCount) {
                return false;
            }
        }

        return true;
    }
}
