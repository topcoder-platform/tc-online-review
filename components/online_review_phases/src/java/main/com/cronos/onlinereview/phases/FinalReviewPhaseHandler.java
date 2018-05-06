/*
 * Copyright (C) 2009-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection, email sending. This class handle the final
 * review phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The final review phase can start as soon as the dependencies are met and can stop when the following conditions
 * met: the dependencies are met and the final review is committed by the final reviewer.
 * </p>
 * <p>
 * The additional logic for executing this phase is: when Final Review phase is stopping, if the final review is
 * rejected, another final fix/review cycle is inserted.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * version 1.1 change notes: Add an approval phase when the final review is approved. The logic is modified in
 * method <code>checkFinalReview</code>
 * </p>
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager,
 * etc).</li>
 * <li>Support for more information in the email generated: for start, find the number of final reviewers. for
 * stop, add the result..</li>
 * </ul>
 * </p>
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #checkFinalReview(Phase, String)} method to use corrected logic for creating
 * <code>Approval</code> phase.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 Change notes:
 * <ol>
 * <li>Updated not to use ContestDependencyAutomation.</li>
 * <li>Updated {@link #perform(Phase, String)} method to calculate the number of approvers for project and bind it
 * to map used for filling email template.</li>
 * </ol>
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
 * @author tuenm, bose_java, argolite, waits, saarixx, myxgyy, isv, microsky
 * @version 1.8.0
 */
