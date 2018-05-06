/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all accuracy test cases.</p>
 *
 * @author myxgyy
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * Gathers all accuracy tests together and return.
     *
     * @return all tests in one suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AbstractPhaseHandlerTestV13.class);
        suite.addTestSuite(ApprovalPhaseHandlerAccTest.class);
        suite.addTestSuite(FinalReviewPhaseHandlerAccTest.class);
        suite.addTestSuite(PostMortemPhaseHandlerAccTests.class);
        suite.addTestSuite(RegistrationPhaseHandlerAccTest.class);
        suite.addTestSuite(ScreeningPhaseHandlerAccTest.class);
        suite.addTestSuite(SubmissionPhaseHandlerAccTest.class);
        suite.addTestSuite(AppealsPhaseHandlerAccTest.class);
        suite.addTestSuite(AppealsResponsePhaseHandlerAccTest.class);
        suite.addTestSuite(AggregationPhaseHandlerAccTest.class);
        suite.addTestSuite(FinalFixPhaseHandlerAccTest.class);
        suite.addTestSuite(ReviewPhaseHandlerAccTest.class);
        suite.addTestSuite(PhaseStatusLookupUtilityAccTest.class);
        suite.addTestSuite(PhaseTypeLookupUtilityAccTest.class);
        suite.addTestSuite(ProjectInfoTypeLookupUtilityAccTest.class);
        suite.addTestSuite(ResourceRoleLookupUtilityAccTest.class);
        suite.addTestSuite(FinalReviewPhaseHandlerTestV12.class);
        suite.addTestSuite(RegistrationPhaseHandlerTestV12.class);
        suite.addTestSuite(SubmissionPhaseHandlerTestV12.class);
        suite.addTestSuite(ApprovalPhaseHandlerAccTestsV11.class);
        suite.addTestSuite(FinalReviewPhaseHandlerAccTestsV11.class);
        suite.addTestSuite(RegistrationPhaseHandlerAccTestsV11.class);
        suite.addTestSuite(SubmissionPhaseHandlerAccTestsV11.class);
        suite.addTestSuite(ApprovalPhaseHandlerTestV12.class);
        suite.addTestSuite(SubmissionTypeLookupUtilityAccuracyTests.class);
        suite.addTestSuite(SpecificationSubmissionPhaseHandlerAccuracyTests.class);
        suite.addTestSuite(SpecificationReviewPhaseHandlerAccuracyTests.class);

        return suite;
    }
}
