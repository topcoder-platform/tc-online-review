/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.mock;

import com.topcoder.management.phase.PhaseValidationException;
import com.topcoder.management.phase.PhaseValidator;
import com.topcoder.project.phases.Phase;

/**
 * <p>
 * This class is used only for testing purposes.
 * </p>
 * @author sokol
 * @version 1.0
 * @since 1.1
 */
public class NullPhaseValidator implements PhaseValidator {

    /**
     * <p>
     * Creates an instance of NullPhaseValidator.
     * </p>
     */
    public NullPhaseValidator() {
    }

    /**
     * <p>
     * Always throws exception.
     * </p>
     * @param phase the phase to validate
     * @throws PhaseValidationException always
     */
    public void validate(Phase phase) throws PhaseValidationException {
        throw new PhaseValidationException("just for testing.");
    }
}
