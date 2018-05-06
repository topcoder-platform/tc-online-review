/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.objectfactory.impl.IllegalReferenceExceptionUnitTest;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationExceptionUnitTest;

/**
 * <p>
 * This test case aggregates all unit test cases.
 * </p>
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class UnitTests extends TestCase {
    /**
     * Aggregate all unit tests.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // object factory
        suite.addTestSuite(ObjectFactoryUnitTest.class);
        suite.addTestSuite(ObjectSpecificationUnitTest.class);

        // exceptions
        suite.addTestSuite(InvalidClassSpecificationExceptionUnitTest.class);
        suite.addTestSuite(ObjectCreationExceptionUnitTest.class);
        suite.addTestSuite(ObjectFactoryExceptionUnitTest.class);
        suite.addTestSuite(SpecificationFactoryExceptionUnitTest.class);
        suite.addTestSuite(UnknownReferenceExceptionUnitTest.class);

        suite.addTestSuite(IllegalReferenceExceptionUnitTest.class);
        suite.addTestSuite(SpecificationConfigurationExceptionUnitTest.class);

        // helper
        suite.addTestSuite(ObjectFactoryHelperUnitTest.class);

        // demo
        suite.addTestSuite(Demo.class);

        return suite;
    }
}
