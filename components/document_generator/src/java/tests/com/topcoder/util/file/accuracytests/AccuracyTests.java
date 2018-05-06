/**
 * Copyright ?2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.file.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(FieldAccuracyTests.class);
        suite.addTestSuite(NodeListAccuracyTests.class);
        suite.addTestSuite(LoopAccuracyTests.class);
        suite.addTestSuite(TemplateFieldsAccuracyTests.class);
        suite.addTestSuite(FileTemplateSourceAccuracyTests.class);
        suite.addTestSuite(XsltTemplateAccuracyTests.class);
        suite.addTestSuite(XmlTemplateDataAccuracyTests.class);
        suite.addTestSuite(ConditionAccuracyTests.class);

        suite.addTestSuite(DocumentGeneratorAccuracyTests.class);
        suite.addTestSuite(DocumentGeneratorCommandAccuracyTest.class);
        suite.addTestSuite(DocumentGeneratorFactoryAccuracyTest.class);
        return suite;
    }

}
