/*
 * Copyright (C) 2011 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The extend from AppealsResponsePhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRAppealResponsePhaseHandler extends AppealsResponsePhaseHandler {
    
    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();

    /**
     * Represents the <code>ReviewResultNotification</code> instance to send notification email.
     */
    private final ReviewResultNotification notification;

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
    public PRAppealResponsePhaseHandler() throws ConfigurationException {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRAppealResponsePhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
        notification = new ReviewResultNotification(namespace);
    }

    /**
     * Provides additional logic to execute a phase. this extension will update valid_submission_ind, submit_timestamp
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
        // Only will perform while submission phase is ended
        prHelper.processAppealResponsePR(getManagerHelper(), phase, toStart, operator);

        try {
            //send winner email as appropriate here
            if (!toStart) {
                notification.sendMailForWinners(getManagerHelper().getProjectManager().getProject(phase.getProject().getId()));
            }
        } catch (Throwable e) {
            throw new PhaseHandlingException(e.getMessage(), e);
        }
    }

}
