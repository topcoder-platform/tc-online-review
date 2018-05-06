/*
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.scheduler.processor.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author bbskill
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    /**
     * <p>
     * Returns all Accuracy test cases
     * </p>
     *
     * @return all Accuracy test cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        //suite.addTest(XXX.suite());
        suite.addTest(JobProcessorExceptionTest.suite());
        suite.addTest(JobProcessorTest.suite());
        return suite;
    }

}
