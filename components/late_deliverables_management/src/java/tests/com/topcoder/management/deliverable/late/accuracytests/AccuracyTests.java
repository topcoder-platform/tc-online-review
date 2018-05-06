/**
 *
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */

package com.topcoder.management.deliverable.late.accuracytests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({DatabaseLateDeliverablePersistenceTest.class,
                     LateDeliverableFilterBuilderTest.class,
                     LateDeliverableManagementConfigurationExceptionTest.class,
                     LateDeliverableManagementExceptionTest.class,
                     LateDeliverableManagerImplTest.class,
                     LateDeliverableTest.class,
                     LateDeliverableNotFoundExceptionTest.class,
                     LateDeliverablePersistenceExceptionTest.class})
public class AccuracyTests extends TestCase {

    /**
     * <p> Gets test suite. </p>
     *
     * @return test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new JUnit4TestAdapter(AccuracyTests.class));
        return suite;
    }

}
