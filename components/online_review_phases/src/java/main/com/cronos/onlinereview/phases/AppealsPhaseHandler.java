/*
 * Copyright (C) 2009-2012 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.phases.logging.LogMessage;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email sending. This class handles the
 * appeals phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The appeals phase can start as soon as the dependencies are met and can stop when the following conditions met:
 * </p>
 * <ul>
 * <li>The dependencies are met</li>
 * <li>The period has passed.</li>
 * </ul>
 * <p>
 * There is no additional logic for executing this phase.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * Version 1.1 (Appeals Early Completion Release Assembly 1.0) Change notes:
 * <ol>
 * <li>Added support for Early Appeals Completion.</li>
 * </ol>
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
 * Version 1.6.1 changes note:
 * <ul>
 * <li>canPerform() method was updated to return not only true/false value, but additionally an explanation message
 * in case if operation cannot be performed.</li>
 * </ul>
 * </p>
 * @author tuenm, bose_java, pulky, waits, saarixx, microsky
 * @version 1.7.9
 */
public class AppealsPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.AppealsPhaseHandler";

    /**
     * This constant stores the log.
     * @since 1.1
     */
    private static final Log log = LogManager.getLog(AppealsPhaseHandler.class.getName());

    /**
     * Create a new instance of AppealsPhaseHandler using the default namespace
     * for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public AppealsPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AppealsPhaseHandler using the given namespace
     * for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public AppealsPhaseHandler(String namespace) throws ConfigurationException {
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
     * conditions:
     * </p>
     * <ul>
     * <li>The dependencies are met.</li>
     * </ul>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions:
     * </p>
     * <ul>
     * <li>The dependencies are met</li>
     * <li>The period has passed or appeals can be closed early (according to submitters input).</li>
     * </ul>
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
     *             &quot;Appeals&quot; type.
     * @throws PhaseHandlingException if there is any error while processing the
     *             phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_APPEALS);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        OperationCheckResult result;
        if (toStart) {
            // return true if all dependencies have stopped and start time has been reached.
            return PhasesHelper.checkPhaseCanStart(phase);
        } else {
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            } else {
                if (PhasesHelper.reachedPhaseEndTime(phase)) {
                    return OperationCheckResult.SUCCESS;
                }

                boolean canCloseAppealsEarly;
                try {
                    // check if all submitters agreed to close appeals phase early
                    canCloseAppealsEarly = PhasesHelper.canCloseAppealsEarly(
                                    getManagerHelper(), phase.getProject().getId());
                } catch (PhaseHandlingException phe) {
                    log.log(Level.ERROR, new LogMessage(phase.getId(), null,
                            "Fail to check if appeals can be closed early.", phe));
                    throw phe;
                }
                if (canCloseAppealsEarly) {
                    return OperationCheckResult.SUCCESS;
                } else {
                    return new OperationCheckResult(
                        "Phase end time is not yet reached and appeals cannot be closed early");
                }
            }
        }
    }

    /**
     * Provides additional logic to execute a phase. This method will be called
     * by start() and end() methods of PhaseManager implementations in Phase
     * Management component. This method can send email to a group of users
     * associated with timeline notification for the project. The email can be
     * send on start phase or end phase based on configuration settings.
     * <p>
     * Update in version 1.2, puts the submissions info/scores into the values map for email generation.
     * </p>
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not
     *             &quot;Appeals&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *             string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_APPEALS);
        PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();
        // puts the submissions info/scores into the values map
        values.put("SUBMITTER", PhasesHelper.getSubmitterValueArray(getManagerHelper(),
            phase.getProject().getId(), true));
        sendPhaseEmails(phase, values);
    }
}