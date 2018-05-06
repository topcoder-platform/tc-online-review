/*
 * Copyright (C) 2010-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cronos.onlinereview.phases.logging.LogMessage;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.*;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * This class implements <code>PhaseHandler</code> interface to provide methods to check if a phase can be executed
 * and to add extra logic to execute a phase. It will be used by Phase Management component. It is configurable
 * using an input namespace. The configurable parameters include database connection and email sending parameters.
 * This class handles the specification review phase. If the input is of other phase types,
 * <code>PhaseNotSupportedException</code> will be thrown.
 * </p>
 * <p>
 * The specification review phase can start when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>Phase start date/time is reached (if specified)</li>
 * <li>If one active submission with &quot;Specification Submission&quot; type exists</li>
 * </ul>
 * </p>
 * <p>
 * The specification review phase can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>If the specification review is done by Specification Reviewer</li>
 * </ul>
 * </p>
 * <p>
 * The additional logic for executing this phase is: when specification Review phase is stopping, if the
 * specification review is rejected, another specification submission/review cycle is inserted.
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
 * @author saarixx, myxgyy, microsky
 * @version 1.8.0
 * @since 1.4
 */
public class SpecificationReviewPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.SpecificationReviewPhaseHandler";

    /**
     * Represents the logger for this class. Is initialized during class loading and never
     * changed after that.
     */
    private static final Log log = LogManager.getLog(SpecificationReviewPhaseHandler.class.getName());

    /**
     * Create a new instance of SpecificationReviewPhaseHandler using the default
     * namespace for loading configuration settings.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public SpecificationReviewPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of SpecificationReviewPhaseHandler using the given namespace
     * for loading configuration settings.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     * @param namespace
     *            the namespace to load configuration settings from.
     */
    public SpecificationReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. This method will check the phase
     * status to see what will be executed. This method will be called by canStart() and
     * canEnd() methods of PhaseManager implementations in Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions: the dependencies are met, Phase start date/time is reached (if specified) and If one active
     * submission with &quot;Specification Submission&quot; type exists.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions: The dependencies are met and the specification review is done by Specification Reviewer
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
     *             if the input phase type is not "Specification Review" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_SPECIFICATION_REVIEW);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        OperationCheckResult result;
        if (toStart) {
            result = PhasesHelper.checkPhaseCanStart(phase);
            if (!result.isSuccess()) {
                return result;
            }

            // can start if one active submission with "Specification Submission" type exists
            if (PhasesHelper.getSpecificationSubmission(getSpecSubmissionPhase(phase), getManagerHelper(), log) != null) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("Specification submission doesn't exist.");
            }
        } else {
            // Check phase dependencies
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            } else if (isSpecificationReviewCommitted(phase)) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("Specification review is not yet committed");
            }
        }
    }

    /**
     * Returns the dependency Specification Submission phase for the specified Specification Review phase.
     * @param specReviewPhase
     *            the Specification Review phase
     * @return Corresponding Specification Submission phase or null if not present.
     * @throws PhaseHandlingException
     *             if any error occurred.
     */
    private Phase getSpecSubmissionPhase(Phase specReviewPhase) throws PhaseHandlingException {
        for(Dependency dep : specReviewPhase.getAllDependencies()) {
            if (dep.getDependent() == specReviewPhase &&
                    dep.getDependency().getPhaseType().getName().equals(Constants.PHASE_SPECIFICATION_SUBMISSION)) {
                return dep.getDependency();
            }
        }
        return null;
    }

    /**
     * Checks whether the specification review is committed.
     * @param phase
     *            the current phase
     * @return true if the specification review is committed, false otherwise.
     * @throws PhaseHandlingException
     *             if any error occurred.
     */
    private boolean isSpecificationReviewCommitted(Phase phase) throws PhaseHandlingException {
        Submission submission = PhasesHelper.getSpecificationSubmission(getSpecSubmissionPhase(phase), getManagerHelper(), log);
        if (null == submission) {
            return false;
        }

        Review review = getSpecificationReview(phase);
        return (review != null) && review.isCommitted();
    }

    /**
     * Searches the Specification Review worksheet.
     * @param phase
     *            the phase.
     * @return the review or null if no review worksheet exists.
     * @throws PhaseHandlingException
     *             if there is any error occurred while searching reviews.
     */
    private Review getSpecificationReview(Phase phase) throws PhaseHandlingException {
        Submission submission = PhasesHelper.getSpecificationSubmission(getSpecSubmissionPhase(phase), getManagerHelper(), log);
        Review[] reviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(), phase.getId(), submission.getId());

        if (reviews.length > 1) {
            log.log(Level.ERROR, "Multiple specification reviews exist");
            throw new PhaseHandlingException("Illegal data : Multiple specification reviews.");
        }

        return (reviews.length == 0) ? null : reviews[0];
    }

    /**
     * Provides additional logic to execute a phase. This method will be called by start()
     * and end() methods of PhaseManager implementations in Phase Management component.
     * This method can send email to a group of users associated with timeline
     * notification for the project. The email can be send on start phase or end phase
     * base on configuration settings.
     * <p>
     * If the input phase status is Scheduled, then it will get the number of specification reviewers assigned.
     * </p>
     * <p>
     * If the input phase status is Open, then it will perform the following additional logic to stop the phase: if
     * the specification review is rejected, another specification submission/review cycle is inserted.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not &quot;Specification Review&quot; type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_SPECIFICATION_REVIEW);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();

        if (toStart) {
            values.put("N_SPECIFICATION_REVIEWERS", getSpecificationReviewerNumber(phase));
        } else {
            values.put("RESULT", checkSpecificationReview(phase, operator) ? "rejected" : "approved");
        }

        sendPhaseEmails(phase, values);
    }

    /**
     * <p>
     * Get the specification reviewer number of the project.
     * </p>
     * @param phase
     *            the current Phase
     * @return the number of specification reviewer
     * @throws PhaseHandlingException
     *             if any error occurs
     */
    private int getSpecificationReviewerNumber(Phase phase) throws PhaseHandlingException {
        return PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
            new String[] {Constants.ROLE_SPECIFICATION_REVIEWER }, phase.getId()).length;
    }

    /**
     * This method is called from perform method when the phase is stopping. It checks if
     * the specification review is rejected, and inserts another specification
     * submission/review cycle.
     * @param phase
     *            phase instance.
     * @param operator
     *            operator name
     * @return if pass the specification review of not
     * @throws PhaseHandlingException
     *             if an error occurs when retrieving/saving data.
     */
    private boolean checkSpecificationReview(Phase phase, String operator) throws PhaseHandlingException {
        ManagerHelper managerHelper = getManagerHelper();

        Submission submission = PhasesHelper.getSpecificationSubmission(getSpecSubmissionPhase(phase), managerHelper, log);

        if (null == submission) {
            log.log(Level.ERROR, "The specification submission does not exist.");
            throw new PhaseHandlingException("No specification submission exists.");
        }

        boolean rejected = wasSpecificationReviewRejected(phase);

        if (rejected) {
            // update specification submission status
            try {
                // Lookup submission status id for "Failed Review" status
                SubmissionStatus status = LookupHelper.getSubmissionStatus(
                    managerHelper.getUploadManager(), Constants.SUBMISSION_STATUS_FAILED_REVIEW);
                submission.setSubmissionStatus(status);
                managerHelper.getUploadManager().updateSubmission(submission, operator);
            } catch (UploadPersistenceException e) {
                log.log(Level.ERROR, new LogMessage(phase.getId(), null,
                        "Persistence layer error when updating submission status.", e));
                throw new PhaseHandlingException("Fails to update submission.", e);
            }

            // use helper method to insert Specification submission/Specification review phases
            int currentPhaseIndex = insertSpecSubmissionSpecReview(phase, operator);

            long specReviewPhaseId = phase.getProject().getAllPhases()[currentPhaseIndex + 2].getId();

            Resource[] resources = PhasesHelper.searchResourcesForRoleNames(managerHelper,
                    new String[] {Constants.ROLE_SPECIFICATION_REVIEWER}, phase.getId());

            if (resources.length == 0) {
                throw new PhaseHandlingException("Unable to find Specification Reviewer resource.");
            }

            PhasesHelper.copyReviewerResource(resources[0], managerHelper,
                    Constants.ROLE_SPECIFICATION_REVIEWER, specReviewPhaseId, operator);
        }

        return rejected;
    }

    /**
     * Finds out if the specification review is rejected. Note if the specification review
     * is found being rejected, the extra info will be reset here.
     * @param phase
     *            the phase.
     * @return true if the specification review is rejected, false otherwise.
     * @throws PhaseHandlingException
     *             if any error occurred.
     */
    private boolean wasSpecificationReviewRejected(Phase phase) throws PhaseHandlingException {
        Review review = getSpecificationReview(phase);

        if (null == review) {
            log.log(Level.ERROR, "The review for the specification does not exist.");
            throw new PhaseHandlingException("The review for the specification does not exist.");
        }

        if (!review.isCommitted()) {
            log.log(Level.ERROR, "The review is not committed.");
            throw new PhaseHandlingException("The review is not committed.");
        }

        Comment[] comments = review.getAllComments();

        Comment comment = null;
        for (Comment currentComment : comments) {
            if (Constants.COMMENT_TYPE_SPECIFICATION_REVIEW_COMMENT.equals(currentComment.getCommentType().getName())) {
                comment = currentComment;
                // use the first found one without checking how many of them exist
                break;
            }
        }

        if (null == comment) {
            log.log(Level.ERROR, "No comment with [Specification Review Comment] type.");
            throw new PhaseHandlingException("No comment with [Specification Review Comment] type.");
        }

        String value = (String) comment.getExtraInfo();
        if (Constants.COMMENT_VALUE_REJECTED.equalsIgnoreCase(value)) {
            // when the submission is rejected, we need to insert another specification
            // submission/specification review cycle,
            // Extra info for Specification Review Comment must be cleared.
            comment.resetExtraInfo();
            return true;
        } else if (!Constants.COMMENT_VALUE_APPROVED.equalsIgnoreCase(value)
            && !Constants.COMMENT_VALUE_ACCEPTED.equalsIgnoreCase(value)) {
            log.log(Level.ERROR, "Invalid comment[" + value + "],"
                    + " comment can either be Approved, Accepted or Rejected.");
            throw new PhaseHandlingException(
                "Comment can either be Approved, Accepted or Rejected.");
        }

        return false;
    }

    /**
     * Inserts a specification submission and specification review phases.
     * @param currentPhase
     *            the current phase after which new phases must be inserted.
     * @param operator
     *            the operator name.
     * @return the index of the current phase.
     * @throws PhaseHandlingException
     *             if any error occurs.
     * @since 1.4
     */
    int insertSpecSubmissionSpecReview(Phase currentPhase, String operator) throws PhaseHandlingException {
        PhaseManager phaseManager = getManagerHelper().getPhaseManager();

        PhaseType specSubmissionPhaseType = LookupHelper.getPhaseType(phaseManager,
                Constants.PHASE_SPECIFICATION_SUBMISSION);
        PhaseType specReviewPhaseType = LookupHelper.getPhaseType(phaseManager, Constants.PHASE_SPECIFICATION_REVIEW);
        PhaseStatus phaseStatus = LookupHelper.getPhaseStatus(phaseManager, Constants.PHASE_STATUS_SCHEDULED);

        Project project = currentPhase.getProject();

        // use helper method to create the new phases
        int currentPhaseIndex = PhasesHelper.createNewPhases(project, currentPhase, new PhaseType[] {
                specSubmissionPhaseType, specReviewPhaseType }, new Long[] {null, null}, phaseStatus, true);

        // Copy phase attributes for the new spec review phase
        Phase newSpecReview = project.getAllPhases()[currentPhaseIndex + 2];
        if (newSpecReview.getPhaseType().getId() == currentPhase.getPhaseType().getId()) {
            @SuppressWarnings("unchecked")
            Set<Serializable> currentPhaseAttributeKeys = (Set<Serializable>) currentPhase.getAttributes().keySet();
            for (Serializable attributeName : currentPhaseAttributeKeys) {
                newSpecReview.setAttribute(attributeName, currentPhase.getAttribute(attributeName));
            }
        }

        // save the phases
        try {
            phaseManager.updatePhases(project, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        }

        return currentPhaseIndex;
    }
}