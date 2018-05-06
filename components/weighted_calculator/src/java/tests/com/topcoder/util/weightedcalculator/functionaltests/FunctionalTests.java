package com.topcoder.util.weightedcalculator.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all functional test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class FunctionalTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(CreateMatrixTestCase.suite());
        suite.addTest(ConfigureMatrixTestCase.suite());
        suite.addTest(QueryResultsForMatrixTestCase.suite());
        suite.addTest(ExploreMatrixTestCase.suite());
        return suite;
    }

}
