/*
 * Copyright (C) 2010-2012 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * This class implements <code>PhaseHandler</code> interface to provide methods to check if a phase can be executed
 * and to add extra logic to execute a phase. It will be used by Phase Management component. It is configurable
 * using an input namespace. The configurable parameters include database connection and email sending parameters.
 * This class handles the specification submission phase. If the input is of other phase types,
 * <code>PhaseNotSupportedException</code> will be thrown.
 * </p>
 * <p>
 * The specification review phase can start when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>Phase start date/time is reached (if specified)</li>
 * <li>This is the first phase in the project and all parent projects are completed</li>
 * </ul>
 * </p>
 * <p>
 * The specification review phase can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>If one active submission with &quot;Specification Submission&quot; type exists</li>
 * </ul>
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
 * @version 1.7.9
 * @since 1.4
 */
public class SpecificationSubmissionPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE =
        "com.cronos.onlinereview.phases.SpecificationSubmissionPhaseHandler";

    /**
     * Represents the logger for this class. Is initialized during class loading and never
     * changed after that.
     */
    private static final Log log = LogManager.getLog(SpecificationSubmissionPhaseHandler.class.getName());

    /**
     * Create a new instance of SpecificationSubmissionPhaseHandler using the default
     * namespace for loading configuration settings.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public SpecificationSubmissionPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of SpecificationSubmissionPhaseHandler using the given
     * namespace for loading configuration settings.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     * @param namespace
     *            the namespace to load configuration settings from.
     */
    public SpecificationSubmissionPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. This method will check the phase status to see what will be
     * executed. This method will be called by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions: the dependencies are met, Phase start date/time is reached (if specified), if this is not the
     * first phase in the project or all parent projects are completed.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions: The dependencies are met and if one active submission with &quot;Specification Submission&quot;
     * type exists.
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
     * @throws PhaseNotSupportedException if the input phase type is not &quot;Specification Submission&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_SPECIFICATION_SUBMISSION);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        OperationCheckResult result;
        if (toStart) {
            result = PhasesHelper.checkPhaseCanStart(phase);
            if (!result.isSuccess()) {
                return result;
            }

            // This is NOT the first phase in the project or all parent projects are completed
            if (!PhasesHelper.isFirstPhase(phase) ||
                PhasesHelper.areParentProjectsCompleted(phase, this.getManagerHelper(), log)) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("Not all parent projects are completed");
            }
        } else {
            // Check phase dependencies
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);

            if (result.isSuccess()) {
                if (PhasesHelper.getSpecificationSubmission(phase, getManagerHelper(), log) != null) {
                    return OperationCheckResult.SUCCESS;
                } else {
                    return new OperationCheckResult("Specification submission doesn't exist");
                }
            }
            // dependencies not met
            return result;
        }
    }

    /**
     * Provides additional logic to execute a phase. This method will be called by start()
     * and end() methods of PhaseManager implementations in Phase Management component.
     * This method can send email to a group of users associated with timeline
     * notification for the project. The email can be send on start phase or end phase
     * base on configuration settings.
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not &quot;Specification Submission&quot;
     *             type.
     * @throws PhaseHandlingException
     *             if phase status is neither &quot;Scheduled&quot; nor &quot;Open&quot;
     *             or there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_SPECIFICATION_SUBMISSION);
        PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        sendPhaseEmails(phase);
    }
}