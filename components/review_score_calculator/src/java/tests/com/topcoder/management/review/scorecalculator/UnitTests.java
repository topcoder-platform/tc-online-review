/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilderTest;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculatorTest;
import com.topcoder.management.review.scorecalculator.calculators.ScaledScoreCalculatorTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class UnitTests extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the unit tests in this component.
     *
     * @return  A TestSuite for the unit tests in this component.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // Exception tests.
        suite.addTest(CalculationExceptionTest.suite());
        suite.addTest(ConfigurationExceptionTest.suite());
        suite.addTest(ReviewStructureExceptionTest.suite());
        suite.addTest(ScoreCalculatorExceptionTest.suite());
        suite.addTest(ScorecardStructureExceptionTest.suite());

        // Builder tests.
        suite.addTest(DefaultScorecardMatrixBuilderTest.suite());

        // Calculator tests.
        suite.addTest(BinaryScoreCalculatorTest.suite());
        suite.addTest(ScaledScoreCalculatorTest.suite());

        // Main tests.
        suite.addTest(CalculationManagerCtorCalculatorTest.suite());
        suite.addTest(CalculationManagerGetScoreTest.suite());
        suite.addTest(ScorecardMatrixTest.suite());
        suite.addTest(UtilTest.suite());

        // Demo
        suite.addTest(Demo.suite());

        return suite;
    }
}
