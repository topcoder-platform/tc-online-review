/*
 * Copyright (c) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all stress test cases.
 * </p>
 *
 * @author Wendell
 * @version 2.0
 */
public class StressTests extends TestCase {
    /**
     * Returns the test suite of all stress test cases.
     *
     * @return the test suite of all stress test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(ObjectFactoryStressTest.suite());

        return suite;
    }
}
