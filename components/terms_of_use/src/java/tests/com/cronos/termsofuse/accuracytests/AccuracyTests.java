/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author sokol
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    /**
     * <p>
     * Creates TestSuite that aggregates all Accuracy tests.
     * </p>
     *
     * @return TestSuite that aggregates all Accuracy tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(TermsOfUseDaoImplAccuracyTest.class);
        suite.addTestSuite(UserTermsOfUseDaoImplAccuracyTest.class);
        suite.addTestSuite(ProjectTermsOfUseDaoImplAccuracyTest.class);
        return suite;
    }
}
