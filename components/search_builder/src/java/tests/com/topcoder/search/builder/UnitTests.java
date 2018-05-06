/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;


import com.topcoder.search.builder.database.AndFragmentBuilderDBTests;
import com.topcoder.search.builder.database.DatabaseSearchStrategyTests;
import com.topcoder.search.builder.database.EqualsFragmentBuildeDBrTests;
import com.topcoder.search.builder.database.InFragmentBuilderDBTests;
import com.topcoder.search.builder.database.LikeFragmentBuilderDBTests;
import com.topcoder.search.builder.database.NotFragmentBuilderDBTests;
import com.topcoder.search.builder.database.NullFragmentBuilderDBTests;
import com.topcoder.search.builder.database.OrFragmentBuilderDBTests;
import com.topcoder.search.builder.database.RangeFragmentBuilderDBTests;
import com.topcoder.search.builder.filter.AndFilterTests;
import com.topcoder.search.builder.filter.BetweenFilterTests;
import com.topcoder.search.builder.filter.EqualToFilterTests;
import com.topcoder.search.builder.filter.GreaterThanFilterTests;
import com.topcoder.search.builder.filter.GreaterThanorEqualToFilterTests;
import com.topcoder.search.builder.filter.InFilterTests;
import com.topcoder.search.builder.filter.LessThanFilterTests;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilterTests;
import com.topcoder.search.builder.filter.LikeFilterTests;
import com.topcoder.search.builder.filter.NotFilterTests;
import com.topcoder.search.builder.filter.NullFilterTests;
import com.topcoder.search.builder.filter.OrFilterTests;
import com.topcoder.search.builder.ldap.AndFragmentBuilderTests;
import com.topcoder.search.builder.ldap.EqualsFragmentBuilderTests;
import com.topcoder.search.builder.ldap.InFragmentBuilderTests;
import com.topcoder.search.builder.ldap.LDAPConnectionInformationTests;
import com.topcoder.search.builder.ldap.LDAPSearchStrategyTests;
import com.topcoder.search.builder.ldap.LikeFragmentBuilderTests;
import com.topcoder.search.builder.ldap.NotFragmentBuilderTests;
import com.topcoder.search.builder.ldap.NullFragmentBuilderTests;
import com.topcoder.search.builder.ldap.OrFragmentBuilderTests;
import com.topcoder.search.builder.ldap.RangeFragmentBuilderTests;

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
public class UnitTests extends TestCase {
    /**
     * The unit test.
     *
     * @return a test suite!
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(DuplicatedElementsExceptionTests.class);
        suite.addTestSuite(OperationNotSupportedExceptionTests.class);
        suite.addTestSuite(PersistenceOperationExceptionTests.class);
        suite.addTestSuite(SearchBuilderConfigurationExceptionTests.class);
        suite.addTestSuite(SearchBuilderExceptionTests.class);
        suite.addTestSuite(ValidationResultTests.class);
        suite.addTestSuite(LDAPConnectionInformationTests.class);
        suite.addTestSuite(AndFilterTests.class);
        suite.addTestSuite(OrFilterTests.class);
        suite.addTestSuite(NotFilterTests.class);
        suite.addTestSuite(InFilterTests.class);
        suite.addTestSuite(BetweenFilterTests.class);
        suite.addTestSuite(EqualToFilterTests.class);
        suite.addTestSuite(GreaterThanFilterTests.class);
        suite.addTestSuite(GreaterThanorEqualToFilterTests.class);
        suite.addTestSuite(LessThanFilterTests.class);
        suite.addTestSuite(LessThanOrEqualToFilterTests.class);
        suite.addTestSuite(LikeFilterTests.class);
        suite.addTestSuite(AlwaysTrueValidatorTests.class);
        suite.addTestSuite(SearchContextTests.class);
        suite.addTestSuite(AndFragmentBuilderTests.class);
        suite.addTestSuite(EqualsFragmentBuilderTests.class);
        suite.addTestSuite(InFragmentBuilderTests.class);
        suite.addTestSuite(LDAPSearchStrategyTests.class);
        suite.addTestSuite(LikeFragmentBuilderTests.class);
        suite.addTestSuite(NotFragmentBuilderTests.class);
        suite.addTestSuite(NullFragmentBuilderTests.class);
        suite.addTestSuite(OrFragmentBuilderTests.class);
        suite.addTestSuite(RangeFragmentBuilderTests.class);
        suite.addTestSuite(AndFragmentBuilderDBTests.class);
        suite.addTestSuite(DatabaseSearchStrategyTests.class);
        suite.addTestSuite(EqualsFragmentBuildeDBrTests.class);
        suite.addTestSuite(InFragmentBuilderDBTests.class);
        suite.addTestSuite(LikeFragmentBuilderDBTests.class);
        suite.addTestSuite(NotFragmentBuilderDBTests.class);
        suite.addTestSuite(NullFragmentBuilderDBTests.class);
        suite.addTestSuite(OrFragmentBuilderDBTests.class);
        suite.addTestSuite(RangeFragmentBuilderDBTests.class);
        suite.addTestSuite(SearchBundleManagerTests.class);
        suite.addTestSuite(SearchBundleTests.class);
        suite.addTestSuite(UnrecognizedFilterExceptionTests.class);
        suite.addTestSuite(Demo.class);
        suite.addTestSuite(NullFilterTests.class);

        return suite;
    }
}
