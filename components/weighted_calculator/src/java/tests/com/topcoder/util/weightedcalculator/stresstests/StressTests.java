package com.topcoder.util.weightedcalculator.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all stress test cases.</p>
 *
 * @author TopCoder Software
 * @author adic
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(ManyLinesTestCase.suite());
        suite.addTest(ManySectionsTestCase.suite());
        suite.addTest(DeepImbricationTestCase.suite());
        suite.addTest(RandomStructureTestCase.suite());
        return suite;
    }

}
