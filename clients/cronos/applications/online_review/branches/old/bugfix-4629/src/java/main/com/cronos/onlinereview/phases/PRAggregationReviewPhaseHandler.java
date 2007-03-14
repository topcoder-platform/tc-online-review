/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The PRAggregationReviewPhaseHandler.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PRAggregationReviewPhaseHandler extends AggregationReviewPhaseHandler {
    /**
     * Create a new instance of AggregationReviewPhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
    public PRAggregationReviewPhaseHandler() throws ConfigurationException {
        super();
    }

    /**
     * Create a new instance of AggregationReviewPhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRAggregationReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Provides additional logic to execute a phase. this exetension will update placed, final_score
     * field of project_result table.</p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseNotSupportedException if the input phase type is not "Submission" type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (!toStart) {
            Connection conn = this.createConnection();
            try {
                processPR(phase.getProject().getId(), conn);
            } finally {
                PRHelper.close(conn);
            }
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    public static void processPR(long projectId, Connection conn) throws PhaseHandlingException {
        try {
            PRHelper.processPlacedFinalScore(projectId, conn);
        } catch(SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }
}
