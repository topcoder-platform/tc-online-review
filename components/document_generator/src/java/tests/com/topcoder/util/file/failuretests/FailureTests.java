/*
 * Copyright (C) 2007 - 2010 TopCoder Inc., All Rights Reserved.
 */
 package com.topcoder.util.file.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(DocumentGeneratorTest.suite());
        suite.addTest(DocumentGeneratorFactoryTest.suite());
        suite.addTest(TemplateFieldsTest.suite());
        suite.addTest(NodeListTest.suite());
        suite.addTest(LoopTest.suite());
        suite.addTest(FieldTest.suite());
        suite.addTest(FileTemplateSourceTest.suite());
        suite.addTest(XsltTemplateTest.suite());
        suite.addTest(XmlTemplateDataTest.suite());

        suite.addTestSuite(TestConditionFailure.class);
        suite.addTestSuite(TestNodeListUtilityFailure.class);
        suite.addTestSuite(FileTemplateSourceFailureTest.class);

        return suite;
    }

}
