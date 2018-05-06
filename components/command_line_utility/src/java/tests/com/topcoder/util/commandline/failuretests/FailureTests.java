package com.topcoder.util.commandline.failuretests;

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
        suite.addTest(IntegerValidatorTestCase.suite());
        suite.addTest(EnumValidatorTestCase.suite());
        suite.addTest(SwitchTestCase.suite());
        suite.addTest(CommandLineUtilityTestCase.suite());
        return suite;
    }

}
