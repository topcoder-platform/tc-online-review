/**
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.security.authenticationfactory.accuracytests.AccuracyTests;
import com.topcoder.security.authenticationfactory.failuretests.FailureTests;
import com.topcoder.security.authenticationfactory.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        
        //unit tests
        suite.addTest(UnitTests.suite());
        
        //accuracy tests
        suite.addTest(AccuracyTests.suite());
        
        //failure tests
        suite.addTest(FailureTests.suite());
        
        //stress tests
        suite.addTest(StressTests.suite());
        
        return suite;
    }

}