public class FinalReviewPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.FinalReviewPhaseHandler";

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
     * Create a new instance of FinalReviewPhaseHandler using the default
     * namespace for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public FinalReviewPhaseHandler() throws ConfigurationException {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of FinalReviewPhaseHandler using the given
     * namespace for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public FinalReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
        finalFixDuration = Long.parseLong(PhasesHelper.getPropertyValue(FinalFixPhaseHandler.class.getName(), "FinalFixPhaseDuration", true));
        studioFinalFixDuration = Long.parseLong(PhasesHelper.getPropertyValue(FinalFixPhaseHandler.class.getName(),
                                                                              "StudioFinalFixPhaseDuration", true));
    }

    /**
     * Check if the input phase can be executed or not. This method will check
     * the phase status to see what will be executed. This method will be called
     * by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions: the dependencies are met
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions: The dependencies are met and the final review is committed by the final reviewer.
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
     *             &quot;Final Review&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_FINAL_REVIEW);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            // return true if all dependencies have stopped and start time has been reached
            return PhasesHelper.checkPhaseCanStart(phase);
        } else {
            OperationCheckResult result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            }

            if (isFinalWorksheetCommitted(phase)) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("Final review scorecard is not yet committed");
            }
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
     * If the input phase status is Open, then it will perform the following additional logic to stop the phase: if
     * the final review is rejected, another final fix/review cycle is inserted.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.2: for start, find the number of final reviewers. for stop, add the result.
     * </p>
     * <p>
     * Change in 1.4: Updated not to use ContestDependencyAutomation.
     * </p>
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not
     *             &quot;Final Review&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *             string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_FINAL_REVIEW);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();
        if (toStart) {
            // for start, put the final reviewer number
            values.put("N_FINAL_REVIEWERS", getFinalReviewerNumber(phase));
        } else {
            // checkFinalReview is changed in version 1.1 to add
            // an approval phase after final review is approved
            values.put("RESULT", checkFinalReview(phase, operator) ? "rejected" : "approved");

            Phase approvalPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_APPROVAL, true, false);
            if (approvalPhase != null) {
                int approvers = getApproverNumbers(approvalPhase);
                int approverNum = getRequiredReviewersNumber(approvalPhase);
                values.put("N_APPROVERS", approvers);
                values.put("N_REQUIRED_APPROVERS", approverNum);
                values.put("NEED_APPROVER", approverNum <= approvers ? 0 : 1);
            } else {
                values.put("NEED_APPROVER", 0);
            }
        }

        sendPhaseEmails(phase, values);
    }

    /**
     * <p>
     * Gets the number of required reviewers for specified phase. If specified phase does not have such a value set
     * then 1 s returned.
     * </p>
     * @param phase a <code>Phase</code> providing the details for current phase.
     * @return an <code>int</code> providing number of required reviewers for specified phase.
     * @throws PhaseHandlingException if an unexpected error occurs while accessing the data store.
     * @since 1.3.1
     */
    private int getRequiredReviewersNumber(Phase phase) throws PhaseHandlingException {
        int approverNum = 1;
        if (phase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER) != null) {
            approverNum = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
        }
        return approverNum;
    }

    /**
     * <p>
     * Get the final reviewer number of the project.
     * </p>
     * @param phase the current Phase
     * @return the number of final reviewer
     * @throws PhaseHandlingException if any error occurs
     */
    private int getFinalReviewerNumber(Phase phase) throws PhaseHandlingException {
        return PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
            new String[] {Constants.ROLE_FINAL_REVIEWER }, phase.getId()).length;
    }

    /**
     * Find the number of 'Approver' of the phase.
     * @param phase the current Phase
     * @return the number of approver
     * @throws PhaseHandlingException if any error occurs
     * @since 1.4
     */
    private int getApproverNumbers(Phase phase) throws PhaseHandlingException {
        return PhasesHelper.searchProjectResourcesForRoleNames(getManagerHelper(),
            new String[] {Constants.ROLE_APPROVER }, phase.getProject().getId()).length;

    }

    /**
     * This method checks if final worksheet exists and has been committed.
     * @param phase phase to check.
     * @return true if final worksheet exists and has been committed, false
     *         otherwise.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     */
    private boolean isFinalWorksheetCommitted(Phase phase) throws PhaseHandlingException {
        Review finalWorksheet = PhasesHelper.getWorksheet(getManagerHelper(), phase.getId());
        return ((finalWorksheet != null) && finalWorksheet.isCommitted());
    }

    /**
     * This method is called from perform method when the phase is stopping. It
     * checks if the final review is rejected, and inserts another final
     * fix/review cycle.
     * <p>
     * Version 1.1 change notes: Add an approval phase if the final review is approved.
     * </p>
     * <p>
     * Version 1.2: add the return value.
     * </p>
     * <p>
     * Change in 1.4: Updated not to use ContestDependencyAutomation.
     * </p>
     * @param phase phase instance.
     * @param operator operator name
     * @return if pass the final review of not
     * @throws PhaseHandlingException if an error occurs when retrieving/saving
     *             data.
     */
    private boolean checkFinalReview(Phase phase, String operator) throws PhaseHandlingException {
        try {
            ManagerHelper managerHelper = getManagerHelper();
            Review finalWorksheet = PhasesHelper.getWorksheet(managerHelper, phase.getId());

            // check for approved/rejected comments.
            Comment[] comments = finalWorksheet.getAllComments();
            boolean rejected = false;

            for (Comment comment : comments) {
                String value = (String) comment.getExtraInfo();

                if (comment.getCommentType().getName().equals(Constants.COMMENT_TYPE_FINAL_REVIEW_COMMENT)) {
                    if (Constants.COMMENT_VALUE_APPROVED.equalsIgnoreCase(value)
                            || Constants.COMMENT_VALUE_APPROVED.equalsIgnoreCase(value)) {
                        continue;
                    } else if (Constants.COMMENT_VALUE_REJECTED.equalsIgnoreCase(value)) {
                        rejected = true;

                        break;
                    } else {
                        throw new PhaseHandlingException("Comment can either be Approved or Rejected.");
                    }
                }
            }

            ProjectManager projectManager = managerHelper.getProjectManager();
            com.topcoder.management.project.Project project =
                    projectManager.getProject(phase.getProject().getId());

            if (rejected) {
                // Extra info for Final Review Comment must be cleared.
                for (Comment comment : comments) {
                    if (comment.getCommentType().getName().equals(Constants.COMMENT_TYPE_FINAL_REVIEW_COMMENT)) {
                        comment.resetExtraInfo();
                    }
                }

                Project currentPrj = phase.getProject();

                // use helper method to insert final fix/final review phases
                int currentPhaseIndex;
                if (PhasesHelper.isStudio(project)) {
                    currentPhaseIndex = PhasesHelper.insertFinalFixAndFinalReview(
                            phase, managerHelper.getPhaseManager(), operator, studioFinalFixDuration);
                } else {
                    currentPhaseIndex = PhasesHelper.insertFinalFixAndFinalReview(
                            phase, managerHelper.getPhaseManager(), operator, finalFixDuration);
                }

                // get the id of the newly created final review phase
                long finalReviewPhaseId = currentPrj.getAllPhases()[currentPhaseIndex + 2].getId();

                Resource[] resources = PhasesHelper.searchResourcesForRoleNames(managerHelper,
                        new String[] {Constants.ROLE_FINAL_REVIEWER}, phase.getId());

                if (resources.length == 0) {
                    throw new PhaseHandlingException("Unable to find Final Reviewer resource.");
                }

                PhasesHelper.copyReviewerResource(resources[0], managerHelper,
                        Constants.ROLE_FINAL_REVIEWER, finalReviewPhaseId, operator);
            } else {
                // Newly added in version 1.1
                // the final review is approved, add approval phase if it does not exist yet
                // and if there is corresponding property value
                Phase approvalPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_APPROVAL, true, false);

                String approvalRequired = (String) project.getProperty("Approval Required");

                if (approvalPhase == null && approvalRequired != null && approvalRequired.equalsIgnoreCase("true")) {
                    insertApprovalPhase(phase.getProject(), phase, operator);
                }
            }
            return rejected;

        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Problem when reading phases", e);
        }
    }

    /**
     * <p>
     * Inserts a Approval phase into persistence.
     * </p>
     * @param currentPrj
     *            current project.
     * @param currentPhase
     *            current phase to insert a approval phase.
     * @param operator
     *            the operator name.
     * @throws PhaseHandlingException
     *             if any error occurs.
     * @since 1.3
     */
    void insertApprovalPhase(Project currentPrj, Phase currentPhase,
                                    String operator) throws PhaseHandlingException {
        PhaseManager phaseManager = getManagerHelper().getPhaseManager();

        // create phase type and status objects
        PhaseType approvalPhaseType = LookupHelper.getPhaseType(phaseManager, Constants.PHASE_APPROVAL);
        PhaseStatus phaseStatus = LookupHelper.getPhaseStatus(phaseManager, Constants.PHASE_STATUS_SCHEDULED);

        try {
            // Create new Approval phase
            String approvalPhaseDuration = PhasesHelper.getPropertyValue(ApprovalPhaseHandler.class.getName(),
                    "ApprovalPhaseDuration", true);

            // Find lst Approval phase (if any)
            Phase lastApprovalPhase = PhasesHelper.locatePhase(currentPhase, Constants.PHASE_APPROVAL, false, false);

            PhasesHelper.createNewPhases(currentPrj, currentPhase, new PhaseType[] {approvalPhaseType },
                    new Long[] {Long.parseLong(approvalPhaseDuration) * Constants.HOUR }, phaseStatus, false);

            // Set the number of required reviewers for Approval phase to default value or
            // to value taken
            // from previous Approval phase if it exists
            String approvalPhaseDefaultReviewerNumber;
            String approvalPhaseDefaultScorecardID;

            if (lastApprovalPhase == null) {
                approvalPhaseDefaultReviewerNumber = PhasesHelper.getPropertyValue(ApprovalPhaseHandler.class
                        .getName(), "ApprovalPhaseDefaultReviewersNumber", true);

                approvalPhaseDefaultScorecardID = PhasesHelper.getPropertyValue(ApprovalPhaseHandler.class.getName(),
                        "ApprovalPhaseDefaultScorecardID", true);
            } else {
                approvalPhaseDefaultReviewerNumber = (String) lastApprovalPhase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
                approvalPhaseDefaultScorecardID = (String) lastApprovalPhase.getAttribute(Constants.PHASE_CRITERIA_SCORECARD_ID);
            }

            Phase approvalPhase = PhasesHelper.getApprovalPhase(currentPrj);
            approvalPhase.setAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER, approvalPhaseDefaultReviewerNumber);
            approvalPhase.setAttribute(Constants.PHASE_CRITERIA_SCORECARD_ID, approvalPhaseDefaultScorecardID);

            phaseManager.updatePhases(currentPrj, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        } catch (ConfigurationException e) {
            throw new PhaseHandlingException("Problem when reading configuration file", e);
        }
    }
}
