/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.project.phases.Phase;

/**
 * <p>A handle for <code>Checkpoint Screening</code> phase implementing the additional phase processing logic specific to
 * <code>Online Review</code> application.</p>
 *
 * <p>
 * Version 1.1 (Online Review Replatforming Release 2) Change notes:
 *   <ol>
 *     <li>Update {@link #perform(Phase, String)} to populate checkpoint screener payments after the phase is closed.</li>
 *     <li>This class is refactor to use <code>ScreeningResultNotification</code> to send screening result notification emails.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2 (Online Review - Project Payments Integration Part 3 v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #perform(Phase, String)} method to call PaymentsHelper.processAutomaticPayments to process
 *     the project payments.</li>
 *     <li>Removed <code>prHelper</code> field.</li>
 *   </ol>
 * </p>
 * 
 * @author isv, flexme
 * @version 1.2 (Checkpoint Support assembly)
 */
public class PRCheckpointScreeningPhaseHandler extends CheckpointScreeningPhaseHandler {
    /**
     * Represents the <code>ScreeningResultNotification</code> instance to send notification email.
     * 
     * @since 1.1
     */
    private final ScreeningResultNotification notification;

    /**
     * <p>Constructs new <code>PRCheckpointScreeningPhaseHandler</code> instance. This implementation does nothing.</p>
     *
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRCheckpointScreeningPhaseHandler() throws ConfigurationException {
        super();
        notification = new ScreeningResultNotification(DEFAULT_NAMESPACE, "Checkpoint Submission", "Checkpoint Screening", "Failed Checkpoint Screening");
    }

    /**
     * <p>Constructs new <code>PRCheckpointScreeningPhaseHandler</code> instance initialized based on parameters from
     * specified configuration namespace.</p>
     *
     * @param namespace a <code>String</code> referencing the namespace for configuration parameters.
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRCheckpointScreeningPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
        notification = new ScreeningResultNotification(namespace, "Checkpoint Submission", "Checkpoint Screening", "Failed Checkpoint Screening");
    }

    /**
     * <p>Handles the current phase state transition. Sends emails on screening results to submitters.</p>
     *
     * @param phase    The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not &quot;Checkpoint Screening&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    @Override
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        ProjectManager projectManager = getManagerHelper().getProjectManager();
        long projectId = phase.getProject().getId();

        if (!toStart && PRHelper.isStudioProject(projectManager, projectId)) {
                
            try {
                notification.sendEmailToSubmitters(projectManager.getProject(projectId));
            } catch (Exception e) {
                throw new PhaseHandlingException("Failed to send email to submitters on Checkpoint Screening results", e);
            }
                
            PaymentsHelper.processAutomaticPayments(projectId, operator);
        }
    }
}
