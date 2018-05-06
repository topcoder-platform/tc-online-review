/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.validation;

import com.topcoder.management.phase.PhaseValidator;
import com.topcoder.project.phases.Phase;

/**
 * <p>
 * A simple validator for phases, which ensures that all the required fields are actually present.
 * This is a sanity check validation, which ensures that all the required fields have been
 * initialized with some values. This basically corresponds to the non-Nullable fields for table
 * phase in the database. So here with this validator we ensure that the table contract is
 * fulfilled. DefaultPhaseValidation is a thread safe utility-like class with no state.
 * </p>
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
public class DefaultPhaseValidator implements PhaseValidator {

    /**
     * <p>
     * Empty constructor .
     * </p>
     * @poseidon-object-id [I171dc60m10bd62d608fmm6f98]
     */
    public DefaultPhaseValidator() {
        // your code here
    }

    /**
     * <p>
     * Validate the input phase object.
     * </p>
     * <p>
     * Implementation details We will check if all of the elements of a Phase instance are set
     * except for: - fixed start time, - actual start time - actual end time which are optional, as
     * they correspond to the Nullable columns in the database schema for phase table. We ensure
     * that the rest of the elements have been set to non-null values (for references only)
     * </p>
     * <p>
     * Exception Handling #throw PhaseValidationException if validation fails #throw
     * IllegalArgumentException if phase input is null.
     * </p>
     * @param phase phase instance to validate
     */
    public void validate(Phase phase) {
        // your code here
    }
}
