/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 * @author Thinfox, stevenfrog
 * @version 1.1
 */
public class AccuracyTests extends TestCase {
    /**
     * <p>
     * Aggragates all accuracy tests.
     * </p>
     * @return test suite aggragating all accuracy tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(DefaultPhaseManagerAccTests.class);
        suite.addTestSuite(DefaultPhaseValidatorTests.class);
        suite.addTestSuite(HandleRegistryInfoTests.class);
        suite.addTestSuite(PhaseOperationEnumTests.class);
        suite.addTestSuite(PhaseStatusEnumTests.class);

        suite.addTestSuite(ConfigurationExceptionAccTests.class);
        suite.addTestSuite(PhaseValidationExceptionAccTests.class);
        suite.addTestSuite(PhasePersistenceExceptionAccTests.class);
        suite.addTestSuite(PhaseManagementExceptionAccTests.class);

        return suite;
    }
}