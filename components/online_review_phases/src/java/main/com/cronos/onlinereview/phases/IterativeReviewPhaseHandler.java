/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;

/**
 * <p>
 * This class implements <code>PhaseHandler</code> interface to provide methods to check if a phase can be executed
 * and to add extra logic to execute a phase. It will be used by Phase Management component. It is configurable
 * using an input namespace. The configurable parameters include database connection and email sending parameters.
 * This class handles the Iterative Review phase. If the input is of other phase types,
 * <code>PhaseNotSupportedException</code> will be thrown.
 * </p>
 * <p>
 * The iterative review phase can start when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>Phase start date/time is reached (if specified)</li>
 * <li>If one or more active submissions with &quot;Contest Submission&quot; type exists</li>
 * </ul>
 * </p>
 * <p>
 * The iterative review phase can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>If the required number of iterative reviews is done for the current submission</li>
 * </ul>
 * </p>
 * <p>
 * The additional logic for executing this phase is: when iterative review phase is stopping, if the
 * iterative review is rejected, another iterative review phase is inserted.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>canPerform() method was updated to return not only true/false value, but additionally an explanation message
 * in case if operation cannot be performed</li>
 * </ul>
 * </p>
 * @author VolodymyrK
 * @version 1.8.0
 * @since 1.8.0
 */
