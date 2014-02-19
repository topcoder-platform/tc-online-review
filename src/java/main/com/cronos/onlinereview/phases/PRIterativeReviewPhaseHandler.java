/**
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The iterative review phase handler.
 * @author duxiaoyang
 * @version 1.0
 */
public class PRIterativeReviewPhaseHandler extends IterativeReviewPhaseHandler {

    /**
     * Used for pulling data to project_result table and filling payments.
     */
    private final PRHelper prHelper = new PRHelper();

    /**
     * Create a new instance of this class using the default namespace for loading configuration settings.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public PRIterativeReviewPhaseHandler() throws ConfigurationException {
        super();
    }

    /**
     * Create a new instance of this class using the given namespace for loading configuration settings.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings or required properties missing.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     */
    public PRIterativeReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Performs the phase operation.
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not "Iterative Review" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        prHelper.processIterativeReviewPR(getManagerHelper(), phase, operator, toStart);
    }
}
