/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests;

import com.cronos.onlinereview.external.failuretests.impl.BaseDBRetrievalFailureTest;
import com.cronos.onlinereview.external.failuretests.impl.DBProjectRetrievalFailureTest;
import com.cronos.onlinereview.external.failuretests.impl.DBUserRetrievalFailureTest;
import com.cronos.onlinereview.external.failuretests.impl.ExternalObjectImplFailureTest;
import com.cronos.onlinereview.external.failuretests.impl.ExternalProjectImplFailureTest;
import com.cronos.onlinereview.external.failuretests.impl.ExternalUserImplFailureTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class FailureTests extends TestCase {

    /**
     * Return the test suite of failure tests.
     *
     * @return the test suite of failure tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // Package "com.cronos.onlinereview.external.failuretests".
        suite.addTestSuite(RatingInfoFailureTest.class);
        suite.addTestSuite(RatingTypeFailureTest.class);

        // Package "com.cronos.onlinereview.external.failuretests.impl".
        suite.addTestSuite(BaseDBRetrievalFailureTest.class);
        suite.addTestSuite(DBProjectRetrievalFailureTest.class);
        suite.addTestSuite(DBUserRetrievalFailureTest.class);
        suite.addTestSuite(ExternalObjectImplFailureTest.class);
        suite.addTestSuite(ExternalProjectImplFailureTest.class);
        suite.addTestSuite(ExternalUserImplFailureTest.class);

        return suite;
    }

}
