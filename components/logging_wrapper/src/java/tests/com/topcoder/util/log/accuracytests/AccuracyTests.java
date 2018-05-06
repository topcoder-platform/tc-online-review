/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.accuracytests.AbstractLogAccuracyTests;
import com.topcoder.util.log.accuracytests.LevelAccuracyTests;
import com.topcoder.util.log.accuracytests.LogExceptionAccuracyTests;
import com.topcoder.util.log.accuracytests.LogManagerAccuracyTests;

import com.topcoder.util.log.accuracytests.BasicLogAccuracyTests;
import com.topcoder.util.log.accuracytests.BasicLogFactoryAccuracyTests;

import com.topcoder.util.log.accuracytests.Jdk14LogAccuracyTests;
import com.topcoder.util.log.accuracytests.Jdk14LogFactoryAccuracyTests;

import com.topcoder.util.log.accuracytests.Log4jLogAccuracyTests;
import com.topcoder.util.log.accuracytests.Log4jLogFactoryAccuracyTests;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all accuracy test cases.
 * </p>
 *
 * @author tianniu
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * <p>
     * The test suite.
     * </p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // package com.topcoder.util.log
        suite.addTestSuite(AbstractLogAccuracyTests.class);
        suite.addTestSuite(LevelAccuracyTests.class);
        suite.addTestSuite(LogExceptionAccuracyTests.class);
        suite.addTestSuite(LogManagerAccuracyTests.class);

        // package com.topcoder.util.log.basic
        suite.addTestSuite(BasicLogAccuracyTests.class);
        suite.addTestSuite(BasicLogFactoryAccuracyTests.class);

        // package com.topcoder.util.log.jdk14Log
        suite.addTestSuite(Jdk14LogAccuracyTests.class);
        suite.addTestSuite(Jdk14LogFactoryAccuracyTests.class);
        
        // package com.topcoder.util.log.log4jLog
        suite.addTestSuite(Log4jLogAccuracyTests.class);
        suite.addTestSuite(Log4jLogFactoryAccuracyTests.class);
 
        return suite;
    }
}