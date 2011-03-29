/*
 * Copyright (C) 2010 - 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * <p>A handle for <code>Milestone Screening</code> phase implementing the additional phase processing logic specific to
 * <code>Online Review</code> application.</p>
 *
 * <p>
 * Version 1.1 (Online Review Replatforming Release 2) Change notes:
 *   <ol>
 *     <li>Update {@link #perform(Phase, String)} to populate milestone screener payments after the phase is closed.</li>
 *     <li>This class is refactor to use <code>ScreeningResultNotification</code> to send screening result notification emails.</li>
 *   </ol>
 * </p>
 * 
 * @author isv, TCSASSEMBER
 * @version 1.1 (Milestone Support assembly)
 */
public class PRMilestoneScreeningPhaseHandler extends MilestoneScreeningPhaseHandler {
    /**
     * Represents the <code>ScreeningResultNotification</code> instance to send notification email.
     * 
     * @since 1.1
     */
    private final ScreeningResultNotification notification;

    /**
     * <p>Constructs new <code>PRMilestoneScreeningPhaseHandler</code> instance. This implementation does nothing.</p>
     *
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRMilestoneScreeningPhaseHandler() throws ConfigurationException {
        super();
        notification = new ScreeningResultNotification(DEFAULT_NAMESPACE, "Milestone Submission", "Milestone Screening", "Failed Milestone Screening");
    }

    /**
     * <p>Constructs new <code>PRMilestoneScreeningPhaseHandler</code> instance initialized based on parameters from
     * specified configuration namespace.</p>
     *
     * @param namespace a <code>String</code> referencing the namespace for configuration parameters.
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRMilestoneScreeningPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
        notification = new ScreeningResultNotification(namespace, "Milestone Submission", "Milestone Screening", "Failed Milestone Screening");
    }

    /**
     * <p>Handles the current phase state transition. Sends emails on screening results to submitters.</p>
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
        
        Connection conn = this.createConnection();
        
        try {
            if (!toStart) {
                long projectId = phase.getProject().getId();
                try {
                    notification.sendEmailToSubmitters(getManagerHelper().getProjectManager().getProject(projectId));
                } catch (Exception e) {
                    throw new PhaseHandlingException("Failed to send email to submitters on Milestone Screening results", 
                                                     e);
                }
                
                try {
                    AutoPaymentUtil.populateReviewerPayments(projectId, conn, AutoPaymentUtil.MILESTONE_SCREENING_PHASE);
                } catch (SQLException e) {
                    throw new PhaseHandlingException("Failed to populate reviewer payment for Milestone Screening phase", 
                            e);
                }
            }
        } finally {
            PRHelper.close(conn);
        }
    }
}
