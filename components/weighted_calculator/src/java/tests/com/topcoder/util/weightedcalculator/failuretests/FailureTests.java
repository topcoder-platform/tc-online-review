package com.topcoder.util.weightedcalculator.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all failure test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(InfiniteLoopCase.suite());
        suite.addTest(CycleCase.suite());
        suite.addTest(ClobberCase.suite());
        suite.addTest(StrangeValuesCase.suite());


        return suite;
    }

}
