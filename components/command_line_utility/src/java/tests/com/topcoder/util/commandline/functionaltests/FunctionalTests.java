package com.topcoder.util.commandline.functionaltests;

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
        suite.addTest(new TestSuite(SingleValueTests.class));
        suite.addTest(new TestSuite(MultipleValueTests.class));
        suite.addTest(new TestSuite(ParameterTests.class));
        suite.addTest(new TestSuite(ProgrammerTests.class));
        suite.addTest(new TestSuite(DelimiterTests.class));
        suite.addTest(new TestSuite(IntegerTests.class));
        suite.addTest(new TestSuite(EnumTests.class));
        suite.addTest(new TestSuite(ErrorHandlingTests.class));
        return suite;
    }

}
