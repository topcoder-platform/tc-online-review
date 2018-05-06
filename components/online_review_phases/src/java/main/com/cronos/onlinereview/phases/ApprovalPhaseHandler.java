/*
 * Copyright (C) 2009-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ValidationException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email sending. This class handle the
 * approval phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The approval phase can start as soon as the dependencies are met and can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>The approval scorecards are committed;</li>
 * <li>All approval scorecards must have passing scores.</li>
 * </ul>
 * </p>
 * <p>
 * There is no additional logic for executing this phase.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * Version 1.1 changes note:
 * <li>Adds another criteria in <code>canPerform</code> to judge whether the phase can stop : At least the required
 * number of Approver resources have filled in a scorecard (use the Reviewer Number phase criteria).</li>
 * <li>Modify the method <code>perform</code> to insert another final fix/final review round when the approval
 * scorecard is rejected.</li>
 * </p>
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager,
 * etc).</li>
 * <li>Support for more information in the email generated: for start, add the approver info. for stop, add the
 * result to the value map.</li>
 * </ul>
 * </p>
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #checkScorecardsCommitted(Phase)} method to use appropriate logic for searching for review
 * scorecards tied to project but not to phase type.</li>
 * </ol>
 * </p>
 * <p>
 * Change in 1.4: Updated not to use ContestDependencyAutomation.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>The return changes from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * <p>
 * Version 1.6.2 change notes:
 * <ul>
 * <li>Insert final fix phase with configured duration.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Version 1.7.13 Change notes:
 *   <ol>
 *     <li>Added {@link #studioFinalFixDuration} property.</li>
 *     <li>Updated {@link #perform(Phase, String)} method to set-up duration for <code>Final Fix</code> phases for
 *     <code>Studio</code> contests differently from <code>Software</code> contests.</li>
 *   </ol>
 * </p>
 * 
 * @author tuenm, bose_java, argolite, waits, saarixx, myxgyy, microsky, isv
 * @version 1.7.13
 */
