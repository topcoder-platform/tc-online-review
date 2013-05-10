/**
 * Copyright (C) 2005 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The PRFinalFixPhaseHandler.
 *
 * <p>
 * Version 1.1 (Online Review - Project Payments Integration Part 3 v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #perform(Phase, String)} method to pass <code>operator</code> when calling
 *     RPHelper.processFinalFixPR.</li>
 *   </ol>
 * </p>
 *
 * @author brain_cn, flexme
 * @version 1.1
 */
public class PRFinalFixPhaseHandler extends FinalFixPhaseHandler {
    
    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private PRHelper prHelper = new PRHelper();
	
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
