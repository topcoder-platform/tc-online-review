/*
 * Copyright (C) 2005 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The extend from FinalFixPhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 * <p>
 * Version 2.1 changes note:
 * <ul>
 * <li>Ignore multiple uploads exception.
 * </ul>
 * </p>
 * @author TCSASSEMBLER
 * @version 2.1
 */
public class PRFinalFixPhaseHandler extends FinalFixPhaseHandler {

    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();

    /**
    * Used for checking against multiple uploads error message.
    */
    private final String MULTIPLE_UPLOAD_MESSAGE = "There cannot be multiple final fix uploads";

    /**
     * Create a new instance of FinalFixPhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
    public PRFinalFixPhaseHandler() throws ConfigurationException {
        super();
    }

    /**
     * Create a new instance of FinalFixPhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRFinalFixPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. Just call super method. Will ignore multiple uploads
     * exception.
     *
     * @param phase The input phase to check.
     *
     * @return True if the input phase can be executed, false otherwise.
     *
     * @throws PhaseNotSupportedException if the input phase type is not "Final Fix" type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        try {
          return super.canPerform(phase);
        } catch (PhaseHandlingException e) {
            if (MULTIPLE_UPLOAD_MESSAGE.equals(e.getMessage())) {
                return OperationCheckResult.SUCCESS;
            }
            throw e;
        }
    }

    /**
     * Provides additional logic to execute a phase. this extension will update placed, final_score
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

        prHelper.processFinalFixPR(phase.getProject().getId(), toStart, operator);
    }

}
