/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.memoryusage.analyzers.IBM14AnalyzerTest;
import com.topcoder.util.memoryusage.analyzers.IBM15AnalyzerTest;
import com.topcoder.util.memoryusage.analyzers.Sun12AnalyzerTest;
import com.topcoder.util.memoryusage.analyzers.Sun13AnalyzerTest;
import com.topcoder.util.memoryusage.analyzers.Sun14AnalyzerTest;
import com.topcoder.util.memoryusage.analyzers.Sun15AnalyzerTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TexWiller
 * @version 1.0
 */
public class UnitTests extends TestCase {
    /**
     * Returns the Suite of unit tests.
     *
     * @return suite of unit tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(new TestSuite(DemoTest.class));

        suite.addTest(new TestSuite(BaseAnalyzerTest.class));
        suite.addTest(new TestSuite(ConfigurationExceptionTest.class));
        suite.addTest(new TestSuite(JVMNotSupportedExceptionTest.class));
        suite.addTest(new TestSuite(MemoryUsageDetailTest.class));
        suite.addTest(new TestSuite(MemoryUsageExceptionTest.class));
        suite.addTest(new TestSuite(MemoryUsageHelperTest.class));
        suite.addTest(new TestSuite(MemoryUsageResultTest.class));
        suite.addTest(new TestSuite(MemoryUsageTest.class));

        suite.addTest(new TestSuite(IBM14AnalyzerTest.class));
        suite.addTest(new TestSuite(IBM15AnalyzerTest.class));
        suite.addTest(new TestSuite(Sun12AnalyzerTest.class));
        suite.addTest(new TestSuite(Sun13AnalyzerTest.class));
        suite.addTest(new TestSuite(Sun14AnalyzerTest.class));
        suite.addTest(new TestSuite(Sun15AnalyzerTest.class));

        return suite;
    }
}

