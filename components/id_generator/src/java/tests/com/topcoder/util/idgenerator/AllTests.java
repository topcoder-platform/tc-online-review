package com.topcoder.util.idgenerator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.idgenerator.accuracytests.AccuracyTests;
import com.topcoder.util.idgenerator.failuretests.FailureTests;
import com.topcoder.util.idgenerator.stresstests.StressTests;

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
        //OK
        suite.addTest(UnitTests.suite());

        //accuracy tests
        suite.addTest(AccuracyTests.suite());

        //failure tests
        suite.addTest(FailureTests.suite());
        
        //stress tests 
        //OK
        suite.addTest(StressTests.suite());

        return suite;
    }

}