public class IterativeReviewPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.IterativeReviewPhaseHandler";

    /**
     * Create a new instance of IterativeReviewPhaseHandler using the default
     * namespace for loading configuration settings.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public IterativeReviewPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of IterativeReviewPhaseHandler using the given namespace
     * for loading configuration settings.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     * @param namespace
     *            the namespace to load configuration settings from.
     */
    public IterativeReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. This method will check the phase
     * status to see what will be executed. This method will be called by canStart() and
     * canEnd() methods of PhaseManager implementations in Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions: the dependencies are met, Phase start date/time is reached (if specified) and if at least one active
     * submission with &quot;Submission&quot; type exists.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions: The dependencies are met and required number of iterative reviews is done for the current submission
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
     * @param phase
     *            The input phase to check.
     * @return the validation result indicating whether the associated operation can be performed, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseNotSupportedException
     *             if the input phase type is not "Iterative Review" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_ITERATIVE_REVIEW);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());        
        
        OperationCheckResult result;
        if (toStart) {
            result = PhasesHelper.checkPhaseCanStart(phase);
            if (!result.isSuccess()) {
                return result;
            } 
            
            Submission nextSubmission = PhasesHelper.getEarliestActiveSubmission(getManagerHelper().getUploadManager(),
                    phase.getProject().getId());
            
            // can start if there's an active submission to be reviewed
            if (nextSubmission != null) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("There's no pending submission");
            }
        } else {
            // Check phase dependencies
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            }
            return allReviewsDone(phase);
        }
    }

    /**
     * This method checks if the next submission has one review scorecard from each reviewer for the phase and
     * returns the validation result.
     * @param phase
     *            the phase instance.
     * @return the validation result indicating whether all the reviews are done, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseHandlingException
     *             if there was an error retrieving data.
     */
    private OperationCheckResult allReviewsDone(Phase phase) throws PhaseHandlingException {
        Submission nextSubmission = PhasesHelper.getEarliestActiveSubmission(getManagerHelper().getUploadManager(),
                phase.getProject().getId());
        Review[] reviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(), phase.getId(), nextSubmission.getId());

        int committedCount = 0;
        for (Review review : reviews) {
            if (review.isCommitted()) {
                committedCount++;
            }
        }

        if (committedCount == 0) {
            return new OperationCheckResult("No committed reviews found");
        }

        Resource[] reviewers = PhasesHelper.searchProjectResourcesForRoleNames(getManagerHelper(),
                new String[] {Constants.ROLE_ITERATIVE_REVIEWER}, phase.getProject().getId());
        int requiredReviewers = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
        if (committedCount != requiredReviewers) {
            return new OperationCheckResult("Number of committed reviews doesn't match the required one");
        }
        if (committedCount != reviewers.length) {
            return new OperationCheckResult("Not all reviewers submitted their reviews");
        }

        return OperationCheckResult.SUCCESS;
     }

    /**
     * Provides additional logic to execute a phase. This method will be called by start()
     * and end() methods of PhaseManager implementations in Phase Management component.
     * This method can send email to a group of users associated with timeline
     * notification for the project. The email can be send on start phase or end phase
     * base on configuration settings.
     * <p>
     * If the input phase status is Open, then it will perform the following additional logic to stop the phase: if
     * the iterative review is rejected, another iterative review phase is inserted.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not &quot;Iterative Review&quot; type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_ITERATIVE_REVIEW);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();

        if (toStart) {
            // for start, puts the reviewer/submission info
            putPhaseStartInfoValues(phase, values);
        } else {
            boolean passedReview = updateSubmissionScores(phase, operator, values);
            values.put("PASSED", passedReview ? 1 : 0);
            if (!passedReview) {
                insertIterativeReview(phase, operator);
            }
        }

        sendPhaseEmails(phase, values);
    }

    /**
     * <p>
     * Puts the start review phase information about submission and reviewers info to the value map.
     * </p>
     * @param phase
     *            the current Phase, not null
     * @param values
     *            the values map
     * @throws PhaseHandlingException
     *             if any error occurs
     */
    private void putPhaseStartInfoValues(Phase phase, Map<String, Object> values) throws PhaseHandlingException {
        Submission nextSubmission = PhasesHelper.getEarliestActiveSubmission(getManagerHelper().getUploadManager(),
                phase.getProject().getId());

        values.put("SUBMITTER", PhasesHelper.constructSubmitterValues(new Submission[] {nextSubmission},
                getManagerHelper().getResourceManager(), false));

        Resource[] reviewers = PhasesHelper.searchProjectResourcesForRoleNames(getManagerHelper(),
                new String[] {Constants.ROLE_ITERATIVE_REVIEWER}, phase.getProject().getId());

        int requiredReviewers = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
        values.put("N_REQUIRED_REVIEWERS", requiredReviewers);
        values.put("N_REVIEWERS", reviewers.length);
        values.put("NEED_REVIEWER", reviewers.length >= requiredReviewers ? 0 : 1);
    }

    /**
     * This method calculates scores, statuses and prizes of the pending submission and saves it.
     * It is called from perform method when phase is stopping.
     * @param phase
     *            phase instance.
     * @param operator
     *            the operator name.
     * @param values
     *            map
     * @return True if passed review.
     * @throws PhaseHandlingException
     *             in case of error when retrieving/updating data.
     */
    private boolean updateSubmissionScores(Phase phase, String operator, Map<String, Object> values)
            throws PhaseHandlingException {

        try {
            Submission[] subs = PhasesHelper.updateSubmissionsResults(getManagerHelper(), phase, operator, true, true);

            // add the submission result to the values map
            DecimalFormat df = new DecimalFormat("#.##");
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (Submission sub : subs) {
                Map<String, Object> infos = new HashMap<String, Object>();
                Resource submitter = getManagerHelper().getResourceManager().getResource(sub.getUpload().getOwner());
                infos.put("SUBMITTER_HANDLE", PhasesHelper.notNullValue(submitter.getProperty(PhasesHelper.HANDLE)));
                infos.put("SUBMITTER_SCORE", df.format(sub.getInitialScore()));
                result.add(infos);
            }
            values.put("SUBMITTER", result);

            return subs[0].getSubmissionStatus().getName().equals(Constants.SUBMISSION_STATUS_ACTIVE);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when looking up resource for the submission.", e);
        }
    }

    /**
     * Inserts a new Iterative Review phase.
     * @param currentPhase
     *            the current phase after which new phase must be inserted.
     * @param operator
     *            the operator name.
     * @throws PhaseHandlingException
     *             if any error occurs.
     */
    void insertIterativeReview(Phase currentPhase, String operator) throws PhaseHandlingException {
        PhaseManager phaseManager = getManagerHelper().getPhaseManager();

        PhaseType iterativeReviewPhaseType = LookupHelper.getPhaseType(phaseManager, Constants.PHASE_ITERATIVE_REVIEW);
        PhaseStatus phaseStatus = LookupHelper.getPhaseStatus(phaseManager, Constants.PHASE_STATUS_SCHEDULED);

        // find current phase index
        com.topcoder.project.phases.Project project = currentPhase.getProject();

        // use helper method to create the new phases
        int currentPhaseIndex = PhasesHelper.createNewPhases(project, currentPhase,
                new PhaseType[] {iterativeReviewPhaseType }, new Long[] {null}, phaseStatus, true);

        // Copy phase attributes for the new iterative review phase
        Phase newIterativeReview = project.getAllPhases()[currentPhaseIndex + 1];
        if (newIterativeReview.getPhaseType().getId() == currentPhase.getPhaseType().getId()) {
            @SuppressWarnings("unchecked")
            Set<Serializable> currentPhaseAttributeKeys = (Set<Serializable>) currentPhase.getAttributes().keySet();
            for (Serializable attributeName : currentPhaseAttributeKeys) {
                newIterativeReview.setAttribute(attributeName, currentPhase.getAttribute(attributeName));
            }
        }

        try {
            phaseManager.updatePhases(project, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        }
    }
}