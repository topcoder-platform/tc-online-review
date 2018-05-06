/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;


import com.topcoder.search.builder.failuretests.ldap.LDAPConnectionInformationTest13;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * TCSDEVELOPER
 * @version 1.3
 */
public class FailureTests extends TestCase {
    /**
     * The unit test.
     *
     * @return a test suite!
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // filter
        suite.addTestSuite(NotFilterFailureTests.class);
        suite.addTestSuite(AndFilterFailureTests.class);
        suite.addTestSuite(OrFilterFailureTests.class);
        suite.addTestSuite(InFilterFailureTests.class);
        suite.addTestSuite(LessThanFilterFailureTests.class);
        suite.addTestSuite(LessThanOrEqualToFilterFailureTests.class);
        suite.addTestSuite(GreaterThanFilterFailureTests.class);
        suite.addTestSuite(GreaterThanOrEqualToFilterFailureTests.class);
        suite.addTestSuite(EqualToFilterFailureTests.class);
        suite.addTestSuite(BetweenFilterFailureTests.class);

        suite.addTestSuite(LDAPConnectionInformationFailureTests.class);

        // main
        suite.addTestSuite(ValidationResultFailureTests.class);
        suite.addTestSuite(SearchBundleFailureTests.class);
        suite.addTestSuite(SearchBundleManagerFailureTests.class);

        // Add in version 1.2
        suite.addTestSuite(LikeFilterTest12.class);
        suite.addTestSuite(SearchBundleTest12.class);

        // Add in version 1.3
        suite.addTestSuite(LDAPConnectionInformationTest13.class);
        suite.addTestSuite(AndFragmentBuilderTest13.class);
        suite.addTestSuite(EqualsFragmentBuilderTest13.class);
        suite.addTestSuite(InFragmentBuilderTest13.class);
        suite.addTestSuite(LikeFragmentBuilderTest13.class);
        suite.addTestSuite(NotFragmentBuilderTest13.class);
        suite.addTestSuite(NullFragmentBuilderTest13.class);
        suite.addTestSuite(OrFragmentBuilderTest13.class);
        suite.addTestSuite(RangeFragmentBuilderTest13.class);

        suite.addTestSuite(SearchBundleTest13.class);
        suite.addTestSuite(SearchBundleManagerTest13.class);

        suite.addTestSuite(AndFragmentBuilderTest13.class);
        suite.addTestSuite(DatabaseSearchStrategyTest13.class);
        suite.addTestSuite(EqualsFragmentBuilderTest13.class);
        suite.addTestSuite(InFragmentBuilderTest13.class);
        suite.addTestSuite(LikeFragmentBuilderTest13.class);
        suite.addTestSuite(NotFragmentBuilderTest13.class);
        suite.addTestSuite(NullFragmentBuilderTest13.class);
        suite.addTestSuite(OrFragmentBuilderTest13.class);
        suite.addTestSuite(RangeFragmentBuilderTest13.class);

        return suite;
    }
}
