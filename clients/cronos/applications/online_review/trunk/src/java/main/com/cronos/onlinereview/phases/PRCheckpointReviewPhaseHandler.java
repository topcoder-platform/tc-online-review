/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * <p>A handle for <code>Checkpoint Review</code> phase implementing the additional phase processing logic specific to
 * <code>Online Review</code> application.</p>
 *
 * <p>
 * Version 1.1 (Online Review - Project Payments Integration Part 3 v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #perform(Phase, String)} method to call PaymentsHelper.processAutomaticPayments to process
 *     the project payments.</li>
 *     <li>Removed <code>prHelper</code> field.</li>
 *   </ol>
 * </p>
 *
 * @author flexme, flexme
 * @version 1.1 (BUGR-4778)
 */
public class PRCheckpointReviewPhaseHandler extends CheckpointReviewPhaseHandler {
    /**
     * <p>Constructs new <code>PRCheckpointReviewPhaseHandler</code> instance. This implementation does nothing.</p>
     *
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRCheckpointReviewPhaseHandler() throws ConfigurationException {
        super();
    }
    
    /**
     * <p>Constructs new <code>PRCheckpointReviewPhaseHandler</code> instance initialized based on parameters from
     * specified configuration namespace.</p>
     *
     * @param namespace a <code>String</code> referencing the namespace for configuration parameters.
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRCheckpointReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }
    
    /**
     * <p>Handles the current phase state transition. It will populate the submitter's checkpoint prize.</p>
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
        if (!toStart) {
            long projectId = phase.getProject().getId();
            PaymentsHelper.processAutomaticPayments(projectId, operator);
        }
    }
}
