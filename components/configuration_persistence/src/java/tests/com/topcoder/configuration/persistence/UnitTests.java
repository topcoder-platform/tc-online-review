/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class UnitTests extends TestCase {

    /**
     * <p>
     * This test case aggregates all Unit test cases.
     * </p>
     *
     * @return all unit tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(HelperUnitTests.class);
        suite.addTestSuite(ConfigurationParserExceptionUnitTests.class);
        suite.addTestSuite(ConfigurationPersistenceExceptionUnitTests.class);
        suite.addTestSuite(ConfigurationUpdateConflictExceptionUnitTests.class);
        suite.addTestSuite(InvalidConfigurationUpdateExceptionUnitTests.class);
        suite.addTestSuite(NamespaceConflictExceptionUnitTests.class);
        suite.addTestSuite(UnrecognizedFileTypeExceptionUnitTests.class);
        suite.addTestSuite(UnrecognizedNamespaceExceptionUnitTests.class);
        suite.addTestSuite(XMLFilePersistenceUnitTests.class);
        suite.addTestSuite(PropertyFilePersistenceUnitTests.class);
        suite.addTestSuite(ConfigurationFileManagerUnitTests.class);
        suite.addTestSuite(Demo.class);
        return suite;
    }
}
