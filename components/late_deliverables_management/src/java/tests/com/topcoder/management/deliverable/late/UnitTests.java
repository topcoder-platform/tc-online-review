/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImplUnitTests;
import com.topcoder.management.deliverable.late.impl.LateDeliverableNotFoundExceptionUnitTests;
import com.topcoder.management.deliverable.late.impl.LateDeliverablePersistenceExceptionUnitTests;
import com.topcoder.management.deliverable.late.impl.persistence.DatabaseLateDeliverablePersistenceUnitTests;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilderUnitTests;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author sparemax
 * @version 1.0.6
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * All unit test cases.
     * </p>
     *
     * @return The test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(Demo.suite());
        suite.addTest(HelperUnitTests.suite());
        suite.addTest(LateDeliverableUnitTests.suite());
        suite.addTest(LateDeliverableTypeUnitTests.suite());

        suite.addTest(LateDeliverableManagerImplUnitTests.suite());

        suite.addTest(DatabaseLateDeliverablePersistenceUnitTests.suite());

        suite.addTest(LateDeliverableFilterBuilderUnitTests.suite());

        // Exceptions
        suite.addTest(LateDeliverableManagementConfigurationExceptionUnitTests.suite());
        suite.addTest(LateDeliverableManagementExceptionUnitTests.suite());
        suite.addTest(LateDeliverableNotFoundExceptionUnitTests.suite());
        suite.addTest(LateDeliverablePersistenceExceptionUnitTests.suite());

        return suite;
    }

}
