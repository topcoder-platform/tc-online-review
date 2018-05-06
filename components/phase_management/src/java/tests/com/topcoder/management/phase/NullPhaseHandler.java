/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import com.topcoder.project.phases.Phase;

/**
 * A simple phase handler that allows all operations to be performed.
 * @author RachaelLCook, sokol
 * @version 1.1
 */
public class NullPhaseHandler implements PhaseHandler {

    /**
     * Used by some subclasses to track whether or not the correct method invocation was made.
     */
    private boolean passed = false;

    /**
     * Creates an instance of NullPhaseHandler.
     */
    public NullPhaseHandler() {
        // do nothing
    }

    /**
     * Returns whether or not the test should pass.
     * @return whether or not the test should pass
     */
    boolean getPassed() {
        return passed;
    }

    /**
     * Sets whether or not the test should pass.
     * @param passed whether or not the test should pass
     */
    void setPassed(boolean passed) {
        this.passed = passed;
    }

    /**
     * Always returns <code>OperationCheckResult.SUCCESS</code>.
     * @param phase the phase to perform
     * @return <code>OperationCheckResult.SUCCESS</code>
     */
    public OperationCheckResult canPerform(Phase phase) {
        return OperationCheckResult.SUCCESS;
    }

    /**
     * Does nothing.
     * @param phase the phase
     * @param operator the operator
     */
    public void perform(Phase phase, String operator) {
    }
}
