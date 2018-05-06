/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator.builders;

import com.topcoder.management.review.scorecalculator.ScorecardMatrix;
import com.topcoder.management.review.scorecalculator.ScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.ScorecardStructureException;
import com.topcoder.management.scorecard.data.Scorecard;

/**
 * A mock implementation of the ScorecardMatrixBuilder interface that doesn't have a public zero argument
 * constructor.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class MockNoZeroArgCtorScorecardMatrixBuilder implements ScorecardMatrixBuilder {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Create a new MockNoZeroArgCtorScorecardMatrixBuilder.
     */
    private MockNoZeroArgCtorScorecardMatrixBuilder() {
        // Do nothing.
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ScorecardMatrixBuilder Methods

    /**
     * Empty implementation.
     *
     * @param   scorecard
     *          Ignored.
     *
     * @return  Always null.
     *
     * @throws  ScorecardStructureException
     *          Only declared to match interface.
     */
    public ScorecardMatrix buildScorecardMatrix(Scorecard scorecard) throws ScorecardStructureException {
        return null;
    }
}
