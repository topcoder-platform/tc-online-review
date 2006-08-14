/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Aggregates all unit tests.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * Aggregates all unit tests.
     * </p>
     * 
     * @return the aggregated test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(UnitTestListScorecardsAction.class);
        suite.addTestSuite(UnitTestViewScorecardAction.class);
        suite.addTestSuite(UnitTestNewScorecardAction.class);
        suite.addTestSuite(UnitTestEditScorecardAction.class);
        suite.addTestSuite(UnitTestCopyScorecardAction.class);
        suite.addTestSuite(UnitTestSaveScorecardAction.class);
        return suite;
    }
}
