package com.topcoder.util.commandline.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all stress test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(CommandLineUtilityTest.suite());
        return suite;
    }

}
