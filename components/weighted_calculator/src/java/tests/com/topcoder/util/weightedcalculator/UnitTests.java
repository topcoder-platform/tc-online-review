package com.topcoder.util.weightedcalculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all unit test cases.</p>
 *
 * @author  WishingBone
 * @version 1.0
 * @version Copyright C 2002, TopCoder, Inc. All rights reserved
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(LineItemTestCase.class));
        suite.addTest(new TestSuite(SectionTestCase.class));
        suite.addTest(new TestSuite(MathMatrixTestCase.class));
        return suite;
    }

}
