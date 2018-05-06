/*
 * Copyright (C) 2007, 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class UnitTests extends TestCase {
    /**
     * Contain all unit test.
     *
     * @return all unit test
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(TestField.class);
        suite.addTestSuite(TestLoop.class);
        suite.addTestSuite(TestNodeList.class);
        suite.addTestSuite(TestTemplateFields.class);
        suite.addTestSuite(TestXmlTemplateData.class);
        suite.addTestSuite(TestXsltTemplate.class);

        suite.addTestSuite(Demo.class);
        suite.addTest(ConditionTests.suite());
        suite.addTest(NodeListUtilityTests.suite());

        // added in 3.0 version.
        suite.addTest(TestDocumentGeneratorCommand.suite());
        suite.addTest(TestDocumentGeneratorFactory.suite());
        suite.addTestSuite(TestDocumentGeneratorConfigurationException.class);

        // modified in 3.0 version.
        suite.addTestSuite(TestDocumentGenerator.class);
        suite.addTestSuite(TestTemplateSourceException.class);
        suite.addTestSuite(TestTemplateFormatException.class);
        suite.addTestSuite(TestTemplateDataFormatException.class);
        suite.addTestSuite(TestFileTemplateSource.class);
        return suite;
    }
}
