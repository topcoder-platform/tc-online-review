/*
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.scorecalculator.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all accuracy test cases.
 * </p>
 *
 * @author qiucx0161
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * aggregates all accuracy test cases.
     *
     * @return Test instance
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(TestScorecardStructureException.class);
        suite.addTestSuite(TestScorecardMatrix.class);
        suite.addTestSuite(TestScoreCalculatorException.class);
        suite.addTestSuite(TestScaledScoreCalculator.class);
        suite.addTestSuite(TestReviewStructureException.class);
        suite.addTestSuite(TestDefaultScorecardMatrixBuilder.class);
        suite.addTestSuite(TestConfigurationException.class);
        suite.addTestSuite(TestCalculationManager.class);
        suite.addTestSuite(TestCalculationException.class);

        suite.addTestSuite(TestBinaryScoreCalculator.class);
        suite.addTestSuite(TestAbstractScoreCalculator.class);

        return suite;
    }
}
