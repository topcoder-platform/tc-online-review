/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Funtional test suite on <b>Online Review Scorecard Admin</b>.
 * </p>
 * @author TCSTESTER
 * @version 1.0
 */
public class ScorecardAdminTestSuite extends TestCase {
    /**
     * <p>
     * Aggregates all the test scenarios.
     * </p>
     * @return the aggregated test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ScenariosViewScorecard.class);
        suite.addTestSuite(ScenariosCreateScorecard.class);
        suite.addTestSuite(ScenariosCopyScorecard.class);
        suite.addTestSuite(ScenariosSetScorecardStatus.class);
        suite.addTestSuite(ScenariosEditScorecard.class);
        return suite;
    }
}
