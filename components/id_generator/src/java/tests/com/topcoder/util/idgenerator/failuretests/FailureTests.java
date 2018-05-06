/*
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.failuretests;

import com.topcoder.util.idgenerator.failuretests.ejb.IDGeneratorBeanFailureTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(IDGeneratorFactoryFailureTest.class);
        suite.addTestSuite(IDGeneratorImplFailureTest.class);
        suite.addTestSuite(OracleSequenceGeneratorFailureTest.class);
        suite.addTestSuite(IDGeneratorBeanFailureTest.class);

        return suite;
    }
}
