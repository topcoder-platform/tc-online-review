/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.onlinereview.migration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(DataMigratorUnitTests.class);
        suite.addTestSuite(ScenarioSelectDataToTransform.class);
        suite.addTestSuite(ScenarioTransform.class);
        suite.addTestSuite(ScenarioDataMapping.class);
        suite.addTestSuite(ScenarioStoreTransformData.class);
        suite.addTestSuite(ScenarioDisplayActivePage.class);
        suite.addTestSuite(ScenarioDisplayCatalogPage.class);
        suite.addTestSuite(ScenarioDisplayScorecardPage.class);
        suite.addTestSuite(ScenarioDisplayContestStatusPage.class);
        
        // dw_load unit test
        // Notes need contains share module class path
        // suite.addTestSuite(TCLoadTCSTests.class);

        // caculate rating unit test
        // Notes need contains web module class path
        // suite.addTestSuite(RatingQubitsUnitTests.class);
        // suite.addTestSuite(ReliabilityRatingUnitTests.class);
        return suite;
    }
}
