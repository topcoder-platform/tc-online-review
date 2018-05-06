/**
 *
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.search.builder.accuracytests.filter.AbstractAssociativeFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.AbstractSimpleFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.AndFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.BetweenFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.EqualToFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.GreaterThanFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.GreaterThanorEqualToFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.InFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.LessThanFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.LessThanOrEqualToFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.LikeFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.NotFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.OrFilterAccuracyTests;
import com.topcoder.search.builder.accuracytests.filter.NullFilterAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.LDAPConnectionInformationAccuracyTests;
import com.topcoder.search.builder.accuracytests.ldap.AndFragmentBuilderAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.EqualsFragmentBuilderAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.InFragmentBuilderAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.LikeFragmentBuilderAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.NotFragmentBuilderAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.NullFragmentBuilderAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.OrFragmentBuilderAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.RangeFragmentBuilderAccuracyTest;
import com.topcoder.search.builder.accuracytests.ldap.LDAPSearchStrategyAccuracyTest;
import com.topcoder.search.builder.accuracytests.database.DatabaseSearchStrategyAccuracyTest;


/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        //filters
        suite.addTestSuite(AndFilterAccuracyTests.class);
        suite.addTestSuite(BetweenFilterAccuracyTests.class);
        suite.addTestSuite(EqualToFilterAccuracyTests.class);
        suite.addTestSuite(GreaterThanFilterAccuracyTests.class);
        suite.addTestSuite(GreaterThanorEqualToFilterAccuracyTests.class);

        suite.addTestSuite(InFilterAccuracyTests.class);
        suite.addTestSuite(LessThanFilterAccuracyTests.class);
        suite.addTestSuite(LessThanOrEqualToFilterAccuracyTests.class);
        suite.addTestSuite(NotFilterAccuracyTests.class);
        suite.addTestSuite(OrFilterAccuracyTests.class);

        suite.addTestSuite(AbstractAssociativeFilterAccuracyTests.class);
        suite.addTestSuite(AbstractSimpleFilterAccuracyTests.class);

        //ldap
        suite.addTestSuite(LDAPConnectionInformationAccuracyTests.class);

        //builder
        suite.addTestSuite(SearchBundleAccuracyTests.class);
        suite.addTestSuite(SearchBundleManagerAccuracyTests.class);
        suite.addTestSuite(ValidationResultAccuracyTests.class);

        // added from version 1.2
        suite.addTestSuite(LikeFilterAccuracyTests.class);

        // added since version 1.3
        suite.addTest(SearchContextAccuracyTest.suite());
        suite.addTest(UnrecognizedFilterExceptionAccuracyTest.suite());

        suite.addTest(NullFilterAccuracyTest.suite());

        suite.addTest(AndFragmentBuilderAccuracyTest.suite());
        suite.addTest(EqualsFragmentBuilderAccuracyTest.suite());
        suite.addTest(InFragmentBuilderAccuracyTest.suite());
        suite.addTest(LikeFragmentBuilderAccuracyTest.suite());
        suite.addTest(NotFragmentBuilderAccuracyTest.suite());
        suite.addTest(NullFragmentBuilderAccuracyTest.suite());
        suite.addTest(OrFragmentBuilderAccuracyTest.suite());
        suite.addTest(RangeFragmentBuilderAccuracyTest.suite());
        suite.addTest(LDAPSearchStrategyAccuracyTest.suite());

        suite.addTest(com.topcoder.search.builder.accuracytests.database.AndFragmentBuilderAccuracyTest.suite());
        suite.addTest(com.topcoder.search.builder.accuracytests.database.EqualsFragmentBuilderAccuracyTest.suite());
        suite.addTest(com.topcoder.search.builder.accuracytests.database.InFragmentBuilderAccuracyTest.suite());
        suite.addTest(com.topcoder.search.builder.accuracytests.database.LikeFragmentBuilderAccuracyTest.suite());
        suite.addTest(com.topcoder.search.builder.accuracytests.database.NotFragmentBuilderAccuracyTest.suite());
        suite.addTest(com.topcoder.search.builder.accuracytests.database.NullFragmentBuilderAccuracyTest.suite());
        suite.addTest(com.topcoder.search.builder.accuracytests.database.OrFragmentBuilderAccuracyTest.suite());
        suite.addTest(com.topcoder.search.builder.accuracytests.database.RangeFragmentBuilderAccuracyTest.suite());
        suite.addTest(DatabaseSearchStrategyAccuracyTest.suite());

        return suite;
    }
}
