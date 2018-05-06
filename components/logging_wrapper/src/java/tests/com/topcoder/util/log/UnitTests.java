/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.log.basic.BasicLogFactoryTest;
import com.topcoder.util.log.basic.BasicLogTest;
import com.topcoder.util.log.jdk14.Jdk14LogFactoryTest;
import com.topcoder.util.log.jdk14.Jdk14LogTest;
import com.topcoder.util.log.log4j.Log4jLogFactoryTest;
import com.topcoder.util.log.log4j.Log4jLogTest;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * Return the unit test suite.
     * </p>
     *
     * @return the unit test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(AbstractLogTest.suite());
        suite.addTest(LevelTest.suite());
        suite.addTest(LogExceptionTest.suite());
        suite.addTest(LogManagerTest.suite());

        suite.addTest(BasicLogFactoryTest.suite());
        suite.addTest(BasicLogTest.suite());

        suite.addTest(Jdk14LogFactoryTest.suite());
        suite.addTest(Jdk14LogTest.suite());

        suite.addTest(Log4jLogFactoryTest.suite());
        suite.addTest(Log4jLogTest.suite());

        suite.addTest(DemoTests.suite());
        return suite;
    }
}
