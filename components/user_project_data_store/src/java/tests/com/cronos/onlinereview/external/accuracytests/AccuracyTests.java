/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.cronos.onlinereview.external.accuracytests.impl.ExternalProjectImplAccuracyTest;
import com.cronos.onlinereview.external.accuracytests.impl.ExternalUserImplAccuracyTest;
import com.cronos.onlinereview.external.impl.DBProjectRetrievalAccuracyTest;
import com.cronos.onlinereview.external.impl.DBUserRetrievalAccuracyTest;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author restarter
 * @version 1.1
 */
public class AccuracyTests extends TestCase {
    /**
     * <p>
     * aggregates all Accuracy test cases
     * </p>
     *
     * @return the accuracy test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(RatingInfoAccuracyTest.class);
        suite.addTestSuite(RatingTypeAccuracyTest.class);
        suite.addTestSuite(DBProjectRetrievalAccuracyTest.class);
        suite.addTestSuite(ExternalProjectImplAccuracyTest.class);
        suite.addTestSuite(ExternalUserImplAccuracyTest.class);
        suite.addTestSuite(DBUserRetrievalAccuracyTest.class);

        return suite;
    }
}
