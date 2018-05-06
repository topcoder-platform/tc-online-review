/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * A simple builder which constructs and returns a ScorecardMatrix instance for a given Scorecard.
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: All implementations of this interface are expected to be thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public interface ScorecardMatrixBuilder {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Interface Methods

    /**
     * Constructs and returns a ScorecardMatrix instance for the given Scorecard instance.
     *
     * @param   scorecard
     *          The Scorecard instance to build into a ScorecardMatrix instance.
     *
     * @return  The ScorecardMatrix instance constructed from the specified Scorecard.
     *
     * @throws  IllegalArgumentException
     *          The scorecard is a null reference.
     * @throws  ScorecardStructureException
     *          THe scorecard cannot be processed by the implementation.
     */
    public ScorecardMatrix buildScorecardMatrix(Scorecard scorecard) throws ScorecardStructureException;
}
