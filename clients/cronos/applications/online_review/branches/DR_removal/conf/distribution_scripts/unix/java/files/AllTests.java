/**
 *
 * Copyright (c) %{current_year}, TopCoder, Inc. All rights reserved
 */
package %{package.name};

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import %{package.name}.accuracytests.AccuracyTests;
import %{package.name}.failuretests.FailureTests;
import %{package.name}.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version %{version.major}.%{version.minor}
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
