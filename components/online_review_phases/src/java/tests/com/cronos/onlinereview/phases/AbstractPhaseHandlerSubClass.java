/*
 * Copyright (C) 2009-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * Dummy sub-class of AbstractPhaseHandler for testing purpose.
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change return type of canPerform from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author bose_java, microsky
 * @version 1.6.1
 */
class AbstractPhaseHandlerSubClass extends AbstractPhaseHandler {

    /**
     * will call super constructor.
     * @param namespace namespace.
     * @throws ConfigurationException if config error occurs.
     */
    public AbstractPhaseHandlerSubClass(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * dummy implementation always returning false.
     * @param phase input phase to check.
     * @return always not successful OperationCheckResult.
     * @throws PhaseHandlingException if phase could not be processed.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        return new OperationCheckResult("msg");
    }

    /**
     * Dummy do-nothing implementation.
     * @param phase phase to check.
     * @param operator operator.
     * @throws PhaseHandlingException if phase could not be processed.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
    }
}
