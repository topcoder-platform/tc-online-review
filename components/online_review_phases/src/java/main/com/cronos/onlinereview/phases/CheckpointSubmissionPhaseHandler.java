/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by <b>Phase Management</b> component. It is configurable using
 * an input namespace. The configurable parameters include database connection, email sending and the required
 * number of submissions that pass screening. This class handle the checkpoint submission phase. If the input is of
 * other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The checkpoint submission phase can be started using when the following conditions:
 * <ul>
 * <li>the dependencies are met</li>
 * <li>Its start time is reached.</li>
 * </ul>
 * </p>
 * <p>
 * The checkpoint submission phase can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>The phase's end time is reached.</li>
 * <li>If there are no checkpoint submissions or if manually checkpoint screening is required, the number of
 * checkpoint submissions that have passed manual checkpoint screening meets the required number.</li>
 * </ul>
 * </p>
 * <p>
 * Note that checkpoint screening phase can be started during a checkpoint submission phase.
 * </p>
 * <p>
 * There is no additional logic for executing this phase.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>The return changes from boolean to OperationCheckResult.</li>
 * <li>Removed private methods arePassedSubmissionsEnough().</li>
 * </ul>
 * </p>
 * @author FireIce, saarixx, TCSDEVELOPER, microsky
 * @version 1.6.1
 * @since 1.6
 */
public class CheckpointSubmissionPhaseHandler extends AbstractPhaseHandler {
    /**
     * <p>
     * Represents the default namespace of this class. It is used in the default constructor to load configuration
     * settings.
     * </p>
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.CheckpointSubmissionPhaseHandler";

    /**
     * <p>
     * Create a new instance of CheckpointSubmissionPhaseHandler using the default namespace for loading
     * configuration settings.
     * </p>
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public CheckpointSubmissionPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * <p>
     * Create a new instance of CheckpointSubmissionPhaseHandler using the given namespace for loading configuration
     * settings.
     * </p>
     * @param namespace
     *            the namespace to load configuration settings from.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings or required properties missing.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     */
    public CheckpointSubmissionPhaseHandler(String namespace) throws ConfigurationException {
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
     * <ul>
     * <li>the dependencies are met</li>
     * <li>Its start time is reached.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met</li>
     * <li>The phase's end time is reached.</li>
     * <li>If there are no checkpoint submissions or if manually checkpoint screening is required, the number of
     * checkpoint submissions that have passed manual checkpoint screening meets the required number.</li>
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
     * @param phase
     *            The input phase to check.
     * @return the validation result indicating whether the associated operation can be performed, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseNotSupportedException
     *             if the input phase type is not &quot;Checkpoint Submission&quot; type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_CHECKPOINT_SUBMISSION);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            return PhasesHelper.checkPhaseCanStart(phase);
        } else {
            OperationCheckResult result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            }
            if (!PhasesHelper.reachedPhaseEndTime(phase)) {
                return new OperationCheckResult("Phase end time is not yet reached");
            }

            return OperationCheckResult.SUCCESS;
        }
    }

    /**
     * Provides additional logic to execute a phase. This method will be called by start() and end() methods of
     * PhaseManager implementations in Phase Management component. This method can send email to a group of users
     * associated with timeline notification for the project. The email can be send on start phase or end phase
     * base on
     * configuration settings.
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not &quot;Checkpoint Submission&quot; type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_CHECKPOINT_SUBMISSION);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();

        // retrieve the submissions information for sending mail.
        populateProperties(phase, values);
        sendPhaseEmails(phase, values);
    }

    /**
     * <p>
     * This method populates properties for the email generation.
     * </p>
     * @param phase
     *            the phase.
     * @param values
     *            the values map to hold the information for email generation.
     * @throws PhaseHandlingException
     *             if any error occurs during processing.
     */
    void populateProperties(Phase phase, Map<String, Object> values)
        throws PhaseHandlingException {
        Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                phase.getProject().getId(), Constants.SUBMISSION_TYPE_CHECKPOINT_SUBMISSION);

        // for stop phase, we are going to support more information.
        if (values != null) {
            values.put("N_SUBMITTERS", subs.length);
            values.put("SUBMITTER", PhasesHelper.constructSubmitterValues(subs,
                    getManagerHelper().getResourceManager(), false));
        }
    }
}