public class ApprovalPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default name space of this class. It is used in the
     * default constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.ApprovalPhaseHandler";

    /**
    * Represents duration of final fix phase to insert.
    */
    private final Long finalFixDuration;

    /**
     * <p>A <code>Long</code> providing the duration for <code>Final Fix</code> phase (in hours) for <code>Studio
     * </code> contests.</p>
     * 
     * @since 1.7.13
     */
    private final Long studioFinalFixDuration;
    
    /**
     * Create a new instance of ApprovalPhaseHandler using the default namespace
     * for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public ApprovalPhaseHandler() throws ConfigurationException {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of ApprovalPhaseHandler using the given namespace
     * for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public ApprovalPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
        finalFixDuration = Long.parseLong(PhasesHelper.getPropertyValue(FinalFixPhaseHandler.class.getName(), "FinalFixPhaseDuration", true));
        studioFinalFixDuration = Long.parseLong(PhasesHelper.getPropertyValue(FinalFixPhaseHandler.class.getName(), 
                                                                              "StudioFinalFixPhaseDuration", true));
    }

    /**
     * <p>
     * Check if the input phase can be executed or not. This method will check the phase status to see what will be
     * executed. This method will be called by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions: The dependencies are met.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met</li>
     * <li>The approval scorecards are committed;</li>
     * <li>All approval scorecards must have passing scores.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.1 change notes: Codes modified to check whether at least the required number of Approver resources
     * have filled in a scorecard (use the Reviewer Number phase criteria).
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
     *             &quot;Approval&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_APPROVAL);

        // Throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        OperationCheckResult result;
        if (toStart) {
            // Return true if all dependencies have stopped and start time has
            // been reached and there are configured number of approvers
            result = PhasesHelper.checkPhaseCanStart(phase);
            if (!result.isSuccess()) {
                return result;
            }

            int projectApproversCount = getApproverNumbers(phase);

            // Set the fallback value to 1
            int approverNum = 1;

            // Get the number of required approval (use reviewer number
            // criteria), if it's not set, fallback value is used
            if (phase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER) != null) {
                approverNum = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
            }

            // version 1.1 : Return true if approver number is met
            if (projectApproversCount >= approverNum) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("There are not enough approvers assigned for the project");
            }

        } else {
            // Check phase dependencies
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);

            // Return true if dependencies are met and minimum number of reviews committed or time has ended.
            if (!result.isSuccess()) {
                return result;
            }
            if (PhasesHelper.reachedPhaseEndTime(phase) || checkScorecardsCommitted(phase)) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult(
                    "Phase end time is not yet reached and not enough approval scorecards are committed");
            }
        }
    }

    /**
     * <p>
     * Provides additional logic to execute a phase. This method will be called by start() and end() methods of
     * PhaseManager implementations in Phase Management component. This method can send email to a group users
     * associated with timeline notification for the project. The email can be send on start phase or end phase
     * base on configuration settings.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.1 changes note: Add logic to insert final review / final fix when there is approval review
     * rejected.
     * </p>
     * <p>
     * Version 1.2 : for start, add the approver info. for stop, add the result to the value map.
     * </p>
     * <p>
     * Change in 1.4: Updated not to use ContestDependencyAutomation.
     * </p>
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not
     *             &quot;Approval&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *             string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_APPROVAL);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();

        if (toStart) {
            // find the number of approvers
            int approvers = getApproverNumbers(phase);
            int approverNum = 1;
            if (phase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER) != null) {
                approverNum = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
            }
            values.put("N_APPROVERS", approvers);
            values.put("N_REQUIRED_APPROVERS", approverNum);
            values.put("NEED_APPROVER", approverNum <= approvers ? 0 : 1);
        } else {
            // Check whether there is approval review rejected
            boolean rejected = checkScorecardsRejected(phase);
            values.put("RESULT", rejected ? "rejected" : "approved");

            ManagerHelper managerHelper = getManagerHelper();

            try {
                ProjectManager projectManager = managerHelper.getProjectManager();
                com.topcoder.management.project.Project project = projectManager.getProject(
                        phase.getProject().getId());

                if (rejected) {
                    // approval review rejected, insert final review/final fix phases
                    // find current phase index and also the lengths of 'final fix'
                    // and 'final review' phases.

                    boolean isStudio = PhasesHelper.isStudio(project);

                    // use helper method to insert final fix/final review phase
                    int currentPhaseIndex;
                    if (isStudio) {
                        currentPhaseIndex = PhasesHelper.insertFinalFixAndFinalReview(phase,
                                managerHelper.getPhaseManager(), operator, studioFinalFixDuration);
                    } else {
                        currentPhaseIndex = PhasesHelper.insertFinalFixAndFinalReview(phase,
                                managerHelper.getPhaseManager(), operator, finalFixDuration);
                    }

                    // get the id of the newly created final review phase
                    long finalReviewPhaseId = phase.getProject().getAllPhases()[currentPhaseIndex + 2].getId();

                    Resource oldResource;
                    if (isStudio) {
                        // For Studio contests the approver is copied as the Final Reviewer resource
                        Resource[] resources = PhasesHelper.searchProjectResourcesForRoleNames(managerHelper,
                                new String[] {Constants.ROLE_APPROVER}, project.getId());

                        if (resources.length == 0) {
                            throw new PhaseHandlingException("Unable to find Approver resource.");
                        }
                        oldResource = resources[0];
                    } else {
                        Phase previousFinalReviewPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_FINAL_REVIEW, false, true);
                        Resource[] resources = PhasesHelper.searchResourcesForRoleNames(managerHelper,
                                new String[] {Constants.ROLE_FINAL_REVIEWER}, previousFinalReviewPhase.getId());

                        if (resources.length == 0) {
                            throw new PhaseHandlingException("Unable to find Final Reviewer resource.");
                        }
                        oldResource = resources[0];
                    }

                    PhasesHelper.copyReviewerResource(oldResource, managerHelper,
                            Constants.ROLE_FINAL_REVIEWER, finalReviewPhaseId, operator);
                } else {
                    // Set project property indicating that it requires other fixes if necessary
                    boolean otherFixesRequired = checkOtherFixesRequired(phase);
                    if (otherFixesRequired) {
                        project.setProperty("Requires Other Fixes", "true");
                        projectManager.updateProject(project, "", operator);
                    }

                    // Send review feedback emails only when approval closes with "approved" status.
                    sendReviewFeedbackEmails(phase, values);
                }
            } catch (PhaseManagementException e) {
                throw new PhaseHandlingException("Problem when persisting phases", e);
            } catch (PersistenceException e) {
                throw new PhaseHandlingException("Problem when retrieving or updating project", e);
            } catch (ValidationException e) {
                throw new PhaseHandlingException("Problem when updating project", e);
            }
        }

        sendPhaseEmails(phase, values);
    }

    /**
     * Find the number of 'Approver' of the phase.
     * @param phase the current Phase
     * @return the number of approver
     * @throws PhaseHandlingException if any error occurs
     */
    private int getApproverNumbers(Phase phase) throws PhaseHandlingException {
        return PhasesHelper.searchProjectResourcesForRoleNames(getManagerHelper(),
            new String[] {Constants.ROLE_APPROVER }, phase.getProject().getId()).length;
    }

    /**
     * This method checks if all approval scorecards are committed.
     * @param phase the phase instance.
     * @return true if all approval scorecards are committed.
     * @throws PhaseHandlingException if an error occurs when retrieving data.
     * @since 1.1
     */
    private boolean checkScorecardsCommitted(Phase phase) throws PhaseHandlingException {
        Review[] approveReviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(),
                phase.getId(), null);

        // No review has been filled, return false
        if (approveReviews.length == 0) {
            return false;
        }

        // approverNum default to 1
        int approverNum = 1;
        if (phase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER) != null) {
            approverNum = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
        }

        // counter for committed reviews
        int commitedCount = 0;

        // Check approval scorecards are committed and return false if there is at least one uncommited scorecard
        for (Review approveReview : approveReviews) {
            if (approveReview.isCommitted()) {
                commitedCount++;
            }
        }

        // Check whether required number of reviews are all committed
        return commitedCount >= approverNum;
    }

    /**
     * Checks whether whether all the approval scorecards are approved. If any
     * approval scorecard is rejected, false will be returned, return true if
     * all are approved.
     * @param phase the input phase.
     * @return true if all approval scorecard are approved, false otherwise.
     * @throws PhaseHandlingException if any error occurs
     * @since 1.1
     */
    private boolean checkScorecardsRejected(Phase phase) throws PhaseHandlingException {
        try {
            Review[] approveReviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(),
                    phase.getId(), null);

            // check for approved/rejected comments.
            boolean rejected = false;
            for (Review approveReview : approveReviews) {
                if (!approveReview.isCommitted()) {
                    continue;
                }

                Comment[] comments = approveReview.getAllComments();

                for (Comment comment : comments) {
                    String value = (String) comment.getExtraInfo();
                    if (comment.getCommentType().getName().equals(Constants.COMMENT_TYPE_APPROVAL_REVIEW_COMMENT)) {
                        if (Constants.COMMENT_VALUE_APPROVED.equalsIgnoreCase(value)
                            || Constants.COMMENT_VALUE_ACCEPTED.equalsIgnoreCase(value)) {
                            continue;
                        } else if (Constants.COMMENT_VALUE_REJECTED.equalsIgnoreCase(value)) {
                            rejected = true;
                            break;
                        } else {
                            throw new PhaseHandlingException("Comment can either be Approved, Accepted or Rejected.");
                        }
                    }
                }
            }

            return rejected;

        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        }
    }

    /**
     * Checks whether whether all the approval scorecards are approved. If any
     * approval scorecard is rejected, false will be returned, return true if
     * all are approved.
     * @param phase the input phase.
     * @return true if all approval scorecard are approved, false otherwise.
     * @throws PhaseHandlingException if any error occurs
     * @since 1.1
     */
    private boolean checkOtherFixesRequired(Phase phase) throws PhaseHandlingException {
        try {
            Review[] approveReviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(),
                    phase.getId(), null);

            // check for approved/rejected comments.
            boolean required = false;
            for (Review approveReview : approveReviews) {
                if (!approveReview.isCommitted()) {
                    continue;
                }

                Comment[] comments = approveReview.getAllComments();

                for (Comment comment : comments) {
                    String value = (String) comment.getExtraInfo();
                    if (comment.getCommentType().getName().equals(Constants.COMMENT_TYPE_APPROVAL_REVIEW_COMMENT_OTHER_FIXES)) {
                        if (Constants.COMMENT_VALUE_REQUIRED.equalsIgnoreCase(value)) {
                            required = true;
                            break;
                        }
                    }
                }
            }
            return required;
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        }
    }
}
