/*
 * Copyright (C) 2003, 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.naming.jndiutility.accuracytests.AccuracyTests;
import com.topcoder.naming.jndiutility.failuretests.FailureTests;
import com.topcoder.naming.jndiutility.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 2.0
 *
 * @since 1.0
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
