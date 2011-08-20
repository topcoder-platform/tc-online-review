/*
 * Copyright (C) 2010 - 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * <p>A handle for <code>Milestone Review</code> phase implementing the additional phase processing logic specific to
 * <code>Online Review</code> application.</p>
 * 
 * @author flexme
 * @version 1.0 (BUGR-4778)
 */
public class PRMilestoneReviewPhaseHandler extends MilestoneReviewPhaseHandler {
    /**
     * <p>Constructs new <code>PRMilestoneReviewPhaseHandler</code> instance. This implementation does nothing.</p>
     *
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRMilestoneReviewPhaseHandler() throws ConfigurationException {
        super();
    }
    
    /**
     * <p>Constructs new <code>PRMilestoneReviewPhaseHandler</code> instance initialized based on parameters from
     * specified configuration namespace.</p>
     *
     * @param namespace a <code>String</code> referencing the namespace for configuration parameters.
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRMilestoneReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }
    
    /**
     * <p>Handles the current phase state transition. It will populate the submitter's milestone prize.</p>
     *
     * @param phase    The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not &quot;Milestone Screening&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    @Override
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        if (!toStart) {
            
            Connection conn = this.createConnection();
            
            try {
                long projectId = phase.getProject().getId();
                AutoPaymentUtil.populateSubmitterPayments(projectId, conn);
            } catch (SQLException e) {
                throw new PhaseHandlingException("Failed to populate submitter payment for Milestone Review phase", e);
            } finally {
                PRHelper.close(conn);
            }
        }
    }
}
