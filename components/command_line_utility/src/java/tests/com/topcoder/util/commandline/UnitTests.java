package com.topcoder.util.commandline;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all unit test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(ErrorTests.class));
        suite.addTest(new TestSuite(OtherUnitTests.class));
        suite.addTest(new TestSuite(SpecificUnitTest.class));
        suite.addTest(new TestSuite(SpecificUnitTestDelimiter.class));
        suite.addTest(new TestSuite(SpecificUnitTestIgnore.class));
        suite.addTest(new TestSuite(SwitchTests.class));
        return suite;
    }

}
