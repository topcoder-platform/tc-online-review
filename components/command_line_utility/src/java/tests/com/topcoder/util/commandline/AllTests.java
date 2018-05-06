package com.topcoder.util.commandline;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.commandline.functionaltests.FunctionalTests;
import com.topcoder.util.commandline.accuracytests.AccuracyTests;
import com.topcoder.util.commandline.failuretests.FailureTests;
import com.topcoder.util.commandline.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases for this package.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        
        //unit tests
        suite.addTest(UnitTests.suite());
        
        //functional tests
        suite.addTest(FunctionalTests.suite());
        
        //accuracy tests
        suite.addTest(AccuracyTests.suite());
        
        //failure tests
        suite.addTest(FailureTests.suite());
        
        //stress tests
        suite.addTest(StressTests.suite());
        
        return suite;
    }

}
