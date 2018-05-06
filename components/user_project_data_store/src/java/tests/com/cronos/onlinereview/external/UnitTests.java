/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import com.cronos.onlinereview.external.impl.BaseDBRetrievalUnitTest;
import com.cronos.onlinereview.external.impl.DBProjectRetrievalUnitTest;
import com.cronos.onlinereview.external.impl.DBUserRetrievalUnitTest;
import com.cronos.onlinereview.external.impl.ExternalObjectImplUnitTest;
import com.cronos.onlinereview.external.impl.ExternalProjectImplUnitTest;
import com.cronos.onlinereview.external.impl.ExternalUserImplUnitTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class UnitTests extends TestCase {

    /**
     * Aggregates all tests.
     *
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // Helper class
        suite.addTestSuite(UserProjectDataStoreHelperUnitTest.class);

        // Exception Class tests.
        suite.addTestSuite(ConfigExceptionUnitTest.class);
        suite.addTestSuite(RetrievalExceptionUnitTest.class);
        suite.addTestSuite(UserProjectDataStoreExceptionUnitTest.class);

        // RatingInfo and RatingType tests.
        suite.addTestSuite(RatingInfoUnitTest.class);
        suite.addTestSuite(RatingTypeUnitTest.class);

        // Impl classes tests
        suite.addTestSuite(ExternalObjectImplUnitTest.class);
        suite.addTestSuite(ExternalProjectImplUnitTest.class);
        suite.addTestSuite(ExternalUserImplUnitTest.class);

        // Retrieval classes tests
        suite.addTestSuite(BaseDBRetrievalUnitTest.class);
        suite.addTestSuite(DBProjectRetrievalUnitTest.class);
        suite.addTestSuite(DBUserRetrievalUnitTest.class);

        // Demo
        suite.addTestSuite(Demo.class);

        return suite;
    }
}
